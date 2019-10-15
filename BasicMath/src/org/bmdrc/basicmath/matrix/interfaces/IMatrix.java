/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicmath.matrix.interfaces;

import java.util.List;
import org.bmdrc.basicmath.matrix.Matrix;
import org.bmdrc.ui.util.interfaces.ITwoDimensionList;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public interface IMatrix extends ITwoDimensionList<Double> {
    
    public List<String> getColumnNameList();

    public void setColumnNameList(List<String> theColumnNameList);

    public List<String> setColumnNameList();
    
    public List<String> getRowNameList();

    public void setRowNameList(List<String> theRowNameList);

    public List<String> setRowNameList();
    
    public void addColumn(List<Double> theList);

    public void addColumn(int theColumnIndex, List<Double> theList);
    
    public void addRow(List<Double> theList);

    public void addRow(int theRowIndex, List<Double> theList);
    
    public void removeColumn(int theColumnIndex);

    public void removeRow(int theRowIndex);
    
    public Double[][] to2dArray(Double[][] the2dArray);
    
    public Integer getColumnCount();
    
    public Integer getRowCount();
    
    public List<Double> getRow(int theRowIndex);
    
    public List<Double> getColumn(int theColumnIndex);
    
    public Matrix transposeMatrix();
}
