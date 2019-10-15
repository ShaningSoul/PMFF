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
import org.bmdrc.basicchemistry.tool.AngleCalculator;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableBendingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableBendingSet;
import org.bmdrc.sbff.parameter.intraenergy.BendingParameter;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffBendingEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction implements Serializable {

    private static final long serialVersionUID = -5802241914920156439L;

    private CalculableBendingList itsCalculableBendingList;
    //constant String variable
    private final String BENDING_ENERGY_KEY = "Bending Energy";
    private final String BENDING_GRADIENT_KEY = "Bending Gradient";

    public SbffBendingEnergyFunction(IAtomContainer theMolecule, CalculableBendingList theCalculableBendingList) {
        this(theMolecule, theCalculableBendingList, new SbffIntraCalculationParameter());
    }

    public SbffBendingEnergyFunction(IAtomContainer theMolecule, CalculableBendingList theCalculableBendingList, 
            ISbffIntraCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculableBendingList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }
    
    public SbffBendingEnergyFunction(IAtomContainer theMolecule, CalculableBendingList theCalculableBendingList, 
            ISbffIntraCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableBendingList = theCalculableBendingList;
    }
    
    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;

        for (CalculableBendingSet theCalculableBendingSet : this.itsCalculableBendingList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableBendingSet);

            if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().containsAll(theCalculableBendingSet.getAtomIndexList())) {
                theCalculableBendingSet.setEnergy(theEnergy);
                this.itsEnergy += theEnergy;
            }
        }

        this.itsMolecule.setProperty(this.BENDING_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private Double __calculateEnergyFunction(CalculableBendingSet theCalculableBendingSet) {
        IAtom theFirstAtom = this.getMolecule().getAtom(theCalculableBendingSet.getFirstAtomIndex());
        IAtom theCenterAtom = this.getMolecule().getAtom(theCalculableBendingSet.getCenterAtomIndex());
        IAtom theSecondAtom = this.getMolecule().getAtom(theCalculableBendingSet.getSecondAtomIndex());
        Double theAngle = Math.toDegrees(AngleCalculator.calculateBondAngle(theFirstAtom, theCenterAtom, theSecondAtom));
        BendingParameter theParameter = theCalculableBendingSet.getParameter();
        Double theThetaZero = theParameter.getStandardAngle();
        Double theKTheta = theParameter.getBendingConstant();
        Double theAngleDifference = theAngle - theThetaZero;
        double theEnergy = 0.021914 * theKTheta * Math.pow(theAngleDifference, 2) * (1.0 - 0.014 * theAngleDifference + 5.6e-5
                * Math.pow(theAngleDifference, 2.0) - 7.0e-7 * Math.pow(theAngleDifference, 3.0) + 9.0e-10 * Math.pow(theAngleDifference, 4.0));

        return theEnergy;
    }

    @Override
    public void calculateGradient() {
        for (CalculableBendingSet theCalculableBendingSet : this.itsCalculableBendingList) {
            double theGradient = this.__calculateGradient(theCalculableBendingSet);

            theCalculableBendingSet.setGradient(theGradient);
        }
    }

    private double __calculateGradient(CalculableBendingSet theBendingSet) {
        double theAngle = AngleCalculator.calculateBondAngle(this.itsMolecule.getAtom(theBendingSet.getFirstAtomIndex()),
                this.itsMolecule.getAtom(theBendingSet.getCenterAtomIndex()),
                this.itsMolecule.getAtom(theBendingSet.getSecondAtomIndex()));
        double theAngleDifference = Math.toDegrees(theAngle)
                - theBendingSet.getParameter().getStandardAngle();
        double theGradient = 0.021914 * theBendingSet.getParameter().getBendingConstant() * theAngleDifference
                * (2.0 - 0.042 * theAngleDifference + 22.4e-5 * Math.pow(theAngleDifference, 2.0)
                - 35.0e-7 * Math.pow(theAngleDifference, 3.0) + 54.0e-10 * Math.pow(theAngleDifference, 4.0));

        return theGradient;
    }

    @Override
    public Vector calculateGradientVector(double theScalingFactor) {
        Vector theCopiedGradientVector = new Vector(this.itsGradientVector);
        
        for (CalculableBendingSet theBendingSet : this.itsCalculableBendingList) {
            this.__calculateGradientVector(theBendingSet, theScalingFactor);
        }
        
        Vector theResult = new Vector(this.itsGradientVector);
        
        this.itsGradientVector = theCopiedGradientVector;
        
        return theResult;
    }

    
    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableBendingSet theBendingSet : this.itsCalculableBendingList) {
            this.__calculateGradientVector(theBendingSet, 1.0);
        }
    }

    private void __calculateGradientVector(CalculableBendingSet theBendingSet, double theScalingFactor) {
        double theGradient = theBendingSet.getGradient() * theScalingFactor;

        this.__calculateGradientVector(theBendingSet, AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_BEND_ANGLE, theGradient);
        this.__calculateGradientVector(theBendingSet, AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_BEND_ANGLE, theGradient);
    }

    private void __calculateGradientVector(CalculableBendingSet theBendingSet, int theDerivativeAtomIndex, double theGradient) {
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theBendingSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theBendingSet.getCenterAtomIndex());
        
        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theAtomNumber)) {
                this._setGradient(theAtomNumber, this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInBendAngle(this.itsMolecule, theBendingSet.getAtomIndexList(),
                        theDerivativeAtomIndex, theAtomNumber, theGradient));
            }
        }
    }

    @Override
    public Vector calculateConjugatedGradientVector(double theScalingFactor) {
        Vector theCopiedConjugatedGradientVector = new Vector(this.itsConjugatedGradientVector);
        
        this._initializeConjugatedGradientVector();

        for (CalculableBendingSet theBendingSet : this.itsCalculableBendingList) {
            this.__reloadConjugatedGradientVector(theBendingSet, theScalingFactor);
        }
        
        Vector theResult = new Vector(this.itsConjugatedGradientVector);
        
        this.itsConjugatedGradientVector = theCopiedConjugatedGradientVector;
        
        return theResult;
    }

    
    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableBendingSet theBendingSet : this.itsCalculableBendingList) {
            this.__reloadConjugatedGradientVector(theBendingSet, 1.0);
        }
    }

    private void __reloadConjugatedGradientVector(CalculableBendingSet theBendingSet, double theScalingFactor) {
        double theGradient = theBendingSet.getConjugatedGradient() * theScalingFactor;

        this.__reloadConjugatedGradientVector(theBendingSet, AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_BEND_ANGLE, theGradient);
        this.__reloadConjugatedGradientVector(theBendingSet, AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_BEND_ANGLE, theGradient);
    }

    private void __reloadConjugatedGradientVector(CalculableBendingSet theBendingSet, int theDerivativeAtomIndex, double theGradient) {
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theBendingSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theBendingSet.getCenterAtomIndex());

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theAtomNumber)) {
                this._setConjugatedGradient(theAtomNumber, this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInBendAngle(this.itsMolecule, theBendingSet.getAtomIndexList(),
                        theDerivativeAtomIndex, theAtomNumber, theGradient));
            }
        }
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableBendingList.makeConjugatedGradient(theScalingFactor);
    }
}