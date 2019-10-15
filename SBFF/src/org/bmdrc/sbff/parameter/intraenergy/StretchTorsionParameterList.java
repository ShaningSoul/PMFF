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
import org.openscience.cdk.interfaces.IBond.Order;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class StretchTorsionParameterList implements Serializable {
    private static final long serialVersionUID = -2285959345484901521L;

    private Integer itsFirstAtomType;
    private Integer itsSecondAtomType;
    private Integer itsThirdAtomType;
    private Integer itsFourthAtomType;
    private Order itsBondOrderBetweenFirstAndSecond;
    private Order itsBondOrderBetweenSecondAndThird;
    private Order itsBondOrderBetweenThirdAndFourth;
    private Boolean itsAnyBondBetweenFirstAndSecond;
    private Boolean itsAnyBondBetweenSecondAndThird;
    private Boolean itsAnyBondBetweenThirdAndFourth;
    private List<StretchTorsionParameter> itsParameterList;
    //constant boolean variable
    public static final boolean ANY_BOND = true;
    public static final boolean NOT_ANY_BOND = false;

    public StretchTorsionParameterList() {
    }

    public StretchTorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsBondOrderBetweenFirstAndSecond = Order.SINGLE;
        this.itsBondOrderBetweenSecondAndThird = Order.SINGLE;
        this.itsBondOrderBetweenThirdAndFourth = Order.SINGLE;
        this.itsAnyBondBetweenFirstAndSecond = StretchTorsionParameterList.NOT_ANY_BOND;
        this.itsAnyBondBetweenSecondAndThird = StretchTorsionParameterList.NOT_ANY_BOND;
        this.itsAnyBondBetweenThirdAndFourth = StretchTorsionParameterList.NOT_ANY_BOND;
        this.itsParameterList = new ArrayList<>();
    }

    public StretchTorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType, 
            Order theBondOrderBetweenFirstAndSecond, Order theBondOrderBetweenSecondAndThird, Order theBondOrderBetweenThirdAndFourth) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsBondOrderBetweenFirstAndSecond = theBondOrderBetweenFirstAndSecond;
        this.itsBondOrderBetweenSecondAndThird = theBondOrderBetweenSecondAndThird;
        this.itsBondOrderBetweenThirdAndFourth = theBondOrderBetweenThirdAndFourth;
        this.itsAnyBondBetweenFirstAndSecond = StretchTorsionParameterList.NOT_ANY_BOND;
        this.itsAnyBondBetweenSecondAndThird = StretchTorsionParameterList.NOT_ANY_BOND;
        this.itsAnyBondBetweenThirdAndFourth = StretchTorsionParameterList.NOT_ANY_BOND;
        this.itsParameterList = new ArrayList<>();
    }

    public StretchTorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType, 
            Boolean theAnyBondBetweenFirstAndSecond, Boolean theAnyBondBetweenSecondAndThird, Boolean theAnyBondBetweenThirdAndFourth) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsBondOrderBetweenFirstAndSecond = Order.SINGLE;
        this.itsBondOrderBetweenSecondAndThird = Order.SINGLE;
        this.itsBondOrderBetweenThirdAndFourth = Order.SINGLE;
        this.itsAnyBondBetweenFirstAndSecond = theAnyBondBetweenFirstAndSecond;
        this.itsAnyBondBetweenSecondAndThird = theAnyBondBetweenSecondAndThird;
        this.itsAnyBondBetweenThirdAndFourth = theAnyBondBetweenThirdAndFourth;
        this.itsParameterList = new ArrayList<>();
    }

    public StretchTorsionParameterList(Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, Integer theFourthAtomType, 
            Order theBondOrderBetweenFirstAndSecond, Order theBondOrderBetweenSecondAndThird, Order theBondOrderBetweenThirdAndFourth, 
            Boolean theAnyBondBetweenFirstAndSecond, Boolean theAnyBondBetweenSecondAndThird, Boolean theAnyBondBetweenThirdAndFourth) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsFourthAtomType = theFourthAtomType;
        this.itsBondOrderBetweenFirstAndSecond = theBondOrderBetweenFirstAndSecond;
        this.itsBondOrderBetweenSecondAndThird = theBondOrderBetweenSecondAndThird;
        this.itsBondOrderBetweenThirdAndFourth = theBondOrderBetweenThirdAndFourth;
        this.itsAnyBondBetweenFirstAndSecond = theAnyBondBetweenFirstAndSecond;
        this.itsAnyBondBetweenSecondAndThird = theAnyBondBetweenSecondAndThird;
        this.itsAnyBondBetweenThirdAndFourth = theAnyBondBetweenThirdAndFourth;
        this.itsParameterList = new ArrayList<>();
    }

    public List<StretchTorsionParameter> getParameterList() {
        return itsParameterList;
    }

    public void setParameterList(List<StretchTorsionParameter> theParameterList) {
        this.itsParameterList = theParameterList;
    }
    
    public List<StretchTorsionParameter> setParameterList() {
        return itsParameterList;
    }
    
    public StretchTorsionParameter getParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        for(StretchTorsionParameter theParameter : this.itsParameterList) {
            if(theParameter.equalConnectedAtomTypeSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
                return theParameter;
            }
        }
        
        return null;
    }
    
    public boolean equalAtomTypeSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        if(this.__equalAtomTypeSet(theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom) && 
                this.__equalOrderSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
            return true;
        } else if(this.__equalAtomTypeSet(theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom) &&
                this.__equalOrderSet(theMolecule, theFourthAtom, theThirdAtom, theSecondAtom, theFirstAtom)) {
            return true;
        }
        
        return false;
    }
    
    private boolean __equalOrderSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        Order theFirstBondOrder = theMolecule.getBond(theFirstAtom, theSecondAtom).getOrder();
        Order theSecondBondOrder = theMolecule.getBond(theSecondAtom, theThirdAtom).getOrder();
        Order theThirdBondOrder = theMolecule.getBond(theThirdAtom, theFourthAtom).getOrder();
        
        return this.__equalOrder(this.itsAnyBondBetweenFirstAndSecond, this.itsBondOrderBetweenFirstAndSecond, theFirstBondOrder) && 
                this.__equalOrder(this.itsAnyBondBetweenSecondAndThird, this.itsBondOrderBetweenSecondAndThird, theSecondBondOrder) &&
                this.__equalOrder(this.itsAnyBondBetweenThirdAndFourth, this.itsBondOrderBetweenThirdAndFourth, theThirdBondOrder);
    }
    
    private boolean __equalOrder(boolean theAnyBond, Order theBondOrderInParameter, Order theBondOrderInMolecule) {
        return this.itsAnyBondBetweenFirstAndSecond || theBondOrderInParameter.equals(theBondOrderInMolecule);
    }
    
    private boolean __equalAtomTypeSet(IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        int theFirstAtomType = (int)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theSecondAtomType = (int)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theThirdAtomType = (int)theThirdAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theFourthAtomType = (int)theFourthAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        
        return this.__equalAtomType(this.itsFirstAtomType, theFirstAtomType) && this.__equalAtomType(this.itsSecondAtomType, theSecondAtomType) &&
                this.__equalAtomType(this.itsThirdAtomType, theThirdAtomType) && this.__equalAtomType(this.itsFourthAtomType, theFourthAtomType);
    }
    
    private boolean __equalAtomType(Integer theAtomTypeInParameter, int theAtomTypeInMolecule) {
        return theAtomTypeInParameter == null || theAtomTypeInParameter.equals(theAtomTypeInMolecule);
    }
}
