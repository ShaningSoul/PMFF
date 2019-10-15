/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.parameter.intraenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond.Order;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class StretchParameterList implements Serializable  {
    private static final long serialVersionUID = 1381744544832866651L;

    private Integer itsFirstAtomType;
    private Integer itsSecondAtomType;
    private Order itsBondOrder;
    private Integer itsSpecialStructureTypeInFirstAtom;
    private Integer itsSpecialStructureTypeInSecondAtom;
    private List<StretchParameter> itsParameterList;
    //constant Boolean variable
    public static final boolean CONTAIN_SPECIAL_STRUCTURE = true;
    public static final boolean NOT_CONTAIN_SPECIAL_STRUCTURE = false;

    public StretchParameterList() {
    }

    public StretchParameterList(Integer theFirstAtomType, Integer theSecondAtomType) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsBondOrder = Order.SINGLE;
        this.itsParameterList = new ArrayList<>();
        this.itsSpecialStructureTypeInFirstAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInSecondAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
    }

    public StretchParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Order theBondOrder) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsBondOrder = theBondOrder;
        this.itsParameterList = new ArrayList<>();
        this.itsSpecialStructureTypeInFirstAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
        this.itsSpecialStructureTypeInSecondAtom = MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE;
    }

    public StretchParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theFirstAtomContainSpecialStructure, Integer theSecondAtomContainSpecialStructure) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsBondOrder = Order.SINGLE;
        this.itsParameterList = new ArrayList<>();
        this.itsSpecialStructureTypeInFirstAtom = theFirstAtomContainSpecialStructure;
        this.itsSpecialStructureTypeInSecondAtom = theSecondAtomContainSpecialStructure;
    }

    public StretchParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Order theBondOrder, Integer theFirstAtomContainSpecialStructure, Integer theSecondAtomContainSpecialStructure) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsBondOrder = theBondOrder;
        this.itsParameterList = new ArrayList<>();
        this.itsSpecialStructureTypeInFirstAtom = theFirstAtomContainSpecialStructure;
        this.itsSpecialStructureTypeInSecondAtom = theSecondAtomContainSpecialStructure;
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

    public List<StretchParameter> getParameterList() {
        return itsParameterList;
    }

    public void setParameterList(List<StretchParameter> theParameterList) {
        this.itsParameterList = theParameterList;
    }

    public List<StretchParameter> setParameterList() {
        return itsParameterList;
    }

    public Order getBondOrder() {
        return itsBondOrder;
    }

    public void setBondOrder(Order theBondOrder) {
        this.itsBondOrder = theBondOrder;
    }

    public StretchParameter getParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom) {
        for (StretchParameter theParameter : this.getParameterList()) {
            if (this.__isSameAtomTypeSet(theMolecule, theFirstAtom, theSecondAtom) && theParameter.equalDescriptionAtomLocation(theMolecule, theFirstAtom, theSecondAtom)) {
                return theParameter;
            } else if (this.__isSameAtomTypeSet(theMolecule, theSecondAtom, theFirstAtom) && theParameter.equalDescriptionAtomLocation(theMolecule, theSecondAtom, theFirstAtom)) {
                StretchParameter theCopiedParameter = new StretchParameter(theParameter);
                
                theCopiedParameter.setBondMoment(-theParameter.getBondMoment());
                return theCopiedParameter;
            }
        }

        return null;
    }

    public boolean isSameAtomTypeSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom) {
        return this.__isSameAtomTypeSet(theMolecule, theFirstAtom, theSecondAtom) || this.__isSameAtomTypeSet(theMolecule, theSecondAtom, theFirstAtom);
    }
    
    private boolean __isSameAtomTypeSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom) {
        Integer theFirstAtomType = (int) theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        Integer theSecondAtomType = (int) theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        
        if(this.getFirstAtomType() == null && this.getSecondAtomType() == null) {
            return true;
        } else if (!this.__isSameAtomType(theFirstAtom, theSecondAtom)) {
            return false;
        } else if (!theMolecule.getBond(theFirstAtom, theSecondAtom).getOrder().equals(this.getBondOrder())) {
            return false;
        }

        return true;
    }

    private boolean __isSameAtomType(IAtom theFirstAtom, IAtom theSecondAtom) {
        int theFirstAtomType = (int) theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theSecondAtomType = (int) theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        
        if(this.itsFirstAtomType != null && this.itsFirstAtomType != theFirstAtomType) {
            return false;
        } else if(this.itsSecondAtomType != null && this.itsSecondAtomType != theSecondAtomType) {
            return false;
        }
        
        return true;
    }
    
    public boolean isSameSpecialStructureType(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom) {
        int theSpecialStructureTypeInFirstAtom = ((Integer)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY)).intValue();
        int theSpecialStructureTypeInSecondAtom = ((Integer)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY)).intValue();
        
        if(this.itsSpecialStructureTypeInFirstAtom != MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE && this.itsSpecialStructureTypeInFirstAtom != theSpecialStructureTypeInFirstAtom) {
            return false;
        } else if(this.itsSpecialStructureTypeInSecondAtom != MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE && this.itsSpecialStructureTypeInSecondAtom != theSpecialStructureTypeInSecondAtom) {
            return false;
        }
        
        return true;
    }
}
