/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.parameter.intraenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class BendingParameter implements Serializable {
    
    private static final long serialVersionUID = -6969330673128816642L;

    private List<Integer> itsAtomTypeListConnectedFirstAtom;
    private List<Integer> itsAtomTypeListConnectedCenterAtom;
    private List<Integer> itsAtomTypeListConnectedSecondAtom;
    private Double itsStandardAngle;
    private Double itsBendingConstant;
    private Double itsBendingBendingConstant;

    public BendingParameter() {
    }

    public BendingParameter(Double theStandardAngle, Double itsBendingConstant, Double itsBendingBendingConstant) {
        this.itsStandardAngle = theStandardAngle;
        this.itsBendingConstant = itsBendingConstant;
        this.itsBendingBendingConstant = itsBendingBendingConstant;
    }

    public BendingParameter(BendingParameter theParameter) {
        if (theParameter.getAtomTypeListConnectedFirstAtom() != null) {
            this.itsAtomTypeListConnectedFirstAtom = new ArrayList<>(theParameter.getAtomTypeListConnectedFirstAtom());
        }

        if (theParameter.getAtomTypeListConnectedCenterAtom() != null) {
            this.itsAtomTypeListConnectedCenterAtom = new ArrayList<>(theParameter.getAtomTypeListConnectedCenterAtom());
        }

        if (theParameter.getAtomTypeListConnectedSecondAtom() != null) {
            this.itsAtomTypeListConnectedSecondAtom = new ArrayList<>(theParameter.getAtomTypeListConnectedSecondAtom());
        }
        
        this.itsStandardAngle = theParameter.getStandardAngle();
        this.itsBendingConstant = theParameter.getBendingConstant();
        this.itsBendingBendingConstant = theParameter.getBendingBendingConstant();
    }

    public List<Integer> getAtomTypeListConnectedFirstAtom() {
        return itsAtomTypeListConnectedFirstAtom;
    }

    public void setAtomTypeListConnectedFirstAtom(List<Integer> theAtomTypeListConnectedFirstAtom) {
        this.itsAtomTypeListConnectedFirstAtom = theAtomTypeListConnectedFirstAtom;
    }

    public List<Integer> getAtomTypeListConnectedCenterAtom() {
        return itsAtomTypeListConnectedCenterAtom;
    }

    public void setAtomTypeListConnectedCenterAtom(List<Integer> theAtomTypeListConnectedCenterAtom) {
        this.itsAtomTypeListConnectedCenterAtom = theAtomTypeListConnectedCenterAtom;
    }

    public List<Integer> getAtomTypeListConnectedSecondAtom() {
        return itsAtomTypeListConnectedSecondAtom;
    }

    public void setAtomTypeListConnectedSecondAtom(List<Integer> theAtomTypeListConnectedSecondAtom) {
        this.itsAtomTypeListConnectedSecondAtom = theAtomTypeListConnectedSecondAtom;
    }

    public Double getStandardAngle() {
        return itsStandardAngle;
    }

    public void setStandardAngle(Double theStandardAngle) {
        this.itsStandardAngle = theStandardAngle;
    }

    public Double getBendingConstant() {
        return itsBendingConstant;
    }

    public void setBendingConstant(Double theBendingConstant) {
        this.itsBendingConstant = theBendingConstant;
    }

    public Double getBendingBendingConstant() {
        return itsBendingBendingConstant;
    }

    public void setBendingBendingConstant(Double theBendingBendingConstant) {
        this.itsBendingBendingConstant = theBendingBendingConstant;
    }

    public boolean equalDescriptionAtom(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        List<IAtom> theParticipatedAtomList = new ArrayList<>();
        
        theParticipatedAtomList.add(theFirstAtom);
        theParticipatedAtomList.add(theCenterAtom);
        theParticipatedAtomList.add(theSecondAtom);
        
        if (this.itsAtomTypeListConnectedFirstAtom != null && !this.__equalDescriptionAtom(theMolecule, theFirstAtom, theParticipatedAtomList, this.itsAtomTypeListConnectedFirstAtom)) {
            return false;
        } else if (this.itsAtomTypeListConnectedCenterAtom != null && !this.__equalDescriptionAtom(theMolecule, theCenterAtom, theParticipatedAtomList, this.itsAtomTypeListConnectedCenterAtom)) {
            return false;
        } else if (this.itsAtomTypeListConnectedSecondAtom != null && !this.__equalDescriptionAtom(theMolecule, theSecondAtom, theParticipatedAtomList, this.itsAtomTypeListConnectedSecondAtom)) {
            return false;
        }

        return true;
    }

    private boolean __equalDescriptionAtom(IAtomContainer theMolecule, IAtom theAtom, List<IAtom> theExceptionAtomList, List<Integer> theAtomTypeListConnectedAtom) {
        List<IAtom> theConnectedAtomList = theMolecule.getConnectedAtomsList(theAtom);
        List<Integer> theCopiedAtomTypeListConnectedAtom = new ArrayList<>(theAtomTypeListConnectedAtom);

        if (theAtom == null) {
            return true;
        }

        for (int ci = theCopiedAtomTypeListConnectedAtom.size() - 1; ci >= 0; ci--) {
            for (int ai = theConnectedAtomList.size() - 1; ai >= 0; ai--) {
                if (theCopiedAtomTypeListConnectedAtom.get(ci).equals((int) theConnectedAtomList.get(ai).getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY))
                        && !theExceptionAtomList.contains(theConnectedAtomList.get(ai))) {
                    theCopiedAtomTypeListConnectedAtom.remove(ci);
                    theConnectedAtomList.remove(ai);
                    break;
                }
            }
        }

        return theCopiedAtomTypeListConnectedAtom.isEmpty();
    }
}
