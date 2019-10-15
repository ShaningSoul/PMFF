/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.ui.vector.Vector;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class GradientVectorList implements List<Vector>, Serializable {
    private static final long serialVersionUID = 5227556497183589739L;

    private IAtomContainer itsMolecule;
    private MoveTogetherAtomNumberList itsMoveTogetherAtomNumberMap;
    private List<Vector> itsGradiendtVectorList;
    //constant Double variable
    private final double ZERO_VALUE = 0.0;
    private final double INITIAL_DERIVATIVE = 1.0;
    //constant Integer variable
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;

    public GradientVectorList() {
        this.itsGradiendtVectorList = new ArrayList<>();
    }

    public GradientVectorList(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
        this.itsMoveTogetherAtomNumberMap = new MoveTogetherAtomNumberList(theMolecule);
        this.generateGradientVectorList();
    }

    public void generateGradientVectorList() {
        this.itsGradiendtVectorList = new ArrayList<>();

        this.__setDistanceGradientVectorList();
        this.__setBendAngleGradientVectorList();
        this.__setTorsionAngleGradientVectorList();
    }

    private void __setDistanceGradientVectorList() {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            for (IAtom theConnectedAtom : this.itsMolecule.getConnectedAtomsList(theAtom)) {
                Vector3d theGradientVector = new Vector3d(theAtom, theConnectedAtom);

                theGradientVector.divideScalar(theGradientVector.length());

                this.itsGradiendtVectorList.add(this.__getGradientVector(theAtom, theConnectedAtom, theGradientVector));
            }
        }
    }

    private Vector __getGradientVector(IAtom theTemplateAtom, IAtom theConnectedAtom, Vector3d theGradientVector) {
        Vector theVector = new Vector();
        List<Integer> theMoveTogetherAtomNumberList = this.itsMoveTogetherAtomNumberMap.get(this.itsMolecule.getAtomNumber(theTemplateAtom),
                this.itsMolecule.getAtomNumber(theConnectedAtom));

        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            if (theMoveTogetherAtomNumberList.contains(ai)) {
                theVector.add(theGradientVector.getX());
                theVector.add(theGradientVector.getY());
                theVector.add(theGradientVector.getZ());
            } else {
                theVector.add(this.ZERO_VALUE);
                theVector.add(this.ZERO_VALUE);
                theVector.add(this.ZERO_VALUE);
            }
        }

        return theVector;
    }

    private void __setBendAngleGradientVectorList() {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            this.__setBendAngleGradientVectorList(theAtom);
        }
    }

    private void __setBendAngleGradientVectorList(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.itsMolecule.getConnectedAtomsList(theAtom);

        for (int fi = 0, fEnd = theConnectedAtomList.size() - 1; fi < fEnd; fi++) {
            int theTemplateAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(fi));

            for (int si = fi + 1, sEnd = theConnectedAtomList.size(); si < sEnd; si++) {
                int theCounterAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomList.get(si));
                Vector3d theAngleGradientVectorInTemplate = this.__getAngleGradientVector(theConnectedAtomList.get(fi), theAtom, theConnectedAtomList.get(si));
                Vector3d theAngleGradientVectorInCounter = this.__getAngleGradientVector(theConnectedAtomList.get(si), theAtom, theConnectedAtomList.get(fi));

                if (theAngleGradientVectorInTemplate != null) {
                    this.itsGradiendtVectorList.add(this.__getGradientVector(theConnectedAtomList.get(fi), theAtom, theAngleGradientVectorInTemplate));
                }

                if (theAngleGradientVectorInCounter != null) {
                    this.itsGradiendtVectorList.add(this.__getGradientVector(theConnectedAtomList.get(si), theAtom, theAngleGradientVectorInCounter));
                }
            }
        }
    }

    private Vector3d __getAngleGradientVector(IAtom theTemplateAtom, IAtom theCenterAtom, IAtom theCounterAtom) {
        Vector3d theCentripetalVector = new Vector3d(theTemplateAtom, theCenterAtom);
        Vector3d thePerpendicularVector = Vector3dCalculator.crossProduct(theCentripetalVector, new Vector3d(theCenterAtom, theCounterAtom));
        Vector3d theAccelerationVector = Vector3dCalculator.crossProduct(thePerpendicularVector, theCentripetalVector);

        if (theAccelerationVector.length() > 0.0) {
            return theAccelerationVector.divideScalar(theAccelerationVector.length());
        } else {
            return null;
        }
    }

    private void __setTorsionAngleGradientVectorList() {
        for (IBond theBond : this.itsMolecule.bonds()) {
            if (this.__containTorsionAngleGradient(theBond)) {
                this.__setTorsionAngleGradientVectorList(theBond);
            }
        }
    }

    private void __setTorsionAngleGradientVectorList(IBond theBond) {
        IAtom theFirstAtom = theBond.getAtom(this.FIRST_INDEX);
        IAtom theSecondAtom = theBond.getAtom(this.SECOND_INDEX);
        List<IAtom> theConnectedAtomListInFirstAtom = this.itsMolecule.getConnectedAtomsList(theFirstAtom);
        List<IAtom> theConnectedAtomListInSecondAtom = this.itsMolecule.getConnectedAtomsList(theSecondAtom);

        for (IAtom theConnectedAtomInFirstAtom : theConnectedAtomListInFirstAtom) {
            int theFirstConnectedAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomInFirstAtom);

            for (IAtom theConnectedAtomInSecondAtom : theConnectedAtomListInSecondAtom) {
                Vector3d theFirstAngleGradientVector = this.__getTorsionAngleGradientVector(theConnectedAtomInFirstAtom, theFirstAtom, theSecondAtom);
                Vector3d theSecondAngleGradientVector = this.__getTorsionAngleGradientVector(theConnectedAtomInSecondAtom, theSecondAtom, theFirstAtom);
                int theSecondConnectedAtomNumber = this.itsMolecule.getAtomNumber(theConnectedAtomInSecondAtom);

                if (theFirstAngleGradientVector != null) {
                    this.itsGradiendtVectorList.add(this.__getGradientVector(theConnectedAtomInFirstAtom, theFirstAtom, theFirstAngleGradientVector));
                }

                if (theSecondAngleGradientVector != null) {
                    this.itsGradiendtVectorList.add(this.__getGradientVector(theConnectedAtomInSecondAtom, theSecondAtom, theSecondAngleGradientVector));
                }
            }
        }
    }

    private Vector3d __getTorsionAngleGradientVector(IAtom theTemplateAtom, IAtom theFirstCenterAtom,
            IAtom theSecondCenterAtom) {
        Vector3d theDistanceVector = new Vector3d(theTemplateAtom, theFirstCenterAtom);
        Vector3d theCenterVector = new Vector3d(theFirstCenterAtom, theSecondCenterAtom);
        Vector3d theAccelerationVector = Vector3dCalculator.crossProduct(theDistanceVector, theCenterVector);

        if (theAccelerationVector.length() > 0.0) {
            return theAccelerationVector.divideScalar(theAccelerationVector.length());
        } else {
            return null;
        }
    }

    private boolean __containTorsionAngleGradient(IBond theBond) {
        IAtom theFirstAtom = theBond.getAtom(this.FIRST_INDEX);
        IAtom theSecondAtom = theBond.getAtom(this.SECOND_INDEX);
        List<IAtom> theConnectedAtomListInFirstAtom = this.itsMolecule.getConnectedAtomsList(theFirstAtom);
        List<IAtom> theConnectedAtomListInSecondAtom = this.itsMolecule.getConnectedAtomsList(theSecondAtom);

        return theConnectedAtomListInFirstAtom.size() > 1 && theConnectedAtomListInSecondAtom.size() > 1;
    }

    @Override
    public int size() {
        return this.itsGradiendtVectorList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itsGradiendtVectorList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.itsGradiendtVectorList.contains(o);
    }

    @Override
    public Iterator<Vector> iterator() {
        return this.itsGradiendtVectorList.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.itsGradiendtVectorList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.itsGradiendtVectorList.toArray(a);
    }

    @Override
    public boolean add(Vector e) {
        return this.itsGradiendtVectorList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.itsGradiendtVectorList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.itsGradiendtVectorList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Vector> c) {
        return this.itsGradiendtVectorList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Vector> c) {
        return this.itsGradiendtVectorList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.itsGradiendtVectorList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.itsGradiendtVectorList.retainAll(c);
    }

    @Override
    public void clear() {
        this.itsGradiendtVectorList.clear();
    }

    @Override
    public Vector get(int index) {
        return this.itsGradiendtVectorList.get(index);
    }

    @Override
    public Vector set(int index, Vector element) {
        return this.itsGradiendtVectorList.set(index, element);
    }

    @Override
    public void add(int index, Vector element) {
        this.itsGradiendtVectorList.add(index, element);
    }

    @Override
    public Vector remove(int index) {
        return this.itsGradiendtVectorList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.itsGradiendtVectorList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.itsGradiendtVectorList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Vector> listIterator() {
        return this.itsGradiendtVectorList.listIterator();
    }

    @Override
    public ListIterator<Vector> listIterator(int index) {
        return this.itsGradiendtVectorList.listIterator(index);
    }

    @Override
    public List<Vector> subList(int fromIndex, int toIndex) {
        return this.itsGradiendtVectorList.subList(fromIndex, toIndex);
    }
}
