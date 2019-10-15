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

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public abstract class AbstractLineSearcher implements Serializable {

    private static final long serialVersionUID = -2315286901542722582L;

    protected AbstractUsableInMinimizationInEnergyFunction itsFunction;
    protected Double itsStartPoint;
    protected Double itsGoldenPoint;
    protected Double itsEndPoint;
    protected Double itsOptimumPoint;
    protected Double itsOptimumFunctionValue;
    protected Double itsFunctionValueInStartPoint;
    protected Double itsFunctionValueInGoldenPoint;
    protected Double itsFunctionValueInEndPoint;
    protected Double itsRange;
    protected Double itsPrecision;
    protected Vector itsSearchVector;
    protected Vector itsDirectionVector;
    protected Double itsInitialValue;
    //constant Double variable
    private final double INITIAL_START_POINT = 0.0;
    private final double INITIAL_RANGE = 1.0;
    private final double GOLDEN_VALUE = 2.0 / (1 + Math.sqrt(5.0));
    protected static final double Default_Precision = 1.0e-10;
    
    public AbstractLineSearcher(AbstractUsableInMinimizationInEnergyFunction theFunction) {
        this(theFunction, AbstractLineSearcher.Default_Precision);
    }

    public AbstractLineSearcher(AbstractUsableInMinimizationInEnergyFunction theFunction, Double thePrecision) {
        this.itsFunction = theFunction;
        this.itsPrecision = thePrecision;
        this.itsSearchVector = new Vector();
    }

    public AbstractLineSearcher(AbstractLineSearcher theAbstractLineSearcher) {
        this(theAbstractLineSearcher.itsFunction, theAbstractLineSearcher.itsPrecision);
    }

    public AbstractUsableInMinimizationInEnergyFunction getFunction() {
        return this.itsFunction;
    }

    public Double getOptimumPoint() {
        return itsOptimumPoint;
    }

    public Double getOptimumFunctionValue() {
        return this.itsOptimumFunctionValue;
    }

    public Double getPrecision() {
        return itsPrecision;
    }

    public void setPrecision(Double thePrecision) {
        this.itsPrecision = thePrecision;
    }

    public abstract Vector getOptimumDirectionVector();

    private void __printValue() {
        System.out.println("(" + this.itsStartPoint + ", " + this.itsFunctionValueInStartPoint + ") (" + this.itsGoldenPoint + ", " 
                + this.itsFunctionValueInGoldenPoint + ") (" + this.itsEndPoint + ", " + this.itsFunctionValueInEndPoint + ")");
    }
    
    public void optimize(Vector theSearchVector) {
        this.itsSearchVector = theSearchVector;

        this._initializeRange();
        this.__initializePointAndFunctionValue();
        
        while (!this._isFinishInitialized()) {
            this.__initializePointAndFunctionValue();
        }
        
        while (Math.abs(this.itsStartPoint - this.itsEndPoint) > this.itsPrecision) {
            this.__findOptimumInFunctionValue();
        }
        
        this.__setOptimumPoint();
    }

    public void optimize(Vector theSearchVector, Vector theDirectionVector) {
        this.itsSearchVector = theSearchVector;
        this.itsDirectionVector = theDirectionVector;

        this._initializeRange();
        this.__initializePointAndFunctionValue();

        while (!this._isFinishInitialized()) {
            this.itsRange *= this.GOLDEN_VALUE;
            this.__initializePointAndFunctionValue();
        }

        while (this.itsFunctionValueInGoldenPoint < this.itsFunctionValueInEndPoint && Math.abs(this.itsStartPoint - this.itsEndPoint) > this.itsPrecision) {
            this.__findOptimumInFunctionValue();
        }

        this.__setOptimumPoint();
    }

    private void __setOptimumPoint() {
        if (this.itsFunctionValueInEndPoint >= this.itsFunctionValueInStartPoint && this.itsFunctionValueInGoldenPoint >= this.itsFunctionValueInStartPoint) {
            this.itsOptimumPoint = this.itsStartPoint;
            this.itsOptimumFunctionValue = this.itsFunctionValueInStartPoint;
        } else if (this.itsFunctionValueInEndPoint >= this.itsFunctionValueInGoldenPoint && this.itsFunctionValueInStartPoint >= this.itsFunctionValueInGoldenPoint) {
            this.itsOptimumPoint = this.itsGoldenPoint;
            this.itsOptimumFunctionValue = this.itsFunctionValueInGoldenPoint;
        } else {
            this.itsOptimumPoint = this.itsEndPoint;
            this.itsOptimumFunctionValue = this.itsFunctionValueInEndPoint;
        }
    }

    private void __findOptimumInFunctionValue() {
        double theCheckPoint = this.itsStartPoint + Math.pow(this.GOLDEN_VALUE, 2.0) * (this.itsEndPoint - this.itsStartPoint);
        double theFunctionValueInCheckPoint = this._calculateEnergyUsingPoint(theCheckPoint);

        
        if (this.itsFunctionValueInEndPoint < theFunctionValueInCheckPoint) {
            this.itsStartPoint = theCheckPoint;
            this.itsFunctionValueInStartPoint = theFunctionValueInCheckPoint;
            this.__initializeGoldenPointAndFunctionValue();
        } else if (theFunctionValueInCheckPoint < this.itsFunctionValueInEndPoint
                && this.itsFunctionValueInGoldenPoint < theFunctionValueInCheckPoint) {
            this.itsStartPoint = theCheckPoint;
            this.itsFunctionValueInStartPoint = theFunctionValueInCheckPoint;
            this.__changeStartAndEndPoint();
            this.__initializeGoldenPointAndFunctionValue();
        } else {
            this.itsEndPoint = this.itsGoldenPoint;
            this.itsFunctionValueInEndPoint = this.itsFunctionValueInGoldenPoint;
            this.itsGoldenPoint = theCheckPoint;
            this.itsFunctionValueInGoldenPoint = theFunctionValueInCheckPoint;
        }
    }

    protected boolean _isFinishInitialized() {
        if(Math.abs(this.itsRange) < this.itsPrecision) {
            return true;
        } 
        
        if (this.__isSmallestPointInEndPoint()) {
            this.itsRange *= this.GOLDEN_VALUE;
            return false;
        } else if (this._isNotCorrectValue(this.itsFunctionValueInStartPoint) || this._isNotCorrectValue(this.itsFunctionValueInEndPoint)) {
            this.itsRange *= this.GOLDEN_VALUE;
            return false;
        } else if(this.itsFunctionValueInEndPoint.equals(Double.MAX_VALUE)) {
            this.itsRange *= this.GOLDEN_VALUE;
            return false;
        }
        
        return true;
    }

    protected boolean _isNotCorrectValue(Double theValue) {
        if(this.itsInitialValue == null) {
            return false;
        }

        double theAbsoluteInitialValue = Math.abs(this.itsInitialValue);
        double theAbsoluteValue = Math.abs(theValue);

        return Math.log10(Math.max(theAbsoluteInitialValue, theAbsoluteValue) / Math.min(theAbsoluteInitialValue, theAbsoluteValue)) > 2;
    }

    private boolean __isSmallestPointInEndPoint() {
        return this.itsFunctionValueInStartPoint >= this.itsFunctionValueInEndPoint
                && this.itsFunctionValueInGoldenPoint >= this.itsFunctionValueInEndPoint;
    }

    private void __initializeGoldenPointAndFunctionValue() {
        this.itsGoldenPoint = this.itsStartPoint + this.GOLDEN_VALUE * (this.itsEndPoint - this.itsStartPoint);
        this.itsFunctionValueInGoldenPoint = this._calculateEnergyUsingPoint(this.itsGoldenPoint);
    }

    private void __initializePointAndFunctionValue() {
        this.itsStartPoint = this.INITIAL_START_POINT;
        this.itsEndPoint = this.itsRange;
        
        this.itsFunctionValueInStartPoint = this._calculateEnergyUsingPoint(this.itsStartPoint);
        this.itsFunctionValueInEndPoint = this._calculateEnergyUsingPoint(this.itsEndPoint);
        this.itsInitialValue = this.itsFunctionValueInStartPoint;

        if (this.itsFunctionValueInStartPoint < this.itsFunctionValueInEndPoint) {
            this.__changeStartAndEndPoint();
        }

        this.__initializeGoldenPointAndFunctionValue();
    }

    private void __changeStartAndEndPoint() {
        final double theTemp = this.itsStartPoint;

        this.itsStartPoint = this.itsEndPoint;
        this.itsEndPoint = theTemp;

        final double theTemp2 = this.itsFunctionValueInStartPoint;

        this.itsFunctionValueInStartPoint = this.itsFunctionValueInEndPoint;
        this.itsFunctionValueInEndPoint = theTemp2;
    }

    protected abstract double _calculateEnergyUsingPoint(double thePoint);
    
    protected void _initializeRange() {
        this.itsRange = 1.0;
    }
}
