/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.minimizer;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractConjugatedGradientMinimizer;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractLineSearcher;
import org.bmdrc.basicchemistry.minimization.linesearcher.DefaultConjugatedGradientLineSearcher;
import org.bmdrc.basicchemistry.minimization.linesearcher.DefaultLineSearcher;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class ConjugatedGradientMinimizerInMolecularStructure<Function extends AbstractUsableInMinimizationInEnergyFunction>
        extends AbstractConjugatedGradientMinimizer<IAtomContainer, Function> implements Serializable {

    private static final long serialVersionUID = 2033459213359124807L;

    protected Integer itsMaximumIteration;
    protected Double itsMinimumRMS;
    protected Double itsInitialRMS;
    //constant Double variable
    private static final double DEFAULT_MINIMUM_RMS = 1.0e-4;
    //constant Integer variable
    private static final int DEFAULT_MAXIMUM_ITERATION = Integer.MAX_VALUE;

    public ConjugatedGradientMinimizerInMolecularStructure(AbstractUsableInMinimizationInEnergyFunction theEnergyFunction) {
        this(new DefaultLineSearcher(theEnergyFunction), new DefaultConjugatedGradientLineSearcher(theEnergyFunction));
    }

    public ConjugatedGradientMinimizerInMolecularStructure(AbstractUsableInMinimizationInEnergyFunction theEnergyFunction, double thePrecision) {
        this(new DefaultLineSearcher(theEnergyFunction), new DefaultConjugatedGradientLineSearcher(theEnergyFunction), 
                ConjugatedGradientMinimizerInMolecularStructure.DEFAULT_MAXIMUM_ITERATION, thePrecision);
    }
    
    public ConjugatedGradientMinimizerInMolecularStructure(AbstractUsableInMinimizationInEnergyFunction theEnergyFunction, Integer theMaximumIteration) {
        this(new DefaultLineSearcher(theEnergyFunction), new DefaultConjugatedGradientLineSearcher(theEnergyFunction), 
                theMaximumIteration, ConjugatedGradientMinimizerInMolecularStructure.DEFAULT_MINIMUM_RMS);
    }
    
    public ConjugatedGradientMinimizerInMolecularStructure(AbstractUsableInMinimizationInEnergyFunction theEnergyFunction, 
            Integer theMaximumIteration, double thePrecision) {
        this(new DefaultLineSearcher(theEnergyFunction), new DefaultConjugatedGradientLineSearcher(theEnergyFunction), 
                theMaximumIteration, thePrecision);
    }
    
    public ConjugatedGradientMinimizerInMolecularStructure(AbstractLineSearcher theLineSearcher, AbstractLineSearcher theConjugatedGradientLineSearcher) {
        this(theLineSearcher, theConjugatedGradientLineSearcher, ConjugatedGradientMinimizerInMolecularStructure.DEFAULT_MAXIMUM_ITERATION,
                ConjugatedGradientMinimizerInMolecularStructure.DEFAULT_MINIMUM_RMS );
    }

    public ConjugatedGradientMinimizerInMolecularStructure(AbstractLineSearcher theLineSearcher, AbstractLineSearcher theConjugatedGradientLineSearcher, Integer theMaximumIteration) {
        this(theLineSearcher, theConjugatedGradientLineSearcher, theMaximumIteration, ConjugatedGradientMinimizerInMolecularStructure.DEFAULT_MINIMUM_RMS);
    }
    
    public ConjugatedGradientMinimizerInMolecularStructure(AbstractLineSearcher theLineSearcher, AbstractLineSearcher theConjugatedGradientLineSearcher, double theMinimumRMS) {
        this(theLineSearcher, theConjugatedGradientLineSearcher, ConjugatedGradientMinimizerInMolecularStructure.DEFAULT_MAXIMUM_ITERATION, theMinimumRMS);
    }

    public ConjugatedGradientMinimizerInMolecularStructure(AbstractLineSearcher theLineSearcher, AbstractLineSearcher theConjugatedGradientLineSearcher, Integer theMaximumIteration, double theMinimumRMS) {
        super(theLineSearcher, theConjugatedGradientLineSearcher);
        this.itsMaximumIteration = theMaximumIteration;
        this.itsMinimumRMS = theMinimumRMS;
    }

    @Override
    public IAtomContainer optimize() {
        AbstractUsableInMinimizationInEnergyFunction theFunction = (AbstractUsableInMinimizationInEnergyFunction)this.itsFunction;
        double theInitialEnergy = theFunction.calculateEnergyFunction();
        
        this.itsCurrentEnergy = theInitialEnergy;
        this._initializeSearchVector();
        
        this._optimize();
        
        theFunction.getMolecule().setProperty(AbstractConjugatedGradientMinimizer.INITIAL_ENERGY_KEY, theInitialEnergy);
        theFunction.getMolecule().setProperty(AbstractConjugatedGradientMinimizer.OPTIMIZED_ENERGY_KEY, this.itsCurrentEnergy);
        
        IAtomContainer theResult = theFunction.setAtomPositionInTotalAtomPositionVector(this.itsSearchVector);
        
        return theResult;
    }
    
    @Override
    protected boolean _isStop() {
        double theRMS = this._calculateRMS();
        
        if (this.itsNumberOfIteration > this.itsMaximumIteration) {
            System.out.println(this.itsNumberOfIteration + "th Maximum Iteration End");
            return true;
        } else if (theRMS < this.itsMinimumRMS) {
            return true;
        } else if(this.itsCurrentEnergy.equals(Double.MAX_VALUE)) {
            return true;
        }
        
        return false;
    }

    @Override
    protected double _calculateRMS() {
        AbstractUsableInMinimizationInEnergyFunction theFunction = (AbstractUsableInMinimizationInEnergyFunction)this.itsFunction;
        int theAtomCount = theFunction.getMolecule().getAtomCount();
        double theRMS = 0.0;

        for (int ai = 0; ai < theAtomCount; ai++) {
            Vector theMovedVectorInAtom = this.itsDirectionVector.subVector(ai * Constant.POSITION_DIMENSION_SIZE, (ai + 1) * Constant.POSITION_DIMENSION_SIZE);

            theRMS += theMovedVectorInAtom.length();
        }

        theRMS /= theAtomCount;

        return theRMS;
    }

    @Override
    protected void _calculateSearchDirection() {
        this.itsFunction.calculateGradient(this.itsSearchVector);
        this.itsFunction.reloadGradientVector(this.itsSearchVector);
        
        this.itsLineSearcher.optimize(this.itsSearchVector);
        this.itsDirectionVector = this.itsLineSearcher.getOptimumDirectionVector();
        this.itsSearchVector = VectorCalculator.sum(this.itsSearchVector, this.itsDirectionVector);
        this.itsCurrentEnergy = this.itsLineSearcher.getOptimumFunctionValue();
        
//        if (this.itsNumberOfIteration > 1 && !this.itsLineSearcher.getOptimumPoint().equals(0.0)) {
//            this.itsFunction.makeConjugatedGradient(this.itsLineSearcher.getOptimumPoint());
//            this.itsFunction.reloadConjugatedGradientVector(this.itsSearchVector);
//            this.itsConjugatedGradientLineSearch.optimize(this.itsSearchVector);
//            Vector theAdditionalMoveVector = this.itsConjugatedGradientLineSearch.getOptimumDirectionVector();
//
//            this.itsSearchVector = VectorCalculator.sum(this.itsSearchVector, theAdditionalMoveVector);
//            this.itsDirectionVector = VectorCalculator.sum(this.itsDirectionVector, theAdditionalMoveVector);
//            this.itsCurrentEnergy = this.itsConjugatedGradientLineSearch.getOptimumFunctionValue();
//        }
    }

    @Override
    protected void _initializeSearchVector() {
        AbstractUsableInMinimizationInEnergyFunction theEnergyFunction = (AbstractUsableInMinimizationInEnergyFunction)this.itsFunction;
        
        this.itsSearchVector = new Vector((theEnergyFunction.getMolecule().getAtomCount() - theEnergyFunction.getNotCalculateAtomNumberList().size())
                * this.DIMENSION_SIZE);
    }
}
