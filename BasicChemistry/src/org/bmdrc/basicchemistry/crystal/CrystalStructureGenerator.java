/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.crystal;

import java.io.Serializable;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Crystal Structure generator
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 11
 */
public class CrystalStructureGenerator implements Serializable {

    private static final long serialVersionUID = -4857032290804937557L;

    //constant Integer variable
    private static final int ALPHA_INDEX = 0;
    private static final int BETA_INDEX = 1;
    private static final int GAMMA_INDEX = 2;
    //constant String variable
    public static final String MOLECULE_POSITION_KEY = "Molecule position";

    /**
     * Generate Crystal Structure set<br>
     * This function need to cell dimension
     *
     * @param theMolecule      template molecule
     * @param theCellDimension cell dimension
     * @param theAngleVector   Degree Angle vector
     * @return Crystal structure set having space group name
     */
    public static IAtomContainerSet generateCrystalStructureSet(IAtomContainer theMolecule, Vector3d theCellDimension, Vector3d theAngleVector) {
        IAtomContainer theFractionCoordinateMolecule = CrystalStructureGenerator.generateMoleculeByFractionCoordinate(theMolecule, theCellDimension, theAngleVector);
        IAtomContainerSet theResultMoleculeSet = new AtomContainerSet();

        for (SpaceGroup theGroup : SpaceGroup.values()) {
            try {
                IAtomContainer theCrystalStructure = theGroup.generateCrystalStructure((IAtomContainer) theFractionCoordinateMolecule.clone());

                theResultMoleculeSet.addAtomContainer(CrystalStructureGenerator.generateMoleculeByOriginalCoordinate(theCrystalStructure, theCellDimension, theAngleVector));
            } catch (CloneNotSupportedException ex) {
                System.err.println("Molecule clone error");
            }
        }

        return theResultMoleculeSet;
    }

    public static Vector generateAtomPositionVectorByFractionCoordinate(Vector theVector, CrystalInformation theCrystalInformation) {
        return CrystalStructureGenerator.generateAtomPositionVectorByFractionCoordinate(theVector, theCrystalInformation.getCellDimensionVector(), theCrystalInformation.getCoordinateAngleVector());
    }

    public static Vector generateAtomPositionVectorByFractionCoordinate(Vector theVector, Vector3d theCellDimension, Vector3d theAngleVector) {
        if (theVector.size() % 3 != 0) {
            System.err.println("Crystal Structure Generator Error in \"generateMoleculeByFractionCoordinate\" method");
            return null;
        }

        Vector theResultVector = new Vector();
        theAngleVector = CrystalStructureGenerator.__convertRadianVector(theAngleVector);
        double theAlpha = theAngleVector.get(CrystalStructureGenerator.ALPHA_INDEX);
        double theBeta = theAngleVector.get(CrystalStructureGenerator.BETA_INDEX);
        double theGamma = theAngleVector.get(CrystalStructureGenerator.GAMMA_INDEX);
        double theV = Math.sqrt(1.0 - Math.pow(Math.cos(theAlpha), 2.0) - Math.pow(Math.cos(theBeta), 2.0) - Math.pow(Math.cos(theGamma), 2.0)
                + 2.0 * Math.cos(theAlpha) * Math.cos(theBeta) * Math.cos(theGamma));

        for (int ii = 0, iEnd = theVector.size() / 3; ii < iEnd; ii++) {
            Vector3d theAtomPosition = (Vector3d) theVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii + 1) * Constant.POSITION_DIMENSION_SIZE);

            theAtomPosition.setX((theAtomPosition.getX() - theAtomPosition.getY() * Math.cos(theGamma) / Math.sin(theGamma) + theAtomPosition.getZ() * (Math.cos(theAlpha) * Math.cos(theGamma) - Math.cos(theBeta))
                    / (theV * Math.sin(theGamma))) / theCellDimension.getX());
            theAtomPosition.setY((theAtomPosition.getY() / Math.sin(theGamma) + theAtomPosition.getZ() * (Math.cos(theBeta) * Math.cos(theGamma) - Math.cos(theAlpha)) / (theV * Math.sin(theGamma)))
                    / theCellDimension.getY());
            theAtomPosition.setZ(theAtomPosition.getZ() * Math.sin(theGamma) / (theCellDimension.getZ() * theV));

            theResultVector.addAll(theAtomPosition);
        }

        return theResultVector;
    }

    /**
     * generate fraction coordinate<br>
     * save coordinate to form IAtomContainer
     *
     * @param theMolecule      template molecule
     * @param theCellDimension Cell dimension to form (x,y,z) = (a,b,c)
     * @param theAngleVector   Degree Angle vector
     * @return Molecule converting original coordinate to fraction coordinate
     */
    public static IAtomContainer generateMoleculeByFractionCoordinate(IAtomContainer theMolecule, Vector3d theCellDimension, Vector3d theAngleVector) {
        try {
            IAtomContainer theCopiedMolecule = (IAtomContainer) theMolecule.clone();

            theAngleVector = CrystalStructureGenerator.__convertRadianVector(theAngleVector);
            double theAlpha = theAngleVector.get(CrystalStructureGenerator.ALPHA_INDEX);
            double theBeta = theAngleVector.get(CrystalStructureGenerator.BETA_INDEX);
            double theGamma = theAngleVector.get(CrystalStructureGenerator.GAMMA_INDEX);
            double theV = Math.sqrt(1.0 - Math.pow(Math.cos(theAlpha), 2.0) - Math.pow(Math.cos(theBeta), 2.0) - Math.pow(Math.cos(theGamma), 2.0)
                    + 2.0 * Math.cos(theAlpha) * Math.cos(theBeta) * Math.cos(theGamma));

            for (IAtom theAtom : theCopiedMolecule.atoms()) {
                Vector3d theAtomPosition = new Vector3d(theAtom);

                theAtomPosition.setX((theAtomPosition.getX() - theAtomPosition.getY() * Math.cos(theGamma) / Math.sin(theGamma) + theAtomPosition.getZ() * (Math.cos(theAlpha) * Math.cos(theGamma) - Math.cos(theBeta))
                        / (theV * Math.sin(theGamma))) / theCellDimension.getX());
                theAtomPosition.setY((theAtomPosition.getY() / Math.sin(theGamma) + theAtomPosition.getZ() * (Math.cos(theBeta) * Math.cos(theGamma) - Math.cos(theAlpha)) / (theV * Math.sin(theGamma)))
                        / theCellDimension.getY());
                theAtomPosition.setZ(theAtomPosition.getZ() * Math.sin(theGamma) / (theCellDimension.getZ() * theV));

                theAtom.setPoint3d(theAtomPosition.toPoint3d());
            }

            return theCopiedMolecule;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule copied error in get molecule by fraction coordinate at fraction coordinate generator");
        }

        return null;
    }

    /**
     * Generate molecule by orignal coordintate<br>
     * This function covert fraction coordinate to original coordinate
     *
     * @param theFractionCoordinateMolecule template molecule having fraction
     *                                      coordinate
     * @param theCellDimension              Crystal cell dimension vector
     * @param theAngleVector                Degree Angle Vector
     * @return molecule converting fraction coordinate to original coordinate
     */
    public static IAtomContainer generateMoleculeByOriginalCoordinate(IAtomContainer theFractionCoordinateMolecule, Vector3d theCellDimension, Vector3d theAngleVector) {
        try {
            IAtomContainer theCopiedMolecule = (IAtomContainer) theFractionCoordinateMolecule.clone();

            theAngleVector = CrystalStructureGenerator.__convertRadianVector(theAngleVector);
            double theAlpha = theAngleVector.get(CrystalStructureGenerator.ALPHA_INDEX);
            double theBeta = theAngleVector.get(CrystalStructureGenerator.BETA_INDEX);
            double theGamma = theAngleVector.get(CrystalStructureGenerator.GAMMA_INDEX);

            for (IAtom theAtom : theCopiedMolecule.atoms()) {
                Vector3d theAtomPosition = new Vector3d(theAtom);

                theAtomPosition.setX(theAtomPosition.getX() * theCellDimension.getX() + theAtomPosition.getY() * theCellDimension.getY() * Math.cos(theGamma)
                        + theAtomPosition.getZ() * theCellDimension.getZ() * Math.cos(theBeta));
                theAtomPosition.setY(theAtomPosition.getY() * theCellDimension.getY() * Math.sin(theGamma) + theAtomPosition.getZ() * theCellDimension.getZ()
                        * (Math.cos(theAlpha) - Math.cos(theBeta) * Math.cos(theGamma)) / Math.sin(theGamma));
                theAtomPosition.setZ(theAtomPosition.getZ() * theCellDimension.getZ() * Math.sqrt(1.0 - Math.pow(Math.cos(theAlpha), 2.0) - Math.pow(Math.cos(theBeta), 2.0)
                        - Math.pow(Math.cos(theGamma), 2.0) + 2.0 * Math.cos(theAlpha) * Math.cos(theBeta) * Math.cos(theGamma)) / Math.sin(theGamma));

                theAtom.setPoint3d(theAtomPosition.toPoint3d());
            }

            return theCopiedMolecule;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule copied error in get molecule by fraction coordinate at fraction coordinate generator");
        }

        return null;
    }

    private static Vector3d __convertRadianVector(Vector3d theAngleVector) {
        Vector3d theVector3d = new Vector3d();

        if (theAngleVector == null) {
            theAngleVector = new Vector3d(90.0, 90.0, 90.0);
        }

        for (int ai = 0, aEnd = theAngleVector.size(); ai < aEnd; ai++) {
            theVector3d.set(ai, Math.toRadians(theAngleVector.get(ai)));
        }

        return theVector3d;
    }

    public static IAtomContainer generateCrystalCell(IAtomContainer theFractionCoordinateMolecule, CrystalInformation theCrystalInformation) {
        return CrystalStructureGenerator.generateCrystalCell(theFractionCoordinateMolecule, theCrystalInformation.getCellDimensionVector(), theCrystalInformation.getCoordinateAngleVector());
    }

    public static Vector generateCrystalCellVectorByFractionCoordinate(Vector theFractionCoordinateVectorByUnitMolecule) {
        Vector theCrystalCellVector = new Vector();

        for (double xi = -1.0; xi <= 1.0; xi += 1.0) {
            for (double yi = -1.0; yi <= 1.0; yi += 1.0) {
                for (double zi = -1.0; zi <= 1.0; zi += 1.0) {
                    for(int ii = 0, iEnd = theFractionCoordinateVectorByUnitMolecule.size() / 3; ii < iEnd; ii++) {
                        Vector3d thePosition = (Vector3d)theFractionCoordinateVectorByUnitMolecule.subVector(ii * Constant.POSITION_DIMENSION_SIZE, 
                                (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                        theCrystalCellVector.add(thePosition.getX() + xi);
                        theCrystalCellVector.add(thePosition.getY() + yi);
                        theCrystalCellVector.add(thePosition.getZ() + zi);
                    }
                }
            }
        }

        return theCrystalCellVector;
    }

    public static Vector generateCrystalCellVectorByOriginalCoordinate(Vector theFractionCoordinateByUnitCell, CrystalInformation theCrystalInformation) {
        return CrystalStructureGenerator.generateCrystalCellVectorByOriginalCoordinate(theFractionCoordinateByUnitCell, theCrystalInformation.getCellDimensionVector(), theCrystalInformation.getCoordinateAngleVector());
    }

    public static Vector generateCrystalCellVectorByOriginalCoordinate(Vector theFractionCoordinateByUnitCell, Vector3d theCellDimension, Vector theAngleVector) {
        double theAlpha = theAngleVector.get(CrystalStructureGenerator.ALPHA_INDEX);
        double theBeta = theAngleVector.get(CrystalStructureGenerator.BETA_INDEX);
        double theGamma = theAngleVector.get(CrystalStructureGenerator.GAMMA_INDEX);
        Vector theCrystalCellVector = new Vector();

        for (double xi = -1.0; xi <= 1.0; xi += 1.0) {
            for (double yi = -1.0; yi <= 1.0; yi += 1.0) {
                for (double zi = -1.0; zi <= 1.0; zi += 1.0) {
                    for(int ii = 0, iEnd = theFractionCoordinateByUnitCell.size() / 3; ii < iEnd; ii++) {
                        Vector theAtomPosition = theFractionCoordinateByUnitCell.subVector(ii * Constant.POSITION_DIMENSION_SIZE, 
                                (ii+1) * Constant.POSITION_DIMENSION_SIZE);
                        double theX = theAtomPosition.get(Vector3d.X_INDEX) + xi;
                        double theY = theAtomPosition.get(Vector3d.Y_INDEX) + yi;
                        double theZ = theAtomPosition.get(Vector3d.Z_INDEX) + zi;

                        theAtomPosition.set(Vector3d.X_INDEX, theX * theCellDimension.getX() + theY * theCellDimension.getY() * Math.cos(theGamma) + theZ * theCellDimension.getZ() * Math.cos(theBeta));
                        theAtomPosition.set(Vector3d.Y_INDEX, theY * theCellDimension.getY() * Math.sin(theGamma) + theZ * theCellDimension.getZ()
                                * (Math.cos(theAlpha) - Math.cos(theBeta) * Math.cos(theGamma)) / Math.sin(theGamma));
                        theAtomPosition.set(Vector3d.Z_INDEX, theZ * theCellDimension.getZ() * Math.sqrt(1.0 - Math.pow(Math.cos(theAlpha), 2.0) - Math.pow(Math.cos(theBeta), 2.0)
                                - Math.pow(Math.cos(theGamma), 2.0) + 2.0 * Math.cos(theAlpha) * Math.cos(theBeta) * Math.cos(theGamma)) / Math.sin(theGamma));

                        theCrystalCellVector.addAll(theAtomPosition);
                    }
                }
            }
        }

        return theCrystalCellVector;
    }

    /**
     * generate crsytal cell<br>
     * this crystal cell is located in 3X3X3 cell
     *
     * @param theFractionCoordinateMolecule
     * @param theCellDimension
     * @param theAngleVector
     * @return
     */
    public static IAtomContainer generateCrystalCell(IAtomContainer theFractionCoordinateMolecule, Vector3d theCellDimension, Vector3d theAngleVector) {
        IAtomContainer theCrystalCell = new AtomContainer();

        theCrystalCell.add(CrystalStructureGenerator.__getMoveCell(theFractionCoordinateMolecule, theCellDimension, theAngleVector, new Vector3d(0.0, 0.0, 0.0)));

        for (double xi = -1.0; xi <= 1.0; xi += 1.0) {
            for (double yi = -1.0; yi <= 1.0; yi += 1.0) {
                for (double zi = -1.0; zi <= 1.0; zi += 1.0) {
                    if (xi != 0.0 || yi != 0.0 || zi != 0.0) {
                        theCrystalCell.add(CrystalStructureGenerator.__getMoveCell(theFractionCoordinateMolecule, theCellDimension, theAngleVector, new Vector3d(xi, yi, zi)));
                    }
                }
            }
        }

        return theCrystalCell;
    }

    private static IAtomContainer __getMoveCell(IAtomContainer theMoleculeByOriginalCoordinate, Vector3d theCellDimension, Vector3d theAngleVector, Vector3d thePositionVector) {
        try {
            IAtomContainer theCopiedMolecule = (IAtomContainer) theMoleculeByOriginalCoordinate.clone();
            theAngleVector = CrystalStructureGenerator.__convertRadianVector(theAngleVector);
            double theAlpha = theAngleVector.get(CrystalStructureGenerator.ALPHA_INDEX);
            double theBeta = theAngleVector.get(CrystalStructureGenerator.BETA_INDEX);
            double theGamma = theAngleVector.get(CrystalStructureGenerator.GAMMA_INDEX);

            for (IAtom theAtom : theCopiedMolecule.atoms()) {
                Vector3d theAtomPosition = new Vector3d(theAtom);
                double theX = theAtomPosition.getX() + thePositionVector.getX();
                double theY = theAtomPosition.getY() + thePositionVector.getY();
                double theZ = theAtomPosition.getZ() + thePositionVector.getZ();

                theAtomPosition.setX(theX * theCellDimension.getX() + theY * theCellDimension.getY() * Math.cos(theGamma) + theZ * theCellDimension.getZ() * Math.cos(theBeta));
                theAtomPosition.setY(theY * theCellDimension.getY() * Math.sin(theGamma) + theZ * theCellDimension.getZ()
                        * (Math.cos(theAlpha) - Math.cos(theBeta) * Math.cos(theGamma)) / Math.sin(theGamma));
                theAtomPosition.setZ(theZ * theCellDimension.getZ() * Math.sqrt(1.0 - Math.pow(Math.cos(theAlpha), 2.0) - Math.pow(Math.cos(theBeta), 2.0)
                        - Math.pow(Math.cos(theGamma), 2.0) + 2.0 * Math.cos(theAlpha) * Math.cos(theBeta) * Math.cos(theGamma)) / Math.sin(theGamma));
                theAtom.setPoint3d(theAtomPosition.toPoint3d());
                theAtom.setProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY, thePositionVector);
            }

            return theCopiedMolecule;
        } catch (CloneNotSupportedException ex) {
        }

        return null;
    }
}
