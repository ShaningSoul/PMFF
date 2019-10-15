/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.interenergy;

import java.io.Serializable;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractDerivativeFunctionOfInternalCoordinate;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.energyfunction.abstracts.AbstractInterEnergyFunction;
import org.bmdrc.sbff.energyfunction.parameter.SbffInterCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffInterCalculationParameter;
import org.bmdrc.sbff.interenergy.calculableset.CalculableNonbondingList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableNonbondingSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Nonboding energy function<br>
 * The role of this class is calculated in nononding energy in inter-molecular
 * energy
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 03(Thr)
 */
public class SbffNonbondingEnergyFunction extends AbstractInterEnergyFunction implements Serializable {

    private static final long serialVersionUID = -557432865836634892L;

    private CalculableNonbondingList itsCalculableNonbondingList;
//    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;
    //constant String variable
    public static final String NON_BOND_ATOM_TYPE = "Non_Bond_Atom_Type";
    private final String VAN_DER_WAALS_ENERGY_KEY = "Van Der Waals Energy";

    public SbffNonbondingEnergyFunction(IAtomContainer theMolecule) {
        this(theMolecule, new CalculableNonbondingList(theMolecule));
    }
    
    public SbffNonbondingEnergyFunction(IAtomContainer theMolecule, CalculableNonbondingList theCalculableNonbondingList) {
        this(theMolecule, theCalculableNonbondingList, new SbffInterCalculationParameter());
    }

    public SbffNonbondingEnergyFunction(IAtomContainer theMolecule, CalculableNonbondingList theCalculableNonbondingList, 
            ISbffInterCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculableNonbondingList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }

    public SbffNonbondingEnergyFunction(IAtomContainer theMolecule, CalculableNonbondingList theCalculableNonbondingList, 
            ISbffInterCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableNonbondingList = theCalculableNonbondingList;
    }

    public CalculableNonbondingList getCalculableNonbondingList() {
        return itsCalculableNonbondingList;
    }

    public void setCalculableNonbondingList(CalculableNonbondingList theCalculableNonbondingList) {
        this.itsCalculableNonbondingList = theCalculableNonbondingList;
    }

    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;

        for (CalculableNonbondingSet theCalculableVanDerWaalsSet : this.itsCalculableNonbondingList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableVanDerWaalsSet);

            theCalculableVanDerWaalsSet.setEnergy(theEnergy);
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.VAN_DER_WAALS_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private double __calculateEnergyFunction(CalculableNonbondingSet theCalculableVanDerWaalsSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableVanDerWaalsSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableVanDerWaalsSet.getSecondAtomIndex());
        Double theEpsilon_ij = theCalculableVanDerWaalsSet.getEpsilon();
        Double theSigma_ij = theCalculableVanDerWaalsSet.getSigma();
        Double theDistance = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d());

        if (theDistance < this.itsCalculationParameter.getMaxNonbondingCutoff()) {
            Double theEnergy = 4.0 * theEpsilon_ij * (Math.pow(theSigma_ij / theDistance, 12) - Math.pow(theSigma_ij / theDistance, 6));

            if (theDistance > this.itsCalculationParameter.getMinNonbondingCutoff()) {
                theEnergy *= (this.itsCalculationParameter.getMaxNonbondingCutoff() - theDistance) / (this.itsCalculationParameter.getMaxNonbondingCutoff() - this.itsCalculationParameter.getMinNonbondingCutoff());
            }

            return theEnergy;
        } else {
            return 0.0;
        }
    }

    @Override
    public void calculateGradient() {
        for (CalculableNonbondingSet theCalculableVanDerWaalsSet : this.itsCalculableNonbondingList) {
            double theGradient = this.__calculateGradient(theCalculableVanDerWaalsSet);

            theCalculableVanDerWaalsSet.setGradient(theGradient);
        }

        this.itsCalculableNonbondingList.sortNonZeroGradient();
    }

    private double __calculateGradient(CalculableNonbondingSet theVanDerWaalsSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theVanDerWaalsSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theVanDerWaalsSet.getSecondAtomIndex());
        Double theEpsilon_ij = theVanDerWaalsSet.getEpsilon();
        Double theSigma_ij = theVanDerWaalsSet.getSigma();
        Double theDistance = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d());

        if (theDistance < this.itsCalculationParameter.getMaxNonbondingCutoff()) {
            Double theGradient = 4.0 * theEpsilon_ij * (-12.0 * Math.pow(theSigma_ij / theDistance, 12) + 6.0 * Math.pow(theSigma_ij / theDistance, 6)) / theDistance;

            if (theDistance > this.itsCalculationParameter.getMinNonbondingCutoff()) {
                theGradient *= (this.itsCalculationParameter.getMaxNonbondingCutoff() - theDistance) / (this.itsCalculationParameter.getMaxNonbondingCutoff() - this.itsCalculationParameter.getMinNonbondingCutoff());
                theGradient += -(4.0 * theEpsilon_ij * (Math.pow(theSigma_ij / theDistance, 12) - Math.pow(theSigma_ij / theDistance, 6)))
                        / (this.itsCalculationParameter.getMaxNonbondingCutoff() - this.itsCalculationParameter.getMinNonbondingCutoff());
            }

            return theGradient;
        } else {
            return 0.0;
        }
    }

    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableNonbondingSet theVanDerWaalsSet : this.itsCalculableNonbondingList) {
            if (!theVanDerWaalsSet.getGradient().equals(0.0)) {
                this.__calculateGradientVector(theVanDerWaalsSet);
            } else {
                break;
            }
        }
    }

    private void __calculateGradientVector(CalculableNonbondingSet theVanDerWaalsSet) {
        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theVanDerWaalsSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE))) {
            this.__calculateGradientVector(theVanDerWaalsSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE);
        }

        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theVanDerWaalsSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE))) {
            this.__calculateGradientVector(theVanDerWaalsSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE);
        }
    }

    private void __calculateGradientVector(CalculableNonbondingSet theVanDerWaalsSet, int theDerivativeAtomIndex, int theCounterAtomIndex) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theVanDerWaalsSet.getAtomIndexList(),
                theDerivativeAtomIndex, theVanDerWaalsSet.getGradient());
        
        if (theVanDerWaalsSet.getAtomIndexList().isEmpty()) {
            System.out.println("Nonbonding Energy Function Error");
        }
        
        this._setGradient(theVanDerWaalsSet.getAtomIndexList().get(theDerivativeAtomIndex), theAtomGradientVector);
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableNonbondingSet theVanDerWaalsSet : this.itsCalculableNonbondingList) {
            if (!theVanDerWaalsSet.getConjugatedGradient().equals(0.0)) {
                this.__reloadConjugatedGradientVector(theVanDerWaalsSet);
            } else {
                break;
            }
        }
    }

    private void __reloadConjugatedGradientVector(CalculableNonbondingSet theVanDerWaalsSet) {
        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theVanDerWaalsSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE))) {
            this.__reloadConjugatedGradientVector(theVanDerWaalsSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE);
        }

        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theVanDerWaalsSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE))) {
            this.__reloadConjugatedGradientVector(theVanDerWaalsSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE);
        }
    }

    private void __reloadConjugatedGradientVector(CalculableNonbondingSet theVanDerWaalsSet, int theDerivativeAtomIndex, int theCounterAtomIndex) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theVanDerWaalsSet.getAtomIndexList(),
                theDerivativeAtomIndex, theVanDerWaalsSet.getConjugatedGradient());
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theVanDerWaalsSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theVanDerWaalsSet.getAtomIndexList().get(theCounterAtomIndex));
        
        this._setConjugatedGradient(theDerivativeAtomIndex, theAtomGradientVector);
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableNonbondingList.makeConjugatedGradient(theScalingFactor);
    }
}