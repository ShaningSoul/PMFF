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
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.energyfunction.abstracts.AbstractInterEnergyFunction;
import org.bmdrc.sbff.energyfunction.parameter.SbffInterCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffInterCalculationParameter;
import org.bmdrc.sbff.interenergy.calculableset.CalculableElectroStaticList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableElectroStaticSet;
import org.bmdrc.sbff.tool.MpeoeChargeCalculator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.bmdrc.ui.abstracts.Constant;

/**
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 */
public class SbffElectroStaticEnergyFunction extends AbstractInterEnergyFunction implements Serializable {

    private static final long serialVersionUID = 1956081398890012572L;

    private CalculableElectroStaticList itsCalculableElectroStaticList;
    //constant Double variable
    private final double CONSTANT = Constant.CONVERT_FROM_J_TO_KCAL * 6.02e23 * Math.pow(Constant.ELECTRON_CHARGE, 2.0) / (4.0 * Math.PI * Constant.VACUUM_PERMITTIVITY);//331.885443736935
    //constant String variable
    private final String ELECTRO_STATIC_ENERGY_KEY = "Electrostatic energy";

    public SbffElectroStaticEnergyFunction(IAtomContainer theMolecule) {
        this(theMolecule, new CalculableElectroStaticList(theMolecule));
    }

    public SbffElectroStaticEnergyFunction(IAtomContainer theMolecule, ISbffInterCalculationParameter theCalculationParameter) {
        this(theMolecule, new CalculableElectroStaticList(theMolecule), theCalculationParameter);
    }
    
    public SbffElectroStaticEnergyFunction(IAtomContainer theMolecule, CalculableElectroStaticList theCalculableElectroStaticList) {
        this(theMolecule, theCalculableElectroStaticList, new SbffInterCalculationParameter());
    }
    
    public SbffElectroStaticEnergyFunction(IAtomContainer theMolecule, CalculableElectroStaticList theCalculableElectroStaticList, 
            ISbffInterCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculableElectroStaticList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }
    
    public SbffElectroStaticEnergyFunction(IAtomContainer theMolecule, CalculableElectroStaticList theCalculableElectroStaticList, 
            ISbffInterCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableElectroStaticList = theCalculableElectroStaticList;
    }

    public CalculableElectroStaticList getCalculableElectroStaticList() {
        return itsCalculableElectroStaticList;
    }

    public void setCalculableElectroStaticList(CalculableElectroStaticList theCalculableElectroStaticList) {
        this.itsCalculableElectroStaticList = theCalculableElectroStaticList;
    }

    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;

        for (CalculableElectroStaticSet theCalculableElectroStaticSet : this.itsCalculableElectroStaticList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableElectroStaticSet);

            theCalculableElectroStaticSet.setEnergy(theEnergy);
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.ELECTRO_STATIC_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private double __calculateEnergyFunction(CalculableElectroStaticSet theCalculableElectroStaticSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getSecondAtomIndex());
        Double theDistance = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d());
        Double theFirstAtomMpeoeCharge = (Double) theFirstAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY);
        Double theSecondAtomMpeoeCharge = (Double) theSecondAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY);

//        if(theCalculableElectroStaticSet.getFirstAtomIndex().equals(5) && theCalculableElectroStaticSet.getSecondAtomIndex().equals(1761)) {
//            System.out.println(theDistance);
//        }
        
        if (theDistance < this.itsCalculationParameter.getMaxElectroStaticCutoff()) {
            Double theEnergy = this.CONSTANT * (theFirstAtomMpeoeCharge * theSecondAtomMpeoeCharge) / (theDistance * this.itsCalculationParameter.getDielectricConstant());

            if (theDistance > this.itsCalculationParameter.getMinElectroStaticCutoff()) {
                theEnergy *= (this.itsCalculationParameter.getMaxElectroStaticCutoff() - theDistance) / (this.itsCalculationParameter.getMaxElectroStaticCutoff() - this.itsCalculationParameter.getMinElectroStaticCutoff());
            }

            return theEnergy;
        } else {
            return 0.0;
        }
    }

    @Override
    public void calculateGradient() {
        for (CalculableElectroStaticSet theCalculableElectroStaticSet : this.itsCalculableElectroStaticList) {
            double theGradient = this.__calculateGradient(theCalculableElectroStaticSet);

            theCalculableElectroStaticSet.setGradient(theGradient);
        }

        this.itsCalculableElectroStaticList.sortNonZeroGradient();
    }

    private double __calculateGradient(CalculableElectroStaticSet theElectroStaticSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theElectroStaticSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theElectroStaticSet.getSecondAtomIndex());
        Double theDistance = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d());
        Double theFirstAtomMpeoeCharge = (Double) theFirstAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY);
        Double theSecondAtomMpeoeCharge = (Double) theSecondAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY);

        if (theDistance < this.itsCalculationParameter.getMaxElectroStaticCutoff()) {
            Double theGradient = -this.CONSTANT * (theFirstAtomMpeoeCharge * theSecondAtomMpeoeCharge) / (theDistance * theDistance * this.itsCalculationParameter.getDielectricConstant());

            if (theDistance > this.itsCalculationParameter.getMinElectroStaticCutoff()) {
                theGradient *= (this.itsCalculationParameter.getMaxElectroStaticCutoff() - theDistance) / (this.itsCalculationParameter.getMaxElectroStaticCutoff() - this.itsCalculationParameter.getMinElectroStaticCutoff());
                theGradient += -this.CONSTANT * (theFirstAtomMpeoeCharge * theSecondAtomMpeoeCharge)
                        / (theDistance * this.itsCalculationParameter.getDielectricConstant() * (this.itsCalculationParameter.getMaxElectroStaticCutoff() - this.itsCalculationParameter.getMinElectroStaticCutoff()));
            }

            return theGradient;
        } else {
            return 0.0;
        }
    }

    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableElectroStaticSet theElectroStaticSet : this.itsCalculableElectroStaticList) {
            if (!theElectroStaticSet.getGradient().equals(0.0)) {
                this.__calculateGradientVector(theElectroStaticSet);
            }
        }
    }

    private void __calculateGradientVector(CalculableElectroStaticSet theElectroStaticSet) {
        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theElectroStaticSet.getAtomIndexList().get(AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE))) {
            this.__calculateGradientVector(theElectroStaticSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE);
        }

        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theElectroStaticSet.getAtomIndexList().get(AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE))) {
            this.__calculateGradientVector(theElectroStaticSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE);
        }
    }

    private void __calculateGradientVector(CalculableElectroStaticSet theElectroStaticSet, int theDerivativeAtomIndex, int theCounterAtomIndex) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theElectroStaticSet.getAtomIndexList(),
                theDerivativeAtomIndex, theElectroStaticSet.getGradient());
        
        if (theElectroStaticSet.getAtomIndexList().isEmpty()) {
            System.out.println("ElectroStaticEnergyFunction Error");
        }
        
        this._setGradient(theElectroStaticSet.getAtomIndexList().get(theDerivativeAtomIndex), theAtomGradientVector);
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableElectroStaticSet theElectroStaticSet : this.itsCalculableElectroStaticList) {
            if (theElectroStaticSet.getConjugatedGradient().equals(0.0)) {
                break;
            }

            this.__reloadConjugatedGradientVector(theElectroStaticSet);
        }
    }

    private void __reloadConjugatedGradientVector(CalculableElectroStaticSet theElectroStaticSet) {
        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theElectroStaticSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE))) {
            this.__reloadConjugatedGradientVector(theElectroStaticSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE);
        }

        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theElectroStaticSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE))) {
            this.__reloadConjugatedGradientVector(theElectroStaticSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE);
        }
    }

    private void __reloadConjugatedGradientVector(CalculableElectroStaticSet theElectroStaticSet, int theDerivativeAtomIndex, int theCounterAtomIndex) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theElectroStaticSet.getAtomIndexList(),
                theDerivativeAtomIndex, theElectroStaticSet.getConjugatedGradient());
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theElectroStaticSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theElectroStaticSet.getAtomIndexList().get(theCounterAtomIndex));

        this._setConjugatedGradient(theDerivativeAtomIndex, theAtomGradientVector);
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableElectroStaticList.makeConjugatedGradient(theScalingFactor);
    }
}
