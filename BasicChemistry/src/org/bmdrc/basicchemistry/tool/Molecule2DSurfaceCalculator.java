/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 10. 14
 */
public class Molecule2DSurfaceCalculator implements Serializable {

    class Range {

        private Double itsStartAngle;
        private Double itsEndAngle;

        public Range() {
        }

        public Range(Double theStartAngle, Double theEndAngle) {
            this.itsStartAngle = theStartAngle;
            this.itsEndAngle = theEndAngle;
        }

        public Double getStartAngle() {
            return itsStartAngle;
        }

        public void setStartAngle(Double theStartAngle) {
            this.itsStartAngle = theStartAngle;
        }

        public Double getEndAngle() {
            return itsEndAngle;
        }

        public void setEndAngle(Double theEndAngle) {
            this.itsEndAngle = theEndAngle;
        }

        public boolean isStartAngle(double theAngle) {
            return this.itsStartAngle == null || (this.itsStartAngle > theAngle && this.itsEndAngle > theAngle);
        }

        public boolean isEndAngle(double theAngle) {
            return this.itsEndAngle == null || (this.itsStartAngle < theAngle && this.itsEndAngle < theAngle);
        }

        @Override
        public String toString() {
            return Double.toString(Math.toDegrees(this.itsStartAngle)) + " " + Double.toString(Math.toDegrees(this.itsEndAngle));
        }

        public double getRangeAngle() {
            return this.itsEndAngle - this.itsStartAngle;
        }

        public boolean isOverlap(Range theRange) {
            if (this.itsStartAngle < theRange.itsStartAngle && this.itsEndAngle > theRange.itsStartAngle) {
                return true;
            } else if (this.itsStartAngle < theRange.itsEndAngle && this.itsEndAngle > theRange.itsEndAngle) {
                return true;
            }

            return false;
        }

        public boolean containRange(Range theRange) {
            if (this.itsStartAngle > theRange.itsStartAngle || this.itsEndAngle < theRange.itsStartAngle) {
                return false;
            } else if (this.itsStartAngle > theRange.itsEndAngle || this.itsEndAngle < theRange.itsEndAngle) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.itsStartAngle);
            hash = 61 * hash + Objects.hashCode(this.itsEndAngle);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Range other = (Range) obj;
            if (!Objects.equals(this.itsStartAngle, other.itsStartAngle)) {
                return false;
            }
            if (!Objects.equals(this.itsEndAngle, other.itsEndAngle)) {
                return false;
            }
            return true;
        }
    }

    private static final long serialVersionUID = -3577841358448415963L;

    //constant Double vairable
    private final double WATER_RADIUS = 1.5;
    //constant Integer variable
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    //constant String variable
    public static final String SURFACE_LENGTH_KEY = "2D surface length";
    public static final String RANGE_KEY = "Range";
    //constant Vector3d variable
    private final Vector3d Y_UNIT_VECTOR = new Vector3d(0.0, 1.0, 0.0);

    public Molecule2DSurfaceCalculator() {
    }

    public void input2DSurfaceLength(IAtomContainer theMolecule) {
        for (IAtom theAtom : theMolecule.atoms()) {
            theAtom.setProperty(this.RANGE_KEY, new ArrayList<>());
        }

        for (int fi = 0, fEnd = theMolecule.getAtomCount() - 1; fi < fEnd; fi++) {
            for (int si = fi + 1, sEnd = theMolecule.getAtomCount(); si < sEnd; si++) {
                if (this.__isCrossBetweenTwoCircle(theMolecule.getAtom(fi), theMolecule.getAtom(si))) {
                    this.__setRangeInAtom(theMolecule.getAtom(fi), theMolecule.getAtom(si));
                }
            }
        }

        for (IAtom theAtom : theMolecule.atoms()) {
            double theSurfaceLength = 2.0 * Math.PI * AtomInformation.getAtomInformation(theAtom.getSymbol()).VDW_RAIDUS * (2.0 * Math.PI - this.__getRemovedSurfaceAngle(theAtom));

            theAtom.setProperty(Molecule2DSurfaceCalculator.SURFACE_LENGTH_KEY, theSurfaceLength);
        }
    }

    private double __getRemovedSurfaceAngle(IAtom theAtom) {
        List<Range> theRangeList = (List<Range>) theAtom.getProperty(this.RANGE_KEY);
        double theRemovedSurfaceAngle = 0.0;

        for (Range theRange : theRangeList) {
            theRemovedSurfaceAngle += theRange.getRangeAngle();
        }

        return theRemovedSurfaceAngle;
    }

    private boolean __isCrossBetweenTwoCircle(IAtom theFirstAtom, IAtom theSecondAtom) {
        double theDistance = theFirstAtom.getPoint2d().distance(theSecondAtom.getPoint2d());
        double theFirstAtomRadius = AtomInformation.getAtomInformation(theFirstAtom.getSymbol()).VDW_RAIDUS + this.WATER_RADIUS;
        double theSecondAtomRadius = AtomInformation.getAtomInformation(theSecondAtom.getSymbol()).VDW_RAIDUS + this.WATER_RADIUS;

        return theDistance < theFirstAtomRadius + theSecondAtomRadius;
    }

    private void __setRangeInAtom(IAtom theFirstAtom, IAtom theSecondAtom) {
        List<Vector3d> theCrossPointVector3dList = this.__calculateCrossPointVector3dList(theFirstAtom, theSecondAtom);

        this.__setRangeInAtom(theFirstAtom, theSecondAtom, theCrossPointVector3dList);
        this.__setRangeInAtom(theSecondAtom, theFirstAtom, theCrossPointVector3dList);
    }

    private void __setRangeInAtom(IAtom theAtom, IAtom theCounterAtom, List<Vector3d> theCrossPointVector3dList) {
        List<Range> theRangeList = (List<Range>) theAtom.getProperty(this.RANGE_KEY);
        double theFirstAngle = Vector3dCalculator.calculateAngle(this.Y_UNIT_VECTOR, Vector3dCalculator.minus(theCrossPointVector3dList.get(this.FIRST_INDEX),
                new Vector3d(theAtom)));
        double theSecondAngle = Vector3dCalculator.calculateAngle(this.Y_UNIT_VECTOR, Vector3dCalculator.minus(theCrossPointVector3dList.get(this.SECOND_INDEX),
                new Vector3d(theAtom)));
        double theCenterAngle = Vector3dCalculator.calculateAngle(this.Y_UNIT_VECTOR, new Vector3d(theAtom, theCounterAtom));

        if (theCrossPointVector3dList.get(this.FIRST_INDEX).getX() < theAtom.getPoint2d().x) {
            theFirstAngle = 2.0 * Math.PI - theFirstAngle;
        }
        if (theCrossPointVector3dList.get(this.SECOND_INDEX).getX() < theAtom.getPoint2d().x) {
            theSecondAngle = 2.0 * Math.PI - theSecondAngle;
        }
        if (new Vector3d(theAtom, theCounterAtom).getX() < 0.0) {
            theCenterAngle = 2.0 * Math.PI - theCenterAngle;
        }
//        System.out.println(Math.toDegrees(theFirstAngle) + " " + Math.toDegrees(theCenterAngle) + " " + Math.toDegrees(theSecondAngle));
        List<Range> theTestRangeList = this.__getTestRange(theFirstAngle, theSecondAngle, theCenterAngle);

        this.__setRangeByOverlap(theRangeList, theTestRangeList);

        theAtom.setProperty(this.RANGE_KEY, theRangeList);
    }

    private void __setRangeByOverlap(List<Range> theTemplateRangeList, List<Range> theTestRangeList) {
        List<Boolean> theOverlapList = this.__initializeOverlapList(theTestRangeList.size() + theTemplateRangeList.size());

        theTemplateRangeList.addAll(theTestRangeList);

        for (Range theTemplateRange : theTemplateRangeList) {
            for (int ti = 0, tEnd = theTemplateRangeList.size(); ti < tEnd; ti++) {
                if (!theTemplateRange.equals(theTemplateRangeList.get(ti)) && theTemplateRange.isOverlap(theTemplateRangeList.get(ti)) && !theOverlapList.get(ti)) {
                    this.__setRange(theTemplateRange, theTemplateRangeList.get(ti));
                    theOverlapList.set(ti, true);
                    break;
                }
            }
        }

        for (Range theTemplateRange : theTemplateRangeList) {
            for (int ti = 0, tEnd = theTemplateRangeList.size(); ti < tEnd; ti++) {
                if (!theTemplateRange.equals(theTemplateRangeList.get(ti)) && theTemplateRange.containRange(theTemplateRangeList.get(ti)) && !theOverlapList.get(ti)) {
                    this.__setRange(theTemplateRange, theTemplateRangeList.get(ti));
                    theOverlapList.set(ti, true);
                }
            }
        }

        for (int vi = theOverlapList.size() - 1; vi >= 0; vi--) {
            if (theOverlapList.get(vi)) {
                theTemplateRangeList.remove(vi);
            }
        }
    }

    private void __setRange(Range theTemplateRange, Range theTestRange) {
        if (theTemplateRange.isStartAngle(theTestRange.itsStartAngle)) {
            theTemplateRange.setStartAngle(theTestRange.itsStartAngle);
        }

        if (theTemplateRange.isEndAngle(theTestRange.itsEndAngle)) {
            theTemplateRange.setEndAngle(theTestRange.itsEndAngle);
        }
    }

    private List<Boolean> __initializeOverlapList(int theSizeOfTestRangeList) {
        List<Boolean> theOverlapList = new ArrayList<>();

        for (int vi = 0; vi < theSizeOfTestRangeList; vi++) {
            theOverlapList.add(false);
        }

        return theOverlapList;
    }

    private List<Range> __getTestRange(double theFirstAngle, double theSecondAngle, double theCenterAngle) {
        double theMaximumAngle = Double.max(theFirstAngle, theSecondAngle);
        double theMinimumAngle = Double.min(theFirstAngle, theSecondAngle);
        List<Range> theTestRange = new ArrayList<>();

        if (theMinimumAngle < theCenterAngle && theMaximumAngle > theCenterAngle) {
            theTestRange.add(new Range(theMinimumAngle, theMaximumAngle));
        } else {
            theTestRange.add(new Range(0.0, theMinimumAngle));
            theTestRange.add(new Range(theMaximumAngle, 2.0 * Math.PI));
        }

        return theTestRange;
    }

    private List<Vector3d> __calculateCrossPointVector3dList(IAtom theFirstAtom, IAtom theSecondAtom) {
        List<Vector3d> theCrossPointVectorList = new ArrayList<>();
        double theXInFirstAtom = theFirstAtom.getPoint2d().x;
        double theYInFirstAtom = theFirstAtom.getPoint2d().y;
        double theXInSecondAtom = theSecondAtom.getPoint2d().x;
        double theYInSecondAtom = theSecondAtom.getPoint2d().y;

        double theDistance = theFirstAtom.getPoint2d().distance(theSecondAtom.getPoint2d());
        double theFirstAtomRadius = AtomInformation.getAtomInformation(theFirstAtom.getSymbol()).VDW_RAIDUS + this.WATER_RADIUS;
        double theSecondAtomRadius = AtomInformation.getAtomInformation(theSecondAtom.getSymbol()).VDW_RAIDUS + this.WATER_RADIUS;

        double theFirstAngle = Math.acos((theFirstAtomRadius * theFirstAtomRadius - theSecondAtomRadius * theSecondAtomRadius + theDistance * theDistance)
                / (2.0 * theFirstAtomRadius * theDistance));
        double theSecondAngle = Math.atan((theYInSecondAtom - theYInFirstAtom) / (theXInSecondAtom - theXInFirstAtom));
        double theCenterAngle = Vector3dCalculator.calculateAngle(new Vector3d(0.0, 1.0, 0.0),
                new Vector3d(theFirstAtom, theSecondAtom));

        if (theXInFirstAtom > theXInSecondAtom) {
            theCenterAngle = 2.0 * Math.PI - theCenterAngle;
        }

        theCrossPointVectorList.add(new Vector3d(theXInFirstAtom + theFirstAtomRadius * Math.cos(theCenterAngle - Math.PI / 2.0 + theFirstAngle),
                theYInFirstAtom + theFirstAtomRadius * -Math.sin(theCenterAngle - Math.PI / 2.0 + theFirstAngle), 0.0));
        theCrossPointVectorList.add(new Vector3d(theXInFirstAtom + theFirstAtomRadius * Math.cos(theCenterAngle - Math.PI / 2.0 - theFirstAngle),
                theYInFirstAtom + theFirstAtomRadius * -Math.sin(theCenterAngle - Math.PI / 2.0 - theFirstAngle), 0.0));

        return theCrossPointVectorList;
    }
}
