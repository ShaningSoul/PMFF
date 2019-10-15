/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.minimizer;

import java.io.Serializable;
import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractCrystalOptimizer;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 30
 */
public class DefaultCrystalOptimizer extends AbstractCrystalOptimizer implements Serializable {

    private static final long serialVersionUID = 6948324539472994974L;

    private Double itsMinimumRMS;
    private Vector itsAtomPositionVector;
    //constant Double variable
    private static final double DEFAULT_MINIMUM_RMS = 1.0e-4;

    public DefaultCrystalOptimizer(AbstractTotalForceFieldEnergyFunction theEnergyFunction, CrystalInformation theCrystalInformation) {
        this(theEnergyFunction, DefaultCrystalOptimizer.DEFAULT_MINIMUM_RMS, theCrystalInformation, theEnergyFunction.getMolecule());
    }

    public DefaultCrystalOptimizer(AbstractTotalForceFieldEnergyFunction theEnergyFunction, CrystalInformation theCrystalInformation, 
            IAtomContainer theOriginalMolecule) {
        this(theEnergyFunction, DefaultCrystalOptimizer.DEFAULT_MINIMUM_RMS, theCrystalInformation, theOriginalMolecule);
    }

    public DefaultCrystalOptimizer(AbstractTotalForceFieldEnergyFunction theEnergyFunction, double theMinimumRMS, CrystalInformation theCrystalInformation, 
            IAtomContainer theOriginalMolecule) {
        super(theEnergyFunction, theCrystalInformation);
        this.itsMinimumRMS = theMinimumRMS;
        this.itsOriginalAtomCount = theOriginalMolecule.getAtomCount();
        this.__initializePreviousSearchVector();
    }
    
    public Integer getNumberOfIteration() {
        return itsNumberOfIteration;
    }

    private void __initializePreviousSearchVector() {
        this.itsAtomPositionVector = new Vector();

        for (int ai = 0, aEnd = this.itsEnergyFunction.getUnitMolecule().getAtomCount() * this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE; ai < aEnd; ai++) {
            Vector3d theMovedAtomPosition = new Vector3d(this.itsEnergyFunction.getMolecule().getAtom(ai));

            this.itsAtomPositionVector.addAll(theMovedAtomPosition);
        }
    }

    @Override
    protected double _calculateRMS() {
        int theAtomCount = this.itsEnergyFunction.getUnitMolecule().getAtomCount() * this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE;
        this.itsRMS = 0.0;
        Vector theMoveVector = this.__calculateMoveVector();

        for (int ai = 0; ai < theAtomCount; ai++) {
            Vector theMovedVectorInAtom = theMoveVector.subVector(ai * this.DIMENSION_SIZE, (ai + 1) * this.DIMENSION_SIZE);

            theMovedVectorInAtom = VectorCalculator.minus(theMovedVectorInAtom, this.itsAtomPositionVector
                    .subVector(ai * this.DIMENSION_SIZE, (ai + 1) * this.DIMENSION_SIZE));
            this.itsRMS += theMovedVectorInAtom.length();
        }
        
        this.itsAtomPositionVector = theMoveVector;

        this.itsRMS /= theAtomCount;

        return this.itsRMS;
    }

    private Vector __calculateMoveVector() {
        Vector theMoveVector = new Vector();
        IAtomContainer theMovedCrystal = this._getMovedCrystal();

        for (int ai = 0, aEnd = this.itsEnergyFunction.getUnitMolecule().getAtomCount() * this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE; ai < aEnd; ai++) {
            Vector3d theMovedAtomPosition = new Vector3d(theMovedCrystal.getAtom(ai));

            theMoveVector.addAll(theMovedAtomPosition);
        }

        return theMoveVector;
    }

    @Override
    protected boolean _isStop() {
        this._calculateRMS();
        
        return this.itsRMS < this.itsMinimumRMS;
    }
}
