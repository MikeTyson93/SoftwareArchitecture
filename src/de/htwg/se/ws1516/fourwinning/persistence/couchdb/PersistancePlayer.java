package de.htwg.se.ws1516.fourwinning.persistence.couchdb;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

public class PersistancePlayer extends CouchDbDocument{
    @TypeDiscriminator
    int id;
    String name;
    int zuege;
    boolean active;
    boolean gewonnen = false;

    PersistancePlayer(){
    }

    /*public PersistancePlayer(String name, int zuege, boolean active, String sessionName){
        this.name = name;
        this.zuege = zuege;
        this.active = active;
        this.sessionName = sessionName;
    }*/

    public void setActive(boolean zustand){
        active = zustand;
    }

    public boolean getActive(){
        return active;
    }

    public void chipSetted(){
        zuege++;
    }

    public void setZuege(int zuege){
        this.zuege = zuege;
    }

    public int getZuege(){
        return zuege;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public int getIdentification() {
        return this.id;
    }
}

