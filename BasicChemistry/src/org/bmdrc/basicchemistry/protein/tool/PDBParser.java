/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.protein.tool;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.reader.PDBReader;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class PDBParser {

    //constant String variable
    private static final String PDB_FILE_START_URL = "www.rcsb.org/pdb/files/";
    private static final String LIGAND_FILE_START_URL = "www.rcsb.org/pdb/files/ligand/";
    private static final String PDB_SUFFIX = ".pdb";
    private static final String SDF_SUFFIX = ".sdf";
    //constant Integer variable
    private static final int FIRST_INDEX = 0;

    
    public static Protein parseProtein(String thePdbID) {
        try {
            URL theUrl = new URL(PDBParser.PDB_FILE_START_URL + thePdbID + PDBParser.PDB_SUFFIX);
            PDBReader thePdbReader = new PDBReader();
            
            return thePdbReader.readPDBFile(theUrl.openConnection().getInputStream());
        } catch (MalformedURLException ex) {
            System.err.println("URL Error in PDB Parser");
        } catch (IOException ex) {
            System.err.println("URL Connection Error in PDB Parser");
        }
        
        return null;
    }
    
    public static IAtomContainer parserLigand(String theLigandID) {
        try {
            URL theUrl = new URL(PDBParser.LIGAND_FILE_START_URL + theLigandID + PDBParser.SDF_SUFFIX);
            IAtomContainerSet theMoleculeSet = SDFReader.openMoleculeFile(theUrl.openConnection().getInputStream());
            
            return theMoleculeSet.getAtomContainer(PDBParser.FIRST_INDEX);
        } catch (MalformedURLException ex) {
            System.err.println("URL Error in PDB Parser");
        } catch (IOException ex) {
            System.err.println("URL Connection Error in PDB Parser");
        }
        
        return null;
    }
}
