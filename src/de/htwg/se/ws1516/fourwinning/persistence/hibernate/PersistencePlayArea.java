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
    @Column(name = "ID")
    public int id;

    @Column(name = "columns")
    private int columns;

    @Column(name = "rows")
    private int rows;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "feld")
    private PersistenceGrid feld;

    //@ElementCollection
    //@CollectionTable(name="Playerlist")
    //@Column(name= "playerlist")
    //@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = PersistencePlayer.class)
    @JoinColumn(name = "playerList")
    private List<PersistencePlayer> playerlist;


    //Konstruktor

    public PersistencePlayArea(int rows, int columns){
        this.feld = new PersistenceGrid(rows, columns);
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
        this.feld = new PersistenceGrid(rows, columns);
        //buildArea(rows,columns);
        replacePlayArea(playArea.getFeld(), name, columns, rows);
        this.playerlist = new LinkedList<>();
        for (Player player:playArea.getPlayerList()) {
            this.playerlist.add(new PersistencePlayer(player));
        }
    }
    public PersistencePlayArea(){
        this.feld = new PersistenceGrid();
    }

    /*
    //Spielfeld wird gebaut
    public void buildArea(int rows, int columns){
        for(int i = 0; i < rows; i++){
            for (int j = 0; j<columns; j++){
                feld[i][j] = new PersistenceFeld();
            }
        }
    }
    */


    public int getColumns(){
        return columns;
    }

    public int getRows(){
        return rows;
    }

    public PersistenceGrid getFeld(){
        return feld;
    }

    public void setFeld(PersistenceGrid zusatzfeld){
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
                PersistenceFeld tmpFeld = new PersistenceFeld(columns, rows);
                tmpFeld.setX(feldcopy[i][j].getX());
                tmpFeld.setY(feldcopy[i][j].getY());
                tmpFeld.setSet(feldcopy[i][j].getSet());
                if(feldcopy[i][j].getOwner() != null){
                    tmpFeld.setOwner(new PersistencePlayer(feldcopy[i][j].getOwner()));
                }
                feld.setField(feldcopy[i][j].getX(), feldcopy[i][j].getY(), tmpFeld);
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

    public List<PersistencePlayer> getPlayerList(){
        return playerlist;
    }
}
