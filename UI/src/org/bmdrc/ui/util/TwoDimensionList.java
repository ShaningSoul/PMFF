/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2014, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.ui.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.bmdrc.ui.util.interfaces.ITwoDimensionList;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class TwoDimensionList<Type extends Object> implements ITwoDimensionList<Type>, Serializable {
    private static final long serialVersionUID = 7815229661653651844L;

    private List<List<Type>> its2dList;

    public TwoDimensionList() {
        this.set2dList(new ArrayList<List<Type>>());
    }

    public TwoDimensionList(TwoDimensionList<Type> the2dList) {
        this.set2dList(new ArrayList<List<Type>>());

        for (List<Type> theList : the2dList.get2dList()) {
            this.set2dList().add(new ArrayList<>(theList));
        }
    }

    public TwoDimensionList(List<List<Type>> its2dList) {
        this.its2dList = its2dList;
    }
    
    public TwoDimensionList(int theRowSize, int theColumnSize) {
        this.its2dList = new ArrayList<>();
        
        for(int ri = 0; ri < theRowSize; ri++) {
            this.its2dList.add(new ArrayList<Type>(theColumnSize));
        }
    }
    
    public TwoDimensionList(Type[][] the2dArray) {
        this.its2dList = new ArrayList<List<Type>>();
        
        for(int ri = 0, rEnd = the2dArray.length; ri < rEnd; ri++) {
            List<Type> theTypeList = new ArrayList<Type>();
            
            for(Type theValue : the2dArray[ri]) {
                theTypeList.add(theValue);
            }
            
            this.its2dList.add(theTypeList);
        }
    }

    @Override
    public List<List<Type>> get2dList() {
        return its2dList;
    }

    @Override
    public void set2dList(List<List<Type>> the2dList) {
        this.its2dList = the2dList;
    }

    @Override
    public List<List<Type>> set2dList() {
        return its2dList;
    }

    @Override
    public List<Type> get(int theIndex) {
        return this.get2dList().get(theIndex);
    }

    @Override
    public Type get(int theFirstIndex, int theSecondIndex) {
        return this.get2dList().get(theFirstIndex).get(theSecondIndex);
    }

    @Override
    public void remove(int theFirstIndex, int theSecondIndex) {
        this.set2dList().get(theFirstIndex).remove(theSecondIndex);
    }

    @Override
    public boolean contains(List<Type> theList) {
        return this.get2dList().contains(theList);
    }

    @Override
    public boolean contains(int theFirstIndex, Type theValue) {
        return this.get2dList().get(theFirstIndex).contains(theValue);
    }

    @Override
    public int size() {
        return this.get2dList().size();
    }

    @Override
    public void addAll(TwoDimensionList<Type> the2dList) {
        this.get2dList().addAll(the2dList.get2dList());
    }

    @Override
    public void addAll(List<List<Type>> the2dList) {
        this.get2dList().addAll(the2dList);
    }
    
    @Override
    public boolean isEmpty() {
        return this.get2dList().isEmpty();
    }

    @Override
    public void setValue(int theRowIndex, int theColumnIndex, Type theValue) {
        this.set2dList().get(theRowIndex).set(theColumnIndex, theValue);
    }

    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        for (List<Type> theList : this.get2dList()) {
            theStringBuilder.append(theList).append("\n");
        }

        return theStringBuilder.toString();
    }

    @Override
    public Iterator iterator() {
        return this.get2dList().iterator();
    }

    @Override
    public int getMaximumNumberOfColumn() {
        int theMaximumSize = 0;

        for (List<Type> theList : this.get2dList()) {
            if (theMaximumSize < theList.size()) {
                theMaximumSize = theList.size();
            }
        }

        return theMaximumSize;
    }

    @Override
    public TwoDimensionList<Type> transposeMatrix() {
        TwoDimensionList<Type> theNew2dList = this.__initializeTransposeMatrix();

        for (int ci = 0, cEnd = this.getMaximumNumberOfColumn(); ci < cEnd; ci++) {
            for (int li = 0, lEnd = this.get2dList().size(); li < lEnd; li++) {
                if(ci < this.get2dList().get(li).size()) {
                    theNew2dList.get(ci).add(this.get(li, ci));
                }
            }
        }

        return theNew2dList;
    }

    private TwoDimensionList<Type> __initializeTransposeMatrix() {
        TwoDimensionList<Type> theNew2dList = new TwoDimensionList<Type>();

        for (int li = 0, lEnd = this.getMaximumNumberOfColumn(); li < lEnd; li++) {
            theNew2dList.add(new ArrayList<Type>());
        }

        return theNew2dList;
    }

    @Override
    public void clear() {
        this.its2dList.clear();
    }

    @Override
    public boolean contains(Object o) {
        return this.its2dList.contains(o);
    }

    
    @Override
    public Object[] toArray() {
        return this.its2dList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.its2dList.toArray(a);
    }

    @Override
    public boolean add(List<Type> e) {
        return this.its2dList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.its2dList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.its2dList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends List<Type>> c) {
        return this.its2dList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends List<Type>> c) {
        return this.its2dList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.its2dList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.its2dList.retainAll(c);
    }

    @Override
    public List<Type> set(int index, List<Type> element) {
        return this.its2dList.set(index, element);
    }

    @Override
    public void add(int index, List<Type> element) {
        this.its2dList.add(index, element);
    }

    @Override
    public List<Type> remove(int index) {
        return this.its2dList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.its2dList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.its2dList.lastIndexOf(o);
    }

    @Override
    public ListIterator<List<Type>> listIterator() {
        return this.its2dList.listIterator();
    }

    @Override
    public ListIterator<List<Type>> listIterator(int index) {
        return this.its2dList.listIterator(index);
    }

    @Override
    public TwoDimensionList<Type> subList(int fromIndex, int toIndex) {
        return (TwoDimensionList<Type>)this.its2dList.subList(fromIndex, toIndex);
    }
    
    public Type[][] to2dArray(Type[][] theTypes) {
        for(int ri = 0, rEnd = this.size(); ri < rEnd; ri++) {
            for(int ci = 0, cEnd = this.get(ri).size(); ci < cEnd; ci++) {
                theTypes[ri][ci] = this.get(ri, ci);
            }
        }
        
        return theTypes;
    }
}