/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.interenergy.calculableset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondParameterSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableHydrogenBondSet extends AbstractCalculableSet implements Serializable {

    public enum ComponentIndex {
        HA, XA, BH;
    }

    private static final long serialVersionUID = 3449161209166959851L;

    private SbffHydrogenBondParameterSet itsHydrogenBondParameter;
    private List<CalculableHydrogenBondComponent> itsComponentList;
    //public Integer variable

    public CalculableHydrogenBondSet(Integer theXAtomIndex, Integer theHAtomIndex, Integer theAAtomIndex, Integer theBAtomIndex) throws NullPointerException {
        super();
        this._initializeAtomIndexList(theXAtomIndex, theHAtomIndex, theAAtomIndex, theBAtomIndex);
        this.__initializeComponent();
    }

    public CalculableHydrogenBondSet(Integer theXAtomIndex, Integer theHAtomIndex, Integer theAAtomIndex, Integer theBAtomIndex, SbffHydrogenBondParameterSet theHydrogenBondParameter) 
    throws NullPointerException {
        super();
        this._initializeAtomIndexList(theXAtomIndex, theHAtomIndex, theAAtomIndex, theBAtomIndex);
        this.itsHydrogenBondParameter = theHydrogenBondParameter;
        this.__initializeComponent();
    }

    public CalculableHydrogenBondSet(CalculableHydrogenBondSet theCalculableHydrogenBondSet) throws NullPointerException {
        super(theCalculableHydrogenBondSet);
        this.itsHydrogenBondParameter = theCalculableHydrogenBondSet.itsHydrogenBondParameter;
        this.__initializeComponent();
    }

    public Integer getXAtomIndex() {
        return this.itsAtomIndexList.get(this.FIRST_INDEX);
    }

    public Integer getHAtomIndex() {
        return this.itsAtomIndexList.get(this.SECOND_INDEX);
    }

    public Integer getAAtomIndex() {
        return this.itsAtomIndexList.get(this.THIRD_INDEX);
    }

    public Integer getBAtomIndex() {
        return this.itsAtomIndexList.get(this.FOURTH_INDEX);
    }

    public SbffHydrogenBondParameterSet getHydrogenBondParameter() {
        return itsHydrogenBondParameter;
    }

    public void setHydrogenBondParameter(SbffHydrogenBondParameterSet theHydrogenBondParameter) {
        this.itsHydrogenBondParameter = theHydrogenBondParameter;
        this.getHAComponent().setParameter(theHydrogenBondParameter.getHAParameter());
        this.getXAComponent().setParameter(theHydrogenBondParameter.getXAParameter());
        this.getBHComponent().setParameter(theHydrogenBondParameter.getBHParameter());
    }

    public List<CalculableHydrogenBondComponent> getComponentList() {
        return itsComponentList;
    }

    public CalculableHydrogenBondComponent getHAComponent() {
        return this.itsComponentList.get(CalculableHydrogenBondSet.ComponentIndex.HA.ordinal());
    }

    public CalculableHydrogenBondComponent getXAComponent() {
        return this.itsComponentList.get(CalculableHydrogenBondSet.ComponentIndex.XA.ordinal());
    }

    public CalculableHydrogenBondComponent getBHComponent() {
        return this.itsComponentList.get(CalculableHydrogenBondSet.ComponentIndex.BH.ordinal());
    }

    private void __initializeComponent() throws NullPointerException {
        this.itsComponentList = new ArrayList<>();

        this.itsComponentList.add(new CalculableHydrogenBondComponent(this.getHAtomIndex(), this.getAAtomIndex(), this.itsHydrogenBondParameter.getHAParameter()));
        this.itsComponentList.add(new CalculableHydrogenBondComponent(this.getXAtomIndex(), this.getAAtomIndex(), this.itsHydrogenBondParameter.getXAParameter()));
        this.itsComponentList.add(new CalculableHydrogenBondComponent(this.getBAtomIndex(), this.getHAtomIndex(), this.itsHydrogenBondParameter.getBHParameter()));
    }

    public boolean containAtomIndexSet(int theFirstAtomIndex, int theSecondAtomIndex) {
        return this.itsAtomIndexList.contains(theFirstAtomIndex) && this.itsAtomIndexList.contains(theSecondAtomIndex);
    }

    public boolean containAtomIndex(int theAtomIndex) {
        return this.itsAtomIndexList.contains(theAtomIndex);
    }
    
    public boolean isXBAtomIndex(int theFirstIndex, int theSecondIndex) {
        if(this.itsAtomIndexList.get(Constant.FIRST_INDEX).equals(theFirstIndex) && this.itsAtomIndexList.get(Constant.THIRD_INDEX).equals(theSecondIndex)) {
            return true;
        } else if(this.itsAtomIndexList.get(Constant.FIRST_INDEX).equals(theSecondIndex) && this.itsAtomIndexList.get(Constant.THIRD_INDEX).equals(theFirstIndex)) {
            return true;
        }
        
        return false;
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        if (this.itsHydrogenBondParameter != null) {
            theStringBuilder.append(this.getXAtomIndex()).append("\t").append(this.getHAtomIndex()).append("\t").append(this.getAAtomIndex()).append("\t")
                    .append(this.getBAtomIndex()).append("\t").append(this.itsHydrogenBondParameter.getHAParameter().getB()).append("\t")
                    .append(this.itsHydrogenBondParameter.getHAParameter().getD()).append("\t").append(this.itsHydrogenBondParameter.getXAParameter().getB())
                    .append("\t").append(this.itsHydrogenBondParameter.getXAParameter().getD()).append("\t").append(this.itsHydrogenBondParameter.getBHParameter().getB())
                    .append("\t").append(this.itsHydrogenBondParameter.getBHParameter().getD());
        }

        return theStringBuilder.toString();
    }
}
