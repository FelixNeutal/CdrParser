package DAO;

import DTO.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class CallBillAllDao {
    SessionFactory sessionFactory = null;

    public CallBillAllDao() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
        try {
            sessionFactory = new MetadataSources(registry).
                    addAnnotatedClass(CallBillAll.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            System.out.println("\n\nERRORRRRR!!!!\n\n");
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
    public void saveData(List<CallBillAll> data) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (Cdr cdr : data) {
                session.persist(cdr);
            }
            session.getTransaction().commit();
        }
    }

    /**
     * Creates session and saves parsed data to database using batches.
     * @param data  List of Cdr
     */
    public void saveBatchData(List<CallBillAll> data) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            for (int i = 0; i < data.size(); i++) {
                session.save(data.get(i));
                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            session.flush();
            session.clear();
            tx.commit();
        }
    }

    public <T> List<T> getAllRows(Class<T> entityClass) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);

        TypedQuery<T> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }
}
