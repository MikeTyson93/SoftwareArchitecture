package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "grid")
public class PersistenceGrid {
    @Id
    @GeneratedValue
    @Column(name = "Grid_id")
    public int id;

    @JoinTable
    @OneToMany(cascade = CascadeType.ALL)
    private List<PersistenceFeld> grid;


    public PersistenceGrid(){
        grid = new LinkedList<>();
    }

    public PersistenceGrid(int rows, int columns){
        grid = new LinkedList<>();
        for (int i=0; i < rows; i++){
            for (int j = 0; j < columns; j++) {
                grid.add(i+j, new PersistenceFeld(i,j));
            }
        }

    }

    public void setField(int row, int column, PersistenceFeld feld){
        PersistenceFeld tmpField = getField(row, column);
        grid.set(grid.indexOf(tmpField), feld);
    }

    public PersistenceFeld getField(int row, int column){
        for (PersistenceFeld tmpField : grid) {
            if (tmpField.getX() == row && tmpField.getY() == column) {
                return tmpField;
            }
        }
        return null;
    }
}
