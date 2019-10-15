/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.ui.vector;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class VectorCalculator implements Serializable {

    private static final long serialVersionUID = -3428799464367945563L;

    public static Vector sum(Vector theFirstVector, Vector theSecondVector) {
        if (theFirstVector.size() != theSecondVector.size()) {
            System.err.println("Two vectors are not same size!!");
            return null;
        }

        Vector theResultVector = new Vector();

        for (int vi = 0, vEnd = theFirstVector.size(); vi < vEnd; vi++) {
            theResultVector.add(theFirstVector.get(vi) + theSecondVector.get(vi));
        }

        return theResultVector;
    }

    public static Vector minus(Vector theFirstVector, Vector theSecondVector) {
        if (theFirstVector.size() != theSecondVector.size()) {
            System.err.println("Two vectors are not same size!!");
            return null;
        }

        Vector theResultVector = new Vector();

        for (int vi = 0, vEnd = theFirstVector.size(); vi < vEnd; vi++) {
            theResultVector.add(theFirstVector.get(vi).doubleValue() - theSecondVector.get(vi).doubleValue());
        }

        return theResultVector;
    }

    public static Double DotProduct(Vector theFirstVector, Vector theSecondVector) {
        if (theFirstVector.size() != theSecondVector.size()) {
            System.err.println("Vector size is not same!! [" + theFirstVector + "] [" + theSecondVector + "]");
            return null;
        }

        Double theResult = 0.0;

        for (int vi = 0, vEnd = theFirstVector.size(); vi < vEnd; vi++) {
            theResult += theFirstVector.get(vi).doubleValue() * theSecondVector.get(vi).doubleValue();

        }

        return theResult;
    }

    public static Vector productScalarValue(double theScalar, Vector theVector) {
        Vector theResultVector = new Vector();

        for (Double theValue : theVector) {
            theResultVector.add(theValue * theScalar);

        }

        return theResultVector;
    }

    public static Vector inverse(Vector theVector) {
        Vector theResultVector = new Vector();

        for (Number theValue : theVector) {
            theResultVector.add(theValue.doubleValue() * -1.0);
        }

        return theResultVector;
    }

    public static Vector productPerElement(Vector theFirstVector, Vector theSecondVector) {
        if (theFirstVector.size() == theSecondVector.size()) {
            Vector theResultVector = new Vector();

            for (int vi = 0, vEnd = theFirstVector.size(); vi < vEnd; vi++) {
                    theResultVector.add(theFirstVector.get(vi).doubleValue() * theSecondVector.get(vi).doubleValue());
            }

            return theResultVector;
        }

        return null;
    }

    public static Vector dividePerElement(Vector theFirstVector, Vector theSecondVector) {
        if (theFirstVector.size() == theSecondVector.size()) {
            Vector theResultVector = new Vector();

            for (int vi = 0, vEnd = theFirstVector.size(); vi < vEnd; vi++) {
                    theResultVector.add(theFirstVector.get(vi).doubleValue() / theSecondVector.get(vi).doubleValue());
            }

            return theResultVector;
        }

        return null;
    }
}
