package org.bmdrc.sbff.energyfunction.parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculationParameter;
import org.bmdrc.sbff.energyfunction.Cutoff;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.ui.abstracts.Constant;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class SbffIntraCalculationParameter extends AbstractCalculationParameter implements Serializable, ISbffIntraCalculationParameter {

    private static final long serialVersionUID = -9126661931751663532L;

    private Cutoff itsIntraElectroStaticCutoff;
    private Cutoff itsIntraNonbondingCutoff;
    
    public SbffIntraCalculationParameter() {
        this(Constant.DEFAULT_DIELECTRIC_CONSTANT, new ArrayList<Integer>());
    }
    
    public SbffIntraCalculationParameter(Double theDielectricConstant) {
        this(Constant.DEFAULT_ELECTROSTATIC_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_ELECTROSTATIC_MAXIMUM_SCALING_FUNCTION_DISTANCE, 
                Constant.DEFAULT_NONBONDING_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_NONBONDING_MAXIMUM_SCALING_FUNCTION_DISTANCE,
                theDielectricConstant);
    }
    
    public SbffIntraCalculationParameter(List<Integer> theNotCalculatedAtomIndexList) {
        this(Constant.DEFAULT_ELECTROSTATIC_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_ELECTROSTATIC_MAXIMUM_SCALING_FUNCTION_DISTANCE, 
                Constant.DEFAULT_NONBONDING_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_NONBONDING_MAXIMUM_SCALING_FUNCTION_DISTANCE,
                theNotCalculatedAtomIndexList);
    }
    
    public SbffIntraCalculationParameter(Double theDielectricConstant, List<Integer> theNotCalculatedAtomIndexList) {
        this(Constant.DEFAULT_ELECTROSTATIC_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_ELECTROSTATIC_MAXIMUM_SCALING_FUNCTION_DISTANCE, 
                Constant.DEFAULT_NONBONDING_MINIMUM_SCALING_FUNCTION_DISTANCE, Constant.DEFAULT_NONBONDING_MAXIMUM_SCALING_FUNCTION_DISTANCE,
                theDielectricConstant, theNotCalculatedAtomIndexList);
    }
    
    public SbffIntraCalculationParameter(Double theMinIntraElectroStaticCutoff, Double theMaxIntraElectroStaticCutoff,
            Double theMinIntraNonbondingCutoff, Double theMaxIntraNonbondingCutoff) {
        super();
        
        this.itsIntraElectroStaticCutoff = new Cutoff(theMinIntraElectroStaticCutoff, theMaxIntraElectroStaticCutoff);
        this.itsIntraNonbondingCutoff = new Cutoff(theMinIntraNonbondingCutoff, theMaxIntraNonbondingCutoff);
    }
    
    public SbffIntraCalculationParameter(Double theMinIntraElectroStaticCutoff, Double theMaxIntraElectroStaticCutoff,
            Double theMinIntraNonbondingCutoff, Double theMaxIntraNonbondingCutoff, List<Integer> theNotCalculatedAtomIndexList) {
        super(theNotCalculatedAtomIndexList);
        
        this.itsIntraElectroStaticCutoff = new Cutoff(theMinIntraElectroStaticCutoff, theMaxIntraElectroStaticCutoff);
        this.itsIntraNonbondingCutoff = new Cutoff(theMinIntraNonbondingCutoff, theMaxIntraNonbondingCutoff);
    }
    
    public SbffIntraCalculationParameter(Double theMinIntraElectroStaticCutoff, Double theMaxIntraElectroStaticCutoff,
            Double theMinIntraNonbondingCutoff, Double theMaxIntraNonbondingCutoff, Double theDielectricConstant) {
        super(theDielectricConstant);
        
        this.itsIntraElectroStaticCutoff = new Cutoff(theMinIntraElectroStaticCutoff, theMaxIntraElectroStaticCutoff);
        this.itsIntraNonbondingCutoff = new Cutoff(theMinIntraNonbondingCutoff, theMaxIntraNonbondingCutoff);
    }
    
    public SbffIntraCalculationParameter(Double theMinIntraElectroStaticCutoff, Double theMaxIntraElectroStaticCutoff,
            Double theMinIntraNonbondingCutoff, Double theMaxIntraNonbondingCutoff, Double theDielectricConstant, 
            List<Integer> theNotCalculatedAtomIndexList) {
        super(theDielectricConstant, theNotCalculatedAtomIndexList);
        
        this.itsIntraElectroStaticCutoff = new Cutoff(theMinIntraElectroStaticCutoff, theMaxIntraElectroStaticCutoff);
        this.itsIntraNonbondingCutoff = new Cutoff(theMinIntraNonbondingCutoff, theMaxIntraNonbondingCutoff);
    }

    public SbffIntraCalculationParameter(ISbffIntraCalculationParameter theIntraCalculationParameter) {
        super(theIntraCalculationParameter);
        
        this.itsIntraElectroStaticCutoff = new Cutoff(theIntraCalculationParameter.getMinIntraElectroStaticCutoff(), 
                theIntraCalculationParameter.getMaxIntraElectroStaticCutoff());
        this.itsIntraNonbondingCutoff = new Cutoff(theIntraCalculationParameter.getMinIntraNonbondingCutoff(), 
                theIntraCalculationParameter.getMaxIntraNonbondingCutoff());
    }
    
    @Override
    public Cutoff getIntraElectroStaticCutoff() {
        return this.itsIntraElectroStaticCutoff;
    }

    @Override
    public void setIntraElectroStaticCutoff(Cutoff theIntraElectroStaticCutoff) {
        this.itsIntraElectroStaticCutoff = theIntraElectroStaticCutoff;
    }

    @Override
    public Cutoff getIntraNonbondingCutoff() {
        return this.itsIntraNonbondingCutoff;
    }

    @Override
    public void setIntraNonbondingCutoff(Cutoff theIntraNonbondingCutoff) {
        this.itsIntraNonbondingCutoff = theIntraNonbondingCutoff;
    }
    
    @Override
    public Double getMinIntraElectroStaticCutoff() {
        return this.itsIntraElectroStaticCutoff.getMin();
    }
    
    @Override
    public void setMinIntraElectroStaticCutoff(Double theMinCutoff) {
        this.itsIntraElectroStaticCutoff.setMin(theMinCutoff);
    }
    
    @Override
    public Double getMaxIntraElectroStaticCutoff() {
        return this.itsIntraElectroStaticCutoff.getMax();
    }
    
    @Override
    public void setMaxIntraElectroStaticCutoff(Double theMaxCutoff) {
        this.itsIntraElectroStaticCutoff.setMax(theMaxCutoff);
    }
    
    @Override
    public Double getMinIntraNonbondingCutoff() {
        return this.itsIntraNonbondingCutoff.getMin();
    }
    
    @Override
    public void setMinIntraNonbondingCutoff(Double theMinCutoff) {
        this.itsIntraNonbondingCutoff.setMin(theMinCutoff);
    }
    
    @Override
    public Double getMaxIntraNonbondingCutoff() {
        return this.itsIntraNonbondingCutoff.getMax();
    }
    
    @Override
    public void setMaxIntraNonbondingCutoff(Double theMaxCutoff) {
        this.itsIntraNonbondingCutoff.setMax(theMaxCutoff);
    }

    @Override
    public AbstractCalculationParameter clone() {
        return new SbffIntraCalculationParameter(this);
    }
}
