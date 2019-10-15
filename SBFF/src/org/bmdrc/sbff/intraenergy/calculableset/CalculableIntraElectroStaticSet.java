/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.intraenergy.calculableset;

import java.io.Serializable;
import org.bmdrc.sbff.interenergy.calculableset.CalculableElectroStaticSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableIntraElectroStaticSet extends CalculableElectroStaticSet implements Serializable {
    private static final long serialVersionUID = 8619240119181943783L;

    public CalculableIntraElectroStaticSet(Integer theFirstAtomIndex, Integer theSecondAtomIndex) {
        super(theFirstAtomIndex, theSecondAtomIndex);
    }
    
    public CalculableIntraElectroStaticSet(CalculableElectroStaticSet theCalculableSet) {
        super(theCalculableSet.getFirstAtomIndex(), theCalculableSet.getSecondAtomIndex());
    }
}
