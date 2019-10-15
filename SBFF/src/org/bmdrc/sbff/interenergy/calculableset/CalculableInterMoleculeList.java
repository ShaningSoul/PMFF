package org.bmdrc.sbff.interenergy.calculableset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import org.bmdrc.sbff.tool.AtomTypeGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class CalculableInterMoleculeList implements Serializable {

    private static final long serialVersionUID = -3821653528388550322L;

    private CalculableElectroStaticList itsElectroStaticList;
    private CalculableHydrogenBondList itsHydrogenBondList;
    private CalculableNonbondingList itsNonbondingList;

    public CalculableInterMoleculeList(IAtomContainer theMolecule) {
        this(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }
    
    public CalculableInterMoleculeList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this(theMolecule, theTopologicalDistanceMatrix, new ArrayList<Integer>());
    }
    
    public CalculableInterMoleculeList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new TopologicalDistanceMatrix(theMolecule), theNotCalculateAtomNumberList);
    }
    
    public CalculableInterMoleculeList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix,
            List<Integer> theNotCalculateAtomNumberList) {
        this.__generateAtomType(theMolecule, theTopologicalDistanceMatrix);
        
        this.itsElectroStaticList = new CalculableElectroStaticList(theMolecule, theTopologicalDistanceMatrix, theNotCalculateAtomNumberList);
        this.itsHydrogenBondList = new CalculableHydrogenBondList(theMolecule, theTopologicalDistanceMatrix, this.itsElectroStaticList, theNotCalculateAtomNumberList);
        this.itsNonbondingList = new CalculableNonbondingList(theMolecule, this.itsElectroStaticList, this.itsHydrogenBondList, theNotCalculateAtomNumberList);
    }
    
    private void __generateAtomType(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        MpeoeAtomTypeGenerator theMpeoeAtomTypeGenerator = new MpeoeAtomTypeGenerator();
        
        theMpeoeAtomTypeGenerator.inputMpeoeAtomType(theMolecule, theTopologicalDistanceMatrix);
    }

    public CalculableElectroStaticList getElectroStaticList() {
        return this.itsElectroStaticList;
    }

    public void setElectroStaticList(CalculableElectroStaticList theElectroStaticList) {
        this.itsElectroStaticList = theElectroStaticList;
    }

    public CalculableHydrogenBondList getHydrogenBondList() {
        return this.itsHydrogenBondList;
    }

    public void setHydrogenBondList(CalculableHydrogenBondList theHydrogenBondList) {
        this.itsHydrogenBondList = theHydrogenBondList;
    }

    public CalculableNonbondingList getNonbondingList() {
        return this.itsNonbondingList;
    }

    public void setNonbondingList(CalculableNonbondingList theNonbondingList) {
        this.itsNonbondingList = theNonbondingList;
    }
}
