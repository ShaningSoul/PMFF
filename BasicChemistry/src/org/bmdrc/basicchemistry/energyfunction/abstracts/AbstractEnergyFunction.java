package org.bmdrc.basicchemistry.energyfunction.abstracts;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.energyfunction.interfaces.ICalculationParameter;
import org.bmdrc.basicchemistry.math.FirstDerivativeOfInternalCoordinate;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public abstract class AbstractEnergyFunction<Parameter extends ICalculationParameter> implements Serializable, Cloneable {

    private static final long serialVersionUID = -5562246737483746984L;

    protected IAtomContainer itsMolecule;
    protected Parameter itsCalculationParameter;
    protected Double itsEnergy;
    protected FirstDerivativeOfInternalCoordinate itsFirstDerivativeFunction;
    //constant Double variable
    protected final double MAXIMUM_ANGLE_DRGREE = 180.0;
    protected final double CONVERTING_FACTOR_FROM_RADIAN_TO_DEGREE = 180.0 / Math.PI;
    //constant Integer variable
    protected final int INVALID_VALUE = -1;
    //constant String variable
    protected final String IS_CALCULATED_KEY = "Is Calculated";
    //constant Boolean variable
    protected final boolean CALCULATED = true;

    public AbstractEnergyFunction(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
        this.itsFirstDerivativeFunction = new FirstDerivativeOfInternalCoordinate(this.itsMolecule);
    }
    
    public AbstractEnergyFunction(IAtomContainer theMolecule, Parameter theCalculationParameter) {
        this.itsMolecule = theMolecule;
        this.itsCalculationParameter = theCalculationParameter;
        this.itsFirstDerivativeFunction = new FirstDerivativeOfInternalCoordinate(this.itsMolecule);
    }

    public AbstractEnergyFunction(AbstractEnergyFunction theEnergyFunction) throws CloneNotSupportedException {
        this.itsMolecule = (IAtomContainer) theEnergyFunction.itsMolecule.clone();
        this.itsCalculationParameter = (Parameter)(theEnergyFunction.itsCalculationParameter.clone());
        this.itsFirstDerivativeFunction = new FirstDerivativeOfInternalCoordinate(theEnergyFunction.itsFirstDerivativeFunction);
    }

    private static int __getTotalNumberOfThread() {
        return Runtime.getRuntime().availableProcessors();
    }

    public IAtomContainer getMolecule() {
        return itsMolecule;
    }

    public void setMolecule(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
    }

    public Parameter getCalculationParameter() {
        return this.itsCalculationParameter;
    }
    
    public void setCalculationParameter(Parameter theCalculationParameter) {
        this.itsCalculationParameter = theCalculationParameter;
    }
    
    public List<Integer> getNotCalculateAtomNumberList() {
        return this.itsCalculationParameter.getNotCalculableAtomIndexList();
    }

    public void setNotCalculateAtomNumberList(List<Integer> theNotCalculateAtomNumberList) {
        this.itsCalculationParameter.setNotCalculableAtomIndexList(theNotCalculateAtomNumberList);
    }

    public IAtomContainer setMolecule() {
        return itsMolecule;
    }

    public Double getEnergy() {
        return itsEnergy;
    }

    public void setEnergy(Double theEnergy) {
        this.itsEnergy = theEnergy;
    }

    public abstract Double calculateEnergyFunction();

    public Double calculateEnergyFunction(IAtomContainer theMolecule) {
        try {
            IAtomContainer theCopiedMolecule = (IAtomContainer) this.itsMolecule.clone();

            this.setMolecule(theMolecule);
            double theEnergy = this.calculateEnergyFunction();
            this.setMolecule(theCopiedMolecule);

            return theEnergy;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule Clone Error");
        }

        return null;
    }

    public IAtomContainer setAtomPositionInTotalAtomPositionVector(Vector theTotalGredientVector) {
        try {
            IAtomContainer theMolecule = (IAtomContainer) this.itsMolecule.clone();

            for (int ai = 0, aEnd = theTotalGredientVector.size() / 3; ai < aEnd; ai++) {
                Point3d thePosition = new Point3d((Double) theTotalGredientVector.get(Constant.POSITION_DIMENSION_SIZE * ai + Constant.X_INDEX) + theMolecule.getAtom(ai).getPoint3d().x,
                        theTotalGredientVector.get(Constant.POSITION_DIMENSION_SIZE * ai + Constant.Y_INDEX) + theMolecule.getAtom(ai).getPoint3d().y,
                        theTotalGredientVector.get(Constant.POSITION_DIMENSION_SIZE * ai + Constant.Z_INDEX) + theMolecule.getAtom(ai).getPoint3d().z);

                theMolecule.getAtom(ai).setPoint3d(thePosition);
            }

            return theMolecule;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule Clone Error");
        }

        return null;
    }

    public Double calculateEnergyUsingGradientVector(Vector theTotalGredientVector) {
        this._convertAtomPositionUsingSearchVector(theTotalGredientVector);

        double theEnergy = this.calculateEnergyFunction();

        this._recoveryAtomPositionUsingSearchVector(theTotalGredientVector);

        return theEnergy;
    }

    protected void _convertAtomPositionUsingSearchVector(Vector theSearchVector) {
        for (int ai = 0, aEnd = theSearchVector.size() / 3; ai < aEnd; ai++) {
            IAtom theAtom = this.itsMolecule.getAtom(ai);
            Point3d thePosition = theAtom.getPoint3d();
            double theX = thePosition.x + theSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.X_INDEX);
            double theY = thePosition.y + theSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Y_INDEX);
            double theZ = thePosition.z + theSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Z_INDEX);

            theAtom.setPoint3d(new Point3d(theX, theY, theZ));
        }
    }

    protected void _recoveryAtomPositionUsingSearchVector(Vector theSearchVector) {
        for (int ai = 0, aEnd = theSearchVector.size() / 3; ai < aEnd; ai++) {
            IAtom theAtom = this.itsMolecule.getAtom(ai);
            Point3d thePosition = theAtom.getPoint3d();
            double theX = thePosition.x - theSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.X_INDEX);
            double theY = thePosition.y - theSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Y_INDEX);
            double theZ = thePosition.z - theSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Z_INDEX);

            theAtom.setPoint3d(new Point3d(theX, theY, theZ));
        }
    }
}
