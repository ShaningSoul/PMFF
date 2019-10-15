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
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchTorsionList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchTorsionSet;
import org.bmdrc.sbff.parameter.intraenergy.StretchParameter;
import org.bmdrc.sbff.parameter.intraenergy.StretchTorsionParameter;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffStretchTorsionEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction implements Serializable {

    private static final long serialVersionUID = -5002969923756600725L;

    private CalculableStretchTorsionList itsCalculableStretchTorsionList;
    //constant String variable
    private final String STRETCH_TORSION_ENERGY_KEY = "Stretch-Torsion Energy";
    //constant Integer variable
    private static final int FIRST_CONNECTED_ATOM_INDEX = 0;
    private static final int FIRST_ATOM_INDEX = 1;
    private static final int SECOND_ATOM_INDEX = 2;
    private static final int SECOND_CONNECTED_ATOM_INDEX = 3;

    public SbffStretchTorsionEnergyFunction(IAtomContainer theMolecule, CalculableStretchTorsionList theCalculableStretchTorsionList) {
        this(theMolecule, theCalculableStretchTorsionList, new SbffIntraCalculationParameter());
    }

    public SbffStretchTorsionEnergyFunction(IAtomContainer theMolecule, CalculableStretchTorsionList theCalculableStretchTorsionList, 
            ISbffIntraCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculableStretchTorsionList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }

    public SbffStretchTorsionEnergyFunction(IAtomContainer theMolecule, CalculableStretchTorsionList theCalculableStretchTorsionList,
            ISbffIntraCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableStretchTorsionList = theCalculableStretchTorsionList;
    }

    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;

        for (CalculableStretchTorsionSet theCalculableStretchTorsionSet : this.itsCalculableStretchTorsionList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableStretchTorsionSet);

            theCalculableStretchTorsionSet.setEnergy(theEnergy);
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.STRETCH_TORSION_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private Double __calculateEnergyFunction(CalculableStretchTorsionSet theStretchTorsionSet) {
        IAtom theFirstConnectedAtom = this.itsMolecule.getAtom(theStretchTorsionSet.getFirstConnectedAtomIndex());
        IAtom theFirstAtom = this.itsMolecule.getAtom(theStretchTorsionSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theStretchTorsionSet.getSecondAtomIndex());
        IAtom theSecondConnectedAtom = this.itsMolecule.getAtom(theStretchTorsionSet.getSecondConnectedAtomIndex());
        Double theTorsionalAngle = AngleCalculator.calculateTorsionAngle(theFirstConnectedAtom, theFirstAtom, theSecondAtom, theSecondConnectedAtom);
        StretchTorsionParameter theStretchTorsionParameter = theStretchTorsionSet.getStretchTorsionParameter();
        StretchParameter theStretchParameter = theStretchTorsionSet.getStretchParameter();
        double theDistanceDifference = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d()) - theStretchParameter.getBondLength();

        if (theStretchTorsionParameter == null) {
            return 0.0;
        }
        return -11.995 * (theStretchTorsionParameter.getConstant() / 2.0) * theDistanceDifference * (1 + Math.cos(3 * theTorsionalAngle));
    }

    @Override
    public void calculateGradient() {
        for (CalculableStretchTorsionSet theCalculableStretchTorsionSet : this.itsCalculableStretchTorsionList) {
            this.__calculateGradient(theCalculableStretchTorsionSet);
        }
    }

    private void __calculateGradient(CalculableStretchTorsionSet theStretchTorsionSet) {
        double theDistanceGradient = this.__calculateDistanceGradient(theStretchTorsionSet);
        double theAngleGradient = this.__calculateAngleGradient(theStretchTorsionSet);

        theStretchTorsionSet.setDistanceGradient(theDistanceGradient);
        theStretchTorsionSet.setAngleGradient(theAngleGradient);
    }

    private double __calculateDistanceGradient(CalculableStretchTorsionSet theStretchTorsionSet) {
        double theTorsionalAngle = this.__getTorsionAngle(theStretchTorsionSet);

        return 11.995 * (theStretchTorsionSet.getStretchTorsionParameter().getConstant() / 2.0) * (1 + Math.cos(3 * theTorsionalAngle));
    }

    private double __calculateAngleGradient(CalculableStretchTorsionSet theStretchTorsionSet) {
        double theDistanceDifference = this.__getDistanceDifference(theStretchTorsionSet);
        double theTorsionAngle = this.__getTorsionAngle(theStretchTorsionSet);

        return Math.toRadians(11.995 * (theStretchTorsionSet.getStretchTorsionParameter().getConstant() / 2.0) * theDistanceDifference * (-3.0 * Math.sin(3.0 * theTorsionAngle)));
    }

    private double __getDistanceDifference(CalculableStretchTorsionSet theStretchTorsionSet) {
        double theDistance = new Vector3d(this.itsMolecule.getAtom(theStretchTorsionSet.getFirstAtomIndex()), this.itsMolecule.getAtom(theStretchTorsionSet.getSecondAtomIndex())).length();

        return theDistance - theStretchTorsionSet.getStretchParameter().getBondLength();
    }

    private double __getTorsionAngle(CalculableStretchTorsionSet theStretchTorsionSet) {
        return AngleCalculator.calculateTorsionAngle(this.itsMolecule.getAtom(theStretchTorsionSet.getFirstConnectedAtomIndex()),
                this.itsMolecule.getAtom(theStretchTorsionSet.getFirstAtomIndex()), this.itsMolecule.getAtom(theStretchTorsionSet.getSecondAtomIndex()),
                this.itsMolecule.getAtom(theStretchTorsionSet.getSecondConnectedAtomIndex()));
    }

    @Override
    public Vector calculateGradientVector(double theScalingFactor) {
        Vector theCopy = new Vector(this.itsGradientVector);

        this._initializeGradientVector();

        for (CalculableStretchTorsionSet theStretchTorsionSet : this.itsCalculableStretchTorsionList) {
            this.__calculateGradientVector(theStretchTorsionSet, theScalingFactor);
        }
        
        Vector theResult = new Vector(this.itsGradientVector);
        
        this.itsGradientVector = theCopy;
        
        return theResult;
    }

    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableStretchTorsionSet theStretchTorsionSet : this.itsCalculableStretchTorsionList) {
            this.__calculateGradientVector(theStretchTorsionSet, 1.0);
        }
    }

    private void __calculateGradientVector(CalculableStretchTorsionSet theStretchTorsionSet, double theScalingFactor) {
        double theAngleGradient = theStretchTorsionSet.getAngleGradient() * theScalingFactor;
        double theDistanceGradient = theStretchTorsionSet.getDistanceGradient() * theScalingFactor;

        this.__calculateGradientVectorInSideAtom(theStretchTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE,
                AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE, theAngleGradient);
        this.__calculateGradientVectorInCenterAtom(theStretchTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE,
                AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE, theDistanceGradient);
        this.__calculateGradientVectorInCenterAtom(theStretchTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE,
                AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE, theDistanceGradient);
        this.__calculateGradientVectorInSideAtom(theStretchTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE,
                AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE, theAngleGradient);
    }

    private void __calculateGradientVectorInSideAtom(CalculableStretchTorsionSet theStretchTorsionSet, int theDerivativeAtomIndex, int theCounterAtomIndex, double theAngleGradient) {
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(
                theStretchTorsionSet.getAtomIndexList().get(theDerivativeAtomIndex), theStretchTorsionSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            Vector3d theGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInTorsionAngle(this.itsMolecule, theStretchTorsionSet.getAtomIndexList(),
                    theDerivativeAtomIndex, theAtomNumber, theAngleGradient);

            this._setGradient(theAtomNumber, theGradientVector);
        }
    }

    private void __calculateGradientVectorInCenterAtom(CalculableStretchTorsionSet theStretchTorsionSet, int theDerivativeAtomIndex, int theCounterAtomIndex,
            double theDistanceGradient) {
        Vector3d theGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theStretchTorsionSet.getDistanceAtomIndexList(),
                theStretchTorsionSet.getDistanceAtomIndexList().indexOf(theStretchTorsionSet.getAtomIndexList().get(theDerivativeAtomIndex)), theDistanceGradient);
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(
                theStretchTorsionSet.getAtomIndexList().get(theDerivativeAtomIndex), theStretchTorsionSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setGradient(theAtomNumber, theGradientVector);
        }
    }

    @Override
    public Vector calculateConjugatedGradientVector(double theScalingFactor) {
        Vector theCopy = new Vector(this.itsConjugatedGradientVector);
        
        this._initializeConjugatedGradientVector();

        for (CalculableStretchTorsionSet theStretchTorsionSet : this.itsCalculableStretchTorsionList) {
            this.__reloadConjugatedGradientVector(theStretchTorsionSet, theScalingFactor);
        }
        
        Vector theResult = new Vector(this.itsConjugatedGradientVector);
        
        this.itsConjugatedGradientVector = theCopy;
        
        return theResult;
    }
    
    
    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableStretchTorsionSet theStretchTorsionSet : this.itsCalculableStretchTorsionList) {
            this.__reloadConjugatedGradientVector(theStretchTorsionSet, 1.0);
        }
    }

    private void __reloadConjugatedGradientVector(CalculableStretchTorsionSet theStretchTorsionSet, double theScalingFactor) {
        double theAngleGradient = theStretchTorsionSet.getConjugatedGradient() * theScalingFactor;
        double theDistanceGradient = theStretchTorsionSet.getConjugatedGradient() * theScalingFactor;

        this.__reloadConjugatedGradientVectorInSideAtom(theStretchTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE,
                AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE, theAngleGradient);
        this.__reloadConjugatedGradientVectorInCenterAtom(theStretchTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE,
                AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE, theDistanceGradient);
        this.__reloadConjugatedGradientVectorInCenterAtom(theStretchTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE,
                AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE, theDistanceGradient);
        this.__reloadConjugatedGradientVectorInSideAtom(theStretchTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE,
                AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE, theAngleGradient);
    }

    private void __reloadConjugatedGradientVectorInSideAtom(CalculableStretchTorsionSet theStretchTorsionSet, int theDerivativeAtomIndex, int theCounterAtomIndex,
            double theAngleGradient) {
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theStretchTorsionSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theStretchTorsionSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            Vector3d theGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInTorsionAngle(this.itsMolecule, theStretchTorsionSet.getAtomIndexList(),
                    theDerivativeAtomIndex, theAtomNumber, theAngleGradient);

            this._setConjugatedGradient(theAtomNumber, theGradientVector);
        }
    }

    private void __reloadConjugatedGradientVectorInCenterAtom(CalculableStretchTorsionSet theStretchTorsionSet, int theDerivativeAtomIndex, int theCounterAtomIndex,
            double theDistanceGradient) {
        Vector3d theGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theStretchTorsionSet.getDistanceAtomIndexList(),
                theStretchTorsionSet.getDistanceAtomIndexList().indexOf(theStretchTorsionSet.getAtomIndexList().get(theDerivativeAtomIndex)), theDistanceGradient);
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(
                theStretchTorsionSet.getAtomIndexList().get(theDerivativeAtomIndex), theStretchTorsionSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setConjugatedGradient(theAtomNumber, theGradientVector);
        }
    }@Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableStretchTorsionList.makeConjugatedGradient(theScalingFactor);
    }
}