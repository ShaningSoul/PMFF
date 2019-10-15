/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.atdl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.tool.RingPerception;
import org.bmdrc.basicchemistry.tool.TopologicalDistanceMatrix;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.RingSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IRingSet;

/**
 * ATDL generator<br>
 * the role of this class generate ATDL by atom
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 15
 */
public class AtdlGenerator implements Serializable {

    private static final long serialVersionUID = -1484526820267582141L;

    private IAtomContainer itsMolecule; //Molecule variable
    private List<Atdl> itsAtdlList; //ATDL List in molecule
    private IRingSet itsRingSet; //Ring set in molecule
    private IRingSet itsAromaticRingSet; // Aromatic ring set in molecule
    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;
    //constant Integer variable
    private final Integer NOT_EXISTED_AROMATIC_BOND = 0;
    private final Integer EXISTED_AROMATIC_BOND = 1;
    //constant String variable
    public static final String IS_CALCULATED_ATDL = "Is_Calculated_Atdl";
    //constant boolean variable
    public static final boolean CALCULATED_ATDL = true;
    public static final boolean NOT_CALCULATED_ATDL = false;

    /**
     * Constructor
     */
    public AtdlGenerator() {
        this.itsMolecule = new AtomContainer();
        this.itsAtdlList = new ArrayList<Atdl>();
        this.itsRingSet = new RingSet();
        this.itsAromaticRingSet = new RingSet();
    }

    /**
     * Constructor<br>
     * This constructor is completed by ATDL generation process
     *
     * @param theMolecule IMolecule type variable
     */
    public AtdlGenerator(IAtomContainer theMolecule) {
        this.itsRingSet = new RingSet();
        this.itsAromaticRingSet = new RingSet();

        this.generateAtdlList(theMolecule);
    }

    /**
     * get molecule
     *
     * @return
     */
    public IAtomContainer getMolecule() {
        return itsMolecule;
    }

    /**
     * set molecule
     *
     * @param theMolecule
     */
    public void setMolecule(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
    }

    /**
     * get molecule used in converting molecule information
     *
     * @return
     */
    public IAtomContainer setMolecule() {
        return itsMolecule;
    }

    /**
     * get ATDL list
     *
     * @return
     */
    public List<Atdl> getAtdlList() {
        return itsAtdlList;
    }

    /**
     * set ATDL list
     *
     * @param theAtdlList
     */
    public void setAtdlList(List<Atdl> theAtdlList) {
        this.itsAtdlList = theAtdlList;
    }

    /**
     * get ATDL list used in converting ATDL list variable information
     *
     * @return
     */
    public List<Atdl> setAtdlList() {
        return itsAtdlList;
    }

    public TopologicalDistanceMatrix getTopologicalDistanceMatrix() {
        return itsTopologicalDistanceMatrix;
    }

    public void setTopologicalDistanceMatrix(TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.itsTopologicalDistanceMatrix = theTopologicalDistanceMatrix;
    }

    public IRingSet getRingSet() {
        return itsRingSet;
    }

    public void setRingSet(IRingSet theRingSet) {
        this.itsRingSet = theRingSet;
    }

    public IRingSet getAromaticRingSet() {
        return itsAromaticRingSet;
    }

    public void setAromaticRingSet(IRingSet theAromaticRingSet) {
        this.itsAromaticRingSet = theAromaticRingSet;
    }

    public void inputAtdl(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule, IRingSet theRingSet) {
        List<Atdl> theAtdlList = this.generateAtdlList(theTotalMolecule, theRingSet);

        if (theAtdlList.isEmpty()) {
            theTotalMolecule.setProperty(AtdlGenerator.IS_CALCULATED_ATDL, AtdlGenerator.NOT_CALCULATED_ATDL);
            return;
        } else {
            for (int vi = 0, vEnd = theTotalMolecule.getAtomCount(); vi < vEnd; vi++) {
                theTotalMolecule.getAtom(vi).setProperty(Atdl.ATDL_KEY, theAtdlList.get(vi % theUnitMolecule.getAtomCount()));
            }

            theTotalMolecule.setProperty(AtdlGenerator.IS_CALCULATED_ATDL, AtdlGenerator.CALCULATED_ATDL);
        }
    }

    public void inputAtdl(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        List<Atdl> theAtdlList = this.generateAtdlList(theUnitMolecule, theTopologicalDistanceMatrix);

        if (theAtdlList.isEmpty()) {
            theTotalMolecule.setProperty(AtdlGenerator.IS_CALCULATED_ATDL, AtdlGenerator.NOT_CALCULATED_ATDL);
            return;
        } else {
            for (int vi = 0, vEnd = theTotalMolecule.getAtomCount(); vi < vEnd; vi++) {
                theTotalMolecule.getAtom(vi).setProperty(Atdl.ATDL_KEY, theAtdlList.get(vi % theUnitMolecule.getAtomCount()));
            }

            theTotalMolecule.setProperty(AtdlGenerator.IS_CALCULATED_ATDL, AtdlGenerator.CALCULATED_ATDL);
        }
    }

    public void inputAtdl(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.inputAtdl(theMolecule, theMolecule, theTopologicalDistanceMatrix);
    }

    /**
     * This function is used to input ATDL list property in molecule variable
     *
     * @param theMolecule
     */
    public void inputAtdl(IAtomContainer theMolecule) {
        this.inputAtdl(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }

    public void inputAtdl(IAtomContainer theTotalMolecule, IAtomContainer theUnitMolecule) {
        this.inputAtdl(theTotalMolecule, theUnitMolecule, new TopologicalDistanceMatrix(theUnitMolecule));
    }

    public List<Atdl> generateAtdlList(IAtomContainer theMolecule, IRingSet theRingSet) {
        this.itsMolecule = theMolecule;
        this.itsAtdlList = new ArrayList<>();

        RingPerception theRingPerception = new RingPerception();
        this.itsRingSet = theRingSet;
        this.itsAromaticRingSet = theRingPerception.recognizeAromaticRing(theMolecule, this.itsRingSet);

        return this.__generateAtdlList();
    }

    public List<Atdl> generateAtdlList(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.itsMolecule = theMolecule;
        this.itsAtdlList = new ArrayList<>();
        this.itsTopologicalDistanceMatrix = theTopologicalDistanceMatrix;

        RingPerception theRingPerception = new RingPerception();
        this.itsRingSet = theRingPerception.recognizeRing(theMolecule, theTopologicalDistanceMatrix);
        this.itsAromaticRingSet = theRingPerception.recognizeAromaticRing(theMolecule, this.itsRingSet);

        return this.__generateAtdlList();
    }
    
    public List<Atdl> generateAtdlList(Protein theProtein, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.itsMolecule = theProtein.getMolecule();
        this.itsAtdlList = new ArrayList<>();
        this.itsTopologicalDistanceMatrix = theTopologicalDistanceMatrix;

        RingPerception theRingPerception = new RingPerception();
        this.itsRingSet = theRingPerception.recognizeRing(theProtein);
        this.itsAromaticRingSet = theRingPerception.recognizeAromaticRing(this.itsMolecule, this.itsRingSet);

        return this.__generateAtdlList();
    }

    /**
     * This function is used to get ATDL list in molecule
     *
     * @param theMolecule
     * @return
     */
    public List<Atdl> generateAtdlList(IAtomContainer theMolecule) {
        return this.generateAtdlList(theMolecule, new TopologicalDistanceMatrix(theMolecule));
    }

    /**
     * This function is used to set ATDL list in class variable(itsAtdlList)
     *
     * @return
     */
    private List<Atdl> __generateAtdlList() {
        for (IAtom theAtom : this.getMolecule().atoms()) {
            Atdl theAtdl = this.__generateAtdl(theAtom);

            this.itsAtdlList.add(theAtdl);
            theAtom.setProperty(Atdl.ATDL_KEY, theAtdl);
        }

        return this.itsAtdlList;
    }

    /**
     * This function is used to generate atdl at one atom
     *
     * @param theAtom
     * @return
     */
    private Atdl __generateAtdl(IAtom theAtom) {
        Atdl theAtdl = new Atdl();

        theAtdl.setSymbol(String.format("%2s", theAtom.getSymbol()));
        theAtdl.setNumberOfConnectedAtom(this.getMolecule().getConnectedAtomsCount(theAtom));
        theAtdl.setRingIndicator(this.__getMaximumRingSize(theAtom));
        theAtdl.setAromaticIndicator(this.__getAromaticIndicator(theAtom));

        return theAtdl;
    }

    /**
     * This function is used to set whether this atom is contained in aromatic
     * ring
     *
     * @param theAtom
     * @return
     */
    private Integer __getAromaticIndicator(IAtom theAtom) {
        if (this.__containAtomInAromaticRing(theAtom)) {
            return this.EXISTED_AROMATIC_BOND;
        }

        return this.NOT_EXISTED_AROMATIC_BOND;
    }

    /**
     * This function is used to get maximum ring size contained in atom
     *
     * @param theAtom
     * @return
     */
    private Integer __getMaximumRingSize(IAtom theAtom) {
        Integer theMaximumRingSize = 0;

        if (this.__containAtomInAromaticRing(theAtom)) {
            for (IAtomContainer theRing : this.itsAromaticRingSet.atomContainers()) {
                if (theRing.contains(theAtom) && theMaximumRingSize < theRing.getAtomCount()) {
                    if (theRing.getAtomCount() < 10) {
                        theMaximumRingSize = theRing.getAtomCount();
                    }
                }
            }
        } else {
            for (IAtomContainer theRing : this.itsRingSet.atomContainers()) {
                if (theRing.getAtomCount() < 10 && theRing.contains(theAtom) && theMaximumRingSize < theRing.getAtomCount()) {
                    theMaximumRingSize = theRing.getAtomCount();
                }
            }
        }

        return theMaximumRingSize;
    }

    /**
     * Check whether the atom is contained in aromatic ring
     *
     * @param theAtom
     * @return
     */
    private Boolean __containAtomInAromaticRing(IAtom theAtom) {
        for (IAtomContainer theRing : this.itsAromaticRingSet.atomContainers()) {
            if (theRing.contains(theAtom)) {
                return true;
            }
        }

        return false;
    }
}
