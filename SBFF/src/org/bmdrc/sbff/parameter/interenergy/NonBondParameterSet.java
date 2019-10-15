/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.parameter.interenergy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class NonBondParameterSet implements Serializable {

    private static final long serialVersionUID = -6875431591396675358L;

    private class Parameter {

        private final double itsEpsilon;
        private final double itsSigma;

        public Parameter(double itsEpsilon, double itsSigma) {
            this.itsEpsilon = itsEpsilon;
            this.itsSigma = itsSigma;
        }
    }

    private Map<String, Parameter> itsNonBondParameterMap;
    private Map<Integer, String> itsNonBondTypeMapByMpeoeType;

    public NonBondParameterSet() {
        this._generateParamterMap();
        this._generateNonBondTypeMapByMpeoeType();
    }

    public Double getEpsilon(String theNonbondingType) {
        Parameter theParameter = this.itsNonBondParameterMap.get(theNonbondingType);

        return theParameter == null ? null : theParameter.itsEpsilon;
    }

    public Double getSigma(String theNonbondingType) {
        Parameter theParameter = this.itsNonBondParameterMap.get(theNonbondingType);

        return theParameter == null ? null : theParameter.itsSigma;
    }

    public Map<Integer, String> getNonBondTypeMapByMpeoeType() {
        return itsNonBondTypeMapByMpeoeType;
    }

    public void setNonBondTypeMapByMpeoeType(Map<Integer, String> theNonBondTypeMapByMpeoeType) {
        this.itsNonBondTypeMapByMpeoeType = theNonBondTypeMapByMpeoeType;
    }

    public Map<Integer, String> setNonBondTypeMapByMpeoeType() {
        return itsNonBondTypeMapByMpeoeType;
    }

    public void _generateParamterMap() {
        this.itsNonBondParameterMap = new HashMap<>();

        this.itsNonBondParameterMap.put("H1", new Parameter(0.031, 2.628));
        this.itsNonBondParameterMap.put("H2", new Parameter(0.094, 2.076));
        this.itsNonBondParameterMap.put("H3", new Parameter(0.011, 2.815));
        this.itsNonBondParameterMap.put("H4", new Parameter(0.089, 2.094));
        this.itsNonBondParameterMap.put("H5", new Parameter(0.098, 2.067));
        this.itsNonBondParameterMap.put("C1", new Parameter(0.042, 3.697));
        this.itsNonBondParameterMap.put("C2", new Parameter(0.096, 3.555));
        this.itsNonBondParameterMap.put("C3", new Parameter(0.139, 3.074));
        this.itsNonBondParameterMap.put("C4", new Parameter(0.139, 3.074));
        this.itsNonBondParameterMap.put("C5", new Parameter(0.088, 2.931));
        this.itsNonBondParameterMap.put("N1", new Parameter(0.235, 2.833));
        this.itsNonBondParameterMap.put("N2", new Parameter(0.105, 3.118));
        this.itsNonBondParameterMap.put("N3", new Parameter(0.157, 3.011));
        this.itsNonBondParameterMap.put("N4", new Parameter(0.157, 3.011));
        this.itsNonBondParameterMap.put("N5", new Parameter(0.388, 2.682));
        this.itsNonBondParameterMap.put("O1", new Parameter(0.226, 2.717));
        this.itsNonBondParameterMap.put("O2", new Parameter(0.226, 2.717));
        this.itsNonBondParameterMap.put("O3", new Parameter(0.200, 2.655));
        this.itsNonBondParameterMap.put("O5", new Parameter(0.181, 2.922));
        this.itsNonBondParameterMap.put("OO", new Parameter(0.226, 3.550));
        this.itsNonBondParameterMap.put("S1", new Parameter(0.480, 3.554));
        this.itsNonBondParameterMap.put("S2", new Parameter(0.480, 3.554));
        this.itsNonBondParameterMap.put("P1", new Parameter(0.220, 3.800));
        this.itsNonBondParameterMap.put("F1", new Parameter(0.069, 3.458));
        this.itsNonBondParameterMap.put("Cl1", new Parameter(0.069, 3.970));
        this.itsNonBondParameterMap.put("Br1", new Parameter(0.100, 4.260));
    }

    protected void _generateNonBondTypeMapByMpeoeType() {
        this.setNonBondTypeMapByMpeoeType(new HashMap<Integer, String>());

        this.setNonBondTypeMapByMpeoeType().put(11, "C1");
        this.setNonBondTypeMapByMpeoeType().put(12, "C2");
        this.setNonBondTypeMapByMpeoeType().put(13, "C1");
        this.setNonBondTypeMapByMpeoeType().put(14, "C1");
        this.setNonBondTypeMapByMpeoeType().put(15, "C1");
        this.setNonBondTypeMapByMpeoeType().put(16, "C1");
        this.setNonBondTypeMapByMpeoeType().put(17, "C1");
        this.setNonBondTypeMapByMpeoeType().put(18, "C1");
        this.setNonBondTypeMapByMpeoeType().put(19, "C1");
        this.setNonBondTypeMapByMpeoeType().put(111, "C4");
        this.setNonBondTypeMapByMpeoeType().put(112, "C5");
        this.setNonBondTypeMapByMpeoeType().put(113, "C4");
        this.setNonBondTypeMapByMpeoeType().put(114, "C1");
        this.setNonBondTypeMapByMpeoeType().put(116, "C2");
        this.setNonBondTypeMapByMpeoeType().put(1, "H1");
        this.setNonBondTypeMapByMpeoeType().put(2, "H3");
        this.setNonBondTypeMapByMpeoeType().put(3, "H1");
        this.setNonBondTypeMapByMpeoeType().put(4, "H1");
        this.setNonBondTypeMapByMpeoeType().put(101, "H5");
        this.setNonBondTypeMapByMpeoeType().put(102, "H1");
        this.setNonBondTypeMapByMpeoeType().put(103, "H1");
        this.setNonBondTypeMapByMpeoeType().put(21, "O3");
        this.setNonBondTypeMapByMpeoeType().put(22, "O1");
        this.setNonBondTypeMapByMpeoeType().put(23, "O3");
        this.setNonBondTypeMapByMpeoeType().put(24, "O3");
        this.setNonBondTypeMapByMpeoeType().put(25, "O1");
        this.setNonBondTypeMapByMpeoeType().put(26, "O3");
        this.setNonBondTypeMapByMpeoeType().put(27, "O1");
        this.setNonBondTypeMapByMpeoeType().put(28, "O3");
        this.setNonBondTypeMapByMpeoeType().put(121, "O5");
        this.setNonBondTypeMapByMpeoeType().put(122, "O3");
        this.setNonBondTypeMapByMpeoeType().put(123, "O3");
        this.setNonBondTypeMapByMpeoeType().put(124, "O3");
        this.setNonBondTypeMapByMpeoeType().put(31, "N2");
        this.setNonBondTypeMapByMpeoeType().put(32, "N1");
        this.setNonBondTypeMapByMpeoeType().put(33, "N3");
        this.setNonBondTypeMapByMpeoeType().put(34, "N4");
        this.setNonBondTypeMapByMpeoeType().put(35, "N5");
        this.setNonBondTypeMapByMpeoeType().put(36, "N3");
        this.setNonBondTypeMapByMpeoeType().put(37, "N3");
        this.setNonBondTypeMapByMpeoeType().put(38, "N5");
        this.setNonBondTypeMapByMpeoeType().put(39, "N2");
        this.setNonBondTypeMapByMpeoeType().put(131, "N5");
        this.setNonBondTypeMapByMpeoeType().put(132, "N5");
        this.setNonBondTypeMapByMpeoeType().put(133, "N5");
        this.setNonBondTypeMapByMpeoeType().put(134, "N1");
        this.setNonBondTypeMapByMpeoeType().put(41, "S1");
        this.setNonBondTypeMapByMpeoeType().put(42, "S1");
        this.setNonBondTypeMapByMpeoeType().put(43, "S2");
        this.setNonBondTypeMapByMpeoeType().put(44, "S2");
        this.setNonBondTypeMapByMpeoeType().put(45, "S2");
        this.setNonBondTypeMapByMpeoeType().put(141, "S2");
        this.setNonBondTypeMapByMpeoeType().put(51, "P1");
        this.setNonBondTypeMapByMpeoeType().put(52, "P1");
        this.setNonBondTypeMapByMpeoeType().put(151, "P1");
        this.setNonBondTypeMapByMpeoeType().put(61, "Si1");
        this.setNonBondTypeMapByMpeoeType().put(71, "F1");
        this.setNonBondTypeMapByMpeoeType().put(72, "Cl1");
        this.setNonBondTypeMapByMpeoeType().put(73, "Br1");
        this.setNonBondTypeMapByMpeoeType().put(74, "I1");
    }
}
