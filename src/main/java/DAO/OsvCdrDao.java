/**
 * Name: AcmeCdrDao
 * Author: Felix Neutal
 * Description: Provides methods for saving parsed osv cdr data to database.
 */

package DAO;

import DTO.AcmeCdr;
import DTO.Cdr;
import DTO.OsvCdr;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.AssertionFailure;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.DataException;

import java.util.ArrayList;
import java.util.List;

public class OsvCdrDao {
    SessionFactory sessionFactory = null;

    /**
     * Constructor is responsible for creating sessionFactory.
     */
    public OsvCdrDao() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
        try {
            sessionFactory = new MetadataSources(registry).
                    addAnnotatedClass(OsvCdr.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            System.out.println("\n\nERRORRRRR!!!!\n\n");
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public List<OsvCdr> getAllRowsByDate(String startDate, String endDate) {
        Transaction transaction = null;
        List cdrList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OsvCdr> cq = cb.createQuery(OsvCdr.class);
            Root<OsvCdr> cdr = cq.from(OsvCdr.class);
            Predicate datePredicate = cb.between(cdr.get("start_date"), startDate, endDate);
            cq.select(cdr).where(datePredicate);
            //cq.multiselect(cdr).where(datePredicate);
            jakarta.persistence.Query query = session.createQuery(cq);
            cdrList = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return cdrList;
    }

    /**
     * Creates session and saves parsed data to database.
     * @param data  List of Cdr
     */
    public void saveData(List<OsvCdr> data) {
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
    public void saveBatchData(List<OsvCdr> data) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            for (int i = 0; i < data.size(); i++) {
                try {
                    session.persist(data.get(i));
                    //session.flush();
                    if (i % 100 == 0) {
                        session.flush();
                        session.clear();
                    }
                } catch (AssertionFailure | IllegalArgumentException e) {
                    //System.out.println("ERR<> " + data.get(i));
                    e.printStackTrace();
                }
            }
		    session.flush();
		    session.clear();
            tx.commit();
        } catch (NullPointerException e) {
		System.out.println("Error occurred in saveBatchData");
		e.printStackTrace();
	}
    }
}
