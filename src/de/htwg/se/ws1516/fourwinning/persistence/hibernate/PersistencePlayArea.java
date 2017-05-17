package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import de.htwg.se.ws1516.fourwinning.models.PlayArea;
import de.htwg.se.ws1516.fourwinning.models.Player;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kuba on 24.04.2017.
 */
@Entity
@Table(name = "PlayArea")
public class PersistencePlayArea {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;

    @Column(name = "Feld")
    private PersistanceFeld[][] feld;

    @Column(name = "columns")
    private int columns;

    @Column(name = "rows")
    private int rows;

    @Column(name = "name")
    String name;

    @ElementCollection
    @CollectionTable(name="Playerlist")
    @Column(name= "playerlist")
    List<PersistancePlayer> playerlist;

    //Konstruktor

    public PersistencePlayArea(int rows, int columns){
        feld = new PersistanceFeld[rows][columns];
        this.columns = columns;
        this.rows = rows;
        name = "default";
        // 1 to many relationship
        playerlist = new LinkedList<>();
        buildArea(rows,columns);
    }
    public PersistencePlayArea(PlayArea playArea){
        this.columns = playArea.getColumns();
        this.rows = playArea.getRows();
        this.feld = new PersistanceFeld[rows][columns];
        this.name = playArea.getName();
        this.playerlist = new LinkedList<>();
        for (Player player:playArea.getPlayerList()) {
            this.playerlist.add(new PersistancePlayer(player));
        }
    }
    public PersistencePlayArea(){
    }

    //Spielfeld wird gebaut
    public void buildArea(int rows, int columns){
        for(int i = 0; i < rows; i++){
            for (int j = 0; j<columns; j++){
                feld[i][j] = new PersistanceFeld(i,j,null);
            }
        }
    }

    //Chip an freie Stelle setzen
    public int setChip(int column, PersistancePlayer p){
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

    public int getColumns(){
        return columns;
    }

    public int getRows(){
        return rows;
    }

    public PersistanceFeld[][] getFeld(){
        return feld;
    }

    public void setFeld(PersistanceFeld[][] zusatzfeld){
        this.feld = zusatzfeld;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void replacePlayArea(PersistanceFeld[][] feldcopy, String name, int columns, int rows) {
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

    public void setPlayers(PersistancePlayer one, PersistancePlayer two){
        playerlist.add(one);
        playerlist.add(two);
    }

    public void clearPlayers(){
        playerlist.clear();

    }

    public PersistancePlayer getPlayer(int idx){
        return playerlist.get(idx);

    }

    public List<PersistancePlayer> getPlayerList(){
        return playerlist;
    }
}
