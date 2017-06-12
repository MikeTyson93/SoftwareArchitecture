package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import de.htwg.se.ws1516.fourwinning.models.Feld;
import de.htwg.se.ws1516.fourwinning.models.PlayArea;
import de.htwg.se.ws1516.fourwinning.models.Player;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
@Table(name = "PlayArea")
public class PersistencePlayArea {

    @Id
    @GeneratedValue
    @Column(name = "area_ID")
    public int id;

    @Column(name = "columns")
    private int columns;

    @Column(name = "rows")
    private int rows;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable
    private List<PersistencePlayer> playerlist;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "grid")
    private PersistenceGrid grid;


    //Konstruktor

    public PersistencePlayArea(int rows, int columns){
        this.grid = new PersistenceGrid(rows, columns);
        this.columns = columns;
        this.rows = rows;
        name = "default";
        // 1 to many relationship
        playerlist = new LinkedList<>();
        //buildArea(rows,columns);
    }
    public PersistencePlayArea(PlayArea playArea){
        this.rows = playArea.getRows();
        this.columns = playArea.getColumns();
        this.name = playArea.getName();
        this.grid = new PersistenceGrid(rows, columns);
        replacePlayArea(playArea.getFeld(), name, columns, rows);
        this.playerlist = new LinkedList<>();
        for (Player player:playArea.getPlayerList()) {
            this.playerlist.add(new PersistencePlayer(player));
        }
    }
    public PersistencePlayArea(){
        this.grid = new PersistenceGrid();
    }



    public int getColumns(){
        return columns;
    }

    public int getRows(){
        return rows;
    }

    public PersistenceGrid getGrid(){
        return grid;
    }

    public void setGrid(PersistenceGrid zusatzfeld){
        this.grid = zusatzfeld;
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
                PersistenceFeld tmpFeld = new PersistenceFeld(columns, rows);
                tmpFeld.setX(feldcopy[i][j].getX());
                tmpFeld.setY(feldcopy[i][j].getY());
                tmpFeld.setSet(feldcopy[i][j].getSet());
                if(feldcopy[i][j].getOwner() != null){
                    tmpFeld.setOwner(new PersistencePlayer(feldcopy[i][j].getOwner()));
                }
                grid.setField(feldcopy[i][j].getX(), feldcopy[i][j].getY(), tmpFeld);
            }
        }
        this.name = name;
        this.columns = columns;
        this.rows = rows;
    }

    public void setPlayers(PersistencePlayer one, PersistencePlayer two){
        playerlist.add(one);
        playerlist.add(two);
    }

    public void clearPlayers(){
        playerlist.clear();

    }

    public PersistencePlayer getPlayer(int idx){
        return playerlist.get(idx);

    }

    public int getID(){
        return this.id;
    }

    public List<PersistencePlayer> getPlayerList(){
        return playerlist;
    }
}
