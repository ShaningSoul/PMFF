/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.basicchemistry.minimization.interfaces;

import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 03. 23
 */
public interface UsableInMinimization {

    public void reloadGradientVector();
    
    public Vector calculateGradientVector(double theScalingFactor);

    public void reloadConjugatedGradientVector();
    
    public Vector calculateConjugatedGradientVector(double theScalingFactor);

    public Vector getConjugatedGradientVector();
    
    public Vector getGradientVector();

    public void makeConjugatedGradient(double theScalingFactor);
}
