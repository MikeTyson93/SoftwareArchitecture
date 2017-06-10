package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import de.htwg.se.ws1516.fourwinning.models.Feld;
import de.htwg.se.ws1516.fourwinning.models.PlayArea;
import de.htwg.se.ws1516.fourwinning.models.PlayAreaInterface;
import de.htwg.se.ws1516.fourwinning.models.Player;
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
            session.saveOrUpdate(area);
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
        List areaList = new LinkedList();
        Session session = HibernateUtil.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List<PersistencePlayArea> areas = new LinkedList<PersistencePlayArea>();
                try{
                    //areas = session.createCriteria("PersistencePlayer.class").list();
                    areas = session.createQuery("from PersistencePlayArea").list();
                }catch(HibernateException e) {
                    tx.rollback();
                    e.printStackTrace();
                }finally{
                    session.close();
                }
                if(!areas.isEmpty()){
                    for(PersistencePlayArea area : areas){
                        areaList.add(copyPlayArea(area));
                    }
                }

        return areaList;
    }

    public PlayAreaInterface copyPlayArea(PersistencePlayArea pArea){
        // We need a transformation from db-model to game-model
        if (pArea == null)
            return null;
        PlayAreaInterface copyPlayArea = new PlayArea(pArea.getRows(), pArea.getColumns());
        copyPlayArea.setName(pArea.getName());
        //List<Player> playerlist = pArea.getPlayerList();
        Player p1 = changeToGameModel(pArea.getPlayer(0));
        Player p2 = changeToGameModel(pArea.getPlayer(1));
        copyPlayArea.setPlayers(p1, p2);
        Feld[][] areaOfGame = copyPlayArea.getFeld();
        PersistenceGrid areaOfDb = pArea.getGrid();
        for (int i = 0; i < pArea.getRows(); i++){
            for (int j = 0; j < pArea.getColumns(); j++){
                areaOfGame[i][j].setX(areaOfDb.getField(i,j).getX());
                areaOfGame[i][j].setY(areaOfDb.getField(i,j).getY());
                areaOfGame[i][j].setSet(areaOfDb.getField(i,j).getSet());
                areaOfGame[i][j].setOwner(changeToGameModel(areaOfDb.getField(i,j).getOwner()));
            }
        }
        copyPlayArea.replacePlayArea(areaOfGame, pArea.getName(), pArea.getColumns(), pArea.getRows());
        return copyPlayArea;
    }

    public Player changeToGameModel(PersistencePlayer player){
        return new Player(player.getName(), player.getMenge());
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
