/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.tool;

import java.io.Serializable;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

/**
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 */
public class HydrogenAdder implements Serializable {
    private static final long serialVersionUID = -6248468850959831325L;

    public static IAtomContainer addHydrogen(IAtomContainer theMolecule) throws CDKException {
        HydrogenAdder.__setAtomType(theMolecule);
        CDKHydrogenAdder theHydrogenAdder = CDKHydrogenAdder.getInstance(theMolecule.getBuilder());

        for (IAtom theAtom : theMolecule.atoms()) {
            theHydrogenAdder.addImplicitHydrogens(theMolecule, theAtom);
        }

        AtomContainerManipulator.convertImplicitToExplicitHydrogens(theMolecule);
        
        return theMolecule;
    }

    private static void __setAtomType(IAtomContainer theMolecule) throws CDKException {
        CDKAtomTypeMatcher theMatcher = CDKAtomTypeMatcher.getInstance(theMolecule.getBuilder());

        for (IAtom theAtom : theMolecule.atoms()) {
            try {
                IAtomType theAtomType = theMatcher.findMatchingAtomType(theMolecule, theAtom);

                AtomTypeManipulator.configure(theAtom, theAtomType);
            } catch (CDKException ex) {
                throw new CDKException(null);
            } catch (IllegalArgumentException ex) {
                throw new CDKException(null);
            } catch (NullPointerException ex) {
                throw new CDKException(null);
            }
        }
    }
}
