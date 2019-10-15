/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.parameter.intraenergy;

import java.io.Serializable;
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond.Order;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class StretchBendParameter implements Serializable {
    private static final long serialVersionUID = -1010383389693926807L;

    private Integer itsFirstAtomType;
    private Integer itsCenterAtomType;
    private Integer itsSecondAtomType;
    private Double itsConstant;
    private Order itsBondOrderBetweenFirstAndCenter;
    private Order itsBondOrderBetweenSecondAndCenter;
    private Boolean itsAnyBondBetweenFirstAndCenter;
    private Boolean itsAnyBondBetweenSecondAndCenter;
    //constant boolean variable
    public static final boolean ANY_BOND = true;
    public static final boolean NOT_ANY_BOND = false;
    
    public StretchBendParameter() {
    }

    public StretchBendParameter(Double theConstant) {
        this.itsConstant = theConstant;
    }
    
    public StretchBendParameter(Integer theFirstAtomType, Integer theCenterAtomType, Integer theSecondAtomType, Double theConstant) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsCenterAtomType = theCenterAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsConstant = theConstant;
        this.itsBondOrderBetweenFirstAndCenter = Order.SINGLE;
        this.itsBondOrderBetweenSecondAndCenter = Order.SINGLE;
        this.itsAnyBondBetweenFirstAndCenter = StretchBendParameter.NOT_ANY_BOND;
        this.itsAnyBondBetweenSecondAndCenter = StretchBendParameter.NOT_ANY_BOND;
    }

    public StretchBendParameter(Integer theFirstAtomType, Integer theCenterAtomType, Integer theSecondAtomType, Double theConstant, 
            Order theBondOrderBetweenFirstAndCenter, Order theBondOrderBetweenSecondAndCenter) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsCenterAtomType = theCenterAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsConstant = theConstant;
        this.itsBondOrderBetweenFirstAndCenter = theBondOrderBetweenFirstAndCenter;
        this.itsBondOrderBetweenSecondAndCenter = theBondOrderBetweenSecondAndCenter;
        this.itsAnyBondBetweenFirstAndCenter = StretchBendParameter.NOT_ANY_BOND;
        this.itsAnyBondBetweenSecondAndCenter = StretchBendParameter.NOT_ANY_BOND;
    }

    public StretchBendParameter(Integer theFirstAtomType, Integer theCenterAtomType, Integer theSecondAtomType, Double theConstant, 
            Boolean theAnyBondBetweenFirstAndCenter, Boolean theAnyBondBetweenSecondAndCenter) {
        this.itsFirstAtomType = theFirstAtomType;
        this.itsCenterAtomType = theCenterAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsConstant = theConstant;
        this.itsBondOrderBetweenFirstAndCenter = Order.SINGLE;
        this.itsBondOrderBetweenSecondAndCenter = Order.SINGLE;
        this.itsAnyBondBetweenFirstAndCenter = theAnyBondBetweenFirstAndCenter;
        this.itsAnyBondBetweenSecondAndCenter = theAnyBondBetweenSecondAndCenter;
    }

    public Double getConstant() {
        return itsConstant;
    }

    public void setConstant(Double theConstant) {
        this.itsConstant = theConstant;
    }
    
    
    public boolean equalAtomTypeSet(IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        int theFirstAtomType = (int)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theCenterAtomType = (int)theCenterAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theSecondAtomType = (int)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        
        if(this.__equalAtomType(this.itsFirstAtomType, theFirstAtomType) && this.__equalAtomType(this.itsCenterAtomType, theCenterAtomType) && 
                this.__equalAtomType(this.itsSecondAtomType, theSecondAtomType)) {
            return true;
        } else if(this.__equalAtomType(this.itsFirstAtomType, theSecondAtomType) && this.__equalAtomType(this.itsCenterAtomType, theCenterAtomType) && 
                this.__equalAtomType(this.itsSecondAtomType, theFirstAtomType)) {
            return true;
        }
        
        return false;
    }
    
    private boolean __equalAtomType(Integer theAtomTypeInParameter, int theAtomTypeInMolecule) {
        return theAtomTypeInParameter == null || theAtomTypeInParameter.equals(theAtomTypeInMolecule);
    }
}
