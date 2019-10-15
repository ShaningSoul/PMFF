/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.sbff.intraenergy.calculableset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import org.bmdrc.sbff.interenergy.SbffNonbondingEnergyFunction;
import org.bmdrc.sbff.parameter.intraenergy.MM3NonbondingParameter;
import org.bmdrc.sbff.parameter.intraenergy.SbffIntraNonbondingParameterSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 03
 */
public class CalculableIntraNonbondingList extends AbstractCalculableList<CalculableIntraNonbondingSet> implements Serializable {

    private static final long serialVersionUID = 4271926014987322880L;

    public CalculableIntraNonbondingList(IAtomContainer theMolecule) {
        this(theMolecule, new ArrayList<Integer>());
    }

    public CalculableIntraNonbondingList(IAtomContainer theMolecule, SbffIntraNonbondingParameterSet theParameterSet) {
        this(theMolecule, theParameterSet, new ArrayList<Integer>());
    }

    public CalculableIntraNonbondingList(IAtomContainer theMolecule, SbffIntraNonbondingParameterSet theParameterSet,
            CalculableIntraElectroStaticList theCalculableIntraElectroStaticList) {
        this(theMolecule, theParameterSet, theCalculableIntraElectroStaticList, new ArrayList<Integer>());
    }

    public CalculableIntraNonbondingList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffIntraNonbondingParameterSet(), theNotCalculateAtomNumberList);
    }

    public CalculableIntraNonbondingList(IAtomContainer theMolecule, SbffIntraNonbondingParameterSet theParameterSet, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theParameterSet, new CalculableIntraElectroStaticList(theMolecule), theNotCalculateAtomNumberList);
    }

    public CalculableIntraNonbondingList(IAtomContainer theMolecule, CalculableIntraElectroStaticList theCalculableIntraElectroStaticList,
            List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffIntraNonbondingParameterSet(), theCalculableIntraElectroStaticList, theNotCalculateAtomNumberList);
    }

    public CalculableIntraNonbondingList(IAtomContainer theMolecule, SbffIntraNonbondingParameterSet theParameterSet,
            CalculableIntraElectroStaticList theCalculableIntraElectroStaticList, List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__setEmptyVanDerWaalsType(theParameterSet);
        this.__initializeIntraNonbondingSetList(theParameterSet, theCalculableIntraElectroStaticList);
    }

    private void __initializeIntraNonbondingSetList(SbffIntraNonbondingParameterSet theParameterSet,
            CalculableIntraElectroStaticList theCalculableIntraElectroStaticList) {
        for (CalculableIntraElectroStaticSet theCalculableElectroStaticSet : theCalculableIntraElectroStaticList) {
            this.__initializeIntraNonbondingList(theCalculableElectroStaticSet.getFirstAtomIndex(),
                    theCalculableElectroStaticSet.getSecondAtomIndex(), theParameterSet);
        }
    }

    private void __initializeIntraNonbondingList(int theFirstAtomNumber, int theSecondAtomNumber, SbffIntraNonbondingParameterSet theParameterSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theFirstAtomNumber);
        IAtom theSecondAtom = this.itsMolecule.getAtom(theSecondAtomNumber);
        MM3NonbondingParameter theFirstParameter = theParameterSet.getParameterInMM3(this.itsMolecule, theFirstAtom);
        MM3NonbondingParameter theSecondParameter = theParameterSet.getParameterInMM3(this.itsMolecule, theSecondAtom);
        
        double theEpsilonInMM3 = Math.sqrt(theFirstParameter.getEpsilon() * theSecondParameter.getEpsilon());
        double theEpsilonInSbff = this.__getEpsilonInSbff(theFirstAtom, theSecondAtom, theParameterSet);
        double theRadius = theFirstParameter.getRadius() + theSecondParameter.getRadius();
        double theSigma = this.__getSigmaInVanDerWaalsInteraction(theFirstAtom, theSecondAtom, theParameterSet);
        CalculableIntraNonbondingSet theCalculableNonbondingSet = new CalculableIntraNonbondingSet(theFirstAtomNumber, theSecondAtomNumber, theEpsilonInMM3,
                theRadius, theEpsilonInSbff, theSigma);

        this.add(theCalculableNonbondingSet);
    }

    private Double __getEpsilonInSbff(IAtom theFirstAtom, IAtom theSecondAtom, SbffIntraNonbondingParameterSet theParameterSet) {
        if (this.__isO1OrO2ByVanDerWaalsType(theFirstAtom) && this.__isO1OrO2ByVanDerWaalsType(theSecondAtom)) {
            return theParameterSet.getParameterSetInSbff().getEpsilon("OO");
        }

        return Math.sqrt(theParameterSet.getParameterSetInSbff().getEpsilon(theFirstAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString())
                * theParameterSet.getParameterSetInSbff().getEpsilon(theSecondAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString()));
    }

    private Double __getSigmaInVanDerWaalsInteraction(IAtom theFirstAtom, IAtom theSecondAtom, SbffIntraNonbondingParameterSet theParameterSet) {
        if (this.__isO1OrO2ByVanDerWaalsType(theFirstAtom) && this.__isO1OrO2ByVanDerWaalsType(theSecondAtom)) {
            return theParameterSet.getParameterSetInSbff().getSigma("OO");
        }

        return (theParameterSet.getParameterSetInSbff().getSigma(theFirstAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString())
                + theParameterSet.getParameterSetInSbff().getSigma(theSecondAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString())) / 2.0;
    }

    private Boolean __isO1OrO2ByVanDerWaalsType(IAtom theAtom) {
        return theAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString().equals("O1") || theAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE).toString().equals("O2");
    }

    private void __setEmptyVanDerWaalsType(SbffIntraNonbondingParameterSet theParameterSet) {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            if (!theAtom.getProperties().containsKey(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE)
                    || theAtom.getProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE) == null) {
                theAtom.setProperty(SbffNonbondingEnergyFunction.NON_BOND_ATOM_TYPE, theParameterSet.getParameterSetInSbff().getNonBondTypeMapByMpeoeType().get(theAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY)));
            }
        }
    }
}
