/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.sbff.intraenergy.calculableset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.sbff.atomtype.HydrogenBondAcceptorList;
import org.bmdrc.sbff.atomtype.HydrogenBondDonorList;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondParameterSet;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondTotalSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 10. 13
 */
public class CalculableIntraHydrogenBondList extends AbstractCalculableList<CalculableIntraHydrogenBondSet> implements Serializable {

    private static final long serialVersionUID = 1852346819582645393L;

    private enum StringIndex {

        XAtomIndex, HAtomIndex, AAtomIndex, BAtomIndex, BInHAParameter, DInHAParameter, BInXAParameter, DInXAParameter, BInBHParameter, DInBHParameter;
    }

    private Double itsMaximumHydrogenBondLength;
    //constant Double variable
    private static final double DEFAULT_MAXIMUM_HYDROGEN_BOND = 3.0;

    public CalculableIntraHydrogenBondList(CalculableIntraHydrogenBondList theCalculableHydrogenBondList) {
        super(theCalculableHydrogenBondList);
        this.itsMaximumHydrogenBondLength = theCalculableHydrogenBondList.itsMaximumHydrogenBondLength;
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule) {
        this(theMolecule, new CalculableIntraElectroStaticList(theMolecule));
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet) {
        this(theMolecule, theParameterSet, new CalculableIntraElectroStaticList(theMolecule));
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new CalculableIntraElectroStaticList(theMolecule), theNotCalculateAtomNumberList);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theParameterSet, new CalculableIntraElectroStaticList(theMolecule), theNotCalculateAtomNumberList);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, Double theMaximumHydrogenBondLength) {
        this(theMolecule, new CalculableIntraElectroStaticList(theMolecule), theMaximumHydrogenBondLength);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet, Double theMaximumHydrogenBondLength) {
        this(theMolecule, theParameterSet, new CalculableIntraElectroStaticList(theMolecule), theMaximumHydrogenBondLength);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, CalculableIntraElectroStaticList theCalculableElectroStaticList, Double theMaximumHydrogenBondLength) {
        this(theMolecule, theCalculableElectroStaticList.getTopologicalDistanceMatrix(), theCalculableElectroStaticList,
                theMaximumHydrogenBondLength, new ArrayList<Integer>());
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet, CalculableIntraElectroStaticList theCalculableElectroStaticList,
            Double theMaximumHydrogenBondLength) {
        this(theMolecule, theParameterSet, theCalculableElectroStaticList, theMaximumHydrogenBondLength, new ArrayList<Integer>());
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, CalculableIntraElectroStaticList theCalculableElectroStaticList) {
        this(theMolecule, theCalculableElectroStaticList.getTopologicalDistanceMatrix(), theCalculableElectroStaticList,
                CalculableIntraHydrogenBondList.DEFAULT_MAXIMUM_HYDROGEN_BOND, new ArrayList<Integer>());
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet, CalculableIntraElectroStaticList theCalculableElectroStaticList) {
        this(theMolecule, theParameterSet, theCalculableElectroStaticList, CalculableIntraHydrogenBondList.DEFAULT_MAXIMUM_HYDROGEN_BOND, 
                new ArrayList<Integer>());
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, CalculableIntraElectroStaticList theCalculableElectroStaticList,
            List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theCalculableElectroStaticList.getTopologicalDistanceMatrix(), theCalculableElectroStaticList,
                CalculableIntraHydrogenBondList.DEFAULT_MAXIMUM_HYDROGEN_BOND, theNotCalculateAtomNumberList);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, CalculableIntraElectroStaticList theCalculableElectroStaticList,
            List<Integer> theNotCalculateAtomNumberList, boolean theIsUsedMultiThread, int theNumberOfThread) {
        this(theMolecule, theCalculableElectroStaticList.getTopologicalDistanceMatrix(), theCalculableElectroStaticList,
                CalculableIntraHydrogenBondList.DEFAULT_MAXIMUM_HYDROGEN_BOND, theNotCalculateAtomNumberList, theIsUsedMultiThread, theNumberOfThread);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet, CalculableIntraElectroStaticList theCalculableElectroStaticList,
            List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theParameterSet, theCalculableElectroStaticList, CalculableIntraHydrogenBondList.DEFAULT_MAXIMUM_HYDROGEN_BOND, theNotCalculateAtomNumberList);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix,
            CalculableIntraElectroStaticList theCalculableElectroStaticList, Double theMaximumHydrogenBondLength, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffHydrogenBondTotalSet(), theCalculableElectroStaticList, theMaximumHydrogenBondLength, theNotCalculateAtomNumberList);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix,
            CalculableIntraElectroStaticList theCalculableElectroStaticList, Double theMaximumHydrogenBondLength, List<Integer> theNotCalculateAtomNumberList,
            boolean theIsUsedMultiThread, int theNumberOfThread) {
        this(theMolecule, new SbffHydrogenBondTotalSet(), theCalculableElectroStaticList, theMaximumHydrogenBondLength, theNotCalculateAtomNumberList, theIsUsedMultiThread, theNumberOfThread);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet, CalculableIntraElectroStaticList theCalculableElectroStaticList,
            Double theMaximumHydrogenBondLength, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theParameterSet, theCalculableElectroStaticList, theMaximumHydrogenBondLength, theNotCalculateAtomNumberList, false, 1);
    }

    public CalculableIntraHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet, CalculableIntraElectroStaticList theCalculableElectroStaticList,
            Double theMaximumHydrogenBondLength, List<Integer> theNotCalculateAtomNumberList, boolean theIsUsedMultiThread, int theNumberOfThread) {
        super(theMolecule, theNotCalculateAtomNumberList);
        this.itsMaximumHydrogenBondLength = theMaximumHydrogenBondLength;
//        this.itsHydrogenBondAcceptorList = new HydrogenBondAcceptorList();
//        this.itsHydrogenBondDonorList = new HydrogenBondDonorList();
        
        this.__initializeCalculableHydrogenBondList(theCalculableElectroStaticList, theParameterSet);

//        this.itsHydrogenBondAcceptorList = null;
//        this.itsHydrogenBondDonorList = null;
    }

    private void __initializeCalculableHydrogenBondList(CalculableIntraElectroStaticList theCalculableElectroStaticList, 
            SbffHydrogenBondTotalSet theParameterSet) {
        HydrogenBondAcceptorList theHydrogenBondAcceptorList = new HydrogenBondAcceptorList();
        HydrogenBondDonorList theHydrogenBondDonorList = new HydrogenBondDonorList();
        
        for (CalculableIntraElectroStaticSet theCalculableElectroStaticSet : theCalculableElectroStaticList) {
            if (this.__isHydrogenBond(theHydrogenBondAcceptorList, theHydrogenBondDonorList, theCalculableElectroStaticSet)) {
                CalculableIntraHydrogenBondSet theHydrogenBondSet = this.__getCalculableHydrogenBondList(theHydrogenBondAcceptorList, theHydrogenBondDonorList,
                        theCalculableElectroStaticSet, theParameterSet);

                if (theHydrogenBondSet.getHydrogenBondParameter() == null) {
                    continue;
                } else {
                    this.add(theHydrogenBondSet);
                }
            }
        }
    }

    private boolean __isHydrogenBond(HydrogenBondAcceptorList theHydrogenBondAcceptorList, HydrogenBondDonorList theHydrogenBondDonorList, 
            CalculableIntraElectroStaticSet theCalculableElectroStaticSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getSecondAtomIndex());

        if (theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d()) > this.itsMaximumHydrogenBondLength) {
            return false;
        } else if (theHydrogenBondAcceptorList.isHydrogenBondAcceptor(theFirstAtom) && theHydrogenBondDonorList.isHydrogenBondDonor(theSecondAtom)) {
            return true;
        } else if (theHydrogenBondAcceptorList.isHydrogenBondAcceptor(theSecondAtom) && theHydrogenBondDonorList.isHydrogenBondDonor(theFirstAtom)) {
            return true;
        }

        return false;
    }

    private CalculableIntraHydrogenBondSet __getCalculableHydrogenBondList(HydrogenBondAcceptorList theHydrogenBondAcceptorList, 
            HydrogenBondDonorList theHydrogenBondDonorList, CalculableIntraElectroStaticSet theCalculableElectroStaticSet, 
            SbffHydrogenBondTotalSet theParameterTotalSet) {
        int theAAtomIndex = this.__getAAtomIndex(theHydrogenBondAcceptorList, theCalculableElectroStaticSet);
        int theHAtomIndex = this.__getHAtomIndex(theHydrogenBondDonorList, theCalculableElectroStaticSet);
        int theBAtomIndex = this.__getBAtomIndex(theAAtomIndex);
        int theXAtomIndex = this.__getXAtomIndex(theHAtomIndex);
        SbffHydrogenBondParameterSet theParameterSet = this.__getParmeter(theXAtomIndex, theHAtomIndex, theAAtomIndex, theBAtomIndex, theParameterTotalSet);
        CalculableIntraHydrogenBondSet theHydrogenBondSet = new CalculableIntraHydrogenBondSet(theXAtomIndex, theHAtomIndex, theAAtomIndex, theBAtomIndex, theParameterSet);

        return theHydrogenBondSet;
    }

    private SbffHydrogenBondParameterSet __getParmeter(int theAtomIndexConnectedDonor, int theDonorAtomIndex, int theAcceptorAtomIndex, 
            int theAtomIndexConnectedAcceptor, SbffHydrogenBondTotalSet theParameterTotalSet) {
        IAtom theAtomConnectedDonor = this.itsMolecule.getAtom(theAtomIndexConnectedDonor);
        IAtom theDonorAtom = this.itsMolecule.getAtom(theDonorAtomIndex);
        IAtom theAcceptorAtom = this.itsMolecule.getAtom(theAcceptorAtomIndex);
        IAtom theAtomConnectedAcceptor = this.itsMolecule.getAtom(theAtomIndexConnectedAcceptor);
        List<IAtom> theHydrogenBondAtomList = new ArrayList<>();

        theHydrogenBondAtomList.add(theAtomConnectedDonor);
        theHydrogenBondAtomList.add(theDonorAtom);
        theHydrogenBondAtomList.add(theAcceptorAtom);
        theHydrogenBondAtomList.add(theAtomConnectedAcceptor);

        return theParameterTotalSet.getParameterSet(theHydrogenBondAtomList);
    }

    private int __getAAtomIndex(HydrogenBondAcceptorList theHydrogenBondAcceptorList, CalculableIntraElectroStaticSet theCalculableElectroStaticSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getSecondAtomIndex());

        if (theHydrogenBondAcceptorList.isHydrogenBondAcceptor(theFirstAtom)) {
            return theCalculableElectroStaticSet.getFirstAtomIndex();
        }

        return theCalculableElectroStaticSet.getSecondAtomIndex();
    }

    private int __getHAtomIndex(HydrogenBondDonorList theHydrogenBondDonorList, CalculableIntraElectroStaticSet theCalculableElectroStaticSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getSecondAtomIndex());

        if (theHydrogenBondDonorList.isHydrogenBondDonor(theFirstAtom)) {
            return theCalculableElectroStaticSet.getFirstAtomIndex();
        }

        return theCalculableElectroStaticSet.getSecondAtomIndex();
    }

    private Integer __getBAtomIndex(int theAAtomIndex) {
        for (IAtom theConnectedAtom : this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(theAAtomIndex))) {
            if (theConnectedAtom.getAtomicNumber() != AtomInformation.Hydrogen.ATOM_NUMBER) {
                return this.itsMolecule.getAtomNumber(theConnectedAtom);
            }
        }

        return null;
    }

    private Integer __getXAtomIndex(int theXAtomIndex) {
        return this.itsMolecule.getAtomNumber(this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(theXAtomIndex)).get(this.FIRST_INDEX));
    }

    public boolean containAtomIndexSet(int theFirstAtomindex, int theSecondAtomIndex) {
        for (CalculableIntraHydrogenBondSet theCalculableHydrogenBondSet : this) {
            if (theCalculableHydrogenBondSet.containAtomIndexSet(theFirstAtomindex, theSecondAtomIndex)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        for (CalculableIntraHydrogenBondSet theSet : this) {
            theStringBuilder.append(theSet).append(" ");
        }

        return theStringBuilder.toString();
    }
}
