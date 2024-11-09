package org.openjfx.projet_jee.schoolmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {

    public static void main(String[] args) {
        // Configuration de Hibernate
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml") // Charge la configuration
                .buildSessionFactory();


        // Ouverture d'une session
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            // Démarrer une transaction
            transaction = session.beginTransaction();

            /*
            // Créer un nouvel étudiant
            Student newStudent = new Student("Dupont", "Jean", "2000-01-01", "0600000000");
            session.save(newStudent); // Sauvegarde l'étudiant dans la base de données

            */
            // Commit de la transaction
            transaction.commit();
        } catch (Exception e) {
           if (transaction != null) {
                transaction.rollback(); // Rollback en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            session.close(); // Ferme la session
            sessionFactory.close(); // Ferme le session factory
        }
    }
}
