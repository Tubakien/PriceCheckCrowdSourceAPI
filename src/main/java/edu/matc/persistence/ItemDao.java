package edu.matc.persistence;

import edu.matc.entity.Item;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Punitha Anandan on 3/11/2017.
 */
public class ItemDao {
    private final Logger log = Logger.getLogger(this.getClass());
    String message;


    /**
     * add a user expense
     *
     * @param item
     */
    public int addItem(Item item) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction transaction = null;
        int itemId = 0;
        try {
            transaction = session.beginTransaction();
            itemId = (Integer) session.save(item);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            if (transaction != null) transaction.rollback();
            log.error("Hibernate Exception", hibernateException);
        } finally {
            session.close();
        }
        return itemId;
    }

    /** Return a list of all items
     *
     * @return All items
     */
    public List<Item> getAllItems() {
        List items = new ArrayList<Item>();
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            items = session.createCriteria(Item.class).list();
            transaction.commit();
        }catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
            log.error("Hibernate Exception", hibernateException);
            message = "400:Not Found - the request made is not found";
        }finally {
            session.close();
        }
        return items;

    }

    /** Get a item for given itemId
     *
     * @param itemId  The name of Expense
     * @return item
     */
    public Item getItemEntity(int itemId) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction transaction = null;
        Item item = null;
        try {
            transaction = session.beginTransaction();
            item = (Item) session.get(Item.class, itemId);
            transaction.commit();

        }catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
            log.error("Hibernate Exception", hibernateException);
        }finally {
            session.close();
        }
        return item;
    }

    /**Get Item by name
     *
     * @param itemName
     * @return itemEntity
     */
    public List<Integer> getItemByName(String itemName) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        List<Integer> itemEntity = null;
        try {
            Criteria criteria = session.createCriteria(Item.class);
            criteria.add(Restrictions.like("itemName", itemName));
            ProjectionList projectionList = Projections.projectionList();
            projectionList.add(Projections.property("itemId"));
            criteria.setProjection(projectionList);
            itemEntity = criteria.list();
        }catch (HibernateException hibernateException) {
            log.error("Hibernate Exception", hibernateException);
        }finally {
            session.close();
        }
        return itemEntity;
    }

    /**
     * return an exact item
     * @param itemName
     * @param unit
     * @param unitValue
     * @return
     */
    public List<Item> getExactItem(String itemName, String unit, int
            unitValue) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        List<Item> itemEntity = null;
        try {
            Criteria criteria = session.createCriteria(Item.class);
            criteria.add(Restrictions.eq("itemName", itemName));
            criteria.add(Restrictions.eq("unit", unit));
            criteria.add(Restrictions.eq("unitValue", unitValue));
            itemEntity = criteria.list();
        }catch (HibernateException hibernateException) {
            log.error("Hibernate Exception", hibernateException);
        }finally {
            session.close();
        }

        return itemEntity;
    }

}
