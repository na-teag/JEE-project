package cyu.schoolmanager.service;

import cyu.schoolmanager.HibernateUtil;
import cyu.schoolmanager.Person;
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



	public Person authenticate(String username, String password) throws IllegalAccessException {
		PersonManager personManager = PersonManager.getInstance();
		Person person = personManager.getUserByUsername(username);
		if (person != null && BCrypt.checkpw(password, person.getPasswordHash())) {
			return person;
		}
		throw new IllegalAccessException("Invalid username or password");
	}
}