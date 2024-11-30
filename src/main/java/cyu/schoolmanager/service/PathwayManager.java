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

public class PathwayManager {
	private static PathwayManager instance;

	private PathwayManager() {}

	public static synchronized PathwayManager getInstance() {
		if (instance == null) {
			instance = new PathwayManager();
		}
		return instance;
	}

	public List<Pathway> getListOfPathways() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Pathway";
			Query<Pathway> query = session.createQuery(request, Pathway.class);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	Pathway getPathwayById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Pathway WHERE id = :id";
			Query<Pathway> query = session.createQuery(request, Pathway.class);
			query.setParameter("id", id);
			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public String createPathway(String name, String email){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Transaction transaction = session.beginTransaction();
			Pathway pathway = new Pathway();
			pathway.setName(name);
			pathway.setEmail(email);
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Pathway>> errors = validator.validate(pathway);
			if (errors.isEmpty()) {
				session.save(pathway);
				transaction.commit();
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Pathway> error : errors) {
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

	public String updatePathwayById(String id, String name, String email){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Pathway pathway = getPathwayById(id);
			pathway.setName(name);
			pathway.setEmail(email);
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Pathway>> errors = validator.validate(pathway);
			if (errors.isEmpty()) {
				session.merge(pathway);
				transaction.commit();
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Pathway> error : errors) {
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

	public String deletePathwayById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String hql = "DELETE FROM Pathway WHERE id = :id";
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
