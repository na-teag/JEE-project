package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ScheduleManager {
	private static ScheduleManager instance;

	private ScheduleManager() {}

	public static synchronized ScheduleManager getInstance() {
		if (instance == null) {
			instance = new ScheduleManager();
		}
		return instance;
	}

	public List<CourseOccurrence> getListOfCourseOccurrence() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM CourseOccurrence";
			Query<CourseOccurrence> query = session.createQuery(request, CourseOccurrence.class);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public String deleteCourseOccurrenceById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String hql = "DELETE FROM CourseOccurrence WHERE id = :id";
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

	private List<CourseOccurrence> getCourseOccurrencesOfCourseForDate(List<Course> courses, LocalDate date) {
		List<CourseOccurrence> occurrences = new ArrayList<>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			for (Course course : courses) {
				String hql = "FROM CourseOccurrence WHERE course = :course AND day = :date";
				Query<CourseOccurrence> query = session.createQuery(hql, CourseOccurrence.class);
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

	private static Map<String, String> getCourseDetails(CourseOccurrence courseOccurrence) {
		Map<String, String> courseDetails = new HashMap<>();
		courseDetails.put("title", courseOccurrence.getCourse().getSubject().getName());
		courseDetails.put("startTime", String.format("%02d", courseOccurrence.getBeginning().getHour()) + "h" + String.format("%02d", courseOccurrence.getBeginning().getMinute()));
		courseDetails.put("endTime", String.format("%02d", courseOccurrence.getEnd().getHour()) + "h" + String.format("%02d", courseOccurrence.getEnd().getMinute()));
		courseDetails.put("room", courseOccurrence.getClassroom());
		courseDetails.put("professor", courseOccurrence.getProfessor().getFirstName() + " " + courseOccurrence.getProfessor().getLastName());
		courseDetails.put("type", courseOccurrence.getCategory().getName());
		courseDetails.put("color", courseOccurrence.getCategory().getColor());

		String studentGroupsNames = "";
		for (StudentGroup studentGroup : courseOccurrence.getCourse().getStudentGroups()) {
			if (!studentGroupsNames.isEmpty()){
				studentGroupsNames += ", ";
			}
			studentGroupsNames += studentGroup.getName();
		}
		courseDetails.put("classes", studentGroupsNames);
		return courseDetails;
	}

	private Map<String, List<Map<String, String>>> getScheduleForCoursesAndDays(List<LocalDate> days, Map<String, List<Map<String, String>>> schedule, List<Course> courses) {
		for (LocalDate day: days) {
			List<CourseOccurrence> coursesOfDayInput = getCourseOccurrencesOfCourseForDate(courses, day);
			List<Map<String, String>> coursesOfDayOutput = new ArrayList<>();
			for (CourseOccurrence courseOccurrence : coursesOfDayInput) {
				Map<String, String> courseDetails = getCourseDetails(courseOccurrence);
				coursesOfDayOutput.add(courseDetails);
			}
			schedule.put(day.getDayOfWeek().toString(), coursesOfDayOutput);
		}
		return schedule;
	}

	public Map<String, List<Map<String, String>>> getCoursesByPersonNumberAndDays(String personNumber, List<LocalDate> days) {
		PersonManager personManager = PersonManager.getInstance();
		CourseManager courseManager = CourseManager.getInstance();
		Person user = personManager.getUserByPersonNumber(personNumber);
		Map<String, List<Map<String, String>>> schedule = new HashMap<>();
		if (Admin.class.getName().equals(user.getClass().getName())) {
			for (LocalDate day : days) {
				schedule.put(day.getDayOfWeek().toString(), new ArrayList<>());
			}
		} else if (Professor.class.getName().equals(user.getClass().getName())) {
			Professor professor = (Professor) user;
			List<Course> courses = courseManager.getCoursesOfProfessor(professor);
			schedule = getScheduleForCoursesAndDays(days, schedule, courses);
		} else {
			// student case
			Student student = (Student) user;
			List<Course> courses = courseManager.getCourseOfStudent(student);
			schedule = getScheduleForCoursesAndDays(days, schedule, courses);
		}
		return schedule;
	}

	public void createCourseOccurrence(Professor professor, ClassCategory classCategory, Course course, String classroom, LocalDate day, LocalTime beginning, LocalTime end) throws Exception {
		// Création du cours
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();

			CourseOccurrence occurrence = new CourseOccurrence();
			occurrence.setCourse(course);
			occurrence.setDay(day);
			occurrence.setBeginning(beginning);
			occurrence.setEnd(end);
			occurrence.setCategory(classCategory);
			// ajouter les options s'il y en a
			if (professor != null) {
				occurrence.setProfessor(professor);
			}
			if (classroom != null) {
				occurrence.setClassroom(classroom);
			}
			// vérifier les validators
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<CourseOccurrence>> errors = validator.validate(occurrence);
			if (errors.isEmpty()) {
				session.save(occurrence);
				transaction.commit();
				return;
			}
			throw new Exception(errors.toString());
		}
	}
}
