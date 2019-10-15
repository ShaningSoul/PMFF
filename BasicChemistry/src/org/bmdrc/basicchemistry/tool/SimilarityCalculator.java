/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.tool;

import java.io.IOException;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SimilarityCalculator {

    public static Double CalculateSimilarity(IAtomContainer theQueryMolecule, IAtomContainer theTargetMolecule) {
        try {
            IAtomContainer theQuery = AtomContainerManipulator.removeHydrogens(theQueryMolecule);
            IAtomContainer theTarget = AtomContainerManipulator.removeHydrogens(theTargetMolecule);
            
            CDKHueckelAromaticityDetector.detectAromaticity(theQuery);
            CDKHueckelAromaticityDetector.detectAromaticity(theTarget);
            
            Isomorphism theCalculator = new Isomorphism(Algorithm.DEFAULT, true);
            theCalculator.init(theQuery, theTarget, true, true);
            
            return theCalculator.getTanimotoAtomSimilarity();
        } catch (IOException ex) {
            System.err.println("Similarity Calculator Error!!");
        } catch (CDKException ex) {
            System.err.println("Similarity Calculator Error!!");
        }
        
        return null;
    }
}
