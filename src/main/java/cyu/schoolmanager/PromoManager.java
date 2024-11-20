package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

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
}
