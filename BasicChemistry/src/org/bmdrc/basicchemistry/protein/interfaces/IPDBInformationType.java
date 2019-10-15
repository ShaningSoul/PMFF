/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2014, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.protein.interfaces;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public interface IPDBInformationType {

    //constant String variable
    public final String HEADER = "HEADER";                     // First line of the entry, contains PDB ID code, classification, and data of deposition
    public final String OBSOLETED = "OBSLTE";                  // This record acts as a flag in an entry that has been removed("obsoleted") from the PDB's full release.
    public final String COMPOUND = "COMPND";                   // Description of macromolecular contsnts of the entry
    public final String SOURCE = "SOURCE";                     // Biological source of macromolecules in the entry
    public final String EXPERIMENT_METHOD = "EXPDAT";          // Experimental technique used for the structure datermination
    public final String AUTHOR = "AUTHOR";                     // List of contributors
    public final String VALIDATE_DATE = "REVDAT";              // Revision date and related information
    public final String KEYWORD = "KEYWDS";                    // List of keywords describing the macromolecule
    public final String JOURNAL = "JRNL";                      // Literature citation that defines the coordinate set
    public final String REMARK = "REMARK";   // List solvent atoms more than 5 Angstroms from any polymer chain
    public final String DB_REFERENCE = "DBREF";                // Cross-reference links between PDB sequences
    public final String SEQUENCE_DIFFERENCE = "SEQADV";                   // Identification of conflicts between PDB and the named sequence database
    public final String AMINO_ACID_SEQUENCE = "SEQRES";        // Primary sequence of backbone residues
    public final String HETERO_INFORMATION = "HET";            // Identification of non-standard groups heterogens
    public final String HETERO_NAME = "HETNAM";                // Compound name of the heterogens
    public final String HETERO_FORMUL = "FORMUL";              // Chemical formula of non-standard groups
    public final String ALPHA_HELIX_INFORMATION = "HELIX";     // Identification of helical substructures
    public final String BETA_SHEET_INFORMATION = "SHEET";      // Identification of sheet substructures
    public final String ACTIVE_SITE_INFORMATION = "SITE";      // Identification of groups comprising important entity sites
    public final String UNIT_CELL_PARAMETER = "CRYST1";        // Present unit cell Parameter
    public final String FIRST_ORIGX = "ORIGX1";                // Tranformation from orthogonal coordinates to the submitted coordinates
    public final String SECOND_ORIGX = "ORIGX2";               // Tranformation from orthogonal coordinates to the submitted coordinates
    public final String THIRD_ORIGX = "ORIGX3";                // Tranformation from orthogonal coordinates to the submitted coordinates
    public final String FIRST_SCALE = "SCALE1";                // Transformation from orthogonal coordinates to fractional crystallographic coordinates
    public final String SECOND_SCALE = "SCALE2";               // Transformation from orthogonal coordinates to fractional crystallographic coordinates
    public final String THIRD_SCALE = "SCALE3";                // Transformation from orthogonal coordinates to fractional crystallographic coordinates
    public final String ATOM = "ATOM";                         // Atomic coordinate records for standard groups
    public final String HETERO_ATOM = "HETATM";                // Atomic coordinate records for heterogens
    public final String HETERO_MOLECULE_CONNECTION = "CONECT"; // connectivity records
    public final String MASTER = "MASTER";                     // Control record for bookkeeping
    public final String END = "END";                           // Last record in the file
    public final String TITLE = "TITLE";
    public final String JOURNAL_TITLE = "TITL";
    public final String JOURNAL_AUTHOR = "AUTH";
    public final String JOURNAL_NAME = "REF";
    public final String JOURNAL_NAME_ID = "REFN";
    public final String JOURNAL_PUBMED_ID = "PMID";
    public final String JOURNAL_PUBLICATION_IDENTIFIER = "DOI";
    public final String ATOM_NAME = "Atom Name";
    public final String ATOM_SERIAL = "Atom Serial";
    public final String WATER = "Water";
    public final String WATER_ID = "HOH";
    public final String WATER_FORMULA = "H2O";
    //constant Remark Serial
    public final String DISTANT_SOLVENT_REMARK_SERIAL = "525";
    //constant Integer variable
    public final Integer RECORD_NAME_START_INDEX = 0;
    public final Integer RECORD_NAME_END_INDEX = 6;
    public final Integer CLASSIFICATION_START_INDEX = 10;
    public final Integer CLASSIFICATION_END_INDEX = 50;
    public final Integer DATE_START_INDEX = 50;
    public final Integer DATE_END_INDEX = 59;
    public final Integer IDCODE_START_INDEX = 62;
    public final Integer IDCODE_END_INDEX = 66;
    public final Integer COMPOUND_INFORMATION_KEY_INDEX = 0;
    public final Integer COMPOUND_INFORMATION_VALUE_INDEX = 1;
    public final Integer SOURCE_INFORMATION_KEY_INDEX = 0;
    public final Integer SOURCE_INFORMATION_VALUE_INDEX = 1;
    public final Integer INFORMATION_START_INDEX = 10;
    public final Integer INFORMATION_TYPE_START_INDEX_IN_JOURNAL = 12;
    public final Integer INFORMATION_TYPE_END_INDEX_IN_JOURNAL = 16;
    public final Integer JOURNAL_PART_INFORMATION_START_INDEX = 19;
    public final Integer AMINO_ACID_SEQUENCE_START_INDEX = 19;
    public final Integer CHAIN_ID_IN_AMINO_ACID_SEQUENCE = 11;
    public final Integer CHAIN_ID_IN_HETERO_INFORMATION = 12;
    public final Integer MOLECULE_ID_START_INDEX_IN_HETERO_INFORMATION = 7;
    public final Integer MOLECULE_ID_END_INDEX_IN_HETERO_INFORMATION = 10;
    public final Integer MOLECULE_SERIAL_START_INDEX_IN_HETERO_INFORMATION = 13;
    public final Integer MOLECULE_SERIAL_END_INDEX_IN_HETERO_INFORMATION = 17;
    public final Integer MOLECULE_ID_START_INDEX_IN_HETERO_NAME = 11;
    public final Integer MOLECULE_ID_END_INDEX_IN_HETERO_NANE = 14;
    public final Integer MOLECULE_NAME_START_INDEX_IN_HETERO_NAME = 15;
    public final Integer MOLECULE_ID_START_INDEX_IN_HETERO_FORMULA = 12;
    public final Integer MOLECULE_ID_END_INDEX_IN_HETERO_FORMULA = 15;
    public final Integer MOLECULE_FORMULA_INFORMATION_START_INDEX_IN_HETERO_FORMULA = 19;
    public final Integer HELIX_ID_START_INDEX_IN_ALPHA_HELIX_INFORMATION = 11;
    public final Integer HELIX_ID_END_INDEX_IN_ALPHA_HELIX_INFORMATION = 14;
    public final Integer HELIX_START_AMINO_ACID_INDEX_START_INDEX_IN_ALPHA_HELIX_INFORMATION = 21;
    public final Integer HELIX_START_AMINO_ACID_INDEX_END_INDEX_IN_ALPHA_HELIX_INFORMATION = 25;
    public final Integer HELIX_END_AMINO_ACID_INDEX_START_INDEX_IN_ALPHA_HELIX_INFORMATION = 33;
    public final Integer HELIX_END_AMINO_ACID_INDEX_END_INDEX_IN_ALPHA_HELIX_INFORMATION = 37;
    public final Integer START_CHAIN_ID_INDEX_IN_ALPHA_HELIX_INFORMATION = 19;
    public final Integer END_CHAIN_ID_INDEX_IN_ALPHA_HELIX_INFORMATION = 31;
    public final Integer SHEET_ID_START_INDEX_IN_BETA_SHEET_INFORMATION = 11;
    public final Integer SHEET_ID_END_INDEX_IN_BETA_SHEET_INFORMATION = 14;
    public final Integer SHEET_START_AMINO_ACID_INDEX_START_INDEX_IN_BETA_SHEET_INFORMATION = 22;
    public final Integer SHEET_START_AMINO_ACID_INDEX_END_INDEX_IN_BETA_SHEET_INFORMATION = 26;
    public final Integer SHEET_END_AMINO_ACID_INDEX_START_INDEX_IN_BETA_SHEET_INFORMATION = 33;
    public final Integer SHEET_END_AMINO_ACID_INDEX_END_INDEX_IN_BETA_SHEET_INFORMATION = 37;
    public final Integer START_CHAIN_ID_INDEX_IN_BETA_SHEET_INFORMATION = 21;
    public final Integer END_CHAIN_ID_INDEX_IN_BETA_SHEET_INFORMATION = 32;
    public final Integer BETA_SHEET_SENSE_START_INDEX_IN_BETA_SHEET_INFORMATION = 38;
    public final Integer BETA_SHEET_SENSE_END_INDEX_IN_BETA_SHEET_INFORMATION = 40;
    public final Integer ACTIVE_SITE_ID_START_INDEX_IN_ACTIVE_SITE_INFORMATION = 11;
    public final Integer ACTIVE_SITE_ID_END_INDEX_IN_ACTIVE_SITE_INFORMATION = 14;
    public final Integer AMINO_ACID_INFORMATION_START_INDEX_IN_ACTIVE_SITE_INFORMATION = 18;
    public final Integer UNIT_CELL_PARAMETER_A_START_INDEX = 6;
    public final Integer UNIT_CELL_PARAMETER_A_END_INDEX = 15;
    public final Integer UNIT_CELL_PARAMETER_B_START_INDEX = 15;
    public final Integer UNIT_CELL_PARAMETER_B_END_INDEX = 24;
    public final Integer UNIT_CELL_PARAMETER_C_START_INDEX = 24;
    public final Integer UNIT_CELL_PARAMETER_C_END_INDEX = 33;
    public final Integer UNIT_CELL_PARAMETER_ALPHA_START_INDEX = 33;
    public final Integer UNIT_CELL_PARAMETER_ALPHA_END_INDEX = 40;
    public final Integer UNIT_CELL_PARAMETER_BETA_START_INDEX = 40;
    public final Integer UNIT_CELL_PARAMETER_BETA_END_INDEX = 47;
    public final Integer UNIT_CELL_PARAMETER_GAMMA_START_INDEX = 47;
    public final Integer UNIT_CELL_PARAMETER_GAMMA_END_INDEX = 54;
    public final Integer X_ORIGIN_START_INDEX = 10;
    public final Integer X_ORIGIN_END_INDEX = 20;
    public final Integer Y_ORIGIN_START_INDEX = 20;
    public final Integer Y_ORIGIN_END_INDEX = 30;
    public final Integer Z_ORIGIN_START_INDEX = 30;
    public final Integer Z_ORIGIN_END_INDEX = 40;
    public final Integer AMINO_ACID_INDEX_START_INDEX_IN_ATOM = 22;
    public final Integer AMINO_ACID_INDEX_END_INDEX_IN_ATOM = 26;
    public final Integer X_START_INDEX_IN_ATOM_AND_HETERO_ATOM = 30;
    public final Integer X_END_INDEX_IN_ATOM_AND_HETERO_ATOM = 38;
    public final Integer Y_START_INDEX_IN_ATOM_AND_HETERO_ATOM = 38;
    public final Integer Y_END_INDEX_IN_ATOM_AND_HETERO_ATOM = 46;
    public final Integer Z_START_INDEX_IN_ATOM_AND_HETERO_ATOM = 46;
    public final Integer Z_END_INDEX_IN_ATOM_AND_HETERO_ATOM = 54;
    public final Integer ATOM_SYMBOL_START_INDEX_IN_ATOM_AND_HETERO_ATOM = 76;
    public final Integer ATOM_SYMBOL_END_INDEX_IN_ATOM_AND_HETERO_ATOM = 78;
    public final Integer OCCUPANCY_START_INDEX_IN_ATOM_AND_HETERO_ATOM = 54;
    public final Integer OCCUPANCY_END_INDEX_IN_ATOM_AND_HETERO_ATOM = 60;
    public final Integer TEMPERATURE_FACTOR_START_INDEX_IN_ATOM_AND_HETERO_ATOM = 60;
    public final Integer TEMPERATURE_FACTOR_END_INDEX_IN_ATOM_AND_HETERO_ATOM = 66;
    public final Integer ATOM_NAME_START_INDEX_IN_ATOM_AND_HETERO_ATOM = 12;
    public final Integer ATOM_NAME_END_INDEX_IN_ATOM_AND_HETERO_ATOM = 16;
    public final Integer CHAIN_ID_START_INDEX_IN_ATOM_AND_HETERO_ATOM = 20;
    public final Integer CHAIN_ID_END_INDEX_IN_ATOM_AND_HETERO_ATOM = 22;
    public final Integer ATOM_SERIAL_START_INDEX_IN_ATOM_AND_HETERO_ATOM = 6;
    public final Integer ATOM_SERIAL_END_INDEX_IN_ATOM_AND_HETERO_ATOM = 11;
    public final Integer HETERO_ATOM_SERIAL_START_INDEX_IN_HETERO_ATOM = 7;
    public final Integer HETERO_ATOM_SERIAL_END_INDEX_IN_HETERO_ATOM = 11;
    public final Integer HETERO_MOLECULE_SERIAL_START_INDEX_IN_HETERO_ATOM = 22;
    public final Integer HETERO_MOLECULE_SERIAL_END_INDEX_IN_HETERO_ATOM = 26;
    public final Integer HETERO_MOLECULE_ID_START_INDEX_IN_HETERO_ATOM = 17;
    public final Integer HETERO_MOLECULE_ID_END_INDEX_IN_HETERO_ATOM = 20;
    public final Integer STARDARD_ATOM_SERIAL_START_INDEX = 6;
    public final Integer STARDARD_ATOM_SERIAL_END_INDEX = 11;
    public final Integer BOUNDED_ATOM_SERIAL_START_INDEX = 11;
    public final Integer UNUSUAL_SOLVENT_SERIAL_START_INDEX = 19;
    public final Integer UNUSUAL_SOLVENT_SERIAL_END_INDEX = 23;
    public final Integer REMARK_SERIAL_START_INDEX = 7;
    public final Integer REMARK_SERIAL_END_INDEX = 10;
    public final Integer INSERTION_RESIDUE_NAME_START_INDEX_IN_SEQUENCE_DIFFERENCE = 12;
    public final Integer INSERTION_RESIDUE_NAME_END_INDEX_IN_SEQUENCE_DIFFERENCE = 15;
    public final Integer INSERTION_RESIDUE_SERIAL_START_INDEX_IN_SEQUENCE_DIFFERENCE = 18;
    public final Integer INSERTION_RESIDUE_SERIAL_END_INDEX_IN_SEQUENCE_DIFFERENCE = 22;
    public final Integer DELETION_RESIDUE_NAME_START_INDEX_IN_SEQUENCE_DIFFERENCE = 39;
    public final Integer DELETION_RESIDUE_NAME_END_INDEX_IN_SEQUENCE_DIFFERENCE = 42;
    public final Integer DELETION_RESIDUE_SERIAL_START_INDEX_IN_SEQUENCE_DIFFERENCE = 43;
    public final Integer DELETION_RESIDUE_SERIAL_END_INDEX_IN_SEQUENCE_DIFFERENCE = 48;
    public final Integer CHAIN_ID_START_INDEX_IN_SEQUENCE_DIFFERENCE = 16;
    public final Integer CHAIN_ID_START_INDEX_IN_DB_REFERENCE = 12;
    public final Integer START_SERIAL_START_INDEX_IN_DB_REFERENCE = 14;
    public final Integer START_SERIAL_END_INDEX_IN_DB_REFERENCE = 18;
    public final Integer CHAIN_ID_START_INDEX_IN_ACTIVE_SITE_INFORMATION = 4;
    public final Integer SERIAL_START_INDEX_IN_ACTIVE_SITE_INFORMATION = 4;
    public final Integer SERIAL_END_INDEX_IN_ACTIVE_SITE_INFORMATION = 8;
    public final Integer ATOM_INDEX_START_INDEX_IN_ATOM = 6;
    public final Integer ATOM_INDEX_END_INDEX_IN_ATOM = 11;
    public final Integer AMINO_ACID_NAME_START_INDEX = 17;
    public final Integer AMINO_ACID_NAME_END_INDEX = 20;
    public final Integer ATOM_SERIAL_LENGTH = 5;
    
}
