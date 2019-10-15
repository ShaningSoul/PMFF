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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class HydrogenBondAcceptorParameterList implements Serializable, List<Double> {
    private static final long serialVersionUID = -1088281910977017977L;

    private List<Double> itsHydrogenBondAcceptorParameterList;

    public HydrogenBondAcceptorParameterList() {
        this.__initializeHydrogenBondAcceptorParameterList();
    }
    
    private void __initializeHydrogenBondAcceptorParameterList() {
        this.itsHydrogenBondAcceptorParameterList = new ArrayList<>();
        
        this.add(0.0);
        this.add(0.039);
        this.add(0.061);
        this.add(0.062);
        this.add(0.001);
        this.add(0.056);
        this.add(0.043);
        this.add(0.140);
        this.add(0.050);
        this.add(0.020);
        this.add(0.450);
        this.add(0.490);
        this.add(0.524);
        this.add(0.380);
        this.add(0.450);
        this.add(0.343);
        this.add(0.560);
        this.add(0.610);
        this.add(0.690);
        this.add(0.767);
        this.add(0.172);
        this.add(0.687);
        this.add(0.682);
        this.add(0.847);
        this.add(0.373);
        this.add(0.350);
        this.add(0.480);
        this.add(0.200);
        this.add(0.289);
        this.add(0.054);
        this.add(0.004);
        this.add(0.013);
        this.add(0.001);
        this.add(0.001);
        this.add(0.600);
        this.add(0.550);
        this.add(0.740);
        this.add(0.330);
        this.add(0.156);
        this.add(0.406);
        this.add(0.560);
        this.add(0.310);
        this.add(0.676);
        this.add(0.485);
        this.add(0.098);
        this.add(0.450);
        this.add(0.413);
        this.add(0.448);
        this.add(0.159);
        this.add(0.676);
        this.add(0.545);
    }

    @Override
    public int size() {
        return this.itsHydrogenBondAcceptorParameterList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itsHydrogenBondAcceptorParameterList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.itsHydrogenBondAcceptorParameterList.contains(o);
    }

    @Override
    public Iterator<Double> iterator() {
        return this.itsHydrogenBondAcceptorParameterList.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.itsHydrogenBondAcceptorParameterList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.itsHydrogenBondAcceptorParameterList.toArray(a);
    }

    @Override
    public boolean add(Double e) {
        return this.itsHydrogenBondAcceptorParameterList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.itsHydrogenBondAcceptorParameterList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.itsHydrogenBondAcceptorParameterList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Double> c) {
        return this.itsHydrogenBondAcceptorParameterList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Double> c) {
        return this.itsHydrogenBondAcceptorParameterList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.itsHydrogenBondAcceptorParameterList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.itsHydrogenBondAcceptorParameterList.retainAll(c);
    }

    @Override
    public void clear() {
        this.itsHydrogenBondAcceptorParameterList.clear();
    }

    @Override
    public Double get(int index) {
        return this.itsHydrogenBondAcceptorParameterList.get(index);
    }

    @Override
    public Double set(int index, Double element) {
        return this.itsHydrogenBondAcceptorParameterList.set(index, element);
    }

    @Override
    public void add(int index, Double element) {
        this.itsHydrogenBondAcceptorParameterList.add(index, element);
    }

    @Override
    public Double remove(int index) {
        return this.itsHydrogenBondAcceptorParameterList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.itsHydrogenBondAcceptorParameterList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.itsHydrogenBondAcceptorParameterList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Double> listIterator() {
        return this.itsHydrogenBondAcceptorParameterList.listIterator();
    }

    @Override
    public ListIterator<Double> listIterator(int index) {
        return this.itsHydrogenBondAcceptorParameterList.listIterator(index);
    }

    @Override
    public List<Double> subList(int fromIndex, int toIndex) {
        return this.itsHydrogenBondAcceptorParameterList.subList(fromIndex, toIndex);
    }
}
