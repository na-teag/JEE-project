package cyu.schoolmanager.service;
import cyu.schoolmanager.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

public class ClassCategoryManager {
    private static ClassCategoryManager instance;

    private ClassCategoryManager() {}

    public static synchronized ClassCategoryManager getInstance() {
        if (instance == null) {
            instance = new ClassCategoryManager();
        }
        return instance;
    }

    public List<ClassCategory> getListOfPathways() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String request = "FROM ClassCategory ";
            Query<ClassCategory> query = session.createQuery(request, ClassCategory.class);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    ClassCategory getClassCategoryById(String id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String request = "FROM ClassCategory WHERE id = :id";
            Query<ClassCategory> query = session.createQuery(request, ClassCategory.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public String createClassCategory(String name, String color){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            Transaction transaction = session.beginTransaction();
            ClassCategory classCategory = new ClassCategory();
            classCategory.setName(name);
            classCategory.setColor(color);
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<ClassCategory>> errors = validator.validate(classCategory);
            if (errors.isEmpty()) {
                session.save(classCategory);
                transaction.commit();
                return null;
            }
            String errorString = "";
            for (ConstraintViolation<ClassCategory> error : errors) {
                errorString += error.getMessage() + "\n";
            }
            return errorString;
        } catch (Exception e){
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            return e.getMessage();
        } finally {
            session.close();
        }
    }

    public String updateClassCategoryById(String id, String name, String color){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction transaction = session.beginTransaction();
            ClassCategory classCategory = getClassCategoryById(id);
            classCategory.setName(name);
            classCategory.setColor(color);
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<ClassCategory>> errors = validator.validate(classCategory);
            if (errors.isEmpty()) {
                session.update(classCategory);
                transaction.commit();
                return null;
            }
            String errorString = "";
            for (ConstraintViolation<ClassCategory> error : errors) {
                errorString += error.getMessage() + "\n";
            }
            return errorString;
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            return e.getMessage();
        } finally {
            session.close();
        }
    }

    public String deleteClassCategoryById(String id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            String hql = "DELETE FROM ClassCategory g WHERE id = :id";
            Query<?> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e){
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
}
