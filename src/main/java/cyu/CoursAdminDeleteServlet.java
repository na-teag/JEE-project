package cyu;

import cyu.schoolmanager.Course;
import cyu.schoolmanager.CourseOccurence;
import cyu.schoolmanager.ClassCategory;
import cyu.schoolmanager.Student;
import cyu.schoolmanager.StudentGroup;
import cyu.schoolmanager.Subject;
import cyu.schoolmanager.Professor;
import cyu.schoolmanager.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "admindeletecours", urlPatterns = {"/admindeletecours"})
public class CoursAdminDeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String deleteId = request.getParameter("deleteId");

        if (deleteId != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();

                CourseOccurence occurence = session.get(CourseOccurence.class, Long.parseLong(deleteId));
                if (occurence != null) {
                    session.delete(occurence);
                    tx.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            List<Student> eleves = session.createQuery("FROM Student", Student.class).getResultList();
            List<Professor> professeurs = session.createQuery("FROM Professor", Professor.class).getResultList();
            List<StudentGroup> groupes = session.createQuery("FROM StudentGroup", StudentGroup.class).getResultList();
            List<ClassCategory> categories = session.createQuery("FROM ClassCategory", ClassCategory.class).getResultList();
            List<CourseOccurence> occurences = session.createQuery("FROM CourseOccurence", CourseOccurence.class).getResultList();

            request.setAttribute("eleves", eleves);
            request.setAttribute("professeurs", professeurs);
            request.setAttribute("groupes", groupes);
            request.setAttribute("categories", categories);
            request.setAttribute("occurences", occurences);

            tx.commit();
        }

        request.getRequestDispatcher("views/adminCoursDelete.jsp").forward(request, response);
    }
}
