package org.bmdrc.sbff.energyfunction.parameter.interfaces;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.interfaces.ICalculationParameter;
import org.bmdrc.sbff.energyfunction.Cutoff;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public interface ISbffIntraCalculationParameter extends Serializable, ICalculationParameter {

    Cutoff getIntraElectroStaticCutoff();

    void setIntraElectroStaticCutoff(Cutoff theIntraElectroStaticCutoff);

    Cutoff getIntraNonbondingCutoff();

    void setIntraNonbondingCutoff(Cutoff theIntraNonbondingCutoff);
    
    Double getMinIntraElectroStaticCutoff();
    
    void setMinIntraElectroStaticCutoff(Double theMinCutoff);
    
    Double getMaxIntraElectroStaticCutoff();
    
    void setMaxIntraElectroStaticCutoff(Double theMaxCutoff);
    
    Double getMinIntraNonbondingCutoff();
    
    void setMinIntraNonbondingCutoff(Double theMinCutoff);
    
    Double getMaxIntraNonbondingCutoff();
    
    void setMaxIntraNonbondingCutoff(Double theMaxCutoff);
}
