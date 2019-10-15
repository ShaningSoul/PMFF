/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.sbff.parameter.intraenergy;

import java.util.ArrayList;
import java.util.List;
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 03
 */
public class MM3NonbondingParameterList extends ArrayList<MM3NonbondingParameter> {

    private Integer itsAtomType;
    //constant Integer variable
    private final int UNVALID_INDEX = -1;

    /**
     * Constructor
     */
    public MM3NonbondingParameterList() {
        super();
    }

    /**
     * Constructor defined by atom type
     *
     * @param theAtomType Atom type defined by Schrodinger
     */
    public MM3NonbondingParameterList(Integer theAtomType) {
        super();
        this.itsAtomType = theAtomType;
    }

    /**
     * Constructor used in deep copy
     *
     * @param theParameterList Template parameter list
     */
    public MM3NonbondingParameterList(MM3NonbondingParameterList theParameterList) {
        super(theParameterList);
        this.itsAtomType = theParameterList.itsAtomType;
    }

    /**
     * get atom type
     *
     * @return double value in atom type defined in Schrodinger
     */
    public Integer getAtomType() {
        return itsAtomType;
    }

    /**
     * set atom type
     *
     * @param theAtomType Atom type defined in Schrodinger
     */
    public void setAtomType(Integer theAtomType) {
        this.itsAtomType = theAtomType;
    }

    /**
     * get intra-nonbonding parameter<br>
     * The parameter must contain parameter having empty connected atom type
     * list<br>
     * When the parameter having empty connected atom type list does not existed
     * and the atom does not matched parameter, this function return null
     *
     * @param theMolecule IAtomContainer variable
     * @param theAtom atom getting intra-nonbonding parameter
     * @return IntraNonbondingParameter variable or null
     */
    public MM3NonbondingParameter getParameter(IAtomContainer theMolecule, IAtom theAtom) {
        List<IAtom> theConnectedAtomList = theMolecule.getConnectedAtomsList(theAtom);

        for (MM3NonbondingParameter theParameter : this) {
            if (theParameter.getConnectedAtomTypeList().isEmpty() || this.__isConnectedAtomByParameter(theConnectedAtomList, theParameter.getConnectedAtomTypeList())) {
                return theParameter;
            }
        }

        return null;
    }

    private boolean __isConnectedAtomByParameter(List<IAtom> theConnectedAtomList, List<Integer> theConnectedAtomTypeList) {
        List<Integer> theAtomTypeListInConnectedAtom = new ArrayList<>();
        
        for(IAtom theAtom : theConnectedAtomList) {
            theAtomTypeListInConnectedAtom.add(Integer.parseInt(theAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY).toString()));
        }

        for(Integer theConnectedAtomType : theConnectedAtomTypeList) {
            int theIndex = theAtomTypeListInConnectedAtom.indexOf(theConnectedAtomType.intValue());
            
            if(theIndex == this.UNVALID_INDEX) {
                return false;
            } else {
                theAtomTypeListInConnectedAtom.remove(theIndex);
            }
        }
        
        return true;
    }
}
