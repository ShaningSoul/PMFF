/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.energyfunction.abstracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 10. 06
 */
public abstract class AbstractCalculableList<Type extends AbstractCalculableSet> extends ArrayList<Type> implements Serializable {

    private static final long serialVersionUID = 8234567918907671186L;

    protected IAtomContainer itsMolecule;
    protected List<Integer> itsNotCalculateAtomNumberList;
    //constant Integer variable
    protected final int FIRST_INDEX = 0;
    protected final int SECOND_INDEX = 1;
    protected final int THIRD_INDEX = 2;

    public AbstractCalculableList() {
        super();
    }

    public AbstractCalculableList(IAtomContainer itsMolecule) {
        super();
        this.itsMolecule = itsMolecule;
    }

    public AbstractCalculableList(IAtomContainer itsMolecule, List<Integer> itsNotCalculateAtomNumberList) {
        this.itsMolecule = itsMolecule;
        this.itsNotCalculateAtomNumberList = itsNotCalculateAtomNumberList;
    }

    public AbstractCalculableList(AbstractCalculableList theCalculableList) {
        super(theCalculableList);
        
        try {
            this.itsMolecule = (IAtomContainer) theCalculableList.itsMolecule.clone();

            if (theCalculableList.itsNotCalculateAtomNumberList != null) {
                this.itsNotCalculateAtomNumberList = new ArrayList<>(theCalculableList.itsNotCalculateAtomNumberList);
            }
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule Clone Error!!");
        }
    }
    
    public void sortNonZeroGradient() {
        Comparator<AbstractCalculableSet> theComparator = new Comparator<AbstractCalculableSet>() {
            @Override
            public int compare(AbstractCalculableSet t, AbstractCalculableSet t1) {
                return -Double.compare(Math.abs(t.getGradient()), Math.abs(t1.getGradient()));
            }
        };
        
        Collections.sort(this, theComparator);
    }

    public void sortNonZeroConjugatedGradient() {
        Comparator<AbstractCalculableSet> theComparator = new Comparator<AbstractCalculableSet>() {
            @Override
            public int compare(AbstractCalculableSet t, AbstractCalculableSet t1) {
                return -Double.compare(Math.abs(t.getConjugatedGradient()), Math.abs(t1.getConjugatedGradient()));
            }
        };
        
        Collections.sort(this, theComparator);
    }

    public void makeConjugatedGradient(Double theScalingFactor) {
        for(AbstractCalculableSet theCalculableSet : this) {
            theCalculableSet.makeConjugatedGradient(theScalingFactor);
        }
        
        this.sortNonZeroConjugatedGradient();
    }
    
    public Type getCalculableSet(Integer ... theAtomIndexs) {
        for(Type theCalculableSet : this) {
            boolean theJugment = true;
            
            for(int theAtomIndex : theAtomIndexs) {
                if(!theCalculableSet.getAtomIndexList().contains(theAtomIndex)) {
                    theJugment = false;
                    break;
                }
            }
            
            if(theJugment) {
                return theCalculableSet;
            }
        }
        
        return null;
    }
}
