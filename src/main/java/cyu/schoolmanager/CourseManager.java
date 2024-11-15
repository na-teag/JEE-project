package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

public class CourseManager {
	private static CourseManager instance;

	private CourseManager() {}

	public static synchronized CourseManager getInstance() {
		if (instance == null) {
			instance = new CourseManager();
		}
		return instance;
	}

	public Person findCoursesByPerson(Person person, Long requestedId) {
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

	public Person authenticate(String username, String password) throws IllegalAccessException {
		Person person = findUserByUsername(username);
		if (person != null && BCrypt.checkpw(password, person.getPasswordHash())) {
			return person;
		}
		throw new IllegalAccessException("Invalid username or password");
	}
}