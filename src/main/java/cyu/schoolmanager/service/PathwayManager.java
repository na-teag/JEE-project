package cyu.schoolmanager.service;
import cyu.schoolmanager.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

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
}
