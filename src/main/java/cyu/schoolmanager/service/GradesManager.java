package cyu.schoolmanager.service;

import cyu.schoolmanager.*;
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
            return errorMessages.toString();
        }

        // Si la validation est réussie, procéder à l'enregistrement dans la base de données
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.merge(grade);
            session.getTransaction().commit();
            return "La note a été enregistrée avec succès.";
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            return "Erreur lors de l'enregistrement de la note : " + e.getMessage();
        } finally {
            session.close();
        }
    }

    public String updateGrade(Grade grade, String context, String comment, int gradeSession, double result){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            if (grade == null) {
                throw new IllegalArgumentException("grade ne peut pas être null");
            }
            session.beginTransaction();
            grade.setContext(context);
            grade.setComment(comment);
            grade.setSession(gradeSession);
            grade.setResult(result);
            session.update(grade);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            return e.getMessage();
        } finally {
            session.close();
        }
        return null;
    }

    public String deleteGrade(Grade grade) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            if (grade == null) {
                throw new IllegalArgumentException("grade ne peut pas être null");
            }
            session.beginTransaction();
            String hql = "DELETE FROM Grade g WHERE g = :grade";
            Query<?> query = session.createQuery(hql);
            query.setParameter("grade", grade);

            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            return e.getMessage();
        } finally {
            session.close();
        }
        return null;
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
