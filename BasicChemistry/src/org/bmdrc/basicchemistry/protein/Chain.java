/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2014, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.protein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bmdrc.basicchemistry.protein.interfaces.ISequenceDifferenceType;
import org.bmdrc.basicchemistry.reader.PDBReader;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.Bond;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class Chain implements ISequenceDifferenceType, Serializable {
    private static final long serialVersionUID = -7982288519720339094L;

    protected List<AminoAcid> itsAminoAcidList;
    private IAtomContainerSet itsHeteroMoleculeList;
    private Map<String, String> itsPropertyMap;
    private List<String> itsSequenceList;
    private String itsChainName;
    private List<AminoAcid> itsInsertionSequence;
    private List<AminoAcid> itsDeletionSequence;
    
    //constant String variable
    public final String ALPHA_HELIX = "Alpha Helix";
    public final String BETA_SHEET = "Beta Sheet";
    protected final String NITROGEN_ATOM_NAME_IN_AMIDE = "N";
    protected final String CARBON_ATOM_NAME_IN_AMIDE = "C";

    public Chain() {
        this.itsAminoAcidList = new ArrayList<>();
        this.itsHeteroMoleculeList = new AtomContainerSet();
        this.itsPropertyMap = new HashMap<>();
        this.itsSequenceList = new ArrayList<>();
        this.itsChainName = new String();
        this.itsInsertionSequence = new ArrayList<>();
        this.itsDeletionSequence = new ArrayList<>();
    }

    public Chain(Chain theChain) {
        try {
            this.itsAminoAcidList = new ArrayList<>(theChain.itsAminoAcidList);
            this.itsHeteroMoleculeList = (IAtomContainerSet)theChain.itsHeteroMoleculeList.clone();
            this.itsPropertyMap = new HashMap<>(theChain.itsPropertyMap);
            this.itsSequenceList = new ArrayList<>(theChain.itsSequenceList);
            this.itsChainName = theChain.itsChainName;
            this.itsInsertionSequence = new ArrayList<>(theChain.itsInsertionSequence);
            this.itsDeletionSequence = new ArrayList<>(theChain.itsDeletionSequence);
        } catch (CloneNotSupportedException ex) {
        }
    }
    
    public List<AminoAcid> getAminoAcidList() {
        return itsAminoAcidList;
    }

    public void setAminoAcidList(List<AminoAcid> theAminoAcidList) {
        this.itsAminoAcidList = theAminoAcidList;
    }

    public List<AminoAcid> setAminoAcidList() {
        return itsAminoAcidList;
    }

    public IAtomContainerSet getHeteroMoleculeList() {
        return itsHeteroMoleculeList;
    }

    public void setHeteroMoleculeList(IAtomContainerSet theHeteroMoleculeList) {
        this.itsHeteroMoleculeList = theHeteroMoleculeList;
    }

    public IAtomContainerSet setHeteroMoleculeList() {
        return itsHeteroMoleculeList;
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

    public List<String> getSequenceList() {
        return itsSequenceList;
    }

    public void setSequenceList(List<String> theSequenceList) {
        this.itsSequenceList = theSequenceList;
    }

    public List<String> setSequenceList() {
        return itsSequenceList;
    }

    public String getChainName() {
        return itsChainName;
    }

    public void setChainName(String theChainName) {
        this.itsChainName = theChainName;
    }

    public List<AminoAcid> getInsertionSequence() {
        return itsInsertionSequence;
    }

    public void setInsertionSequence(List<AminoAcid> itsInsertionSequence) {
        this.itsInsertionSequence = itsInsertionSequence;
    }

    public List<AminoAcid> setInsertionSequence() {
        return itsInsertionSequence;
    }

    public List<AminoAcid> getDeletionSequence() {
        return itsDeletionSequence;
    }

    public void setDeletionSequence(List<AminoAcid> theDeletionSequence) {
        this.itsDeletionSequence = theDeletionSequence;
    }

    public List<AminoAcid> setDeletionSequence() {
        return itsDeletionSequence;
    }

    public Map<String, List<AminoAcid>> getAlphaHelixMap() {
        Map<String, List<AminoAcid>> theAlphaHelixMap = new HashMap<>();

        for (AminoAcid theAminoAcid : this.getAminoAcidList()) {
            if (theAminoAcid.getSecondaryStructureType() != null && theAminoAcid.getSecondaryStructureType().equals(this.ALPHA_HELIX)) {
                String theHelixID = theAminoAcid.getSecondaryStructureID();

                if (theAlphaHelixMap.containsKey(theAminoAcid)) {
                    theAlphaHelixMap.get(theAminoAcid).add(theAminoAcid);
                } else {
                    List<AminoAcid> theAminoAcidList = new ArrayList<>();

                    theAminoAcidList.add(theAminoAcid);
                    theAlphaHelixMap.put(theHelixID, theAminoAcidList);
                }
            }
        }

        return theAlphaHelixMap;
    }

    public Map<String, List<AminoAcid>> getBetaSheetMap() {
        Map<String, List<AminoAcid>> theBetaSheetMap = new HashMap<>();

        for (AminoAcid theAminoAcid : this.getAminoAcidList()) {
            if (theAminoAcid.getSecondaryStructureType() != null && theAminoAcid.getSecondaryStructureType().equals(this.BETA_SHEET)) {
                String theSheetID = theAminoAcid.getSecondaryStructureID();

                if (theBetaSheetMap.containsKey(theAminoAcid)) {
                    theBetaSheetMap.get(theAminoAcid).add(theAminoAcid);
                } else {
                    List<AminoAcid> theAminoAcidList = new ArrayList<>();

                    theAminoAcidList.add(theAminoAcid);
                    theBetaSheetMap.put(theSheetID, theAminoAcidList);
                }
            }
        }

        return theBetaSheetMap;
    }

    public List<String> getInsertionResidueSerialList() {
        List<String> theInsertionResidueSerialList = new ArrayList<>();

        for (AminoAcid theAminoAcid : this.getInsertionSequence()) {
            theInsertionResidueSerialList.add(theAminoAcid.getSerial());
        }

        return theInsertionResidueSerialList;
    }

    public List<String> getDeletionResidueSerialList() {
        List<String> theDeletionResidueSerialList = new ArrayList<>();

        for (AminoAcid theAminoAcid : this.getDeletionSequence()) {
            theDeletionResidueSerialList.add(theAminoAcid.getSerial());
        }

        return theDeletionResidueSerialList;
    }
    
    public List<String> getAminoAcidSerialList() {
        List<String> theAminoAcidSerialList = new ArrayList<>();

        for (AminoAcid theAminoAcid : this.getAminoAcidList()) {
            theAminoAcidSerialList.add(theAminoAcid.getSerial());
        }

        return theAminoAcidSerialList;
    }
    
    public boolean isInsertionSerial(String theSerial) {
        return this.getInsertionResidueSerialList().contains(theSerial);
    }
    
    public boolean isDeletionSerial(String theSerial) {
        return this.getDeletionResidueSerialList().contains(theSerial);
    }
    
    public AminoAcid getInsertionAminoAcid(String theSerial) {
        for(AminoAcid theAminoAcid : this.getInsertionSequence()) {
            if(theAminoAcid.getSerial().equals(theSerial)) {
                return theAminoAcid;
            }
        }
        
        return null;
    }
    
    public AminoAcid getDeletionAminoAcid(String theSerial) {
        for(AminoAcid theAminoAcid : this.getDeletionSequence()) {
            if(theAminoAcid.getSerial().equals(theSerial)) {
                return theAminoAcid;
            }
        }
        
        return null;
    }
    
    public AminoAcid getSequenceAminoAcid(String theSerial) {
        for(AminoAcid theAminoAcid : this.getAminoAcidList()) {
            if(theAminoAcid.getSerial().equals(theSerial)) {
                return theAminoAcid;
            }
        }
        
        return null;
    }
    
    public void generateSequenceList() {
        this.setSequenceList(new ArrayList<String>());
        
        for(AminoAcid theAminoAcid : this.getAminoAcidList()) {
            this.setSequenceList().add(theAminoAcid.getName());
        }
    }
    
    public void inputMM3AtomType() {
        IAtomContainer theMoleculeForm = this.getMolecule();
//        MM3AtomTypeGenerator theGenerator = new MM3AtomTypeGenerator();
        
//        theGenerator.generateAtomType(theMoleculeForm);
        
//        for(IAtom theAtom : theMoleculeForm.atoms()) {
//            this.__inputMM3AtomType(theAtom);
//        }
    }
    
    private void __inputMM3AtomType(IAtom theMoleculeAtom) {
       for(AminoAcid theAminoAcid : this.getAminoAcidList()) {
           if(theMoleculeAtom.getProperty(PDBReader.ATOM_SERIAL).equals(theAminoAcid.getSerial())) {
               this.__inputMM3AtomType(theAminoAcid, theMoleculeAtom);
           }
       }
    }
    
    private void __inputMM3AtomType(AminoAcid theAminoAcid, IAtom theMoleculeAtom) {
        for(IAtom theAminoAcidAtom : theAminoAcid.getMolecule().atoms()) {
//            if(theAminoAcidAtom.equals(theMoleculeAtom)) {
//                theAminoAcidAtom.setProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY, theMoleculeAtom.getProperty(MM3AtomTypeGenerator.MM3_ATOM_TYPE_KEY));
//                break;
//            }
        }
    }
    
    public IAtomContainer getMolecule() {
        IAtomContainer theMolecule = new AtomContainer();
        Integer theConnectingAtomIndexInPreviousAminoAcid = null;
        
        for(AminoAcid theAminoAcid :this.itsAminoAcidList) {
            theMolecule.add(theAminoAcid.getMolecule());
            int theNitrogenAtomIndexInAmide = this.__getNitrogenInAmide(theAminoAcid);
            int theCarbonAtomIndexInAmide = this.__getCarbonInAmide(theAminoAcid);
            int thePreviousAtomSize = theMolecule.getAtomCount() - theAminoAcid.getMolecule().getAtomCount();
            
            if(theConnectingAtomIndexInPreviousAminoAcid != null) {
                IBond theBond = new Bond(theMolecule.getAtom(theConnectingAtomIndexInPreviousAminoAcid), theMolecule.getAtom(thePreviousAtomSize+theNitrogenAtomIndexInAmide));
                
                theMolecule.addBond(theBond);
            }
            theConnectingAtomIndexInPreviousAminoAcid = thePreviousAtomSize + theCarbonAtomIndexInAmide;
        }
        
        return theMolecule;
    }
    
    private int __getNitrogenInAmide(AminoAcid theAminoAcid) {
        for(int ai = 0, aEnd = theAminoAcid.getMolecule().getAtomCount(); ai < aEnd ; ai++) {
            if(theAminoAcid.getMolecule().getAtom(ai).getProperty(AminoAcidInformation.ATOM_NAME).equals(this.NITROGEN_ATOM_NAME_IN_AMIDE)) {
                return ai;
            }
        }
        
        return -1;
    }
    
    private int __getCarbonInAmide(AminoAcid theAminoAcid) {
        for(int ai = 0, aEnd = theAminoAcid.getMolecule().getAtomCount(); ai < aEnd ; ai++) {
            if(theAminoAcid.getMolecule().getAtom(ai).getProperty(AminoAcidInformation.ATOM_NAME).equals(this.CARBON_ATOM_NAME_IN_AMIDE)) {
                return ai;
            }
        }
        
        return -1;
    }
}
