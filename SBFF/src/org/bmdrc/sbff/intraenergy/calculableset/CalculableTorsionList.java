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
import org.bmdrc.sbff.parameter.intraenergy.SbffTorsionParameterSet;
import org.bmdrc.sbff.parameter.intraenergy.TorsionParameter;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableTorsionList extends AbstractCalculableList<CalculableTorsionSet> implements Serializable {

    private static final long serialVersionUID = -313487621895415217L;

    public CalculableTorsionList(IAtomContainer theMolecule) {
        this(theMolecule, new ArrayList<Integer>());
    }

    public CalculableTorsionList(IAtomContainer theMolecule, SbffTorsionParameterSet theParameterSet) {
        this(theMolecule, theParameterSet, new ArrayList<Integer>());
    }

    public CalculableTorsionList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffTorsionParameterSet(), theNotCalculateAtomNumberList);
    }

    public CalculableTorsionList(IAtomContainer theMolecule, SbffTorsionParameterSet theParameterSet, List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__initializeCalculableTorsionList(theParameterSet);
    }

    private void __initializeCalculableTorsionList(SbffTorsionParameterSet theParameterSet) {
        for (IBond theBond : this.itsMolecule.bonds()) {
            if (this.__containCalculableTorsion(theBond)) {
                this.__initializeCalculableTorsionList(theBond, theParameterSet);
            }
        }
    }

    private void __initializeCalculableTorsionList(IBond theBond, SbffTorsionParameterSet theParameterSet) {
        for (IAtom theFirstConnectedAtom : this.itsMolecule.getConnectedAtomsList(theBond.getAtom(this.FIRST_INDEX))) {
            if (!theFirstConnectedAtom.equals(theBond.getAtom(this.SECOND_INDEX))) {
                this.__initializeCalculableTorsionList(theBond, theFirstConnectedAtom, theParameterSet);
            }
        }
    }

    private void __initializeCalculableTorsionList(IBond theBond, IAtom theFirstConnectedAtom, SbffTorsionParameterSet theParameterSet) {
        for (IAtom theSecondConnectedAtom : this.itsMolecule.getConnectedAtomsList(theBond.getAtom(this.SECOND_INDEX))) {
            int theFirstConnectedAtomIndex = this.itsMolecule.getAtomNumber(theFirstConnectedAtom);
            int theFirstAtomIndex = this.itsMolecule.getAtomNumber(theBond.getAtom(this.FIRST_INDEX));
            int theSecondAtomIndex = this.itsMolecule.getAtomNumber(theBond.getAtom(this.SECOND_INDEX));
            int theSecondConnectedAtomIndex = this.itsMolecule.getAtomNumber(theSecondConnectedAtom);

            if (!theSecondConnectedAtom.equals(theBond.getAtom(this.FIRST_INDEX)) && this.__isCalculableTorsionList(theFirstConnectedAtomIndex, theFirstAtomIndex,
                    theSecondAtomIndex, theSecondConnectedAtomIndex)) {
                TorsionParameter theParameter = theParameterSet.getParameter(this.itsMolecule, theFirstConnectedAtom, theBond.getAtom(this.FIRST_INDEX), theBond.getAtom(this.SECOND_INDEX), theSecondConnectedAtom);
                CalculableTorsionSet theCalculableTorsionSet = new CalculableTorsionSet(theFirstConnectedAtomIndex, theFirstAtomIndex, theSecondAtomIndex, theSecondConnectedAtomIndex, theParameter);

                this.add(theCalculableTorsionSet);

                if (theParameter == null) {
                    System.err.println("Torsion Parameter not existed!! " + theFirstConnectedAtomIndex + " " + theFirstAtomIndex + " " + theSecondAtomIndex + " " + theSecondConnectedAtomIndex);
                }
            }
        }
    }

    private boolean __isCalculableTorsionList(int theFirstConnectedAtomIndex, int theFirstAtomIndex, int theSecondAtomIndex, int theSecondConnectedAtomIndex) {
        if (!this.itsNotCalculateAtomNumberList.contains(theFirstConnectedAtomIndex)) {
            return true;
        } else if (!this.itsNotCalculateAtomNumberList.contains(theFirstAtomIndex)) {
            return true;
        } else if (!this.itsNotCalculateAtomNumberList.contains(theSecondAtomIndex)) {
            return true;
        } else if (!this.itsNotCalculateAtomNumberList.contains(theSecondConnectedAtomIndex)) {
            return true;
        }

        return false;
    }

    private boolean __containCalculableTorsion(IBond theBond) {
        return this.itsMolecule.getConnectedAtomsCount(theBond.getAtom(this.FIRST_INDEX)) > 1
                && this.itsMolecule.getConnectedAtomsCount(theBond.getAtom(this.SECOND_INDEX)) > 1;
    }
}
