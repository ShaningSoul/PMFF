package org.bmdrc.sbff.interenergy.calculableset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import org.bmdrc.sbff.interenergy.SbffNonbondingEnergyFunction;
import org.bmdrc.sbff.parameter.interenergy.NonBondParameterSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableNonbondingList extends AbstractCalculableList<CalculableNonbondingSet> implements Serializable {

    private static final long serialVersionUID = 1055602531599111055L;
    //String variable
    public static final String HYDROGEN_BOND_LIST_OVERLAP_KEY = "Hydrogen bond overlap";

    public CalculableNonbondingList(IAtomContainer theMolecule) {
        this(theMolecule, new CalculableElectroStaticList(theMolecule), new ArrayList<Integer>());
    }

    public CalculableNonbondingList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new CalculableElectroStaticList(theMolecule, theNotCalculateAtomNumberList), theNotCalculateAtomNumberList);
    }

    public CalculableNonbondingList(IAtomContainer theMolecule, NonBondParameterSet theParameterSet, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theParameterSet, new CalculableElectroStaticList(theMolecule, theNotCalculateAtomNumberList), theNotCalculateAtomNumberList);
    }

    public CalculableNonbondingList(IAtomContainer theMolecule, CalculableElectroStaticList theCalculableElectroStaticList,
            List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theCalculableElectroStaticList, new CalculableHydrogenBondList(theMolecule, theCalculableElectroStaticList, theNotCalculateAtomNumberList),
                new ArrayList<Integer>());
    }

    public CalculableNonbondingList(IAtomContainer theMolecule, NonBondParameterSet theParameterSet, CalculableElectroStaticList theCalculableElectroStaticList,
            List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theParameterSet, theCalculableElectroStaticList,
                new CalculableHydrogenBondList(theMolecule, theCalculableElectroStaticList, theNotCalculateAtomNumberList), new ArrayList<Integer>());
    }

    public CalculableNonbondingList(IAtomContainer theMolecule, CalculableElectroStaticList theCalculableElectroStaticList,
            CalculableHydrogenBondList theCalculableHydrogenBondList) {
        this(theMolecule, new NonBondParameterSet(), theCalculableElectroStaticList, theCalculableHydrogenBondList, new ArrayList<Integer>());
    }

    public CalculableNonbondingList(IAtomContainer theMolecule, CalculableElectroStaticList theCalculableElectroStaticList,
            CalculableHydrogenBondList theCalculableHydrogenBondList, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new NonBondParameterSet(), theCalculableElectroStaticList, theCalculableHydrogenBondList, theNotCalculateAtomNumberList);
    }

    public CalculableNonbondingList(IAtomContainer theMolecule, NonBondParameterSet theParameterSet, CalculableElectroStaticList theCalculableElectroStaticList,
            CalculableHydrogenBondList theCalculableHydrogenBondList) {
        this(theMolecule, theParameterSet, theCalculableElectroStaticList, theCalculableHydrogenBondList, new ArrayList<Integer>());
    }

    public CalculableNonbondingList(IAtomContainer theMolecule, NonBondParameterSet theParameterSet, CalculableElectroStaticList theCalculableElectroStaticList,
            CalculableHydrogenBondList theCalculableHydrogenBondList, List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);

        this.__setEmptyVanDerWaalsType(theParameterSet);
        this.__initializeCalculableVanDerWaalsList(theCalculableElectroStaticList, theCalculableHydrogenBondList, theParameterSet);
    }

    public CalculableNonbondingList(CalculableNonbondingList theCalculableVanDerWaalsList) {
        super(theCalculableVanDerWaalsList);
    }

    private void __initializeCalculableVanDerWaalsList(CalculableElectroStaticList theCalculableElectroStaticList,
            CalculableHydrogenBondList theCalculableHydrogenBondList, NonBondParameterSet theParameterSet) {
        for (CalculableElectroStaticSet theCalculableElectroStaticSet : theCalculableElectroStaticList) {
            if (this.__isNonbondingSet(theCalculableElectroStaticSet, theCalculableHydrogenBondList)) {
                this.__initializeCalculableVanDerWaalsList(theCalculableElectroStaticSet, theCalculableHydrogenBondList, theParameterSet);
            }
        }
    }

    private boolean __isNonbondingSet(CalculableElectroStaticSet theCalculableElectroStaticSet, CalculableHydrogenBondList theCalculableHydrogenBondList) {
        return theCalculableHydrogenBondList.isNonbondingCalculableSet(theCalculableElectroStaticSet.getFirstAtomIndex(),
                theCalculableElectroStaticSet.getSecondAtomIndex());
    }

    private void __initializeCalculableVanDerWaalsList(CalculableElectroStaticSet theCalculableElectroStaticSet, 
            CalculableHydrogenBondList theCalculableHydrogenBondList, NonBondParameterSet theParameterSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getSecondAtomIndex());
        double theEpsilon = this.__getEpsilonInVanDerWaalsInteraction(theFirstAtom, theSecondAtom, theParameterSet);
        double theSigma = this.__getSigmaInVanDerWaalsInteraction(theFirstAtom, theSecondAtom, theParameterSet);
        CalculableNonbondingSet theCalculableVanDerWaalsSet = new CalculableNonbondingSet(theCalculableElectroStaticSet.getFirstAtomIndex(), theCalculableElectroStaticSet.getSecondAtomIndex(), theEpsilon, theSigma);

        if (this.__isNonbondingSet(theCalculableElectroStaticSet, theCalculableHydrogenBondList)) {
            theCalculableVanDerWaalsSet.setProperty(CalculableNonbondingList.HYDROGEN_BOND_LIST_OVERLAP_KEY, true);
        } else {
            theCalculableVanDerWaalsSet.setProperty(CalculableNonbondingList.HYDROGEN_BOND_LIST_OVERLAP_KEY, false);
        }
        
        this.add(theCalculableVanDerWaalsSet);
    }

    private Double __getEpsilonInVanDerWaalsInteraction(IAtom theFirstAtom, IAtom theSecondAtom, NonBondParameterSet theParameterSet) {
        if (this.__isO1OrO2ByVanDerWaalsType(theFirstAtom) && this.__isO1OrO2ByVanDerWaalsType(theSecondAtom)) {
            return theParameterSet.getEpsilon("OO");
        }

        return Math.sqrt(theParameterSet.getEpsilon(theFirstAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString())
                * theParameterSet.getEpsilon(theSecondAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString()));
    }

    private Double __getSigmaInVanDerWaalsInteraction(IAtom theFirstAtom, IAtom theSecondAtom, NonBondParameterSet theParameterSet) {
        if (this.__isO1OrO2ByVanDerWaalsType(theFirstAtom) && this.__isO1OrO2ByVanDerWaalsType(theSecondAtom)) {
            return theParameterSet.getSigma("OO");
        }

        return (theParameterSet.getSigma(theFirstAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString())
                + theParameterSet.getSigma(theSecondAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString())) / 2.0;
    }

    private Boolean __isO1OrO2ByVanDerWaalsType(IAtom theAtom) {
        return theAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, String.class).equals("O1") || theAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, String.class).equals("O2");
    }

    private void __setEmptyVanDerWaalsType(NonBondParameterSet theParameterSet) {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            if (!theAtom.getProperties().containsKey(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE)
                    || theAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE) == null) {
                theAtom.setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, theParameterSet.getNonBondTypeMapByMpeoeType().get(theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)));
            }
        }
    }
}
