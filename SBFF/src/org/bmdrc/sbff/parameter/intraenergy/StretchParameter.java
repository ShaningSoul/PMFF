/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.parameter.intraenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class StretchParameter implements Serializable {
    private static final long serialVersionUID = -6935786124833617562L;

    private TwoDimensionList<Integer> itsAtomType2dListConnectedFirstAtom;
    private TwoDimensionList<Integer> itsAtomType2dListConnectedSecondAtom;
    private Double itsBondLength;
    private Double itsConstant;
    private Double itsBondMoment;

    public StretchParameter() {
    }

    public StretchParameter(Double theBondLength, Double theConstant, Double theBondMoment) {
        this.itsBondLength = theBondLength;
        this.itsConstant = theConstant;
        this.itsBondMoment = theBondMoment;
    }

    public StretchParameter(StretchParameter theStretchParameter) {
        if (theStretchParameter.getAtomType2dListConnectedFirstAtom() != null) {
            this.itsAtomType2dListConnectedFirstAtom = new TwoDimensionList<>(theStretchParameter.getAtomType2dListConnectedFirstAtom());
        }

        if (theStretchParameter.getAtomType2dListConnectedSecondAtom() != null) {
            this.itsAtomType2dListConnectedSecondAtom = new TwoDimensionList<>(theStretchParameter.getAtomType2dListConnectedSecondAtom());
        }

        this.itsBondLength = theStretchParameter.getBondLength();
        this.itsBondMoment = theStretchParameter.getBondMoment();
        this.itsConstant = theStretchParameter.getConstant();
    }

    public TwoDimensionList<Integer> getAtomType2dListConnectedFirstAtom() {
        return itsAtomType2dListConnectedFirstAtom;
    }

    public void setAtomType2dListConnectedFirstAtom(TwoDimensionList<Integer> theAtomType2dListConnectedFirstAtom) {
        this.itsAtomType2dListConnectedFirstAtom = theAtomType2dListConnectedFirstAtom;
    }

    public TwoDimensionList<Integer> getAtomType2dListConnectedSecondAtom() {
        return itsAtomType2dListConnectedSecondAtom;
    }

    public void setAtomType2dListConnectedSecondAtom(TwoDimensionList<Integer> theAtomType2dListConnectedSecondAtom) {
        this.itsAtomType2dListConnectedSecondAtom = theAtomType2dListConnectedSecondAtom;
    }

    public Double getBondLength() {
        return itsBondLength;
    }

    public void setBondLength(Double theBondLength) {
        this.itsBondLength = theBondLength;
    }

    public Double getConstant() {
        return itsConstant;
    }

    public void setConstant(Double theConstant) {
        this.itsConstant = theConstant;
    }

    public Double getBondMoment() {
        return itsBondMoment;
    }

    public void setBondMoment(Double theBondMoment) {
        this.itsBondMoment = theBondMoment;
    }

    public boolean equalDescriptionAtomLocation(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom) {
        if (this.getAtomType2dListConnectedFirstAtom() != null && !this.__equalDescriptionAtomLocation(theMolecule, theFirstAtom, theSecondAtom, this.getAtomType2dListConnectedFirstAtom())) {
            return false;
        } else if (this.getAtomType2dListConnectedSecondAtom() != null && !this.__equalDescriptionAtomLocation(theMolecule, theSecondAtom, theFirstAtom, this.getAtomType2dListConnectedSecondAtom())) {
            return false;
        }

        return true;
    }

    private boolean __equalDescriptionAtomLocation(IAtomContainer theMolecule, IAtom theAtom, IAtom theParticipatedAtom, TwoDimensionList<Integer> theAtomTypeListConnectedAtom) {
        IAtom thePreviousAtom = theAtom;
        
        for (List<Integer> theDescriptionAtomTypeList : theAtomTypeListConnectedAtom) {
            List<IAtom> theConnectedAtomList = theMolecule.getConnectedAtomsList(thePreviousAtom);
            List<Integer> theCopiedDescriptionAtomTypeList = new ArrayList<>(theDescriptionAtomTypeList);
            
            for (int vi = theCopiedDescriptionAtomTypeList.size() - 1; vi >= 0 ; vi--) {
                for (int ai = theConnectedAtomList.size() - 1; ai >= 0; ai--) {
                    if (theCopiedDescriptionAtomTypeList.get(vi).equals((int) theConnectedAtomList.get(ai).getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY)) && 
                            !theConnectedAtomList.get(ai).equals(theParticipatedAtom)) {
                        thePreviousAtom = theConnectedAtomList.get(ai);
                        theCopiedDescriptionAtomTypeList.remove(vi);
                        break;
                    }
                }
            }

            if (!theCopiedDescriptionAtomTypeList.isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
