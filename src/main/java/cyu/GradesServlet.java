package cyu;

import cyu.schoolmanager.Grade;
import cyu.schoolmanager.HibernateUtil;
import cyu.schoolmanager.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GradesServlet", urlPatterns = {"/grades"})
public class GradesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // On vérifie que la personne est connecté et que c'est un étudiant
        if (session.getAttribute("user") != null && session.getAttribute("role").equals(Student.class.getName())) {
            Student student = (Student) session.getAttribute("user");
            List<Grade> grades = getGradesForStudent(student.getId());
            //Si l'étudiant a des notes, on calcule la moyenne de celle-ci
            if(!grades.isEmpty()){
                double average = getAverageForStudent(grades);
                session.setAttribute("average", average);
            }
            //List<Course> courses = student.getClasse().getCourses(); dispo dans
            //session.setAttribute("courses", courses);
            session.setAttribute("grades", grades);
            request.getRequestDispatcher("views/grades.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("views/error.jsp").forward(request, response);
        }
    }


    //Méthode pour récupérer les notes de l'étudiant
    private List<Grade> getGradesForStudent(Long studentId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "FROM Grade g JOIN FETCH g.course WHERE g.student.id = :studentId";
        Query<Grade> query = session.createQuery(hql, Grade.class);
        query.setParameter("studentId", studentId);

        // Exécution de la requête et récupération des résultats
        List<Grade> grades = query.getResultList();

        // Commit de la transaction et fermeture de la session
        session.getTransaction().commit();
        return grades;
    }

    private double getAverageForStudent(List<Grade> grades) {
        double sum = 0;
        for(Grade grade : grades){
            sum += grade.getResult();
        }
        return sum/grades.size();
    }
}