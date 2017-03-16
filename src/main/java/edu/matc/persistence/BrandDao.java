package edu.matc.persistence;

import edu.matc.entity.Brand;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Punitha Anandan on 3/11/2017.
 */
public class BrandDao {
    private final Logger log = Logger.getLogger(this.getClass());


    /**
     * add a Brand
     *
     * @param brandEntity
     */
    public void addBrand(Brand brandEntity) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(brandEntity);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            if (transaction != null) transaction.rollback();
            log.error("Hibernate Exception", hibernateException);
        } finally {
            session.close();
        }
    }
    /** Get a brand for given brandId
     *
     * @param brandId  Brand Id
     * @return brand
     */
    public Brand getBrandTest(int brandId) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        List<Brand> brandEntity = null;
        Query query = session.createQuery("from Brand where brandId = " +
                ":brandID");
        query.setParameter("brandID", brandId);
        brandEntity = query.list();


        return brandEntity.get(0);
    }


    /** Get a brand for given brandId
     *
     * @param brandId  Brand Id
     * @return brand
     */
    public Brand getBrand(int brandId) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction transaction = null;
        Brand brandEntity = null;
        try {
            transaction = session.beginTransaction();
            brandEntity = (Brand) session.get(Brand.class, brandId);
            transaction.commit();

        }catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
            log.error("Hibernate Exception", hibernateException);
        }finally {
            session.close();
        }
        return brandEntity;
    }

    public List<Integer> getBrandByName(String brandName) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        List<Integer> brandEntity = null;
        try {
            Criteria criteria = session.createCriteria(Brand.class);
            criteria.add(Restrictions.like("brandName",brandName));
            ProjectionList projectionList = Projections.projectionList();
            projectionList.add(Projections.property("brandId"));
            criteria.setProjection(projectionList);
            brandEntity = criteria.list();
        }catch (HibernateException hibernateException) {
            log.error("Hibernate Exception", hibernateException);
        }finally {
            session.close();
        }
        return brandEntity;
    }
}
