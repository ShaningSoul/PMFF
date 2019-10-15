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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.ui.abstracts.StringConstant;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class Protein implements Iterable<Chain>, Serializable {
    private static final long serialVersionUID = -557456966670596646L;

    private List<Chain> itsChainList;
    private Map<String, String> itsSourceMap;
    private String itsAuthor;
    private List<String> itsValidateDateList;
    private Map<String, String> itsJournalInformationMap;
    private Map<String, List<AminoAcid>> itsActiveSiteSequenceMap;
    private Map<String, String> itsCompoundInformationMap;
    private Map<String, Double> itsUnitCellParameter;
    private String itsProteinType;
    private String itsExperimentType;
    private List<Vector3d> itsOriginCoordinateList;
    private List<Vector3d> itsCrystallographicCoordinateList;
    private List<String> itsUnusualSolventMoleculeSerialList;
    private IAtomContainerSet itsWaterMoleculeSet;
    private Map<String, String> itsPropertyMap;
    private IAtomContainer itsMolecule;

    //constant Map Key variable
    public static final String JOURNAL_PUBLICATION_IDENTIFIER_KEY = "Publication Identifier";
    public static final String HETERO_MOLECULE_ID_KEY = "ID";
    public static final String HETERO_MOLECULE_SERIAL_KEY = "Serial";
    public static final String HETERO_MOLECULE_NAME_KEY = "Name";
    public static final String HETERO_MOLECULE_FORMULA_KEY = "Formula";
    public static final String HETERO_MOLECULE_CHAIN_ID_KEY = "Chain ID";
    public static final String ALPHA_HELIX = "Alpha Helix";
    public static final String BETA_SHEET = "Beta Sheet";
    public static final String A_KEY_IN_UNIT_CELL_PARAMETER = "A";
    public static final String B_KEY_IN_UNIT_CELL_PARAMETER = "B";
    public static final String C_KEY_IN_UNIT_CELL_PARAMETER = "C";
    public static final String ALPHA_KEY_IN_UNIT_CELL_PARAMETER = "Alpha";
    public static final String BETA_KEY_IN_UNIT_CELL_PARAMETER = "Beta";
    public static final String GAMMA_KEY_IN_UNIT_CELL_PARAMETER = "Gamma";
    public static final String OCCUPANCY_IN_ATOM = "Occupancy";
    public static final String TEMPERATURE_FACTOR = "Temperature factor";
    public static final String JOURNAL_PUBMED_ID_KEY = "PubMed ID";
    public static final String JOURNAL_NAME_ID_KEY = "Reference Number";
    public static final String JOURNAL_NAME_KEY = "Reference";
    public static final String JOURNAL_AUTHOR_KEY = "Author";
    public static final String JOURNAL_TITLE_KEY = "Title";
    
    public Protein() {
        this.itsChainList = new ArrayList<>();
        this.itsSourceMap = new HashMap<>();
        this.itsValidateDateList = new ArrayList<>();
        this.itsJournalInformationMap = new HashMap<>();
        this.itsActiveSiteSequenceMap = new HashMap<>();
        this.itsCompoundInformationMap = new HashMap<>();
        this.itsUnitCellParameter = new HashMap<>();
        this.itsOriginCoordinateList = new ArrayList<>();
        this.itsCrystallographicCoordinateList = new ArrayList<>();
        this.itsUnusualSolventMoleculeSerialList = new ArrayList<>();
        this.itsWaterMoleculeSet = new AtomContainerSet();
        this.itsPropertyMap = new HashMap<>();
        this.itsMolecule = new AtomContainer();
    }
    
    public Protein(Protein theProtein) throws CloneNotSupportedException {
        this.itsChainList = new ArrayList<>(theProtein.getChainList());
        this.itsSourceMap = new HashMap<>(theProtein.getSourceMap());
        this.itsValidateDateList = new ArrayList<>(theProtein.getValidateDateList());
        this.itsJournalInformationMap = new HashMap<>(theProtein.getJournalInformationMap());
        this.itsActiveSiteSequenceMap = new HashMap<>(theProtein.getActiveSiteSequenceMap());
        this.itsCompoundInformationMap = new HashMap<>(theProtein.getCompoundInformationMap());
        this.itsUnitCellParameter = new HashMap<>(theProtein.getUnitCellParameter());
        this.itsOriginCoordinateList = new ArrayList<>(theProtein.getOriginCoordinateList());
        this.itsCrystallographicCoordinateList = new ArrayList<>(theProtein.getCrystallographicCoordinateList());
        this.itsUnusualSolventMoleculeSerialList = new ArrayList<>(theProtein.getUnusualSolventMoleculeSerialList());
        this.itsWaterMoleculeSet = (IAtomContainerSet)theProtein.getWaterMoleculeSet().clone();
        this.itsPropertyMap = new HashMap<>(theProtein.getPropertyMap());
        this.itsMolecule = (IAtomContainer)theProtein.getMolecule().clone();
    }

    public List<Chain> getChainList() {
        return itsChainList;
    }

    public void setChainList(List<Chain> theChainList) {
        this.itsChainList = theChainList;
    }

    public List<Chain> setChainList() {
        return itsChainList;
    }
    
    public Map<String, String> getSourceMap() {
        return itsSourceMap;
    }

    public void setSourceMap(Map<String, String> theSourceMap) {
        this.itsSourceMap = theSourceMap;
    }

    public Map<String, String> setSourceMap() {
        return itsSourceMap;
    }

    public String getAuthor() {
        return itsAuthor;
    }

    public void setAuthor(String theAuthor) {
        this.itsAuthor = theAuthor;
    }

    public List<String> getValidateDateList() {
        return itsValidateDateList;
    }

    public void setValidateDateList(List<String> theValidateDateList) {
        this.itsValidateDateList = theValidateDateList;
    }

    public List<String> setValidateDateList() {
        return itsValidateDateList;
    }

    public Map<String, String> getJournalInformationMap() {
        return itsJournalInformationMap;
    }

    public void setJournalInformationMap(Map<String, String> theJournalInformationMap) {
        this.itsJournalInformationMap = theJournalInformationMap;
    }

    public Map<String, String> setJournalInformationMap() {
        return itsJournalInformationMap;
    }

    public Map<String, List<AminoAcid>> getActiveSiteSequenceMap() {
        return itsActiveSiteSequenceMap;
    }

    public void setActiveSiteSequenceMap(Map<String, List<AminoAcid>> theActiveSiteSequenceMap) {
        this.itsActiveSiteSequenceMap = theActiveSiteSequenceMap;
    }
    
    public Map<String, List<AminoAcid>> setActiveSiteSequenceMap() {
        return itsActiveSiteSequenceMap;
    }
    
    public String getProteinType() {
        return itsProteinType;
    }

    public void setProteinType(String theProteinType) {
        this.itsProteinType = theProteinType;
    }

    public String getExperimentType() {
        return itsExperimentType;
    }

    public void setExperimentType(String theExperimentType) {
        this.itsExperimentType = theExperimentType;
    }
    
    public Map<String, String> getCompoundInformationMap() {
        return itsCompoundInformationMap;
    }

    public void setCompoundInformationMap(Map<String, String> theCompoundInformationMap) {
        this.itsCompoundInformationMap = theCompoundInformationMap;
    }

    public Map<String, String> setCompoundInformationMap() {
        return itsCompoundInformationMap;
    }

    public Map<String, Double> getUnitCellParameter() {
        return itsUnitCellParameter;
    }

    public void setUnitCellParameter(Map<String, Double> theUnitCellParameter) {
        this.itsUnitCellParameter = theUnitCellParameter;
    }
    
    public Map<String, Double> setUnitCellParameter() {
        return itsUnitCellParameter;
    }

    public List<Vector3d> getOriginCoordinateList() {
        return itsOriginCoordinateList;
    }

    public void setOriginCoordinateList(List<Vector3d> theOriginCoordinateList) {
        this.itsOriginCoordinateList = theOriginCoordinateList;
    }
    
    public List<Vector3d> setOriginCoordinateList() {
        return itsOriginCoordinateList;
    }

    public List<Vector3d> getCrystallographicCoordinateList() {
        return itsCrystallographicCoordinateList;
    }

    public void setCrystallographicCoordinateList(List<Vector3d> theCrystallographicCoordinateList) {
        this.itsCrystallographicCoordinateList = theCrystallographicCoordinateList;
    }
    
    public List<Vector3d> setCrystallographicCoordinateList() {
        return itsCrystallographicCoordinateList;
    }

    public List<String> getUnusualSolventMoleculeSerialList() {
        return itsUnusualSolventMoleculeSerialList;
    }

    public void setUnusualSolventMoleculeSerialList(List<String> theUnusualSolventMoleculeSerialList) {
        this.itsUnusualSolventMoleculeSerialList = theUnusualSolventMoleculeSerialList;
    }
    
    public List<String> setUnusualSolventMoleculeSerialList() {
        return itsUnusualSolventMoleculeSerialList;
    }

    public IAtomContainerSet getWaterMoleculeSet() {
        return itsWaterMoleculeSet;
    }

    public void setWaterMoleculeSet(IAtomContainerSet theWaterMoleculeSet) {
        this.itsWaterMoleculeSet = theWaterMoleculeSet;
    }
    
    public IAtomContainerSet setWaterMoleculeSet() {
        return itsWaterMoleculeSet;
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

    public IAtomContainer getMolecule() {
        return itsMolecule;
    }

    public void setMoleculeType(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
    }
    
    @Override
    public Iterator<Chain> iterator() {
        return this.getChainList().iterator();
    }
    
    public Chain getChain(String theChainName) {
        for(Chain theChain : this.getChainList()) {
            if(theChain.getChainName().equals(theChainName)) {
                return theChain;
            }
        }
        
        return null;
    }
    
    public Integer getChainIndex(String theChainName) {
        for(int ci = 0, cEnd = this.getChainList().size(); ci < cEnd ; ci++) {
            if(this.getChainList().get(ci).getChainName().equals(theChainName)) {
                return ci;
            }
        }
        
        return -1;
    }
    
    public boolean containChainName(String theChainName) {
        for(Chain theChain : this.getChainList()) {
            if(theChain.getChainName().equals(theChainName)) {
                return true;
            }
        }
        
        return false;
    }
    
    public void addChain(Chain theChain) {
        this.setChainList().add(theChain);
    }
    
    public void setChain(int theIndex, Chain theChain) {
        this.setChainList().set(theIndex, theChain);
    }
    
    public boolean containWater() {
        return !this.getWaterMoleculeSet().isEmpty() ;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append("Protein Type : ").append(this.getProteinType()).append(StringConstant.END_LINE);
        theStringBuilder.append("Number of Amino Acid : ").append(this.__getTotalNumberOfAminoAcid()).append(StringConstant.END_LINE);
        
        return theStringBuilder.toString();
    }
    
    private Integer __getTotalNumberOfAminoAcid() {
        Integer theTotalNumberOfAminoAcid = 0;
        
        for(Chain theChain : this.getChainList()) {
            theTotalNumberOfAminoAcid += theChain.getAminoAcidList().size();
        }
        
        return theTotalNumberOfAminoAcid;
    }
    
    public IAtomContainer generateMolecule() {
        this.itsMolecule = new AtomContainer();
        
        for(Chain theChain : this.itsChainList) {
            this.itsMolecule.add(theChain.getMolecule());
        }
        
        return this.itsMolecule;
    }
}
