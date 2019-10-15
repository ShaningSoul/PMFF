/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.sbff.intraenergy;

import java.io.Serializable;
import java.util.List;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractDerivativeFunctionOfInternalCoordinate;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.sbff.abstracts.SbffConstant;
import org.bmdrc.sbff.energyfunction.abstracts.AbstractIntraEnergyFunction;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableIntraNonbondingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableIntraNonbondingSet;
import org.bmdrc.ui.abstracts.Constant;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 03
 */
public class SbffIntraNonbondingEnergyFunction extends AbstractIntraEnergyFunction implements Serializable {

    private static final long serialVersionUID = 1697641575321697427L;

    private CalculableIntraNonbondingList itsCalculableIntraNonbondingList;
    //constant Double variable
    private final double HYDROGEN_ACTUAL_DISTANCE = 0.923;
    //constant String variable
    public static final String INTRA_NONBONDING_ENERGY_KEY = "Intra-nonbonding energy";

    public SbffIntraNonbondingEnergyFunction(IAtomContainer theMolecule, CalculableIntraNonbondingList theIntraNonbondingList) {
        this(theMolecule, theIntraNonbondingList, new SbffIntraCalculationParameter());
    }

    public SbffIntraNonbondingEnergyFunction(IAtomContainer theMolecule, CalculableIntraNonbondingList theIntraNonbondingList,
            ISbffIntraCalculationParameter theCalculationParameter) {
        this(theMolecule, theIntraNonbondingList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }

    public SbffIntraNonbondingEnergyFunction(IAtomContainer theMolecule, CalculableIntraNonbondingList theIntraNonbondingList,
            ISbffIntraCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);

        this.itsCalculableIntraNonbondingList = theIntraNonbondingList;
    }

    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = SbffConstant.INITIAL_ENERGY;

        for (CalculableIntraNonbondingSet theIntraNonbondingSet : this.itsCalculableIntraNonbondingList) {
            Double theEnergy = this.__calculateEnergyFunctionInMM3(theIntraNonbondingSet);

            theIntraNonbondingSet.setEnergy(theEnergy);

            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(SbffIntraNonbondingEnergyFunction.INTRA_NONBONDING_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private double __calculateEnergyFunctionInMM3(CalculableIntraNonbondingSet theIntraNonbondingSet) {
        double theDistance = this.__calculateDistanceInVanDerWaals(theIntraNonbondingSet);

        if (theDistance < this.itsCalculationParameter.getMaxIntraNonbondingCutoff()) {
            Double theEnergy = theIntraNonbondingSet.getEpsilonInMM3() * (1.84e5 * Math.exp(-12.0 * theDistance / theIntraNonbondingSet.getSumOfVdwRadius())
                    - 2.25 * Math.pow(theIntraNonbondingSet.getSumOfVdwRadius() / theDistance, 6.0));

            if (theDistance > this.itsCalculationParameter.getMaxIntraNonbondingCutoff()) {
                theEnergy *= (this.itsCalculationParameter.getMaxIntraNonbondingCutoff() - theDistance) / (this.itsCalculationParameter.getMaxIntraNonbondingCutoff() - this.itsCalculationParameter.getMinIntraNonbondingCutoff());
            }

            return theEnergy;
        }

        return 0.0;
    }

    private double __calculateEnergyFunctionInSBFF(CalculableIntraNonbondingSet theIntraNonbondingSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theIntraNonbondingSet.getFirstAtomNumber());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theIntraNonbondingSet.getSecondAtomNumber());
        Double theEpsilon_ij = theIntraNonbondingSet.getEpsilonInSbff();
        Double theSigma_ij = theIntraNonbondingSet.getSigma();
        Double theDistance = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d());

        if (theDistance < this.itsCalculationParameter.getMaxIntraNonbondingCutoff()) {
            Double theEnergy = 4.0 * theEpsilon_ij * (Math.pow(theSigma_ij / theDistance, 12) - Math.pow(theSigma_ij / theDistance, 6));

            if (theDistance > this.itsCalculationParameter.getMinIntraNonbondingCutoff()) {
                theEnergy *= (this.itsCalculationParameter.getMaxIntraNonbondingCutoff() - theDistance) / (this.itsCalculationParameter.getMaxIntraNonbondingCutoff() - this.itsCalculationParameter.getMinIntraNonbondingCutoff());
            }

            return theEnergy;
        } else {
            return 0.0;
        }
    }

    private double __calculateDistanceInVanDerWaals(CalculableIntraNonbondingSet theVanDerWaalsSet) {
        Vector3d theFirstAtomPosition = this.__calculatePositionInVanDerWaals(this.itsMolecule.getAtom(theVanDerWaalsSet.getFirstAtomNumber()));
        Vector3d theSecondAtomPosition = this.__calculatePositionInVanDerWaals(this.itsMolecule.getAtom(theVanDerWaalsSet.getSecondAtomNumber()));

        return theFirstAtomPosition.distance(theSecondAtomPosition);
    }

    private Vector3d __calculatePositionInVanDerWaals(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);
        Vector3d thePosition = new Vector3d(theAtom);

        if (theAtom.getAtomicNumber() == AtomInformation.Hydrogen.ATOM_NUMBER && theConnectedAtomList.get(Constant.FIRST_INDEX).getAtomicNumber() == AtomInformation.Carbon.ATOM_NUMBER) {
            Vector3d theDirectionVector = new Vector3d(theConnectedAtomList.get(Constant.FIRST_INDEX), theAtom);

            thePosition = Vector3dCalculator.sum(Vector3dCalculator.productByScalar(this.HYDROGEN_ACTUAL_DISTANCE, theDirectionVector), new Vector3d(theConnectedAtomList.get(Constant.FIRST_INDEX)));
        }

        return thePosition;
    }

    private void __generateMolecule() {
        try {
            IAtomContainer theCopiedMolecule = (IAtomContainer) this.itsMolecule.clone();

            for (IAtom theAtom : theCopiedMolecule.atoms()) {
                List<IAtom> theConnectedAtomList = theCopiedMolecule.getConnectedAtomsList(theAtom);

                if (theAtom.getAtomicNumber() == AtomInformation.Hydrogen.ATOM_NUMBER && theConnectedAtomList.get(Constant.FIRST_INDEX).getAtomicNumber() == AtomInformation.Carbon.ATOM_NUMBER) {
                    Vector3d theDirectionVector = new Vector3d(theConnectedAtomList.get(Constant.FIRST_INDEX), theAtom);

                    theAtom.setPoint3d(Vector3dCalculator.sum(Vector3dCalculator.productByScalar(this.HYDROGEN_ACTUAL_DISTANCE, theDirectionVector),
                            new Vector3d(theConnectedAtomList.get(Constant.FIRST_INDEX))).toPoint3d());
                }
            }

            this.itsFirstDerivativeFunction.setMolecule(theCopiedMolecule);
        } catch (CloneNotSupportedException ex) {
        }
    }

    @Override
    public void calculateGradient() {
        this.__generateMolecule();

        for (CalculableIntraNonbondingSet theIntraNonbondingSet : this.itsCalculableIntraNonbondingList) {
            double theGradient = 0.0;

            theGradient = this.__calculateGradient(theIntraNonbondingSet);

            theIntraNonbondingSet.setGradient(theGradient);
        }
    }

    private double __calculateGradient(CalculableIntraNonbondingSet theIntraNonbondingSet) {
        double theDistance = this.__calculateDistanceInVanDerWaals(theIntraNonbondingSet);

        if (theDistance < this.itsCalculationParameter.getMaxIntraNonbondingCutoff()) {
            double theGradient = theIntraNonbondingSet.getEpsilonInMM3() * (1.84e5 * (-12.0 / theIntraNonbondingSet.getSumOfVdwRadius())
                    * Math.exp(-12.0 * theDistance / theIntraNonbondingSet.getSumOfVdwRadius()) + 2.25 * (6.0 / theDistance)
                    * Math.pow(theIntraNonbondingSet.getSumOfVdwRadius() / theDistance, 6.0));

            if (theDistance > this.itsCalculationParameter.getMinIntraNonbondingCutoff()) {
                theGradient *= (this.itsCalculationParameter.getMaxIntraNonbondingCutoff() - theDistance) / (this.itsCalculationParameter.getMaxIntraNonbondingCutoff() - this.itsCalculationParameter.getMinIntraNonbondingCutoff());
                theGradient = -(theIntraNonbondingSet.getEpsilonInMM3() * (1.84e5 * Math.exp(-12.0 * theDistance / theIntraNonbondingSet.getSumOfVdwRadius())
                        - 2.25 * Math.pow(theIntraNonbondingSet.getSumOfVdwRadius() / theDistance, 6.0))) / (this.itsCalculationParameter.getMaxIntraNonbondingCutoff() - this.itsCalculationParameter.getMinIntraNonbondingCutoff());
            }

            return theGradient;
        }

        return 0.0;
    }
    
    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableIntraNonbondingSet theIntraNonbondingSet : this.itsCalculableIntraNonbondingList) {
            this.__calculateGradientVector(theIntraNonbondingSet);
        }
    }

    private void __calculateGradientVector(CalculableIntraNonbondingSet theIntraNonbondingSet) {
        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theIntraNonbondingSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE))) {
            this.__calculateGradientVector(theIntraNonbondingSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE, theIntraNonbondingSet.getGradient());
        }

        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theIntraNonbondingSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE))) {
            this.__calculateGradientVector(theIntraNonbondingSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE, theIntraNonbondingSet.getGradient());
        }
    }

    private void __calculateGradientVector(CalculableIntraNonbondingSet theIntraNonbondingSet, int theDerivativeAtomIndex, int theCounterAtomIndex,
            double theGradient) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theIntraNonbondingSet.getAtomIndexList(),
                theDerivativeAtomIndex, theGradient);
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theIntraNonbondingSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theIntraNonbondingSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setGradient(theAtomNumber, theAtomGradientVector);
        }
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableIntraNonbondingSet theIntraNonbondingSet : this.itsCalculableIntraNonbondingList) {
            this.__reloadConjugatedGradientVector(theIntraNonbondingSet);
        }
    }

    private void __reloadConjugatedGradientVector(CalculableIntraNonbondingSet theIntraNonbondingSet) {
        this.__reloadConjugatedGradientVector(theIntraNonbondingSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE);
        this.__reloadConjugatedGradientVector(theIntraNonbondingSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE);
    }

    private void __reloadConjugatedGradientVector(CalculableIntraNonbondingSet theIntraNonbondingSet, int theDerivativeAtomIndex, int theCounterAtomIndex) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theIntraNonbondingSet.getAtomIndexList(),
                theDerivativeAtomIndex, theIntraNonbondingSet.getConjugatedGradient());
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theIntraNonbondingSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theIntraNonbondingSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setConjugatedGradient(theAtomNumber, theAtomGradientVector);
        }
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableIntraNonbondingList.makeConjugatedGradient(theScalingFactor);
    }
}
