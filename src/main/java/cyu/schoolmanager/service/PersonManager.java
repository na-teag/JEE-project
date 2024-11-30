package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String request = "FROM Person WHERE username = :username";
			Query<Person> query = session.createQuery(request, Person.class);
			query.setParameter("username", username);

			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Person getUserByPersonNumber(String personNumber) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String request = "FROM Person WHERE personNumber = :personNumber";
			Query<Person> query = session.createQuery(request, Person.class);
			query.setParameter("personNumber", personNumber);

			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Person getUserByEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String request = "FROM Person WHERE email = :email";
			Query<Person> query = session.createQuery(request, Person.class);
			query.setParameter("email", email);

			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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

	public Admin getAdminById(String id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String request = "FROM Admin a WHERE id = :id";
			Query<Admin> query = session.createQuery(request, Admin.class);
			query.setParameter("id", id);
			return query.uniqueResult();
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
			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Student> getStudentsFromClasse(Classe classe) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Student s WHERE s.classe = :classe";
			Query<Student> query = session.createQuery(hql, Student.class);
			query.setParameter("classe", classe);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Student> getSelectedStudentsForPathway(Pathway pathway) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Student s WHERE s.classe.pathway.id = :pathwayId";
			Query<Student> query = session.createQuery(hql, Student.class);
			query.setParameter("pathwayId", pathway.getId());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Student> getSelectedStudentsForPromo(Promo promo) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Student s WHERE s.classe.promo.id = :promoId";
			Query<Student> query = session.createQuery(hql, Student.class);
			query.setParameter("promoId", promo.getId());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Professor> getListOfProfessors() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String request = "FROM Professor";
			Query<Professor> query = session.createQuery(request, Professor.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Professor getProfessorById(String id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String request = "FROM Professor s WHERE id = :id";
			Query<Professor> query = session.createQuery(request, Professor.class);
			query.setParameter("id", id);
			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	private String setProf(String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country, Professor professor, List<Subject> subjectList) {
		Person person = getUserByEmail(email);
		if (person != null && !person.getId().equals(professor.getId())) {
			return "cet email est déjà attribué";
		}
		professor.setEmail(email);
		professor.setLastName(lastName);
		professor.setFirstName(firstName);
		Address address = new Address();
		address.setNumber(number);
		address.setStreet(street);
		address.setCity(city);
		address.setPostalCode(Integer.parseInt(postalCode));
		address.setCountry(Country);
		professor.setAddress(address);
		professor.setTeachingSubjects(subjectList);
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Professor>> violations = validator.validate(professor);
		String errors = "";
		for (ConstraintViolation<Professor> violation : violations) {
			errors += violation.getMessage() + "\n";
		}
		return errors;
	}

	private String setStudent(String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country, Student student, String classeId) {
		Person person = getUserByEmail(email);
		if (person != null && !person.getId().equals(student.getId())) {
			return "cet email est déjà attribué";
		}
		student.setEmail(email);
		student.setLastName(lastName);
		student.setFirstName(firstName);
		Address address = new Address();
		address.setNumber(number);
		address.setStreet(street);
		address.setCity(city);
		address.setPostalCode(Integer.parseInt(postalCode));
		address.setCountry(Country);
		student.setAddress(address);
		ClasseManager classeManager = ClasseManager.getInstance();
		Classe classe = classeManager.getClasseById(classeId);
		if (classe == null) {
			return "la classe n'existe pas";
		}
		student.setClasse(classe);
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Student>> violations = validator.validate(student);
		String errors = "";
		for (ConstraintViolation<Student> violation : violations) {
			errors += violation.getMessage() + "\n";
		}
		return errors;
	}

	private String setAdmin(String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country, Admin admin) {
		Person person = getUserByEmail(email);
		if (person != null && !person.getId().equals(admin.getId())) {
			return "cet email est déjà attribué";
		}
		admin.setEmail(email);
		admin.setLastName(lastName);
		admin.setFirstName(firstName);
		Address address = new Address();
		address.setNumber(number);
		address.setStreet(street);
		address.setCity(city);
		address.setPostalCode(Integer.parseInt(postalCode));
		address.setCountry(Country);
		admin.setAddress(address);
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
		String errors = "";
		for (ConstraintViolation<Admin> violation : violations) {
			errors += violation.getMessage() + "\n";
		}
		return errors;
	}


	public String createStudent(String email, String lastName, String firstName, LocalDate birthday, String number, String street, String city, String postalCode, String Country, String classeId){
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			Student student = new Student();

			student.setPersonNumber();
			student.setBirthday(birthday);
			String username = "e-" + firstName.charAt(0);
			username = username.toLowerCase();
			String lastNameCut = lastName.length() > 7 ? lastName.substring(0, 7) : lastName;
			lastNameCut = lastNameCut.toLowerCase();
			if (getUserByUsername(username + lastNameCut) != null) { // if the username already exists, add the most little number behind
				int x = 1;
				while (getUserByUsername(username + lastNameCut + x) != null) {
					x++;
				}
				student.setUsername(username + lastNameCut + x);
			} else {
				student.setUsername(username + lastNameCut);
			}
			String password = String.format("%02d%02d%d", birthday.getDayOfMonth(), birthday.getMonthValue(), birthday.getYear());
			student.setPassword(password);

			String errors = setStudent(email, lastName, firstName, number, street, city, postalCode, Country, student, classeId);
			if (errors.isEmpty()) {
				session.save(student);
				transaction.commit();
				MailManager mailManager = MailManager.getInstance();
				ClasseManager classeManager = ClasseManager.getInstance();
				mailManager.sendEmail("do.not.reply@cytech.fr", student, "première inscription à CYTech", "Bonjour, Vous recevez cet email car vous venez d'être attribué à une nouvelle classe : " + classeManager.getClasseById(classeId).getName() + ".\nConsultez votre emploi du temps pour voir vos nouveau cours.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail");
				return null;
			}
			return errors;
		} catch (NumberFormatException e) {
			return "le code postal doit être un entier positif";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	public String createProf(String email, String lastName, String firstName, LocalDate birthday, String number, String street, String city, String postalCode, String Country, List<Subject> subjectList){
		// peut avoir une liste de cours vide
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			Professor professor = new Professor();

			professor.setPersonNumber();
			professor.setBirthday(birthday);
			String username = "e-" + firstName.charAt(0);
			username = username.toLowerCase();
			String lastNameCut = lastName.length() > 7 ? lastName.substring(0, 7) : lastName;
			lastNameCut = lastNameCut.toLowerCase();
			if (getUserByUsername(username + lastNameCut) != null) { // if the username already exists, add the most little number behind
				int x = 1;
				while (getUserByUsername(username + lastNameCut + x) != null) {
					x++;
				}
				professor.setUsername(username + lastNameCut + x);
			} else {
				professor.setUsername(username + lastNameCut);
			}
			String password = String.format("%02d%02d%d", birthday.getDayOfMonth(), birthday.getMonthValue(), birthday.getYear());
			professor.setPassword(password);

			String errors = setProf(email, lastName, firstName, number, street, city, postalCode, Country, professor, subjectList);
			if (errors.isEmpty()) {
				session.save(professor);
				transaction.commit();
				return null;
			}
			return errors;
		} catch (NumberFormatException e) {
			return "le code postal doit être un entier positif";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	public String createAdmin(String email, String lastName, String firstName, LocalDate birthday, String number, String street, String city, String postalCode, String Country){
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			Admin admin = new Admin();

			admin.setPersonNumber();
			admin.setBirthday(birthday);
			String username = "e-" + firstName.charAt(0);
			username = username.toLowerCase();
			String lastNameCut = lastName.length() > 7 ? lastName.substring(0, 7) : lastName;
			lastNameCut = lastNameCut.toLowerCase();
			if (getUserByUsername(username + lastNameCut) != null) { // if the username already exists, add the most little number behind
				int x = 1;
				while (getUserByUsername(username + lastNameCut + x) != null) {
					x++;
				}
				admin.setUsername(username + lastNameCut + x);
			} else {
				admin.setUsername(username + lastNameCut);
			}
			String password = String.format("%02d%02d%d", birthday.getDayOfMonth(), birthday.getMonthValue(), birthday.getYear());
			admin.setPassword(password);

			String errors = setAdmin(email, lastName, firstName, number, street, city, postalCode, Country, admin);
			if (errors.isEmpty()) {
				session.save(admin);
				transaction.commit();
				return null;
			}
			return errors;
		} catch (NumberFormatException e) {
			return "le code postal doit être un entier positif";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	public String updateStudent(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country, String classeId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Student student = getStudentById(id);
			if (student == null) {
				return "cet élève n'existe pas";
			}
			Classe formerClasse = student.getClasse();
			String errors = setStudent(email, lastName, firstName, number, street, city, postalCode, Country, student, classeId);
			if (errors.isEmpty()) {
				session.merge(student);
				transaction.commit();
				if (formerClasse.getId() != student.getClasse().getId()) {
					// s'il y a un changement de classe, envoyer un mail pour prévenir
					MailManager mailManager = MailManager.getInstance();
					ClasseManager classeManager = ClasseManager.getInstance();
					mailManager.sendEmail("do.not.reply@cytech.fr", student, "Changement dans vos inscriptions", "Bonjour, Vous recevez cet email car vous venez d'être attribué à une nouvelle classe : " + classeManager.getClasseById(classeId).getName() + ".\nConsultez votre emploi du temps pour voir vos nouveau cours.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail");
				}
				return null;
			}
			return errors;
		} catch (Exception e) {
			e.printStackTrace();
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			return e.getMessage();
		} finally {
			session.close();
		}
	}


	public String updateProf(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country, List<Subject> subjectList){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Professor professor = getProfessorById(id);
			if (professor == null) {
				return "ce professeur n'existe pas";
			}
			// vérifier qu'on n'a pas supprimé la possibilité que le prof enseigne des cours qu'il enseigne déjà
			List<Course> formerCourses = CourseManager.getInstance().getCoursesOfProfessor(professor);
			List<Long> subjectIdsList = new ArrayList<>();
			if (!formerCourses.isEmpty()) {
				for (Subject subject : subjectList) { // faire une liste pour comparer les id, sinon la comparaison est toujours fausse
					subjectIdsList.add(subject.getId());
				}
				for (Course course : formerCourses) {
					if (!subjectIdsList.contains(course.getSubject().getId())) {
						return "Le professeur " + professor.getFirstName() + " " + professor.getLastName() + " enseigne déjà des cours de la matière " + course.getSubject().getName() + ". Veuillez supprimer ces cours ou leur assigner un autre professeur";
					}
				}
			}
			String errors = setProf(email, lastName, firstName, number, street, city, postalCode, Country, professor, subjectList);
			if (errors.isEmpty()) {
				session.merge(professor);
				transaction.commit();
				return null;
			}
			return errors;
		} catch (Exception e) {
			e.printStackTrace();
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			return e.getMessage();
		} finally {
			session.close();
		}
	}


	public String updateAdmin(String id, String email, String lastName, String firstName, String number, String street, String city, String postalCode, String Country){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Admin admin = getAdminById(id);
			if (admin == null) {
				return "cet administrateur n'existe pas";
			}
			String errors = setAdmin(email, lastName, firstName, number, street, city, postalCode, Country, admin);
			if (errors.isEmpty()) {
				session.merge(admin);
				transaction.commit();
				return null;
			}
			return errors;
		} catch (Exception e) {
			e.printStackTrace();
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			return e.getMessage();
		} finally {
			session.close();
		}
	}

	public String deletePersonById(String id) {
		// supprimer les sujets du prof, pour éviter une erreur sql due à la clé étrangère de la table intermédiaire de manytomany
		Professor professor = getProfessorById(id);
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction transaction = session.beginTransaction();
			if (professor != null && !professor.getTeachingSubjects().isEmpty()) {
				professor.setTeachingSubjects(new ArrayList<>());
				session.merge(professor);
				transaction.commit();
			}
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			session.close();
		}

		session = HibernateUtil.getSessionFactory().openSession();
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

			if (professor != null){
				CourseManager courseManager = CourseManager.getInstance();
				if (!courseManager.getCoursesOfProfessor(professor).isEmpty()){
					return "il y a des cours donnés par le professeur " + professor.getFirstName() + " " + professor.getLastName() + " d'enregistrés, veuillez les associer à un autre professeur ou les supprimer auparavant";
				}
				// supprimer les occurrences isolées de cours associées s'il y en a
				String hql = "DELETE FROM CourseOccurrence c WHERE professor = :professor";
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


	public LocalDate getBirthday(HttpServletRequest request){
		String birthdayStr = request.getParameter("birthday");
		if (birthdayStr == null || birthdayStr.isEmpty()) {
			return null;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			return LocalDate.parse(birthdayStr, formatter);
		} catch (DateTimeParseException e) {
			return null;
		}
	}


}