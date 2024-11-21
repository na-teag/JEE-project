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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GradesServlet", urlPatterns = {"/grades", "/gradesManagement"})
public class GradesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getServletPath();

        // Initialisation des managers
        CourseManager courseManager = CourseManager.getInstance();
        GradesManager gradesManager = GradesManager.getInstance();
        StudentGroupManager studentGroupManager = StudentGroupManager.getInstance();
        PersonManager personManager = PersonManager.getInstance();

        if ("/grades".equals(action)) {
            handleStudentGrades(session, gradesManager, courseManager, request, response);
        } else {
            handleProfessorGradesManagement(session, studentGroupManager, personManager, courseManager, gradesManager, request, response);
        }
    }

    // Gérer l'affichage des grades de l'étudiant
    private void handleStudentGrades(HttpSession session, GradesManager gradesManager, CourseManager courseManager, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (session.getAttribute("user") != null && session.getAttribute("role").equals(Student.class.getName())) {
            Student student = (Student) session.getAttribute("user");

            // Récupérer les notes de l'étudiant
            List<Grade> grades = gradesManager.getGradesForStudent(student);
            if (!grades.isEmpty()) {
                double average = getAverageForStudent(grades);
                session.setAttribute("average", average);
            }

            // Récupérer les cours associés à l'étudiant
            List<Course> courses = courseManager.getCourseOfStudent(student);
            session.setAttribute("courses", courses);
            session.setAttribute("grades", grades);

            // Rediriger vers la vue des grades
            request.getRequestDispatcher("views/grades.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("views/error.jsp").forward(request, response);
        }
    }

    // Gérer l'affichage et la gestion des grades pour un professeur
    private void handleProfessorGradesManagement(HttpSession session, StudentGroupManager studentGroupManager, PersonManager personManager, CourseManager courseManager, GradesManager gradesManager, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (session.getAttribute("user") != null && session.getAttribute("role").equals(Professor.class.getName())) {
            Professor professor = (Professor) session.getAttribute("user");

            String coursesParam = request.getParameter("courses");

            if (coursesParam != null && !coursesParam.isEmpty()) {
                handleCourseSelection(session, coursesParam, request, response);
            } else {
                handleClassSelection(session, studentGroupManager, courseManager, gradesManager, personManager, professor, request, response);
            }
        } else {
            request.getRequestDispatcher("views/error.jsp").forward(request, response);
        }
    }

    // Gérer la sélection d'un cours
    private void handleCourseSelection(HttpSession session, String coursesParam, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer le cours sélectionné
        Course course = CourseManager.getSelectedCourse(coursesParam);
        session.setAttribute("courses", course);
        session.setAttribute("coursesId", course.getId());

        session.setAttribute("classesId", null);
        session.setAttribute("classesList", null);
        session.setAttribute("studentsList", null);
        session.setAttribute("selectedStudentGrade", null);
        session.setAttribute("selectedStudentId", null);

        // Récupérer les classes associées au cours
        List<StudentGroup> classesList = course.getStudentGroups();
        session.setAttribute("classesList", classesList);


        request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
    }

    // Gérer la sélection d'une classe et des étudiants associés
    private void handleClassSelection(HttpSession session, StudentGroupManager studentGroupManager, CourseManager courseManager, GradesManager gradesManager, PersonManager personManager, Professor professor, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String classesParam = request.getParameter("classes");
        if (classesParam != null && !classesParam.isEmpty()) {
            StudentGroup studentGroup = studentGroupManager.getStudentGroupFromId(classesParam);
            session.setAttribute("classes", studentGroup);
            session.setAttribute("classesId", studentGroup.getId());

            session.setAttribute("studentsList", null);
            session.setAttribute("selectedStudentGrade", null);
            session.setAttribute("selectedStudentId", null);

            // Récupérer la liste des étudiants en fonction du groupe
            List<Student> students = getStudentsForGroup(studentGroup, personManager);
            session.setAttribute("studentsList", students);

            request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);

        } else {
            String studentId = request.getParameter("students");
            if (studentId != null && !studentId.isEmpty()) {
                handleStudentSelection(session, personManager, gradesManager, request, response);
            } else {
                // Si aucun cours ni classe n'est sélectionné, afficher la liste des cours
                session.setAttribute("coursesId", null);
                session.setAttribute("classesList", null);
                session.setAttribute("studentsList", null);
                session.setAttribute("selectedStudentGrade", null);
                session.setAttribute("selectedStudentId", null);
                List<Course> coursesList = courseManager.getCoursesOfProfessor(professor);
                session.setAttribute("coursesList", coursesList);
                request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
            }
        }
    }

    // Récupérer les étudiants pour un groupe donné (classe, promo ou parcours)
    private List<Student> getStudentsForGroup(StudentGroup studentGroup, PersonManager personManager) {
        if (studentGroup instanceof Classe) {
            return personManager.getStudentsFromClasse((Classe) studentGroup);
        } else if (studentGroup instanceof Promo) {
            return personManager.getSelectedStudentsForPromo((Promo) studentGroup);
        } else if (studentGroup instanceof Pathway) {
            return personManager.getSelectedStudentsForPathway((Pathway) studentGroup);
        }
        return new ArrayList<>();
    }

    // Gérer la sélection d'un étudiant et la récupération de sa note
    private void handleStudentSelection(HttpSession session, PersonManager personManager, GradesManager gradesManager, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("students");
        if (studentId != null && !studentId.isEmpty()) {
            Student selectedStudent = personManager.getStudentById(studentId);
            if (selectedStudent != null) {
                Grade grade = gradesManager.getGradeForStudentAndForOneCourse(selectedStudent, (Course) session.getAttribute("courses"));
                session.setAttribute("selectedStudentGrade", grade);
                session.setAttribute("selectedStudentId", studentId);
            }
        }

        request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("user") != null && session.getAttribute("role").equals(Professor.class.getName())) {
            GradesManager gradesManager = GradesManager.getInstance();
            PersonManager personManager = PersonManager.getInstance();

            // Récupérer l'étudiant sélectionné et la note
            String studentId = (String) session.getAttribute("selectedStudentId");
            String gradeParam = request.getParameter("grade");
            String contextParam = request.getParameter("context");
            String commentParam = request.getParameter("comment");
            String sessionParam = request.getParameter("session");
            Course selectedCourse = (Course) session.getAttribute("courses");

            if (studentId != null && !studentId.isEmpty() && gradeParam != null && !gradeParam.isEmpty()
                    && selectedCourse != null && sessionParam != null && !sessionParam.isEmpty()) {
                try {
                    double result = Double.parseDouble(gradeParam);
                    Student selectedStudent = personManager.getStudentById(studentId);
                    String message;

                    if (session.getAttribute("selectedStudentGrade") == null) {
                        // Enregistrer la note pour l'étudiant sélectionné dans le cours et la classe
                        Grade grade = new Grade();
                        grade.setStudent(selectedStudent);
                        grade.setCourse(selectedCourse);
                        grade.setResult(result);
                        grade.setContext(contextParam);
                        grade.setComment(commentParam);
                        grade.setSession(Integer.parseInt(sessionParam));
                        message = gradesManager.createGrade(grade);
                        session.setAttribute("selectedStudentGrade", grade);
                    } else {
                        Grade oldGrade = (Grade) session.getAttribute("selectedStudentGrade");
                        message = gradesManager.modifyGrade(oldGrade, contextParam, commentParam, Integer.parseInt(sessionParam), result);
                    }
                    // Rediriger vers la page de confirmation ou rafraîchir les informations
                    session.setAttribute("message", message);
                    request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    // Si la conversion de la note échoue, afficher un message d'erreur
                    session.setAttribute("error", "La note doit être un nombre valide.");
                    request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
                }
            } else {
                // Si l'étudiant ou la note n'est pas fourni, afficher un message d'erreur
                session.setAttribute("error", "Veuillez sélectionner un étudiant et entrer une note.");
                request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("views/error.jsp").forward(request, response);
        }
    }


    private double getAverageForStudent(List<Grade> grades) {
        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getResult();
        }
        return sum / grades.size();
    }
}