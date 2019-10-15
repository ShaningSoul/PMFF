/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.parameter.interenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffHydrogenBondParameter implements Serializable {
    private static final long serialVersionUID = 4179323236987031306L;

    private List<Integer> itsAtomType;
    private Double itsB;
    private Double itsD;
    private String itsParameterType;
    //constant String variable
    public static final String EMPTY_PARAMETER = "Empty";
    public static final String HA_PARAMETER = "HA";
    public static final String BH_PARAMETER = "BH";
    public static final String XA_PARAMETER = "XA";

    public SbffHydrogenBondParameter() {
        this.setAtomType(new ArrayList<Integer>());
        this.itsB = 0.0;
        this.itsD = 0.0;
        this.itsParameterType = SbffHydrogenBondParameter.EMPTY_PARAMETER;
    }

    public SbffHydrogenBondParameter(List<Integer> theAtomType, Double theB, Double theD, String theParameterType) {
        this.itsAtomType = theAtomType;
        this.itsB = theB;
        this.itsD = theD;
        this.itsParameterType = theParameterType;
    }

    public List<Integer> getAtomType() {
        return itsAtomType;
    }

    public void setAtomType(List<Integer> theAtomType) {
        this.itsAtomType = theAtomType;
    }

    public List<Integer> setAtomType() {
        return itsAtomType;
    }

    public Double getB() {
        return itsB;
    }

    public void setB(Double theB) {
        this.itsB = theB;
    }

    public Double getD() {
        return itsD;
    }

    public void setD(Double theD) {
        this.itsD = theD;
    }

    public String getParameterType() {
        return itsParameterType;
    }

    public void setParameterType(String theParameterType) {
        this.itsParameterType = theParameterType;
    }
}
