package cyu;

import cyu.schoolmanager.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.query.PathException;
import org.hibernate.query.Query;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@WebServlet(name = "GradesServlet", urlPatterns = {"/grades", "/gradesManagement"})
public class GradesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getServletPath();

        if ("/grades".equals(action)) {
            // On vérifie que la personne est connecté et que c'est un étudiant
            if (session.getAttribute("user") != null && session.getAttribute("role").equals(Student.class.getName())) {
                Student student = (Student) session.getAttribute("user");
                List<Grade> grades = GradesManager.getInstance().getGradesForStudent(student);
                //Si l'étudiant a des notes, on calcule la moyenne de celle-ci
                if (!grades.isEmpty()) {
                    double average = getAverageForStudent(grades);
                    session.setAttribute("average", average);
                }

                CourseManager courseManager = CourseManager.getInstance();
                List<Course> courses = courseManager.getCourseOfStudent(student);
                session.setAttribute("courses", courses);
                session.setAttribute("grades", grades);
                request.getRequestDispatcher("views/grades.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("views/error.jsp").forward(request, response);
            }
        } else {
            if (session.getAttribute("user") != null && session.getAttribute("role").equals(Professor.class.getName())) {
                Professor professor = (Professor) session.getAttribute("user");
                /*if (action == null || action.equals("get")) {
                    List<Course> coursesList = CourseManager.getInstance().getCoursesOfProfessor(professor); //récup les cours que le prof enseigne
                    session.setAttribute("coursesList", coursesList);
                    request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
                } else if (action.equals("afficherClasses")) {
                    Course course = (Course) request.getAttribute("coursesList");
                    List<StudentGroup> classesList = course.getStudentGroups(); //récup les classes
                    session.setAttribute("classesList", classesList);
                    request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
                } else if (action.equals("afficherEtudiants")) {
                    StudentGroup etudiantsList = (StudentGroup) request.getAttribute("classesList");  //récup les étudiants
                    session.setAttribute("studentsList", etudiantsList);
                    request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
                }*/
                String coursesParam = request.getParameter("courses");
                if (coursesParam != null && !coursesParam.isEmpty()) {
                    // Récupérer les cours selon le paramètre "courses"
                    Course course = getSelectedCourse(coursesParam);
                    session.setAttribute("courses", course);
                    session.setAttribute("coursesId", course.getId());
                    List<StudentGroup> classesList = course.getStudentGroups();
                    session.setAttribute("classesList", classesList);
                    request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);

                } else {
                    // Si le paramètre "courses" est vide ou inexistant, on vérifie le paramètre "classes"
                    String classesParam = request.getParameter("classes");
                    if (classesParam != null && !classesParam.isEmpty()) {
                        // On pourrait récupérer les étudiants dans cette classe ici
                        StudentGroup studentGroup = getSelectedClasse(classesParam);
                        session.setAttribute("classes", studentGroup);
                        session.setAttribute("classesId", studentGroup.getId());
                        if (studentGroup instanceof Classe) {
                            List<Student> students = getSelectedStudentsForClass((Classe) studentGroup);
                            session.setAttribute("studentsList", students);
                        } else if (studentGroup instanceof Promo) {
                            List<Student> students = getSelectedStudentsForPromo((Promo) studentGroup);
                            session.setAttribute("studentsList", students);
                        } else if (studentGroup instanceof Pathway) {
                            List<Student> students = getSelectedStudentsForPathway((Pathway) studentGroup);
                            session.setAttribute("studentsList", students);
                        }
                        request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
                    } else {
                        // Si aucun des deux paramètres n'est défini, afficher la liste des cours
                        List<Course> coursesList = CourseManager.getInstance().getCoursesOfProfessor(professor);
                        session.setAttribute("coursesList", coursesList);
                        request.getRequestDispatcher("views/gradesManagement.jsp").forward(request, response);
                    }
                }
            } else {
                request.getRequestDispatcher("views/error.jsp").forward(request, response);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Récupérer l'étudiant sélectionné et la note
        String studentId = request.getParameter("students");
        String gradeParam = request.getParameter("grade");
        String contextParam = request.getParameter("context");
        String commentParam = request.getParameter("comment");
        String sessionParam = request.getParameter("session");

        if (studentId != null && !studentId.isEmpty() && gradeParam != null && !gradeParam.isEmpty()) {
            try {
                double result = Double.parseDouble(gradeParam);
                Student selectedStudent = (Student) getStudentById(studentId);
                Course selectedCourse = (Course) session.getAttribute("courses");

                // Enregistrer la note pour l'étudiant sélectionné dans le cours et la classe
                GradesManager gradesManager = GradesManager.getInstance();
                Grade grade = new Grade();
                grade.setStudent(selectedStudent);
                grade.setCourse(selectedCourse);
                grade.setResult(result);
                grade.setContext(contextParam);
                grade.setComment(commentParam);
                grade.setSession(Integer.parseInt(sessionParam));
                gradesManager.createGrade(grade);

                // Rediriger vers la page de confirmation ou rafraîchir les informations
                session.setAttribute("message", "Note enregistrée avec succès.");
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
    }


    private double getAverageForStudent(List<Grade> grades) {
        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getResult();
        }
        return sum / grades.size();
    }


    private Course getSelectedCourse(String id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "FROM Course c WHERE c.id = :id";
        Query<Course> query = session.createQuery(hql, Course.class);
        query.setParameter("id", id);

        // Exécution de la requête et récupération des résultats
        Course course = query.getSingleResult();

        // Commit de la transaction et fermeture de la session
        session.getTransaction().commit();
        return course;
    }

    private StudentGroup getSelectedClasse(String id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "FROM StudentGroup s WHERE s.id = :id";
        Query<StudentGroup> query = session.createQuery(hql, StudentGroup.class);
        query.setParameter("id", id);

        // Exécution de la requête et récupération des résultats
        StudentGroup classe = query.getSingleResult();

        // Commit de la transaction et fermeture de la session
        session.getTransaction().commit();
        return classe;
    }

    private List<Student> getSelectedStudentsForClass(Classe classe) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "FROM Student s WHERE s.classe.id = :classeId";
        Query<Student> query = session.createQuery(hql, Student.class);
        query.setParameter("classeId", classe.getId());
        List<Student> students = query.getResultList();

        session.getTransaction().commit();

        return students;
    }

    private List<Student> getSelectedStudentsForPathway(Pathway pathway) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "FROM Student s WHERE s.classe.pathway.id = :pathwayId";
        Query<Student> query = session.createQuery(hql, Student.class);
        query.setParameter("pathwayId", pathway.getId());
        List<Student> students = query.getResultList();

        session.getTransaction().commit();

        return students;
    }

    private List<Student> getSelectedStudentsForPromo(Promo promo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "FROM Student s WHERE s.classe.promo.id = :promoId";
        Query<Student> query = session.createQuery(hql, Student.class);
        query.setParameter("promoId", promo.getId());
        List<Student> students = query.getResultList();

        session.getTransaction().commit();

        return students;
    }

    private Student getStudentById(String id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String request = "FROM Student s WHERE id = :id";
        Query<Student> query = session.createQuery(request, Student.class);
        query.setParameter("id", id);

        return (Student) query.getSingleResult();
    }
}