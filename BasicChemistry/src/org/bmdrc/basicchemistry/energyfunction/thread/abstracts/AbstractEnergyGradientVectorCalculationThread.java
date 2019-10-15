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
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 02. 03
 */
public abstract class AbstractEnergyGradientVectorCalculationThread extends AbstractEnergyFunctionThread implements Serializable {

    private static final long serialVersionUID = -367156387255231938L;

    protected Vector itsGradientVector;
    protected FirstDerivativeOfInternalCoordinate itsFirstDerivativeFunction;
    protected MoveTogetherAtomNumberList itsMoveTogetherAtomNumberMap;
    protected Double itsScalingFactor;
    //constant Double variable
    protected static final double INITIAL_SCALING_FACTOR = 1.0;

    public AbstractEnergyGradientVectorCalculationThread(IAtomContainer theMolecule, AbstractCalculableList theCalculableList, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap,
            int theThreadNumber, int theTotalThreadNumber) {
        this(theMolecule, theCalculableList, theMoveTogetherAtomNumberMap, theThreadNumber, theTotalThreadNumber, AbstractEnergyGradientVectorCalculationThread.INITIAL_SCALING_FACTOR);
    }

    public AbstractEnergyGradientVectorCalculationThread(IAtomContainer theMolecule, AbstractCalculableList theCalculableList, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap,
            int theThreadNumber, int theTotalThreadNumber, double theScalingFactor) {
        super(theMolecule, theCalculableList, theThreadNumber, theTotalThreadNumber);
        this.itsFirstDerivativeFunction = new FirstDerivativeOfInternalCoordinate(theMolecule);
        this.itsMoveTogetherAtomNumberMap = theMoveTogetherAtomNumberMap;
        this.itsScalingFactor = theScalingFactor;
    }

    public Vector getGradientVector() {
        return itsGradientVector;
    }

    public void setGradientVector(Vector theGradientVector) {
        this.itsGradientVector = theGradientVector;
    }

    @Override
    public void run() {
        this.itsGradientVector = new Vector(this.itsMolecule.getAtomCount() * Constant.POSITION_DIMENSION_SIZE);

        if (this.itsCalculableList.size() >= this.itsTotalThreadNumber) {
            for (int vi = (int) Math.round((double) this.itsCalculableList.size() / (double) this.itsTotalThreadNumber * (double) this.itsThreadNumber),
                    vEnd = (int) Math.round((double) this.itsCalculableList.size() / (double) this.itsTotalThreadNumber * (1.0 + (double) this.itsThreadNumber)); vi < vEnd; vi++) {
                this._calculateGradientVector(this.itsCalculableList.get(vi));
            }
        } else if (this.itsCalculableList.size() > this.itsThreadNumber) {
            this._calculateGradientVector(this.itsCalculableList.get(this.itsThreadNumber));
        }

    }

    protected void _setGradient(int theAtomNumber, Vector3d theTotalGradientVector) {
        for (int di = 0; di < Constant.POSITION_DIMENSION_SIZE; di++) {
            this.itsGradientVector.set(theAtomNumber * Constant.POSITION_DIMENSION_SIZE + di, 
                    this.itsGradientVector.get(theAtomNumber * Constant.POSITION_DIMENSION_SIZE + di) + theTotalGradientVector.get(di));
        }
    }

    protected abstract void _calculateGradientVector(AbstractCalculableSet theCalculableSet);
}
