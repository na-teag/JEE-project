package cyu;

import cyu.schoolmanager.*;
import cyu.schoolmanager.service.PathwayManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "PathwayServlet", urlPatterns = {"/pathways", "/pathway"})
public class PathwayServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String path = request.getServletPath();
		PathwayManager pathwayManager = PathwayManager.getInstance();

		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
			if ("/pathways".equals(path)) {
				session.setAttribute("pathways", pathwayManager.getListOfPathways());
				request.getRequestDispatcher("views/pathways.jsp").forward(request, response);
			} else if ("/pathway".equals(path)) {
				String action = request.getParameter("action");
				String pathwayName = request.getParameter("name");
				String pathwayId = request.getParameter("id");

				if (pathwayName != null) {
					if ("save".equals(action)) {
						if (pathwayId == null || pathwayId.isEmpty()) {
							// if there is no id, then it is a new object
							String error = pathwayManager.createPathway(pathwayName);
							if (error != null && !error.isEmpty()) {
								System.out.println(error);
								request.setAttribute("errorMessage", error);
							}
						} else {
							// if there is an id the object already exists
							String error = pathwayManager.updatePathwayById(pathwayId, pathwayName);
							if (error != null && !error.isEmpty()) {
								request.setAttribute("errorMessage", error);
								System.out.println(error);
							}
						}
						request.getRequestDispatcher("/pathways").forward(request, response);
					} else if ("delete".equals(action) && pathwayId != null && !pathwayId.isEmpty()) {
						String error = pathwayManager.deletePathwayById(pathwayId);
						if (error != null) {
							request.setAttribute("errorMessage", error);
						}
						request.getRequestDispatcher("/pathways").forward(request, response);
					} else {
						request.setAttribute("errorMessage", "Requête non reconnue");
						request.getRequestDispatcher("/pathways").forward(request, response);
					}
				}
				request.setAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
				request.getRequestDispatcher("/pathways").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("views/error.jsp").forward(request, response);
		}

	}
}