/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.intraenergy.calculableset;

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
public class CalculableIntraElectroStaticList extends AbstractCalculableList<CalculableIntraElectroStaticSet> implements Serializable {

    private static final long serialVersionUID = 5201303846321702789L;

    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;
    private Double itsMaximumDistance;
    private Integer itsMinimumTopologicalDistance;
    //constant Integer variable
    private final int DEFAULT_MINIMUM_TOPOLOGICAL_DISTANCE = 3;

    public CalculableIntraElectroStaticList(IAtomContainer theMolecule) {
        this(theMolecule, new TopologicalDistanceMatrix(theMolecule), new ArrayList<Integer>());
    }

    public CalculableIntraElectroStaticList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this(theMolecule, theTopologicalDistanceMatrix, new ArrayList<Integer>());
    }

    public CalculableIntraElectroStaticList(IAtomContainer theMolecule, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, new TopologicalDistanceMatrix(theMolecule), theNotCalculateAtomNumberList);
    }

    public CalculableIntraElectroStaticList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix, List<Integer> theNotCalculateAtomNumberList) {
        this(theMolecule, theTopologicalDistanceMatrix, theNotCalculateAtomNumberList, false, 1);
    }

    public CalculableIntraElectroStaticList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix, List<Integer> theNotCalculateAtomNumberList, boolean theIsUsedMultiThread, int theNumberOfThread) {
        super(theMolecule, theNotCalculateAtomNumberList);
        this.itsTopologicalDistanceMatrix = theTopologicalDistanceMatrix;

        this.__initializeCalculableElectroStaticList();

        this.itsTopologicalDistanceMatrix = null;
    }

    public TopologicalDistanceMatrix getTopologicalDistanceMatrix() {
        return itsTopologicalDistanceMatrix;
    }

    private void __initializeCalculableElectroStaticList() {
        for (int fi = 0, fEnd = this.itsMolecule.getAtomCount() - 1; fi < fEnd; fi++) {
            for (int si = fi + 1, sEnd = this.itsMolecule.getAtomCount(); si < sEnd; si++) {
                if (this.__isCalculableElectroStaticSet(fi, si)) {
                    CalculableIntraElectroStaticSet theCalculableElectroStaticSet = new CalculableIntraElectroStaticSet(fi, si);

                    this.add(theCalculableElectroStaticSet);
                }
            }
        }
    }

    private boolean __isCalculableElectroStaticSet(int theFirstAtomIndex, int theSecondAtomIndex) {
        if (this.itsTopologicalDistanceMatrix.getDistance(theFirstAtomIndex, theSecondAtomIndex) < this.DEFAULT_MINIMUM_TOPOLOGICAL_DISTANCE) {
            return false;
        } else if (this.itsNotCalculateAtomNumberList != null && this.itsNotCalculateAtomNumberList.contains(theFirstAtomIndex)
                && this.itsNotCalculateAtomNumberList.contains(theSecondAtomIndex)) {
            return false;
        }

        return true;
    }
}
