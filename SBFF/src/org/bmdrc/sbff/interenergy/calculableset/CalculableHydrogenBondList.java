/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.interenergy.calculableset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.sbff.atomtype.HydrogenBondAcceptorList;
import org.bmdrc.sbff.atomtype.HydrogenBondAtomTypeGenerator;
import org.bmdrc.sbff.atomtype.HydrogenBondDonorList;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondParameterSet;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondTotalSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableHydrogenBondList extends AbstractCalculableList<CalculableHydrogenBondSet> implements Serializable {
    
    private static final long serialVersionUID = 7312353292145877166L;
    
    private enum StringIndex {
        
        XAtomIndex, HAtomIndex, AAtomIndex, BAtomIndex, BInHAParameter, DInHAParameter, BInXAParameter, DInXAParameter, BInBHParameter, DInBHParameter;
    }
    
    public CalculableHydrogenBondList() {
        super();
    }
    
    public CalculableHydrogenBondList(CalculableHydrogenBondList theCalculableHydrogenBondList) {
        super(theCalculableHydrogenBondList);
    }
    
    public CalculableHydrogenBondList(IAtomContainer theMolecule) {
        this(theMolecule, new CalculableElectroStaticList(theMolecule));
    }
    
    public CalculableHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet) {
        this(theMolecule, theParameterSet, new CalculableElectroStaticList(theMolecule));
    }
    
    public CalculableHydrogenBondList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new CalculableElectroStaticList(theMolecule), theNotCalculateAtomNumberList);
    }
    
    public CalculableHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theParameterSet, new CalculableElectroStaticList(theMolecule), theNotCalculateAtomNumberList);
    }
    
    public CalculableHydrogenBondList(IAtomContainer theMolecule, CalculableElectroStaticList theCalculableElectroStaticList) {
        this(theMolecule, new TopologicalDistanceMatrix(theMolecule), theCalculableElectroStaticList,
                new ArrayList<Integer>());
    }
    
    public CalculableHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet,
            CalculableElectroStaticList theCalculableElectroStaticList) {
        this(theMolecule, theParameterSet, theCalculableElectroStaticList, new ArrayList<Integer>());
    }
    
    public CalculableHydrogenBondList(IAtomContainer theMolecule, CalculableElectroStaticList theCalculableElectroStaticList,
            List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new TopologicalDistanceMatrix(theMolecule), theCalculableElectroStaticList,
                theNotCalculateAtomNumberList);
    }
    
    public CalculableHydrogenBondList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix,
            CalculableElectroStaticList theCalculableElectroStaticList, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new SbffHydrogenBondTotalSet(), theCalculableElectroStaticList, theNotCalculateAtomNumberList);
    }
    
    public CalculableHydrogenBondList(IAtomContainer theMolecule, SbffHydrogenBondTotalSet theParameterSet,
            CalculableElectroStaticList theCalculableElectroStaticList, List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__initializeCalculableHydrogenBondList(theCalculableElectroStaticList, theParameterSet);
    }
    
    private void __initializeCalculableHydrogenBondList(CalculableElectroStaticList theCalculableElectroStaticList,
            SbffHydrogenBondTotalSet theParameterTotalSet) {
        HydrogenBondAcceptorList theAcceptorList = new HydrogenBondAcceptorList();
        HydrogenBondDonorList theDonorList = new HydrogenBondDonorList();
        
        for (int ci = theCalculableElectroStaticList.size() - 1; ci >= 0; ci--) {
            CalculableElectroStaticSet theCalculableElectroStaticSet = theCalculableElectroStaticList.get(ci);
            
            this.__generateHydrogenBondList(theCalculableElectroStaticSet, theParameterTotalSet, theAcceptorList, theDonorList);
        }
    }
    
    private void __generateHydrogenBondList(CalculableElectroStaticSet theCalculableElectroStaticSet, SbffHydrogenBondTotalSet theParameterTotalSet,
            HydrogenBondAcceptorList theAcceptorList, HydrogenBondDonorList theDonorList) {
        if (this.__isHydrogenBond(theCalculableElectroStaticSet, theAcceptorList, theDonorList)) {
            CalculableHydrogenBondSet theHydrogenBondSet = this.__getCalculableHydrogenBondList(theCalculableElectroStaticSet, theAcceptorList, theDonorList,
                    theParameterTotalSet);
            
            if (theHydrogenBondSet != null && this.__containsParameter(theHydrogenBondSet)) {
                this.add(theHydrogenBondSet);
            }
        }
    }
    
    private boolean __containsParameter(CalculableHydrogenBondSet theHydrogenBondSet) {
        for (CalculableHydrogenBondComponent theComponent : theHydrogenBondSet.getComponentList()) {
            if (theComponent.getParameter() == null) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean __isHydrogenBond(CalculableElectroStaticSet theCalculableElectroStaticSet, HydrogenBondAcceptorList theAcceptorList,
            HydrogenBondDonorList theDonorList) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getSecondAtomIndex());
        
        if (this.itsNotCalculateAtomNumberList != null && this.itsNotCalculateAtomNumberList.contains(theCalculableElectroStaticSet.getFirstAtomIndex())
                && this.itsNotCalculateAtomNumberList.contains(theCalculableElectroStaticSet.getSecondAtomIndex())) {
            return false;
        }
        
        if (theAcceptorList.isHydrogenBondAcceptor(theFirstAtom) && theDonorList.isHydrogenBondDonor(theSecondAtom)) {
            return true;
        } else if (theAcceptorList.isHydrogenBondAcceptor(theSecondAtom) && theDonorList.isHydrogenBondDonor(theFirstAtom)) {
            return true;
        }
        
        return false;
    }
    
    private CalculableHydrogenBondSet __getCalculableHydrogenBondList(CalculableElectroStaticSet theCalculableElectroStaticSet,
            HydrogenBondAcceptorList theAcceptorList, HydrogenBondDonorList theDonorList, SbffHydrogenBondTotalSet theParameterTotalSet) {
        Integer theAAtomIndex = this.__getAAtomIndex(theCalculableElectroStaticSet, theAcceptorList);
        Integer theHAtomIndex = this.__getHAtomIndex(theCalculableElectroStaticSet, theDonorList);
        Integer theBAtomIndex = this.__getBAtomIndex(theAAtomIndex);
        Integer theXAtomIndex = this.__getXAtomIndex(theHAtomIndex);
        
        if (theAAtomIndex == null || theHAtomIndex == null || theBAtomIndex == null || theXAtomIndex == null) {
            return null;
        }
        
        SbffHydrogenBondParameterSet theParameterSet = this.__getParmaeter(theXAtomIndex, theHAtomIndex, theAAtomIndex, theBAtomIndex, theParameterTotalSet);
        CalculableHydrogenBondSet theHydrogenBondSet = new CalculableHydrogenBondSet(theXAtomIndex, theHAtomIndex, theAAtomIndex, theBAtomIndex, theParameterSet);
        
        return theHydrogenBondSet;
    }
    
    private SbffHydrogenBondParameterSet __getParmaeter(Integer theAtomIndexConnectedDonor, Integer theDonorAtomIndex, Integer theAcceptorAtomIndex,
            Integer theAtomIndexConnectedAcceptor, SbffHydrogenBondTotalSet theParameterTotalSet) {
        IAtom theAtomConnectedDonor = this.itsMolecule.getAtom(theAtomIndexConnectedDonor);
        IAtom theDonorAtom = this.itsMolecule.getAtom(theDonorAtomIndex);
        IAtom theAcceptorAtom = this.itsMolecule.getAtom(theAcceptorAtomIndex);
        IAtom theAtomConnectedAcceptor = this.itsMolecule.getAtom(theAtomIndexConnectedAcceptor);
        List<IAtom> theHydrogenBondAtomList = new ArrayList<>();
        
        theHydrogenBondAtomList.add(theAtomConnectedDonor);
        theHydrogenBondAtomList.add(theDonorAtom);
        theHydrogenBondAtomList.add(theAcceptorAtom);
        theHydrogenBondAtomList.add(theAtomConnectedAcceptor);
        
        return theParameterTotalSet.getParameterSet(theHydrogenBondAtomList);
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
        for (CalculableHydrogenBondSet theCalculableHydrogenBondSet : this) {
            if (theCalculableHydrogenBondSet.containAtomIndexSet(theFirstAtomIndex, theSecondAtomIndex)) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean isNonbondingCalculableSet(int theFirstAtomIndex, int theSecondAtomIndex) {
        for (CalculableHydrogenBondSet theCalculableHydrogenBondSet : this) {
            if (theCalculableHydrogenBondSet.containAtomIndexSet(theFirstAtomIndex, theSecondAtomIndex)
                    && !theCalculableHydrogenBondSet.isXBAtomIndex(theFirstAtomIndex, theSecondAtomIndex)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        for (CalculableHydrogenBondSet theSet : this) {
            theStringBuilder.append(theSet).append(" ");
        }
        
        return theStringBuilder.toString();
    }
}
