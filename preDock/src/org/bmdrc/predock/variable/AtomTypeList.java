package org.bmdrc.predock.variable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class AtomTypeList extends ArrayList<AtomType> implements Serializable {

    private static final long serialVersionUID = 6675465594447966673L;

    private Map<AtomType, Integer> itsAtomTypeCountMap;
    
    public AtomTypeList() {
        this.itsAtomTypeCountMap = new TreeMap<>();
    }

    public AtomTypeList(Collection<? extends AtomType> clctn) {
        super(clctn);
        this.itsAtomTypeCountMap = new TreeMap<>();
    }

    public Map<AtomType, Integer> getAtomTypeCountMap() {
        return itsAtomTypeCountMap;
    }

    public void setAtomTypeCountMap(Map<AtomType, Integer> theAtomTypeCountMap) {
        this.itsAtomTypeCountMap = theAtomTypeCountMap;
    }
}
