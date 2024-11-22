package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
			session.merge(address);


			// Admin
			Admin admin = new Admin();
			admin.setFirstName("gaetan");
			admin.setPersonNumber();
			admin.setLastName("gaetan");
			admin.setPassword("admin");
			admin.setUsername("admin");
			admin.setEmail("gaetan@cy-tech.fr");
			admin.setAddress(address);
			session.merge(admin);

			// Promo
			Promo promo = new Promo();
			promo.setName("ING2");
			promo.setEmail("ing2@cy-tech.fr");
			session.merge(promo);

			// Pathway
			Pathway pathway = new Pathway();
			pathway.setName("GSI");
			pathway.setEmail("gsi@cy-tech.fr");
			session.merge(pathway);

			transaction.commit();
			session.close();
			Session session2 = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction2 = session2.beginTransaction();

			// Address2
			Address address2 = new Address();
			address2.setNumber("1");
			address2.setStreet("rue Lebon");
			address2.setCity("Cergy");
			address2.setPostalCode(95000);
			address2.setCountry("France");
			session2.merge(address2);

			Address address4 = new Address();
			address4.setNumber("2");
			address4.setStreet("rue Lemauvais");
			address4.setCity("Le Vésinet");
			address4.setPostalCode(78110);
			address4.setCountry("France");
			session2.merge(address4);

			// Subject
			Subject subject = new Subject();
			subject.setName("info");
			session2.merge(subject);

			Subject subject2 = new Subject();
			subject2.setName("IA");
			session2.merge(subject2);

			Subject subject3 = new Subject();
			subject3.setName("Math");
			session2.merge(subject3);

			// ProfessorStatus
			ProfessorStatus professorStatus = new ProfessorStatus();
			professorStatus.setStatus("titulaire");
			session2.merge(professorStatus);

			ProfessorStatus professorStatus2 = new ProfessorStatus();
			professorStatus2.setStatus("titulaire2");
			session2.merge(professorStatus2);

			List<Subject> subjects = new ArrayList<>();
			subjects.add(subject);
			subjects.add(subject2);

			List<Subject> subjects1 = new ArrayList<>();
			subjects1.add(subject3);

			// Professor
			Professor professor = new Professor();
			professor.setAddress(address2);
			professor.setPersonNumber();
			professor.setEmail("julien@cy-tech.fr");
			professor.setPassword("prof");
			professor.setUsername("prof");
			professor.setFirstName("julien");
			professor.setLastName("julien");
			professor.setStatus(professorStatus2);
			professor.setTeachingSubjects(subjects);
			session2.merge(professor);


			transaction2.commit();
			session2.close();
			Session session3 = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction3 = session3.beginTransaction();


			// Address3
			Address address3 = new Address();
			address3.setNumber("1");
			address3.setStreet("rue Lebon");
			address3.setCity("Cergy");
			address3.setPostalCode(95000);
			address3.setCountry("France");
			session3.merge(address3);

			// ClassCategory
			ClassCategory classCategory = new ClassCategory();
			classCategory.setName("TD");
			session3.merge(classCategory);

			List<StudentGroup> studentGroups = new ArrayList<>();
			studentGroups.add(pathway);
			studentGroups.add(promo);

			// Course
			Course course = new Course();
			course.setStudentGroups(studentGroups);
			course.setSubject(subject);
			course.setProfessor(professor);
			course.setClassroom("A656");
			session3.merge(course);

			Course course2 = new Course();
			course2.setStudentGroups(studentGroups);
			course2.setSubject(subject2);
			course2.setProfessor(professor);
			course2.setClassroom("A664");
			session3.merge(course2);

			Classe classe = new Classe();
			classe.setPathway(pathway);
			classe.setPromo(promo);
			classe.setEmail("ing2-gsi2@cy-tech.fr");
			session3.merge(classe);

			//Student
			Student student = new Student();
			student.setPersonNumber();
			student.setClasse(classe);
			student.setAddress(address3);
			student.setLastName("guillaume");
			student.setFirstName("guillaume");
			student.setEmail("guillaume@cy-tech.fr");
			student.setUsername("student");
			student.setPassword("student");
			session3.merge(student);

			// Email
			Email email = new Email();
			email.setObject("Mail important");
			email.setBody("Ce mail est important");
			session3.merge(email);

			LocalDate date = LocalDate.now();
			LocalDate monday;
			DayOfWeek dayOfWeek = date.getDayOfWeek();
			if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
				monday = date.with(DayOfWeek.MONDAY).plusWeeks(1);
			} else {
				monday = date.with(DayOfWeek.MONDAY);
			}

			// CourseOccurence
			CourseOccurence courseOccurence1 = new CourseOccurence();
			courseOccurence1.setCourse(course);
			courseOccurence1.setClassroom("A688");
			courseOccurence1.setProfessor(professor);
			courseOccurence1.setDay(monday);
			courseOccurence1.setBeginning(LocalTime.of(8, 0));
			courseOccurence1.setEnd(LocalTime.of(11, 0));
			courseOccurence1.setCategory(classCategory);
			session3.merge(courseOccurence1);

			CourseOccurence courseOccurence = new CourseOccurence();
			courseOccurence.setCourse(course);
			courseOccurence.setDay(monday.plusDays(3));
			courseOccurence.setBeginning(LocalTime.of(14, 45));
			courseOccurence.setEnd(LocalTime.of(16, 15));
			courseOccurence.setCategory(classCategory);
			session3.merge(courseOccurence);

			// Grade
			Grade grade = new Grade();
			grade.setStudent(student);
			grade.setCourse(course);
			grade.setDay(LocalDate.now());
			grade.setContext("Final exam DP");
			grade.setComment("GG");
			grade.setResult(20);
			grade.setSession(1);
			session3.merge(grade);

			Grade grade2 = new Grade();
			grade2.setStudent(student);
			grade2.setCourse(course2);
			grade2.setDay(LocalDate.now());
			grade2.setContext("Final exam");
			grade2.setComment("Good");
			grade2.setResult(15);
			grade2.setSession(1);
			session3.merge(grade2);

			transaction3.commit();
			session3.close();



			System.out.println(admin.getPersonNumber());
			System.out.println(professor.getPersonNumber());
			System.out.println(student.getPersonNumber());


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
			e.printStackTrace();
			if (transaction != null) transaction.rollback();
		} finally {
			HibernateUtil.shutdown();
		}


	}
}

