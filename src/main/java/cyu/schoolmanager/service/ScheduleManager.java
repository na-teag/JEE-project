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

	public List<CourseOccurence> getListOfCourseOccurence() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM CourseOccurence";
			Query<CourseOccurence> query = session.createQuery(request, CourseOccurence.class);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public String deleteCourseOccurenceById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String hql = "DELETE FROM CourseOccurence WHERE id = :id";
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

	public void getOrCreateCourseOccurence(Professor professor, ClassCategory classCategory, Course course, String classroom, LocalDate day, LocalTime beginning, LocalTime end) throws Exception {
		// Création du cours
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();

			CourseOccurence occurence = new CourseOccurence();
			occurence.setCourse(course);
			occurence.setDay(day);
			occurence.setBeginning(beginning);
			occurence.setEnd(end);
			occurence.setCategory(classCategory);
			// ajouter les options s'il y en a
			if (professor != null) {
				occurence.setProfessor(professor);
			}
			if (classroom != null) {
				occurence.setClassroom(classroom);
			}
			// vérifier les validators
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<CourseOccurence>> errors = validator.validate(occurence);
			if (errors.isEmpty()) {
				session.save(occurence);
				transaction.commit();
				return;
			}
			throw new Exception(errors.toString());
		}
	}
}
