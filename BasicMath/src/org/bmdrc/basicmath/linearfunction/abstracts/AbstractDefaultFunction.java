/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicmath.linearfunction.abstracts;

import java.io.Serializable;
import org.bmdrc.basicmath.function.interfaces.IFunction;
import org.bmdrc.ui.vector.Vector;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public abstract class AbstractDefaultFunction implements IFunction, Serializable, Cloneable {
    private static final long serialVersionUID = -9152170949300297740L;

    protected Vector itsWeightList;

    public AbstractDefaultFunction() {
        this.itsWeightList = new Vector();
    }
    
    public AbstractDefaultFunction(AbstractDefaultFunction theFunction) {
        this.itsWeightList = new Vector(theFunction.itsWeightList);
    }
    
    @Override
    public Vector getWeightList() {
        return this.itsWeightList;
    }

    @Override
    public void setWeightList(Vector theWeightList) {
        this.itsWeightList = theWeightList;
    }

    @Override
    public void setWeight(int theIndex, Double theWeight) {
        this.itsWeightList.set(theIndex, theWeight);
    }
}
