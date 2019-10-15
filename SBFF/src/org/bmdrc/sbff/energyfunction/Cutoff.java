package org.bmdrc.sbff.energyfunction;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class Cutoff implements Serializable {

    private static final long serialVersionUID = -8495698947503789195L;

    private Double itsMin;
    private Double itsMax;

    public Cutoff(Double itsMin, Double itsMax) {
        this.itsMin = itsMin;
        this.itsMax = itsMax;
    }

    public Double getMin() {
        return this.itsMin;
    }

    public void setMin(Double theMin) {
        this.itsMin = theMin;
    }

    public Double getMax() {
        return this.itsMax;
    }

    public void setMax(Double theMax) {
        this.itsMax = theMax;
    }
}
