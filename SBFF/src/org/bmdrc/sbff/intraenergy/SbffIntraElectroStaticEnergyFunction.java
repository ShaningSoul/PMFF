/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.intraenergy;

import java.io.Serializable;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractDerivativeFunctionOfInternalCoordinate;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableIntraElectroStaticList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableIntraElectroStaticSet;
import org.bmdrc.sbff.tool.MpeoeChargeCalculator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 */
public class SbffIntraElectroStaticEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction implements Serializable {

    private static final long serialVersionUID = 1956081398890012572L;

    private CalculableIntraElectroStaticList itsCalculableElectroStaticList;
    //constant Double variable
    private final double CONVERT_FROM_J_TO_KCAL = 2.3901e-4;
    private final double VACUUM_PERMITTIVITY = 8.854e-22;
    private final double ELECTRON_CHARGE = 1.602e-19;
    private final double CONSTANT = this.CONVERT_FROM_J_TO_KCAL * 6.02e23 * Math.pow(this.ELECTRON_CHARGE, 2.0) / (4.0 * Math.PI * this.VACUUM_PERMITTIVITY);//331.885443736935
    //constant String variable
    private final String ELECTRO_STATIC_ENERGY_KEY = "Intra-Electrostatic energy";
    private final String ELECTRO_STATIC_GRADIENT_KEY = "Intra-Electrostatic Gradient";

    public SbffIntraElectroStaticEnergyFunction(IAtomContainer theMolecule, CalculableIntraElectroStaticList theCalculableElectroStaticList) {
        this(theMolecule, theCalculableElectroStaticList, new SbffIntraCalculationParameter());
    }

    public SbffIntraElectroStaticEnergyFunction(IAtomContainer theMolecule, CalculableIntraElectroStaticList theCalculableElectroStaticList, 
            ISbffIntraCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculableElectroStaticList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }

    public SbffIntraElectroStaticEnergyFunction(IAtomContainer theMolecule, CalculableIntraElectroStaticList theCalculableElectroStaticList, 
            ISbffIntraCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableElectroStaticList = theCalculableElectroStaticList;
    }
    
    public CalculableIntraElectroStaticList getCalculableElectroStaticList() {
        return itsCalculableElectroStaticList;
    }

    public void setCalculableElectroStaticList(CalculableIntraElectroStaticList theCalculableElectroStaticList) {
        this.itsCalculableElectroStaticList = theCalculableElectroStaticList;
    }

    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;
        
        for (CalculableIntraElectroStaticSet theCalculableElectroStaticSet : this.itsCalculableElectroStaticList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableElectroStaticSet);

            theCalculableElectroStaticSet.setEnergy(theEnergy);
            
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.ELECTRO_STATIC_ENERGY_KEY, String.format("%.5f", this.itsEnergy));
        
        return this.itsEnergy;
    }

    private double __calculateEnergyFunction(CalculableIntraElectroStaticSet theCalculableElectroStaticSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableElectroStaticSet.getSecondAtomIndex());
        Double theDistance = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d());
        Double theFirstAtomMpeoeCharge = (Double) theFirstAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY);
        Double theSecondAtomMpeoeCharge = (Double) theSecondAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY);
        double theEnergy = this.CONSTANT * (theFirstAtomMpeoeCharge * theSecondAtomMpeoeCharge) / (theDistance * this.itsCalculationParameter.getDielectricConstant());

        return theEnergy;
    }

    @Override
    public void calculateGradient() {
        for (CalculableIntraElectroStaticSet theCalculableElectroStaticSet : this.itsCalculableElectroStaticList) {
            double theGradient = this.__calculateGradient(theCalculableElectroStaticSet);

            theCalculableElectroStaticSet.setGradient(theGradient);
        }
    }

    private double __calculateGradient(CalculableIntraElectroStaticSet theElectroStaticSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theElectroStaticSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theElectroStaticSet.getSecondAtomIndex());
        Double theDistance = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d());
        Double theFirstAtomMpeoeCharge = (Double) theFirstAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY);
        Double theSecondAtomMpeoeCharge = (Double) theSecondAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY);

        return -this.CONSTANT * (theFirstAtomMpeoeCharge * theSecondAtomMpeoeCharge) / (theDistance * theDistance * this.itsCalculationParameter.getDielectricConstant());
    }
    
    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableIntraElectroStaticSet theElectroStaticSet : this.itsCalculableElectroStaticList) {
            this.__calculateGradientVector(theElectroStaticSet);
        }
    }

    private void __calculateGradientVector(CalculableIntraElectroStaticSet theElectroStaticSet) {
        if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theElectroStaticSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE))) {
        this.__calculateGradientVector(theElectroStaticSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE);
        }
        
        if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theElectroStaticSet.getAtomIndexList()
                .get(AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE))) {
        this.__calculateGradientVector(theElectroStaticSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE);
        }
    }

    private void __calculateGradientVector(CalculableIntraElectroStaticSet theElectroStaticSet, int theDerivativeAtomIndex, int theCounterAtomIndex) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theElectroStaticSet.getAtomIndexList(),
                theDerivativeAtomIndex, theElectroStaticSet.getGradient());
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theElectroStaticSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theElectroStaticSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setGradient(theAtomNumber, theAtomGradientVector);
        }
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableIntraElectroStaticSet theElectroStaticSet : this.itsCalculableElectroStaticList) {
            this.__reloadConjugatedGradientVector(theElectroStaticSet);
        }
    }
    
    private void __reloadConjugatedGradientVector(CalculableIntraElectroStaticSet theElectroStaticSet) {
        this.__reloadConjugatedGradientVector(theElectroStaticSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE);
        this.__reloadConjugatedGradientVector(theElectroStaticSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE);
    }

    private void __reloadConjugatedGradientVector(CalculableIntraElectroStaticSet theElectroStaticSet, int theDerivativeAtomIndex, int theCounterAtomIndex) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theElectroStaticSet.getAtomIndexList(),
                theDerivativeAtomIndex, theElectroStaticSet.getConjugatedGradient());
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theElectroStaticSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theElectroStaticSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setConjugatedGradient(theAtomNumber, theAtomGradientVector);
        }
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableElectroStaticList.makeConjugatedGradient(theScalingFactor);
    }
}