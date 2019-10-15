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
public class TorsionParameterList implements Serializable {
    private static final long serialVersionUID = -2562712888329479716L;

    private Integer itsFirstAtomType;
    private Integer itsSecondAtomType;
    private Integer itsThirdAtomType;
    private Integer itsFourthAtomType;
    private Order itsBondOrderBetweenFirstAndSecond;
    private Order itsBondOrderBetweenSecondAndThird;
    private Order itsBondOrderBetweenThirdAndFourth;
    private Boolean itsAnyBondBetweenFirstAndSecond;
    private Boolean itsAnyBondBetweenSecondAndThird;
    private Boolean itsAnybondBetweenThirdAndFourth;
    private Integer itsSpecialStructureTypeInFirstAtom;
    private Integer itsSpecialStructureTypeInSecondAtom;
    private Integer itsSpecialStructureTypeInThirdAtom;
    private Integer itsSpecialStructureTypeInFourthAtom;
    private List<TorsionParameter> itsParameterList;
    //constant boolean variable
    public static final boolean ANY_BOND = true;
    public static final boolean NOT_ANY_BOND = false;
    public static final boolean CONTAIN_SPECIAL_STRUCTURE = true;
    public static final boolean NOT_CONTAIN_SPECIAL_STRUCTURE = false;

    public TorsionParameterList() {
    }

    public TorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsBondOrderBetweenFirstAndSecond = Order.SINGLE;
        this.itsBondOrderBetweenSecondAndThird = Order.SINGLE;
        this.itsBondOrderBetweenThirdAndFourth = Order.SINGLE;
        this.itsAnyBondBetweenFirstAndSecond = TorsionParameterList.NOT_ANY_BOND;
        this.itsAnyBondBetweenSecondAndThird = TorsionParameterList.NOT_ANY_BOND;
        this.itsAnybondBetweenThirdAndFourth = TorsionParameterList.NOT_ANY_BOND;
        this.itsSpecialStructureTypeInFirstAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInSecondAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInThirdAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInFourthAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsParameterList = new ArrayList<>();
    }

    public TorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType, Order theBondOrderBetweenFirstAndSecond,
            Order theBondOrderBetweenSecondAndThird, Order theBondOrderBetweenThirdAndFourth) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsBondOrderBetweenFirstAndSecond = theBondOrderBetweenFirstAndSecond;
        this.itsBondOrderBetweenSecondAndThird = theBondOrderBetweenSecondAndThird;
        this.itsBondOrderBetweenThirdAndFourth = theBondOrderBetweenThirdAndFourth;
        this.itsAnyBondBetweenFirstAndSecond = TorsionParameterList.NOT_ANY_BOND;
        this.itsAnyBondBetweenSecondAndThird = TorsionParameterList.NOT_ANY_BOND;
        this.itsAnybondBetweenThirdAndFourth = TorsionParameterList.NOT_ANY_BOND;
        this.itsSpecialStructureTypeInFirstAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInSecondAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInThirdAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInFourthAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsParameterList = new ArrayList<>();
    }

    public TorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType, boolean theAnyBondBetweenFirstAndSecond,
            boolean theAnyBondBetweenSecondAndThird, boolean theAnybondBetweenThirdAndFourth) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsBondOrderBetweenFirstAndSecond = Order.SINGLE;
        this.itsBondOrderBetweenSecondAndThird = Order.SINGLE;
        this.itsBondOrderBetweenThirdAndFourth = Order.SINGLE;
        this.itsAnyBondBetweenFirstAndSecond = theAnyBondBetweenFirstAndSecond;
        this.itsAnyBondBetweenSecondAndThird = theAnyBondBetweenSecondAndThird;
        this.itsAnybondBetweenThirdAndFourth = theAnybondBetweenThirdAndFourth;
        this.itsSpecialStructureTypeInFirstAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInSecondAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInThirdAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInFourthAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsParameterList = new ArrayList<>();
    }

    public TorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType, Integer theSpecialStructureTypeInFirstAtom, 
            Integer theSpecialStructureTypeInSecondAtom, Integer theSpecialStructureTypeInThirdAtom, Integer theSpecialStructureTypeInFourthAtom) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsSpecialStructureTypeInFirstAtom = theSpecialStructureTypeInFirstAtom;
        this.itsSpecialStructureTypeInSecondAtom = theSpecialStructureTypeInSecondAtom;
        this.itsSpecialStructureTypeInThirdAtom = theSpecialStructureTypeInThirdAtom;
        this.itsSpecialStructureTypeInFourthAtom = theSpecialStructureTypeInFourthAtom;
        this.itsParameterList = new ArrayList<>();
    }
    
    public TorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType, Order theBondOrderBetweenFirstAndSecond,
            Order theBondOrderBetweenSecondAndThird, Order theBondOrderBetweenThirdAndFourth, Boolean theAnyBondBetweenFirstAndSecond, Boolean theAnyBondBetweenSecondAndThird,
            Boolean theAnybondBetweenThirdAndFourth) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsBondOrderBetweenFirstAndSecond = theBondOrderBetweenFirstAndSecond;
        this.itsBondOrderBetweenSecondAndThird = theBondOrderBetweenSecondAndThird;
        this.itsBondOrderBetweenThirdAndFourth = theBondOrderBetweenThirdAndFourth;
        this.itsAnyBondBetweenFirstAndSecond = theAnyBondBetweenFirstAndSecond;
        this.itsAnyBondBetweenSecondAndThird = theAnyBondBetweenSecondAndThird;
        this.itsAnybondBetweenThirdAndFourth = theAnybondBetweenThirdAndFourth;
        this.itsSpecialStructureTypeInFirstAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInSecondAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInThirdAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInFourthAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsParameterList = new ArrayList<>();
    }

    public TorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType, Order theBondOrderBetweenFirstAndSecond, 
            Order theBondOrderBetweenSecondAndThird, Order theBondOrderBetweenThirdAndFourth, Boolean theAnyBondBetweenFirstAndSecond, Boolean theAnyBondBetweenSecondAndThird, 
            Boolean theAnybondBetweenThirdAndFourth, Integer theSpecialStructureTypeInFirstAtom, Integer theSpecialStructureTypeInSecondAtom, Integer theSpecialStructureTypeInThirdAtom,
            Integer theSpecialStructureTypeInFourthAtom) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsBondOrderBetweenFirstAndSecond = theBondOrderBetweenFirstAndSecond;
        this.itsBondOrderBetweenSecondAndThird = theBondOrderBetweenSecondAndThird;
        this.itsBondOrderBetweenThirdAndFourth = theBondOrderBetweenThirdAndFourth;
        this.itsAnyBondBetweenFirstAndSecond = theAnyBondBetweenFirstAndSecond;
        this.itsAnyBondBetweenSecondAndThird = theAnyBondBetweenSecondAndThird;
        this.itsAnybondBetweenThirdAndFourth = theAnybondBetweenThirdAndFourth;
        this.itsSpecialStructureTypeInFirstAtom = theSpecialStructureTypeInFirstAtom;
        this.itsSpecialStructureTypeInSecondAtom = theSpecialStructureTypeInSecondAtom;
        this.itsSpecialStructureTypeInThirdAtom = theSpecialStructureTypeInThirdAtom;
        this.itsSpecialStructureTypeInFourthAtom = theSpecialStructureTypeInFourthAtom;
        this.itsParameterList = new ArrayList<>();
    }

    public Integer getFirstAtomType() {
        return itsFirstAtomType;
    }

    public void setFirstAtomType(Integer theFirstAtomType) {
        this.itsFirstAtomType = theFirstAtomType;
    }

    public Integer getSecondAtomType() {
        return itsSecondAtomType;
    }

    public void setSecondAtomType(Integer theSecondAtomType) {
        this.itsSecondAtomType = theSecondAtomType;
    }

    public Integer getThirdAtomType() {
        return itsThirdAtomType;
    }

    public void setThirdAtomType(Integer theThirdAtomType) {
        this.itsThirdAtomType = theThirdAtomType;
    }

    public Integer getFourthAtomType() {
        return itsFourthAtomType;
    }

    public void setFourthAtomType(Integer theFourthAtomType) {
        this.itsFourthAtomType = theFourthAtomType;
    }

    public List<TorsionParameter> getParameterList() {
        return itsParameterList;
    }

    public void setParameterList(List<TorsionParameter> theParameterList) {
        this.itsParameterList = theParameterList;
    }
    
    public List<TorsionParameter> setParameterList() {
        return itsParameterList;
    }
    
    public TorsionParameter getParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        for(TorsionParameter theParameter : this.itsParameterList) {
            if(theParameter.equalAtomDescription(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
                return theParameter;
            }
        }
        
        return null;
    }
    
    public boolean equalAtomTypeListAndOrder(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        if(this.__equalAtomTypeList(theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom) && 
                this.__equalOrder(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
            return true;
        } else if(this.__equalAtomTypeList(theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom) &&
                this.__equalOrder(theMolecule, theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom)) {
            return true;
        }
        
        return false;
    }
    
    public boolean equalAtomTypeAndAnyBond(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        if(this.equalAtomTypeList(theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom) &&
                this.__equalAnyBond(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
            return true;
        } else if(this.equalAtomTypeList(theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom) &&
                this.__equalAnyBond(theMolecule, theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom)) {
            return true;
        }
        
        return false;
    }
    
    private boolean __equalAnyBond(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        IBond theFirstBond = theMolecule.getBond(theFirstAtom, theSecondAtom);
        IBond theSecondBond = theMolecule.getBond(theSecondAtom, theThirdAtom);
        IBond theThirdBond = theMolecule.getBond(theThirdAtom, theFourthAtom);
        
        if(!this.itsAnyBondBetweenFirstAndSecond && !theFirstBond.getOrder().equals(this.itsBondOrderBetweenFirstAndSecond)) {
            return false;
        } else if(!this.itsAnyBondBetweenSecondAndThird && !theSecondBond.getOrder().equals(this.itsBondOrderBetweenSecondAndThird)) {
            return false;
        } else if(!this.itsAnybondBetweenThirdAndFourth && !theThirdBond.getOrder().equals(this.itsBondOrderBetweenThirdAndFourth)) {
            return false;
        }
        
        return true;
    }
    
    private boolean __equalOrder(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        IBond theFirstBond = theMolecule.getBond(theFirstAtom, theSecondAtom);
        IBond theSecondBond = theMolecule.getBond(theSecondAtom, theThirdAtom);
        IBond theThirdBond = theMolecule.getBond(theThirdAtom, theFourthAtom);
        
        return theFirstBond.getOrder().equals(this.itsBondOrderBetweenFirstAndSecond) && theSecondBond.getOrder().equals(this.itsBondOrderBetweenSecondAndThird) &&
                theThirdBond.getOrder().equals(this.itsBondOrderBetweenThirdAndFourth);
    }
    
    public boolean equalAtomTypeAndSpecialStructureType(IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        if(this.__equalAtomTypeList(theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom) && this.__equalSpecialStructureType(theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
            return true;
        } else if(this.__equalAtomTypeList(theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom) && this.__equalSpecialStructureType(theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom)) {
            return true;
        }
        
        return false;
    }
    
    private boolean __equalSpecialStructureType(IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        int theSpecialStructureTypeInFirstAtom = (int)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY);
        int theSpecialStructureTypeInSecondAtom = (int)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY);
        int theSpecialStructureTypeInThirdAtom = (int)theThirdAtom.getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY);
        int theSpecialStructureTypeInFourthAtom = (int)theFourthAtom.getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY);
        
        if(this.itsSpecialStructureTypeInFirstAtom != MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE && this.itsSpecialStructureTypeInFirstAtom != theSpecialStructureTypeInFirstAtom) {
            return false;
        } else if(this.itsSpecialStructureTypeInSecondAtom != MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE && this.itsSpecialStructureTypeInSecondAtom != theSpecialStructureTypeInSecondAtom) {
            return false;
        } else if(this.itsSpecialStructureTypeInThirdAtom != MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE && this.itsSpecialStructureTypeInThirdAtom != theSpecialStructureTypeInThirdAtom) {
            return false;
        } else if(this.itsSpecialStructureTypeInFourthAtom != MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE && this.itsSpecialStructureTypeInFourthAtom != theSpecialStructureTypeInFourthAtom) {
            return false;
        }
        
        return true;
    }
    
    public boolean equalAtomTypeList(IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        return this.__equalAtomTypeList(theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom) || 
                this.__equalAtomTypeList(theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom);
    }
    
    private boolean __equalAtomTypeList(IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        Integer theFirstAtomType = (int)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        Integer theSecondAtomType = (int)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        Integer theThirdAtomType = (int)theThirdAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        Integer theFourthAtomType = (int)theFourthAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        
        return this.__equalAtomType(this.itsFirstAtomType, theFirstAtomType) && this.__equalAtomType(this.itsSecondAtomType, theSecondAtomType) &&
                this.__equalAtomType(this.itsThirdAtomType, theThirdAtomType) && this.__equalAtomType(this.itsFourthAtomType, theFourthAtomType);
    }
    
    private boolean __equalAtomType(Integer theAtomTypeInParameter, int theAtomTypeInMolecule) {
        return theAtomTypeInParameter == null || theAtomTypeInParameter.equals(theAtomTypeInMolecule);
    }
}
