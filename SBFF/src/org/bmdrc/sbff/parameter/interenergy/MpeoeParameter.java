/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.sbff.parameter.interenergy;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2016. 03. 22
 */
public class MpeoeParameter implements Serializable {

    private static final long serialVersionUID = -1036034227834072300L;

    private Integer itsIndex;
    private Double itsA;
    private Double itsB;
    private Double itsInitialCharge;
    
    public MpeoeParameter() {
        this(0, 0.0, 0.0, 0.0);
    }

    public MpeoeParameter(Integer theIndex) {
        this(theIndex, 0.0, 0.0, 0.0);
    }
    

    public MpeoeParameter(Integer theIndex, Double theA, Double theB, Double theInitialCharge) {
        this.itsIndex = theIndex;
        this.itsA = theA;
        this.itsB = theB;
        this.itsInitialCharge = theInitialCharge;
    }

    public Integer getIndex() {
        return itsIndex;
    }

    public void setIndex(Integer theIndex) {
        this.itsIndex = theIndex;
    }
    
    public Double getA() {
        return itsA;
    }

    public void setA(Double theA) {
        this.itsA = theA;
    }

    public Double getB() {
        return itsB;
    }

    public void setB(Double theB) {
        this.itsB = theB;
    }

    public Double getInitialCharge() {
        return itsInitialCharge;
    }

    public void setInitialCharge(Double theInitialCharge) {
        this.itsInitialCharge = theInitialCharge;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append("(index, a, b, initial charge) = (").append(this.itsIndex).append(", ")
                .append(this.itsA).append(", ").append(this.itsB).append(", ").append(this.itsInitialCharge);
        
        return theStringBuilder.toString();
    }
}
