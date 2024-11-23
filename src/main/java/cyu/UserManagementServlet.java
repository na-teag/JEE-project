package cyu;
import cyu.schoolmanager.*;
import cyu.schoolmanager.service.PersonManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserManagementServlet", urlPatterns = "/UserManagementServlet")
public class UserManagementServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

	// Get the type parameter and handle null cases
	String type = request.getParameter("type");
	if (type == null) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter 'type' is required.");
		return;
	}

	String hql;
	List<? extends Person> listUser = null;
	PersonManager personManager = PersonManager.getInstance();

	// Determine HQL query based on 'type'
	if (type.equals("student")) {
		listUser = personManager.getAllStudents();
	}
	else if (type.equals("teacher")) {
		listUser = personManager.getListOfProfessors();
	}
	else{
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Type d'utilisateur non reconnu");
	}

	// Process the result and add it to the request scope
	request.setAttribute("results", listUser);
	request.setAttribute("type", type);
	// Forward to the JSP page
	String page = "views/user.jsp";
	request.getRequestDispatcher(page).forward(request, response);
	}
}
