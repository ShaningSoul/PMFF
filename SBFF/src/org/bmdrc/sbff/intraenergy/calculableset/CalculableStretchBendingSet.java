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
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractDerivativeFunctionOfInternalCoordinate;
import org.bmdrc.sbff.parameter.intraenergy.BendingParameter;
import org.bmdrc.sbff.parameter.intraenergy.StretchBendParameter;
import org.bmdrc.sbff.parameter.intraenergy.StretchParameter;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableStretchBendingSet extends AbstractCalculableSet implements Serializable {
    private static final long serialVersionUID = -4702821618513185050L;

    public enum StringIndex {
        FirstAtom, CenterAtom, SecondAtom, BondLengthInFirstStretch, BondConstantInFirstStretch, BondMomentInFirstStretch, BondLengthInSecondStretch, BondConstantInSecondStretch,
        BondMomentInSecondStretch, StandardAngle, BendingConstant, BendingBendingConstant, ConstantInStretchBendingParameter;
    }
    
    private StretchParameter itsFirstStretchParameter;
    private StretchParameter itsSecondStretchParameter;
    private BendingParameter itsBendingParameter;
    private StretchBendParameter itsStretchBendParameter;
    private Double itsDistanceGradient;
    private Double itsAngleGradient;
    private Double itsDistanceConjugatedGradient;
    private Double itsAngleConjugatedGradient;
    //constant Integer variable
    public static final int FIRST_ATOM = 0;
    public static final int CENTER_ATOM = 1;
    public static final int SECOND_ATOM = 2;
    
    public CalculableStretchBendingSet(Integer theFirstAtomIndex, Integer theCenterAtomIndex, Integer theSecondAtomIndex, StretchParameter theFirstStretchParameter, StretchParameter theSecondStretchParameter, BendingParameter theBendingParameter, StretchBendParameter theStretchBendParameter) {
        super();
        this.__generateAtomIndexList(theFirstAtomIndex, theCenterAtomIndex, theSecondAtomIndex);
        this.itsFirstStretchParameter = theFirstStretchParameter;
        this.itsSecondStretchParameter = theSecondStretchParameter;
        this.itsBendingParameter = theBendingParameter;
        this.itsStretchBendParameter = theStretchBendParameter;
        
        this.itsDistanceGradient = 0.0;
        this.itsAngleGradient = 0.0;
        
        this.itsDistanceConjugatedGradient = 0.0;
        this.itsAngleConjugatedGradient = 0.0;
    }

    public CalculableStretchBendingSet(CalculableBendingSet theCalculableBendingSet, StretchParameter theFirstStretchParameter, StretchParameter theSecondStretchParameter, StretchBendParameter theStretchBendParameter) {
        super();
        this.itsAtomIndexList = new ArrayList<>(theCalculableBendingSet.getAtomIndexList());
        this.itsFirstStretchParameter = theFirstStretchParameter;
        this.itsSecondStretchParameter = theSecondStretchParameter;
        this.itsBendingParameter = theCalculableBendingSet.getParameter();
        this.itsStretchBendParameter = theStretchBendParameter;
        
        this.itsDistanceGradient = 0.0;
        this.itsAngleGradient = 0.0;
        
        this.itsDistanceConjugatedGradient = 0.0;
        this.itsAngleConjugatedGradient = 0.0;
    }
    
    public Integer getFirstAtomIndex() {
        return this.itsAtomIndexList.get(CalculableStretchBendingSet.FIRST_ATOM);
    }

    public Integer getCenterAtomIndex() {
        return this.itsAtomIndexList.get(CalculableStretchBendingSet.CENTER_ATOM);
    }

    public Integer getSecondAtomIndex() {
        return this.itsAtomIndexList.get(CalculableStretchBendingSet.SECOND_ATOM);
    }

    public StretchParameter getFirstStretchParameter() {
        return itsFirstStretchParameter;
    }

    public StretchParameter getSecondStretchParameter() {
        return itsSecondStretchParameter;
    }

    public BendingParameter getBendingParameter() {
        return itsBendingParameter;
    }

    public StretchBendParameter getStretchBendParameter() {
        return itsStretchBendParameter;
    }

    public Double getDistanceGradient() {
        return itsDistanceGradient;
    }

    public void setDistanceGradient(Double theDistanceGradient) {
        this.itsDistanceGradient = theDistanceGradient;
    }

    public Double getAngleGradient() {
        return itsAngleGradient;
    }

    public void setAngleGradient(Double theAngleGradient) {
        this.itsAngleGradient = theAngleGradient;
    }

    public Double getDistanceConjugatedGradient() {
        return itsDistanceConjugatedGradient;
    }

    public void setDistanceConjugatedGradient(Double theDistanceConjugatedGradient) {
        this.itsDistanceConjugatedGradient = theDistanceConjugatedGradient;
    }

    public Double getAngleConjugatedGradient() {
        return itsAngleConjugatedGradient;
    }

    public void setAngleConjugatedGradient(Double theAngleConjugatedGradient) {
        this.itsAngleConjugatedGradient = theAngleConjugatedGradient;
    }

    private void __generateAtomIndexList(Integer theFirstAtomIndex, Integer theCenterAtomIndex, Integer theSecondAtomIndex) {
        this.itsAtomIndexList.add(theFirstAtomIndex);
        this.itsAtomIndexList.add(theCenterAtomIndex);
        this.itsAtomIndexList.add(theSecondAtomIndex);
    }
    
    public boolean equalAtomIndexSet(int theFirstAtomIndex, int theCenterAtomIndex, int theSecondAtomIndex) {
        if(!this.getCenterAtomIndex().equals(theCenterAtomIndex)) {
            return false;
        } else if(this.getFirstAtomIndex().equals(theFirstAtomIndex) && this.getSecondAtomIndex().equals(theSecondAtomIndex)) {
            return true;
        } else if(this.getFirstAtomIndex().equals(theSecondAtomIndex) && this.getSecondAtomIndex().equals(theFirstAtomIndex)) {
            return true;
        }
        
        return false;
    }
    
    public boolean containAtomIndex(int theAtomIndex) {
        if(this.getCenterAtomIndex().intValue() == theAtomIndex) {
            return true;
        } else if(this.getFirstAtomIndex().intValue() == theAtomIndex) {
            return true;
        } else if(this.getSecondAtomIndex().intValue() == theAtomIndex) {
            return true;
        }
        
        return false;
    }
    
    public List<Integer> getAtomIndexListInFirstBend() {
        List<Integer> theAtomIndexList = new ArrayList<>();
        
        theAtomIndexList.add(this.getFirstAtomIndex());
        theAtomIndexList.add(this.getCenterAtomIndex());
        
        return theAtomIndexList;
    }
    
    public List<Integer> getAtomIndexListInSecondBend() {
        List<Integer> theAtomIndexList = new ArrayList<>();
        
        theAtomIndexList.add(this.getSecondAtomIndex());
        theAtomIndexList.add(this.getCenterAtomIndex());
        
        return theAtomIndexList;
    }
    
    public List<Integer> getAtomIndexlistByBond(int theTargetAtomIndex) {
        if(theTargetAtomIndex == AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_BEND_ANGLE) {
            return this.getAtomIndexListInFirstBend();
        } else if(theTargetAtomIndex == AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_BEND_ANGLE) {
            return this.getAtomIndexListInSecondBend();
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.getFirstAtomIndex()).append("\t").append(this.getCenterAtomIndex()).append("\t").append(this.getSecondAtomIndex()).append("\t")
                .append(this.itsFirstStretchParameter.getBondLength()).append("\t").append(this.itsFirstStretchParameter.getConstant()).append("\t")
                .append(this.itsFirstStretchParameter.getBondMoment()).append("\t").append(this.itsSecondStretchParameter.getBondLength()).append("\t")
                .append(this.itsSecondStretchParameter.getConstant()).append("\t").append(this.itsSecondStretchParameter.getBondMoment()).append("\t")
                .append(this.itsBendingParameter.getStandardAngle()).append("\t").append(this.itsBendingParameter.getBendingConstant()).append("\t")
                .append(this.itsBendingParameter.getBendingBendingConstant()).append("\t").append(this.itsStretchBendParameter.getConstant());
        
        return theStringBuilder.toString();
    }

    @Override
    public void makeConjugatedGradient(Double theScalingFactor) {
        this.itsDistanceConjugatedGradient += this.itsDistanceGradient * theScalingFactor;
        this.itsAngleConjugatedGradient += this.itsAngleGradient * theScalingFactor;
    }
}
