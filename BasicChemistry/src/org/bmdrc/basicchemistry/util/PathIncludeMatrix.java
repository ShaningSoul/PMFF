/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.ui.util.FourDimensionList;
import org.bmdrc.ui.util.ThreeDimensionList;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 24
 */
public class PathIncludeMatrix implements Serializable {

    private static final long serialVersionUID = 4046493226281979274L;

    private IAtomContainer itsTotalMolecule;
    private IAtomContainer itsUnitMolecule;
    private FourDimensionList<IBond> itsPMatrix;
    private FourDimensionList<IBond> itsPCommaMatrix;
    private FourDimensionList<IBond> itsMatrixAtSpecificDistance;
    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;

    public PathIncludeMatrix() {
        this.itsPCommaMatrix = new FourDimensionList<>();
        this.itsPCommaMatrix = new FourDimensionList<>();
        this.itsTotalMolecule = new AtomContainer();
    }

    public PathIncludeMatrix(PathIncludeMatrix theList) {
        try {
            this.itsPCommaMatrix = new FourDimensionList<>();
            this.itsPCommaMatrix = new FourDimensionList<>();
            this.itsTotalMolecule = (IAtomContainer) theList.itsTotalMolecule.clone();
        } catch (CloneNotSupportedException ex) {
        }
    }

    public PathIncludeMatrix(IAtomContainer theMolecule) {
        this(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }
    
    public PathIncludeMatrix(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.itsPCommaMatrix = new FourDimensionList<>();
        this.itsPCommaMatrix = new FourDimensionList<>();
        this.generateMatrix(theMolecule, theTopologicalDistanceMatrix);
    }

    public FourDimensionList<IBond> getPMatrix() {
        return itsPMatrix;
    }

    public FourDimensionList<IBond> getPCommaMatrix() {
        return itsPCommaMatrix;
    }

    public TopologicalDistanceMatrix getTopologicalDistanceMatrix() {
        return itsTopologicalDistanceMatrix;
    }
    
    public void generateMatrix(IAtomContainer theMolecule) {
        this.generateMatrix(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }

    public void generateMatrix(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.itsTotalMolecule = theMolecule;
        this.itsUnitMolecule = theMolecule;
        this.itsTopologicalDistanceMatrix = theTopologicalDistanceMatrix;
        
        this.__generateMatrix();
    }
    
    public void generateMatrix(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.itsTotalMolecule = theTotalMolecule;
        this.itsUnitMolecule = theUnitMolecule;
        this.itsTopologicalDistanceMatrix = theTopologicalDistanceMatrix;
        
        this.__generateMatrix();
    }
    
    private void __generateMatrix() {
        this.itsPMatrix = this.__initializeList();
        this.itsPCommaMatrix = this.__initializeList();
        
        int theMaximumDistance = this.itsTopologicalDistanceMatrix.getMaximumDistance();

        this.__initializeMatrixAtSpecificDistanceAndPMatrix();
            
        for (int di = 0; di < theMaximumDistance; di++) {
            this.__generateNextDistanceMatrix();
            this.__setPAndPCommaMatrix(di+2);
        }
    }
    
    private void __setPAndPCommaMatrix(int theTopologicalDistance) {
        int theAtomCount = this.itsTotalMolecule.getAtomCount();

        for (int ri = 0; ri < theAtomCount; ri++) {
            for (int ci = 0; ci < theAtomCount; ci++) {
                if (!this.itsMatrixAtSpecificDistance.get(ri, ci).isEmpty()) {
                    if (this.itsPMatrix.get(ri, ci).isEmpty()) {
                        this.itsPMatrix.setValue(ri, ci, new TwoDimensionList<>(this.itsMatrixAtSpecificDistance.get(ri, ci)));
                    } else if (this.itsPCommaMatrix.get(ri, ci).isEmpty() && this.itsTopologicalDistanceMatrix.getDistance(ri, ci) + 1 == theTopologicalDistance) {
                        this.itsPCommaMatrix.setValue(ri, ci, new TwoDimensionList<>(this.itsMatrixAtSpecificDistance.get(ri, ci)));
                    }
                }
            }
        }
    }

    private void __generateNextDistanceMatrix() {
        FourDimensionList<IBond> theNextDistanceMatrix = this.__initializeList();
        int theAtomCount = this.itsTotalMolecule.getAtomCount();

        for (int ri = 0; ri < theAtomCount; ri++) {
            for (int ci = 0; ci < theAtomCount; ci++) {
                if (ri != ci && !this.itsMatrixAtSpecificDistance.get(ri, ci).isEmpty()) {
                    this.__generateNextDistanceMatrix(theNextDistanceMatrix, ri, ci);
                }
            }
        }

        this.itsMatrixAtSpecificDistance = theNextDistanceMatrix;
    }

    private void __generateNextDistanceMatrix(FourDimensionList<IBond> theNextDistanceMatrix, int theStartAtomNumber, int theEndAtomNumber) {
        List<IAtom> theConnectedAtomList = this.itsTotalMolecule.getConnectedAtomsList(this.itsTotalMolecule.getAtom(theEndAtomNumber));

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            int theConnectedAtomNumber = this.itsTotalMolecule.getAtomNumber(theConnectedAtom);
            IBond theBond = this.itsTotalMolecule.getBond(this.itsTotalMolecule.getAtom(theEndAtomNumber), theConnectedAtom);
            TwoDimensionList<IBond> theCopiedPathList = new TwoDimensionList<>(this.itsMatrixAtSpecificDistance.get(theStartAtomNumber, theEndAtomNumber));
            TwoDimensionList<IBond> theNewPathList = new TwoDimensionList<>();

            for (List<IBond> theBondList : theCopiedPathList) {
                if (!theBondList.contains(theBond)) {
                    theBondList.add(theBond);
                    Collections.sort(theBondList, this.__getBondComparator());
                    
                    if(!theNextDistanceMatrix.get(theStartAtomNumber, theConnectedAtomNumber).contains(theBondList)) {
                        theNewPathList.add(theBondList);
                    }
                }
            }

            theNextDistanceMatrix.get(theStartAtomNumber, theConnectedAtomNumber).addAll(theNewPathList);
            theNextDistanceMatrix.get(theConnectedAtomNumber, theStartAtomNumber).addAll(theNewPathList);
        }
    }

    private Comparator<IBond> __getBondComparator() {
        Comparator<IBond> theComparator = new Comparator<IBond>() {
            @Override
            public int compare(IBond o1, IBond o2) {
                return ((Integer)itsTotalMolecule.getBondNumber(o1)).compareTo((Integer)itsTotalMolecule.getBondNumber(o2));
            }
        };
        
        return theComparator;
    }
    
    private void __initializeMatrixAtSpecificDistanceAndPMatrix() {
        this.itsMatrixAtSpecificDistance = this.__initializeList();
        
        for (IAtom theAtom : this.itsTotalMolecule.atoms()) {
            List<IAtom> theConnectedAtomList = this.itsTotalMolecule.getConnectedAtomsList(theAtom);
            int theFirstAtomIndex = this.itsTotalMolecule.getAtomNumber(theAtom);

            for (IAtom theConnectedAtom : theConnectedAtomList) {
                List<IBond> theAddedList = new ArrayList<>();
                int theSecondAtomIndex = this.itsTotalMolecule.getAtomNumber(theConnectedAtom);

                theAddedList.add(this.itsTotalMolecule.getBond(theAtom, theConnectedAtom));
                this.itsMatrixAtSpecificDistance.get(theFirstAtomIndex, theSecondAtomIndex).add(theAddedList);
                this.itsPMatrix.get(theFirstAtomIndex, theSecondAtomIndex).add(theAddedList);
            }
        }
    }

    private FourDimensionList<IBond> __initializeList() {
        FourDimensionList<IBond> theInitialList = new FourDimensionList<>();
        int theAtomCount = this.itsUnitMolecule.getAtomCount();
        
        for (int ri = 0; ri < theAtomCount; ri++) {
            theInitialList.add(new ThreeDimensionList<IBond>());

            for (int ci = 0; ci < theAtomCount; ci++) {
                theInitialList.get(ri).add(new TwoDimensionList<IBond>());
            }
        }

        return theInitialList;
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        theStringBuilder.append("P matrix\n");
        theStringBuilder.append(this.__toString(this.itsPMatrix));
        theStringBuilder.append("\nP' matrix\n");
        theStringBuilder.append(this.__toString(this.itsPCommaMatrix));

        return theStringBuilder.toString();
    }

    private String __toString(FourDimensionList<IBond> theBond4dList) {
        StringBuilder theStringBuilder = new StringBuilder();
        int theAtomCount = this.itsUnitMolecule.getAtomCount();

        for (int ri = 0; ri < theAtomCount; ri++) {
            theStringBuilder.append("[");

            for (int ci = 0; ci < theAtomCount; ci++) {
                TwoDimensionList<IBond> theBond2dList = theBond4dList.get(ri, ci);

                theStringBuilder.append(this.__toString(theBond2dList));

                if (ci < theAtomCount - 1) {
                    theStringBuilder.append(", ");
                }
            }

            theStringBuilder.append("]\n");
        }

        return theStringBuilder.toString();
    }

    private String __toString(TwoDimensionList<IBond> theBond2dList) {
        StringBuilder theStringBuilder = new StringBuilder();

        for (List<IBond> theBondList : theBond2dList) {
            theStringBuilder.append("(");

            for (IBond theBond : theBondList) {
                theStringBuilder.append("e").append(this.itsTotalMolecule.getBondNumber(theBond));
            }

            theStringBuilder.append(")");
        }

        return theStringBuilder.toString();
    }
}
