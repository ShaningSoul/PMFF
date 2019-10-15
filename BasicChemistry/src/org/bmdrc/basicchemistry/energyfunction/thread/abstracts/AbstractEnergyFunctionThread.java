/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.energyfunction.thread.abstracts;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractThread;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 02. 03
 */
public abstract class AbstractEnergyFunctionThread extends AbstractThread implements Serializable {

    private static final long serialVersionUID = -1055581361763036202L;

    protected IAtomContainer itsMolecule;
    protected AbstractCalculableList<AbstractCalculableSet> itsCalculableList;

    public AbstractEnergyFunctionThread(IAtomContainer theMolecule, AbstractCalculableList theCalculableList, int theThreadNumber, int theTotalThreadNumber) {
        super(theThreadNumber, theTotalThreadNumber);
        this.itsMolecule = theMolecule;
        this.itsCalculableList = theCalculableList;
    }
}
