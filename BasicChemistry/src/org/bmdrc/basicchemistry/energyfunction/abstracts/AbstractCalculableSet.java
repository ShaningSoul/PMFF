/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.energyfunction.abstracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bmdrc.basicchemistry.vector.Vector3d;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public abstract class AbstractCalculableSet implements Serializable {

    private static final long serialVersionUID = -7749779000070414728L;

    protected List<Integer> itsAtomIndexList;
    protected Map<Object, Object> itsPropertyMap;
    protected Map<Integer, Vector3d> itsGradiVectorMap;
    protected Double itsEnergy;
    protected Double itsGradient;
    protected Double itsConjugatedGradient;
    //constant Integer variable
    protected final int FIRST_INDEX = 0;
    protected final int SECOND_INDEX = 1;
    protected final int THIRD_INDEX = 2;
    protected final int FOURTH_INDEX = 3;
    
    public AbstractCalculableSet() {
        this.itsAtomIndexList = new ArrayList<>();
        this.itsPropertyMap = new HashMap<>();
        this.itsGradient = 0.0;
        this.itsConjugatedGradient = 0.0;
    }

    public AbstractCalculableSet(AbstractCalculableSet theAbstractCalculableSet) {
        this.itsAtomIndexList = new ArrayList<>(theAbstractCalculableSet.itsAtomIndexList);
        this.itsPropertyMap = new HashMap<>(theAbstractCalculableSet.itsPropertyMap);
        this.itsGradient = theAbstractCalculableSet.itsGradient;
        this.itsConjugatedGradient = theAbstractCalculableSet.itsConjugatedGradient;
    }
    
    public List<Integer> getAtomIndexList() {
        return itsAtomIndexList;
    }
   
    public Map<Object, Object> getProperties() {
        return itsPropertyMap;
    }

    public void setProperties(Map<Object, Object> thePropertyMap) {
        this.itsPropertyMap = thePropertyMap;
    }
    
    public Object getProperty(Object theKey) {
        return this.itsPropertyMap.get(theKey);
    }
    
    public void setProperty(Object theKey, Object theValue) {
        this.itsPropertyMap.put(theKey, theValue);
    }

    public Double getEnergy() {
        return itsEnergy;
    }

    public void setEnergy(Double theEnergy) {
        this.itsEnergy = theEnergy;
    }

    public Double getGradient() {
        return itsGradient;
    }

    public void setGradient(Double theGradient) {
        this.itsGradient = theGradient;
    }

    public Double getConjugatedGradient() {
        return itsConjugatedGradient;
    }

    public void setConjugatedGradient(Double theConjugatedGradient) {
        this.itsConjugatedGradient = theConjugatedGradient;
    }
    
    protected void _initializeAtomIndexList(Integer... theAtomIndex) {
        Collections.addAll(this.itsAtomIndexList, theAtomIndex);
    }
    
    public void setGradientVector(Integer theAtomIndex, Vector3d theGradientVector) {
        this.itsGradiVectorMap.put(theAtomIndex, theGradientVector);
    }

    public void makeConjugatedGradient(Double theScalingFactor) {
        this.itsConjugatedGradient += this.itsGradient * theScalingFactor;
    }
}
