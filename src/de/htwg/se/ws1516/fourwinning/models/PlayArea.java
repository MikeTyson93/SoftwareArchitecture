package de.htwg.se.ws1516.fourwinning.models;

import java.util.LinkedList;
import java.util.List;

import de.htwg.se.ws1516.fourwinning.models.Feld;

public class PlayArea implements PlayAreaInterface
{
    private Feld[][] feld;
    private int columns;
    private int rows;
    String name;
    LinkedList<Player> playerlist;
    private String id;

    //Konstruktor
    public PlayArea(int rows, int columns){
        feld = new Feld[rows][columns];
        this.columns = columns;
        this.rows = rows;
        name = "default";
        // 1 to many relationship
        playerlist = new LinkedList<>();
        buildArea(rows,columns);
    }

    //Spielfeld wird gebaut
    @Override
    public void buildArea(int rows, int columns){
        for(int i = 0; i < rows; i++){
            for (int j = 0; j<columns; j++){
                feld[i][j] = new Feld(i,j,null);
            }
        }
    }

    //Chip an freie Stelle setzen
    @Override
    public int setChip(int column, Player p){
        if (column < 0
			|| column >= columns) return -2; //Zug fehlgeschlagen
        boolean find = false;
        int emptyRow = -1;
        int currentRow = 0;

        //Freien Platz an der Stelle der Spalte finden
        while (!find){
            if (feld[currentRow][column].getSet()){
                find = true;
                emptyRow = currentRow - 1;
            } else if (currentRow == rows-1){
                find = true;
                emptyRow = rows-1;
            }
            currentRow++;
        }

        //Chip wird gelegt
        if (emptyRow != -1){
            feld[emptyRow][column].setOwner(p);
            p.chipSetted();
            return emptyRow;            //Zug erfolgreich
        } 
        return -2;                          //Zug fehlgeschalgen
    }

    @Override
    public int getColumns(){
        return columns;
    }

    @Override
    public int getRows(){
        return rows;
    }

    @Override
    public Feld[][] getFeld(){
        return feld;
    }
    
    public void setFeld(Feld[][] zusatzfeld){
    	this.feld = zusatzfeld;
    }

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
        this.name = name;
	}

	@Override
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
	
	@Override
	public void setPlayers(Player one, Player two){
		playerlist.add(one);
		playerlist.add(two);
	}
	
	@Override
	public void clearPlayers(){
		playerlist.clear();
		
	}
	    
	@Override
	public Player getPlayer(int idx){
		return playerlist.get(idx);
	
	}
	
	@Override
	public LinkedList<Player> getPlayerList(){
		return playerlist;
	}

    @Override
	public String getId(){
	    return this.id;
    }

    @Override
    public void setId(String id){
	    this.id = id;
    }
}