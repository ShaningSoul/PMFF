/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.minimization.linesearcher;

import java.io.Serializable;
import org.openscience.cdk.interfaces.IAtomContainer;

import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.crystal.CrystalStructureGenerator;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractCrystalLineSearcher;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 12. 15
 */
public class FractionCoordinateLineSearcher extends AbstractCrystalLineSearcher implements Serializable {

    private static final long serialVersionUID = -4569757025125263890L;

    public FractionCoordinateLineSearcher(AbstractTotalForceFieldEnergyFunction theEnergyFunction, CrystalInformation theInitialCrystalInformation) {
        super(theEnergyFunction, theInitialCrystalInformation);
    }


    @Override
    public Vector getOptimumDirectionVector() {
        return VectorCalculator.productScalarValue(this.itsOptimumPoint, this.itsSearchVector);
    }

    @Override
    protected double _calculateEnergyUsingPoint(double thePoint) {
        AbstractUsableInMinimizationInEnergyFunction theEnergyFunction = (AbstractUsableInMinimizationInEnergyFunction)this.itsFunction;
        IAtomContainer theMovedFractionMolecule = this._getMoleculeMovedDirection(this.itsSearchVector);
        CrystalInformation theCurrentCrystalInformation = this._sumOfCrystalInformation(this.itsInitialCrystalInformation, this.itsSearchCrystalInformation);
        Vector theDirectionVector = this.itsFirstDerivative.calculateFristaDerivativeOfFractionCoordinateInGradient(theMovedFractionMolecule, theCurrentCrystalInformation, 
                thePoint);

        this.__makeDirectionVectorByUnitMolecule();
        Vector theGradientVector = this._getGradientVector(theDirectionVector, null);

        return theEnergyFunction.calculateEnergyUsingGradientVector(theGradientVector);
    }

    private void __makeDirectionVectorByUnitMolecule() {
        AbstractTotalForceFieldEnergyFunction theFunction = (AbstractTotalForceFieldEnergyFunction)this.itsFunction;
        IAtomContainer theMovedCell = this._getMovedCrystal();
        int theUnitAtomCount = theFunction.getUnitMolecule().getAtomCount();

        theFunction.calculateGradient(theMovedCell);
        theFunction.reloadGradientVector(theMovedCell);

        this.itsDirectionVector = new Vector(theUnitAtomCount * Constant.POSITION_DIMENSION_SIZE);
        Vector theGradientVector = theFunction.getGradientVector();

        for(int mi = 0; mi < this.itsInitialCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE; mi++) {
            int theStartIndex = mi * theUnitAtomCount;

            for(int ai = 0; ai < theUnitAtomCount; ai++) {
                Vector theGradientVectorByAtom = theGradientVector.subVector((theStartIndex + ai) * Constant.POSITION_DIMENSION_SIZE, (theStartIndex + ai + 1) * Constant.POSITION_DIMENSION_SIZE);
                Vector theChangeGradientByCenterAtom = this.itsInitialCrystalInformation.getSpaceGroup().generateIntraGradientVectorByMotherMolecule(theGradientVectorByAtom,
                        theFunction.getMolecule().getAtom(theStartIndex + ai));
                int theXIndex = ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.X_INDEX;
                int theYIndex = ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.Y_INDEX;
                int theZIndex = ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.Z_INDEX;

                this.itsDirectionVector.set(theXIndex, this.itsDirectionVector.get(theXIndex) + theChangeGradientByCenterAtom.get(Vector3d.X_INDEX));
                this.itsDirectionVector.set(theYIndex, this.itsDirectionVector.get(theYIndex) + theChangeGradientByCenterAtom.get(Vector3d.Y_INDEX));
                this.itsDirectionVector.set(theZIndex, this.itsDirectionVector.get(theZIndex) + theChangeGradientByCenterAtom.get(Vector3d.Z_INDEX));
            }
        }
    }

    protected IAtomContainer _getMovedCrystal() {
        CrystalInformation theCurrentCrystalInformation = this._sumOfCrystalInformation(this.itsInitialCrystalInformation, this.itsSearchCrystalInformation);
        IAtomContainer theMovedFractionMolecule = this.__getMovedFractionMolecule();
        IAtomContainer theUnitCell = theCurrentCrystalInformation.getSpaceGroup().generateCrystalStructure(theMovedFractionMolecule);

        return CrystalStructureGenerator.generateCrystalCell(theUnitCell, theCurrentCrystalInformation);
    }

    private IAtomContainer __getMovedFractionMolecule() {
        try {
            AbstractTotalForceFieldEnergyFunction theFunction = (AbstractTotalForceFieldEnergyFunction)this.itsFunction;
            IAtomContainer theFractionMolecule = (IAtomContainer) theFunction.getUnitMolecule().clone();

            for (int ai = 0, aEnd = theFractionMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d theAtomPosition = theFractionMolecule.getAtom(ai).getPoint3d();
                double theX = theAtomPosition.x + (Double) this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.X_INDEX);
                double theY = theAtomPosition.y + (Double) this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.Y_INDEX);
                double theZ = theAtomPosition.z + (Double) this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.Z_INDEX);

                theFractionMolecule.getAtom(ai).setPoint3d(new Point3d(theX, theY, theZ));
            }

            return theFractionMolecule;
        } catch (CloneNotSupportedException ex) {
            System.err.println("");
        }

        return null;
    }

    public Vector getDirectionVector() {
        IAtomContainer theMovedFractionMolecule = this._getMoleculeMovedDirection(this.itsSearchVector);
        CrystalInformation theCurrentCrystalInformation = this._sumOfCrystalInformation(this.itsInitialCrystalInformation, this.itsSearchCrystalInformation);
        
        return this.itsFirstDerivative.calculateFristaDerivativeOfFractionCoordinateInGradient(theMovedFractionMolecule, theCurrentCrystalInformation, this.itsOptimumPoint);
    }
}
