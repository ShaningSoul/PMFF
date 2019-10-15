/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicmath.linearfunction.abstracts;

import java.io.Serializable;
import org.bmdrc.basicmath.function.interfaces.ILinearFunction;
import org.bmdrc.ui.vector.Vector;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 03. 23
 */
public abstract class AbstractLinearFunction extends AbstractDefaultFunction implements ILinearFunction, Serializable {
    private static final long serialVersionUID = 1978028731300553164L;

    @Override
    public Vector getCoefficientList() {
        return this.itsWeightList;
    }

    @Override
    public void setCoefficientList(Vector theCoefficientList) {
        this.itsWeightList = theCoefficientList;
    }
}
