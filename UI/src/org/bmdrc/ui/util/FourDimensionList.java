/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */

package org.bmdrc.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since  2015. 09. 25
 */
public class FourDimensionList<Type extends Object> implements List<ThreeDimensionList<Type>> {
    private static final long serialVersionUID = 1L;
    
    private List<ThreeDimensionList<Type>> itsFourDimensionList;
    
    public FourDimensionList() {
        this.itsFourDimensionList = new ArrayList<>();
    }

    public FourDimensionList(FourDimensionList<Type> the4dList) {
        this.itsFourDimensionList = new ArrayList<>(the4dList);
    }

    @Override
    public int size() {
        return this.itsFourDimensionList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itsFourDimensionList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.contains(o);
    }

    @Override
    public Iterator<ThreeDimensionList<Type>> iterator() {
        return this.itsFourDimensionList.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.itsFourDimensionList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.itsFourDimensionList.toArray(a);
    }

    @Override
    public boolean add(ThreeDimensionList<Type> e) {
        return this.itsFourDimensionList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.itsFourDimensionList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.itsFourDimensionList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends ThreeDimensionList<Type>> c) {
        return this.itsFourDimensionList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends ThreeDimensionList<Type>> c) {
        return this.itsFourDimensionList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.itsFourDimensionList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.itsFourDimensionList.retainAll(c);
    }

    @Override
    public void clear() {
        this.itsFourDimensionList.clear();
    }

    @Override
    public ThreeDimensionList<Type> get(int index) {
        return this.itsFourDimensionList.get(index);
    }

    @Override
    public ThreeDimensionList<Type> set(int index, ThreeDimensionList<Type> element) {
        return this.itsFourDimensionList.set(index, element);
    }

    @Override
    public void add(int index, ThreeDimensionList<Type> element) {
        this.itsFourDimensionList.add(index, element);
    }

    @Override
    public ThreeDimensionList<Type> remove(int index) {
        return this.itsFourDimensionList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.itsFourDimensionList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.itsFourDimensionList.lastIndexOf(o);
    }

    @Override
    public ListIterator<ThreeDimensionList<Type>> listIterator() {
        return this.itsFourDimensionList.listIterator();
    }

    @Override
    public ListIterator<ThreeDimensionList<Type>> listIterator(int index) {
        return this.itsFourDimensionList.listIterator(index);
    }

    @Override
    public List<ThreeDimensionList<Type>> subList(int fromIndex, int toIndex) {
        return this.itsFourDimensionList.subList(fromIndex, toIndex);
    }
    
    public TwoDimensionList<Type> get(int theFirstIndex, int theSecondIndex) {
        return this.itsFourDimensionList.get(theFirstIndex).get(theSecondIndex);
    }
    
    public List<Type> get(int theFirstIndex, int theSecondIndex, int theThirdIndex) {
        return this.get(theFirstIndex, theSecondIndex).get(theThirdIndex);
    }
    
    public Type get(int theFirstIndex, int theSecondIndex, int theThirdIndex, int theFourthIndex) {
        return this.get(theFirstIndex, theSecondIndex, theThirdIndex).get(theFourthIndex);
    }
    
    public void setValue(int theFirstIndex, int theSecondIndex, TwoDimensionList<Type> theValue) {
        this.itsFourDimensionList.get(theFirstIndex).set(theSecondIndex, theValue);
    }
    
    public void setValue(int theFirstIndex, int theSecondIndex, int theThirdIndex, List<Type> theValue) {
        this.get(theFirstIndex, theSecondIndex).set(theThirdIndex, theValue);
    }
    
    public void setValue(int theFirstindex, int theSecondIndex, int theThirdIndex, int theFourthIndex, Type theValue) {
        this.get(theFirstindex, theSecondIndex, theThirdIndex).set(theFourthIndex, theValue);
    }
}
