/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.minimization.abstracts;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public abstract class AbstractConjugatedGradientMinimizer<T extends Object, Function extends AbstractUsableInMinimizationInEnergyFunction> implements Serializable {

    private static final long serialVersionUID = 7972542595450524540L;

    protected IAtomContainer itsMolecule;
    protected Function itsFunction;
    protected AbstractLineSearcher itsLineSearcher;
    protected AbstractLineSearcher itsConjugatedGradientLineSearch;
    protected Vector itsSearchVector;
    protected Vector itsPreviousSearchVector;
    protected Vector itsPreviousDirectionVector;
    protected Vector itsInitialGradientVector;
    protected Vector itsDirectionVector;
    protected Double itsPreviousEnergy;
    protected Double itsCurrentEnergy;
    protected Double itsDelta;
    protected Integer itsNumberOfIteration;
    //constant Integer variable
    protected final int X_INDEX = 0;
    protected final int Y_INDEX = 1;
    protected final int Z_INDEX = 2;
    protected final int DIMENSION_SIZE = 3;
    //constant Double variable
    private final double ZERO_VALUE = 0.0;
    //constant String variable
    public static final String INITIAL_ENERGY_KEY = "Initial_Energy";
    public static final String OPTIMIZED_ENERGY_KEY = "Final_Energy";

    public AbstractConjugatedGradientMinimizer(Function theEnergyFunction) {
        this.itsFunction = theEnergyFunction;
        this.itsNumberOfIteration = 0;
    }

    public AbstractConjugatedGradientMinimizer(AbstractLineSearcher theLineSearcher, AbstractLineSearcher theConjugatedGradientLineSearcher) {
        this.itsLineSearcher = theLineSearcher;
        this.itsConjugatedGradientLineSearch = theConjugatedGradientLineSearcher;
        this.itsFunction = (Function)theLineSearcher.getFunction();
        this.itsNumberOfIteration = 0;
    }

    protected void _optimize() {
        do {
            this.itsNumberOfIteration++;
            this.itsPreviousEnergy = this.itsCurrentEnergy.doubleValue();
            this._calculateSearchDirection();
//            System.out.println(this.itsPreviousEnergy + " " + this.itsCurrentEnergy + " " + this.itsLineSearcher.getOptimumPoint());
        } while (!this._isStop());
    }

    public Function getFunction() {
        return itsFunction;
    }

    public void setFunction(Function theFunction) {
        this.itsFunction = theFunction;
    }
    
    public abstract T optimize();

    protected abstract void _initializeSearchVector();

    protected abstract void _calculateSearchDirection();

    protected abstract boolean _isStop();

    protected abstract double _calculateRMS();
}
