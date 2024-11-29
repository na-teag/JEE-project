package cyu;

import cyu.schoolmanager.*;
import cyu.schoolmanager.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@WebServlet(name = "scheduleAdmin", urlPatterns = {"/scheduleAdmin"})
public class ScheduleAdminServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PersonManager personManager = PersonManager.getInstance();
		ClassCategoryManager classCategoryManager = ClassCategoryManager.getInstance();
		ScheduleManager scheduleManager = ScheduleManager.getInstance();
		CourseManager courseManager = CourseManager.getInstance();

		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {

			List<Professor> professors = personManager.getListOfProfessors();
			List<ClassCategory> categories = classCategoryManager.getListOfClassCategories();
			List<Course> courses = courseManager.getListOfCourses();

			request.setAttribute("professors", professors);
			request.setAttribute("categories", categories);
			request.setAttribute("courses", courses);


			String dayStr = request.getParameter("day");
			String beginningStr = request.getParameter("beginning");
			String endStr = request.getParameter("end");
			String courseId = request.getParameter("course");
			String classCategoryId = request.getParameter("category");
			String professorId = request.getParameter("professor");
			String classroom = request.getParameter("classroom");


			if (dayStr != null && !dayStr.isBlank() && beginningStr != null && !beginningStr.isBlank() && endStr != null && !endStr.isBlank() && classCategoryId != null && !classCategoryId.isBlank() && courseId != null && !courseId.isBlank()) {
				try {
					LocalDate day = LocalDate.parse(dayStr);
					LocalTime beginning = LocalTime.parse(beginningStr);
					LocalTime end = LocalTime.parse(endStr);

					if (day.getDayOfWeek().getValue() == 6 || day.getDayOfWeek().getValue() == 7) {
						throw new IllegalArgumentException("Les cours ne peuvent pas être planifiés un samedi ou un dimanche.");
					}
					if (beginning.isBefore(LocalTime.of(8, 0)) || end.isAfter(LocalTime.of(20, 0))) {
						throw new IllegalArgumentException("Les cours doivent se dérouler entre 08h00 et 20h00.");
					}
					if (beginning.getMinute() % 15 != 0 || end.getMinute() % 15 != 0) {
						throw new IllegalArgumentException("Les horaires doivent finir en 00, 15, 30 ou 45 minutes");
					}
					if (!beginning.isBefore(end)) {
						throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin.");
					}

					Course course = courseManager.getCourseById(courseId);
					if (course == null) {
						throw new IllegalArgumentException("Cours invalide.");
					}

					ClassCategory classCategory = classCategoryManager.getClassCategoryById(classCategoryId);
					if (classCategory == null) {
						throw new IllegalArgumentException("Catégorie invalide.");
					}

					// le professeur n'est pas obligatoire
					Professor professor = null;
					if (professorId != null && !professorId.isBlank()) {
						professor = personManager.getProfessorById(professorId);
						if (professor == null) {
							throw new IllegalArgumentException("Professeur invalide.");
						}
						// Vérifier que le professeur peut enseigner la matière spécéfiée
						List<Long> subjectIds = new ArrayList<>();
						for (Subject subject2 : professor.getTeachingSubjects()) {
							subjectIds.add(subject2.getId());
						}
						if (!subjectIds.contains(course.getSubject().getId())) {
							throw new IllegalArgumentException("Le professeur sélectionné ne peut pas enseigner cette matière.");
						}
					}

					// la salle de classe n'est pas obligatoire
					if (classroom != null && classroom.isBlank()) {
						classroom = null;
					}

					// Création de l'occurence du cours
					scheduleManager.createCourseOccurrence(professor, classCategory, course, classroom, day, beginning, end);

					request.setAttribute("successMessage", "Cours enregistré avec succès !");
					request.getRequestDispatcher("WEB-INF/views/admin-schedule.jsp").forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("errorMessage", e.getMessage());
					request.getRequestDispatcher("WEB-INF/views/admin-schedule.jsp").forward(request, response);
				}

			} else {
				request.getRequestDispatcher("WEB-INF/views/admin-schedule.jsp").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("WEB-INF/views/error.jsp").forward(request, response);
		}
	}
}
