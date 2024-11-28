package cyu;

import cyu.schoolmanager.CourseOccurence;
import cyu.schoolmanager.service.ScheduleManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "admindeletecours", urlPatterns = {"/admindeletecours"})
public class CoursAdminDeleteServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ScheduleManager scheduleManager = ScheduleManager.getInstance();
		String deleteId = request.getParameter("deleteId");

		if (deleteId != null) {
			String error = scheduleManager.deleteCourseOccurrenceById(deleteId);
			if (error != null) {
				request.setAttribute("errorMessage", error);
			}
		}


		List<CourseOccurence> occurences = scheduleManager.getListOfCourseOccurrence();
		request.setAttribute("occurences", occurences);

		request.getRequestDispatcher("views/adminCoursDelete.jsp").forward(request, response);
	}
}
