/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.tool;

import java.io.Serializable;
import java.util.ArrayList;
import org.bmdrc.sbff.parameter.interenergy.SbffMpeoeParameterList;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import java.util.List;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.tool.RingPerception;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.sbff.interenergy.SbffNonbondingEnergyFunction;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IRingSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class MpeoeChargeCalculator implements Serializable {

    private static final long serialVersionUID = 4056507772068769911L;

    private IAtomContainer itsMolecule;
    private SbffMpeoeParameterList itsParameterList;
    //constant Integer variable
    private final Integer FIRST_INDEX = 0;
    private final Integer SECOND_INDEX = 1;
    private final Integer DAMP_NAME_INDEX = 0;
    private final Integer DAMP_TYPE_INDEX = 1;
    //constant Double variable
    private final Double INITIAL_ELECTRONEGATIVITY = 0.0;
    private final double STOP_CONDITION_DQ = 1.0e-3;
    //constant String variable
    public static final String IS_CALCULATED = "Is_MPEOE_Charge_Calculated";
    public static final String MPEOE_CHARGE_KEY = "MPEOE_Charge";
    public static final String BOND_DAMP_TYPE_KEY = "Damp_Type";
    public static final String ELECTRONEGATIVITY_KEY = "Electronegativity";
    public static final String CHARGE_HISTORY_KEY = "Charge history";
    public static final String ELECTRO_NEGATIVITY_HISTORY_KEY = "Electro-negativity history";
    public static final String NUMBER_OF_ITERATION_KEY = "Number of iteration";
    private final String CHARGE_DIFFERENCE_KEY = "MPEOE_Charge_Difference";
    private final String COMMA_REGEX = "\\.";
    private final String POSITIVE = "+";
    private final String H_SYMBOL = "H";
    private final String S_SYMBOL = "S";
    private final String N_SYMBOL = "N";
    private final String O_SYMBOL = "O";
    private final String C_SYMBOL = "C";
    private final String BR_SYMBOL = "Br";
    private final String CL_SYMBOL = "Cl";
    private final String F_SYMBOL = "F";
    private final String I_SYMBOL = "I";
    private final String SI_SYMBOL = "Si";
    private final String HALOGEN_SYMBOL = "X";
    private final String SP3_TYPE = "sp3";
    private final String SP2_TYPE = "sp2";
    private final String SP_TYPE = "sp";
    private final String AROMATIC_TYPE = "ar";
    private final String IS_WATER_KEY = "Is water";

    public MpeoeChargeCalculator() {
        this.itsParameterList = new SbffMpeoeParameterList();
    }
    
    public MpeoeChargeCalculator(SbffMpeoeParameterList theParameterList) {
        this.itsParameterList = theParameterList;
    }

    public IAtomContainer getMolecule() {
        return itsMolecule;
    }

    public void setMolecule(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
    }

    public IAtomContainer setMolecule() {
        return itsMolecule;
    }

    public void setParameterList(SbffMpeoeParameterList theParameterList) {
        this.itsParameterList = theParameterList;
    }

    public void inputMpeoeCharge(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.setMolecule(theMolecule);
        MpeoeAtomTypeGenerator theAtomType = new MpeoeAtomTypeGenerator();

        theAtomType.inputMpeoeAtomType(this.itsMolecule, theTopologicalDistanceMatrix);

        this.__setWaterAtom();
        this.__setEmptyEnPlusValue();
        this.__setEmptydampType();
        this.__calculateMpeoeCharge();
        this.itsMolecule.setProperty(MpeoeChargeCalculator.IS_CALCULATED, true);
    }
    
    public void inputMpeoeCharge(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix, IRingSet theRingSet) {
        this.setMolecule(theMolecule);
        MpeoeAtomTypeGenerator theAtomType = new MpeoeAtomTypeGenerator();

        theAtomType.inputMpeoeAtomType(this.itsMolecule, theTopologicalDistanceMatrix, theRingSet);

        this.__setWaterAtom();
        this.__setEmptyEnPlusValue();
        this.__setEmptydampType();
        this.__calculateMpeoeCharge();
        this.itsMolecule.setProperty(MpeoeChargeCalculator.IS_CALCULATED, true);
    }

    public void inputMpeoeCharge(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        RingPerception theRingPerception = new RingPerception();
        
        this.inputMpeoeCharge(theTotalMolecule, theUnitMolecule, theTopologicalDistanceMatrix, theRingPerception.recognizeAromaticRing(theUnitMolecule, theTopologicalDistanceMatrix));
    }
    
    public void inputMpeoeCharge(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule, 
            TopologicalDistanceMatrix theTopologicalDistanceMatrix, IRingSet theRingSet) {
        this.itsMolecule = theUnitMolecule;
        MpeoeAtomTypeGenerator theAtomType = new MpeoeAtomTypeGenerator();

        theAtomType.inputMpeoeAtomType(this.itsMolecule, theTopologicalDistanceMatrix, theRingSet);

        this.__setWaterAtom();
        this.__setEmptyEnPlusValue();
        this.__setEmptydampType();
        this.__calculateMpeoeCharge();

        for (int ai = 0, aEnd = theTotalMolecule.getAtomCount(); ai < aEnd; ai++) {
            theTotalMolecule.getAtom(ai).setProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY,
                    this.itsMolecule.getAtom(ai % this.itsMolecule.getAtomCount()).getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY));
            theTotalMolecule.getAtom(ai).setProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY,
                    this.itsMolecule.getAtom(ai % this.itsMolecule.getAtomCount()).getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY));
            theTotalMolecule.getAtom(ai).setProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY,
                    this.itsMolecule.getAtom(ai % this.itsMolecule.getAtomCount()).getProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY));
            theTotalMolecule.getAtom(ai).setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE,
                    this.itsMolecule.getAtom(ai % this.itsMolecule.getAtomCount()).getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE));
            theTotalMolecule.getAtom(ai).setProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY,
                    this.itsMolecule.getAtom(ai % this.itsMolecule.getAtomCount()).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY));
            theTotalMolecule.getAtom(ai).setProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY,
                    this.itsMolecule.getAtom(ai % this.itsMolecule.getAtomCount()).getProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY));
        }

        theTotalMolecule.setProperty(MpeoeChargeCalculator.IS_CALCULATED, true);
    }

    public void inputMpeoeCharge(IAtomContainer theMolecule) {
        this.inputMpeoeCharge(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }
    
    public void inputMpeoeCharge(Protein theProtein) {
        RingPerception theRingPerception = new RingPerception();
        TopologicalDistanceMatrix theTopologicalDistanceMatrix = new TopologicalDistanceMatrix(theProtein);
        IRingSet theRingSet = theRingPerception.recognizeRing(theProtein);
        
        this.inputMpeoeCharge(theProtein.getMolecule(), theTopologicalDistanceMatrix, theRingSet);
    }

    private void __setEmptyEnPlusValue() {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            if (!theAtom.getProperties().containsKey(MpeoeAtomTypeGenerator.EN_PULS_KEY)) {
                Double theEnPlusValue = this.itsParameterList.getMpeoeEnPlusCoefficientMap().get((Integer) theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)).getA()
                        + this.itsParameterList.getMpeoeEnPlusCoefficientMap().get((Integer) theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)).getB();
                theAtom.setProperty(MpeoeAtomTypeGenerator.EN_PULS_KEY, theEnPlusValue);
            }
        }
    }

    private void __setEmptydampType() {
        for (IBond theBond : this.getMolecule().bonds()) {
            this.__assignDampType(theBond);
        }
    }

    private void __assignDampType(IBond theBond) {
        if (this.__isAtomDampName(theBond, this.N_SYMBOL + this.POSITIVE)) {
            this.__assignDampTypeByPositiveNitrogen(theBond);
        } else if (this.__isAtomDampName(theBond, this.HALOGEN_SYMBOL)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f14");
        } else if (this.__isAtomDampName(theBond, this.SI_SYMBOL)) {
            this.__assignDampTypeBySilicon(theBond);
        } else if (this.__isAtomDampType(theBond, this.AROMATIC_TYPE)) {
            this.__assignDampTypeByAromaticType(theBond);
        } else if (this.__isMpeoeAtomType(theBond, 123, 133)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f14");
        } else if (this.__isMatchedDampTypeAndDampName(theBond, this.H_SYMBOL, this.SP3_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f1");
        } else if (this.__isMatchedDampTypeAndDampName(theBond, this.H_SYMBOL, this.SP2_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f2");
        } else if (this.__isAtomSameType(theBond, this.SP3_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f3");
        } else if (this.__isMatchedDampTypeSet(theBond, this.SP2_TYPE, this.SP3_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f4");
        } else if (this.__isAtomSameType(theBond, this.SP2_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f5");
        } else if (this.__isMatchedDampTypeAndDampName(theBond, this.H_SYMBOL, this.SP_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f21");
        } else if (this.__isAtomSameType(theBond, this.SP_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f18");
        } else if (this.__isMatchedDampTypeSet(theBond, this.SP_TYPE, this.SP2_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f19");
        } else if (this.__isMatchedDampTypeSet(theBond, this.SP_TYPE, this.SP3_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f20");
        } else if (this.__isChargedDampTypeSet(theBond)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f15");
        } else {
            System.out.println(this.getMolecule().getAtomNumber(theBond.getAtom(this.FIRST_INDEX)) + " " + this.getMolecule().getAtomNumber(theBond.getAtom(this.SECOND_INDEX)));
            System.out.println(theBond.getAtom(this.SECOND_INDEX).getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY).toString());
        }
    }

    private void __assignDampTypeByAromaticType(IBond theBond) {
        if (this.__isAtomSameType(theBond, this.AROMATIC_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f11");
        } else if (this.__isAtomDampName(theBond, this.H_SYMBOL)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f12");
        } else {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f13");
        }
    }

    private void __assignDampTypeBySilicon(IBond theBond) {
        if (this.__isAtomDampName(theBond, this.H_SYMBOL)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f16");
        } else if (this.__isAtomDampType(theBond, this.SP3_TYPE)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f17");
        } else {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "Error");
        }
    }

    private void __assignDampTypeByPositiveNitrogen(IBond theBond) {
        if (this.__isAtomDampName(theBond, this.H_SYMBOL)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f6");
        } else if (this.__isAtomDampName(theBond, this.C_SYMBOL)) {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f7");
        } else {
            theBond.setProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY, "f15");
        }
    }

    private Boolean __isAtomDampName(IBond theBond, String theDampName) {
        if (!theBond.getAtom(this.FIRST_INDEX).getProperties().containsKey(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY)
                || !theBond.getAtom(this.SECOND_INDEX).getProperties().containsKey(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY)) {
            return false;
        }

        String[] theSplitedDampStringInFirstAtom = theBond.getAtom(this.FIRST_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);
        String[] theSplitedDampStringInSecondAtom = theBond.getAtom(this.SECOND_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);

        if (theSplitedDampStringInFirstAtom[this.DAMP_NAME_INDEX].equals(theDampName) || theSplitedDampStringInSecondAtom[this.DAMP_NAME_INDEX].equals(theDampName)) {
            return true;
        }

        return false;
    }

    private Boolean __isAtomDampType(IBond theBond, String theDampType) {
        String[] theSplitedDampStringInFirstAtom = theBond.getAtom(this.FIRST_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);
        String[] theSplitedDampStringInSecondAtom = theBond.getAtom(this.SECOND_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);

        if (theSplitedDampStringInFirstAtom[this.DAMP_TYPE_INDEX].equals(theDampType) || theSplitedDampStringInSecondAtom[this.DAMP_TYPE_INDEX].equals(theDampType)) {
            return true;
        }

        return false;
    }

    private Boolean __isAtomSameType(IBond theBond, String theDampType) {
        String[] theSplitedDampStringInFirstAtom = theBond.getAtom(this.FIRST_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);
        String[] theSplitedDampStringInSecondAtom = theBond.getAtom(this.SECOND_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);

        if (!theSplitedDampStringInFirstAtom[this.DAMP_TYPE_INDEX].equals(theDampType)) {
            return false;
        }

        return theSplitedDampStringInFirstAtom[this.DAMP_TYPE_INDEX].equals(theSplitedDampStringInSecondAtom[this.DAMP_TYPE_INDEX]);
    }

    private Boolean __isMpeoeAtomType(IBond theBond, Integer theFirstAtomtype, Integer theSecondAtomType) {
        IAtom theFirstAtom = theBond.getAtom(this.FIRST_INDEX);
        IAtom theSecondAtom = theBond.getAtom(this.SECOND_INDEX);

        if (theFirstAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY).equals(theFirstAtomtype)
                && theSecondAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY).equals(theSecondAtomType)) {
            return true;
        } else if (theFirstAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY).equals(theSecondAtomType)
                && theSecondAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY).equals(theFirstAtomtype)) {
            return true;
        }

        return false;
    }

    private Boolean __isMatchedDampTypeAndDampName(IBond theBond, String theDampName, String theDampType) {
        String[] theSplitedDampStringInFirstAtom = theBond.getAtom(this.FIRST_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);
        String[] theSplitedDampStringInSecondAtom = theBond.getAtom(this.SECOND_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);

        if (theSplitedDampStringInFirstAtom[this.DAMP_NAME_INDEX].equals(theDampName) && theSplitedDampStringInSecondAtom[this.DAMP_TYPE_INDEX].equals(theDampType)) {
            return true;
        } else if (theSplitedDampStringInFirstAtom[this.DAMP_TYPE_INDEX].equals(theDampType) && theSplitedDampStringInSecondAtom[this.DAMP_NAME_INDEX].equals(theDampName)) {
            return true;
        }

        return false;
    }

    private Boolean __isMatchedDampTypeSet(IBond theBond, String theFirstType, String theSecondType) {
        String[] theSplitedDampStringInFirstAtom = theBond.getAtom(this.FIRST_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);
        String[] theSplitedDampStringInSecondAtom = theBond.getAtom(this.SECOND_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);

        if (theSplitedDampStringInFirstAtom[this.DAMP_TYPE_INDEX].equals(theFirstType) && theSplitedDampStringInSecondAtom[this.DAMP_TYPE_INDEX].equals(theSecondType)) {
            return true;
        } else if (theSplitedDampStringInFirstAtom[this.DAMP_TYPE_INDEX].equals(theSecondType) && theSplitedDampStringInSecondAtom[this.DAMP_TYPE_INDEX].equals(theFirstType)) {
            return true;
        }

        return false;
    }

    private boolean __isChargedDampTypeSet(IBond theBond) {
        String[] theSplitedDampStringInFirstAtom = theBond.getAtom(this.FIRST_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);
        String[] theSplitedDampStringInSecondAtom = theBond.getAtom(this.SECOND_INDEX).getProperty(MpeoeAtomTypeGenerator.DAMP_ATOM_TYPE_KEY).toString().split(this.COMMA_REGEX);

        if (theSplitedDampStringInFirstAtom[this.DAMP_TYPE_INDEX].contains("+") || theSplitedDampStringInSecondAtom[this.DAMP_TYPE_INDEX].contains("+")) {
            return true;
        }

        return false;
    }

    private void __calculateMpeoeCharge() {
        Integer theNumberOfIteration = 0;

        this.__initializeMpeoeCharge();
        this.__setHistory();
        
        while (true) {
            theNumberOfIteration++;
            this.__calculateElectronegativity();
            Double theDifference = this.__calculateMpeoeCharge(theNumberOfIteration);

            this.__setHistory();
            
            if (theDifference < this.STOP_CONDITION_DQ) {
                break;
            }
        }
        
        this.itsMolecule.setProperty(MpeoeChargeCalculator.NUMBER_OF_ITERATION_KEY, theNumberOfIteration);
    }
    
    private void __setHistory() {
        List<Double> theChargeList = new ArrayList<>();
        List<Double> theElectroNegativityList = new ArrayList<>();
        
        for(IAtom theAtom : this.itsMolecule.atoms()) {
            theChargeList.add((Double)theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY));
            theElectroNegativityList.add((Double)theAtom.getProperty(MpeoeChargeCalculator.ELECTRONEGATIVITY_KEY));
        }
        
        ((TwoDimensionList)this.itsMolecule.getProperty(MpeoeChargeCalculator.CHARGE_HISTORY_KEY)).add(theChargeList);
        ((TwoDimensionList)this.itsMolecule.getProperty(MpeoeChargeCalculator.ELECTRO_NEGATIVITY_HISTORY_KEY)).add(theElectroNegativityList);
    }
    
    private void __setMolecularMpeoeCharge() {
        Double theTotalCharge = 0.0;

        for (IAtom theAtom : this.getMolecule().atoms()) {
            theTotalCharge += (Double) (theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY));
        }

        this.setMolecule().setProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY, theTotalCharge);
    }

    private void __initializeMpeoeCharge() {
        for (IAtom theAtom : this.getMolecule().atoms()) {
            theAtom.setProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY,
                    this.itsParameterList.getMpeoeEnPlusCoefficientMap().get(theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)).getInitialCharge());
            theAtom.setProperty(MpeoeChargeCalculator.ELECTRONEGATIVITY_KEY, this.INITIAL_ELECTRONEGATIVITY);
        }
        
        this.itsMolecule.setProperty(MpeoeChargeCalculator.CHARGE_HISTORY_KEY, new TwoDimensionList<>());
        this.itsMolecule.setProperty(MpeoeChargeCalculator.ELECTRO_NEGATIVITY_HISTORY_KEY, new TwoDimensionList<>());
    }

    private void __calculateElectronegativity() {
        for (IAtom theAtom : this.getMolecule().atoms()) {
            Double theElectronegativity = this.itsParameterList.getMpeoeEnPlusCoefficientMap().get(theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)).getA()
                    + this.itsParameterList.getMpeoeEnPlusCoefficientMap().get(theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)).getB()
                    * (Double) (theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY));

            theAtom.setProperty(MpeoeChargeCalculator.ELECTRONEGATIVITY_KEY, theElectronegativity);
//            System.out.println(theElectronegativity);
        }
    }

    private Double __calculateMpeoeCharge(Integer theNumberOfIteration) {
        Double theDifference = 0.0;

        this.__initializeChargeDifference();

        for (IAtom theAtom : this.getMolecule().atoms()) {
            if (!(boolean) theAtom.getProperty(this.IS_WATER_KEY)) {
                Double theValue = this.__calculateMpeoeCharge(theAtom, theNumberOfIteration);

                theDifference += theValue;
            }
        }

//        this.__setMpeoeCharge();

        return theDifference;
    }

    private void __initializeChargeDifference() {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            theAtom.setProperty(this.CHARGE_DIFFERENCE_KEY, 0.0);
        }
    }

    private void __setMpeoeCharge() {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            theAtom.setProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY,
                    (Double) (theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY)) + (Double) (theAtom.getProperty(this.CHARGE_DIFFERENCE_KEY)));
        }
    }

    private Double __calculateMpeoeCharge(IAtom theAtom, Integer theNumberOfIteration) {
        Double theDifference = 0.0;
        List<IBond> theConnectedBondList = this.getMolecule().getConnectedBondsList(theAtom);

        for (IBond theConnectedBond : theConnectedBondList) {
            IAtom theConnectedAtom = this.__getConnectedAtom(theConnectedBond, theAtom);
            try {
                Double theDf = Math.pow(this.itsParameterList.getDampValueMap().get(theConnectedBond.getProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY)), theNumberOfIteration);
                Double theDelen = (Double) (theConnectedAtom.getProperty(MpeoeChargeCalculator.ELECTRONEGATIVITY_KEY))
                        - (Double) (theAtom.getProperty(MpeoeChargeCalculator.ELECTRONEGATIVITY_KEY));
                Double theDq = 0.0;

                if (theDelen > 0.0) {
                    theDq = theDf * theDelen / (Double) (theAtom.getProperty(MpeoeAtomTypeGenerator.EN_PULS_KEY));
                }

                theAtom.setProperty(this.MPEOE_CHARGE_KEY, (Double) (theAtom.getProperty(this.MPEOE_CHARGE_KEY)) + theDq);
                theConnectedAtom.setProperty(this.MPEOE_CHARGE_KEY, (Double) (theConnectedAtom.getProperty(this.MPEOE_CHARGE_KEY)) - theDq);
                theDifference += Math.abs(theDq);
            } catch (NullPointerException ex) {
                System.out.println(this.itsParameterList.getDampValueMap().get(theConnectedBond.getProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY)) + " " + theConnectedBond.getProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY));
                System.err.println(this.itsMolecule.getAtomNumber(theConnectedBond.getAtom(0)) + " " + this.itsMolecule.getAtomNumber(theConnectedBond.getAtom(1)));
            }
        }

        return theDifference;
    }

    private IAtom __getConnectedAtom(IBond theBond, IAtom theAtom) {
        if (!theBond.getAtom(this.FIRST_INDEX).equals(theAtom)) {
            return theBond.getAtom(this.FIRST_INDEX);
        }

        return theBond.getAtom(this.SECOND_INDEX);
    }

    private void __setWaterAtom() {
        this.__initializeWaterAtom();

        for (IAtom theAtom : this.itsMolecule.atoms()) {
            if (this.__isWaterOxygen(theAtom)) {
                this.__setWaterAtom(theAtom);
            }
        }
    }

    private void __initializeWaterAtom() {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            theAtom.setProperty(this.IS_WATER_KEY, false);
        }
    }

    private void __setWaterAtom(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            theConnectedAtom.setProperty(this.IS_WATER_KEY, true);
            theConnectedAtom.setProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY, 0.417);
        }

        theAtom.setProperty(this.IS_WATER_KEY, true);
        theAtom.setProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY, -0.834);
    }

    private boolean __isWaterOxygen(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (!theConnectedAtom.getSymbol().equals(this.H_SYMBOL)) {
                return false;
            }
        }

        return theAtom.getSymbol().equals(this.O_SYMBOL);
    }
}
