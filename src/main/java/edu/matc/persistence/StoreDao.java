package edu.matc.persistence;

import edu.matc.entity.Store;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Punitha Anandan on 3/11/2017.
 */
public class StoreDao {
    private final Logger log = Logger.getLogger(this.getClass());
    String message;

    /**
     * add a user expense
     *
     * @param store
     */
    public int addStore(Store store) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction transaction = null;
        int storeId = 0;
        try {
            transaction = session.beginTransaction();
            storeId = (Integer) session.save(store);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            if (transaction != null) transaction.rollback();
            log.error("Hibernate Exception", hibernateException);
        } finally {
            session.close();
        }
        return storeId;
    }


    /** Return a list of all items
     *
     * @return All items
     */
    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<Store>();
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            stores = session.createCriteria(Store.class).list();
            transaction.commit();
        }catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
            log.error("Hibernate Exception", hibernateException);
            message = "400:Not Found - the request made is not found";
        }finally {
            session.close();
        }
        return stores;
    }


    /** Get a store for given expenseName
     *
     * @param storeId  The name of Expense
     * @return store
     */
    public Store getStore(int storeId) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction transaction = null;
        Store store = null;
        try {
            transaction = session.beginTransaction();
            store = (Store) session.get(Store.class, storeId);
            transaction.commit();

        }catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
            log.error("Hibernate Exception", hibernateException);
        }finally {
            session.close();
        }
        return store;
    }


    /**
     * Given name and long/lat find the store
     * @param name
     * @param latitude
     * @param longtitude
     * @return stores
     */
    public List<Store> getExactStore(String name, double latitude, double longtitude) {

        List<Store> stores = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Store.class);
        criteria.add(Restrictions.eq("storeName",name));

        criteria.add(Restrictions.eq("longtitude",BigDecimal
                .valueOf(longtitude)));
        criteria.add(Restrictions.eq("latitude",BigDecimal.valueOf(latitude)));

        stores = criteria.list();
        session.close();

        return stores;

    }


    /**
     * given the long/lat and distance find all store ids in the radius of
     * distance from long/lat center.
     * @param latitude
     * @param longtitude
     * @param distance
     * @return storeIds
     * @throws Exception
     */
    public List<Integer> getNearestStoreId(double latitude, double longtitude,
                                       double distance) throws Exception {

        List<Integer> storeIds = new ArrayList<Integer>();
        List<Store> stores = getNearestStore(latitude, longtitude, distance);

        for (Store store: stores) {
            storeIds.add(store.getStoreId());
        }

        return storeIds;

    }


    /**
     * Given long/lat and distance find all the stores within the radius
     * @param latitude
     * @param longtitude
     * @param distance
     * @return
     * @throws Exception
     */
    public List<Store> getNearestStore(double latitude, double longtitude,
                                       double distance) throws Exception {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        double earthRadius = 3958.762079; //miles
        GeoLocation location = GeoLocation.fromDegrees(latitude, longtitude);
        GeoLocation[] boundingCoordinates =
                location.boundingCoordinates(distance, earthRadius);
        boolean meridian180WithinDistance =
                boundingCoordinates[0].getLongitudeInRadians() >
                        boundingCoordinates[1].getLongitudeInRadians();
        String andOr = null;
        if (meridian180WithinDistance) {
            andOr = "OR";
        } else {
            andOr = "AND";
        }

        String hql = "FROM Store WHERE (latitude >= :one "
                + "AND latitude <= :two) AND (longtitude >= :three "
                + andOr
                + " longtitude <= :four) AND acos(sin(:five) * sin(radians(latitude)) "
                + "+ cos(:six) * cos(radians(latitude)) * "
                + "cos(radians(longtitude) - :seven)) <= :eight";

        Query query = session.createQuery(hql);
        query.setParameter("one", BigDecimal.valueOf(boundingCoordinates[0]
                .getLatitudeInDegrees()));
        query.setParameter("two", BigDecimal.valueOf(boundingCoordinates[1]
                .getLatitudeInDegrees()));
        query.setParameter("three", BigDecimal.valueOf(boundingCoordinates[0]
                .getLongitudeInDegrees()));
        query.setParameter("four", BigDecimal.valueOf(boundingCoordinates[1]
                .getLongitudeInDegrees()));
        query.setParameter("five", location.getLatitudeInRadians());
        query.setParameter("six", location.getLatitudeInRadians());
        query.setParameter("seven", location.getLongitudeInRadians());
        query.setParameter("eight", distance / earthRadius);

        return query.list();

    }
    
}
