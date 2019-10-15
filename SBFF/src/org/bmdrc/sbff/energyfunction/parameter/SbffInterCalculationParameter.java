package org.bmdrc.sbff.energyfunction.parameter;

import java.io.Serializable;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculationParameter;
import org.bmdrc.sbff.energyfunction.Cutoff;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffInterCalculationParameter;
import org.bmdrc.ui.abstracts.Constant;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class SbffInterCalculationParameter extends AbstractCalculationParameter implements Serializable, ISbffInterCalculationParameter {

    private static final long serialVersionUID = -4831139502648461L;

    private Cutoff itsElectroStaticCutoff;
    private Cutoff itsNonbondingCutoff;
    
    public SbffInterCalculationParameter() {
        this(Constant.DEFAULT_ELECTROSTATIC_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_ELECTROSTATIC_MAXIMUM_SCALING_FUNCTION_DISTANCE, 
                Constant.DEFAULT_NONBONDING_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_NONBONDING_MAXIMUM_SCALING_FUNCTION_DISTANCE);
    }

    public SbffInterCalculationParameter(Double theDielectricConstant) {
        this(Constant.DEFAULT_ELECTROSTATIC_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_ELECTROSTATIC_MAXIMUM_SCALING_FUNCTION_DISTANCE, 
                Constant.DEFAULT_NONBONDING_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_NONBONDING_MAXIMUM_SCALING_FUNCTION_DISTANCE,
                theDielectricConstant);
    }
    
    public SbffInterCalculationParameter(List<Integer> theNotCalculatedAtomIndexList) {
        this(Constant.DEFAULT_ELECTROSTATIC_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_ELECTROSTATIC_MAXIMUM_SCALING_FUNCTION_DISTANCE, 
                Constant.DEFAULT_NONBONDING_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_NONBONDING_MAXIMUM_SCALING_FUNCTION_DISTANCE,
                theNotCalculatedAtomIndexList);
    }
    
    public SbffInterCalculationParameter(Double theMinElectroStaticCutoff, Double theMaxElectroStaticCutoff, Double theMinNonbondingCutoff, 
            Double theMaxNonbondingCutoff) {
        super();
        
        this.itsElectroStaticCutoff = new Cutoff(theMinElectroStaticCutoff, theMaxElectroStaticCutoff);
        this.itsNonbondingCutoff = new Cutoff(theMinNonbondingCutoff, theMaxNonbondingCutoff);
    }
    
    public SbffInterCalculationParameter(Double theMinElectroStaticCutoff, Double theMaxElectroStaticCutoff, Double theMinNonbondingCutoff,
            Double theMaxNonbondingCutoff, List<Integer> theNotCalculatedAtomIndexList) {
        super(theNotCalculatedAtomIndexList);
        
        this.itsElectroStaticCutoff = new Cutoff(theMinElectroStaticCutoff, theMaxElectroStaticCutoff);
        this.itsNonbondingCutoff = new Cutoff(theMinNonbondingCutoff, theMaxNonbondingCutoff);
    }
    
    public SbffInterCalculationParameter(Double theMinElectroStaticCutoff, Double theMaxElectroStaticCutoff, Double theMinNonbondingCutoff,
            Double theMaxNonbondingCutoff, Double theDielectricConstant) {
        super(theDielectricConstant);
        
        this.itsElectroStaticCutoff = new Cutoff(theMinElectroStaticCutoff, theMaxElectroStaticCutoff);
        this.itsNonbondingCutoff = new Cutoff(theMinNonbondingCutoff, theMaxNonbondingCutoff);
    }
    
    public SbffInterCalculationParameter(Double theMinElectroStaticCutoff, Double theMaxElectroStaticCutoff, Double theMinNonbondingCutoff,
            Double theMaxNonbondingCutoff, Double theDielectricConstant, List<Integer> theNotCalculatedAtomIndexList) {
        super(theDielectricConstant, theNotCalculatedAtomIndexList);
        
        this.itsElectroStaticCutoff = new Cutoff(theMinElectroStaticCutoff, theMaxElectroStaticCutoff);
        this.itsNonbondingCutoff = new Cutoff(theMinNonbondingCutoff, theMaxNonbondingCutoff);
    }
    
    public SbffInterCalculationParameter(ISbffInterCalculationParameter theCalculationParameter) {
        super(theCalculationParameter);
        
        this.itsElectroStaticCutoff = new Cutoff(theCalculationParameter.getMinElectroStaticCutoff(), theCalculationParameter.getMaxElectroStaticCutoff());
        this.itsNonbondingCutoff = new Cutoff(theCalculationParameter.getMinNonbondingCutoff(), theCalculationParameter.getMaxNonbondingCutoff());
    }

    @Override
    public Cutoff getElectroStaticCutoff() {
        return itsElectroStaticCutoff;
    }

    @Override
    public void setElectroStaticCutoff(Cutoff theElectroStaticCutoff) {
        this.itsElectroStaticCutoff = theElectroStaticCutoff;
    }

    @Override
    public Cutoff getNonbondingCutoff() {
        return itsNonbondingCutoff;
    }

    @Override
    public void setNonbondingCutoff(Cutoff theNonbondingCutoff) {
        this.itsNonbondingCutoff = theNonbondingCutoff;
    }

    @Override
    public Double getMinElectroStaticCutoff() {
        return this.itsElectroStaticCutoff.getMin();
    }
    
    @Override
    public void setMinElectroStaticCutoff(Double theMinCutoff) {
        this.itsElectroStaticCutoff.setMin(theMinCutoff);
    }
    
    @Override
    public Double getMaxElectroStaticCutoff() {
        return this.itsElectroStaticCutoff.getMax();
    }
    
    @Override
    public void setMaxElectroStaticCutoff(Double theMaxCutoff) {
        this.itsElectroStaticCutoff.setMax(theMaxCutoff);
    }
    
    @Override
    public Double getMinNonbondingCutoff() {
        return this.itsNonbondingCutoff.getMin();
    }
    
    @Override
    public void setMinNonbondingCutoff(Double theMinCutoff) {
        this.itsNonbondingCutoff.setMin(theMinCutoff);
    }
    
    @Override
    public Double getMaxNonbondingCutoff() {
        return this.itsNonbondingCutoff.getMax();
    }
    
    @Override
    public void setMaxNonbondingCutoff(Double theMaxCutoff) {
        this.itsNonbondingCutoff.setMax(theMaxCutoff);
    }

    @Override
    public AbstractCalculationParameter clone() {
        return new SbffInterCalculationParameter(this);
    }
}