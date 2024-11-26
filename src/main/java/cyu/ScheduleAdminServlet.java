package cyu;

import cyu.schoolmanager.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "scheduleAdmin", urlPatterns = {"/scheduleAdmin"})
public class ScheduleAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            List<Student> eleves = session.createQuery("FROM Student", Student.class).getResultList();
            List<Professor> professeurs = session.createQuery("FROM Professor", Professor.class).getResultList();
            List<StudentGroup> groupes = session.createQuery("FROM StudentGroup", StudentGroup.class).getResultList();
            List<ClassCategory> categories = session.createQuery("FROM ClassCategory", ClassCategory.class).getResultList();

            Map<Long, List<Subject>> professeurSubjectsMap = new HashMap<>();
            for (Professor professeur : professeurs) {
                professeurSubjectsMap.put(professeur.getId(), professeur.getTeachingSubjects());
            }

            request.setAttribute("eleves", eleves);
            request.setAttribute("professeurs", professeurs);
            request.setAttribute("groupes", groupes);
            request.setAttribute("categories", categories);
            request.setAttribute("professeurSubjectsMap", professeurSubjectsMap);

            tx.commit();
        }

        String classroom = request.getParameter("classroom");
        String dayStr = request.getParameter("day");
        String beginningStr = request.getParameter("beginning");
        String endStr = request.getParameter("end");
        String categoryIdStr = request.getParameter("category");
        String professorIdStr = request.getParameter("professeur");
        String subjectIdStr = request.getParameter("subject");

        if (classroom != null && dayStr != null && beginningStr != null && endStr != null && categoryIdStr != null && professorIdStr != null && subjectIdStr != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

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

                    Long professorId = Long.parseLong(professorIdStr);
                    Long subjectId = Long.parseLong(subjectIdStr);
                    Long categoryId = Long.parseLong(categoryIdStr);

                    Professor professeur = session.get(Professor.class, professorId);
                    Subject subject = session.get(Subject.class, subjectId);
                    ClassCategory category = session.get(ClassCategory.class, categoryId);

                    if (professeur == null || subject == null || category == null) {
                        throw new IllegalArgumentException("Professeur, matière ou catégorie invalide.");
                    }

                    if (!professeur.getTeachingSubjects().contains(subject)) {
                        throw new IllegalArgumentException("Le professeur sélectionné ne peut pas enseigner cette matière.");
                    }

                    // Création ou récupération du cours
                    String hql = "FROM Course WHERE professor.id = :professorId AND subject.id = :subjectId AND classroom = :classroom";
                    Course course = session.createQuery(hql, Course.class)
                            .setParameter("professorId", professorId)
                            .setParameter("subjectId", subjectId)
                            .setParameter("classroom", classroom)
                            .uniqueResult();

                    if (course == null) {
                        course = new Course();
                        course.setProfessor(professeur);
                        course.setSubject(subject);
                        course.setClassroom(classroom);
                        session.persist(course);
                    }

                    CourseOccurence occurence = new CourseOccurence();
                    occurence.setCourse(course);
                    occurence.setClassroom(classroom);
                    occurence.setDay(day);
                    occurence.setBeginning(beginning);
                    occurence.setEnd(end);
                    occurence.setProfessor(professeur);
                    occurence.setCategory(category);

                    session.persist(occurence);

                    transaction.commit();
                    request.getRequestDispatcher("views/admin-schedule.jsp").forward(request, response);

                } catch (Exception e) {
                    transaction.rollback();
                    e.printStackTrace();
                    request.getRequestDispatcher("views/admin-schedule.jsp").forward(request, response);
                }
            }
        } else {
            request.getRequestDispatcher("views/admin-schedule.jsp").forward(request, response);
        }
    }
}
