package org.bmdrc.predock.energyfunction;

import java.io.Serializable;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractDerivativeFunctionOfInternalCoordinate;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffInterCalculationParameter;
import org.bmdrc.sbff.interenergy.SbffHydrogenBondEnergyFunction;
import org.bmdrc.sbff.interenergy.calculableset.CalculableHydrogenBondComponent;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondParameterSet;
import org.bmdrc.ui.abstracts.Constant;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class HydrogenBondEnergyCalculatorInGrid extends SbffHydrogenBondEnergyFunction implements Serializable {

    private static final long serialVersionUID = -1039279760765766499L;

    private CalculableHydrogenBondListInGrid itsCalculableHydrogenBondList;
    //constant Double variable
    private final double ATTRACTION_POWER = 6.0;
    private final double REPULSION_POWER = 12.0;
    //constant String variable
    private final String HYDROGEN_BOND_ENERGY_KEY = "Hydrogen Bond Energy";

    public HydrogenBondEnergyCalculatorInGrid(IAtomContainer theMolecule, ISbffInterCalculationParameter theCalculationParameter,
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, null, theCalculationParameter, theMoveTogetherAtomNumberMap);
        
        this.itsCalculableHydrogenBondList = new CalculableHydrogenBondListInGrid(this.itsMolecule);
    }

    public CalculableHydrogenBondListInGrid getCalculableHydrogenBondListInGrid() {
        return itsCalculableHydrogenBondList;
    }

    public void setCalculableHydrogenBondList(CalculableHydrogenBondListInGrid theCalculableHydrogenBondList) {
        this.itsCalculableHydrogenBondList = theCalculableHydrogenBondList;
    }

    public void regenerateCalculableSet() {
        this.itsCalculableHydrogenBondList = new CalculableHydrogenBondListInGrid(this.itsMolecule);
    }

    @Override
    public Double calculateEnergyFunction() {
        this.itsEnergy = 0.0;

        for (CalculableHydrogenBondSetInGrid theCalculableHydrogenBondSet : this.itsCalculableHydrogenBondList) {
            double theEnergy = this.__calculateEnergyFunction(theCalculableHydrogenBondSet);

            theCalculableHydrogenBondSet.setEnergy(theEnergy);
            this.itsEnergy += theEnergy;
        }

        this.itsMolecule.setProperty(this.HYDROGEN_BOND_ENERGY_KEY, String.format("%.5f", this.itsEnergy));

        return this.itsEnergy;
    }

    private double __calculateEnergyFunction(CalculableHydrogenBondSetInGrid theCalculableHydrogenBondSet) {
        double theEnergy = theCalculableHydrogenBondSet.getType().equals(CalculableHydrogenBondSetInGrid.ACCEPTOR_TYPE_IN_LIGAND) ?
                this.__calculateEnergyInAcceptorGridAtom(theCalculableHydrogenBondSet) :
                this.__calculateEnergyInDonorGridAtom(theCalculableHydrogenBondSet);
        
        return theEnergy;
    }

    private Double __calculateEnergyInAcceptorGridAtom(CalculableHydrogenBondSetInGrid theCalculableHydrogenBondSet) {
        SbffHydrogenBondParameterSet theParameter = theCalculableHydrogenBondSet.getHydrogenBondParameter();
        double theHADistance = this.itsMolecule.getAtom(theCalculableHydrogenBondSet.getHAtomIndex()).getPoint3d().distance(this.itsMolecule.getAtom(theCalculableHydrogenBondSet.getAAtomIndex()).getPoint3d());
        double theXADistance = this.itsMolecule.getAtom(theCalculableHydrogenBondSet.getXAtomIndex()).getPoint3d().distance(this.itsMolecule.getAtom(theCalculableHydrogenBondSet.getAAtomIndex()).getPoint3d());
        double theEnergy = 0.0;

        if (theHADistance < this.itsCalculationParameter.getMaxNonbondingCutoff()) {
            theEnergy += -(theParameter.getHAParameter().getB() / Math.pow(theHADistance, this.ATTRACTION_POWER))
                    + (theParameter.getHAParameter().getD() / Math.pow(theHADistance, this.REPULSION_POWER));
            theEnergy += -(theParameter.getXAParameter().getB() / Math.pow(theXADistance, this.ATTRACTION_POWER))
                    + (theParameter.getXAParameter().getD() / Math.pow(theXADistance, this.REPULSION_POWER));

            if (theHADistance > this.itsCalculationParameter.getMinNonbondingCutoff()) {
                theEnergy *= (this.itsCalculationParameter.getMaxNonbondingCutoff() - theHADistance) / (this.itsCalculationParameter.getMaxNonbondingCutoff() - this.itsCalculationParameter.getMinNonbondingCutoff());
            }
        }

        return theEnergy;
    }

    private Double __calculateEnergyInDonorGridAtom(CalculableHydrogenBondSetInGrid theCalculableHydrogenBondSet) {
        SbffHydrogenBondParameterSet theParameter = theCalculableHydrogenBondSet.getHydrogenBondParameter();
        double theHADistance = this.itsMolecule.getAtom(theCalculableHydrogenBondSet.getHAtomIndex()).getPoint3d().distance(this.itsMolecule.getAtom(theCalculableHydrogenBondSet.getAAtomIndex()).getPoint3d());
        double theBHDistance = this.itsMolecule.getAtom(theCalculableHydrogenBondSet.getBAtomIndex()).getPoint3d().distance(this.itsMolecule.getAtom(theCalculableHydrogenBondSet.getHAtomIndex()).getPoint3d());
        double theEnergy = 0.0;

        if (theHADistance < this.itsCalculationParameter.getMaxNonbondingCutoff()) {
            theEnergy += -(theParameter.getHAParameter().getB() / Math.pow(theHADistance, this.ATTRACTION_POWER))
                    + (theParameter.getHAParameter().getD() / Math.pow(theHADistance, this.REPULSION_POWER));
            theEnergy += -(theParameter.getBHParameter().getB() / Math.pow(theBHDistance, this.ATTRACTION_POWER))
                    + (theParameter.getBHParameter().getD() / Math.pow(theBHDistance, this.REPULSION_POWER));

            if (theHADistance > this.itsCalculationParameter.getMinNonbondingCutoff()) {
                theEnergy *= (this.itsCalculationParameter.getMaxNonbondingCutoff() - theHADistance) / (this.itsCalculationParameter.getMaxNonbondingCutoff() - this.itsCalculationParameter.getMinNonbondingCutoff());
            }
        }

        return theEnergy;
    }
    
    @Override
    public void calculateGradient() {
        for (CalculableHydrogenBondSetInGrid theHydrogenBondSet : this.itsCalculableHydrogenBondList) {
            double theHADistance = this.itsMolecule.getAtom(theHydrogenBondSet.getHAtomIndex()).getPoint3d().distance(
                    this.itsMolecule.getAtom(theHydrogenBondSet.getAAtomIndex()).getPoint3d());

            if (theHADistance <= this.itsCalculationParameter.getMaxNonbondingCutoff()) {
                this.__calculateGradient(theHydrogenBondSet);
            }
        }

        this.itsCalculableHydrogenBondList.sortNonZeroGradient();
    }

    private void __calculateGradient(CalculableHydrogenBondSetInGrid theHydrogenBondSet) {
        double theHADistance = this.itsMolecule.getAtom(theHydrogenBondSet.getHAtomIndex()).getPoint3d().distance(
                this.itsMolecule.getAtom(theHydrogenBondSet.getAAtomIndex()).getPoint3d());

        for (CalculableHydrogenBondComponent theComponent : theHydrogenBondSet.getComponentList()) {
            double theGradient = this.__calculateGradient(theComponent, theHADistance);

            theComponent.setGradient(theGradient);
        }
    }

    private double __calculateGradient(CalculableHydrogenBondComponent theComponent, double theHADistance) {
        double theDistance = this.itsMolecule.getAtom(theComponent.getAtomIndexList().get(Constant.FIRST_INDEX)).getPoint3d()
                .distance(this.itsMolecule.getAtom(theComponent.getAtomIndexList().get(Constant.SECOND_INDEX)).getPoint3d());
        double theGradient = 0.0;

        if (theDistance <= this.itsCalculationParameter.getMaxNonbondingCutoff()) {
            theGradient = this.ATTRACTION_POWER * (theComponent.getParameter().getB() / Math.pow(theDistance, this.ATTRACTION_POWER + 1))
                    - this.REPULSION_POWER * (theComponent.getParameter().getD() / Math.pow(theDistance, this.REPULSION_POWER + 1));

            if (theDistance > this.itsCalculationParameter.getMinNonbondingCutoff()) {
                theGradient *= (this.itsCalculationParameter.getMaxNonbondingCutoff() - theHADistance) / (this.itsCalculationParameter.getMaxNonbondingCutoff() - this.itsCalculationParameter.getMinNonbondingCutoff());
                theGradient += -(-(theComponent.getParameter().getB() / Math.pow(theDistance, this.ATTRACTION_POWER))
                        + (theComponent.getParameter().getD() / Math.pow(theDistance, this.REPULSION_POWER)))
                        / (this.itsCalculationParameter.getMaxNonbondingCutoff() - this.itsCalculationParameter.getMinNonbondingCutoff());
            }
        }

        return theGradient;
    }

    @Override
    public void reloadGradientVector() {
        this._initializeGradientVector();

        for (CalculableHydrogenBondSetInGrid theHydrogenBondSet : this.itsCalculableHydrogenBondList) {
            for (CalculableHydrogenBondComponent theComponent : theHydrogenBondSet.getComponentList()) {
                this.__calculateGradientVector(theComponent, 1.0);
            }
        }
    }

    private void __calculateGradientVector(CalculableHydrogenBondComponent theComponent, double theScalingFactor) {
        double theGradient = -theComponent.getGradient();

        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theComponent.getAtomIndexList().get(AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE))) {
            this.__calculateGradientVector(theComponent, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE, theGradient * theScalingFactor);
        }

        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theComponent.getAtomIndexList().get(AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE))) {
            this.__calculateGradientVector(theComponent, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE, theGradient * theScalingFactor);
        }
    }

    private void __calculateGradientVector(CalculableHydrogenBondComponent theComponent, int theTargetIndex, int theCounterIndex, double theGradient) {
        if (theComponent.getAtomIndexList().isEmpty()) {
            System.out.println("HydrogenBondEnergyFunction Error!!");
        }

        Vector3d theGradientVectorInDimension = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theComponent.getAtomIndexList(),
                theTargetIndex, theGradient);

        this._setGradient(theComponent.getAtomIndexList().get(theTargetIndex), theGradientVectorInDimension);
    }

    @Override
    public void reloadConjugatedGradientVector() {
        this._initializeConjugatedGradientVector();

        for (CalculableHydrogenBondSetInGrid theHydrogenBondSet : this.itsCalculableHydrogenBondList) {
            double theHADistance = this.itsMolecule.getAtom(theHydrogenBondSet.getHAtomIndex()).getPoint3d().distance(
                    this.itsMolecule.getAtom(theHydrogenBondSet.getAAtomIndex()).getPoint3d());

            if (theHADistance <= this.itsCalculationParameter.getMaxNonbondingCutoff()) {
                for (CalculableHydrogenBondComponent theComponent : theHydrogenBondSet.getComponentList()) {
                    this.__reloadConjugatedGradientVector(theComponent, 1.0);
                }
            }
        }
    }

    private void __reloadConjugatedGradientVector(CalculableHydrogenBondComponent theComponent, double theScalingFactor) {
        double theGradient = -theComponent.getConjugatedGradient() * theScalingFactor;

        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theComponent.getAtomIndexList().get(AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE))) {
            this.__reloadConjugatedGradientVector(theComponent, AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE, theGradient);
        }

        if (!this.itsCalculationParameter.getNotCalculableAtomIndexList().contains(theComponent.getAtomIndexList().get(AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE))) {
            this.__reloadConjugatedGradientVector(theComponent, AbstractDerivativeFunctionOfInternalCoordinate.END_ATOM_INDEX_IN_DISTANCE,
                    AbstractDerivativeFunctionOfInternalCoordinate.START_ATOM_INDEX_IN_DISTANCE, theGradient);
        }
    }

    private void __reloadConjugatedGradientVector(CalculableHydrogenBondComponent theComponent, int theTargetIndex, int theCounterIndex, double theGradient) {
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(theComponent.getAtomIndexList().get(theTargetIndex),
                theComponent.getAtomIndexList().get(theCounterIndex));
        Vector3d theGradientVectorInDimension = this.itsFirstDerivativeFunction.calculateFirstDerivativeVectorInDistance(this.itsMolecule, theComponent.getAtomIndexList(),
                theTargetIndex, theGradient);

        this._setConjugatedGradient(theTargetIndex, theGradientVectorInDimension);
    }

    @Override
    public void makeConjugatedGradient(double theScalingFactor) {
        this.itsCalculableHydrogenBondList.makeConjugatedGradient(theScalingFactor);
    }

}
