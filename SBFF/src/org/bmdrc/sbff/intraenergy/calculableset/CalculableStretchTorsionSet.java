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
import org.bmdrc.sbff.parameter.intraenergy.StretchParameter;
import org.bmdrc.sbff.parameter.intraenergy.StretchTorsionParameter;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableStretchTorsionSet extends AbstractCalculableSet implements Serializable {
    private static final long serialVersionUID = 2541238447932600195L;

    public enum StringIndex {
        FirstConnectedAtom, FirstAtom, SecondAtom, SecondConnectedAtom, BondLengthInParameter, BondConstantInParameter, BondMomentInParameter,
        ConstantInStretchTorsionParameter;
    }
    private StretchParameter itsStretchParameter;
    private StretchTorsionParameter itsStretchTorsionParameter;
    private Double itsDistanceGradient;
    private Double itsAngleGradient;
    private Double itsDistanceConjugatedGradient;
    private Double itsAngleConjugatedGradient;
    
    public CalculableStretchTorsionSet(Integer theFirstConnectedAtomIndex, Integer theFirstAtomIndex, Integer theSecondAtomIndex, Integer theSecondConnectedAtomIndex, StretchParameter theStretchParameter, StretchTorsionParameter theStretchTorsionParameter) {
        super();
        this.__generateAtomIndexList(theFirstConnectedAtomIndex, theFirstAtomIndex, theSecondAtomIndex, theSecondConnectedAtomIndex);
        this.itsStretchParameter = theStretchParameter;
        this.itsStretchTorsionParameter = theStretchTorsionParameter;
        
        this.itsDistanceConjugatedGradient = 0.0;
        this.itsAngleConjugatedGradient = 0.0;
        
        this.itsAngleGradient = 0.0;
        this.itsDistanceGradient = 0.0;
    }

    public CalculableStretchTorsionSet(CalculableTorsionSet theCalculableTorsionSet, StretchParameter theStretchParameter, StretchTorsionParameter theStretchTorsionParameter) {
        super();
        this.itsAtomIndexList = new ArrayList<>(theCalculableTorsionSet.getAtomIndexList());
        this.itsStretchParameter = theStretchParameter;
        this.itsStretchTorsionParameter = theStretchTorsionParameter;
        
        this.itsDistanceConjugatedGradient = 0.0;
        this.itsAngleConjugatedGradient = 0.0;
        
        this.itsAngleGradient = 0.0;
        this.itsDistanceGradient = 0.0;
    }
    
    private void __generateAtomIndexList(Integer theFirstConnectedAtomIndex, Integer theFirstAtomIndex, Integer theSecondAtomIndex, Integer theSecondConnectedAtomIndex) {
        this.itsAtomIndexList.add(theFirstConnectedAtomIndex);
        this.itsAtomIndexList.add(theFirstAtomIndex);
        this.itsAtomIndexList.add(theSecondAtomIndex);
        this.itsAtomIndexList.add(theSecondConnectedAtomIndex);
    }

    
    public Integer getFirstConnectedAtomIndex() {
        return this.itsAtomIndexList.get(this.FIRST_INDEX);
    }

    public Integer getFirstAtomIndex() {
        return this.itsAtomIndexList.get(this.SECOND_INDEX);
    }

    public Integer getSecondAtomIndex() {
        return this.itsAtomIndexList.get(this.THIRD_INDEX);
    }

    public Integer getSecondConnectedAtomIndex() {
        return this.itsAtomIndexList.get(this.FOURTH_INDEX);
    }

    public StretchParameter getStretchParameter() {
        return itsStretchParameter;
    }

    public StretchTorsionParameter getStretchTorsionParameter() {
        return itsStretchTorsionParameter;
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

    public boolean containAtomIndex(int theAtomIndex) {
        return this.itsAtomIndexList.contains(theAtomIndex);
    }
    
    public List<Integer> getDistanceAtomIndexList() {
        List<Integer> theAtomIndexList = new ArrayList<>();
        
        theAtomIndexList.add(this.getSecondAtomIndex());
        theAtomIndexList.add(this.getFirstAtomIndex());
        
        return theAtomIndexList;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.getFirstConnectedAtomIndex()).append("\t").append(this.getFirstAtomIndex()).append("\t").append(this.getSecondAtomIndex()).append("\t")
                .append(this.getSecondConnectedAtomIndex()).append("\t").append(this.itsStretchParameter.getBondLength()).append("\t").append(this.itsStretchParameter.getConstant())
                .append("\t").append(this.itsStretchParameter.getBondMoment()).append("\t").append(this.itsStretchTorsionParameter.getConstant());
        
        return theStringBuilder.toString();
    }

    @Override
    public void makeConjugatedGradient(Double theScalingFactor) {
        this.itsDistanceConjugatedGradient += this.itsDistanceGradient * theScalingFactor;
        this.itsAngleConjugatedGradient += this.itsAngleGradient * theScalingFactor;
    }
}
