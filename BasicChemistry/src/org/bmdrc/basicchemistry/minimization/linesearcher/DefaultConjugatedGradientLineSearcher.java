/*
 *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 *  * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  *
 *  * Copyright (C) 2017, SungBo Hwang <tyamazaki@naver.com>.
 *
 */

package org.bmdrc.basicchemistry.minimization.linesearcher;
/**
 * Created by Sungbo on 2017. 4. 21..
 */

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractLineSearcher;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;

public class DefaultConjugatedGradientLineSearcher extends AbstractLineSearcher implements Serializable {

    private static final long serialVersionUID = 8850799301013709209L;

    public DefaultConjugatedGradientLineSearcher(AbstractUsableInMinimizationInEnergyFunction theFunction) {
        super(theFunction);
    }

    public DefaultConjugatedGradientLineSearcher(AbstractUsableInMinimizationInEnergyFunction theFunction, Double thePrecision) {
        super(theFunction, thePrecision);
    }

    @Override
    protected double _calculateEnergyUsingPoint(double thePoint) {
        Vector theConjugatedGradient = this.itsFunction.calculateConjugatedGradientVector(this.itsSearchVector, thePoint);
        Vector theDirectionVector = VectorCalculator.sum(this.itsSearchVector, theConjugatedGradient);
        double theEnergy = this.itsFunction.calculateEnergyUsingGradientVector(theDirectionVector);
        
        if(this._isNotCorrectValue(theEnergy)) {
            return Double.MAX_VALUE;
        } else if(theConjugatedGradient.length() > 1.0) {
            return Double.MAX_VALUE;
        }

        return theEnergy;
    }

    @Override
    public Vector getOptimumDirectionVector() {
        return this.itsFunction.calculateConjugatedGradientVector(this.itsSearchVector, this.itsOptimumPoint);
    }
}
