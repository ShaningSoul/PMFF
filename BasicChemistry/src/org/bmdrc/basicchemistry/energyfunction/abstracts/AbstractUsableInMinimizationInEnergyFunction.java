/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.energyfunction.abstracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.interfaces.ICalculationParameter;
import org.bmdrc.basicchemistry.minimization.interfaces.UsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public abstract class AbstractUsableInMinimizationInEnergyFunction<T extends ICalculationParameter> extends AbstractEnergyFunction<T> implements UsableInMinimizationInEnergyFunction, Serializable {

    private static final long serialVersionUID = 4263964902095438900L;

    protected MoveTogetherAtomNumberList itsMoveTogetherAtomNumberMap;
    protected Vector itsGradientVector;
    protected Vector itsConjugatedGradientVector;
    //constant Double variable
    private final double INITIAL_SCALING_FACTOR = 1.0;

    public AbstractUsableInMinimizationInEnergyFunction(IAtomContainer theMolecule, T theCalculationParameter) {
        this(theMolecule, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }

    public AbstractUsableInMinimizationInEnergyFunction(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule,
            T theCalculationParameter) {
        this(theTotalMolecule, theCalculationParameter, new MoveTogetherAtomNumberList(theTotalMolecule, theUnitMolecule));
    }

    public AbstractUsableInMinimizationInEnergyFunction(Protein theProtein, IAtomContainer theLigand, 
            T theCalculationParameter) {
        this(AbstractUsableInMinimizationInEnergyFunction._getComplex(theLigand, theProtein.getMolecule()),
                theCalculationParameter, new MoveTogetherAtomNumberList(theLigand));
    }

    public AbstractUsableInMinimizationInEnergyFunction(IAtomContainer theMolecule, T theCalculationParameter,
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter);
        this.itsMoveTogetherAtomNumberMap = theMoveTogetherAtomNumberMap;
        this.itsGradientVector = new Vector((this.itsMolecule.getAtomCount() - theCalculationParameter.getNotCalculableAtomIndexList().size()) * Constant.POSITION_DIMENSION_SIZE);
    }

    public Vector getGradientVector() {
        return itsGradientVector;
    }

    public void setGradientVector(Vector theGradientVector) {
        this.itsGradientVector = theGradientVector;
    }

    public Vector getConjugatedGradientVector() {
        return itsConjugatedGradientVector;
    }

    public void setConjugatedGradientVector(Vector theConjugatedGradientVector) {
        this.itsConjugatedGradientVector = theConjugatedGradientVector;
    }

    public MoveTogetherAtomNumberList getMoveTogetherAtomNumberMap() {
        return itsMoveTogetherAtomNumberMap;
    }

    public void setMoveTogetherAtomNumberMap(MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this.itsMoveTogetherAtomNumberMap = theMoveTogetherAtomNumberMap;
    }

    protected static IAtomContainer _getComplex(IAtomContainer... theMolecules) {
        IAtomContainer theComplex = new AtomContainer();

        for (IAtomContainer theMolecule : theMolecules) {
            theComplex.add(theMolecule);
        }

        return theComplex;
    }

    protected static List<Integer> _getNotCalculatedAtomNumberList(IAtomContainer theCalculateMolecule, IAtomContainer theNotCalculatedMolecule) {
        List<Integer> theNotCalculateAtomNumberList = new ArrayList<>();
        int theNumberOfAtomInCalculatedMolecule = theCalculateMolecule.getAtomCount();

        for (int ai = 0, aEnd = theNotCalculatedMolecule.getAtomCount(); ai < aEnd; ai++) {
            theNotCalculateAtomNumberList.add(ai + theNumberOfAtomInCalculatedMolecule);
        }

        return theNotCalculateAtomNumberList;
    }

    protected void _initializeGradientVector() {
        this.itsGradientVector = new Vector((this.itsMolecule.getAtomCount() - this.itsCalculationParameter.getNotCalculableAtomIndexList().size()) * Constant.POSITION_DIMENSION_SIZE);
    }

    protected void _initializeConjugatedGradientVector() {
        this.itsConjugatedGradientVector = new Vector((this.itsMolecule.getAtomCount() - this.itsCalculationParameter.getNotCalculableAtomIndexList().size()) * Constant.POSITION_DIMENSION_SIZE);
    }

    @Override
    public void calculateGradient(IAtomContainer theMolecule) {
        List<Vector3d> theChangedVectorList = new ArrayList<>();

        if (theMolecule.getAtomCount() != this.itsMolecule.getAtomCount()) {
            System.err.println("The number of atom are not equal!!");
            return;
        }

        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            Vector3d thePositionVector = new Vector3d(theMolecule.getAtom(ai));
            Vector3d theOriginalPositionVector = new Vector3d(this.itsMolecule.getAtom(ai));

            theChangedVectorList.add(theOriginalPositionVector);
            this.itsMolecule.getAtom(ai).setPoint3d(thePositionVector.toPoint3d());
        }

        this.calculateGradient();

        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            this.itsMolecule.getAtom(ai).setPoint3d(theChangedVectorList.get(ai).toPoint3d());
        }
    }

    @Override
    public void calculateGradient(Vector theSearchVector) {
        this._convertAtomPositionUsingSearchVector(theSearchVector);
        this.calculateGradient();
        this._recoveryAtomPositionUsingSearchVector(theSearchVector);
    }

    @Override
    public void reloadGradientVector(IAtomContainer theMolecule) {
        List<Vector3d> theChangedVectorList = new ArrayList<>();

        if (theMolecule.getAtomCount() != this.itsMolecule.getAtomCount()) {
            System.err.println("The number of atom are not equal!!");
            return;
        }

        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            Vector3d thePositionVector = new Vector3d(theMolecule.getAtom(ai));
            Vector3d theOriginalPositionVector = new Vector3d(this.itsMolecule.getAtom(ai));

            theChangedVectorList.add(theOriginalPositionVector);
            this.itsMolecule.getAtom(ai).setPoint3d(thePositionVector.toPoint3d());
        }

        this.reloadGradientVector();

        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            this.itsMolecule.getAtom(ai).setPoint3d(theChangedVectorList.get(ai).toPoint3d());
        }
    }

    public Vector calculateGradientVector(IAtomContainer theMolecule, double theScalingFactor) {
        this.reloadGradientVector(theMolecule);

        return VectorCalculator.productScalarValue(theScalingFactor, this.itsGradientVector);
    }

    public Vector calculateGradientVector(Vector theSearchVector, double theScalingFactor) {
        this.reloadGradientVector(theSearchVector);

        Vector theResult = VectorCalculator.productScalarValue(theScalingFactor, this.itsGradientVector);
        
        return theResult;
    }

    @Override
    public void reloadGradientVector(Vector theSearchVector) {
        this._convertAtomPositionUsingSearchVector(theSearchVector);
        this.reloadGradientVector();
        this._recoveryAtomPositionUsingSearchVector(theSearchVector);
    }

    protected void _setGradient(int theAtomNumber, Vector3d theTotalGradientVector) {
        for (int di = 0; di < Constant.POSITION_DIMENSION_SIZE; di++) {
            this.itsGradientVector.set(theAtomNumber * Constant.POSITION_DIMENSION_SIZE + di, this.itsGradientVector.get(theAtomNumber * Constant.POSITION_DIMENSION_SIZE + di)
                    + theTotalGradientVector.get(di));
        }
    }

    protected void _setConjugatedGradient(int theAtomNumber, Vector3d theTotalGradientVector) {
        for (int di = 0; di < Constant.POSITION_DIMENSION_SIZE; di++) {
            this.itsConjugatedGradientVector.set(theAtomNumber * Constant.POSITION_DIMENSION_SIZE + di, this.itsConjugatedGradientVector.get(theAtomNumber * Constant.POSITION_DIMENSION_SIZE + di)
                    + theTotalGradientVector.get(di));
        }
    }

    public Vector calculateConjugatedGradientVector(IAtomContainer theMolecule, double theScalingFactor) {
        this.reloadConjugatedGradientVector(theMolecule);

        return VectorCalculator.productScalarValue(theScalingFactor, this.itsConjugatedGradientVector);
    }

    @Override
    public void reloadConjugatedGradientVector(IAtomContainer theMolecule) {
        List<Vector3d> theChangedVectorList = new ArrayList<>();

        if (theMolecule.getAtomCount() != this.itsMolecule.getAtomCount()) {
            System.err.println("The number of atom are not equal!!");
            return;
        }

        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            Vector3d thePositionVector = new Vector3d(theMolecule.getAtom(ai));
            Vector3d theOriginalPositionVector = new Vector3d(this.itsMolecule.getAtom(ai));

            theChangedVectorList.add(theOriginalPositionVector);
            this.itsMolecule.getAtom(ai).setPoint3d(thePositionVector.toPoint3d());
        }
    }

    public Vector calculateConjugatedGradientVector(Vector theSearchVector, double theScalingFactor) {
        this.reloadConjugatedGradientVector(theSearchVector);

        return VectorCalculator.productScalarValue(theScalingFactor, this.itsConjugatedGradientVector);
    }

    @Override
    public void reloadConjugatedGradientVector(Vector theSearchVector) {
        this._convertAtomPositionUsingSearchVector(theSearchVector);
        this.reloadConjugatedGradientVector();
        this._recoveryAtomPositionUsingSearchVector(theSearchVector);
    }

    public Vector calculateGradientVector(double theScalingFactor) {
        return VectorCalculator.productScalarValue(theScalingFactor, this.itsGradientVector);
    }

    public Vector calculateConjugatedGradientVector(double theScalingFactor) {
        return VectorCalculator.productScalarValue(theScalingFactor, this.itsConjugatedGradientVector);
    }
}
