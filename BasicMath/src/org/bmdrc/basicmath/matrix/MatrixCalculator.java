/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicmath.matrix;

import java.io.Serializable;
import org.bmdrc.ui.vector.VectorCalculator;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class MatrixCalculator implements Serializable {

    private static final long serialVersionUID = 4956606215752422702L;

    public static Matrix cross(Matrix theFirstMatrix, Matrix theSecondMatrix) {
        Matrix theMatrix = new Matrix(theFirstMatrix.getRowCount(), theSecondMatrix.getColumnCount());
        
        for (int ri = 0, rEnd = theFirstMatrix.getRowCount(); ri < rEnd; ri++) {
            for (int ci = 0, cEnd = theSecondMatrix.getColumnCount(); ci < cEnd; ci++) {
                Double theValue = VectorCalculator.DotProduct(theFirstMatrix.getRow(ri), theSecondMatrix.getColumn(ci));

                theMatrix.setValue(ri, ci, theValue);
            }
        }

        return theMatrix;
    }

    public static Matrix sum(Matrix theFirstMatrix, Matrix theSecondMatrix) {
        Matrix theMatrix = new Matrix(theFirstMatrix.getRowCount(), theFirstMatrix.getColumnCount());

        if (!theFirstMatrix.getRowCount().equals(theSecondMatrix.getRowCount()) || !theFirstMatrix.getColumnCount().equals(theSecondMatrix.getColumnCount())) {
            System.err.println("Matrix size is not equal in MatrixCalculator-sum()");
            return null;
        }

        int theRowCount = theFirstMatrix.getRowCount();
        int theColumnCount = theFirstMatrix.getColumnCount();

        for (int ri = 0; ri < theRowCount; ri++) {
            for (int ci = 0; ci < theColumnCount; ci++) {
                theMatrix.setValue(ri, ci, theFirstMatrix.get(ri, ci).doubleValue() + theSecondMatrix.get(ri, ci).doubleValue());
            }
        }

        return theMatrix;
    }

    public static Matrix minus(Matrix theFirstMatrix, Matrix theSecondMatrix) {
        Matrix theMatrix = new Matrix(theFirstMatrix.getRowCount(), theFirstMatrix.getColumnCount());

        if (!theFirstMatrix.getRowCount().equals(theSecondMatrix.getRowCount()) || !theFirstMatrix.getColumnCount().equals(theSecondMatrix.getColumnCount())) {
            System.err.println("Matrix size is not equal in MatrixCalculator-sum()");
            return null;
        }

        int theRowCount = theFirstMatrix.getRowCount();
        int theColumnCount = theFirstMatrix.getColumnCount();

        for (int ri = 0; ri < theRowCount; ri++) {
            for (int ci = 0; ci < theColumnCount; ci++) {
                theMatrix.setValue(ri, ci, theFirstMatrix.get(ri, ci).doubleValue() - theSecondMatrix.get(ri, ci).doubleValue());
            }
        }

        return theMatrix;
    }
}
