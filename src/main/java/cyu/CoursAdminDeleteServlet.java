package cyu;

import cyu.schoolmanager.Admin;
import cyu.schoolmanager.CourseOccurrence;
import cyu.schoolmanager.service.ScheduleManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "adminDeleteCourse", urlPatterns = {"/adminDeleteCourse"})
public class CoursAdminDeleteServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ScheduleManager scheduleManager = ScheduleManager.getInstance();
		String deleteId = request.getParameter("deleteId");


		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {
			if (deleteId != null) {
				String error = scheduleManager.deleteCourseOccurrenceById(deleteId);
				if (error != null) {
					request.setAttribute("errorMessage", error);
				}
			}

			List<CourseOccurrence> occurrences = scheduleManager.getListOfCourseOccurrence();
			request.setAttribute("occurrences", occurrences);

			request.getRequestDispatcher("WEB-INF/views/adminCoursDelete.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("WEB-INF/views/error.jsp").forward(request, response);
		}
	}
}
