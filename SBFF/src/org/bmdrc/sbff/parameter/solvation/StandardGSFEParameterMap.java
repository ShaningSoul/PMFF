/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.parameter.solvation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.bmdrc.sbff.solvation.parameter.SolventParameter;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class StandardGSFEParameterMap implements Map<String, SolventParameter>, Serializable {
    private static final long serialVersionUID = -4373810740853508741L;

    private Map<String, SolventParameter> itsStandardGSFEParameterMap;

    public StandardGSFEParameterMap() {
        this.__generateStandardGSFEParameterMap();
    }

    private void __generateStandardGSFEParameterMap() {
        this.itsStandardGSFEParameterMap = new TreeMap<>();

        this.itsStandardGSFEParameterMap.put("1,2,4-trimethylbenzene", new SolventParameter(2.3653, 1.5048, 29.2, 0.0, 0.19));
        this.itsStandardGSFEParameterMap.put("1,2-dibromoethane", new SolventParameter(4.9313, 1.5387, 39.55, 0.1, 0.17));
        this.itsStandardGSFEParameterMap.put("1,2-dichloroethane", new SolventParameter(10.125, 1.4448, 31.86, 0.1, 0.11));
        this.itsStandardGSFEParameterMap.put("1,4-dioxane", new SolventParameter(2.2099, 1.4224, 32.75, 0.0, 0.64));
        this.itsStandardGSFEParameterMap.put("1-bromooctane", new SolventParameter(5.0244, 1.4524, 28.68, 0.0, 0.12));
        this.itsStandardGSFEParameterMap.put("1-butanol", new SolventParameter(17.3323, 1.3993, 24.94, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("1-chlorobutane", new SolventParameter(7.090877728, 1.402, 23.18, 0.0, 0.1));
        this.itsStandardGSFEParameterMap.put("1-decanol", new SolventParameter(7.5305, 1.4372, 28.51, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("1-fluorooctane", new SolventParameter(3.89, 1.3935, 23.53210026, 0.0, 0.1));
        this.itsStandardGSFEParameterMap.put("1-heptanol", new SolventParameter(11.321, 1.4242, 28.5, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("1-hexanol", new SolventParameter(12.5102, 1.4182, 25.81, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("1-iodohexadecane", new SolventParameter(3.5338, 1.4806, 32.29, 0.0, 0.15));
        this.itsStandardGSFEParameterMap.put("1-nonanol", new SolventParameter(8.5991, 1.4338, 27.89, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("1-octanol", new SolventParameter(9.8629, 1.429, 27.1, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("1-pentanol", new SolventParameter(15.13, 1.4101, 25.36, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("1-propanol", new SolventParameter(20.5237, 1.384, 23.32, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("2,2,4-trimethylpentane", new SolventParameter(1.9358, 1.3915, 16.8775, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("2,6-dimethylpyridine", new SolventParameter(7.1735, 1.4953, 30.96921998, 0.0, 0.63));
        this.itsStandardGSFEParameterMap.put("2-butanone", new SolventParameter(18.2457, 1.3788, 23.97, 0.0, 0.51));
        this.itsStandardGSFEParameterMap.put("2-methoxyethanol", new SolventParameter(17.2, 1.4024, 30.84, 0.3, 0.84));
        this.itsStandardGSFEParameterMap.put("2-methylpyridine", new SolventParameter(9.9533, 1.4957, 33.0, 0.0, 0.58));
        this.itsStandardGSFEParameterMap.put("2-Pentanol", new SolventParameter(13.17, 1.403, 23.45, 0.33, 0.56));
        this.itsStandardGSFEParameterMap.put("3-Methyl-1-butanol", new SolventParameter(14.7, 1.407, 23.71, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("4-methyl-2-pentanone", new SolventParameter(12.8871, 1.3962, 23.1375, 0.0, 0.51));
        this.itsStandardGSFEParameterMap.put("acetic acid", new SolventParameter(6.2528, 1.372, 27.1, 0.61, 0.44));
        this.itsStandardGSFEParameterMap.put("acetone", new SolventParameter(20.4933, 1.3588, 23.46, 0.04, 0.49));
        this.itsStandardGSFEParameterMap.put("acetonitrile", new SolventParameter(35.6881, 1.3442, 28.66, 0.07, 0.32));
        this.itsStandardGSFEParameterMap.put("Aniline", new SolventParameter(6.8882, 1.5863, 42.12, 0.26, 0.41));
        this.itsStandardGSFEParameterMap.put("anion_water", new SolventParameter(78.2, 1.33, 78.99, 0.82, 0.35));
        this.itsStandardGSFEParameterMap.put("anisole", new SolventParameter(4.2247, 1.5174, 35.1, 0.0, 0.29));
        this.itsStandardGSFEParameterMap.put("benzene", new SolventParameter(2.2706, 1.5011, 28.22, 0.0, 0.14));
        this.itsStandardGSFEParameterMap.put("benzonitrile", new SolventParameter(25.592, 1.5289, 38.79, 0.0, 0.33));
        this.itsStandardGSFEParameterMap.put("benzyl alcohol", new SolventParameter(12.4569, 1.5396, 34.7975, 0.33, 0.56));
        this.itsStandardGSFEParameterMap.put("bromobenzene", new SolventParameter(5.3954, 1.5597, 35.24, 0.0, 0.09));
        this.itsStandardGSFEParameterMap.put("bromoethane", new SolventParameter(9.01, 1.4239, 23.62, 0.0, 0.12));
        this.itsStandardGSFEParameterMap.put("bromoform", new SolventParameter(4.2488, 1.6005, 44.87, 0.15, 0.06));
        this.itsStandardGSFEParameterMap.put("carbon disulfide", new SolventParameter(2.6105, 1.6319, 31.58, 0.0, 0.07));
        this.itsStandardGSFEParameterMap.put("carbon tetrachloride", new SolventParameter(2.228, 1.4601, 26.43, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("cation_methanol", new SolventParameter(32.61316865, 1.3288, 22.07, 0.43, 0.47));
        this.itsStandardGSFEParameterMap.put("cation_water", new SolventParameter(78.2, 1.33, 71.99, 0.82, 0.35));
        this.itsStandardGSFEParameterMap.put("chlorobenzene", new SolventParameter(5.6968, 1.5241, 32.99, 0.0, 0.07));
        this.itsStandardGSFEParameterMap.put("chloroform", new SolventParameter(4.7113, 1.4459, 26.67, 0.15, 0.02));
        this.itsStandardGSFEParameterMap.put("chlorohexane", new SolventParameter(5.9491, 1.4199, 25.73, 0.0, 0.1));
        this.itsStandardGSFEParameterMap.put("cyclohexane", new SolventParameter(2.0165, 1.4266, 24.65, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("cyclohexanone", new SolventParameter(15.6186, 1.4507, 34.57, 0.0, 0.56));
        this.itsStandardGSFEParameterMap.put("decalin", new SolventParameter(2.196, 1.4528, 31.595, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("dichloromethane", new SolventParameter(8.93, 1.4242, 27.2, 0.1, 0.05));
        this.itsStandardGSFEParameterMap.put("diethyl ether", new SolventParameter(4.24, 1.3526, 16.65, 0.0, 0.41));
        this.itsStandardGSFEParameterMap.put("diisopropyl ether", new SolventParameter(3.38, 1.3679, 17.27, 0.0, 0.41));
        this.itsStandardGSFEParameterMap.put("dimethylacetamide", new SolventParameter(37.7807, 1.438, 33.125, 0.0, 0.78));
        this.itsStandardGSFEParameterMap.put("dimethylformamide", new SolventParameter(37.219, 1.4305, 36.17, 0.0, 0.74));
        this.itsStandardGSFEParameterMap.put("dimethylsulfoxide", new SolventParameter(46.8257, 1.4775, 42.92, 0.0, 0.88));
        this.itsStandardGSFEParameterMap.put("di-n-butyl ether", new SolventParameter(3.0473, 1.3992, 22.445, 0.0, 0.45));
        this.itsStandardGSFEParameterMap.put("diphenyl ether", new SolventParameter(3.73, 1.4, 24.35, 0.0, 0.2));
        this.itsStandardGSFEParameterMap.put("ethanol", new SolventParameter(24.852, 1.3611, 21.97, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("ethoxybenzene", new SolventParameter(4.1797, 1.5076, 32.41, 0.0, 0.32));
        this.itsStandardGSFEParameterMap.put("ethyl acetate", new SolventParameter(5.9867, 1.3723, 23.39, 0.0, 0.45));
        this.itsStandardGSFEParameterMap.put("ethylbenzene", new SolventParameter(2.4339, 1.4959, 28.75, 0.0, 0.15));
        this.itsStandardGSFEParameterMap.put("ethylene glycol", new SolventParameter(40.2455, 1.4318, 47.99, 0.58, 0.78));
        this.itsStandardGSFEParameterMap.put("fluorobenzene", new SolventParameter(5.42, 1.4684, 26.66, 0.0, 0.1));
        this.itsStandardGSFEParameterMap.put("iodobenzene", new SolventParameter(4.547, 1.62, 38.71, 0.0, 0.12));
        this.itsStandardGSFEParameterMap.put("isobutanol", new SolventParameter(16.7766, 1.3955, 22.5425, 0.37, 0.48));
        this.itsStandardGSFEParameterMap.put("isopropanol", new SolventParameter(19.2645, 1.3776, 20.93, 0.33, 0.56));
        this.itsStandardGSFEParameterMap.put("isopropylbenzene", new SolventParameter(2.3712, 1.4915, 27.685, 0.0, 0.16));
        this.itsStandardGSFEParameterMap.put("m-cresol", new SolventParameter(12.44, 1.5438, 35.69, 0.57, 0.34));
        this.itsStandardGSFEParameterMap.put("mesitylene", new SolventParameter(2.265, 1.4994, 27.55, 0.0, 0.19));
        this.itsStandardGSFEParameterMap.put("methanol", new SolventParameter(32.61316865, 1.3288, 22.07, 0.43, 0.47));
        this.itsStandardGSFEParameterMap.put("methyl acetate", new SolventParameter(6.8615, 1.3614, 24.73, 0.0, 0.45));
        this.itsStandardGSFEParameterMap.put("methyl phenyl ketone", new SolventParameter(17.44, 1.5372, 39.04, 0.0, 0.48));
        this.itsStandardGSFEParameterMap.put("Methyl tert-butyl ether", new SolventParameter(4.5, 1.369, 17.75, 0.0, 0.55));
        this.itsStandardGSFEParameterMap.put("methylformamide", new SolventParameter(181.5619, 1.4319, 38.7, 0.4, 0.55));
        this.itsStandardGSFEParameterMap.put("m-xylene", new SolventParameter(2.3478, 1.4972, 29.76, 0.0, 0.16));
        this.itsStandardGSFEParameterMap.put("n-butyl acetate", new SolventParameter(4.9941, 1.3941, 24.88, 0.0, 0.45));
        this.itsStandardGSFEParameterMap.put("n-butylbenzene", new SolventParameter(2.36, 1.4898, 28.7175, 0.0, 0.15));
        this.itsStandardGSFEParameterMap.put("n-decane", new SolventParameter(1.9846, 1.4102, 22.445, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("n-dodecane", new SolventParameter(2.006, 1.4216, 24.91, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("n-heptane", new SolventParameter(1.9113, 1.3878, 19.65, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("n-hexadecane", new SolventParameter(2.0402, 1.4345, 27.05, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("n-hexane", new SolventParameter(1.8819, 1.3749, 17.89, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("nitrobenzene", new SolventParameter(34.8091, 1.5562, 45.66, 0.0, 0.28));
        this.itsStandardGSFEParameterMap.put("nitroethane", new SolventParameter(28.2896, 1.3917, 32.13, 0.02, 0.33));
        this.itsStandardGSFEParameterMap.put("nitromethane", new SolventParameter(36.5623, 1.3817, 36.53, 0.06, 0.31));
        this.itsStandardGSFEParameterMap.put("N-Methyl-2-pyrrolidone", new SolventParameter(32.2, 1.47, 40.7, 0.0, 0.79));
        this.itsStandardGSFEParameterMap.put("n-nonane", new SolventParameter(1.9605, 1.4054, 22.38, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("n-octane", new SolventParameter(1.9406, 1.3974, 21.14, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("n-pentadecane", new SolventParameter(2.0333, 1.4315, 26.6375, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("n-pentane", new SolventParameter(1.8371, 1.3575, 15.4475, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("n-undecane", new SolventParameter(1.991, 1.4398, 24.21, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("o-dichlorobenzene", new SolventParameter(9.9949, 1.5515, 35.8, 0.0, 0.04));
        this.itsStandardGSFEParameterMap.put("o-nitrotoluene", new SolventParameter(25.6692, 1.545, 41.17, 0.0, 0.27));
        this.itsStandardGSFEParameterMap.put("perfluorobenzene", new SolventParameter(2.029, 1.3777, 22.0, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("p-isopropyltoluene", new SolventParameter(2.2322, 1.4909, 26.600292, 0.0, 0.19));
        this.itsStandardGSFEParameterMap.put("Propylene Carbonate", new SolventParameter(64.4, 1.4189, 41.93, 0.0, 0.6));
        this.itsStandardGSFEParameterMap.put("pyridine", new SolventParameter(12.9776, 1.5095, 36.56, 0.0, 0.52));
        this.itsStandardGSFEParameterMap.put("sec-butanol", new SolventParameter(15.9436, 1.3978, 23.47, 0.33, 0.56));
        this.itsStandardGSFEParameterMap.put("sec-butylbenzene", new SolventParameter(2.3446, 1.4895, 28.0325, 0.0, 0.16));
        this.itsStandardGSFEParameterMap.put("tert-butanol", new SolventParameter(12.47, 1.3878, 19.96, 0.31, 0.6));
        this.itsStandardGSFEParameterMap.put("tert-butylbenzene", new SolventParameter(2.3447, 1.4927, 27.6375, 0.0, 0.16));
        this.itsStandardGSFEParameterMap.put("tetrachloroethene", new SolventParameter(2.268, 1.5053, 31.8, 0.0, 0.0));
        this.itsStandardGSFEParameterMap.put("tetrahydrofuran", new SolventParameter(7.4257, 1.405, 26.5, 0.0, 0.48));
        this.itsStandardGSFEParameterMap.put("tetrahydrothiophene-1,1-dioxide", new SolventParameter(43.9622, 1.4833, 35.5, 0.0, 0.88));
        this.itsStandardGSFEParameterMap.put("tetralin", new SolventParameter(2.771, 1.5413, 33.12367712, 0.0, 0.19));
        this.itsStandardGSFEParameterMap.put("toluene", new SolventParameter(2.3741, 1.4961, 27.93, 0.0, 0.14));
        this.itsStandardGSFEParameterMap.put("tributylphosphate", new SolventParameter(8.1781, 1.4224, 19.11419, 0.0, 1.21));
        this.itsStandardGSFEParameterMap.put("triethylamine", new SolventParameter(2.3832, 1.401, 20.22, 0.0, 0.79));
        this.itsStandardGSFEParameterMap.put("water (defulat)", new SolventParameter(78.3553, 1.33, 71.99, 0.82, 0.35));
        this.itsStandardGSFEParameterMap.put("[MBIm]+[BF4]-", new SolventParameter(12.9, 1.4211, 44.7, 0.263, 0.32));
        this.itsStandardGSFEParameterMap.put("[MBIm]+[Tf2N]-", new SolventParameter(9.4, 1.427, 37.0, 0.259, 0.238));
        this.itsStandardGSFEParameterMap.put("[MBIm]+[PF6]-", new SolventParameter(14.0, 1.409, 43.78, 0.266, 0.216));
        this.itsStandardGSFEParameterMap.put("[MEIm]+[BF4]-", new SolventParameter(14.8, 1.4121, 50.1, 0.229, 0.265));
        this.itsStandardGSFEParameterMap.put("[MEIm]+[Tf2N]-", new SolventParameter(11.5, 1.4225, 39.0, 0.229, 0.265));
        this.itsStandardGSFEParameterMap.put("[MEIm]+[CF3SO3]-", new SolventParameter(15.8, 1.4332, 44.42, 0.229, 0.265));
        this.itsStandardGSFEParameterMap.put("[MHIm]+[Tf2N]-", new SolventParameter(7.0, 1.42958, 32.5, 0.229, 0.265));
        this.itsStandardGSFEParameterMap.put("[MHIm]+[PF6]-", new SolventParameter(11.1, 1.4169, 38.26, 0.229, 0.265));
        this.itsStandardGSFEParameterMap.put("[MOIm]+[PF6]-", new SolventParameter(9.7, 1.4235, 33.862, 0.229, 0.265));
        
        this.itsStandardGSFEParameterMap.put("(Use define)", new SolventParameter(0.0, 0.0, 0.0, 0.0, 0.0));
    }

    @Override
    public int size() {
        return this.itsStandardGSFEParameterMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itsStandardGSFEParameterMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.itsStandardGSFEParameterMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.itsStandardGSFEParameterMap.containsValue(value);
    }

    @Override
    public SolventParameter get(Object key) {
        return this.itsStandardGSFEParameterMap.get(key);
    }

    @Override
    public SolventParameter put(String key, SolventParameter value) {
        return this.itsStandardGSFEParameterMap.put(key, value);
    }

    @Override
    public SolventParameter remove(Object key) {
        return this.itsStandardGSFEParameterMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends SolventParameter> m) {
        this.itsStandardGSFEParameterMap.putAll(m);
    }

    @Override
    public void clear() {
        this.itsStandardGSFEParameterMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.itsStandardGSFEParameterMap.keySet();
    }

    @Override
    public Collection<SolventParameter> values() {
        return this.itsStandardGSFEParameterMap.values();
    }

    @Override
    public Set<Entry<String, SolventParameter>> entrySet() {
        return this.itsStandardGSFEParameterMap.entrySet();
    }
    
    public List<String> getStandardSolventNameList() {
        List<String> theNameList = new ArrayList<>();
        
        for(String theKey : this.keySet()) {
            theNameList.add(theKey);
        }
        
        Collections.sort(theNameList);
        
        return theNameList;
    }
}
