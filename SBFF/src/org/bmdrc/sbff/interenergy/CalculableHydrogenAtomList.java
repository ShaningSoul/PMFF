/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.interenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.sbff.atomtype.HydrogenBondAtomTypeGenerator;
import org.openscience.cdk.Atom;
import org.openscience.cdk.interfaces.IAtom;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableHydrogenAtomList implements Serializable {

    private static final long serialVersionUID = 4946148538912992178L;

    private List<IAtom> itsAtomList;
    private List<Integer> itsHydrogenAtomTypeList;
    //constant Integer variable
    private final int X_INDEX = 0;
    private final int H_INDEX = 1;
    private final int A_INDEX = 2;
    private final int B_INDEX = 3;

    public CalculableHydrogenAtomList() {
        this.itsAtomList = new ArrayList<IAtom>();
        
        this.itsAtomList.add(new Atom());
        this.itsAtomList.add(new Atom());
        this.itsAtomList.add(new Atom());
        this.itsAtomList.add(new Atom());
    }

    public CalculableHydrogenAtomList(IAtom theXAtom, IAtom theHAtom, IAtom theAAtom, IAtom theBAtom) {
        this.itsAtomList = new ArrayList<IAtom>();
        
        this.itsAtomList.add(theXAtom);
        this.itsAtomList.add(theHAtom);
        this.itsAtomList.add(theAAtom);
        this.itsAtomList.add(theBAtom);
    }

    public List<IAtom> getAtomList() {
        return itsAtomList;
    }

    public void setAtomList(List<IAtom> theAtomList) {
        this.itsAtomList = theAtomList;
    }
    
    public List<IAtom> setAtomList() {
        return itsAtomList;
    }

    public List<Integer> getHydrogenAtomTypeList() {
        return itsHydrogenAtomTypeList;
    }

    public void setHydrogenAtomTypeList(List<Integer> theHydrogenAtomTypeList) {
        this.itsHydrogenAtomTypeList = theHydrogenAtomTypeList;
    }
    
    public void setHydrogenAtomTypeList(int theXType, int theHType, int theAType, int theBType) {
        this.itsHydrogenAtomTypeList = new ArrayList<Integer>();
        
        this.itsHydrogenAtomTypeList.add(theXType);
        this.itsHydrogenAtomTypeList.add(theHType);
        this.itsHydrogenAtomTypeList.add(theAType);
        this.itsHydrogenAtomTypeList.add(theBType);
    }
    
    public List<Integer> setHydrogenAtomTypeList() {
        return itsHydrogenAtomTypeList;
    }
    
    public IAtom getXAtom() {
        return this.getAtomList().get(this.X_INDEX);
    }
    
    public void setXAtom(IAtom theXAtom) {
        this.itsAtomList.set(this.X_INDEX, theXAtom);
    }
    
    public IAtom setXAtom() {
        return this.getAtomList().get(this.X_INDEX);
    }
    
    public IAtom getHAtom() {
        return this.itsAtomList.get(this.H_INDEX);
    }
    
    public void setHAtom(IAtom theHAtom) {
        this.itsAtomList.set(this.H_INDEX, theHAtom);
    }
    
    public IAtom setHAtom() {
        return this.itsAtomList.get(this.H_INDEX);
    }
    
    public IAtom getAAtom() {
        return this.itsAtomList.get(this.A_INDEX);
    }
    
    public void setAAtom(IAtom theAAtom) {
        this.itsAtomList.set(this.A_INDEX, theAAtom);
    }
    
    public IAtom setAAtom() {
        return this.itsAtomList.get(this.A_INDEX);
    }
    
    public IAtom getBAtom() {
        return this.itsAtomList.get(this.B_INDEX);
    }
    
    public void setBAtom(IAtom theBAtom) {
        this.itsAtomList.set(this.B_INDEX, theBAtom);
    }
    
    public IAtom setBAtom() {
        return this.itsAtomList.get(this.B_INDEX);
    }
    
    public double getHADistance() {
        return this.getHAtom().getPoint3d().distance(this.getAAtom().getPoint3d());
    }
    
    public double getBHDistance() {
        return this.getBAtom().getPoint3d().distance(this.getHAtom().getPoint3d());
    }
    
    public double getXADistance() {
        return this.getXAtom().getPoint3d().distance(this.getAAtom().getPoint3d());
    }
    
    public double getXBDistance() {
        return this.getXAtom().getPoint3d().distance(this.getBAtom().getPoint3d());
    }
    
    @Override
    public boolean equals(Object theObject) {
        if(!theObject.getClass().getName().equals(this.getClass().getName())) {
            return false;
        }
        
        CalculableHydrogenAtomList theCheckList = (CalculableHydrogenAtomList)theObject;
        
        for(int ai = 0, aEnd = this.getAtomList().size(); ai < aEnd; ai++) {
            if(!this.getAtomList().get(ai).equals(theCheckList.getAtomList().get(ai))) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append("Hydrogen Type : [X, H, A, B] = [")
                .append(this.getXAtom().getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY).toString()).append(", ")
                .append(this.getHAtom().getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY).toString()).append(", ")
                .append(this.getAAtom().getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY).toString()).append(", ")
                .append(this.getBAtom().getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY).toString()).append("]");
        
        return theStringBuilder.toString();
    }
}
