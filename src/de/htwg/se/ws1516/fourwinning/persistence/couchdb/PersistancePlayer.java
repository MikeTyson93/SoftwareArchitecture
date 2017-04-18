package de.htwg.se.ws1516.fourwinning.persistence.couchdb;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

public class PersistancePlayer extends CouchDbDocument{
    @TypeDiscriminator
    String id;
    String name;
    int menge;
    boolean active;
    boolean gewonnen = false;

    PersistancePlayer(){
    }

    /*public PersistancePlayer(String name, int menge, boolean active, String sessionName){
        this.name = name;
        this.menge = menge;
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
        menge--;
    }

    public void setMenge(int menge){
        this.menge = menge;
    }

    public int getMenge(){
        return menge;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}

