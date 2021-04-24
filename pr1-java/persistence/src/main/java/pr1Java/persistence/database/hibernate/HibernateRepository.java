package pr1Java.persistence.database.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public abstract class HibernateRepository {
    protected static SessionFactory sessionFactory = null;

    public HibernateRepository() {
        try {
            if (sessionFactory == null)
                init();
        } catch (Exception e) {
            System.err.println("Error initializing session factory " + e);
            e.printStackTrace();
        } finally {
            if (sessionFactory != null)
                sessionFactory.close();
        }
    }

    private void init() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Error building session factory "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }
}
