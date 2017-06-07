package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "grid")
public class PersistenceGrid {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;

    //@ElementCollection
    //@CollectionTable(name="gridList")
    //@Column(name= "grid")
    //@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "gridColumn")
    private List<PersistenceColumn> grid;


    public PersistenceGrid(){
        grid = new LinkedList<>();
    }

    public PersistenceGrid(int rows, int columns){
        int counter = 0;
        grid = new LinkedList<>();
        for (int i=0; i < rows; i++){
            PersistenceColumn tmpList = new PersistenceColumn();
            for (int j = 0; j < columns; j++) {
                tmpList.setColumn(j, new PersistenceFeld());
            }
            grid.add(counter, tmpList);
            counter++;
        }

    }

    public void setField(int row, int column, PersistenceFeld feld){
        PersistenceColumn tmpList = grid.get(row);
        tmpList.setColumn(column, feld);
        grid.add(row, tmpList);
    }

    public PersistenceFeld getField(int row, int column){
        PersistenceColumn tmpList = grid.get(row);
        return tmpList.getValue(column);
    }
}
