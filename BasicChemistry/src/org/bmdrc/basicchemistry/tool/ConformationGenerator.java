/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.tool;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.util.ConformationVariable;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.bmdrc.basicchemistry.vector.AngleGradientVector;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.basicchemistry.writer.SDFWriter;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * generate conformation<br>
 * the gaussian format can write conformation using step method<br>
 * this class is implemented in that method
 * 
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 15
 */
public class ConformationGenerator implements Serializable {
    private static final long serialVersionUID = 8762401457710462202L;

    private IAtomContainer itsMolecule;
    private IAtomContainerSet itsMoleculeSet;
    private String itsResultDir;
    private Integer itsNumberOfTemp;
    private MoveTogetherAtomNumberList itsMoveTogetherAtomNumberMap;
    //constant Integer variable
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    private final int THIRD_INDEX = 2;
    private final int FOURTH_INDEX = 3;
    private final int MAXIMUM_NUMBER_OF_MOLECULE = 5000;
    //constant String variable
    private final String TEMP_FILE_NAME = "Temp_";
    private final String SDF_SUFFIX = ".sdf";

    /**
     * Constructor
     */
    public ConformationGenerator() {
        this.itsNumberOfTemp = 0;
        this.itsMoleculeSet = new AtomContainerSet();
    }

    /**
     * Constructor to use defined molecule
     * 
     * @param theMolecule IAtomContainer variable
     */
    public ConformationGenerator(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
        this.itsNumberOfTemp = 0;
        this.itsMoleculeSet = new AtomContainerSet();
    }

    /**
     * generate conformation only using rotation bond and specific step<br>
     * 
     * @param theMolecule template molecule
     * @param theConformationVariableList conformation variable list
     * @param theStepList speicific step by conformation variable
     * @return conformation 
     */
    public IAtomContainer generateConformationUsingOnlyAtomRotationAtSpecificStep(IAtomContainer theMolecule, List<ConformationVariable> theConformationVariableList, 
            List<Integer> theStepList) {
        try {
            IAtomContainer theCopiedMolecule = (IAtomContainer) theMolecule.clone();

            this.itsMoveTogetherAtomNumberMap = new MoveTogetherAtomNumberList(theCopiedMolecule);
            this.itsMoleculeSet = new AtomContainerSet();

            for (int vi = 0, vEnd = theConformationVariableList.size(); vi < vEnd; vi++) {
                theCopiedMolecule = this.__generateConformationUsingOnlyAtomRotation(theCopiedMolecule, theConformationVariableList.get(vi), theConformationVariableList.get(vi).getInterval() * (theStepList.get(vi) + 1));
            }

            return theCopiedMolecule;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule Clone Error!!");
        }

        return null;
    }

    /**
     * generate conformation only using rotation bond
     * 
     * @param theMolecule template molecule
     * @param theConformationVariableList conformation variable list
     * @param theResultDir save file directory path
     * @return conformation set
     */
    public IAtomContainerSet generateConformationUsingOnlyAtomRotation(IAtomContainer theMolecule, List<ConformationVariable> theConformationVariableList, String theResultDir) {
        File theResultDirFile = new File(theResultDir);

        //If the saved directory is not existed, it makes directory
        if (!theResultDirFile.exists()) {
            theResultDirFile.mkdir();
        }

        this.itsResultDir = theResultDir;
        this.itsMoveTogetherAtomNumberMap = new MoveTogetherAtomNumberList(theMolecule);
        this.itsMoleculeSet = new AtomContainerSet();

        this.itsMoleculeSet.addAtomContainer(theMolecule);
        SDFWriter.writeSDFile(this.itsMoleculeSet, new File(this.itsResultDir + this.TEMP_FILE_NAME + this.itsNumberOfTemp++ + this.SDF_SUFFIX));

        //generate conformation at each conformation variable step
        for (ConformationVariable theConformationVariable : theConformationVariableList) {
            List<File> theFileList = Arrays.asList(new File(theResultDir).listFiles());
            this.itsMoleculeSet = new AtomContainerSet();

            for (File theTempFile : theFileList) {
                IAtomContainerSet theMoleculeSet = SDFReader.openMoleculeFile(theTempFile);
                
                for (IAtomContainer theTemplateMolecule : theMoleculeSet.atomContainers()) {
                    this.generateConformationUsingOnlyAtomRotation(theTemplateMolecule, theConformationVariable);
                }
            }
            SDFWriter.writeSDFile(this.itsMoleculeSet, new File(this.itsResultDir + this.TEMP_FILE_NAME + this.itsNumberOfTemp++ + this.SDF_SUFFIX));
        }

        return this.itsMoleculeSet;
    }

    /**
     * generate conformations using only rotation bond at one conformation variable
     * 
     * @param theMolecule template molecule
     * @param theConformationVariable conformation variable
     * @return conformation set to change in conformation variable
     */
    public IAtomContainerSet generateConformationUsingOnlyAtomRotation(IAtomContainer theMolecule, ConformationVariable theConformationVariable) {
        if (this.itsMoveTogetherAtomNumberMap == null) {
            this.itsMoveTogetherAtomNumberMap = new MoveTogetherAtomNumberList(theMolecule);
        }

        List<Integer> theConnectedAtomIndexList = this.__getConnectedAtomIndexList(theMolecule, theConformationVariable);

        for (int si = 0, sEnd = theConformationVariable.getNumberOfStep(); si < sEnd; si++) {
            this.__generateConformationUsingOnlyAtomRotation(theMolecule, theConformationVariable, theConformationVariable.getInterval() * (si + 1));
        }

        return this.itsMoleculeSet;
    }

    /**
     * generate conformations using only rotation bond at specific step
     * 
     * @param theMolecule template molecule
     * @param theConformationVariable conformation variable
     * @param theAngle conformation angle
     * @return conformation used in conformation variable and specific angle
     */
    private IAtomContainer __generateConformationUsingOnlyAtomRotation(IAtomContainer theMolecule, ConformationVariable theConformationVariable, double theAngle) {
        try {
            List<Integer> theConnectedAtomIndexList = this.__getConnectedAtomIndexList(theMolecule, theConformationVariable);
            IAtomContainer theCopiedMolecule = (IAtomContainer) theMolecule.clone();

            for (Integer theConnectedAtomindex : theConnectedAtomIndexList) {
                ConformationVariable theConnectedConformationVariable = new ConformationVariable(theConformationVariable);

                theConnectedConformationVariable.getAtomIndexList().set(this.FOURTH_INDEX, theConnectedAtomindex);

                Map<Integer, AngleGradientVector> theAngleGradientVectorMap = this.__getAngleGradientVectorMapInTorsionAngle(theMolecule, theConnectedConformationVariable);

                for (Integer theAtomIndex : this.itsMoveTogetherAtomNumberMap.get(theConnectedConformationVariable.getAtomIndexList().get(this.FOURTH_INDEX), theConnectedConformationVariable.getAtomIndexList().get(this.THIRD_INDEX))) {
                    Vector3d theMoveVector = theAngleGradientVectorMap.get(theAtomIndex).calculateAngleGredientVectorUsingAngleDifference(theAngle);
                    Vector3d theAtomPosition = new Vector3d(theMolecule.getAtom(theAtomIndex));

                    theAtomPosition.add(theMoveVector);
                    theCopiedMolecule.getAtom(theAtomIndex).setPoint3d(theAtomPosition.toPoint3d());
                }
            }

            this.itsMoleculeSet.addAtomContainer(theCopiedMolecule);

            if (this.itsMoleculeSet.getAtomContainerCount() > this.MAXIMUM_NUMBER_OF_MOLECULE) {
                SDFWriter.writeSDFile(this.itsMoleculeSet, new File(this.itsResultDir + this.TEMP_FILE_NAME + this.itsNumberOfTemp++ + this.SDF_SUFFIX));
                this.itsMoleculeSet = new AtomContainerSet();
            }

            return theCopiedMolecule;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule Clone Error");
        }

        return null;
    }

    /**
     * get atom index list connected in conformation variable
     * 
     * @param theMolecule template molecule
     * @param theConformationVariable conformation variable
     * @return Atom index list connected in conformation variable
     */
    private List<Integer> __getConnectedAtomIndexList(IAtomContainer theMolecule, ConformationVariable theConformationVariable) {
        List<Integer> theConnectedAtomIndexList = new ArrayList<>();
        List<IAtom> theConnectedAtomList = theMolecule.getConnectedAtomsList(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX)));

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (!theConformationVariable.getAtomIndexList().get(this.SECOND_INDEX).equals(theMolecule.getAtomNumber(theConnectedAtom))) {
                theConnectedAtomIndexList.add(theMolecule.getAtomNumber(theConnectedAtom));
            }
        }

        return theConnectedAtomIndexList;
    }

    /**
     * generate conformations using various conformation variable
     * 
     * @param theMolecule template molecule
     * @param theConformationVariableList various conformation variable
     * @return conformation set using various conformation variable
     */
    public IAtomContainerSet generateConformation(IAtomContainer theMolecule, List<ConformationVariable> theConformationVariableList) {
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();

        theMoleculeSet.addAtomContainer(theMolecule);

        for (ConformationVariable theConformationVariable : theConformationVariableList) {
            IAtomContainerSet theNewMoleculeSet = new AtomContainerSet();

            for (IAtomContainer theTemplateMolecule : theMoleculeSet.atomContainers()) {
                theNewMoleculeSet.add(this.generateConformation(theTemplateMolecule, theConformationVariable));
            }
        }

        return theMoleculeSet;
    }

    /**
     * generate conformations using conformation variable
     * 
     * @param theMolecule template molecule
     * @param theConformationVariable conformation variable
     * @return conformation set using conformation variable
     */
    public IAtomContainerSet generateConformation(IAtomContainer theMolecule, ConformationVariable theConformationVariable) {
        this.itsMolecule = theMolecule;
        this.itsMoveTogetherAtomNumberMap = new MoveTogetherAtomNumberList(theMolecule);

        switch (theConformationVariable.getVariableType()) {
            case ConformationVariable.DISTANCE_VARIABLE:
                return this.__getConformationSetUsingDistanceVariable(theMolecule, theConformationVariable);
            case ConformationVariable.BOND_ANGLE_VARIABLE:
                return this.__getConformationSetUsingBondAngleVariable(theMolecule, theConformationVariable);
            case ConformationVariable.TORSION_ANGLE_VARIABLE:
                return this.__getConformationSetUsingTorionAngleVariable(theMolecule, theConformationVariable);
            default:
                System.err.println("Conformation Variable Type Error");
        }

        return null;
    }

    /**
     * get conformation set using distance variable
     * 
     * @param theMolecule template molecule
     * @param theConformationVariable conformation variable
     * @return conformation set having various distance
     */
    private IAtomContainerSet __getConformationSetUsingDistanceVariable(IAtomContainer theMolecule, ConformationVariable theConformationVariable) {
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();
        Vector3d theUnitDistanceMoveVector = this.__getUnitDistanceMoveVector(theMolecule, theConformationVariable);

        for (int si = 0; si < theConformationVariable.getNumberOfStep(); si++) {
            try {
                IAtomContainer theNewMolecule = (IAtomContainer) theMolecule.clone();
                Vector3d theDistanceMoveVector = Vector3dCalculator.productByScalar((si + 1) * theConformationVariable.getInterval(), theUnitDistanceMoveVector);

                for (Integer theAtomIndex : this.itsMoveTogetherAtomNumberMap.get(theConformationVariable.getAtomIndexList().get(this.FIRST_INDEX), theConformationVariable.getAtomIndexList().get(this.SECOND_INDEX))) {
                    Vector3d theNewPosition = new Vector3d(theNewMolecule.getAtom(theAtomIndex));

                    theNewMolecule.getAtom(theAtomIndex).setPoint3d(new Point3d(Vector3dCalculator.sum(theNewPosition, theDistanceMoveVector).toPoint3d()));
                }

                theMoleculeSet.addAtomContainer(theNewMolecule);
            } catch (CloneNotSupportedException ex) {
                System.err.println("Molecule Clone Error");
            }
        }

        return theMoleculeSet;
    }

    /**
     * get unit vector to move specific atom
     * 
     * @param theMolecule template molecule
     * @param theConformationVariable conformation variable
     * @return unit vector to move specific atom
     */
    private Vector3d __getUnitDistanceMoveVector(IAtomContainer theMolecule, ConformationVariable theConformationVariable) {
        Vector3d theUnitDistanceMoveVector = new Vector3d(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.FIRST_INDEX)), theMolecule.getAtom(this.SECOND_INDEX));

        theUnitDistanceMoveVector.divideScalar(theUnitDistanceMoveVector.length());

        return theUnitDistanceMoveVector;
    }

    /**
     * get conformation set using bond variable
     * 
     * @param theMolecule template molecule
     * @param theConformationVariable
     * @return 
     */
    private IAtomContainerSet __getConformationSetUsingBondAngleVariable(IAtomContainer theMolecule, ConformationVariable theConformationVariable) {
        Map<Integer, AngleGradientVector> theAngleGradientMap = this.__getAngleGradientVectorMapInBondAngle(theMolecule, theConformationVariable);
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();

        for (int si = 0; si < theConformationVariable.getNumberOfStep(); si++) {
            try {
                IAtomContainer theCopiedMolecule = (IAtomContainer) theMolecule.clone();

                for (Integer theAtomIndex : this.itsMoveTogetherAtomNumberMap.get(theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX), theConformationVariable.getAtomIndexList().get(this.SECOND_INDEX))) {
                    Vector3d theMovementVector = theAngleGradientMap.get(theAtomIndex).calculateAngleGredientVectorUsingAngleDifference(theConformationVariable.getInterval() * (si + 1));
                    Vector3d theAtomPosition = new Vector3d(theCopiedMolecule.getAtom(theAtomIndex));

                    theAtomPosition.add(theMovementVector);
                    theCopiedMolecule.getAtom(theAtomIndex).setPoint3d(theAtomPosition.toPoint3d());
                }

                theMoleculeSet.addAtomContainer(theCopiedMolecule);
            } catch (CloneNotSupportedException ex) {
                System.err.println("Molecule Clone Error");
            }
        }

        return theMoleculeSet;
    }

    private Map<Integer, AngleGradientVector> __getAngleGradientVectorMapInBondAngle(IAtomContainer theMolecule, ConformationVariable theConformationVariable) {
        Map<Integer, AngleGradientVector> theAngleGradientVectorMap = new HashMap<>();
        Vector3d theVerticalVector = Vector3dCalculator.crossProduct(new Vector3d(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.SECOND_INDEX)), theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX))),
                new Vector3d(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.SECOND_INDEX)), theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.FIRST_INDEX))));

        for (Integer theAtomIndex : this.itsMoveTogetherAtomNumberMap.get(theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX), theConformationVariable.getAtomIndexList().get(this.SECOND_INDEX))) {
            Vector3d theMoveCenterAtomPosition = new Vector3d(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.SECOND_INDEX)));
            Vector3d theTemplateAtomPosition = new Vector3d(theMolecule.getAtom(theAtomIndex));

            if (Vector3dCalculator.calculateDotProduct(theVerticalVector, theTemplateAtomPosition) != 0.0) {
                double theMovementLength = -Vector3dCalculator.calculateDotProduct(theMoveCenterAtomPosition, theTemplateAtomPosition) / Vector3dCalculator.calculateDotProduct(theVerticalVector, theTemplateAtomPosition);

                theMoveCenterAtomPosition = Vector3dCalculator.sum(theMoveCenterAtomPosition, Vector3dCalculator.productByScalar(theMovementLength, theVerticalVector));
            }

            Vector3d theCentriputalForceUnitVector = Vector3dCalculator.minus(theMoveCenterAtomPosition, theTemplateAtomPosition);
            Vector3d theAccelerationUnitVector = Vector3dCalculator.crossProduct(theCentriputalForceUnitVector, theVerticalVector);
            double theRadius = theCentriputalForceUnitVector.length();

            theCentriputalForceUnitVector.divideScalar(theCentriputalForceUnitVector.length());
            theAccelerationUnitVector.divideScalar(theAccelerationUnitVector.length());

            theAngleGradientVectorMap.put(theAtomIndex, new AngleGradientVector(theAccelerationUnitVector, theCentriputalForceUnitVector, theRadius));
        }

        return theAngleGradientVectorMap;
    }

    private IAtomContainerSet __getConformationSetUsingTorionAngleVariable(IAtomContainer theMolecule, ConformationVariable theConformationVariable) {
        Map<Integer, AngleGradientVector> theAngleGradientVectorMap = this.__getAngleGradientVectorMapInTorsionAngle(theMolecule, theConformationVariable);
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();

        for (int si = 0; si < theConformationVariable.getNumberOfStep(); si++) {
            try {
                IAtomContainer theCopiedMolecule = (IAtomContainer) theMolecule.clone();

                for (Integer theAtomIndex : this.itsMoveTogetherAtomNumberMap.get(theConformationVariable.getAtomIndexList().get(this.FOURTH_INDEX), theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX))) {
                    Vector3d theMoveVector = theAngleGradientVectorMap.get(theAtomIndex).calculateAngleGredientVectorUsingAngleDifference(theConformationVariable.getInterval() * (si + 1));
                    Vector3d theAtomPosition = new Vector3d(theMolecule.getAtom(theAtomIndex));

                    theAtomPosition.add(theMoveVector);
                    theCopiedMolecule.getAtom(theAtomIndex).setPoint3d(theAtomPosition.toPoint3d());
                }

                theMoleculeSet.addAtomContainer(theCopiedMolecule);
            } catch (CloneNotSupportedException ex) {
                System.err.println("Molecule Clone Error");
            }
        }

        return theMoleculeSet;
    }

    private Map<Integer, AngleGradientVector> __getAngleGradientVectorMapInTorsionAngle(IAtomContainer theMolecule, ConformationVariable theConformationVariable) {
        Map<Integer, AngleGradientVector> theAngleGradientVectorMap = new HashMap<>();
        Vector3d theVerticalVector = new Vector3d(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.SECOND_INDEX)), theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX)));

        for (Integer theAtomIndex : this.itsMoveTogetherAtomNumberMap.get(theConformationVariable.getAtomIndexList().get(this.FOURTH_INDEX), theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX))) {
            Vector3d theAccelerationUnitVector = Vector3dCalculator.crossProduct(theVerticalVector, new Vector3d(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX)), theMolecule.getAtom(theAtomIndex)));
            Vector3d theCentriputalForceUnitVector = Vector3dCalculator.crossProduct(theVerticalVector, theAccelerationUnitVector);
            Vector3d theMoveThirdAtomPosition = new Vector3d(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX)));
            double theConstant = -Vector3dCalculator.calculateDotProduct(theVerticalVector, new Vector3d(theMolecule.getAtom(theAtomIndex)));
            double theDistance = (Vector3dCalculator.calculateDotProduct(theVerticalVector, new Vector3d(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX)))) + theConstant) / theVerticalVector.length();
            double theRadius = Math.sqrt(Math.pow(theMolecule.getAtom(theConformationVariable.getAtomIndexList().get(this.THIRD_INDEX)).getPoint3d().distance(theMolecule.getAtom(theAtomIndex).getPoint3d()), 2.0) - (theDistance * theDistance));

            theAccelerationUnitVector.divideScalar(theAccelerationUnitVector.length());
            theCentriputalForceUnitVector.divideScalar(theCentriputalForceUnitVector.length());

            theAngleGradientVectorMap.put(theAtomIndex, new AngleGradientVector(theAccelerationUnitVector, theCentriputalForceUnitVector, theRadius));
        }

        return theAngleGradientVectorMap;
    }
}
