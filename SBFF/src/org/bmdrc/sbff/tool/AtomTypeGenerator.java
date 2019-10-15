/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.tool;

import org.bmdrc.basicchemistry.tool.HydrogenAdder;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.sbff.atomtype.HydrogenBondAtomTypeGenerator;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class AtomTypeGenerator {

    public static void generateAtomType(IAtomContainer theMolecule) {
        try {
            HydrogenAdder.addHydrogen(theMolecule);
            
            MM3AtomTypeGenerator theMM3AtomTypeGenerator = new MM3AtomTypeGenerator();
            HydrogenBondAtomTypeGenerator theHydrogenBondAtomTypeGenerator = new HydrogenBondAtomTypeGenerator();
            MpeoeAtomTypeGenerator theMpeoeAtomType = new MpeoeAtomTypeGenerator();
            
            theMM3AtomTypeGenerator.generateAtomType(theMolecule);
            theHydrogenBondAtomTypeGenerator.inputHydrogenBondAtomType(theMolecule, new TopologicalDistanceMatrix(theMolecule));
            theMpeoeAtomType.inputMpeoeAtomType(theMolecule);
        } catch (CDKException ex) {
            System.err.println("Hydrogen Adder error in atom type generator");
        }
    }
}
