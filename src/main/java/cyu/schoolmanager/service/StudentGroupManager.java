package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class StudentGroupManager {
	private static StudentGroupManager instance;

	private StudentGroupManager() {}

	public static synchronized StudentGroupManager getInstance() {
		if (instance == null) {
			instance = new StudentGroupManager();
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

	public StudentGroup getStudentGroupFromId(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		String hql = "FROM StudentGroup s WHERE s.id = :id";
		Query<StudentGroup> query = session.createQuery(hql, StudentGroup.class);
		query.setParameter("id", id);

		// Exécution de la requête et récupération des résultats
		StudentGroup classe = query.getSingleResult();

		// Commit de la transaction et fermeture de la session
		session.getTransaction().commit();
		return classe;
	}
}