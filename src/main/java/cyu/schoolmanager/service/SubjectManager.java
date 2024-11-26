package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

public class SubjectManager {
	private static SubjectManager instance;

	private SubjectManager() {}

	public static synchronized SubjectManager getInstance() {
		if (instance == null) {
			instance = new SubjectManager();
		}
		return instance;
	}


	public List<Subject> getListOfSubject() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Subject";
			Query<Subject> query = session.createQuery(request, Subject.class);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public Subject getSubjectById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String request = "FROM Subject c WHERE id = :id";
			Query<Subject> query = session.createQuery(request, Subject.class);
			query.setParameter("id", id);
			return query.uniqueResult();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public String createSubject(String name){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Transaction transaction = session.beginTransaction();
			Subject subject = new Subject();
			subject.setName(name);
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Subject>> errors = validator.validate(subject);
			if (errors.isEmpty()) {
				session.save(subject);
				transaction.commit();
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Subject> error : errors) {
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

	public String updateSubjectById(String id, String name){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Subject subject = getSubjectById(id);
			subject.setName(name);
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Subject>> errors = validator.validate(subject);
			if (errors.isEmpty()) {
				session.update(subject);
				transaction.commit();
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Subject> error : errors) {
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

	public String deleteSubjectById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String hql = "DELETE FROM Subject WHERE id = :id";
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
}
