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
import java.util.List;
import org.bmdrc.basicchemistry.atdl.Atdl;
import org.bmdrc.basicchemistry.atdl.AtdlGenerator;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Atom Type Description <br>
 * ---------Carbon Atom Type-------------- <br>
 * 101 : Carbonyl carbon in carboxylic group <br>
 * 102 : Carbonyl carbon in amide <br>
 * 103 : Carbon bounded primary amine <br>
 * 104 : Carbon bounded secondary amine <br>
 * 105 : Carbon bounded alcohol <br>
 * 106 : Carbon in carboxylate ion <br>
 * ---------Hydrogen Atom Type-------------- <br>
 * 201 : Amide Hydrogen <br>
 * 202 : Hydrogen in CO2H <br>
 * 203 : Hydrogen in other alcohol <br>
 * 204 : Hydrogen in primary amine <br>
 * 205 : Hydrogen in secondary amine <br>
 * 206 : Hydrogen in N+ <br>
 * ---------Oxygen Atom Type-------------- <br>
 * 301 : Carbonyl oxygen in carboxylic group <br>
 * 302 : Carbonyl oxygen in amide <br>
 * 303 : sp3 oxygen in CO2H <br>
 * 304 : Oxygen in other alcohol <br>
 * 305 : Oxygen in carboxylate ion <br>
 * ---------Nitrogen Atom Type-------------- <br>
 * 401 : Nitrogen in amide <br>
 * 402 : Nitrogen in praimary amine <br>
 * 403 : Nitrogen in secondary amine <br>
 * 404 : Nitrogen in Ammonium ion <br>
 * ---------Other Atom Type-------------- <br>
 * 0 : No matched atom type <br>
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class HydrogenBondAtomTypeGenerator implements Serializable {

    private static final long serialVersionUID = 247425573142101594L;

    private IAtomContainer itsMolecule;
    private List<Integer> itsUsefulAtomicNumberList;
    //constant String variable
    public static final String HYDROGEN_BOND_TYPE_KEY = "Hydrogen Bond Type";
    //constant Integer variable
    private static final int HYDROGEN_ATOMIC_NUMBER = 1;
    private static final int CARBON_ATOMIC_NUMBER = 6;
    private static final int NITROGEN_ATOMIC_NUMBER = 7;
    private static final int OXYGEN_ATOMIC_NUMBER = 8;
    private final int FIRST_INDEX = 0;
    //Carbon Atom Type
    public static final int CARBONYL_CARBON_IN_CARBOXYLIC = 101;
    public static final int CARBONYL_CARBON_IN_AMIDE = 102;
    public static final int CARBON_BOUNDED_AMINE = 103;
    public static final int CARBON_BOUNDED_ALCHOL = 104;
    public static final int CARBON_IN_CARBOXYLATE_ION = 105;
    public static final int CARBON_IN_AMMONIUM_ION = 106;
    //Hydrogen Atom Type
    public static final int HYDROGEN_IN_AMIDE = 201;
    public static final int HYDROGEN_IN_CO2H = 202;
    public static final int HYDROGEN_IN_OTHER_ALCHOL = 203;
    public static final int HYDROGEN_IN_PRIMARY_AMINE = 204;
    public static final int HYDROGEN_IN_SECONDARY_AMINE = 205;
    public static final int Hydrogen_in_N_PLUS = 206;
    //Oxygen Atom Type
    public static final int CARBONYL_OXYGEN_IN_CARBOXYLIC = 301;
    public static final int CARBONYL_OXYGEN_IN_AMIDE = 302;
    public static final int SP3_OXYGEN_IN_CO2H = 303;
    public static final int OXYGEN_IN_OTHER_ALCHOL = 304;
    public static final int OXYGEN_IN_CARBOXYLATE_ION = 305;
    //Nitrogen Atom Type
    public static final int NITROGEN_IN_AMIDE = 401;
    public static final int NITROGEN_IN_PRIMARY_AMINE = 402;
    public static final int NITROGEN_IN_SECONDARY_AMINE = 403;
    public static final int NITROGEN_IN_AMMONIUM_ION = 404;
    //Other Atom Type
    public static final int NO_HYDROGEN_ATOM_TYPE = 0;

    public HydrogenBondAtomTypeGenerator() {
        this.__generateUsefulAtomicNumberList();
    }

    public HydrogenBondAtomTypeGenerator(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
        this.__generateUsefulAtomicNumberList();
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

    private void __setUsefulAtomicNumberList(List<Integer> theUsefulAtomicNumberList) {
        this.itsUsefulAtomicNumberList = theUsefulAtomicNumberList;
    }

    private List<Integer> __setUsefulAtomicNumberList() {
        return itsUsefulAtomicNumberList;
    }

    public void inputHydrogenBondAtomType(IAtomContainer theMolecule) {
        this.inputHydrogenBondAtomType(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }

    public void inputHydrogenBondAtomType(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistancematrix) {
        this.itsMolecule = theMolecule;

        if (!this.itsMolecule.getProperties().containsKey(AtdlGenerator.IS_CALCULATED_ATDL) || !this.itsMolecule.getProperty(AtdlGenerator.IS_CALCULATED_ATDL).equals(AtdlGenerator.CALCULATED_ATDL)) {
            AtdlGenerator theGenerator = new AtdlGenerator();

            theGenerator.inputAtdl(this.itsMolecule, theTopologicalDistancematrix);
        }

        for (IAtom theAtom : this.getMolecule().atoms()) {
            this.__inputHydrogenBondAtomType(theAtom);
        }
    }

    private void __generateUsefulAtomicNumberList() {
        this.__setUsefulAtomicNumberList(new ArrayList<Integer>());

        this.__setUsefulAtomicNumberList().add(AtomInformation.Hydrogen.ATOM_NUMBER);
        this.__setUsefulAtomicNumberList().add(HydrogenBondAtomTypeGenerator.CARBON_ATOMIC_NUMBER);
        this.__setUsefulAtomicNumberList().add(HydrogenBondAtomTypeGenerator.NITROGEN_ATOMIC_NUMBER);
        this.__setUsefulAtomicNumberList().add(HydrogenBondAtomTypeGenerator.OXYGEN_ATOMIC_NUMBER);
    }

    private void __inputHydrogenBondAtomType(IAtom theAtom) {
        switch (theAtom.getAtomicNumber()) {
            case HydrogenBondAtomTypeGenerator.HYDROGEN_ATOMIC_NUMBER:
                this.__inputHydrogenBondAtomTypeInHydrogen(theAtom);
                break;
            case HydrogenBondAtomTypeGenerator.CARBON_ATOMIC_NUMBER:
                this.__inputHydrogenBondAtomTypeInCarbon(theAtom);
                break;
            case HydrogenBondAtomTypeGenerator.NITROGEN_ATOMIC_NUMBER:
                this.__inputHydrogenBondAtomTypeInNitrogen(theAtom);
                break;
            case HydrogenBondAtomTypeGenerator.OXYGEN_ATOMIC_NUMBER:
                this.__inputHydrogenBondAtomTypeInOxygen(theAtom);
                break;
            default:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NO_HYDROGEN_ATOM_TYPE);
        }

        if (!theAtom.getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY).equals(HydrogenBondAtomTypeGenerator.NO_HYDROGEN_ATOM_TYPE)) {

        }
    }

    private void __inputHydrogenBondAtomTypeInHydrogen(IAtom theAtom) {
        if (this.__isHydrogenInAmide(theAtom)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        } else if (this.__isHydrogenInCarboxylAcid(theAtom)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        } else if (this.__isHydrogenInAlcohol(theAtom)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        } else if (this.__isHydrogenInAmine(theAtom)) {
            this.__setHydrogenBondAtomTypeInHydrogenInAmine(theAtom);
        } else {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NO_HYDROGEN_ATOM_TYPE);
        }
    }

    private boolean __isHydrogenInAmine(IAtom theAtom) {
        IAtom theConnectedAtom = this.itsMolecule.getConnectedAtomsList(theAtom).get(this.FIRST_INDEX);

        return theConnectedAtom.getAtomicNumber() == HydrogenBondAtomTypeGenerator.NITROGEN_ATOMIC_NUMBER;
    }

    private void __setHydrogenBondAtomTypeInHydrogenInAmine(IAtom theAtom) {
        IAtom theConnectedAtom = this.itsMolecule.getConnectedAtomsList(theAtom).get(this.FIRST_INDEX);
        int theNumberOfBoundedHydrogen = this.__getBoundedHydrogenCount(theConnectedAtom);

        if(theConnectedAtom.getFormalCharge().equals(1)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
            return;
        }

        switch (theNumberOfBoundedHydrogen) {
            case 1:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
                break;
            case 2:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
                break;
            case 3:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.Hydrogen_in_N_PLUS);
            default:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NO_HYDROGEN_ATOM_TYPE);
        }
    }

    private int __getBoundedHydrogenCount(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);
        int theNumberOfHydrogen = 0;

        for (IAtom theConnectedAtomInNitrogen : theConnectedAtomList) {
            if (theConnectedAtomInNitrogen.getAtomicNumber() == HydrogenBondAtomTypeGenerator.HYDROGEN_ATOMIC_NUMBER) {
                theNumberOfHydrogen++;
            }
        }

        return theNumberOfHydrogen;
    }

    private boolean __isHydrogenInAlcohol(IAtom theAtom) {
        IAtom theConnectedAtom = this.getMolecule().getConnectedAtomsList(theAtom).get(this.FIRST_INDEX);

        if (theConnectedAtom.getAtomicNumber() == HydrogenBondAtomTypeGenerator.OXYGEN_ATOMIC_NUMBER) {
            return true;
        }

        return false;
    }

    private boolean __isHydrogenInAmide(IAtom theAtom) {
        try {
            IAtom theConnectedAtom = this.itsMolecule.getConnectedAtomsList(theAtom).get(this.FIRST_INDEX);

            if (theConnectedAtom.getAtomicNumber() != HydrogenBondAtomTypeGenerator.NITROGEN_ATOMIC_NUMBER) {
                return false;
            }

            return this.__isNitrogenInAmideAtHydrogenCase(theConnectedAtom);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(this.itsMolecule.getAtomNumber(theAtom));
            ex.printStackTrace();
        }

        return false;
    }

    private boolean __isNitrogenInAmideAtHydrogenCase(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (this.__isCarbonInAmideAtHydrogenCase(theConnectedAtom)) {
                return true;
            }
        }

        return false;
    }

    private boolean __isCarbonInAmideAtHydrogenCase(IAtom theAtom) {
        Atdl theAtomAtdl = (Atdl) theAtom.getProperty(Atdl.ATDL_KEY);

        if (theAtom.getAtomicNumber() != HydrogenBondAtomTypeGenerator.CARBON_ATOMIC_NUMBER) {
            return false;
        } else if (!theAtomAtdl.getNumberOfConnectedAtom().equals(Atdl.THREE_ATOM_CONNECTED)) {
            return false;
        } else if (!this.__isConnectedOxygenUsingDoubleBond(theAtom)) {
            return false;
        }

        return true;
    }

    private boolean __isConnectedOxygenUsingDoubleBond(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            Order theBondOrder = this.getMolecule().getBond(theAtom, theConnectedAtom).getOrder();

            if (theConnectedAtom.getAtomicNumber() == HydrogenBondAtomTypeGenerator.OXYGEN_ATOMIC_NUMBER && theBondOrder.equals(Order.DOUBLE)) {
                return true;
            }
        }

        return false;
    }

    private boolean __isHydrogenInCarboxylAcid(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);
        IAtom theConnectedAtom = this.getMolecule().getConnectedAtomsList(theAtom).get(this.FIRST_INDEX);

        if (theConnectedAtom.getAtomicNumber() != HydrogenBondAtomTypeGenerator.OXYGEN_ATOMIC_NUMBER) {
            return false;
        } else if (!this.__isOxygenInAlcholInCarboxylAcid(theConnectedAtom)) {
            return false;
        }

        return true;
    }

    private boolean __isOxygenInAlcholInCarboxylAcid(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (this.__isConnectedOxygenUsingDoubleBond(theConnectedAtom)) {
                return true;
            }
        }

        return false;
    }

    private void __inputHydrogenBondAtomTypeInCarbon(IAtom theAtom) {
        IAtom theTargetAtom = null;

        if (this.__isCarbonBoundedTwoOxygen(theAtom)) {
            if (this.__isBoundedChargedOxygen(theAtom)) {
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.CARBON_IN_CARBOXYLATE_ION);
            } else {
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_CARBOXYLIC);
            }
        } else if (this.__isCarbonylCarbonInAmide(theAtom)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE);
        } else if ((theTargetAtom = this.__getCarbonBoundedAmine(theAtom)) != null) {
            this.__setHydrogenBondAtomTypeForCarbonBoundedAmine(theTargetAtom, theAtom);
        } else if (this.__isConnectedAlcholOxygen(theAtom)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_ALCHOL);
        } else {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NO_HYDROGEN_ATOM_TYPE);
        }
    }

    private boolean __isConnectedAlcholOxygen(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);

        for(IAtom theConnectedAtom : theConnectedAtomList) {
            if(this.__isAlcholOxygen(theConnectedAtom, theAtom)) {
                return true;
            }
        }

        return false;
    }

    private boolean __isAlcholOxygen(IAtom theConnectedAtom, IAtom theAtom) {
        if(!theConnectedAtom.getAtomicNumber().equals(AtomInformation.Oxygen.ATOM_NUMBER)) {
            return false;
        } else if(!this.itsMolecule.getBond(theAtom, theConnectedAtom).getOrder().equals(Order.SINGLE)) {
            return false;
        }

        List<IAtom> theSecondConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theConnectedAtom);

        if(theSecondConnectedAtomList.size() != 2) {
            return false;
        }

        for(IAtom theSecondConnectedAtom : theSecondConnectedAtomList) {
            if(!theSecondConnectedAtom.equals(theAtom)) {
                return theSecondConnectedAtom.getAtomicNumber().equals(AtomInformation.Hydrogen.ATOM_NUMBER);
            }
        }

        return false;
    }

    private boolean __isBoundedChargedOxygen(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getAtomicNumber().equals(AtomInformation.Oxygen.ATOM_NUMBER) && theConnectedAtom.getFormalCharge().equals(1)) {
                return true;
            }
        }

        return false;
    }

    private void __setHydrogenBondAtomTypeForCarbonBoundedAmine(IAtom theTargetAtom, IAtom theAtom) {
        int theConnectedAtomCount = this.__getBoundedHydrogenCount(theTargetAtom);

        switch (theConnectedAtomCount) {
            case 1:
            case 2:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE);
                break;
            default:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NO_HYDROGEN_ATOM_TYPE);
        }
    }

    private boolean __isCarbonBoundedTwoOxygen(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);
        Atdl theAtomAtdl = (Atdl) theAtom.getProperty(Atdl.ATDL_KEY);
        boolean theHasOxygenUsingDoubleBond = false;
        int theNumberOfOxygen = 0;

        if (!theAtomAtdl.getNumberOfConnectedAtom().equals(Atdl.THREE_ATOM_CONNECTED)) {
            return false;
        }

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            Order theBondOrder = this.getMolecule().getBond(theAtom, theConnectedAtom).getOrder();

            if (theConnectedAtom.getAtomicNumber() == HydrogenBondAtomTypeGenerator.OXYGEN_ATOMIC_NUMBER) {
                theNumberOfOxygen++;

                if (theBondOrder.equals(Order.DOUBLE)) {
                    theHasOxygenUsingDoubleBond = true;
                }
            }
        }

        return (theNumberOfOxygen == 2) && theHasOxygenUsingDoubleBond;
    }

    private boolean __isCarbonylCarbonInAmide(IAtom theAtom) {
        return this.__isConnectedOxygenUsingDoubleBond(theAtom) && this.__getCarbonBoundedAmine(theAtom) != null;
    }

    private IAtom __getCarbonBoundedAmine(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (this.__isNitrogenInAmideInCarbonCase(theConnectedAtom)) {
                return theConnectedAtom;
            }
        }

        return null;
    }

    private boolean __isNitrogenInAmideInCarbonCase(IAtom theAtom) {
        Atdl theAtomAtdl = (Atdl) theAtom.getProperty(Atdl.ATDL_KEY);

        if (theAtom.getAtomicNumber() != HydrogenBondAtomTypeGenerator.NITROGEN_ATOMIC_NUMBER) {
            return false;
        } else if (!theAtomAtdl.getNumberOfConnectedAtom().equals(Atdl.THREE_ATOM_CONNECTED)) {
            return false;
        } else if (!this.__isConnectedHydrogen(theAtom)) {
            return false;
        }

        return true;
    }

    private boolean __isConnectedHydrogen(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getAtomicNumber() == HydrogenBondAtomTypeGenerator.HYDROGEN_ATOMIC_NUMBER) {
                return true;
            }
        }

        return false;
    }

    private void __inputHydrogenBondAtomTypeInNitrogen(IAtom theAtom) {
        if (theAtom.getFormalCharge().equals(1)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMMONIUM_ION);
        } else if (this.__isNitrogenInAmide(theAtom)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE);
        } else {
            this.__setHydrogenBondAtomTypeInNitrogenInAmine(theAtom);
        }
    }

    private void __setHydrogenBondAtomTypeInNitrogenInAmine(IAtom theAtom) {
        int theNumberOfBoundedHydrogen = this.__getBoundedHydrogenCount(theAtom);

        switch (theNumberOfBoundedHydrogen) {
            case 1:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE);
                break;
            case 2:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NITROGEN_IN_PRIMARY_AMINE);
                break;
            default:
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NO_HYDROGEN_ATOM_TYPE);
        }
    }

    private boolean __isNitrogenInAmide(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);
        Atdl theAtomAtdl = (Atdl) theAtom.getProperty(Atdl.ATDL_KEY);

        if (!theAtomAtdl.getNumberOfConnectedAtom().equals(Atdl.THREE_ATOM_CONNECTED)) {
            return false;
        }

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (this.__isCarbonylCarbonInAmide(theConnectedAtom)) {
                return true;
            }
        }

        return false;
    }

    private void __inputHydrogenBondAtomTypeInOxygen(IAtom theAtom) {
        if (this.__isOxygenInCarboxyl(theAtom)) {
            this.__inputHydrogenBondAtomTypeInOxygenAtCarboxyl(theAtom);
        } else if (this.__isOxygenInAmide(theAtom)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_AMIDE);
        } else if (this.__isOxygenInAlcohol(theAtom)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.OXYGEN_IN_OTHER_ALCHOL);
        } else {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.NO_HYDROGEN_ATOM_TYPE);
        }
    }

    private boolean __isOxygenInAlcohol(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);

        if (theConnectedAtomList.size() != Atdl.TWO_ATOM_CONNECTED) {
            return false;
        }

        return this.__containAtomType(theConnectedAtomList, HydrogenBondAtomTypeGenerator.HYDROGEN_ATOMIC_NUMBER)
                && this.__containAtomType(theConnectedAtomList, HydrogenBondAtomTypeGenerator.CARBON_ATOMIC_NUMBER);
    }

    private boolean __containAtomType(List<IAtom> theAtomList, int theAtomicNumber) {
        for (IAtom theAtom : theAtomList) {
            if (theAtom.getAtomicNumber() == theAtomicNumber) {
                return true;
            }
        }

        return false;
    }

    private boolean __isOxygenInCarboxyl(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (this.__isCarbonBoundedTwoOxygen(theConnectedAtom)) {
                return true;
            }
        }

        return false;
    }

    private void __inputHydrogenBondAtomTypeInOxygenAtCarboxyl(IAtom theAtom) {
        Atdl theAtomAtdl = (Atdl) theAtom.getProperty(Atdl.ATDL_KEY);

        if (this.__isOxygenInCarboxyl(theAtom)) {
            if (this.__isOxygenInCarboxylateIon(theAtom)) {
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.OXYGEN_IN_CARBOXYLATE_ION);
            } else {
                theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.CARBONYL_OXYGEN_IN_CARBOXYLIC);
            }
        } else if (theAtomAtdl.getNumberOfConnectedAtom().equals(Atdl.TWO_ATOM_CONNECTED)) {
            theAtom.setProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY, HydrogenBondAtomTypeGenerator.SP3_OXYGEN_IN_CO2H);
        }
    }

    private boolean __isOxygenInAmide(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (this.__isCarbonylCarbonInAmide(theConnectedAtom)) {
                return true;
            }
        }

        return false;
    }

    private boolean __isOxygenInCarboxylateIon(IAtom theAtom) {
        List<IAtom> theConnectedCarbonAtomList = this.__getConnectedAtomBoundedAtomicNumber(theAtom, AtomInformation.Carbon.ATOM_NUMBER);

        if (theConnectedCarbonAtomList.size() != 1) {
            return false;
        }

        IAtom theCarbonAtom = theConnectedCarbonAtomList.get(this.FIRST_INDEX);
        List<IAtom> theConnectedOxygenAtomList = this.__getConnectedAtomBoundedAtomicNumber(theCarbonAtom, AtomInformation.Oxygen.ATOM_NUMBER);

        if (theConnectedOxygenAtomList.size() != 2) {
            return false;
        }

        for (IAtom theOxygenAtom : theConnectedOxygenAtomList) {
            if (this.itsMolecule.getConnectedAtomsCount(theOxygenAtom) != 1 || !this.itsMolecule.getBond(theCarbonAtom, theOxygenAtom).getOrder().equals(Order.SINGLE)) {
                return false;
            }
        }

        return true;
    }

    private List<IAtom> __getConnectedAtomBoundedAtomicNumber(IAtom theAtom, Integer theAtomicNumber) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);
        List<IAtom> theResultAtomList = new ArrayList<>();

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getAtomicNumber().equals(theAtomicNumber)) {
                theResultAtomList.add(theConnectedAtom);
            }
        }

        return theResultAtomList;
    }
}
