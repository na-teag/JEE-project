package cyu;

import cyu.schoolmanager.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class HibernateListener implements ServletContextListener {
	// initiate session with hibernate immediately, to avoid delay during first request, as the login request

	@Override
	public void contextInitialized(ServletContextEvent sce) { // open hibernate session as soon as the user get on a page, to prevent delay during his first request
		HibernateUtil.getSessionFactory();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		HibernateUtil.shutdown();
	}
}