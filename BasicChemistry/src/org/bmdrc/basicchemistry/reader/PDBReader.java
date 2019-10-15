/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2014, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.protein.AminoAcid;
import org.bmdrc.basicchemistry.protein.AminoAcidInformation;
import org.bmdrc.basicchemistry.protein.Chain;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.protein.interfaces.IPDBInformationType;
import org.bmdrc.ui.abstracts.StringConstant;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class PDBReader implements IPDBInformationType, Serializable {

    private static final long serialVersionUID = -3234919536949800977L;

    private Protein itsProtein;
    private String itsPreviousKey;
    private String itsPreviousHeteroName;
    private Integer itsPreviousAminoAcidIndex;
    private String itsPreviousAminoAcidSerial;
    private List<String> itsUsedHeteroMoleculeIndexList;
    private Integer itsSolventRemarkLineNumber;
    private Map<String, Integer> itsResidueSerialMap;
    private String itsPreviousChainID;
    private List<String> itsChainNameList;
    //constant String variable
    private final String SPACE_STRING_REGEX = "[\\s]+";
    private final String CLASS_ERROR_MESSAGE = "readPDBFile IOException!!";
    private final String JOURNAL_READING_ERROR_MESSAGE = "Journal Part Reading Error!!";
    private final String ALPHA_HELIX_READING_ERROR_MESSAGE = "Alpha Helix Reading Error!!";
    private final String BETA_SHEET_READING_ERROR_MESSAGE = "Beta Sheet Reading Error!!";
    private final String BETA_SHEET_SENSE_ERROR_MESSAGE = "Beta Sheet Sense Error!!";
    private final String AMINO_ACID_SERIAL_ERROR_MESSAGE = "Amino Acid Serial Error!!";
    private final String FIRST_STRAND_SENSE = "0";
    private final String PARALLEL_SENSE = "1";
    private final String ANTI_PARALLEL_SENSE = "-1";
    private final String FIRST_STRAND_SENSE_KEY = "First Strand";
    private final String PARALLEL_KEY = "Parallel";
    private final String ANIT_PARALLEL_KEY = "Anti-Parallel";
    private final String CDK_MOLECULE_TITLE = "cdk:Title";
    private final String HYDROGEN_SYMBOL = "H";
    private final String PDB_HETERO_MOLECULE_URL_START = "http://www.rcsb.org/pdb/files/ligand/";
    private final String PDB_HETERO_MOLECULE_URL_END = ".sdf";
    private final String MOLECULE_NAME_KEY = "cdk:Title";
    //constant Integer variable
    private final Integer DEFAULT_INTEGER = 0;
    private final Integer FIRST_INDEX = 0;
    private final Integer SECOND_INDEX = 1;
    private final Integer SOLVENT_INFORMATION_START_LINE_NUMEBER = 12;
    private final Integer ACTIVE_SITE_INFORMATION_LENGTH = 11;
    private final Integer FIRST_ATOM_INDEX = 1;

    public PDBReader() {
        this.__initializeVariable();
    }

    private void __initializeVariable() {
        this.itsProtein = new Protein();
        this.itsUsedHeteroMoleculeIndexList = new ArrayList<>();
        this.itsSolventRemarkLineNumber = this.DEFAULT_INTEGER;
        this.itsResidueSerialMap = new HashMap<>();
        this.itsChainNameList = new ArrayList<>();
    }

    public Protein readPDBFile(File thePDBFile) throws IOException {
        this.__initializeVariable();

        BufferedReader theFileReader = new BufferedReader(new FileReader(thePDBFile));
        String theFileString = new String();

        while ((theFileString = theFileReader.readLine()) != null) {
            this.__sortInformationType(theFileString);
        }
        
        this.__inputAminoAcidBondInformation();
        this.__readHeteroAtomConnectionPart();
        this.itsProtein.generateMolecule();

        return this.itsProtein;
    }

    public Protein readPDBFile(InputStream thePDBInputStream) throws IOException {
        this.__initializeVariable();
        
        BufferedReader theInputStreamReader = new BufferedReader(new InputStreamReader(thePDBInputStream));
        String theInputStreamString = new String();
        
        while((theInputStreamString = theInputStreamReader.readLine()) != null) {
            this.__sortInformationType(theInputStreamString);
        }
        
        this.__inputAminoAcidBondInformation();
        this.__readHeteroAtomConnectionPart();
        this.itsProtein.generateMolecule();
        
        return this.itsProtein;
    }
    
    private void __inputAminoAcidBondInformation() {
        AminoAcidInformation theConnectivityInformation = new AminoAcidInformation();

        for (Chain theChain : this.itsProtein.getChainList()) {
            for (AminoAcid theAminoAcid : theChain.getAminoAcidList()) {
                if (!theAminoAcid.getMolecule().isEmpty() && theAminoAcid.getName().trim().length() == 3) {
                    theConnectivityInformation.inputAminoAcidBondInformation(theAminoAcid);
                    theAminoAcid.setMolecule(theConnectivityInformation.getAminoAcid().getMolecule());
                }
            }
        }
    }

    private void __sortInformationType(String theFileString) throws IOException {
        String[] theSplitedString = theFileString.split(this.SPACE_STRING_REGEX);

        switch (theSplitedString[this.FIRST_INDEX]) {
//            case PDBReader.HEADER:
//                this.__readHeaderPart(theFileString);
//                break;
//            case PDBReader.COMPOUND:
//                this.__readCompoundPart(theFileString);
//                break;
//            case PDBReader.SOURCE:
//                this.__readSourcePart(theFileString);
//                break;
//            case PDBReader.EXPERIMENT_METHOD:
//                this.__readExperimentMethodPart(theFileString);
//                break;
//            case PDBReader.AUTHOR:
//                this.__readAuthorPart(theFileString);
//                break;
//            case PDBReader.VALIDATE_DATE:
//                this.__readValidateDatePart(theFileString);
//                break;
//            case PDBReader.JOURNAL:
//                this.__readJournalPart(theFileString);
//                break;
//            case PDBReader.REMARK:
//                this.__readRemarkPart(theFileString);
//                break;
//            case PDBReader.DB_REFERENCE:
//                this.__readDBReferencePart(theFileString);
//                break;
//            case PDBReader.SEQUENCE_DIFFERENCE:
//                this.__readSeqenceDifferencePart(theFileString);
//                break;
//            case PDBReader.AMINO_ACID_SEQUENCE:
//                this.__readAminoAcidSequencePart(theFileString);
//                break;
//            case PDBReader.HETERO_INFORMATION:
//                this.__readHeteroInformationPart(theFileString);
//                break;
//            case PDBReader.HETERO_NAME:
//                this.__readHeteroNamePart(theFileString);
//                break;
//            case PDBReader.HETERO_FORMUL:
//                this.__readHeterFormulaPart(theFileString);
//                break;
//            case PDBReader.ALPHA_HELIX_INFORMATION:
//                this.__readAlphaHelixInformationPart(theFileString);
//                break;
//            case PDBReader.BETA_SHEET_INFORMATION:
//                this.__readBetaSheetInformationPart(theFileString);
//                break;
//            case PDBReader.ACTIVE_SITE_INFORMATION:
//                this.__readActiveSiteInformationPart(theFileString);
//                break;
//            case PDBReader.UNIT_CELL_PARAMETER:
//                this.__readUnitCellParameterPart(theFileString);
//                break;
//            case PDBReader.FIRST_ORIGX:
//            case PDBReader.SECOND_ORIGX:
//            case PDBReader.THIRD_ORIGX:
//                this.__readOriginCoordinateInformationPart(theFileString);
//                break;
//            case PDBReader.FIRST_SCALE:
//            case PDBReader.SECOND_SCALE:
//            case PDBReader.THIRD_SCALE:
//                this.__readCrystallographicCoordinateInformationPart(theFileString);
//                break;
            case PDBReader.ATOM:
                this.__readAtomPart(theFileString);
                break;
            case PDBReader.HETERO_ATOM:
                this.__readHeteroAtomPart(theFileString);
                break;
            default:
        }
    }

    private void __readHeaderPart(String theFileString) {
        this.itsProtein.setProteinType(theFileString.substring(this.CLASSIFICATION_START_INDEX, this.CLASSIFICATION_END_INDEX));
    }

    private void __readCompoundPart(String theFileString) {
        String[] theSplitedString = theFileString.split(StringConstant.DOUBLE_DOT_REGEX);

        if (theSplitedString.length > 1) {
            String theValue = theSplitedString[this.COMPOUND_INFORMATION_VALUE_INDEX].trim().replaceAll(StringConstant.SEMICOLON_REGEX, StringConstant.EMPTY_STRING);

            this.itsProtein.setCompoundInformationMap().put(theSplitedString[this.COMPOUND_INFORMATION_KEY_INDEX], theValue);
            this.itsPreviousKey = theSplitedString[this.COMPOUND_INFORMATION_KEY_INDEX];
        } else {
            String theValue = this.itsProtein.setCompoundInformationMap().get(theSplitedString[this.COMPOUND_INFORMATION_KEY_INDEX]) + theFileString.trim();

            this.itsProtein.setCompoundInformationMap().put(theSplitedString[this.COMPOUND_INFORMATION_KEY_INDEX], theValue);
        }
    }

    private void __readSourcePart(String theFileString) {
        String theInformation = theFileString.substring(this.INFORMATION_START_INDEX).trim().replaceAll(StringConstant.SEMICOLON_REGEX, StringConstant.EMPTY_STRING);

        if (theInformation.contains(StringConstant.DOUBLE_DOT_STRING)) {
            String[] theSplitedInformation = theInformation.split(StringConstant.DOUBLE_DOT_REGEX);

            this.itsPreviousKey = theSplitedInformation[this.SOURCE_INFORMATION_KEY_INDEX];
            this.itsProtein.setSourceMap().put(theSplitedInformation[this.SOURCE_INFORMATION_KEY_INDEX], theSplitedInformation[this.SOURCE_INFORMATION_VALUE_INDEX]);
        } else {
            String theValue = this.itsProtein.getSourceMap().get(this.itsPreviousKey).toString();

            this.itsProtein.setSourceMap().put(this.itsPreviousKey, theValue + StringConstant.SPACE_STRING + theInformation);
        }
    }

    private void __readExperimentMethodPart(String theFileString) {
        this.itsProtein.setExperimentType(theFileString.substring(this.INFORMATION_START_INDEX).trim());
    }

    private void __readAuthorPart(String theFileString) {
        this.itsProtein.setAuthor(theFileString.substring(this.INFORMATION_START_INDEX).trim());
    }

    private void __readValidateDatePart(String theFileString) {
        this.itsProtein.setValidateDateList().add(theFileString.substring(this.INFORMATION_START_INDEX).trim());
    }

    private void __readJournalPart(String theFileString) {
        String theInformationType = theFileString.substring(this.INFORMATION_TYPE_START_INDEX_IN_JOURNAL, this.INFORMATION_TYPE_END_INDEX_IN_JOURNAL).trim();
        String theInformation = theFileString.substring(this.JOURNAL_PART_INFORMATION_START_INDEX).trim();
        String theKey = new String();

        switch (theInformationType) {
            case PDBReader.JOURNAL_AUTHOR:
                theKey = Protein.JOURNAL_AUTHOR_KEY;
                break;
            case PDBReader.JOURNAL_TITLE:
                theKey = Protein.JOURNAL_TITLE_KEY;
                break;
            case PDBReader.JOURNAL_NAME:
                theKey = Protein.JOURNAL_NAME_KEY;
                break;
            case PDBReader.JOURNAL_NAME_ID:
                theKey = Protein.JOURNAL_NAME_ID_KEY;
                break;
            case PDBReader.JOURNAL_PUBMED_ID:
                theKey = Protein.JOURNAL_PUBMED_ID_KEY;
                break;
            case PDBReader.JOURNAL_PUBLICATION_IDENTIFIER:
                theKey = Protein.JOURNAL_PUBLICATION_IDENTIFIER_KEY;
                break;
            default:
                System.err.println(this.JOURNAL_READING_ERROR_MESSAGE);
        }

        if (theKey.equals(this.itsPreviousKey)) {
            this.itsProtein.setJournalInformationMap().put(theKey, this.itsProtein.setJournalInformationMap().get(this.itsPreviousKey).toString() + theInformation);
        } else {
            this.itsProtein.setJournalInformationMap().put(theKey, theInformation);
            this.itsPreviousKey = theKey;
        }
    }

    private void __readRemarkPart(String theFileString) {
        if (theFileString.length() > this.REMARK_SERIAL_END_INDEX) {
            String theRemarkSerial = theFileString.substring(this.REMARK_SERIAL_START_INDEX, this.REMARK_SERIAL_END_INDEX).trim();

            switch (theRemarkSerial) {
                case PDBReader.DISTANT_SOLVENT_REMARK_SERIAL:
                    this.__readDistantSolventAtomPart(theFileString);
                    break;
                default:
            }
        }
    }

    private void __readDBReferencePart(String theFileString) {
        String theChainID = theFileString.substring(this.CHAIN_ID_START_INDEX_IN_DB_REFERENCE, this.CHAIN_ID_START_INDEX_IN_DB_REFERENCE + 1);
        Integer theStartSerial = Integer.parseInt(theFileString.substring(this.START_SERIAL_START_INDEX_IN_DB_REFERENCE, this.START_SERIAL_END_INDEX_IN_DB_REFERENCE).trim());

        if (!this.itsResidueSerialMap.containsKey(theChainID)) {
            this.itsResidueSerialMap.put(theChainID, theStartSerial);
        } else {
            if (this.itsResidueSerialMap.get(theChainID) > theStartSerial) {
                this.itsResidueSerialMap.put(theChainID, theStartSerial);
            }
        }
    }

    private void __readDistantSolventAtomPart(String theFileString) {
        if (this.itsSolventRemarkLineNumber >= this.SOLVENT_INFORMATION_START_LINE_NUMEBER) {
            this.itsProtein.setUnusualSolventMoleculeSerialList().add(theFileString.substring(this.UNUSUAL_SOLVENT_SERIAL_START_INDEX, this.UNUSUAL_SOLVENT_SERIAL_END_INDEX));
        }

        this.itsSolventRemarkLineNumber += 1;

    }

    private void __readSeqenceDifferencePart(String theFileString) {
        String theChainID = theFileString.substring(this.CHAIN_ID_START_INDEX_IN_SEQUENCE_DIFFERENCE, this.CHAIN_ID_START_INDEX_IN_SEQUENCE_DIFFERENCE + 1);
        String theResidueName = theFileString.substring(this.INSERTION_RESIDUE_NAME_START_INDEX_IN_SEQUENCE_DIFFERENCE, this.INSERTION_RESIDUE_NAME_END_INDEX_IN_SEQUENCE_DIFFERENCE).trim();
        String theSerial = theFileString.substring(this.INSERTION_RESIDUE_SERIAL_START_INDEX_IN_SEQUENCE_DIFFERENCE, this.INSERTION_RESIDUE_SERIAL_END_INDEX_IN_SEQUENCE_DIFFERENCE).trim();
        AminoAcid theAminoAcid = new AminoAcid();

        this.__checkNewChain(theChainID);

        this.__inputInsertionSequence(theChainID, theResidueName, theSerial);

        theResidueName = theFileString.substring(this.DELETION_RESIDUE_NAME_START_INDEX_IN_SEQUENCE_DIFFERENCE, this.DELETION_RESIDUE_NAME_END_INDEX_IN_SEQUENCE_DIFFERENCE).trim();
        theSerial = theFileString.substring(this.DELETION_RESIDUE_SERIAL_START_INDEX_IN_SEQUENCE_DIFFERENCE, this.DELETION_RESIDUE_SERIAL_END_INDEX_IN_SEQUENCE_DIFFERENCE).trim();

        this.__inputDeletionSequence(theChainID, theResidueName, theSerial);
    }

    private void __inputInsertionSequence(String theChainID, String theResidueName, String theSerial) {
        AminoAcid theAminoAcid = new AminoAcid();

        if (!theResidueName.isEmpty() && !theSerial.isEmpty()) {
            theAminoAcid.setName(theResidueName);
            theAminoAcid.setSerial(theSerial);
            this.itsProtein.getChain(theChainID).setInsertionSequence().add(theAminoAcid);

            if (this.itsResidueSerialMap.get(theChainID) > Integer.parseInt(theSerial)) {
                this.itsResidueSerialMap.put(theChainID, Integer.parseInt(theSerial));
            }

        }
    }

    private void __inputDeletionSequence(String theChainID, String theResidueName, String theSerial) {
        AminoAcid theAminoAcid = new AminoAcid();

        if (!theResidueName.isEmpty() && !theSerial.isEmpty()) {
            theAminoAcid.setName(theResidueName);
            theAminoAcid.setSerial(theSerial);
            this.itsProtein.getChain(theChainID).setDeletionSequence().add(theAminoAcid);
        }
    }

    private void __checkNewChain(String theChainID) {
        if (!this.itsProtein.containChainName(theChainID)) {
            Chain theNewChain = new Chain();

            theNewChain.setChainName(theChainID);
            this.itsProtein.addChain(theNewChain);
        }
    }

    private void __readAminoAcidSequencePart(String theFileString) {
        String theChainID = theFileString.substring(this.CHAIN_ID_IN_AMINO_ACID_SEQUENCE, this.CHAIN_ID_IN_AMINO_ACID_SEQUENCE + 1);
        String[] theSequenceArray = theFileString.substring(this.AMINO_ACID_SEQUENCE_START_INDEX).trim().split(this.SPACE_STRING_REGEX);

        for (String theSequence : theSequenceArray) {
            AminoAcid theAminoAcid = new AminoAcid();
            Integer theSerial = this.itsResidueSerialMap.get(theChainID);

            theAminoAcid.setName(theSequence);
            theAminoAcid.setSerial(Integer.toString(theSerial));
            theAminoAcid.getMolecule().setProperty(this.CDK_MOLECULE_TITLE, theSequence + theSerial);

            this.__checkNewChain(theChainID);
            this.itsResidueSerialMap.put(theChainID, ++theSerial);
            this.itsProtein.getChain(theChainID).setAminoAcidList().add(theAminoAcid);
        }
    }

    private void __readHeteroInformationPart(String theFileString) {
        String theChainID = theFileString.substring(this.CHAIN_ID_IN_HETERO_INFORMATION, this.CHAIN_ID_IN_HETERO_INFORMATION + 1);
        String theHeteroMoleculeID = theFileString.substring(this.MOLECULE_ID_START_INDEX_IN_HETERO_INFORMATION, this.MOLECULE_ID_END_INDEX_IN_HETERO_INFORMATION).trim();
        String theHeteroMoleculeSerial = theFileString.substring(this.MOLECULE_SERIAL_START_INDEX_IN_HETERO_INFORMATION, this.MOLECULE_SERIAL_END_INDEX_IN_HETERO_INFORMATION).trim();
        Chain theChain = this.itsProtein.getChain(theChainID);
        IAtomContainer theMolecule = new AtomContainer();

        theMolecule.setProperty(Protein.HETERO_MOLECULE_ID_KEY, theHeteroMoleculeID);
        theMolecule.setProperty(Protein.HETERO_MOLECULE_SERIAL_KEY, theHeteroMoleculeSerial);

        theChain.setHeteroMoleculeList().addAtomContainer(theMolecule);
    }

    private void __readHeteroNamePart(String theFileString) {
        String theMoleculeID = theFileString.substring(this.MOLECULE_ID_START_INDEX_IN_HETERO_NAME, this.MOLECULE_ID_END_INDEX_IN_HETERO_NANE).trim();
        String theMoleculeName = theFileString.substring(this.MOLECULE_NAME_START_INDEX_IN_HETERO_NAME).trim();

        for (Chain theChain : this.itsProtein) {
            for (IAtomContainer theHeteroMolecule : theChain.getHeteroMoleculeList().atomContainers()) {
                if (theHeteroMolecule.getProperty(Protein.HETERO_MOLECULE_ID_KEY).toString().equals(theMoleculeID)) {
                    theHeteroMolecule.setProperty(Protein.HETERO_MOLECULE_NAME_KEY, theMoleculeName);
                }
            }
        }
    }

    private void __readHeterFormulaPart(String theFileString) {
        String theMoleculeID = theFileString.substring(this.MOLECULE_ID_START_INDEX_IN_HETERO_FORMULA, this.MOLECULE_ID_END_INDEX_IN_HETERO_FORMULA).trim();
        String theMolecularFormulaInformation = theFileString.substring(this.MOLECULE_FORMULA_INFORMATION_START_INDEX_IN_HETERO_FORMULA).trim();
        String theMolecularFormula = this.__getMolecularFormula(theMolecularFormulaInformation);

        for (Chain theChain : this.itsProtein) {
            for (IAtomContainer theHeteroMolecule : theChain.getHeteroMoleculeList().atomContainers()) {
                if (theHeteroMolecule.getProperty(Protein.HETERO_MOLECULE_ID_KEY).toString().equals(theMoleculeID)) {
                    theHeteroMolecule.setProperty(Protein.HETERO_MOLECULE_FORMULA_KEY, theMolecularFormula);
                }
            }
        }
    }

    private String __getMolecularFormula(String theMolecularFormulaInformation) {
        if (theMolecularFormulaInformation.contains(StringConstant.OPEN_PARENTHESES_STRING)) {
            return theMolecularFormulaInformation.substring(theMolecularFormulaInformation
                    .indexOf(StringConstant.OPEN_PARENTHESES_STRING) + 1, theMolecularFormulaInformation
                            .indexOf(StringConstant.CLOSE_PARENTHESES_STRING)).replaceAll(this.SPACE_STRING_REGEX, StringConstant.EMPTY_STRING);
        }

        return theMolecularFormulaInformation.replaceAll(this.SPACE_STRING_REGEX, StringConstant.EMPTY_STRING);
    }

    private void __readAlphaHelixInformationPart(String theFileString) {
        String theHelixID = theFileString.substring(this.HELIX_ID_START_INDEX_IN_ALPHA_HELIX_INFORMATION, this.HELIX_ID_END_INDEX_IN_ALPHA_HELIX_INFORMATION);
        Integer theStartAminoAcidIndex = Integer.parseInt(theFileString.substring(this.HELIX_START_AMINO_ACID_INDEX_START_INDEX_IN_ALPHA_HELIX_INFORMATION, this.HELIX_START_AMINO_ACID_INDEX_END_INDEX_IN_ALPHA_HELIX_INFORMATION).trim());
        Integer theEndAminoAcidIndex = Integer.parseInt(theFileString.substring(this.HELIX_END_AMINO_ACID_INDEX_START_INDEX_IN_ALPHA_HELIX_INFORMATION, this.HELIX_END_AMINO_ACID_INDEX_END_INDEX_IN_ALPHA_HELIX_INFORMATION).trim());
        String theStartAminoAcidChain = theFileString.substring(this.START_CHAIN_ID_INDEX_IN_ALPHA_HELIX_INFORMATION, this.START_CHAIN_ID_INDEX_IN_ALPHA_HELIX_INFORMATION + 1);
        String theEndAminoAcidChain = theFileString.substring(this.END_CHAIN_ID_INDEX_IN_ALPHA_HELIX_INFORMATION, this.END_CHAIN_ID_INDEX_IN_ALPHA_HELIX_INFORMATION + 1);

        if (theStartAminoAcidChain.equals(theEndAminoAcidChain)) {
            Chain theChain = this.itsProtein.getChain(theStartAminoAcidChain);

            for (int ai = theStartAminoAcidIndex, aEnd = theEndAminoAcidIndex; ai <= aEnd; ai++) {
                if (theChain.getSequenceAminoAcid(Integer.toString(ai)) != null) {
                    theChain.getSequenceAminoAcid(Integer.toString(ai)).setSecondaryStructureID(theHelixID);
                    theChain.getSequenceAminoAcid(Integer.toString(ai)).setSecondaryStructureType(Protein.ALPHA_HELIX);
                } else if (theChain.getDeletionResidueSerialList().contains(Integer.toString(ai))) {
                    continue;
                } else {
                    System.out.println(this.AMINO_ACID_SERIAL_ERROR_MESSAGE + " " + Integer.toString(ai) + " " + theChain.getAminoAcidSerialList());
                }
            }
        } else {
            System.err.println(this.ALPHA_HELIX_READING_ERROR_MESSAGE);
        }
    }

    private void __readBetaSheetInformationPart(String theFileString) throws IOException {
        String theBetaSheetID = theFileString.substring(this.SHEET_ID_START_INDEX_IN_BETA_SHEET_INFORMATION, this.SHEET_ID_END_INDEX_IN_BETA_SHEET_INFORMATION).trim();
        Integer theStartAminoAcidIndex = Integer.parseInt(theFileString.substring(this.SHEET_START_AMINO_ACID_INDEX_START_INDEX_IN_BETA_SHEET_INFORMATION, this.SHEET_START_AMINO_ACID_INDEX_END_INDEX_IN_BETA_SHEET_INFORMATION).trim());
        Integer theEndAminoAcidIndex = Integer.parseInt(theFileString.substring(this.SHEET_END_AMINO_ACID_INDEX_START_INDEX_IN_BETA_SHEET_INFORMATION, this.SHEET_END_AMINO_ACID_INDEX_END_INDEX_IN_BETA_SHEET_INFORMATION).trim());
        String theStartChainID = theFileString.substring(this.START_CHAIN_ID_INDEX_IN_BETA_SHEET_INFORMATION, this.START_CHAIN_ID_INDEX_IN_BETA_SHEET_INFORMATION + 1);
        String theEndChainID = theFileString.substring(this.END_CHAIN_ID_INDEX_IN_BETA_SHEET_INFORMATION, this.END_CHAIN_ID_INDEX_IN_BETA_SHEET_INFORMATION + 1);
        String theSense = theFileString.substring(this.BETA_SHEET_SENSE_START_INDEX_IN_BETA_SHEET_INFORMATION, this.BETA_SHEET_SENSE_END_INDEX_IN_BETA_SHEET_INFORMATION).trim();

        if (theStartChainID.equals(theEndChainID)) {
            Chain theChain = this.itsProtein.getChain(theEndChainID);

            for (int ai = theStartAminoAcidIndex, aEnd = theEndAminoAcidIndex; ai <= aEnd; ai++) {
                theChain.getSequenceAminoAcid(Integer.toString(ai)).setSecondaryStructureID(theBetaSheetID);
                theChain.getSequenceAminoAcid(Integer.toString(ai)).setSecondaryStructureType(Protein.BETA_SHEET);
                theChain.getSequenceAminoAcid(Integer.toString(ai)).setBetaSheetSense(this.__getBetaSheetSense(theSense));
            }
        } else {
            System.err.println(this.BETA_SHEET_READING_ERROR_MESSAGE);
        }
    }

    private String __getBetaSheetSense(String theSense) throws IOException {
        if (theSense.equals(this.FIRST_STRAND_SENSE) || theSense.isEmpty()) {
            return this.FIRST_STRAND_SENSE_KEY;
        } else if (theSense.equals(this.PARALLEL_SENSE)) {
            return this.PARALLEL_KEY;
        } else if (theSense.equals(this.ANTI_PARALLEL_SENSE)) {
            return this.ANIT_PARALLEL_KEY;
        } else {
            System.err.println(this.BETA_SHEET_SENSE_ERROR_MESSAGE);
            throw new IOException();
        }
    }

    private void __readActiveSiteInformationPart(String theFileString) {
        String theActiveSiteID = theFileString.substring(this.ACTIVE_SITE_ID_START_INDEX_IN_ACTIVE_SITE_INFORMATION, this.ACTIVE_SITE_ID_END_INDEX_IN_ACTIVE_SITE_INFORMATION).trim();
        String theAminoAcidInformation = theFileString.substring(this.AMINO_ACID_INFORMATION_START_INDEX_IN_ACTIVE_SITE_INFORMATION).trim();

        for (int ai = 0, aEnd = theAminoAcidInformation.length(); ai < aEnd; ai += this.ACTIVE_SITE_INFORMATION_LENGTH) {
            String theSubString = theAminoAcidInformation.substring(ai);
            String theChainID = theSubString.substring(this.CHAIN_ID_START_INDEX_IN_ACTIVE_SITE_INFORMATION, this.CHAIN_ID_START_INDEX_IN_ACTIVE_SITE_INFORMATION + 1);
            String theAminoAcidSerial = theSubString.substring(this.SERIAL_START_INDEX_IN_ACTIVE_SITE_INFORMATION, this.SERIAL_END_INDEX_IN_ACTIVE_SITE_INFORMATION).trim();
            List<AminoAcid> theAminoAcidList = new ArrayList<>();

            if (this.itsProtein.getActiveSiteSequenceMap().containsKey(theActiveSiteID)) {
                theAminoAcidList = this.itsProtein.setActiveSiteSequenceMap().get(theActiveSiteID);
            } else {
                this.itsProtein.setActiveSiteSequenceMap().put(theChainID, theAminoAcidList);
            }
        }
    }

    private void __readUnitCellParameterPart(String theFileString) {
        Double theA = Double.parseDouble(theFileString.substring(this.UNIT_CELL_PARAMETER_A_START_INDEX, this.UNIT_CELL_PARAMETER_A_END_INDEX).trim());
        Double theB = Double.parseDouble(theFileString.substring(this.UNIT_CELL_PARAMETER_B_START_INDEX, this.UNIT_CELL_PARAMETER_B_END_INDEX).trim());
        Double theC = Double.parseDouble(theFileString.substring(this.UNIT_CELL_PARAMETER_C_START_INDEX, this.UNIT_CELL_PARAMETER_C_END_INDEX).trim());
        Double theAlpha = Double.parseDouble(theFileString.substring(this.UNIT_CELL_PARAMETER_ALPHA_START_INDEX, this.UNIT_CELL_PARAMETER_ALPHA_END_INDEX).trim());
        Double theBeta = Double.parseDouble(theFileString.substring(this.UNIT_CELL_PARAMETER_BETA_START_INDEX, this.UNIT_CELL_PARAMETER_BETA_END_INDEX).trim());
        Double theGamma = Double.parseDouble(theFileString.substring(this.UNIT_CELL_PARAMETER_GAMMA_START_INDEX, this.UNIT_CELL_PARAMETER_GAMMA_END_INDEX).trim());

        this.itsProtein.setUnitCellParameter().put(Protein.A_KEY_IN_UNIT_CELL_PARAMETER, theA);
        this.itsProtein.setUnitCellParameter().put(Protein.B_KEY_IN_UNIT_CELL_PARAMETER, theB);
        this.itsProtein.setUnitCellParameter().put(Protein.C_KEY_IN_UNIT_CELL_PARAMETER, theC);
        this.itsProtein.setUnitCellParameter().put(Protein.ALPHA_KEY_IN_UNIT_CELL_PARAMETER, theAlpha);
        this.itsProtein.setUnitCellParameter().put(Protein.BETA_KEY_IN_UNIT_CELL_PARAMETER, theBeta);
        this.itsProtein.setUnitCellParameter().put(Protein.GAMMA_KEY_IN_UNIT_CELL_PARAMETER, theGamma);
    }

    private void __readOriginCoordinateInformationPart(String theFileString) {
        Double theX = Double.parseDouble(theFileString.substring(this.X_ORIGIN_START_INDEX, this.X_ORIGIN_END_INDEX).trim());
        Double theY = Double.parseDouble(theFileString.substring(this.Y_ORIGIN_START_INDEX, this.Y_ORIGIN_END_INDEX).trim());
        Double theZ = Double.parseDouble(theFileString.substring(this.Z_ORIGIN_START_INDEX, this.Z_ORIGIN_END_INDEX).trim());
        Vector3d theCoordinate = new Vector3d(theX, theY, theZ);

         this.itsProtein.setOriginCoordinateList().add(theCoordinate);
    }

    private void __readCrystallographicCoordinateInformationPart(String theFileString) {
        Double theX = Double.parseDouble(theFileString.substring(this.X_ORIGIN_START_INDEX, this.X_ORIGIN_END_INDEX).trim());
        Double theY = Double.parseDouble(theFileString.substring(this.Y_ORIGIN_START_INDEX, this.Y_ORIGIN_END_INDEX).trim());
        Double theZ = Double.parseDouble(theFileString.substring(this.Z_ORIGIN_START_INDEX, this.Z_ORIGIN_END_INDEX).trim());
        Vector3d theCoordinate = new Vector3d(theX, theY, theZ);

        this.itsProtein.setCrystallographicCoordinateList().add(theCoordinate);
    }

    private void __readAtomPart(String theFileString) throws IOException {
        /*if (theFileString.substring(this.ATOM_SYMBOL_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.ATOM_SYMBOL_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim().equals(this.HYDROGEN_SYMBOL)) {
            this.__inputHydrogenAtomInformationUsingPreviousInformation(theFileString);
        } else*/ if (this.itsProtein.getProteinType() != null) {
            this.__inputAtomInformationUsingPreviousInformation(theFileString);
        } else {
            this.__inputAtomInformationOnlyAtomPart(theFileString);
        }
    }

    private void __inputHydrogenAtomInformationUsingPreviousInformation(String theFileString) {
        String theAminoAcidSerial = theFileString.substring(this.AMINO_ACID_INDEX_START_INDEX_IN_ATOM, this.AMINO_ACID_INDEX_END_INDEX_IN_ATOM).trim();
        String theChainID = theFileString.substring(this.CHAIN_ID_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.CHAIN_ID_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim();;
        Integer theAtomIndex = Integer.parseInt(theFileString.substring(this.ATOM_INDEX_START_INDEX_IN_ATOM, this.ATOM_INDEX_END_INDEX_IN_ATOM).trim());
        String theAminoAcidName = theFileString.substring(this.AMINO_ACID_NAME_START_INDEX, this.AMINO_ACID_NAME_END_INDEX);
        IAtom theAtom = this.__getAtomUsingFileString(theFileString);
        
        
        if (this.itsPreviousAminoAcidSerial.equals(theAminoAcidSerial) && !this.itsProtein.getChain(theChainID.trim()).getAminoAcidList().isEmpty()) {
            int theLastIndex = this.itsProtein.getChain(theChainID.trim()).getAminoAcidList().size() - 1;
            Integer theLastHeavyAtomIndex = this.__getLastHeavyAtomIndex(this.itsProtein.getChain(theChainID).getAminoAcidList().get(theLastIndex).setMolecule());
            int theNumberOfAtom = this.itsProtein.getChain(theChainID).getAminoAcidList().get(theLastIndex).setMolecule().getAtomCount();

            this.itsProtein.getChain(theChainID).getAminoAcidList().get(theLastIndex).setMolecule().addAtom(theAtom);
            this.itsProtein.getChain(theChainID).getAminoAcidList().get(theLastIndex).setMolecule().addBond(theNumberOfAtom, theLastHeavyAtomIndex, Order.SINGLE);

        } else {
            int theIndex = this.itsProtein.getChain(theChainID).getAminoAcidList().size();
            Integer theLastHeavyAtomIndex = this.__getLastHeavyAtomIndex(this.itsProtein.getChain(theChainID).getAminoAcidList().get(theIndex).setMolecule());
            int theNumberOfAtom = this.itsProtein.getChain(theChainID).getAminoAcidList().get(theIndex).setMolecule().getAtomCount();

            this.itsProtein.getChain(theChainID).setAminoAcidList().add(new AminoAcid());
            this.itsProtein.getChain(theChainID).setAminoAcidList().get(theIndex).setMolecule().addAtom(theAtom);
            this.itsProtein.getChain(theChainID).getAminoAcidList().get(theIndex).setMolecule().addBond(theNumberOfAtom, theLastHeavyAtomIndex, Order.SINGLE);
            this.itsProtein.getChain(theChainID).setAminoAcidList().get(theIndex).getMolecule().setProperty(this.CDK_MOLECULE_TITLE, theAminoAcidName + theAminoAcidSerial);
            this.itsProtein.getChain(theChainID).setAminoAcidList().get(theIndex).setSerial(theAminoAcidSerial);
            this.itsProtein.getChain(theChainID).setAminoAcidList().get(theIndex).setName(theAminoAcidName);

            this.itsPreviousAminoAcidSerial = theAminoAcidSerial;;
        }
    }
    
    private Integer __getLastHeavyAtomIndex(IAtomContainer theMolecule) {
        for(int ai = theMolecule.getAtomCount() - 1; ai >= 0; ai--) {
            if(!theMolecule.getAtom(ai).getSymbol().equals(this.HYDROGEN_SYMBOL))  {
                return ai;
            }
        }
        
        return null;
    }
    
    private void __inputAtomInformationUsingPreviousInformation(String theFileString) {
        String theAminoAcidSerial = theFileString.substring(this.AMINO_ACID_INDEX_START_INDEX_IN_ATOM, this.AMINO_ACID_INDEX_END_INDEX_IN_ATOM).trim();
        String theChainID = theFileString.substring(this.CHAIN_ID_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.CHAIN_ID_START_INDEX_IN_ATOM_AND_HETERO_ATOM + 1);
        Integer theAtomIndex = Integer.parseInt(theFileString.substring(this.ATOM_INDEX_START_INDEX_IN_ATOM, this.ATOM_INDEX_END_INDEX_IN_ATOM).trim());
        IAtom theAtom = this.__getAtomUsingFileString(theFileString);

        
        if (theAtomIndex.equals(this.FIRST_ATOM_INDEX) || !this.itsPreviousChainID.equals(theChainID)) {
            this.itsPreviousAminoAcidIndex = this.__getAminoAcidIndex(theAminoAcidSerial, theChainID);
            this.itsProtein.getChain(theChainID).getAminoAcidList().get(this.itsPreviousAminoAcidIndex).setMolecule().addAtom(theAtom);
            this.itsPreviousAminoAcidSerial = theAminoAcidSerial;
            this.itsPreviousChainID = theChainID;
        } else if (this.itsPreviousAminoAcidSerial.equals(theAminoAcidSerial)) {
            this.itsProtein.getChain(theChainID).getAminoAcidList().get(this.itsPreviousAminoAcidIndex).setMolecule().addAtom(theAtom);
        } else {
            this.itsPreviousAminoAcidIndex = this.__getAminoAcidIndex(theAminoAcidSerial, theChainID);
            this.itsProtein.getChain(theChainID).getAminoAcidList().get(this.itsPreviousAminoAcidIndex).setMolecule().addAtom(theAtom);
            this.itsPreviousAminoAcidSerial = theAminoAcidSerial;
        }
    }

    private void __inputAtomInformationOnlyAtomPart(String theFileString) throws IOException {
        String theAminoAcidSerial = theFileString.substring(this.AMINO_ACID_INDEX_START_INDEX_IN_ATOM, this.AMINO_ACID_INDEX_END_INDEX_IN_ATOM).trim();
        String theChainID = theFileString.substring(this.CHAIN_ID_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.CHAIN_ID_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim();
        Integer theAtomIndex = Integer.parseInt(theFileString.substring(this.ATOM_INDEX_START_INDEX_IN_ATOM, this.ATOM_INDEX_END_INDEX_IN_ATOM).trim());
        String theAminoAcidName = theFileString.substring(this.AMINO_ACID_NAME_START_INDEX, this.AMINO_ACID_NAME_END_INDEX);
        IAtom theAtom = this.__getAtomUsingFileString(theFileString);
        
        if (theAminoAcidName.trim().length() < 3) {
            throw new IOException();
        }

        if (!this.itsChainNameList.contains(theChainID)) {
            Chain theAddingChain = new Chain();
            AminoAcid theAminoAcid = new AminoAcid();

            theAddingChain.setChainName(theChainID);
            this.itsChainNameList.add(theChainID);
            this.itsProtein.setChainList().add(theAddingChain);
            theAminoAcid.setMolecule().addAtom(theAtom);
            theAminoAcid.setMolecule().setProperty(this.CDK_MOLECULE_TITLE, theAminoAcidName + theAminoAcidSerial);
            theAminoAcid.setSerial(theAminoAcidSerial);
            theAminoAcid.setName(theAminoAcidName);
            this.itsProtein.getChain(theChainID).setAminoAcidList().add(theAminoAcid);

            this.itsPreviousAminoAcidSerial = theAminoAcidSerial;
            this.itsPreviousChainID = theChainID;
        } else if (this.itsPreviousAminoAcidSerial.equals(theAminoAcidSerial) && !this.itsProtein.getChain(theChainID.trim()).getAminoAcidList().isEmpty()) {
            int theLastIndex = this.itsProtein.getChain(theChainID.trim()).getAminoAcidList().size() - 1;

            this.itsProtein.getChain(theChainID).getAminoAcidList().get(theLastIndex).setMolecule().addAtom(theAtom);

        } else {
            int theIndex = this.itsProtein.getChain(theChainID).getAminoAcidList().size();

            this.itsProtein.getChain(theChainID).setAminoAcidList().add(new AminoAcid());
            this.itsProtein.getChain(theChainID).setAminoAcidList().get(theIndex).setMolecule().addAtom(theAtom);
            this.itsProtein.getChain(theChainID).setAminoAcidList().get(theIndex).getMolecule().setProperty(this.CDK_MOLECULE_TITLE, theAminoAcidName + theAminoAcidSerial);
            this.itsProtein.getChain(theChainID).setAminoAcidList().get(theIndex).setSerial(theAminoAcidSerial);
            this.itsProtein.getChain(theChainID).setAminoAcidList().get(theIndex).setName(theAminoAcidName);

            this.itsPreviousAminoAcidSerial = theAminoAcidSerial;
        }
    }

    private Integer __getAminoAcidIndex(String theAminoAcidSerial, String theChainID) {
        for (int ai = 0, aEnd = this.itsProtein.getChain(theChainID).getAminoAcidList().size(); ai < aEnd; ai++) {
            if (this.itsProtein.getChain(theChainID).getAminoAcidList().get(ai).getSerial().equals(theAminoAcidSerial)) {
                return ai;
            }
        }

        return -1;
    }

    private IAtom __getAtomUsingFileString(String theFileString) {
        String theAtomSymbol = theFileString.substring(this.ATOM_SYMBOL_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.ATOM_SYMBOL_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim();
        String theAtomName = theFileString.substring(this.ATOM_NAME_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.ATOM_NAME_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim();
        String theAtomSerial = theFileString.substring(this.ATOM_SERIAL_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.ATOM_SERIAL_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim();
        Double theX = Double.parseDouble(theFileString.substring(this.X_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.X_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim());
        Double theY = Double.parseDouble(theFileString.substring(this.Y_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.Y_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim());
        Double theZ = Double.parseDouble(theFileString.substring(this.Z_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.Z_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim());
        Double theOccupancy = Double.parseDouble(theFileString.substring(this.OCCUPANCY_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.OCCUPANCY_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim());
        Double theTemperatureFactor = Double.parseDouble(theFileString.substring(this.TEMPERATURE_FACTOR_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.TEMPERATURE_FACTOR_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim());
        IAtom theAtom = new Atom(theAtomSymbol);
        Point3d the3dCoordinate = new Point3d(theX, theY, theZ);

        theAtom.setPoint3d(the3dCoordinate);
        theAtom.setProperty(Protein.OCCUPANCY_IN_ATOM, String.format("%.2f", theOccupancy));
        theAtom.setProperty(Protein.TEMPERATURE_FACTOR, String.format("%.2f", theTemperatureFactor));
        theAtom.setProperty(this.ATOM_NAME, theAtomName);
        theAtom.setProperty(this.ATOM_SERIAL, theAtomSerial);

        return theAtom;
    }

    private void __readHeteroAtomPart(String theFileString) {
        String theHeteroMoleculeID = theFileString.substring(this.HETERO_MOLECULE_ID_START_INDEX_IN_HETERO_ATOM, this.HETERO_MOLECULE_ID_END_INDEX_IN_HETERO_ATOM).trim();
        String theChainID = theFileString.substring(this.CHAIN_ID_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.CHAIN_ID_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim();

        if (!this.itsChainNameList.contains(theChainID)) {
            //System.out.print("AA ");
            this.__inputNewChain(theChainID);
            this.itsChainNameList.add(theChainID);
        }

        if (theHeteroMoleculeID.equals(this.WATER_ID)) {
            //System.out.println("BB " + theFileString);
            this.__inputWaterMolecule(theFileString);
        } else if (!this.itsProtein.getChain(theChainID).getHeteroMoleculeList().isEmpty()) {
            //System.out.println("CC " + theFileString);
            this.__inputHeteroAtomUsingPreviousInformation(theFileString);
        } else {
            //System.out.println("DD " + theFileString);
            this.__inputHeteroAtomOnlyAtomPart(theFileString);
        }
    }

    private void __inputNewChain(String theChainName) {
        Chain theNewChain = new Chain();

        theNewChain.setChainName(theChainName);
        this.itsProtein.setChainList().add(theNewChain);

    }

    private void __inputWaterMolecule(String theFileString) {
        String theHeteroMoleculeSerial = theFileString.substring(this.HETERO_MOLECULE_SERIAL_START_INDEX_IN_HETERO_ATOM, this.HETERO_MOLECULE_SERIAL_END_INDEX_IN_HETERO_ATOM).trim();
        IAtom theAtom = this.__getAtomUsingFileString(theFileString);
        IAtomContainer theWaterMolecule = this.__getWaterMolecule();

        theWaterMolecule.setProperty(Protein.HETERO_MOLECULE_SERIAL_KEY, theHeteroMoleculeSerial);
        theWaterMolecule.addAtom(theAtom);
        this.__addHydrogen(theWaterMolecule);
        this.itsProtein.setWaterMoleculeSet().addAtomContainer(theWaterMolecule);
    }

    private void __inputHeteroAtomUsingPreviousInformation(String theFileString) {
        String theChainID = theFileString.substring(this.CHAIN_ID_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.CHAIN_ID_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim();
        String theHeteroMoleculeSerial = theFileString.substring(this.HETERO_MOLECULE_SERIAL_START_INDEX_IN_HETERO_ATOM, this.HETERO_MOLECULE_SERIAL_END_INDEX_IN_HETERO_ATOM).trim();
        IAtom theAtom = this.__getAtomUsingFileString(theFileString);
        boolean theHasMolecule = false;

        for (IAtomContainer theMolecule : this.itsProtein.getChain(theChainID).getHeteroMoleculeList().atomContainers()) {
            if (theMolecule.getProperty(Protein.HETERO_MOLECULE_SERIAL_KEY).toString().equals(theHeteroMoleculeSerial)) {
                theMolecule.addAtom(theAtom);
                theHasMolecule = true;
                break;
            }
        }

        if (!theHasMolecule) {
            this.__inputHeteroAtomOnlyAtomPart(theFileString);
        }
    }

    private void __inputHeteroAtomOnlyAtomPart(String theFileString) {
        String theChainID = theFileString.substring(this.CHAIN_ID_START_INDEX_IN_ATOM_AND_HETERO_ATOM, this.CHAIN_ID_END_INDEX_IN_ATOM_AND_HETERO_ATOM).trim();
        String theHeteroMoleculeSerial = theFileString.substring(this.HETERO_MOLECULE_SERIAL_START_INDEX_IN_HETERO_ATOM, this.HETERO_MOLECULE_SERIAL_END_INDEX_IN_HETERO_ATOM).trim();
        String theHeteroMoleculeName = theFileString.substring(this.HETERO_MOLECULE_ID_START_INDEX_IN_HETERO_ATOM, this.HETERO_MOLECULE_ID_END_INDEX_IN_HETERO_ATOM);
        IAtom theAtom = this.__getAtomUsingFileString(theFileString);
        IAtomContainer theMolecule = new AtomContainer();

        theMolecule.addAtom(theAtom);
        theMolecule.setProperty(Protein.HETERO_MOLECULE_SERIAL_KEY, theHeteroMoleculeSerial);
        theMolecule.setProperty(this.CDK_MOLECULE_TITLE, theHeteroMoleculeName);
        theMolecule.setProperty(Protein.HETERO_MOLECULE_CHAIN_ID_KEY, theChainID);

        this.itsProtein.getChain(theChainID).setHeteroMoleculeList().addAtomContainer(theMolecule);
    }

    private void __addHydrogen(IAtomContainer theMolecule) {
        try {
            CDKAtomTypeMatcher theMatcher = CDKAtomTypeMatcher.getInstance(theMolecule.getBuilder());

            for (IAtom theAtomInMolecule : theMolecule.atoms()) {
                IAtomType theType = theMatcher.findMatchingAtomType(theMolecule, theAtomInMolecule);
                AtomTypeManipulator.configure(theAtomInMolecule, theType);
            }

            CDKHydrogenAdder theAdder = CDKHydrogenAdder.getInstance(theMolecule.getBuilder());
            theAdder.addImplicitHydrogens(theMolecule);

        } catch (CDKException ex) {
            System.err.println("Adding Hydrogen Error!!");
        }
    }

    private IAtomContainer __getWaterMolecule() {
        IAtomContainer theWaterMolecule = new AtomContainer();

        theWaterMolecule.setProperty(Protein.HETERO_MOLECULE_ID_KEY, this.WATER_ID);
        theWaterMolecule.setProperty(Protein.HETERO_MOLECULE_FORMULA_KEY, this.WATER_FORMULA);
        theWaterMolecule.setProperty(Protein.HETERO_MOLECULE_NAME_KEY, this.WATER);

        return theWaterMolecule;
    }

    private void __readHeteroAtomConnectionPart() {
        for (Chain theChain : this.itsProtein) {
            for (IAtomContainer theMolecule : theChain.getHeteroMoleculeList().atomContainers()) {
                try {
                    URL thePdbLigandUrl = new URL(this.PDB_HETERO_MOLECULE_URL_START + theMolecule.getProperty(this.MOLECULE_NAME_KEY).toString() + this.PDB_HETERO_MOLECULE_URL_END);
                    InputStream theInputStream = thePdbLigandUrl.openConnection().getInputStream();
                    IAtomContainer thePDBTemplateLigand = SDFReader.openMoleculeFile(theInputStream).getAtomContainer(this.FIRST_INDEX);
                    
                    for (IBond theBond : thePDBTemplateLigand.bonds()) {
                        int theFirstAtomNumber = thePDBTemplateLigand.getAtomNumber(theBond.getAtom(this.FIRST_INDEX));
                        int theSecondAtomNumber = thePDBTemplateLigand.getAtomNumber(theBond.getAtom(this.SECOND_INDEX));

                        if (theFirstAtomNumber < theMolecule.getAtomCount() && theSecondAtomNumber < theMolecule.getAtomCount()) {
                            theMolecule.addBond(theFirstAtomNumber, theSecondAtomNumber, theBond.getOrder());
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("__readHeteroAtomConnectionPart in PDBReader");
                }
            }
        }
    }

    private List<String> __getBoundedAtomSerialList(String theFileString) {
        List<String> theBoundedAtomSerialList = new ArrayList<>();
        String theString = theFileString.trim().substring(this.HETERO_MOLECULE_CONNECTION.length());

        for (int vi = 0, vEnd = theString.length() / this.ATOM_SERIAL_LENGTH; vi < vEnd; vi++) {
            theBoundedAtomSerialList.add(theString.substring(this.ATOM_SERIAL_LENGTH * vi, this.ATOM_SERIAL_LENGTH * (vi + 1)).trim());
        }

        return theBoundedAtomSerialList;
    }

    private boolean __containAtomSerial(IAtomContainer theMolecule, String theAtomSerial) {
        for (IAtom theAtom : theMolecule.atoms()) {
            if (theAtom.getProperty(this.ATOM_SERIAL).toString().equals(theAtomSerial)) {
                return true;
            }
        }

        return false;
    }

    private IAtom __getAtomContainAtomSerial(IAtomContainer theMolecule, String theAtomSerial) {
        for (IAtom theAtom : theMolecule.atoms()) {
            if (theAtom.getProperty(this.ATOM_SERIAL).toString().equals(theAtomSerial)) {
                return theAtom;
            }
        }

        return null;
    }

    private void __inputBondInformation(IAtomContainer theMolecule, String theStandardAtomSerial, List<String> theBoundedAtomSerialList) {
        IAtom theStandardAtom = this.__getAtomContainAtomSerial(theMolecule, theStandardAtomSerial);

        for (String theBoundedAtomSerial : theBoundedAtomSerialList) {
            IAtom theBoundedAtom = this.__getAtomContainAtomSerial(theMolecule, theBoundedAtomSerial);

            if (Integer.parseInt(theStandardAtomSerial) < Integer.parseInt(theBoundedAtomSerial) && theBoundedAtom != null) {
                theMolecule.addBond(new Bond(theStandardAtom, theBoundedAtom, Order.SINGLE));
            }
        }
    }
}
