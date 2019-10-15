/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class XyzReader implements Serializable {
    private static final long serialVersionUID = -4870301699164904340L;

    public static IAtomContainerSet openXyzFile(File theXyzFile) {
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();
        try {
            BufferedReader theFileReader = new BufferedReader(new FileReader(theXyzFile));
            String theFileString = new String();
            IAtomContainer theMolecule = new AtomContainer();
            Integer theNumberOfAtom = null;
            
            while((theFileString = theFileReader.readLine()) != null) {
                if(theNumberOfAtom == null) {
                    theNumberOfAtom = XyzReader.__getNumberOfAtom(theFileString);
                } else {
                    
                }
            }
        } catch (IOException ex) {
            System.err.println("XyzReader Error!!");
            return null;
        }
        
        return theMoleculeSet;
    }
    
    private static Integer __getNumberOfAtom(String theFileString) {
        return Integer.parseInt(theFileString.trim().split(" ")[0]);
    }
    
    private static void __addAtomInformation(String theFileString) {
        
    }
}
