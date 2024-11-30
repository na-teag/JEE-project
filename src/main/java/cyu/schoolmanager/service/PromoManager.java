package cyu.schoolmanager.service;

import cyu.schoolmanager.HibernateUtil;
import cyu.schoolmanager.Promo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

public class PromoManager {
	private static PromoManager instance;

	private PromoManager() {}

	public static synchronized PromoManager getInstance() {
		if (instance == null) {
			instance = new PromoManager();
		}
		return instance;
	}

	public List<Promo> getListOfPromos() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Promo";
			Query<Promo> query = session.createQuery(request, Promo.class);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public Promo getPromoById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Promo WHERE id = :id";
			Query<Promo> query = session.createQuery(request, Promo.class);
			query.setParameter("id", id);
			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public String createPromo(String name, String email){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Transaction transaction = session.beginTransaction();
			Promo promo = new Promo();
			promo.setName(name);
			promo.setEmail(email);
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Promo>> errors = validator.validate(promo);
			if (errors.isEmpty()) {
				session.save(promo);
				transaction.commit();
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Promo> error : errors) {
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

	public String updatePromoById(String id, String name, String email){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Promo promo = getPromoById(id);
			promo.setName(name);
			promo.setEmail(email);
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Promo>> errors = validator.validate(promo);
			if (errors.isEmpty()) {
				session.merge(promo);
				transaction.commit();
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Promo> error : errors) {
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

	public String deletePromoById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String hql = "DELETE FROM Promo g WHERE id = :id";
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
