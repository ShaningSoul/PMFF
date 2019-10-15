/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.tool;

import java.io.Serializable;
import java.util.List;
import org.bmdrc.basicchemistry.atdl.Atdl;
import org.bmdrc.basicchemistry.atdl.AtdlGenerator;
import org.bmdrc.basicchemistry.tool.RingPerception;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IRingSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class MM3AtomTypeGenerator implements Serializable {

    private static final long serialVersionUID = 7287210657517428092L;

    private IAtomContainer itsUnitMolecule;
    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;
    private IRingSet itsRingSet;
    private IRingSet itsAromaticRingSet;
    //constant Integer variable
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    private final int THIRD_INDEX = 2;
    private final int FOURTH_INDEX = 3;
    private final int NON_CHARGED = 0;
    private final int ZERO_INT = 0;
    private static final int ONE_ATOM_CONNECTED = 1;
    private static final int TWO_ATOM_CONNECTED = 2;
    private static final int THREE_ATOM_CONNECTED = 3;
    private static final int FOUR_ATOM_CONNECTED = 4;
    public static final int NOT_DEFINED_ATOM = 0;
    public static final int SP_CARBON = 1;
    public static final int SP2_CARBON = 2;
    public static final int SP3_CARBON = 3;
    public static final int POSITIVE_CARBON = 10;
    public static final int DOUBLE_BOND_OXYGEN = 15;
    public static final int SINGLE_BOND_OXYGEN = 16;
    public static final int NEGATIVE_OXYGEN = 18;
    public static final int SP_NITROGEN = 24;
    public static final int SP2_NITROGEN = 25;
    public static final int SP3_NITROGEN = 26;
    public static final int POSITIVE_SP2_NITROGEN = 31;
    public static final int POSITIVE_SP3_NITROGEN = 32;
    public static final int NEGATIVE_SP3_NITROGEN = 38;
    public static final int NEGATIVE_SP2_NITROGEN = 39;
    public static final int NEUTRAL_HYDROGEN = 41;
    public static final int HYDROGEN_BOUNDED_OXYGEN = 42;
    public static final int HYDROGEN_BOUNDED_NITROGEN = 43;
    public static final int POSITIVE_HYDROGEN = 44;
    public static final int NEGATIVE_HYDROGEN = 45;
    public static final int SULFUR = 49;
    public static final int PHOSPHORUS = 53;
    public static final int FLUORINE = 56;
    public static final int CHLORINE = 57;
    public static final int BROMINE = 58;
    public static final int IODINE = 59;
    public static final int SILICON = 60;
    public static final int NOT_CONTAIN_SPECIAL_STRUCTURE = 0;
    public static final int BENZENOID = 1;
    //constant String variable
    public static final String MM3_ATOM_TYPE_KEY = "MM3 Atom type";
    public static final String MM3_SPECIAL_STRUCTURE_KEY = "MM3 Special Structure";
    private static final String CARBON_SYMBOL = "C";
    private static final String HYDROGEN_SYMBOL = "H";
    private static final String NITROGEN_SYMBOL = "N";
    private static final String OXYGEN_SYMBOL = "O";
    private static final String SULFUR_SYMBOL = "S";
    private static final String PHOSPHORUS_SYMBOL = "P";
    private static final String FLUORINE_SYMBOL = "F";
    private static final String CHLORINE_SYMBOL = "Cl";
    private static final String BROMINE_SYMBOL = "Br";
    private static final String IODINE_SYMBOL = "I";
    private static final String SILICON_SYMBOL = "Si";

    public MM3AtomTypeGenerator() {
    }

    public MM3AtomTypeGenerator(IAtomContainer theMolecule) {
        this.itsUnitMolecule = theMolecule;
    }

    public IAtomContainer getMolecule() {
        return itsUnitMolecule;
    }

    public void setMolecule(IAtomContainer theMolecule) {
        this.itsUnitMolecule = theMolecule;
    }

    public IAtomContainer setMolecule() {
        return itsUnitMolecule;
    }

    public void generateAtomType(IAtomContainer theMolecule, IAtomContainer theUnitMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        if (theUnitMolecule == null) {
            this.generateAtomType(theMolecule, theTopologicalDistanceMatrix);
            return;
        } else {
            this.generateAtomType(theUnitMolecule, theTopologicalDistanceMatrix);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                theMolecule.getAtom(ai).setProperty(Atdl.ATDL_KEY, theUnitMolecule.getAtom(ai % theUnitMolecule.getAtomCount()).getProperty(Atdl.ATDL_KEY));
                theMolecule.getAtom(ai).setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY,
                        theUnitMolecule.getAtom(ai % theUnitMolecule.getAtomCount()).getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY));
                theMolecule.getAtom(ai).setProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY,
                        theUnitMolecule.getAtom(ai % theUnitMolecule.getAtomCount()).getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY));
            }

            theMolecule.setProperty(AtdlGenerator.IS_CALCULATED_ATDL, AtdlGenerator.CALCULATED_ATDL);
        }
    }
    
    public void generateAtomType(IAtomContainer theMolecule, IAtomContainer theUnitMolecule, IRingSet theRingSet) {
        RingPerception theRingPerception = new RingPerception();
        
        this.generateAtomType(theMolecule, theUnitMolecule, theRingSet, theRingPerception.recognizeAromaticRing(theMolecule, theRingSet));
    }
    
    public void generateAtomType(IAtomContainer theMolecule, IAtomContainer theUnitMolecule, IRingSet theRingSet, IRingSet theAromaticRingSet) {
        if (theUnitMolecule == null || theMolecule.getAtomCount() == theUnitMolecule.getAtomCount()) {
            this.generateAtomType(theMolecule, theRingSet, theAromaticRingSet);
            return;
        } else {
            this.generateAtomType(theUnitMolecule, theRingSet, theAromaticRingSet);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                theMolecule.getAtom(ai).setProperty(Atdl.ATDL_KEY, theUnitMolecule.getAtom(ai % theUnitMolecule.getAtomCount()).getProperty(Atdl.ATDL_KEY));
                theMolecule.getAtom(ai).setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY,
                        theUnitMolecule.getAtom(ai % theUnitMolecule.getAtomCount()).getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY));
                theMolecule.getAtom(ai).setProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY,
                        theUnitMolecule.getAtom(ai % theUnitMolecule.getAtomCount()).getProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY));
            }

            theMolecule.setProperty(AtdlGenerator.IS_CALCULATED_ATDL, AtdlGenerator.CALCULATED_ATDL);
        }
    }

    public void generateAtomType(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.itsUnitMolecule = theMolecule;

        RingPerception theRingPerception = new RingPerception();
        AtdlGenerator theGenerator = new AtdlGenerator();
        int theAtomCount = this.getMolecule().getAtomCount();

        this.itsRingSet = theRingPerception.recognizeRing(this.itsUnitMolecule, theTopologicalDistanceMatrix);
        this.itsAromaticRingSet = theRingPerception.recognizeAromaticRing(this.itsUnitMolecule, this.itsRingSet);

        if (this.getMolecule() == null) {
            System.err.println("Not contain molecule in MM3 Atom Type generator");
            return;
        }

        theGenerator.inputAtdl(this.itsUnitMolecule, theTopologicalDistanceMatrix);

        for (IAtom theAtom : this.getMolecule().atoms()) {
            this.__generateSpecialStructureType(theAtom);
            this.__generateAtomType(theAtom);
        }

        if (theAtomCount != this.getMolecule().getAtomCount()) {
            this.__removeAddingHydrogen(theAtomCount);
        }

        this.itsRingSet = null;
        this.itsAromaticRingSet = null;
    }

    public void generateAtomType(IAtomContainer theMolecule, IRingSet theRingSet, IRingSet theAromaticRingSet) {
        this.itsUnitMolecule = theMolecule;
        this.itsRingSet = theRingSet;
        this.itsAromaticRingSet = theAromaticRingSet;

        for (IAtom theAtom : this.itsUnitMolecule.atoms()) {
            if (!theAtom.getProperties().containsKey(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY) || !theAtom.getProperties().containsKey(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY)) {
                this.__generateSpecialStructureType(theAtom);
                this.__generateAtomType(theAtom);
            }
        }
    }

    public void generateAtomType(IAtomContainer theMolecule) {
        this.generateAtomType(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }

    public void generateAtomType() {
        this.generateAtomType(this.itsUnitMolecule, new TopologicalDistanceMatrix(this.itsUnitMolecule));
    }

    private void __generateSpecialStructureType(IAtom theAtom) {
        if (this.__isBezenoid(theAtom)) {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY, MM3AtomTypeGenerator.BENZENOID);
        } else {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_SPECIAL_STRUCTURE_KEY, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE);
        }
    }

    private boolean __isBezenoid(IAtom theAtom) {
        for (IAtomContainer theRing : this.itsAromaticRingSet.atomContainers()) {
            if (theRing.contains(theAtom)) {
                return true;
            }
        }

        return false;
    }

    private void __removeAddingHydrogen(int theOriginalAtomCount) {
        this.__removeAddingBond(theOriginalAtomCount);

        for (int ai = this.getMolecule().getAtomCount() - 1; ai >= theOriginalAtomCount; ai--) {
            this.getMolecule().removeAtom(ai);
        }
    }

    private void __removeAddingBond(int theOriginalAtomCount) {
        for (int bi = this.getMolecule().getBondCount() - 1; bi >= 0; bi--) {
            if (this.__isAddedBond(this.getMolecule().getBond(bi), theOriginalAtomCount)) {
                this.getMolecule().removeBond(this.getMolecule().getBond(bi));
            } else {
                return;
            }
        }
    }

    private boolean __isAddedBond(IBond theBond, int theOriginalAtomCount) {
        IAtom theFirstAtom = theBond.getAtom(this.FIRST_INDEX);
        IAtom theSecondAtom = theBond.getAtom(this.SECOND_INDEX);

        return this.getMolecule().getAtomNumber(theFirstAtom) >= theOriginalAtomCount || this.getMolecule().getAtomNumber(theSecondAtom) >= theOriginalAtomCount;
    }

    private void __generateAtomType(IAtom theAtom) {
        switch (theAtom.getSymbol()) {
            case MM3AtomTypeGenerator.CARBON_SYMBOL:
                this.__generateAtomTypeInCarbon(theAtom);
                break;
            case MM3AtomTypeGenerator.OXYGEN_SYMBOL:
                this.__generateAtomTypeInOxygen(theAtom);
                break;
            case MM3AtomTypeGenerator.NITROGEN_SYMBOL:
                this.__generateAtomTypeInNitrogen(theAtom);
                break;
            case MM3AtomTypeGenerator.HYDROGEN_SYMBOL:
                this.__generateAtomTypeInHydrogen(theAtom);
                break;
            case MM3AtomTypeGenerator.SULFUR_SYMBOL:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SULFUR);
                break;
            case MM3AtomTypeGenerator.PHOSPHORUS_SYMBOL:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.PHOSPHORUS);
                break;
            case MM3AtomTypeGenerator.FLUORINE_SYMBOL:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.FLUORINE);
                break;
            case MM3AtomTypeGenerator.CHLORINE_SYMBOL:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.CHLORINE);
                break;
            case MM3AtomTypeGenerator.BROMINE_SYMBOL:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.BROMINE);
                break;
            case MM3AtomTypeGenerator.IODINE_SYMBOL:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.IODINE);
                break;
            case MM3AtomTypeGenerator.SILICON_SYMBOL:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SILICON);
                break;
            default:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.NOT_DEFINED_ATOM);
        }
    }

    private void __generateAtomTypeInHydrogen(IAtom theAtom) {
        IAtom theConnectedAtom = this.itsUnitMolecule.getConnectedAtomsList(theAtom).get(this.FIRST_INDEX);
        
        if (theAtom.getFormalCharge().intValue() > 0) {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.POSITIVE_HYDROGEN);
        } else if (theAtom.getFormalCharge().intValue() < 0) {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.NEGATIVE_HYDROGEN);
        } else {
            this.__generateAtomTypeInNonChargedHydrogen(theAtom);
        }
    }

    private void __generateAtomTypeInNonChargedHydrogen(IAtom theAtom) {
        IAtom theConnectedAtom = this.getMolecule().getConnectedAtomsList(theAtom).get(this.FIRST_INDEX);

        switch (theConnectedAtom.getSymbol()) {
            case MM3AtomTypeGenerator.OXYGEN_SYMBOL:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
                break;
            case MM3AtomTypeGenerator.NITROGEN_SYMBOL:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
                break;
            default:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        }
    }

    private void __generateAtomTypeInNitrogen(IAtom theAtom) {
        Integer theConnectedAtomCount = this.getMolecule().getConnectedAtomsCount(theAtom);

        switch (theConnectedAtomCount) {
            case MM3AtomTypeGenerator.ONE_ATOM_CONNECTED:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SP_NITROGEN);
                break;
            case MM3AtomTypeGenerator.TWO_ATOM_CONNECTED:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SP2_NITROGEN);
                break;
            case MM3AtomTypeGenerator.THREE_ATOM_CONNECTED:
                this.__generateAtomTypeInNitrogenConnectedThreeAtom(theAtom);
                break;
            case MM3AtomTypeGenerator.FOUR_ATOM_CONNECTED:
                this.__generateAtomTypeInPositiveNitrogen(theAtom);
                break;
            default:
                System.err.println(this.getMolecule().getAtomNumber(theAtom) + "th atom error!!");
        }
    }

    private void __generateAtomTypeInNitrogenConnectedThreeAtom(IAtom theAtom) {
        List<IBond> theConnectedBondList = this.itsUnitMolecule.getConnectedBondsList(theAtom);

        for (IBond theBond : theConnectedBondList) {
            if (theBond.getOrder().equals(Order.DOUBLE)) {
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN);
                return;
            }
        }

        theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SP3_NITROGEN);
    }

    private void __generateAtomTypeInPositiveNitrogen(IAtom theAtom) {
        List<IBond> theConnectedBondList = this.getMolecule().getConnectedBondsList(theAtom);

        theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN);
    }

    private void __generateAtomTypeInOxygen(IAtom theAtom) {
        Integer theConnectedAtomCount = this.getMolecule().getConnectedAtomsCount(theAtom);

        switch (theConnectedAtomCount) {
            case MM3AtomTypeGenerator.ONE_ATOM_CONNECTED:
                this.__generateAtomTypeInOxygenConnectedOneAtom(theAtom);
                break;
            case MM3AtomTypeGenerator.TWO_ATOM_CONNECTED:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
                break;
            default:
                theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
//                System.err.println(this.getMolecule().getAtomNumber(theAtom) + "th atom error!!");
        }
    }

    private void __generateAtomTypeInOxygenConnectedOneAtom(IAtom theAtom) {
        IAtom theConnectedAtom = this.itsUnitMolecule.getConnectedAtomsList(theAtom).get(this.FIRST_INDEX);

        if (this.itsUnitMolecule.getBond(theAtom, theConnectedAtom).getOrder().equals(Order.DOUBLE)) {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        } else if (this.itsUnitMolecule.getBond(theAtom, theConnectedAtom).getOrder().equals(Order.SINGLE)) {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        }
    }

    private void __generateAtomTypeInCarbon(IAtom theAtom) {
        if (theAtom.getFormalCharge() != null && theAtom.getFormalCharge() != 0) {
            this.__generateAtomTypeInChargedCarbon(theAtom);
        } else {
            this.__generateAtomTypeInNonChargedCarbon(theAtom);
        }
    }

    private void __generateAtomTypeInChargedCarbon(IAtom theAtom) {
        if (theAtom.getFormalCharge() > this.NON_CHARGED) {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.POSITIVE_CARBON);
            return;
        }

        theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.NOT_DEFINED_ATOM);
    }

    private void __generateAtomTypeInNonChargedCarbon(IAtom theAtom) {
        Integer theConnectedAtomCount = this.getMolecule().getConnectedAtomsCount(theAtom);

        if (this.__isSPCarbon(theAtom)) {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SP_CARBON);
        } else if (this.__isSP2Carbon(theAtom)) {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SP2_CARBON);
        } else {
            theAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, MM3AtomTypeGenerator.SP3_CARBON);
        }
    }

    private boolean __isSPCarbon(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsUnitMolecule.getConnectedAtomsList(theAtom);

        if (theConnectedAtomList.size() != MM3AtomTypeGenerator.TWO_ATOM_CONNECTED) {
            return false;
        } else if (this.__getNumberOfBond(theAtom, Order.DOUBLE) == MM3AtomTypeGenerator.TWO_ATOM_CONNECTED) {
            return true;
        } else if (this.__getNumberOfBond(theAtom, Order.TRIPLE) == MM3AtomTypeGenerator.ONE_ATOM_CONNECTED) {
            return true;
        }

        return false;
    }

    private boolean __isSP2Carbon(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsUnitMolecule.getConnectedAtomsList(theAtom);

        if (theConnectedAtomList.size() != MM3AtomTypeGenerator.THREE_ATOM_CONNECTED) {
            return false;
        } else if (this.__getNumberOfBond(theAtom, Order.DOUBLE) == MM3AtomTypeGenerator.ONE_ATOM_CONNECTED) {
            return true;
        }

        return false;
    }

    private int __getNumberOfBond(IAtom theAtom, Order theOrder) {
        List<IAtom> theConnectedAtomList = this.itsUnitMolecule.getConnectedAtomsList(theAtom);
        int theNumberOfDoubleBond = 0;

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (this.itsUnitMolecule.getBond(theAtom, theConnectedAtom).getOrder().equals(theOrder)) {
                theNumberOfDoubleBond++;
            }
        }

        return theNumberOfDoubleBond;
    }
}
