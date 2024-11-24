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

	public List<Student> getAllStudents() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String request = "FROM Student";
			Query<Student> query = session.createQuery(request, Student.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Admin> getAllAdmins() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String request = "FROM Admin";
			Query<Admin> query = session.createQuery(request, Admin.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public List<Professor> getListOfProfessors() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Professor";
			Query<Professor> query = session.createQuery(request, Professor.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public Professor getProfessorById(String id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String request = "FROM Professor s WHERE id = :id";
			Query<Professor> query = session.createQuery(request, Professor.class);
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String checkParametersPersonCreation(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country){
		String errorMessage = "";
		return errorMessage;
	}


	public String createStudent(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country, String classeId){
		String error = checkParametersPersonCreation(id, email, lastName, firstName, number, street, city, postalCode, Country);
		return error;
		/*
		TODO envoyer un mail quand on assigne un cours
		MailManager mailManager = MailManager.getInstance();
		mailManager.sendEmail("do.not.reply@cytech.fr", student, "Changement dans vos inscription", "Bonjour, Vous recevez cet email car vous venez d'être attribué à une nouvelle classe : " + classe.getName() + ".\nConsultez votre emploi du temps pour voir vos nouveau cours.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail");
	 */
	}


	public String createProf(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country, List<Subject> subjectList){
		// peut avoir une liste de cours vide
		String error = checkParametersPersonCreation(id, email, lastName, firstName, number, street, city, postalCode, Country);
		return error;
	}


	public String createAdmin(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country){
		String error = checkParametersPersonCreation(id, email, lastName, firstName, number, street, city, postalCode, Country);
		return error;
	}


	public String updateStudent(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country, String classeId){
		String error = checkParametersPersonCreation(id, email, lastName, firstName, number, street, city, postalCode, Country);
		return error;
		/*
		TODO envoyer un mail quand on assigne un cours
		MailManager mailManager = MailManager.getInstance();
		mailManager.sendEmail("do.not.reply@cytech.fr", student, "Changement dans vos inscription", "Bonjour, Vous recevez cet email car vous venez d'être attribué à une nouvelle classe : " + classe.getName() + ".\nConsultez votre emploi du temps pour voir vos nouveau cours.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail");
	 */
	}


	public String updateProf(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country, List<Subject> subjectList){
		// peut avoir une liste de cours vide
		String error = checkParametersPersonCreation(id, email, lastName, firstName, number, street, city, postalCode, Country);
		return error;
	}


	public String updateAdmin(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country){
		String error = checkParametersPersonCreation(id, email, lastName, firstName, number, street, city, postalCode, Country);
		return error;
	}

	public String deletePersonById(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try{
			session.beginTransaction();

			Student student = getStudentById(id);
			if (student != null){
				// supprimer les notes associées s'il y en a
				String hql = "DELETE FROM Grade g WHERE student = :student";
				Query<?> query = session.createQuery(hql);
				query.setParameter("student", student);
				query.executeUpdate();
			}

			Professor professor = getProfessorById(id);
			if (professor != null){
				// supprimer les occurences de cours associées s'il y en a
				String hql = "DELETE FROM CourseOccurence c WHERE professor = :professor";
				Query<?> query = session.createQuery(hql);
				query.setParameter("professor", professor);
				query.executeUpdate();
			}

			String hql = "DELETE FROM Person p WHERE id = :id";
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