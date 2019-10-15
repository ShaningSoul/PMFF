/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.tool;

import java.io.Serializable;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicmath.matrix.Matrix;
import org.openide.util.Exceptions;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class MoleculeManipulator implements Serializable {

    private static final long serialVersionUID = 1383889490179532389L;

    public static IAtomContainer moveMoleculeInCenterOfCoordinate(IAtomContainer theMolecule) {
        Vector3d theCenterOfMolecule = MoleculeManipulator.getCenterCoordinateInMolecule(theMolecule);

        for (IAtom theAtom : theMolecule.atoms()) {
            double theX = theAtom.getPoint3d().x - theCenterOfMolecule.getX();
            double theY = theAtom.getPoint3d().y - theCenterOfMolecule.getY();
            double theZ = theAtom.getPoint3d().z - theCenterOfMolecule.getZ();

            theAtom.setPoint3d(new Point3d(theX, theY, theZ));
        }

        return theMolecule;
    }

    public static Vector3d getCenterCoordinateInMolecule(IAtomContainer theMolecule) {
        Vector3d theTotalSumOfCoordinates = new Vector3d();

        for (IAtom theAtom : theMolecule.atoms()) {
            Vector3d theAtomPosition = new Vector3d(theAtom);

            theTotalSumOfCoordinates.add(theAtomPosition);
        }

        return theTotalSumOfCoordinates.divideScalar(theMolecule.getAtomCount());
    }

    public static void translateMoleculeSelf(IAtomContainer theMolecule, Vector3d theTranslateVector) {
        MoleculeManipulator.translateMoleculeSelf(theMolecule, theTranslateVector.getX(), theTranslateVector.getY(), theTranslateVector.getZ());
    }

    public static void translateMoleculeSelf(IAtomContainer theMolecule, Double theX, Double theY, Double theZ) {
        for (IAtom theAtom : theMolecule.atoms()) {
            Point3d thePosition = theAtom.getPoint3d();
            
            theAtom.getPoint3d().set(thePosition.x + theX, thePosition.y + theY, thePosition.z + theZ);
        }
    }

    public static IAtomContainer translateMolecule(IAtomContainer theMolecule, Vector3d theTranslationVector) {
        return MoleculeManipulator.translateMolecule(theMolecule, theTranslationVector.getX(), theTranslationVector.getY(), theTranslationVector.getZ());
    }

    public static IAtomContainer translateMolecule(IAtomContainer theMolecule, Double theX, Double theY, Double theZ) {
        try {
            IAtomContainer theResultMolecule = theMolecule.clone();

            for (IAtom theAtom : theResultMolecule.atoms()) {
                Point3d thePosition = theAtom.getPoint3d();

                theAtom.getPoint3d().set(thePosition.x + theX, thePosition.y + theY, thePosition.z + theZ);
            }

            return theResultMolecule;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void rotateByCenterSelf(IAtomContainer theMolecule, Vector3d theRotatedDegreeAngleVector) {
        MoleculeManipulator.rotateByCenterSelf(theMolecule, theRotatedDegreeAngleVector.getZ(), theRotatedDegreeAngleVector.getX(),
                theRotatedDegreeAngleVector.getY());
    }

    public static void rotateByCenterSelf(IAtomContainer theMolecule, Double theXYDegreeAngle, Double theYZDegreeAngle, Double theZXDegreeAngle) {
        Vector3d theCenter = new Vector3d(GeometryTools.get3DCenter(theMolecule));

        for (IAtom theAtom : theMolecule.atoms()) {
            Vector3d theAtomPosition = new Vector3d(theAtom);

            theAtomPosition = AngleCalculator.rotatePosition(theAtomPosition, theCenter, new Vector3d(0.0, 0.0, 1.0), theXYDegreeAngle);
            theAtomPosition = AngleCalculator.rotatePosition(theAtomPosition, theCenter, new Vector3d(1.0, 0.0, 0.0), theYZDegreeAngle);
            theAtomPosition = AngleCalculator.rotatePosition(theAtomPosition, theCenter, new Vector3d(0.0, 1.0, 0.0), theZXDegreeAngle);

            theAtom.getPoint3d().set(theAtomPosition.getX(), theAtomPosition.getY(), theAtomPosition.getZ());
        }
    }
    
    public static IAtomContainer rotateByCenter(IAtomContainer theMolecule, Vector3d theRotatedDegreeAngleVector) {
        return MoleculeManipulator.rotateByCenter(theMolecule, theRotatedDegreeAngleVector.getZ(), theRotatedDegreeAngleVector.getX(),
                theRotatedDegreeAngleVector.getY());
    }

    public static IAtomContainer rotateByCenter(IAtomContainer theMolecule, Double theXYDegreeAngle, Double theYZDegreeAngle, Double theZXDegreeAngle) {
        try {
            Vector3d theCenter = new Vector3d(GeometryTools.get3DCenter(theMolecule));
            IAtomContainer theCopiedMolecule = theMolecule.clone();

            for (IAtom theAtom : theCopiedMolecule.atoms()) {
                Vector3d theAtomPosition = new Vector3d(theAtom);

                theAtomPosition = AngleCalculator.rotatePosition(theAtomPosition, theCenter, new Vector3d(0.0, 0.0, 1.0), theXYDegreeAngle);
                theAtomPosition = AngleCalculator.rotatePosition(theAtomPosition, theCenter, new Vector3d(1.0, 0.0, 0.0), theYZDegreeAngle);
                theAtomPosition = AngleCalculator.rotatePosition(theAtomPosition, theCenter, new Vector3d(0.0, 1.0, 0.0), theZXDegreeAngle);

                theAtom.getPoint3d().set(theAtomPosition.getX(), theAtomPosition.getY(), theAtomPosition.getZ());
            }

            return theCopiedMolecule;
        } catch (CloneNotSupportedException ex) {
            Exceptions.printStackTrace(ex);
        }

        return null;
    }
    
    public static IAtomContainer rotateMolecule(IAtomContainer theMolecule, Matrix theRotationMatrix, Vector3d theCenterPosition) {
        try {
            IAtomContainer theCopiedMolecule = theMolecule.clone();

            for (IAtom theAtom : theCopiedMolecule.atoms()) {
                Vector3d theAtomPosition = new Vector3d(theAtom);

                theAtomPosition = AngleCalculator.rotatePosition(theAtomPosition, theRotationMatrix, theCenterPosition);
                
                theAtom.getPoint3d().set(theAtomPosition.getX(), theAtomPosition.getY(), theAtomPosition.getZ());
            }

            return theCopiedMolecule;
        } catch (CloneNotSupportedException ex) {
            Exceptions.printStackTrace(ex);
        }

        return null;
    }
}
