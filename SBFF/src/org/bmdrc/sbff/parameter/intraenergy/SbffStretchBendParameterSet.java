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

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffStretchBendParameterSet implements Serializable {
    private static final long serialVersionUID = -3635343468931453142L;

    private List<StretchBendParameter> itsParameterSet;

    public SbffStretchBendParameterSet() {
        this.__generateParameterSet();
    }
    
    private void __generateParameterSet() {
        this.itsParameterSet = new ArrayList<>();
        
        this.itsParameterSet.add(new StretchBendParameter(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP_CARBON, 
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, 0.0));
        this.itsParameterSet.add(new StretchBendParameter(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON, 
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, 0.0));
        this.itsParameterSet.add(new StretchBendParameter(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON, 
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, 0.0));
        this.itsParameterSet.add(new StretchBendParameter(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.POSITIVE_CARBON, 
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, 0.0));
        this.itsParameterSet.add(new StretchBendParameter(null, MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, 0.08));
        this.itsParameterSet.add(new StretchBendParameter(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, 0.08));
        this.itsParameterSet.add(new StretchBendParameter(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, 0.08));
        this.itsParameterSet.add(new StretchBendParameter(null, MM3AtomTypeGenerator.POSITIVE_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, 0.08));
        this.itsParameterSet.add(new StretchBendParameter(null, MM3AtomTypeGenerator.SP_CARBON, null, 0.13));
        this.itsParameterSet.add(new StretchBendParameter(null, MM3AtomTypeGenerator.SP2_CARBON, null, 0.13));
        this.itsParameterSet.add(new StretchBendParameter(null, MM3AtomTypeGenerator.SP3_CARBON, null, 0.13));
        this.itsParameterSet.add(new StretchBendParameter(null, MM3AtomTypeGenerator.POSITIVE_CARBON, null, 0.13));
    }
    
    public StretchBendParameter getParameter(IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        for(StretchBendParameter theParameter : this.itsParameterSet) {
            if(theParameter.equalAtomTypeSet(theFirstAtom, theCenterAtom, theSecondAtom)) {
                return theParameter;
            }
        }
        
        return null;
    }
}
