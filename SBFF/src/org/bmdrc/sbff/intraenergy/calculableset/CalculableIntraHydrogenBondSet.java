/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.sbff.intraenergy.calculableset;

import java.io.Serializable;
import org.bmdrc.sbff.interenergy.calculableset.CalculableHydrogenBondSet;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondParameterSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2015. 10. 13
 */
public class CalculableIntraHydrogenBondSet extends CalculableHydrogenBondSet implements Serializable {

    public CalculableIntraHydrogenBondSet(CalculableHydrogenBondSet theCalculableHydrogenBondSet) {
        super(theCalculableHydrogenBondSet);
    }

    public CalculableIntraHydrogenBondSet(Integer theXAtomIndex, Integer theHAtomIndex, Integer theAAtomIndex, Integer theBAtomIndex, SbffHydrogenBondParameterSet theHydrogenBondParameter) {
        super(theXAtomIndex, theHAtomIndex, theAAtomIndex, theBAtomIndex, theHydrogenBondParameter);
    }
}
