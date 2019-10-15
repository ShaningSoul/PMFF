/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.minimizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractConjugatedGradientMinimizer;
import org.bmdrc.basicchemistry.minimization.linesearcher.ProteinLigandComplexConjugatedGradientLineSearcher;
import org.bmdrc.basicchemistry.minimization.linesearcher.ProteinLigandComplexLineSearcher;
import org.bmdrc.basicchemistry.protein.AminoAcid;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.tool.AngleCalculator;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 02. 15
 */
public class ProteinLigandComplexMinimizer extends ConjugatedGradientMinimizerInMolecularStructure implements Serializable {

    private static final long serialVersionUID = 7684362696086987346L;

    protected Protein itsProtein;
    protected Protein itsResultProtein;
    protected IAtomContainer itsLigand;
    protected IAtomContainer itsResultLigand;
    //constant Double variable
    private static final double MINIMUM_RMS = 1e-4;
    private static final double DEFAULT_FLEXIBLE_DISTANCE = 5.0;
    //constant Boolean variable
    private static final boolean DEFAULT_FLEXIBLE = false;

    public ProteinLigandComplexMinimizer(AbstractTotalForceFieldEnergyFunction theEnergyFunction, Protein theProtein, IAtomContainer theLigand) {
        this(theEnergyFunction, theProtein, theLigand, ProteinLigandComplexMinimizer.MINIMUM_RMS);
    }
    
    public ProteinLigandComplexMinimizer(AbstractTotalForceFieldEnergyFunction theEnergyFunction, Protein theProtein, IAtomContainer theLigand, 
            boolean theFlexibleAminoAcid) {
        this(theEnergyFunction, theProtein, theLigand, ProteinLigandComplexMinimizer.MINIMUM_RMS, theFlexibleAminoAcid, 
                ProteinLigandComplexMinimizer.DEFAULT_FLEXIBLE_DISTANCE);
    }
    
    public ProteinLigandComplexMinimizer(AbstractTotalForceFieldEnergyFunction theEnergyFunction, Protein theProtein, IAtomContainer theLigand, 
            boolean theFlexibleAminoAcid, double theFlexibleDistance) {
        this(theEnergyFunction, theProtein, theLigand, ProteinLigandComplexMinimizer.MINIMUM_RMS, theFlexibleAminoAcid, theFlexibleDistance);
    }
    
    public ProteinLigandComplexMinimizer(AbstractTotalForceFieldEnergyFunction theEnergyFunction, Protein theProtein, IAtomContainer theLigand, double theMinimumRMS) {
        this(theEnergyFunction, theProtein, theLigand, theMinimumRMS, false, 1.0);
    }
    
    public ProteinLigandComplexMinimizer(AbstractTotalForceFieldEnergyFunction theEnergyFunction, Protein theProtein, IAtomContainer theLigand,
            double theMinimumRMS, boolean theFlexibleAminoAcid, double theFlexibleDistance) {
        super(theEnergyFunction);
        this.itsLineSearcher = new ProteinLigandComplexLineSearcher(this.itsFunction, theLigand);
        this.itsConjugatedGradientLineSearch = new ProteinLigandComplexConjugatedGradientLineSearcher(this.itsFunction, theLigand);
        this.itsProtein = theProtein;
        this.itsLigand = theLigand;
        this.itsMinimumRMS = theMinimumRMS;
    }

    public Protein getResultProtein() {
        return itsResultProtein;
    }

    public void setResultProtein(Protein theResultProtein) {
        this.itsResultProtein = theResultProtein;
    }

    public IAtomContainer getResultLigand() {
        return itsResultLigand;
    }

    private static List<Integer> __getAtomNumberListNotContainingInBindingSite(Protein theProtein, IAtomContainer theLigand, boolean theFlexibleAminoAcid, double theFlexibleDistance) {
        List<Integer> theAtomNumberListNotContainingInBindingSite = new ArrayList<>();
        int theNumberOfAtomInLigand = theLigand.getAtomCount();

        if (!theFlexibleAminoAcid) {
            for (int ai = 0, aEnd = theProtein.getMolecule().getAtomCount(); ai < aEnd; ai++) {
                theAtomNumberListNotContainingInBindingSite.add(ai + theNumberOfAtomInLigand);
            }
        } else {
            int thePreviousAtomIndex = 0;
            int theAtomIndex = 0;

            for (AminoAcid theAminoAcid : theProtein.getChainList().get(0).getAminoAcidList()) {
                IAtomContainer theMolecule = theAminoAcid.getMolecule();

                theAtomIndex += theMolecule.getAtomCount();

                if (!ProteinLigandComplexMinimizer.__isCalculableAminoAcid(theMolecule, theLigand, theFlexibleDistance)) {
                    for (int vi = thePreviousAtomIndex; vi < theAtomIndex; vi++) {
                        theAtomNumberListNotContainingInBindingSite.add(vi + theNumberOfAtomInLigand);
                    }
                }
            }
        }

        return theAtomNumberListNotContainingInBindingSite;
    }

    private static boolean __isCalculableAminoAcid(IAtomContainer theAminoAcid, IAtomContainer theLigand, double theFlexibleDistance) {
        for (IAtom theAminoAcidAtom : theAminoAcid.atoms()) {
            for (IAtom theLigandAtom : theLigand.atoms()) {
                if (theAminoAcidAtom.getPoint3d().distance(theLigandAtom.getPoint3d()) <= theFlexibleDistance) {
                    return true;
                }
            }
        }

        return false;
    }

    public void optimizeUsingProtein(Protein theProtein, IAtomContainer theLigand) {
        this.itsProtein = theProtein;
        this.itsLigand = theLigand;

        this.optimizeUsingProtein();
    }

    public void optimizeUsingProtein() {
        this.__initializeResultObject();
        this._initializeSearchVector();
        this.itsResultLigand = this.__optimize();
    }

    public IAtomContainerSet optimizeUsingTotalRandomSampling(int theNumberOfSample, double theMaximumDegree) {
        AbstractTotalForceFieldEnergyFunction theFunction = (AbstractTotalForceFieldEnergyFunction) this.itsFunction;
        Vector3d theCenterPosition = new Vector3d(GeometryTools.get3DCenter(this.itsLigand));
        double theInitialEnergy = theFunction.calculateEnergyFunction();
        double theMinimumEnergy = theInitialEnergy;
        Vector theResultSearchVector = null;
        StringBuilder theStringBuilder = new StringBuilder();
        IAtomContainerSet theResultSet = new AtomContainerSet();

        this.__initializeResultObject();

        for (int si = 0; si < theNumberOfSample; si++) {
            int theCount = 0;
            do {
                this.__setSearchVectorInRandomSample(theCenterPosition, theMaximumDegree);
            } while (!this.__isStopForSampling(theInitialEnergy));

            IAtomContainer theOptimizedMolecule = this.__optimize();

            theStringBuilder.append("\n(").append(theOptimizedMolecule.getProperty(AbstractConjugatedGradientMinimizer.INITIAL_ENERGY_KEY).toString())
                    .append(", ").append(theOptimizedMolecule.getProperty(AbstractConjugatedGradientMinimizer.OPTIMIZED_ENERGY_KEY).toString())
                    .append(")");
            theResultSet.addAtomContainer(theOptimizedMolecule);
        }

        System.out.println(theStringBuilder.toString());
        
        return theResultSet;
    }

    public IAtomContainer optimizeUsingRandomSampling(int theNumberOfSample, double theMaximumDegree) {
        AbstractTotalForceFieldEnergyFunction theFunction = (AbstractTotalForceFieldEnergyFunction) this.itsFunction;
        Vector3d theCenterPosition = new Vector3d(GeometryTools.get3DCenter(this.itsLigand));
        double theInitialEnergy = theFunction.calculateEnergyFunction();
        double theMinimumEnergy = theInitialEnergy;
        Vector theResultSearchVector = null;
        StringBuilder theStringBuilder = new StringBuilder();

        this.__initializeResultObject();

        for (int si = 0; si < theNumberOfSample; si++) {
            do {
                this.__setSearchVectorInRandomSample(theCenterPosition, theMaximumDegree);
            } while (!this.__isStopForSampling(theInitialEnergy));

            IAtomContainer theOptimizedMolecule = this.__optimize();
            double theOptimizedEnergy = (Double) theOptimizedMolecule.getProperty(AbstractConjugatedGradientMinimizer.OPTIMIZED_ENERGY_KEY);

            theStringBuilder.append("(").append(theInitialEnergy).append(", ").append(theOptimizedEnergy).append(")\n");
            if (theMinimumEnergy > theOptimizedEnergy) {
                theMinimumEnergy = theOptimizedEnergy;
                theResultSearchVector = new Vector(this.itsSearchVector);
                this.itsResultLigand.setProperty(AbstractConjugatedGradientMinimizer.OPTIMIZED_ENERGY_KEY, theOptimizedEnergy);
            }
        }

        System.out.println(theStringBuilder.toString());
        this.itsSearchVector = theResultSearchVector;
        this.__moveAtomForLigand();

        return this.itsResultLigand;
    }

    private boolean __isStopForSampling(double theInitialEnergy) {
        AbstractTotalForceFieldEnergyFunction theFunction = (AbstractTotalForceFieldEnergyFunction) this.itsFunction;
        double theCurrentEnergy = theFunction.calculateEnergyUsingGradientVector(this.itsSearchVector);

        if (theInitialEnergy > theCurrentEnergy) {
            return true;
        }

        return false;
    }

    private IAtomContainer __optimize() {
        AbstractTotalForceFieldEnergyFunction theFunction = (AbstractTotalForceFieldEnergyFunction) this.itsFunction;
        double theInitialEnergy = theFunction.calculateEnergyUsingGradientVector(this.itsSearchVector);

        this.itsCurrentEnergy = theInitialEnergy;

        this._optimize();

        try {
            IAtomContainer theResult = theFunction.setAtomPositionInTotalAtomPositionVector(this.itsSearchVector);
            IAtomContainer theCopiedResultMolecule = (IAtomContainer) this.itsResultLigand.clone();

            for (int ai = 0, aEnd = theCopiedResultMolecule.getAtomCount(); ai < aEnd; ai++) {
                theCopiedResultMolecule.getAtom(ai).setPoint3d(theResult.getAtom(ai).getPoint3d());
            }

            theCopiedResultMolecule.setProperty(AbstractConjugatedGradientMinimizer.INITIAL_ENERGY_KEY, theInitialEnergy);
            theCopiedResultMolecule.setProperty(AbstractConjugatedGradientMinimizer.OPTIMIZED_ENERGY_KEY, this.itsCurrentEnergy);
            
            return theCopiedResultMolecule;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ProteinLigandComplexMinimizer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private void __setSearchVectorInRandomSample(Vector3d theCenterPosition, double theMaximumDegree) {
        Random theRandom = new Random();
        this.itsSearchVector = new Vector(this.itsLigand.getAtomCount()
                * Constant.POSITION_DIMENSION_SIZE);

        for (int di = 0; di < Constant.POSITION_DIMENSION_SIZE; di++) {
            Vector3d theAxis = new Vector3d(theCenterPosition);
            double theRotationAngle = (0.5 - theRandom.nextDouble()) * theMaximumDegree * 2.0;

            theAxis.set(di, theCenterPosition.get(di) + 1.0);

            for (int ai = 0, aEnd = this.itsLigand.getAtomCount(); ai < aEnd; ai++) {
                this.__setSearchVectorInRandomSample(ai, theCenterPosition, theAxis, theRotationAngle);
            }
        }
    }

    private void __setSearchVectorInRandomSample(int theAtomIndex, Vector3d theCenterPosition, Vector3d theAxis, double theRotationAngle) {
        try {
            IAtom theCopiedAtom = (IAtom)this.itsLigand.getAtom(theAtomIndex).clone();
            Vector3d theAtomPosition = new Vector3d(this.itsLigand.getAtom(theAtomIndex));
            Vector3d theMovedPosition = AngleCalculator.rotatePosition(theAtomPosition, theCenterPosition, theAxis, theRotationAngle);
            Vector3d theDirectionVector = Vector3dCalculator.minus(theMovedPosition, theAtomPosition);
            
            for (int di = 0; di < Constant.POSITION_DIMENSION_SIZE; di++) {
                int theIndex = theAtomIndex * Constant.POSITION_DIMENSION_SIZE + di;
                
                this.itsSearchVector.set(theIndex, theDirectionVector.get(di) + this.itsSearchVector.get(theIndex));
            }
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ProteinLigandComplexMinimizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Vector3d __getCenterPosition() {
        double theMaximumX = Double.MIN_VALUE;
        double theMaximumY = Double.MIN_VALUE;
        double theMaximumZ = Double.MIN_VALUE;

        double theMinimumX = Double.MAX_VALUE;
        double theMinimumY = Double.MAX_VALUE;
        double theMinimumZ = Double.MAX_VALUE;

        for (IAtom theAtom : this.itsLigand.atoms()) {
            Vector3d thePosition = new Vector3d(theAtom);

            if (thePosition.getX() < theMinimumX) {
                theMinimumX = thePosition.getX();
            }

            if (thePosition.getY() < theMinimumY) {
                theMinimumY = thePosition.getY();
            }

            if (thePosition.getZ() < theMinimumZ) {
                theMinimumZ = thePosition.getZ();
            }

            if (theMaximumX < thePosition.getX()) {
                theMaximumX = thePosition.getX();
            }

            if (theMaximumY < thePosition.getY()) {
                theMaximumY = thePosition.getY();
            }

            if (theMaximumZ < thePosition.getZ()) {
                theMaximumZ = thePosition.getZ();
            }
        }

        return new Vector3d((theMaximumX + theMinimumX) / 2.0, (theMaximumY + theMinimumY) / 2.0, (theMaximumZ + theMinimumZ) / 2.0);
    }

    private void __initializeResultObject() {
        try {
            this.itsResultProtein = new Protein(this.itsProtein);
            this.itsResultLigand = (IAtomContainer) this.itsLigand.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println("optimizeUsingProtein error!!");
        }
    }

    private void __moveAtomForLigand() {
        for (int ai = 0, aEnd = this.itsResultLigand.getAtomCount(); ai < aEnd; ai++) {
            IAtom theAtom = this.itsResultLigand.getAtom(ai);

            double theX = theAtom.getPoint3d().x + (Double) this.itsSearchVector.get((ai) * this.DIMENSION_SIZE + this.X_INDEX);
            double theY = theAtom.getPoint3d().y + (Double) this.itsSearchVector.get((ai) * this.DIMENSION_SIZE + this.Y_INDEX);
            double theZ = theAtom.getPoint3d().z + (Double) this.itsSearchVector.get((ai) * this.DIMENSION_SIZE + this.Z_INDEX);

            theAtom.setPoint3d(new Point3d(theX, theY, theZ));
        }
    }

    @Override
    protected double _calculateRMS() {
        int theAtomCount = this.itsResultLigand.getAtomCount();
        double theRMS = 0.0;

        for (int ai = 0; ai < theAtomCount; ai++) {
            Vector theMovedVectorInAtom = this.itsDirectionVector.subVector((ai) * Constant.POSITION_DIMENSION_SIZE,
                    (ai + 1) * Constant.POSITION_DIMENSION_SIZE);

            theRMS += theMovedVectorInAtom.length();
        }

        theRMS /= theAtomCount;

        return theRMS;
    }

    @Override
    protected void _initializeSearchVector() {
        this.itsSearchVector = new Vector(this.itsLigand.getAtomCount() * Constant.POSITION_DIMENSION_SIZE);
    }
}
