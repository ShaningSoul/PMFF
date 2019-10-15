/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.sbff.tool;

import org.bmdrc.basicchemistry.crystal.CrystalInformation;
import org.bmdrc.basicchemistry.crystal.CrystalStructureGenerator;
import org.bmdrc.sbff.energyfunction.SbffEnergyFunction;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 11. 27
 */
public class HeatOfSublimationCalculator {

    //constant double variable
    private static double GAS_CONSTANT = 1.987e-3;
    private static double STANDARD_TEMPERATURE = 298.15;

    /**
     * calculate Heat of sublimation in standard condition
     * 
     * @param theMolecule template molecule having fraction coordinate and crystal information
     * @return heat of sublimation (kcal/mol)
     */
    public static double calculateHeatOfSublimation(IAtomContainer theMolecule) {
        return HeatOfSublimationCalculator.calculateHeatOfSublimation(theMolecule, HeatOfSublimationCalculator.STANDARD_TEMPERATURE);
    }
    
    /**
     * calculate heat of sublimation
     * 
     * @param theMolecule template molecule having fraction coordinate and crystal information
     * @param theKelvinTemperature temperature in kelvin
     * @return heat of sublimation (kcal/mol)
     */
    public static double calculateHeatOfSublimation(IAtomContainer theMolecule, double theKelvinTemperature) {
        double theEnergyInSolid = HeatOfSublimationCalculator.__getEnergyInSolidPhase(theMolecule);
        double theEnergyInGas = 0;//HeatOfSublimationCalculator.__getEnergyInGas(theMolecule);
        
//        System.out.println(theEnergyInSolid + " " + theEnergyInGas + " " + (HeatOfSublimationCalculator.GAS_CONSTANT * theKelvinTemperature));
        return HeatOfSublimationCalculator.GAS_CONSTANT * theKelvinTemperature + (theEnergyInGas - theEnergyInSolid);
    }

    private static double __getEnergyInSolidPhase(IAtomContainer theMolecule) {
        CrystalInformation theCrystalInformation = new CrystalInformation(theMolecule);
        IAtomContainer theCrystal = CrystalStructureGenerator.generateCrystalCell(theMolecule, theCrystalInformation.getCellDimensionVector(),
                theCrystalInformation.getCoordinateAngleVector());
        SbffEnergyFunction theEnergyFunction = new SbffEnergyFunction(theCrystal, theMolecule);
        
//        theEnergyFunction.calculateEnergyFunction();
        System.out.println("Solid : " + theEnergyFunction.calculateIntraEnergy() + " " + theEnergyFunction.calculateInterEnergy());
        return theEnergyFunction.calculateEnergyFunction();
    }

    private static double __getEnergyInGas(IAtomContainer theMolecule) {
        CrystalInformation theCrystalInformation = new CrystalInformation(theMolecule);
        IAtomContainer theMoleculeInCartesianCoordinate = CrystalStructureGenerator.generateMoleculeByOriginalCoordinate(theMolecule, theCrystalInformation.getCellDimensionVector(), 
                theCrystalInformation.getCoordinateAngleVector());
        SbffEnergyFunction theEnergyFunction = new SbffEnergyFunction(theMoleculeInCartesianCoordinate);
        
        System.out.println("Gas : " + theEnergyFunction.calculateIntraEnergy() + " " + theEnergyFunction.calculateInterEnergy());
        
//        DefaultLineSearcher theLineSearcher = new DefaultLineSearcher(theEnergyFunction);
//        ConjugatedGradientMinimizerInMolecularStructure theMinimizer = new ConjugatedGradientMinimizerInMolecularStructure(theLineSearcher, 1e-4);
//        IAtomContainer theOptimizedMolecule = theMinimizer.optimize();
//        SbffEnergyFunction theOptimizedEnergyFunction = new SbffEnergyFunction(theOptimizedMolecule);
//        theEnergyFunction.setMolecule(theOptimizedMolecule);
//        System.out.println("Gas : " + theOptimizedEnergyFunction.calculateIntraEnergy() + " " + theOptimizedEnergyFunction.calculateInterEnergy());
//        return Double.parseDouble(theOptimizedMolecule.getProperty(ConjugatedGradientMinimizerInMolecularStructure.OPTIMIZED_ENERGY).toString());
        return 0;
    }
}
