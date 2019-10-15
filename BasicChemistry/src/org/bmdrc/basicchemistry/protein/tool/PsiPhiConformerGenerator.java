/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.protein.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bmdrc.basicchemistry.tool.ConformationGenerator;
import org.bmdrc.basicchemistry.util.ConformationVariable;
//import org.bmdrc.function.energy.SbffEnergyFunction;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.reader.PDBReader;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class PsiPhiConformerGenerator implements Serializable {

    private static final long serialVersionUID = 3035308212168719930L;

    private Protein itsProtein;
    private IAtomContainer itsProteinMolecule;
    private ConformationGenerator itsConformationGenerator;
    private List<ConformationVariable> itsConformationVariableList;
    private List<Integer> itsStepList;
    private Integer itsNumberOfStep;
    private Double itsInterval;
    //constant String variable
    private final String ALPHA_CARBON_NAME = "CA";
    private final String CARON_NAME_CONTAIN_PSI = "C";
    private final String NITROGEN_NAME_CONTAIN_PHI = "N";
    private final String SBFF_ENERGY = "SBFF_Energy";
    //constant Integer variable
    private final int ZERO_INT_VALUE = 0;
    private final int FIRST_INDEX = 0;

    public PsiPhiConformerGenerator() {
        this.itsConformationGenerator = new ConformationGenerator();
        this.itsConformationVariableList = new ArrayList<>();
    }

    public IAtomContainerSet generateProteinConformer(Protein theProtein, int theNumberOfStep, double theInterval, String theResultDir) {
        this.itsProtein = theProtein;
        this.itsNumberOfStep = theNumberOfStep;
        this.itsInterval = theInterval;

        this.__initializeConformationVariableList();

        return this.itsConformationGenerator.generateConformationUsingOnlyAtomRotation(this.itsProteinMolecule, this.itsConformationVariableList, theResultDir);
    }

    public IAtomContainerSet generateStableProteinConformer(Protein theProtein, int theNumberOfStep, double theInterval, int theNumberOfStableConformer) {
        this.itsProtein = theProtein;
        this.itsNumberOfStep = theNumberOfStep;
        this.itsInterval = theInterval;

        this.__initializeConformationVariableList();
        this.__initializeStepList();

        return this.__generateStableProteinConformer(theNumberOfStableConformer);
    }

    private IAtomContainerSet __generateStableProteinConformer(int theNumberOfStableConformer) {
//        SbffEnergyFunction theEnergyFunction = new SbffEnergyFunction(this.itsProteinMolecule);
        List<IAtomContainer> theMoleculeList = new ArrayList<>();
        List<Double> theEnergyList = new ArrayList<>();
        
        for (int si = 0, sEnd = (int) Math.pow(this.itsNumberOfStep + 1, this.itsConformationVariableList.size()); si < sEnd; si++) {
            System.out.println((si+1) + "th Start!!");
            this.__setStepList(si, this.itsNumberOfStep);
            
            IAtomContainer theConformer = this.itsConformationGenerator.generateConformationUsingOnlyAtomRotationAtSpecificStep(this.itsProteinMolecule, this.itsConformationVariableList,
                    this.itsStepList);
            
//            double theConformerEnergy = theEnergyFunction.calculateEnergyFunction(theConformer);
//            
//            theConformer.setProperty(this.SBFF_ENERGY, theConformerEnergy);
//            
//            if(theMoleculeList.size() < theNumberOfStableConformer) {
//                theMoleculeList.add(theConformer);
//                theEnergyList.add(theConformerEnergy);
//            } else if(Collections.max(theEnergyList) > theConformerEnergy) {
//                int theMaximumEnergyIndex = theEnergyList.indexOf(Collections.max(theEnergyList));
//                
//                theMoleculeList.set(theMaximumEnergyIndex, theConformer);
//                theEnergyList.set(theMaximumEnergyIndex, theConformerEnergy);
//            }
        }
        
        return this.__convertMoleculeListToAtomContainerSet(theMoleculeList);
    }
    
    private IAtomContainerSet __convertMoleculeListToAtomContainerSet(List<IAtomContainer> theMoleculeList) {
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();
        
        for(IAtomContainer theMolecule : theMoleculeList) {
            theMoleculeSet.addAtomContainer(theMolecule);
        }
        
        return theMoleculeSet;
    }
    
    private void __setStepList(int theStep, int theNumberOfStep) {
        this.itsStepList.set(this.FIRST_INDEX, theStep % (theNumberOfStep + 1));

        for (int si = 0; si < theNumberOfStep; si++) {
            if (theStep != 0) {
                this.itsStepList.set(si, theStep % (theNumberOfStep + 1));
                theStep /= theNumberOfStep + 1;
            } else {
                this.itsStepList.set(si, this.ZERO_INT_VALUE);
            }
        }
    }

    private void __initializeConformationVariableList() {
        this.itsProteinMolecule = this.itsProtein.getMolecule();

        this.itsConformationVariableList = new ArrayList<>();

        for (IAtom theAtom : this.itsProteinMolecule.atoms()) {
            if (theAtom.getProperty(PDBReader.ATOM_NAME).equals(this.ALPHA_CARBON_NAME)) {
                this.__initializeConformationVariableList(theAtom);
            }
        }
    }

    private void __initializeStepList() {
        this.itsStepList = new ArrayList<>();

        for (ConformationVariable theConformationVariable : this.itsConformationVariableList) {
            this.itsStepList.add(this.ZERO_INT_VALUE);
        }
    }

    private void __initializeConformationVariableList(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsProteinMolecule.getConnectedAtomsList(theAtom);
        int theAlphaCarbonIndex = this.itsProteinMolecule.getAtomNumber(theAtom);
        int thePsiCarbonIndex = -1;
        int thePhiNitrogenIndex = -1;
        int theAtomIndexBoundedPsiCarbon = -1;
        int theAtomIndexBoundedPhiNitrogen = -1;

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getProperty(PDBReader.ATOM_NAME).equals(this.CARON_NAME_CONTAIN_PSI)) {
                thePsiCarbonIndex = this.itsProteinMolecule.getAtomNumber(theConnectedAtom);
                theAtomIndexBoundedPsiCarbon = this.__getAtomIndexBoundedConnectedAtom(theAtom, theConnectedAtom);
            } else if (theConnectedAtom.getProperty(PDBReader.ATOM_NAME).equals(this.NITROGEN_NAME_CONTAIN_PHI)) {
                thePhiNitrogenIndex = this.itsProteinMolecule.getAtomNumber(theConnectedAtom);
                theAtomIndexBoundedPhiNitrogen = this.__getAtomIndexBoundedConnectedAtom(theAtom, theConnectedAtom);
            }
        }

        this.itsConformationVariableList.add(this.__generateConformationVariable(theAlphaCarbonIndex, thePsiCarbonIndex, thePhiNitrogenIndex, theAtomIndexBoundedPsiCarbon));
        this.itsConformationVariableList.add(this.__generateConformationVariable(theAlphaCarbonIndex, thePhiNitrogenIndex, thePsiCarbonIndex, theAtomIndexBoundedPhiNitrogen));
    }

    private Integer __getAtomIndexBoundedConnectedAtom(IAtom theAtom, IAtom theConnectedAtom) {
        List<IAtom> theConnectedAtomListAtSecondLayer = this.itsProteinMolecule.getConnectedAtomsList(theConnectedAtom);

        for (IAtom theConnectedAtomAtSecondLayer : theConnectedAtomListAtSecondLayer) {
            if (!theAtom.equals(theConnectedAtomAtSecondLayer)) {
                return this.itsProteinMolecule.getAtomNumber(theConnectedAtomAtSecondLayer);
            }
        }

        return null;
    }

    private ConformationVariable __generateConformationVariable(int theAlphaCarbonIndex, int theAtomIndexContainAngle, int theAtomIndexBoundedPrivousAtom, int theCounterAtomIndex) {
        List<Integer> theAtomIndexList = new ArrayList<>();

        theAtomIndexList.add(theCounterAtomIndex);
        theAtomIndexList.add(theAlphaCarbonIndex);
        theAtomIndexList.add(theAtomIndexContainAngle);
        theAtomIndexList.add(theAtomIndexBoundedPrivousAtom);

        return new ConformationVariable(theAtomIndexList, this.itsNumberOfStep, this.itsInterval, ConformationVariable.TORSION_ANGLE_VARIABLE);
    }
}
