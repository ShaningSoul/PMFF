/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.sbff.parameter;

import java.io.Serializable;
import org.bmdrc.sbff.parameter.intraenergy.*;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2015. 10. 07
 */
public class SbffIntraParameterSet implements Serializable {

    private static final long serialVersionUID = -1512330498156973748L;
    
    //Intra parameter set
    private SbffStretchParameterSet itsStretchParameterSet;
    private SbffBendingParameterSet itsBendingParameterSet;
    private SbffTorsionParameterSet itsTorsionParameterSet;
    private SbffStretchBendParameterSet itsStretchBendParameterSet;
    private SbffStretchTorsionParameterSet itsStretchTorsionParameterSet;
    private SbffOutOfPlaneBendingParameterSet itsOutOfPlaneBendingParameterSet;
    private SbffIntraNonbondingParameterSet itsIntraNonbondingParameterSet;
    
    public SbffIntraParameterSet() {
        this.itsStretchParameterSet = new SbffStretchParameterSet();
        this.itsBendingParameterSet = new SbffBendingParameterSet();
        this.itsTorsionParameterSet = new SbffTorsionParameterSet();
        this.itsStretchBendParameterSet = new SbffStretchBendParameterSet();
        this.itsStretchTorsionParameterSet = new SbffStretchTorsionParameterSet();
        this.itsOutOfPlaneBendingParameterSet = new SbffOutOfPlaneBendingParameterSet();
        this.itsIntraNonbondingParameterSet = new SbffIntraNonbondingParameterSet();
    }

    public SbffStretchParameterSet getStretchParameterSet() {
        return itsStretchParameterSet;
    }

    public SbffBendingParameterSet getBendingParameterSet() {
        return itsBendingParameterSet;
    }

    public SbffTorsionParameterSet getTorsionParameterSet() {
        return itsTorsionParameterSet;
    }

    public SbffStretchBendParameterSet getStretchBendParameterSet() {
        return itsStretchBendParameterSet;
    }

    public SbffStretchTorsionParameterSet getStretchTorsionParameterSet() {
        return itsStretchTorsionParameterSet;
    }

    public SbffOutOfPlaneBendingParameterSet getOutOfPlaneBendingParameterSet() {
        return itsOutOfPlaneBendingParameterSet;
    }

    public SbffIntraNonbondingParameterSet getIntraNonbondingParameterSet() {
        return itsIntraNonbondingParameterSet;
    }
}
