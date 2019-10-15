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
import org.bmdrc.basicchemistry.energyfunction.interfaces.ICalculationParameter;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchSet;
import org.bmdrc.sbff.parameter.intraenergy.StretchParameter;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffStretchEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction implements Serializable {

    private static final long serialVersionUID = -3522465226146398769L;

    private CalculableStretchList itsCalculableStretchList;
    //constant String variable
    private final String STRETCH_ENERGY_KEY = "Stretch Energy";

    public SbffStretchEnergyFunction(IAtomContainer theMolecule, CalculableStretchList theCalculableStretchList) {
        this(theMolecule, theCalculableStretchList, new MoveTogetherAtomNumberList(theMolecule));
    }

    public SbffStretchEnergyFunction(IAtomContainer theMolecule, CalculableStretchList theCalculableStretchList, 
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, theCalculableStretchList, new SbffIntraCalculationParameter(), theMoveTogetherAtomNumberMap);
    }
    
    public SbffStretchEnergyFunction(IAtomContainer theMolecule, CalculableStretchList theCalculableStretchList, 
            ICalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        this.itsCalculableStretchList = theCalculableStretchList;
    }
    
    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;
        
        for (CalculableStretchSet theCalculableStretchSet : this.itsCalculableStretchList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableStretchSet);
            
            theCalculableStretchSet.setEnergy(theEnergy);
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.STRETCH_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private Double __calculateEnergyFunction(CalculableStretchSet theStretchSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theStretchSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theStretchSet.getSecondAtomIndex());
        StretchParameter theParameter = theStretchSet.getParameter();
        Double theDifferenceBondLength = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d()) - theParameter.getBondLength();
        double theEnergy = 71.94 * theParameter.getConstant() * theDifferenceBondLength * theDifferenceBondLength
                * (1.0 - (2.55 * theDifferenceBondLength) + (7.0 / 12.0 * Math.pow(2.55 * theDifferenceBondLength, 2.0)));

        return theEnergy;
    }

    @Override
    public void calculateGradient() {
        for (CalculableStretchSet theStretchSet : this.itsCalculableStretchList) {
            double theGradient = this.__calculateGradient(theStretchSet);
            
            theStretchSet.setGradient(theGradient);
        }
    }

    private double __calculateGradient(CalculableStretchSet theStretchSet) {
        IAtom theFirstAtom = this.itsMolecule.getAtom(theStretchSet.getFirstAtomIndex());
        IAtom theSecondAtom = this.itsMolecule.getAtom(theStretchSet.getSecondAtomIndex());
        StretchParameter theParameter = theStretchSet.getParameter();
        double theDifferenceBondLength = theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d()) - theParameter.getBondLength();

        double theGradient = 71.94 * theParameter.getConstant() * theDifferenceBondLength
                * (2.0 - (7.65 * theDifferenceBondLength) + (7.0 / 3.0 * Math.pow(2.55 * theDifferenceBondLength, 2.0)));
        
        return theGradient;
    }
    
    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableStretchSet theStretchSet : this.itsCalculableStretchList) {
            this.__calculateGradientVector(theStretchSet);
        }
    }

    private void __calculateGradientVector(CalculableStretchSet theStretchSet) {
        this.__calculateGradientVector(theStretchSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE);
        this.__calculateGradientVector(theStretchSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE);
    }

    private void __calculateGradientVector(CalculableStretchSet theStretchSet, int theDerivativeAtomIndex, int theCounterAtomIndex) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theStretchSet.getAtomIndexList(),
                theDerivativeAtomIndex, theStretchSet.getGradient());
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theStretchSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theStretchSet.getAtomIndexList().get(theCounterAtomIndex));
        
        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setGradient(theAtomNumber, theAtomGradientVector);
        }
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableStretchSet theStretchSet : this.itsCalculableStretchList) {
            this.__reloadConjugatedGradientVector(theStretchSet);
        }
    }
    
    private void __reloadConjugatedGradientVector(CalculableStretchSet theStretchSet) {
        this.__reloadConjugatedGradientVector(theStretchSet, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE);
        this.__reloadConjugatedGradientVector(theStretchSet, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE);
    }

    private void __reloadConjugatedGradientVector(CalculableStretchSet theStretchSet, int theDerivativeAtomIndex, int theCounterAtomIndex) {
        Vector3d theAtomGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theStretchSet.getAtomIndexList(),
                theDerivativeAtomIndex, theStretchSet.getConjugatedGradient());
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theStretchSet.getAtomIndexList().get(theDerivativeAtomIndex),
                theStretchSet.getAtomIndexList().get(theCounterAtomIndex));
        
        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            this._setConjugatedGradient(theAtomNumber, theAtomGradientVector);
        }
    }
    
    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableStretchList.makeConjugatedGradient(theScalingFactor);
    }
}