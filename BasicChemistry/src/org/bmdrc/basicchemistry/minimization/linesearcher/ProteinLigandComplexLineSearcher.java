/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.minimization.linesearcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 02. 16
 */
public class ProteinLigandComplexLineSearcher extends DefaultLineSearcher implements Serializable {

    private static final long serialVersionUID = 3563919000562759056L;

    private IAtomContainer itsLigand;
    private List<Vector3d> itsTransitionVectorListByAtom;

    public ProteinLigandComplexLineSearcher(AbstractUsableInMinimizationInEnergyFunction theEnergyFunction, IAtomContainer theLigand) {
        super(theEnergyFunction);
        try {
            this.itsLigand = theLigand.clone();
            this.__initializeTransitionVectorList();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    private void __initializeTransitionVectorList() {
        this.itsTransitionVectorListByAtom = new ArrayList<>();

        for (IAtom theAtom : this.itsLigand.atoms()) {
            this.itsTransitionVectorListByAtom.add(new Vector3d());
        }
    }

    @Override
    protected double _calculateEnergyUsingPoint(double thePoint) {
//        Vector theGradientVector = this.itsFunction.calculateGradientVector(this.itsSearchVector, thePoint).subVector(0, this.itsLigand.getAtomCount() * Vector3d.DIMENSION_SIZE);

        Vector theInterGradientVector = ((AbstractTotalForceFieldEnergyFunction) this.itsFunction).getInterEnergyFunction().calculateGradientVector(this.itsSearchVector, thePoint);
        Vector theIntraGradientVector = ((AbstractTotalForceFieldEnergyFunction) this.itsFunction).getIntraEnergyFunction().calculateGradientVector(this.itsSearchVector, thePoint);
        
        Vector3d theCenterOfMass = this.__calculateCenterOfMass();
        Vector theTransitionGradient = this.__calculateTransitionGradient(theInterGradientVector, theCenterOfMass);
        Vector theRotationGradient = this.__calculateRotationGradient(theInterGradientVector, theCenterOfMass);
        Vector theGradientVector = VectorCalculator.sum(theTransitionGradient, theRotationGradient);

        theGradientVector = VectorCalculator.sum(theGradientVector, theIntraGradientVector);

        if (this._calculateRMS(theGradientVector) > 1.0) {
            return Double.MAX_VALUE;
        }
        
        Vector theTotalGradient = VectorCalculator.sum(this.itsSearchVector, theGradientVector);

        double theEnergy = this.itsFunction.calculateEnergyUsingGradientVector(theTotalGradient);
        
        if (this._isNotCorrectValue(theEnergy)) {
            return Double.MAX_VALUE;
        }

        return theEnergy;
    }

    private Vector __calculateRotationGradient(Vector theInterGradientVector, Vector3d theCenterOfMass) {
        try {
            IAtomContainer theCopiedLigand = this.itsLigand.clone();
            Vector3d theRotationAngleGradient = this.__calculateRotationAngleGradient(theInterGradientVector, theCenterOfMass);
            Vector theRotationGradient = new Vector();

            for (IAtom theAtom : theCopiedLigand.atoms()) {
                Vector3d theOriginalPosition = new Vector3d(theAtom);

                for (int ai = 0; ai < Constant.POSITION_DIMENSION_SIZE; ai++) {
                    Vector3d theAtomPosition = new Vector3d(theAtom);
                    Vector3d theAxis = new Vector3d(theCenterOfMass);

                    theAxis.set(ai, theAxis.get(ai) + 1.0);

                    GeometryTools.rotate(theAtom, theCenterOfMass.toPoint3d(), theAxis.toPoint3d(), -theRotationAngleGradient.get(ai));
                }

                theRotationGradient.addAll(Vector3dCalculator.minus(new Vector3d(theAtom), theOriginalPosition));
            }

            return theRotationGradient;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private Vector3d __calculateRotationAngleGradient(Vector theInterGradientVector, Vector3d theCenterOfMass) {
        Vector3d theRotationGradient = new Vector3d();

        for (int ai = 0, aEnd = this.itsLigand.getAtomCount(); ai < aEnd; ai++) {
            Vector3d theInterGradientByAtom = new Vector3d(theInterGradientVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.X_INDEX),
                    theInterGradientVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Y_INDEX),
                    theInterGradientVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Z_INDEX));
            if (!theInterGradientByAtom.length().equals(0.0)) {
                Vector3d theAtomPosition = new Vector3d(this.itsLigand.getAtom(ai));
                Vector3d theTransitionGradientByAtom = this.itsTransitionVectorListByAtom.get(ai);
                Vector3d theRotationGradientVector = Vector3dCalculator.minus(theInterGradientByAtom, theTransitionGradientByAtom);
                Vector3d theRelativeAtomPosition = Vector3dCalculator.minus(theAtomPosition, theCenterOfMass);

                double theAngleByXAxial = this.__calculateRotationGradient(theRelativeAtomPosition, theRotationGradientVector, Constant.X_INDEX);
                double theAngleByYAxial = this.__calculateRotationGradient(theRelativeAtomPosition, theRotationGradientVector, Constant.Y_INDEX);
                double theAngleByZAxial = this.__calculateRotationGradient(theRelativeAtomPosition, theRotationGradientVector, Constant.Z_INDEX);

                theRotationGradient.add(new Vector3d(theAngleByXAxial, theAngleByYAxial, theAngleByZAxial));
            }
        }

        return theRotationGradient;
    }

    private double __calculateRotationGradient(Vector3d theRelativeAtomPosition, Vector3d theRotationGradient, int theUsedAxial) {
        Vector3d theProjectionVectorByAtom = new Vector3d(theRelativeAtomPosition);
        Vector3d theProjectionVectorByGradient = Vector3dCalculator.sum(theRotationGradient, theProjectionVectorByAtom);
        Vector3d theStandardAxialVector = new Vector3d();

        theStandardAxialVector.set((theUsedAxial + 1) % Constant.POSITION_DIMENSION_SIZE, 1.0);

        theProjectionVectorByAtom.set(theUsedAxial, 0.0);
        theProjectionVectorByGradient.set(theUsedAxial, 0.0);

        if (theProjectionVectorByAtom.length() == 0.0 || theProjectionVectorByGradient.length() == 0.0
                || Vector3dCalculator.minus(theProjectionVectorByAtom, theProjectionVectorByGradient).length() == 0.0) {
            return 0.0;
        }

        int theAtomProjectionVectorQuadrant = this.__calculateQuadrant(theProjectionVectorByAtom, theUsedAxial);
        int theGradientProjectionVectorQuadrant = this.__calculateQuadrant(theProjectionVectorByGradient, theUsedAxial);

        double theAtomProjectionAngle = Math.toDegrees(Vector3dCalculator.calculateAngle(theProjectionVectorByAtom, theStandardAxialVector));
        double theGradientProjectionAngle = Math.toDegrees(Vector3dCalculator.calculateAngle(theProjectionVectorByGradient, theStandardAxialVector));

        theAtomProjectionAngle = this.__calculateAbsoluteAngle(theAtomProjectionAngle, theAtomProjectionVectorQuadrant);
        theGradientProjectionAngle = this.__calculateAbsoluteAngle(theGradientProjectionAngle, theGradientProjectionVectorQuadrant);

        if (theAtomProjectionAngle >= 270.0 && theGradientProjectionAngle <= 90.0) {
            theGradientProjectionAngle += 360.0;
        } else if (theAtomProjectionAngle <= 90.0 && theGradientProjectionAngle >= 270.0) {
            theAtomProjectionAngle += 360.0;
        }

        return theGradientProjectionAngle - theAtomProjectionAngle;
    }

    private double __calculateAbsoluteAngle(double theAngleByStandardAxial, int theQuadrant) {
        switch (theQuadrant) {
            case 1:
                return 90.0 - theAngleByStandardAxial;
            case 2:
                return 90.0 + theAngleByStandardAxial;
            case 3:
                return 90.0 + theAngleByStandardAxial;
            case 4:
                return 450.0 - theAngleByStandardAxial;
            default:
                System.err.println("Error in ProteinLigandComplexLineSearcher.java (\"__calculateAbsoluteAngle\" function");
        }

        return 0.0;
    }

    private int __calculateQuadrant(Vector3d theVector3d, int theUsedAxial) {
        double theFirstValue = theVector3d.get((theUsedAxial + 1) % Constant.POSITION_DIMENSION_SIZE);
        double theSecondValue = theVector3d.get((theUsedAxial + 2) % Constant.POSITION_DIMENSION_SIZE);

        if (theFirstValue >= 0.0) {
            if (theSecondValue >= 0.0) {
                return 1;
            } else {
                return 2;
            }
        } else {
            if (theSecondValue >= 0.0) {
                return 4;
            } else {
                return 3;
            }
        }
    }

    private Vector __calculateTransitionGradient(Vector theInterGradientVector, Vector3d theCenterOfMass) {
        Vector3d theTransitionGradient = new Vector3d(0.0, 0.0, 0.0);
        Vector theResultTransitionGradient = new Vector();
        int theAtomIndex = 0;

        for (IAtom theAtom : this.itsLigand.atoms()) {
            Vector3d theAtomGradientVector = new Vector3d(theInterGradientVector.get(theAtomIndex * Constant.POSITION_DIMENSION_SIZE + Constant.X_INDEX),
                    theInterGradientVector.get(theAtomIndex * Constant.POSITION_DIMENSION_SIZE + Constant.Y_INDEX),
                    theInterGradientVector.get(theAtomIndex * Constant.POSITION_DIMENSION_SIZE + Constant.Z_INDEX));

            if (!theAtomGradientVector.length().equals(0.0)) {
                Point3d theAtomPosition = theAtom.getPoint3d();
                Vector3d theAtomPositionVector = new Vector3d(theAtomPosition.x + this.itsSearchVector.get(theAtomIndex * Constant.POSITION_DIMENSION_SIZE + Constant.X_INDEX),
                        theAtomPosition.y + this.itsSearchVector.get(theAtomIndex * Constant.POSITION_DIMENSION_SIZE + Constant.Y_INDEX),
                        theAtomPosition.z + this.itsSearchVector.get(theAtomIndex * Constant.POSITION_DIMENSION_SIZE + Constant.Z_INDEX));
                Vector3d theTransitionVectorByAtom = Vector3dCalculator.minus(theCenterOfMass, theAtomPositionVector).getUnitVector();

                theTransitionVectorByAtom = Vector3dCalculator.productByScalar(Vector3dCalculator.calculateDotProduct(theTransitionVectorByAtom, theAtomGradientVector),
                        theTransitionVectorByAtom);
                theTransitionGradient = Vector3dCalculator.sum(theTransitionGradient, theTransitionVectorByAtom);
                this.itsTransitionVectorListByAtom.set(theAtomIndex, theTransitionVectorByAtom);
            } else {
                this.itsTransitionVectorListByAtom.set(theAtomIndex, new Vector3d());
            }

            theAtomIndex++;
        }

        for (IAtom theAtom : this.itsLigand.atoms()) {
            theResultTransitionGradient.addAll(theTransitionGradient);
        }

        return theResultTransitionGradient;
    }

    private Vector3d __calculateCenterOfMass() {
        for (int ai = 0, aEnd = this.itsLigand.getAtomCount(); ai < aEnd; ai++) {
            IAtom theAtom = this.itsLigand.getAtom(ai);
            Point3d thePosition = theAtom.getPoint3d();

            theAtom.setPoint3d(new Point3d(thePosition.x + this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.X_INDEX),
                    thePosition.y + this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Y_INDEX),
                    thePosition.z + this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Z_INDEX)));
        }

        Point3d theCenterOfMass = GeometryTools.get3DCenter(this.itsLigand);

        for (int ai = 0, aEnd = this.itsLigand.getAtomCount(); ai < aEnd; ai++) {
            IAtom theAtom = this.itsLigand.getAtom(ai);
            Point3d thePosition = theAtom.getPoint3d();

            theAtom.setPoint3d(new Point3d(thePosition.x - this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.X_INDEX),
                    thePosition.y - this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Y_INDEX),
                    thePosition.z - this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Constant.Z_INDEX)));
        }

        return new Vector3d(theCenterOfMass);
    }

    protected double _calculateRMS(Vector theVector) {
        AbstractUsableInMinimizationInEnergyFunction theFunction = (AbstractUsableInMinimizationInEnergyFunction) this.itsFunction;
        int theAtomCount = theFunction.getMolecule().getAtomCount();
        double theRMS = 0.0;

        for (int ai = 0; ai < theVector.size() / Constant.POSITION_DIMENSION_SIZE; ai++) {
            Vector theMovedVectorInAtom = theVector.subVector(ai * Constant.POSITION_DIMENSION_SIZE, (ai + 1) * Constant.POSITION_DIMENSION_SIZE);

            theRMS += theMovedVectorInAtom.length();
        }

        theRMS /= theAtomCount;

        return theRMS;
    }
    
    @Override
    public Vector getOptimumDirectionVector() {
        Vector theInterGradientVector = ((AbstractTotalForceFieldEnergyFunction) this.itsFunction).getInterEnergyFunction().calculateGradientVector(this.itsSearchVector, this.itsOptimumPoint);
        Vector theIntraGradientVector = ((AbstractTotalForceFieldEnergyFunction) this.itsFunction).getIntraEnergyFunction().calculateGradientVector(this.itsSearchVector, this.itsOptimumPoint);
        
        Vector3d theCenterOfMass = this.__calculateCenterOfMass();
        Vector theTransitionGradient = this.__calculateTransitionGradient(theInterGradientVector, theCenterOfMass);
        Vector theRotationGradient = this.__calculateRotationGradient(theInterGradientVector, theCenterOfMass);
        Vector theGradientVector = VectorCalculator.sum(theTransitionGradient, theRotationGradient);

        return VectorCalculator.sum(theGradientVector, theIntraGradientVector);
    }
}
