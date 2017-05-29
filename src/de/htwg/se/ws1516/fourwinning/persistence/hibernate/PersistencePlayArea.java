package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import de.htwg.se.ws1516.fourwinning.models.Feld;
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

    @Column(name = "columns")
    private int columns;

    @Column(name = "rows")
    private int rows;

    @ElementCollection
    @CollectionTable(name="FeldList")
    @OrderColumn
    @Column(name = "Feld")
    private PersistanceFeld[][] feld;

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
        this.rows = playArea.getRows();
        this.columns = playArea.getColumns();
        this.name = playArea.getName();
        this.feld = new PersistanceFeld[rows][columns];
        buildArea(rows,columns);
        replacePlayArea(playArea.getFeld(), name, columns, rows);
        //this.feld = new PersistanceFeld[rows][columns];
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
                feld[i][j] = new PersistanceFeld();
            }
        }
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

    public void replacePlayArea(Feld[][] feldcopy, String name, int columns, int rows) {
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                feld[i][j].setX(feldcopy[i][j].getX());
                feld[i][j].setY(feldcopy[i][j].getY());
                feld[i][j].setSet(feldcopy[i][j].getSet());
                if(feldcopy[i][j].getOwner() != null){
                    feld[i][j].setOwner(new PersistancePlayer(feldcopy[i][j].getOwner()));
                }

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
