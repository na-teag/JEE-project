package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

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



	public StudentGroup getStudentGroupFromId(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		String hql = "FROM StudentGroup s WHERE s.id = :id";
		Query<StudentGroup> query = session.createQuery(hql, StudentGroup.class);
		query.setParameter("id", id);

		// Exécution de la requête et récupération des résultats
		StudentGroup classe = query.uniqueResult();

		// Commit de la transaction et fermeture de la session
		session.getTransaction().commit();
		return classe;
	}

	public List<StudentGroup> getListOfStudentGroups() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM StudentGroup";
			Query<StudentGroup> query = session.createQuery(request, StudentGroup.class);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}
}