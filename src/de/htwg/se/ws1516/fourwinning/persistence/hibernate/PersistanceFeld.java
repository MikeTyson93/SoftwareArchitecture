package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import de.htwg.se.ws1516.fourwinning.models.Feld;

import javax.persistence.*;

/**
 * Created by Kuba on 24.04.2017.
 */
@Entity
@Table(name = "Field")
public class PersistanceFeld {
    @Id @GeneratedValue
    @Column(name = "ID")
    public int id;

    @Column(name = "isSet")
    boolean isSet = false;

    @ManyToOne
    @JoinColumn(name = "owner")
    PersistancePlayer owner;

    @Column(name = "xValue")
    private int x;

    @Column(name = "yValue")
    private int y;

    public PersistanceFeld(int x, int y){
        this.x = x;
        this.y = y;
    }

    public PersistanceFeld(){
    }

    public PersistanceFeld(int x, int y, PersistancePlayer owner){
        this.owner = owner;
        this.x = x;
        this.y = y;
    }

    public PersistanceFeld(Feld feld){
        this.x = feld.getX();
        this.y = feld.getY();
        this.owner = new PersistancePlayer(feld.getOwner());
    }

    public void setSet(boolean isSet){
        this.isSet = isSet;
    }

    public boolean getSet(){
        return this.isSet;
    }

    public void setOwner(PersistancePlayer owner){
        this.owner = owner;
    }

    public PersistancePlayer getOwner(){
        return this.owner;
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
