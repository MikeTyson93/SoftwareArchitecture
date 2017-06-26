package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Created by Kuba on 24.04.2017.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    static {
        final AnnotationConfiguration cfg = new
                AnnotationConfiguration();
                cfg.configure("de/htwg/se/ws1516/fourwinning/hibernate.cfg.xml");
                sessionFactory = cfg.buildSessionFactory();
    }
    private HibernateUtil() {
    }
    public static SessionFactory getInstance() {
        return sessionFactory;
    }
}
