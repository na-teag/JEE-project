package cyu.schoolmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Address address = new Address();
            address.setNumber("1");
            address.setStreet("rue Lebon");
            address.setCity("Cergy");
            address.setPostalCode(95000);
            address.setCountry("France");
            session.persist(address);

            Admin admin = new Admin();
            admin.setFirstName("gaetan");
            admin.setLastName("retel");
            admin.setPassword("root");
            admin.setLogin("root");
            admin.setEmailAddress("retelgaeta@cy-tech.fr");
            admin.setAddress(address);
            session.persist(admin);

            //Créez une promo
            Promo promo = new Promo();
            promo.setName("ING2");
            session.persist(promo);

            //Créez une filière
            Pathway pathway = new Pathway();
            pathway.setName("GSI");
            session.persist(pathway);

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
