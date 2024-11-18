package cyu;

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
import java.util.*;

@WebServlet(name = "ScheduleServlet", urlPatterns = "/schedule")
public class ScheduleServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		// check the user authenticated
		if (session == null) {
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
			return;
		}
		String role = (String) session.getAttribute("role");
		if (role == null || session.getAttribute("user") == null) {
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
			return;
		}

		// check he has the right to get what he's asking for
		String id = request.getParameter("id");
		if (role.equals(Student.class.getName()) || role.equals(Professor.class.getName())) {
			Person user = (Person) session.getAttribute("user");
			if (id == null) { id = user.getPersonNumber(); } // if no schedule specified, set to his own
			if (!user.getPersonNumber().equals(id)) {
				request.setAttribute("errorMessage", "Vous n'avez pas l'autorisation d'accéder à cette ressource");
				request.getRequestDispatcher("views/schedule.jsp").forward(request, response);
				return;
			}
		} else if (role.equals(Admin.class.getName())) {
			Person user = (Person) session.getAttribute("user");
			if (id == null) { id = user.getPersonNumber(); }
		} else {
			request.setAttribute("errorMessage", "role inconnu, merci de vous reconnecter");
			request.getRequestDispatcher("views/login.jsp").forward(request, response);
			return;
		}

		String yearParam = request.getParameter("year");
		String monthParam = request.getParameter("month");
		String dayParam = request.getParameter("day");
		if (yearParam == null && monthParam == null && dayParam == null) {
			dayParam = String.format("%02d", LocalDate.now().getDayOfMonth()) ;
			monthParam = String.format("%02d", LocalDate.now().getMonthValue());
			yearParam = String.format("%04d", LocalDate.now().getYear());
		}else if (yearParam == null || monthParam == null || dayParam == null) {
			request.setAttribute("errorMessage", "date incorrecte");
			request.getRequestDispatcher("views/schedule.jsp").forward(request, response);
			return;
		}

		int year = Integer.parseInt(yearParam);
		int month = Integer.parseInt(monthParam);
		int day = Integer.parseInt(dayParam);

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
		Map<String, List<Map<String, String>>> schedule = courseManager.getCoursesByPersonNumberAndDays(id, dates);
		Map<String, Integer> days = new LinkedHashMap<>();
		days.put(DayOfWeek.MONDAY.toString(), monday.getDayOfMonth());
		days.put(DayOfWeek.TUESDAY.toString(), monday.plusDays(1).getDayOfMonth());
		days.put(DayOfWeek.WEDNESDAY.toString(), monday.plusDays(2).getDayOfMonth());
		days.put(DayOfWeek.THURSDAY.toString(), monday.plusDays(3).getDayOfMonth());
		days.put(DayOfWeek.FRIDAY.toString(), monday.plusDays(4).getDayOfMonth());

		// Trier les jours par ordre chronologique
		List<Map.Entry<String, Integer>> sortedDays = new ArrayList<>(days.entrySet());
		sortedDays.sort(Map.Entry.comparingByValue());

		// Reconstituer le Map trié
		Map<String, Integer> sortedDaysMap = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : sortedDays) {
			sortedDaysMap.put(entry.getKey(), entry.getValue());
		}

		request.getSession().setAttribute("schedule", schedule);
		request.getSession().setAttribute("days", sortedDaysMap);
		request.getRequestDispatcher("views/schedule.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("errorMessage", "les requetes post ne sont pas prises en compte");
		request.getRequestDispatcher("views/schedule.jsp").forward(request, response);
	}
}