/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.energyfunction.thread.abstracts;

import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractThread;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 02. 05
 */
public abstract class AbstractCalculableListInitializerThread<Type extends AbstractCalculableSet> extends AbstractThread {

    protected List<Type> itsCalculableList;
    protected IAtomContainer itsMolecule;
    
    public AbstractCalculableListInitializerThread(IAtomContainer theMolecule, Integer theThreadNumber, Integer theTotalThreadNumber) {
        super(theThreadNumber, theTotalThreadNumber);
        this.itsMolecule = theMolecule;
        this.itsCalculableList = new ArrayList<>();
    }

    public List<Type> getCalculableList() {
        return itsCalculableList;
    }
}
