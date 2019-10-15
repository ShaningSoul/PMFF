/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.sbff.parameter;

import java.io.Serializable;
import org.bmdrc.sbff.parameter.interenergy.NonBondParameterSet;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondTotalSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2015. 10. 07
 */
public class SbffInterParameterSet implements Serializable {

    private static final long serialVersionUID = -6271413012652280904L;

    //Inter parameter set
    private NonBondParameterSet itsNonbondParameterSet;
    private SbffHydrogenBondTotalSet itsHydrogenBondParameterSet;

    public SbffInterParameterSet() {
        this.itsNonbondParameterSet = new NonBondParameterSet();
        this.itsHydrogenBondParameterSet = new SbffHydrogenBondTotalSet();
    }

    public NonBondParameterSet getNonbondParameterSet() {
        return itsNonbondParameterSet;
    }

    public SbffHydrogenBondTotalSet getHydrogenBondParameterSet() {
        return itsHydrogenBondParameterSet;
    }
}
