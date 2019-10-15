/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.parameter.intraenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Order;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class BendingParameterList implements Serializable {
    private static final long serialVersionUID = -2846325794952547921L;

    private Integer itsFirstAtomType;
    private Integer itsCenterAtomType;
    private Integer itsSecondAtomType;
    private Order itsBondOrderBetweenFirstAtomAndCenterAtom;
    private Order itsBondOrderBetweenSecondAtomAndCenterAtom;
    private Boolean itsAnyBondBetweenFirstAtomAndCenterAtom;
    private Boolean itsAnyBondBetweenSecondAtomAndCenterAtom;
    private Integer itsSpecialStructureTypeInFirstAtom;
    private Integer itsSpecialStructureTypeInSecondAtom;
    private Integer itsSpecialStructureTypeInThirdAtom;
    private List<BendingParameter> itsParameterList;
    //constant boolean variable
    public static final boolean ANY_BOND = true;
    public static final boolean NOT_ANY_BOND = false;
    public static final boolean CONTAIN_SPECIAL_STRUCTURE = true;
    public static final boolean NOT_CONTAIN_SPECIAL_STRUCTURE = false;

    public BendingParameterList() {
    }

    public BendingParameterList(Integer theFirstAtomType, Integer theCenterAtomType, Integer theSecondAtomType) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsCenterAtomType = theCenterAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsParameterList = new ArrayList<>();
        this.itsBondOrderBetweenFirstAtomAndCenterAtom = Order.SINGLE;
        this.itsBondOrderBetweenSecondAtomAndCenterAtom = Order.SINGLE;
        this.itsAnyBondBetweenFirstAtomAndCenterAtom = BendingParameterList.NOT_ANY_BOND;
        this.itsAnyBondBetweenSecondAtomAndCenterAtom = BendingParameterList.NOT_ANY_BOND;
        this.itsSpecialStructureTypeInFirstAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInSecondAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInThirdAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
    }

    public BendingParameterList(Integer theFirstAtomType, Integer theCenterAtomType, Integer theSecondAtomType, Order theBondOrderBetweenFirstAtomAndCenterAtom, 
            Order theBondOrderBetweenSecondAtomAndCenterAtom) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsCenterAtomType = theCenterAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsParameterList = new ArrayList<>();
        this.itsBondOrderBetweenFirstAtomAndCenterAtom = theBondOrderBetweenFirstAtomAndCenterAtom;
        this.itsBondOrderBetweenSecondAtomAndCenterAtom = theBondOrderBetweenSecondAtomAndCenterAtom;
        this.itsAnyBondBetweenFirstAtomAndCenterAtom = BendingParameterList.NOT_ANY_BOND;
        this.itsAnyBondBetweenSecondAtomAndCenterAtom = BendingParameterList.NOT_ANY_BOND;
        this.itsSpecialStructureTypeInFirstAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInSecondAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInThirdAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
    }

    public BendingParameterList(Integer theFirstAtomType, Integer theCenterAtomType, Integer theSecondAtomType, Integer theSpecialStructureTypeInFirstAtom, 
            Integer theSpecialStructureTypeInSecondAtom, Integer theSpecialStructureTypeInThirdAtom) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsCenterAtomType = theCenterAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsParameterList = new ArrayList<>();
        this.itsSpecialStructureTypeInFirstAtom = theSpecialStructureTypeInFirstAtom;
        this.itsSpecialStructureTypeInSecondAtom = theSpecialStructureTypeInSecondAtom;
        this.itsSpecialStructureTypeInThirdAtom = theSpecialStructureTypeInThirdAtom;
    }
    
    public BendingParameterList(Integer theFirstAtomType, Integer theCenterAtomType, Integer theSecondAtomType, boolean theAromaticBondBetweenFirstAtomAndCenterAtom, 
            boolean theAromaticBondBetweenSecondAtomAndCenterAtom) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsCenterAtomType = theCenterAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsParameterList = new ArrayList<>();
        this.itsBondOrderBetweenFirstAtomAndCenterAtom = Order.SINGLE;
        this.itsBondOrderBetweenSecondAtomAndCenterAtom = Order.SINGLE;
        this.itsAnyBondBetweenFirstAtomAndCenterAtom = theAromaticBondBetweenFirstAtomAndCenterAtom;
        this.itsAnyBondBetweenSecondAtomAndCenterAtom = theAromaticBondBetweenSecondAtomAndCenterAtom;
        this.itsSpecialStructureTypeInFirstAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInSecondAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInThirdAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
    }

    public BendingParameterList(Integer theFirstAtomType, Integer theCenterAtomType, Integer theSecondAtomType, Order theBondOrderBetweenFirstAtomAndCenterAtom, 
            Order theBondOrderBetweenSecondAtomAndCenterAtom, Boolean theAnyBondBetweenFirstAtomAndCenterAtom, Boolean theAnyBondBetweenSecondAtomAndCenterAtom, 
            Integer theSpecialStructureTypeInFirstAtom, Integer theSpecialStructureTypeInSecondAtom, Integer theSpecialStructureTypeInThirdAtom) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsCenterAtomType = theCenterAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsParameterList = new ArrayList<>();
        this.itsBondOrderBetweenFirstAtomAndCenterAtom = theBondOrderBetweenFirstAtomAndCenterAtom;
        this.itsBondOrderBetweenSecondAtomAndCenterAtom = theBondOrderBetweenSecondAtomAndCenterAtom;
        this.itsAnyBondBetweenFirstAtomAndCenterAtom = theAnyBondBetweenFirstAtomAndCenterAtom;
        this.itsAnyBondBetweenSecondAtomAndCenterAtom = theAnyBondBetweenSecondAtomAndCenterAtom;
        this.itsSpecialStructureTypeInFirstAtom = theSpecialStructureTypeInFirstAtom;
        this.itsSpecialStructureTypeInSecondAtom = theSpecialStructureTypeInSecondAtom;
        this.itsSpecialStructureTypeInThirdAtom = theSpecialStructureTypeInThirdAtom;
    }
    
    
    public Integer getFirstAtomType() {
        return itsFirstAtomType;
    }

    public void setFirstAtomType(Integer theFirstAtomType) {
        this.itsFirstAtomType = theFirstAtomType;
    }

    public Integer getCenterAtomType() {
        return itsCenterAtomType;
    }

    public void setCenterAtomType(Integer theCenterAtomType) {
        this.itsCenterAtomType = theCenterAtomType;
    }

    public Integer getSecondAtomType() {
        return itsSecondAtomType;
    }

    public void setSecondAtomType(Integer theSecondAtomType) {
        this.itsSecondAtomType = theSecondAtomType;
    }

    public List<BendingParameter> getParameterList() {
        return itsParameterList;
    }

    public void setParameterList(List<BendingParameter> theParameterList) {
        this.itsParameterList = theParameterList;
    }
    
    public List<BendingParameter> setParameterList() {
        return itsParameterList;
    }
    
    public BendingParameter getParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        for(BendingParameter theParameter : this.itsParameterList) {
            if(theParameter.equalDescriptionAtom(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom)) {
                return theParameter;
            } else if(theParameter.equalDescriptionAtom(theMolecule, theSecondAtom, theCenterAtom, theFirstAtom)) {
                return theParameter;
            }
        }
        
        return null;
    }
    
    public boolean equalAtomTypeAndOrder(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        if(this.__equalAtomType(theFirstAtom, theCenterAtom, theSecondAtom) && this.__equalOrder(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom)) {
            return true;
        } else if(this.__equalAtomType(theSecondAtom, theCenterAtom, theFirstAtom) && this.__equalOrder(theMolecule, theSecondAtom, theCenterAtom, theFirstAtom)) {
            return true;
        } 
        
        return false;
    }
    
    private boolean __equalOrder(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        IBond theFirstBond = theMolecule.getBond(theFirstAtom, theCenterAtom);
        IBond theSecondBond = theMolecule.getBond(theSecondAtom, theCenterAtom);
        
        return theFirstBond.getOrder().equals(this.itsBondOrderBetweenFirstAtomAndCenterAtom) && theSecondBond.getOrder().equals(this.itsBondOrderBetweenSecondAtomAndCenterAtom);
    }
    
    public boolean equalAtomTypeAndSpecialStructureType(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        int theFirstAtomType = (int)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theCenterAtomType = (int)theCenterAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theSecondAtomType = (int)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        
        if(this.equalAtomType(theFirstAtomType, theCenterAtomType, theSecondAtomType) && this.__equalSpecialStructureType(theFirstAtom, theCenterAtom, theSecondAtom)) {
            return true;
        } else if(this.equalAtomType(theSecondAtomType, theCenterAtomType, theFirstAtomType) && this.__equalSpecialStructureType(theSecondAtom, theCenterAtom, theFirstAtom)) {
            return true;
        }
        
        return false;
    }
    
    private boolean __equalSpecialStructureType(IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        int theSpecialStructureTypeInFirstAtom = (int)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY);
        int theSpecialStructureTypeInSecondAtom = (int)theCenterAtom.getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY);
        int theSpecialStructureTypeInThirdAtom = (int)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY);
        
        if(this.itsSpecialStructureTypeInFirstAtom != MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE && this.itsSpecialStructureTypeInFirstAtom != theSpecialStructureTypeInFirstAtom) {
            return false;
        } else if(this.itsSpecialStructureTypeInSecondAtom != MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE && this.itsSpecialStructureTypeInSecondAtom != theSpecialStructureTypeInSecondAtom) {
            return false;
        } else if(this.itsSpecialStructureTypeInThirdAtom != MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE && this.itsSpecialStructureTypeInThirdAtom != theSpecialStructureTypeInThirdAtom) {
            return false;
        }
        
        return true;
    }
    
    public boolean equalAtomTypeAndAnyBond(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        int theFirstAtomType = (int)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theCenterAtomType = (int)theCenterAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theSecondAtomType = (int)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        
        if(this.equalAtomType(theFirstAtomType, theCenterAtomType, theSecondAtomType) && this.__equalAnyBondSet(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom)) {
            return true;
        } else if(this.equalAtomType(theSecondAtomType, theCenterAtomType, theFirstAtomType) && this.__equalAnyBondSet(theMolecule, theSecondAtom, theCenterAtom, theFirstAtom)) {
            return true;
        }
        
        return false;
    }
    
    private boolean __equalAnyBondSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        IBond theFirstBond = theMolecule.getBond(theFirstAtom, theCenterAtom);
        IBond theSecondBond = theMolecule.getBond(theSecondAtom, theCenterAtom);
        
        if(!this.itsAnyBondBetweenFirstAtomAndCenterAtom && !theFirstBond.getOrder().equals(this.itsBondOrderBetweenFirstAtomAndCenterAtom)) {
            return false;
        } else if(!this.itsAnyBondBetweenSecondAtomAndCenterAtom && !theSecondBond.getOrder().equals(this.itsBondOrderBetweenSecondAtomAndCenterAtom)) {
            return false;
        }
        
        return true;
    }
    
    private boolean __equalAtomType(IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        int theFirstAtomType = (int)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theCenterAtomType = (int)theCenterAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theSecondAtomType = (int)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        
        return this.equalAtomType(theFirstAtomType, theCenterAtomType, theSecondAtomType);
    }
    
    public boolean equalAtomType(int theFirstAtomType, int theCenterAtomType, int theSecondAtomType) {
        if(this.itsFirstAtomType != null && this.itsFirstAtomType != theFirstAtomType) {
            return false;
        } else if(this.itsCenterAtomType != null && this.itsCenterAtomType != theCenterAtomType) {
            return false;
        } else if(this.itsSecondAtomType != null && this.itsSecondAtomType != theSecondAtomType) {
            return false;
        }
        
        return true;
    }
}
