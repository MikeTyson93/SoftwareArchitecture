package de.htwg.se.ws1516.fourwinning.persistence.couchdb;
import java.util.LinkedList;
import java.util.List;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

import de.htwg.se.ws1516.fourwinning.models.Feld;
import de.htwg.se.ws1516.fourwinning.models.Player;

public class PersistancePlayArea extends CouchDbDocument {
	@TypeDiscriminator
	public String id;
	private Feld[][] feld;
    private int columns;
    private int rows;
    List<Player> playerlist;
    String name;
	
    public PersistancePlayArea(){
    }
    
    public void setFeld(Feld[][] feld){
    	this.feld = feld;
    }
    
    public Feld[][] getFeld(){
    	return this.feld;
    }
    
    public void setColumns(int columns){
    	this.columns = columns;
    }
    
    public int getColumns(){
    	return this.columns;
    }
    
    public void setRows(int rows){
    	this.rows = rows;
    }
    
    public int getRows(){
    	return this.rows;
    }
    
    public void setPlayers(List<Player> playerlist){
    	this.playerlist = playerlist;
    }
    
    public List<Player> getPlayers(){
    	return this.playerlist;
    }

    public void setName(String name){
    	this.name = name;
    }
    
    public String getName(){
    	return this.name;
    }
}
