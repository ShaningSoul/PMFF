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
public class HydrogenBondDonorParameterList implements Serializable, List<Double>{
    private static final long serialVersionUID = 3517296042306543160L;

    private List<Double> itsHydrogenBondDonorParameterList;

    public HydrogenBondDonorParameterList() {
        this.__initializeHydorgenBondDonorParameterList();
    }

    private void __initializeHydorgenBondDonorParameterList() {
        this.itsHydrogenBondDonorParameterList = new ArrayList<>();
        
        this.itsHydrogenBondDonorParameterList.add(0.0);
        this.itsHydrogenBondDonorParameterList.add(0.120);
        this.itsHydrogenBondDonorParameterList.add(0.075);
        this.itsHydrogenBondDonorParameterList.add(0.094);
        this.itsHydrogenBondDonorParameterList.add(0.080);
        this.itsHydrogenBondDonorParameterList.add(0.271);
        this.itsHydrogenBondDonorParameterList.add(0.363);
        this.itsHydrogenBondDonorParameterList.add(0.610);
        this.itsHydrogenBondDonorParameterList.add(0.486);
        this.itsHydrogenBondDonorParameterList.add(0.330);
        this.itsHydrogenBondDonorParameterList.add(0.310);
        this.itsHydrogenBondDonorParameterList.add(0.400);
        this.itsHydrogenBondDonorParameterList.add(0.760);
        this.itsHydrogenBondDonorParameterList.add(0.257);
        this.itsHydrogenBondDonorParameterList.add(0.402);
        this.itsHydrogenBondDonorParameterList.add(0.137);
        this.itsHydrogenBondDonorParameterList.add(0.319);
        this.itsHydrogenBondDonorParameterList.add(0.330);
        this.itsHydrogenBondDonorParameterList.add(0.220);
        this.itsHydrogenBondDonorParameterList.add(0.567);
    }
    
    @Override
    public int size() {
        return this.itsHydrogenBondDonorParameterList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itsHydrogenBondDonorParameterList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.itsHydrogenBondDonorParameterList.contains(o);
    }

    @Override
    public Iterator<Double> iterator() {
        return this.itsHydrogenBondDonorParameterList.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.itsHydrogenBondDonorParameterList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.itsHydrogenBondDonorParameterList.toArray(a);
    }

    @Override
    public boolean add(Double e) {
        return this.itsHydrogenBondDonorParameterList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.itsHydrogenBondDonorParameterList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.itsHydrogenBondDonorParameterList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Double> c) {
        return this.itsHydrogenBondDonorParameterList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Double> c) {
        return this.itsHydrogenBondDonorParameterList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.itsHydrogenBondDonorParameterList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.itsHydrogenBondDonorParameterList.retainAll(c);
    }

    @Override
    public void clear() {
        this.itsHydrogenBondDonorParameterList.clear();
    }

    @Override
    public Double get(int index) {
        return this.itsHydrogenBondDonorParameterList.get(index);
    }

    @Override
    public Double set(int index, Double element) {
        return this.itsHydrogenBondDonorParameterList.set(index, element);
    }

    @Override
    public void add(int index, Double element) {
        this.itsHydrogenBondDonorParameterList.add(index, element);
    }

    @Override
    public Double remove(int index) {
        return this.itsHydrogenBondDonorParameterList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.itsHydrogenBondDonorParameterList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.itsHydrogenBondDonorParameterList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Double> listIterator() {
        return this.itsHydrogenBondDonorParameterList.listIterator();
    }

    @Override
    public ListIterator<Double> listIterator(int index) {
        return this.itsHydrogenBondDonorParameterList.listIterator(index);
    }

    @Override
    public List<Double> subList(int fromIndex, int toIndex) {
        return this.itsHydrogenBondDonorParameterList.subList(fromIndex, toIndex);
    }
}
