package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClasseManager {
	private static ClasseManager instance;

	private ClasseManager() {}

	public static synchronized ClasseManager getInstance() {
		if (instance == null) {
			instance = new ClasseManager();
		}
		return instance;
	}


	public List<Classe> getClassesByStudentGroup(StudentGroup studentGroup) {
		if (Classe.class.getName().equals(studentGroup.getClass().getName())) {
			Classe classe = (Classe)studentGroup;
			List<Classe> classeList = new ArrayList<>();
			classeList.add(classe);
			return classeList;
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "FROM Classe c WHERE c.pathway = :studentGroup OR c.promo = :studentGroup";
			Query<Classe> query = session.createQuery(hql, Classe.class);
			query.setParameter("studentGroup", studentGroup);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}


	public List<Classe> getListOfClasses() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Classe";
			Query<Classe> query = session.createQuery(request, Classe.class);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public String createClasse(String classeName, String promoId, String pathwayId, String email){
		PromoManager promoManager = PromoManager.getInstance();
		PathwayManager pathwayManager = PathwayManager.getInstance();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Transaction transaction = session.beginTransaction();
			Classe classe = new Classe();
			classe.setName(classeName);
			classe.setEmail(email);
			classe.setPathway(pathwayManager.getPathwayById(pathwayId));
			classe.setPromo(promoManager.getPromoById(promoId));
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Classe>> errors = validator.validate(classe);
			if (errors.isEmpty()) {
				session.save(classe);
				transaction.commit();
				System.out.println("create confirmed : " + classe.getId());
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Classe> error : errors) {
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

	public String deleteClasseById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String hql = "DELETE FROM Classe g WHERE id = :id";
			Query<?> query = session.createQuery(hql);
			query.setParameter("id", id);
			query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("delete confirmed");
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

	public String updateClasseById(String id, String classeName, String promoId, String pathwayId, String email){
		PromoManager promoManager = PromoManager.getInstance();
		PathwayManager pathwayManager = PathwayManager.getInstance();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Classe classe = getClasseById(id);
			classe.setName(classeName);
			classe.setEmail(email);
			classe.setPathway(pathwayManager.getPathwayById(pathwayId));
			classe.setPromo(promoManager.getPromoById(promoId));
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Classe>> errors = validator.validate(classe);
			if (errors.isEmpty()) {
				session.update(classe);
				transaction.commit();
				System.out.println("update confirmed : " + classe.getId());
				return null;
			}
			String errorString = "";
			for (ConstraintViolation<Classe> error : errors) {
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

	public Classe getClasseById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String request = "FROM Classe c WHERE id = :id";
			Query<Classe> query = session.createQuery(request, Classe.class);
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}
}
