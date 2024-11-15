package cyu;

import cyu.schoolmanager.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		HttpSession session = request.getSession();

		if ("/logout".equals(action)) {
			session.invalidate(); // delete all session information
			response.sendRedirect(request.getContextPath());
		} else {
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		Map<String, String> roles = new HashMap<>();
		roles.put("admin", Admin.class.getName());
		roles.put("professor", Professor.class.getName());
		roles.put("student", Student.class.getName());

		try {
			LoginManager loginManager = LoginManager.getInstance();
			Person user = loginManager.authenticate(username, password);
			if (user != null) {
				request.getSession().setAttribute("user", user);
				request.getSession().setAttribute("role", user.getClass().getName());
				request.getSession().setAttribute("roles", roles);
				response.sendRedirect(request.getContextPath());
			}
		} catch (IllegalAccessException e) {
			request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect.");
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
		}
	}
}