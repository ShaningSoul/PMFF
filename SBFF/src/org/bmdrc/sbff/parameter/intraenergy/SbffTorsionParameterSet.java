/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.parameter.intraenergy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond.Order;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffTorsionParameterSet implements Serializable {

    private static final long serialVersionUID = -485754809671417004L;

    private List<TorsionParameterList> itsParameterSet;
    private List<TorsionParameterList> itsAnyBondParameterSet;
    private List<TorsionParameterList> itsExceptionParameterSet;
    private List<TorsionParameterList> itsAnyBondExceptionParameterSet;
    private List<TorsionParameterList> itsSpecialStrctureParameterSet;

    public SbffTorsionParameterSet() {
        this.__generateParameterSet();
        this.__generateAnyBondParameterSet();
        this.__generateExceptionParameterSet();
        this.__generateAnyBondExceptionParameterSet();
        this.__generateSpecialStructureParameterSet();
    }

    public TorsionParameter getParameter(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        TorsionParameter theParameter;

        if((theParameter = this.__getParameterInSpecialStructureParameterSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) != null) {
            return theParameter;
        } else if ((theParameter = this.__getParameterInNormalSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) != null) {
            return theParameter;
        } else if ((theParameter = this.__getParameterInAnyBondSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) != null) {
            return theParameter;
        } else if ((theParameter = this.__getParameterInExceptionSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) != null) {
            return theParameter;
        } else if ((theParameter = this.__getParameterInAnyBondExceptionSet(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) != null) {
            return theParameter;
        }

        return null;
    }

    private TorsionParameter __getParameterInSpecialStructureParameterSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        for (TorsionParameterList theParameterList : this.itsSpecialStrctureParameterSet) {
            if (theParameterList.equalAtomTypeAndSpecialStructureType(theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom);
            }
        }
        
        return null;
    }
    
    private TorsionParameter __getParameterInNormalSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        for (TorsionParameterList theParameterList : this.itsParameterSet) {
            if (theParameterList.equalAtomTypeListAndOrder(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom);
            }
        }

        return null;
    }

    private TorsionParameter __getParameterInAnyBondSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        for (TorsionParameterList theParameterList : this.itsAnyBondParameterSet) {
            if (theParameterList.equalAtomTypeAndAnyBond(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom);
            }
        }

        return null;
    }

    private TorsionParameter __getParameterInExceptionSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        for (TorsionParameterList theParameterList : this.itsExceptionParameterSet) {
            if (theParameterList.equalAtomTypeAndAnyBond(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom);
            }
        }

        return null;
    }

    private TorsionParameter __getParameterInAnyBondExceptionSet(IAtomContainer theMolecule, IAtom theFirstAtom, IAtom theSecondAtom, IAtom theThirdAtom, IAtom theFourthAtom) {
        for (TorsionParameterList theParameterList : this.itsAnyBondExceptionParameterSet) {
            if (theParameterList.equalAtomTypeAndAnyBond(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom)) {
                return theParameterList.getParameter(theMolecule, theFirstAtom, theSecondAtom, theThirdAtom, theFourthAtom);
            }
        }

        return null;
    }

    private void __generateParameterSet() {
        this.itsParameterSet = new ArrayList<>();

        this.itsParameterSet.add(this.__getH1C3C3H1ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3H1ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3C3ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3C2ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3C1ParameterList());
        this.itsParameterSet.add(this.__getC2C3C3H1ParameterList());
        this.itsParameterSet.add(this.__getH1C3C3C2ParameterList());
        this.itsParameterSet.add(this.__getC3C3C2C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3C2C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3C2H1ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2H1ParameterList());
        this.itsParameterSet.add(this.__getH1C1C1H1AtSecondTripleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3C2H1ParameterList());
        this.itsParameterSet.add(this.__getC3C3C2C2ParameterList());
        this.itsParameterSet.add(this.__getC3C3C2C1ParameterList());
        this.itsParameterSet.add(this.__getC3C3C1C1AtThirdTripleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3C2C2ParameterList());
        this.itsParameterSet.add(this.__getC3C2C2C3ParameterList());
        this.itsParameterSet.add(this.__getC3C2C2C3AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C2C2H1ParameterList());
        this.itsParameterSet.add(this.__getC3C2C2H1AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2C2H1AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C3C3C2ParameterList());
        this.itsParameterSet.add(this.__getC2C3C3C1ParameterList());
        this.itsParameterSet.add(this.__getC2C3C2C3ParameterList());
        this.itsParameterSet.add(this.__getC2C3C2H1ParameterList());
        this.itsParameterSet.add(this.__getC2C2C2H1AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C2C2C2AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C2C2C2ParameterList());
        this.itsParameterSet.add(this.__getC3C1C1C2AtSecondTripleBondParameterList());
        this.itsParameterSet.add(this.__getC3C1C1C1AtSecondTripleBondParameterList());
        this.itsParameterSet.add(this.__getC2C1C1C1AtSecondTripleBondParameterList());
        this.itsParameterSet.add(this.__getC2C1C1H1AtSecondTripleBondParameterList());
        this.itsParameterSet.add(this.__getC1C1C1C1AtSecondTripleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2C2C3ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2C2ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2C2AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3C2C1ParameterList());
        this.itsParameterSet.add(this.__getH1C3C1C1AtThirdTripleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2C2AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2C2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2C2AtFirstAndThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2C3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2H1AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2C1AtSecondDoubleBoudParameterList());
        this.itsParameterSet.add(this.__getC2C3C2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C1C1AtFirstDoubleBondAndThirdTripleBondParameterList());
        this.itsParameterSet.add(this.__getC2C1C1C2AtSecondTripleBondParameterList());
        this.itsParameterSet.add(this.__getC1C3C3H1ParameterList());
        this.itsParameterSet.add(this.__getC1C3C2H1ParameterList());
        this.itsParameterSet.add(this.__getC1C2C2H1AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC1C3C2C3ParameterList());
        this.itsParameterSet.add(this.__getC1C3C2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC1C3C3C1ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3O3ParameterList());
        this.itsParameterSet.add(this.__getC3C3O3C3ParameterList());
        this.itsParameterSet.add(this.__getC3C3C2O3ParameterList());
        this.itsParameterSet.add(this.__getC3C3O3C2ParameterList());
        this.itsParameterSet.add(this.__getC3C2O3C3ParameterList());
        this.itsParameterSet.add(this.__getC3C2O3C3ParameterList());
        this.itsParameterSet.add(this.__getC2C3O3C3ParameterList());
        this.itsParameterSet.add(this.__getO3C3C2C3ParameterList());
        this.itsParameterSet.add(this.__getC3C2C2O3AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C2O3C2ParameterList());
        this.itsParameterSet.add(this.__getC3C2C2O3ParameterList());
        this.itsParameterSet.add(this.__getC2C3C2O3ParameterList());
        this.itsParameterSet.add(this.__getC2C2O3C3ParameterList());
        this.itsParameterSet.add(this.__getC2C2O3C3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO3C3C2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2O3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2O3AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2O3C2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC1C3C3O3ParameterList());
        this.itsParameterSet.add(this.__getH1C3C3O3ParameterList());
        this.itsParameterSet.add(this.__getH1C3C2O3ParameterList());
        this.itsParameterSet.add(this.__getH1C3O3C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3O3C2ParameterList());
        this.itsParameterSet.add(this.__getH1C2O3C3ParameterList());
        this.itsParameterSet.add(this.__getO3C3C2H1ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2O3AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2O3C2ParameterList());
        this.itsParameterSet.add(this.__getC3C3O3O3ParameterList());
        this.itsParameterSet.add(this.__getC3O3O3C3ParameterList());
        this.itsParameterSet.add(this.__getO3C3C3O3ParameterList());
        this.itsParameterSet.add(this.__getO3C3O3C3ParameterList());
        this.itsParameterSet.add(this.__getO3C2C2O3AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO3C3C2O3ParameterList());
        this.itsParameterSet.add(this.__getO3C3O3H1ParameterList());
        this.itsParameterSet.add(this.__getO3C3O3O3ParameterList());
        this.itsParameterSet.add(this.__getH1C3O3O3ParameterList());
        this.itsParameterSet.add(this.__getC1C3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getCPC3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C2C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C2C2OMParameterList());
        this.itsParameterSet.add(this.__getC2C2C2O2AtFirstAndThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2OMAtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3C2OMParameterList());
        this.itsParameterSet.add(this.__getC1C3C2OMParameterList());
        this.itsParameterSet.add(this.__getC2C3C2OMParameterList());
        this.itsParameterSet.add(this.__getC3C3C2OMParameterList());
        this.itsParameterSet.add(this.__getCPC3C2OMParameterList());
        this.itsParameterSet.add(this.__getH1C3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2C2O3ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2N2ParameterList());
        this.itsParameterSet.add(this.__getO3C3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO2C2O3C3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO2C2O3C2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3C3N3ParameterList());
        this.itsParameterSet.add(this.__getC3C3N3C3ParameterList());
        this.itsParameterSet.add(this.__getC3C3N3C2ParameterList());
        this.itsParameterSet.add(this.__getC2C3C3N3ParameterList());
        this.itsParameterSet.add(this.__getC2C3N3C3ParameterList());
        this.itsParameterSet.add(this.__getN3C3C2C3ParameterList());
        this.itsParameterSet.add(this.__getN3C3C2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2N3C3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2N3AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3C3N3ParameterList());
        this.itsParameterSet.add(this.__getH1C3N3C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3N3C2ParameterList());
        this.itsParameterSet.add(this.__getH1C2N3C3ParameterList());
        this.itsParameterSet.add(this.__getN3C3C2H1ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2N3AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO3C3C3N3ParameterList());
        this.itsParameterSet.add(this.__getN3C3C2O2AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3N3N3ParameterList());
        this.itsParameterSet.add(this.__getC3N3N3C3ParameterList());
        this.itsParameterSet.add(this.__getN3C3C3N3ParameterList());
        this.itsParameterSet.add(this.__getN3C3N3C3ParameterList());
        this.itsParameterSet.add(this.__getC2C3N3N3ParameterList());
        this.itsParameterSet.add(this.__getH1C3N3N3ParameterList());
        this.itsParameterSet.add(this.__getN3C3N3N3ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3N2ParameterList());
        this.itsParameterSet.add(this.__getC3C3N2C3ParameterList());
        this.itsParameterSet.add(this.__getC3C3N2C2ParameterList());
        this.itsParameterSet.add(this.__getC3C3N2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C3C3N2ParameterList());
        this.itsParameterSet.add(this.__getC3C3C2N2ParameterList());
        this.itsParameterSet.add(this.__getC3C2N2C3ParameterList());
        this.itsParameterSet.add(this.__getC3C2N3H3ParameterList());
        this.itsParameterSet.add(this.__getC3C2C2N2ParameterList());
        this.itsParameterSet.add(this.__getC2C3N2C3ParameterList());
        this.itsParameterSet.add(this.__getC2C2N2C3ParameterList());
        this.itsParameterSet.add(this.__getC2C2C2N2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C3N2C2ParameterList());
        this.itsParameterSet.add(this.__getH1C3C3N2ParameterList());
        this.itsParameterSet.add(this.__getH1C3N2C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3N2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3C2N2ParameterList());
        this.itsParameterSet.add(this.__getH1C3N2C2ParameterList());
        this.itsParameterSet.add(this.__getH1C2N2C3ParameterList());
        this.itsParameterSet.add(this.__getO3C3C3N2ParameterList());
        this.itsParameterSet.add(this.__getN2C3C2O3ParameterList());
        this.itsParameterSet.add(this.__getO2C2N2C3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getN2C3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO2C2N2C2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getN3C3C2N2ParameterList());
        this.itsParameterSet.add(this.__getN2C2N2C3ParameterList());
        this.itsParameterSet.add(this.__getN2C2N2C3AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getN2C3C2N2ParameterList());
        this.itsParameterSet.add(this.__getN2C2N2C2ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3F0ParameterList());
        this.itsParameterSet.add(this.__getF0C3C2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2F0AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getF0C3C2C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3C3F0ParameterList());
        this.itsParameterSet.add(this.__getF0C3C2H1ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2F0AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO3C3C3F0ParameterList());
        this.itsParameterSet.add(this.__getF0C3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getF0C3C3F0ParameterList());
        this.itsParameterSet.add(this.__getF0C2C2F0AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3C3ClParameterList());
        this.itsParameterSet.add(this.__getC3C2C2ClAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getClC3C2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2ClAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getClC3C2C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3C3ClParameterList());
        this.itsParameterSet.add(this.__getClC3C2H1ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2ClAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO3C3C3ClParameterList());
        this.itsParameterSet.add(this.__getClC3O3C3ParameterList());
        this.itsParameterSet.add(this.__getO3C2C2ClAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getClC3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getF0C3C3ClParameterList());
        this.itsParameterSet.add(this.__getClC3C3ClParameterList());
        this.itsParameterSet.add(this.__getClC2C2ClAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3C3BrParameterList());
        this.itsParameterSet.add(this.__getC2C2C2BrAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getBrC3C2C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3C3BrParameterList());
        this.itsParameterSet.add(this.__getH1C2C2BrAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getO3C3C3BrParameterList());
        this.itsParameterSet.add(this.__getO3C2C2BrAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getBrC3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getF0C3C3BrParameterList());
        this.itsParameterSet.add(this.__getClC3C3BrParameterList());
        this.itsParameterSet.add(this.__getBrC3C3BrParameterList());
        this.itsParameterSet.add(this.__getBrC2C2BrAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3C3I0ParameterList());
        this.itsParameterSet.add(this.__getI0C3C2C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3C3I0ParameterList());
        this.itsParameterSet.add(this.__getO3C3C3I0ParameterList());
        this.itsParameterSet.add(this.__getI0C3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3C3SXParameterList());
        this.itsParameterSet.add(this.__getC3C3SXC3ParameterList());
        this.itsParameterSet.add(this.__getC2C3C3SXParameterList());
        this.itsParameterSet.add(this.__getC2C3SXC3ParameterList());
        this.itsParameterSet.add(this.__getSXC3C2C3ParameterList());
        this.itsParameterSet.add(this.__getSXC3C2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3C3SXParameterList());
        this.itsParameterSet.add(this.__getH1C3SXC3ParameterList());
        this.itsParameterSet.add(this.__getSXC3C2H1ParameterList());
        this.itsParameterSet.add(this.__getO3C3C3SXParameterList());
        this.itsParameterSet.add(this.__getSXC3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getN2C3C3SXParameterList());
        this.itsParameterSet.add(this.__getC3C3SXSXParameterList());
        this.itsParameterSet.add(this.__getC3SXSXC3ParameterList());
        this.itsParameterSet.add(this.__getH1SXSXC3ParameterList());
        this.itsParameterSet.add(this.__getSXC3C3SXParameterList());
        this.itsParameterSet.add(this.__getSXC3SXC3ParameterList());
        this.itsParameterSet.add(this.__getC2C3SXSXParameterList());
        this.itsParameterSet.add(this.__getH1C3SXSXParameterList());
        this.itsParameterSet.add(this.__getSXC3SXSXParameterList());
        this.itsParameterSet.add(this.__getC3C3SXO2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3SXO2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getSXC3SXO2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3C3SiParameterList());
        this.itsParameterSet.add(this.__getC3C3SiC3ParameterList());
        this.itsParameterSet.add(this.__getC3C3C2SiParameterList());
        this.itsParameterSet.add(this.__getC3C3SiC2ParameterList());
        this.itsParameterSet.add(this.__getC3C2SiC3ParameterList());
        this.itsParameterSet.add(this.__getC2C3C3SiParameterList());
        this.itsParameterSet.add(this.__getC2C3SiC3ParameterList());
        this.itsParameterSet.add(this.__getC3C2C2SiAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C2SiC2ParameterList());
        this.itsParameterSet.add(this.__getC2C3SiC2ParameterList());
        this.itsParameterSet.add(this.__getC2C2SiC3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getSiC3C2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2SiAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2SiC2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3SiH1ParameterList());
        this.itsParameterSet.add(this.__getH1C3C3SiParameterList());
        this.itsParameterSet.add(this.__getH1C3SiC3ParameterList());
        this.itsParameterSet.add(this.__getC2C3SiH1ParameterList());
        this.itsParameterSet.add(this.__getH1C3SiC2ParameterList());
        this.itsParameterSet.add(this.__getH1C2SiC3ParameterList());
        this.itsParameterSet.add(this.__getSiC3C2H1ParameterList());
        this.itsParameterSet.add(this.__getC2C2SiH1AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2C2SiAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2SiC2ParameterList());
        this.itsParameterSet.add(this.__getH1C3SiH1ParameterList());
        this.itsParameterSet.add(this.__getH1C2SiH1ParameterList());
        this.itsParameterSet.add(this.__getC3C3O3SiParameterList());
        this.itsParameterSet.add(this.__getC3C3SiO3ParameterList());
        this.itsParameterSet.add(this.__getC3O3SiC3ParameterList());
        this.itsParameterSet.add(this.__getC3O3SiH1ParameterList());
        this.itsParameterSet.add(this.__getH1C3O3SiParameterList());
        this.itsParameterSet.add(this.__getH1C3SiO3ParameterList());
        this.itsParameterSet.add(this.__getC3O3SiO3ParameterList());
        this.itsParameterSet.add(this.__getC3C3SiSiParameterList());
        this.itsParameterSet.add(this.__getC3SiSiC3ParameterList());
        this.itsParameterSet.add(this.__getSiC3C3SiParameterList());
        this.itsParameterSet.add(this.__getSiC3SiC3ParameterList());
        this.itsParameterSet.add(this.__getC3SiSiC2ParameterList());
        this.itsParameterSet.add(this.__getC2C2SiSiAtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2SiSiC2ParameterList());
        this.itsParameterSet.add(this.__getSiC2C2SiAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3SiSiH1ParameterList());
        this.itsParameterSet.add(this.__getH1C3SiSiParameterList());
        this.itsParameterSet.add(this.__getSiC3SiH1ParameterList());
        this.itsParameterSet.add(this.__getC2SiSiH1ParameterList());
        this.itsParameterSet.add(this.__getH1SiSiH1ParameterList());
        this.itsParameterSet.add(this.__getSiO3SiC3ParameterList());
        this.itsParameterSet.add(this.__getSiO3SiH1ParameterList());
        this.itsParameterSet.add(this.__getSiO3SiO3ParameterList());
        this.itsParameterSet.add(this.__getC3SiSiSiParameterList());
        this.itsParameterSet.add(this.__getH1SiSiSiParameterList());
        this.itsParameterSet.add(this.__getC3C3O3H2ParameterList());
        this.itsParameterSet.add(this.__getC2C2O3H2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3O3H2ParameterList());
        this.itsParameterSet.add(this.__getC3O3O3H2ParameterList());
        this.itsParameterSet.add(this.__getO3C3O3H2ParameterList());
        this.itsParameterSet.add(this.__getH2O3O3H2ParameterList());
        this.itsParameterSet.add(this.__getC3C3N3H3ParameterList());
        this.itsParameterSet.add(this.__getC2C3N3H3ParameterList());
        this.itsParameterSet.add(this.__getC2C2N3H3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3N3H3ParameterList());
        this.itsParameterSet.add(this.__getH1C2N3H3ParameterList());
        this.itsParameterSet.add(this.__getC3N3N3H3ParameterList());
        this.itsParameterSet.add(this.__getN3C3N3H3ParameterList());
        this.itsParameterSet.add(this.__getN3N3N3H3ParameterList());
        this.itsParameterSet.add(this.__getH3N3N3H3ParameterList());
        this.itsParameterSet.add(this.__getC3C2O3H2ParameterList());
        this.itsParameterSet.add(this.__getH1C2O3H2ParameterList());
        this.itsParameterSet.add(this.__getO2C2O3H2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3C3PXParameterList());
        this.itsParameterSet.add(this.__getC3C3PXC3ParameterList());
        this.itsParameterSet.add(this.__getC3C3PXC2ParameterList());
        this.itsParameterSet.add(this.__getC2C2PXC3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2PXAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2PXC2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C3C3PXParameterList());
        this.itsParameterSet.add(this.__getC3C3PXH1ParameterList());
        this.itsParameterSet.add(this.__getH1C3C3PXParameterList());
        this.itsParameterSet.add(this.__getH1C3PXC3ParameterList());
        this.itsParameterSet.add(this.__getH1C3PXC2ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2PXAtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3PXH1ParameterList());
        this.itsParameterSet.add(this.__getC3C3O3PXParameterList());
        this.itsParameterSet.add(this.__getC2C2O3PXAtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3O3PXParameterList());
        this.itsParameterSet.add(this.__getH1C2O3PXParmeterList());
        this.itsParameterSet.add(this.__getC3O3PXO3ParameterList());
        this.itsParameterSet.add(this.__getC2O3PXO3ParameterList());
        this.itsParameterSet.add(this.__getC3C3N2H3ParameterList());
        this.itsParameterSet.add(this.__getC3C2N2H3ParameterList());
        this.itsParameterSet.add(this.__getC2C3N2H3ParameterList());
        this.itsParameterSet.add(this.__getH1C3N2H3ParameterList());
        this.itsParameterSet.add(this.__getH1C2N2H3ParameterList());
        this.itsParameterSet.add(this.__getO2C2N2H3AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getN2C2N2H3ParameterList());
        this.itsParameterSet.add(this.__getC3C3N2CPParameterList());
        this.itsParameterSet.add(this.__getH1C3N2CPParameterList());
        this.itsParameterSet.add(this.__getC3N2CPN2ParameterList());
        this.itsParameterSet.add(this.__getH3N2CPN2ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3N5ParameterList());
        this.itsParameterSet.add(this.__getC3C3N5C3ParameterList());
        this.itsParameterSet.add(this.__getC2C3C3N5ParameterList());
        this.itsParameterSet.add(this.__getC2C3N5C3ParameterList());
        this.itsParameterSet.add(this.__getC3C2C2N5AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C2N5C2ParameterList());
        this.itsParameterSet.add(this.__getN5C3C2C2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2C2N5AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2N5C2AtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3C3N5ParameterList());
        this.itsParameterSet.add(this.__getH1C3N5C3ParameterList());
        this.itsParameterSet.add(this.__getH1C3C2N5ParameterList());
        this.itsParameterSet.add(this.__getH1C3N5C2ParameterList());
        this.itsParameterSet.add(this.__getH1C2N5C3ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2N5AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2N5C2ParameterList());
        this.itsParameterSet.add(this.__getO3C3C3N5ParameterList());
        this.itsParameterSet.add(this.__getN5C3C2O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getN3C3N5C3ParameterList());
        this.itsParameterSet.add(this.__getN5C3C2N2ParameterList());
        this.itsParameterSet.add(this.__getSXC3C3N5ParameterList());
        this.itsParameterSet.add(this.__getC3C3N5H4ParameterList());
        this.itsParameterSet.add(this.__getC2C3N5H4ParameterList());
        this.itsParameterSet.add(this.__getH1C3N5H4ParameterList());
        this.itsParameterSet.add(this.__getC3C3C3N4ParameterList());
        this.itsParameterSet.add(this.__getC2C2C2N4AtSecondParameterList());
        this.itsParameterSet.add(this.__getH1C3C3N4ParameterList());
        this.itsParameterSet.add(this.__getH1C2C2N4AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC3C3N4O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getC2C2N4O2AtFirstAndThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3N4O2AtThirdDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C2N4O2AtThirdDoublebondParameterList());
        this.itsParameterSet.add(this.__getC3C3N4OMParameterList());
        this.itsParameterSet.add(this.__getC2C2N4OMAtFirstDoubleBondParameterList());
        this.itsParameterSet.add(this.__getH1C3N4OMParameterList());
        this.itsParameterSet.add(this.__getH1C2N4OMParameterList());
        this.itsParameterSet.add(this.__getN4C2C2N4AtSecondDoubleBondParameterList());
        this.itsParameterSet.add(this.__getN2C2N2H3AtFirstDoubleBondParamterList());
        this.itsParameterSet.add(this.__getO2C2C2O2AtFirstAndThirdDoubleBondParameterList());
    }

    private void __generateAnyBondParameterSet() {
        this.itsAnyBondParameterSet = new ArrayList<>();

        this.itsAnyBondParameterSet.add(this.__getC3C3C2C2InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getH1C3C2C2InAnyBondparameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2C2C2InAnyBondAtThirdDoubleBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2C2H1InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2C2C3InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C3C2C2InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2O3C3InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2C2O3InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2C2O2InAnyBondAtThirdDoubleBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2C2OMInAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2C2N2InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2O3H2InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2SXC3InAnyBondParameterList());
        this.itsAnyBondParameterSet.add(this.__getC2C2N4O2InAnyBondAtThirdDoubleBondParameterList());
    }

    private void __generateExceptionParameterSet() {
        this.itsExceptionParameterSet = new ArrayList<>();

        this.itsExceptionParameterSet.add(this.__getO2C2C200ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C2C200ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__getO2C2N200AtFirstDoubleBondExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C2N200ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C3SX00ExceptionParameterList());
        //add to dot bond parameter
        this.itsExceptionParameterSet.add(this.__getO2C2O300AtFirstDoubleBondExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__getOMC2C300ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C3C300ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C2C300ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C2C300AtFirstDoubleBondExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C2O200AtSecondDoubleBondExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C2O300ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C3O300ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__getN2C2N200AtFirstDoubleBondExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__getO2C2N200AtFirstDoubleBondExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C2N200AtSecondDoubleBondExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C3N300ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C3N200AtThirdDoubleBondExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00C3N200ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00N3N300ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00O3Si00ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00N3Si00ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00N2Si00ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00SiSi00ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00SXSXH1ExceptionParameterList());
        this.itsExceptionParameterSet.add(this.__get00SXSX00ExceptionParameterList());
    }

    private void __generateAnyBondExceptionParameterSet() {
        this.itsAnyBondExceptionParameterSet = new ArrayList<>();

        this.itsAnyBondExceptionParameterSet.add(this.__get00C2C200InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2N300InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2N200InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2N400InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2C200InAnyBondAtSecondDoubleBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2Si00InAnyBondExceptionParameterList());
        //add to dot bond parameter
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2C200InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2O300InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00N2N200InAnyBondAtSecondDoubleBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2N400InAnyBondAtSecondDoubleBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C3N400InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C3N500InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C3PX00InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2PX00InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00O3PX00InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C3SX00InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C2SX00InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00O3SX00InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00N3SX00InAnyBondExceptionParameterList());
        this.itsAnyBondExceptionParameterSet.add(this.__get00C10000InAnyBondExceptionParameterList());
    }

    private void __generateSpecialStructureParameterSet() {
        this.itsSpecialStrctureParameterSet = new ArrayList<>();
        
        this.itsSpecialStrctureParameterSet.add(this.__getAllAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__get00N2TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getC2C3TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getC2C2TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getH1C2TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getH3N2TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getC2N2TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getC3N2TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getO3N2TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getH1C3TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getC3C3TwoAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getC3C3C3OneAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getH1C3C3OneAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getC3O3C3OneAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getH1C3N2OneAtomInBenzenoidParameterList());
        this.itsSpecialStrctureParameterSet.add(this.__getC2C3C3OneAtomInBenzenoidParameterList());
    }
    
    private TorsionParameterList __getC2C3C3OneAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.8, 0.8, 2.56);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getH1C3N2OneAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.58);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getC3O3C3OneAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.403);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getH1C3C3OneAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getC3C3C3OneAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.225, 0.41, 0.436);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getC3C3TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.8, -0.1, -0.55);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getH1C3TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON, null, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.09);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getO3N2TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_NITROGEN, null, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getC3N2TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN, null, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-1.65, 3.2, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getC2N2TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN, null, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.7, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getH3N2TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN, MM3AtomTypeGenerator.SP2_NITROGEN, null, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.7, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getH1C2TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON, null, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getC2C2TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, null, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;
        
        theParameter = new TorsionParameter(0.0, 1.45, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));
        theAtomTypeList.clear();

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __getC2C3TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null, null, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.3, -0.65, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
    
    private TorsionParameterList __get00N2TwoAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_NITROGEN, null, null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, 
                MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.BENZENOID);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 3.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getAllAtomInBenzenoidParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, null, null, null, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE, MM3AtomTypeGenerator.BENZENOID,
                MM3AtomTypeGenerator.BENZENOID, MM3AtomTypeGenerator.NOT_CONTAIN_SPECIAL_STRUCTURE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 36.8, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C10000InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP_CARBON, null, null,
                TorsionParameterList.ANY_BOND, TorsionParameterList.ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00SXSX00ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SULFUR, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -4.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00SXSXH1ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SULFUR,
                MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -7.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00N2SX00InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SULFUR, null,
                TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00N3SX00InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SULFUR, null,
                TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.6, 0.25);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00O3SX00InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SULFUR, null,
                TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.25);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2SX00InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SULFUR, null,
                TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.6, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3SX00InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR, null,
                TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00SiSi00ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00N2Si00ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SILICON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00N3Si00ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SILICON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00O3Si00ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SILICON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00O3PX00InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.PHOSPHORUS, null,
                TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 1.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2PX00InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.PHOSPHORUS, null,
                TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3PX00InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.PHOSPHORUS, null,
                TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3N500InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3N400InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, null,
                TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2N400InAnyBondAtSecondDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, null,
                null, Order.DOUBLE, null, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 5.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00N2N200InAnyBondAtSecondDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_NITROGEN, null,
                null, Order.DOUBLE, null, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 10.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00N3N300ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3N200ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3N200AtThirdDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN, null,
                Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3N300ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2N200AtSecondDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN, null,
                Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 10.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C2N200AtFirstDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, null, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 4.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3O300ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2O300InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, null,
                TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2O300ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-2.5, 1.4, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2O200AtSecondDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, null,
                Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2C300AtFirstDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null,
                Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2C300ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3C300ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getOMC2C300ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEGATIVE_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2O300AtFirstDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, null, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-1.7, 9.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2Si00InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, null, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2C200InAnyBondAtSecondDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                null, null, Order.DOUBLE, null, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2N400InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, null, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C3SX00ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2N200InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, null, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2N200ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 4.5, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2N200AtFirstDoubleBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, null, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 4.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2N300InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, null, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2C200InAnyBondExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, null, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.25, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __get00C2C200ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(null, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, null);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.1, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2C200ExceptionParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, null, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.1, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2N4O2InAnyBondAtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, null, Order.SINGLE, Order.DOUBLE,
                TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2SXC3InAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2C2O2AtFirstAndThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.DOUBLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.1, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C2N2H3AtFirstDoubleBondParamterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN4C2C2N4AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 10.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2N4OMParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.3, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N4OMParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.06);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2N4OMAtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.NEGATIVE_OXYGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.3, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N4OMParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.61, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2N4O2AtThirdDoublebondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.3, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N4O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.61, 0.06);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2N4O2AtFirstAndThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.DOUBLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.3, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N4O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.61, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2N4AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.5, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3N4ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2N4AtSecondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3N4ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.POSITIVE_SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.8);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N5H4ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.POSITIVE_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.25);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3N5H4ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.POSITIVE_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N5H4ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.POSITIVE_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.12, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSXC3C3N5ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN5C3C2N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3N5C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN5C3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C3N5ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -0.6, 0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2N5C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 10.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2N5AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2N5C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 10.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N5C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.24);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2N5ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.24);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N5C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.52);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3N5ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.15, 0.0, 0.15);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2N5C2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 10.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2N5AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN5C3C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2N5C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 10.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2N5AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3N5C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3N5ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.4, 0.5);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.18);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N5C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.2, 0.73, 0.8);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3N5ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.POSITIVE_SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.1, 0.4, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH3N2CPN2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.POSITIVE_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 4.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3N2CPN2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN,
                MM3AtomTypeGenerator.POSITIVE_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 4.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N2CPParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.POSITIVE_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N2CPParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.POSITIVE_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.6);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C2N2H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2N2H3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.0, 4.1, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2N2H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 4.6, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N2H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.08);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3N2H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2N2H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 5.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N2H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.2, 0.0, 0.01);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2O3PXO3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.2, 0.0, -0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3O3PXO3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2O3PXParmeterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.1, 2.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3O3PXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.53);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2O3PXAtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.PHOSPHORUS, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.1, 4.6, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3O3PXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.2, 4.6, 0.45);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3PXH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.484);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2PXAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.PHOSPHORUS, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 16.25, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3PXC2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.05, 0.0, 0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3PXC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.05, 0.0, 0.48);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3PXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3PXH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.52, -0.4, 0.664);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3PXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2PXC2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.2, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2PXAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.PHOSPHORUS, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 16.25, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2PXC3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3PXC2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.12, -0.25, 0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3PXC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.PHOSPHORUS, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.175, 0.0, 0.69);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3PXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.PHOSPHORUS);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.2, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2O3H2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-3.285, 5.6, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2O3H2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.77, 0.85, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2O3H2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.5, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH3N3N3H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.8, -9.68, 0.21);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3N3N3H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.025, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3N3H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3N3N3H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(2.0, -8.5, 0.8);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2N3H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 3.3, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N3H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.121, -0.648, 0.199);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2N3H3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3N3H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N3H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.073, -0.422, 0.327);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH2O3O3H2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.8, -1.945, -0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3O3H2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3O3O3H2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.655, -2.005, 0.545);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3O3H2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2O3H2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.3, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2O3H2InAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.4, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3O3H2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.09);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3O3H2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.4, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiSiSiSiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.125);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1SiSiSiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.07);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3SiSiSiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiO3SiO3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiO3SiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiO3SiC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1SiSiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.132);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2SiSiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.45);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiC3SiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.167);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3SiSiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.27);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3SiSiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.127);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiC2C2SiAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 6.45, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2SiSiC2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2SiSiAtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.44, -0.24, 0.06);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3SiSiC2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.093);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiC3SiC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiC3C3SiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.167);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3SiSiC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.107);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3SiSiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3O3SiO3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.15);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3SiO3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.21);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3O3SiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3O3SiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.32);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3O3SiC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.23);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3SiO3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.167);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3O3SiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2SiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.52);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3SiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.176);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2SiC2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.6);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2SiAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 6.45, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2SiH1AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.3, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiC3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.717);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2SiC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.717);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3SiC2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.117);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3SiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3SiC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.195);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3SiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.225);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3SiH1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.272);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2SiC2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.232);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2SiAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 6.45, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSiC3C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.75, 0.0, 0.505);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2SiC3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.3, 0.3, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3SiC2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2SiC2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.44, -0.24, 0.06);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2SiAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 6.45, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3SiC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.167);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3SiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.167);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2SiC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3SiC2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.167);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2SiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3SiC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SILICON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.167);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3SiParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SILICON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSXC3SXO2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3SXO2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.175);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3SXO2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.235, -0.15, 0.175);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSXC3SXSXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.2, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3SXSXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.3, 0.0, 0.6);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3SXSXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.3, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSXC3SXC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.8, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, -0.9, 0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSXC3C3SXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(1.25, -0.3, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1SXSXC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SULFUR,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.85, -6.685, 0.75);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3SXSXC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.85, -7.555, 2.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3SXSXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.2, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C3C3SXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSXC3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C3SXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.8, 0.0, -0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(-0.8, 0.9, -0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(-0.7, 0.8, -0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSXC3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3SXC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.55);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.66);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3SXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.125);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.54);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSXC3C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getSXC3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3SXC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.483);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3SXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.483);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3SXC3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SULFUR, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(-0.44, -0.26, 0.6);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3SXParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SULFUR);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.27, 0.093);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.2, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getI0C3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.IODINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.017);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C3I0ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.IODINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3I0ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.IODINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.267);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getI0C3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.IODINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3I0ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.IODINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -0.5, 0.267);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getBrC2C2BrAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.BROMINE, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.BROMINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.8, 15.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getBrC3C3BrParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.BROMINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getClC3C3BrParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.267);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getF0C3C3BrParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.253);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getBrC3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.BROMINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.78, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C2C2BrAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.BROMINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C3BrParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -1.4, 0.18);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2BrAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.BROMINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3BrParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.385);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getBrC3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.BROMINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2BrAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.BROMINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3BrParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.BROMINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -0.41, 1.06);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getClC2C2ClAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.CHLORINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-1.6, 15.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getClC3C3ClParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.24, 0.62, 0.54);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getF0C3C3ClParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.253);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getClC3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.6, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C2C2ClAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.5, 15.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getClC3O3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.5, -0.42, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C3ClParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.2, 0.18);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2ClAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.CHLORINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getClC3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3ClParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.406);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getClC3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2ClAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.CHLORINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getClC3C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.CHLORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.5, 0.3, -0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2ClAtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.CHLORINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.32, 15.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3ClParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.CHLORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -0.25, 0.55);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getF0C2C2F0AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.FLUORINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-2.3, 15.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getF0C3C3F0ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.1, -2.0, 0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getF0C3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 4.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C3F0ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -1.4, 0.18);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2F0AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getF0C3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.848, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3F0ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.351);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getF0C3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2F0AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.FLUORINE, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getF0C3C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.FLUORINE, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.243, 1.445, -1.243);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3F0ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.FLUORINE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -0.086, 0.93);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C2N2C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.8, 1.3, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C3C2N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.7, 0.7, 0.2);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C2N2C3AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C2N2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.01);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3C2N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2N2C2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.6, 1.3, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.1, 0.7, 2.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2N2C3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.9, 12.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN2C3C2O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.8, 1.5);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C3N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2N2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.0, 3.9, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N2C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.01);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.23);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.46);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3N2C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(2.3, -1.2, 0.8);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2N2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.5, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2N2InAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.9, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2N2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 5.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3N2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.457);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.5, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2N3H3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.HYDROGEN_BOUNDED_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 5.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2N2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.1, 3.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.7, -1.1, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.18);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N2C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.3);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.91);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3N3N3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N3N3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.52);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3N3N3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3N3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3C3N3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.069, -1.267, 1.38);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3N3N3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.9, -6.8, 0.21);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N3N3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.2, 0.73, 0.8);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3C2O2AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C3N3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -3.22, 1.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2N3AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 9.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2N3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.67, 1.6, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N3C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.65);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3N3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.072, -0.012, 0.563);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3N3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.374);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2N3AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 9.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2N3C3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-1.65, 3.2, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getN3C3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3N3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3N3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.4, 0.5);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.18);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N3C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.45);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3N3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_NITROGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.958, -0.155, 0.766);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3N3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.302, 0.696, 0.499);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2O3C2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-1.66, 8.98, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO2C2O3C3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 6.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.42, 2.33, 0.64);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2N2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_NITROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.8, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.4, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.4, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_NITROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.7, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(-0.154, 0.044, -0.086);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getCPC3C2OMParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.POSITIVE_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2OMParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C2OMParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C3C2OMParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2OMParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2OMAtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEGATIVE_OXYGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.2, 1.65, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2OMInAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEGATIVE_OXYGEN, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.35, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2O2AtFirstAndThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.DOUBLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.95, 1.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.4, 1.7, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.5, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.4, 1.4, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(2.3, 2.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.8, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, -2.3, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.8, 2.5, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_NITROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(1.4, 1.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_NITROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.65, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2O2InAnyBondAtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, null, Order.SINGLE, Order.DOUBLE, TorsionParameterList.ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 3.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 2.9, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_NITROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.35, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2OMParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.7, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 2.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.7, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 2.5, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_NITROGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(2.12, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getCPC3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.POSITIVE_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(-0.457, 1.106, -0.16);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, -0.35);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C3C2O2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.NEGATIVE_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3O3O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.5, 0.0, 0.775);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3O3O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.403);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3O3H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.7, -2.35, 0.2);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C2O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(3.55, -0.02, -2.09);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C2C2O3AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-2.0, 16.25, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3O3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-2.5, 0.0, 0.85);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C3O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.0, -2.0, 0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3O3O3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(2.61, -2.55, 0.845);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3O3O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.355);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2O3C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2O3AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 16.25, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2O3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.88, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(3.0, 3.1, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3O3C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.132);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.53);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3O3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.68);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.2);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.54);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C3C3O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, -0.4, 0.18);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2O3C2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.3, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2O3AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 16.25, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2O3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.4, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2O3InAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2O3C3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(3.53, 2.3, -3.53);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2O3C3InAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.7, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2O3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.4, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(3.53, 2.3, -3.53);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C2O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.417);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2O3C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-2.5, 1.39, 0.18);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2O3AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-1.2, 16.25, 0.18);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getO3C3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3O3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.403);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.18);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2O3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(4.7, 6.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(2.3, 4.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3O3C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-2.03, 1.21, -0.67);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.4);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.4, 1.0, -0.07);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3O3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.45, 0.05, 0.757);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3O3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SINGLE_BOND_OXYGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.2, 0.0, 0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C3C3C1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(1.0, 0.0, 0.093);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C3C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.78);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C2C2H1AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.78);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C3C3H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.56);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C1C1C2AtSecondTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.TRIPLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.001, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C1C1AtFirstDoubleBondAndThirdTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON, Order.DOUBLE, Order.SINGLE, Order.TRIPLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.001, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C2C2InAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.25, -0.65, 0.6);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2C1AtSecondDoubleBoudParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP_CARBON, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2H1AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2C3InAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2C2AtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON, Order.SINGLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.55, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2C3AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.7, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 2.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2C2AtFirstAndThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, Order.DOUBLE, Order.SINGLE, Order.DOUBLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP3_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.55, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2H1InAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, TorsionParameterList.ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2C2InAnyBondAtThirdDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, null, Order.SINGLE, Order.DOUBLE, TorsionParameterList.ANY_BOND,
                TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 3.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2C2AtFirstDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.DOUBLE, Order.SINGLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 9.82, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2C2AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(-0.67, 10.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C1C1AtThirdTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON, Order.SINGLE, Order.SINGLE, Order.TRIPLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.001, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2C1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.24);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2C2AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.25, 9.0, -0.55);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.4, 9.82, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.7, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 8.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 2.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC1C1C1C1AtSecondTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON, Order.SINGLE, Order.TRIPLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.001, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C1C1H1AtSecondTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, Order.SINGLE, Order.TRIPLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.001, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C1C1C1AtSecondTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON, Order.SINGLE, Order.TRIPLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.001, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C1C1C1AtSecondTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP_CARBON, Order.SINGLE, Order.TRIPLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.001, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C1C1C2AtSecondTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP_CARBON,
                MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.TRIPLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.001, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 9.82, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2C2AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 15.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(-0.61, 7.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C2C2H1AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 10.4, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.8);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(-0.9, 0.0, -0.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3C1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.093);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.1);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 1.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2H1AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 11.5, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2H1AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 10.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 2.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(3.25, 15.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 10.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2C3AtSecondDoubleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON, Order.SINGLE, Order.DOUBLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.3, 8.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C2C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.8, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.98, 10.379, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2C2InAnyBondparameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, -0.09);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.274);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, -0.14);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C1C1AtThirdTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP_CARBON, MM3AtomTypeGenerator.SP2_CARBON, Order.SINGLE, Order.SINGLE, Order.TRIPLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.001, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2C1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.44, 0.24, 0.06);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2C2InAnyBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON, TorsionParameterList.NOT_ANY_BOND, TorsionParameterList.NOT_ANY_BOND,
                TorsionParameterList.ANY_BOND);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(-0.7, -0.2, -0.55);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.457);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.457);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.655, 0.266, 0.474);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.01);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C1C1H1AtSecondTripleBondParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, Order.SINGLE, Order.TRIPLE, Order.SINGLE);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.04, 0.0, 0.0);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C2C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP2_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 1.6, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 1.55, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.SP2_CARBON);
        theParameter.setConnectedAtomTypeListInSecondAtom(new ArrayList<>(theAtomTypeList));
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(2.75, 12.0, 0.0);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.115, 0.027, 0.269);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.58);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.13);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.0, 0.0, 0.54);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C2C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.606, 0.292, 0.014);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInThirdAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.06, 0.03, 1.25);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.5);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC2C3C3H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP2_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.018);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFirstAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3C1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.2, -0.26, 0.093);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3C2ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP2_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.4, 0.01);
        theAtomTypeList.add(MM3AtomTypeGenerator.DOUBLE_BOND_OXYGEN);
        theParameter.setConnectedAtomTypeListInFourthAtom(new ArrayList<>(theAtomTypeList));
        theAtomTypeList.clear();
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        theParameter = new TorsionParameter(0.2, -0.2, 1.3);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3C3ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.185, 0.17, 0.52);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getC3C3C3H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.28);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }

    private TorsionParameterList __getH1C3C3H1ParameterList() {
        TorsionParameterList theParameterList = new TorsionParameterList(MM3AtomTypeGenerator.NEUTRAL_HYDROGEN, MM3AtomTypeGenerator.SP3_CARBON,
                MM3AtomTypeGenerator.SP3_CARBON, MM3AtomTypeGenerator.NEUTRAL_HYDROGEN);
        List<Integer> theAtomTypeList = new ArrayList<>();
        TorsionParameter theParameter;

        theParameter = new TorsionParameter(0.0, 0.0, 0.238);
        theParameterList.setParameterList().add(new TorsionParameter(theParameter));

        return theParameterList;
    }
}
