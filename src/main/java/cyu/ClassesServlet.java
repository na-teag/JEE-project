package cyu;

import cyu.schoolmanager.*;
import cyu.schoolmanager.service.ClasseManager;
import cyu.schoolmanager.service.PathwayManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "GradesServlet", urlPatterns = {"/classes", "/classe"})
public class ClassesServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String path = request.getServletPath();
		ClasseManager classeManager = ClasseManager.getInstance();
		PromoManager promoManager = PromoManager.getInstance();
		PathwayManager pathwayManager = PathwayManager.getInstance();

		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
			if ("/classes".equals(path)) {
				// get the lists of classes, promos, and pathways
				System.out.println("get");
				session.setAttribute("classes", classeManager.getListOfClasses());
				session.setAttribute("promos", promoManager.getListOfPromos());
				session.setAttribute("pathways", pathwayManager.getListOfPathways());
				request.getRequestDispatcher("views/classes.jsp").forward(request, response);
			} else if ("/classe".equals(path)) {
				String action = request.getParameter("action");
				String classeName = request.getParameter("classeName");
				String classeId = request.getParameter("classeId");
				String promoId = request.getParameter("promoId");
				String pathwayId = request.getParameter("pathwayId");
				String email = request.getParameter("email");

				if (classeName != null && promoId != null && pathwayId != null && email != null) {
					if ("save".equals(action)) {
						if (classeId == null || classeId.isEmpty()) {
							System.out.println("create");
							// if there is no id, then it is a new object
							String error = classeManager.createClasse(classeName, promoId, pathwayId, email);
							if (error != null && !error.isEmpty()) {
								System.out.println(error);
								request.setAttribute("errorMessage", error);
							}
						} else {
							System.out.println("update");
							// if there is an id the object already exists
							String error = classeManager.updateClasseById(classeId, classeName, promoId, pathwayId, email);
							if (error != null && !error.isEmpty()) {
								request.setAttribute("errorMessage", error);
								System.out.println(error);
							}
						}
						request.getRequestDispatcher("/classes").forward(request, response);
					} else if ("delete".equals(action) && classeId != null && !classeId.isEmpty()) {
						System.out.println("delete");
						String error = classeManager.deleteClasseById(classeId);
						if (error != null) {
							request.setAttribute("errorMessage", error);
							request.getRequestDispatcher("views/classes.jsp").forward(request, response);
						}
					} else {
						request.setAttribute("errorMessage", "Requête non reconnue");
						request.getRequestDispatcher("/classes").forward(request, response);
					}
				}
				request.setAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
				request.getRequestDispatcher("/classes").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("views/error.jsp").forward(request, response);
		}

	}
}