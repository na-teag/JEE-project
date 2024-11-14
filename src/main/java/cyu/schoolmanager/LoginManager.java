package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

public class LoginManager {
	private static LoginManager instance;

	private LoginManager() {}

	public static synchronized LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	public Person findUserByUsername(String username) {
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