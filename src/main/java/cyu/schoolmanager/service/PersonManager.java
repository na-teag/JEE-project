package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PersonManager {
	private static PersonManager instance;

	private PersonManager() {}

	public static synchronized PersonManager getInstance() {
		if (instance == null) {
			instance = new PersonManager();
		}
		return instance;
	}

	public Person getUserByUsername(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Person WHERE username = :username";
			Query<Person> query = session.createQuery(request, Person.class);
			query.setParameter("username", username);

			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public Person getUserByPersonNumber(String personNumber) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Person WHERE personNumber = :personNumber";
			Query<Person> query = session.createQuery(request, Person.class);
			query.setParameter("personNumber", personNumber);

			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public Student getStudentById(String id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String request = "FROM Student s WHERE id = :id";
			Query<Student> query = session.createQuery(request, Student.class);
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Student> getStudentsFromClasse(Classe classe) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "FROM Student s WHERE s.classe = :classe";
			Query<Student> query = session.createQuery(hql, Student.class);
			query.setParameter("classe", classe);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public List<Student> getSelectedStudentsForPathway(Pathway pathway) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		String hql = "FROM Student s WHERE s.classe.pathway.id = :pathwayId";
		Query<Student> query = session.createQuery(hql, Student.class);
		query.setParameter("pathwayId", pathway.getId());
		List<Student> students = query.getResultList();

		session.getTransaction().commit();

		return students;
	}

	public List<Student> getSelectedStudentsForPromo(Promo promo) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		String hql = "FROM Student s WHERE s.classe.promo.id = :promoId";
		Query<Student> query = session.createQuery(hql, Student.class);
		query.setParameter("promoId", promo.getId());
		List<Student> students = query.getResultList();

		session.getTransaction().commit();

		return students;
	}


}