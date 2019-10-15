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
public class SbffBendingParameterSet implements Serializable {

    private static final long serialVersionUID = -2822890632833611816L;

    private List<BendingParameterList> itsParameterSet;
    private List<BendingParameterList> itsAnyBondParameterSet;
    private List<BendingParameterList> itsExceptionParameterSet;
    private List<BendingParameterList> itsSpecialStructureParameterSet;

    public SbffBendingParameterSet() {
        this.__generateParameterSet();
        this.__generateAnyBondParameterSet();
        this.__generateExceptionBendingParameterList();
        this.__generateSpecialStructureParameterList();
    }

    public BendingParameter getBendingParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        BendingParameter theParameter;

        if((theParameter = this.__getBendingParameterInSpecialStructureParameterList(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom)) != null) {
            return theParameter;
        } 

        return this.getBendingParameterNotSpecialStructureType(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom);
    }

    public BendingParameter getBendingParameterNotSpecialStructureType(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        BendingParameter theParameter;

        if ((theParameter = this.__getBendingParameterInNormalParameterSet(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom)) != null) {
            return theParameter;
        } else if ((theParameter = this.__getBendingParameterInAnyBondParameterSet(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom)) != null) {
            return theParameter;
        }

        return this.__getBendingParameterInExceptionParameterSet(theMolecule, theCenterAtom);
    }
    
    private BendingParameter __getBendingParameterInSpecialStructureParameterList(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        for(BendingParameterList theParameterList :this.itsSpecialStructureParameterSet) {
            if(theParameterList.equalAtomTypeAndSpecialStructureType(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom);
            }
        }

        return null;
    }

    private BendingParameter __getBendingParameterInExceptionParameterSet(IAtomContainer theMolecule, IAtom theCenterAtom) {
        int theCenterAtomType = (int) theCenterAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);

        for (BendingParameterList theParameterList : this.itsExceptionParameterSet) {
            if (theParameterList.getCenterAtomType().equals(theCenterAtomType)) {
                return theParameterList.getParameter(theMolecule, null, theCenterAtom, null);
            }
        }

        return null;
    }

    private BendingParameter __getBendingParameterInAnyBondParameterSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        for (BendingParameterList theParameterList : this.itsAnyBondParameterSet) {
            if (theParameterList.equalAtomTypeAndAnyBond(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom);
            }
        }

        return null;
    }

    private BendingParameter __getBendingParameterInNormalParameterSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        int theFirstAtomType = (int) theFirstAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theCenterAtomType = (int) theCenterAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);
        int theSecondAtomType = (int) theSecondAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY);

        for (BendingParameterList theParameterList : this.itsParameterSet) {
            if (theParameterList.equalAtomTypeAndOrder(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theCenterAtom, theSecondAtom);
            }
        }

        return null;
    }

    private void __generateParameterSet() {
        this.itsParameterSet = new ArrayList<>();

        this.itsParameterSet.add(this.__getH1C3H1ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3ParameterList());
        this.itsParameterSet.add(this.__getC3C3H1ParameterList());
        this.itsParameterSet.add(this.__getC2C3C3ParameterList());
        this.itsParameterSet.add(this.__getC3C2C3ParameterList());
        this.itsParameterSet.add(this.__getC2C2C3ParameterList());
        this.itsParameterSet.add(this.__getC2C2C3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C3C2ParameterList());
        this.itsParameterSet.add(this.__getC2C2C2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2ParameterList());
        this.itsParameterSet.add(this.__getC1C3C3ParameterList());
        this.itsParameterSet.add(this.__getC1C2C3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C1C2BothDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC1C3C2ParameterList());
        this.itsParameterSet.add(this.__getC1C1C3AtFirstTripleBondParameterList());
        this.itsParameterSet.add(this.__getC1C1C2AtFirstTripleBondParameterList());
        this.itsParameterSet.add(this.__getC3C2H1ParameterList());
        this.itsParameterSet.add(this.__getC2C3H1ParameterList());
        this.itsParameterSet.add(this.__getC2C2H1ParameterList());
        this.itsParameterSet.add(this.__getC1C3H1ParameterList());
        this.itsParameterSet.add(this.__getC1C1H1ParameterList());
        this.itsParameterSet.add(this.__getH1C2H1ParameterList());
        this.itsParameterSet.add(this.__getC3C3CPParameterList());
        this.itsParameterSet.add(this.__getH1C3CPParameterList());
        this.itsParameterSet.add(this.__getC3C3O3ParameterList());
        this.itsParameterSet.add(this.__getH1C3O3ParameterList());
        this.itsParameterSet.add(this.__getC3O3C3ParameterList());
        this.itsParameterSet.add(this.__getC3O3H2ParameterList());
        this.itsParameterSet.add(this.__getH2O3H2ParameterList());
        this.itsParameterSet.add(this.__getC3C2O3ParameterList());
        this.itsParameterSet.add(this.__getH1C2O3ParameterList());
        this.itsParameterSet.add(this.__getC2C3O3ParameterList());
        this.itsParameterSet.add(this.__getC2C2O3ParameterList());
        this.itsParameterSet.add(this.__getC3O3C2ParameterList());
        this.itsParameterSet.add(this.__getC2O3H2ParameterList());
        this.itsParameterSet.add(this.__getC2O3C2ParameterList());
        this.itsParameterSet.add(this.__getO3C3O3ParameterList());
        this.itsParameterSet.add(this.__getC3O3O3ParameterList());
        this.itsParameterSet.add(this.__getO3O3H2ParameterList());
        this.itsParameterSet.add(this.__getC3C2O2AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2O2AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2O2AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO3C2O2AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3N3ParameterList());
        this.itsParameterSet.add(this.__getH1C3N3ParameterList());
        this.itsParameterSet.add(this.__getC2C3N3ParameterList());
        this.itsParameterSet.add(this.__getC3C3N2ParameterList());
        this.itsParameterSet.add(this.__getH1C3N2ParameterList());
        this.itsParameterSet.add(this.__getC2C3N2ParameterList());
        this.itsParameterSet.add(this.__getC2C2N2ParameterList());
        this.itsParameterSet.add(this.__getC3C2N2ParameterList());
        this.itsParameterSet.add(this.__getO2C2N2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C1N1AtSecondTripleBondParameterList());
        this.itsParameterSet.add(this.__getC3N3C3ParameterList());
        this.itsParameterSet.add(this.__getC3N3H3ParameterList());
        this.itsParameterSet.add(this.__getC3N2C3ParameterList());
        this.itsParameterSet.add(this.__getC3N2H3ParameterList());
        this.itsParameterSet.add(this.__getC2N2H3ParameterList());
        this.itsParameterSet.add(this.__getH3N2H3ParameterList());
        this.itsParameterSet.add(this.__getC2N3H3ParameterList());
        this.itsParameterSet.add(this.__getC2N3C3ParameterList());
        this.itsParameterSet.add(this.__getH3N3H3ParameterList());
        this.itsParameterSet.add(this.__getC2N2C3ParameterList());
        this.itsParameterSet.add(this.__getC3N3N3ParameterList());
        this.itsParameterSet.add(this.__getN3N3H3ParameterList());
        this.itsParameterSet.add(this.__getN3C3N3ParameterList());
        this.itsParameterSet.add(this.__getN2C2N2ParameterList());
        this.itsParameterSet.add(this.__getC3C3SXParameterList());
        this.itsParameterSet.add(this.__getH1C3SXParameterList());
        this.itsParameterSet.add(this.__getC3SXC3ParameterList());
        this.itsParameterSet.add(this.__getC3SXO2AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO2SXO2AtBothDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C3SXParameterList());
        this.itsParameterSet.add(this.__getC3SXSXParameterList());
        this.itsParameterSet.add(this.__getSXC3SXParameterList());
        this.itsParameterSet.add(this.__getC3C3PXParameterList());
        this.itsParameterSet.add(this.__getH1C3PXParameterList());
        this.itsParameterSet.add(this.__getC3O3PXParameterList());
        this.itsParameterSet.add(this.__getC2O3PXParameterList());
        this.itsParameterSet.add(this.__getC3PXC3ParameterList());
        this.itsParameterSet.add(this.__getC2PXC3ParameterList());
        this.itsParameterSet.add(this.__getC2PXC2ParameterList());
        this.itsParameterSet.add(this.__getC3PXH1ParameterList());
        this.itsParameterSet.add(this.__getH1PXH1ParameterList());
        this.itsParameterSet.add(this.__getO3PXO3ParameterList());
        this.itsParameterSet.add(this.__getC3C3F0ParameterList());
        this.itsParameterSet.add(this.__getH1C3F0ParameterList());
        this.itsParameterSet.add(this.__getC3C2F0ParameterList());
        this.itsParameterSet.add(this.__getH1C2F0ParameterList());
        this.itsParameterSet.add(this.__getC2C3F0ParameterList());
        this.itsParameterSet.add(this.__getF0C3F0ParameterList());
        this.itsParameterSet.add(this.__getC3C3ClParameterList());
        this.itsParameterSet.add(this.__getH1C3ClParameterList());
        this.itsParameterSet.add(this.__getC3C2ClParameterList());
        this.itsParameterSet.add(this.__getH1C2ClParameterList());
        this.itsParameterSet.add(this.__getC2C3ClParameterList());
        this.itsParameterSet.add(this.__getClC3O3ParameterList());
        this.itsParameterSet.add(this.__getClC3F0ParameterList());
        this.itsParameterSet.add(this.__getClC3ClParameterList());
        this.itsParameterSet.add(this.__getC3C3BrParameterList());
        this.itsParameterSet.add(this.__getH1C3BrParameterList());
        this.itsParameterSet.add(this.__getC2C3BrParameterList());
        this.itsParameterSet.add(this.__getC3C2BrParameterList());
        this.itsParameterSet.add(this.__getH1C2BrParameterList());
        this.itsParameterSet.add(this.__getBrC3F0ParameterList());
        this.itsParameterSet.add(this.__getBrC3ClParameterList());
        this.itsParameterSet.add(this.__getBrC3BrParameterList());
        this.itsParameterSet.add(this.__getC3C3I0ParameterList());
        this.itsParameterSet.add(this.__getC2C3I0ParameterList());
        this.itsParameterSet.add(this.__getH1C3I0ParameterList());
        this.itsParameterSet.add(this.__getC3C2I0ParameterList());
        this.itsParameterSet.add(this.__getH1C2I0ParameterList());
        this.itsParameterSet.add(this.__getC3C3SiParameterList());
        this.itsParameterSet.add(this.__getH1C3SiParameterList());
        this.itsParameterSet.add(this.__getC3C2SiParameterList());
        this.itsParameterSet.add(this.__getH1C2SiParameterList());
        this.itsParameterSet.add(this.__getC2C3SiParameterList());
        this.itsParameterSet.add(this.__getC3O3SiParameterList());
        this.itsParameterSet.add(this.__getSiO3SiParameterList());
        this.itsParameterSet.add(this.__getSiC3SiParameterList());
        this.itsParameterSet.add(this.__getSiC2SiParameterList());
        this.itsParameterSet.add(this.__getC3SiC3ParameterList());
        this.itsParameterSet.add(this.__getH1SiC3ParameterList());
        this.itsParameterSet.add(this.__getH1SiH1ParameterList());
        this.itsParameterSet.add(this.__getC2SiC3ParameterList());
        this.itsParameterSet.add(this.__getC2SiH1ParameterList());
        this.itsParameterSet.add(this.__getC2SiC2ParameterList());
        this.itsParameterSet.add(this.__getC3SiO3ParameterList());
        this.itsParameterSet.add(this.__getH1SiO3ParameterList());
        this.itsParameterSet.add(this.__getO3SiO3ParameterList());
        this.itsParameterSet.add(this.__getC3SiSiParameterList());
        this.itsParameterSet.add(this.__getH1SiSiParameterList());
        this.itsParameterSet.add(this.__getC2SiSiParameterList());
        this.itsParameterSet.add(this.__getSiSiSiParameterList());
    }

    private void __generateAnyBondParameterSet() {
        this.itsAnyBondParameterSet = new ArrayList<>();

        this.itsAnyBondParameterSet.add(this.__getC1C2H1InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2H1InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2O3InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2N2InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC3C2N2InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getH1C2N2InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2N2C3InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2N2C2InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getN2C2N2InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2PXInAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2F0InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2ClInAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2BrInAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2SiInAnyBondParameterList());
    }

    private void __generateExceptionBendingParameterList() {
        this.itsExceptionParameterSet = new ArrayList<>();

        this.itsExceptionParameterSet.add(this.__getC3ParameterList());
        this.itsExceptionParameterSet.add(this.__getC2ParameterList());
        this.itsExceptionParameterSet.add(this.__getC1ParameterList());
        this.itsExceptionParameterSet.add(this.__getCPParameterList());
        this.itsExceptionParameterSet.add(this.__getO2ParameterList());
        this.itsExceptionParameterSet.add(this.__getO3ParameterList());
        this.itsExceptionParameterSet.add(this.__getN2ParameterList());
        this.itsExceptionParameterSet.add(this.__getN3ParameterList());
        this.itsExceptionParameterSet.add(this.__getN4ParameterList());
        this.itsExceptionParameterSet.add(this.__getN5ParameterList());
        this.itsExceptionParameterSet.add(this.__getSXParameterList());
        this.itsExceptionParameterSet.add(this.__getPXParameterList());
        this.itsExceptionParameterSet.add(this.__getSiParameterList());
        this.itsExceptionParameterSet.add(this.__getClParameterList());
        this.itsExceptionParameterSet.add(this.__getBrParameterList());
        this.itsExceptionParameterSet.add(this.__getI0ParameterList());
    }

    private void __generateSpecialStructureParameterList() {
        this.itsSpecialStructureParameterSet = new ArrayList<>();

        this.itsSpecialStructureParameterSet.add(this.__getAllAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getC2TwoAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getC3TwoAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getH1TwoAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getN2TwoAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getC3C3OneAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getH1C3OneAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getH1C2OneAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getO3C3OneAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getC3N2OneAtomInBenzenoidParameterList());
        this.itsSpecialStructureParameterSet.add(this.__getH3N2OneAtomInBenzenoidParameterList());
    }

    private BendingParameterList __getH3N2OneAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN, MM3AtomTypeGenerator.SP2_NITROGEN,
                null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.5, 0.587, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(109.0, 0.587, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3N2OneAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN,
                null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(103.0, 0.598, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(102.5, 0.598, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO3C3OneAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(107.0, 0.7, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2OneAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.5, 0.49, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(120.0, 0.49, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3OneAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.4, 0.55, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(109.31, 0.55, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(109.5, 0.55, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3OneAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;
        
        theParameter = new BendingParameter(110.6, 0.54, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getN2TwoAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, null, null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(123.0, 0.6, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1TwoAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, null, null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.49, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3TwoAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, null, null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(122.3, 0.47, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2TwoAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, null, null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(121.7, 0.76, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getAllAtomInBenzenoidParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(null, null, null, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(121.7, 0.76, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getI0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.IODINE,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.3, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getBrParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.BROMINE,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.4, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getClParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.CHLORINE,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.5, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getSiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.55, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getPXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.PHOSPHORUS,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.0, 0.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(100.0, 0.6, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getSXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.SULFUR,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(96.0, 0.9, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getN5ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.0, 0.7, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getN4ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.7, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getN3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.0, 0.7, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getN2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.7, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(106.0, 0.7, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.3, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getCPParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.POSITIVE_CARBON,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 2.0, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(180.0, 0.3, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.5, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NOT_DEFINED_ATOM, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.NOT_DEFINED_ATOM, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.2, 0.6, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getSiSiSiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(111.2, 0.25, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.8, 0.25, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(118.0, 0.25, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2SiSiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.2, 0.4, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1SiSiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.4, 0.42, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3SiSiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.0, 0.45, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO3SiO3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(113.5, 0.45, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1SiO3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.35, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3SiO3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.5, 0.35, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2SiC2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(104.5, 0.6, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2SiH1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.55, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2SiC3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.2, 0.4, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1SiH1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.46, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(108.7, 0.46, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(106.5, 0.46, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1SiC3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(111.0, 0.4, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(107.0, 0.4, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.3, 0.4, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3SiC3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.2, 0.48, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.4, 0.48, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.5, 0.48, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getSiC2SiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.4, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getSiC3SiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(117.0, 0.35, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(119.5, 0.35, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.5, 0.35, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getSiO3SiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(145.7, 0.15, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3O3SiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(114.6, 0.4, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2SiInAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, BendingParameterList.ANY_BOND, BendingParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(122.0, 0.32, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3SiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.5, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(105.0, 0.5, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.5, 0.5, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2SiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(119.5, 0.525, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2SiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.4, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3SiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.9, 0.54, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.0, 0.54, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.5, 0.54, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3SiParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(111.5, 0.4, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(112.7, 0.4, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.0, 0.4, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2I0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.IODINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(112.0, 0.4, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2I0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.IODINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.4, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3I0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.IODINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(106.4, 0.63, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3I0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.IODINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.9, 0.49, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3I0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.IODINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.9, 0.57, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getBrC3BrParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.BROMINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.7, 0.69, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getBrC3ClParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.BROMINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.7, 0.72, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getBrC3F0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.BROMINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.4, 0.72, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2BrInAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.8, 0.56, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2BrParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(112.1, 0.46, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2BrParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.46, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3BrParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.1, 0.63, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3BrParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(106.5, 0.51, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3BrParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.2, 0.74, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getClC3ClParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(111.7, 0.76, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getClC3F0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.4, 0.75, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getClC3O3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.5, 0.56, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2ClInAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.CHLORINE, BendingParameterList.ANY_BOND, BendingParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(118.8, 0.55, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3ClParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.8, 0.56, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.5, 0.56, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2ClParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(112.6, 0.45, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2ClParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(112.6, 0.45, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3ClParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(107.5, 0.6, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3ClParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.2, 0.65, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getF0C3F0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.1, 0.75, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2F0InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.FLUORINE, BendingParameterList.ANY_BOND, BendingParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(121.0, 0.65, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3F0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.2, 0.65, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.0, 0.65, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2F0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.45, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2F0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.45, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3F0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.57, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3F0ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.76, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO3PXO3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.PHOSPHORUS,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(99.5, 0.45, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1PXH1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.PHOSPHORUS,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(92.0, 0.43, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3PXH1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.PHOSPHORUS,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(95.0, 0.48, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2PXC2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.PHOSPHORUS,
                MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(95.0, 0.48, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2PXC3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.PHOSPHORUS,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(92.5, 0.48, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3PXC3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.PHOSPHORUS,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(95.6, 0.576, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2O3PXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(118.0, 0.8, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3O3PXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(116.0, 0.77, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3PXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.6, 0.36, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2PXInAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS, BendingParameterList.ANY_BOND, BendingParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.38, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3PXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(111.5, 0.48, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getSXC3SXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.0, 0.42, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3SXSXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR,
                MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(102.0, 1.0, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3SXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(107.8, 0.42, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.5, 0.42, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(107.8, 0.42, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO2SXO2AtBothDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SULFUR,
                MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.DOUBLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(116.6, 0.9, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3SXO2AtSecondDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR,
                MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(107.7, 0.597, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(106.0, 0.597, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3SXC3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(102.0, 0.72, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(93.0, 0.72, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(95.0, 0.84, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3SXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.6, 0.384, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(108.0, 0.74, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.8, 0.74, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3SXParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(102.74, 0.42, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(106.0, 0.42, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.1, 0.74, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.5, 0.74, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(108.0, 0.74, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getN2C2N2InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.SP2_NITROGEN, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.4, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getN2C2N2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.5, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getN3C3N3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.74, 1.045, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getN3N3H3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(103.2, 0.43, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3N3N3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(105.5, 0.74, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2N2C2InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.SP2_CARBON, BendingParameterList.ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(125.0, 0.5, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2N2C3InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.SP3_CARBON, BendingParameterList.ANY_BOND, BendingParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(119.9, 0.63, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2N2C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(121.1, 1.62, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH3N3H3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(107.1, 0.605, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(106.4, 0.605, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2N3C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(103.0, 0.598, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(102.5, 0.598, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2N3H3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.587, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.0, 0.587, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH3N2H3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(123.5, 0.41, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2N2H3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(118.5, 0.58, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.0, 0.5, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3N2H3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(122.4, 0.19, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3N2C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(122.5, 0.76, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3N3H3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.9, 0.6, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(108.1, 0.6, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3N3C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.2, 0.72, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(107.2, 0.72, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C1N1AtSecondTripleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP_NITROGEN, Order.SINGLE, Order.TRIPLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(180.0, 0.335, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO2C2N2AtFirstDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(124.8, 1.07, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2N2InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, BendingParameterList.NOT_ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(117.0, 0.54, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2N2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.3, 0.44, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2N2InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, BendingParameterList.NOT_ANY_BOND, BendingParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(117.4, 0.5, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2N2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(114.4, 0.57, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2N2InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, BendingParameterList.ANY_BOND, BendingParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(125.0, 0.6, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(123.0, 0.6, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2N2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 9.0, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3N2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(112.3, 0.85, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.6, 0.85, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.5, 0.85, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3N2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(111.0, 0.76, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3N2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(111.8, 0.75, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(111.3, 0.75, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.48, 0.75, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3N3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.74, 1.045, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3N3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.3, 0.82, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3N3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(111.0, 0.78, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(108.0, 0.78, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.47, 0.78, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO3C2O2AtSecondDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(122.0, 0.8, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2O2AtSecondDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(123.0, 1.2, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(122.0, 1.2, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2O2AtSecondDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(119.2, 0.85, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2O2AtSecondDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(123.5, 0.85, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO3O3H2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(99.5, 0.852, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3O3O3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(103.3, 1.058, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getO3C3O3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.6, 0.54, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2O3C2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(106.8, 0.77, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setAtomTypeListConnectedSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2O3H2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(106.1, 0.3, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.0, 0.36, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3O3C2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.9, 0.6, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.8, 0.77, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2O3InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, BendingParameterList.ANY_BOND, BendingParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.0, 0.6, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2O3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(124.3, 0.7, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3O3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.7, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(107.0, 0.7, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2O3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(105.9, 0.65, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(116.4, 0.54, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2O3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(117.1, 0.65, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(120.0, 0.5, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH2O3H2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(105.0, 0.63, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3O3H2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(106.8, 0.75, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3O3C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(107.2, 0.82, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3O3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.7, 0.82, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(108.9, 0.82, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.0, 0.82, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3O3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(107.9, 0.83, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(107.0, 0.83, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(107.5, 0.83, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3CPParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.52, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3CPParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.5, 0.52, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C2H1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(114.5, 0.65, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(119.0, 0.45, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2H1InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, BendingParameterList.ANY_BOND, BendingParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(120.5, 0.49, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(120.0, 0.49, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC1C1H1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(180.0, 0.25, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC1C2H1InAnyBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, BendingParameterList.ANY_BOND, BendingParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(119.5, 0.3, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC1C3H1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(108.0, 0.68, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.41, 0.68, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.39, 0.68, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2H1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(112.0, 0.3, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(117.0, 0.5, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3H1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.49, 0.54, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.4, 0.55, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.31, 0.55, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.5, 0.55, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C2H1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(117.3, 0.464, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(117.5, 0.49, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC1C3C2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.2, 0.47, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.51, 0.47, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.47, 0.47, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC1C1C2AtFirstTripleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, Order.TRIPLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(180.0, 0.47, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC1C1C3AtFirstTripleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, Order.TRIPLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(180.0, 0.315, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C1C2BothDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(180.0, 0.4, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC1C2C3AtFirstDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(116.6, 0.47, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC1C3C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.0, 0.96, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.7, 0.96, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(108.8, 0.96, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2C2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(115.0, 0.6, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2C2AtFirstDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(116.0, 0.5, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(122.3, 0.76, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setAtomTypeListConnectedSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(121.7, 0.76, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(122.0, 0.76, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3C2ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.2, 0.47, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(110.51, 0.47, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(109.47, 0.47, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(113.0, 0.45, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(115.5, 0.45, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        theParameter = new BendingParameter(113.2, 0.45, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C2C3AtFirstDoubleBondParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(122.3, 0.47, 0.24);
        theAtomTypeList.clear();

        return theParameterList;
    }

    private BendingParameterList __getC2C2C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(117.0, 0.5, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(116.0, 0.5, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        return theParameterList;
    }

    private BendingParameterList __getC3C2C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(116.8, 1.25, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(117.0, 0.54, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC2C3C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.6, 0.8, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setAtomTypeListConnectedFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(110.6, 0.54, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(109.8, 0.54, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(110.6, 0.54, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3H1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(110.7, 0.59, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(109.31, 0.59, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(109.8, 0.59, 0.3);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getC3C3C3ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(111.0, 0.67, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(110.2, 0.67, 0.24);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(109.5, 0.7, 0.24);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }

    private BendingParameterList __getH1C3H1ParameterList() {
        BendingParameterList theParameterList = new BendingParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        BendingParameter theParameter;

        theParameter = new BendingParameter(109.47, 0.55, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(107.8, 0.55, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setAtomTypeListConnectedCenterAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new BendingParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new BendingParameter(107.6, 0.55, 0.0);
        theParameterList.setParameterList().add(new BendingParameter(theParameter));

        return theParameterList;
    }
}
