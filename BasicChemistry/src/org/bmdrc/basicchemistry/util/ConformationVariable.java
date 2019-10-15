/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class ConformationVariable {

    private List<Integer> itsAtomIndexList;
    private Integer itsNumberOfStep;
    private Double itsInterval;
    private Integer itsVariableType;
    //constant Integer variable
    public static final int DISTANCE_VARIABLE = 0;
    public static final int BOND_ANGLE_VARIABLE = 1;
    public static final int TORSION_ANGLE_VARIABLE = 2;

    public ConformationVariable(List<Integer> theAtomIndexList, int theNumberOfStep, Double theInterval, Integer theVariableType) {
        this.itsAtomIndexList = theAtomIndexList;
        this.itsNumberOfStep = theNumberOfStep;
        this.itsInterval = theInterval;
        this.itsVariableType = theVariableType;
    }
    
    public ConformationVariable(ConformationVariable theConformationVariable) {
        this.itsAtomIndexList = new ArrayList<>(theConformationVariable.itsAtomIndexList);
        this.itsNumberOfStep = theConformationVariable.itsNumberOfStep;
        this.itsInterval = theConformationVariable.itsInterval;
        this.itsVariableType = theConformationVariable.itsVariableType;
    }

    public List<Integer> getAtomIndexList() {
        return itsAtomIndexList;
    }

    public Integer getNumberOfStep() {
        return itsNumberOfStep;
    }

    public Double getInterval() {
        return itsInterval;
    }

    public Integer getVariableType() {
        return itsVariableType;
    }
}
