package cyu.schoolmanager;

import org.hibernate.Session;
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

	private List<Course> getCoursesOfStudentGroup(StudentGroup studentGroup) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			// Requête HQL pour récupérer les cours liés à un groupe d'étudiants
			String hql = "SELECT c FROM Course c JOIN c.studentGroups sg WHERE sg = :studentGroup";
			Query<Course> query = session.createQuery(hql, Course.class);
			query.setParameter("studentGroup", studentGroup);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	private List<Course> getCourseOfStudent(Student student) {
		Classe studentClasse = student.getClasse();
		Promo studentPromo = studentClasse.getPromo();
		Pathway studentPathway = studentClasse.getPathway();
		List<Course> courses = new ArrayList<>();
		List<Course> tmp = getCoursesOfStudentGroup(studentClasse);
		if (tmp != null) { courses.addAll(tmp); tmp = null; }
		tmp = getCoursesOfStudentGroup(studentPromo);
		if (tmp != null) { courses.addAll(tmp); tmp = null; }
		tmp = getCoursesOfStudentGroup(studentPathway);
		if (tmp != null) { courses.addAll(tmp); tmp = null; }
		return new ArrayList<>(new HashSet<>(courses)); // delete duplicates
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
		} finally {
			session.close();
		}
		return occurrences;
	}

	private static Map<String, Object> getCourseDetails(CourseOccurence courseOccurence) {
		Map<String, Object> courseDetails = new HashMap<>();
		courseDetails.put("title", courseOccurence.getCourse().getSubject().getName());
		courseDetails.put("startTime", String.format("%02d", courseOccurence.getBeginning().getHour()));
		courseDetails.put("endTime", String.format("%02d", courseOccurence.getEnd().getHour()));
		courseDetails.put("room", courseOccurence.getClassroom());
		courseDetails.put("professor", courseOccurence.getProfessor().getFirstName() + " " + courseOccurence.getProfessor().getLastName());
		// TODO add type

		List<String> studentGroupsNames = new ArrayList<>();
		for (StudentGroup studentGroup : courseOccurence.getCourse().getStudentGroups()) {
			studentGroupsNames.add(studentGroup.getName());
		}
		courseDetails.put("classes", studentGroupsNames);
		return courseDetails;
	}

	public Map<Integer, List<Map<String, Object>>> getCoursesByPersonNumberAndDays(String personNumber, List<LocalDate> days) {
		LoginManager loginManager = LoginManager.getInstance();
		Person user = loginManager.getUserByPersonNumber(personNumber);
		Map<Integer, List<Map<String, Object>>> schedule = new HashMap<>();
		if (Admin.class.getName().equals(user.getClass().getName())) {
			// TODO return empty course list
		} else if (Professor.class.getName().equals(user.getClass().getName())) {
			Professor professor = (Professor) user;
			// TODO get courses where professor = user
		} else {
			// student case
			Student student = (Student) user;
			List<Course> courses = getCourseOfStudent(student);
			for (LocalDate day: days) {
				List<CourseOccurence> coursesOfDayInput = getCourseOccurencesOfCourseForDate(courses, day);
				List<Map<String, Object>> coursesOfDayOutput = new ArrayList<>();
				for (CourseOccurence courseOccurence : coursesOfDayInput) {
					Map<String, Object> courseDetails = getCourseDetails(courseOccurence);
					coursesOfDayOutput.add(courseDetails);
				}
				schedule.put(day.getDayOfMonth(), coursesOfDayOutput);
			}
		}
		return schedule;
	}


}