/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.tool;

import java.io.Serializable;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IRingSet;

/**
 * generate aromatic bond and input property in molecule<br>
 * the property key is implemented in class (AROMATIC_BOND_KEY)
 * 
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 15
 */
public class AromaticBondGenerator implements Serializable {

    private static final long serialVersionUID = 4561738077785937560L;
    public static String AROMATIC_BOND_KEY = "Aromatic_Bond";
    public static boolean AROMATIC_BOND = true;
    public static boolean NON_AROMATIC_BOND = false;
    
    /**
     * check whether the molecule contain aromatic bond and input property in molecule<br>
     * the key is static variable AROMATIC_BOND_KEY<br>
     * <br>
     * this function is used to ring perception algorithm<br>
     * if the molecule is big, the calculation time is expensive<br>
     * 
     * @param theMolecule IAtomContainer variable
     * @throws CDKException error implmented in cdk library
     */
    public static void generateAromaticBondProperty(IAtomContainer theMolecule) throws CDKException {
        RingPerception theRingPerception = new RingPerception();
        IRingSet theAromaticRingSet = theRingPerception.recognizeAromaticRing((IAtomContainer)theMolecule);
        
        AromaticBondGenerator.__initializeAromaticBondproperty(theMolecule);
        
        for(IAtomContainer theRing : theAromaticRingSet.atomContainers()) {
            for(IBond theBond : theRing.bonds()) {
                theBond.setProperty(AromaticBondGenerator.AROMATIC_BOND_KEY, true);
            }
        }
    }
    
    /**
     * initialize aromatic bond property<br>
     * the initial aromatic bond property is false
     * 
     * @param theMolecule template molecule
     */
    private static void __initializeAromaticBondproperty(IAtomContainer theMolecule) {
        for(IBond theBond : theMolecule.bonds()) {
            theBond.setProperty(AromaticBondGenerator.AROMATIC_BOND_KEY, false);
        }
    }
}
