/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.sbff.parameter.intraenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 03
 */
public class MM3NonbondingParameter implements Serializable {

    private static final long serialVersionUID = 1040644341864663024L;

    private Double itsRadius;
    private Double itsEpsilon;
    private List<Integer> itsConnectedAtomTypeList;

    /**
     * Constructor
     * 
     * @param theRadius Van der Waals radius
     * @param theEpsilon Epsilon
     * @param theConnectedAtomTypeList Atom type defined in Schrodinger
     */
    public MM3NonbondingParameter(Double theRadius, Double theEpsilon, Integer... theConnectedAtomTypeList) {
        this.itsRadius = theRadius;
        this.itsEpsilon = theEpsilon;
        this.itsConnectedAtomTypeList = new ArrayList<>();

        if (theConnectedAtomTypeList.length > 0) {
            Collections.addAll(this.itsConnectedAtomTypeList, theConnectedAtomTypeList);
        }
    }

    /**
     * get Atomic Radius
     * 
     * @return double value in atomic raidus
     */
    public Double getRadius() {
        return itsRadius;
    }

    /**
     * get epsilon
     * 
     * @return double value by epsilon
     */
    public Double getEpsilon() {
        return itsEpsilon;
    }
    
    /**
     * get connected atom type list
     * 
     * @return integer list in connected atom type
     */
    public List<Integer> getConnectedAtomTypeList() {
        return itsConnectedAtomTypeList;
    }
}
