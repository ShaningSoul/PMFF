package org.bmdrc.predock.variable;

import java.io.Serializable;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class ConformerGeneratorParameter implements Serializable {

    private static final long serialVersionUID = -4391494358378700899L;
    
    private Double itsMinimumEnergyVariance;
    private Double itsMaximumEnergyVariance;
    private Double itsRotationStepSize;
    private Integer itsMaximumConformerCount;
    private SbffIntraCalculationParameter itsEnergyCalculationParameter;
    
    //constant Double variable
    public static final Double DEFAULT_MINIMUM_ENERGY_VARIANCE = 0.0001;
    public static final Double DEFAULT_MAXIMUM_ENERGY_VARIANCE = 1.0;
    public static final Double DEFAULT_ROTATION_STEP_SIZE = 60.0;
    public static final Integer DEFAULT_MAXIMUM_CONFORMER_COUNT = Integer.MAX_VALUE;

    public ConformerGeneratorParameter() {
        this(ConformerGeneratorParameter.DEFAULT_MINIMUM_ENERGY_VARIANCE, ConformerGeneratorParameter.DEFAULT_MAXIMUM_ENERGY_VARIANCE, ConformerGeneratorParameter.DEFAULT_ROTATION_STEP_SIZE,
                ConformerGeneratorParameter.DEFAULT_MAXIMUM_CONFORMER_COUNT, new SbffIntraCalculationParameter());
    }

    public ConformerGeneratorParameter(Double itsMinimumEnergyVariance, Double itsMaximumEnergyVariance, Double itsRotationStepSize, Integer theMaximumConformerCount,
            SbffIntraCalculationParameter theEnergyCalculationParameter) {
        this.itsMinimumEnergyVariance = itsMinimumEnergyVariance;
        this.itsMaximumEnergyVariance = itsMaximumEnergyVariance;
        this.itsRotationStepSize = itsRotationStepSize;
        this.itsMaximumConformerCount = theMaximumConformerCount;
        this.itsEnergyCalculationParameter = theEnergyCalculationParameter;
    }

    public ConformerGeneratorParameter(ConformerGeneratorParameter theConformerGeneratorParameter) {
        this.itsMinimumEnergyVariance = theConformerGeneratorParameter.itsMinimumEnergyVariance;
        this.itsMaximumEnergyVariance = theConformerGeneratorParameter.itsMaximumEnergyVariance;
        this.itsRotationStepSize = theConformerGeneratorParameter.itsRotationStepSize;
        this.itsMaximumConformerCount = theConformerGeneratorParameter.itsMaximumConformerCount;
        this.itsEnergyCalculationParameter = new SbffIntraCalculationParameter(theConformerGeneratorParameter.itsEnergyCalculationParameter);
    }
    
    public Double getMinimumEnergyVariance() {
        return itsMinimumEnergyVariance;
    }

    public void setMinimumEnergyVariance(Double theMinimumEnergyVariance) {
        this.itsMinimumEnergyVariance = theMinimumEnergyVariance;
    }

    public Double getMaximumEnergyVariance() {
        return itsMaximumEnergyVariance;
    }

    public void setMaximumEnergyVariance(Double theMaximumEnergyVariance) {
        this.itsMaximumEnergyVariance = theMaximumEnergyVariance;
    }

    public Double getRotationStepSize() {
        return itsRotationStepSize;
    }

    public void setRotationStepSize(Double theRotationStepSize) {
        this.itsRotationStepSize = theRotationStepSize;
    }

    public Integer getMaximumConformerCount() {
        return itsMaximumConformerCount;
    }

    public void setMaximumConformerCount(Integer theMaximumConformerCount) {
        this.itsMaximumConformerCount = theMaximumConformerCount;
    }

    public SbffIntraCalculationParameter getEnergyCalculationParameter() {
        return itsEnergyCalculationParameter;
    }

    public void setEnergyCalculationParameter(SbffIntraCalculationParameter theEnergyCalculationParameter) {
        this.itsEnergyCalculationParameter = theEnergyCalculationParameter;
    }
}
