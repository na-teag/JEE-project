package cyu;

import cyu.schoolmanager.*;
import cyu.schoolmanager.service.StudentGroupManager;
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
		StudentGroupManager studentGroupManager = StudentGroupManager.getInstance();

		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
			if ("/classes".equals(path)) {
				// get the lists of classes, promos, and pathways
				session.setAttribute("classes", studentGroupManager.getListOfClasses());
				session.setAttribute("promos", studentGroupManager.getListOfPromos());
				session.setAttribute("pathways", studentGroupManager.getListOfPathways());
				request.getRequestDispatcher("views/classes.jsp").forward(request, response);
			} else if ("/classe".equals(path)) {
				String action = request.getParameter("action");
				String classeName = request.getParameter("classeName");
				String classeId = request.getParameter("classeId");
				String promoId = request.getParameter("promoId");
				String pathwayId = request.getParameter("pathwayId");

				if (classeName != null && promoId != null && pathwayId != null) {
					if ("save".equals(action)) {
						if (classeId == null || classeId.isEmpty()) {
							// if there is no id, then it is a new object
							System.out.println("create " + classeName + " with pathway " + pathwayId + " and promo " + promoId);
						} else {
							// if there is an id the object already exists
							System.out.println("update " + classeName + " (" + classeId + ")" + " with pathway " + pathwayId + " and promo " + promoId);
						}
					} else if ("delete".equals(action) && classeId != null && !classeId.isEmpty()) {
						System.out.println("delete " + classeName + " (" + classeId + ")");
					} else {
						request.getRequestDispatcher("views/error.jsp").forward(request, response);
					}
				}
				request.getRequestDispatcher("views/classes.jsp").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("views/error.jsp").forward(request, response);
		}

	}
}