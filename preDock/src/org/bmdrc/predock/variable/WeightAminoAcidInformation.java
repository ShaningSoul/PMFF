package org.bmdrc.predock.variable;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class WeightAminoAcidInformation implements Serializable, Comparable<WeightAminoAcidInformation> {

    private static final long serialVersionUID = 126878872928264155L;

    private Integer itsAminoAcidSerial;
    private Double itsWeight;

    public WeightAminoAcidInformation() {
    }

    public WeightAminoAcidInformation(Integer theAminoAcidSerial, Double theWeight) {
        this.itsAminoAcidSerial = theAminoAcidSerial;
        this.itsWeight = theWeight;
    }

    public Integer getAminoAcidSerial() {
        return itsAminoAcidSerial;
    }

    public void setAminoAcidSerial(Integer theAminoAcidSerial) {
        this.itsAminoAcidSerial = theAminoAcidSerial;
    }

    public Double getWeight() {
        return itsWeight;
    }

    public void setWeight(Double theWeight) {
        this.itsWeight = theWeight;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.itsAminoAcidSerial);
        hash = 43 * hash + Objects.hashCode(this.itsWeight);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WeightAminoAcidInformation other = (WeightAminoAcidInformation) obj;
        if (!Objects.equals(this.itsAminoAcidSerial, other.itsAminoAcidSerial)) {
            return false;
        }
        if (!Objects.equals(this.itsWeight, other.itsWeight)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Serial : " + this.itsAminoAcidSerial + ", Weight : " + String.format("%.4e", this.itsWeight);
    }

    @Override
    public int compareTo(WeightAminoAcidInformation t) {
        int theResult = t.itsAminoAcidSerial.compareTo(this.itsAminoAcidSerial);
        
        return theResult == 0 ? t.itsWeight.compareTo(this.itsWeight) : theResult;
    }
}
