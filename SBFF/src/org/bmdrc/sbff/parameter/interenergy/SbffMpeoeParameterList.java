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
public class SbffMpeoeParameterList implements Serializable {

    private static final long serialVersionUID = -3242862510098479008L;

    private Map<Integer, MpeoeParameter> itsMpeoeEnPlusCoefficientMap;
    private Map<String, Double> itsDampValueMap;
    
    public SbffMpeoeParameterList() {
        this.itsMpeoeEnPlusCoefficientMap = new HashMap<>();
        this.itsDampValueMap = new HashMap<>();
        
        this.__generateMpeoeEnPlusCoefficientMap();
        this.__generateDampValueMap();
    }

    public Map<Integer, MpeoeParameter> getMpeoeEnPlusCoefficientMap() {
        return itsMpeoeEnPlusCoefficientMap;
    }

    public Map<String, Double> getDampValueMap() {
        return itsDampValueMap;
    }
    
    private void __generateMpeoeEnPlusCoefficientMap() {
        this.itsMpeoeEnPlusCoefficientMap.put(11, new MpeoeParameter(11, 9.795, 25.195, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(12, new MpeoeParameter(12, 9.228, 7.919, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(13, new MpeoeParameter(13, 7.967, 4.862, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(14, new MpeoeParameter(14, 8.218, 8.288, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(15, new MpeoeParameter(15, 12.397, 6.667, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(16, new MpeoeParameter(16, 7.767, 12.429, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(17, new MpeoeParameter(17, 10.0, 5.0, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(18, new MpeoeParameter(18, 9.292, 3.764, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(19, new MpeoeParameter(19, 9.624, 35.675, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(111, new MpeoeParameter(111, 8.66, 6.893, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(112, new MpeoeParameter(112, 5.159, 3.005, 0.2));
        this.itsMpeoeEnPlusCoefficientMap.put(113, new MpeoeParameter(113, 7.772, 2.008, 0.35));
        this.itsMpeoeEnPlusCoefficientMap.put(114, new MpeoeParameter(114, 14.384, 7.411, 0.2));
        this.itsMpeoeEnPlusCoefficientMap.put(115, new MpeoeParameter(115, 7.991, 5.791, 0.35));
        this.itsMpeoeEnPlusCoefficientMap.put(116, new MpeoeParameter(115, 9.022, 9.09, 0.35));
        this.itsMpeoeEnPlusCoefficientMap.put(117, new MpeoeParameter(117, 5.125, 4.841, -0.2));
        this.itsMpeoeEnPlusCoefficientMap.put(118, new MpeoeParameter(118, 4.721, 4.155, -0.2));
        this.itsMpeoeEnPlusCoefficientMap.put(119, new MpeoeParameter(119, 8.148, 7.388, 0.35));
        this.itsMpeoeEnPlusCoefficientMap.put(1, new MpeoeParameter(1, 7.711, 31.958, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(2, new MpeoeParameter(2, 7.428, 6.722, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(3, new MpeoeParameter(3, 9.097, 3.727, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(4, new MpeoeParameter(4, 7.78, 20.0, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(101, new MpeoeParameter(101, 7.067, 8.445, 0.35));
        this.itsMpeoeEnPlusCoefficientMap.put(102, new MpeoeParameter(102, 9.024, 9.962, 0.05));
        this.itsMpeoeEnPlusCoefficientMap.put(103, new MpeoeParameter(103, 7.963, 19.067, 0.1));
        this.itsMpeoeEnPlusCoefficientMap.put(104, new MpeoeParameter(104, 9.605, 15.465, 0.35));
        this.itsMpeoeEnPlusCoefficientMap.put(21, new MpeoeParameter(21, 10.896, 11.136, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(22, new MpeoeParameter(22, 14.284, 13.857, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(23, new MpeoeParameter(23, 12.941, 12.808, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(24, new MpeoeParameter(24, 13.685, 12.446, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(25, new MpeoeParameter(25, 15.409, 12.341, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(26, new MpeoeParameter(26, 7.767, 12.429, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(27, new MpeoeParameter(27, 14.495, 13.039, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(28, new MpeoeParameter(28, 13.062, 10.86, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(121, new MpeoeParameter(121, 14.664, 9.324, -0.6));
        this.itsMpeoeEnPlusCoefficientMap.put(122, new MpeoeParameter(122, 17.692, 6.478, -0.6));
        this.itsMpeoeEnPlusCoefficientMap.put(123, new MpeoeParameter(123, 16.263, 13.13, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(124, new MpeoeParameter(124, 16.5, 17.0, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(125, new MpeoeParameter(125, 15.23, 15.878, -0.8));
        this.itsMpeoeEnPlusCoefficientMap.put(31, new MpeoeParameter(31, 15.13, 3.155, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(32, new MpeoeParameter(32, 12.941, 3.24, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(33, new MpeoeParameter(33, 15.478, 11.914, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(34, new MpeoeParameter(34, 12.184, 13.538, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(35, new MpeoeParameter(35, 14.385, 8.896, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(36, new MpeoeParameter(36, 11.7, 31.0, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(37, new MpeoeParameter(37, 15.5, 12.5, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(38, new MpeoeParameter(38, 12.792, 5.295, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(39, new MpeoeParameter(39, 14.209, 9.611, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(131, new MpeoeParameter(131, 15.722, 14.277, -0.4));
        this.itsMpeoeEnPlusCoefficientMap.put(132, new MpeoeParameter(132, 14.615, 2.975, -0.4));
        this.itsMpeoeEnPlusCoefficientMap.put(133, new MpeoeParameter(133, 7.967, 15.621, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(134, new MpeoeParameter(134, 16.234, 6.428, -0.05));
        this.itsMpeoeEnPlusCoefficientMap.put(41, new MpeoeParameter(41, 9.34, 12.157, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(42, new MpeoeParameter(42, 10.435, 5.126, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(43, new MpeoeParameter(43, 7.861, 3.92, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(44, new MpeoeParameter(44, 12.892, 18.852, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(45, new MpeoeParameter(45, 8.599, 5.952, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(141, new MpeoeParameter(141, 3.329, 8.156, 1.6));
        this.itsMpeoeEnPlusCoefficientMap.put(51, new MpeoeParameter(51, 11.133, 17.7, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(52, new MpeoeParameter(52, 4.664, 2.951, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(151, new MpeoeParameter(151, 2.972, 6.209, 1.4));
        this.itsMpeoeEnPlusCoefficientMap.put(61, new MpeoeParameter(61, 4.402, 7.703, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(71, new MpeoeParameter(71, 13.246, 16.570, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(72, new MpeoeParameter(72, 11.861, 13.647, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(73, new MpeoeParameter(73, 11.649, 13.388, 0.0));
        this.itsMpeoeEnPlusCoefficientMap.put(74, new MpeoeParameter(74, 11.375, 17.898, 0.0));
    }

    private void __generateDampValueMap() {
        this.itsDampValueMap.put("f1", 0.482);
        this.itsDampValueMap.put("f2", 0.569);
        this.itsDampValueMap.put("f3", 0.501);
        this.itsDampValueMap.put("f4", 0.53);
        this.itsDampValueMap.put("f5", 0.972);
        this.itsDampValueMap.put("f6", 0.467);
        this.itsDampValueMap.put("f7", 0.703);
        this.itsDampValueMap.put("f8", 0.466);
        this.itsDampValueMap.put("f9", 0.683);
        this.itsDampValueMap.put("f10", 0.805);
        this.itsDampValueMap.put("f11", 0.441);
        this.itsDampValueMap.put("f12", 0.549);
        this.itsDampValueMap.put("f13", 0.664);
        this.itsDampValueMap.put("f14", 0.699);
        this.itsDampValueMap.put("f15", 0.731);
        this.itsDampValueMap.put("f16", 0.501);
        this.itsDampValueMap.put("f17", 0.457);
        this.itsDampValueMap.put("f18", 0.99);
        this.itsDampValueMap.put("f19", 0.98);
        this.itsDampValueMap.put("f20", 0.554);
        this.itsDampValueMap.put("f21", 0.21);
    }
}
