/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point3d;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.basicchemistry.writer.SDFWriter;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 05. 19
 */
public class PdbqtReader implements Serializable {

    private static final long serialVersionUID = 2732818667425084951L;

    private enum DataType {

        Model("MODEL"), Hetero_Atom("HETATM"), Energy("REMARK VINA RESULT");

        public final String NAME;

        private DataType(String NAME) {
            this.NAME = NAME;
        }
    }

    private List<String> itsAtomAnnotationList;
    //constant Integer variable
    private final int ENERGY_INDEX = 3;

    private enum AtomInformationIndex {

        DataType, AtomNumber, AtomAnnotation, Empty, X, Y, Z;
    }

    public IAtomContainerSet read(String thePdbqtFilePath, String theSdfFilePath) {
        return this.read(new File(thePdbqtFilePath), new File(theSdfFilePath));
    }

    public IAtomContainerSet read(File thePdbqtFile, File theSdfFile) {
        IAtomContainer theTemplateMolecule = SDFReader.openMoleculeFile(theSdfFile).getAtomContainer(Constant.FIRST_INDEX);
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();
        this.itsAtomAnnotationList = this.__getAtomAnnotationList(theTemplateMolecule);

        theTemplateMolecule = AtomContainerManipulator.removeHydrogens(theTemplateMolecule);
        
        try {
            BufferedReader theFileReader = new BufferedReader(new FileReader(thePdbqtFile));
            String theFileString;

            while ((theFileString = theFileReader.readLine()) != null && !theFileString.isEmpty()) {
                String[] theSplitedString = theFileString.split("[\\s]+");

                if (theSplitedString[AtomInformationIndex.DataType.ordinal()].equals(DataType.Model.NAME)) {
                    theMoleculeSet.addAtomContainer((IAtomContainer) theTemplateMolecule.clone());
                } else if (theFileString.contains(DataType.Energy.NAME)) {
                    theMoleculeSet.getAtomContainer(theMoleculeSet.getAtomContainerCount() - 1).setProperty("Energy", theSplitedString[this.ENERGY_INDEX]);
                } else if (theSplitedString[AtomInformationIndex.DataType.ordinal()].equals(DataType.Hetero_Atom.NAME)) {
                    this.__setAtomPosition(theSplitedString, theMoleculeSet.getAtomContainer(theMoleculeSet.getAtomContainerCount() - 1));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }

        return theMoleculeSet;
    }

    private void __setAtomPosition(String[] theSplitedString, IAtomContainer theMolecule) {
        int theAtomIndex = this.itsAtomAnnotationList.indexOf(theSplitedString[AtomInformationIndex.AtomAnnotation.ordinal()]);
        Point3d thePosition = new Point3d(Double.parseDouble(theSplitedString[AtomInformationIndex.X.ordinal()]), Double.parseDouble(theSplitedString[AtomInformationIndex.Y.ordinal()]),
                Double.parseDouble(theSplitedString[AtomInformationIndex.Z.ordinal()]));
        
        theMolecule.getAtom(theAtomIndex).setPoint3d(thePosition);
    }

    private List<String> __getAtomAnnotationList(IAtomContainer theMolecule) {
        List<String> theAtomAnnotationList = new ArrayList<>();

        for (IAtom theAtom : theMolecule.atoms()) {
            theAtomAnnotationList.add(theAtom.getSymbol() + (theMolecule.getAtomNumber(theAtom) + 1));
        }

        return theAtomAnnotationList;
    }
    
    public void convertSdf(String thePdbqtFilePath, String theTemplateFilePath, String theOutputFilePath) {
        this.convertSdf(new File(thePdbqtFilePath), new File(theTemplateFilePath), theOutputFilePath);
    }
    
    public void convertSdf(File thePdbqtFile, File theTemplateFile, String theOutputFilePath) {
        IAtomContainerSet theMoleculeSet = this.read(thePdbqtFile, theTemplateFile);
        
        SDFWriter.writeSDFile(theMoleculeSet, new File(theOutputFilePath));
    }
}
