/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.energyfunction.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bmdrc.basicchemistry.math.FirstDerivativeOfInternalCoordinate;
import org.bmdrc.basicchemistry.tool.AngleCalculator;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public abstract class AbstractDerivativeFunctionOfInternalCoordinate {

    protected IAtomContainer itsMolecule;
    //constant Integer variable
    public static final int END_ATOM_INDEX_IN_DISTANCE = 0;
    public static final int START_ATOM_INDEX_IN_DISTANCE = 1;
    public static final int FIRST_ATOM_IN_BEND_ANGLE = 0;
    public static final int CENTER_ATOM_IN_BEND_ANGLE = 1;
    public static final int SECOND_ATOM_IN_BEND_ANGLE = 2;
    public static final int ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE = 0;
    public static final int FIRST_ATOM_IN_TORSION_ANGLE = 1;
    public static final int SECOND_ATOM_IN_TORSION_ANGLE = 2;
    public static final int ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE = 3;
    protected final int VECTOR_FROM_FIRST_TO_CONNECTED_FIRST_IN_TORSION = 0;
    protected final int VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION = 1;
    protected final int VECTOR_FROM_SECOND_TO_CONNECTED_SECOND_IN_TORSION = 2;
    protected final int DISTANCE_ATOM_LIST_SIZE = 2;
    protected final int BEND_ANGLE_ATOM_LIST_SIZE = 3;
    protected final int VECTOR_CONTAIN_DERIVATIVE_ATOM = 0;
    protected final int VECTOR_NOT_CONTAIN_DERIVATIVE_ATOM = 1;
    protected final int CROSS_PRODUCT_BETWEEN_FIRST_AND_SECOND = 0;
    protected final int CROSS_PRODUCT_BETWEEN_INVERSE_SECOND_AND_THIRD = 1;
    protected final int DIMENSION_SIZE = 3;
    //constant Double variable
    protected final double MINIMUM_VALUE = 1.0e-10;
    protected final double ZERO_VALUE = 0.0;

    public AbstractDerivativeFunctionOfInternalCoordinate(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
    }
    
    public AbstractDerivativeFunctionOfInternalCoordinate(AbstractDerivativeFunctionOfInternalCoordinate theAbstractDerivativeFunction) {
        try {
            this.itsMolecule = (IAtomContainer)theAbstractDerivativeFunction.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println("Molecule Clone Error in AbstractDerivativeFunction");
        }
    }

    public IAtomContainer getMolecule() {
        return itsMolecule;
    }

    public void setMolecule(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
    }
    
    protected List<Vector3d> _getTorsionVectorList(List<Integer> theAtomIndexList) {
        List<Vector3d> theVectorList = new ArrayList<>();
        
        theVectorList.add(new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE)),
                this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE))));
        theVectorList.add(new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE)),
                this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE))));
        theVectorList.add(new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE)),
                this.itsMolecule.getAtom(theAtomIndexList.get(FirstDerivativeOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE))));
        
        return theVectorList;
    }
    
    protected List<Vector3d> _getCrossProductVectorListInTorsion(List<Integer> theAtomIndexList) {
        List<Vector3d> theVectorList = this._getTorsionVectorList(theAtomIndexList);
        List<Vector3d> theCrossProductVectorList = new ArrayList<>();
        
        theCrossProductVectorList.add(Vector3dCalculator.crossProduct(theVectorList.get(this.VECTOR_FROM_FIRST_TO_CONNECTED_FIRST_IN_TORSION), 
                theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION)));
        theCrossProductVectorList.add(Vector3dCalculator.crossProduct(Vector3dCalculator.productByScalar(-1.0, theVectorList.get(this.VECTOR_FROM_FIRST_TO_SECOND_IN_TORSION)), 
                theVectorList.get(this.VECTOR_FROM_SECOND_TO_CONNECTED_SECOND_IN_TORSION)));
        
        return theCrossProductVectorList;
    }
    
    protected List<Vector3d> _getPositionVectorList(List<Integer> theAtomIndexList) {
        List<Vector3d> thePositionVectorList = new ArrayList<>();
        
        for(Integer theAtomIndex : theAtomIndexList) {
            thePositionVectorList.add(new Vector3d(this.itsMolecule.getAtom(theAtomIndex)));
        }
        
        return thePositionVectorList;
    }
    
    protected List<Vector3d> _getBendAngleVectorList(List<Integer> theAtomIndexList, int theDerivativeAtomIndex) {
        List<Vector3d> theVector3dList = new ArrayList<>();

        theVector3dList.add(new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(this.CENTER_ATOM_IN_BEND_ANGLE)),
                this.itsMolecule.getAtom(theAtomIndexList.get(this.FIRST_ATOM_IN_BEND_ANGLE))));
        theVector3dList.add(new Vector3d(this.itsMolecule.getAtom(theAtomIndexList.get(this.CENTER_ATOM_IN_BEND_ANGLE)),
                this.itsMolecule.getAtom(theAtomIndexList.get(this.SECOND_ATOM_IN_BEND_ANGLE))));

        if (theDerivativeAtomIndex == this.SECOND_ATOM_IN_BEND_ANGLE) {
            Collections.reverse(theVector3dList);
        }
        
        return theVector3dList;
    }
    
    protected double _getTorsionAngle(List<Integer> theAtomIndexList) {
        return AngleCalculator.calculateTorsionAngle(this.itsMolecule.getAtom(theAtomIndexList.get(AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_FIRST_ATOM_IN_TORSION_ANGLE)),
                this.itsMolecule.getAtom(theAtomIndexList.get(AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_TORSION_ANGLE)),
                this.itsMolecule.getAtom(theAtomIndexList.get(AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_TORSION_ANGLE)),
                this.itsMolecule.getAtom(theAtomIndexList.get(AbstractDerivativeFunctionOfInternalCoordinate.ATOM_CONNECTED_SECOND_ATOM_IN_TORSION_ANGLE)));
    }
    
    protected double _getBendAngle(List<Integer> theAtomIndexList) {
        return AngleCalculator.calculateBondAngle(this.itsMolecule.getAtom(theAtomIndexList.get(AbstractDerivativeFunctionOfInternalCoordinate.FIRST_ATOM_IN_BEND_ANGLE)), 
                this.itsMolecule.getAtom(theAtomIndexList.get(AbstractDerivativeFunctionOfInternalCoordinate.CENTER_ATOM_IN_BEND_ANGLE)), 
                this.itsMolecule.getAtom(theAtomIndexList.get(AbstractDerivativeFunctionOfInternalCoordinate.SECOND_ATOM_IN_BEND_ANGLE)));
    }
}
