/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.math;

import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractDerivativeFunctionOfInternalCoordinate;
import org.bmdrc.basicchemistry.tool.AngleCalculator;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class FirstDerivativeOfInternalCoordinate extends AbstractDerivativeFunctionOfInternalCoordinate {

    //constant Double variable
    private final double ZERO_VALUE = 0.0;

    public FirstDerivativeOfInternalCoordinate(IAtomContainer theMolecule) {
        super(theMolecule);
    }

    public FirstDerivativeOfInternalCoordinate(FirstDerivativeOfInternalCoordinate theFirstDerivativeFunction) {
        super(theFirstDerivativeFunction);
    }

    public Vector3d calculateFirstDerivativeVectorInDistance(IAtomContainer theMolecule, List<Integer> theAtomIndexList, int theDerivativeAtomIndexInAtomList, double theGradient) {
        this.itsMolecule = theMolecule;

        if (this.DISTANCE_ATOM_LIST_SIZE <= theDerivativeAtomIndexInAtomList || theAtomIndexList.size() != this.DISTANCE_ATOM_LIST_SIZE) {
            System.err.println("Atom Index error in first derivative in distance " + theAtomIndexList + " " + theDerivativeAtomIndexInAtomList);
            return null;
        } else if (theGradient == this.ZERO_VALUE) {
            return new Vector3d();
        } else {
            Vector3d theVector = new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(this.END_ATOM_INDEX_IN_DISTANCE)),
                    this.itsMolecule.getAtom(theAtomIndexList.get(this.START_ATOM_INDEX_IN_DISTANCE)));
            Vector3d theDerivativeVector = theVector.getUnitVector();

//            System.out.println(theVector + " " + this.itsMolecule.getAtom(theAtomIndexList.get(this.END_ATOM_INDEX_IN_DISTANCE)) + " " + this.itsMolecule.getAtom(theAtomIndexList.get(this.START_ATOM_INDEX_IN_DISTANCE)));
            if (FirstDerivativeOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE == theDerivativeAtomIndexInAtomList) {
                theDerivativeVector.inverse();
            }

            theDerivativeVector.productScalar(theGradient);

            return theDerivativeVector;
        }
    }

    public Vector3d calculateFirstDerivativeVectorInBendAngle(IAtomContainer theMolecule, List<Integer> theAtomIndexList, int theDerivativeAtomIndexInAtomList, int theMoveAtomIndex, double theGradient) {
        this.itsMolecule = theMolecule;

        if (theAtomIndexList.size() != this.BEND_ANGLE_ATOM_LIST_SIZE) {
            System.err.println(theAtomIndexList + " " + theDerivativeAtomIndexInAtomList);
            System.err.println("Atom Index error in first derivative in bend angle");
            return null;
        } else if (theGradient == this.ZERO_VALUE) {
            return new Vector3d();
        } else {
            return this.__calculateFirstDerivativeVectorInBendAngle(theAtomIndexList, theDerivativeAtomIndexInAtomList, theMoveAtomIndex, theGradient);
        }
    }

    private Vector3d __calculateFirstDerivativeVectorInBendAngle(List<Integer> theAtomIndexList, int theDerivativeAtomIndex, int theMoveAtomIndex, double theGradient) {
        List<Vector3d> theBendAngleVectorList = this._getBendAngleVectorList(theAtomIndexList, theDerivativeAtomIndex);
        int theCenterAtomIndex = theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.CENTER_ATOM_IN_BEND_ANGLE);
        Vector3d theAxisUnitVector = this.__getAxisUnitVector(theBendAngleVectorList);
        Vector3d theMoveAtomPosition = new Vector3d(this.itsMolecule.getAtom(theMoveAtomIndex));

        Vector3d theNewPosition = AngleCalculator.rotatePosition(theMoveAtomPosition, new Vector3d(this.itsMolecule.getAtom(theCenterAtomIndex)), theAxisUnitVector, theGradient);

        return Vector3dCalculator.minus(theNewPosition, theMoveAtomPosition);
    }

    private Vector3d __getAxisUnitVector(List<Vector3d> theBendAngleVectorList) {
        return Vector3dCalculator.crossProduct(theBendAngleVectorList.get(this.VECTOR_CONTAIN_DERIVATIVE_ATOM),
                theBendAngleVectorList.get(this.VECTOR_NOT_CONTAIN_DERIVATIVE_ATOM));
    }

    public Vector3d calculateFirstDerivativeVectorInTorsionAngle(IAtomContainer theMolecule, List<Integer> theAtomIndexList, int theDerivativeAtomIndex, int theMoveAtomIndex, double theGradient) {
        this.itsMolecule = theMolecule;

        if (theGradient == this.ZERO_VALUE) {
            return new Vector3d();
        } else {
            switch (theDerivativeAtomIndex) {
                case AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE:
                case AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE:
                    return this.__calculateFirstDerivativeVectorInTorsionAngle(theAtomIndexList, theDerivativeAtomIndex, theMoveAtomIndex, theGradient);
                default:
                    return null;
            }
        }
    }

    private Vector3d __calculateFirstDerivativeVectorInTorsionAngle(List<Integer> theAtomIndexList, int theDerivativeAtomIndex, int theMoveAtomIndex, double theGradient) {
        Vector3d theAxisUnitVector = this.__getAxisUnitVectorInTorsion(theAtomIndexList, theDerivativeAtomIndex);
        int theConnectedAtomIndex = theDerivativeAtomIndex == FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE ?
                theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE) : theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE);
        Vector3d theNewPosition = AngleCalculator.rotatePosition(this.itsMolecule.getAtom(theMoveAtomIndex), this.itsMolecule.getAtom(theConnectedAtomIndex), theAxisUnitVector, theGradient);

        return Vector3dCalculator.minus(theNewPosition, new Vector3d(this.itsMolecule.getAtom(theMoveAtomIndex)));
    }

    private Double __calculateTorsionAngle(List<Integer> theAtomIndexList, int theDerivativeAtomIndex, Vector3d theNewPosition) {
        switch (theDerivativeAtomIndex) {
            case FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE:
                return AngleCalculator.calculateTorsionAngle(theNewPosition, new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE))),
                        new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE))),
                        new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE))));
            case FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE:
                return AngleCalculator.calculateTorsionAngle(new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE))),
                        new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE))),
                        new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE))),
                        theNewPosition);
            default:
                return null;
        }
    }

    private Vector3d __getAxisUnitVectorInTorsion(List<Integer> theAtomIndexList, int theDerivativeAtomIndex) {
        Vector3d theAxisUnitVectorInTorsion = new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE)),
                this.itsMolecule.getAtom(theAtomIndexList.get(AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE))).getUnitVector();

        if (theDerivativeAtomIndex == AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE) {
            theAxisUnitVectorInTorsion.inverse();
        }

        return theAxisUnitVectorInTorsion;
    }

    private double __calculateFirstDerivativeConstantInTorsionAngleAtAtomConnectedFirstAtom(List<Integer> theAtomIndexList, int theDimensionIndex) {
        List<Vector3d> theVectorList = this._getTorsionVectorList(theAtomIndexList);
        List<Vector3d> theCrossProductVectorList = this._getCrossProductVectorListInTorsion(theAtomIndexList);

        double theFirstDerivative = (theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                - theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 1) % this.DIMENSION_SIZE))
                / Vector3dCalculator.calculateDotProduct(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND),
                theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD))
                + (theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                / Math.pow(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).length(), 2.0);

        if (Math.abs(theFirstDerivative) < this.MINIMUM_VALUE) {
            return this.ZERO_VALUE;
        }

        return theFirstDerivative;
    }

    private double __calculateFirstDerivativeConstantInTorsionAngleAtFirstAtom(List<Integer> theAtomIndexList, int theDimensionIndex) {
        List<Vector3d> theVectorList = this._getTorsionVectorList(theAtomIndexList);
        List<Vector3d> theCrossProductVectorList = this._getCrossProductVectorListInTorsion(theAtomIndexList);
        List<Vector3d> thePositionVectorList = this._getPositionVectorList(theAtomIndexList);

        double theFirstDerivative = ((thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                - thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - theVectorList.get(this.VECTOR_FROM_SECOND_TO_CONNECTED_SECOND_IN_TORSION).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - (thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 1) % this.DIMENSION_SIZE))
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                + theVectorList.get(this.VECTOR_FROM_SECOND_TO_CONNECTED_SECOND_IN_TORSION).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                / Vector3dCalculator.calculateDotProduct(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND),
                theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD))
                - ((thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                - thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - (thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 1) % this.DIMENSION_SIZE))
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                / Math.pow(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).length(), 2.0)
                - (theVectorList.get(this.VECTOR_FROM_SECOND_TO_CONNECTED_SECOND_IN_TORSION).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                - theVectorList.get(this.VECTOR_FROM_SECOND_TO_CONNECTED_SECOND_IN_TORSION).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 1) % this.DIMENSION_SIZE))
                / Math.pow(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).length(), 2.0);

        if (Math.abs(theFirstDerivative) < this.MINIMUM_VALUE) {
            return this.ZERO_VALUE;
        }

        return theFirstDerivative;
    }

    private double __calculateFirstDerivativeConstantInTorsionAngleAtSecondAtom(List<Integer> theAtomIndexList, int theDimensionIndex) {
        List<Vector3d> theVectorList = this._getTorsionVectorList(theAtomIndexList);
        List<Vector3d> theCrossProductVectorList = this._getCrossProductVectorListInTorsion(theAtomIndexList);
        List<Vector3d> thePositionVectorList = this._getPositionVectorList(theAtomIndexList);

        double theFirstDerivative = (theVectorList.get(this.VECTOR_FROM_FIRST_TO_CONNECTED_FIRST_IN_TORSION).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                + (thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                - thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - theVectorList.get(this.VECTOR_FROM_FIRST_TO_CONNECTED_FIRST_IN_TORSION).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                - (thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 1) % this.DIMENSION_SIZE))
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                / Vector3dCalculator.calculateDotProduct(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND),
                theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD))
                - (theVectorList.get(this.VECTOR_FROM_FIRST_TO_CONNECTED_FIRST_IN_TORSION).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - theVectorList.get(this.VECTOR_FROM_FIRST_TO_CONNECTED_FIRST_IN_TORSION).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                / Math.pow(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).length(), 2.0)
                - ((thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                - thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - (thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                - thePositionVectorList.get(FirstDerivativeOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE).get((theDimensionIndex + 1) % this.DIMENSION_SIZE))
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 2) % this.DIMENSION_SIZE))
                / Math.pow(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).length(), 2.0);

        if (Math.abs(theFirstDerivative) < this.MINIMUM_VALUE) {
            return this.ZERO_VALUE;
        }

        return theFirstDerivative;
    }

    private double __calculateFirstDerivativeConstantInTorsionAngleAtAtomConnectedSecondAtom(List<Integer> theAtomIndexList, int theDimensionIndex) {
        List<Vector3d> theVectorList = this._getTorsionVectorList(theAtomIndexList);
        List<Vector3d> theCrossProductVectorList = this._getCrossProductVectorListInTorsion(theAtomIndexList);

        double theFirstDerivative = (theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                - theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND).get((theDimensionIndex + 1) % this.DIMENSION_SIZE))
                / Vector3dCalculator.calculateDotProduct(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND),
                theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD))
                - (theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION).get((theDimensionIndex + 1) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                - theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION).get((theDimensionIndex + 2) % this.DIMENSION_SIZE)
                * theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).get((theDimensionIndex + 1) % this.DIMENSION_SIZE))
                / Math.pow(theCrossProductVectorList.get(this.CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD).length(), 2.0);

        if (Math.abs(theFirstDerivative) < this.MINIMUM_VALUE) {
            return this.ZERO_VALUE;
        }

        return theFirstDerivative;
    }
}
