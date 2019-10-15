/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.ui.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class ThreeDimensionList<Type extends Object> extends ArrayList<TwoDimensionList<Type>> implements Serializable {

    private static final long serialVersionUID = -6610056157589657511L;

    public ThreeDimensionList() {
        super();
    }

    public ThreeDimensionList(ThreeDimensionList theList) {
        super(theList);
    }

    public List<Type> get(int theFirstIndex, int theSecondIndex) {
        return this.get(theFirstIndex).get(theSecondIndex);
    }

    public Type get(int theFirstIndex, int theSecondIndex, int theThirdIndex) {
        return this.get(theFirstIndex, theSecondIndex).get(theThirdIndex);
    }

    public void set(int theFirstIndex, int theSecondIndex, List<Type> theTypeList) {
        this.get(theFirstIndex).set(theSecondIndex, theTypeList);
    }

    public void set(int theFirstIndex, int theSecondIndex, int theThirdIndex, Type theType) {
        this.get(theFirstIndex, theSecondIndex).set(theThirdIndex, theType);
    }
}
