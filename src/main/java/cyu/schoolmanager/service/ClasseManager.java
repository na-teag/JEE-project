package cyu.schoolmanager.service;

import cyu.schoolmanager.Classe;
import cyu.schoolmanager.HibernateUtil;
import cyu.schoolmanager.StudentGroup;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

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

	public StudentGroup getSelectedClasse(String id) {
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