/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.tool;

import java.io.Serializable;
import java.util.List;
import org.bmdrc.basicmath.matrix.Matrix;
import org.bmdrc.basicmath.matrix.MatrixCalculator;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.ui.abstracts.Constant;
import org.openscience.cdk.interfaces.IAtom;

/**
 * This class is used in angle calculation You can calculate Bend angle, Torsion
 * Angle Also this class can convert from Radian to degree value
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 */
public class AngleCalculator implements Serializable {

    private static final long serialVersionUID = 4415509481541296443L;
    //constant Integer variable
    private static final int POSITION_MATRIX_COLUMN_COUNT = 1;
    private static final int POSITION_MATRIX_ROW_COUNT = 3;
    //constant Double variable
    private static final double MAXIMUM_ANGLE_DRGREE = 360.0;
    private static final double MAXIMUM_RADIAN = Math.PI * 2.0;

    /**
     * calculate Bend Angle used in atom list variable <br> This variable is
     * important to alignment to atom
     *
     * @param theAtomList Atom list [First atom, Center atom, Second atom]
     * @return double variable to make angle among three atoms
     */
    public static double calculateBondAngle(List<IAtom> theAtomList) {
        return AngleCalculator.calculateBondAngle(theAtomList.get(Constant.FIRST_INDEX), theAtomList.get(Constant.SECOND_INDEX),
                theAtomList.get(Constant.THIRD_INDEX));
    }

    /**
     * calculate bend angle used in three atom variable
     * 
     * @param theFirstAtom IAtom variable
     * @param theCenterAtom IAtom variable
     * @param theSecondAtom IAtom variable
     * @return double variable to make angle among three atoms
     */
    public static Double calculateBondAngle(IAtom theFirstAtom, IAtom theCenterAtom, IAtom theSecondAtom) {
        Vector3d theFirstVector = new Vector3d(theCenterAtom, theFirstAtom);
        Vector3d theSecondVector = new Vector3d(theCenterAtom, theSecondAtom);

        return Vector3dCalculator.calculateAngle(theFirstVector, theSecondVector);
    }

    public static Double calculateBondAngle(Vector3d theFirstAtomPosition, Vector3d theCenterAtomPosition, Vector3d theSecondAtomPosition) {
        Vector3d theFirstVector = Vector3dCalculator.minus(theFirstAtomPosition, theCenterAtomPosition);
        Vector3d theSecondVector = Vector3dCalculator.minus(theSecondAtomPosition, theCenterAtomPosition);

        return Vector3dCalculator.calculateAngle(theFirstVector, theSecondVector);
    }
    
    /**
     * calculate Torsion angle used in atom list<br>
     * This atom list used in this function is important to atom alignment in list
     * 
     * @param theAtomList Atom list [Atom connected first atom, First atom, Second atom, Atom connected second atom]
     * @return Torsion angle to double variable
     */
    public static double calculateTorsionAngle(List<IAtom> theAtomList) {
        return AngleCalculator.calculateTorsionAngle(theAtomList.get(Constant.FIRST_INDEX), theAtomList.get(Constant.SECOND_INDEX),
                theAtomList.get(Constant.THIRD_INDEX), theAtomList.get(Constant.FOURTH_INDEX));
    }

    /**
     * calculate Torsion angle used in atom
     * 
     * @param theConnectedAtomInFirstAtom IAtom variable
     * @param theFirstAtom IAtom variable
     * @param theSecondAtom IAtom variable
     * @param theConnectedAtomInSecondAtom IAtom variable
     * @return Torsion angle to double variable
     */
    public static double calculateTorsionAngle(IAtom theConnectedAtomInFirstAtom, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theConnectedAtomInSecondAtom) {
        return AngleCalculator.calculateTorsionAngle(new Vector3d(theConnectedAtomInFirstAtom), new Vector3d(theFirstAtom), new Vector3d(theSecondAtom), new Vector3d(theConnectedAtomInSecondAtom));
    }
    
    public static double calculateTorsionAngle(Vector3d theConnectedAtomInFirstAtomPosition, Vector3d theFirstAtomPosition, Vector3d theSecondAtomPosition, Vector3d theConnectedAtomInSecondAtomPosition) {
        Vector3d theFirstVector = Vector3dCalculator.minus(theConnectedAtomInFirstAtomPosition, theFirstAtomPosition);
        Vector3d theSecondVector = Vector3dCalculator.minus(theConnectedAtomInSecondAtomPosition, theSecondAtomPosition);
        Vector3d theConnectedVector = Vector3dCalculator.minus(theSecondAtomPosition, theFirstAtomPosition);
        Vector3d theFirstCrossVector = Vector3dCalculator.crossProduct(theFirstVector, theConnectedVector);
        Vector3d theSecondCrossVector = Vector3dCalculator.crossProduct(theConnectedVector.inverse(), theSecondVector);
        
        double theDotProduct = Vector3dCalculator.calculateDotProduct(theFirstCrossVector, theSecondCrossVector);
        double theCosineValue = theDotProduct / (theFirstCrossVector.length() * theSecondCrossVector.length());

        if (theCosineValue > 1.0) {
            return 0.0;
        } else if (theCosineValue < -1.0) {
            return Math.PI;
        }

        return Math.acos(theCosineValue);
    }
    /**
     * calculate angle used in two vector3d
     * 
     * @param theFirstVector first template vector
     * @param theSecondVector second template vector
     * @return angle value to radian form
     */
    public static double calculateAngle(Vector3d theFirstVector, Vector3d theSecondVector) {
        return Vector3dCalculator.calculateAngle(theFirstVector, theSecondVector);
    }
    
    public static Vector3d rotatePosition(IAtom theTargetAtom, IAtom theConnectedAtom, Vector3d theAxisVector, double theRotateAngle) {
        return AngleCalculator.rotatePosition(new Vector3d(theTargetAtom), new Vector3d(theConnectedAtom), theAxisVector, theRotateAngle);
    }
    
    public static Vector3d rotatePosition(Vector3d thePosition, Vector3d theConnectedPosition, Vector3d theAxisVector, double theRotatedAngle) {
        return AngleCalculator.rotatePosition(thePosition, AngleCalculator.__getRotationMatrix(theAxisVector.getUnitVector(), theRotatedAngle), 
                theConnectedPosition);
    }
    
    public static Vector3d rotatePosition(Vector3d thePosition, Matrix theRotationMatrix, Vector3d theCenterPosition) {
        Matrix thePositionMatrix = new Matrix();
        Vector3d theMovedPosition = Vector3dCalculator.minus(thePosition, theCenterPosition);

        thePositionMatrix.addColumn(theMovedPosition);

        List<Double> theResult = (List<Double>)(MatrixCalculator.cross(theRotationMatrix, thePositionMatrix).getColumn(Constant.FIRST_INDEX));
        Vector3d theDirectionVector = new Vector3d(theResult.get(Constant.FIRST_INDEX), theResult.get(Constant.SECOND_INDEX), theResult.get(Constant.THIRD_INDEX));
        
        return Vector3dCalculator.sum(thePosition, Vector3dCalculator.minus(theDirectionVector, theMovedPosition));
    }
    
    private static Matrix __getRotationMatrix(Vector3d theAxisVector, double theRotatedAngle) {
        double theRadianAngle = Math.toRadians(theRotatedAngle);
        Matrix theRotationMatrix = new Matrix(3,3);
        
        theRotationMatrix.setValue(0, 0, Math.cos(theRadianAngle) + theAxisVector.getX() * theAxisVector.getX() * (1.0 - Math.cos(theRadianAngle)));
        theRotationMatrix.setValue(0, 1, theAxisVector.getX() * theAxisVector.getY() * (1.0 - Math.cos(theRadianAngle)) - theAxisVector.getZ() * Math.sin(theRadianAngle));
        theRotationMatrix.setValue(0, 2, theAxisVector.getX() * theAxisVector.getZ() * (1.0 - Math.cos(theRadianAngle)) + theAxisVector.getY() * Math.sin(theRadianAngle));
        theRotationMatrix.setValue(1, 0, theAxisVector.getY() * theAxisVector.getX() * (1.0 - Math.cos(theRadianAngle)) + theAxisVector.getZ() * Math.sin(theRadianAngle));
        theRotationMatrix.setValue(1, 1, Math.cos(theRadianAngle) + theAxisVector.getY() * theAxisVector.getY() * (1.0 - Math.cos(theRadianAngle)));
        theRotationMatrix.setValue(1, 2, theAxisVector.getY() * theAxisVector.getZ() * (1.0 - Math.cos(theRadianAngle)) - theAxisVector.getX() * Math.sin(theRadianAngle));
        theRotationMatrix.setValue(2, 0, theAxisVector.getZ() * theAxisVector.getX() * (1.0 - Math.cos(theRadianAngle)) - theAxisVector.getY() * Math.sin(theRadianAngle));
        theRotationMatrix.setValue(2, 1, theAxisVector.getZ() * theAxisVector.getY() * (1.0 - Math.cos(theRadianAngle)) + theAxisVector.getX() * Math.sin(theRadianAngle));
        theRotationMatrix.setValue(2, 2, Math.cos(theRadianAngle) + theAxisVector.getZ() * theAxisVector.getZ() * (1.0 - Math.cos(theRadianAngle)));
        
        return theRotationMatrix;
    }
}
