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
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.bmdrc.sbff.parameter.intraenergy.OutOfPlaneBendParameter;
import org.bmdrc.sbff.parameter.intraenergy.SbffOutOfPlaneBendingParameterSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableOutOfPlaneList extends AbstractCalculableList<CalculableOutOfPlaneSet> implements Serializable {

    private static final long serialVersionUID = -4637047054559973637L;

    public CalculableOutOfPlaneList(IAtomContainer theMolecule) {
        this(theMolecule, new ArrayList<Integer>());
    }

    public CalculableOutOfPlaneList(IAtomContainer theMolecule, SbffOutOfPlaneBendingParameterSet theParameterSet) {
        this(theMolecule, theParameterSet, new ArrayList<Integer>());
    }
    
    public CalculableOutOfPlaneList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffOutOfPlaneBendingParameterSet(), theNotCalculateAtomNumberList);
    }
    
    public CalculableOutOfPlaneList(IAtomContainer theMolecule, SbffOutOfPlaneBendingParameterSet theParameterSet, List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__initializeCalculableOutOfPlaneList(theParameterSet);
    }

    private void __initializeCalculableOutOfPlaneList(SbffOutOfPlaneBendingParameterSet theParameterSet) {
        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            if (this.itsMolecule.getConnectedAtomsCount(this.itsMolecule.getAtom(ai)) == 3 && this.__isCalculableAtom(this.itsMolecule.getAtom(ai))) {
               this.__initializeCalculableOutOfPlaneList(this.itsMolecule.getAtom(ai), theParameterSet);
            }
        }
    }

    private void __initializeCalculableOutOfPlaneList(IAtom theAtom, SbffOutOfPlaneBendingParameterSet theParameterSet) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);
        int theCenterAtomNumber = this.itsMolecule.getAtomNumber(theAtom);
        int theConnectedFirstAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(this.FIRST_INDEX));
        int theConnectedSecondAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(this.SECOND_INDEX));
        int theConnectedThirdAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(this.THIRD_INDEX));
        OutOfPlaneBendParameter theParameter = theParameterSet.getParameter(this.itsMolecule, theAtom);
        CalculableOutOfPlaneSet theCalculableOutOfPlaneSet = new CalculableOutOfPlaneSet(theCenterAtomNumber, theConnectedFirstAtomNumber, theConnectedSecondAtomNumber, theConnectedThirdAtomNumber, theParameter);

            this.add(theCalculableOutOfPlaneSet);
        
    }

    private boolean __isCalculableAtom(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);
        int theCenterAtomNumber = this.itsMolecule.getAtomNumber(theAtom);
        int theConnectedFirstAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(this.FIRST_INDEX));
        int theConnectedSecondAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(this.SECOND_INDEX));
        int theConnectedThirdAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(this.THIRD_INDEX));
        int theAtomType = (Integer) theAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);

        if (!this.__isCorrectAtomType(theAtom)) {
            return false;
        }

        if (!this.itsNotCalculateAtomNumberList.contains(theCenterAtomNumber)) {
            return true;
        } else if (!this.itsNotCalculateAtomNumberList.contains(theConnectedFirstAtomNumber)) {
            return true;
        } else if (!this.itsNotCalculateAtomNumberList.contains(theConnectedSecondAtomNumber)) {
            return true;
        } else if (!this.itsNotCalculateAtomNumberList.contains(theConnectedThirdAtomNumber)) {
            return true;
        }

        return false;
    }

    private boolean __isCorrectAtomType(IAtom theAtom) {
        int theAtomType = (Integer) theAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);

        if (theAtomType == MM3AtomTypeGenerator.SP2_CARBON) {
            return true;
        } else if (theAtomType == MM3AtomTypeGenerator.SP2_NITROGEN) {
            return true;
        } else if (theAtomType == MM3AtomTypeGenerator.POSITIVE_CARBON) {
            return true;
        }

        return false;
    }
}
