/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.interenergy;

import java.io.Serializable;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.sbff.energyfunction.parameter.SbffInterCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffInterCalculationParameter;
import org.bmdrc.sbff.interenergy.calculableset.CalculableElectroStaticList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableHydrogenBondList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableInterMoleculeList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableNonbondingList;
import org.bmdrc.sbff.parameter.SbffInterParameterSet;
import org.bmdrc.sbff.tool.MpeoeChargeCalculator;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffInterEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction<ISbffInterCalculationParameter> implements Serializable {

    private static final long serialVersionUID = 4596254506826715878L;

    private IAtomContainer itsUnitMolecule;
    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;
    private SbffElectroStaticEnergyFunction itsElectroStaticEnergyFunction;
    private SbffNonbondingEnergyFunction itsNonbondingEnergyFunction;
    private SbffHydrogenBondEnergyFunction itsHydrogenBondEnergyFunction;
    private CalculableInterMoleculeList itsCalculableList;
    private SbffInterParameterSet itsParameterSet;
    //constant String variable
    private final String INTER_ENERGY_KEY = "Inter-molecular energy";

    public SbffInterEnergyFunction(IAtomContainer theMolecule) {
        this(theMolecule, new SbffInterCalculationParameter());
    }

    public SbffInterEnergyFunction(IAtomContainer theMolecule, ISbffInterCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculationParameter, new SbffInterParameterSet());
    }

    public SbffInterEnergyFunction(IAtomContainer theMolecule, ISbffInterCalculationParameter theCalculationParameter,
            SbffInterParameterSet theInterParameterSet) {
        this(theMolecule, theCalculationParameter, theInterParameterSet, new MoveTogetherAtomNumberList(theMolecule));
    }

    public SbffInterEnergyFunction(IAtomContainer theMolecule, ISbffInterCalculationParameter theCalculationParameter,
            SbffInterParameterSet theInterParameterSet, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, new CalculableInterMoleculeList(theMolecule, theMoveTogetherAtomNumberMap.getTopologicalDistanceMatrix(),
                theCalculationParameter.getNotCalculableAtomIndexList()), theCalculationParameter, theInterParameterSet, theMoveTogetherAtomNumberMap);
    }

    public SbffInterEnergyFunction(IAtomContainer theMolecule, IAtomContainer theUnitMolecule,
            ISbffInterCalculationParameter theCalculationParameter, SbffInterParameterSet theInterParameterSet,
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, theMolecule, new CalculableInterMoleculeList(theMolecule, theMoveTogetherAtomNumberMap.getTopologicalDistanceMatrix(),
                theCalculationParameter.getNotCalculableAtomIndexList()), theCalculationParameter, theInterParameterSet,
                theMoveTogetherAtomNumberMap);
    }

    public SbffInterEnergyFunction(IAtomContainer theMolecule, CalculableInterMoleculeList theCalculableList,
            ISbffInterCalculationParameter theCalculationParameter, SbffInterParameterSet theInterParameterSet,
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, theMolecule, theCalculableList, theCalculationParameter, theInterParameterSet, theMoveTogetherAtomNumberMap);
    }

    public SbffInterEnergyFunction(IAtomContainer theMolecule, IAtomContainer theUnitMolecule, CalculableInterMoleculeList theCalculableList,
            ISbffInterCalculationParameter theCalculationParameter, SbffInterParameterSet theInterParameterSet,
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);

        this.itsUnitMolecule = theUnitMolecule;
        this.itsParameterSet = theInterParameterSet;

        this.__generatePoint3d();
        this.__generateMpeoeCharge();
        
        this.itsTopologicalDistanceMatrix = theMoveTogetherAtomNumberMap.getTopologicalDistanceMatrix();
        this.itsCalculableList = theCalculableList;

        this.itsElectroStaticEnergyFunction = new SbffElectroStaticEnergyFunction(theMolecule, theCalculableList.getElectroStaticList(), theCalculationParameter,
                theMoveTogetherAtomNumberMap);
        this.itsHydrogenBondEnergyFunction = new SbffHydrogenBondEnergyFunction(theMolecule, theCalculableList.getHydrogenBondList(), theCalculationParameter,
                theMoveTogetherAtomNumberMap);
        this.itsNonbondingEnergyFunction = new SbffNonbondingEnergyFunction(theMolecule, theCalculableList.getNonbondingList(), theCalculationParameter,
                theMoveTogetherAtomNumberMap);
    }

    public CalculableElectroStaticList getCalculableElectroStaticList() {
        return this.itsCalculableList.getElectroStaticList();
    }

    public CalculableNonbondingList getCalculableVanDerWaalsList() {
        return this.itsCalculableList.getNonbondingList();
    }

    public CalculableHydrogenBondList getCalculableHydrogneBondList() {
        return this.itsCalculableList.getHydrogenBondList();
    }

    public SbffElectroStaticEnergyFunction getElectroStaticEnergyFunction() {
        return itsElectroStaticEnergyFunction;
    }

    public SbffNonbondingEnergyFunction getVanDerWaalsEnergyFunction() {
        return itsNonbondingEnergyFunction;
    }

    public SbffHydrogenBondEnergyFunction getHydrogenBondEnergyFunction() {
        return itsHydrogenBondEnergyFunction;
    }

    public TopologicalDistanceMatrix getTopologicalDistanceMatrix() {
        return itsTopologicalDistanceMatrix;
    }
    
    public void regenerateCalculableSet() {
        this.itsCalculableList = new CalculableInterMoleculeList(this.itsMolecule, this.itsTopologicalDistanceMatrix, 
                this.itsCalculationParameter.getNotCalculableAtomIndexList());
        
        this.itsElectroStaticEnergyFunction.setCalculableElectroStaticList(this.itsCalculableList.getElectroStaticList());
        this.itsHydrogenBondEnergyFunction.setCalculableHydrogenBondList(this.itsCalculableList.getHydrogenBondList());
        this.itsNonbondingEnergyFunction.setCalculableNonbondingList(this.itsCalculableList.getNonbondingList());
    }

    public double calculateInterEnergy() {
        this.__setMoleculeInEachFunction();

        double theElectroStaticEnergy = this.itsElectroStaticEnergyFunction.calculateEnergyFunction();
        double theNonbondingEnergy = this.itsNonbondingEnergyFunction.calculateEnergyFunction();
        double theHydrogenBondEnergy = this.itsHydrogenBondEnergyFunction.calculateEnergyFunction();

        this.itsEnergy = theElectroStaticEnergy + theNonbondingEnergy + theHydrogenBondEnergy;

        this.itsMolecule.setProperty(this.INTER_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private void __setMoleculeInEachFunction() {
        this.itsElectroStaticEnergyFunction.setMolecule(this.itsMolecule);
        this.itsNonbondingEnergyFunction.setMolecule(this.itsMolecule);
        this.itsHydrogenBondEnergyFunction.setMolecule(this.itsMolecule);

        this.itsElectroStaticEnergyFunction.setCalculableElectroStaticList(this.itsCalculableList.getElectroStaticList());
        this.itsHydrogenBondEnergyFunction.setCalculableHydrogenBondList(this.itsCalculableList.getHydrogenBondList());
        this.itsNonbondingEnergyFunction.setCalculableNonbondingList(this.itsCalculableList.getNonbondingList());
    }

    private void __generateMpeoeCharge() {
        for (IAtom theAtom : this.itsUnitMolecule.atoms()) {
            if (!theAtom.getProperties().containsKey(MpeoeChargeCalculator.MPEOE_CHARGE_KEY)) {
                MpeoeChargeCalculator theCalculator = new MpeoeChargeCalculator();

                theCalculator.inputMpeoeCharge(this.itsMolecule, this.itsUnitMolecule, this.itsMoveTogetherAtomNumberMap.getTopologicalDistanceMatrix(),
                        this.itsMoveTogetherAtomNumberMap.getRingSet());
                break;
            }
        }
    }

    @Override
    public Double calculateEnergyFunction() {
        return this.calculateInterEnergy();
    }

    @Override
    public Double calculateEnergyFunction(IAtomContainer theMolecule) {
        try {
            IAtomContainer theCopiedMolecule = (IAtomContainer) theMolecule.clone();

            this.setMolecule(theMolecule);
            double theEnergy = this.calculateEnergyFunction();
            this.setMolecule(theCopiedMolecule);

            return theEnergy;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule Clone Error");
        }

        return null;
    }

    private void __generatePoint3d() {
        for (IAtom theAtom : this.getMolecule().atoms()) {
            if (theAtom.getPoint3d() == null) {
                theAtom.setPoint3d(new Point3d(0.0, 0.0, 0.0));
            }
        }
    }

    public void updateCalculableAtomList() {
        this.itsCalculableList.setElectroStaticList(new CalculableElectroStaticList(this.itsMolecule, this.itsTopologicalDistanceMatrix, this.itsCalculationParameter.getNotCalculableAtomIndexList()));
        this.itsCalculableList.setHydrogenBondList(new CalculableHydrogenBondList(this.itsMolecule, this.itsParameterSet.getHydrogenBondParameterSet(), this.itsCalculationParameter.getNotCalculableAtomIndexList()));
        this.itsCalculableList.setNonbondingList(new CalculableNonbondingList(this.itsMolecule, this.itsParameterSet.getNonbondParameterSet(), this.itsCalculableList.getElectroStaticList(),
                this.itsCalculationParameter.getNotCalculableAtomIndexList()));
    }

    @Override
    public void calculateGradient() {
        this.itsElectroStaticEnergyFunction.calculateGradient();
        this.itsHydrogenBondEnergyFunction.calculateGradient();
        this.itsNonbondingEnergyFunction.calculateGradient();
    }

    @Override
    public void reloadGradientVector() {
        this.__setMoleculeInEachFunction();

        this.itsElectroStaticEnergyFunction.reloadGradientVector();
        this.itsNonbondingEnergyFunction.reloadGradientVector();
        this.itsHydrogenBondEnergyFunction.reloadGradientVector();

        this.itsGradientVector = VectorCalculator.sum(this.itsElectroStaticEnergyFunction.getGradientVector(), this.itsNonbondingEnergyFunction.getGradientVector());
        this.itsGradientVector = VectorCalculator.sum((Vector) this.itsGradientVector, this.itsHydrogenBondEnergyFunction.getGradientVector());
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this.__setMoleculeInEachFunction();

        this.itsElectroStaticEnergyFunction.reloadConjugatedGradientVector();
        this.itsNonbondingEnergyFunction.reloadConjugatedGradientVector();
        this.itsHydrogenBondEnergyFunction.reloadConjugatedGradientVector();

        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsElectroStaticEnergyFunction.getConjugatedGradientVector(),
                this.itsNonbondingEnergyFunction.getConjugatedGradientVector());
        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsConjugatedGradientVector,
                this.itsHydrogenBondEnergyFunction.getConjugatedGradientVector());
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsElectroStaticEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsHydrogenBondEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsNonbondingEnergyFunction.makeConjugatedGradient(theScalingFactor);
    }
}
