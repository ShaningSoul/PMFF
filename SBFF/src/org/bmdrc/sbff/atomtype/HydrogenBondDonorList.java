/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.atomtype;

import java.util.ArrayList;
import java.util.List;
import org.openscience.cdk.interfaces.IAtom;

/**
 * Atom Type Description <br>
 * 0 : No matched atom type <br>
 * 1 : Amide Hydrogen <br>
 * 2 : Hydrogen in CO2H <br>
 * 3 : Carbonyl carbon in carboxylic group <br>
 * 4 : Carbonyl carbon in amide <br>
 * 5 : Carbonyl oxygen in carboxylic group <br>
 * 6 : Carbonyl oxygen in amide <br>
 * 7 : sp3 oxygen in CO2H <br>
 * 8 : Nitrogen in amide <br>
 * ---------Update Atom Type-------------- <br>
 * 9 : Hydrogen in other alcohol <br>
 * 10: Hydrogen in primary amine <br>
 * 11: Hydrogen in secondary amine <br>
 * 12: Nitrogen in praimary amine <br>
 * 13: Nitrogen in secondary amine <br>
 * 14: Oxygen in other alcohol <br>
 * 15: Carbon bounded primary amine <br> 
 * 16: Carbon bounded secondary amine <br>
 * 17: Carbon bounded alcohol <br>
 * 
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class HydrogenBondDonorList {

    private List<Integer> itsHydrogenBondDonorTypeList;

    public HydrogenBondDonorList() {
        this.__initializeHydrogenBondDonorTypeList();
    }

    private void __initializeHydrogenBondDonorTypeList() {
        this.itsHydrogenBondDonorTypeList = new ArrayList<>();
        
        this.itsHydrogenBondDonorTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_AMIDE);
        this.itsHydrogenBondDonorTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_CO2H);
        this.itsHydrogenBondDonorTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_OTHER_ALCHOL);
        this.itsHydrogenBondDonorTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_PRIMARY_AMINE);
        this.itsHydrogenBondDonorTypeList.add(HydrogenBondAtomTypeGenerator.HYDROGEN_IN_SECONDARY_AMINE);
    }
    
    public boolean isHydrogenBondDonor(IAtom theAtom) {
        Integer theHydrogenBondType = (Integer)theAtom.getProperty(HydrogenBondAtomTypeGenerator.HYDROGEN_BOND_TYPE_KEY);
        
        return this.itsHydrogenBondDonorTypeList.contains(theHydrogenBondType);
    }
}
