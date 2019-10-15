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
import org.bmdrc.sbff.parameter.intraenergy.SbffStretchParameterSet;
import org.bmdrc.sbff.parameter.intraenergy.StretchParameter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableStretchList extends AbstractCalculableList<CalculableStretchSet> implements Serializable {

    private static final long serialVersionUID = 7623230069043687631L;

    public CalculableStretchList(IAtomContainer theMolecule) {
        this(theMolecule, new ArrayList<Integer>());
    }

    public CalculableStretchList(IAtomContainer theMolecule, SbffStretchParameterSet theParameterSet) {
        this(theMolecule, theParameterSet, new ArrayList<Integer>());
    }
    
    public CalculableStretchList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffStretchParameterSet(), theNotCalculateAtomNumberList);
    }
    
    public CalculableStretchList(IAtomContainer theMolecule, SbffStretchParameterSet theParameterSet, List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__initializeCalculableStretchParameterList(theParameterSet);
    }

    public CalculableStretchList(CalculableStretchList theCalculableStretchList) {
        super(theCalculableStretchList);
    }

    private void __initializeCalculableStretchParameterList(SbffStretchParameterSet theParameterSet) {
        for (IBond theBond : this.itsMolecule.bonds()) {
            if (this.__isCalculableStretchSet(theBond)) {
                CalculableStretchSet theCalculableStretchSetString = this.__initializeCalculableStretchParameterList(theBond, theParameterSet);

                this.add(theCalculableStretchSetString);
            }
        }
    }

    private boolean __isCalculableStretchSet(IBond theBond) {
        return !this.itsNotCalculateAtomNumberList.contains(this.itsMolecule.getAtomNumber(theBond.getAtom(this.FIRST_INDEX)))
                || !this.itsNotCalculateAtomNumberList.contains(this.itsMolecule.getAtomNumber(theBond.getAtom(this.SECOND_INDEX)));
    }

    private CalculableStretchSet __initializeCalculableStretchParameterList(IBond theBond, SbffStretchParameterSet theParameterSet) {
        StretchParameter theParameter = theParameterSet.getStretchParameter(this.itsMolecule, theBond.getAtom(this.FIRST_INDEX), theBond.getAtom(this.SECOND_INDEX));
        int theFirstAtomIndex = this.itsMolecule.getAtomNumber(theBond.getAtom(this.FIRST_INDEX));
        int theSecondAtomIndex = this.itsMolecule.getAtomNumber(theBond.getAtom(this.SECOND_INDEX));
        CalculableStretchSet theCalculableStretchSet = new CalculableStretchSet(theFirstAtomIndex, theSecondAtomIndex, theParameter);
        
        return new CalculableStretchSet(theFirstAtomIndex, theSecondAtomIndex, theParameter);
    }
}
