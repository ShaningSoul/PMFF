/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.vector;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class AngleGradientVector implements Serializable {
    private static final long serialVersionUID = -7172607461357954508L;

    protected Vector3d itsAccerlationUnitVector;
    protected Vector3d itsCentripetalForceUnitVector;
    protected Double itsRadius;
    protected Double itsAngleDerivative;
    //constant double variable
    protected final double CONVERTING_FACTOR_FROM_DEGREE_TO_RADIAN = Math.PI / 180.0;
    private final double DEFAULT_ANGLE_DERIVATIVE = 1.0;

    public AngleGradientVector() {
    }

    public AngleGradientVector(Vector3d theAccerlationUnitVector, Vector3d theCentripetalForceUnitVector) {
        this.itsAccerlationUnitVector = theAccerlationUnitVector;
        this.itsCentripetalForceUnitVector = theCentripetalForceUnitVector;
    }

    public AngleGradientVector(Vector3d theAccerlationUnitVector, Vector3d theCentripetalForceUnitVector, double theRadius) {
        this.itsAccerlationUnitVector = theAccerlationUnitVector;
        this.itsCentripetalForceUnitVector = theCentripetalForceUnitVector;
        this.itsRadius = theRadius;
        this.itsAngleDerivative = this.DEFAULT_ANGLE_DERIVATIVE;
    }
    
    public AngleGradientVector(Vector3d theAccerlationUnitVector, Vector3d theCentripetalForceUnitVector, double theRadius, double theAngleDerivative) {
        super();
        this.itsAccerlationUnitVector = theAccerlationUnitVector;
        this.itsCentripetalForceUnitVector = theCentripetalForceUnitVector;
        this.itsRadius = theRadius;
        this.itsAngleDerivative = theAngleDerivative;
    }

    
    public Vector3d getAccerlationVector() {
        return itsAccerlationUnitVector;
    }

    public void setAccerlationVector(Vector3d theAccerlationUnitVector) {
        this.itsAccerlationUnitVector = theAccerlationUnitVector;
    }

    public Vector3d getCentripetalForceVector() {
        return itsCentripetalForceUnitVector;
    }

    public void setCentripetalForceVector(Vector3d theCentripetalForceUnitVector) {
        this.itsCentripetalForceUnitVector = theCentripetalForceUnitVector;
    }

    public Double getRadius() {
        return itsRadius;
    }

    public void setRadius(Double theRadius) {
        this.itsRadius = theRadius;
    }

    public Vector3d calculateAngleGredientVectorUsingAngleDifference(Double theAngleDifference) {
        double theAngleDifferenceToRadian = theAngleDifference * this.CONVERTING_FACTOR_FROM_DEGREE_TO_RADIAN;
        
        return Vector3dCalculator.sum(Vector3dCalculator.productByScalar(this.itsRadius * Math.sin(theAngleDifferenceToRadian), this.itsAccerlationUnitVector), 
                Vector3dCalculator.productByScalar(this.itsRadius * (1.0 - Math.cos(theAngleDifferenceToRadian)), this.itsCentripetalForceUnitVector));
    }
    
    public Vector3d calculateAngleGredientVectorUsingWeight(double theWeight) {
        double theAngleDifferenceToRadian = (this.itsAngleDerivative * theWeight) * this.CONVERTING_FACTOR_FROM_DEGREE_TO_RADIAN;
        
        return Vector3dCalculator.sum(Vector3dCalculator.productByScalar(this.itsRadius * Math.sin(theAngleDifferenceToRadian), this.itsAccerlationUnitVector), 
                Vector3dCalculator.productByScalar(this.itsRadius * (1.0 - Math.cos(theAngleDifferenceToRadian)), this.itsCentripetalForceUnitVector));
    }
    
    public Vector3d calculateAngleGredientVector() {
        return this.calculateAngleGredientVectorUsingWeight(1.0);
    }
}
