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
public class TorsionParameter implements Serializable {
    private static final long serialVersionUID = 3068270643579090327L;

    private List<Integer> itsConnectedAtomTypeListInFirstAtom;
    private List<Integer> itsConnectedAtomTypeListInSecondAtom;
    private List<Integer> itsConnectedAtomTypeListInThirdAtom;
    private List<Integer> itsConnectedAtomTypeListInFourthAtom;
    private Double itsV1;
    private Double itsV2;
    private Double itsV3;

    public TorsionParameter() {
    }

    public TorsionParameter(Double theV1, Double theV2, Double theV3) {
        this.itsV1 = theV1;
        this.itsV2 = theV2;
        this.itsV3 = theV3;
    }

    public TorsionParameter(TorsionParameter theParameter) {
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
        
        this.itsV1 = theParameter.getV1();
        this.itsV2 = theParameter.getV2();
        this.itsV3 = theParameter.getV3();
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

    public Double getV1() {
        return itsV1;
    }

    public void setsV1(Double theV1) {
        this.itsV1 = itsV1;
    }

    public Double getV2() {
        return itsV2;
    }

    public void setV2(Double theV2) {
        this.itsV2 = theV2;
    }

    public Double getV3() {
        return itsV3;
    }

    public void setV3(Double theV3) {
        this.itsV3 = theV3;
    }

    public boolean equalAtomDescription(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        return this.__equalAtomDescription(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom) ||
                this.__equalAtomDescription(theMolecule, theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom);
    }
    
    private boolean __equalAtomDescription(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        List<IAtom> theParticipatedAtomList = new ArrayList<>();
        
        theParticipatedAtomList.add(theFirstAtom);
        theParticipatedAtomList.add(theSecondAtom);
        theParticipatedAtomList.add(theThirdAtom);
        theParticipatedAtomList.add(theFourthAtom);
        
        if (this.itsConnectedAtomTypeListInFirstAtom != null && !this.__equalAtomDescription(theMolecule, theFirstAtom, theParticipatedAtomList, itsConnectedAtomTypeListInFirstAtom)) {
            return false;
        } else if (this.itsConnectedAtomTypeListInSecondAtom != null && !this.__equalAtomDescription(theMolecule, theSecondAtom, theParticipatedAtomList, itsConnectedAtomTypeListInSecondAtom)) {
            return false;
        } else if (this.itsConnectedAtomTypeListInThirdAtom != null && !this.__equalAtomDescription(theMolecule, theThirdAtom, theParticipatedAtomList, itsConnectedAtomTypeListInThirdAtom)) {
            return false;
        } else if (this.itsConnectedAtomTypeListInFourthAtom != null && !this.__equalAtomDescription(theMolecule, theFourthAtom, theParticipatedAtomList, itsConnectedAtomTypeListInFourthAtom)) {
            return false;
        }

        return true;
    }

    private boolean __equalAtomDescription(IAtomContainer theMolecule, IAtom theAtom, List<IAtom> theExceptionAtomList, List<Integer> theConnectedAtomTypeList) {
        List<IAtom> theConnectedAtomList = theMolecule.getConnectedAtomsList(theAtom);
        List<Integer> theCopiedAtomTypeList = new ArrayList<>(theConnectedAtomTypeList);

        if (theAtom == null) {
            return true;
        }

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            Integer theAtomType = (Integer) theConnectedAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);

            if (theCopiedAtomTypeList.contains(theAtomType) && !theExceptionAtomList.contains(theConnectedAtom)) {
                theCopiedAtomTypeList.remove(theAtomType);
            }
        }

        return theCopiedAtomTypeList.isEmpty();
    }
}
