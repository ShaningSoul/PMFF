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
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffStretchParameterSet implements Serializable {
    private static final long serialVersionUID = -351005977622861139L;

    private List<StretchParameterList> itsParameterList;
    private List<StretchParameterList> itsSpecialStructureParameterList;
    
    public SbffStretchParameterSet() {
        this.__generateParameterSet();
        this.__generateSpecialStructureParameterList();
    }

    public StretchParameter getStretchParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom) {
        StretchParameter theParameter;
        
        if((theParameter = this.__getSpecialStructureParameter(theMolecule, theFirstAtom, theSecondAtom)) != null) {
            return theParameter;
        }
        
        return this.__getStretchParameter(theMolecule, theFirstAtom, theSecondAtom);
    }
    
    private StretchParameter __getSpecialStructureParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom) {
        for(StretchParameterList theParameterList : this.itsSpecialStructureParameterList) {
            if(theParameterList.isSameAtomTypeSet(theMolecule, theFirstAtom, theSecondAtom) && theParameterList.isSameSpecialStructureType(theMolecule, theFirstAtom, theSecondAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theSecondAtom);
            }
        }
        
        return null;
    }
    
    private StretchParameter __getStretchParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom) {
        for(StretchParameterList theParameterList : this.itsParameterList) {
            if(theParameterList.isSameAtomTypeSet(theMolecule, theFirstAtom, theSecondAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theSecondAtom);
            }
        }
        
        return null;
    }
    
    private void __generateParameterSet() {
        this.itsParameterList = new ArrayList<>();

        this.itsParameterList.add(this.__getC3C3ParameterList());
        this.itsParameterList.add(this.__getC2C3ParameterList());
        this.itsParameterList.add(this.__getC3H1ParameterList());
        this.itsParameterList.add(this.__getC2H1ParameterList());
        this.itsParameterList.add(this.__getC1H1ParameterList());
        this.itsParameterList.add(this.__getO3C3ParameterList());
        this.itsParameterList.add(this.__getO3C2ParameterList());
        this.itsParameterList.add(this.__getO3O3ParameterList());
        this.itsParameterList.add(this.__getO3H2ParameterList());
        this.itsParameterList.add(this.__getOMC2ParameterList());
        this.itsParameterList.add(this.__getDoubleBondO2C2ParameterList());
        this.itsParameterList.add(this.__getDoubleBondC2C2ParameterList());
        this.itsParameterList.add(this.__getC2C2ParameterList());
        this.itsParameterList.add(this.__getC2SXParameterList());
        this.itsParameterList.add(this.__getC1C3ParameterList());
        this.itsParameterList.add(this.__getDoubleBondC1C2ParameterList());
        this.itsParameterList.add(this.__getC1C2ParameterList());
        this.itsParameterList.add(this.__getTripleBondC1C1ParameterList());
        this.itsParameterList.add(this.__getN3C3ParmaeterList());
        this.itsParameterList.add(this.__getN3H3ParameterList());
        this.itsParameterList.add(this.__getN3C2ParameterList());
        this.itsParameterList.add(this.__getN3N3ParameterList());
        this.itsParameterList.add(this.__getN2C3ParameterList());
        this.itsParameterList.add(this.__getN2C2ParameterList());
        this.itsParameterList.add(this.__getN2H3ParameterList());
        this.itsParameterList.add(this.__getDoubleBondN2C2ParameterList());
        this.itsParameterList.add(this.__getTripleBondC1C1ParameterList());
        this.itsParameterList.add(this.__getF0C3ParameterList());
        this.itsParameterList.add(this.__getF0C2ParameterList());
        this.itsParameterList.add(this.__getClC3ParameterList());
        this.itsParameterList.add(this.__getClC2ParameterList());
        this.itsParameterList.add(this.__getBrC3ParameterList());
        this.itsParameterList.add(this.__getBrC2ParameterList());
        this.itsParameterList.add(this.__getI0C3ParameterList());
        this.itsParameterList.add(this.__getI0C2ParameterList());
        this.itsParameterList.add(this.__getSXC3ParameterList());
        this.itsParameterList.add(this.__getSXH1ParameterList());
        this.itsParameterList.add(this.__getSXSXParameterList());
        this.itsParameterList.add(this.__getPXC3ParameterList());
        this.itsParameterList.add(this.__getPXC2ParameterList());
        this.itsParameterList.add(this.__getPXH1ParameterList());
        this.itsParameterList.add(this.__getPXO3ParameterList());
        this.itsParameterList.add(this.__getSiC3ParameterList());
        this.itsParameterList.add(this.__getSiH1ParameterList());
        this.itsParameterList.add(this.__getO3SiParameterList());
        this.itsParameterList.add(this.__getSiC2ParameterList());
        this.itsParameterList.add(this.__getSiSiParameterList());
        this.itsParameterList.add(this.__getCPC3ParameterList());
        this.itsParameterList.add(this.__getCPH1ParameterList());
        this.itsParameterList.add(this.__getDoubleBondN4C2ParameterList());
        this.itsParameterList.add(this.__getN4C3ParameterList());
        this.itsParameterList.add(this.__getN4C2ParameterList());
        this.itsParameterList.add(this.__getDoubleBondN4O2ParameterList());
        this.itsParameterList.add(this.__getN4OMParameterList());
        this.itsParameterList.add(this.__getN5C3ParameterList());
        this.itsParameterList.add(this.__getN5C2ParameterList());
        this.itsParameterList.add(this.__getN5H3ParameterList());
        this.itsParameterList.add(this.__getN4H4ParameterList());
    }
    
    private void __generateSpecialStructureParameterList() {
        this.itsSpecialStructureParameterList = new ArrayList<>();
        
        this.itsSpecialStructureParameterList.add(this.__getAllAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterList.add(this.__getC2H1OneAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterList.add(this.__getC2C3OneAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterList.add(this.__getC2C2OneAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterList.add(this.__getC2N2OneAtomInBenzenoidParameterList());
    }
    
    private StretchParameterList __getC2N2OneAtomInBenzenoidParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(null, MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE);
        StretchParameter theParameter = new StretchParameter(1.3780, 6.32, -0.63);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getC2C2OneAtomInBenzenoidParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(1.4575, 5.4177, 0.0);
        
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedSecondAtom(new TwoDimensionList<>(theAtomType2dList));
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getC2C3OneAtomInBenzenoidParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE);
        StretchParameter theParameter = new StretchParameter(1.4990, 6.3, -0.9);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getC2H1OneAtomInBenzenoidParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(null, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE);
        StretchParameter theParameter = new StretchParameter(1.1010, 5.16, -0.6);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getAllAtomInBenzenoidParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(null, null, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        StretchParameter theParameter = new StretchParameter(1.3887, 6.56, 0.0);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    /**
     * Add parameter using N3H
     * @return 
     */
    private StretchParameterList __getN4H4ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        StretchParameter theParameter = new StretchParameter(1.015, 6.42, -1.34);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getN5H3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        StretchParameter theParameter = new StretchParameter(1.022, 5.24, -0.6);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getN5C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        StretchParameter theParameter = new StretchParameter(1.26, 11.09, -0.583);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getN5C3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        StretchParameter theParameter = new StretchParameter(1.473, 3.95, -1.8);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getN4OMParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(1.2225, 7.5, -0.411);
        
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getDoubleBondN4O2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.DOUBLE);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(1.2225, 7.5, -0.411);
        
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getN4C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(1.495, 5.5, -1.33);
        
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        
        theParameter = new StretchParameter(1.473, 5.05, -0.9);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getN4C3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(1.495, 5.5, -1.33);
        
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getDoubleBondN4C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(1.325, 6.83, -0.72);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getCPH1ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(1.0857, 7.4, 0.0);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));
    
        return theParameterList;
    }
    
    private StretchParameterList __getCPC3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.POSITIVE_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(1.148, 7.48, 0.0);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getSiSiParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(2.326, 1.65, 0.0);
        
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(2.32, 1.65, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SILICON);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(2.322, 1.65, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getSiC2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter = new StretchParameter(1.856, 3.0, 1.2);
        
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getO3SiParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SILICON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.626, 5.5, -0.4);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getSiH1ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.483, 2.65, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getSiC3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.872, 3.05, 0.7);
        theAtomTypeList.add(MM3AtomTypeGenerator.SILICON);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.876, 3.05, 0.7);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getPXO3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.615, 2.9, 0.97);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getPXH1ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.43, 3.33, -0.64);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getPXC2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SP2_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.828, 2.91, -1.04);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getPXC3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SP3_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.849, 2.91, -0.85);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getSXSXParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SULFUR);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(2.037, 2.62, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(2.015, 2.62, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getDoubleBondSXO2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.346, 3.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getSXH1ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.346, 3.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.339, 3.87, -0.9);
        theAtomTypeList.add(MM3AtomTypeGenerator.SULFUR);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.342, 3.87, -0.9);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getSXC3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.784, 3.213, -1.2);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.805, 3.213, -1.2);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.805, 3.0, -1.2);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getI0C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.IODINE, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(2.08, 2.2, -1.4);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getI0C3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.IODINE, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(2.139, 2.2, -1.3);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getBrC2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.BROMINE, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.88, 2.5, -1.56);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getBrC3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.BROMINE, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.944, 2.3, -1.79);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getClC2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.719, 3.4, -1.58);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getClC3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.753, 3.23, -1.94);
        theAtomTypeList.add(MM3AtomTypeGenerator.FLUORINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.763, 3.23, -1.94);
        theAtomTypeList.add(MM3AtomTypeGenerator.CHLORINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.783, 3.23, -1.94);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getF0C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.32, 5.4, -1.48);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getF0C3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP3_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.346, 5.1, -1.82);
        theAtomTypeList.add(MM3AtomTypeGenerator.FLUORINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.36, 5.1, -1.82);
        theAtomTypeList.add(MM3AtomTypeGenerator.CHLORINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.376, 5.1, -1.82);
        theAtomTypeList.add(MM3AtomTypeGenerator.BROMINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.38, 5.1, -1.82);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getTripleBondN1C1ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.158, 17.33, -2.5);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getDoubleBondN2C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.345, 10.0, -1.3);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getN2H3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.028, 6.77, -1.58);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.028, 6.77, -1.58);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getN2C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.377, 6.7, 0.72);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedSecondAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.33, 6.3, -1.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getN2C3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.446, 5.21, -1.65);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getN3N3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.381, 5.6, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getN3C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.378, 6.32, 0.63);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getN3H3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.015, 6.42, -1.34);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getN3C3ParmaeterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.448, 5.3, -0.68);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getTripleBondC1C1ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON, Order.TRIPLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.21, 15.25, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getC1C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.313, 9.9, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getDoubleBondC1C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.31, 11.2, -0.3);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getC1C3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.47, 5.5, -1.64);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getC2SXParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.765, 4.0, 1.15);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getC2C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

//        theParameter = new StretchParameter(1.51, 5.0, 0.0);
//        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
//        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
//        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
//        theParameter.setAtomType2dListConnectedSecondAtom(new TwoDimensionList<>(theAtomType2dList));
//        theAtomTypeList.clear();
//        theAtomType2dList.clear();
//        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.46, 5.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theParameter.setAtomType2dListConnectedSecondAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.47, 6.0, -0.5);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.48, 5.0, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getDoubleBondC2C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.343, 7.5, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SILICON);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.332, 7.5, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getDoubleBondO2C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.25, 7.57, -1.9);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedSecondAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.208, 10.1, -1.86);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getOMC2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.NEGATIVE_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.25, 7.57, -1.9);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedSecondAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getO3H2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(0.972, 7.2, -0.7);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(0.964, 7.63, -1.67);
        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(0.947, 7.63, -1.67);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getO3O3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.454, 3.95, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.454, 3.95, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getO3C2ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.338, 5.05, 0.2);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedSecondAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.355, 6.0, -0.001);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getO3C3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.413, 5.7, -1.17);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getC1H1ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.08, 5.97, -0.92);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getC2H1ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.118, 4.37, -0.6);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.101, 5.15, -0.6);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getC3H1ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.11, 4.74, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.109, 4.74, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_NITROGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.102, 4.74, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.FLUORINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.107, 4.74, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.CHLORINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.112, 4.74, 0.0);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getC2C3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.509, 4.8, -1.01);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.5220, 6.3, -0.9);
        theAtomTypeList.add(MM3AtomTypeGenerator.SILICON);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter = new StretchParameter(1.499, 6.3, -0.9);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }

    private StretchParameterList __getC3C3ParameterList() {
        StretchParameterList theParameterList = new StretchParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        TwoDimensionList<Integer> theAtomType2dList = new TwoDimensionList<>();
        List<Integer> theAtomTypeList = new ArrayList<>();
        StretchParameter theParameter;

        theParameter = new StretchParameter(1.5177, 4.49, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter.setBondLength(1.5197);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_NITROGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter.setBondLength(1.5097);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_NITROGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter.setBondLength(1.5027);
        theAtomTypeList.add(MM3AtomTypeGenerator.FLUORINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter.setBondLength(1.5167);
        theAtomTypeList.add(MM3AtomTypeGenerator.CHLORINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter.setBondLength(1.5127);
        theAtomTypeList.add(MM3AtomTypeGenerator.BROMINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter.setBondLength(1.5197);
        theAtomTypeList.add(MM3AtomTypeGenerator.IODINE);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter.setBondLength(1.5237);
        theAtomTypeList.add(MM3AtomTypeGenerator.SULFUR);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter.setBondLength(1.5187);
        theAtomTypeList.add(MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN);
        theAtomType2dList.add(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomType2dListConnectedFirstAtom(new TwoDimensionList<>(theAtomType2dList));
        theAtomTypeList.clear();
        theAtomType2dList.clear();
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        theParameter.setBondLength(1.5247);
        theParameter.setAtomType2dListConnectedFirstAtom(null);
        theParameterList.setParameterList().add(new StretchParameter(theParameter));

        return theParameterList;
    }
}
