/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.sbff.parameter;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2015. 10. 07
 */
public class SbffTotalParameterSet implements Serializable {

    private static final long serialVersionUID = 2374488219985250289L;

    private SbffIntraParameterSet itsSbffIntraParameterSet;
    private SbffInterParameterSet itsSbffInterParameterSet;

    public SbffTotalParameterSet() {
        this.itsSbffIntraParameterSet = new SbffIntraParameterSet();
        this.itsSbffInterParameterSet = new SbffInterParameterSet();
    }

    public SbffIntraParameterSet getSbffIntraParameterSet() {
        return itsSbffIntraParameterSet;
    }

    public SbffInterParameterSet getSbffInterParameterSet() {
        return itsSbffInterParameterSet;
    }
    
}
