/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.interfaces;

import java.io.Serializable;

/**
 * This interface has hose code symbol <br>
 * This interface is used in HoseCodeGenerator
 * 
 * @author SungBo Hwang (tyamazaki@naver.com)
 */
public interface IHoseCodeSymbol extends Serializable {
    public static final String POSITIVE_CHARGE = "+";
    public static final String NEGATIVE_CHARGE = "-";
    public static final String DOUBLE_BOND_SYMBOL = "=";
    public static final String TRIPLE_BOND_SYMBOL = "%";
    public static final String AROMATIC_SYMBOL = "*";
    public static final String RING_CLOSER_SYMBOL = "&";
    public static final String AROMATIC_CARBON = "*C";
    public static final String HYDROGEN_SYMBOL = "H";
    public static final String FLUORINE_SYMBOL = "F";
    public static final String CHLORIDE_SYMBOL = "X";
    public static final String IODIDE_SYMBOL = "I";
    public static final String BROMIDE_SYMBOL = "Y";
    public static final String CARBON_SYMBOL = "C";
    public static final String OXYGEN_SYMBOL = "O";
    public static final String NITROGEN_SYMBOL = "N";
    public static final String PHOSPHATE_SYMBOL = "P";
    public static final String SULFUR_SYMBOL = "S";
}
