/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.sbff.parameter.intraenergy;

import java.util.ArrayList;
import java.util.List;
import org.bmdrc.sbff.parameter.interenergy.NonBondParameterSet;
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2015. 09. 03
 */
public class SbffIntraNonbondingParameterSet {

    private List<MM3NonbondingParameterList> itsParameterSetInMM3;
    private NonBondParameterSet itsParameterSetInSbff;
    /**
     * Constructor
     */
    public SbffIntraNonbondingParameterSet() {
        this.__initializeParameterSetInMM3();
        this.itsParameterSetInSbff = new NonBondParameterSet();
    }
    
    public MM3NonbondingParameter getParameterInMM3(IAtomContainer theMolecule, IAtom theAtom) {
        for(MM3NonbondingParameterList theParameterList : this.itsParameterSetInMM3) {
            if(theParameterList.getAtomType().equals(theAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY))) {
                return theParameterList.getParameter(theMolecule, theAtom);
            }
        }
        
        return null;
    }

    public NonBondParameterSet getParameterSetInSbff() {
        return itsParameterSetInSbff;
    }
    
    /**
     * initialize parameter set
     * this parameter is used in Van der Waals interaction energy at MM3
     */
    private void __initializeParameterSetInMM3() {
        this.itsParameterSetInMM3 = new ArrayList<>();
        MM3NonbondingParameterList theParameterList = new MM3NonbondingParameterList();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.SP_CARBON);
        theParameterList.add(new MM3NonbondingParameter(1.94, 0.056));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.SP2_CARBON);
        theParameterList.add(new MM3NonbondingParameter(1.94, 0.056, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN));
        theParameterList.add(new MM3NonbondingParameter(1.96, 0.056));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.SP3_CARBON);
        theParameterList.add(new MM3NonbondingParameter(2.04, 0.027));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.POSITIVE_CARBON);
        theParameterList.add(new MM3NonbondingParameter(1.94, 0.056));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameterList.add(new MM3NonbondingParameter(1.82, 0.059));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theParameterList.add(new MM3NonbondingParameter(1.82, 0.059));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameterList.add(new MM3NonbondingParameter(1.82, 0.059));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameterList.add(new MM3NonbondingParameter(1.62, 0.02));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        theParameterList.add(new MM3NonbondingParameter(1.60, 0.016));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        theParameterList.add(new MM3NonbondingParameter(1.60, 0.015, MM3AtomTypeGenerator.SP2_NITROGEN));
        theParameterList.add(new MM3NonbondingParameter(1.60, 0.018));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.POSITIVE_HYDROGEN);
        theParameterList.add(new MM3NonbondingParameter(1.60, 0.034));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.SP_NITROGEN);
        theParameterList.add(new MM3NonbondingParameter(1.93, 0.043));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.SP2_NITROGEN);
        theParameterList.add(new MM3NonbondingParameter(1.93, 0.043));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.SP3_NITROGEN);
        theParameterList.add(new MM3NonbondingParameter(1.93, 0.043));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN);
        theParameterList.add(new MM3NonbondingParameter(1.93, 0.043));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN);
        theParameterList.add(new MM3NonbondingParameter(1.93, 0.043));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.SULFUR);
        theParameterList.add(new MM3NonbondingParameter(2.15, 0.202));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.PHOSPHORUS);
        theParameterList.add(new MM3NonbondingParameter(2.27, 0.168));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.FLUORINE);
        theParameterList.add(new MM3NonbondingParameter(1.71, 0.075));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.CHLORINE);
        theParameterList.add(new MM3NonbondingParameter(2.07, 0.24));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.BROMINE);
        theParameterList.add(new MM3NonbondingParameter(2.22, 0.32));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.IODINE);
        theParameterList.add(new MM3NonbondingParameter(2.36, 0.42));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.SILICON);
        theParameterList.add(new MM3NonbondingParameter(2.29, 0.14));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
        
        theParameterList.setAtomType(MM3AtomTypeGenerator.NOT_DEFINED_ATOM);
        theParameterList.add(new MM3NonbondingParameter(2.0, 0.63));
        this.itsParameterSetInMM3.add(new MM3NonbondingParameterList(theParameterList));
        theParameterList.clear();
    }
}
