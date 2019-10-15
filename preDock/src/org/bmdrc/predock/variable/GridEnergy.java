package org.bmdrc.predock.variable;

import java.io.Serializable;
import java.util.Comparator;
import org.bmdrc.sbff.solvation.Grid;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class GridEnergy implements Serializable {

    private static final long serialVersionUID = -2204582648378484165L;

    private Grid itsGrid;
    private Double itsEnergy;
    private AtomType itsAtomType;

    public GridEnergy() {
    }
    
    public GridEnergy(Grid theGrid) {
        this.itsGrid = theGrid;
    }

    public GridEnergy(Double theEnergy, AtomType theAtomType) {
        this.itsEnergy = theEnergy;
        this.itsAtomType = theAtomType;
    }

    public GridEnergy(Double theX, Double theY, Double theZ, Double theEnergy, AtomType theAtomType) {
        this.itsGrid = new Grid(theX, theY, theZ);
        this.itsEnergy = theEnergy;
        this.itsAtomType = theAtomType;
    }
    
    public GridEnergy(Grid theGrid, Double theEnergy, AtomType theAtomType) {
        this.itsGrid = theGrid;
        this.itsEnergy = theEnergy;
        this.itsAtomType = theAtomType;
    }
    
    public GridEnergy(GridEnergy theGridEnergy) {
        this.itsGrid = new Grid(theGridEnergy.itsGrid);
        this.itsEnergy = theGridEnergy.itsEnergy;
        this.itsAtomType = theGridEnergy.itsAtomType;
    }

    public Double getEnergy() {
        return this.itsEnergy;
    }

    public void setEnergy(Double theEnergy) {
        this.itsEnergy = theEnergy;
    }

    public AtomType getAtomType() {
        return this.itsAtomType;
    }

    public void setAtomType(AtomType theAtomType) {
        this.itsAtomType = theAtomType;
    }

    public Grid getGrid() {
        return this.itsGrid;
    }

    public void setGrid(Grid theGrid) {
        this.itsGrid = theGrid;
    }
    
    public static Comparator<GridEnergy> getEnergyComparator() {
        Comparator<GridEnergy> theComparator = new Comparator<GridEnergy>() {
            @Override
            public int compare(GridEnergy theFirst, GridEnergy theSecond) {
                return theFirst.getEnergy().compareTo(theSecond.getEnergy());
            }
        };
        
        return theComparator;
    }
    
    public static Comparator<GridEnergy> getAtomTypeComparator() {
        Comparator<GridEnergy> theComparator = new Comparator<GridEnergy>() {
            @Override
            public int compare(GridEnergy theFirst, GridEnergy theSecond) {
                return theFirst.getAtomType().compareTo(theSecond.getAtomType());
            }
        };
        
        return theComparator;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append("Grid : ").append(this.itsGrid.toString()).append(", Atom type : ").append(this.itsAtomType).append(", Energy : ")
                .append(this.itsEnergy);
        
        return theStringBuilder.toString();
    }
}
