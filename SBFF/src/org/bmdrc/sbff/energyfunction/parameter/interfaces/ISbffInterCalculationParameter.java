package org.bmdrc.sbff.energyfunction.parameter.interfaces;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.interfaces.ICalculationParameter;
import org.bmdrc.sbff.energyfunction.Cutoff;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public interface ISbffInterCalculationParameter extends Serializable, ICalculationParameter {

    Cutoff getElectroStaticCutoff();

    void setElectroStaticCutoff(Cutoff theElectroStaticCutoff);

    Cutoff getNonbondingCutoff();

    void setNonbondingCutoff(Cutoff theNonbondingCutoff);
    
    Double getMinElectroStaticCutoff();
    
    void setMinElectroStaticCutoff(Double theMinCutoff);
    
    Double getMaxElectroStaticCutoff();
    
    void setMaxElectroStaticCutoff(Double theMaxCutoff);
    
    Double getMinNonbondingCutoff();
    
    void setMinNonbondingCutoff(Double theMinCutoff);
    
    Double getMaxNonbondingCutoff();
    
    void setMaxNonbondingCutoff(Double theMaxCutoff);
}
