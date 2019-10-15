/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.ui.util.interfaces;

import java.util.List;
import org.bmdrc.ui.util.TwoDimensionList;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public interface ITwoDimensionList<Type extends Object> extends List<List<Type>> {

    public List<List<Type>> get2dList();

    public void set2dList(List<List<Type>> the2dList);

    public List<List<Type>> set2dList();

    public Type get(int theFirstIndex, int theSecondIndex);

    public void remove(int theFirstIndex, int theSecondIndex);

    public boolean contains(List<Type> theList);

    public boolean contains(int theFirstIndex, Type theValue);

    public void addAll(TwoDimensionList<Type> the2dList);

    public void addAll(List<List<Type>> the2dList);

    public void setValue(int theRowIndex, int theColumnIndex, Type theValue);

    public int getMaximumNumberOfColumn();

    public TwoDimensionList<Type> transposeMatrix();
}
