/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.energyfunction;


import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import javax.vecmath.Point3d;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.atdl.AtdlGenerator;
import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.tool.FormalChargeCalculator;
import org.bmdrc.sbff.atomtype.HydrogenBondAtomTypeGenerator;
import org.bmdrc.sbff.energyfunction.parameter.SbffCalculationParameter;
import org.bmdrc.sbff.interenergy.SbffInterEnergyFunction;
import org.bmdrc.sbff.interenergy.SbffSolvationEnergyFunction;
import org.bmdrc.sbff.interenergy.calculableset.CalculableElectroStaticList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableHydrogenBondList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableNonbondingList;
import org.bmdrc.sbff.intraenergy.SbffIntraEnergyFunction;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableBendingBendingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableBendingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableIntraElectroStaticList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableIntraNonbondingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableOutOfPlaneList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchBendingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableStretchTorsionList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableTorsionList;
import org.bmdrc.sbff.parameter.SbffTotalParameterSet;
import org.bmdrc.sbff.tool.MpeoeChargeCalculator;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;

/**
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 */
public class SbffEnergyFunction extends AbstractTotalForceFieldEnergyFunction<SbffCalculationParameter> implements Serializable {

    private static final long serialVersionUID = -3082853728687314777L;

    protected SbffIntraEnergyFunction itsIntraEnergyFunction;
    protected SbffInterEnergyFunction itsInterEnergyFunction;
    private SbffSolvationEnergyFunction itsSolvationEnergyFunction;
    private SbffTotalParameterSet itsTotalParameterSet;
    //constant String variable
    public static final String SBFF_ENERGY_KEY = "SBFF energy";
    private final String TEMP_FILE_NAME = "Temp_";

    public SbffEnergyFunction(SbffEnergyFunction theEnergyFunction) throws CloneNotSupportedException {
        super((IAtomContainer) theEnergyFunction.itsMolecule.clone(), theEnergyFunction.itsCalculationParameter);
        this.itsIntraEnergyFunction = new SbffIntraEnergyFunction(this.itsMolecule);
        this.itsInterEnergyFunction = new SbffInterEnergyFunction(this.itsMolecule);
        this.itsSolvationEnergyFunction = new SbffSolvationEnergyFunction(this.itsMolecule);
        this.itsTotalParameterSet = new SbffTotalParameterSet();
    }

    public SbffEnergyFunction(IAtomContainer theMolecule) {
        this(theMolecule, new SbffCalculationParameter());
    }

    public SbffEnergyFunction(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theMolecule, new SbffCalculationParameter(theNotCalculateAtomNumberList));
    }

    public SbffEnergyFunction(IAtomContainer theMolecule, SbffCalculationParameter theCalculationParameter) {
        this(theMolecule, theMolecule, theCalculationParameter);
    }

    public SbffEnergyFunction(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule) {
        this(theTotalMolecule, theUnitMolecule, SbffEnergyFunction._initializeNotCalculateAtomNumberList(theTotalMolecule, theUnitMolecule));
    }

    public SbffEnergyFunction(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule, SbffCalculationParameter theCalculationParameter) {
        super(theTotalMolecule, theUnitMolecule, theCalculationParameter);
        
        this.__inputFormalCharge();
        this._generatePoint3d();
        this._generateAtdl();
        this.__generateMpeoeCharge();
        this.__generateHydrogenBondAtomType();
        
        this.itsTotalParameterSet = new SbffTotalParameterSet();
        this.itsIntraEnergyFunction = new SbffIntraEnergyFunction(this.itsMolecule, this.itsUnitMolecule, theCalculationParameter, 
                this.itsTotalParameterSet.getSbffIntraParameterSet(), this.itsMoveTogetherAtomNumberMap);
        this.itsInterEnergyFunction = new SbffInterEnergyFunction(this.itsMolecule, this.itsUnitMolecule, theCalculationParameter, 
                this.itsTotalParameterSet.getSbffInterParameterSet(), this.itsMoveTogetherAtomNumberMap);
        this.itsSolvationEnergyFunction = new SbffSolvationEnergyFunction(this.itsMolecule);
    }

    public SbffEnergyFunction(Protein theProtein, IAtomContainer theLigand, SbffCalculationParameter theCalculationParameter) {
        super(theProtein, theLigand, theCalculationParameter);
        
        this.__inputFormalCharge();
        this._generatePoint3d();
        this._generateAtdl();
        this.__generateMpeoeCharge();
        this.__generateHydrogenBondAtomType();
        
        this.itsTotalParameterSet = new SbffTotalParameterSet();
        this.itsIntraEnergyFunction = new SbffIntraEnergyFunction(this.itsMolecule, this.itsUnitMolecule, theCalculationParameter, 
                this.itsTotalParameterSet.getSbffIntraParameterSet(), this.itsMoveTogetherAtomNumberMap);
        this.itsInterEnergyFunction = new SbffInterEnergyFunction(this.itsMolecule, this.itsUnitMolecule, theCalculationParameter, 
                this.itsTotalParameterSet.getSbffInterParameterSet(), this.itsMoveTogetherAtomNumberMap);
        this.itsSolvationEnergyFunction = new SbffSolvationEnergyFunction(this.itsMolecule);
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
    
    protected static SbffCalculationParameter _initializeNotCalculateAtomNumberList(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule) {
        List<Integer> theNotCalculateAtomNumberList = new ArrayList<>();
        CrystalInformation theCrystalInformation = new CrystalInformation(theUnitMolecule);

        for (int vi = theUnitMolecule.getAtomCount() * theCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE, vEnd = theTotalMolecule.getAtomCount(); vi < vEnd; vi++) {
            theNotCalculateAtomNumberList.add(vi);
        }

        return new SbffCalculationParameter(theNotCalculateAtomNumberList);
    }

    public CalculableStretchList getCalculableStretchList() {
        return this.itsIntraEnergyFunction.getCalculableStretchList();
    }

    public CalculableBendingList getCalculableBendingList() {
        return this.itsIntraEnergyFunction.getCalculableBendingList();
    }

    public CalculableTorsionList getCalculableTorsionList() {
        return this.itsIntraEnergyFunction.getCalculableTorsionList();
    }

    public CalculableStretchBendingList getCalculableStretchBendingList() {
        return this.itsIntraEnergyFunction.getCalculableStretchBendingList();
    }

    public CalculableStretchTorsionList getCalculableStretchTorsionList() {
        return this.itsIntraEnergyFunction.getCalculableStretchTorsionList();
    }

    public CalculableBendingBendingList getCalculableBendingBendingList() {
        return this.itsIntraEnergyFunction.getCalculableBendingBendingList();
    }
    
    public CalculableOutOfPlaneList getCalculableOutOfPlaneList() {
        return this.itsIntraEnergyFunction.getCalculableOutOfPlaneList();
    }
    
    public CalculableIntraElectroStaticList getCalculableIntraElectroStaticList() {
        return this.itsIntraEnergyFunction.getCalculableIntraElectroStaticList();
    }
    
    public CalculableIntraNonbondingList getCalculableIntraNonbondingList() {
        return this.itsIntraEnergyFunction.getCalculableIntraNonbondingList();
    }

    public CalculableElectroStaticList getCalculableElectroStaticList() {
        return this.itsInterEnergyFunction.getCalculableElectroStaticList();
    }

    public CalculableNonbondingList getCalculableVanDerWaalsList() {
        return this.itsInterEnergyFunction.getCalculableVanDerWaalsList();
    }

    public CalculableHydrogenBondList getCalculableHydrogneBondList() {
        return this.itsInterEnergyFunction.getCalculableHydrogneBondList();
    }

    @Override
    public SbffIntraEnergyFunction getIntraEnergyFunction() {
        return itsIntraEnergyFunction;
    }

    @Override
    public SbffInterEnergyFunction getInterEnergyFunction() {
        return itsInterEnergyFunction;
    }

    @Override
    public Double calculateEnergyFunction() {
        this.itsIntraEnergyFunction.setMolecule(this.itsMolecule);
        this.itsInterEnergyFunction.setMolecule(this.itsMolecule);

        double theIntraEnergy = this.itsIntraEnergyFunction.calculateEnergyFunction();
        double theInterEnergy = this.itsInterEnergyFunction.calculateEnergyFunction();

        this.itsEnergy = theIntraEnergy + theInterEnergy;
        this.itsMolecule.setProperty(SbffEnergyFunction.SBFF_ENERGY_KEY, String.format("%.5f", this.itsEnergy));
        
        return this.itsEnergy;
    }

    public Double calculateEnergyFunctionContainSolvation() {
        this.itsSolvationEnergyFunction.setMolecule(this.itsMolecule);

        return this.calculateEnergyFunction() + this.itsSolvationEnergyFunction.calculateEnergyFunction();
    }

    @Override
    public double calculateIntraEnergy() {
        this.itsIntraEnergyFunction.setMolecule(this.itsMolecule);

        return this.itsIntraEnergyFunction.calculateEnergyFunction();
    }

    @Override
    public double calculateInterEnergy() {
        this.itsInterEnergyFunction.setMolecule(this.itsMolecule);

        return this.itsInterEnergyFunction.calculateEnergyFunction();
    }

    @Override
    public Double calculateEnergyFunction(IAtomContainer theMolecule) {
        try {
            IAtomContainer theCopiedMoleculeInFunction = (IAtomContainer) this.itsMolecule.clone();
            double theOriginalEnergy = this.itsEnergy;
            double theEnergy;

            this.setMolecule(theMolecule);
            this._generatePoint3d();
            this._generateAtdl();

            theEnergy = this.calculateEnergyFunction();
            this.setMolecule(theCopiedMoleculeInFunction);
            this.itsIntraEnergyFunction.setMolecule(theCopiedMoleculeInFunction);
            this.itsInterEnergyFunction.setMolecule(theCopiedMoleculeInFunction);

            this.itsEnergy = theOriginalEnergy;

            return theEnergy;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule Clone error!!");
        }

        return null;
    }
    
    protected void _generatePoint3d() {
        for (IAtom theAtom : this.getMolecule().atoms()) {
            if (theAtom.getPoint2d() == null && theAtom.getPoint3d() == null) {
                theAtom.setPoint3d(new Point3d(0.0, 0.0, 0.0));
            } else if (theAtom.getPoint3d() == null) {
                theAtom.setPoint3d(new Point3d(theAtom.getPoint2d().x, theAtom.getPoint2d().y, 0.0));
            }
        }
    }

    protected void _generateAtdl() {
        AtdlGenerator theGenerator = new AtdlGenerator();

        theGenerator.inputAtdl(this.itsMolecule, this.itsUnitMolecule, this.itsMoveTogetherAtomNumberMap.getRingSet());
    }

    @Override
    public void updateCalculableAtomList() {
        this.itsInterEnergyFunction.updateCalculableAtomList();
    }

    public double calculateInteractionEnergy() {
        return this.itsInterEnergyFunction.calculateEnergyFunction();
    }

    @Override
    public void calculateGradient() {
        this.itsIntraEnergyFunction.calculateGradient();
        this.itsInterEnergyFunction.calculateGradient();
    }

    private void __generateHydrogenBondAtomType() {
        HydrogenBondAtomTypeGenerator theGenerator = new HydrogenBondAtomTypeGenerator();

        theGenerator.inputHydrogenBondAtomType(this.itsMolecule, this.itsMoveTogetherAtomNumberMap.getTopologicalDistanceMatrix());
    }
    
    private void __inputFormalCharge() {
        FormalChargeCalculator.inputFormalCharge(this.itsMolecule);
    }

    private void __deleteTempFile() {
        File[] theFileArray = new File(".").listFiles();

        for (File theFile : theFileArray) {
            if (theFile.isFile() && theFile.getName().contains(this.TEMP_FILE_NAME)) {
                theFile.delete();
            }
        }
    }

    @Override
    public void reloadGradientVector() {
        this.itsIntraEnergyFunction.setMolecule(this.itsMolecule);
        this.itsInterEnergyFunction.setMolecule(this.itsMolecule);

        this.itsIntraEnergyFunction.reloadGradientVector();
        this.itsInterEnergyFunction.reloadGradientVector();
        
        this.itsGradientVector = VectorCalculator.sum(this.itsIntraEnergyFunction.getGradientVector(), this.itsInterEnergyFunction.getGradientVector());
    }

    @Override
    public Vector calculateGradientVector(double theScalingFactor) {
        return VectorCalculator.sum(this.itsIntraEnergyFunction.calculateGradientVector(theScalingFactor), 
                this.itsInterEnergyFunction.calculateGradientVector(theScalingFactor));
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this.itsIntraEnergyFunction.setMolecule(this.itsMolecule);
        this.itsInterEnergyFunction.setMolecule(this.itsMolecule);

        this.itsIntraEnergyFunction.reloadConjugatedGradientVector();
        this.itsInterEnergyFunction.reloadConjugatedGradientVector();

        this.itsConjugatedGradientVector = VectorCalculator.sum(this.itsIntraEnergyFunction.getConjugatedGradientVector(),
                this.itsInterEnergyFunction.getConjugatedGradientVector());
    }

    @Override
    public Vector calculateConjugatedGradientVector(double theScalingFactor) {
        return VectorCalculator.sum(this.itsIntraEnergyFunction.calculateConjugatedGradientVector(theScalingFactor), 
                this.itsInterEnergyFunction.calculateConjugatedGradientVector(theScalingFactor));
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsIntraEnergyFunction.makeConjugatedGradient(theScalingFactor);
        this.itsInterEnergyFunction.makeConjugatedGradient(theScalingFactor);
    }
    
    public Double calculateInterEnergyUsingGradientVector(Vector theAtomPositionDifferenceVector) {
        return this.itsInterEnergyFunction.calculateEnergyUsingGradientVector(theAtomPositionDifferenceVector);
    }
}
