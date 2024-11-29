package cyu;

import cyu.schoolmanager.*;
import cyu.schoolmanager.service.PromoManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "PromoServlet", urlPatterns = {"/promos", "/promo"})
public class PromoServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String path = request.getServletPath();
		PromoManager promoManager = PromoManager.getInstance();

		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
			if ("/promos".equals(path)) {
				session.setAttribute("promos", promoManager.getListOfPromos());
				request.getRequestDispatcher("WEB-INF/views/promos.jsp").forward(request, response);
			} else if ("/promo".equals(path)) {
				String action = request.getParameter("action");
				String name = request.getParameter("name");
				String email = request.getParameter("email");
				String id = request.getParameter("id");

				if (name != null && email != null) {
					if ("save".equals(action)) {
						if (id == null || id.isEmpty()) {
							// if there is no id, then it is a new object
							String error = promoManager.createPromo(name, email);
							if (error != null && !error.isEmpty()) {
								System.out.println(error);
								request.setAttribute("errorMessage", error);
							}
						} else {
							// if there is an id the object already exists
							String error = promoManager.updatePromoById(id, name, email);
							if (error != null && !error.isEmpty()) {
								request.setAttribute("errorMessage", error);
								System.out.println(error);
							}
						}
						request.getRequestDispatcher("/promos").forward(request, response);
					} else if ("delete".equals(action) && id != null && !id.isEmpty()) {
						String error = promoManager.deletePromoById(id);
						if (error != null) {
							request.setAttribute("errorMessage", error);
						}
						request.getRequestDispatcher("/promos").forward(request, response);
					} else {
						request.setAttribute("errorMessage", "Requête non reconnue");
						request.getRequestDispatcher("/promos").forward(request, response);
					}
				}
				request.setAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
				request.getRequestDispatcher("/promos").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("WEB-INF/views/error.jsp").forward(request, response);
		}

	}
}