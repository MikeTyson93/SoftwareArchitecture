package de.htwg.se.ws1516.fourwinning.persistence.hibernate;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "Columns")
public class PersistenceColumn {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;

    //@ElementCollection
    //@CollectionTable(name="columnList")
    //@Column(name= "columns")
    //@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
    @ManyToOne(targetEntity = PersistenceFeld.class)
    @JoinColumn(name = "clm_id")
    private List<PersistenceFeld> gridColumn = new LinkedList<>();

    public PersistenceColumn(){
        gridColumn = new LinkedList<>();
    }

    public void setColumn(int x, PersistenceFeld feld){
        gridColumn.add(x, feld);
    }

    public PersistenceFeld getValue(int x){
        return gridColumn.get(x);
    }
}
