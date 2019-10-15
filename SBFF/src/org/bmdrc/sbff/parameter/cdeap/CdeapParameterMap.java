/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.sbff.parameter.cdeap;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CdeapParameterMap implements Serializable, Map<String, CdeapParameter> {
    
    private static final long serialVersionUID = -7298365200767090261L;

    private Map<String, CdeapParameter> itsCdeapParameterMap;

    public CdeapParameterMap() {
        this.__initializeCdeapParameterMap();
    }

    public Map<String, CdeapParameter> getCdeapParameterMap() {
        return itsCdeapParameterMap;
    }

    public void setCdeapParameterMap(Map<String, CdeapParameter> theCdeapParameterMap) {
        this.itsCdeapParameterMap = theCdeapParameterMap;
    }

    
    private void __initializeCdeapParameterMap() {
        this.itsCdeapParameterMap = new HashMap<>();
        
        this.__initializeCdeapParameterMapInCarbon();
        this.__initializeCdeapParameterMapInHydrogen();
        this.__initializeCdeapParameterMapInOxygen();
        this.__initializeCdeapParameterMapInNitrogen();
        this.__initializeCdeapParameterMapInSulfur();
        this.__initializeCdeapParameterMapInHalogen();
        this.__initializeCdeapParameterMapInPhosphorus();
    }
    
    private void __initializeCdeapParameterMapInCarbon() {
        this.itsCdeapParameterMap.put("C0", new CdeapParameter(1.49, 1.1));
        this.itsCdeapParameterMap.put("C1", new CdeapParameter(1.516, 0.568));
        this.itsCdeapParameterMap.put("C2", new CdeapParameter(1.45, 0.763));
        this.itsCdeapParameterMap.put("C3", new CdeapParameter(1.253, 0.862));
        this.itsCdeapParameterMap.put("C4", new CdeapParameter(1.031, 0.59));
    }

    private void __initializeCdeapParameterMapInHydrogen() {
        this.itsCdeapParameterMap.put("H1", new CdeapParameter(0.396, 0.219));
        this.itsCdeapParameterMap.put("H2", new CdeapParameter(0.298, 0.404));
    }
    
    private void __initializeCdeapParameterMapInOxygen() {
        this.itsCdeapParameterMap.put("O1", new CdeapParameter(0.72, 0.347));
        this.itsCdeapParameterMap.put("O2", new CdeapParameter(0.623, 0.281));
    }
    
    private void __initializeCdeapParameterMapInNitrogen() {
        this.itsCdeapParameterMap.put("N0", new CdeapParameter(0.980, 0.310));
        this.itsCdeapParameterMap.put("N1", new CdeapParameter(0.871, 0.424));
        this.itsCdeapParameterMap.put("N2", new CdeapParameter(0.656, 0.436));
        this.itsCdeapParameterMap.put("N3", new CdeapParameter(0.821, 0.422));
        this.itsCdeapParameterMap.put("N4", new CdeapParameter(0.966, 0.437));
    }
    
    private void __initializeCdeapParameterMapInSulfur() {
        this.itsCdeapParameterMap.put("S1", new CdeapParameter(2.688, 1.319));
        this.itsCdeapParameterMap.put("S6", new CdeapParameter(5.152, -1.73));
    }
    
    private void __initializeCdeapParameterMapInHalogen() {
        this.itsCdeapParameterMap.put("F1", new CdeapParameter(0.226, 0.144));
        this.itsCdeapParameterMap.put("Cl1", new CdeapParameter(2.18, 1.089));
        this.itsCdeapParameterMap.put("Br1", new CdeapParameter(3.114, 1.402));
        this.itsCdeapParameterMap.put("I1", new CdeapParameter(5.166, 2.573));
    }
    
    private void __initializeCdeapParameterMapInPhosphorus() {
        this.itsCdeapParameterMap.put("P5", new CdeapParameter(11.101, -7.006));
    }
    
    public CdeapParameter getParameter(String theCdeapAtomType) {
        return this.itsCdeapParameterMap.get(theCdeapAtomType);
    }
    
    @Override
    public int size() {
        return this.itsCdeapParameterMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itsCdeapParameterMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.itsCdeapParameterMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.itsCdeapParameterMap.containsValue(value);
    }

    @Override
    public CdeapParameter get(Object key) {
        return this.itsCdeapParameterMap.get(key);
    }

    @Override
    public CdeapParameter put(String key, CdeapParameter value) {
        return this.itsCdeapParameterMap.put(key, value);
    }

    @Override
    public CdeapParameter remove(Object key) {
        return this.itsCdeapParameterMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends CdeapParameter> m) {
        this.itsCdeapParameterMap.putAll(m);
    }

    @Override
    public void clear() {
        this.itsCdeapParameterMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.itsCdeapParameterMap.keySet();
    }

    @Override
    public Collection<CdeapParameter> values() {
        return this.itsCdeapParameterMap.values();
    }

    @Override
    public Set<Entry<String, CdeapParameter>> entrySet() {
        return this.itsCdeapParameterMap.entrySet();
    }
    
}
