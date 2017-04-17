package de.htwg.se.ws1516.fourwinning.persistence.couchdb;
import java.util.LinkedList;
import java.util.List;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

import de.htwg.se.ws1516.fourwinning.models.Feld;
import de.htwg.se.ws1516.fourwinning.models.Player;

public class PersistancePlayArea extends CouchDbDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TypeDiscriminator
	public String id;
	private PersistanceFeld[][] feld;
    private int columns;
    private int rows;
    List<Player> playerlist;
    String name;
	
    public PersistancePlayArea(){
    }
    
    public void setFeld(PersistanceFeld[][] feld){
    	for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				feld[i][j].setX(feld[i][j].getX());
				feld[i][j].setY(feld[i][j].getY());
				feld[i][j].setSet(feld[i][j].getSet());
				feld[i][j].setOwner(feld[i][j].getOwner());
			}
		}
    }
    
    public PersistanceFeld[][] getFeld(){
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
    
    public void replacePlayArea(Feld[][] feldcopy, String name, int columns, int rows) {
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				feld[i][j].setX(feldcopy[i][j].getX());
				feld[i][j].setY(feldcopy[i][j].getY());
				feld[i][j].setSet(feldcopy[i][j].getSet());
				feld[i][j].setOwner(feldcopy[i][j].getOwner());
			}
		}
		this.name = name;
		this.columns = columns;
		this.rows = rows;
	}
}
