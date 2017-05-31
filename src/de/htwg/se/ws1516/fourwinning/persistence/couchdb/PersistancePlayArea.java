package de.htwg.se.ws1516.fourwinning.persistence.couchdb;
import java.util.ArrayList;
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
	String id;
	private PersistanceFeld[][] feld;
    private int columns;
    private int rows;
    List<PersistancePlayer> players;
	String name;

    public PersistancePlayArea(){
    }
    
    public void setFeld(PersistanceFeld[][] feld){

    	rows = feld.length;
    	columns = feld[0].length;
		setClearFeld(rows,columns);
    	for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				this.feld[i][j].setX(feld[i][j].getX());
				this.feld[i][j].setY(feld[i][j].getY());
				this.feld[i][j].setSet(feld[i][j].getSet());
				PersistancePlayer owner = null;
				if (feld[i][j].getOwner() != null) {
					owner = new PersistancePlayer();
					owner.setActive(feld[i][j].getOwner().getActive());
					owner.setName(feld[i][j].getOwner().getName());
				}
				this.feld[i][j].setOwner(owner);
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
    
    public void setPlayers(LinkedList<Player> players){
		this.players = new LinkedList<>();
    	for (Player p : players){
    		PersistancePlayer pp = new PersistancePlayer();
    		pp.setName(p.getName());
    		pp.setActive(p.getActive());
    		pp.setZuege(p.getZuege());
    		this.players.add(pp);
		}
    }
    
    public List<PersistancePlayer> getPlayers(){
    	return this.players;
    }

    public void setName(String name){
    	this.name = name;
    }
    
    public String getName(){
    	return this.name;
    }

	public String getId(){
		return this.id;
	}

	public void setId(String id){
		this.id = id;
	}

    public void replacePlayArea(PersistanceFeld[][] feld, Feld[][] feldcopy, String name, int columns, int rows) {
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				feld[i][j].setX(feldcopy[i][j].getX());
				feld[i][j].setY(feldcopy[i][j].getY());
				feld[i][j].setSet(feldcopy[i][j].getSet());
				PersistancePlayer owner = null;
				if (feldcopy[i][j].getOwner() != null) {
					owner = new PersistancePlayer();
					owner.setActive(feldcopy[i][j].getOwner().getActive());
					owner.setName(feldcopy[i][j].getOwner().getName());
				}
					feld[i][j].setOwner(owner);
			}
		}
		this.name = name;
		this.columns = columns;
		this.rows = rows;
	}

	public void setClearFeld(int rows, int columns){
		this.feld = new PersistanceFeld[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				this.feld[i][j] = new PersistanceFeld();
				this.feld[i][j].setOwner(null);
				this.feld[i][j].setX(i);
				this.feld[i][j].setY(j);
			}
		}
	}
}
