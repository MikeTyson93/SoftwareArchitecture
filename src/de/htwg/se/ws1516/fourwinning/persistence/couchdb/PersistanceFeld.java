package de.htwg.se.ws1516.fourwinning.persistence.couchdb;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

import de.htwg.se.ws1516.fourwinning.models.Player;

public class PersistanceFeld extends CouchDbDocument {
	
	@TypeDiscriminator
	public String id;
	
	boolean isSet = false;
    Player owner;
    private int x;
    private int y;
    
    public PersistanceFeld(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    
    public void setIsSet(boolean isSet){
    	this.isSet = isSet;
    }
    
    public boolean getSet(){
    	return this.isSet;
    }
    
    public void setOwner(Player owner){
    	this.owner = owner;
    }
    
    public Player getOwner(){
    	return this.owner;
    }
    
    public void setId(String id){
    	this.id = id;
    }
    
    public String getId(){
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
