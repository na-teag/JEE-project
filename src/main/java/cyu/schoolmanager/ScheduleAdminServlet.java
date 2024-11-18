package cyu.schoolmanager;

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
import java.util.List;

@WebServlet(name = "scheduleAdmin", urlPatterns = ("/scheduleAdmin"))
public class ScheduleAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Préparer les listes pour les données
        List<String> eleves;
        List<String> professeurs;
        List<String> groupes;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Requête pour récupérer les élèves
            String elevesRequest = "SELECT CONCAT(firstName, ' ', lastName) FROM Student";
            Query<String> elevesQuery = session.createQuery(elevesRequest, String.class);
            eleves = elevesQuery.getResultList();

            // Requête pour récupérer les professeurs
            String profsRequest = "SELECT CONCAT(firstName, ' ', lastName) FROM Professor";
            Query<String> profsQuery = session.createQuery(profsRequest, String.class);
            professeurs = profsQuery.getResultList();

            // Requête pour récupérer les groupes
            String groupesRequest = "SELECT name FROM StudentGroup";
            Query<String> groupesQuery = session.createQuery(groupesRequest, String.class);
            groupes = groupesQuery.getResultList();

            // Requête pour récupérer les cours
            String coursRequest = "SELECT name FROM Subject ";
            Query<String> coursQuery = session.createQuery(coursRequest, String.class);
            List<String> cours = coursQuery.getResultList();

// Ajouter les cours à la requête
            request.setAttribute("cours", cours);

            tx.commit();
        }

        // Passer les données à la JSP
        request.setAttribute("eleves", eleves);
        request.setAttribute("professeurs", professeurs);
        request.setAttribute("groupes", groupes);

        // Redirection vers la JSP
        request.getRequestDispatcher("views/admin-admin-schedule.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Préparer les listes pour les données
        List<String> eleves;
        List<String> professeurs;
        List<String> groupes;

        // Récupérer les données depuis la base de données avec Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Requête pour récupérer les élèves
            String elevesRequest = "SELECT CONCAT(firstName, ' ', lastName) FROM Student";
            Query<String> elevesQuery = session.createQuery(elevesRequest, String.class);
            eleves = elevesQuery.getResultList();

            // Requête pour récupérer les professeurs
            String profsRequest = "SELECT CONCAT(firstName, ' ', lastName) FROM Professor";
            Query<String> profsQuery = session.createQuery(profsRequest, String.class);
            professeurs = profsQuery.getResultList();

            // Requête pour récupérer les groupes
            String groupesRequest = "SELECT name FROM StudentGroup";
            Query<String> groupesQuery = session.createQuery(groupesRequest, String.class);
            groupes = groupesQuery.getResultList();

            // Requête pour récupérer les cours
            String coursRequest = "SELECT subject FROM Course";
            Query<String> coursQuery = session.createQuery(coursRequest, String.class);
            List<String> cours = coursQuery.getResultList();

// Ajouter les cours à la requête
            request.setAttribute("cours", cours);


            tx.commit();
        }

        // Passer les données à la JSP
        request.setAttribute("eleves", eleves);
        request.setAttribute("professeurs", professeurs);
        request.setAttribute("groupes", groupes);

        // Redirection vers la JSP
        request.getRequestDispatcher("views/admin-admin-schedule.jsp").forward(request, response);
    }


}
