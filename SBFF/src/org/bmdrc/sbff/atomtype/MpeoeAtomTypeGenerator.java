/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.atomtype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.bmdrc.basicchemistry.atdl.Atdl;
import org.bmdrc.basicchemistry.atdl.AtdlGenerator;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.tool.RingPerception;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.sbff.abstracts.SbffConstant;
import org.bmdrc.sbff.interenergy.SbffNonbondingEnergyFunction;
import org.bmdrc.sbff.tool.MpeoeChargeCalculator;
import org.bmdrc.ui.abstracts.Constant;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IRingSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class MpeoeAtomTypeGenerator implements Serializable {

    private static final long serialVersionUID = -6485078803878120831L;

    private IAtomContainer itsMolecule;
    private Integer itsAtomIndex;
    private List<Atdl> itsAtdlList;
    private List<String> itsPriorityListByAtomSymbol;
    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;
    private IRingSet itsRingSet;
    //constat Integer variable
    private final Integer NEGATIVE_CHARGE = -1;
    private final int SIX_MEMEBERED_RING = 6;
    //constant String variable
    public static final String MPEOE_ATOM_TYPE_KEY = "MPEOE_Atom_Type";
    public static final String CDEAP_ATOM_TYPE_KEY = "CDEAP_Atom_Type";
    public static final String VDW_ATOM_TYPE_KEY = "VDW_Atom_Type";
    public static final String DAMP_ATOM_TYPE_KEY = "Damp_Atom_Type";
    public static final String HYDROGEN_BOND_ATOM_TYPE_KEY = "HBond_Atom_Type";
    public static final String EN_PULS_KEY = "enPlus";

    public MpeoeAtomTypeGenerator() {
        this.__generatePriorityList();
    }

    private void __generatePriorityList() {
        this.itsPriorityListByAtomSymbol = new ArrayList<String>();

        this.itsPriorityListByAtomSymbol.add(AtomInformation.Fluorine.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Phosphorus.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Sulfur.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Nitrogen.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Carbon.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Oxygen.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Bromine.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Chlorine.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Iodine.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Silicon.SYMBOL);
        this.itsPriorityListByAtomSymbol.add(AtomInformation.Hydrogen.SYMBOL);
    }

    private List<IAtom> __sortedAtomListByPriority() {
        List<IAtom> theAtomList = this.__getAtomList();

        Collections.sort(theAtomList, this.__getComparatorByPriority());

        return theAtomList;
    }

    private List<IAtom> __getAtomList() {
        List<IAtom> theAtomList = new ArrayList<>();

        for (IAtom theAtom : this.itsMolecule.atoms()) {
            theAtomList.add(theAtom);
        }

        return theAtomList;
    }

    private Comparator<IAtom> __getComparatorByPriority() {
        Comparator<IAtom> theComparator = new Comparator<IAtom>() {

            @Override
            public int compare(IAtom theFirstAtom, IAtom theSecondAtom) {
                Integer theFirstAtomPriority = itsPriorityListByAtomSymbol.indexOf(theFirstAtom.getSymbol());
                Integer theSecondAtomPriority = itsPriorityListByAtomSymbol.indexOf(theSecondAtom.getSymbol());

                return theFirstAtomPriority.compareTo(theSecondAtomPriority);
            }
        };

        return theComparator;
    }

    public void inputMpeoeAtomType(Protein theProtein) {
        RingPerception theRingPerception = new RingPerception();
        TopologicalDistanceMatrix theTopologicalDistanceMatrix = new TopologicalDistanceMatrix(theProtein);
        IRingSet theRingSet = theRingPerception.recognizeRing(theProtein);
        
        this.inputMpeoeAtomType(theProtein.getMolecule(), theTopologicalDistanceMatrix, theRingSet);
    }
    
    public void inputMpeoeAtomType(IAtomContainer theMolecule) {
        this.inputMpeoeAtomType(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }

    public void inputMpeoeAtomType(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        RingPerception theRingPerception = new RingPerception();

        this.inputMpeoeAtomType(theMolecule, theTopologicalDistanceMatrix, theRingPerception.recognizeRing(theMolecule, theTopologicalDistanceMatrix));
    }

    public void inputMpeoeAtomType(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix, IRingSet theRingSet) {
        this.itsMolecule = theMolecule;
        this.itsTopologicalDistanceMatrix = theTopologicalDistanceMatrix;
        this.itsRingSet = theRingSet;
        
        List<IAtom> theAtomList = this.__sortedAtomListByPriority();
        int theIndex = 0;

        this.__initializeAtdlList();

        for (IAtom theAtom : theAtomList) {
            if (!theAtom.getProperties().keySet().contains(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)) {
                this.__generateMpeoeAtomType(theMolecule.getAtomNumber(theAtom), theTopologicalDistanceMatrix);
            }
        }
    }

    private void __initializeAtdlList() {
        if (!this.itsMolecule.getProperties().containsKey(AtdlGenerator.IS_CALCULATED_ATDL)
                || this.itsMolecule.getProperty(AtdlGenerator.IS_CALCULATED_ATDL).equals(AtdlGenerator.NOT_CALCULATED_ATDL)) {
            AtdlGenerator theGenerator = new AtdlGenerator();

            this.itsAtdlList = theGenerator.generateAtdlList(this.itsMolecule, this.itsRingSet);
        } else {
            this.itsAtdlList = new ArrayList<>();

            for (IAtom theAtom : this.itsMolecule.atoms()) {
                this.itsAtdlList.add((Atdl) theAtom.getProperty(Atdl.ATDL_KEY));
            }
        }
    }

    private void __generateMpeoeAtomType(int theAtomIndex, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        String theSymbol = this.itsMolecule.getAtom(theAtomIndex).getSymbol();

        this.itsAtomIndex = theAtomIndex;

        if (AtomInformation.Hydrogen.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInHydrogen();
        } else if (AtomInformation.Nitrogen.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInNitrogen();
        } else if (AtomInformation.Carbon.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInCarbon();
        } else if (AtomInformation.Oxygen.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInOxygen();
        } else if (AtomInformation.Sulfur.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInSulfur();
        } else if (AtomInformation.Phosphorus.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInPhosphorus();
        } else if (AtomInformation.Silicon.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInSilicon();
        } else if (AtomInformation.Chlorine.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInChloride();
        } else if (AtomInformation.Bromine.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInBrome();
        } else if (AtomInformation.Fluorine.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInFluorine();
        } else if (AtomInformation.Iodine.SYMBOL.equals(theSymbol)) {
            this.__generateMpeoeAtomTypeInIodine();
        } else {
            this.__generateErrorAtomType();
            System.err.println(this.itsMolecule.getAtom(this.itsAtomIndex).getSymbol() + "Atom Error!!");
        }
    }

    private void __generateMpeoeAtomTypeInIodine() {
        IAtom theConnectedAtom = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex)).get(Constant.FIRST_INDEX);

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 74);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "I1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "I1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "I.sp3");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 38);

        if (((Atdl) theConnectedAtom.getProperty(Atdl.ATDL_KEY)).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 33);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 7);
        }
    }

    private void __generateMpeoeAtomTypeInFluorine() {
        IAtom theConnectedAtom = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex)).get(Constant.FIRST_INDEX);

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 71);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "F1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "F1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "F.sp3");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 35);

        if (theConnectedAtom.getProperty(Atdl.ATDL_KEY, Atdl.class).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 30);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 4);
        }
    }

    private void __generateMpeoeAtomTypeInBrome() {
        IAtom theConnectedAtom = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex)).get(Constant.FIRST_INDEX);

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 73);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "Br1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "Br1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "Br.sp3");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 37);

        if (((Atdl) theConnectedAtom.getProperty(Atdl.ATDL_KEY)).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 32);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 6);
        }
    }

    private void __generateMpeoeAtomTypeInChloride() {
        IAtom theConnectedAtom = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex)).get(Constant.FIRST_INDEX);

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 72);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "Cl1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "Cl1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "Cl.sp3");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 36);

        if (((Atdl) theConnectedAtom.getProperty(Atdl.ATDL_KEY)).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 31);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 5);
        }
    }

    private void __generateMpeoeAtomTypeInSilicon() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 61);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "Si.sp3");
    }

    private void __generateMpeoeAtomTypeInPhosphorus() {
        Integer theNumberOfBond = this.itsMolecule.getConnectedBondsCount(this.itsMolecule.getAtom(this.itsAtomIndex));

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 33);

        if (this.itsMolecule.getBondOrderSum(this.itsMolecule.getAtom(this.itsAtomIndex)) == 5) {
            this.__generateMpeoeAtomTypeInPhosphorusBoundedFiveAtom();
        } else if (this.itsMolecule.getBondOrderSum(this.itsMolecule.getAtom(this.itsAtomIndex)) == 3) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 51);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "X.sp3");
        } else {
            this.__generateErrorAtomType();
            System.err.println("Phosphorus Error!!");
        }
    }

    private void __generateMpeoeAtomTypeInPhosphorusBoundedFiveAtom() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "P5");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "X.sp3");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "P1");

        if (this.__isAtomBoundedNegativeChargeAtom()) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 151);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 52);
        }
    }

    private void __generateMpeoeAtomTypeInSulfur() {
        Integer theNumberOfBond = ((Double) this.itsMolecule.getBondOrderSum(this.itsMolecule.getAtom(this.itsAtomIndex))).intValue();

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 34);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "S1");

        if (theNumberOfBond.equals(SbffConstant.SIX_ATOM_BOUNDED)) {
            this.__generateMpeoeAtomTypeInSulfurBoundedSixAtom();
        } else if (theNumberOfBond.equals(SbffConstant.FOUR_ATOM_BOUNDED)) {
            this.__generateMpeoeAtomTypeInSulfurBoundedFourAtom();
        } else if (this.itsAtdlList.get(this.itsAtomIndex).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.__generateMpeoeAtomTypeInAromaticSulfur();
        } else if (theNumberOfBond.equals(SbffConstant.TWO_ATOM_BOUNDED)
                && this.itsMolecule.getConnectedAtomsCount(this.itsMolecule.getAtom(this.itsAtomIndex)) == SbffConstant.TWO_ATOM_BOUNDED) {
            this.__generateMpeoeAtomTypeInSulfurBoundedTwoAtom();
        } else if (this.itsMolecule.getConnectedAtomsCount(this.itsMolecule.getAtom(this.itsAtomIndex)) == SbffConstant.ONE_ATOM_BOUNDED) {
            this.__generateMpeoeAtomTypeInSulfurBoundedOneAtom();
        } else {
            this.__generateErrorAtomType();
            System.err.println("Sulfur Error!!");
        }
    }

    private void __generateMpeoeAtomTypeInSulfurBoundedOneAtom() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 44);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "S1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "S.sp2");
    }

    private void __generateMpeoeAtomTypeInSulfurBoundedTwoAtom() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 42);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "S1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "S.sp3");

        if (theConnectedAtomList.get(Constant.FIRST_INDEX).getSymbol().equals(AtomInformation.Sulfur.SYMBOL) || theConnectedAtomList.get(Constant.SECOND_INDEX).getSymbol().equals(AtomInformation.Sulfur.SYMBOL)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 38);
        } else if (theConnectedAtomList.get(Constant.FIRST_INDEX).getSymbol().equals(AtomInformation.Hydrogen.SYMBOL) || theConnectedAtomList.get(Constant.SECOND_INDEX).getSymbol().equals(AtomInformation.Hydrogen.SYMBOL)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 27);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 28);
        }
    }

    private void __generateMpeoeAtomTypeInAromaticSulfur() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 41);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "S1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "S.ar");
    }

    private void __generateMpeoeAtomTypeInSulfurBoundedFourAtom() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 45);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "S6");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "X.sp3");
    }

    private void __generateMpeoeAtomTypeInSulfurBoundedSixAtom() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "S6");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "X.sp3");

        if (this.__isAtomBoundedNegativeChargeAtom()) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 141);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 43);
        }
    }

    private Boolean __isAtomBoundedNegativeChargeAtom() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getFormalCharge().equals(this.NEGATIVE_CHARGE)) {
                return true;
            }
        }

        return false;
    }

    private void __generateMpeoeAtomTypeInOxygen() {
        switch (this.itsMolecule.getConnectedAtomsCount(this.itsMolecule.getAtom(this.itsAtomIndex))) {
            case SbffConstant.ONE_ATOM_BOUNDED:
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 23);
                this.__generateMpeoeAtomTypeInOxygenBoundedOneAtom();
                break;
            case SbffConstant.TWO_ATOM_BOUNDED:
                this.__generateMpeoeAtomTypeInOxygenBoundedTwoAtom();
                break;
            default:
                this.__generateMpeoeAtomTypeInOxygenForWater();
        }
    }

    private void __generateMpeoeAtomTypeInOxygenForWater() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 23);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY, -0.834);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "O2");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 18);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "O.sp3");
    }

    private void __generateMpeoeAtomTypeInOxygenBoundedTwoAtom() {
        if (this.itsAtdlList.get(this.itsAtomIndex).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 21);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "O1");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 21);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "O.sp2");
        } else if (this.__isWaterOxygen(this.itsMolecule.getAtom(this.itsAtomIndex))) {
            this.__generateMpeoeAtomTypeInOxygenForWater();
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "O2");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 18);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "O.sp3");
            this.__setMpeoeAtomTypeInOxygenBoundedTwoAtom();
        }
    }

    private boolean __isWaterOxygen(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (!theConnectedAtom.getSymbol().equals(AtomInformation.Hydrogen.SYMBOL)) {
                return false;
            }
        }

        return true;
    }

    private void __setMpeoeAtomTypeInOxygenBoundedTwoAtom() {
        List<Integer> theConnectedAtomTypeList = this.__getConnectedAtomTypeList();

        if (theConnectedAtomTypeList.contains(43) || theConnectedAtomTypeList.contains(52)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 52);
        } else if (theConnectedAtomTypeList.contains(45)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 28);
        } else if (this.__getNumberOfBoundedAtomSymbol(AtomInformation.Silicon.SYMBOL).equals(SbffConstant.ONE_ATOM_BOUNDED)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 26);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 23);
            this.__setHydrogenBondAtomTypeInOxygenBoundedTwoAtom();
        }
    }

    private void __setHydrogenBondAtomTypeInOxygenBoundedTwoAtom() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));
        int theNumberOfHydrogen = 0;
        int theHydrogenIndex = -1;
        int theOtherAtomIndex = -1;

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getSymbol().equals(AtomInformation.Hydrogen.SYMBOL)) {
                theHydrogenIndex = this.itsMolecule.getAtomNumber(theConnectedAtom);
                theNumberOfHydrogen++;
            } else {
                theOtherAtomIndex = this.itsMolecule.getAtomNumber(theConnectedAtom);
            }
        }

        if (theNumberOfHydrogen == 1) {
            this.__setHydrogenBondAtomTypeInOxygenBoundedOneHydrogenAndOtherAtom(theHydrogenIndex, theOtherAtomIndex);
        } else if (theNumberOfHydrogen == 0) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 39);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 25);

            for (IAtom theHydrogenAtom : theConnectedAtomList) {
                theHydrogenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 8);
            }
        }
    }

    private void __setHydrogenBondAtomTypeInOxygenBoundedOneHydrogenAndOtherAtom(int theHydrogenIndex, int theOtherAtomIndex) {
        IAtom theOtherAtom = this.itsMolecule.getAtom(theOtherAtomIndex);

        if (((Atdl) theOtherAtom.getProperty(Atdl.ATDL_KEY)).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 41);
            this.itsMolecule.getAtom(theHydrogenIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 14);
        } else if (!((Atdl) theOtherAtom.getProperty(Atdl.ATDL_KEY)).getRingIndicator().equals(Atdl.NOT_HAVE_RING)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 40);
            this.itsMolecule.getAtom(theHydrogenIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 13);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 26);
            this.itsMolecule.getAtom(theHydrogenIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 9);
        }
    }

    private void __generateMpeoeAtomTypeInOxygenBoundedOneAtom() {
        if (this.itsMolecule.getBondOrderSum(this.itsMolecule.getAtom(this.itsAtomIndex)) == 1.0) {
            this.__setMpeoeAtomTypeInOxygenHavingOneInBondOrderSum();
        } else if (this.__isOxygenBoundedSulfurOrPhosphorus()) {
            this.__generateMpeoeAtomTypeInOxygenBoundedSulfurOrPhosphorus();
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "O2");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "O.sp2");
            this.__setMpeoeAtomTypeInOxygenBoundedOneAtomNotSulfurAndPhosphorus();
        }
    }

    private void __setMpeoeAtomTypeInOxygenHavingOneInBondOrderSum() {
        List<Integer> theConnectedAtomTypeList = this.__getConnectedAtomTypeList();

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "O1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "O.sp2");

        if (theConnectedAtomTypeList.contains(112)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 121);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 22);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "O3");
        } else if (theConnectedAtomTypeList.contains(133)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 19);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "O5");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 123);
            this.__setHydrogenBondAtomTypeIn123();
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 23);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "O5");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 125);
        }
    }

    private void __setHydrogenBondAtomTypeIn123() {
        List<Integer> theConnectedAtomTypeList = this.__getConnectedAtomTypeList(this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex)).get(Constant.FIRST_INDEX));

        if (theConnectedAtomTypeList.contains(12)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 44);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 20);
        }
    }

    private void __setMpeoeAtomTypeInOxygenBoundedOneAtomNotSulfurAndPhosphorus() {
        List<Integer> theConnectedAtomTypeList = this.__getConnectedAtomTypeList();
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "O1");

        if (theConnectedAtomTypeList.contains(112)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 121);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 22);
        } else if (theConnectedAtomTypeList.contains(133)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 19);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 123);
            this.__setHydrogenBondAtomTypeIn123();
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 20);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 22);
        }
    }

    private void __generateMpeoeAtomTypeInOxygenBoundedSulfurOrPhosphorus() {
        List<Integer> theConnectedAtomTypeList = this.__getConnectedAtomTypeList();

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 21);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "O1");

        if (theConnectedAtomTypeList.contains(141) || theConnectedAtomTypeList.contains(151)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 122);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "O2");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "O.sp3");
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "O1");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "O.sp2");

            if (theConnectedAtomTypeList.contains(45)) {
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 27);
            } else {
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 25);
            }
        }
    }

    private Boolean __isOxygenBoundedSulfurOrPhosphorus() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getSymbol().equals(AtomInformation.Sulfur.SYMBOL) || theConnectedAtom.getSymbol().equals(AtomInformation.Phosphorus.SYMBOL)) {
                return true;
            }
        }

        return false;
    }

    private void __generateMpeoeAtomTypeInCarbon() {
        switch (this.itsMolecule.getConnectedAtomsCount(this.itsMolecule.getAtom(this.itsAtomIndex))) {
            case SbffConstant.TWO_ATOM_BOUNDED:
                this.__generateMpeoeAtomTypeInCarbonBoundedTwoAtom();
                break;
            case SbffConstant.THREE_ATOM_BOUNDED:
                this.__generateMpeoeAtomTypeInCarbonBoundedThreeAtom();
                break;
            case SbffConstant.FOUR_ATOM_BOUNDED:
                this.__generateMpeoeAtomTypeInCarbonBoundedFourAtom();
                break;
            default:
                this.__generateErrorAtomType();
                System.err.println("Carbon Error!!");
        }
    }

    private void __generateMpeoeAtomTypeInCarbonBoundedTwoAtom() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 17);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "C0");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 14);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "C.sp");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 3);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getSymbol().endsWith(AtomInformation.Hydrogen.SYMBOL)) {
                theConnectedAtom.setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 4);
                theConnectedAtom.setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 6);
                theConnectedAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 1);
            }
        }
    }

    private void __generateMpeoeAtomTypeInCarbonBoundedThreeAtom() {
        if (this.itsAtdlList.get(this.itsAtomIndex).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.__generateMpeoeAtomTypeInAromaticCarbonBoundedThreeAtom();
        } else {
            this.__generateMpeoeAtomTypeInNonAromaticCarbonBoundedThreeAtom();
        }
    }

    private void __generateMpeoeAtomTypeInNonAromaticCarbonBoundedThreeAtom() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));
        IAtom theSp2OxygenAtom = null;
        IAtom theSp3OxygenAtom = null;
        int theNumberOfHydrogen = 0;
        int theNumberOfOtherAtom = 0;

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getSymbol().equals(AtomInformation.Oxygen.SYMBOL)) {
                if (this.itsMolecule.getConnectedAtomsCount(theConnectedAtom) == SbffConstant.ONE_ATOM_BOUNDED
                        && this.itsMolecule.getBondOrderSum(theConnectedAtom) == 2) {
                    theSp2OxygenAtom = theConnectedAtom;
                } else {
                    theSp3OxygenAtom = theConnectedAtom;
                }
            } else if (theConnectedAtom.getSymbol().equals(AtomInformation.Hydrogen.SYMBOL)) {
                theNumberOfHydrogen++;
            } else {
                theNumberOfOtherAtom++;
            }
        }

        if (theSp2OxygenAtom != null) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "C3");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 12);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "C.sp2");

            if (theSp3OxygenAtom != null) {
                this.__setMpeoeAtomTypeInCarboxylCarbon(theSp2OxygenAtom, theSp3OxygenAtom, theNumberOfOtherAtom);
            } else if (theNumberOfOtherAtom == 2) {
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 14);

                if (!((Atdl) this.itsMolecule.getAtom(this.itsAtomIndex).getProperty(Atdl.ATDL_KEY)).getRingIndicator().equals(Atdl.NOT_HAVE_RING)) {
                    theSp2OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 12);
                } else {
                    theSp2OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 11);
                }
            } else if (theNumberOfOtherAtom == 1) {
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 14);
                theSp2OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 10);
            }
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 11);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "C1");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 13);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "C.sp2");
            this.__generateHydrogenBondAtomTypeInNonAromaticBoundedThreeAtom();
        }
    }

    private void __generateHydrogenBondAtomTypeInNonAromaticBoundedThreeAtom() {
        if (!((Atdl) this.itsMolecule.getAtom(this.itsAtomIndex).getProperty(Atdl.ATDL_KEY)).getRingIndicator().equals(Atdl.NOT_HAVE_RING)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 2);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 1);
        }
    }

    private void __generateMpeoeAtomTypeInAromaticCarbonBoundedThreeAtom() {
        List<Integer> theMpeoeAtomTypeListInConnectedAtom = this.__getConnectedAtomTypeList();

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "C2");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 12);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "C.ar");

        if (theMpeoeAtomTypeListInConnectedAtom.contains(132)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 115);
        } else if (theMpeoeAtomTypeListInConnectedAtom.contains(134)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 116);
        } else if (theMpeoeAtomTypeListInConnectedAtom.contains(125)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 118);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 17);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 12);
            this.__generateHydrogenAtomTypeInAromaticCarbonBoundedThreeAtom();
        }
    }

    private void __generateHydrogenAtomTypeInAromaticCarbonBoundedThreeAtom() {
        List<Integer> theMpeoeAtomTypeListInConnectedAtom = this.__getConnectedAtomTypeList();

        if (theMpeoeAtomTypeListInConnectedAtom.contains(13)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 48);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 29);
        }
    }

    private void __setMpeoeAtomTypeInCarboxylCarbon(IAtom theSp2OxygenAtom, IAtom theSp3OxygenAtom, int theNumberOfOtherAtom) {
        if (this.itsMolecule.getBondOrderSum(theSp3OxygenAtom) == 1) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 112);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 15);
        } else if (this.itsMolecule.getBondOrderSum(theSp3OxygenAtom) == 2) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 14);
            this.__setHydrogenBondAtomTypeInCarboxylCarbonOrAlphaCarbon(theSp2OxygenAtom, theSp3OxygenAtom, theNumberOfOtherAtom);
        }
    }

    private void __setHydrogenBondAtomTypeInCarboxylCarbonOrAlphaCarbon(IAtom theSp2OxygenAtom, IAtom theSp3OxygenAtom, int theNumberOfOtherAtom) {
        List<IAtom> theConnectedAtomListInSp3Oxygen = this.itsMolecule.getConnectedAtomsList(theSp3OxygenAtom);
        boolean theIsBoundedHydrogen = false;
        IAtom theHydrogenAtom = null;

        for (IAtom theConnectedAtom : theConnectedAtomListInSp3Oxygen) {
            if (theConnectedAtom.getSymbol().equals(AtomInformation.Hydrogen.SYMBOL)) {
                theIsBoundedHydrogen = true;
                theHydrogenAtom = theConnectedAtom;
                break;
            }
        }

        if (theIsBoundedHydrogen) {
            if (theNumberOfOtherAtom > 0) {
                theSp3OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 0);
                theSp2OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 24);
                theHydrogenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 7);
            } else {
                theSp3OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 0);
                theSp2OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 37);
                theHydrogenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 12);
            }
        } else {
            if (theNumberOfOtherAtom > 0) {
                theSp3OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 0);
                theSp2OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 14);
            } else {
                theSp3OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 0);
                theSp2OxygenAtom.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 13);
            }
        }
    }

    private Boolean __isAtomBoundedTwoOxygenOrSulfur() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));
        Integer theNumberOfAtom = 0;
        Integer theCharge = 0;

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getSymbol().equals(AtomInformation.Oxygen.SYMBOL) || theConnectedAtom.getSymbol().equals(AtomInformation.Sulfur.SYMBOL)) {
                theCharge += theConnectedAtom.getFormalCharge();
                theNumberOfAtom++;
            }
        }

        return theNumberOfAtom.equals(SbffConstant.TWO_ATOM_BOUNDED) && theCharge.equals(this.NEGATIVE_CHARGE);
    }

    private Boolean __isCarbonBoundedDoubleBondOxygenOrSulfur() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getSymbol().equals(AtomInformation.Oxygen.SYMBOL)
                    && this.itsMolecule.getBond(this.itsMolecule.getAtom(this.itsAtomIndex), theConnectedAtom).getOrder().equals(Order.DOUBLE)) {
                return true;
            }
        }

        return false;
    }

    private Boolean __isOxygenOrSulfur(IAtom theAtom) {
        return theAtom.getSymbol().equals(AtomInformation.Oxygen.SYMBOL) || theAtom.getSymbol().equals(AtomInformation.Sulfur.SYMBOL);
    }

    private void __generateMpeoeAtomTypeInCarbonBoundedFourAtom() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "C4");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 10);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "C.sp3");

        this.__setMpeoeAtomTypeInCarbonBoundedFourAtom();
    }

    private void __setMpeoeAtomTypeInCarbonBoundedFourAtom() {
        List<Integer> theConnectedAtomTypeList = this.__getConnectedAtomTypeList();

        if (theConnectedAtomTypeList.contains(141) || theConnectedAtomTypeList.contains(151)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 114);
        } else if (theConnectedAtomTypeList.contains(71)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 19);
        } else if (theConnectedAtomTypeList.contains(43) || theConnectedAtomTypeList.contains(52)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 15);
        } else if (theConnectedAtomTypeList.contains(45)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 18);
        } else if (theConnectedAtomTypeList.contains(125)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 117);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 16);
        } else if (theConnectedAtomTypeList.contains(131)) {
            IAtom theAtom = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex)).get(theConnectedAtomTypeList.indexOf(131));

            this.__setMpeoeAtomTypeInAlphaCarbonOrCarbonBoundPositiveNitrogen();
        } else if (theConnectedAtomTypeList.contains(134)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 119);
        } else if (this.__getNumberOfBoundedAtomSymbol(AtomInformation.Silicon.SYMBOL).compareTo(1) > 0) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 16);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 13);
        }
    }

    private void __setMpeoeAtomTypeInAlphaCarbonOrCarbonBoundPositiveNitrogen() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getProperties().containsKey(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY) && this.__isConnectedAtomAtomTypeInHydrogen(112, theConnectedAtom)) {
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 113);
                return;
            }
        }

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 111);
    }

    private void __generateMpeoeAtomTypeInNitrogen() {
        switch (this.itsMolecule.getConnectedBondsCount(this.itsMolecule.getAtom(this.itsAtomIndex))) {
            case SbffConstant.ONE_ATOM_BOUNDED:
                this.__generateMpeoeAtomTypeInNitrogenBoundedOneAtom();
                break;
            case SbffConstant.TWO_ATOM_BOUNDED:
                this.__generateMpeoeAtomTypeInNitrogenBoundedTwoAtom();
                break;
            case SbffConstant.THREE_ATOM_BOUNDED:
                this.__generateMpeoeAtomTypeInNitrogenBoundedThreeAtom();
                break;
            case SbffConstant.FOUR_ATOM_BOUNDED:
                this.__generateMpeoeAtomTypeInNitrogenBoundedFourAtom();
                break;
            default:
                this.__generateErrorAtomType();
                System.err.println("Nitrogen Atom Error!!");
        }
    }

    private void __generateMpeoeAtomTypeInNitrogenBoundedFourAtom() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 131);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N4");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 31);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N+.sp3");
    }

    private void __generateMpeoeAtomTypeInNitrogenBoundedThreeAtom() {
        if (this.itsAtdlList.get(this.itsAtomIndex).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.__generateMpeoeAtomTypeInAromaticNitrogenBoundedThreeAtom();
        } else {
            this.__generateMpeoeAtomTypeInNonAromaticNitrogenBoundedThreeAtom();
        }
    }

    private void __generateMpeoeAtomTypeInAromaticNitrogenBoundedThreeAtom() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.ar");

        if (this.itsMolecule.getBondOrderSum(this.itsMolecule.getAtom(this.itsAtomIndex)) == 4.0) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 134);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 32);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 32);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 24);
        }
    }

    private void __generateMpeoeAtomTypeInNonAromaticNitrogenBoundedThreeAtom() {
        List<Integer> theConnectedAtomTypeList = this.__getConnectedAtomTypeList();

        if (theConnectedAtomTypeList.contains(141) || theConnectedAtomTypeList.contains(151)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 131);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N4");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 27);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.sp3");
        } else if (theConnectedAtomTypeList.contains(43) || theConnectedAtomTypeList.contains(52)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 35);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N4");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 27);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.sp3");
        } else if (theConnectedAtomTypeList.contains(45)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 38);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N4");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 27);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.sp3");
        } else if (theConnectedAtomTypeList.contains(14)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 33);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N3");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 26);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.sp2");
            this.__setHydrogenBondAtomTypeInInNonAromaticNitrogenBoundedThreeAtomContain14();
        } else if (this.__getNumberOfBoundedAtomSymbol(AtomInformation.Oxygen.SYMBOL).equals(SbffConstant.TWO_ATOM_BOUNDED)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 133);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N3");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 25);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.sp2");
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 34);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N4");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 27);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.sp3");
            this.__setHydrogenBondAtomTypeInNonAromaticNitrogenBoundedThreeAtom();
        }
    }

    private void __setHydrogenBondAtomTypeInNonAromaticNitrogenBoundedThreeAtom() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));
        List<Integer> theHydrogenAtomIndexList = new ArrayList<>();
        int theNumberOfHydrogen = 0;
        boolean theIsBoundedAromaticAtom = false;

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getSymbol().equals(AtomInformation.Hydrogen.SYMBOL)) {
                theHydrogenAtomIndexList.add(this.itsMolecule.getAtomNumber(theConnectedAtom));
                theNumberOfHydrogen++;
            } else if (((Atdl) theConnectedAtom.getProperty(Atdl.ATDL_KEY)).getAromaticIndicator().equals(Atdl.AROMATIC)) {
                theIsBoundedAromaticAtom = true;
            }
        }

        if (theIsBoundedAromaticAtom) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 43);

            for (Integer theHydrogenAtomIndex : theHydrogenAtomIndexList) {
                this.itsMolecule.getAtom(theHydrogenAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 15);
            }
        } else if (theNumberOfHydrogen == 3) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 16);

            for (Integer theHydrogenAtomIndex : theHydrogenAtomIndexList) {
                this.itsMolecule.getAtom(theHydrogenAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 2);
            }
        } else if (theNumberOfHydrogen == 2) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 17);

            for (Integer theHydrogenAtomIndex : theHydrogenAtomIndexList) {
                this.itsMolecule.getAtom(theHydrogenAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 3);
            }
        } else if (theNumberOfHydrogen == 1) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 18);

            for (Integer theHydrogenAtomIndex : theHydrogenAtomIndexList) {
                this.itsMolecule.getAtom(theHydrogenAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 4);
            }
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 19);
        }
    }

    private void __setHydrogenBondAtomTypeInInNonAromaticNitrogenBoundedThreeAtomContain14() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));
        List<Integer> theConnectedHydrogenAtomIndexList = new ArrayList<>();
        IAtom theSp2Oxygen = null;
        int theNumberOfHydrogen = 0;
        boolean theContainHydrogenBoundedCarbon = false;

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getSymbol().equals(AtomInformation.Hydrogen.SYMBOL)) {
                theConnectedHydrogenAtomIndexList.add(this.itsMolecule.getAtomNumber(theConnectedAtom));
                theNumberOfHydrogen++;
            } else if (((Integer) theConnectedAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)).intValue() == 14) {
                List<IAtom> theConnectedAtomListInSecondLayer = this.itsMolecule.getConnectedAtomsList(theConnectedAtom);

                for (IAtom theConnectedAtomInSecondLayer : theConnectedAtomListInSecondLayer) {
                    if (theConnectedAtomInSecondLayer.getSymbol().equals(AtomInformation.Oxygen.SYMBOL)) {
                        theSp2Oxygen = theConnectedAtomInSecondLayer;
                    } else if (theConnectedAtomInSecondLayer.getSymbol().equals(AtomInformation.Hydrogen.SYMBOL)) {
                        theContainHydrogenBoundedCarbon = true;
                    }
                }
            }
        }

        if (theContainHydrogenBoundedCarbon) {
            if (theNumberOfHydrogen == 2) {
                this.__setHydrogenBondAtomTypeInInNonAromaticNitrogenBoundedTwoHydrogenAtomAnd14Atom(theSp2Oxygen, theConnectedHydrogenAtomIndexList);
            } else if (theNumberOfHydrogen == 1) {
                this.__setHydrogenBondAtomTypeInInNonAromaticNitrogenBoundedOneHydrogenAtomAnd14Atom(theSp2Oxygen, theConnectedHydrogenAtomIndexList);
            } else {
                theSp2Oxygen.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 36);
            }
        } else {
            if (theNumberOfHydrogen == 2) {
                theSp2Oxygen.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 21);
                this.itsMolecule.getAtom(theConnectedHydrogenAtomIndexList.get(Constant.FIRST_INDEX)).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 5);
                this.itsMolecule.getAtom(theConnectedHydrogenAtomIndexList.get(Constant.SECOND_INDEX)).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 5);
            } else if (theNumberOfHydrogen == 1) {
                theSp2Oxygen.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 22);
                this.itsMolecule.getAtom(theConnectedHydrogenAtomIndexList.get(Constant.FIRST_INDEX)).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 6);
            } else {
                theSp2Oxygen.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 23);
            }
        }
    }

    private void __setHydrogenBondAtomTypeInInNonAromaticNitrogenBoundedOneHydrogenAtomAnd14Atom(IAtom theSp2Oxygen, List<Integer> theHydrogenAtomIndexList) {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 0);
        theSp2Oxygen.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 35);
        this.itsMolecule.getAtom(theHydrogenAtomIndexList.get(Constant.FIRST_INDEX)).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 11);
    }

    private void __setHydrogenBondAtomTypeInInNonAromaticNitrogenBoundedTwoHydrogenAtomAnd14Atom(IAtom theSp2Oxygen, List<Integer> theHydrogenAtomIndexList) {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 0);
        theSp2Oxygen.setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 34);
        this.itsMolecule.getAtom(theHydrogenAtomIndexList.get(Constant.FIRST_INDEX)).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 10);
        this.itsMolecule.getAtom(theHydrogenAtomIndexList.get(Constant.SECOND_INDEX)).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 10);
    }

    private Integer __getNumberOfBoundedAtomSymbol(String theAtomSymbol) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));
        Integer theNumberOfBoundedAtomSymbol = 0;

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getSymbol().equals(theAtomSymbol)) {
                theNumberOfBoundedAtomSymbol++;
            }
        }

        return theNumberOfBoundedAtomSymbol;
    }

    private Boolean __isAmideNitrogen() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (this.__isAmideCarbon(theConnectedAtom)) {
                return true;
            }
        }

        return false;
    }

    private Boolean __isAmideCarbon(IAtom theCarbonAtom) {
        if (theCarbonAtom.getSymbol().equals(AtomInformation.Carbon.SYMBOL)
                && this.itsMolecule.getConnectedBondsCount(theCarbonAtom) == SbffConstant.THREE_ATOM_BOUNDED) {
            List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theCarbonAtom);

            for (IAtom theConnectedAtom : theConnectedAtomList) {
                if (theConnectedAtom.getSymbol().equals(AtomInformation.Oxygen.SYMBOL)
                        || theConnectedAtom.getSymbol().equals(AtomInformation.Sulfur.SYMBOL)) {
                    if (this.itsMolecule.getBond(theCarbonAtom, theConnectedAtom).getOrder().equals(Order.DOUBLE)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private List<Integer> __getConnectedAtomTypeList(IAtom theConnectedAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theConnectedAtom);
        List<Integer> theConnectedAtomTypeList = new ArrayList<>();

        for (IAtom theAtom : theConnectedAtomList) {
            if (theAtom.getProperties().containsKey(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)) {
                theConnectedAtomTypeList.add((Integer) theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY));
            }
        }

        return theConnectedAtomTypeList;
    }

    private List<Integer> __getConnectedAtomTypeList() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));
        List<Integer> theConnectedAtomTypeList = new ArrayList<>();

        for (IAtom theAtom : theConnectedAtomList) {
            if (theAtom.getProperties().containsKey(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)) {
                theConnectedAtomTypeList.add((Integer) theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY));
            }
        }

        return theConnectedAtomTypeList;
    }

    private void __generateMpeoeAtomTypeInNitrogenBoundedTwoAtom() {
        if (this.__isNitrogenInPyrazine()) {//??
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 39);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N2");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 28);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.ar");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 42);
        } else if (this.itsAtdlList.get(this.itsAtomIndex).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 31);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N2");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 28);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.ar");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 42);
        } else if (this.__getNumberOfBond(Order.DOUBLE).equals(SbffConstant.TWO_ATOM_BOUNDED)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 37);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N0");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 30);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.sp");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 15);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 36);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N4");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 36);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.sp2");
        }
    }

    private Boolean __isNitrogenInPyrazine() {
        RingPerception theRingPerception = new RingPerception();
        IRingSet theAromaticRingSet = theRingPerception.recognizeAromaticRing(this.itsMolecule, this.itsRingSet);

        for (IAtomContainer theRing : theAromaticRingSet.atomContainers()) {
            if (this.__isThreeTopologicalDistanceInSixMemberedRing(theRing)) {
                return true;
            }
        }
        return false;
    }

    private Boolean __isThreeTopologicalDistanceInSixMemberedRing(IAtomContainer theRing) {
        if (theRing.getAtomCount() != this.SIX_MEMEBERED_RING || !this.__containsTwoNitrogenAtomInRing(theRing)) {
            return false;
        }

        List<Integer> theNitrogenAtomIndexList = this.__getNitrogenAtomIndexInSixMemberedRing(theRing);

        return this.itsTopologicalDistanceMatrix.getDistance(theNitrogenAtomIndexList.get(Constant.FIRST_INDEX), theNitrogenAtomIndexList.get(Constant.SECOND_INDEX)) == 3;
    }

    private List<Integer> __getNitrogenAtomIndexInSixMemberedRing(IAtomContainer theRing) {
        List<Integer> theNitrogenAtomIndexList = new ArrayList<>();

        for (int ai = 0, aEnd = theRing.getAtomCount(); ai < aEnd; ai++) {
            if (theRing.getAtom(ai).getSymbol().equals(AtomInformation.Nitrogen.SYMBOL)) {
                theNitrogenAtomIndexList.add(this.itsMolecule.getAtomNumber(theRing.getAtom(ai)));
            }
        }

        return theNitrogenAtomIndexList;
    }

    private Boolean __containsTwoNitrogenAtomInRing(IAtomContainer theRing) {
        int theNumberOfNitrogen = 0;

        for (IAtom theAtom : theRing.atoms()) {
            if (theAtom.getSymbol().equals(AtomInformation.Nitrogen.SYMBOL)) {
                theNumberOfNitrogen++;
            }
        }

        return theNumberOfNitrogen == 2;
    }

    private Integer __getNumberOfBond(Order theOrder) {
        List<IBond> theConnectedBondList = this.itsMolecule.getConnectedBondsList(this.itsMolecule.getAtom(this.itsAtomIndex));
        Integer theNumberOfBond = 0;

        for (IBond theConnectedBond : theConnectedBondList) {
            if (theConnectedBond.getOrder().equals(Order.DOUBLE)) {
                theNumberOfBond++;
            }
        }

        return theNumberOfBond;
    }

    private void __generateMpeoeAtomTypeInNitrogenBoundedOneAtom() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 37);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "N0");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "N.sp");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 30);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY, 15);
    }

    private void __generateMpeoeAtomTypeInHydrogen() {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(this.itsAtomIndex));

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "H.sp3");
        
        if (((Atdl) theConnectedAtomList.get(Constant.FIRST_INDEX).getProperty(Atdl.ATDL_KEY)).getAromaticIndicator().equals(Atdl.AROMATIC)) {
            this.__generateMpeoeAtomTypeInHydrogenBoundedAromaticAtom(theConnectedAtomList.get(Constant.FIRST_INDEX));
        } else {
            this.__generateMpeoeAtomTypeBoundedNonAromaticAtom(theConnectedAtomList.get(Constant.FIRST_INDEX));
        }
    }

    private void __generateMpeoeAtomTypeInHydrogenBoundedAromaticAtom(IAtom theConnectedAtom) {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "H2");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "H3");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.EN_PULS_KEY, 13.594);

        if (((Integer) theConnectedAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)).intValue() == 134) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 104);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 9);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 2);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 1);
        }
    }

    private void __generateMpeoeAtomTypeBoundedNonAromaticAtom(IAtom theConnectedAtom) {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "H1");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.EN_PULS_KEY, 3.389);

        if (theConnectedAtom.getSymbol().equals(AtomInformation.Carbon.SYMBOL)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 6);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "H1");
            this.__generateMpeoeAtomTypeBoundedNonAromaticCarbon(theConnectedAtom);
        } else {
            this.__generateMpeoeAtomTypeBoundedNonAromaticOtherAtom(theConnectedAtom);
        }
    }

    private void __generateMpeoeAtomTypeBoundedNonAromaticOtherAtom(IAtom theConnectedAtom) {
        if (this.__isConnectedAtomAtomTypeInHydrogen(131, theConnectedAtom)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 101);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 8);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "H5");
        } else if (theConnectedAtom.getSymbol().equals(AtomInformation.Silicon.SYMBOL)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 3);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "H1");
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 1);
            this.__generateOtherAtomTypeBoundedNonAromaticOtherAtom(theConnectedAtom);
        }
    }

    private void __generateOtherAtomTypeBoundedNonAromaticOtherAtom(IAtom theConnectedAtom) {
        String theSymbol = theConnectedAtom.getSymbol();

        if (AtomInformation.Oxygen.SYMBOL.equals(theSymbol)) {
            this.__generateOtherAtomTypeBoundedNonAromaticOxygenAtom(theConnectedAtom);
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "H4");
        } else if (AtomInformation.Sulfur.SYMBOL.equals(theSymbol)) {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "H1");
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 7);
        } else if (AtomInformation.Nitrogen.SYMBOL.equals(theSymbol)) {
            this.__generateOtehreAtomTypeBoundedNonAromaticNitrogenAtom(theConnectedAtom);
        } else {
            this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 6);
        }
    }

    private void __generateOtehreAtomTypeBoundedNonAromaticNitrogenAtom(IAtom theConnectedAtom) {
        List<IAtom> theAtomListConnectedSecondLayer = this.itsMolecule.getConnectedAtomsList(theConnectedAtom);

        for (IAtom theAtom : theAtomListConnectedSecondLayer) {
            if (!theAtom.getSymbol().equals(AtomInformation.Hydrogen.SYMBOL) && theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY).equals(14)) {
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "H2");
            }
        }
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 4);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "H1");
    }

    private void __generateOtherAtomTypeBoundedNonAromaticOxygenAtom(IAtom theConnectedAtom) {
        List<IAtom> theTwoDistanceConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theConnectedAtom);

        for (IAtom theAtom : theTwoDistanceConnectedAtomList) {
            if (!theAtom.equals(this.itsMolecule.getAtom(this.itsAtomIndex)) && this.__isConnectedAtomAtomTypeInHydrogen(14, theAtom)) {
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 3);
                return;
            }
        }

        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, 2);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, "H4");
    }

    private void __generateMpeoeAtomTypeBoundedNonAromaticCarbon(IAtom theConnectedAtom) {
        Integer theCarbonMpeoeAtomType = (Integer) theConnectedAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY);

        switch (theCarbonMpeoeAtomType) {
            case 17:
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 4);
                break;
            case 113:
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 102);
                break;
            case 112:
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 103);
                break;
            default:
                this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, 1);
        }
    }

    private Boolean __isConnectedAtomAtomTypeInHydrogen(Integer theMpeoeAtomType, IAtom theConnectedAtom) {
        if (!theConnectedAtom.getProperties().containsKey(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)) {
            return false;
        }

        return ((Integer) theConnectedAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)).equals(theMpeoeAtomType);
    }

    private void __generateErrorAtomType() {
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY, -1);
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY, "Error");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY, "Error");
        this.itsMolecule.getAtom(this.itsAtomIndex).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY, -1);
    }
}
