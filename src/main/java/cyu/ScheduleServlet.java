package cyu;

import com.google.gson.Gson;
import cyu.schoolmanager.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ScheduleServlet", urlPatterns = "/schedule/get")
public class ScheduleServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);

		if (session == null) {
			// renvoyer erreur
		}

		String role = (String) session.getAttribute("role");
		String personNumber = (String) session.getAttribute("user.personNumber");

		if (role == null || personNumber == null) {
			// renvoyer erreur
		}

		String yearParam = request.getParameter("year");
		String monthParam = request.getParameter("month");
		String dayParam = request.getParameter("day");
		String id = request.getParameter("id");

		if (id == null || yearParam == null || monthParam == null || dayParam == null) {
			// renvoyer erreur
		}

		int year = Integer.parseInt(yearParam);
		int week = Integer.parseInt(monthParam);
		int day = Integer.parseInt(dayParam);

		if (role.equals(Student.class.getName()) && personNumber != id) {
			// renvoyer erreur
		} else if (role.equals(Professor.class.getName()) && personNumber != id) {
			// renvoyer erreur
		} else if (!role.equals(Admin.class.getName()) && !role.equals(Professor.class.getName()) && !role.equals(Student.class.getName())) {
			// renvoyer erreur
		}

		// recuperer les dates des 5 jours basé sur le dernier lundi
		// faire la requete des cours


		Map<Integer, List<Course>> schedule = Map.of();

		// Convertir les données en JSON
		Gson gson = new Gson();
		String json = gson.toJson(schedule);

		// Configurer la réponse
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}