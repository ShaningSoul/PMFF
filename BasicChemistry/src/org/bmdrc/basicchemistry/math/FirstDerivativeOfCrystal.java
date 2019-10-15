/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.math;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import java.io.Serializable;
import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.crystal.CrystalStructureGenerator;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.ui.vector.Vector;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 12. 15
 */
public class FirstDerivativeOfCrystal implements Serializable {

    private static final long serialVersionUID = -917089015899229553L;

    private AbstractTotalForceFieldEnergyFunction itsEnergyFunction;
    private CrystalInformation itsCrystalInformation;
    private IAtomContainer itsMolecule;
    //constant Integer variable
    private final int DIMENSION_SIZE = 3;
    private final int X_INDEX = 0;
    private final int Y_INDEX = 1;
    private final int Z_INDEX = 2;
    //constant double variable
    private final double ZERO_VALUE = 0.0;
    private final double RACTANGLE_VALUE = Math.PI / 2.0;

    public FirstDerivativeOfCrystal(AbstractTotalForceFieldEnergyFunction theEnergyFunction) {
        this.itsEnergyFunction = theEnergyFunction;
    }

    public Vector calculateFristaDerivativeOfFractionCoordinateInGradient(IAtomContainer theFractionMolecule, CrystalInformation theCrystalInformation, 
            double theScalingFactor) {
        this.itsCrystalInformation = theCrystalInformation;
        this.itsMolecule = this.__getTotalMolecule(theFractionMolecule, theCrystalInformation);

        Vector theGradientVector = this.itsEnergyFunction.calculateGradientVector(this.itsMolecule, theScalingFactor);

        if (theCrystalInformation.getSpaceGroup().INDEX >= 3 && theCrystalInformation.getSpaceGroup().INDEX <= 15) {
            return this.__calculateFirstDerivativeOfFractionCoordinateInMonoclinic(theGradientVector);
        } else if (theCrystalInformation.getSpaceGroup().INDEX >= 16 && theCrystalInformation.getSpaceGroup().INDEX <= 142) {
            return this.__calculateFirstDerivativeOfFractionCoordinateInOrhorhombicAndTetragonal(theGradientVector);
        }

        return null;
    }

    public Vector calculateFristaDerivativeOfFractionCoordinateInConjugatedGradient(IAtomContainer theFractionMolecule, CrystalInformation theCrystalInformation,
            double theScalingFactor) {
        this.itsCrystalInformation = theCrystalInformation;
        this.itsMolecule = this.__getTotalMolecule(theFractionMolecule, theCrystalInformation);

        Vector theConjugatedGradientVector = this.itsEnergyFunction.calculateConjugatedGradientVector(this.itsMolecule, theScalingFactor);

        if (theCrystalInformation.getSpaceGroup().INDEX >= 3 && theCrystalInformation.getSpaceGroup().INDEX <= 15) {
            return this.__calculateFirstDerivativeOfFractionCoordinateInMonoclinic(theConjugatedGradientVector);
        } else if (theCrystalInformation.getSpaceGroup().INDEX >= 16 && theCrystalInformation.getSpaceGroup().INDEX <= 142) {
            return this.__calculateFirstDerivativeOfFractionCoordinateInOrhorhombicAndTetragonal(theConjugatedGradientVector);
        }

        return null;
    }

    private IAtomContainer __getTotalMolecule(IAtomContainer theFractionMolecule, CrystalInformation theCrystalInformation) {
        IAtomContainer theNewUnitCell = theCrystalInformation.getSpaceGroup().generateCrystalStructure(theFractionMolecule);
        IAtomContainer theNewCrystal = CrystalStructureGenerator.generateCrystalCell(theNewUnitCell, this.itsCrystalInformation);

        return theNewCrystal;
    }

    private Vector __calculateFirstDerivativeOfFractionCoordinateInMonoclinic(Vector theTargetVector) {
        int theUnitMoleculeAtomCount = this.itsEnergyFunction.getUnitMolecule().getAtomCount();
        Vector theVector = new Vector(theUnitMoleculeAtomCount * this.DIMENSION_SIZE);

        for (int fi = 0; fi < theUnitMoleculeAtomCount; fi++) {
            for (int si = 0, sEnd = this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE; si < sEnd; si++) {
                Vector theGradientVector = this.__convertFractionalVectorInMonoclinic(theTargetVector.subVector((theUnitMoleculeAtomCount * si + fi) * this.DIMENSION_SIZE,
                        (theUnitMoleculeAtomCount * si + fi + 1) * this.DIMENSION_SIZE));
                Vector theSymmetryVector = this.itsCrystalInformation.getSpaceGroup().generateIntraGradientVectorByMotherMolecule(theGradientVector, this.itsMolecule.getAtom(theUnitMoleculeAtomCount * si + fi));

                theVector.set(fi * this.DIMENSION_SIZE + this.X_INDEX, (Double) theVector.get(fi * this.DIMENSION_SIZE + this.X_INDEX)
                        + (Double) theSymmetryVector.get(this.X_INDEX));
                theVector.set(fi * this.DIMENSION_SIZE + this.Y_INDEX, (Double) theVector.get(fi * this.DIMENSION_SIZE + this.Y_INDEX)
                        + (Double) theSymmetryVector.get(this.Y_INDEX));
                theVector.set(fi * this.DIMENSION_SIZE + this.Z_INDEX, (Double) theVector.get(fi * this.DIMENSION_SIZE + this.Z_INDEX)
                        + (Double) theSymmetryVector.get(this.Z_INDEX));
            }
        }

        return theVector;
    }

    private Vector __convertFractionalVectorInMonoclinic(Vector theVector) {
        Vector theResultVector = new Vector(this.DIMENSION_SIZE);

        theResultVector.set(this.Y_INDEX, theVector.get(this.Y_INDEX) / this.itsCrystalInformation.getBInCellDimension());
        theResultVector.set(this.Z_INDEX, theVector.get(this.Z_INDEX) / (this.itsCrystalInformation.getCInCellDimension()
                * Math.sin(Math.toRadians(this.itsCrystalInformation.getBeta()))));
        theResultVector.set(this.X_INDEX, (theVector.get(this.X_INDEX) - (this.itsCrystalInformation.getCInCellDimension() * theResultVector.get(this.Z_INDEX)
                * Math.cos(Math.toRadians(this.itsCrystalInformation.getBeta())))) / this.itsCrystalInformation.getAInCellDimension());

        return theResultVector;
    }

    private Vector __calculateFirstDerivativeOfFractionCoordinateInOrhorhombicAndTetragonal(Vector theTargetVector) {
        int theUnitMoleculeAtomCount = this.itsEnergyFunction.getUnitMolecule().getAtomCount();
        Vector theVector = new Vector(theUnitMoleculeAtomCount * this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE * this.DIMENSION_SIZE);

        for (int si = 0, sEnd = this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE; si < sEnd; si++) {
            for (int fi = 0; fi < theUnitMoleculeAtomCount; fi++) {
                int theTargetAtomIndex = theUnitMoleculeAtomCount * si + fi;
                Vector theGradientVector = theTargetVector.subVector(theTargetAtomIndex * this.DIMENSION_SIZE, (theTargetAtomIndex + 1) * this.DIMENSION_SIZE);
                Vector theSymmetryVector = this.itsCrystalInformation.getSpaceGroup().generateIntraGradientVectorByMotherMolecule(theGradientVector, this.itsMolecule.getAtom(theUnitMoleculeAtomCount * si + fi));

                theVector.set(theTargetAtomIndex * this.DIMENSION_SIZE + this.X_INDEX, (Double) theVector.get(theTargetAtomIndex * this.DIMENSION_SIZE + this.X_INDEX)
                        + (Double) theSymmetryVector.get(this.X_INDEX) / this.itsCrystalInformation.getAInCellDimension());
                theVector.set(theTargetAtomIndex * this.DIMENSION_SIZE + this.Y_INDEX, (Double) theVector.get(theTargetAtomIndex * this.DIMENSION_SIZE + this.Y_INDEX)
                        + (Double) theSymmetryVector.get(this.Y_INDEX) / this.itsCrystalInformation.getBInCellDimension());
                theVector.set(theTargetAtomIndex * this.DIMENSION_SIZE + this.Z_INDEX, (Double) theVector.get(theTargetAtomIndex * this.DIMENSION_SIZE + this.Z_INDEX)
                        + (Double) theSymmetryVector.get(this.Z_INDEX) / this.itsCrystalInformation.getCInCellDimension());
            }
        }

        return theVector;
    }

    public CrystalInformation calculateFirstDerivativeOfCrystalInformationInGradient(Vector theFractionGradientVector, CrystalInformation theCrystalInformation) {
        this.itsCrystalInformation = theCrystalInformation;

        if (theCrystalInformation.getSpaceGroup().INDEX >= 3 && theCrystalInformation.getSpaceGroup().INDEX <= 15) {
            return this.__calculateFirstDerivativeOfCrystalInformationInMonoclinic(theFractionGradientVector);
        } else if (theCrystalInformation.getSpaceGroup().INDEX >= 16 && theCrystalInformation.getSpaceGroup().INDEX <= 142) {
            return this.__calculateFirstDerivativeOfCrystalInformationInOrthorhombicAndTetragonal(theFractionGradientVector);
        }

        return null;
    }

    /**
     * calculate first derivative of crystal information
     *
     * @param theMolecule molecule containing cartesian coordinate
     * @param theCrystalInformation crystal information contianing crystal cell
     * dimension lengths and angles
     * @param theGradientVectorInMolecule gradient vector
     * @param theSpaceGroup space group of crystal cell
     * @return crystal information containing first derivative value
     */
    public CrystalInformation calculateFirstDerivativeOfCrystalInformationInGradient(IAtomContainer theFractionMolecule, CrystalInformation theCrystalInformation, double theScalingFactor) {
        this.itsCrystalInformation = theCrystalInformation;
        this.itsMolecule = this.__getTotalMolecule(theFractionMolecule, theCrystalInformation);

        Vector theGradientVector = this.itsEnergyFunction.calculateGradientVector(this.itsMolecule, theScalingFactor);

        if (theCrystalInformation.getSpaceGroup().INDEX >= 3 && theCrystalInformation.getSpaceGroup().INDEX <= 15) {
            return this.__calculateFirstDerivativeOfCrystalInformationInMonoclinic(theGradientVector);
        } else if (theCrystalInformation.getSpaceGroup().INDEX >= 16 && theCrystalInformation.getSpaceGroup().INDEX <= 142) {
            return this.__calculateFirstDerivativeOfCrystalInformationInOrthorhombicAndTetragonal(theGradientVector);
        }

        return null;
    }

    public CrystalInformation calculateFirstDerivativeOfCrystalInformationInConjugatedGradient(IAtomContainer theFractionMolecule, CrystalInformation theCrystalInformation,
            double theScalingFactor) {
        this.itsCrystalInformation = theCrystalInformation;
        this.itsMolecule = this.__getTotalMolecule(theFractionMolecule, theCrystalInformation);

        Vector theConjugatedGradientVector = this.itsEnergyFunction.calculateConjugatedGradientVector(this.itsMolecule, theScalingFactor);

        if (theCrystalInformation.getSpaceGroup().INDEX >= 3 && theCrystalInformation.getSpaceGroup().INDEX <= 15) {
            return this.__calculateFirstDerivativeOfCrystalInformationInMonoclinic(theConjugatedGradientVector);
        } else if (theCrystalInformation.getSpaceGroup().INDEX >= 16 && theCrystalInformation.getSpaceGroup().INDEX <= 142) {
            return this.__calculateFirstDerivativeOfCrystalInformationInOrthorhombicAndTetragonal(theConjugatedGradientVector);
        }

        return null;
    }

    private CrystalInformation __calculateFirstDerivativeOfCrystalInformationInMonoclinic(Vector theTargetVector) {
        CrystalInformation theCrystalInformation = new CrystalInformation(this.itsCrystalInformation.getSpaceGroup());
        int theCrystalAtomCount = this.itsEnergyFunction.getUnitMolecule().getAtomCount() * this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE;

        for (int vi = 0, vEnd = theCrystalAtomCount; vi < vEnd; vi++) {
            Vector theGradientVector = theTargetVector.subVector(vi * this.DIMENSION_SIZE, (vi + 1) * this.DIMENSION_SIZE);
            Vector theSymmetryVector = this.itsCrystalInformation.getSpaceGroup().generateIntraGradientVectorByMotherMolecule(theGradientVector, this.itsMolecule.getAtom(vi));

            theCrystalInformation.setAInCellDimension(theCrystalInformation.getAInCellDimension()
                    + (Double) theGradientVector.get(this.X_INDEX) * this.itsMolecule.getAtom(vi).getPoint3d().x);
            theCrystalInformation.setBInCellDimension(theCrystalInformation.getBInCellDimension()
                    + (Double) theGradientVector.get(this.Y_INDEX) * this.itsMolecule.getAtom(vi).getPoint3d().y);
            theCrystalInformation.setCInCellDimension(theCrystalInformation.getCInCellDimension() + this.itsMolecule.getAtom(vi).getPoint3d().z
                    * ((Double) theGradientVector.get(this.X_INDEX) * Math.cos(Math.toRadians(this.itsCrystalInformation.getBeta()))
                    + (Double) theGradientVector.get(this.Z_INDEX) * Math.sin(Math.toRadians(this.itsCrystalInformation.getBeta()))));
            theCrystalInformation.setBeta(theCrystalInformation.getBeta() + this.itsCrystalInformation.getCInCellDimension()
                    * this.itsMolecule.getAtom(vi).getPoint3d().z
                    * ((Double) theGradientVector.get(this.Z_INDEX) * Math.cos(Math.toRadians(this.itsCrystalInformation.getBeta()))
                    - (Double) theGradientVector.get(this.X_INDEX) * Math.sin(Math.toRadians(this.itsCrystalInformation.getBeta()))));
        }

        theCrystalInformation.setBeta(Math.toDegrees(theCrystalInformation.getBeta()));

        return theCrystalInformation;
    }

    private CrystalInformation __calculateFirstDerivativeOfCrystalInformationInOrthorhombicAndTetragonal(Vector theTargetVector) {
        CrystalInformation theCrystalInformation = new CrystalInformation(this.itsCrystalInformation.getSpaceGroup());
        int theCrystalAtomCount = this.itsEnergyFunction.getUnitMolecule().getAtomCount() * this.itsCrystalInformation.getSpaceGroup().NUMBER_OF_MOLECULE;

        for (int vi = 0, vEnd = theCrystalAtomCount; vi < vEnd; vi++) {
            Vector theGradientVector = theTargetVector.subVector(vi * this.DIMENSION_SIZE, (vi + 1) * this.DIMENSION_SIZE);
            Vector theSymmetryVector = this.itsCrystalInformation.getSpaceGroup().generateIntraGradientVectorByMotherMolecule(theGradientVector, this.itsMolecule.getAtom(vi));

            theCrystalInformation.setAInCellDimension(theCrystalInformation.getAInCellDimension()
                    + (Double) theGradientVector.get(this.X_INDEX) * this.itsMolecule.getAtom(vi).getPoint3d().x);
            theCrystalInformation.setBInCellDimension(theCrystalInformation.getBInCellDimension()
                    + (Double) theGradientVector.get(this.Y_INDEX) * this.itsMolecule.getAtom(vi).getPoint3d().y);
            theCrystalInformation.setCInCellDimension(theCrystalInformation.getCInCellDimension()
                    + (Double) theGradientVector.get(this.Z_INDEX) * this.itsMolecule.getAtom(vi).getPoint3d().z);
        }

        return theCrystalInformation;
    }

    public Vector calculateMoleculeFractionCoordinateGradientVectorChangedOnCrystalInformation(IAtomContainer theMolecule,
            CrystalInformation theInitialCrystalInformation, CrystalInformation theVariableCrystalInformation) {
        if (theInitialCrystalInformation.equals(theVariableCrystalInformation)) {
            return new Vector(theMolecule.getAtomCount() * this.DIMENSION_SIZE);
        } else if (theInitialCrystalInformation.getSpaceGroup().INDEX >= 3 && theInitialCrystalInformation.getSpaceGroup().INDEX <= 15) {
            return this.__calculateMoleculeFractionCoordinateGradientVectorChangedOnCrystalInformationInMonoclinic(theMolecule, theInitialCrystalInformation, theVariableCrystalInformation);
        } else if (theInitialCrystalInformation.getSpaceGroup().INDEX >= 16 && theInitialCrystalInformation.getSpaceGroup().INDEX <= 142) {
            return this.__calculateMoleculeFractionCoordinateGradientVectorChangedOnCrystalInformationInOrhorhombicAndTetragonal(theMolecule, theInitialCrystalInformation,
                    theVariableCrystalInformation);
        }

        return null;
    }

    private Vector __calculateMoleculeFractionCoordinateGradientVectorChangedOnCrystalInformationInMonoclinic(IAtomContainer theMolecule,
            CrystalInformation theInitialCrystalInformation, CrystalInformation theVariableCrystalInformation) {
        Vector theGradientVector = new Vector();

        for (IAtom theAtom : theMolecule.atoms()) {
            Vector3d thePositionVector = new Vector3d(theAtom);
            Vector3d theNewPositionVector = new Vector3d();

            theNewPositionVector.setY(thePositionVector.getY() * theInitialCrystalInformation.getBInCellDimension()
                    / theVariableCrystalInformation.getBInCellDimension());
            theNewPositionVector.setZ(thePositionVector.getZ() * theInitialCrystalInformation.getCInCellDimension() * Math.cos(Math.toRadians(theInitialCrystalInformation.getBeta()))
                    / (theVariableCrystalInformation.getCInCellDimension() * Math.cos(Math.toRadians(theVariableCrystalInformation.getBeta()))));

            double theOriginalX = thePositionVector.getX() * theInitialCrystalInformation.getAInCellDimension()
                    + theInitialCrystalInformation.getCInCellDimension() * thePositionVector.getZ() * Math.sin(Math.toRadians(theInitialCrystalInformation.getBeta()));
            theNewPositionVector.setX(theOriginalX - theNewPositionVector.getZ() * theVariableCrystalInformation.getCInCellDimension() * Math.sin(Math.toRadians(theVariableCrystalInformation.getBeta())));
            theNewPositionVector.setX(theNewPositionVector.getX() / theVariableCrystalInformation.getAInCellDimension());

            theGradientVector.addAll(Vector3dCalculator.minus(theNewPositionVector, thePositionVector));
        }

        return theGradientVector;
    }

    private Vector __calculateMoleculeFractionCoordinateGradientVectorChangedOnCrystalInformationInOrhorhombicAndTetragonal(IAtomContainer theMolecule,
            CrystalInformation theInitialCrystalInformation, CrystalInformation theVariableCrystalInformation) {
        Vector3d theRatioVector = Vector3dCalculator.dividePerElement(theInitialCrystalInformation.getCellDimensionVector(),
                theVariableCrystalInformation.getCellDimensionVector());
        Vector theGradientVector = new Vector();

        for (IAtom theAtom : theMolecule.atoms()) {
            Vector3d thePositionVector = new Vector3d(theAtom);
            Vector3d theNewPositionVector = Vector3dCalculator.productPerElement(theRatioVector, thePositionVector);

            theGradientVector.addAll(Vector3dCalculator.minus(theNewPositionVector, thePositionVector));
        }

        return theGradientVector;
    }
}
