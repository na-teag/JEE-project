package cyu.schoolmanager.service;

import cyu.schoolmanager.Course;
import cyu.schoolmanager.Grade;
import cyu.schoolmanager.HibernateUtil;
import cyu.schoolmanager.Student;
import jakarta.persistence.NoResultException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

public class GradesManager{
    private static GradesManager instance;

    private GradesManager() {}

    public static synchronized GradesManager getInstance() {
        if (instance == null) {
            instance = new GradesManager();
        }
        return instance;
    }

    public String createGrade(Grade grade){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Valider l'objet Grade
        Set<ConstraintViolation<Grade>> violations = validator.validate(grade);

        // Si des violations sont présentes, renvoyer les erreurs
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<Grade> violation : violations) {
                errorMessages.append(violation.getMessage()).append(" ");
            }
            return errorMessages.toString(); // Retourne les erreurs de validation
        }

        // Si la validation est réussie, procéder à l'enregistrement dans la base de données
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.merge(grade);
            session.getTransaction().commit();
            session.close();
            MailManager mailManager = MailManager.getInstance();
            mailManager.sendEmail("do.not.reply@cytech.fr", grade.getStudent(), "Nouvelle note diponible", "Bonjour, Vous recevez cet email car une nouvelle note concernant la matière " + grade.getCourse().getSubject().getName() + ".\nConsultez votre note sur l'ENT.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail");
            return "La note a été enregistrée avec succès.";
        } catch (Exception e) {
            return "Erreur lors de l'enregistrement de la note : " + e.getMessage();
        }

    }

    public String modifyGrade(Grade grade, String context, String comment, int session, double result) {
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        hibernateSession.beginTransaction();

        try {
            // Modifier les propriétés de l'objet Grade
            grade.setContext(context);
            grade.setComment(comment);
            grade.setSession(session);
            grade.setResult(result);

            // Mettre à jour l'objet Grade dans la base de données
            hibernateSession.merge(grade);

            // Valider la transaction
            hibernateSession.getTransaction().commit();
            hibernateSession.close();
            return "La note a été modifiée avec succès.";
        } catch (Exception e) {
            return "Erreur lors de l'enregistrement de la note : " + e.getMessage();
        }
    }

    public void deleteGrade(Grade grade) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "DELETE FROM Grade g WHERE g = :grade";
        Query<?> query = session.createQuery(hql);
        query.setParameter("grade", grade);

        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }


    //Méthode pour récupérer les notes de l'étudiant
    public List<Grade> getGradesForStudent(Student student){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "FROM Grade g JOIN FETCH g.course WHERE g.student = :student";
        Query<Grade> query = session.createQuery(hql, Grade.class);
        query.setParameter("student", student);

        List<Grade> grades = query.getResultList();
        session.getTransaction().commit();
        return grades;
    }


    //Méthode pour récupérer la note de l'étudiant pour un cours donné
    public Grade getGradeForStudentAndForOneCourse(Student student, Course course){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Grade grade = null;

        try {

            // Requête HQL pour récupérer la note d'un étudiant pour un cours spécifique
            String hql = "FROM Grade g JOIN FETCH g.course WHERE g.student = :student AND g.course = :course";
            Query<Grade> query = session.createQuery(hql, Grade.class);
            query.setParameter("student", student);
            query.setParameter("course", course);

            // Récupérer la note
            grade = query.uniqueResult();

            // Valider la transaction
            session.getTransaction().commit();
        } catch (NoResultException e) {
            // Gérer le cas où aucune note n'est trouvée
            System.out.println("Aucune note trouvée pour cet étudiant et ce cours.");
        }
        session.close();
        return grade;
    }

}
