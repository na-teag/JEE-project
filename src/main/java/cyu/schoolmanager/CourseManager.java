package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;

public class CourseManager {
	private static CourseManager instance;

	private CourseManager() {}

	public static synchronized CourseManager getInstance() {
		if (instance == null) {
			instance = new CourseManager();
		}
		return instance;
	}

	public Person findCoursesByPersonNumberAndDay(String personNumber, LocalDate day) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String request = "FROM Person WHERE username = :username";
			Query<Person> query = session.createQuery(request, Person.class);
			query.setParameter("username", "test");

			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}
}