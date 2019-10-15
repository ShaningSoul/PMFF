/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.sbff.tool;

import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.sbff.parameter.interenergy.SbffMpeoeParameterList;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 03. 19
 */
public class DipoleMomentCalculator {

    private MpeoeChargeCalculator itsChargeCalculator;
    private Vector3d itsCenterOfMass;
    //constant Double variable
    private final double ELECTRON_CHARGE = 1.602e-19;
    private final double DISTANCE = 1.0e-10;
    private final double CONVERSION_FACTOR = this.ELECTRON_CHARGE * this.DISTANCE / 3.33564e-30;
    //constant String variable
    public static final String DIPOLE_MONENT_KEY = "Dipole monent";
    
    public DipoleMomentCalculator() {
        this.itsChargeCalculator = new MpeoeChargeCalculator();
    }

    public void setMpeoeParameterList(SbffMpeoeParameterList theList) {
        this.itsChargeCalculator.setParameterList(theList);
    }
    
    public Vector3d calculateDipoleMoment(IAtomContainer theMolecule) {
        Vector3d theCenterOfMass = new Vector3d(GeometryTools.get3DCentreOfMass(theMolecule));
        Vector3d theDipoleMoment = new Vector3d();

        this.itsChargeCalculator.inputMpeoeCharge(theMolecule);

        for (IAtom theAtom : theMolecule.atoms()) {
            Vector3d theDirectionVector = Vector3dCalculator.minus(new Vector3d(theAtom), theCenterOfMass);
            Vector3d theAtomDipole = Vector3dCalculator.productByScalar(this.CONVERSION_FACTOR * (Double) theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY), theDirectionVector);
            
            theDipoleMoment.add(theAtomDipole);
            theAtom.setProperty(DipoleMomentCalculator.DIPOLE_MONENT_KEY, theAtomDipole);
        }

        theMolecule.setProperty(DipoleMomentCalculator.DIPOLE_MONENT_KEY, theDipoleMoment);
        
        return theDipoleMoment;
    }

    public double calculateDipoleMomentForScalar(IAtomContainer theMolecule) {
        return this.calculateDipoleMoment(theMolecule).length();
    }
}
