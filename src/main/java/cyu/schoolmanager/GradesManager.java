package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class GradesManager{
    private static GradesManager instance;

    private GradesManager() {}

    public static synchronized GradesManager getInstance() {
        if (instance == null) {
            instance = new GradesManager();
        }
        return instance;
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
