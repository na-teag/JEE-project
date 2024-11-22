package cyu;
import cyu.schoolmanager.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManagementServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = null;

        try {
            // Open Hibernate session
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            // Get the type parameter and handle null cases
            String type = request.getParameter("type");
            if (type == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter 'type' is required.");
                return;
            }

            String hql;
            List<?> resultList = null;

            // Determine HQL query based on 'type'
            if (type.equals("student")) {
                /*hql = "SELECT s.id, s.email, s.firstName, s.lastName, s.password, s.username, " +
                        "s.studentNumber, c.email, c.name, p.name, p.email, pr.email, pr.name " +
                        "a.city, a.country, a.number, a.postal_code, a.street" +
                        "FROM Student s " +
                        "JOIN Address a " +
                        "JOIN Classe c " +
                        "JOIN Pathway p " +
                        "JOIN Promo romo pr";*/
                hql = "from Student";
                Query<Student> query = session.createQuery(hql, Student.class);
                resultList = query.getResultList();
            }
            /*else if (type.equals("teacher")) {
                hql = "SELECT p.email, p.first_name, p.last_name, p.password, p.username, pr.status, s.nom  " +
                        "a.city, a.country, a.number, a.postal_code, a.street" +
                        "FROM Professor p " +
                        "JOIN Address a " +
                        "JOIN ProfessorStatus pr " +
                        "JOIN Subject s";
                Query<Object[]> query = session.createQuery(hql, Object[].class);
                resultList = query.getResultList();

            }*/

            else{
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Type d'utilisateur non reconnu");
            }

            // Process the result and add it to the request scope
            request.setAttribute("results", resultList);

            // Forward to the JSP page
            String page = "user.jsp";
            request.getRequestDispatcher(page).forward(request, response);

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new ServletException("Error processing request", e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
