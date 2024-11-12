package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
		session.createQuery("DELETE FROM Address").executeUpdate();
		session.createQuery("DELETE FROM Admin").executeUpdate();
		session.createQuery("DELETE FROM ClassCategory").executeUpdate();
		session.createQuery("DELETE FROM Classe").executeUpdate();
		session.createQuery("DELETE FROM Course").executeUpdate();
		session.createQuery("DELETE FROM Pathway").executeUpdate();
		session.createQuery("DELETE FROM Professor").executeUpdate();
		session.createQuery("DELETE FROM Promo").executeUpdate();
		session.createQuery("DELETE FROM Student").executeUpdate();
		session.createQuery("DELETE FROM Subject").executeUpdate();
		session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

		try {
			// Address
			Address address = new Address();
			address.setNumber("1");
			address.setStreet("rue Lebon");
			address.setCity("Cergy");
			address.setPostalCode(95000);
			address.setCountry("France");
			session.persist(address);

			// Admin
			Admin admin = new Admin();
			admin.setFirstName("gaetan");
			admin.setLastName("retel");
			admin.setPassword("admin");
			admin.setLogin("admin");
			admin.setEmail("retelgaeta@cy-tech.fr");
			admin.setAddress(address);
			session.persist(admin);

			// Promo
			Promo promo = new Promo();
			promo.setName("ING2");
			promo.setEmail("ing2@cy-tech.fr");
			session.persist(promo);

			// Pathway
			Pathway pathway = new Pathway();
			pathway.setName("GSI");
			pathway.setEmail("gsi@cy-tech.fr");
			session.persist(pathway);

			// Subject
			Subject subject = new Subject();
			subject.setName("info");
			session.persist(subject);

			// ClassCategory
			ClassCategory classCategory = new ClassCategory();
			classCategory.setName("TD");
			session.persist(classCategory);

			// ProfessorStatus
			ProfessorStatus professorStatus = new ProfessorStatus();
			professorStatus.setStatus("titulaire");
			session.persist(professorStatus);

			List<Subject> subjects = new ArrayList<>();
			subjects.add(subject);

			// Professor
			Professor professor = new Professor();
			professor.setAddress(admin.getAddress());
			professor.setEmail("guyotjulie@cy-tech.fr");
			professor.setPassword("prof");
			professor.setLogin("prof");
			professor.setFirstName("Julien");
			professor.setLastName("Guyot");
			professor.setStatus(professorStatus);
			professor.setTeachingSubjects(subjects);
			session.persist(professor);

			List<StudentGroup> studentGroups = new ArrayList<>();
			studentGroups.add(pathway);
			studentGroups.add(promo);

			// Course
			Course course = new Course();
			course.setStudentGroups(studentGroups);
			course.setCategory(classCategory);
			course.setSubject(subject);
			course.setProfessor(professor);
			session.persist(course);

			List<Course> courses = new ArrayList<>();
			courses.add(course);

			// Classe
			Classe classe = new Classe();
			classe.setPathway(pathway);
			classe.setPromo(promo);
			classe.setEmail("ing2-gsi2@cy-tech.fr");
			classe.setCourses(courses);
			session.persist(classe);

			//Student
			Student student = new Student();
			student.setStudentNumber("0123456789");
			student.setClasse(classe);
			student.setCourses(null);
			student.setAddress(address);
			student.setLastName("Androny");
			student.setFirstName("Guillaume");
			student.setEmail("andronygui@cy-tech.fr");
			student.setLogin("student");
			student.setPassword("student");
			session.persist(student);


			transaction.commit();


			// LoginManager
			String login = "student";
			String password = "student";
			try {
				Person user = LoginManager.getInstance().authenticate(login, password);
				System.out.println("Bonjour " + user.getFirstName());
			}catch (IllegalAccessException e){
				System.out.println("Identifiant ou mot de passe incorrecte");
			}


		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
			HibernateUtil.shutdown();
		}
	}
}
