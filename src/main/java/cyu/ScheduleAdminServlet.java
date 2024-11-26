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

@WebServlet(name = "scheduleAdmin", urlPatterns = {"/scheduleAdmin"})
public class ScheduleAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            List<Student> eleves = session.createQuery("FROM Student", Student.class).getResultList();
            List<Professor> professeurs = session.createQuery("FROM Professor", Professor.class).getResultList();
            List<StudentGroup> groupes = session.createQuery("FROM StudentGroup", StudentGroup.class).getResultList();
            //List<Subject> cours = session.createQuery("FROM Subject", Subject.class).getResultList();
            List<ClassCategory> categories = session.createQuery("FROM ClassCategory", ClassCategory.class).getResultList();

            Map<Long, List<Subject>> professeurSubjectsMap = new HashMap<>();
            for (Professor professeur : professeurs) {
                professeurSubjectsMap.put(professeur.getId(), professeur.getTeachingSubjects());
            }

            request.setAttribute("eleves", eleves);
            request.setAttribute("professeurs", professeurs);
            request.setAttribute("groupes", groupes);
            //request.setAttribute("cours", cours);
            request.setAttribute("categories", categories);
            request.setAttribute("professeurSubjectsMap", professeurSubjectsMap);

            tx.commit();
        }
        request.getRequestDispatcher("views/admin-schedule.jsp").forward(request, response);

        String classroom = request.getParameter("classroom");
            String dayStr = request.getParameter("day");
            String beginningStr = request.getParameter("beginning");
            String endStr = request.getParameter("end");
            String categoryIdStr = request.getParameter("category");
            String[] assignments = request.getParameterValues("assignments");


        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                // Validation des champs requis
                if (classroom == null || dayStr == null || beginningStr == null || endStr == null || categoryIdStr == null || assignments == null) {
                    throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
                }

                LocalDate day = LocalDate.parse(dayStr);
                LocalTime beginning = LocalTime.parse(beginningStr);
                LocalTime end = LocalTime.parse(endStr);

                // 1. Vérification que le jour n'est pas un samedi ou un dimanche
                if (day.getDayOfWeek().getValue() == 6 || day.getDayOfWeek().getValue() == 7) {
                    throw new IllegalArgumentException("Les cours ne peuvent pas être planifiés un samedi ou un dimanche.");
                }

                // 2. Vérification des horaires
                if (beginning.isBefore(LocalTime.of(8, 0)) || end.isAfter(LocalTime.of(20, 0))) {
                    throw new IllegalArgumentException("Les cours doivent se dérouler entre 08h00 et 20h00.");
                }

                // Vérification que les horaires sont divisibles par 15 minutes
                if (beginning.getMinute() % 15 != 0 || end.getMinute() % 15 != 0) {
                    throw new IllegalArgumentException("Les horaires doivent être arrondis à des intervalles de 15 minutes.");
                }

                // Vérification que l'heure de début est avant l'heure de fin
                if (!beginning.isBefore(end)) {
                    throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin.");
                }

                // 3. Récupération des autres entités nécessaires
                ClassCategory category = session.get(ClassCategory.class, Long.parseLong(categoryIdStr));
                if (category == null) {
                    throw new IllegalArgumentException("Catégorie invalide.");
                }

                for (String assignment : assignments) {
                    if (!assignment.isEmpty()) {
                        String[] parts = assignment.split(",");
                        Long eleveId = null, groupeId = null, professeurId = null, coursId = null;

                        for (String part : parts) {
                            String[] keyValue = part.split(":");
                            switch (keyValue[0]) {
                                case "eleve":
                                    eleveId = Long.parseLong(keyValue[1]);
                                    break;
                                case "groupe":
                                    groupeId = Long.parseLong(keyValue[1]);
                                    break;
                                case "professeur":
                                    professeurId = Long.parseLong(keyValue[1]);
                                    break;
                                case "cours":
                                    coursId = Long.parseLong(keyValue[1]);
                                    break;
                            }
                        }

                        // Vérification que le professeur et le cours sont valides
                        Professor professeur = session.get(Professor.class, professeurId);
                        Subject cours = session.get(Subject.class, coursId);

                        if (professeur == null || cours == null) {
                            throw new IllegalArgumentException("Professeur ou cours invalide.");
                        }

                        // 4. Création ou récupération du cours
                        String hql = "FROM Course WHERE professor.id = :professorId AND subject.id = :subjectId AND classroom = :classroom";
                        Query<Course> query = session.createQuery(hql, Course.class);
                        query.setParameter("professorId", professeurId);
                        query.setParameter("subjectId", coursId);
                        query.setParameter("classroom", classroom);

                        Course existingCourse = query.uniqueResult();
                        if (existingCourse == null) {
                            Course course = new Course();
                            course.setProfessor(professeur);
                            course.setSubject(cours);
                            course.setClassroom(classroom);
                            session.persist(course);
                            existingCourse = course;
                        }

                        // 5. Ajout d'une occurrence du cours
                        CourseOccurence occurence = new CourseOccurence();
                        occurence.setCourse(existingCourse);
                        occurence.setClassroom(classroom);
                        occurence.setDay(day);
                        occurence.setBeginning(beginning);
                        occurence.setEnd(end);
                        occurence.setProfessor(professeur);
                        occurence.setCategory(category);

                        session.persist(occurence);
                    }
                }
                transaction.commit();
                response.sendRedirect("scheduleAdmin.jsp");

            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
                response.sendRedirect("scheduleAdmin.jsp");
            }
        }

    }

}
