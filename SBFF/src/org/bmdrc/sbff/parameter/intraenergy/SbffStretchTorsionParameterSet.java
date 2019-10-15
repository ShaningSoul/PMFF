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
public class SbffStretchTorsionParameterSet implements Serializable {
    private static final long serialVersionUID = -7250414176507844573L;

    private List<StretchTorsionParameterList> itsParameterSet;

    public SbffStretchTorsionParameterSet() {
        this.__generateParameterSet();
    }
    
    public StretchTorsionParameter getParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        for(StretchTorsionParameterList theParameterList : this.itsParameterSet) {
            if(theParameterList.equalAtomTypeSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom);
            }
        }
        
        return null;
    }
    
    private void __generateParameterSet() {
        this.itsParameterSet = new ArrayList<>();
        
        this.itsParameterSet.add(this.__get00C3C300ParameterList());
        this.itsParameterSet.add(this.__getC1C2C300ParameterList());
        this.itsParameterSet.add(this.__getC2C2C300ParameterList());
        this.itsParameterSet.add(this.__get00C2C300ParameterList());
        this.itsParameterSet.add(this.__get00O3C300ParameterList());
        this.itsParameterSet.add(this.__get00O3O300ParameterList());
    }
    
    private StretchTorsionParameterList __get00O3O300ParameterList() {
        StretchTorsionParameterList theParameterList = new StretchTorsionParameterList(null, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, 
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchTorsionParameter theParameter;
        
        theParameter = new StretchTorsionParameter(0.22);
        theParameterList.setParameterList().add(new StretchTorsionParameter(theParameter));
        
        return theParameterList;
    }
    
    private StretchTorsionParameterList __get00O3C300ParameterList() {
        StretchTorsionParameterList theParameterList = new StretchTorsionParameterList(null, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, 
                MM3AtomTypeGenerator.SP3_CARBON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchTorsionParameter theParameter;
        
        theParameter = new StretchTorsionParameter(0.1);
        theParameterList.setParameterList().add(new StretchTorsionParameter(theParameter));
        
        return theParameterList;
    }
    
    private StretchTorsionParameterList __get00C2C300ParameterList() {
        StretchTorsionParameterList theParameterList = new StretchTorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchTorsionParameter theParameter;
        
        theParameter = new StretchTorsionParameter(0.1);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new StretchTorsionParameter(theParameter));
        
        theParameter = new StretchTorsionParameter(0.27);
        theParameterList.setParameterList().add(new StretchTorsionParameter(theParameter));
        
        return theParameterList;
    }
    
    private StretchTorsionParameterList __getC2C2C300ParameterList() {
        StretchTorsionParameterList theParameterList = new StretchTorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, 
                MM3AtomTypeGenerator.SP3_CARBON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchTorsionParameter theParameter;
        
        theParameter = new StretchTorsionParameter(0.1);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new StretchTorsionParameter(theParameter));
        
        theParameter = new StretchTorsionParameter(0.0);
        theParameterList.setParameterList().add(new StretchTorsionParameter(theParameter));
        
        return theParameterList;
    }
    
    private StretchTorsionParameterList __getC1C2C300ParameterList() {
        StretchTorsionParameterList theParameterList = new StretchTorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP2_CARBON, 
                MM3AtomTypeGenerator.SP3_CARBON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchTorsionParameter theParameter;
        
        theParameter = new StretchTorsionParameter(0.1);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new StretchTorsionParameter(theParameter));
        
        theParameter = new StretchTorsionParameter(0.0);
        theParameterList.setParameterList().add(new StretchTorsionParameter(theParameter));
        
        return theParameterList;
    }
    
    private StretchTorsionParameterList __get00C3C300ParameterList() {
        StretchTorsionParameterList theParameterList = new StretchTorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchTorsionParameter theParameter;
        
        theParameter = new StretchTorsionParameter(0.059);
        theParameterList.setParameterList().add(new StretchTorsionParameter(theParameter));
        
        return theParameterList;
    }
}
