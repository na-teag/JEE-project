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

	public <T extends Person> T findUserByUsername(String username, Class<T> type) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM " + type.getSimpleName() + " WHERE username = :username";
			Query<T> query = session.createQuery(request, type);
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
		Admin admin = findUserByUsername(username, Admin.class);
		if (admin != null && BCrypt.checkpw(password, admin.getPasswordHash())) {
			return admin;
		}
		Professor professor = findUserByUsername(username, Professor.class);
		if (professor != null && BCrypt.checkpw(password, professor.getPasswordHash())) {
			return professor;
		}
		Student student = findUserByUsername(username, Student.class);
		if (student != null && BCrypt.checkpw(password, student.getPasswordHash())) {
			return student;
		}
		throw new IllegalAccessException("Invalid username or password");
	}
}