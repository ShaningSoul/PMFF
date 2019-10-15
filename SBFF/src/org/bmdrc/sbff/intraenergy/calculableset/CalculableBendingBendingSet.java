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
import java.util.Collections;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CalculableBendingBendingSet extends AbstractCalculableSet implements Serializable {

    private static final long serialVersionUID = -2540973346397636158L;

    public enum StringIndex {
        CenterAtom, FirstAtomInFirstBend, SecondAtomInFirstBend, FirstAtomInSecondBend, SecondAtomInSecondBend, StandardAngleinFirstBend, BendingBendingConstantInFirstBend,
        BendingConstantInFirstBend, StandardAngleInSecondBend, BendingConstantInSecondBend, BendingBendingConstantInSecondBend;
    }

    private Integer itsOverlappedAtomIndex;
    private CalculableBendingSet itsFirstCalculableBendingSet;
    private CalculableBendingSet itsSecondCalculableBendingSet;

    //constant Integer variable
    public static final int CENTER_ATOM = 0;
    public static final int FIRST_ATOM_IN_FIRST_BEND = 1;
    public static final int SECOND_ATOM_IN_FIRST_BEND = 2;
    public static final int FIRST_ATOM_IN_SECOND_BEND = 3;
    public static final int SECOND_ATOM_IN_SECOND_BEND = 4;
    public static final int FIRST_BEND = 0;
    public static final int SECOND_BEND = 1;
    public static final int TOTAL_NUMBER_OF_BEND = 2;
    //constant String variable
    public static final String OVERLAP_ATOM_GRADIENT_KEY = "Overlap atom gradient";
    public static final String PREVIOUS_OVERLAP_ATOM_GRADIENT_KEY = "Previous overlap atom gradient";

    public CalculableBendingBendingSet(CalculableBendingSet theFirstCalculableBendingSet, CalculableBendingSet theSecondCalculableBendingSet) {
        super();
        this.__generateAtomIndexList(theFirstCalculableBendingSet, theSecondCalculableBendingSet);
        this.__generateOverlappedAtomIndex();
        this.itsFirstCalculableBendingSet = new CalculableBendingSet(theFirstCalculableBendingSet);
        this.itsSecondCalculableBendingSet = new CalculableBendingSet(theSecondCalculableBendingSet);
    }

    private void __generateAtomIndexList(CalculableBendingSet theFirstCalculableBendingSet, CalculableBendingSet theSecondCalculableBendingSet) {
        this.itsAtomIndexList.add(theFirstCalculableBendingSet.getCenterAtomIndex());
        this.itsAtomIndexList.add(theFirstCalculableBendingSet.getFirstAtomIndex());
        this.itsAtomIndexList.add(theFirstCalculableBendingSet.getSecondAtomIndex());
        this.itsAtomIndexList.add(theSecondCalculableBendingSet.getFirstAtomIndex());
        this.itsAtomIndexList.add(theSecondCalculableBendingSet.getSecondAtomIndex());
    }

    private void __generateOverlappedAtomIndex() {
        for (Integer theAtomIndex : this.itsAtomIndexList) {
            if (Collections.frequency(this.itsAtomIndexList, theAtomIndex) > 1) {
                this.itsOverlappedAtomIndex = theAtomIndex;
            }
        }
    }

    public Integer getOverlappedAtomIndex() {
        return itsOverlappedAtomIndex;
    }

    public Integer getCenterAtomIndex() {
        return this.itsAtomIndexList.get(CalculableBendingBendingSet.CENTER_ATOM);
    }

    public Integer getFirstAtomIndexInFirstBend() {
        return this.itsAtomIndexList.get(CalculableBendingBendingSet.FIRST_ATOM_IN_FIRST_BEND);
    }

    public Integer getSecondAtomIndexInFirstBend() {
        return this.itsAtomIndexList.get(CalculableBendingBendingSet.SECOND_ATOM_IN_FIRST_BEND);
    }

    public Integer getFirstAtomIndexInSecondBend() {
        return this.itsAtomIndexList.get(CalculableBendingBendingSet.FIRST_ATOM_IN_SECOND_BEND);
    }

    public Integer getSecondAtomIndexInSecondBend() {
        return this.itsAtomIndexList.get(CalculableBendingBendingSet.SECOND_ATOM_IN_SECOND_BEND);
    }

    public CalculableBendingSet getFirstCalculableBendingSet() {
        return itsFirstCalculableBendingSet;
    }

    public CalculableBendingSet getSecondCalculableBendingSet() {
        return itsSecondCalculableBendingSet;
    }

    public boolean containAtomIndex(int theAtomIndex) {
        return this.itsAtomIndexList.contains(theAtomIndex);
    }

    public boolean containOverlappedAtomIndex() {
        return this.itsOverlappedAtomIndex != null;
    }

    public List<Integer> getIndexInOverlappedAtom() {
        List<Integer> theIndexList = new ArrayList<>();

        for (int ai = 0, aEnd = this.itsAtomIndexList.size(); ai < aEnd; ai++) {
            if (this.itsAtomIndexList.get(ai).equals(this.itsOverlappedAtomIndex)) {
                theIndexList.add(ai);
            }
        }

        return theIndexList;
    }

    public boolean containFirstBend(int theAtomIndex) {
        return this.getFirstAtomIndexInFirstBend().equals(theAtomIndex) || this.getSecondAtomIndexInFirstBend().equals(theAtomIndex);
    }

    public boolean containSecondBend(int theAtomIndex) {
        return this.getFirstAtomIndexInSecondBend().equals(theAtomIndex) || this.getSecondAtomIndexInSecondBend().equals(theAtomIndex);
    }

    public boolean containSameBend(int theFirstAtomIndex, int theSecondAtomIndex) {
        if (this.containFirstBend(theFirstAtomIndex) && this.containFirstBend(theSecondAtomIndex)) {
            return true;
        } else if (this.containSecondBend(theFirstAtomIndex) && this.containSecondBend(theSecondAtomIndex)) {
            return true;
        }

        return false;
    }

    public List<Integer> getFirstBendAtomIndexList() {
        List<Integer> theAtomIndexList = new ArrayList<>();

        theAtomIndexList.add(this.getFirstAtomIndexInFirstBend());
        theAtomIndexList.add(this.getCenterAtomIndex());
        theAtomIndexList.add(this.getSecondAtomIndexInFirstBend());

        return theAtomIndexList;
    }

    public List<Integer> getSecondBendAtomIndexList() {
        List<Integer> theAtomIndexList = new ArrayList<>();

        theAtomIndexList.add(this.getFirstAtomIndexInSecondBend());
        theAtomIndexList.add(this.getCenterAtomIndex());
        theAtomIndexList.add(this.getSecondAtomIndexInSecondBend());

        return theAtomIndexList;
    }

    public List<Integer> getSideAtomIndexListRemovedOverlapAtom() {
        List<Integer> theAtomIndexList = new ArrayList<>();

        for (Integer theAtomIndex : this.itsAtomIndexList) {
            if (!theAtomIndex.equals(this.getCenterAtomIndex()) && !theAtomIndex.equals(this.itsOverlappedAtomIndex)) {
                theAtomIndexList.add(theAtomIndex);
            }
        }

        return theAtomIndexList;
    }

    public double getBendingBendingConstant() {
        return this.itsFirstCalculableBendingSet.getParameter().getBendingBendingConstant();
    }

    public Integer getBendIndex(int theAtomIndex) {
        if (this.getFirstBendAtomIndexList().contains(theAtomIndex)) {
            return CalculableBendingBendingSet.FIRST_BEND;
        } else if (this.getSecondBendAtomIndexList().contains(theAtomIndex)) {
            return CalculableBendingBendingSet.SECOND_BEND;
        }

        return null;
    }

    public CalculableBendingSet getBendSet(int theBendIndex) {
        if (theBendIndex == CalculableBendingBendingSet.FIRST_BEND) {
            return this.itsFirstCalculableBendingSet;
        } else if (theBendIndex == CalculableBendingBendingSet.SECOND_BEND) {
            return this.itsSecondCalculableBendingSet;
        }

        System.err.println("Bend Index Error!");

        return null;
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        for (Integer theAtomIndex : this.itsAtomIndexList) {
            theStringBuilder.append(theAtomIndex).append("\t");
        }

        theStringBuilder.append(this.itsFirstCalculableBendingSet.getParameter().getStandardAngle()).append("\t")
                .append(this.itsFirstCalculableBendingSet.getParameter().getBendingBendingConstant()).append("\t")
                .append(this.itsFirstCalculableBendingSet.getParameter().getBendingConstant()).append("\t")
                .append(this.itsSecondCalculableBendingSet.getParameter().getStandardAngle()).append("\t")
                .append(this.itsSecondCalculableBendingSet.getParameter().getBendingConstant()).append("\t")
                .append(this.itsSecondCalculableBendingSet.getParameter().getBendingBendingConstant());

        return theStringBuilder.toString();
    }

    @Override
    public void makeConjugatedGradient(Double theScalingFactor) {
        this.itsFirstCalculableBendingSet.makeConjugatedGradient(theScalingFactor);
        this.itsSecondCalculableBendingSet.makeConjugatedGradient(theScalingFactor);
        
        if(this.itsOverlappedAtomIndex != null) {
            super.makeConjugatedGradient(theScalingFactor);
        }
    }
}
