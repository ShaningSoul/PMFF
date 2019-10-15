/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.intraenergy;

import java.io.Serializable;
import java.util.List;
import org.bmdrc.basicchemistry.tool.AngleCalculator;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.energyfunction.abstracts.AbstractIntraEnergyFunction;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableBendingBendingList;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableBendingBendingSet;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableBendingSet;
import org.bmdrc.sbff.parameter.intraenergy.BendingParameter;
import org.bmdrc.ui.abstracts.Constant;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffBendingBendingEnergyFunction extends AbstractIntraEnergyFunction implements Serializable {

    private static final long serialVersionUID = 5687107316203458706L;

    private CalculableBendingBendingList itsCalculableBendingBendingList;

    //constant Integer variable
    private final int NOT_CONNECTED_HYDROGEN = 0;
    private final int ONE_CONNECTED_HYDROGEN = 1;
    private final int TWO_CONNECTED_ATOM = 2;
    private final int FOUR_CONNECTED_ATOM = 4;
    //constant Double variable
    private final double CONVERTING_FACTOR_FROM_DEGREE_TO_RADIAN = Math.PI / 180.0;
    //constant String variable
    private final String BENDING_BENDING_ENERGY_KEY = "Bending-Bending Energy";
    private final String BENDING_BENDING_GRADIENT_KEY = "Bending-Bending Gradient";

    public SbffBendingBendingEnergyFunction(IAtomContainer theMolecule, CalculableBendingBendingList theCalculableBendingBendingList) {
        this(theMolecule, theCalculableBendingBendingList, new SbffIntraCalculationParameter());
    }

    public SbffBendingBendingEnergyFunction(IAtomContainer theMolecule, CalculableBendingBendingList theCalculableBendingBendingList,
            ISbffIntraCalculationParameter theCalculationParameter) {
        this(theMolecule, theCalculableBendingBendingList, theCalculationParameter, new MoveTogetherAtomNumberList(theMolecule));
    }
    
    public SbffBendingBendingEnergyFunction(IAtomContainer theMolecule, CalculableBendingBendingList theCalculableBendingBendingList,
            ISbffIntraCalculationParameter theCalculationParameter, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableBendingBendingList = theCalculableBendingBendingList;
    }
    
    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;

        for (CalculableBendingBendingSet theCalculableBendingBendingSet : this.itsCalculableBendingBendingList) {
            double theEnergy = this.__calculateEnergy(theCalculableBendingBendingSet);

            theCalculableBendingBendingSet.setEnergy(theEnergy);
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.BENDING_BENDING_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    protected double __calculateEnergy(CalculableBendingBendingSet theCalculableBendingBendingSet) {
        IAtom theCenterAtom = this.itsMolecule.getAtom(theCalculableBendingBendingSet.getCenterAtomIndex());
        IAtom theFirstAtomInFirstBond = this.itsMolecule.getAtom(theCalculableBendingBendingSet.getFirstAtomIndexInFirstBend());
        IAtom theSecondAtomInFirstBond = this.itsMolecule.getAtom(theCalculableBendingBendingSet.getSecondAtomIndexInFirstBend());
        IAtom theFirstAtomInSecondBond = this.itsMolecule.getAtom(theCalculableBendingBendingSet.getFirstAtomIndexInSecondBend());
        IAtom theSecondAtomInSecondBond = this.itsMolecule.getAtom(theCalculableBendingBendingSet.getSecondAtomIndexInSecondBend());

        BendingParameter theFirstBendingParameter = theCalculableBendingBendingSet.getFirstCalculableBendingSet().getParameter();
        BendingParameter theSecondBendingParameter = theCalculableBendingBendingSet.getSecondCalculableBendingSet().getParameter();

        double theFirstAngle = Math.toDegrees(AngleCalculator.calculateBondAngle(theFirstAtomInFirstBond,
                theCenterAtom, theSecondAtomInFirstBond));
        double theSecondAngle = Math.toDegrees(AngleCalculator.calculateBondAngle(theFirstAtomInSecondBond,
                theCenterAtom, theSecondAtomInSecondBond));
        double theEnergy = -0.021914 * theCalculableBendingBendingSet.getBendingBendingConstant() * (theFirstAngle - theFirstBendingParameter.getStandardAngle())
                * (theSecondAngle - theSecondBendingParameter.getStandardAngle());

        return theEnergy;
    }

    @Override
    public void calculateGradient() {
        for (CalculableBendingBendingSet theCalculableBendingBendingSet : this.itsCalculableBendingBendingList) {
            this.__calculateGradient(theCalculableBendingBendingSet);
        }
    }

    private void __calculateGradient(CalculableBendingBendingSet theBendingBendingSet) {
        double theFirstBendGradient = this.__calculateAngleGradient(theBendingBendingSet, CalculableBendingBendingSet.FIRST_BEND);
        double theSecondBendGradient = this.__calculateAngleGradient(theBendingBendingSet, CalculableBendingBendingSet.SECOND_BEND);
        
        theBendingBendingSet.getFirstCalculableBendingSet().setGradient(theFirstBendGradient);
        theBendingBendingSet.getSecondCalculableBendingSet().setGradient(theSecondBendGradient);
    }

    private double __calculateAngleGradient(CalculableBendingBendingSet theBendingBendingSet, int theBendIndex) {
        double theAngleDifference = this.__getOtherAngleDifference(theBendingBendingSet, theBendIndex);

        return -0.021914 * theBendingBendingSet.getBendingBendingConstant() * theAngleDifference;
    }

    private double __getOtherAngleDifference(CalculableBendingBendingSet theBendingBendingSet, int theBendIndex) {
        if (theBendIndex == CalculableBendingBendingSet.FIRST_BEND) {
            return this.__getAngle(theBendingBendingSet.getSecondBendAtomIndexList()) - theBendingBendingSet.getSecondCalculableBendingSet().getParameter().getStandardAngle();
        } else {
            return this.__getAngle(theBendingBendingSet.getFirstBendAtomIndexList()) - theBendingBendingSet.getFirstCalculableBendingSet().getParameter().getStandardAngle();
        }
    }

    private double __getAngle(List<Integer> theAtomIndexList) {
        return Math.toDegrees(AngleCalculator.calculateBondAngle(this.itsMolecule.getAtom(theAtomIndexList.get(Constant.FIRST_INDEX)), this.itsMolecule.getAtom(theAtomIndexList.get(Constant.SECOND_INDEX)),
                this.itsMolecule.getAtom(theAtomIndexList.get(Constant.THIRD_INDEX))));
    }

    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableBendingBendingSet theBendingBendingSet : this.itsCalculableBendingBendingList) {
            this.__calculateGradientVector(theBendingBendingSet, 1.0);
        }
    }

    private void __calculateGradientVector(CalculableBendingBendingSet theBendingBendingSet, double theScalingFactor) {
        List<Integer> theAtomIndexListRemovedOverlapAtom = theBendingBendingSet.getSideAtomIndexListRemovedOverlapAtom();
        
        for (Integer theAtomIndex : theAtomIndexListRemovedOverlapAtom) {
            if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theAtomIndex)) {
                this.__calculateGradientVectorInNotOverlapAtom(theBendingBendingSet, theAtomIndex, theScalingFactor);
            }
        }

        if (theBendingBendingSet.containOverlappedAtomIndex() && !this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theBendingBendingSet.getOverlappedAtomIndex())) {
            this.__calculateGradientVectorInOverlapAtom(theBendingBendingSet, theScalingFactor);
        }
    }

    private void __calculateGradientVectorInNotOverlapAtom(CalculableBendingBendingSet theBendingBendingSet, int theAtomIndex, double theScalingFactor) {
        CalculableBendingSet theBendingSet = theBendingBendingSet.getBendSet(theBendingBendingSet.getBendIndex(theAtomIndex));
        double theGradient = theBendingSet.getGradient() * theScalingFactor;
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theAtomIndex,
                theBendingBendingSet.getCenterAtomIndex());
        int theIndex = theBendingSet.getAtomIndexList().indexOf(theAtomIndex);

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theAtomNumber)) {
                this._setGradient(theAtomNumber, this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInBendAngle(this.itsMolecule, theBendingSet.getAtomIndexList(),
                        theIndex, theAtomNumber, theGradient));
            }
        }
    }

    private void __calculateGradientVectorInOverlapAtom(CalculableBendingBendingSet theBendingBendingSet, double theScalingFactor) {
        CalculableBendingSet theBendingSet = theBendingBendingSet.getFirstCalculableBendingSet();
        double theGradient = theBendingBendingSet.getFirstCalculableBendingSet().getGradient()
                - theBendingBendingSet.getSecondCalculableBendingSet().getGradient();
        int theIndex = theBendingSet.getAtomIndexList().indexOf(theBendingBendingSet.getOverlappedAtomIndex());
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theBendingBendingSet.getOverlappedAtomIndex(),
                theBendingBendingSet.getCenterAtomIndex());

        theBendingBendingSet.setGradient(theGradient);

        theGradient *= theScalingFactor;

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theAtomNumber)) {
                Vector3d theGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInBendAngle(this.itsMolecule, theBendingSet.getAtomIndexList(),
                        theIndex, theAtomNumber, theGradient);

                this._setGradient(theAtomNumber, theGradientVector);
            }
        }
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableBendingBendingSet theBendingBendingSet : this.itsCalculableBendingBendingList) {
            this.__reloadConjugatedGradientVector(theBendingBendingSet, 1.0);
        }
    }

    private void __reloadConjugatedGradientVector(CalculableBendingBendingSet theBendingBendingSet, double theScalingFactor) {
        List<Integer> theAtomIndexListRemovedOverlapAtom = theBendingBendingSet.getSideAtomIndexListRemovedOverlapAtom();

        for (Integer theAtomIndex : theAtomIndexListRemovedOverlapAtom) {
            if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theAtomIndex)) {
                this.__reloadConjugatedGradientVectorInNotOverlapAtom(theBendingBendingSet, theAtomIndex, theScalingFactor);
            }
        }

        if (theBendingBendingSet.containOverlappedAtomIndex() && !this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theBendingBendingSet.getOverlappedAtomIndex())) {
            this.__reloadConjugatedGradientVectorInOverlapAtom(theBendingBendingSet, theScalingFactor);
        }
    }

    private void __reloadConjugatedGradientVectorInNotOverlapAtom(CalculableBendingBendingSet theBendingBendingSet, int theAtomIndex, double theScalingFactor) {
        CalculableBendingSet theBendingSet = theBendingBendingSet.getBendSet(theBendingBendingSet.getBendIndex(theAtomIndex));
        double theGradient = theBendingSet.getConjugatedGradient() * theScalingFactor;
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theAtomIndex,
                theBendingBendingSet.getCenterAtomIndex());
        int theIndex = theBendingSet.getAtomIndexList().indexOf(theAtomIndex);

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theAtomNumber)) {
                Vector3d theGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInBendAngle(this.itsMolecule, theBendingSet.getAtomIndexList(),
                        theIndex, theAtomNumber, theGradient);

                this._setGradient(theAtomNumber, theGradientVector);
            }
        }
    }

    private void __reloadConjugatedGradientVectorInOverlapAtom(CalculableBendingBendingSet theBendingBendingSet, double theScalingFactor) {
        double theGradient = theBendingBendingSet.getConjugatedGradient() * theScalingFactor;
        CalculableBendingSet theBendingSet = theBendingBendingSet.getFirstCalculableBendingSet();
        int theIndex = theBendingSet.getAtomIndexList().indexOf(theBendingBendingSet.getOverlappedAtomIndex());
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theBendingBendingSet.getOverlappedAtomIndex(),
                theBendingBendingSet.getCenterAtomIndex());

        for (Integer theAtomNumber : theMoveTogetherAtomNumberList) {
            if(!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theAtomNumber)) {
                Vector3d theGradientVector = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInBendAngle(this.itsMolecule, theBendingSet.getAtomIndexList(),
                        theIndex, theAtomNumber, theGradient);

                this._setGradient(theAtomNumber, theGradientVector);
            }
        }
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableBendingBendingList.makeConjugatedGradient(theScalingFactor);
    }
}