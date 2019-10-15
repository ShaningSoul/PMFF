/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.energyfunction.abstracts;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 02. 03
 */
public abstract class AbstractThread extends Thread implements Serializable {

    private static final long serialVersionUID = 480517014363374259L;

    protected Integer itsThreadNumber;
    protected Integer itsTotalThreadNumber;

    public AbstractThread(Integer theThreadNumber, Integer theTotalThreadNumber) {
        this.itsThreadNumber = theThreadNumber;
        this.itsTotalThreadNumber = theTotalThreadNumber;
    }
    
    @Override
    public abstract void run();
}
