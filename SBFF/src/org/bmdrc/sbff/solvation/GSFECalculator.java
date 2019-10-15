/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.solvation;

import org.bmdrc.sbff.solvation.parameter.SolventParameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import org.bmdrc.sbff.parameter.solvation.HydrogenBondAcceptorParameterList;
import org.bmdrc.sbff.parameter.solvation.HydrogenBondDonorParameterList;
import org.bmdrc.sbff.tool.CdeapCalculator;
import org.bmdrc.sbff.tool.MpeoeChargeCalculator;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class GSFECalculator implements Serializable {

    private static final long serialVersionUID = -1851976222717819989L;

    private IAtomContainer itsMolecule;
    private List<GridList> itsSurfaceGridList;
    private SolventParameter itsParameter;
    private Vector itsCoefficientVector;
    private HydrogenBondAcceptorParameterList itsHydrogenBondAcceptorParameterList;
    private HydrogenBondDonorParameterList itsHydrogenBondDonorParameterList;
    //constant Integer variable
    private final int HYDROGEN_BOND_ACIDITY = 0;
    private final int HYDROGEN_BOND_BASICITY = 1;
    private final int HYDROGEN_ATOMIC_NUMBER = 1;
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    private final int THIRD_INDEX = 2;
    private final int FOURTH_INDEX = 3;
    //constant String variable
    public static final String GSFE_KEY = "Solvation Free Energy (kcal/mol)";
    //constant Double variable
    private final double WATER_DIELECTRIC_CONSTANT = 78.2;
    private final double WATER_REGELCTIVE_INDEX = 1.33;
    private final double WATER_SURFACE_TENSION = 71.99;
    private final double WATER_HYDROGEN_BOND_ACIDITY = 0.82;
    private final double WATER_HYDROGEN_BOND_BASICITY = 0.35;
    private final double ZERO_VALUE = 0.0;

    public GSFECalculator() {
        this.itsParameter = new SolventParameter(this.WATER_DIELECTRIC_CONSTANT, this.WATER_REGELCTIVE_INDEX, this.WATER_SURFACE_TENSION, this.WATER_HYDROGEN_BOND_ACIDITY,
                this.WATER_HYDROGEN_BOND_BASICITY);
        this.itsHydrogenBondAcceptorParameterList = new HydrogenBondAcceptorParameterList();
        this.itsHydrogenBondDonorParameterList = new HydrogenBondDonorParameterList();
    }

    public GSFECalculator(Double theDielectriConstant, Double theReflectiveIndex, Double theSurfaceTension, Double theHydrogenBondAcidity, Double theHydrogenBondBasicity) {
        this.itsParameter = new SolventParameter(theDielectriConstant, theReflectiveIndex, theSurfaceTension, theHydrogenBondAcidity, theHydrogenBondBasicity);
        this.itsHydrogenBondAcceptorParameterList = new HydrogenBondAcceptorParameterList();
        this.itsHydrogenBondDonorParameterList = new HydrogenBondDonorParameterList();
    }
    
    public GSFECalculator(SolventParameter theParameter) {
        this();itsParameter = theParameter;
        this.itsHydrogenBondAcceptorParameterList = new HydrogenBondAcceptorParameterList();
        this.itsHydrogenBondDonorParameterList = new HydrogenBondDonorParameterList();
    }

    public void inputGSFE(IAtomContainerSet theMoleculeSet) {
        this.__initializeCoefficientVeector();

        for (IAtomContainer theMolecule : theMoleculeSet.atomContainers()) {
            SurfaceGridGenerator theSurfaceGridGenerator = new SurfaceGridGenerator();

            this.itsMolecule = theMolecule;

            this.__initializeMpeoeAndCdeap();
            this.itsSurfaceGridList = theSurfaceGridGenerator.draw(theMolecule);
        }
    }

    public double calculateGSFE(IAtomContainer theMolecule, SolventParameter theParameter) {
        this.itsParameter = theParameter;

        return this.calculateGSFE(theMolecule);
    }

    public double calculateGSFE(IAtomContainer theMolecule) {
        SurfaceGridGenerator theSurfaceGridGenerator = new SurfaceGridGenerator();

        this.itsMolecule = theMolecule;

        this.__initializeMpeoeAndCdeap();
        this.__initializeCoefficientVeector();

        this.itsSurfaceGridList = theSurfaceGridGenerator.draw(theMolecule);

        return this.__calculateGSFE();
    }

    private double __calculateGSFE() {
        Vector theTermVector = this.__getTermVector();
        
        Double theEnergy = VectorCalculator.DotProduct(this.itsCoefficientVector, theTermVector);
        
        this.itsMolecule.setProperty(GSFECalculator.GSFE_KEY, String.format("%.4f", theEnergy));
        
        return theEnergy;
    }

    private void __initializeCoefficientVeector() {
        this.itsCoefficientVector = new Vector();

        this.itsCoefficientVector.add(-0.00176253 * this.itsParameter.getDielectricConstant() - 0.13717963);
        this.itsCoefficientVector.add(-0.00289167 * this.itsParameter.getDielectricConstant() - 0.18369616);
        this.itsCoefficientVector.add(-0.21598982 * this.itsParameter.getReflectiveIndex() + 0.2643995);
        this.itsCoefficientVector.add(6.72090557 * this.itsParameter.getReflectiveIndex() - 8.98637637);
        this.itsCoefficientVector.add(0.00007119 * this.itsParameter.getSurfaceTension());
        this.itsCoefficientVector.add(-0.26580166);
        this.itsCoefficientVector.add(-7.52867 * this.itsParameter.getHydrogenBondBasicity());
        this.itsCoefficientVector.add(-4.34766 * this.itsParameter.getHydrogenBondAcidity());
    }

    private Vector __initializeTermVector() {
        Vector theTermVector = new Vector();

        theTermVector.add(this.ZERO_VALUE);
        theTermVector.add(this.ZERO_VALUE);
        theTermVector.add(this.ZERO_VALUE);
        theTermVector.add(this.ZERO_VALUE);

        return theTermVector;
    }

    private Vector __getTermVector() {
        Vector theTermVector = this.__initializeTermVector();
        double theNumberOfSurfaceGrid = 0.0;
        List<Double> theHydrogenBondTermList = this.__getHydrogenBondTermList();

        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            GridList theSurfaceGridList = this.itsSurfaceGridList.get(ai);

            for (Vector3d theSurfaceGrid : theSurfaceGridList) {
                this.__setTermList(theSurfaceGrid, theTermVector);
                theNumberOfSurfaceGrid += 1.0;
            }
        }

        theTermVector.add(theNumberOfSurfaceGrid);
        theTermVector.add(1.0);
        theTermVector.add(theHydrogenBondTermList.get(this.HYDROGEN_BOND_ACIDITY));
        theTermVector.add(theHydrogenBondTermList.get(this.HYDROGEN_BOND_BASICITY));

        return theTermVector;
    }

    private void __setTermList(Vector3d theSurfaceGrid, Vector theTermVector) {
        double theT1 = 0.0;
        double theT2 = 0.0;
        double theT3 = 0.0;
        double theT4 = 0.0;

        for (IAtom theAtom : this.itsMolecule.atoms()) {
            double theDistance = theAtom.getPoint3d().distance(theSurfaceGrid.toPoint3d());

            theT1 += (Double) theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY) / (theDistance * theDistance);
            theT2 += ((Double) theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY) * (Double) theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY)) / Math.pow(theDistance, 3.0);
            theT3 += (Double) theAtom.getProperty(CdeapCalculator.CDEAP_KEY) / Math.pow(theDistance, 3.0);
            theT4 += (Double) theAtom.getProperty(CdeapCalculator.CDEAP_KEY) / Math.pow(theDistance, 6.0);
        }

        theTermVector.set(this.FIRST_INDEX, theTermVector.get(this.FIRST_INDEX) + Math.abs(theT1));
        theTermVector.set(this.SECOND_INDEX, theTermVector.get(this.SECOND_INDEX) + theT2);
        theTermVector.set(this.THIRD_INDEX, theTermVector.get(this.THIRD_INDEX) + theT3);
        theTermVector.set(this.FOURTH_INDEX, theTermVector.get(this.FOURTH_INDEX) + theT4);
    }

    private List<Double> __setHydrogenBondTermVector(List<Double> theAcidityParameterList, List<Double> theBasicityParameterList) {
        List<Double> theHydrogenBondTermVector = this.__initializeHydrogenBondTermList();

        for (Double theAcidityParameter : theAcidityParameterList) {
            theHydrogenBondTermVector.set(this.HYDROGEN_BOND_ACIDITY, theHydrogenBondTermVector.get(this.HYDROGEN_BOND_ACIDITY) + theAcidityParameter);
        }

        for (Double theBasicityParameter : theBasicityParameterList) {
            theHydrogenBondTermVector.set(this.HYDROGEN_BOND_BASICITY, theHydrogenBondTermVector.get(this.HYDROGEN_BOND_BASICITY) + theBasicityParameter);
        }

        return theHydrogenBondTermVector;
    }

    private void __calculateParameterList(List<Double> theParameterList, TwoDimensionList<Double> theDistance2dList) {
        for (int fi = 0, fEnd = theParameterList.size(); fi < fEnd; fi++) {
            for (int si = 0, sEnd = theParameterList.size(); si < sEnd; si++) {
                if (fi != si) {
                    theParameterList.set(si, theParameterList.get(si) * (1.0 - Math.exp(-theDistance2dList.get(fi, si) / 1.3) / 2.0));
                }
            }
        }
    }

    private TwoDimensionList<Double> __getDistance2dList(List<Vector3d> theCoordinateList) {
        TwoDimensionList<Double> theDistance2dList = new TwoDimensionList<>();

        for (Vector3d theFirstCoordinate : theCoordinateList) {
            List<Double> theDistanceList = new ArrayList<>();

            for (Vector3d theSecondCoordinate : theCoordinateList) {
                theDistanceList.add(theFirstCoordinate.distance(theSecondCoordinate));
            }

            theDistance2dList.add(theDistanceList);
        }

        return theDistance2dList;
    }

    private List<Double> __getHydrogenBondTermList() {
        List<Double> theAcidityParameterList = new ArrayList<>();
        List<Double> theBasicityParameterList = new ArrayList<>();
        List<Vector3d> theAcidityCoordinateList = new ArrayList<>();
        List<Vector3d> theBasicityCoordinateList = new ArrayList<>();
        TwoDimensionList<Double> theAcidityDistance2dList;
        TwoDimensionList<Double> theBasicityDistance2dList;

        for (IAtom theAtom : this.itsMolecule.atoms()) {
            if (theAtom.getProperties().containsKey(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY)) {
                if (theAtom.getAtomicNumber() == this.HYDROGEN_ATOMIC_NUMBER) {
                    theAcidityParameterList.add(this.itsHydrogenBondDonorParameterList.get((Integer) theAtom.getProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY)));
                    theAcidityCoordinateList.add(new Vector3d(theAtom));
                } else {
                    theBasicityParameterList.add(this.itsHydrogenBondAcceptorParameterList.get((Integer) theAtom.getProperty(MpeoeAtomTypeGenerator.HYDROGEN_BOND_ATOM_TYPE_KEY)));
                    theBasicityCoordinateList.add(new Vector3d(theAtom));
                }
            }
        }

        theAcidityDistance2dList = this.__getDistance2dList(theAcidityCoordinateList);
        theBasicityDistance2dList = this.__getDistance2dList(theBasicityCoordinateList);

        this.__calculateParameterList(theAcidityParameterList, theAcidityDistance2dList);
        this.__calculateParameterList(theBasicityParameterList, theBasicityDistance2dList);

        return this.__setHydrogenBondTermVector(theAcidityParameterList, theBasicityParameterList);
    }

    private List<Double> __initializeHydrogenBondTermList() {
        List<Double> theHydrogenBondTermList = new ArrayList<>();

        theHydrogenBondTermList.add(this.ZERO_VALUE);
        theHydrogenBondTermList.add(this.ZERO_VALUE);

        return theHydrogenBondTermList;
    }

    private void __initializeMpeoeAndCdeap() {
        if (!this.itsMolecule.getProperties().containsKey(MpeoeChargeCalculator.IS_CALCULATED) || !(Boolean) this.itsMolecule.getProperty(MpeoeChargeCalculator.IS_CALCULATED)) {
            MpeoeChargeCalculator theChargeCalculator = new MpeoeChargeCalculator();

            theChargeCalculator.inputMpeoeCharge(this.itsMolecule);
        }
        
        if (!this.itsMolecule.getProperties().containsKey(CdeapCalculator.IS_CDEAP_CALCULATED) || !(Boolean) this.itsMolecule.getProperty(CdeapCalculator.IS_CDEAP_CALCULATED)) {
            CdeapCalculator theCdeapCalculator = new CdeapCalculator();

            theCdeapCalculator.inputCdeapPolarizability(this.itsMolecule);
        }
    }
}
