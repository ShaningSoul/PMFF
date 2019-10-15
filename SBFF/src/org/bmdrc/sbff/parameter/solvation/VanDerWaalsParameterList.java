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
public class VanDerWaalsParameterList implements Serializable, List<Double> {

    private static final long serialVersionUID = -7292047755191388950L;

    private List<Double> itsVanDerWaalsParameterList;

    public VanDerWaalsParameterList() {
        this.__initializeVanDerWaalsParameterList();
    }
    
    private void __initializeVanDerWaalsParameterList() {
        this.itsVanDerWaalsParameterList = new ArrayList<>();
        
        this.itsVanDerWaalsParameterList.add(0.0);
        
        this.itsVanDerWaalsParameterList.add(1.181);//1
        this.itsVanDerWaalsParameterList.add(1.052);//2
        this.itsVanDerWaalsParameterList.add(1.038);//3
        this.itsVanDerWaalsParameterList.add(1.096);//4
        this.itsVanDerWaalsParameterList.add(1.08);//5
        this.itsVanDerWaalsParameterList.add(1.189);//6
        this.itsVanDerWaalsParameterList.add(1.24);//7
        this.itsVanDerWaalsParameterList.add(0.9);//8
        this.itsVanDerWaalsParameterList.add(0.95);//9
        
        this.itsVanDerWaalsParameterList.add(1.947);//11
        this.itsVanDerWaalsParameterList.add(1.886);//12
        this.itsVanDerWaalsParameterList.add(1.833);//13
        this.itsVanDerWaalsParameterList.add(1.855);//14
        this.itsVanDerWaalsParameterList.add(1.817);//15
        this.itsVanDerWaalsParameterList.add(1.734);//16
        this.itsVanDerWaalsParameterList.add(1.742);//17
        this.itsVanDerWaalsParameterList.add(1.801);//18
        
        this.itsVanDerWaalsParameterList.add(1.545);//21
        this.itsVanDerWaalsParameterList.add(1.518);//22
        this.itsVanDerWaalsParameterList.add(1.518);//23
        this.itsVanDerWaalsParameterList.add(1.518);//24
        this.itsVanDerWaalsParameterList.add(1.517);//25
        this.itsVanDerWaalsParameterList.add(1.429);//26
        
        this.itsVanDerWaalsParameterList.add(1.67);//31
        this.itsVanDerWaalsParameterList.add(1.92);//32
        this.itsVanDerWaalsParameterList.add(1.66);//33
        this.itsVanDerWaalsParameterList.add(1.704);//34
        this.itsVanDerWaalsParameterList.add(1.696);//35
        this.itsVanDerWaalsParameterList.add(1.68);//36
        this.itsVanDerWaalsParameterList.add(1.645);//37
        this.itsVanDerWaalsParameterList.add(1.495);//38
        this.itsVanDerWaalsParameterList.add(1.69);//39
        
        this.itsVanDerWaalsParameterList.add(3.749);//41
        this.itsVanDerWaalsParameterList.add(2.101);//42
        
        this.itsVanDerWaalsParameterList.add(1.47);//51
        this.itsVanDerWaalsParameterList.add(1.7);//52
        this.itsVanDerWaalsParameterList.add(1.89);//53
        this.itsVanDerWaalsParameterList.add(1.9);//54
    }

    @Override
    public int size() {
        return this.itsVanDerWaalsParameterList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itsVanDerWaalsParameterList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.itsVanDerWaalsParameterList.contains(o);
    }

    @Override
    public Iterator<Double> iterator() {
        return this.itsVanDerWaalsParameterList.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.itsVanDerWaalsParameterList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.itsVanDerWaalsParameterList.toArray(a);
    }

    @Override
    public boolean add(Double e) {
        return this.itsVanDerWaalsParameterList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.itsVanDerWaalsParameterList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Double> c) {
        return this.itsVanDerWaalsParameterList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Double> c) {
        return this.itsVanDerWaalsParameterList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.itsVanDerWaalsParameterList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.itsVanDerWaalsParameterList.retainAll(c);
    }

    @Override
    public void clear() {
        this.itsVanDerWaalsParameterList.clear();
    }

    @Override
    public Double get(int index) {
        return this.itsVanDerWaalsParameterList.get(index);
    }

    @Override
    public Double set(int index, Double element) {
        return this.itsVanDerWaalsParameterList.set(index, element);
    }

    @Override
    public void add(int index, Double element) {
        this.itsVanDerWaalsParameterList.add(index, element);
    }

    @Override
    public Double remove(int index) {
        return this.itsVanDerWaalsParameterList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.itsVanDerWaalsParameterList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.itsVanDerWaalsParameterList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Double> listIterator() {
        return this.itsVanDerWaalsParameterList.listIterator();
    }

    @Override
    public ListIterator<Double> listIterator(int index) {
        return this.itsVanDerWaalsParameterList.listIterator(index);
    }

    @Override
    public List<Double> subList(int fromIndex, int toIndex) {
        return this.itsVanDerWaalsParameterList.subList(fromIndex, toIndex);
    }

}
