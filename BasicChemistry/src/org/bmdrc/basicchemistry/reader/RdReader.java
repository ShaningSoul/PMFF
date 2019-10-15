/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.basicchemistry.reaction.Reaction;
import org.bmdrc.basicchemistry.reaction.ReactionList;
import org.bmdrc.basicchemistry.fileformat.enums.RdFileTag;
import org.bmdrc.ui.abstracts.StringConstant;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 07. 09
 */
public class RdReader implements Serializable {

    private static final long serialVersionUID = -5522471794334355955L;

    private IAtomContainerSet itsSubstrateSet;
    private IAtomContainerSet itsProductSet;
    private Map itsProperties;
    private String itsName;
    private String itsType;
    private StringBuilder itsValue;
    private Integer itsNumberOfSubstrate;
    private Integer itsNumberOfProduct;
    //constant Integer variable
    private final int START_INDEX = 3;
    private final int REACTION_NAME_INDEX = 0;
    private final int NUMBER_OF_SUBSTRATE_START_INDEX = 0;
    private final int NUMBER_OF_PRODUCT_START_INDEX = 3;
    private final int NUMBER_OF_MOLECULE_STRING_SIZE = 3;
    private final int NUMBER_OF_MOLECULE_INDEX = 3;
    private final int MAXIMUM_RXN_LINE = 4;
    //constant String variable
    private final String START_STRING = "$RFMT";
    private final String MOL_END_STRING = "M  END";
    //constant File variable
    private final String TEMP_FILE_PATH = Constant.TEMP_DIR + "Temp_SBFF_RdReader_Molecule.sdf";

    public ReactionList read(File theRdFile) throws IOException {
        BufferedReader theReader = new BufferedReader(new FileReader(theRdFile));
        ReactionList theReactionList = new ReactionList();
        String theString;
        StringBuilder theValue = new StringBuilder();

        int theLineIndex = 1;

        theReader.readLine();
        theReader.readLine();
        theReader.readLine();

        this.__initializeVariable();

        while ((theString = theReader.readLine()) != null) {
            if (theString.contains(RdFileTag.RXN.TAG)) {
                this.__readRXNPart(theReader);
            } else if (theString.contains(RdFileTag.MOL.TAG)) {
                this.__readMOLPart(theReader);
            } else if (theString.contains(RdFileTag.DTYPE.TAG)) {
                this.itsType = theString.substring(RdFileTag.DTYPE.TAG.length() + 1);
                this.__readDtypeAndDatumPart(theReader);

                theReactionList.addReaction(new Reaction(this.itsSubstrateSet, this.itsProductSet, this.itsProperties, this.itsName));

                this.__initializeVariable();
            }

            theLineIndex++;
        }

        return theReactionList;
    }

    private void __initializeVariable() {
        this.itsSubstrateSet = new AtomContainerSet();
        this.itsProductSet = new AtomContainerSet();
        this.itsProperties = new HashMap<>();
        this.itsType = null;
        this.itsValue = new StringBuilder();
    }

    private void __readDtypeAndDatumPart(BufferedReader theReader) throws IOException {
        String theString = null;

        while ((theString = theReader.readLine()) != null && !theString.contains(this.START_STRING)) {
            if (theString.contains(RdFileTag.DTYPE.TAG)) {
                this.itsProperties.put(this.itsType, this.itsValue.toString());
                this.itsType = theString.substring(RdFileTag.DTYPE.TAG.length() + 1);
                this.itsValue = new StringBuilder();
            } else if (theString.contains(RdFileTag.DATUM.TAG)) {
                this.itsValue.append(theString.substring(RdFileTag.DATUM.TAG.length() + 1));
            } else {
                this.itsValue.append(StringConstant.END_LINE).append(theString);
            }
        }
        
        this.itsProperties.put(this.itsType, this.itsValue.toString());
    }

    private void __readDTYPEPart(String theFileString) {
        if (this.itsType != null) {
            this.itsProperties.put(this.itsType, this.itsValue.toString());
        }

        this.itsType = theFileString.substring(RdFileTag.DTYPE.TAG.length() + 1);
    }

    private void __readRXNPart(BufferedReader theReader) throws IOException {
        String theFileString = null;

        for (int li = 0; li < this.MAXIMUM_RXN_LINE; li++) {
            theFileString = theReader.readLine();

            if (li == this.REACTION_NAME_INDEX) {
                this.itsName = theFileString;
            } else if (li == this.NUMBER_OF_MOLECULE_INDEX) {
                this.itsNumberOfSubstrate = Integer.parseInt(theFileString.substring(this.NUMBER_OF_SUBSTRATE_START_INDEX,
                        this.NUMBER_OF_SUBSTRATE_START_INDEX + this.NUMBER_OF_MOLECULE_STRING_SIZE).trim());
                this.itsNumberOfProduct = Integer.parseInt(theFileString.substring(this.NUMBER_OF_PRODUCT_START_INDEX,
                        this.NUMBER_OF_PRODUCT_START_INDEX + this.NUMBER_OF_MOLECULE_STRING_SIZE).trim());
            }
        }
    }

    private void __readMOLPart(BufferedReader theReader) throws IOException {
        String theFileString = null;
        StringBuilder theStringBuilder = new StringBuilder();
        int theReadMolecule = 0;
        int theTotalMolecule = this.itsNumberOfSubstrate + this.itsNumberOfProduct;

        while (theTotalMolecule > theReadMolecule) {
            theFileString = theReader.readLine();

            if (theFileString.equals(RdFileTag.MOL.TAG)) {
                continue;
            }

            theStringBuilder.append(theFileString).append(StringConstant.END_LINE);

            if (theFileString.equals(this.MOL_END_STRING)) {
                IAtomContainer theMolecule = this.__getMolecule(theStringBuilder.toString());

                if (this.itsNumberOfSubstrate > theReadMolecule) {
                    this.itsSubstrateSet.addAtomContainer(theMolecule);
                } else {
                    this.itsProductSet.addAtomContainer(theMolecule);
                }

                theStringBuilder = new StringBuilder();
                theReadMolecule++;
            }
        }
    }

    private IAtomContainer __getMolecule(String theMoleculeString) throws IOException {
        BufferedWriter theFileWriter = new BufferedWriter(new FileWriter(this.TEMP_FILE_PATH));

        theFileWriter.flush();
        theFileWriter.write(theMoleculeString + "$$$$");
        theFileWriter.close();

        return SDFReader.openMoleculeFile(new File(this.TEMP_FILE_PATH)).getAtomContainer(Constant.FIRST_INDEX);
    }
}
