/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.sbff.energyfunction.linearfunction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimization;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import org.bmdrc.sbff.parameter.interenergy.MpeoeParameter;
import org.bmdrc.sbff.parameter.interenergy.SbffMpeoeParameterList;
import org.bmdrc.sbff.tool.DipoleMomentCalculator;
import org.bmdrc.sbff.tool.MpeoeChargeCalculator;
import org.bmdrc.sbff.tool.QuadrupoleMomentCalculator;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 03. 23
 */
public class MpeoeParameterFunction extends AbstractUsableInMinimization implements Serializable {

    private static final long serialVersionUID = 7692021204577331691L;

    private Map<IAtomContainer, Double> itsDipoleMomentMap;
    private Map<IAtomContainer, Vector3d> itsQuadrupoleMomentMap;
    private SbffMpeoeParameterList itsParameterList;
    private Map<Integer, MpeoeParameter> itsGradientParameterMap;
    private DipoleMomentCalculator itsDipoleMomentCalculator;
    private QuadrupoleMomentCalculator itsQuadrupoleMomentCalculator;
    //constant Integer variable
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    private final int DIMENSION_SIZE = 3;
    //constant Boolean variable
    private static final boolean DEFAULT_IS_USED_MULTI_THREAD = false;

    public MpeoeParameterFunction(IAtomContainerSet theMoleculeSet, List<Double> theDipoleMomentList) {
        this(MpeoeParameterFunction.__getDipoleMomentMap(theMoleculeSet, theDipoleMomentList),
                MpeoeParameterFunction.DEFAULT_IS_USED_MULTI_THREAD, null);
    }

    public MpeoeParameterFunction(Map<IAtomContainer, Double> theDipoleMomentMap, boolean theIsUsedMultiThread, Integer theNumberOfThread) {
        super(theIsUsedMultiThread, theNumberOfThread);
        this.itsDipoleMomentMap = theDipoleMomentMap;
        this.itsParameterList = new SbffMpeoeParameterList();
        this.itsDipoleMomentCalculator = new DipoleMomentCalculator();
        this.itsQuadrupoleMomentCalculator = new QuadrupoleMomentCalculator();
    }

    private static Map<IAtomContainer, Double> __getDipoleMomentMap(IAtomContainerSet theMoleculeSet, List<Double> theDipoleMomentList) {
        Map<IAtomContainer, Double> theDipoleMomentMap = new HashMap<>();

        for (int mi = 0, mEnd = theMoleculeSet.getAtomContainerCount(); mi < mEnd; mi++) {
            theDipoleMomentMap.put(theMoleculeSet.getAtomContainer(mi), theDipoleMomentList.get(mi));
        }

        return theDipoleMomentMap;
    }

    @Override
    public Double calculateFunction() {
        double theErrorSum = 0.0;

        for (IAtomContainer theMolecule : this.itsDipoleMomentMap.keySet()) {
            double theCalculatedDipoleMoment = this.itsDipoleMomentCalculator.calculateDipoleMomentForScalar(theMolecule);

            theErrorSum += Math.pow(theCalculatedDipoleMoment - this.itsDipoleMomentMap.get(theMolecule), 2.0);
        }

        for (IAtomContainer theMolecule : this.itsQuadrupoleMomentMap.keySet()) {
            Vector3d theCalculatedQuadrupoleMoment = this.itsDipoleMomentCalculator.calculateDipoleMoment(theMolecule);
            Vector3d theExperimentQuadrupoleMoment = this.itsQuadrupoleMomentMap.get(theMolecule);

            for (int di = 0; di < this.DIMENSION_SIZE; di++) {
                theErrorSum += Math.pow(theCalculatedQuadrupoleMoment.get(di) - theExperimentQuadrupoleMoment.get(di), 2.0);
            }
        }

        return theErrorSum;
    }

    @Override
    public void reloadGradientVector() {
        this.itsGradientParameterMap = new HashMap<>();
        
        this.__reloadGradientVectorForDipoleMoment();

    }

    private void __reloadGradientVectorForDipoleMoment() {
        for (IAtomContainer theMolecule : this.itsDipoleMomentMap.keySet()) {
            for (IAtom theAtom : theMolecule.atoms()) {

            }
        }
    }

    private void __reloadGradientVectorForDipoleMoment(IAtomContainer theMolecule, IAtom theTargetAtom) {
        List<IBond> theConnectedBondList = theMolecule.getConnectedBondsList(theTargetAtom);
        TwoDimensionList<Double> theElectroNegativityHistory2dList = (TwoDimensionList<Double>) theMolecule.getProperty(MpeoeChargeCalculator.ELECTRO_NEGATIVITY_HISTORY_KEY);
        MpeoeParameter theGradientParameter = new MpeoeParameter((Integer)theTargetAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY));

        for (int ii = 1, iEnd = (Integer)theMolecule.getProperty(MpeoeChargeCalculator.NUMBER_OF_ITERATION_KEY); ii < iEnd; ii++) {
            double theGradientTermForA = 0.0;
            
            for (IBond theConnectedBond : theConnectedBondList) {
                theGradientTermForA += this.__getGradientTermForACoefficient(theMolecule, theConnectedBond, theTargetAtom, ii, theGradientParameter.getA());
            }
            
            theGradientParameter.setA(theGradientParameter.getA() + theGradientTermForA);
        }
    }

    private double __getGradientTermForACoefficient(IAtomContainer theMolecule, IBond theBond, IAtom theTargetAtom, 
            int theIterationCount, double theGradient) {
        
        
        
        
        return 0.0;//theValue * Math.pow(theDampingFactor, (double)theIterationCount);
    }
    
    private double __calculateGradientForAInTargetAtom(IAtomContainer theMolecule, IBond theBond, IAtom theTargetAtom, 
            int theIterationCount, double theGradient) {
        TwoDimensionList<Double> theElectroNegativityHistory2dList = (TwoDimensionList<Double>) theMolecule.getProperty(MpeoeChargeCalculator.ELECTRO_NEGATIVITY_HISTORY_KEY);
        MpeoeParameter theTargetAtomParameter = this.itsParameterList.getMpeoeEnPlusCoefficientMap().get(theTargetAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY));
        IAtom theConnectedAtom = theBond.getConnectedAtom(theTargetAtom);
        MpeoeParameter theConnectedAtomParameter = this.itsParameterList.getMpeoeEnPlusCoefficientMap().get(theConnectedAtom.getProperty(MpeoeAtomTypeGenerator.MPEOE_ATOM_TYPE_KEY));
        double theDampingFactor = this.itsParameterList.getDampValueMap().get(theBond.getProperty(MpeoeChargeCalculator.BOND_DAMP_TYPE_KEY));
        int theTargetAtomNumber = theMolecule.getAtomNumber(theTargetAtom);
        int theConnectedAtomNumber = theMolecule.getAtomNumber(theConnectedAtom);
        double theValue;
        
        
        if (theElectroNegativityHistory2dList.get(theIterationCount, theTargetAtomNumber) > theElectroNegativityHistory2dList.get(theIterationCount, theConnectedAtomNumber)) {
            theValue = (1.0 + theTargetAtomParameter.getB() * theGradient) / (theConnectedAtomParameter.getA() + theConnectedAtomParameter.getB());
        } else {
            theValue = -(theElectroNegativityHistory2dList.get(theIterationCount, theConnectedAtomNumber) - theElectroNegativityHistory2dList.get(theIterationCount, theTargetAtomNumber)) 
                    / Math.pow(theTargetAtomParameter.getA() + theTargetAtomParameter.getB(), 2.0) - (1.0 + theTargetAtomParameter.getB() * theGradient) 
                    / (theTargetAtomParameter.getA() + theTargetAtomParameter.getB());
        }
        
        return theValue * Math.pow(theDampingFactor, (double)theIterationCount);
    }

    @Override
    public Vector calculateGradientVector(double theScalingFactor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reloadConjugatedGradientVector() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector calculateConjugatedGradientVector(double theScalingFactor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
