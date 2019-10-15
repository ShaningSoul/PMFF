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
 * Atom Type Description <br>
 * 0 : No matched atom type <br>
 * 1 : Amide Hydrogen <br>
 * 2 : Hydrogen in CO2H <br>
 * 3 : Carbonyl carbon in carboxylic group <br>
 * 4 : Carbonyl carbon in amide <br>
 * 5 : Carbonyl oxygen in carboxylic group <br>
 * 6 : Carbonyl oxygen in amide <br>
 * 7 : sp3 oxygen in CO2H <br>
 * 8 : Nitrogen in amide <br>
 * 
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */

public class SbffHydrogenBondParameterSet implements Serializable{
    private static final long serialVersionUID = 5664469064234799254L;

    private List<SbffHydrogenBondParameter> itsParameterSet;
    private List<Integer> itsAtomTypeList;
    //constant Integer variable
    private final int HA_PARAMETER_INDEX = 0;
    private final int XA_PARAMETER_INDEX = 1;
    private final int BH_PARAMETER_INDEX = 2;

    public SbffHydrogenBondParameterSet() {
        this.setParameterSet(new ArrayList<SbffHydrogenBondParameter>());
        this.setAtomTypeList(new ArrayList<Integer>());
    }

    public SbffHydrogenBondParameterSet(List<SbffHydrogenBondParameter> theParameterSet, List<Integer> theAtomTypeList) {
        this.itsParameterSet = theParameterSet;
        this.itsAtomTypeList = theAtomTypeList;
    }

    public List<SbffHydrogenBondParameter> getParameterSet() {
        return itsParameterSet;
    }

    public void setParameterSet(List<SbffHydrogenBondParameter> theParameterSet) {
        this.itsParameterSet = theParameterSet;
    }
    
    public List<SbffHydrogenBondParameter> setParameterSet() {
        return itsParameterSet;
    }

    public List<Integer> getAtomTypeList() {
        return itsAtomTypeList;
    }

    public void setAtomTypeList(List<Integer> theAtomTypeList) {
        this.itsAtomTypeList = theAtomTypeList;
    }

    public List<Integer> setAtomTypeList() {
        return itsAtomTypeList;
    }

    public SbffHydrogenBondParameter getHAParameter() {
        return this.itsParameterSet.get(this.HA_PARAMETER_INDEX);
    }
    
    public SbffHydrogenBondParameter getXAParameter() {
        return this.itsParameterSet.get(this.XA_PARAMETER_INDEX);
    }
    
    public SbffHydrogenBondParameter getBHParameter() {
        return this.itsParameterSet.get(this.BH_PARAMETER_INDEX);
    }
}
