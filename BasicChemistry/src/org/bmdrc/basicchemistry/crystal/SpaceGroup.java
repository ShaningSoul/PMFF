/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.basicchemistry.crystal;

import java.util.HashMap;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.Bond;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

/**
 * Space group<br>
 * The space group is used to define crystal structure<br>
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 11
 */
public enum SpaceGroup {

    P2(3, "P2", 2) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z));
            }

            theResultMolecule.add(theMoleculeSet.getAtomContainer(0));
            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultVector.add(theGradientVector.get(this.X_INDEX));
                    theResultVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultVector.add((Double) theGradientVector.get(this.Y_INDEX));
                    theResultVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultVector.add(theGradientVector.get(this.X_INDEX));
                    theResultVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultVector.add((Double) theGradientVector.get(this.Y_INDEX));
                    theResultVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultVector;
        }
    },
    P21(4, "P21", 2) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y + 0.5, -thePosition.z));
            }

            theResultMolecule.add(theMoleculeSet.getAtomContainer(0));
            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();
            Vector3d thePositionVector = (Vector3d) theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY);

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 1:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2.0 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2.0 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2.0 * thePositionVector.getX())));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    C2(5, "C2", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, thePosition.y + 0.5, thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, thePosition.y + 0.5, -thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();
            Vector3d thePositionVector = (Vector3d) theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY);

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    break;
                case 1:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2.0 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2.0 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2.0 * thePositionVector.getZ())));
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2.0 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2.0 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2.0 * thePositionVector.getZ())));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2.0 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2.0 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2.0 * thePositionVector.getZ())));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    PM(6, "PM", 2) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, thePosition.z));
            }

            theResultMolecule.add(theMoleculeSet.getAtomContainer(0));
            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    PC(7, "PC", 2) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, thePosition.z + 0.5));
            }

            theResultMolecule.add(theMoleculeSet.getAtomContainer(0));
            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    CM(8, "CM", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, thePosition.z));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, thePosition.y + 0.5, thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    CC(9, "CC", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, thePosition.y + 0.5, thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, thePosition.z + 0.5));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    P2_M(10, "P2/M", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y + 0.5, thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    P21_M(11, "P21/M", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y + 0.5, -thePosition.z));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y + 0.5, thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    C2_M(12, "C2/M", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    P2_C(13, "P2/C", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, thePosition.z + 0.5));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    P21_C(14, "P21/C", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y + 0.5, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y + 0.5, thePosition.z + 0.5));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.P21_C.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();
            Vector3d thePositionVector = (Vector3d) theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY);

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 1:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    P21_A(14, "P21/A", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, thePosition.y + 0.5, -thePosition.z));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.P21_A.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();
            Vector3d thePositionVector = (Vector3d) theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY);

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 - (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 - (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 - (2 * thePositionVector.getZ())));
                    break;
                case 1:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 - (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 - (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 - (2 * thePositionVector.getZ())));
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 - (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 - (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 - (2 * thePositionVector.getZ())));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 - (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 - (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 - (2 * thePositionVector.getZ())));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    C2_C(15, "C2/C", 8) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 4);
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 5);
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 6);
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 7);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, thePosition.y + 0.5, thePosition.z));
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, thePosition.y + 0.5, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, -thePosition.y + 0.5, -thePosition.z));
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, thePosition.z + 0.5));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }


            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 4:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 5:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 6:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 7:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 4:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 5:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 6:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 7:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    P222(16, "P222", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, thePosition.z));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, -thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    P2221(17, "P2221", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, -thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    P21212(18, "P21212", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, thePosition.z));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, thePosition.y + 0.5, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, -thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();
            Vector3d thePositionVector = (Vector3d) theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY);

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 1:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    P212121(19, "P212121", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, -thePosition.y, thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y + 0.5, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, -thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();
            
            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();
            Vector3d thePositionVector = (Vector3d) theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY);

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 1:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2 * thePositionVector.getZ())));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    C2221(20, "C2221", 8) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 4);
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 5);
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 6);
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 7);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, thePosition.y + 0.5, thePosition.z));
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, -thePosition.y + 0.5, thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, thePosition.y + 0.5, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, -thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 4:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 5:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 6:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 7:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 4:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 5:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 6:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX) + 0.5);
                    break;
                case 7:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    C222(21, "C222", 8) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 4);
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 5);
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 6);
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 7);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, thePosition.z));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, thePosition.y + 0.5, thePosition.z));
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, -thePosition.y + 0.5, thePosition.z));
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, thePosition.y + 0.5, -thePosition.z));
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, -thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 4:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 5:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 6:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 7:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 4:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 5:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 6:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 7:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) + 0.5);
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    PCA21(29, "PCA21", 4) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y, thePosition.z));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, thePosition.y, thePosition.z + 0.5));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();
            Vector3d thePositionVector = (Vector3d) theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY);

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * thePositionVector.getX());
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * thePositionVector.getY());
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * thePositionVector.getZ());
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX) * thePositionVector.getX());
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) * thePositionVector.getY());
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (0.5 + thePositionVector.getZ()));
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (0.5 + thePositionVector.getX()));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX) * thePositionVector.getY());
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * thePositionVector.getZ());
                    break;
                case 3:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX) * (0.5 + thePositionVector.getX()));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * thePositionVector.getY());
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (0.5 + thePositionVector.getZ()));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    },
    PBCA(61, "Pbca", 8) {
        @Override
        public IAtomContainer generateCrystalStructure(IAtomContainer theMolecule) {
            IAtomContainerSet theMoleculeSet = this._initializeMoleculeSet(theMolecule);
            IAtomContainer theResultMolecule = new AtomContainer();

            this._initializeMolecule(theMolecule);
            theResultMolecule.add(theMolecule);

            for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theMolecule.getAtom(ai).getPoint3d();

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, ai);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 1);
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 2);
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 3);
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 4);
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 5);
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 6);
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 7);

                theMoleculeSet.getAtomContainer(0).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, -thePosition.y, thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(1).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, thePosition.y + 0.5, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(2).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, -thePosition.y + 0.5, -thePosition.z));
                theMoleculeSet.getAtomContainer(3).getAtom(ai).setPoint3d(new Point3d(-thePosition.x, -thePosition.y, -thePosition.z));
                theMoleculeSet.getAtomContainer(4).getAtom(ai).setPoint3d(new Point3d(thePosition.x + 0.5, thePosition.y, -thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(5).getAtom(ai).setPoint3d(new Point3d(thePosition.x, -thePosition.y + 0.5, thePosition.z + 0.5));
                theMoleculeSet.getAtomContainer(6).getAtom(ai).setPoint3d(new Point3d(-thePosition.x + 0.5, thePosition.y + 0.5, thePosition.z));
            }

            for (IAtomContainer theCrystalMolecule : theMoleculeSet.atomContainers()) {
                theResultMolecule.add(theCrystalMolecule);
            }

            theResultMolecule.setProperty(SpaceGroup.SPACE_GROUP_KEY, this.NAME);

            return theResultMolecule;
        }

        @Override
        public Vector generateCrystalStructure(Vector theFractionCoordinateVector) {
            Vector theResultVector = new Vector(theFractionCoordinateVector);

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX));
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(thePosition.get(Vector3d.X_INDEX));
                theResultVector.add(-thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX) + 0.5);
            }

            for(int ii = 0, iEnd = theFractionCoordinateVector.size() / 3; ii < iEnd; ii++) {
                Vector thePosition = theFractionCoordinateVector.subVector(ii * Constant.POSITION_DIMENSION_SIZE, (ii+1) * Constant.POSITION_DIMENSION_SIZE);

                theResultVector.add(-thePosition.get(Vector3d.X_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Y_INDEX) + 0.5);
                theResultVector.add(thePosition.get(Vector3d.Z_INDEX));
            }

            return theResultVector;
        }

        @Override
        public Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 1:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 2:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 4:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 5:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Z_INDEX));
                    break;
                case 6:
                    theResultGradientVector.add(theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                case 7:
                    theResultGradientVector.add(-(Double) theGradientVector.get(this.X_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Y_INDEX));
                    theResultGradientVector.add(theGradientVector.get(this.Z_INDEX));
                    break;
                default:
            }

            return theResultGradientVector;
        }

        @Override
        public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom) {
            Vector theResultGradientVector = new Vector();
            Vector3d thePositionVector = (Vector3d) theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY);

            switch ((Integer) theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY)) {
                case 0:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 1:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 2:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 3:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 4:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 5:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (-1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 6:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (-1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                case 7:
                    theResultGradientVector.add((Double) theGradientVector.get(this.X_INDEX) * (-1.0 + (2 * thePositionVector.getX())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Y_INDEX) * (1.0 + (2 * thePositionVector.getY())));
                    theResultGradientVector.add((Double) theGradientVector.get(this.Z_INDEX) * (1.0 + (2 * thePositionVector.getZ())));
                    break;
                default:
            }

            return theResultGradientVector;
        }
    };

    public int INDEX;
    public String NAME;
    public int NUMBER_OF_MOLECULE;
    //constant Integer variable
    protected final int FIRST_INDEX = 0;
    protected final int X_INDEX = 0;
    protected final int Y_INDEX = 1;
    protected final int Z_INDEX = 2;
    protected final int DIMENSION_SIZE = 3;
    protected final int A_MOVED_MOLECULE_INDEX = 3;
    protected final int B_MOVED_MOLECULE_INDEX = 2;
    protected final int C_MOVED_MOLECULE_INDEX = 1;
    protected final int ORIGINAL_MOLECULE_INDEX = 0;
    //constant String variable
    public static final String SPACE_GROUP_KEY = "Space group";
    public static final String MOLECULE_NUMBER_KEY = "Space group molecule number";
    public static final String ORIGINAL_ATOM_NUMBER = "Original atom number";

    /**
     * Constructor
     *
     * @param theIndex Space group index
     * @param theName Space group name
     * @param theNumberOfMolecule Number of molecule in space group
     */
    private SpaceGroup(int theIndex, String theName, int theNumberOfMolecule) {
        this.INDEX = theIndex;
        this.NAME = theName;
        this.NUMBER_OF_MOLECULE = theNumberOfMolecule;
    }

    /**
     * Generate crystal structure using fraction coordinate<br>
     * The IAtomContainer variable must have fraction coordinate, not original
     * coordinate
     *
     * @param theMolecule template molecule having fraction coordinate
     * @return Crystal structure to form IAtomContainer
     */
    public abstract IAtomContainer generateCrystalStructure(IAtomContainer theMolecule);

    public abstract Vector generateCrystalStructure(Vector thePositionVector);
    /**
     * generate gradient vector by mother molecule at fix cell dimension length
     *
     * @param theGradientVector Gradient vector at mother molecule
     * @param theAtom template atom
     * @return gradient vector at template atom
     */
    public abstract Vector generateIntraGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom);

    /**
     * generate gradient vector by mother molecule
     *
     * @param theGradientVector Gradient vector at mother molecule
     * @param theAtom template atom
     * @return gradient vector at template atom
     */
    public abstract Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom);

    /**
     * generate gradient vector by mother molecule
     *
     * @param theGradientVector Gradient vector at mother molecule
     * @param theAtom template atom
     * @param theCellPositionVector submolecule cell position vector
     * @return gradientvector at template atom
     */
    public Vector generateInterGradientVectorByMotherMolecule(Vector theGradientVector, IAtom theAtom, Vector3d theCellPositionVector) {
        theAtom.setProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY, theCellPositionVector);

        return this.generateInterGradientVectorByMotherMolecule(theGradientVector, theAtom);
    }

    /**
     * initialize molecule set used to save each molecules in crystal structure
     *
     * @param theMolecule template molecule
     * @return IAtomContainerSet having size of molecule in crystal structure
     */
    protected IAtomContainerSet _initializeMoleculeSet(IAtomContainer theMolecule) {
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();

        for (int mi = 0, mEnd = this.NUMBER_OF_MOLECULE - 1; mi < mEnd; mi++) {
            try {
                theMoleculeSet.addAtomContainer((IAtomContainer) theMolecule.clone());
            } catch (CloneNotSupportedException ex) {
                System.err.println("molecule clone error in space group");
            }
        }

        return theMoleculeSet;
    }

    /**
     * initialize molecule<br>
     * input original atom number and molecule number
     *
     * @param theMolecule template molecule
     */
    protected void _initializeMolecule(IAtomContainer theMolecule) {
        int theIndex = 0;

        for (IAtom theAtom : theMolecule.atoms()) {
            theAtom.setProperty(SpaceGroup.ORIGINAL_ATOM_NUMBER, theIndex++);
            theAtom.setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, 0);
        }
    }

    private IAtomContainerSet __splitMolecule(IAtomContainer theResultMolecule, IAtomContainer theOriginalMolecule) {
        int theOriginalAtomCount = theOriginalMolecule.getAtomCount();
        int theResultAtomCount = theResultMolecule.getAtomCount();
        IAtomContainerSet theMoleculeSet = this.__initializeAtomContainerSetInSplitMolecule(theResultAtomCount / theOriginalAtomCount);

        for (int ai = 0, aEnd = theResultMolecule.getAtomCount(); ai < aEnd; ai++) {
            IAtom theAtom = theResultMolecule.getAtom(ai);

            theMoleculeSet.getAtomContainer(ai / theOriginalAtomCount).addAtom(theAtom);
            theMoleculeSet.getAtomContainer(ai / theOriginalAtomCount).setProperties(new HashMap<>(theResultMolecule.getProperties()));
            theMoleculeSet.getAtomContainer(ai / theOriginalAtomCount).setProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY, theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY));
            theMoleculeSet.getAtomContainer(ai / theOriginalAtomCount).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY));
        }

        for (IBond theBond : theResultMolecule.bonds()) {
            int theFirstAtomIndex = theResultMolecule.getAtomNumber(theBond.getAtom(0));
            int theSecondAtomIndex = theResultMolecule.getAtomNumber(theBond.getAtom(1));
            int theMoleculeNumber = theFirstAtomIndex / theOriginalAtomCount;
            IAtomContainer theMolecule = theMoleculeSet.getAtomContainer(theMoleculeNumber);

            theMolecule.addBond(new Bond(theMolecule.getAtom(theFirstAtomIndex % theOriginalAtomCount), theMolecule.getAtom(theSecondAtomIndex % theOriginalAtomCount), theBond.getOrder()));
        }

        return theMoleculeSet;
    }

    private IAtomContainerSet __initializeAtomContainerSetInSplitMolecule(int theMoleculeCount) {
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();

        for (int mi = 0; mi < theMoleculeCount; mi++) {
            theMoleculeSet.addAtomContainer(new AtomContainer());
        }

        return theMoleculeSet;
    }

    private CrystalInformation __roundPointThreePosition(CrystalInformation theCrystalInformation) {
        double theX = (double) Math.round(theCrystalInformation.getAInCellDimension() * 1000.0) / 1000.0;
        double theY = (double) Math.round(theCrystalInformation.getBInCellDimension() * 1000.0) / 1000.0;
        double theZ = (double) Math.round(theCrystalInformation.getCInCellDimension() * 1000.0) / 1000.0;
        Vector3d theCellDimensionConstant = new Vector3d(theX, theY, theZ);

        return new CrystalInformation(this, theCellDimensionConstant, null);
    }

    private double __round(double theValue) {
        return (double) Math.round(theValue * 1000.0) / 1000.0;
    }

    public static SpaceGroup getSpaceGroup(String theSpaceGroupName) {
        for (SpaceGroup theGroup : SpaceGroup.values()) {
            if (theGroup.NAME.equals(theSpaceGroupName)) {
                return theGroup;
            }
        }

        return null;
    }
}
