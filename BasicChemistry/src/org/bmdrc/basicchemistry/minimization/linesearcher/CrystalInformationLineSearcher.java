/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.minimization.linesearcher;

import java.io.Serializable;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.crystal.CrystalStructureGenerator;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractCrystalLineSearcher;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.vector.VectorCalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 03. 15
 */
public class CrystalInformationLineSearcher extends AbstractCrystalLineSearcher implements Serializable {

    private static final long serialVersionUID = 8668593560306733204L;

    public CrystalInformationLineSearcher(AbstractTotalForceFieldEnergyFunction theEnergyFunction, CrystalInformation theInitialCrystalInformation) {
        super(theEnergyFunction, theInitialCrystalInformation);
    }

    @Override
    public Vector getOptimumDirectionVector() {
        return VectorCalculator.productScalarValue(this.itsOptimumPoint, this.itsSearchVector);
    }

    @Override
    protected double _calculateEnergyUsingPoint(double thePoint) {
        AbstractUsableInMinimizationInEnergyFunction theEnergyFunction = (AbstractUsableInMinimizationInEnergyFunction) this.itsFunction;
        IAtomContainer theMovedFractionMolecule = this._getMoleculeMovedDirection(this.itsSearchVector);
        CrystalInformation theDirectionCrystalInformation = this.__getDirectionCrystalInformation(thePoint);
        CrystalInformation theVariableCrystalInformation = this._sumOfCrystalInformation(this.itsSearchCrystalInformation, theDirectionCrystalInformation);
        IAtomContainer theUnitCell = theVariableCrystalInformation.getSpaceGroup().generateCrystalStructure(theMovedFractionMolecule);
        IAtomContainer theCell = CrystalStructureGenerator.generateCrystalCell(theUnitCell, theVariableCrystalInformation);
        Vector theGradientVector = this.__calculateGradientVector(theCell);
        
        System.out.println(theDirectionCrystalInformation);
        System.out.println(theGradientVector);
        return theEnergyFunction.calculateEnergyUsingGradientVector(theGradientVector);
    }

    private Vector __calculateGradientVector(IAtomContainer theCell) {
        AbstractUsableInMinimizationInEnergyFunction theEnergyFunction = (AbstractUsableInMinimizationInEnergyFunction) this.itsFunction;
        IAtomContainer theOriginalCell = theEnergyFunction.getMolecule();
        Vector theGradientVector = new Vector();
        
        for(int ai = 0, aEnd = theCell.getAtomCount(); ai < aEnd; ai++) {
            Point3d theOriginalAtomPosition = theOriginalCell.getAtom(ai).getPoint3d();
            Point3d theAtomPosition = theCell.getAtom(ai).getPoint3d();
            
            theGradientVector.add(theAtomPosition.x - theOriginalAtomPosition.x);
            theGradientVector.add(theAtomPosition.y - theOriginalAtomPosition.y);
            theGradientVector.add(theAtomPosition.z - theOriginalAtomPosition.z);
        }
        
        return theGradientVector;
    }
    
    private CrystalInformation __getDirectionCrystalInformation(double thePoint) {
        CrystalInformation theNewDirectionCrystalInformation = new CrystalInformation();
        
        theNewDirectionCrystalInformation.setAlpha(this.itsDirectionCrystalInformation.getAlpha() * thePoint);
        theNewDirectionCrystalInformation.setBeta(this.itsDirectionCrystalInformation.getBeta() * thePoint);
        theNewDirectionCrystalInformation.setGamma(this.itsDirectionCrystalInformation.getGamma() * thePoint);
        
        theNewDirectionCrystalInformation.setAInCellDimension(this.itsDirectionCrystalInformation.getAInCellDimension() * thePoint);
        theNewDirectionCrystalInformation.setBInCellDimension(this.itsDirectionCrystalInformation.getBInCellDimension() * thePoint);
        theNewDirectionCrystalInformation.setCInCellDimension(this.itsDirectionCrystalInformation.getCInCellDimension() * thePoint);
        
        return theNewDirectionCrystalInformation;
    }
    
    public CrystalInformation getOptimumCrystalInformation() {
        return this.__getDirectionCrystalInformation(this.itsOptimumPoint);
    }
}
