package de.htwg.se.ws1516.fourwinning.persistence.couchdb;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

public class PersistanceFeld extends CouchDbDocument {

    @TypeDiscriminator
    String id;
	boolean isSet = false;
    PersistancePlayer owner;
    private int x;
    private int y;
    
    public PersistanceFeld(){
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

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }
}
