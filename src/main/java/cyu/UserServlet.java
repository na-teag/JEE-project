package cyu;
import cyu.schoolmanager.*;
import cyu.schoolmanager.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = {"/users", "/user"})
public class UserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String path = request.getServletPath();
		ClasseManager classeManager = ClasseManager.getInstance();
		SubjectManager subjectManager = SubjectManager.getInstance();
		PersonManager personManager = PersonManager.getInstance();

		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
			if ("/users".equals(path)) {
				// get the lists of users, classes, and course subjects
				session.setAttribute("studentUsers", personManager.getAllStudents());
				session.setAttribute("adminUsers", personManager.getAllAdmins());
				session.setAttribute("profUsers", personManager.getListOfProfessors());
				session.setAttribute("classes", classeManager.getListOfClasses());
				session.setAttribute("subjects", subjectManager.getListOfSubject());
				request.getRequestDispatcher("views/users.jsp").forward(request, response);
			} else if ("/user".equals(path)) {
				// check that parameters needed everywhere are good
				String action = request.getParameter("action");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String id = request.getParameter("id");
				String email = request.getParameter("email");
				String number = request.getParameter("number");
				String street = request.getParameter("street");
				String city = request.getParameter("city");
				String postalCode = request.getParameter("postalCode");
				String country = request.getParameter("country");

				if (firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank() && number != null && !number.isBlank() && email != null && !email.isBlank() && street != null && city != null && !city.isBlank() && postalCode != null && !postalCode.isBlank() && country != null && !country.isBlank()) {
					if ("saveStudent".equals(action)) {
						// if the request is about the creation/modification of a student
						String classeId = request.getParameter("classeId");
						if (classeId == null || classeId.isEmpty()) {
							request.setAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
							request.getRequestDispatcher("/users").forward(request, response);
							return;
						}
						if (id == null || id.isEmpty()) {
							// if there is no id, then it is a new object
							LocalDate birthday = personManager.getBirthday(request);
							if (birthday == null) {
								request.setAttribute("errorMessage", "Date manquante ou impossible à convertir, impossible de traiter la requête");
								request.getRequestDispatcher("/users").forward(request, response);
								return;
							}
							String error = personManager.createStudent(id, email, lastName, firstName, birthday, number, street, city, postalCode, country, classeId);
							if (error != null && !error.isEmpty()) {
								System.out.println(error);
								request.setAttribute("errorMessage", error);
							}
						} else {
							// if there is an id the object already exists
							String error = personManager.updateStudent(id, email, lastName, firstName, number, street, city, postalCode, country, classeId);
							if (error != null && !error.isEmpty()) {
								request.setAttribute("errorMessage", error);
								System.out.println(error);
							}
						}
						request.getRequestDispatcher("/users").forward(request, response);
						return;
					} else if ("saveProf".equals(action)) {
						// if the request is about the creation/modification of a professor
						String[] subjectIds = request.getParameterValues("subjectId");
						if (subjectIds == null || subjectIds.length == 0) {
							request.setAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
							request.getRequestDispatcher("/users").forward(request, response);
							return;
						}
						List<String> subjectIdsList = Arrays.asList(subjectIds);

						// check that the Subjects exists
						List<Subject> subjects = new ArrayList<>();
						Subject subject;
						if (!subjectIdsList.contains("aucun")){
							for (String studentId : subjectIdsList) {
								subject = subjectManager.getSubjectById(studentId);
								if (subject == null) {
									request.setAttribute("errorMessage", "subject does not exist");
									request.getRequestDispatcher("/users").forward(request, response);
									return;
								}
								subjects.add(subject);
							}
						}
						if (id == null || id.isEmpty()) {
							// if there is no id, then it is a new object
							LocalDate birthday = personManager.getBirthday(request);
							if (birthday == null) {
								request.setAttribute("errorMessage", "Date manquante ou impossible à convertir, impossible de traiter la requête");
								request.getRequestDispatcher("/users").forward(request, response);
								return;
							}
							String error = personManager.createProf(id, email, lastName, firstName, birthday, number, street, city, postalCode, country, subjects);
							if (error != null && !error.isEmpty()) {
								System.out.println(error);
								request.setAttribute("errorMessage", error);
							}
						} else {
							// if there is an id the object already exists
							String error = personManager.updateProf(id, email, lastName, firstName, number, street, city, postalCode, country, subjects);
							if (error != null && !error.isEmpty()) {
								request.setAttribute("errorMessage", error);
								System.out.println(error);
							}
						}
						request.getRequestDispatcher("/users").forward(request, response);
						return;
					} else if ("saveAdmin".equals(action)) {
						// if the request is about the creation/modification of an administrator

						if (id == null || id.isEmpty()) {
							// if there is no id, then it is a new object
							LocalDate birthday = personManager.getBirthday(request);
							if (birthday == null) {
								request.setAttribute("errorMessage", "Date manquante ou impossible à convertir, impossible de traiter la requête");
								request.getRequestDispatcher("/users").forward(request, response);
								return;
							}
							String error = personManager.createAdmin(id, email, lastName, firstName, birthday, number, street, city, postalCode, country);
							if (error != null && !error.isEmpty()) {
								System.out.println(error);
								request.setAttribute("errorMessage", error);
							}
						} else {
							// if there is an id the object already exists
							String error = personManager.updateAdmin(id, email, lastName, firstName, number, street, city, postalCode, country);
							if (error != null && !error.isEmpty()) {
								request.setAttribute("errorMessage", error);
								System.out.println(error);
							}
						}
						request.getRequestDispatcher("/users").forward(request, response);
						return;
					} else if ("delete".equals(action) && id != null && !id.isEmpty()) {
						// if the request is about the deletion of a user, no matter the type
						String error = personManager.deletePersonById(id);
						if (error != null) {
							request.setAttribute("errorMessage", error);
						}
						request.getRequestDispatcher("/users").forward(request, response);
						return;
					} else {
						request.setAttribute("errorMessage", "Requête non reconnue");
						request.getRequestDispatcher("/users").forward(request, response);
						return;
					}
				}
				request.setAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
				request.getRequestDispatcher("/users").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("views/error.jsp").forward(request, response);
		}
	}
}