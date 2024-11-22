package cyu;

import cyu.schoolmanager.*;
import cyu.schoolmanager.service.CourseManager;
import cyu.schoolmanager.service.PersonManager;
import cyu.schoolmanager.service.StudentGroupManager;
import cyu.schoolmanager.service.SubjectManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "CourseServlet", urlPatterns = {"/courses", "/course"})
public class CourseServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String path = request.getServletPath();
		CourseManager courseManager = CourseManager.getInstance();
		SubjectManager subjectManager = SubjectManager.getInstance();
		PersonManager personManager = PersonManager.getInstance();
		StudentGroupManager studentGroupManager = StudentGroupManager.getInstance();

		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
			if ("/courses".equals(path)) {
				session.setAttribute("courses", courseManager.getListOfCourses());
				session.setAttribute("subjects", subjectManager.getListOfSubject());
				session.setAttribute("professors", personManager.getListOfProfessors());
				session.setAttribute("groups", studentGroupManager.getListOfStudentGroups());
				request.getRequestDispatcher("views/course.jsp").forward(request, response);
			} else if ("/course".equals(path)) {
				String action = request.getParameter("action");
				String classroom = request.getParameter("classroom");
				String professorId = request.getParameter("professorId");
				String subjectId = request.getParameter("subjectId");
				String id = request.getParameter("id");
				String[] studentGroupsId = request.getParameterValues("groupsId");

				if (classroom != null && professorId != null && subjectId != null && studentGroupsId != null) {
					List<String> studentGroupsIdList = Arrays.asList(studentGroupsId);
					if ("save".equals(action)) {
						if (id == null || id.isEmpty()) {
							// if there is no id, then it is a new object
							String error = courseManager.createCourse(professorId, subjectId, classroom, studentGroupsIdList);
							if (error != null && !error.isEmpty()) {
								System.out.println(error);
								request.setAttribute("errorMessage", error);
							}
						} else {
							// if there is an id the object already exists
							String error = courseManager.updateCourseById(id, professorId, subjectId, classroom, studentGroupsIdList);
							if (error != null && !error.isEmpty()) {
								request.setAttribute("errorMessage", error);
								System.out.println(error);
							}
						}
						request.getRequestDispatcher("/courses").forward(request, response);
					} else if ("delete".equals(action) && id != null && !id.isEmpty()) {
						String error = courseManager.deleteCourseById(id);
						if (error != null) {
							request.setAttribute("errorMessage", error);
						}
						request.getRequestDispatcher("/courses").forward(request, response);
					} else {
						request.setAttribute("errorMessage", "Requête non reconnue");
						request.getRequestDispatcher("/courses").forward(request, response);
					}
				}
				request.setAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
				request.getRequestDispatcher("/courses").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("views/error.jsp").forward(request, response);
		}

	}
}