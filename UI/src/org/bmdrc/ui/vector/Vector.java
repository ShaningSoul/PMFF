/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.ui.vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class Vector extends ArrayList<Double> implements Serializable {

    private static final long serialVersionUID = 1461580760637627043L;

    //constant Integer variable
    protected final int X_INDEX = 0;
    protected final int Y_INDEX = 1;
    protected final int Z_INDEX = 2;
    private static final int DIMENSION_SIZE = 3;
    //constant Double variable
    private final Double INITIAL_VALUE = 0.0;

    public Vector() {
        super();
    }

    public Vector(int theSize) {
        super();

        for (int vi = 0, vEnd = theSize; vi < vEnd; vi++) {
            this.add((Double) this.INITIAL_VALUE);
        }
    }

    public Vector(List<Double> theVector) {
        super(theVector);
    }

    public Vector(Vector theVector) {
        super(theVector);
    }

    public Double length() {
        Double theSquaredSum = 0.0;

        for (Double theValue : this) {
            theSquaredSum += theValue.doubleValue() * theValue.doubleValue();
        }

        return Math.sqrt(theSquaredSum);
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        theStringBuilder.append("[");
        for (int vi = 0, vEnd = this.size(); vi < vEnd; vi++) {
            theStringBuilder.append(String.format("%.2e", this.get(vi)));

            if (vi < vEnd - 1) {
                theStringBuilder.append(", ");
            }
        }

        theStringBuilder.append("]");

        return theStringBuilder.toString();
    }

    public Vector inverse() {
        for (int ii = 0, iEnd = this.size(); ii < iEnd; ii++) {
            this.set(ii, this.get(ii).doubleValue() * -1.0);
        }

        return this;
    }

    public Vector subVector(int theStartIndex, int theEndIndex) {
        Vector theSubVector = new Vector();

        for (int vi = theStartIndex; vi < theEndIndex; vi++) {
            theSubVector.add(this.get(vi));
        }

        return theSubVector;
    }

    public Double calculateSimilarity(Vector theVector) {
        if (theVector.size() != this.size()) {
            return null;
        }

        Double theValue = VectorCalculator.DotProduct(this, theVector);

        return theValue / (this.length() * theVector.length());
    }

    public Double distance(Vector theVector) {
        if (theVector.size() != this.size()) {
            return null;
        }

        Double theValue = 0.0;

        for (int ii = 0, iEnd = this.size(); ii < iEnd; ii++) {
            if (this.get(ii) != null && theVector.get(ii) != null) {
                theValue += Math.pow(this.get(ii) - theVector.get(ii), 2.0);
            } else {
                return null;
            }
        }

        return Math.sqrt(theValue);
    }
}
