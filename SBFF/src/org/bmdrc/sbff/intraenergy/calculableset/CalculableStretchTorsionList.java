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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.sbff.parameter.intraenergy.SbffStretchParameterSet;
import org.bmdrc.sbff.parameter.intraenergy.SbffStretchTorsionParameterSet;
import org.bmdrc.sbff.parameter.intraenergy.StretchParameter;
import org.bmdrc.sbff.parameter.intraenergy.StretchTorsionParameter;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableStretchTorsionList extends AbstractCalculableList<CalculableStretchTorsionSet> implements Serializable {

    private static final long serialVersionUID = -4255013082408101365L;

    public CalculableStretchTorsionList(IAtomContainer theMolecule) {
        this(theMolecule, new CalculableTorsionList(theMolecule));
    }

    public CalculableStretchTorsionList(IAtomContainer theMolecule, CalculableTorsionList theCalculableTorsionList) {
        this(theMolecule, theCalculableTorsionList, new ArrayList<Integer>());
    }

    public CalculableStretchTorsionList(IAtomContainer theMolecule, SbffStretchParameterSet theStretchParameterSet, SbffStretchTorsionParameterSet theStretchTorsionParameterSet,
            CalculableTorsionList theCalculableTorsionList) {
        this(theMolecule, theStretchParameterSet, theStretchTorsionParameterSet, theCalculableTorsionList, new ArrayList<Integer>());
    }

    public CalculableStretchTorsionList(IAtomContainer theMolecule, CalculableTorsionList theCalculableTorsionList, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffStretchParameterSet(), new SbffStretchTorsionParameterSet(), theCalculableTorsionList, theNotCalculateAtomNumberList);
    }

    public CalculableStretchTorsionList(IAtomContainer theMolecule, SbffStretchParameterSet theStretchParameterSet, SbffStretchTorsionParameterSet theStretchTorsionParameterSet,
            CalculableTorsionList theCalculableTorsionList, List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__initializeCalculableStretchTorsionList(theStretchParameterSet, theStretchTorsionParameterSet, theCalculableTorsionList);

    }

    private void __initializeCalculableStretchTorsionList(SbffStretchParameterSet theStretchParameterSet, 
            SbffStretchTorsionParameterSet theStretchTorsionParameterSet, CalculableTorsionList theCalculableTorsionList) {
        Map<Integer, StretchParameter> theStretchParameterMap = new HashMap<>();
        
        for (CalculableTorsionSet theCalculableTorsionSet : theCalculableTorsionList) {
            if (this.__isStretchTorsionList(theCalculableTorsionSet)) {
                StretchParameter theStretchParameter = this.__getStretchParameter(theStretchParameterMap, theCalculableTorsionSet, theStretchParameterSet);
                
                this.__initializeCalculableStretchTorsionList(theCalculableTorsionSet, theStretchParameter, theStretchTorsionParameterSet);
            }
        }
    }

    private StretchParameter __getStretchParameter(Map<Integer, StretchParameter> theStretchParameterMap, CalculableTorsionSet theCalculableTorsionSet,
            SbffStretchParameterSet theStretchParameterSet) {
        int theBondIndex = this.itsMolecule.getBondNumber(this.itsMolecule.getAtom(theCalculableTorsionSet.getFirstAtomIndex()),
                this.itsMolecule.getAtom(theCalculableTorsionSet.getSecondAtomIndex()));

        if (!theStretchParameterMap.containsKey(theBondIndex)) {
            StretchParameter theParameter = theStretchParameterSet.getStretchParameter(this.itsMolecule, this.itsMolecule.getAtom(theCalculableTorsionSet.getFirstAtomIndex()),
                    this.itsMolecule.getAtom(theCalculableTorsionSet.getSecondAtomIndex()));

            theStretchParameterMap.put(theBondIndex, theParameter);
        }

        return theStretchParameterMap.get(theBondIndex);
    }

    private boolean __isStretchTorsionList(CalculableTorsionSet theCalculableTorsionSet) {
        return !this.itsNotCalculateAtomNumberList.containsAll(theCalculableTorsionSet.getAtomIndexList());
    }

    private void __initializeCalculableStretchTorsionList(CalculableTorsionSet theCalculableTorsionSet, StretchParameter theStretchParameter,
            SbffStretchTorsionParameterSet theStretchTorsionParameterSet) {
        StretchTorsionParameter theStretchTorsionParameter = theStretchTorsionParameterSet.getParameter(this.itsMolecule,
                this.itsMolecule.getAtom(theCalculableTorsionSet.getFirstConnectedAtomIndex()), this.itsMolecule.getAtom(theCalculableTorsionSet.getFirstAtomIndex()),
                this.itsMolecule.getAtom(theCalculableTorsionSet.getSecondAtomIndex()), this.itsMolecule.getAtom(theCalculableTorsionSet.getSecondConnectedAtomIndex()));
        CalculableStretchTorsionSet theCalculableStretchTorsionSet = new CalculableStretchTorsionSet(theCalculableTorsionSet, theStretchParameter, theStretchTorsionParameter);

        if (theStretchTorsionParameter != null) {
            this.add(theCalculableStretchTorsionSet);
        }
    }
}
