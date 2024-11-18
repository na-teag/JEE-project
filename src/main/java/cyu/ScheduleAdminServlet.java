package cyu;

import cyu.schoolmanager.*;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "scheduleAdmin", urlPatterns = {"/scheduleAdmin"})
public class ScheduleAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Récupérer les données nécessaires
            List<String> eleves = session.createQuery("SELECT CONCAT(firstName, ' ', lastName) FROM Student", String.class).getResultList();
            List<String> professeurs = session.createQuery("SELECT CONCAT(firstName, ' ', lastName) FROM Professor", String.class).getResultList();
            List<String> groupes = session.createQuery("SELECT name FROM StudentGroup", String.class).getResultList();
            List<String> cours = session.createQuery("SELECT name FROM Subject", String.class).getResultList();
            List<String> categories = session.createQuery("SELECT name FROM ClassCategory", String.class).getResultList();

            // Ajouter les données comme attributs
            request.setAttribute("eleves", eleves);
            request.setAttribute("professeurs", professeurs);
            request.setAttribute("groupes", groupes);
            request.setAttribute("cours", cours);
            request.setAttribute("categories", categories);

            tx.commit();
        }

        // Afficher la vue JSP
        request.getRequestDispatcher("views/admin-schedule.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String classroom = request.getParameter("classroom");
        String dayStr = request.getParameter("day");
        String beginningStr = request.getParameter("beginning");
        String endStr = request.getParameter("end");
        String categoryName = request.getParameter("category");
        String[] assignments = request.getParameterValues("assignments");

        if (classroom == null || dayStr == null || beginningStr == null || endStr == null || categoryName == null || assignments == null) {
            throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                LocalDate day = LocalDate.parse(dayStr);
                LocalTime beginning = LocalTime.parse(beginningStr);
                LocalTime end = LocalTime.parse(endStr);

                // Récupérer la catégorie
                ClassCategory category = session.createQuery("FROM ClassCategory WHERE name = :name", ClassCategory.class)
                        .setParameter("name", categoryName)
                        .uniqueResult();
                if (category == null) {
                    throw new IllegalArgumentException("Catégorie invalide.");
                }

                // Traiter les assignations
                for (String assignment : assignments) {
                    // Parser la chaîne d'assignation (eleve, professeur, cours)
                    String[] parts = assignment.split(",");
                    String eleve = null;
                    String groupe = null;
                    String professeur = null;
                    String cours = null;

                    for (String part : parts) {
                        if (part.startsWith("eleve:")) {
                            eleve = part.split(":")[1];
                        } else if (part.startsWith("groupe:")) {
                            groupe = part.split(":")[1];
                        } else if (part.startsWith("professeur:")) {
                            professeur = part.split(":")[1];
                        } else if (part.startsWith("cours:")) {
                            cours = part.split(":")[1];
                        }
                    }

                    if (cours == null || professeur == null) {
                        throw new IllegalArgumentException("Données d'assignation invalides.");
                    }

                    Subject subject = new Subject();
                    subject.setName(cours);
                    // Créer le cours
                    Course course = new Course();
                    course.setSubject(subject);
                    course.setClassroom(classroom);
                    if (groupe != null) {
                        List<StudentGroup> studentGroups = new ArrayList<>();
                        course.setStudentGroups(studentGroups);
                    }

                    Professor prof = new Professor();
                    course.setProfessor(professeur);
                    session.merge(course);

                    // Créer l'occurrence du cours
                    CourseOccurence occurence = new CourseOccurence();
                    occurence.setCourse(course);
                    occurence.setClassroom(classroom);
                    occurence.setProfessor(professeur);
                    occurence.setDay(day);
                    occurence.setBeginning(beginning);
                    occurence.setEnd(end);
                    occurence.setCategory(category);

                    // Sauvegarder l'occurrence
                    session.merge(occurence);
                }

                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }

        // Rediriger vers la page d'administration
        response.sendRedirect("scheduleAdmin");
    }
}
