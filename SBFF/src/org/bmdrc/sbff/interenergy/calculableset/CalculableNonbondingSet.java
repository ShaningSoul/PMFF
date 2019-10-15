/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.interenergy.calculableset;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableNonbondingSet extends AbstractCalculableSet implements Serializable {
    private static final long serialVersionUID = 1604560695703846308L;

    private Double itsEpsilon;
    private Double itsSigma;
    //constant Integer variable
    public static final int FIRST_ATOM_NUMBER_INDEX = 0;
    public static final int SECOND_ATOM_NUMBER_INDEX = 1;
    public static final int EPSILON_INDEX = 2;
    public static final int SIGMA_INDEX = 3;

    public CalculableNonbondingSet(Integer theFirstAtomIndex, Integer theSecondAtomIndex, Double theEpsilon, Double theSigma) {
        super();
        this._initializeAtomIndexList(theFirstAtomIndex, theSecondAtomIndex);
        this.itsEpsilon = theEpsilon;
        this.itsSigma = theSigma;
    }

    public Integer getFirstAtomIndex() {
        return this.itsAtomIndexList.get(this.FIRST_INDEX);
    }

    public Integer getSecondAtomIndex() {
        return this.itsAtomIndexList.get(this.SECOND_INDEX);
    }

    public Double getEpsilon() {
        return itsEpsilon;
    }

    public Double getSigma() {
        return itsSigma;
    }

    public boolean containAtomIndex(int theAtomIndex) {
        return this.itsAtomIndexList.contains(theAtomIndex);
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.getFirstAtomIndex()).append("\t").append(this.getSecondAtomIndex()).append("\t").append(this.itsEpsilon).append("\t").append(this.itsSigma);
        
        return theStringBuilder.toString();
    }
}
