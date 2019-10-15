/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.intraenergy.calculableset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableBendingBendingList extends AbstractCalculableList<CalculableBendingBendingSet> implements Serializable {

    private static final long serialVersionUID = 8389528584127102430L;

    public CalculableBendingBendingList(IAtomContainer theMolecule) {
        this(theMolecule, new CalculableBendingList(theMolecule), new ArrayList<Integer>());
    }

    public CalculableBendingBendingList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new CalculableBendingList(theMolecule), theNotCalculateAtomNumberList);
    }

    public CalculableBendingBendingList(IAtomContainer theMolecule, CalculableBendingList theCalculableBendingList) {
        this(theMolecule, theCalculableBendingList, new ArrayList<Integer>());
    }

    public CalculableBendingBendingList(IAtomContainer theMolecule, CalculableBendingList theCalculableBendingList, 
            List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__initializeCalculableBendingBendingList(theCalculableBendingList);
    }

    private void __initializeCalculableBendingBendingList(CalculableBendingList theCalculableBendingList) {
        Set<Integer> theCenterAtomIndexSet = this.__getCenterAtomIndexSet(theCalculableBendingList);

        for (Integer theCenterAtomIndex : theCenterAtomIndexSet) {
            this.__addBendingBendingSet(theCenterAtomIndex, theCalculableBendingList);
        }
    }

    private void __addBendingBendingSet(Integer theCenterAtomIndex, CalculableBendingList theCalculableBendingList) {
        List<CalculableBendingSet> theBendingListContainSameAtom = this.__getBendingSetContainSameCenterAtom(theCenterAtomIndex, theCalculableBendingList);
        
        for (int fi = 0, fEnd = theBendingListContainSameAtom.size() - 1; fi < fEnd; fi++) {
            for (int si = fi + 1, sEnd = fEnd + 1; si < sEnd; si++) {
                if (this.__isBendingBendingList(theBendingListContainSameAtom.get(fi), theBendingListContainSameAtom.get(si))) {
                    CalculableBendingBendingSet theCalculableBendingBendingSet = new CalculableBendingBendingSet(theBendingListContainSameAtom.get(fi),
                            theBendingListContainSameAtom.get(si));

                    this.add(theCalculableBendingBendingSet);
                }
            }
        }
    }

    private boolean __isBendingBendingList(CalculableBendingSet theFirstBendingSet, CalculableBendingSet theSecondBendingSet) {
        for (int vi = 0, vEnd = theFirstBendingSet.getAtomIndexList().size(); vi < vEnd; vi++) {
            if (!this.itsNotCalculateAtomNumberList.contains(theFirstBendingSet.getAtomIndexList().get(vi))
                    || !this.itsNotCalculateAtomNumberList.contains(theSecondBendingSet.getAtomIndexList().get(vi))) {
                return true;
            }
        }

        return false;
    }

    private List<CalculableBendingSet> __getBendingSetContainSameCenterAtom(Integer theCenterAtomIndex, CalculableBendingList theCalculableBendingList) {
        List<CalculableBendingSet> theBendingList = new ArrayList<>();

        for (CalculableBendingSet theSet : theCalculableBendingList) {
            if (theSet.getCenterAtomIndex().equals(theCenterAtomIndex)) {
                theBendingList.add(new CalculableBendingSet(theSet));
            }
        }

        return theBendingList;
    }

    private Set<Integer> __getCenterAtomIndexSet(CalculableBendingList theCalculableBendingList) {
        Set<Integer> theCenterAtomIndexSet = new HashSet<>();

        for (CalculableBendingSet theSet : theCalculableBendingList) {
            theCenterAtomIndexSet.add(theSet.getCenterAtomIndex());
        }

        return theCenterAtomIndexSet;
    }
}
