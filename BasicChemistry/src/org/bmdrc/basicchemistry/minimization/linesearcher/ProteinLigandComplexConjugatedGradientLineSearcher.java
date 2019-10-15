/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.minimization.linesearcher;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 02. 16
 */
public class ProteinLigandComplexConjugatedGradientLineSearcher extends DefaultConjugatedGradientLineSearcher implements Serializable {

    private static final long serialVersionUID = 3563919000562759056L;

    private IAtomContainer itsLigand;
    
    public ProteinLigandComplexConjugatedGradientLineSearcher(AbstractUsableInMinimizationInEnergyFunction theEnergyFunction, IAtomContainer theLigand) {
        super(theEnergyFunction);
        this.itsLigand = theLigand;
    }
    
    @Override
    protected double _calculateEnergyUsingPoint(double thePoint) {
        Vector theConjugatedGradientVector = this.itsFunction.calculateConjugatedGradientVector(this.itsSearchVector, thePoint)
                .subVector(0, this.itsLigand.getAtomCount() * Constant.POSITION_DIMENSION_SIZE);
        
        if(this._calculateRMS(theConjugatedGradientVector) > 1.0) {
            return Double.MAX_VALUE;
        }
        
        Vector theTotalGradient = VectorCalculator.sum(this.itsSearchVector, theConjugatedGradientVector);
        double theEnergy = this.itsFunction.calculateEnergyUsingGradientVector(theTotalGradient);

        if(this._isNotCorrectValue(theEnergy)) {
            return Double.MAX_VALUE;
        }

        return theEnergy;
    }

    @Override
    protected void _initializeRange() {
        this.itsFunction.reloadGradientVector();
        
        this.itsRange = 1.0 / this._calculateRMS(this.itsFunction.getGradientVector().subVector(0, 
                this.itsLigand.getAtomCount() * Constant.POSITION_DIMENSION_SIZE));
    }
    
    protected double _calculateRMS(Vector theVector) {
        AbstractUsableInMinimizationInEnergyFunction theFunction = (AbstractUsableInMinimizationInEnergyFunction)this.itsFunction;
        int theAtomCount = theFunction.getMolecule().getAtomCount();
        double theRMS = 0.0;

        for (int ai = 0; ai < theVector.size() / Constant.POSITION_DIMENSION_SIZE; ai++) {
            Vector theMovedVectorInAtom = theVector.subVector(ai * Constant.POSITION_DIMENSION_SIZE, (ai + 1) * Constant.POSITION_DIMENSION_SIZE);

            theRMS += theMovedVectorInAtom.length();
        }

        theRMS /= theAtomCount;

        return theRMS;
    }
    
    @Override
    public Vector getOptimumDirectionVector() {
        return this.itsFunction.calculateConjugatedGradientVector(this.itsSearchVector, this.itsOptimumPoint);
    }
}
