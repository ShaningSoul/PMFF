/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.intraenergy;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableBendingBendingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableBendingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableIntraElectroStaticList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableIntraNonbondingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableOutOfPlaneList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchBendingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchTorsionList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableTorsionList;
import org.bmdrc.sbff.parameter.SbffIntraParameterSet;
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffIntraEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction implements Serializable {

    private static final long serialVersionUID = 4273254499934897239L;

    private IAtomContainer itsUnitMolecule;
    protected SbffStretchEnergyFunction itsStretchEnergyFunction;
    protected SbffBendingEnergyFunction itsBendingEnergyFunction;
    protected SbffTorsionEnergyFunction itsTorsionEnergyFunction;
    protected SbffStretchBendingEnergyFunction itsStretchBendingEnergyFunction;
    protected SbffStretchTorsionEnergyFunction itsStretchTorsionEnergyFunction;
    protected SbffBendingBendingEnergyFunction itsBendingBendingEnergyFunction;
    protected SbffOutOfPlaneEnergyFunction itsOutOfPlaneEnergyFunction;
    protected SbffIntraElectroStaticEnergyFunction itsIntraElectroStaticFunction;
    protected SbffIntraNonbondingEnergyFunction itsIntraNonbondingEnergyFunction;
    protected CalculableStretchList itsCalculableStretchList;
    protected CalculableBendingList itsCalculableBendingList;
    protected CalculableTorsionList itsCalculableTorsionList;
    protected CalculableStretchBendingList itsCalculableStretchBendingList;
    protected CalculableStretchTorsionList itsCalculableStretchTorsionList;
    protected CalculableBendingBendingList itsCalculableBendingBendingList;
    protected CalculableOutOfPlaneList itsCalculableOutOfPlaneList;
    protected CalculableIntraElectroStaticList itsCalculableIntraElectroStaticList;
    protected CalculableIntraNonbondingList itsCalculableIntraNonbondingList;
    //constant Double variable
    protected final double INITIAL_GREDIENT = 1.0;
    protected final double NORMAL_GREDIENT = 1.0;
    protected final double MAXIMUM_DEGREE = 360.0;
    protected final double MAXIMUM_RADIAN = 2 * Math.PI;
    protected final double MAXIMUM_RATIO = 1.0;
    //constant String variable
    public static final String X_GREDIENT = "X_Gredient";
    public static final String Y_GREDIENT = "Y_Gredient";
    public static final String Z_GREDIENT = "Z_Gredient";
    protected final String INTRA_ENERGY_KEY = "Intra-molecular energy";

    public SbffIntraEnergyFunction(IAtomContainer theMolecule) {
        this(theMolecule, new SbffIntraCalculationParameter(), new SbffIntraParameterSet(), new MoveTogetherAtomNumberList(theMolecule));
    }
    
    public SbffIntraEnergyFunction(IAtomContainer theMolecule, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, new SbffIntraCalculationParameter(), new SbffIntraParameterSet(), theMoveTogetherAtomNumberMap);
    }

    public SbffIntraEnergyFunction(IAtomContainer theMolecule, ISbffIntraCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculationParameter, new SbffIntraParameterSet(), new MoveTogetherAtomNumberList(theMolecule));
    }
    
    public SbffIntraEnergyFunction(IAtomContainer theMolecule, SbffIntraParameterSet theParameterSet) {
        this(theMolecule, new SbffIntraCalculationParameter(), theParameterSet, new MoveTogetherAtomNumberList(theMolecule));
    }
    
    public SbffIntraEnergyFunction(IAtomContainer theMolecule, SbffIntraParameterSet theParameterSet, 
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, new SbffIntraCalculationParameter(), theParameterSet, theMoveTogetherAtomNumberMap);
    }
    
    public SbffIntraEnergyFunction(IAtomContainer theMolecule, ISbffIntraCalculationParameter theCalculationParameter, 
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, theCalculationParameter, new SbffIntraParameterSet(), theMoveTogetherAtomNumberMap);
    }
    
    public SbffIntraEnergyFunction(IAtomContainer theMolecule, ISbffIntraCalculationParameter theCalculationParameter, 
            SbffIntraParameterSet theParameterSet) {
        this(theMolecule, theCalculationParameter, theParameterSet, new MoveTogetherAtomNumberList(theMolecule));
    }
    
    public SbffIntraEnergyFunction(IAtomContainer theMolecule, ISbffIntraCalculationParameter theCalculationParameter, 
            SbffIntraParameterSet theParameterSet, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, null, theCalculationParameter, theParameterSet, theMoveTogetherAtomNumberMap);
    }

    public SbffIntraEnergyFunction(IAtomContainer theMolecule, IAtomContainer theUnitMolecule, ISbffIntraCalculationParameter theCalculationParameter, 
            SbffIntraParameterSet theParameterSet, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsUnitMolecule = theUnitMolecule;
        
        this.__generateMM3AtomType();
        
        this.itsCalculableStretchList = new CalculableStretchList(this.itsMolecule, theParameterSet.getStretchParameterSet(), 
                theCalculationParameter.getNotCalculableAtomIndexList());
        this.itsCalculableBendingList = new CalculableBendingList(this.itsMolecule, theParameterSet.getBendingParameterSet(), 
                theCalculationParameter.getNotCalculableAtomIndexList());
        this.itsCalculableTorsionList = new CalculableTorsionList(this.itsMolecule, theParameterSet.getTorsionParameterSet(), 
                theCalculationParameter.getNotCalculableAtomIndexList());
        this.itsCalculableStretchBendingList = new CalculableStretchBendingList(this.itsMolecule, theParameterSet.getStretchParameterSet(),
                theParameterSet.getStretchBendParameterSet(), this.itsCalculableBendingList, theCalculationParameter.getNotCalculableAtomIndexList());
        this.itsCalculableStretchTorsionList = new CalculableStretchTorsionList(this.itsMolecule, theParameterSet.getStretchParameterSet(),
                theParameterSet.getStretchTorsionParameterSet(), this.itsCalculableTorsionList, theCalculationParameter.getNotCalculableAtomIndexList());
        this.itsCalculableBendingBendingList = new CalculableBendingBendingList(this.itsMolecule, this.itsCalculableBendingList, theCalculationParameter.getNotCalculableAtomIndexList());
        this.itsCalculableOutOfPlaneList = new CalculableOutOfPlaneList(this.itsMolecule, theParameterSet.getOutOfPlaneBendingParameterSet(), 
                theCalculationParameter.getNotCalculableAtomIndexList());
        this.itsCalculableIntraElectroStaticList = new CalculableIntraElectroStaticList(this.itsMolecule, theMoveTogetherAtomNumberMap.getTopologicalDistanceMatrix(),
                theCalculationParameter.getNotCalculableAtomIndexList());
        this.itsCalculableIntraNonbondingList = new CalculableIntraNonbondingList(this.itsMolecule, theParameterSet.getIntraNonbondingParameterSet(),
                this.itsCalculableIntraElectroStaticList, theCalculationParameter.getNotCalculableAtomIndexList());

        this.itsStretchEnergyFunction = new SbffStretchEnergyFunction(this.itsMolecule, this.itsCalculableStretchList, theCalculationParameter, theMoveTogetherAtomNumberMap);
        this.itsBendingEnergyFunction = new SbffBendingEnergyFunction(this.itsMolecule, this.itsCalculableBendingList, theCalculationParameter, theMoveTogetherAtomNumberMap);
        this.itsTorsionEnergyFunction = new SbffTorsionEnergyFunction(this.itsMolecule, this.itsCalculableTorsionList, theCalculationParameter, theMoveTogetherAtomNumberMap);
        this.itsStretchBendingEnergyFunction = new SbffStretchBendingEnergyFunction(this.itsMolecule, this.itsCalculableStretchBendingList, theCalculationParameter, theMoveTogetherAtomNumberMap);
        this.itsStretchTorsionEnergyFunction = new SbffStretchTorsionEnergyFunction(this.itsMolecule, this.itsCalculableStretchTorsionList, theCalculationParameter, theMoveTogetherAtomNumberMap);
        this.itsBendingBendingEnergyFunction = new SbffBendingBendingEnergyFunction(this.itsMolecule, this.itsCalculableBendingBendingList, theCalculationParameter, theMoveTogetherAtomNumberMap);
        this.itsOutOfPlaneEnergyFunction = new SbffOutOfPlaneEnergyFunction(this.itsMolecule, this.itsCalculableOutOfPlaneList, theCalculationParameter, theMoveTogetherAtomNumberMap);
        this.itsIntraElectroStaticFunction = new SbffIntraElectroStaticEnergyFunction(this.itsMolecule, this.itsCalculableIntraElectroStaticList, theCalculationParameter,
                theMoveTogetherAtomNumberMap);
        this.itsIntraNonbondingEnergyFunction = new SbffIntraNonbondingEnergyFunction(this.itsMolecule, this.itsCalculableIntraNonbondingList, theCalculationParameter, theMoveTogetherAtomNumberMap);
    }

    public CalculableStretchList getCalculableStretchList() {
        return itsCalculableStretchList;
    }

    public CalculableBendingList getCalculableBendingList() {
        return itsCalculableBendingList;
    }

    public CalculableTorsionList getCalculableTorsionList() {
        return itsCalculableTorsionList;
    }

    public CalculableStretchBendingList getCalculableStretchBendingList() {
        return itsCalculableStretchBendingList;
    }

    public CalculableStretchTorsionList getCalculableStretchTorsionList() {
        return itsCalculableStretchTorsionList;
    }

    public CalculableBendingBendingList getCalculableBendingBendingList() {
        return itsCalculableBendingBendingList;
    }

    public CalculableOutOfPlaneList getCalculableOutOfPlaneList() {
        return itsCalculableOutOfPlaneList;
    }

    public CalculableIntraElectroStaticList getCalculableIntraElectroStaticList() {
        return itsCalculableIntraElectroStaticList;
    }

    public CalculableIntraNonbondingList getCalculableIntraNonbondingList() {
        return itsCalculableIntraNonbondingList;
    }

    public SbffStretchEnergyFunction getStretchEnergyFunction() {
        return itsStretchEnergyFunction;
    }

    public SbffBendingEnergyFunction getBendingEnergyFunction() {
        return itsBendingEnergyFunction;
    }

    public SbffTorsionEnergyFunction getTorsionEnergyFunction() {
        return itsTorsionEnergyFunction;
    }

    public SbffStretchBendingEnergyFunction getStretchBendingEnergyFunction() {
        return itsStretchBendingEnergyFunction;
    }

    public SbffStretchTorsionEnergyFunction getStretchTorsionEnergyFunction() {
        return itsStretchTorsionEnergyFunction;
    }

    public SbffBendingBendingEnergyFunction getBendingBendingEnergyFunction() {
        return itsBendingBendingEnergyFunction;
    }

    public SbffOutOfPlaneEnergyFunction getOutOfPlaneEnergyFunction() {
        return itsOutOfPlaneEnergyFunction;
    }

    public SbffIntraNonbondingEnergyFunction getIntraNonbondingEnergyFunction() {
        return itsIntraNonbondingEnergyFunction;
    }

    public SbffIntraElectroStaticEnergyFunction getIntraElectroStaticEnergyFunction() {
        return itsIntraElectroStaticFunction;
    }

    public Double calculateIntraEnergy() {
        this._setMoleculeInEachEnergyFunction();

        this.itsEnergy = this.itsStretchEnergyFunction.calculateEnergyFunction() + this.itsBendingEnergyFunction.calculateEnergyFunction()
                + this.itsTorsionEnergyFunction.calculateEnergyFunction() + this.itsStretchBendingEnergyFunction.calculateEnergyFunction()
                + this.itsStretchTorsionEnergyFunction.calculateEnergyFunction() + this.itsBendingBendingEnergyFunction.calculateEnergyFunction()
                + this.itsOutOfPlaneEnergyFunction.calculateEnergyFunction() + this.itsIntraElectroStaticFunction.calculateEnergyFunction()
                + this.itsIntraNonbondingEnergyFunction.calculateEnergyFunction();
        
        this.itsMolecule.setProperty(this.INTRA_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    protected void _setMoleculeInEachEnergyFunction() {
        this.itsStretchEnergyFunction.setMolecule(this.itsMolecule);
        this.itsBendingEnergyFunction.setMolecule(this.itsMolecule);
        this.itsTorsionEnergyFunction.setMolecule(this.itsMolecule);
        this.itsStretchBendingEnergyFunction.setMolecule(this.itsMolecule);
        this.itsStretchTorsionEnergyFunction.setMolecule(this.itsMolecule);
        this.itsBendingBendingEnergyFunction.setMolecule(this.itsMolecule);
        this.itsOutOfPlaneEnergyFunction.setMolecule(this.itsMolecule);
        this.itsIntraElectroStaticFunction.setMolecule(this.itsMolecule);
        this.itsIntraNonbondingEnergyFunction.setMolecule(this.itsMolecule);
    }

    private void __generateMM3AtomType() {
        MM3AtomTypeGenerator theAtomType = new MM3AtomTypeGenerator();

        theAtomType.generateAtomType(this.itsMolecule, this.itsUnitMolecule, this.itsMoveTogetherAtomNumberMap.getRingSet());
    }

    @Override
    public Double calculateEnergyFunction() {
        return this.calculateIntraEnergy();
    }

    @Override
    public void calculateGradient() {
        this.itsStretchEnergyFunction.calculateGradient();
        this.itsBendingEnergyFunction.calculateGradient();
        this.itsTorsionEnergyFunction.calculateGradient();
        this.itsStretchBendingEnergyFunction.calculateGradient();
        this.itsStretchTorsionEnergyFunction.calculateGradient();
        this.itsBendingBendingEnergyFunction.calculateGradient();
        this.itsOutOfPlaneEnergyFunction.calculateGradient();
        this.itsIntraElectroStaticFunction.calculateGradient();
        this.itsIntraNonbondingEnergyFunction.calculateGradient();
    }

    @Override
    public Vector calculateGradientVector(double theScalingFactor) {
        Vector theGradientVector = new Vector(this.itsMolecule.getAtomCount() * Constant.POSITION_DIMENSION_SIZE);

        theGradientVector = VectorCalculator.sum(theGradientVector, this.itsStretchEnergyFunction.calculateGradientVector(theScalingFactor));
        theGradientVector = VectorCalculator.sum(theGradientVector, this.itsBendingEnergyFunction.calculateGradientVector(theScalingFactor));
        theGradientVector = VectorCalculator.sum(theGradientVector, this.itsTorsionEnergyFunction.calculateGradientVector(theScalingFactor));
        theGradientVector = VectorCalculator.sum(theGradientVector, this.itsStretchBendingEnergyFunction.calculateGradientVector(theScalingFactor));
        theGradientVector = VectorCalculator.sum(theGradientVector, this.itsStretchTorsionEnergyFunction.calculateGradientVector(theScalingFactor));
        theGradientVector = VectorCalculator.sum(theGradientVector, this.itsBendingBendingEnergyFunction.calculateGradientVector(theScalingFactor));
        theGradientVector = VectorCalculator.sum(theGradientVector, this.itsOutOfPlaneEnergyFunction.calculateGradientVector(theScalingFactor));
        theGradientVector = VectorCalculator.sum(theGradientVector, this.itsIntraElectroStaticFunction.calculateGradientVector(theScalingFactor));
        theGradientVector = VectorCalculator.sum(theGradientVector, this.itsIntraNonbondingEnergyFunction.calculateGradientVector(theScalingFactor));

        return theGradientVector;
    }

    @Override
    public void reloadGradientVector() {
        this._setMoleculeInEachEnergyFunction();

        this.itsStretchEnergyFunction.reloadGradientVector();
        this.itsBendingEnergyFunction.reloadGradientVector();
        this.itsTorsionEnergyFunction.reloadGradientVector();
        this.itsStretchBendingEnergyFunction.reloadGradientVector();
        this.itsStretchTorsionEnergyFunction.reloadGradientVector();
        this.itsBendingBendingEnergyFunction.reloadGradientVector();
        this.itsOutOfPlaneEnergyFunction.reloadGradientVector();
        this.itsIntraElectroStaticFunction.reloadGradientVector();
        this.itsIntraNonbondingEnergyFunction.reloadGradientVector();

        this.itsGradientVector = VectorCalculator.sum(this.itsStretchEnergyFunction.getGradientVector(), this.itsBendingEnergyFunction.getGradientVector());
        this.itsGradientVector = VectorCalculator.sum(this.itsGradientVector, this.itsTorsionEnergyFunction.getGradientVector());
        this.itsGradientVector = VectorCalculator.sum(this.itsGradientVector, this.itsStretchBendingEnergyFunction.getGradientVector());
        this.itsGradientVector = VectorCalculator.sum(this.itsGradientVector, this.itsStretchTorsionEnergyFunction.getGradientVector());
        this.itsGradientVector = VectorCalculator.sum(this.itsGradientVector, this.itsBendingBendingEnergyFunction.getGradientVector());
        this.itsGradientVector = VectorCalculator.sum(this.itsGradientVector, this.itsOutOfPlaneEnergyFunction.getGradientVector());
        this.itsGradientVector = VectorCalculator.sum(this.itsGradientVector, this.itsIntraElectroStaticFunction.getGradientVector());
        this.itsGradientVector = VectorCalculator.sum(this.itsGradientVector, this.itsIntraNonbondingEnergyFunction.getGradientVector());
    }

    @Override
    public Vector calculateConjugatedGradientVector(double theScalingFactor) {
        Vector theConjugatedGradientVector = new Vector(this.itsMolecule.getAtomCount() * Constant.POSITION_DIMENSION_SIZE);

        theConjugatedGradientVector = VectorCalculator.sum(theConjugatedGradientVector,
                this.itsStretchEnergyFunction.calculateConjugatedGradientVector(theScalingFactor));
        theConjugatedGradientVector = VectorCalculator.sum(theConjugatedGradientVector,
                this.itsBendingEnergyFunction.calculateConjugatedGradientVector(theScalingFactor));
        theConjugatedGradientVector = VectorCalculator.sum(theConjugatedGradientVector,
                this.itsTorsionEnergyFunction.calculateConjugatedGradientVector(theScalingFactor));
        theConjugatedGradientVector = VectorCalculator.sum(theConjugatedGradientVector,
                this.itsStretchBendingEnergyFunction.calculateConjugatedGradientVector(theScalingFactor));
        theConjugatedGradientVector = VectorCalculator.sum(theConjugatedGradientVector,
                this.itsStretchTorsionEnergyFunction.calculateConjugatedGradientVector(theScalingFactor));
        theConjugatedGradientVector = VectorCalculator.sum(theConjugatedGradientVector,
                this.itsBendingBendingEnergyFunction.calculateConjugatedGradientVector(theScalingFactor));
        theConjugatedGradientVector = VectorCalculator.sum(theConjugatedGradientVector,
                this.itsOutOfPlaneEnergyFunction.calculateConjugatedGradientVector(theScalingFactor));
        theConjugatedGradientVector = VectorCalculator.sum(theConjugatedGradientVector,
                this.itsIntraElectroStaticFunction.calculateConjugatedGradientVector(theScalingFactor));
        theConjugatedGradientVector = VectorCalculator.sum(theConjugatedGradientVector,
                this.itsIntraNonbondingEnergyFunction.calculateConjugatedGradientVector(theScalingFactor));

        return theConjugatedGradientVector;
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this._setMoleculeInEachEnergyFunction();

        this.itsStretchEnergyFunction.reloadConjugatedGradientVector();
        this.itsBendingEnergyFunction.reloadConjugatedGradientVector();
        this.itsTorsionEnergyFunction.reloadConjugatedGradientVector();
        this.itsStretchBendingEnergyFunction.reloadConjugatedGradientVector();
        this.itsStretchTorsionEnergyFunction.reloadConjugatedGradientVector();
        this.itsBendingBendingEnergyFunction.reloadConjugatedGradientVector();
        this.itsOutOfPlaneEnergyFunction.reloadConjugatedGradientVector();
        this.itsIntraElectroStaticFunction.reloadConjugatedGradientVector();
        this.itsIntraNonbondingEnergyFunction.reloadConjugatedGradientVector();

        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsStretchEnergyFunction.getConjugatedGradientVector(),
                this.itsBendingEnergyFunction.getConjugatedGradientVector());
        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsConjugatedGradientVector, this.itsTorsionEnergyFunction.getConjugatedGradientVector());
        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsConjugatedGradientVector, this.itsStretchBendingEnergyFunction.getConjugatedGradientVector());
        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsConjugatedGradientVector, this.itsStretchTorsionEnergyFunction.getConjugatedGradientVector());
        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsConjugatedGradientVector, this.itsBendingBendingEnergyFunction.getConjugatedGradientVector());
        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsConjugatedGradientVector, this.itsOutOfPlaneEnergyFunction.getConjugatedGradientVector());
        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsConjugatedGradientVector, this.itsIntraElectroStaticFunction.getConjugatedGradientVector());
        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsConjugatedGradientVector, this.itsIntraNonbondingEnergyFunction.getConjugatedGradientVector());
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsStretchEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsBendingEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsTorsionEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsStretchBendingEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsStretchTorsionEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsBendingBendingEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsOutOfPlaneEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsIntraElectroStaticFunction.makeConjugatedGradient(theScalingFactor);
        this.itsIntraNonbondingEnergyFunction.makeConjugatedGradient(theScalingFactor);
    }
}
