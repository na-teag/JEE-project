package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.*;

public class CourseManager {
	private static CourseManager instance;

	private CourseManager() {}

	public static synchronized CourseManager getInstance() {
		if (instance == null) {
			instance = new CourseManager();
		}
		return instance;
	}

	public List<Course> getListOfCourses() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Course";
			Query<Course> query = session.createQuery(request, Course.class);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public Course getCourseById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Course WHERE id = :id";
			Query<Course> query = session.createQuery(request, Course.class);
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public String createCourse(String professorId, String subjectId, String classroom, List<String> studentGroupsId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		PersonManager personManager = PersonManager.getInstance();
		Professor professor = personManager.getProfessorById(professorId);
		StudentGroupManager studentGroupManager = StudentGroupManager.getInstance();
		SubjectManager subjectManager = SubjectManager.getInstance();
		Subject subject = subjectManager.getSubjectById(subjectId);

		// check that the StudentGroups exists
		List<StudentGroup> studentGroups = new ArrayList<>();
		StudentGroup studentGroup;
		for (String studentGroupId : studentGroupsId) {
			if (studentGroupId == null || studentGroupId.isEmpty()) {
				return "student group does not exist";
			}
			studentGroup = studentGroupManager.getStudentGroupFromId(studentGroupId);
			if (studentGroup == null) {
				return "student group does not exist";
			}
			studentGroups.add(studentGroup);
		}

		try{
			Transaction transaction = session.beginTransaction();

			Course course = new Course();
			course.setSubject(subject);
			course.setClassroom(classroom);
			course.setProfessor(professor);
			course.setStudentGroups(studentGroups);
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Course>> errors = validator.validate(course);
			if (errors.isEmpty()) {
				session.save(course);
				transaction.commit();
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Course> error : errors) {
				errorString += error.getMessage() + "\n";
			}
			return errorString;
		} catch (Exception e){
			e.printStackTrace();
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			return e.getMessage();
		} finally {
			session.close();
		}
	}

	public String updateCourseById(String id, String professorId, String subjectId, String classroom, List<String> studentGroupsId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		PersonManager personManager = PersonManager.getInstance();
		Professor professor = personManager.getProfessorById(professorId);
		StudentGroupManager studentGroupManager = StudentGroupManager.getInstance();
		SubjectManager subjectManager = SubjectManager.getInstance();
		Subject subject = subjectManager.getSubjectById(subjectId);

		// check that the StudentGroups exists
		List<StudentGroup> studentGroups = new ArrayList<>();
		StudentGroup studentGroup;
		for (String studentGroupId : studentGroupsId) {
			studentGroup = studentGroupManager.getStudentGroupFromId(studentGroupId);
			if (studentGroup == null) {
				return "student group does not exist";
			}
			studentGroups.add(studentGroup);
		}

		try {
			Transaction transaction = session.beginTransaction();
			Course course = getCourseById(id);
			course.setProfessor(professor);
			course.setClassroom(classroom);
			course.setSubject(subject);
			course.setStudentGroups(studentGroups);
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Course>> errors = validator.validate(course);
			if (errors.isEmpty()) {
				session.update(course);
				transaction.commit();
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Course> error : errors) {
				errorString += error.getMessage() + "\n";
			}
			return errorString;
		} catch (Exception e) {
			e.printStackTrace();
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			return e.getMessage();
		} finally {
			session.close();
		}
	}

	public String deleteCourseById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Course course = getCourseById(id);
			if (course != null) {
				String hql = "DELETE FROM Grade g WHERE course = :course";
				Query<?> query = session.createQuery(hql);
				query.setParameter("course", course);
				query.executeUpdate();
			}

			String hql = "DELETE FROM Course g WHERE id = :id";
			Query<?> query = session.createQuery(hql);
			query.setParameter("id", id);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e){
			e.printStackTrace();
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			return e.getMessage();
		} finally {
			session.close();
		}
		return null;
	}

	public List<Course> getCoursesOfStudentGroup(StudentGroup studentGroup) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "SELECT c FROM Course c JOIN c.studentGroups sg WHERE sg = :studentGroup";
			Query<Course> query = session.createQuery(hql, Course.class);
			query.setParameter("studentGroup", studentGroup);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		} finally {
			session.close();
		}
	}

	public List<Course> getCourseOfStudent(Student student) {
		Classe studentClasse = student.getClasse();
		Promo studentPromo = studentClasse.getPromo();
		Pathway studentPathway = studentClasse.getPathway();
		List<Course> courses = new ArrayList<>();
		List<Course> tmp = getCoursesOfStudentGroup(studentClasse);
		if (tmp != null) { courses.addAll(tmp); }
		tmp = getCoursesOfStudentGroup(studentPromo);
		if (tmp != null) { courses.addAll(tmp); }
		tmp = getCoursesOfStudentGroup(studentPathway);
		if (tmp != null) { courses.addAll(tmp); }

		Set<Long> seenIds = new HashSet<>();
		List<Course> uniqueCourses = new ArrayList<>();

		// delete the potential duplicates
		for (Course course : courses) {
			if (seenIds.add(course.getId())) {
				uniqueCourses.add(course);
			}
		}
		return uniqueCourses;
	}

	private List<CourseOccurence> getCourseOccurencesOfCourseForDate(List<Course> courses, LocalDate date) {
		List<CourseOccurence> occurrences = new ArrayList<>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			for (Course course : courses) {
				String hql = "FROM CourseOccurence WHERE course = :course AND day = :date";
				Query<CourseOccurence> query = session.createQuery(hql, CourseOccurence.class);
				query.setParameter("course", course);
				query.setParameter("date", date);
				occurrences.addAll(query.getResultList());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		} finally {
			session.close();
		}
		return occurrences;
	}

	private static Map<String, String> getCourseDetails(CourseOccurence courseOccurence) {
		Map<String, String> courseDetails = new HashMap<>();
		courseDetails.put("title", courseOccurence.getCourse().getSubject().getName());
		courseDetails.put("startTime", String.format("%02d", courseOccurence.getBeginning().getHour()) + "h" + String.format("%02d", courseOccurence.getBeginning().getMinute()));
		courseDetails.put("endTime", String.format("%02d", courseOccurence.getEnd().getHour()) + "h" + String.format("%02d", courseOccurence.getEnd().getMinute()));
		courseDetails.put("room", courseOccurence.getClassroom());
		courseDetails.put("professor", courseOccurence.getProfessor().getFirstName() + " " + courseOccurence.getProfessor().getLastName());
		courseDetails.put("type", courseOccurence.getCategory().getName());
		courseDetails.put("color", courseOccurence.getCategory().getColor());

		String studentGroupsNames = "";
		for (StudentGroup studentGroup : courseOccurence.getCourse().getStudentGroups()) {
			if (!studentGroupsNames.isEmpty()){
				studentGroupsNames += ", ";
			}
			studentGroupsNames += studentGroup.getName();
		}
		courseDetails.put("classes", studentGroupsNames);
		return courseDetails;
	}

	public List<Course> getCoursesOfProfessor(Professor professor) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "FROM Course WHERE professor = :professor";
			Query<Course> query = session.createQuery(hql, Course.class);
			query.setParameter("professor", professor);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		} finally {
			session.close();
		}
	}

	private Map<String, List<Map<String, String>>> getScheduleForCoursesAndDays(List<LocalDate> days, Map<String, List<Map<String, String>>> schedule, List<Course> courses) {
		for (LocalDate day: days) {
			List<CourseOccurence> coursesOfDayInput = getCourseOccurencesOfCourseForDate(courses, day);
			List<Map<String, String>> coursesOfDayOutput = new ArrayList<>();
			for (CourseOccurence courseOccurence : coursesOfDayInput) {
				Map<String, String> courseDetails = getCourseDetails(courseOccurence);
				coursesOfDayOutput.add(courseDetails);
			}
			schedule.put(day.getDayOfWeek().toString(), coursesOfDayOutput);
		}
		return schedule;
	}

	public Map<String, List<Map<String, String>>> getCoursesByPersonNumberAndDays(String personNumber, List<LocalDate> days) {
		PersonManager personManager = PersonManager.getInstance();
		Person user = personManager.getUserByPersonNumber(personNumber);
		Map<String, List<Map<String, String>>> schedule = new HashMap<>();
		if (Admin.class.getName().equals(user.getClass().getName())) {
			for (LocalDate day : days) {
				schedule.put(day.getDayOfWeek().toString(), new ArrayList<>());
			}
		} else if (Professor.class.getName().equals(user.getClass().getName())) {
			Professor professor = (Professor) user;
			List<Course> courses = getCoursesOfProfessor(professor);
			schedule = getScheduleForCoursesAndDays(days, schedule, courses);
		} else {
			// student case
			Student student = (Student) user;
			List<Course> courses = getCourseOfStudent(student);
			schedule = getScheduleForCoursesAndDays(days, schedule, courses);
		}
		return schedule;
	}


	public static Course getSelectedCourse(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		String hql = "FROM Course c WHERE c.id = :id";
		Query<Course> query = session.createQuery(hql, Course.class);
		query.setParameter("id", id);

		// Exécution de la requête et récupération des résultats
		Course course = query.getSingleResult();

		// Commit de la transaction et fermeture de la session
		session.getTransaction().commit();
		return course;
	}
}