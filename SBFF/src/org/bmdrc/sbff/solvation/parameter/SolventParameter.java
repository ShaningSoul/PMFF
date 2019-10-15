/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.solvation.parameter;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SolventParameter implements Serializable {

    private static final long serialVersionUID = 2237202619829329836L;

    private Double itsDielectricConstant;
    private Double itsReflectiveIndex;
    private Double itsSurfaceTension;
    private Double itsHydrogenBondAcidity;
    private Double itsHydrogenBondBasicity;

    public SolventParameter(Double theDielectricConstant, Double theReflectiveIndex, Double theSurfaceTension, Double theHydrogenBondAcidity, Double theHydrogenBondBasicity) {
        this.itsDielectricConstant = theDielectricConstant;
        this.itsReflectiveIndex = theReflectiveIndex;
        this.itsSurfaceTension = theSurfaceTension;
        this.itsHydrogenBondAcidity = theHydrogenBondAcidity;
        this.itsHydrogenBondBasicity = theHydrogenBondBasicity;
    }

    public Double getDielectricConstant() {
        return itsDielectricConstant;
    }

    public void setDielectricConstant(Double theDielectricConstant) {
        this.itsDielectricConstant = theDielectricConstant;
    }

    public Double getReflectiveIndex() {
        return itsReflectiveIndex;
    }

    public void setReflectiveIndex(Double theReflectiveIndex) {
        this.itsReflectiveIndex = theReflectiveIndex;
    }

    public Double getSurfaceTension() {
        return itsSurfaceTension;
    }

    public void setSurfaceTension(Double theSurfaceTension) {
        this.itsSurfaceTension = theSurfaceTension;
    }

    public Double getHydrogenBondAcidity() {
        return itsHydrogenBondAcidity;
    }

    public void setHydrogenBondAcidity(Double theHydrogenBondAcidity) {
        this.itsHydrogenBondAcidity = theHydrogenBondAcidity;
    }

    public Double getHydrogenBondBasicity() {
        return itsHydrogenBondBasicity;
    }

    public void setHydrogenBondBasicity(Double theHydrogenBondBasicity) {
        this.itsHydrogenBondBasicity = theHydrogenBondBasicity;
    }
}
