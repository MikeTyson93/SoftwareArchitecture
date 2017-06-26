package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import de.htwg.se.ws1516.fourwinning.models.Feld;
import de.htwg.se.ws1516.fourwinning.models.Player;

import javax.persistence.*;


@Entity
@Table(name = "Field")
public class PersistenceFeld {
    @Id @GeneratedValue
    @Column(name = "ID")
    public int id;

    @Column(name = "isSet")
    boolean isSet = false;

    @Column
    String owner;

    @Column
    int zuege;

    @Column(name = "xValue")
    private int x;

    @Column(name = "yValue")
    private int y;

    public PersistenceFeld(int x, int y){
        this.x = x;
        this.y = y;
    }

    public PersistenceFeld(){
    }

    public PersistenceFeld(int x, int y, PersistencePlayer owner){
        this.owner = owner.getName();
        this.zuege = owner.getZuege();
        this.x = x;
        this.y = y;
    }

    public PersistenceFeld(Feld feld){
        this.x = feld.getX();
        this.y = feld.getY();
        this.owner = feld.getOwner().getName();
        this.zuege = feld.getOwner().getZuege();
    }

    public void setSet(boolean isSet){
        this.isSet = isSet;
    }

    public boolean getSet(){
        return this.isSet;
    }

    public void setOwner(PersistencePlayer owner){
        this.owner = owner.getName();
        this.zuege = owner.getZuege();
    }

    public PersistencePlayer getOwner(){
        return new PersistencePlayer(this.owner, this.zuege);
    }

    public Player getOwnerAsGameModel(){
        if(this.owner != null) {
            return new Player(this.owner, this.zuege);
        }
        else{
            return null;
        }

    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getX(){
        return this.x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getY(){
        return this.y;
    }
}
