package org.bmdrc.predock.energyfunction;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.sbff.energyfunction.parameter.SbffInterCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffInterCalculationParameter;
import org.bmdrc.sbff.interenergy.SbffElectroStaticEnergyFunction;
import org.bmdrc.sbff.interenergy.SbffNonbondingEnergyFunction;
import org.bmdrc.sbff.interenergy.calculableset.CalculableElectroStaticList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableHydrogenBondList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableNonbondingList;
import org.bmdrc.sbff.interenergy.calculableset.CalculableNonbondingSet;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class GridEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction<ISbffInterCalculationParameter> implements Serializable {

    private static final long serialVersionUID = 1488125939473031307L;

    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;
    private SbffElectroStaticEnergyFunction itsElectroStaticEnergyFunction;
    private SbffNonbondingEnergyFunction itsNonbondingEnergyFunction;
    private HydrogenBondEnergyCalculatorInGrid itsHydrogenBondEnergyFunctionInGrid;
    //constant String variable
    private final String ENERGY_KEY = "Grid energy";

    public GridEnergyFunction(IAtomContainer theMolecule, SbffInterCalculationParameter theParameter) {
        super(theMolecule, theParameter, new MoveTogetherAtomNumberList(theMolecule));

        this.itsElectroStaticEnergyFunction = new SbffElectroStaticEnergyFunction(this.itsMolecule, new CalculableElectroStaticList(this.itsMolecule,
                this.itsMoveTogetherAtomNumberMap.getTopologicalDistanceMatrix(),
                this.itsCalculationParameter.getNotCalculableAtomIndexList()), theParameter,
                this.itsMoveTogetherAtomNumberMap);
        this.itsHydrogenBondEnergyFunctionInGrid = new HydrogenBondEnergyCalculatorInGrid(this.itsMolecule,
                theParameter, this.itsMoveTogetherAtomNumberMap);
        this.itsNonbondingEnergyFunction = new SbffNonbondingEnergyFunction(this.itsMolecule, this.__getCalculableNonbondingList(), 
                theParameter, this.itsMoveTogetherAtomNumberMap);
    }

    public SbffElectroStaticEnergyFunction getElectroStaticEnergyFunction() {
        return itsElectroStaticEnergyFunction;
    }

    public SbffNonbondingEnergyFunction getNonbondingEnergyFunction() {
        return itsNonbondingEnergyFunction;
    }

    public HydrogenBondEnergyCalculatorInGrid getHydrogenBondEnergyFunctionInGrid() {
        return itsHydrogenBondEnergyFunctionInGrid;
    }

    public void regenerateCalculableSet() {
        this.itsElectroStaticEnergyFunction.setCalculableElectroStaticList(new CalculableElectroStaticList(this.itsMolecule,
                this.itsMoveTogetherAtomNumberMap.getTopologicalDistanceMatrix(),
                this.itsCalculationParameter.getNotCalculableAtomIndexList()));
        this.itsHydrogenBondEnergyFunctionInGrid.regenerateCalculableSet();
        this.itsNonbondingEnergyFunction.setCalculableNonbondingList(this.__getCalculableNonbondingList());
    }

    private CalculableNonbondingList __getCalculableNonbondingList() {
        CalculableNonbondingList theCalculableList = new CalculableNonbondingList(this.itsMolecule,
                this.itsElectroStaticEnergyFunction.getCalculableElectroStaticList(), new CalculableHydrogenBondList(),
                this.itsCalculationParameter.getNotCalculableAtomIndexList());

        for (int ni = theCalculableList.size() - 1; ni >= 0; ni--) {
            CalculableNonbondingSet theCalculableSet = theCalculableList.get(ni);

            if (this.itsHydrogenBondEnergyFunctionInGrid.getCalculableHydrogenBondListInGrid()
                    .isHydrogenBondCalculableSet(theCalculableSet.getFirstAtomIndex(), theCalculableSet.getSecondAtomIndex())) {
                theCalculableList.remove(ni);
            }
        }

        return theCalculableList;
    }

    @Override
    public Double calculateEnergyFunction(IAtomContainer theMolecule) {
        try {
            IAtomContainer theCopiedMolecule = (IAtomContainer) theMolecule.clone();

            this.setMolecule(theMolecule);
            double theEnergy = this.calculateEnergyFunction();
            this.setMolecule(theCopiedMolecule);

            return theEnergy;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule Clone Error");
        }

        return null;
    }

    @Override
    public Double calculateEnergyFunction() {
        this.__setMoleculeInEachFunction();

        double theElectroStaticEnergy = this.itsElectroStaticEnergyFunction.calculateEnergyFunction();
        double theNonbondingEnergy = this.itsNonbondingEnergyFunction.calculateEnergyFunction();
        double theHydrogenBondEnergy = this.itsHydrogenBondEnergyFunctionInGrid.calculateEnergyFunction();

        this.itsEnergy = theElectroStaticEnergy + theNonbondingEnergy + theHydrogenBondEnergy;

        this.itsMolecule.setProperty(this.ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private void __setMoleculeInEachFunction() {
        this.itsElectroStaticEnergyFunction.setMolecule(this.itsMolecule);
        this.itsNonbondingEnergyFunction.setMolecule(this.itsMolecule);
        this.itsHydrogenBondEnergyFunctionInGrid.setMolecule(this.itsMolecule);
    }

    @Override
    public void calculateGradient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reloadGradientVector() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reloadConjugatedGradientVector() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
