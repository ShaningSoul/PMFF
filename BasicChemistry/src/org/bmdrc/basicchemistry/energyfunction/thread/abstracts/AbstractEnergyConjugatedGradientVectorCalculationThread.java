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
import org.bmdrc.basicchemistry.math.FirstDerivativeOfInternalCoordinate;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 02. 03
 */
public abstract class AbstractEnergyConjugatedGradientVectorCalculationThread extends AbstractEnergyFunctionThread implements Serializable {

    private static final long serialVersionUID = -3501340895364231618L;

    protected Vector itsConjugatedGradientVector;
    protected FirstDerivativeOfInternalCoordinate itsFirstDerivativeFunction;
    protected MoveTogetherAtomNumberList itsMoveTogetherAtomNumberMap;
    protected Double itsScalingFactor;
    //constant Integer variable
    protected final int FIRST_INDEX = 0;
    protected final int SECOND_INDEX = 1;
    protected final int THIRD_INDEX = 2;
    protected final int DIMENSION_SIZE = 3;
    //constant Double variable
    protected static final double INITIAL_SCALING_FACTOR = 1.0;

    public AbstractEnergyConjugatedGradientVectorCalculationThread(IAtomContainer theMolecule, AbstractCalculableList theCalculableList, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap,
            int theThreadNumber, int theTotalThreadNumber) {
        this(theMolecule, theCalculableList, theMoveTogetherAtomNumberMap, theThreadNumber, theTotalThreadNumber, AbstractEnergyConjugatedGradientVectorCalculationThread.INITIAL_SCALING_FACTOR);
    }

    public AbstractEnergyConjugatedGradientVectorCalculationThread(IAtomContainer theMolecule, AbstractCalculableList theCalculableList, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap,
            int theThreadNumber, int theTotalThreadNumber, double theScalingFactor) {
        super(theMolecule, theCalculableList, theThreadNumber, theTotalThreadNumber);
        this.itsFirstDerivativeFunction = new FirstDerivativeOfInternalCoordinate(theMolecule);
        this.itsMoveTogetherAtomNumberMap = theMoveTogetherAtomNumberMap;
        this.itsScalingFactor = theScalingFactor;
    }

    public Vector getConjugatedGradientVector() {
        return itsConjugatedGradientVector;
    }

    @Override
    public void run() {
        this.itsConjugatedGradientVector = new Vector(this.itsMolecule.getAtomCount() * this.DIMENSION_SIZE);

        if (this.itsCalculableList.size() >= this.itsTotalThreadNumber) {
            for (int vi = (int) Math.round((double) this.itsCalculableList.size() / (double) this.itsTotalThreadNumber * (double) this.itsThreadNumber),
                    vEnd = (int) Math.round((double) this.itsCalculableList.size() / (double) this.itsTotalThreadNumber * (1.0 + (double) this.itsThreadNumber)); vi < vEnd; vi++) {
                this._reloadConjugatedGradientVector(this.itsCalculableList.get(vi));
            }
        } else if (this.itsCalculableList.size() > this.itsThreadNumber) {
            this._reloadConjugatedGradientVector(this.itsCalculableList.get(this.itsThreadNumber));
        }
    }

    protected void _setConjugatedGradient(int theAtomNumber, Vector3d theTotalGradientVector) {
        for (int di = 0; di < this.DIMENSION_SIZE; di++) {
            this.itsConjugatedGradientVector.set(theAtomNumber * this.DIMENSION_SIZE + di, this.itsConjugatedGradientVector.get(theAtomNumber * this.DIMENSION_SIZE + di)
                    + theTotalGradientVector.get(di));
        }
    }

    protected abstract void _reloadConjugatedGradientVector(AbstractCalculableSet theCalculableSet);
}
