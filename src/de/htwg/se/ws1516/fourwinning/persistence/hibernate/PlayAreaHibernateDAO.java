package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import de.htwg.se.ws1516.fourwinning.models.PlayAreaInterface;
import de.htwg.se.ws1516.fourwinning.persistence.PlayAreaInterfaceDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.LinkedList;
import java.util.List;


public class PlayAreaHibernateDAO implements PlayAreaInterfaceDAO {
    //private static Session session = HibernateUtil.getInstance().getCurrentSession();

    public PlayAreaHibernateDAO(){

    }
    @Override
    public void savePlayArea(PlayAreaInterface PlayArea) {
        Session session = HibernateUtil.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Integer id = null;
        try{
            PersistencePlayArea area = new PersistencePlayArea((de.htwg.se.ws1516.fourwinning.models.PlayArea) PlayArea);
            id = (Integer) session.save(area);
            tx.commit();
        }catch(HibernateException e){
            if(tx!=null) tx.rollback();
            e.printStackTrace();
        }finally{
            session.close();
        }
    }

    @Override
    public List getAllPlayAreas() {
        Session session = HibernateUtil.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List areas = new LinkedList();
                try{
                    areas = session.createQuery("from java.lang.Object").list();
                }catch(HibernateException e) {
                    tx.rollback();
                    e.printStackTrace();
                }finally{
                    session.close();
                }
        return areas;
    }

    @Override
    public boolean deletePlayArea(String name) {
        return false;
    }

    @Override
    public boolean containsPlayAreaByName(String name) {
        return false;
    }

    @Override
    public PlayAreaInterface getPlayArea(String name) {
        return null;
    }
}
