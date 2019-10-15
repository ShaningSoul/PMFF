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
import org.bmdrc.sbff.parameter.intraenergy.BendingParameter;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableBendingSet extends AbstractCalculableSet implements Serializable {
    private static final long serialVersionUID = 646492505902867850L;

    public enum StringIndex {
        FirstAtom, CenterAtom, SecondAtom, StandardAngle, BendingConstant, BendingBendingConstant;
    }
    private BendingParameter itsParameter;
    //constant Integer variable
    public static final int FIRST_ATOM = 0;
    public static final int CENTER_ATOM = 1;
    public static final int SECOND_ATOM = 2;

    public CalculableBendingSet(Integer theFirstAtomIndex, Integer theCenterAtomIndex, Integer theSecondAtomIndex, BendingParameter theParameter) {
        super();
        this._initializeAtomIndexList(theFirstAtomIndex, theCenterAtomIndex, theSecondAtomIndex);
        this.itsParameter = theParameter;
    }

    public CalculableBendingSet(CalculableBendingSet theCalculableBendingSet) {
        super(theCalculableBendingSet);
        this.itsParameter = theCalculableBendingSet.itsParameter;
    }
    
    public Integer getFirstAtomIndex() {
        return this.itsAtomIndexList.get(CalculableBendingSet.FIRST_ATOM);
    }

    public Integer getCenterAtomIndex() {
        return this.itsAtomIndexList.get(CalculableBendingSet.CENTER_ATOM);
    }

    public Integer getSecondAtomIndex() {
        return this.itsAtomIndexList.get(CalculableBendingSet.SECOND_ATOM);
    }

    public BendingParameter getParameter() {
        return itsParameter;
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
        return this.itsAtomIndexList.contains(theAtomIndex);
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.getFirstAtomIndex()).append("\t").append(this.getCenterAtomIndex()).append("\t").append(this.getSecondAtomIndex()).append("\t")
                .append(this.itsParameter.getStandardAngle()).append("\t").append(this.itsParameter.getBendingConstant()).append("\t")
                .append(this.itsParameter.getBendingBendingConstant());
        
        return theStringBuilder.toString();
    }
}
