/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.tool;

import java.io.Serializable;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import org.bmdrc.sbff.parameter.cdeap.CdeapParameter;
import org.bmdrc.sbff.parameter.cdeap.CdeapParameterMap;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class CdeapCalculator implements Serializable {

    private static final long serialVersionUID = -8040828179255111390L;

    private CdeapParameterMap itsParameterMap;
    private IAtomContainer itsMolecule;
    //constant String variable
    public static final String CDEAP_KEY = "CDEAP";
    public static final String IS_CDEAP_CALCULATED = "Is_CDEAP_Calculated";

    public CdeapCalculator() {
        this.itsParameterMap = new CdeapParameterMap();
    }

    public IAtomContainer getMolecule() {
        return itsMolecule;
    }

    public void setMolecule(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
    }

    public void inputCdeapPolarizability(IAtomContainer theMolecule) {
        this.setMolecule(theMolecule);

        if (!this.itsMolecule.getProperties().containsKey(MpeoeChargeCalculator.IS_CALCULATED) || this.itsMolecule.getProperty(MpeoeChargeCalculator.IS_CALCULATED).equals(false)) {
            MpeoeChargeCalculator theChargeCalculator = new MpeoeChargeCalculator();

            theChargeCalculator.inputMpeoeCharge(theMolecule);
        }

        this.inputCdeapPolarizability();
        theMolecule.setProperty(CdeapCalculator.IS_CDEAP_CALCULATED, true);
    }

    public void inputCdeapPolarizability() {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            String theCdeapAtomType = theAtom.getProperty(MpeoeAtomTypeGenerator.CDEAP_ATOM_TYPE_KEY).toString();
            CdeapParameter theParameter = this.itsParameterMap.getParameter(theCdeapAtomType);
            double thePolarizability = theParameter.getInitialPolarizability() - theParameter.getCoefficient() * (Double) (theAtom.getProperty(MpeoeChargeCalculator.MPEOE_CHARGE_KEY));

            theAtom.setProperty(CdeapCalculator.CDEAP_KEY, thePolarizability);
        }
    }
}
