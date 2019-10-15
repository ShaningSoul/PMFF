/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.energyfunction.abstracts;

import java.io.Serializable;
import java.util.List;
import org.bmdrc.basicmath.function.interfaces.IPartialDerivativeFunction;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public abstract class AbstractDefaultPartialDerivativeFunction implements IPartialDerivativeFunction, Serializable {
    private static final long serialVersionUID = 2576749560066383270L;

    private List<Double> itsPartialDerivativeWeightList;
    
    @Override
    public List<Double> getPartialDerivativeWeightList() {
        return this.itsPartialDerivativeWeightList;
    }

    @Override
    public void setPartialDerivativeWeightList(List<Double> theWeightList) {
        this.itsPartialDerivativeWeightList = theWeightList;
    }

    @Override
    public List<Double> setPartialDerivativeWeightList() {
        return this.itsPartialDerivativeWeightList;
    }

    @Override
    public abstract Double calculatePartialDerivativeValue(Double theValue);
}
