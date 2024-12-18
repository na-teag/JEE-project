package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
			return query.uniqueResult();
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
				session.merge(course);
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
				// delete grade of the former course
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


	public static Course getSelectedCourse(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		String hql = "FROM Course c WHERE c.id = :id";
		Query<Course> query = session.createQuery(hql, Course.class);
		query.setParameter("id", id);

		// Exécution de la requête et récupération des résultats
		Course course = query.uniqueResult();

		// Commit de la transaction et fermeture de la session
		session.getTransaction().commit();
		return course;
	}
}