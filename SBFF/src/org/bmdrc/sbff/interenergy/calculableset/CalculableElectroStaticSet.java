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
public class CalculableElectroStaticSet extends AbstractCalculableSet implements Serializable {
    private static final long serialVersionUID = 8619240119181943783L;

    public enum StringIndex {
        FirstAtom, SecondAtom;
    }
    
    public CalculableElectroStaticSet(Integer theFirstAtomIndex, Integer theSecondAtomIndex) {
        super();
        this._initializeAtomIndexList(theFirstAtomIndex, theSecondAtomIndex);
    }

    public CalculableElectroStaticSet(CalculableElectroStaticSet theCalculableElectroStaticSet) {
        super(theCalculableElectroStaticSet);
    }
    
    public Integer getFirstAtomIndex() {
        return this.itsAtomIndexList.get(this.FIRST_INDEX);
    }

    public Integer getSecondAtomIndex() {
        return this.itsAtomIndexList.get(this.SECOND_INDEX);
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.getFirstAtomIndex()).append("\t").append(this.getSecondAtomIndex());
        
        return theStringBuilder.toString();
    }
}
