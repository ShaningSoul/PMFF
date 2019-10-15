/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.parameter.cdeap;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CdeapParameter implements Serializable {

    private static final long serialVersionUID = 3381864844498349916L;

    private Double itsInitialPolarizability;
    private Double itsCoefficient;

    public CdeapParameter(Double theInitialPolarizability, Double theCoefficient) {
        this.itsInitialPolarizability = theInitialPolarizability;
        this.itsCoefficient = theCoefficient;
    }

    public Double getInitialPolarizability() {
        return itsInitialPolarizability;
    }

    public Double getCoefficient() {
        return itsCoefficient;
    }
}
