/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.sbff.intraenergy.calculableset;

import java.io.Serializable;
import java.util.ArrayList;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2015. 09. 03
 */
public class CalculableIntraNonbondingSet extends AbstractCalculableSet implements Serializable {
    private static final long serialVersionUID = 7765512422863542418L;

    public enum StringIndex {
        FirstAtom, SecondAtom, Epsilon, SumOfVdwRadius;
    }
    
    private Double itsEpsilonInMM3;
    private Double itsEpsilonInSbff;
    private Double itsSumOfVdwRadius;
    private Double itsSigma;
    
    /**
     * Constructor
     * 
     * @param theFirstAtomNumber Atom number in molecule
     * @param theSecondAtomNumber Atom number in molecule
     * @param theEpsilonInMM3 Epsilon used in Intra-nonbonding energy
     * @param theSumOfVdwRadius Sum of Vdw radius used in Intra-nonbonding energy
     */
    public CalculableIntraNonbondingSet(Integer theFirstAtomNumber, Integer theSecondAtomNumber, Double theEpsilonInMM3, Double theSumOfVdwRadius,
            Double theEpsilonInSbff, Double theSigma) {
        super();
        this.itsEpsilonInMM3 = theEpsilonInMM3;
        this.itsEpsilonInSbff = theEpsilonInSbff;
        this.itsSumOfVdwRadius = theSumOfVdwRadius;
        this.itsSigma = theSigma;
        
        this.__generateAtomIndexList(theFirstAtomNumber, theSecondAtomNumber);
    }

    /**
     * get first atom number in molecule
     * 
     * @return First atom number in molecule
     */
    public Integer getFirstAtomNumber() {
        return this.itsAtomIndexList.get(this.FIRST_INDEX);
    }

    /**
     * get second atom number in molecule
     * 
     * @return Second atom number in molecule
     */
    public Integer getSecondAtomNumber() {
        return this.itsAtomIndexList.get(this.SECOND_INDEX);
    }

    /**
     * get epsilon used to calculate intra-nonbonding energy
     * 
     * @return Epsilon to form double 
     */
    public Double getEpsilonInMM3() {
        return itsEpsilonInMM3;
    }

    public Double getEpsilonInSbff() {
        return itsEpsilonInSbff;
    }

    public Double getSigma() {
        return itsSigma;
    }

    /**
     * get sum of VDW radius used to calculate intra-nonbonding energy
     * 
     * @return Sum of VDW radius to form double
     */
    public Double getSumOfVdwRadius() {
        return itsSumOfVdwRadius;
    }

    /**
     * get property value
     * 
     * @param theObject property key
     * @return property value
     */
    public Object getProperty(Object theObject) {
        return this.itsPropertyMap.get(theObject);
    }
    
    private void __generateAtomIndexList(Integer theFirstAtomNumber, Integer theSecondAtomNumber) {
        this.itsAtomIndexList = new ArrayList<>();
        
        this.itsAtomIndexList.add(theFirstAtomNumber);
        this.itsAtomIndexList.add(theSecondAtomNumber);
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.getFirstAtomNumber()).append("\t").append(this.getSecondAtomNumber()).append("\t").append(this.itsEpsilonInMM3).append("\t")
                .append(this.itsEpsilonInSbff).append("\t").append(this.itsSumOfVdwRadius).append("\t").append(this.itsSigma);
        
        return theStringBuilder.toString();
    }
}
