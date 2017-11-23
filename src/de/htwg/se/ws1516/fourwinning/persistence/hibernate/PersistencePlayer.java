package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import de.htwg.se.ws1516.fourwinning.models.Player;
import de.htwg.se.ws1516.fourwinning.models.PlayerInterface;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Kuba on 17.05.2017.
 */

@Entity
@Table(name = "Player")
public class PersistencePlayer implements PlayerInterface {
    @Id
    @GeneratedValue
    @Column(name = "player_ID")
    public int id;



    @Column(name = "name")
    String name;

    @Column(name= "zuege")
    int zuege;

    @Column(name = "active")
    boolean active;

    @Column(name = "won")
    boolean gewonnen = false;

    @Column(name = "sessionName")
    String sessionName;

    public PersistencePlayer(String name, int zuege){
        this.name = name;
        this.zuege = zuege;
        this.sessionName = "default";
    }
    public PersistencePlayer(Player player){
        this.name = player.getName() ;
        this.zuege = player.getZuege();
        this.sessionName = "default";
    }
    public PersistencePlayer(){
    }

    public String getName(){
        return name;
    }

    public void setName(String name) { this.name = name; }

    public void setActive(boolean zustand){
        active = zustand;
    }

    public boolean getActive(){
        return active;
    }

    public void chipSetted(){
        zuege++;
    }

    public int getZuege(){
        return zuege;
    }

    public void resetZuege() { this.zuege = 0; }

    public int getIdentification() {
        return this.id;
    }
}
