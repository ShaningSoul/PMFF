/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.vector;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class Vector3dCalculator implements Serializable {

    private static final long serialVersionUID = -2119949642541185159L;

    /**
     * calculate Radian Agnel
     * 
     * @param theFirstVector First Vector
     * @param theSecondVector Second Vector
     * @return Radian Angle
     */
    public static Double calculateAngle(Vector3d theFirstVector, Vector3d theSecondVector) {
        Double theCosValue = Vector3dCalculator.calculateDotProduct(theFirstVector, theSecondVector)
                / (Vector3dCalculator.calculateVectorSize(theFirstVector) * Vector3dCalculator.calculateVectorSize(theSecondVector));

        if (theCosValue < -1.0) {
            return Math.PI;
        } else if (theCosValue > 1.0) {
            return 0.0;
        }

        return Math.acos(Vector3dCalculator.calculateDotProduct(theFirstVector, theSecondVector)
                / (theFirstVector.length() * theSecondVector.length()));
    }

    public static Double calculateDotProduct(Vector3d theFirstVector, Vector3d theSecondVector) {
        return theFirstVector.getX() * theSecondVector.getX() + theFirstVector.getY() * theSecondVector.getY() + theFirstVector.getZ() * theSecondVector.getZ();
    }

    public static Double calculateVectorSize(Vector3d theVector) {
        return Math.sqrt(Vector3dCalculator.calculateDotProduct(theVector, theVector));
    }

    public static Vector3d sum(Vector3d theFirstVector, Vector3d theSecondVector) {
        Vector3d theNewVector = new Vector3d();

        theNewVector.setX(theFirstVector.getX() + theSecondVector.getX());
        theNewVector.setY(theFirstVector.getY() + theSecondVector.getY());
        theNewVector.setZ(theFirstVector.getZ() + theSecondVector.getZ());

        return theNewVector;
    }

    public static Vector3d minus(Vector3d theFirstVector, Vector3d theSecondVector) {
        Vector3d theNewVector = new Vector3d();

        theNewVector.setX(theFirstVector.getX() - theSecondVector.getX());
        theNewVector.setY(theFirstVector.getY() - theSecondVector.getY());
        theNewVector.setZ(theFirstVector.getZ() - theSecondVector.getZ());

        return theNewVector;
    }

    public static Vector3d productByScalar(double theScalar, Vector3d theVector) {
        Vector3d theNewVector = new Vector3d();

        theNewVector.setX(theScalar * theVector.getX());
        theNewVector.setY(theScalar * theVector.getY());
        theNewVector.setZ(theScalar * theVector.getZ());

        return theNewVector;
    }

    public static Vector3d divideByScalar(double theScalar, Vector3d theVector) {
        Vector3d theNewVector = new Vector3d();

        theNewVector.setX(theVector.getX() / theScalar);
        theNewVector.setY(theVector.getY() / theScalar);
        theNewVector.setZ(theVector.getZ() / theScalar);

        return theNewVector;
    }

    public static Vector3d crossProduct(Vector3d theFirstVector, Vector3d theSecondVector) {
        double theX = theFirstVector.getY() * theSecondVector.getZ() - theFirstVector.getZ() * theSecondVector.getY();
        double theY = theFirstVector.getZ() * theSecondVector.getX() - theFirstVector.getX() * theSecondVector.getZ();
        double theZ = theFirstVector.getX() * theSecondVector.getY() - theFirstVector.getY() * theSecondVector.getX();

        return new Vector3d(theX, theY, theZ);
    }

    public static Vector3d dividePerElement(Vector3d theFirstVector, Vector3d theSecondVector) {
        Vector3d theResultVector = new Vector3d();

        theResultVector.setX(theFirstVector.getX() / theSecondVector.getX());
        theResultVector.setY(theFirstVector.getY() / theSecondVector.getY());
        theResultVector.setZ(theFirstVector.getZ() / theSecondVector.getZ());

        return theResultVector;
    }
    
    public static Vector3d productPerElement(Vector3d theFirstVector, Vector3d theSecondVector) {
        Vector3d theResultVector = new Vector3d();

        theResultVector.setX(theFirstVector.getX() * theSecondVector.getX());
        theResultVector.setY(theFirstVector.getY() * theSecondVector.getY());
        theResultVector.setZ(theFirstVector.getZ() * theSecondVector.getZ());

        return theResultVector;
    }
}
