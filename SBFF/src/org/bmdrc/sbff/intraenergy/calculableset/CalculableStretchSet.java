/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.intraenergy.calculableset;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;
import org.bmdrc.sbff.parameter.intraenergy.StretchParameter;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableStretchSet extends AbstractCalculableSet implements Serializable {
    private static final long serialVersionUID = -5027562255855670136L;

    public enum StringIndex {
        FirstAtom, SecondAtom, BondLengthInParameter, BondConstantInParameter, BondMomentInParameter;
    }
    
    private StretchParameter itsParameter;
    //constant Integer variable
    public static final int FIRST_ATOM = 0;
    public static final int SECOND_ATOM = 1;

    public CalculableStretchSet(Integer theFirstAtomIndex, Integer theSecondAtomIndex, StretchParameter theParameter) {
        super();
        this.__generateAtomIndexList(theFirstAtomIndex, theSecondAtomIndex);
        this.itsParameter = theParameter;
    }

    public Integer getFirstAtomIndex() {
        return this.itsAtomIndexList.get(CalculableStretchSet.FIRST_ATOM);
    }

    public Integer getSecondAtomIndex() {
        return this.itsAtomIndexList.get(CalculableStretchSet.SECOND_ATOM);
    }

    public StretchParameter getParameter() {
        return itsParameter;
    }
    
    private void __generateAtomIndexList(Integer theFirstAtomIndex, Integer theSecondAtomIndex) {
        this.itsAtomIndexList.add(theFirstAtomIndex);
        this.itsAtomIndexList.add(theSecondAtomIndex);
    }

    public boolean equalAtomIndexSet(int theFirstAtomIndex, int theSecondAtomIndex) {
        if(this.getFirstAtomIndex().equals(theFirstAtomIndex) && this.getSecondAtomIndex().equals(theSecondAtomIndex)) {
            return true;
        } else if(this.getFirstAtomIndex().equals(theSecondAtomIndex) && this.getSecondAtomIndex().equals(theFirstAtomIndex)) {
            return true;
        }
        
        return false;
    }
    
    public boolean containAtomIndex(int theAtomIndex) {
        if(this.getFirstAtomIndex().intValue() == theAtomIndex) {
            return true;
        } else if(this.getSecondAtomIndex().intValue() == theAtomIndex) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
             
        theStringBuilder.append(this.getFirstAtomIndex()).append("\t").append(this.getSecondAtomIndex()).append("\t").append(this.itsParameter.getBondLength()).append("\t")
                .append(this.itsParameter.getConstant()).append("\t").append(this.itsParameter.getBondMoment());
        
        return theStringBuilder.toString();
    }
}
