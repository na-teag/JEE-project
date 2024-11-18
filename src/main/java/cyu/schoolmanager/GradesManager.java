package cyu.schoolmanager;

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
            return "La note a été enregistrée avec succès.";
        } catch (Exception e) {
            return "Erreur lors de l'enregistrement de la note : " + e.getMessage();
        }

    }

    public void modifyGrade(Grade grade, String context, String comment, int session, double result){
        grade.setContext(context);
        grade.setComment(comment);
        grade.setSession(session);
        grade.setResult(result);
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
}
