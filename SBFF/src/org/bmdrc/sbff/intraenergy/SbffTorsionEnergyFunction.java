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
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableTorsionList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableTorsionSet;
import org.bmdrc.sbff.parameter.intraenergy.TorsionParameter;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffTorsionEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction implements Serializable {

    private static final long serialVersionUID = -3092892060390639737L;

    private CalculableTorsionList itsCalculableTorsionList;
    //constant String variable
    private final String TORSION_ENERGY_KEY = "Torsion Energy";

    public SbffTorsionEnergyFunction(IAtomContainer theMolecule, CalculableTorsionList theCalculableTorsionList) {
        this(theMolecule, theCalculableTorsionList, new SbffIntraCalculationParameter());
    }

    public SbffTorsionEnergyFunction(IAtomContainer theMolecule, CalculableTorsionList theCalculableTorsionList, 
            ISbffIntraCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculableTorsionList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }
    
    public SbffTorsionEnergyFunction(IAtomContainer theMolecule, CalculableTorsionList theCalculableTorsionList,
            ISbffIntraCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableTorsionList = theCalculableTorsionList;
    }
    
    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;

        for (CalculableTorsionSet theCalculableTorsionSet : this.itsCalculableTorsionList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableTorsionSet);

            theCalculableTorsionSet.setEnergy(theEnergy);
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.TORSION_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private Double __calculateEnergyFunction(CalculableTorsionSet theCalculableTorsionSet) {
        IAtom theFirstConnectedAtom = this.itsMolecule.getAtom(theCalculableTorsionSet.getFirstConnectedAtomIndex());
        IAtom theFirstAtom = this.itsMolecule.getAtom(theCalculableTorsionSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theCalculableTorsionSet.getSecondAtomIndex());
        IAtom theSecondConnectedAtom = this.itsMolecule.getAtom(theCalculableTorsionSet.getSecondConnectedAtomIndex());
        TorsionParameter theParameter = theCalculableTorsionSet.getParameter();
        Double theTorsionalAngle = AngleCalculator.calculateTorsionAngle(theFirstConnectedAtom, theFirstAtom, theSecondAtom, theSecondConnectedAtom);

        double theEnergy = (theParameter.getV1() / 2.0) * (1 + Math.cos(theTorsionalAngle)) + (theParameter.getV2() / 2.0) * (1 - Math.cos(2 * theTorsionalAngle))
                + (theParameter.getV3() / 2.0) * (1 + Math.cos(3 * theTorsionalAngle));

        return theEnergy;
    }

    @Override
    public void calculateGradient() {
        for (CalculableTorsionSet theCalculableTorsionSet : this.itsCalculableTorsionList) {
            double theGradient = this.__calculateGradient(theCalculableTorsionSet);

            theCalculableTorsionSet.setGradient(theGradient);
        }
    }

    private double __calculateGradient(CalculableTorsionSet theTorsionSet) {
        double theTorsionAngle = AngleCalculator.calculateTorsionAngle(this.itsMolecule.getAtom(theTorsionSet.getFirstConnectedAtomIndex()),
                this.itsMolecule.getAtom(theTorsionSet.getFirstAtomIndex()), this.itsMolecule.getAtom(theTorsionSet.getSecondAtomIndex()),
                this.itsMolecule.getAtom(theTorsionSet.getSecondConnectedAtomIndex()));

        return Math.toRadians(-Math.sin(theTorsionAngle) * theTorsionSet.getParameter().getV1() / 2.0
                + theTorsionSet.getParameter().getV2() * Math.sin(2.0 * theTorsionAngle)
                - theTorsionSet.getParameter().getV3() * Math.sin(3.0 * theTorsionAngle) * 1.5);
    }

    @Override
    public Vector calculateGradientVector(double theScalingFactor) {
        Vector theCopy = new Vector(this.itsGradientVector);
        
        this._initializeGradientVector();
        
        for (CalculableTorsionSet theTorsionSet : this.itsCalculableTorsionList) {
            this.__calculateGradientVector(theTorsionSet, theScalingFactor);
        }
        
        Vector theResult = new Vector(this.itsGradientVector);
        
        this.itsGradientVector = theCopy;
        
        return theResult;
    }
    
    
    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();
        
        for (CalculableTorsionSet theTorsionSet : this.itsCalculableTorsionList) {
            this.__calculateGradientVector(theTorsionSet, 1.0);
        }
    }

    private void __calculateGradientVector(CalculableTorsionSet theTorsionSet, double theScalingFactor) {
        double theGradient = theTorsionSet.getGradient() * theScalingFactor;

        this.__calculateGradientVector(theTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE, 
                AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE, theGradient);
        this.__calculateGradientVector(theTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE, 
                AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE, theGradient);
    }

    private void __calculateGradientVector(CalculableTorsionSet theTorsionSet, int theDerivativeAtomIndex, int theCounterAtomIndex, double theGradient) {
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(
                theTorsionSet.getAtomIndexList().get(theDerivativeAtomIndex), theTorsionSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setGradient(theAtomNumber, this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInTorsionAngle(this.itsMolecule, theTorsionSet.getAtomIndexList(),
                    theDerivativeAtomIndex, theAtomNumber, theGradient));
        }
    }

    @Override
    public Vector calculateConjugatedGradientVector(double theScalingFactor) {
        Vector theCopy = new Vector(this.itsConjugatedGradientVector);
        
        this._initializeConjugatedGradientVector();
        
        for (CalculableTorsionSet theTorsionSet : this.itsCalculableTorsionList) {
            this.__reloadConjugatedGradientVector(theTorsionSet, theScalingFactor);
        }
        
        Vector theResult = new Vector(this.itsConjugatedGradientVector);
        
        this.itsConjugatedGradientVector = theCopy;
        
        return theResult;
    }
    
    
    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();
        
        for (CalculableTorsionSet theTorsionSet : this.itsCalculableTorsionList) {
            this.__reloadConjugatedGradientVector(theTorsionSet, 1.0);
        }
    }
    
    private void __reloadConjugatedGradientVector(CalculableTorsionSet theTorsionSet, double theScalingFactor) {
        double theGradient = theTorsionSet.getConjugatedGradient() * theScalingFactor;

        this.__reloadConjugatedGradientVector(theTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE, 
                AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE, theGradient);
        this.__reloadConjugatedGradientVector(theTorsionSet, AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE, 
                AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE, theGradient);
    }

    private void __reloadConjugatedGradientVector(CalculableTorsionSet theTorsionSet, int theDerivativeAtomIndex, int theCounterAtomIndex, double theGradient) {
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(
                theTorsionSet.getAtomIndexList().get(theDerivativeAtomIndex), theTorsionSet.getAtomIndexList().get(theCounterAtomIndex));

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setConjugatedGradient(theAtomNumber, this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInTorsionAngle(this.itsMolecule, theTorsionSet.getAtomIndexList(),
                    theDerivativeAtomIndex, theAtomNumber, theGradient));
        }
    }
    
    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableTorsionList.makeConjugatedGradient(theScalingFactor);
    }
}