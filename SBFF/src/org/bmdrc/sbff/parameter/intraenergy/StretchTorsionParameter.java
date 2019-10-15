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
public class StretchTorsionParameter implements Serializable {
    private static final long serialVersionUID = 5483663510461467822L;

    private List<Integer> itsConnectedAtomTypeListInFirstAtom;
    private List<Integer> itsConnectedAtomTypeListInSecondAtom;
    private List<Integer> itsConnectedAtomTypeListInThirdAtom;
    private List<Integer> itsConnectedAtomTypeListInFourthAtom;
    private Double itsConstant;

    public StretchTorsionParameter() {
    }

    public StretchTorsionParameter(Double theConstant) {
        this.itsConstant = theConstant;
    }

    public StretchTorsionParameter(StretchTorsionParameter theParameter) {
        if (theParameter.getConnectedAtomTypeListInFirstAtom() != null) {
            this.itsConnectedAtomTypeListInFirstAtom = new ArrayList<>(theParameter.getConnectedAtomTypeListInFirstAtom());
        }

        if (theParameter.getConnectedAtomTypeListInSecondAtom() != null) {
            this.itsConnectedAtomTypeListInSecondAtom = new ArrayList<>(theParameter.getConnectedAtomTypeListInSecondAtom());
        }

        if (theParameter.getConnectedAtomTypeListInThirdAtom() != null) {
            this.itsConnectedAtomTypeListInThirdAtom = new ArrayList<>(theParameter.getConnectedAtomTypeListInThirdAtom());
        }

        if (theParameter.getConnectedAtomTypeListInFourthAtom() != null) {
            this.itsConnectedAtomTypeListInFourthAtom = new ArrayList<>(theParameter.getConnectedAtomTypeListInFourthAtom());
        }
        
        this.itsConstant = theParameter.getConstant();
    }

    public List<Integer> getConnectedAtomTypeListInFirstAtom() {
        return itsConnectedAtomTypeListInFirstAtom;
    }

    public void setConnectedAtomTypeListInFirstAtom(List<Integer> theConnectedAtomTypeListInFirstAtom) {
        this.itsConnectedAtomTypeListInFirstAtom = theConnectedAtomTypeListInFirstAtom;
    }

    public List<Integer> getConnectedAtomTypeListInSecondAtom() {
        return itsConnectedAtomTypeListInSecondAtom;
    }

    public void setConnectedAtomTypeListInSecondAtom(List<Integer> theConnectedAtomTypeListInSecondAtom) {
        this.itsConnectedAtomTypeListInSecondAtom = theConnectedAtomTypeListInSecondAtom;
    }

    public List<Integer> getConnectedAtomTypeListInThirdAtom() {
        return itsConnectedAtomTypeListInThirdAtom;
    }

    public void setConnectedAtomTypeListInThirdAtom(List<Integer> theConnectedAtomTypeListInThirdAtom) {
        this.itsConnectedAtomTypeListInThirdAtom = theConnectedAtomTypeListInThirdAtom;
    }

    public List<Integer> getConnectedAtomTypeListInFourthAtom() {
        return itsConnectedAtomTypeListInFourthAtom;
    }

    public void setConnectedAtomTypeListInFourthAtom(List<Integer> theConnectedAtomTypeListInFourthAtom) {
        this.itsConnectedAtomTypeListInFourthAtom = theConnectedAtomTypeListInFourthAtom;
    }

    public Double getConstant() {
        return itsConstant;
    }

    public void setConstant(Double theConstant) {
        this.itsConstant = theConstant;
    }

    public boolean equalConnectedAtomTypeSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        return this.__equalConnectedAtomTypeSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)
                || this.__equalConnectedAtomTypeSet(theMolecule, theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom);
    }

    private boolean __equalConnectedAtomTypeSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        return this.__equalConnectedAtomType(theMolecule, theFirstAtom, this.itsConnectedAtomTypeListInFirstAtom)
                && this.__equalConnectedAtomType(theMolecule, theSecondAtom, this.itsConnectedAtomTypeListInSecondAtom)
                && this.__equalConnectedAtomType(theMolecule, theThirdAtom, this.itsConnectedAtomTypeListInThirdAtom)
                && this.__equalConnectedAtomType(theMolecule, theFourthAtom, itsConnectedAtomTypeListInFourthAtom);
    }

    private boolean __equalConnectedAtomType(IAtomContainer theMolecule, IAtom theAtom, List<Integer> theConnectedAtomTypeListInParameter) {
        if (theConnectedAtomTypeListInParameter == null) {
            return true;
        }

        List<IAtom> theConnectedAtomList = theMolecule.getConnectedAtomsList(theAtom);
        List<Integer> theCopiedConnectedAtomTypeListInParameter = new ArrayList<>(theConnectedAtomTypeListInParameter);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            Integer theConnectedAtomType = (int) theConnectedAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);

            if (theCopiedConnectedAtomTypeListInParameter.contains(theConnectedAtomType)) {
                theCopiedConnectedAtomTypeListInParameter.remove(theConnectedAtomType);
            }
        }

        return theCopiedConnectedAtomTypeListInParameter.isEmpty();
    }
}
