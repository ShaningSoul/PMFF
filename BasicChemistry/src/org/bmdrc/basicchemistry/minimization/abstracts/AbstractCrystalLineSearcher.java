/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.minimization.abstracts;

import java.io.Serializable;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.crystal.CrystalStructureGenerator;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractTotalForceFieldEnergyFunction;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.math.FirstDerivativeOfCrystal;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 03. 15
 */
public abstract class AbstractCrystalLineSearcher extends AbstractLineSearcher implements Serializable {

    private static final long serialVersionUID = -7345118210795682009L;

    protected CrystalInformation itsInitialCrystalInformation;
    protected CrystalInformation itsSearchCrystalInformation;
    protected CrystalInformation itsDirectionCrystalInformation;
    protected FirstDerivativeOfCrystal itsFirstDerivative;
    private IAtomContainer itsCellInStartPoint;
    
    public AbstractCrystalLineSearcher(AbstractTotalForceFieldEnergyFunction theEnergyFunction, CrystalInformation theInitialCrystalInformation) {
        super(theEnergyFunction);
        this.itsInitialCrystalInformation = theInitialCrystalInformation;
        this.itsFirstDerivative = new FirstDerivativeOfCrystal(theEnergyFunction);
    }

    public void optimize(Vector theSearchVector, CrystalInformation theSearchCrystalInformation, CrystalInformation theDirectionCrystalInformation) {
        this.itsSearchVector = theSearchVector;
        this.itsSearchCrystalInformation = theSearchCrystalInformation;
        this.itsDirectionCrystalInformation = theDirectionCrystalInformation;

        this.optimize(theSearchVector, null);
    }

    protected CrystalInformation _sumOfCrystalInformation(CrystalInformation theFirstInformation, CrystalInformation theSecondInformation) {
        if (theSecondInformation == null) {
            return theFirstInformation;
        }

        CrystalInformation theResultCrystalInformation = new CrystalInformation(theFirstInformation.getSpaceGroup());

        theResultCrystalInformation.setCellDimensionVector(Vector3dCalculator.sum(theFirstInformation.getCellDimensionVector(),
                theSecondInformation.getCellDimensionVector()));
        theResultCrystalInformation.setCoordinateAngleVector(Vector3dCalculator.sum(theFirstInformation.getCoordinateAngleVector(),
                theSecondInformation.getCoordinateAngleVector()));

        return theResultCrystalInformation;
    }
    
    protected IAtomContainer _getMoleculeMovedDirection(Vector theDirectionVector) {
        try {
            IAtomContainer theCopiedFractionMolecule = (IAtomContainer) ((AbstractTotalForceFieldEnergyFunction) this.itsFunction)
                    .getUnitMolecule().clone();

            for (int ai = 0, aEnd = theCopiedFractionMolecule.getAtomCount(); ai < aEnd; ai++) {
                Point3d thePosition = theCopiedFractionMolecule.getAtom(ai).getPoint3d();

                double theX = thePosition.x + this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.X_INDEX)
                        + theDirectionVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.X_INDEX);
                double theY = thePosition.y + this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.Y_INDEX)
                        + theDirectionVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.Y_INDEX);
                double theZ = thePosition.z + this.itsSearchVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.Z_INDEX)
                        + theDirectionVector.get(ai * Constant.POSITION_DIMENSION_SIZE + Vector3d.Z_INDEX);

                theCopiedFractionMolecule.getAtom(ai).setPoint3d(new Point3d(theX, theY, theZ));
            }

            return theCopiedFractionMolecule;
        } catch (CloneNotSupportedException ex) {
        }

        return null;
    }

    protected Vector _getGradientVector(Vector theDirectionVector, CrystalInformation theDirectionCrystalInformation) {
        AbstractUsableInMinimizationInEnergyFunction theFunction = (AbstractUsableInMinimizationInEnergyFunction)this.itsFunction;
        IAtomContainer theMovedFractionMolecule = this._getMoleculeMovedDirection(theDirectionVector);
        CrystalInformation theCurrentCrystalInformation = this._sumOfCrystalInformation(this.itsInitialCrystalInformation, this.itsSearchCrystalInformation);
        theCurrentCrystalInformation = this._sumOfCrystalInformation(theCurrentCrystalInformation, theDirectionCrystalInformation);
        
        IAtomContainer theNewUnitCell = this.itsInitialCrystalInformation.getSpaceGroup().generateCrystalStructure(theMovedFractionMolecule);
        IAtomContainer theNewCrystal = CrystalStructureGenerator.generateCrystalCell(theNewUnitCell, theCurrentCrystalInformation);
        Vector theGradientVector = new Vector();

        for (int ai = 0, aEnd = theFunction.getMolecule().getAtomCount(); ai < aEnd; ai++) {
            Vector3d theOriginalAtomPosition = new Vector3d(theFunction.getMolecule().getAtom(ai));
            Vector3d theMovedAtomPosition = new Vector3d(theNewCrystal.getAtom(ai));
            
            theGradientVector.addAll(Vector3dCalculator.minus(theMovedAtomPosition, theOriginalAtomPosition));
        }
        
        return theGradientVector;
    }
}
