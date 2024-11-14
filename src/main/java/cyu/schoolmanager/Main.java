package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
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
		session.createQuery("DELETE FROM CourseOccurence").executeUpdate();
		session.createQuery("DELETE FROM Email").executeUpdate();
		session.createQuery("DELETE FROM Grade").executeUpdate();
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

			Address address2 = new Address();
			address2.setNumber("1");
			address2.setStreet("rue Lebon");
			address2.setCity("Cergy");
			address2.setPostalCode(95000);
			address2.setCountry("France");
			session.persist(address2);

			Address address3 = new Address();
			address3.setNumber("1");
			address3.setStreet("rue Lebon");
			address3.setCity("Cergy");
			address3.setPostalCode(95000);
			address3.setCountry("France");
			session.persist(address3);


			// Admin
			Admin admin = new Admin();
			admin.setFirstName("gaetan");
			admin.setLastName("gaetan");
			admin.setPassword("admin");
			admin.setUsername("admin");
			admin.setEmail("gaetan@cy-tech.fr");
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
			professor.setAddress(address2);
			professor.setEmail("julien@cy-tech.fr");
			professor.setPassword("prof");
			professor.setUsername("prof");
			professor.setFirstName("julien");
			professor.setLastName("julien");
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
			course.setClassroom("A656");
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
			student.setStudentNumber();
			student.setClasse(classe);
			student.setAddress(address3);
			student.setLastName("guillaume");
			student.setFirstName("guillaume");
			student.setEmail("guillaume@cy-tech.fr");
			student.setUsername("student");
			student.setPassword("student");
			session.persist(student);

			// Email
			Email email = new Email();
			email.setObject("Mail important");
			email.setBody("Ce mail est important");
			session.persist(email);

			// CourseOccurence
			CourseOccurence courseOccurence1 = new CourseOccurence();
			courseOccurence1.setCourse(course);
			courseOccurence1.setClassroom("A688");
			courseOccurence1.setProfessor(professor);
			courseOccurence1.setDay(LocalDate.now());
			courseOccurence1.setBeginning(LocalDate.now());
			courseOccurence1.setEnd(LocalDate.now());
			session.persist(courseOccurence1);

			CourseOccurence courseOccurence = new CourseOccurence();
			courseOccurence.setCourse(course);
			courseOccurence.setDay(LocalDate.now());
			courseOccurence.setBeginning(LocalDate.now());
			courseOccurence.setEnd(LocalDate.now());
			session.persist(courseOccurence);

			// Grade
			Grade grade = new Grade();
			grade.setStudent(student);
			grade.setCourse(course);
			grade.setDay(LocalDate.now());
			grade.setContext("Final exam DP");
			grade.setComment("GG");
			grade.setResult(20);
			grade.setSession(1);
			session.persist(grade);

			transaction.commit();

			System.out.println(student.getStudentNumber());


			// LoginManager
			String login = "prof";
			String password = "prof";
			try {
				Person user = LoginManager.getInstance().authenticate(login, password);
				System.out.println("Bonjour " + user.getClass().getName() + " " + user.getFirstName());
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

