/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.sbff.interenergy.calculableset;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculableSet;
import org.bmdrc.sbff.parameter.interenergy.SbffHydrogenBondParameter;

/**
 *
 * @author labwindows
 */
public class CalculableHydrogenBondComponent extends AbstractCalculableSet implements Serializable {

    private static final long serialVersionUID = 5489773812740679181L;
    
    private SbffHydrogenBondParameter itsParameter;
    
    public CalculableHydrogenBondComponent(Integer theFirstAtomIndex, Integer theSecondAtomIndex) {
        this._initializeAtomIndexList(theFirstAtomIndex, theSecondAtomIndex);
    }
    
    public CalculableHydrogenBondComponent(Integer theFirstAtomIndex, Integer theSecondAtomIndex, SbffHydrogenBondParameter theParameter) {
        this._initializeAtomIndexList(theFirstAtomIndex, theSecondAtomIndex);
        this.itsParameter = theParameter;
    }

    public SbffHydrogenBondParameter getParameter() {
        return itsParameter;
    }

    public void setParameter(SbffHydrogenBondParameter theParameter) {
        this.itsParameter = theParameter;
    }
}
