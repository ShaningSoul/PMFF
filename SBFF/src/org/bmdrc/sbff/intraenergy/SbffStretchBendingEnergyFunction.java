/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.intraenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractDerivativeFunctionOfInternalCoordinate;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.tool.AngleCalculator;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchBendingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchBendingSet;
import org.bmdrc.sbff.parameter.intraenergy.BendingParameter;
import org.bmdrc.sbff.parameter.intraenergy.StretchBendParameter;
import org.bmdrc.sbff.parameter.intraenergy.StretchParameter;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffStretchBendingEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction implements Serializable {

    private static final long serialVersionUID = 8523240966587473011L;

    private CalculableStretchBendingList itsCalculableStretchBendingList;
    //constant String variable
    private final String STRETCH_BENDING_ENERGY_KEY = "Stretch-Bending Energy";

    public SbffStretchBendingEnergyFunction(IAtomContainer theMolecule, CalculableStretchBendingList theCalculableStretchBendingList) {
        this(theMolecule, theCalculableStretchBendingList, new SbffIntraCalculationParameter(), new MoveTogetherAtomNumberList(theMolecule));
    }
    
    public SbffStretchBendingEnergyFunction(IAtomContainer theMolecule, CalculableStretchBendingList theCalculableStretchBendingList,
            ISbffIntraCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculableStretchBendingList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }

    public SbffStretchBendingEnergyFunction(IAtomContainer theMolecule, CalculableStretchBendingList theCalculableStretchBendingList, 
            ISbffIntraCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableStretchBendingList = theCalculableStretchBendingList;
    }
    
    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;

        for (CalculableStretchBendingSet theCalculableStretchBendingSet : this.itsCalculableStretchBendingList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableStretchBendingSet);

            theCalculableStretchBendingSet.setEnergy(theEnergy);
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.STRETCH_BENDING_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private Double __calculateEnergyFunction(CalculableStretchBendingSet theCalculableStretchBendingSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableStretchBendingSet.getFirstAtomIndex());
        IAtom theCenterAtom = this.itsMolecule.getAtom(theCalculableStretchBendingSet.getCenterAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableStretchBendingSet.getSecondAtomIndex());
        StretchBendParameter theStretchBendParameter = theCalculableStretchBendingSet.getStretchBendParameter();
        StretchParameter theFirstStretchParameter = theCalculableStretchBendingSet.getFirstStretchParameter();
        StretchParameter theSecondStretchParameter = theCalculableStretchBendingSet.getSecondStretchParameter();
        BendingParameter theBendingParameter = theCalculableStretchBendingSet.getBendingParameter();
        Double theFirstDistanceDifference = theCenterAtom.getPoint3d().distance(theFirstAtom.getPoint3d()) - theFirstStretchParameter.getBondLength();
        Double theSecondDistanceDifference = theCenterAtom.getPoint3d().distance(theSecondAtom.getPoint3d()) - theSecondStretchParameter.getBondLength();
        double theBondAngle = Math.toDegrees(AngleCalculator.calculateBondAngle(theFirstAtom, theCenterAtom, theSecondAtom));

        return 2.51118 * theStretchBendParameter.getConstant() * (theFirstDistanceDifference + theSecondDistanceDifference)
                * (theBondAngle - theBendingParameter.getStandardAngle());
    }

    @Override
    public void calculateGradient() {
        for (CalculableStretchBendingSet theCalculableStretchBendingSet : this.itsCalculableStretchBendingList) {
            this.__calculateGradient(theCalculableStretchBendingSet);
        }
    }

    private void __calculateGradient(CalculableStretchBendingSet theStretchBendingSet) {
        double theDistanceGradient = this.__calculateDistanceGradient(theStretchBendingSet);
        double theAngleGradient = this.__calculateAngleGradient(theStretchBendingSet);
        
        theStretchBendingSet.setDistanceGradient(theDistanceGradient);
        theStretchBendingSet.setAngleGradient(theAngleGradient);
    }

    private double __calculateDistanceGradient(CalculableStretchBendingSet theStretchBendingSet) {
        double theBondAngle = AngleCalculator.calculateBondAngle(this.itsMolecule.getAtom(theStretchBendingSet.getFirstAtomIndex()),
                this.itsMolecule.getAtom(theStretchBendingSet.getCenterAtomIndex()), this.itsMolecule.getAtom(theStretchBendingSet.getSecondAtomIndex()));
        double theAngleDifference = Math.toDegrees(theBondAngle) - theStretchBendingSet.getBendingParameter().getStandardAngle();

        return 2.51118 * theStretchBendingSet.getStretchBendParameter().getConstant() * theAngleDifference;
    }

    private double __calculateAngleGradient(CalculableStretchBendingSet theStretchBendingSet) {
        double theSumOfDistanceDifference = this.__getSumOfDistanceDifference(theStretchBendingSet);

        return 2.0 * 2.51118 * theStretchBendingSet.getStretchBendParameter().getConstant() * theSumOfDistanceDifference;
    }

    private double __getSumOfDistanceDifference(CalculableStretchBendingSet theStretchBendingSet) {
        double theDistanceDifference = 0.0;

        theDistanceDifference += new Vector3d(this.itsMolecule.getAtom(theStretchBendingSet.getFirstAtomIndex()),
                this.itsMolecule.getAtom(theStretchBendingSet.getCenterAtomIndex())).length() - theStretchBendingSet.getFirstStretchParameter().getBondLength();
        theDistanceDifference += new Vector3d(this.itsMolecule.getAtom(theStretchBendingSet.getSecondAtomIndex()),
                this.itsMolecule.getAtom(theStretchBendingSet.getCenterAtomIndex())).length() - theStretchBendingSet.getSecondStretchParameter().getBondLength();

        return theDistanceDifference;
    }

    @Override
    public Vector calculateGradientVector(double theScalingFactor) {
        Vector theCopiedGradientVector = new Vector(this.itsGradientVector);
        
        this._initializeGradientVector();

        for (CalculableStretchBendingSet theStretchBendingSet : this.itsCalculableStretchBendingList) {
            this.__calculateGradientVector(theStretchBendingSet, theScalingFactor);
        }
        
        Vector theResult = new Vector(this.itsGradientVector);
        
        this.itsGradientVector = theCopiedGradientVector;
        
        return theResult;
    }
    
    
    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableStretchBendingSet theStretchBendingSet : this.itsCalculableStretchBendingList) {
            this.__calculateGradientVector(theStretchBendingSet, 1.0);
        }
    }

    private void __calculateGradientVector(CalculableStretchBendingSet theStretchBendingSet, double theScalingFactor) {
        double theDistanceGradient = theStretchBendingSet.getDistanceGradient() * theScalingFactor;
        double theAngleGradient = theStretchBendingSet.getAngleGradient() * theScalingFactor;

        this.__calculateGradientVectorInSideAtom(theStretchBendingSet, AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_BEND_ANGLE,
                theDistanceGradient, theAngleGradient);
        this.__calculateGradientVectorInCenterAtom(theStretchBendingSet, theDistanceGradient);
        this.__calculateGradientVectorInSideAtom(theStretchBendingSet, AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_BEND_ANGLE,
                theDistanceGradient, theAngleGradient);
    }

    private void __calculateGradientVectorInSideAtom(CalculableStretchBendingSet theStretchBendingSet, int theAngleDerivativeAtomIndex, double theDistanceGradient,
            double theAngleGradient) {
        Vector3d theDistanceGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule,
                theStretchBendingSet.getAtomIndexlistByBond(theAngleDerivativeAtomIndex), AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE, theDistanceGradient);
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(
                theStretchBendingSet.getAtomIndexlistByBond(theAngleDerivativeAtomIndex).get(AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE), theStretchBendingSet.getCenterAtomIndex());

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            Vector3d theAngleGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInBendAngle(this.itsMolecule, theStretchBendingSet.getAtomIndexList(),
                    theAngleDerivativeAtomIndex, theAtomNumber, theAngleGradient);
            Vector3d theTotalGradientVector = Vector3dCalculator.sum(theDistanceGradientVector, theAngleGradientVector);

            this._setGradient(theAtomNumber, theTotalGradientVector);
        }
    }

    private void __calculateGradientVectorInCenterAtom(CalculableStretchBendingSet theStretchBendingSet, double theDistanceGradient) {
        Vector3d theDistanceGradientVector = Vector3dCalculator.sum(this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theStretchBendingSet.getAtomIndexListInFirstBend(),
                AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE, theDistanceGradient), this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theStretchBendingSet.getAtomIndexListInSecondBend(),
                        AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE, theDistanceGradient));
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theStretchBendingSet.getCenterAtomIndex(), theStretchBendingSet.getFirstAtomIndex());
        List<Integer> theNotMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theStretchBendingSet.getCenterAtomIndex(), theStretchBendingSet.getSecondAtomIndex());

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            if (theNotMoveTogetherAtomNumberList.contains(theAtomNumber)) {
                this._setGradient(theAtomNumber, theDistanceGradientVector);
            }
        }
    }

    @Override
    public Vector calculateConjugatedGradientVector(double theScalingFactor) {
        Vector theCopy = new Vector(this.itsConjugatedGradientVector);
        
        this._initializeConjugatedGradientVector();

        for (CalculableStretchBendingSet theStretchBendingSet : this.itsCalculableStretchBendingList) {
            this.__reloadConjugatedGradientVector(theStretchBendingSet, theScalingFactor);
        }
        
        Vector theResult = new Vector(this.itsConjugatedGradientVector);
        
        this.itsConjugatedGradientVector = theCopy;
        
        return theResult;
    }
    
    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableStretchBendingSet theStretchBendingSet : this.itsCalculableStretchBendingList) {
            this.__reloadConjugatedGradientVector(theStretchBendingSet, 1.0);
        }
    }

    private void __reloadConjugatedGradientVector(CalculableStretchBendingSet theStretchBendingSet, double theScalingFactor) {
        double theDistanceGradient = theStretchBendingSet.getDistanceConjugatedGradient() * theScalingFactor;
        double theAngleGradient = theStretchBendingSet.getAngleConjugatedGradient() * theScalingFactor;

        this.__reloadConjugatedGradientVectorInSideAtom(theStretchBendingSet, AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_BEND_ANGLE,
                theDistanceGradient * theScalingFactor, theAngleGradient);
        this.__reloadConjugatedGradientVectorInCenterAtom(theStretchBendingSet, theDistanceGradient);
        this.__reloadConjugatedGradientVectorInSideAtom(theStretchBendingSet, AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_BEND_ANGLE,
                theDistanceGradient * theScalingFactor, theAngleGradient);
    }

    private void __reloadConjugatedGradientVectorInSideAtom(CalculableStretchBendingSet theStretchBendingSet, int theDerivativeAtomIndex, double theDistanceGradient,
            double theAngleGradient) {
        Vector3d theDistanceGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule,
                theStretchBendingSet.getAtomIndexlistByBond(theDerivativeAtomIndex), AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE, theDistanceGradient);
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(
                theStretchBendingSet.getAtomIndexList().get(theDerivativeAtomIndex), theStretchBendingSet.getCenterAtomIndex());

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            Vector3d theAngleGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInBendAngle(this.itsMolecule, theStretchBendingSet.getAtomIndexList(),
                    theDerivativeAtomIndex, theAtomNumber, theAngleGradient);
            Vector3d theTotalGradientVector = Vector3dCalculator.sum(theDistanceGradientVector, theAngleGradientVector);

            this._setConjugatedGradient(theAtomNumber, theTotalGradientVector);
        }
    }

    private void __reloadConjugatedGradientVectorInCenterAtom(CalculableStretchBendingSet theStretchBendingSet, double theDistanceGradient) {
        Vector3d theDistanceGradientVector = Vector3dCalculator.sum(this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule,
                theStretchBendingSet.getAtomIndexListInFirstBend(), AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE, theDistanceGradient),
                this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theStretchBendingSet.getAtomIndexListInSecondBend(),
                        AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE, theDistanceGradient));
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(
                theStretchBendingSet.getCenterAtomIndex(), theStretchBendingSet.getFirstAtomIndex());
        List<Integer> theNotMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(
                theStretchBendingSet.getCenterAtomIndex(), theStretchBendingSet.getSecondAtomIndex());

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            if (theNotMoveTogetherAtomNumberList.contains(theAtomNumber)) {
                this._setConjugatedGradient(theAtomNumber, theDistanceGradientVector);
            }
        }
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableStretchBendingList.makeConjugatedGradient(theScalingFactor);
    }
}