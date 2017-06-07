package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import de.htwg.se.ws1516.fourwinning.models.Player;
import de.htwg.se.ws1516.fourwinning.models.PlayerInterface;

import javax.persistence.*;

/**
 * Created by Kuba on 17.05.2017.
 */

@Entity
@Table(name = "Player")
public class PersistencePlayer implements PlayerInterface {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;

    @Column(name = "name")
    String name;

    @Column(name= "menge")
    int menge;

    @Column(name = "active")
    boolean active;

    @Column(name = "won")
    boolean gewonnen = false;

    @Column(name = "sessionName")
    String sessionName;

    public PersistencePlayer(String name, int menge){
        this.name = name;
        this.menge = menge;
        this.sessionName = "default";
    }
    public PersistencePlayer(Player player){
        this.name = player.getName() ;
        this.menge = player.getMenge();
        this.sessionName = "default";
    }
    public PersistencePlayer(){

    }

    public String getName(){
        return name;
    }

    public void setActive(boolean zustand){
        active = zustand;
    }

    public boolean getActive(){
        return active;
    }

    public void chipSetted(){
        menge--;
    }

    public int getMenge(){
        return menge;
    }
}
