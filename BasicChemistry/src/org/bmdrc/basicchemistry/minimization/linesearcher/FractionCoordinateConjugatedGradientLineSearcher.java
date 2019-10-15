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
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractCrystalLineSearcher;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 03. 15
 */
public class FractionCoordinateConjugatedGradientLineSearcher extends AbstractCrystalLineSearcher implements Serializable {

    private static final long serialVersionUID = -5751915040091005222L;

    public FractionCoordinateConjugatedGradientLineSearcher(AbstractTotalForceFieldEnergyFunction theEnergyFunction, 
            CrystalInformation theInitialCrystalInformation) {
        super(theEnergyFunction, theInitialCrystalInformation);
    }

    @Override
    public Vector getOptimumDirectionVector() {
        return null;
    }

    @Override
    protected double _calculateEnergyUsingPoint(double thePoint) {
        CrystalInformation theCurrentCrystalInformation = this._sumOfCrystalInformation(this.itsInitialCrystalInformation, this.itsSearchCrystalInformation);
        Vector theDirectionVector = this.itsFirstDerivative.calculateFristaDerivativeOfFractionCoordinateInConjugatedGradient(null,
                theCurrentCrystalInformation, thePoint);

        Vector theGradientVector = this._getGradientVector(theDirectionVector, null);
        double theEnergy = this.itsFunction.calculateEnergyUsingGradientVector(theGradientVector);

        return theEnergy;
    }

    public Vector getDirectionVector() {
        IAtomContainer theMovedFractionMolecule = this._getMoleculeMovedDirection(new Vector(this.itsSearchVector.size()));
        CrystalInformation theCurrentCrystalInformation = this._sumOfCrystalInformation(this.itsInitialCrystalInformation, this.itsSearchCrystalInformation);

        return this.itsFirstDerivative.calculateFristaDerivativeOfFractionCoordinateInConjugatedGradient(theMovedFractionMolecule, theCurrentCrystalInformation, this.itsOptimumPoint);
    }
}
