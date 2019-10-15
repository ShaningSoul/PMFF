/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.predock.energyfunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.sbff.atomtype.HydrogenBondAcceptorList;
import org.bmdrc.sbff.atomtype.HydrogenBondAtomTypeGenerator;
import org.bmdrc.sbff.atomtype.HydrogenBondDonorList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableElectroStaticSet;
import org.bmdrc.sbff.interenergy.calculableset.CalculableHydrogenBondComponent;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondParameterSet;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondTotalSet;
import org.bmdrc.ui.abstracts.Constant;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableHydrogenBondListInGrid extends AbstractCalculableList<CalculableHydrogenBondSetInGrid> implements Serializable {

    private static final long serialVersionUID = 7312353292145877166L;

    private enum StringIndex {

        XAtomIndex, HAtomIndex, AAtomIndex, BAtomIndex, BInHAParameter, DInHAParameter, BInXAParameter, DInXAParameter, BInBHParameter, DInBHParameter;
    }

    public CalculableHydrogenBondListInGrid(CalculableHydrogenBondListInGrid theCalculableHydrogenBondList) {
        super(theCalculableHydrogenBondList);
    }

    public CalculableHydrogenBondListInGrid(IAtomContainer theLigand) {
        super(theLigand);

        this.__initializeCalculableHydrogenBondList();
    }

    private void __initializeCalculableHydrogenBondList() {
        HydrogenBondAcceptorList theAcceptorList = new HydrogenBondAcceptorList();
        HydrogenBondDonorList theDonorList = new HydrogenBondDonorList();
        SbffHydrogenBondTotalSet theParameterTotalSet = new SbffHydrogenBondTotalSet();
        int theType = this.__getGridAtomType(theAcceptorList, theDonorList);

        if (theType == CalculableHydrogenBondSetInGrid.NOT_H_BOND) {
            this.clear();
            return;
        }

        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount() - 1; ai < aEnd; ai++) {
            IAtom theCheckAtom = this.itsMolecule.getAtom(ai);

            if (this.__isHydrogenBond(theCheckAtom, theAcceptorList, theDonorList, theType)) {
                CalculableHydrogenBondSetInGrid theHydrogenBondSet = this.__getCalculableHydrogenBondList(theCheckAtom, theAcceptorList,
                        theDonorList, theParameterTotalSet, theType);

                if (theHydrogenBondSet != null) {
                    this.add(theHydrogenBondSet);
                }
            }
        }
    }

    private int __getGridAtomType(HydrogenBondAcceptorList theAcceptorList, HydrogenBondDonorList theDonorList) {
        IAtom theGridAtom = this.itsMolecule.getAtom(this.itsMolecule.getAtomCount() - 1);

        if (theAcceptorList.isHydrogenBondAcceptor(theGridAtom)) {
            return CalculableHydrogenBondSetInGrid.ACCEPTOR_TYPE_IN_LIGAND;
        } else if (theDonorList.isHydrogenBondDonor(theGridAtom)) {
            return CalculableHydrogenBondSetInGrid.DONOR_TYPE_IN_LIGAND;
        }

        return CalculableHydrogenBondSetInGrid.NOT_H_BOND;
    }

    private boolean __containsParameter(CalculableHydrogenBondSetInGrid theHydrogenBondSet) {
        for (CalculableHydrogenBondComponent theComponent : theHydrogenBondSet.getComponentList()) {
            if (theComponent.getParameter() == null) {
                return false;
            }
        }

        return true;
    }

    private boolean __isHydrogenBond(IAtom theCheckAtom, HydrogenBondAcceptorList theAcceptorList,
            HydrogenBondDonorList theDonorList, int theType) {
        IAtom theGridAtom = this.itsMolecule.getAtom(this.itsMolecule.getAtomCount() - 1);

        if (theAcceptorList.isHydrogenBondAcceptor(theCheckAtom) && theType == CalculableHydrogenBondSetInGrid.DONOR_TYPE_IN_LIGAND) {
            return true;
        } else if (theType == CalculableHydrogenBondSetInGrid.ACCEPTOR_TYPE_IN_LIGAND && theDonorList.isHydrogenBondDonor(theCheckAtom)) {
            return true;
        }

        return false;
    }

    private CalculableHydrogenBondSetInGrid __getCalculableHydrogenBondList(IAtom theCheckAtom, HydrogenBondAcceptorList theAcceptorList,
            HydrogenBondDonorList theDonorList, SbffHydrogenBondTotalSet theParameterTotalSet, int theType) {
        List<Integer> theHydrogenBondAtomIndexList = this.__getHydrogenBondComponentAtomIndexList(theCheckAtom, theType);
        SbffHydrogenBondParameterSet theParameterSet = this.__getParmaeter(theHydrogenBondAtomIndexList, theType, theParameterTotalSet);

        if (theParameterSet == null) {
            return null;
        }

        return new CalculableHydrogenBondSetInGrid(theHydrogenBondAtomIndexList, theType, theParameterSet);
    }

    private List<Integer> __getHydrogenBondComponentAtomIndexList(IAtom theCheckAtom, int theType) {
        List<Integer> theAtomIndexList = new ArrayList<>();

        if (theType == CalculableHydrogenBondSetInGrid.ACCEPTOR_TYPE_IN_LIGAND) {
            Collections.addAll(theAtomIndexList,
                    this.itsMolecule.getAtomNumber(this.itsMolecule.getConnectedAtomsList(theCheckAtom).get(Constant.FIRST_INDEX)),
                    this.itsMolecule.getAtomNumber(theCheckAtom),
                    this.itsMolecule.getAtomCount() - 1);
        } else if (theType == CalculableHydrogenBondSetInGrid.DONOR_TYPE_IN_LIGAND) {
            Collections.addAll(theAtomIndexList,
                    this.itsMolecule.getAtomCount() - 1,
                    this.itsMolecule.getAtomNumber(theCheckAtom),
                    this.__getBAtomIndex(this.itsMolecule.getAtomNumber(theCheckAtom)));
        }

        return theAtomIndexList;
    }

    private SbffHydrogenBondParameterSet __getParmaeter(List<Integer> theHydrogenBondAtomIndexList, int theType,
            SbffHydrogenBondTotalSet theParameterTotalSet) {
        List<Integer> theCheckAtomTypeList = this.__getHydrogenBondAtomTypeList(theHydrogenBondAtomIndexList);

        for (SbffHydrogenBondParameterSet theParameterSet : theParameterTotalSet.getTotalParameterSet()) {
            List<Integer> theAtomTypeList = theParameterSet.getAtomTypeList();

            if (this.__isParameter(theAtomTypeList, theCheckAtomTypeList, theType)) {
                return theParameterSet;
            }
        }

        return null;
    }

    private boolean __isParameter(List<Integer> theAtomTypeList, List<Integer> theCheckAtomTypeList, int theType) {
        switch (theType) {
            case CalculableHydrogenBondSetInGrid.ACCEPTOR_TYPE_IN_LIGAND:
                return theAtomTypeList.subList(Constant.FIRST_INDEX, Constant.THIRD_INDEX + 1).equals(theCheckAtomTypeList);
            case CalculableHydrogenBondSetInGrid.DONOR_TYPE_IN_LIGAND:
                return theAtomTypeList.subList(Constant.SECOND_INDEX, Constant.FOURTH_INDEX + 1).equals(theCheckAtomTypeList);
            default:
                return false;
        }
    }

    private List<Integer> __getHydrogenBondAtomTypeList(List<Integer> theHydrogenBondAtomIndexList) {
        List<Integer> theAtomTypeList = new ArrayList<>();

        for (Integer theHydrogenBondAtomIndex : theHydrogenBondAtomIndexList) {
            if (theHydrogenBondAtomIndex != null) {
                theAtomTypeList.add((Integer)this.itsMolecule.getAtom(theHydrogenBondAtomIndex)
                        .getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY));
            } else {
                theAtomTypeList.add(null);
            }
        }

        return theAtomTypeList;
    }

    private int __getAAtomIndex(CalculableElectroStaticSet theCalculableElectroStaticSet, HydrogenBondAcceptorList theAcceptorList) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getFirstAtomIndex());

        if (theAcceptorList.isHydrogenBondAcceptor(theFirstAtom)) {
            return theCalculableElectroStaticSet.getFirstAtomIndex();
        }

        return theCalculableElectroStaticSet.getSecondAtomIndex();
    }

    private int __getHAtomIndex(CalculableElectroStaticSet theCalculableElectroStaticSet, HydrogenBondDonorList theDonorList) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getFirstAtomIndex());

        if (theDonorList.isHydrogenBondDonor(theFirstAtom)) {
            return theCalculableElectroStaticSet.getFirstAtomIndex();
        }

        return theCalculableElectroStaticSet.getSecondAtomIndex();
    }

    private Integer __getBAtomIndex(int theAAtomIndex) {
        Integer theHydrogenBondAtomType = this.__getHydrogenAtomType(theAAtomIndex);

        if (theHydrogenBondAtomType.equals(HydrogenBondAtomTypeGenerator.NITROGEN_IN_AMIDE)) {
            return this.__getBAtomIndexForCarbonBoundedAmide(theAAtomIndex);
        } else if (theHydrogenBondAtomType.equals(HydrogenBondAtomTypeGenerator.NITROGEN_IN_SECONDARY_AMINE)) {
            return this.__getBAtomIndexForCarbonBoundedSecondaryAmine(theAAtomIndex);
        } else {
            for (IAtom theConnectedAtom : this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(theAAtomIndex))) {
                if (theConnectedAtom.getAtomicNumber().equals(AtomInformation.Carbon.ATOM_NUMBER)) {
                    return this.itsMolecule.getAtomNumber(theConnectedAtom);
                }
            }
        }

        return null;
    }

    private Integer __getBAtomIndexForCarbonBoundedSecondaryAmine(int theAAtomIndex) {
        IAtom theAAtom = this.itsMolecule.getAtom(theAAtomIndex);
        double theDistance = Double.MAX_VALUE;
        Integer theAtomIndex = null;

        for (IAtom theConnectedAtom : this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(theAAtomIndex))) {
            Integer theHydrogenBondAtomTypeByConnectedAtom = (Integer) theConnectedAtom.getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY);

            if (theHydrogenBondAtomTypeByConnectedAtom.equals(HydrogenBondAtomTypeGenerator.CARBON_BOUNDED_AMINE) && theDistance > theAAtom.getPoint3d().distance(theConnectedAtom.getPoint3d())) {
                theDistance = theAAtom.getPoint3d().distance(theConnectedAtom.getPoint3d());
                theAtomIndex = this.itsMolecule.getAtomNumber(theConnectedAtom);
            }
        }

        return theAtomIndex;
    }

    private Integer __getBAtomIndexForCarbonBoundedAmide(int theAAtomIndex) {
        for (IAtom theConnectedAtom : this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(theAAtomIndex))) {
            Integer theHydrogenBondAtomTypeByConnectedAtom = (Integer) theConnectedAtom.getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY);

            if (theHydrogenBondAtomTypeByConnectedAtom.equals(HydrogenBondAtomTypeGenerator.CARBONYL_CARBON_IN_AMIDE)) {
                return this.itsMolecule.getAtomNumber(theConnectedAtom);
            }
        }

        return null;
    }

    private Integer __getHydrogenAtomType(int theAtomIndex) {
        return (Integer) this.itsMolecule.getAtom(theAtomIndex).getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY);
    }

    private Integer __getXAtomIndex(int theXAtomIndex) {
        return this.itsMolecule.getAtomNumber(this.itsMolecule.getConnectedAtomsList(this.itsMolecule.getAtom(theXAtomIndex)).get(this.FIRST_INDEX));
    }

    public boolean containAtomIndexSet(int theFirstAtomIndex, int theSecondAtomIndex) {
        for (CalculableHydrogenBondSetInGrid theCalculableHydrogenBondSet : this) {
            if (theCalculableHydrogenBondSet.containAtomIndexSet(theFirstAtomIndex, theSecondAtomIndex)) {
                return true;
            }
        }

        return false;
    }

    public boolean isHydrogenBondCalculableSet(int theFirstAtomIndex, int theSecondAtomIndex) {
        for (CalculableHydrogenBondSetInGrid theCalculableHydrogenBondSet : this) {
            if (theCalculableHydrogenBondSet.containAtomIndexSet(theFirstAtomIndex, theSecondAtomIndex)
                    && !theCalculableHydrogenBondSet.isXBAtomIndex(theFirstAtomIndex, theSecondAtomIndex)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        for (CalculableHydrogenBondSetInGrid theSet : this) {
            theStringBuilder.append(theSet).append(" ");
        }

        return theStringBuilder.toString();
    }
}
