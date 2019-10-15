/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.basicchemistry.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bmdrc.basicchemistry.protein.AminoAcid;
import org.bmdrc.basicchemistry.protein.AminoAcidInformation;
import org.bmdrc.basicchemistry.protein.Chain;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class TopologicalDistanceMatrix implements Serializable {

    private static final long serialVersionUID = 7164576114486369203L;

    private IAtomContainer itsTotalMolecule;
    private IAtomContainer itsUnitMolecule;
    private TwoDimensionList<Integer> itsDistanceMatrix;
    private TwoDimensionList<Integer> itsNTerminalDistanceArray2dList;
    private TwoDimensionList<Integer> itsCTerminalDistanceArray2dList;
    private List<Integer> itsStartAtomNumberList;
    //constant Integer variable
    public static final int NON_CONNECTED_ATOM_PAIR = -1;
    private final int DEFAULT_DISTANCE_IN_PROTEIN = 0;
    private final int START_INDEX_IN_ATOM_NUMBER_RANGE = 0;
    private final int END_INDEX_IN_ATOM_NUMBER_RANGE = 1;

    public TopologicalDistanceMatrix() {
        this.itsDistanceMatrix = new TwoDimensionList<>();
    }

    public TopologicalDistanceMatrix(Protein theProtein) {
        this.__initializeDistanceMatrixUsingProtein(theProtein);
    }

    public TopologicalDistanceMatrix(Protein theProtein, IAtomContainer theLigand) {
        this.__generateDistanceMatrixUsingProteinLigandComplex(theProtein, theLigand);
    }

    public TopologicalDistanceMatrix(IAtomContainer theMolecule) {
        this.itsTotalMolecule = theMolecule;
        this.itsUnitMolecule = theMolecule;
        this.itsDistanceMatrix = this.__generateDistanceMatrix();
    }

    public TopologicalDistanceMatrix(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule) {
        this.itsTotalMolecule = theTotalMolecule;
        this.itsUnitMolecule = theUnitMolecule;
        this.itsDistanceMatrix = this.__generateDistanceMatrix();
    }

    public TopologicalDistanceMatrix(TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        try {
            this.itsTotalMolecule = theTopologicalDistanceMatrix.itsTotalMolecule.clone();
            this.itsUnitMolecule = theTopologicalDistanceMatrix.itsUnitMolecule.clone();
            this.itsDistanceMatrix = new TwoDimensionList<>(theTopologicalDistanceMatrix.itsDistanceMatrix);
        } catch (CloneNotSupportedException ex) {
        }
    }

    public IAtomContainer getMolecule() {
        return itsUnitMolecule;
    }

    public void setMolecule(IAtomContainer theMolecule) {
        this.itsUnitMolecule = theMolecule;
    }

    public TwoDimensionList<Integer> getDistanceMatrix() {
        return itsDistanceMatrix;
    }

    public void setDistanceMatrix(TwoDimensionList<Integer> theDistanceMatrix) {
        this.itsDistanceMatrix = theDistanceMatrix;
    }

    public TwoDimensionList<Integer> setDistanceMatrix() {
        return itsDistanceMatrix;
    }

    public int getDistance(int theFirstAtomNumber, int theSecondAtomNumber) {
        int theAtomCountInUnitMolecule = this.itsUnitMolecule.getAtomCount();

        if ((theFirstAtomNumber / theAtomCountInUnitMolecule) == (theSecondAtomNumber / theAtomCountInUnitMolecule)) {
            return this.itsDistanceMatrix.get(theFirstAtomNumber % theAtomCountInUnitMolecule, theSecondAtomNumber % theAtomCountInUnitMolecule);
        } else {
            return TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR;
        }
    }

    private void __generateDistanceMatrixUsingProteinLigandComplex(Protein theProtein, IAtomContainer theLigand) {
        this.__initializeDistanceMatrixUsingProtein(theProtein);
        this.__addLigandDistanceMatrix(theLigand);
    }

    private void __addLigandDistanceMatrix(IAtomContainer theLigand) {
        this.itsTotalMolecule = theLigand;
        this.itsUnitMolecule = theLigand;
        
        TwoDimensionList<Integer> theLigandDistanceMatrix = this.__generateDistanceMatrix();
        int theLigandAtomCount = theLigand.getAtomCount();
        
        for(List<Integer> theProteinDistanceArray : this.itsDistanceMatrix) {
            for(int vi = 0; vi < theLigandAtomCount; vi++) {
                theProteinDistanceArray.add(TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR);
            }
        }
        
        for(List<Integer> theLigandDistanceArray : theLigandDistanceMatrix) {
            List<Integer> theNewLigandDistanceArray = new ArrayList<>();
            
            for(int vi = 0, vEnd = this.itsDistanceMatrix.size(); vi < vEnd; vi++) {
                theNewLigandDistanceArray.add(TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR);
            }
            
            theNewLigandDistanceArray.addAll(theLigandDistanceArray);
            this.itsDistanceMatrix.add(theNewLigandDistanceArray);
        }
    }
    
    private void __initializeDistanceMatrixUsingProtein(Protein theProtein) {
        TwoDimensionList<Integer> theAtomNumberRangeInProtein = this.__getAtomNumberRangeInProtein(theProtein);

        this.itsDistanceMatrix = new TwoDimensionList<>();
        this.itsNTerminalDistanceArray2dList = new TwoDimensionList();
        this.itsCTerminalDistanceArray2dList = new TwoDimensionList();

        for (int ci = 0, cEnd = theProtein.getChainList().size(); ci < cEnd; ci++) {
            this.__initializeDistanceMatrixUsingProtein(theProtein.getChainList().get(ci), theAtomNumberRangeInProtein.get(ci));
            this.__fillDistanceMatrixUsingChain(theProtein.getChainList().get(ci), theAtomNumberRangeInProtein.get(ci));
            
            this.itsNTerminalDistanceArray2dList.clear();
            this.itsCTerminalDistanceArray2dList.clear();
        }
    }

    private void __fillDistanceMatrixUsingChain(Chain theChain, List<Integer> theAtomNumberRangeInProtein) {
        for(int fi = 0, fEnd = theChain.getAminoAcidList().size(); fi < fEnd; fi++) {
            for(int si = 0; si < fEnd ; si++) {
                if(si != fi) {
                    this.__fillDistanceMatrixUsingChain(theChain, fi, si, theAtomNumberRangeInProtein);
                }
            }
        }
    }
    
    private void __fillDistanceMatrixUsingChain(Chain theChain, int theFirstAminoAcidIndex, int theSecondAminoAcidIndex, List<Integer> theAtomNumberRangeInProtein) {
        List<Integer> theFirstAminoAcidDistanceArray;
        List<Integer> theSecondAminoAcidDistanceArray;
        
        if(theFirstAminoAcidIndex > theSecondAminoAcidIndex) {
            theFirstAminoAcidDistanceArray = this.itsNTerminalDistanceArray2dList.get(theFirstAminoAcidIndex);
            theSecondAminoAcidDistanceArray = this.itsCTerminalDistanceArray2dList.get(theSecondAminoAcidIndex);
        } else if(theFirstAminoAcidIndex < theSecondAminoAcidIndex) {
            theFirstAminoAcidDistanceArray = this.itsCTerminalDistanceArray2dList.get(theFirstAminoAcidIndex);
            theSecondAminoAcidDistanceArray = this.itsNTerminalDistanceArray2dList.get(theSecondAminoAcidIndex);
        } else {
            theFirstAminoAcidDistanceArray = null;
            theSecondAminoAcidDistanceArray = null;
        }
        
        for(int ai = 0, aEnd = theChain.getAminoAcidList().get(theFirstAminoAcidIndex).getMolecule().getAtomCount(); ai < aEnd; ai++) {
            int theBaseDistance = theFirstAminoAcidDistanceArray.get(ai);
            
            for(int di = 0, dEnd = theSecondAminoAcidDistanceArray.size(); di < dEnd; di++) {
                this.itsDistanceMatrix.setValue(this.itsStartAtomNumberList.get(theFirstAminoAcidIndex) + ai, this.itsStartAtomNumberList.get(theSecondAminoAcidIndex) + di,
                        theBaseDistance + theSecondAminoAcidDistanceArray.get(di) + (Math.abs(theFirstAminoAcidIndex - theSecondAminoAcidIndex)-1) * 3 + 1);
            }
        }
    }
    
    private Integer __getAtomInAminoAcidIndex(AminoAcid theAminoAcid, String theAtomSynonum) {
        for (int ai = 0, aEnd = theAminoAcid.getMolecule().getAtomCount(); ai < aEnd; ai++) {
            if (theAminoAcid.getMolecule().getAtom(ai).getProperty(AminoAcidInformation.ATOM_NAME).toString().equals(theAtomSynonum)) {
                return ai;
            }
        }

        return null;
    }
    
    private void __initializeDistanceMatrixUsingProtein(Chain theChain, List<Integer> theAtomNumberRangeInProtein) {
        int theAtomCount = 0;

        this.itsStartAtomNumberList = new ArrayList<>();
        
        for (AminoAcid theAminoAcid : theChain.getAminoAcidList()) {
            this.itsStartAtomNumberList.add(theAtomCount);
            this.__initializeDistanceMatrixUsingProtein(theAminoAcid, theAtomCount, theAtomNumberRangeInProtein);
            theAtomCount += theAminoAcid.getMolecule().getAtomCount();
        }
    }

    private TwoDimensionList<Integer> __getAtomNumberRangeInProtein(Protein theProtein) {
        TwoDimensionList<Integer> theAtomNumberRangeInProtein = new TwoDimensionList<>();

        for (int ci = 0, cEnd = theProtein.getChainList().size(); ci < cEnd; ci++) {
            List<Integer> theAtomNumberRange = new ArrayList<>();

            if (ci == 0) {
                theAtomNumberRange.add(0);
                theAtomNumberRange.add(theProtein.getChainList().get(ci).getMolecule().getAtomCount());
            } else {
                int theStartIndex = theAtomNumberRangeInProtein.get(ci - 1, this.END_INDEX_IN_ATOM_NUMBER_RANGE);

                theAtomNumberRange.add(theStartIndex);
                theAtomNumberRange.add(theStartIndex + theProtein.getChainList().get(ci).getMolecule().getAtomCount());
            }

            theAtomNumberRangeInProtein.add(theAtomNumberRange);
        }

        return theAtomNumberRangeInProtein;
    }

    private void __initializeDistanceMatrixUsingProtein(AminoAcid theAminoAcid, int thePreviousAtomCount, List<Integer> theChainAtomNumberRange) {
        for (int ai = 0, aEnd = theAminoAcid.getMolecule().getAtomCount(); ai < aEnd; ai++) {
            List<Integer> theDistanceArray = this.__initializeDistanceArrayUsingProtein(thePreviousAtomCount, theChainAtomNumberRange);
            List<Integer> theAminoAcidDistanceArray = this.generateDistanceArray(theAminoAcid.getMolecule(), ai);

            theDistanceArray.addAll(theAminoAcidDistanceArray);
            
            if(theAminoAcid.getMolecule().getAtom(ai).getProperty(AminoAcidInformation.ATOM_NAME).equals(AminoAcidInformation.BACK_BONE_NITROGEN)) {
                this.itsNTerminalDistanceArray2dList.add(theAminoAcidDistanceArray);
            } else if(theAminoAcid.getMolecule().getAtom(ai).getProperty(AminoAcidInformation.ATOM_NAME).equals(AminoAcidInformation.BACK_BONE_CARBON_IN_AMIDE)) {
                this.itsCTerminalDistanceArray2dList.add(theAminoAcidDistanceArray);
            }

            for (int vi = thePreviousAtomCount + theAminoAcid.getMolecule().getAtomCount(); vi < theChainAtomNumberRange.get(this.END_INDEX_IN_ATOM_NUMBER_RANGE); vi++) {
                if (theChainAtomNumberRange.get(this.START_INDEX_IN_ATOM_NUMBER_RANGE) <= vi && theChainAtomNumberRange.get(this.END_INDEX_IN_ATOM_NUMBER_RANGE) >= vi) {
                    theDistanceArray.add(this.DEFAULT_DISTANCE_IN_PROTEIN);
                } else {
                    theDistanceArray.add(TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR);
                }
            }

            this.itsDistanceMatrix.add(theDistanceArray);
        }
    }

    private List<Integer> __initializeDistanceArrayUsingProtein(int theListSize, List<Integer> theChainAtomNumberRange) {
        List<Integer> theList = new ArrayList<>();

        for (int vi = 0; vi < theListSize; vi++) {
            if (theChainAtomNumberRange.get(this.START_INDEX_IN_ATOM_NUMBER_RANGE) <= vi && theChainAtomNumberRange.get(this.END_INDEX_IN_ATOM_NUMBER_RANGE) >= vi) {
                theList.add(this.DEFAULT_DISTANCE_IN_PROTEIN);
            } else {
                theList.add(TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR);
            }
        }

        return theList;
    }

    private TwoDimensionList<Integer> __generateDistanceMatrix() {
        TwoDimensionList<Integer> theDistanceMatrix = new TwoDimensionList<>();

        for (int ai = 0, aEnd = this.itsUnitMolecule.getAtomCount(); ai < aEnd; ai++) {
            List<Integer> theDistanceArray = this.__generateDistanceArray(ai);

            theDistanceMatrix.add(this.__generateDistanceArray(ai));
        }

        return theDistanceMatrix;
    }

    public List<Integer> generateDistanceArray(IAtomContainer theMolecule, int theTargetAtomNumber) {
        this.itsTotalMolecule = theMolecule;
        this.itsUnitMolecule = theMolecule;
        
        return this.__generateDistanceArray(theTargetAtomNumber);
    }

    private List<Integer> __generateDistanceArray(int theIndexOfAtom) {
        List<Integer> theDistanceArray = this.__intializeDistanceArray();
        List<Integer> theStartAtomNumberList = new ArrayList<>();
        List<Integer> theUsedAtomNumberList = new ArrayList<>();
        int theDistance = 0;

        theStartAtomNumberList.add(theIndexOfAtom);
        theUsedAtomNumberList.add(theIndexOfAtom);
        theDistanceArray.set(theIndexOfAtom, 0);

        while (!theStartAtomNumberList.isEmpty()) {
            theDistance++;
            theStartAtomNumberList = this.__generateConnectedAtomNumberListNotUsed(theStartAtomNumberList, theUsedAtomNumberList);

            for (Integer theStartAtomNumber : theStartAtomNumberList) {
                if (theDistanceArray.get(theStartAtomNumber) == TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR) {
                    theDistanceArray.set(theStartAtomNumber, theDistance);
                }
            }
        }

        return theDistanceArray;
    }

    public int getTopologicalDistance(IAtomContainer theMolecule, int theFirstAtomIndex, int theSecondAtomIndex) {
        this.itsUnitMolecule = theMolecule;

        List<Integer> theStartAtomNumberList = new ArrayList<>();
        List<Integer> theUsedAtomNumberList = new ArrayList<>();
        int theDistance = 0;

        theStartAtomNumberList.add(theFirstAtomIndex);
        theUsedAtomNumberList.add(theFirstAtomIndex);

        while (!theStartAtomNumberList.isEmpty()) {
            theDistance++;
            theStartAtomNumberList = this.__generateConnectedAtomNumberListNotUsed(theStartAtomNumberList, theUsedAtomNumberList);

            if (theStartAtomNumberList.contains(theSecondAtomIndex)) {
                return theDistance;
            }
        }

        return TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR;
    }

    private List<Integer> __intializeDistanceArray() {
        List<Integer> theDistanceArray = new ArrayList<>();

        for (int ai = 0, aEnd = this.itsUnitMolecule.getAtomCount(); ai < aEnd; ai++) {
            theDistanceArray.add(TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR);
        }

        return theDistanceArray;
    }

    private List<Integer> __generateConnectedAtomNumberListNotUsed(List<Integer> theStartAtomNumberList, List<Integer> theUsedAtomNumberList) {
        List<Integer> theConnectedAtomNumberListNotUsed = new ArrayList<>();

        for (Integer theStartAtomNumber : theStartAtomNumberList) {
            List<Integer> theConnectedAtomNumberList = this.__getConnectedAtomNumberList(theStartAtomNumber);

            for (Integer theAtomNumber : theConnectedAtomNumberList) {
                if (!theUsedAtomNumberList.contains(theAtomNumber)) {
                    theConnectedAtomNumberListNotUsed.add(theAtomNumber);
                    theUsedAtomNumberList.add(theAtomNumber);
                }
            }
        }

        return theConnectedAtomNumberListNotUsed;
    }

    private List<Integer> __getConnectedAtomNumberList(int theIndexOfAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(this.getMolecule().getAtom(theIndexOfAtom));
        List<Integer> theConnectedAtomNumberList = new ArrayList<>();

        for (IAtom theAtom : theConnectedAtomList) {
            theConnectedAtomNumberList.add(this.getMolecule().getAtomNumber(theAtom));
        }

        return theConnectedAtomNumberList;
    }

    public List<Integer> getDistanceArray(int theIndex) {
        if (this.itsTotalMolecule != null) {
            int theMoleculeIndex = theIndex / this.itsUnitMolecule.getAtomCount();
            List<Integer> theDistanceList = new ArrayList<>();

            for (int vi = 0, vEnd = this.itsTotalMolecule.getAtomCount() / this.itsUnitMolecule.getAtomCount(); vi < vEnd; vi++) {
                if (vi != theMoleculeIndex) {
                    theDistanceList.addAll(this.__intializeDistanceArray());
                } else {
                    theDistanceList.addAll(this.getDistanceMatrix().get(theIndex % this.itsUnitMolecule.getAtomCount()));
                }
            }

            return theDistanceList;
        }

        return this.getDistanceMatrix().get(theIndex);
    }

    public int getNumberOfAtomAtDistance(int theAtomNumber, int theCriterionDistance) {
        int theNumberOfAtomAtDistance = 0;

        for (Integer theDistance : this.getDistanceArray(theAtomNumber)) {
            if (theDistance == theCriterionDistance) {
                theNumberOfAtomAtDistance++;
            }
        }

        return theNumberOfAtomAtDistance;
    }

    public int indexOfDistance(int theAtomNumber, int theDistance, int theStartIndex) {
        for (int di = theStartIndex, dEnd = this.getDistanceArray(theAtomNumber).size(); di < dEnd; di++) {
            if (this.getDistance(theAtomNumber, di) == theDistance) {
                return di;
            }
        }

        return TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR;
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        for (List<Integer> theList : this.getDistanceMatrix()) {
            theStringBuilder.append(theList).append("\n");
        }

        return theStringBuilder.toString();
    }

    public int getMaximumDistance() {
        int theMaximumDistance = 0;

        for (List<Integer> theDistanceArray : this.itsDistanceMatrix) {
            int theMaximumValue = Collections.max(theDistanceArray);

            if (theMaximumDistance < theMaximumValue) {
                theMaximumDistance = theMaximumValue;
            }
        }

        return theMaximumDistance;
    }
}
