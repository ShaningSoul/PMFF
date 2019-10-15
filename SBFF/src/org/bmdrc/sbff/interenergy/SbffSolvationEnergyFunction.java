/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.interenergy;

import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractEnergyFunction;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffInterCalculationParameter;
import org.bmdrc.sbff.solvation.GSFECalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffSolvationEnergyFunction extends AbstractEnergyFunction {

    private static final long serialVersionUID = -7218612755259888012L;

    private GSFECalculator itsSolvationEnergyFunction;
    //constant String variable
    private final String GSFE_ENERGY_KEY = "Solvation Energy";

    public SbffSolvationEnergyFunction(IAtomContainer theMolecule) {
        super(theMolecule);
        
        this.itsSolvationEnergyFunction = new GSFECalculator();
    }

    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = this.itsSolvationEnergyFunction.calculateGSFE(this.itsMolecule);
        
        this.itsMolecule.setProperty(this.GSFE_ENERGY_KEY, String.format("%.5f", this.itsEnergy));
        
        return this.itsEnergy;
    }
}
