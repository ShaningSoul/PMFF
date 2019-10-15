/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicmath.function.interfaces;

import java.util.List;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public interface IPartialDerivativeFunction {

    public List<Double> getPartialDerivativeWeightList();
    
    public void setPartialDerivativeWeightList(List<Double> theWeightList);
    
    public List<Double> setPartialDerivativeWeightList();

    public Double calculatePartialDerivativeValue(Double theValue);
}
