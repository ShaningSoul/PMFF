/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.energyfunction.interfaces;

import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public interface UsableInMinimizationInEnergyFunction extends UsableInMinimization {

    public void calculateGradient();
    
    public void calculateGradient(Vector theSearchVector);
    
    public void calculateGradient(IAtomContainer theMolecule);
    
    public void reloadGradientVector(IAtomContainer theMolecule);
    
    public Vector calculateGradientVector(IAtomContainer theMolecule, double theScalingFactor);
    
    public void reloadGradientVector(Vector theSearchVector);
    
    public Vector calculateGradientVector(Vector theSearchVector, double theScalingFactor);
    
    public void reloadConjugatedGradientVector(IAtomContainer theMolecule);
    
    public Vector calculateConjugatedGradientVector(IAtomContainer theMolecule, double theScalingFactor);
    
    public void reloadConjugatedGradientVector(Vector theSearchVector);
    
    public Vector calculateConjugatedGradientVector(Vector theSearchVector, double theScalingFactor);
}
