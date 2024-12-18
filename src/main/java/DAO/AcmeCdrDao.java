/**
* Name: AcmeCdrDao
* Author: Felix Neutal
* Description: Provides methods for saving parsed acme cdr data to database.
 */
package DAO;

import DTO.AcmeCdr;
import DTO.Cdr;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.exception.DataException;

public class AcmeCdrDao {
    SessionFactory sessionFactory = null;

    /**
     * Constructor is responsible for creating sessionFactory.
     */
    public AcmeCdrDao() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
        try {
            sessionFactory = new MetadataSources(registry).
                    addAnnotatedClass(AcmeCdr.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            System.out.println("\n\nERRORRRRR!!!!\n\n");
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public List<AcmeCdr> getAllRowsByDate(String startDate, String endDate) {
        Transaction transaction = null;
        List cdrList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<AcmeCdr> cq = cb.createQuery(AcmeCdr.class);
            Root<AcmeCdr> cdr = cq.from(AcmeCdr.class);
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.plusDays(1).atStartOfDay(); // to include the end date fully
            Predicate datePredicate = cb.between(cdr.get("start_time"), startDateTime, endDateTime);
            cq.select(cdr).where(datePredicate);
            cq.orderBy(cb.asc(cdr.get("start_time")));
            //cq.multiselect(cdr).where(datePredicate);
            jakarta.persistence.Query query = session.createQuery(cq);
            cdrList = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
            e.printStackTrace();
        }
        return cdrList;
    }

    public List<AcmeCdr> getAllRows() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<AcmeCdr> cq = cb.createQuery(AcmeCdr.class);
        Root<AcmeCdr> rootEntry = cq.from(AcmeCdr.class);
        CriteriaQuery<AcmeCdr> all = cq.select(rootEntry);

        TypedQuery<AcmeCdr> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }

    /**
     * Creates session and saves parsed data to database.
     * @param data  List of Cdr
     */
    public void saveData(List<AcmeCdr> data) {
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
    public void saveBatchData(List<AcmeCdr> data) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            for (int i = 0; i < data.size(); i++) {
		        session.save(data.get(i));
                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }
		//Sometimes referred to as Pokémon Exception Handling from the TV's catchphrase: Gotta catch em all.
            }
		session.flush();
		session.clear();
            tx.commit();
        }
    }

    public void updateBatchData(List<AcmeCdr> data) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            for (int i = 0; i < data.size(); i++) {
                session.merge(data.get(i));
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
}
