/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.crystal.tool;

import java.io.Serializable;
import java.util.HashMap;
import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.crystal.CrystalStructureGenerator;
import org.bmdrc.basicchemistry.crystal.SpaceGroup;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.Bond;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 12. 01
 */
public class CrystalGeometryCalculator implements Serializable {

    private static final long serialVersionUID = 4403614304423797605L;

    private IAtomContainerSet itsCrystalUnitSet;
    //constant Integer variable
    private final int ORIGINAL_MOLECULE_INDEX = 0;
    private final int FIRST_INDEX = 0;
    //constant Double variable
    private final double RIGHT_ANGLE = 90.0;

    public CrystalInformation calculateCrystalInformation(IAtomContainerSet theCrystal, SpaceGroup theSpaceGroup) {
        this.itsCrystalUnitSet = theCrystal;

        if(theSpaceGroup.INDEX >= 3 && theSpaceGroup.INDEX <= 15) {
            return this.__calculateCrystalInformationInMonoclinic(theSpaceGroup);
        } else if(theSpaceGroup.INDEX >= 16 && theSpaceGroup.INDEX <= 74) {
            return this.__calculateCrystalInformationInOrthorhombic(theSpaceGroup);
        } else if(theSpaceGroup.INDEX >= 75 && theSpaceGroup.INDEX <= 142) {
            return this.__calculateCrystalInformationInTetragonal(theSpaceGroup);
        }

        this.itsCrystalUnitSet = null;
        
        return null;
    }

    private CrystalInformation __calculateCrystalInformationInMonoclinic(SpaceGroup theSpaceGroup) {
        IAtomContainer theXMovedMolecule = this.__getMatchedMolecule(new Vector3d(1.0, 0.0, 0.0), this.ORIGINAL_MOLECULE_INDEX);
        IAtomContainer theYMovedMolecule = this.__getMatchedMolecule(new Vector3d(0.0, 1.0, 0.0), this.ORIGINAL_MOLECULE_INDEX);
        IAtomContainer theZMovedMolecule = this.__getMatchedMolecule(new Vector3d(0.0, 0.0, 1.0), this.ORIGINAL_MOLECULE_INDEX);
        IAtomContainer theOriginalMolecule = this.itsCrystalUnitSet.getAtomContainer(this.FIRST_INDEX);
        CrystalInformation theCrystalInformation = null;
        
        for(int ai = 0, aEnd = theOriginalMolecule.getAtomCount(); ai < aEnd; ai++) {
            double theA = this.__round(theXMovedMolecule.getAtom(ai).getPoint3d().x - theOriginalMolecule.getAtom(ai).getPoint3d().x);
            double theB = this.__round(theYMovedMolecule.getAtom(ai).getPoint3d().y - theOriginalMolecule.getAtom(ai).getPoint3d().y);
            double theCCosBeta = theZMovedMolecule.getAtom(ai).getPoint3d().x - theOriginalMolecule.getAtom(ai).getPoint3d().x;
            double theCSinBeta = theZMovedMolecule.getAtom(ai).getPoint3d().z - theOriginalMolecule.getAtom(ai).getPoint3d().z;
            double theBeta = this.__round(Math.atan(theCSinBeta / theCCosBeta) + Math.PI);
            double theC = this.__round(theCCosBeta / Math.cos(theBeta));
            CrystalInformation theCheckCrystalInformation = new CrystalInformation(theSpaceGroup, theA, theB, theC, this.RIGHT_ANGLE, Math.toDegrees(theBeta), this.RIGHT_ANGLE);
            
            if(theCrystalInformation == null) {
                theCrystalInformation = theCheckCrystalInformation;
            } else if(!theCrystalInformation.equals(theCheckCrystalInformation)) {
                System.out.println(theCrystalInformation + "\n" + theCheckCrystalInformation + "\n" + ai + "th atom");
                System.err.println("Cannot calculate crystal information");
                return null;
            }
        }
        
        return theCrystalInformation;
    }
    
    private CrystalInformation __calculateCrystalInformationInOrthorhombic(SpaceGroup theSpaceGroup) {
        IAtomContainer theXMovedMolecule = this.__getMatchedMolecule(new Vector3d(1.0, 0.0, 0.0), this.ORIGINAL_MOLECULE_INDEX);
        IAtomContainer theYMovedMolecule = this.__getMatchedMolecule(new Vector3d(0.0, 1.0, 0.0), this.ORIGINAL_MOLECULE_INDEX);
        IAtomContainer theZMovedMolecule = this.__getMatchedMolecule(new Vector3d(0.0, 0.0, 1.0), this.ORIGINAL_MOLECULE_INDEX);
        IAtomContainer theOriginalMolecule = this.itsCrystalUnitSet.getAtomContainer(this.FIRST_INDEX);
        CrystalInformation theCrystalInformation = null;
        
        for(int ai = 0, aEnd = theOriginalMolecule.getAtomCount(); ai < aEnd; ai++) {
            double theA = this.__round(theXMovedMolecule.getAtom(ai).getPoint3d().x - theOriginalMolecule.getAtom(ai).getPoint3d().x);
            double theB = this.__round(theYMovedMolecule.getAtom(ai).getPoint3d().y - theOriginalMolecule.getAtom(ai).getPoint3d().y);
            double theC = this.__round(theZMovedMolecule.getAtom(ai).getPoint3d().z - theOriginalMolecule.getAtom(ai).getPoint3d().z);
            CrystalInformation theCheckCrystalInformation = new CrystalInformation(theSpaceGroup, theA, theB, theC, this.RIGHT_ANGLE, this.RIGHT_ANGLE, this.RIGHT_ANGLE);
            
            if(theCrystalInformation == null) {
                theCrystalInformation = theCheckCrystalInformation;
            } else if(!theCrystalInformation.equals(theCheckCrystalInformation)) {
                System.out.println(theCrystalInformation + "\n" + theCheckCrystalInformation + "\n" + ai + "th atom");
                System.err.println("Cannot calculate crystal information");
                return null;
            }
        }
        
        return theCrystalInformation;
    }
    
    private CrystalInformation __calculateCrystalInformationInTetragonal(SpaceGroup theSpaceGroup) {
        IAtomContainer theXMovedMolecule = this.__getMatchedMolecule(new Vector3d(1.0, 0.0, 0.0), this.ORIGINAL_MOLECULE_INDEX);
        IAtomContainer theZMovedMolecule = this.__getMatchedMolecule(new Vector3d(0.0, 0.0, 1.0), this.ORIGINAL_MOLECULE_INDEX);
        IAtomContainer theOriginalMolecule = this.itsCrystalUnitSet.getAtomContainer(this.FIRST_INDEX);
        CrystalInformation theCrystalInformation = null;
        
        for(int ai = 0, aEnd = theOriginalMolecule.getAtomCount(); ai < aEnd; ai++) {
            double theA = this.__round(theXMovedMolecule.getAtom(ai).getPoint3d().x - theOriginalMolecule.getAtom(ai).getPoint3d().x);
            double theC = this.__round(theZMovedMolecule.getAtom(ai).getPoint3d().z - theOriginalMolecule.getAtom(ai).getPoint3d().z);
            CrystalInformation theCheckCrystalInformation = new CrystalInformation(theSpaceGroup, theA, theA, theC, this.RIGHT_ANGLE, this.RIGHT_ANGLE, this.RIGHT_ANGLE);
            
            if(theCrystalInformation == null) {
                theCrystalInformation = theCheckCrystalInformation;
            } else if(!theCrystalInformation.equals(theCheckCrystalInformation)) {
                System.err.println("Cannot calculate crystal information");
                return null;
            }
        }
        
        return theCrystalInformation;
        
    }
    
    private IAtomContainer __getMatchedMolecule(Vector3d thePositionVector, int theMoleculeIndex) {
        for(IAtomContainer theMolecule : this.itsCrystalUnitSet.atomContainers()) {
            Vector3d thePositionVector3d = (Vector3d)theMolecule.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY);
            
            if(thePositionVector3d.equals(thePositionVector) &&
                    theMolecule.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY).equals(theMoleculeIndex)) {
                return theMolecule;
            }
        }
        
        return null;
    }
    
    private IAtomContainerSet __splitMolecule(IAtomContainer theResultMolecule, IAtomContainer theOriginalMolecule) {
        int theOriginalAtomCount = theOriginalMolecule.getAtomCount();
        int theResultAtomCount = theResultMolecule.getAtomCount();
        IAtomContainerSet theMoleculeSet = this.__initializeAtomContainerSetInSplitMolecule(theResultAtomCount / theOriginalAtomCount);

        for (int ai = 0, aEnd = theResultMolecule.getAtomCount(); ai < aEnd; ai++) {
            IAtom theAtom = theResultMolecule.getAtom(ai);

            theMoleculeSet.getAtomContainer(ai / theOriginalAtomCount).addAtom(theAtom);
            theMoleculeSet.getAtomContainer(ai / theOriginalAtomCount).setProperties(new HashMap<>(theResultMolecule.getProperties()));
            theMoleculeSet.getAtomContainer(ai / theOriginalAtomCount).setProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY, theAtom.getProperty(CrystalStructureGenerator.MOLECULE_POSITION_KEY));
            theMoleculeSet.getAtomContainer(ai / theOriginalAtomCount).setProperty(SpaceGroup.MOLECULE_NUMBER_KEY, theAtom.getProperty(SpaceGroup.MOLECULE_NUMBER_KEY));
        }

        for (IBond theBond : theResultMolecule.bonds()) {
            int theFirstAtomIndex = theResultMolecule.getAtomNumber(theBond.getAtom(0));
            int theSecondAtomIndex = theResultMolecule.getAtomNumber(theBond.getAtom(1));
            int theMoleculeNumber = theFirstAtomIndex / theOriginalAtomCount;
            IAtomContainer theMolecule = theMoleculeSet.getAtomContainer(theMoleculeNumber);

            theMolecule.addBond(new Bond(theMolecule.getAtom(theFirstAtomIndex % theOriginalAtomCount), theMolecule.getAtom(theSecondAtomIndex % theOriginalAtomCount), theBond.getOrder()));
        }

        return theMoleculeSet;
    }

    private IAtomContainerSet __initializeAtomContainerSetInSplitMolecule(int theMoleculeCount) {
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();

        for (int mi = 0; mi < theMoleculeCount; mi++) {
            theMoleculeSet.addAtomContainer(new AtomContainer());
        }

        return theMoleculeSet;
    }

    private double __round(double theValue) {
        return (double) Math.round(theValue * 1000.0) / 1000.0;
    }
}
