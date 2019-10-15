/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.minimization.linesearcher;

import java.io.Serializable;
import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractCrystalLineSearcher;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 03. 15
 */
public class CrystalInformationConjugatedGradientLineSearcher extends AbstractCrystalLineSearcher implements Serializable {
    private static final long serialVersionUID = 8668593560306733204L;

    public CrystalInformationConjugatedGradientLineSearcher(AbstractTotalForceFieldEnergyFunction theEnergyFunction, 
            CrystalInformation theInitialCrystalInformation) {
        super(theEnergyFunction, theInitialCrystalInformation);
    }

    @Override
    public Vector getOptimumDirectionVector() {
        return null;
    }

    @Override
    protected double _calculateEnergyUsingPoint(double thePoint) {
        IAtomContainer theMovedFractionMolecule = this._getMoleculeMovedDirection(new Vector(this.itsSearchVector.size()));
        CrystalInformation theCurrentCrystalInformation = this._sumOfCrystalInformation(this.itsInitialCrystalInformation, this.itsSearchCrystalInformation);
        CrystalInformation theDirectionCrystalInformation = this.itsFirstDerivative.calculateFirstDerivativeOfCrystalInformationInConjugatedGradient(theMovedFractionMolecule, 
                theCurrentCrystalInformation, thePoint);
        Vector theMoveVector = this.itsFirstDerivative.calculateMoleculeFractionCoordinateGradientVectorChangedOnCrystalInformation(theMovedFractionMolecule, 
                theCurrentCrystalInformation, this._sumOfCrystalInformation(theCurrentCrystalInformation, theDirectionCrystalInformation));
        
        Vector theGradientVector = this._getGradientVector(theMoveVector, theDirectionCrystalInformation);

        return ((AbstractUsableInMinimizationInEnergyFunction)this.itsFunction).calculateEnergyUsingGradientVector(theGradientVector);
    }

    public CrystalInformation getDirectionCrystalInformation() {
        IAtomContainer theMovedFractionMolecule = this._getMoleculeMovedDirection(new Vector(this.itsSearchVector.size()));
        CrystalInformation theCurrentCrystalInformation = this._sumOfCrystalInformation(this.itsInitialCrystalInformation, this.itsSearchCrystalInformation);
        
        return this.itsFirstDerivative.calculateFirstDerivativeOfCrystalInformationInConjugatedGradient(theMovedFractionMolecule, 
                theCurrentCrystalInformation, this.itsOptimumPoint);
    }
    
    public Vector getMoveVector() {
        IAtomContainer theMovedFractionMolecule = this._getMoleculeMovedDirection(new Vector(this.itsSearchVector.size()));
        CrystalInformation theCurrentCrystalInformation = this._sumOfCrystalInformation(this.itsInitialCrystalInformation, this.itsSearchCrystalInformation);
        CrystalInformation theDirectionCrystalInformation = this.itsFirstDerivative.calculateFirstDerivativeOfCrystalInformationInConjugatedGradient(theMovedFractionMolecule, 
                theCurrentCrystalInformation, this.itsOptimumPoint);
        
        return this.itsFirstDerivative.calculateMoleculeFractionCoordinateGradientVectorChangedOnCrystalInformation(theMovedFractionMolecule, 
                theCurrentCrystalInformation, this._sumOfCrystalInformation(theCurrentCrystalInformation, theDirectionCrystalInformation));
    }
}
