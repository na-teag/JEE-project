package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Créez une nouvelle adresse
            Address address = new Address();
            address.setNumber("1");
            address.setStreet("rue Lebon");
            address.setCity("Cergy");
            address.setPostalCode(95000);
            address.setCountry("France");
            session.persist(address);

            // Créez un nouvel utilisateur
            Admin admin = new Admin();
            admin.setFirstName("gaetan");
            admin.setLastName("retel");
            admin.setPassword("root");
            admin.setLogin("root");
            admin.setEmailAddress("retelgaeta@cy-tech.fr");
            admin.setAddress(address);

            // Sauvegardez l'utilisateur dans la base
            session.persist(admin);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.shutdown();
        }
    }
}
