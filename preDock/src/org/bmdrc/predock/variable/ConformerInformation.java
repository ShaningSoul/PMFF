package org.bmdrc.predock.variable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class ConformerInformation extends HashMap<Integer, Double> implements Serializable {

    private static final long serialVersionUID = 7783464417415655968L;

    public ConformerInformation() {
    }

    public ConformerInformation(Map<? extends Integer, ? extends Double> map) {
        super(map);
    }
}
