/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2014, SungBo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.protein;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class AminoAcid implements Comparable<AminoAcid>, Serializable {

    private static final long serialVersionUID = -1019966681579024878L;

    private String itsName;
    private String itsSecondaryStructureID;
    private String itsSecondaryStructureType;
    private String itsBetaSheetSense;
    private String itsSerial;
    private IAtomContainer itsMolecule;
    private Map<String, String> itsPropertyMap;
    
    //constant Map Key variable
    public static final String AMINO_ACID_DIFFERENCE = "Amino Acid Difference";

    public AminoAcid() {
        this.itsMolecule = new AtomContainer();
        this.setPropertyMap(new HashMap<String, String>());
    }
    
    public AminoAcid(AminoAcid theAminoAcid) {
        try {
            this.itsMolecule = (IAtomContainer)theAminoAcid.itsMolecule.clone();
            this.itsPropertyMap = new HashMap<>(theAminoAcid.itsPropertyMap);
        } catch (CloneNotSupportedException ex) {
        }
        
    }

    public String getName() {
        return itsName;
    }

    public void setName(String theName) {
        this.itsName = theName;
    }

    public String getSecondaryStructureID() {
        return itsSecondaryStructureID;
    }

    public void setSecondaryStructureID(String theSecondaryStructureID) {
        this.itsSecondaryStructureID = theSecondaryStructureID;
    }

    public String getSecondaryStructureType() {
        return itsSecondaryStructureType;
    }

    public void setSecondaryStructureType(String theSecondaryStructureType) {
        this.itsSecondaryStructureType = theSecondaryStructureType;
    }

    public String getBetaSheetSense() {
        return itsBetaSheetSense;
    }

    public void setBetaSheetSense(String theBetaSheetSense) {
        this.itsBetaSheetSense = theBetaSheetSense;
    }

    public IAtomContainer getMolecule() {
        return itsMolecule;
    }

    public void setMolecule(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
    }

    public IAtomContainer setMolecule() {
        return itsMolecule;
    }
    
    public String getSerial() {
        return itsSerial;
    }

    public void setSerial(String itsSerial) {
        this.itsSerial = itsSerial;
    }

    public Map<String, String> getPropertyMap() {
        return itsPropertyMap;
    }

    public void setPropertyMap(Map<String, String> thePropertyMap) {
        this.itsPropertyMap = thePropertyMap;
    }

    public Map<String, String> setPropertyMap() {
        return itsPropertyMap;
    }
    
    @Override
    public int compareTo(AminoAcid theAminoAcid) {
        Integer theSerial = Integer.parseInt(this.getSerial());
        
        return theSerial.compareTo(Integer.parseInt(theAminoAcid.getSerial()));
    }
}
