/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.energyfunction.thread.abstracts;

import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 04. 19
 */
public abstract class AbstractEnergyGradientCalculationThread extends AbstractEnergyFunctionThread {

    public AbstractEnergyGradientCalculationThread(IAtomContainer theMolecule, AbstractCalculableList theCalculableList, int theThreadNumber, int theTotalThreadNumber) {
        super(theMolecule, theCalculableList, theThreadNumber, theTotalThreadNumber);
    }

    @Override
    public void run() {
        if (this.itsCalculableList.size() >= this.itsTotalThreadNumber) {
            for (int vi = (int) Math.round((double) this.itsCalculableList.size() / (double) this.itsTotalThreadNumber * (double) this.itsThreadNumber),
                    vEnd = (int) Math.round((double) this.itsCalculableList.size() / (double) this.itsTotalThreadNumber * (1.0 + (double) this.itsThreadNumber)); vi < vEnd; vi++) {
                this._calculateGradient(this.itsCalculableList.get(vi));
            }
        } else if (this.itsCalculableList.size() > this.itsThreadNumber) {
            this._calculateGradient(this.itsCalculableList.get(this.itsThreadNumber));
        }
    }
    
    protected abstract void _calculateGradient(AbstractCalculableSet theCalculableSet);

}
