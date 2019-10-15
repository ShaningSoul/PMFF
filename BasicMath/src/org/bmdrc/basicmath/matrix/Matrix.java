/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicmath.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicmath.matrix.interfaces.IMatrix;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.util.TwoDimensionList;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class Matrix extends TwoDimensionList<Double> implements IMatrix, Serializable{
    private static final long serialVersionUID = 3243845480399682587L;

    private List<String> itsColumnNameList;
    private List<String> itsRowNameList;
    //constant Double variable
    private final double INITIAL_VALUE = 0.0;
    
    public Matrix() {
        super();
        this.setColumnNameList(new ArrayList<String>());
        this.setRowNameList(new ArrayList<String>());
    }
    
    public Matrix(Double[][] the2dArray) {
        this(new TwoDimensionList<Double>(the2dArray));
    }
    
    public Matrix(TwoDimensionList<Double> the2dList) {
        super(the2dList);
        this.setColumnNameList(new ArrayList<String>());
        this.setRowNameList(new ArrayList<String>());
    }
    
    public Matrix(Matrix theMatrix) {
        this.set2dList(new ArrayList<>(theMatrix.get2dList()));
        this.setColumnNameList(new ArrayList<>(theMatrix.getColumnNameList()));
        this.setRowNameList(new ArrayList<>(theMatrix.getRowNameList()));
    }
    
    public Matrix(int theRowCount, int theColumnCount) {
        this();
        
        for(int ri = 0; ri < theRowCount; ri++) {
            List<Double> theNumberList = new ArrayList<>();
            
            for(int ci = 0; ci < theColumnCount; ci++) {
                theNumberList.add(this.INITIAL_VALUE);
            }
            
            this.add(theNumberList);
        }
    }
    
    public Matrix(double[][] the2dArray) {
        this();
        
        for(int ri = 0, rEnd = the2dArray.length; ri < rEnd; ri++) {
            List<Double> theNumberList = new ArrayList<>();
            
            for(int ci = 0, cEnd = the2dArray[ri].length; ci < cEnd; ci++) {
                theNumberList.add(the2dArray[ri][ci]);
            }
            
            this.add(theNumberList);
        }
    }
    
    @Override
    public List<String> getColumnNameList() {
        return itsColumnNameList;
    }

    @Override
    public void setColumnNameList(List<String> theColumnNameList) {
        this.itsColumnNameList = theColumnNameList;
    }

    @Override
    public List<String> setColumnNameList() {
        return itsColumnNameList;
    }
    
    @Override
    public List<String> getRowNameList() {
        return itsRowNameList;
    }

    @Override
    public void setRowNameList(List<String> theRowNameList) {
        this.itsRowNameList = theRowNameList;
    }

    @Override
    public List<String> setRowNameList() {
        return itsRowNameList;
    }
    
    @Override
    public void addColumn(List<Double> theList) {
        TwoDimensionList<Double> theMatrix = this.transposeMatrix();
        
        theMatrix.add(theList);
        this.set2dList(theMatrix.transposeMatrix().get2dList());
    }

    @Override
    public void addColumn(int theColumnIndex, List<Double> theList) {
        Matrix theNewMatrix = this.transposeMatrix();
        
        theNewMatrix.addRow(theColumnIndex, theList);
        this.set2dList(theNewMatrix.get2dList());
    }
    
    @Override
    public void addRow(List<Double> theList) {
        this.add(theList);
    }

    @Override
    public void addRow(int theRowIndex, List<Double> theList) {
        Matrix theNewMatrix = new Matrix();
        
        for(int ri = 0, rEnd = this.getRowCount(); ri < rEnd ; ri++) {
            if(ri == theRowIndex) {
                theNewMatrix.addRow(theList);
            }
            
            theNewMatrix.addRow(this.getRow(ri));
        }
        
        this.set2dList(new ArrayList<>(theNewMatrix.get2dList()));
    }
    
    @Override
    public void removeColumn(int theColumnIndex) {
        TwoDimensionList<Double> theMatrix = this.transposeMatrix();
        
        theMatrix.remove(theColumnIndex);
        this.set2dList(theMatrix.transposeMatrix().get2dList());
    }

    @Override
    public void removeRow(int theRowIndex) {
        this.remove(theRowIndex);
    }
    
    @Override
    public Double[][] to2dArray(Double[][] the2dArray) {
        for(int ai = 0, aEnd = this.size(); ai < aEnd ; ai++) {
            for(int vi = 0, vEnd = this.get(ai).size(); vi < vEnd ; vi++) {
                the2dArray[ai][vi] = this.get(ai, vi);
            }
        }
        
        return the2dArray;
    }
    
    @Override
    public Integer getColumnCount() {
        return this.getMaximumNumberOfColumn();
    }
    
    @Override
    public Integer getRowCount() {
        return this.size();
    }
    
    @Override
    public Vector getRow(int theRowIndex) {
        return new Vector(this.get(theRowIndex));
    }
    
    @Override
    public Vector getColumn(int theColumnIndex) {
        Vector theColumn = new Vector();
        
        for(int ri = 0, rEnd = this.getRowCount(); ri < rEnd ; ri++) {
            theColumn.add(this.get(ri, theColumnIndex));
        }
        
        return theColumn;
    }
    
    @Override
    public Matrix transposeMatrix() {
        return new Matrix(super.transposeMatrix());
    }
    
    public boolean containNan() {
        for(List<Double> theList: this) {
            for(Double theValue : theList) {
                if(theValue.isNaN()) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public double determinant() {
        Jama.Matrix theMatrix = new Jama.Matrix(this.__get2dDoubleArray());
        
        return theMatrix.det();
    }
    
    private double[][] __get2dDoubleArray() {
        double[][] theValues = new double[this.getRowCount()][this.getColumnCount()];
        
        for(int ri = 0, rEnd = this.getRowCount(); ri < rEnd; ri++) {
            for(int ci = 0, cEnd = this.getColumnCount(); ci < cEnd; ci++) {
                theValues[ri][ci] = this.get(ri, ci);
            }
        }
        
        return theValues;
    }
}
