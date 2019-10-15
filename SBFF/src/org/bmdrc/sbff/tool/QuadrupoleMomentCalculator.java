/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.sbff.tool;

import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.sbff.parameter.interenergy.SbffMpeoeParameterList;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 03. 23
 */
public class QuadrupoleMomentCalculator {

    private MpeoeChargeCalculator itsChargeCalculator;
    //constant Integer variable
    private final int DIMENSION_SIZE = 3;
    //constant Double variable
    //constant Double variable
    private final double ELECTRON_CHARGE = 1.602e-19;
    private final double DISTANCE = 1.0e-10;
    private final double CONVERSION_FACTOR = this.ELECTRON_CHARGE * this.DISTANCE / 3.33564e-30;

    public QuadrupoleMomentCalculator() {
        this.itsChargeCalculator = new MpeoeChargeCalculator();
    }
    
    public void setMpeoeParameterList(SbffMpeoeParameterList theList) {
        this.itsChargeCalculator.setParameterList(theList);
    }
    
    public Vector3d calculateQuadrupoleMoment(IAtomContainer theMolecule) {
        this.__initialize(theMolecule);
        
        Vector3d theCenterOfMass = new Vector3d(GeometryTools.get3DCentreOfMass(theMolecule));
        Vector3d theQuadrupoleMoment = new Vector3d();
        
        for(IAtom theAtom : theMolecule.atoms()) {
            Vector3d theDirectionVector = Vector3dCalculator.minus(new Vector3d(theAtom), theCenterOfMass);
            Vector3d theSquaredVector = Vector3dCalculator.productPerElement(theDirectionVector, theDirectionVector);
            double theCharge = (Double)theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY);
            
            for(int di = 0; di < this.DIMENSION_SIZE; di++) {
                theQuadrupoleMoment.set(di, theQuadrupoleMoment.get(di) + 0.5 * theCharge * (2.0 * theSquaredVector.get(di) - theSquaredVector.get((di+1)%this.DIMENSION_SIZE) 
                        - theSquaredVector.get((di+2)%this.DIMENSION_SIZE)));
            }
        }
        
        return Vector3dCalculator.productByScalar(this.CONVERSION_FACTOR, theQuadrupoleMoment);
    }
    
    public double calculateQuadrupoleMomentInScalar(IAtomContainer theMolecule) {
        Vector3d theQuadrupoleMoment = this.calculateQuadrupoleMoment(theMolecule);
        double theScalar = 0.0;
        
        for(Double theValue : theQuadrupoleMoment) {
            theScalar += theValue;
        }
        
        return theScalar;
    }
    
    private void __initialize(IAtomContainer theMolecule) {
        for(IAtom theAtom : theMolecule.atoms()) {
            if(theAtom.getPoint3d() == null) {
                theAtom.setPoint3d(new Point3d(theAtom.getPoint2d().x, theAtom.getPoint2d().y, 0.0));
            }
        }
        
        this.itsChargeCalculator.inputMpeoeCharge(theMolecule);
    }
}
