package org.bmdrc.predock.variable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class WeightAminoAcidInformationList extends ArrayList<WeightAminoAcidInformation> implements Serializable {

    private static final long serialVersionUID = 7459239788614019631L;

    public WeightAminoAcidInformationList() {
        super();
    }

    public WeightAminoAcidInformationList(int i) {
        super(i);
    }

    public WeightAminoAcidInformationList(Collection<? extends WeightAminoAcidInformation> clctn) {
        super(clctn);
    }
    
    public WeightAminoAcidInformationList(WeightAminoAcidInformation... theInformation) {
        super();
        
        Collections.addAll(this, theInformation);
    }
    
    public boolean containSerial(Integer theSerial) {
        for(WeightAminoAcidInformation theInformation : this) {
            if(theInformation.getAminoAcidSerial().equals(theInformation)) {
                return true;
            }
        }
        
        return false;
    }
    
    public WeightAminoAcidInformation get(Integer theSerial) {
        for(WeightAminoAcidInformation theInformation : this) {
            if(theInformation.getAminoAcidSerial().equals(theSerial)) {
                return theInformation;
            }
        }
        
        return null;
    }
}
