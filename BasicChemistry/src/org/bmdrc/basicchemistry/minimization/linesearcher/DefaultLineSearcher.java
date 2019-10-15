/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.minimization.linesearcher;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractLineSearcher;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;

/**
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class DefaultLineSearcher extends AbstractLineSearcher implements Serializable {

    private static final long serialVersionUID = 7900745941789925648L;

    public DefaultLineSearcher(AbstractUsableInMinimizationInEnergyFunction theEnergyFunction) {
        super(theEnergyFunction);
    }

    public DefaultLineSearcher(AbstractUsableInMinimizationInEnergyFunction theEnergyFunction, double thePrecision) {
        super(theEnergyFunction, thePrecision);
    }

    @Override
    protected double _calculateEnergyUsingPoint(double thePoint) {
        Vector theGradientVector = this.itsFunction.calculateGradientVector(this.itsSearchVector, thePoint);

        Vector theTotalGradient = VectorCalculator.sum(this.itsSearchVector, theGradientVector);
        double theEnergy = this.itsFunction.calculateEnergyUsingGradientVector(theTotalGradient);

        if(this._isNotCorrectValue(theEnergy)) {
            return Double.MAX_VALUE;
        } 

        return theEnergy;
    }

    @Override
    public Vector getOptimumDirectionVector() {
        return this.itsFunction.calculateGradientVector(this.itsSearchVector, this.itsOptimumPoint);
    }
}
