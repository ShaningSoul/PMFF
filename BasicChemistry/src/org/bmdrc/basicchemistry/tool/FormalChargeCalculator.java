/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.tool;

import org.bmdrc.basicchemistry.interfaces.IAtomSymbol;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class FormalChargeCalculator {

    public static void inputFormalCharge(IAtomContainer theMolecule) {
        for(IAtom theAtom : theMolecule.atoms()) {
            switch(theAtom.getSymbol()) {
                case IAtomSymbol.C_SYMBOL:
                    theAtom.setFormalCharge((int)Math.round(theMolecule.getBondOrderSum(theAtom)) - 4);
                    break;
                case IAtomSymbol.H_SYMBOL:
                    theAtom.setFormalCharge(1 - (int)Math.round(theMolecule.getBondOrderSum(theAtom)));
                    break;
                case IAtomSymbol.O_SYMBOL:
                    theAtom.setFormalCharge((int)Math.round(theMolecule.getBondOrderSum(theAtom)) - 2);
                    break;
                case IAtomSymbol.N_SYMBOL:
                    theAtom.setFormalCharge((int)Math.round(theMolecule.getBondOrderSum(theAtom)) - 3);
                    break;
                default:
            }
        }
    }
}
