/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2014, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.predock.variable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.protein.AminoAcid;
import org.bmdrc.basicchemistry.protein.Chain;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class BindingSite implements Serializable {

    private static final long serialVersionUID = 6087582312696265786L;

    private Double itsRadius;
    private Vector3d itsCenterPosition;
    private IAtomContainer itsAminoAcids;
    private List<Integer> itsNewAminoAcidAtomIndexList;
    private IAtomContainer itsLigand;
    //constant String variable
    public final static String ATOM_SERIAL_KEY = "Amino acid serial";
    
    public BindingSite(Double theXOfCenter, Double theYOfCenter, Double theZOfCenter, Double theRadius) {
        this(new Vector3d(theXOfCenter, theYOfCenter, theZOfCenter), theRadius);
    }

    public BindingSite(Vector3d theCenterPosition, Double theRadius) {
        this.itsRadius = theRadius;
        this.itsCenterPosition = theCenterPosition;
    }

    public BindingSite(IAtomContainer theLigand, Double theXOfCenter, Double theYOfCenter, Double theZOfCenter, Double theRadius) {
        this(theLigand, new Vector3d(theXOfCenter, theYOfCenter, theZOfCenter), theRadius);
    }

    public BindingSite(IAtomContainer theLigand, Vector3d theCenterPosition, Double theRadius) {
        this.itsRadius = theRadius;
        this.itsCenterPosition = theCenterPosition;
        try {
            this.itsLigand = theLigand.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }
    
    public BindingSite(Protein theProtein, IAtomContainer theLigand, Vector3d theCenterPosition, Double theRadius) {
        this(theLigand, theCenterPosition, theRadius);
        
        this.generateAminoAcidsInBindingSite(theProtein);
    }

    public BindingSite(BindingSite theBindingSite) {
        this.itsRadius = theBindingSite.itsRadius;
        this.itsCenterPosition = new Vector3d(theBindingSite.itsCenterPosition);

        try {
            if (theBindingSite.itsLigand != null) {
                this.itsLigand = theBindingSite.itsLigand.clone();
            }
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public Double getRadius() {
        return this.itsRadius;
    }

    public void setRadius(Double theRadius) {
        this.itsRadius = theRadius;
    }

    public Vector3d getCenterPosition() {
        return itsCenterPosition;
    }

    public void setCenterPosition(Vector3d theCenterPosition) {
        this.itsCenterPosition = theCenterPosition;
    }

    public List<Integer> getNewAminoAcidAtomIndexList() {
        return this.itsNewAminoAcidAtomIndexList;
    }

    public void setNewAminoAcidAtomIndexList(List<Integer> theNewAminoAcidAtomIndexList) {
        this.itsNewAminoAcidAtomIndexList = theNewAminoAcidAtomIndexList;
    }

    public IAtomContainer getLigand() {
        return itsLigand;
    }

    public void setLigand(IAtomContainer theLigand) {
        this.itsLigand = theLigand;
    }

    public IAtomContainer getAminoAcids() {
        return itsAminoAcids;
    }
    
    public void generateAminoAcidsInBindingSite(Protein theProtein) {
        int theAtomNumber = 0;
        
        this.itsAminoAcids = new AtomContainer();
        this.itsNewAminoAcidAtomIndexList = new ArrayList<>();
        
        for(Chain theChain : theProtein.getChainList()) {
            for(AminoAcid theAminoAcid : theChain.getAminoAcidList()) {
                IAtomContainer theMolecule = theAminoAcid.getMolecule();
                
                if(this.__containAminoAcid(theMolecule)) {
                    this.__setAminoAcidSerial(theMolecule, theAminoAcid);
                    
                    this.itsAminoAcids.add(theMolecule);
                    this.itsNewAminoAcidAtomIndexList.add(theAtomNumber);
                    
                    theAtomNumber += theMolecule.getAtomCount();
                }
            }
        }
    }
    
    private void __setAminoAcidSerial(IAtomContainer theAminoAcidMolecule, AminoAcid theAminoAcid) {
        for(IAtom theAtom : theAminoAcidMolecule.atoms()) {
            theAtom.setProperty(BindingSite.ATOM_SERIAL_KEY, theAminoAcid.getSerial());
        }
    }
    
    private boolean __containAminoAcid(IAtomContainer theAminoAcidMolecule) {
        for(IAtom theAtom : theAminoAcidMolecule.atoms()) {
            Vector3d theAtomPosition = new Vector3d(theAtom);
            
            if(this.itsRadius.compareTo(theAtomPosition.distance(this.itsCenterPosition)) > 0) {
                return true;
            }
        }
        
        return false;
    }
}
