package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

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
}
