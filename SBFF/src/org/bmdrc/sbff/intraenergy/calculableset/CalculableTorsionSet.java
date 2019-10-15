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
import org.bmdrc.sbff.parameter.intraenergy.TorsionParameter;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableTorsionSet extends AbstractCalculableSet implements Serializable {
    private static final long serialVersionUID = 7698180985015931979L;

    public enum StringIndex {
        FirstConnectedAtom, FirstAtom, SecondAtom, SecondConnectedAtom, V1, V2, V3;
    }
    private TorsionParameter itsParameter;
    
    //constant Integer variable
    public static final int FIRST_CONNECTED_ATOM = 0;
    public static final int FIRST_ATOM = 1;
    public static final int SECOND_ATOM = 2;
    public static final int SECOND_CONNECTED_ATOM = 3;
    
    
    public CalculableTorsionSet(Integer theFirstConnectedAtomIndex, Integer theFirstAtomIndex, Integer theSecondAtomIndex, Integer theSecondConnectedAtomIndex, TorsionParameter theParameter) {
        super();
        this._initializeAtomIndexList(theFirstConnectedAtomIndex, theFirstAtomIndex, theSecondAtomIndex, theSecondConnectedAtomIndex);
        this.itsParameter = theParameter;
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

    public TorsionParameter getParameter() {
        return itsParameter;
    }
    
    public boolean equalAtomSet(int theFirstConnectedAtomIndex, int theFirstAtomIndex, int theSecondAtomIndex, int theSecondConnectedAtomIndex) {
        if(this.getFirstConnectedAtomIndex().equals(theFirstConnectedAtomIndex) && this.getFirstAtomIndex().equals(theFirstAtomIndex) 
                && this.getSecondAtomIndex().equals(theSecondAtomIndex) && this.getSecondConnectedAtomIndex().equals(theSecondConnectedAtomIndex)) {
            return true;
        } else if(this.getFirstConnectedAtomIndex().equals(theSecondConnectedAtomIndex) && this.getFirstAtomIndex().equals(theSecondAtomIndex) &&
                this.getSecondAtomIndex().equals(theFirstAtomIndex) && this.getSecondConnectedAtomIndex().equals(theFirstConnectedAtomIndex)) {
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
        
        theStringBuilder.append(this.getFirstConnectedAtomIndex()).append("\t").append(this.getFirstAtomIndex()).append("\t").append(this.getSecondAtomIndex()).append("\t")
                .append(this.getSecondConnectedAtomIndex()).append("\t").append(this.itsParameter.getV1()).append("\t").append(this.itsParameter.getV2()).append("\t")
                .append(this.itsParameter.getV3());
        
        return theStringBuilder.toString();
    }
}
