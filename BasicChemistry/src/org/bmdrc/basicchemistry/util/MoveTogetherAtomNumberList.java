/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bmdrc.basicchemistry.tool.RingPerception;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.ui.util.ThreeDimensionList;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IRingSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class MoveTogetherAtomNumberList implements Serializable {

    private static final long serialVersionUID = -5198437318963571281L;

    private IAtomContainer itsTotalMolecule;
    private IAtomContainer itsUnitMolecule;
    private ThreeDimensionList<Integer> itsMoveTogetherAtomNumber3dList;
    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;
    private IRingSet itsRingSet;
    private List<Integer> itsNotCalculateAtomNumberList;

    public MoveTogetherAtomNumberList(IAtomContainer theMolecule) {
        this(theMolecule, theMolecule);
    }

    public MoveTogetherAtomNumberList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theMolecule, theNotCalculateAtomNumberList);
    }

    public MoveTogetherAtomNumberList(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule) {
        this(theTotalMolecule, theUnitMolecule, new ArrayList<Integer>());
    }

    public MoveTogetherAtomNumberList(Protein theProtein, IAtomContainer theLigand) {
        this.itsTotalMolecule = this.__getProteinLigandComplex(theProtein.getMolecule(), theLigand);
        this.itsUnitMolecule = this.itsTotalMolecule;
        this.itsTopologicalDistanceMatrix = new TopologicalDistanceMatrix(theProtein, theLigand);

        RingPerception theRingPerception = new RingPerception();
        this.itsRingSet = theRingPerception.recognizeRing(theProtein);
        this.itsRingSet.add(theRingPerception.recognizeRing(theLigand));
        this.initializeMoveTogetherAtomNumberMap(this.itsUnitMolecule);
    }

    public MoveTogetherAtomNumberList(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this.itsTopologicalDistanceMatrix = new TopologicalDistanceMatrix(theTotalMolecule, theUnitMolecule);
        this.itsTotalMolecule = theTotalMolecule;
        this.itsUnitMolecule = theUnitMolecule;
        this.itsNotCalculateAtomNumberList = theNotCalculateAtomNumberList;

        RingPerception theRingPerception = new RingPerception();
        this.itsRingSet = theRingPerception.recognizeRing(theUnitMolecule, this.itsTopologicalDistanceMatrix);
        this.initializeMoveTogetherAtomNumberMap(this.itsUnitMolecule);
    }

    public MoveTogetherAtomNumberList(MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        try {
            this.itsMoveTogetherAtomNumber3dList = new ThreeDimensionList<>(theMoveTogetherAtomNumberMap.itsMoveTogetherAtomNumber3dList);
            this.itsTopologicalDistanceMatrix = new TopologicalDistanceMatrix(theMoveTogetherAtomNumberMap.itsTopologicalDistanceMatrix);
            this.itsRingSet = (IRingSet) theMoveTogetherAtomNumberMap.itsRingSet.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println("RingSet clone error in MoveTogetheratomnumber constructor");
        }
    }

    public TopologicalDistanceMatrix getTopologicalDistanceMatrix() {
        return itsTopologicalDistanceMatrix;
    }

    public void setTopologicalDistanceMatrix(TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.itsTopologicalDistanceMatrix = theTopologicalDistanceMatrix;
    }

    public IRingSet getRingSet() {
        return itsRingSet;
    }

    public IAtomContainer getUnitMolecule() {
        return itsUnitMolecule;
    }

    public List<Integer> get(int theTemplateAtomNumber, int theCounterAtomNumber) {
        int theUnitMoleculeAtomCount = this.itsUnitMolecule.getAtomCount();

        if (theTemplateAtomNumber / theUnitMoleculeAtomCount == theCounterAtomNumber / theUnitMoleculeAtomCount) {
            return this.itsMoveTogetherAtomNumber3dList.get(theTemplateAtomNumber % this.itsUnitMolecule.getAtomCount(), theCounterAtomNumber % this.itsUnitMolecule.getAtomCount());
        } else {
            return this.__getUnitMoleculeList(theTemplateAtomNumber);
        }
//        return this.getMoveTogetherAtomNumberList(theTemplateAtomNumber, theCounterAtomNumber);
    }

    private List<Integer> __getUnitMoleculeList(int theTemplateAtomNumber) {
        int theMoleculeIndex = theTemplateAtomNumber / this.itsUnitMolecule.getAtomCount();
        List<Integer> theAtomIndexList = new ArrayList<>();

        for (int ai = theMoleculeIndex * this.itsUnitMolecule.getAtomCount(), aEnd = (theMoleculeIndex + 1) * this.itsUnitMolecule.getAtomCount(); ai < aEnd; ai++) {
            theAtomIndexList.add(ai);
        }

        return theAtomIndexList;
    }

    private IAtomContainer __getProteinLigandComplex(IAtomContainer... theMolecules) {
        IAtomContainer theComplex = new AtomContainer();

        for (IAtomContainer theMolecule : theMolecules) {
            theComplex.add(theMolecule);
        }

        return theComplex;
    }

    public void initializeMoveTogetherAtomNumberMap(IAtomContainer theMolecule) {
        this.itsMoveTogetherAtomNumber3dList = new ThreeDimensionList<Integer>();
        int theAtomCount = theMolecule.getAtomCount();

        for (int fi = 0; fi < theAtomCount; fi++) {
            this.itsMoveTogetherAtomNumber3dList.add(new TwoDimensionList<Integer>());

            for (int si = 0; si < theAtomCount; si++) {
                this.itsMoveTogetherAtomNumber3dList.get(fi).add(new ArrayList<Integer>());

                if (fi != si) {
                    if (this.itsTopologicalDistanceMatrix.getDistance(fi, si) != TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR) {
                        this.itsMoveTogetherAtomNumber3dList.set(fi, si, this.__initializeMoveTogetherAtomNumberMapUsingTotalMolecule(fi, si));
                    } else {
                        this.itsMoveTogetherAtomNumber3dList.set(fi, si, this.__initializeMoveTogetherAtomNumberMap(fi, si));
                    }
                }
            }
        }
    }

    private List<Integer> __initializeMoveTogetherAtomNumberMap(int theTemplateAtomNumber, int theCounterAtomNumber) {
        List<Integer> theDistanceArray = this.itsTopologicalDistanceMatrix.getDistanceArray(theTemplateAtomNumber);
        List<Integer> theAtomNumberList = new ArrayList<>();

        for (int vi = 0, vEnd = theDistanceArray.size(); vi < vEnd; vi++) {
            if (theDistanceArray.get(vi) != TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR) {
                theAtomNumberList.add(vi);
            }
        }

        return theAtomNumberList;
    }

    private List<Integer> __initializeMoveTogetherAtomNumberMapUsingTotalMolecule(int theTemplateAtomNumber,
            int theCounterAtomNumber) {
        List<Integer> theMoveTogetherAtomNumberList = this.__initializeMoveTogetherAtomNumberList(theTemplateAtomNumber, theCounterAtomNumber);
        List<Integer> theNewAddedAtomNumberList = new ArrayList<>(theMoveTogetherAtomNumberList);

        do {
            List<Integer> theCheckList = new ArrayList<>(theNewAddedAtomNumberList);

            theNewAddedAtomNumberList.clear();

            for (Integer theMoveTogetherAtomNumber : theCheckList) {
                List<IAtom> theConnectedAtomList = this.itsTotalMolecule.getConnectedAtomsList(this.itsTotalMolecule.getAtom(theMoveTogetherAtomNumber));

                for (IAtom theConnectedAtom : theConnectedAtomList) {
                    int theConnectedAtomIndex = this.itsTotalMolecule.getAtomNumber(theConnectedAtom);
                    
                    if(theConnectedAtomIndex != theCounterAtomNumber && theConnectedAtomIndex != theTemplateAtomNumber) {
                    int theConnectedAtomNumber = this.itsTotalMolecule.getAtomNumber(theConnectedAtom);

                    if (theTemplateAtomNumber > theConnectedAtomNumber) {
                        List<Integer> theAddingAtomIndexList = this.get(theConnectedAtomNumber, theMoveTogetherAtomNumber);

                        for (Integer theAddingAtomIndex : theAddingAtomIndexList) {
                            if (!theMoveTogetherAtomNumberList.contains(theAddingAtomIndex) && !theAddingAtomIndex.equals(theCounterAtomNumber)) {
                                theMoveTogetherAtomNumberList.add(theAddingAtomIndex);
                            }
                        }
                    } else if (!this.__containSameRing(this.itsTotalMolecule.getAtom(theTemplateAtomNumber), theConnectedAtom)
                            && !theMoveTogetherAtomNumberList.contains(theConnectedAtomNumber)) {
                        theMoveTogetherAtomNumberList.add(theConnectedAtomNumber);
                        theNewAddedAtomNumberList.add(theConnectedAtomNumber);
                    }
                }
                }
            }
        } while (!theNewAddedAtomNumberList.isEmpty());

        return theMoveTogetherAtomNumberList;
    }

    private List<Integer> __initializeMoveTogetherAtomNumberList(int theTemplateAtomNumber,
            int theCounterAtomNumber) {
        List<Integer> theMoveTogetherAtomNumberList = new ArrayList<>();
        List<IAtom> theConnectedAtomList = this.itsTotalMolecule.getConnectedAtomsList(this.itsTotalMolecule.getAtom(theTemplateAtomNumber));

        theMoveTogetherAtomNumberList.add(theTemplateAtomNumber);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (!this.__containSameRing(this.itsTotalMolecule.getAtom(theTemplateAtomNumber), theConnectedAtom)
                    && !this.__isNotMoveAtom(theTemplateAtomNumber, theCounterAtomNumber, this.itsTotalMolecule.getAtomNumber(theConnectedAtom))) {
                theMoveTogetherAtomNumberList.add(this.itsTotalMolecule.getAtomNumber(theConnectedAtom));
            }
        }

        return theMoveTogetherAtomNumberList;
    }

    private boolean __containSameRing(IAtom theTemplateAtom, IAtom theTestAtom) {
        int theTemplateAtomNumberInUnitMolecule = this.itsTotalMolecule.getAtomNumber(theTemplateAtom) % this.itsUnitMolecule.getAtomCount();
        int theTestAtomNumberInUnitMolecule = this.itsTotalMolecule.getAtomNumber(theTestAtom) % this.itsUnitMolecule.getAtomCount();
        IAtom theTemplateAtomInUnitMolecule = this.itsUnitMolecule.getAtom(theTemplateAtomNumberInUnitMolecule);
        IAtom theTestAtomInUnitMolecule = this.itsUnitMolecule.getAtom(theTestAtomNumberInUnitMolecule);

        for (IAtomContainer theRing : this.itsRingSet.atomContainers()) {
            if (theRing.contains(theTemplateAtomInUnitMolecule) && theRing.contains(theTestAtomInUnitMolecule)) {
                return true;
            }
        }

        return false;
    }

    private boolean __isNotMoveAtom(int theTemplateAtomNumber, int theCounterAtomNumber, int theConnectedAtomNumber) {
        int theTopologicalDistanceBetweenTemplateAndCounter = this.itsTopologicalDistanceMatrix.getDistance(theTemplateAtomNumber, theCounterAtomNumber);
        int theTopologicalDisdtanceBetweenTemplateAndConnectedAtom = this.itsTopologicalDistanceMatrix.getDistance(theTemplateAtomNumber, theConnectedAtomNumber);
        int theTopologicalDistanceBetwenCounterAndConnectedAtom = this.itsTopologicalDistanceMatrix.getDistance(theConnectedAtomNumber, theCounterAtomNumber);

        return theTopologicalDistanceBetweenTemplateAndCounter == (theTopologicalDisdtanceBetweenTemplateAndConnectedAtom + theTopologicalDistanceBetwenCounterAndConnectedAtom);
    }
}
