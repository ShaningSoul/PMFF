/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.intraenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.tool.AngleCalculator;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.energyfunction.abstracts.AbstractIntraEnergyFunction;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableOutOfPlaneList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableOutOfPlaneSet;
import org.bmdrc.sbff.parameter.intraenergy.OutOfPlaneBendParameter;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffOutOfPlaneEnergyFunction extends AbstractIntraEnergyFunction implements Serializable {

    private static final long serialVersionUID = -5167097598939388353L;

    private CalculableOutOfPlaneList itsCalculableOutOfPlaneList;
    //constant String variable
    private final String OUT_OF_PLANE_ENERGY_KEY = "Out of Plane Energy";

    public SbffOutOfPlaneEnergyFunction(IAtomContainer theMolecule, CalculableOutOfPlaneList theCalculableOutOfPlaneList) {
        this(theMolecule, theCalculableOutOfPlaneList, new SbffIntraCalculationParameter());
    }

    public SbffOutOfPlaneEnergyFunction(IAtomContainer theMolecule, CalculableOutOfPlaneList theCalculableOutOfPlaneList, 
             ISbffIntraCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculableOutOfPlaneList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }

    public SbffOutOfPlaneEnergyFunction(IAtomContainer theMolecule, CalculableOutOfPlaneList theCalculableOutOfPlaneList, 
            ISbffIntraCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableOutOfPlaneList = theCalculableOutOfPlaneList;
    }

    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;

        for (CalculableOutOfPlaneSet theCalculableOutOfPlaneSet : this.itsCalculableOutOfPlaneList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableOutOfPlaneSet);

            theCalculableOutOfPlaneSet.setEnergy(theEnergy);
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.OUT_OF_PLANE_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private double __calculateEnergyFunction(CalculableOutOfPlaneSet theCalculableOutOfPlaneSet) {
        OutOfPlaneBendParameter theParameter = theCalculableOutOfPlaneSet.getParameter();
        List<IAtom> theAtomList = new ArrayList<>();

        theAtomList.add(this.itsMolecule.getAtom(theCalculableOutOfPlaneSet.getCenterAtomIndex()));
        theAtomList.add(this.itsMolecule.getAtom(theCalculableOutOfPlaneSet.getConnectedFirstAtomIndex()));
        theAtomList.add(this.itsMolecule.getAtom(theCalculableOutOfPlaneSet.getConnectedSecondAtomIndex()));
        theAtomList.add(this.itsMolecule.getAtom(theCalculableOutOfPlaneSet.getConnectedThirdAtomIndex()));

        double theImproperTorsionAngle = AngleCalculator.calculateTorsionAngle(theAtomList);
        double theEnergy = theParameter.getConstant() * Math.pow(theImproperTorsionAngle - theParameter.getAngle(), 2.0);

        return theEnergy;
    }

    @Override
    public void calculateGradient() {
        for (CalculableOutOfPlaneSet theCalculableOutOfPlaneSet : this.itsCalculableOutOfPlaneList) {
            double theGradient = this.__calculateGradient(theCalculableOutOfPlaneSet);

            theCalculableOutOfPlaneSet.setGradient(theGradient);
        }
    }

    private double __calculateGradient(CalculableOutOfPlaneSet theOutOfPlaneSet) {
        OutOfPlaneBendParameter theParameter = theOutOfPlaneSet.getParameter();
        List<IAtom> theAtomList = this.__getAtomListUsedToCalculateImporperTorsionAngle(theOutOfPlaneSet);

        double theImproperTorsionAngle = AngleCalculator.calculateTorsionAngle(theAtomList);

        return 2.0 * theParameter.getConstant() * (theImproperTorsionAngle - theParameter.getAngle());
    }

    private List<IAtom> __getAtomListUsedToCalculateImporperTorsionAngle(CalculableOutOfPlaneSet theOutOfPlaneSet) {
        List<IAtom> theAtomList = new ArrayList<>();

        theAtomList.add(this.itsMolecule.getAtom(theOutOfPlaneSet.getCenterAtomIndex()));
        theAtomList.add(this.itsMolecule.getAtom(theOutOfPlaneSet.getConnectedFirstAtomIndex()));
        theAtomList.add(this.itsMolecule.getAtom(theOutOfPlaneSet.getConnectedSecondAtomIndex()));
        theAtomList.add(this.itsMolecule.getAtom(theOutOfPlaneSet.getConnectedThirdAtomIndex()));

        return theAtomList;
    }

    @Override
    public Vector calculateGradientVector(double theScalingFactor) {
        Vector theCopiedGradientVector = new Vector(this.itsGradientVector);
        
        this._initializeGradientVector();

        for (CalculableOutOfPlaneSet theCalculableSet : this.itsCalculableOutOfPlaneList) {
            if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theCalculableSet.getCenterAtomIndex())) {
                this.__calculateGradientVector(theCalculableSet, theScalingFactor);
            }
        }
        
        Vector theResult = new Vector(this.itsGradientVector);
        
        this.itsGradientVector = theCopiedGradientVector;
        
        return theResult;
    }

    
    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableOutOfPlaneSet theCalculableSet : this.itsCalculableOutOfPlaneList) {
            if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theCalculableSet.getCenterAtomIndex())) {
                this.__calculateGradientVector(theCalculableSet, 1.0);
            }
        }
    }

    private void __calculateGradientVector(CalculableOutOfPlaneSet theCalculableSet, double theScalingFactor) {
        double theGradient = theCalculableSet.getGradient() * theScalingFactor;
        Vector3d theAtomGradient = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInTorsionAngle(this.itsMolecule, theCalculableSet.getAtomIndexList(),
                Constant.FIRST_INDEX, theCalculableSet.getCenterAtomIndex(), theGradient);

        this._setGradient(theCalculableSet.getCenterAtomIndex(), theAtomGradient);
    }

    @Override
    public Vector calculateConjugatedGradientVector(double theScalingFactor) {
        Vector theCopiedConjugatedGradientVector = new Vector(this.itsConjugatedGradientVector);
        
        this._initializeConjugatedGradientVector();

        for (CalculableOutOfPlaneSet theCalculableSet : this.itsCalculableOutOfPlaneList) {
            this.__calculateConjugatedGradientVector(theCalculableSet, 1.0);
        }
        
        Vector theResult = new Vector(this.itsConjugatedGradientVector);
        
        this.itsConjugatedGradientVector = theCopiedConjugatedGradientVector;
        
        return theResult;
    }

    
    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableOutOfPlaneSet theCalculableSet : this.itsCalculableOutOfPlaneList) {
            this.__calculateConjugatedGradientVector(theCalculableSet, 1.0);
        }
    }

    private void __calculateConjugatedGradientVector(CalculableOutOfPlaneSet theCalculableSet, double theScalingFactor) {
        double theGradient = theCalculableSet.getConjugatedGradient() * theScalingFactor;
        Vector3d theAtomGradient = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInTorsionAngle(this.itsMolecule, theCalculableSet.getAtomIndexList(),
                Constant.FIRST_INDEX, theCalculableSet.getCenterAtomIndex(), theGradient);

        this._setGradient(theCalculableSet.getCenterAtomIndex(), theAtomGradient);
    }@Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableOutOfPlaneList.makeConjugatedGradient(theScalingFactor);
    }
}