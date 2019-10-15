/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.energyfunction.abstracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.interfaces.ICalculationParameter;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.tool.FormalChargeCalculator;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2015. 09. 09
 */
public abstract class AbstractTotalForceFieldEnergyFunction<T extends ICalculationParameter> extends AbstractUsableInMinimizationInEnergyFunction<T> implements Serializable {

    private static final long serialVersionUID = 1180928502907637526L;

    protected IAtomContainer itsUnitMolecule;
    
    public AbstractTotalForceFieldEnergyFunction(IAtomContainer theMolecule, T theCalculationParameter) {
        super(theMolecule, theCalculationParameter);
        
        this.itsUnitMolecule = theMolecule;
        
        FormalChargeCalculator.inputFormalCharge(theMolecule);
    }

    public AbstractTotalForceFieldEnergyFunction(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule, 
            T theCalculationParameter) {
        super(theTotalMolecule, theUnitMolecule, theCalculationParameter);
        
        this.itsUnitMolecule = theUnitMolecule;
        
        FormalChargeCalculator.inputFormalCharge(theUnitMolecule);
    }
    
    public AbstractTotalForceFieldEnergyFunction(Protein theProtein, IAtomContainer theLigand, T theCalculationParameter) {
        super(theProtein, theLigand, theCalculationParameter);
        
        this.itsUnitMolecule = this.itsMolecule;
        
        FormalChargeCalculator.inputFormalCharge(this.itsMolecule);
    }
    
    public AbstractTotalForceFieldEnergyFunction(IAtomContainer theMolecule, T theCalculationParameter, 
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsUnitMolecule = theMolecule;
        
        FormalChargeCalculator.inputFormalCharge(theMolecule);
    }
    
    public IAtomContainer getUnitMolecule() {
        return itsUnitMolecule;
    }
    
    public abstract double calculateIntraEnergy();

    public abstract double calculateInterEnergy();
    
//    public abstract void removeCalculableSet(List<Integer> theRemoveAtomNumberList);
    
    public abstract AbstractUsableInMinimizationInEnergyFunction getIntraEnergyFunction();
    
    public abstract AbstractUsableInMinimizationInEnergyFunction getInterEnergyFunction();
    
    public abstract void updateCalculableAtomList();
    
    public void updateCalculableAtomList(IAtomContainer theMolecule) {
        List<Vector3d> theGradientVectorList = new ArrayList<>();
        
        if(this.itsMolecule.getAtomCount() != theMolecule.getAtomCount()) {
            System.err.println("There are not same molecule");
            return;
        }
        
        for(int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
            Vector3d theOriginalPosition = new Vector3d(this.itsMolecule.getAtom(ai));
            Vector3d theNewPosition = new Vector3d(theMolecule.getAtom(ai));
            
            theGradientVectorList.add(Vector3dCalculator.minus(theNewPosition, theOriginalPosition));
            this.itsMolecule.getAtom(ai).setPoint3d(theMolecule.getAtom(ai).getPoint3d());
        }
        
        this.updateCalculableAtomList();
        
        for(int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
            Vector3d thePosition = new Vector3d(this.itsMolecule.getAtom(ai));
            
            this.itsMolecule.getAtom(ai).setPoint3d(Vector3dCalculator.minus(thePosition, theGradientVectorList.get(ai)).toPoint3d());
        }
    }
    
    
}
