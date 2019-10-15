/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.energyfunction.abstracts;

import org.bmdrc.basicchemistry.minimization.interfaces.UsableInMinimization;
import org.bmdrc.basicmath.linearfunction.abstracts.AbstractDefaultFunction;
import org.bmdrc.ui.vector.Vector;


/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 03. 23
 */
public abstract class AbstractUsableInMinimization extends AbstractDefaultFunction implements UsableInMinimization {

    private static final long serialVersionUID = 5757855526275291435L;

    protected Vector itsGradientVector;
    protected Vector itsConjugatedGradientVector;
    protected Boolean itsIsUsedMultiThread;
    protected Integer itsNumberOfThread;

    public AbstractUsableInMinimization(Boolean theIsUsedMultiThread, Integer theNumberOfThread) {
        super();
        this.itsIsUsedMultiThread = theIsUsedMultiThread;
        this.itsNumberOfThread = theNumberOfThread;
    }

    @Override
    public Vector getGradientVector() {
        return itsGradientVector;
    }

    @Override
    public Vector getConjugatedGradientVector() {
        return itsConjugatedGradientVector;
    }
}
