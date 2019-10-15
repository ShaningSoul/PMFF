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
public class SbffOutOfPlaneBendingParameterSet implements Serializable {
    private static final long serialVersionUID = -8573450777925644296L;

    private List<OutOfPlaneBendParameter> itsParameterSet;
    //constant Integer variable
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    private final int THIRD_INDEX = 2;

    public SbffOutOfPlaneBendingParameterSet() {
        this.__generateParameterSet();
    }
    
    private void __generateParameterSet() {
        this.itsParameterSet = new ArrayList<>();
        
        this.itsParameterSet.add(new OutOfPlaneBendParameter(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, null, null, 0.0, 0.8));
        this.itsParameterSet.add(new OutOfPlaneBendParameter(MM3AtomTypeGenerator.SP2_CARBON, null, null, null, 0.0, 0.05));
        this.itsParameterSet.add(new OutOfPlaneBendParameter(MM3AtomTypeGenerator.POSITIVE_CARBON, null, null, null, 0.0, 0.8));
        this.itsParameterSet.add(new OutOfPlaneBendParameter(MM3AtomTypeGenerator.SP2_NITROGEN, null, null, null, 0.0, 0.05));
    }
    
    public OutOfPlaneBendParameter getParameter(IAtomContainer theMolecule, IAtom theCenterAtom) {
        List<IAtom> theConnectedAtomList = theMolecule.getConnectedAtomsList(theCenterAtom);
        
        return this.getParameter(theMolecule, theCenterAtom, theConnectedAtomList.get(this.FIRST_INDEX), theConnectedAtomList.get(this.SECOND_INDEX), theConnectedAtomList.get(this.THIRD_INDEX));
    }
    
    public OutOfPlaneBendParameter getParameter(IAtomContainer theMolecule, IAtom theCenterAtom, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom) {
        for(OutOfPlaneBendParameter theParameter : this.itsParameterSet) {
            if(theParameter.equalAtomTypeSet(theMolecule, theCenterAtom, theFirstAtom, theSecondAtom, theThirdAtom)) {
                return theParameter;
            }
        }
        
        return null;
    }
}
