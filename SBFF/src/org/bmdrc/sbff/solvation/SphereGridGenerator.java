/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.solvation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.parameter.solvation.VanDerWaalsParameterList;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SphereGridGenerator implements Serializable {

    private static final long serialVersionUID = -1611929077661797782L;

    private Double itsInterval;
    private Double itsShell;
    private VanDerWaalsParameterList itsParameterList;
    private Map<Double, GridList> itsSphereGrid;
    //constant Double variable
    public static final double INITIAL_INTERVAL = 0.5;
    public static final double INITIAL_SHELL = 1.4;

    public SphereGridGenerator() {
        this.itsInterval = this.INITIAL_INTERVAL;
        this.itsShell = this.INITIAL_SHELL;
        this.itsParameterList = new VanDerWaalsParameterList();
    }

    public SphereGridGenerator(VanDerWaalsParameterList theParameterList) {
        this.itsInterval = this.INITIAL_INTERVAL;
        this.itsShell = this.INITIAL_SHELL;
        this.itsParameterList = theParameterList;
    }

    public Map<Double, GridList> draw() {
        return this.draw(this.INITIAL_SHELL, this.INITIAL_INTERVAL);
    }
    
    public Map<Double, GridList> draw(Double theShell) {
        return this.draw(theShell, this.INITIAL_INTERVAL);
    }
    
    public Map<Double, GridList> draw(Double theShell, Double theInterval) {
        this.itsSphereGrid = new HashMap<>();
        this.itsShell = theShell;

        for (int pi = 1, pEnd = this.itsParameterList.size(); pi < pEnd ; pi++) {
            double theRange = this.itsParameterList.get(pi) + this.itsShell;
            int theNumberOfCircle = (int) Math.round(Math.PI * theRange / this.itsInterval);
            double theDTheta = Math.PI / theNumberOfCircle;
            
            this.itsSphereGrid.put(theRange, new GridList());
            
            if (theNumberOfCircle % 2 == 1) {
                for (int ci = 0; ci < theNumberOfCircle / 2; ci++) {
                    double theTheta = Math.PI / 2.0 - (1 + ci) * theDTheta;
                    double theDpi = this.__getAngle(theRange, theTheta);
                    
                    this.__circle(theRange, theDpi, theTheta, true);
                }
                this.__circle(theRange, theDTheta, Math.PI / 2.0, false);
            } else {
                for(int ci = 0; ci < theNumberOfCircle / 2; ci++) {
                    double theTheta = Math.PI / 2.0 - (0.5 + ci) * theDTheta;
                    double theDpi = this.__getAngle(theRange, theTheta);
                    
                    this.__circle(theRange, theDpi, theTheta, true);
                }
            }
        }

        return this.itsSphereGrid;
    }

    private void __circle(double theRange, double theDpi, double theTheta, boolean theJugment) {
        final double theCosineTheta = Math.cos(theTheta);
        final double theSineTheta = Math.sin(theTheta);
        final double theZ = theRange * theCosineTheta;
        final double theCircleRadius = theRange * theSineTheta;
        double thePi = 0.0;

        for (int ni = 0, nEnd = (int) Math.round(2.0 * Math.PI / theDpi); ni < nEnd; ni++) {
            double theX = theCircleRadius * Math.cos(thePi);
            double theY = theCircleRadius * Math.sin(thePi);

            this.itsSphereGrid.get(theRange).add(new Grid(theX, theZ, theY));
            
            if (theJugment) {
                this.itsSphereGrid.get(theRange).add(new Grid(theX, -theZ, theY));
            }
            
            thePi += theDpi;
        }
    }
    
    private double __getAngle(double theRange, double theTheta) {
        return 2.0 * Math.PI / Math.round(2.0 * Math.PI * theRange * Math.sin(theTheta) / this.itsInterval);
    }
}
