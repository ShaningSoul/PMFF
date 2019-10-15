/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.minimization.abstracts;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import javax.vecmath.Point3d;
import java.io.Serializable;
import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.crystal.CrystalStructureGenerator;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.math.FirstDerivativeOfCrystal;
import org.bmdrc.basicchemistry.minimization.linesearcher.CrystalInformationLineSearcher;
import org.bmdrc.basicchemistry.minimization.linesearcher.FractionCoordinateLineSearcher;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;

/**
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 23
 */
public abstract class AbstractCrystalOptimizer implements Serializable {

    private static final long serialVersionUID = 6374802409801922638L;

    protected FirstDerivativeOfCrystal itsFirstDerivative;
    protected IAtomContainer itsMolecule;
    protected CrystalInformation itsCrystalInformation;
    protected CrystalInformation itsSearchCrystalInformation;
    protected AbstractTotalForceFieldEnergyFunction itsEnergyFunction;
    protected FractionCoordinateLineSearcher itsFractionLineSearcher;
    protected CrystalInformationLineSearcher itsCrystalInformationLineSeacher;
    protected Vector itsInitialPositionVectorByFractionCoordinate;
    protected Vector itsInitialPositionVectorByOriginalCoordinate;
    protected Vector itsSearchVectorByOriginalCoordinate;
    protected Vector itsSearchVectorByFractionCoordinate;
    protected Vector itsDirectionVectorByOriginalCoordinate;
    protected Vector itsDirectionVectorByFractionCoordinate;
    protected Double itsPreviousEnergy;
    protected Double itsCurrentEnergy;
    protected Integer itsNumberOfIteration;
    protected Integer itsOriginalAtomCount;
    protected Double itsRMS;
    protected Integer itsNumberOfIterationForCellParameter;
    protected Integer itsNumberOfIterationForCoordinate;
    //constant Integer variable
    protected final int X_INDEX = 0;
    protected final int Y_INDEX = 1;
    protected final int Z_INDEX = 2;
    protected final int DIMENSION_SIZE = 3;
    //constant Double variable
    private final double ZERO_VALUE = 0.0;
    private final double INVERSE_FACTOR = -1.0;
    private final double MINIMUM_RMSD = 1.0e-4;
    //constant Boolean variable
    public static final boolean INITIAL_FIX_CELL_DIMENSION = true;

    public AbstractCrystalOptimizer(AbstractTotalForceFieldEnergyFunction theEnergyFunction,
            CrystalInformation theCrystalInformation) {
        this.itsEnergyFunction = theEnergyFunction;
        this.itsFractionLineSearcher = new FractionCoordinateLineSearcher(theEnergyFunction, theCrystalInformation);
        this.itsCrystalInformationLineSeacher = new CrystalInformationLineSearcher(theEnergyFunction, theCrystalInformation);
        this.itsFirstDerivative = new FirstDerivativeOfCrystal(theEnergyFunction);
        this.itsSearchCrystalInformation = new CrystalInformation(theCrystalInformation.getSpaceGroup());
        this.itsCrystalInformation = theCrystalInformation;
        this.itsNumberOfIteration = 0;
    }

    public Integer getOriginalAtomCount() {
        return itsOriginalAtomCount;
    }

    public void setOriginalAtomCount(Integer theOriginalAtomCount) {
        this.itsOriginalAtomCount = theOriginalAtomCount;
    }

    public CrystalInformation getSearchCrystalInformation() {
        return itsSearchCrystalInformation;
    }

    public IAtomContainer optimize() {
        double theInitialEnergy = this.itsEnergyFunction.calculateEnergyFunction();
        this.itsCurrentEnergy = theInitialEnergy;
        this.itsMolecule = this.itsEnergyFunction.getMolecule();
        this.itsNumberOfIteration = 0;

        this.__initializeVector();
        
        do {
            this.__calculateSearchDirection();
        } while (this.itsNumberOfIterationForCellParameter != 1 || this.itsNumberOfIterationForCoordinate != 1);

        IAtomContainer theResultCrystal = this._getMovedCrystal();
        
        theResultCrystal.setProperty("Initial_Energy", theInitialEnergy);
        theResultCrystal.setProperty("Final_Energy", this.itsCurrentEnergy);

        return theResultCrystal;
    }

    private void __initializeVector() {
        this.__initializePositionVector();

        this.itsSearchVectorByOriginalCoordinate = new Vector(this.itsEnergyFunction.getMolecule().getAtomCount() * this.DIMENSION_SIZE);
        this.itsDirectionVectorByOriginalCoordinate = new Vector(this.itsEnergyFunction.getMolecule().getAtomCount() * this.DIMENSION_SIZE);

        this.itsSearchVectorByFractionCoordinate = new Vector(this.itsEnergyFunction.getUnitMolecule().getAtomCount() * this.DIMENSION_SIZE);
        this.itsDirectionVectorByFractionCoordinate = new Vector(this.itsEnergyFunction.getUnitMolecule().getAtomCount() * this.DIMENSION_SIZE);
    }

    private void __initializePositionVector() {
        this.itsInitialPositionVectorByFractionCoordinate = new Vector();
        this.itsInitialPositionVectorByOriginalCoordinate = new Vector();

        for (IAtom theAtom : this.itsEnergyFunction.getUnitMolecule().atoms()) {
            Point3d thePosition = theAtom.getPoint3d();

            this.itsInitialPositionVectorByFractionCoordinate.add(thePosition.x);
            this.itsInitialPositionVectorByFractionCoordinate.add(thePosition.y);
            this.itsInitialPositionVectorByFractionCoordinate.add(thePosition.z);
        }

        for (IAtom theAtom : this.itsEnergyFunction.getMolecule().atoms()) {
            Point3d thePosition = theAtom.getPoint3d();

            this.itsInitialPositionVectorByOriginalCoordinate.add(thePosition.x);
            this.itsInitialPositionVectorByOriginalCoordinate.add(thePosition.y);
            this.itsInitialPositionVectorByOriginalCoordinate.add(thePosition.z);
        }
    }

    protected abstract double _calculateRMS();

    private void __calculateSearchDirection() {
        this.itsNumberOfIterationForCoordinate = 0;
        this.itsNumberOfIterationForCellParameter = 0;
        
        do {
            this.itsPreviousEnergy = this.itsCurrentEnergy.doubleValue();

            this.__setSearchVector();

            this.itsNumberOfIterationForCoordinate++;
            this.itsNumberOfIteration++;
        } while (!this._isStop());
        
        do {
            this.itsPreviousEnergy = this.itsCurrentEnergy.doubleValue();

            this.__setSearchCrystalInformation();

            this.itsNumberOfIterationForCellParameter++;
            this.itsNumberOfIteration++;
        } while (!this._isStop());
        
    }

    private void __setSearchVector() {
        this.__makeDirectionVectorByFractionCoordinateUsedInCoordinate();
        this.__makeDirectionVectorByOriginalCoordinate();

        this.itsFractionLineSearcher.optimize(this.itsSearchVectorByOriginalCoordinate, VectorCalculator.productScalarValue(1.0 / this.itsDirectionVectorByOriginalCoordinate.length(),
                this.itsDirectionVectorByOriginalCoordinate));

        this.itsSearchVectorByFractionCoordinate = VectorCalculator.sum(this.itsSearchVectorByFractionCoordinate,
                VectorCalculator.productScalarValue(this.itsFractionLineSearcher.getOptimumPoint(), this.itsDirectionVectorByFractionCoordinate));
        this.itsSearchVectorByOriginalCoordinate = VectorCalculator.sum(this.itsSearchVectorByOriginalCoordinate,
                VectorCalculator.productScalarValue(this.itsFractionLineSearcher.getOptimumPoint(), this.itsDirectionVectorByOriginalCoordinate));
        this.itsCurrentEnergy = this.itsFractionLineSearcher.getOptimumFunctionValue();
    }

    private void __setSearchCrystalInformation() {
        this.__makeDirectionVectorByFractionCoordinateUsedInCrytstalInformation();
        this.__makeDirectionVectorByOriginalCoordinate();

        this.itsCrystalInformationLineSeacher.optimize(this.itsSearchVectorByOriginalCoordinate, this.itsDirectionVectorByFractionCoordinate);

        this.itsSearchCrystalInformation = this.__sumOfCrystalInformation(this.itsSearchCrystalInformation, 
                this.itsCrystalInformationLineSeacher.getOptimumCrystalInformation());
        this.itsCurrentEnergy = this.itsCrystalInformationLineSeacher.getOptimumFunctionValue();
    }

    protected abstract boolean _isStop();

    protected IAtomContainer _getMovedCrystal() {
        Vector theCurrentAtomPositionInFractionCoordinate = VectorCalculator.sum(this.itsInitialPositionVectorByFractionCoordinate, this.itsSearchVectorByFractionCoordinate);
        Vector theCurrentAtomPositionInOriginalCoordinate = VectorCalculator.sum(this.itsInitialPositionVectorByOriginalCoordinate, this.itsSearchVectorByOriginalCoordinate);
        IAtomContainer theMovedFractionMolecule = this.__getMovedFractionMolecule();
        IAtomContainer theUnitCell = this.itsCrystalInformation.getSpaceGroup().generateCrystalStructure(theMovedFractionMolecule);

        return CrystalStructureGenerator.generateCrystalCell(theUnitCell, this.__sumOfCrystalInformation(this.itsCrystalInformation,
                this.itsSearchCrystalInformation));
    }

    private IAtomContainer __getMovedFractionMolecule() {
        try {
            IAtomContainer theFractionMolecule = (IAtomContainer) this.itsEnergyFunction.getUnitMolecule().clone();

            for (int ai = 0, aEnd = theFractionMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d theAtomPosition = theFractionMolecule.getAtom(ai).getPoint3d();
                double theX = theAtomPosition.x + (Double) this.itsSearchVectorByFractionCoordinate.get(ai * this.DIMENSION_SIZE + this.X_INDEX);
                double theY = theAtomPosition.y + (Double) this.itsSearchVectorByFractionCoordinate.get(ai * this.DIMENSION_SIZE + this.Y_INDEX);
                double theZ = theAtomPosition.z + (Double) this.itsSearchVectorByFractionCoordinate.get(ai * this.DIMENSION_SIZE + this.Z_INDEX);

                theFractionMolecule.getAtom(ai).setPoint3d(new Point3d(theX, theY, theZ));
            }

            return theFractionMolecule;
        } catch (CloneNotSupportedException ex) {
        }

        return null;
    }

    private CrystalInformation __sumOfCrystalInformation(CrystalInformation theFirstInformation, CrystalInformation theSecondInformation) {
        CrystalInformation theResultCrystalInformation = new CrystalInformation(theFirstInformation.getSpaceGroup());

        theResultCrystalInformation.setCellDimensionVector(Vector3dCalculator.sum(theFirstInformation.getCellDimensionVector(),
                theSecondInformation.getCellDimensionVector()));
        theResultCrystalInformation.setCoordinateAngleVector(Vector3dCalculator.sum(theFirstInformation.getCoordinateAngleVector(),
                theSecondInformation.getCoordinateAngleVector()));

        return theResultCrystalInformation;
    }

    private Vector __makeDirectionVectorByFunction() {
        IAtomContainer theMovedCell = this._getMovedCrystal();

        this.itsEnergyFunction.calculateGradient(theMovedCell);
        this.itsEnergyFunction.reloadGradientVector(theMovedCell);

        return this.itsEnergyFunction.getGradientVector();
    }

    private void __makeDirectionVectorByFractionCoordinateUsedInCoordinate() {
        Vector theDirectionVectorByFunction = this.__makeDirectionVectorByFunction();
        int theUnitAtomCount = this.itsEnergyFunction.getUnitMolecule().getAtomCount();

        this.itsDirectionVectorByFractionCoordinate = new Vector(this.itsEnergyFunction.getUnitMolecule().getAtomCount() * this.DIMENSION_SIZE);

        for (int mi = 0; mi < this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE; mi++) {
            int theStartIndex = mi * theUnitAtomCount;

            for (int ai = 0; ai < theUnitAtomCount; ai++) {
                Vector theGradientVectorByAtom = theDirectionVectorByFunction.subVector((theStartIndex + ai) * this.DIMENSION_SIZE,
                        (theStartIndex + ai + 1) * this.DIMENSION_SIZE);
                Vector theChangeGradientByCenterAtom = this.itsCrystalInformation.getSpaceGroup().generateIntraGradientVectorByMotherMolecule(theGradientVectorByAtom,
                        this.itsEnergyFunction.getMolecule().getAtom(theStartIndex + ai));
                int theXIndex = ai * this.DIMENSION_SIZE + this.X_INDEX;
                int theYIndex = ai * this.DIMENSION_SIZE + this.Y_INDEX;
                int theZIndex = ai * this.DIMENSION_SIZE + this.Z_INDEX;

                this.itsDirectionVectorByFractionCoordinate.set(theXIndex, theDirectionVectorByFunction.get(theXIndex) + theChangeGradientByCenterAtom.get(this.X_INDEX));
                this.itsDirectionVectorByFractionCoordinate.set(theYIndex, theDirectionVectorByFunction.get(theYIndex) + theChangeGradientByCenterAtom.get(this.Y_INDEX));
                this.itsDirectionVectorByFractionCoordinate.set(theZIndex, theDirectionVectorByFunction.get(theZIndex) + theChangeGradientByCenterAtom.get(this.Z_INDEX));
            }
        }
    }

    private void __makeDirectionVectorByFractionCoordinateUsedInCrytstalInformation() {
        Vector theDirectionVectorByFunction = this.__makeDirectionVectorByFunction();
        int theUnitAtomCount = this.itsEnergyFunction.getUnitMolecule().getAtomCount();

        this.itsDirectionVectorByFractionCoordinate = new Vector(this.itsEnergyFunction.getUnitMolecule().getAtomCount() * this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE
                * this.DIMENSION_SIZE);

        for (int mi = 0; mi < this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE; mi++) {
            int theStartIndex = mi * theUnitAtomCount;

            for (int ai = 0; ai < theUnitAtomCount; ai++) {
                Vector theGradientVectorByAtom = theDirectionVectorByFunction.subVector((theStartIndex + ai) * this.DIMENSION_SIZE,
                        (theStartIndex + ai + 1) * this.DIMENSION_SIZE);
                Vector theChangeGradientByCenterAtom = this.itsCrystalInformation.getSpaceGroup().generateIntraGradientVectorByMotherMolecule(theGradientVectorByAtom,
                        this.itsEnergyFunction.getMolecule().getAtom(theStartIndex + ai));
                int theXIndex = ai * this.DIMENSION_SIZE + this.X_INDEX;
                int theYIndex = ai * this.DIMENSION_SIZE + this.Y_INDEX;
                int theZIndex = ai * this.DIMENSION_SIZE + this.Z_INDEX;

                this.itsDirectionVectorByFractionCoordinate.set(theXIndex, theDirectionVectorByFunction.get(theXIndex) + theChangeGradientByCenterAtom.get(this.X_INDEX));
                this.itsDirectionVectorByFractionCoordinate.set(theYIndex, theDirectionVectorByFunction.get(theYIndex) + theChangeGradientByCenterAtom.get(this.Y_INDEX));
                this.itsDirectionVectorByFractionCoordinate.set(theZIndex, theDirectionVectorByFunction.get(theZIndex) + theChangeGradientByCenterAtom.get(this.Z_INDEX));
            }
        }
    }

    private void __makeDirectionVectorByOriginalCoordinate() {
        Vector theCurrentPositionVectorByFractionCoordinate = VectorCalculator.sum(this.itsInitialPositionVectorByFractionCoordinate, this.itsDirectionVectorByFractionCoordinate);
        Vector theCurrentPositionVectorByUnitCell = this.itsCrystalInformation.getSpaceGroup().generateCrystalStructure(theCurrentPositionVectorByFractionCoordinate);
        Vector theCurrentPositionVectorByOriginalCoordinate = VectorCalculator.sum(this.itsInitialPositionVectorByOriginalCoordinate, this.itsSearchVectorByOriginalCoordinate);
        CrystalInformation theCrystalInformation = this.__sumOfCrystalInformation(this.itsCrystalInformation, this.itsSearchCrystalInformation);
        Vector theAtomPositionOriginalCoordinateVectorInCrystalcell = CrystalStructureGenerator.generateCrystalCellVectorByOriginalCoordinate(theCurrentPositionVectorByUnitCell,
                theCrystalInformation);

        this.itsDirectionVectorByOriginalCoordinate = VectorCalculator.minus(theCurrentPositionVectorByOriginalCoordinate, theAtomPositionOriginalCoordinateVectorInCrystalcell);
    }
}
