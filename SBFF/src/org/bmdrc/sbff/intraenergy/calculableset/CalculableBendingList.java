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
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.sbff.parameter.intraenergy.BendingParameter;
import org.bmdrc.sbff.parameter.intraenergy.SbffBendingParameterSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableBendingList extends AbstractCalculableList<CalculableBendingSet> implements Serializable {

    private static final long serialVersionUID = -7999273675858166955L;

    public CalculableBendingList(IAtomContainer theMolecule) {
        this(theMolecule, new ArrayList<Integer>());
    }

    public CalculableBendingList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffBendingParameterSet(), theNotCalculateAtomNumberList);
    }

    public CalculableBendingList(IAtomContainer theMolecule, SbffBendingParameterSet theParameterSet) {
        this(theMolecule, theParameterSet, new ArrayList<Integer>());
    }

    public CalculableBendingList(IAtomContainer theMolecule, SbffBendingParameterSet theParameterSet, List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__initializeCalculableBendingList(theParameterSet);
    }

    private void __initializeCalculableBendingList(SbffBendingParameterSet theParameterSet) {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            if (this.__containCalculableBendingSet(theAtom)) {
                 this.__initializeCalculableBendingList(theAtom, theParameterSet);
            }
        }
    }

    private void __initializeCalculableBendingList(IAtom theAtom, SbffBendingParameterSet theParameterSet) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);
        
        for (int fi = 0, fEnd = theConnectedAtomList.size() - 1; fi < fEnd; fi++) {
            for (int si = fi + 1, sEnd = theConnectedAtomList.size(); si < sEnd; si++) {
                int theFirstAtomIndex = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(fi));
                int theSecondAtomIndex = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(si));
                int theCenterAtomIndex = this.itsMolecule.getAtomNumber(theAtom);

                if (!this.itsNotCalculateAtomNumberList.contains(theFirstAtomIndex) || !this.itsNotCalculateAtomNumberList.contains(theCenterAtomIndex)
                        || !this.itsNotCalculateAtomNumberList.contains(theSecondAtomIndex)) {
                    BendingParameter theParameter = theParameterSet.getBendingParameter(this.itsMolecule, theConnectedAtomList.get(fi), theAtom, theConnectedAtomList.get(si));
                    CalculableBendingSet theCalculableBendingSet = new CalculableBendingSet(theFirstAtomIndex, theCenterAtomIndex, theSecondAtomIndex, theParameter);

                    this.add(theCalculableBendingSet);
                }
            }
        }
    }

    private boolean __containCalculableBendingSet(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);

        return theConnectedAtomList.size() >= 2;
    }
}
