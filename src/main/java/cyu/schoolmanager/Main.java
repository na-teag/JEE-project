package cyu.schoolmanager;

import cyu.schoolmanager.service.CourseManager;
import cyu.schoolmanager.service.LoginManager;
import cyu.schoolmanager.service.MailManager;
import cyu.schoolmanager.service.PersonManager;
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

		// remove objects in intermediate tables
		List<Course> courses = CourseManager.getInstance().getListOfCourses();
		for (Course course : courses) {
			course.setStudentGroups(new ArrayList<>());
		}
		List<Professor> professors = PersonManager.getInstance().getListOfProfessors();
		for (Professor professor : professors) {
			professor.setTeachingSubjects(new ArrayList<>());
		}

		session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
		session.createQuery("DELETE FROM Address").executeUpdate();
		session.createQuery("DELETE FROM Admin").executeUpdate();
		session.createQuery("DELETE FROM ClassCategory").executeUpdate();
		session.createQuery("DELETE FROM Classe").executeUpdate();
		session.createQuery("DELETE FROM Course").executeUpdate();
		session.createQuery("DELETE FROM CourseOccurrence").executeUpdate();
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
			admin.setFirstName("John");
			admin.setPersonNumber();
			admin.setLastName("Doe");
			admin.setPassword("admin");
			admin.setUsername("admin");
			admin.setEmail("john.doe@cy-tech.fr");
			admin.setAddress(address);
			admin.setBirthday(LocalDate.of(2000,1,1));
			session.merge(admin);

			// Promo
			Promo promo = new Promo();
			promo.setName("ING2");
			promo.setEmail("ing2@cy-tech.fr");
			session.merge(promo);

			Promo promo2 = new Promo();
			promo2.setName("ING1");
			promo2.setEmail("ing1@cy-tech.fr");
			session.merge(promo2);

			// Pathway
			Pathway pathway = new Pathway();
			pathway.setName("GSI");
			pathway.setEmail("gsi@cy-tech.fr");
			session.merge(pathway);

			Pathway pathway2 = new Pathway();
			pathway2.setName("GI");
			pathway2.setEmail("gi@cy-tech.fr");
			session.merge(pathway2);

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

			List<Subject> subjects = new ArrayList<>();
			subjects.add(subject);
			subjects.add(subject2);

			// Professor
			Professor professor = new Professor();
			professor.setAddress(address2);
			professor.setPersonNumber();
			professor.setEmail("alex.smith@cy-tech.fr");
			professor.setPassword("prof");
			professor.setUsername("prof");
			professor.setFirstName("Alex");
			professor.setLastName("Smith");
			professor.setBirthday(LocalDate.of(2000,1,1));
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
			classCategory.setColor("#4a4aff");
			session3.merge(classCategory);

			ClassCategory classCategory2 = new ClassCategory();
			classCategory2.setName("CM");
			classCategory2.setColor("#ff0000");
			session3.merge(classCategory2);

			// Classe
			Classe classe = new Classe();
			classe.setName("ING2 GSI2");
			classe.setPathway(pathway);
			classe.setPromo(promo);
			classe.setEmail("ing2-gsi2@cy-tech.fr");
			session3.merge(classe);

			List<StudentGroup> studentGroups = new ArrayList<>();
			studentGroups.add(pathway);
			studentGroups.add(promo);
			studentGroups.add(classe);

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

			Classe classe2 = new Classe();
			classe2.setPathway(pathway);
			classe2.setPromo(promo);
			classe2.setName("ING2 GSI1");
			classe2.setEmail("ing1-gsi2@cy-tech.fr");
			session3.merge(classe2);

			//Student
			Student student = new Student();
			student.setPersonNumber();
			student.setAddress(address3);
			student.setLastName("Johnson");
			student.setFirstName("Emma");
			student.setEmail("emma.johnson@cy-tech.fr");
			student.setUsername("student");
			student.setPassword("student");
			student.setBirthday(LocalDate.of(2000,1,1));
			student.setClasse(classe);
			session3.merge(student);

			LocalDate date = LocalDate.now();
			LocalDate monday;
			DayOfWeek dayOfWeek = date.getDayOfWeek();
			if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
				monday = date.with(DayOfWeek.MONDAY).plusWeeks(1);
			} else {
				monday = date.with(DayOfWeek.MONDAY);
			}

			// CourseOccurrence
			CourseOccurrence courseOccurrence1 = new CourseOccurrence();
			courseOccurrence1.setCourse(course);
			courseOccurrence1.setClassroom("A688");
			courseOccurrence1.setProfessor(professor);
			courseOccurrence1.setDay(monday);
			courseOccurrence1.setBeginning(LocalTime.of(8, 0));
			courseOccurrence1.setEnd(LocalTime.of(11, 0));
			courseOccurrence1.setCategory(classCategory);
			session3.merge(courseOccurrence1);

			CourseOccurrence courseOccurrence = new CourseOccurrence();
			courseOccurrence.setCourse(course);
			courseOccurrence.setDay(monday.plusDays(3));
			courseOccurrence.setBeginning(LocalTime.of(14, 45));
			courseOccurrence.setEnd(LocalTime.of(16, 15));
			courseOccurrence.setCategory(classCategory2);
			session3.merge(courseOccurrence);

			// Grade
			Grade grade = new Grade();
			grade.setStudent(student);
			grade.setCourse(course);
			grade.setContext("Final exam DP");
			grade.setComment("GG");
			grade.setResult(20);
			grade.setSession(1);
			session3.merge(grade);

			Grade grade2 = new Grade();
			grade2.setStudent(student);
			grade2.setCourse(course2);
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

				List<Emailable> list = new ArrayList<>();
				list.add(student);
				MailManager.getInstance().sendEmail(professor.getEmail(), list, "subject", "body");

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

