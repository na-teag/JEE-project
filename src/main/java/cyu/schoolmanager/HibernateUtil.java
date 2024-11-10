package cyu.schoolmanager;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Créer un StandardServiceRegistry
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml") // Charge la configuration à partir de hibernate.cfg.xml
                    .build();

            // Créer le MetadataSources
            MetadataSources metadataSources = new MetadataSources(standardRegistry);

            // Créer le Metadata
            Metadata metadata = metadataSources.getMetadataBuilder().build();

            // Créer la SessionFactory
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Fermer le cache et les connexions de la SessionFactory
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}