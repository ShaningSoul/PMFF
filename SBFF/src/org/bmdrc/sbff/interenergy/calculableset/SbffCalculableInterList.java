/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.sbff.interenergy.calculableset;

import java.util.List;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.sbff.parameter.SbffInterParameterSet;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 03. 04
 */
public class SbffCalculableInterList {

    private CalculableElectroStaticList itsCalculableElectroStaticList;
    private CalculableHydrogenBondList itsCalculableHydrogenBondList;
    private CalculableNonbondingList itsCalculableNonbondingList;

    public SbffCalculableInterList(IAtomContainer theMolecule, IAtomContainer theUnitMolecule, SbffInterParameterSet theParameterSet,
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap, double theDielectricConstant, List<Integer> theNotCalculateAtomNumberList) {
        TopologicalDistanceMatrix theTopologicalDistanceMatrix = theMoveTogetherAtomNumberMap.getTopologicalDistanceMatrix();
        
        this.itsCalculableElectroStaticList = new CalculableElectroStaticList(theMolecule, theTopologicalDistanceMatrix, theNotCalculateAtomNumberList);
        this.itsCalculableHydrogenBondList = new CalculableHydrogenBondList(theMolecule, theParameterSet.getHydrogenBondParameterSet(), this.itsCalculableElectroStaticList,
                theNotCalculateAtomNumberList);
        this.itsCalculableNonbondingList = new CalculableNonbondingList(theMolecule, theParameterSet.getNonbondParameterSet(), this.itsCalculableElectroStaticList,
                this.itsCalculableHydrogenBondList, theNotCalculateAtomNumberList);

    }

    public CalculableElectroStaticList getCalculableElectroStaticList() {
        return itsCalculableElectroStaticList;
    }

    public void setCalculableElectroStaticList(CalculableElectroStaticList theCalculableElectroStaticList) {
        this.itsCalculableElectroStaticList = theCalculableElectroStaticList;
    }

    public CalculableHydrogenBondList getCalculableHydrogenBondList() {
        return itsCalculableHydrogenBondList;
    }

    public void setCalculableHydrogenBondList(CalculableHydrogenBondList theCalculableHydrogenBondList) {
        this.itsCalculableHydrogenBondList = theCalculableHydrogenBondList;
    }

    public CalculableNonbondingList getCalculableNonbondingList() {
        return itsCalculableNonbondingList;
    }

    public void setCalculableNonbondingList(CalculableNonbondingList theCalculableNonbondingList) {
        this.itsCalculableNonbondingList = theCalculableNonbondingList;
    }
    
    
}
