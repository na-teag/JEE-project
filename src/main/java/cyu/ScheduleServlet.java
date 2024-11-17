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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ScheduleServlet", urlPatterns = "/schedule/get")
public class ScheduleServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession(false);

		if (session == null) {
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
			return;
		}

		String role = (String) session.getAttribute("role");
		String personNumber = (String) session.getAttribute("user.personNumber");

		if (role == null || personNumber == null) {
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
			return;
		}

		String yearParam = request.getParameter("year");
		String monthParam = request.getParameter("month");
		String dayParam = request.getParameter("day");
		String id = request.getParameter("id");

		if (id == null) { id = personNumber; }

		if (yearParam == null || monthParam == null || dayParam == null) {
			request.setAttribute("errorMessage", "aucune date spécifiée");
			request.getRequestDispatcher("views/schedule.jsp").forward(request, response);
			return;
		}

		int year = Integer.parseInt(yearParam);
		int month = Integer.parseInt(monthParam);
		int day = Integer.parseInt(dayParam);

		if (( role.equals(Student.class.getName()) || role.equals(Professor.class.getName()) ) && !personNumber.equals(id)) {
			request.setAttribute("errorMessage", "Vous n'avez pas l'autorisation d'accéder à cette ressource");
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
			return;
		} else if (!role.equals(Admin.class.getName()) && !role.equals(Professor.class.getName()) && !role.equals(Student.class.getName())) {
			request.setAttribute("errorMessage", "role inconnu, merci de vous reconnecter");
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
			return;
		}

		LocalDate monday;
		try {
			LocalDate date = LocalDate.of(year, month, day);

			DayOfWeek dayOfWeek = date.getDayOfWeek();
			if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
				monday = date.with(DayOfWeek.MONDAY).plusWeeks(1);
			} else {
				monday = date.with(DayOfWeek.MONDAY);
			}
		} catch (Exception e) {
			request.setAttribute("errorMessage", "la date est invalide");
			request.getRequestDispatcher("views/schedule.jsp").forward(request, response);
			return;
		}




		CourseManager courseManager = CourseManager.getInstance();
		List<LocalDate> dates = new ArrayList<>();
		for (int i=0; i<5; i++) {
			dates.add(monday.plusDays(i));
		}
		Map<Integer, List<Map<String, Object>>> schedule = courseManager.getCoursesByPersonNumberAndDays(id, dates);

		Gson gson = new Gson();
		String json = gson.toJson(schedule);

		response.setContentType("application/json");
		response.getWriter().write(json);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("errorMessage", "les requetes post ne sont pas prises en compte");
		request.getRequestDispatcher("views/schedule.jsp").forward(request, response);
	}
}