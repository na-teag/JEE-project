package cyu;

import cyu.schoolmanager.LoginManager;
import cyu.schoolmanager.Person;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		HttpSession session = request.getSession();

		if ("/logout".equals(action)) {
			session.invalidate(); // delete all session information
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		} else {
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try {
			LoginManager loginManager = LoginManager.getInstance();
			Person user = loginManager.authenticate(username, password);
			if (user != null) {
				request.getSession().setAttribute("user", user);
				request.getSession().setAttribute("role", user.getClass().getName());
				response.sendRedirect(request.getContextPath() + "/index.jsp");
			}
		} catch (IllegalAccessException e) {
			request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect.");
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
		}
	}
}