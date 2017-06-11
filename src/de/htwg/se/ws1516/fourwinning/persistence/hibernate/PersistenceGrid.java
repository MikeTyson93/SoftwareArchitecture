package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "grid")
public class PersistenceGrid {
    @Id
    @GeneratedValue
    @Column(name = "Grid_id")
    public int id;

    @JoinTable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PersistenceFeld> grid;


    public PersistenceGrid(){
        grid = new HashSet<>();
    }

    public PersistenceGrid(int rows, int columns){
        grid = new HashSet<>();
        for (int i=0; i < rows; i++){
            for (int j = 0; j < columns; j++) {
                grid.add(new PersistenceFeld(i,j));
            }
        }

    }

    public void setField(int row, int column, PersistenceFeld feld){
        PersistenceFeld tmpField = getField(row, column);
        grid.remove(tmpField);
        grid.add(feld);
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
