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
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.sbff.parameter.intraenergy.SbffStretchBendParameterSet;
import org.bmdrc.sbff.parameter.intraenergy.SbffStretchParameterSet;
import org.bmdrc.sbff.parameter.intraenergy.StretchBendParameter;
import org.bmdrc.sbff.parameter.intraenergy.StretchParameter;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableStretchBendingList extends AbstractCalculableList<CalculableStretchBendingSet> implements Serializable {

    private static final long serialVersionUID = -2876453896310679683L;

    public CalculableStretchBendingList(IAtomContainer theMolecule) {
        this(theMolecule, new CalculableBendingList(theMolecule));
    }

    public CalculableStretchBendingList(IAtomContainer theMolecule, CalculableBendingList theCalculableBendingList) {
        this(theMolecule, theCalculableBendingList, new ArrayList<Integer>());
    }

    public CalculableStretchBendingList(IAtomContainer theMolecule, SbffStretchParameterSet theStretchParameterSet, SbffStretchBendParameterSet theStretchBendParameterSet,
            CalculableBendingList theCalculableBendingList) {
        this(theMolecule, theStretchParameterSet, theStretchBendParameterSet, theCalculableBendingList, new ArrayList<Integer>());
    }

    public CalculableStretchBendingList(IAtomContainer theMolecule, CalculableBendingList theCalculableBendingList,
            List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffStretchParameterSet(), new SbffStretchBendParameterSet(), theCalculableBendingList, theNotCalculateAtomNumberList);
    }

    public CalculableStretchBendingList(IAtomContainer theMolecule, SbffStretchParameterSet theStretchParameterSet, SbffStretchBendParameterSet theStretchBendParameterSet,
            CalculableBendingList theCalculableBendingList, List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__initializeCalculableStretchBendingList(theStretchParameterSet, theStretchBendParameterSet, theCalculableBendingList);
    }

    private void __initializeCalculableStretchBendingList(SbffStretchParameterSet theStretchParameterSet, SbffStretchBendParameterSet theStretchBendParameterSet,
            CalculableBendingList theCalculableBendingList) {
        for (CalculableBendingSet theCalculableBendingSet : theCalculableBendingList) {
            if (this.__isStretchBendingSet(theCalculableBendingSet)) {
                this.__initializeCalculableStretchBendingList(theCalculableBendingSet, theStretchParameterSet, theStretchBendParameterSet);
            }
        }
    }

    private boolean __isStretchBendingSet(CalculableBendingSet theCalculableBendingSet) {
        if (!this.itsMolecule.getAtom(theCalculableBendingSet.getCenterAtomIndex()).getSymbol().equals(AtomInformation.Carbon.SYMBOL)) {
            return false;
        } else if (this.itsNotCalculateAtomNumberList.containsAll(theCalculableBendingSet.getAtomIndexList())) {
            return false;
        }

        return true;
    }

    private void __initializeCalculableStretchBendingList(CalculableBendingSet theCalculableBendingSet, SbffStretchParameterSet theStretchParameterSet, 
            SbffStretchBendParameterSet theStretchBendParameterSet) {
        StretchParameter theFirstStretchParameter = theStretchParameterSet.getStretchParameter(this.itsMolecule,
                this.itsMolecule.getAtom(theCalculableBendingSet.getFirstAtomIndex()), this.itsMolecule.getAtom(theCalculableBendingSet.getCenterAtomIndex()));
        StretchParameter theSecondStretchParameter = theStretchParameterSet.getStretchParameter(this.itsMolecule,
                this.itsMolecule.getAtom(theCalculableBendingSet.getCenterAtomIndex()), this.itsMolecule.getAtom(theCalculableBendingSet.getSecondAtomIndex()));;
        StretchBendParameter theStretchBendParameter = theStretchBendParameterSet.getParameter(this.itsMolecule.getAtom(theCalculableBendingSet.getFirstAtomIndex()), this.itsMolecule.getAtom(theCalculableBendingSet.getCenterAtomIndex()), this.itsMolecule.getAtom(theCalculableBendingSet.getSecondAtomIndex()));
        CalculableStretchBendingSet theCalculableStretchBendingSet = new CalculableStretchBendingSet(theCalculableBendingSet, theFirstStretchParameter, theSecondStretchParameter, theStretchBendParameter);

        this.add(theCalculableStretchBendingSet);
    }
}
