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
import org.bmdrc.sbff.parameter.intraenergy.OutOfPlaneBendParameter;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableOutOfPlaneSet extends AbstractCalculableSet implements Serializable {
    private static final long serialVersionUID = 1619502708397470619L;

    public enum StringIndex {
        CenterAtom, ConnectedFirstAtom, ConnectedSecondAtom, ConnectedThirdAtom, Constant, Angle;
    }
    
    private OutOfPlaneBendParameter itsParameter;
    //constant Integer variable
    public static final int CENTER_ATOM = 0;
    public static final int CONNECTED_FIRST_ATOM = 1;
    public static final int CONNECTED_SECOND_ATOM = 2;
    public static final int CONNECTED_THIRD_ATOM = 3;
    public static final int TOTAL_ATOM_COUNT = 4;
    
    public CalculableOutOfPlaneSet(Integer theCenterAtomIndex, Integer theConnectedFirstAtomIndex, Integer theConnectedSecondAtomIndex, Integer theConnectedThirdAtomIndex, 
            OutOfPlaneBendParameter theParameter) {
        super();
        this.__generateAtomIndexList(theCenterAtomIndex, theConnectedFirstAtomIndex, theConnectedSecondAtomIndex, theConnectedThirdAtomIndex);
        this.itsParameter = theParameter;
    }

    private void __generateAtomIndexList(Integer theCenterAtomIndex, Integer theConnectedFirstAtomIndex, Integer theConnectedSecondAtomIndex, Integer theConnectedThirdAtomIndex) {
        this.itsAtomIndexList.add(theCenterAtomIndex);
        this.itsAtomIndexList.add(theConnectedFirstAtomIndex);
        this.itsAtomIndexList.add(theConnectedSecondAtomIndex);
        this.itsAtomIndexList.add(theConnectedThirdAtomIndex);
    }

    public Integer getCenterAtomIndex() {
        return this.itsAtomIndexList.get(CalculableOutOfPlaneSet.CENTER_ATOM);
    }

    public Integer getConnectedFirstAtomIndex() {
        return this.itsAtomIndexList.get(CalculableOutOfPlaneSet.CONNECTED_FIRST_ATOM);
    }

    public Integer getConnectedSecondAtomIndex() {
        return this.itsAtomIndexList.get(CalculableOutOfPlaneSet.CONNECTED_SECOND_ATOM);
    }

    public Integer getConnectedThirdAtomIndex() {
        return this.itsAtomIndexList.get(CalculableOutOfPlaneSet.CONNECTED_THIRD_ATOM);
    }

    public OutOfPlaneBendParameter getParameter() {
        return itsParameter;
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.getCenterAtomIndex()).append("\t").append(this.getConnectedFirstAtomIndex()).append("\t").append(this.getConnectedSecondAtomIndex())
                .append("\t").append(this.getConnectedThirdAtomIndex()).append("\t").append(this.itsParameter.getConstant()).append("\t").append(this.itsParameter.getAngle());
        
        return theStringBuilder.toString();
    }
}
