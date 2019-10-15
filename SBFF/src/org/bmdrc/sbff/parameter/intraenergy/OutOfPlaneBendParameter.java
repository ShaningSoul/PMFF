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

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class OutOfPlaneBendParameter implements Serializable {
    private static final long serialVersionUID = 2774961978649701794L;

    private Integer itsCenterAtomType;
    private Integer itsFirstAtomType;
    private Integer itsSecondAtomType;
    private Integer itsThirdAtomType;
    private List<Integer> itsConnectedAtomTypeInCenterAtom;
    private List<Integer> itsConnectedAtomTypeInFirstAtom;
    private List<Integer> itsConnectedAtomTypeInSecondAtom;
    private List<Integer> itsConnectedAtomTypeInThirdAtom;
    private Double itsAngle;
    private Double itsConstant;
    
    public OutOfPlaneBendParameter() {
    }

    public OutOfPlaneBendParameter(Double theAngle, Double theConstant) {
        this.itsAngle = theAngle;
        this.itsConstant = theConstant;
    }
    
    public OutOfPlaneBendParameter(Integer theCenterAtomType, Integer theFirstAtomType, Integer theSecondAtomType, Integer theThirdAtomType, 
            Double theAngle, Double theConstant) {
        this.itsCenterAtomType = theCenterAtomType;
        this.itsFirstAtomType = theFirstAtomType;
        this.itsSecondAtomType = theSecondAtomType;
        this.itsThirdAtomType = theThirdAtomType;
        this.itsAngle = theAngle;
        this.itsConstant = theConstant;
    }

    public Double getAngle() {
        return itsAngle;
    }

    public void setAngle(Double theAngle) {
        this.itsAngle = theAngle;
    }

    public Double getConstant() {
        return itsConstant;
    }

    public void setConstant(Double theConstant) {
        this.itsConstant = theConstant;
    }
    
    public boolean equalAtomTypeSet(IAtomContainer theMolecule, IAtom theCneterAtom, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom) {
        if(!this.itsCenterAtomType.equals((int)theCneterAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY))) {
            return false;
        } else if(!this.__equalAtomTypeSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom)) {
            return false;
        }
        
        return true;
    }
    
    private boolean __equalAtomTypeSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom) {
        Integer theFirstAtomType = (int)theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        Integer theSecondAtomType = (int)theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        Integer theThirdAtomType = (int)theThirdAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        List<Integer> theAtomTypeListInParameter = this.__initializeAtomTypeListInParameter();
        
        if(theAtomTypeListInParameter.contains(theFirstAtomType)) {
            theAtomTypeListInParameter.remove(theFirstAtomType);
        }
        
        if(theAtomTypeListInParameter.contains(theSecondAtomType)) {
            theAtomTypeListInParameter.remove(theSecondAtomType);
        }
        
        if(theAtomTypeListInParameter.contains(theThirdAtomType)) {
            theAtomTypeListInParameter.remove(theThirdAtomType);
        }
        
        return theAtomTypeListInParameter.isEmpty();
    }
    
    private List<Integer> __initializeAtomTypeListInParameter() {
        List<Integer> theAtomTypeListInParameter = new ArrayList<>();
        
        if(this.itsFirstAtomType != null) {
            theAtomTypeListInParameter.add(this.itsFirstAtomType);
        }
        
        if(this.itsSecondAtomType != null) {
            theAtomTypeListInParameter.add(this.itsSecondAtomType);
        }
        
        if(this.itsThirdAtomType != null) {
            theAtomTypeListInParameter.add(this.itsThirdAtomType);
        }
        
        return theAtomTypeListInParameter;
    }
}
