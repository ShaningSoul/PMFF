/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.interenergy.calculableset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableList;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableElectroStaticList extends AbstractCalculableList<CalculableElectroStaticSet> implements Serializable {

    private static final long serialVersionUID = 5201303846321702789L;

    public CalculableElectroStaticList(IAtomContainer theMolecule) {
        this(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }

    public CalculableElectroStaticList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this(theMolecule, theTopologicalDistanceMatrix, new ArrayList<Integer>());
    }

    public CalculableElectroStaticList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new TopologicalDistanceMatrix(theMolecule), theNotCalculateAtomNumberList);
    }

    public CalculableElectroStaticList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix,
            List<Integer> theNotCalculateAtomNumberList) {
        super(theMolecule, theNotCalculateAtomNumberList);
        
        this.__initializeCalculableElectroStaticList(theTopologicalDistanceMatrix);
    }

    public CalculableElectroStaticList(CalculableElectroStaticList theCalculableElectroStaticList) {
        super(theCalculableElectroStaticList);
    }

    private void __initializeCalculableElectroStaticList(TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        int theAtomCount = this.itsMolecule.getAtomCount();

        for (int fi = 0, fEnd = theAtomCount - 1; fi < fEnd; fi++) {
            for (int si = fi + 1, sEnd = theAtomCount; si < sEnd; si++) {
                if (this.__isCalculableElectroStaticSet(fi, si, theTopologicalDistanceMatrix)) {
                    CalculableElectroStaticSet theCalculableElectroStaticSet = new CalculableElectroStaticSet(fi, si);

                    this.add(theCalculableElectroStaticSet);
                }
            }
        }
    }

    private boolean __isCalculableElectroStaticSet(int theFirstAtomIndex, int theSecondAtomIndex, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        if (this.itsNotCalculateAtomNumberList != null && this.itsNotCalculateAtomNumberList.contains(theFirstAtomIndex) && 
                this.itsNotCalculateAtomNumberList.contains(theSecondAtomIndex)) {
            return false;
        } 

        return theTopologicalDistanceMatrix.getDistance(theFirstAtomIndex, theSecondAtomIndex) == TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR;
    }
}
