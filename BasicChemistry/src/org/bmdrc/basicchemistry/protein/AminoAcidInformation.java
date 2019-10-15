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
import java.util.List;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.protein.interfaces.IAminoAcidName;
import org.bmdrc.basicchemistry.reader.PDBReader;
import org.bmdrc.ui.abstracts.Constant;
//import org.bmdrc.sbff.tool.MM3AtomTypeGenerator;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class AminoAcidInformation implements IAminoAcidName, Serializable {

    private static final long serialVersionUID = 4843857102977647171L;

    private enum BackboneIndex {
        BACK_BONE_NITROGEN,
        BACK_BONE_ALPHA_CARBON,
        BACK_BONE_CARBON_IN_AMIDE,
        BACK_BONE_OXYGEN,
        BACK_BONE_HYDROGEN_BOUNDED_NITROGEN;
    }

    private AminoAcid itsAminoAcid;
    //constant String variable
    public static final String ATOM_NAME = "Atom Name";
    public static final String BACK_BONE_NITROGEN = "N";
    public static final String BACK_BONE_CARBON_IN_AMIDE = "C";
    public static final String BACK_BONE_OXYGEN = "O";
    public static final String BACK_BOND_ALPHA_CARBON = "CA";
    public final String TERMINATION = "T";
    public static final String FIRST = "1";
    public static final String SECOND = "2";
    public static final String THIRD = "3";
    //constant Integer variable
    private final Integer POSITIVE_CHARGE = 1;
    private final Integer NEGATIVE_CHARGE = -1;

    public AminoAcid getAminoAcid() {
        return itsAminoAcid;
    }

    public void setAminoAcid(AminoAcid theAminoAcid) {
        this.itsAminoAcid = theAminoAcid;
    }

    public AminoAcid setAminoAcid() {
        return itsAminoAcid;
    }

    public void inputAminoAcidBondInformation(AminoAcid theAminoAcid) {
        this.itsAminoAcid = theAminoAcid;

        this.inputAminoAcidBondInformation();
    }

    public void inputAminoAcidBondInformation() {
        this.__inputBackBondBondInformation();
        this.__inputHydrogenBond();

        try {
            switch (this.itsAminoAcid.getName()) {
                case AminoAcidInformation.ARGININE_THREE_LETTER:
                    this.__inputArginineBondInformation();
                    break;
                case AminoAcidInformation.HISTIDINE_THREE_LETTER:
                    this.__inputHistidineBondInformation();
                    break;
                case AminoAcidInformation.LYSINE_THREE_LETTER:
                    this.__inputLysineBondInformation();
                    break;
                case AminoAcidInformation.ASPARTIC_ACID_THREE_LETTER:
                    this.__inputAsparticAcidBondInformation();
                    break;
                case AminoAcidInformation.GLUTAMIC_ACID_THREE_LETTER:
                    this.__inputGlutamicAcidBondInformation();
                    break;
                case AminoAcidInformation.SERINE_THREE_LETTER:
                    this.__inputSerineBondInformation();
                    break;
                case AminoAcidInformation.THREONINE_THREE_LETTER:
                    this.__inputThreonineBondInformation();
                    break;
                case AminoAcidInformation.ASPARAGINE_THREE_LETTER:
                    this.__inputAsparagineBondInformation();
                    break;
                case AminoAcidInformation.GLUTAMINE_THREE_LETTER:
                    this.__inputGlutamineBondInformation();
                    break;
                case AminoAcidInformation.CYSTEINE_THREE_LETTER:
                    this.__inputCysteinBondInformation();
                    break;
                case AminoAcidInformation.SELENOCYSTEINE_THREE_LETTER:
                    this.__inputSelenocysteineBondInformation();
                    break;
                case AminoAcidInformation.GLYCINE_THREE_LETTER:
                    this.__inputGlycineBondInformation();
                    break;
                case AminoAcidInformation.PROLINE_THREE_LETTER:
                    this.__inputProlineBondInformation();
                    break;
                case AminoAcidInformation.ALANINE_THREE_LETTER:
                    this.__inputAlanineBondInformation();
                    break;
                case AminoAcidInformation.VALINE_THREE_LETTER:
                    this.__inputValineBondInformation();
                    break;
                case AminoAcidInformation.ISOLEUCINE_THREE_LETTER:
                    this.__inputIsoLeucineBondInformation();
                    break;
                case AminoAcidInformation.LEUCINE_THREE_LETTER:
                    this.__inputLeucineBondInformation();
                    break;
                case AminoAcidInformation.METHIONINE_THREE_LETTER:
                    this.__inputMethionineBondInformation();
                    break;
                case AminoAcidInformation.PHENYLALANINE_THREE_LETTER:
                    this.__inputPhenylalanineBondInformation();
                    break;
                case AminoAcidInformation.TYROSINE_THREE_LETTER:
                    this.__inputTyrosineBondInformation();
                    break;
                case AminoAcidInformation.TRYPTOPHAN_THREE_LETTER:
                    this.__inputTryptophanBondInformation();
                    break;
                default:
//                    System.err.println("Un-Natural Amino Acid!! -> " + this.itsAminoAcid.getName());
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println(this.itsAminoAcid.getName() + this.itsAminoAcid.getSerial() + " is Error!!");
            ex.printStackTrace();
        }
    }

    private boolean __inputHydrogenBond() {
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();
        boolean theJugment = false;

        for (IAtom theAtom : theMolecule.atoms()) {
            if (theAtom.getSymbol().equals("D")) {
                theAtom.setAtomicNumber(AtomInformation.Hydrogen.ATOM_NUMBER);
            }
                      
            if (theAtom.getAtomicNumber() != null && theAtom.getAtomicNumber().equals(AtomInformation.Hydrogen.ATOM_NUMBER)) {
                if (this.__isHydrogenBoundedBackboneNitrogen(theAtom)
                        && theMolecule.getBond(theMolecule.getAtom(this.__getAtomIndex(AminoAcidInformation.BACK_BONE_NITROGEN)), theAtom) == null) {
                    theMolecule.addBond(new Bond(theMolecule.getAtom(this.__getAtomIndex(AminoAcidInformation.BACK_BONE_NITROGEN)),
                            theAtom, Order.SINGLE));
                } else if (this.__isHydrogenBoundedBackboneCarbon(theAtom)
                        && theMolecule.getBond(theMolecule.getAtom(this.__getAtomIndex(AminoAcidInformation.BACK_BONE_CARBON_IN_AMIDE)),
                                theAtom) == null) {
                    theMolecule.addBond(new Bond(theMolecule.getAtom(this.__getAtomIndex(AminoAcidInformation.BACK_BONE_CARBON_IN_AMIDE)),
                            theAtom, Order.SINGLE));
                }

                theJugment = true;
            }
        }

        return theJugment;
    }

    private boolean __isHydrogenBoundedBackboneCarbon(IAtom theAtom) {
        String theName = theAtom.getProperty(PDBReader.ATOM_NAME).toString();

        return theName.length() > 1 && (theName.substring(0, 2).equals("HC") || theName.contains("HXT"));
    }

    private boolean __isHydrogenBoundedBackboneNitrogen(IAtom theAtom) {
        String theName = theAtom.getProperty(PDBReader.ATOM_NAME).toString();

        return theName.length() == 1 || theName.substring(1, 2).matches("\\d") || theName.substring(0, 2).equals("HN") || theName.substring(0, 2).equals("HT");
    }

    private void __inputBackBondBondInformation() {
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();
        Integer theBackboneAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBackboneNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL);
        Integer theBackboneCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL);
        Integer theBackboneOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL);

        this.__addBond(theBackboneAlphaCarbonIndex, theBackboneCarbonIndex, Order.SINGLE);
        this.__addBond(theBackboneCarbonIndex, theBackboneOxygenIndex, Order.DOUBLE);
        this.__addBond(theBackboneNitrogenIndex, theBackboneAlphaCarbonIndex, Order.SINGLE);
    }

    private Integer __getAtomIndex(String theAtomSynonum) {
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();

        for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
            if (this.__isMatchedAtom(theMolecule.getAtom(ai), theAtomSynonum)) {
                return ai;
            }
        }

        return -1;
    }

    private boolean __isMatchedAtom(IAtom theAtom, String theAtomSynonum) {
        String theAtomName = theAtom.getProperty(AminoAcidInformation.ATOM_NAME).toString();

        if (theAtomName.length() != theAtomSynonum.length()) {
            return false;
        } else if (theAtomName.equals(theAtomSynonum)) {
            return true;
        } else if (theAtomName.length() > 3 && theAtomName.equals(theAtomSynonum.substring(3) + theAtomSynonum.substring(0, 3))) {
            return true;
        }

        return false;
    }

    private void __inputArginineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA);
        Integer theEpsilonNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL + Constant.EPSILON);
        Integer theZetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ZETA);
        Integer theFirstEtaNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL + Constant.ETA + this.FIRST);
        Integer theSecondEtaNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL + Constant.ETA + this.SECOND);
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theDeltaCarbonIndex, Order.SINGLE);
        this.__addBond(theDeltaCarbonIndex, theEpsilonNitrogenIndex, Order.SINGLE);
        this.__addBond(theEpsilonNitrogenIndex, theZetaCarbonIndex, Order.SINGLE);
        this.__addBond(theZetaCarbonIndex, theFirstEtaNitrogenIndex, Order.SINGLE);
        this.__addBond(theZetaCarbonIndex, theSecondEtaNitrogenIndex, Order.DOUBLE);

        if (theSecondEtaNitrogenIndex >= 0) {
            theMolecule.getAtom(theSecondEtaNitrogenIndex).setFormalCharge(this.POSITIVE_CHARGE);
        }

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA);
            List<Integer> theHydrogenIndexListBoundedEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON);
            List<Integer> theHydrogenIndexListBoundedZetaCarbon = this.__getHydrogenAtomIndexList(Constant.ZETA);
            List<Integer> theHydrogenIndexListBoundedFirstEtaNitrogen = this.__getHydrogenAtomIndexList(Constant.ETA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondEtaNitrogen = this.__getHydrogenAtomIndexList(Constant.ETA + this.SECOND);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theDeltaCarbonIndex, theHydrogenIndexListBoundedDeltaCarbon);

            if (!theHydrogenIndexListBoundedEpsilonCarbon.isEmpty()) {
                this.__addBondByHydrogen(theEpsilonNitrogenIndex, theHydrogenIndexListBoundedEpsilonCarbon);
            } else if (theEpsilonNitrogenIndex >= 0 && theZetaCarbonIndex >= 0) {
                theMolecule.getBond(theMolecule.getAtom(theEpsilonNitrogenIndex), theMolecule.getAtom(theZetaCarbonIndex)).setOrder(Order.DOUBLE);
            }

            this.__addBondByHydrogen(theZetaCarbonIndex, theHydrogenIndexListBoundedZetaCarbon);
            this.__addBondByHydrogen(theFirstEtaNitrogenIndex, theHydrogenIndexListBoundedFirstEtaNitrogen);
            this.__addBondByHydrogen(theSecondEtaNitrogenIndex, theHydrogenIndexListBoundedSecondEtaNitrogen);
        }
    }

    private void __inputHistidineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theDeltaNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL + Constant.DELTA + this.FIRST);
        Integer theDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.SECOND);
        Integer theEpsilonCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.EPSILON + this.FIRST);
        Integer theEpsilonNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL + Constant.EPSILON + this.SECOND);
        IAtomContainer theMolecule = this.getAminoAcid().setMolecule();

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theDeltaNitrogenIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theDeltaCarbonIndex, Order.DOUBLE);
        this.__addBond(theDeltaNitrogenIndex, theEpsilonCarbonIndex, Order.SINGLE);
        this.__addBond(theDeltaCarbonIndex, theEpsilonNitrogenIndex, Order.SINGLE);
        this.__addBond(theEpsilonCarbonIndex, theEpsilonNitrogenIndex, Order.DOUBLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedDeltaNitrogen = this.__getHydrogenAtomIndexList(Constant.DELTA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.SECOND);
            List<Integer> theHydrogenIndexListBoundedEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedEpsilonNitrogen = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.SECOND);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theDeltaNitrogenIndex, theHydrogenIndexListBoundedDeltaNitrogen);
            this.__addBondByHydrogen(theDeltaCarbonIndex, theHydrogenIndexListBoundedDeltaCarbon);
            this.__addBondByHydrogen(theEpsilonCarbonIndex, theHydrogenIndexListBoundedEpsilonCarbon);
            this.__addBondByHydrogen(theEpsilonNitrogenIndex, theHydrogenIndexListBoundedEpsilonNitrogen);
        }
    }

    private void __inputLysineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA);
        Integer theEpsilonCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.EPSILON);
        Integer theZetaNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL + Constant.ZETA);
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theDeltaCarbonIndex, Order.SINGLE);
        this.__addBond(theDeltaCarbonIndex, theEpsilonCarbonIndex, Order.SINGLE);
        this.__addBond(theEpsilonCarbonIndex, theZetaNitrogenIndex, Order.SINGLE);

        if (theZetaNitrogenIndex >= 0) {
            theMolecule.getAtom(theZetaNitrogenIndex).setFormalCharge(this.POSITIVE_CHARGE);
        }

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA);
            List<Integer> theHydrogenIndexListBoundedEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON);
            List<Integer> theHydrogenIndexListBoundedZetaNitrogen = this.__getHydrogenAtomIndexList(Constant.ZETA);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theDeltaCarbonIndex, theHydrogenIndexListBoundedDeltaCarbon);
            this.__addBondByHydrogen(theEpsilonCarbonIndex, theHydrogenIndexListBoundedEpsilonCarbon);
            this.__addBondByHydrogen(theZetaNitrogenIndex, theHydrogenIndexListBoundedZetaNitrogen);
        }
    }

    private void __inputAsparticAcidBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theFirstDeltaOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL + Constant.DELTA + this.FIRST);
        Integer theSecondDeltaOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL + Constant.DELTA + this.SECOND);
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theFirstDeltaOxygenIndex, Order.DOUBLE);
        this.__addBond(theGammaCarbonIndex, theSecondDeltaOxygenIndex, Order.SINGLE);

        if (theSecondDeltaOxygenIndex >= 0) {
            theMolecule.getAtom(theSecondDeltaOxygenIndex).setFormalCharge(this.NEGATIVE_CHARGE);
        }
        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedFirstDeltaOxygen = this.__getHydrogenAtomIndexList(Constant.DELTA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondDeltaOxygen = this.__getHydrogenAtomIndexList(Constant.DELTA + this.SECOND);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theFirstDeltaOxygenIndex, theHydrogenIndexListBoundedFirstDeltaOxygen);
            this.__addBondByHydrogen(theSecondDeltaOxygenIndex, theHydrogenIndexListBoundedSecondDeltaOxygen);
        }
    }

    private void __inputGlutamicAcidBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA);
        Integer theFirstEpsilonOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL + Constant.EPSILON + this.FIRST);
        Integer theSecondEpsilonOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL + Constant.EPSILON + this.SECOND);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theDeltaCarbonIndex, Order.SINGLE);
        this.__addBond(theDeltaCarbonIndex, theFirstEpsilonOxygenIndex, Order.DOUBLE);
        this.__addBond(theDeltaCarbonIndex, theSecondEpsilonOxygenIndex, Order.SINGLE);

        if (theSecondEpsilonOxygenIndex >= 0) {
            this.itsAminoAcid.getMolecule().getAtom(theSecondEpsilonOxygenIndex).setFormalCharge(this.NEGATIVE_CHARGE);
        }

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA);
            List<Integer> theHydrogenIndexListBoundedFirstEpsilonOxygen = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondEpsilonOxygen = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.SECOND);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theDeltaCarbonIndex, theHydrogenIndexListBoundedDeltaCarbon);
            this.__addBondByHydrogen(theFirstEpsilonOxygenIndex, theHydrogenIndexListBoundedFirstEpsilonOxygen);
            this.__addBondByHydrogen(theSecondEpsilonOxygenIndex, theHydrogenIndexListBoundedSecondEpsilonOxygen);
        }
    }

    private void __inputSerineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL + Constant.GAMMA);
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaOxygenIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaOxygen = this.__getHydrogenAtomIndexList(Constant.GAMMA);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaOxygenIndex, theHydrogenIndexListBoundedGammaOxygen);
        }
    }

    private void __inputThreonineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL + Constant.GAMMA + this.FIRST);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA + this.SECOND);
        IAtomContainer theMolecule = this.getAminoAcid().setMolecule();

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaOxygenIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaOxygen = this.__getHydrogenAtomIndexList(Constant.GAMMA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA + this.SECOND);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaOxygenIndex, theHydrogenIndexListBoundedGammaOxygen);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
        }
    }

    private void __inputAsparagineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theDeltaOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL + Constant.DELTA + this.FIRST);
        Integer theDeltaNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL + Constant.DELTA + this.SECOND);
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theDeltaOxygenIndex, Order.DOUBLE);
        this.__addBond(theGammaCarbonIndex, theDeltaNitrogenIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedDeltaNitrogen = this.__getHydrogenAtomIndexList(Constant.DELTA + this.SECOND);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theDeltaNitrogenIndex, theHydrogenIndexListBoundedDeltaNitrogen);
        }
    }

    private void __inputGlutamineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA);
        Integer theEpsilonOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL + Constant.EPSILON + this.FIRST);
        Integer theEpsilonNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL + Constant.EPSILON + this.SECOND);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theDeltaCarbonIndex, Order.SINGLE);
        this.__addBond(theDeltaCarbonIndex, theEpsilonOxygenIndex, Order.DOUBLE);
        this.__addBond(theDeltaCarbonIndex, theEpsilonNitrogenIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA);
            List<Integer> theHydrogenIndexListBoundedEpsilonNitrogen = this.__getHydrogenAtomIndexList(Constant.EPSILON);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theDeltaCarbonIndex, theHydrogenIndexListBoundedDeltaCarbon);
            this.__addBondByHydrogen(theEpsilonNitrogenIndex, theHydrogenIndexListBoundedEpsilonNitrogen);
        }
    }

    private void __inputCysteinBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaSulfurIndex = this.__getAtomIndex(AtomInformation.Sulfur.SYMBOL + Constant.GAMMA);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaSulfurIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaSulfur = this.__getHydrogenAtomIndexList(Constant.GAMMA);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaSulfurIndex, theHydrogenIndexListBoundedGammaSulfur);
        }
    }

    private void __inputSelenocysteineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaSelenIndex = this.__getAtomIndex(AtomInformation.Selenium.SYMBOL.toUpperCase());
        IAtomContainer theMolecule = this.getAminoAcid().setMolecule();

        theMolecule.addBond(new Bond(theMolecule.getAtom(theAlphaCarbonIndex), theMolecule.getAtom(theBetaCarbonIndex), Order.SINGLE));
        theMolecule.addBond(new Bond(theMolecule.getAtom(theBetaCarbonIndex), theMolecule.getAtom(theGammaSelenIndex), Order.SINGLE));
    }

    private void __inputGlycineBondInformation() throws ArrayIndexOutOfBoundsException {
        if (this.__inputHydrogenBond()) {
            Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
        }
    }

    private void __inputProlineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theDeltaCarbonIndex, Order.SINGLE);
        this.__addBond(theDeltaCarbonIndex, BackboneIndex.BACK_BONE_NITROGEN.ordinal(), Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theDeltaCarbonIndex, theHydrogenIndexListBoundedDeltaCarbon);
        }
    }

    private void __inputAlanineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        IAtomContainer theMolecule = this.getAminoAcid().getMolecule();

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
        }
    }

    private void __inputValineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theFirstGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA + this.FIRST);
        Integer theSecondGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA + this.SECOND);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theFirstGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theSecondGammaCarbonIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedFirstGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA + this.SECOND);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theFirstGammaCarbonIndex, theHydrogenIndexListBoundedFirstGammaCarbon);
            this.__addBondByHydrogen(theSecondGammaCarbonIndex, theHydrogenIndexListBoundedSecondGammaCarbon);
        }
    }

    private void __inputIsoLeucineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theFirstGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA + this.FIRST);
        Integer theSecondGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA + this.SECOND);
        Integer theFirstDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.FIRST);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theFirstGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theSecondGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theFirstGammaCarbonIndex, theFirstDeltaCarbonIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedFirstGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA + this.SECOND);
            List<Integer> theHydrogenIndexListBoundedFirstDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.FIRST);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theFirstGammaCarbonIndex, theHydrogenIndexListBoundedFirstGammaCarbon);
            this.__addBondByHydrogen(theSecondGammaCarbonIndex, theHydrogenIndexListBoundedSecondGammaCarbon);
            this.__addBondByHydrogen(theFirstDeltaCarbonIndex, theHydrogenIndexListBoundedFirstDeltaCarbon);
        }
    }

    private void __inputLeucineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theFirstDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.FIRST);
        Integer theSecondDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.SECOND);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theFirstDeltaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theSecondDeltaCarbonIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedFirstDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondGammaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.SECOND);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theFirstDeltaCarbonIndex, theHydrogenIndexListBoundedFirstDeltaCarbon);
            this.__addBondByHydrogen(theSecondDeltaCarbonIndex, theHydrogenIndexListBoundedSecondGammaCarbon);
        }
    }

    private void __inputMethionineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theDeltaSulfurIndex = this.__getAtomIndex(AtomInformation.Sulfur.SYMBOL + Constant.DELTA);
        Integer theEpsilonCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.EPSILON);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theDeltaSulfurIndex, Order.SINGLE);
        this.__addBond(theDeltaSulfurIndex, theEpsilonCarbonIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedDeltaSulfur = this.__getHydrogenAtomIndexList(Constant.DELTA);
            List<Integer> theHydrogenIndexListBoundedEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theDeltaSulfurIndex, theHydrogenIndexListBoundedDeltaSulfur);
            this.__addBondByHydrogen(theEpsilonCarbonIndex, theHydrogenIndexListBoundedEpsilonCarbon);
        }
    }

    private void __inputPhenylalanineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theFirstDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.FIRST);
        Integer theSecondDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.SECOND);
        Integer theFirstEpsilonCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.EPSILON + this.FIRST);
        Integer theSecondEpsilonCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.EPSILON + this.SECOND);
        Integer theZetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ZETA);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theFirstDeltaCarbonIndex, Order.DOUBLE);
        this.__addBond(theGammaCarbonIndex, theSecondDeltaCarbonIndex, Order.SINGLE);
        this.__addBond(theFirstDeltaCarbonIndex, theFirstEpsilonCarbonIndex, Order.SINGLE);
        this.__addBond(theSecondDeltaCarbonIndex, theSecondEpsilonCarbonIndex, Order.DOUBLE);
        this.__addBond(theFirstEpsilonCarbonIndex, theZetaCarbonIndex, Order.DOUBLE);
        this.__addBond(theSecondEpsilonCarbonIndex, theZetaCarbonIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedFirstDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.SECOND);
            List<Integer> theHydrogenIndexListBoundedFirstEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.SECOND);
            List<Integer> theHydrogenIndexListBoundedZetaCarbon = this.__getHydrogenAtomIndexList(Constant.ZETA);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theFirstDeltaCarbonIndex, theHydrogenIndexListBoundedFirstDeltaCarbon);
            this.__addBondByHydrogen(theSecondDeltaCarbonIndex, theHydrogenIndexListBoundedSecondDeltaCarbon);
            this.__addBondByHydrogen(theFirstEpsilonCarbonIndex, theHydrogenIndexListBoundedFirstEpsilonCarbon);
            this.__addBondByHydrogen(theSecondEpsilonCarbonIndex, theHydrogenIndexListBoundedSecondEpsilonCarbon);
            this.__addBondByHydrogen(theZetaCarbonIndex, theHydrogenIndexListBoundedZetaCarbon);
        }
    }

    private void __inputTyrosineBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theFirstDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.FIRST);
        Integer theSecondDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.SECOND);
        Integer theFirstEpsilonCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.EPSILON + this.FIRST);
        Integer theSecondEpsilonCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.EPSILON + this.SECOND);
        Integer theZetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ZETA);
        Integer theEtaOxygenIndex = this.__getAtomIndex(AtomInformation.Oxygen.SYMBOL + Constant.ETA);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theFirstDeltaCarbonIndex, Order.DOUBLE);
        this.__addBond(theGammaCarbonIndex, theSecondDeltaCarbonIndex, Order.SINGLE);
        this.__addBond(theFirstDeltaCarbonIndex, theFirstEpsilonCarbonIndex, Order.SINGLE);
        this.__addBond(theSecondDeltaCarbonIndex, theSecondEpsilonCarbonIndex, Order.DOUBLE);
        this.__addBond(theFirstEpsilonCarbonIndex, theZetaCarbonIndex, Order.DOUBLE);
        this.__addBond(theSecondEpsilonCarbonIndex, theZetaCarbonIndex, Order.SINGLE);
        this.__addBond(theZetaCarbonIndex, theEtaOxygenIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedFirstDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.SECOND);
            List<Integer> theHydrogenIndexListBoundedFirstEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.SECOND);
            List<Integer> theHydrogenIndexListBoundedEtaOxygen = this.__getHydrogenAtomIndexList(Constant.ETA + Constant.ETA);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theFirstDeltaCarbonIndex, theHydrogenIndexListBoundedFirstDeltaCarbon);
            this.__addBondByHydrogen(theSecondDeltaCarbonIndex, theHydrogenIndexListBoundedSecondDeltaCarbon);
            this.__addBondByHydrogen(theFirstEpsilonCarbonIndex, theHydrogenIndexListBoundedFirstEpsilonCarbon);
            this.__addBondByHydrogen(theSecondEpsilonCarbonIndex, theHydrogenIndexListBoundedSecondEpsilonCarbon);
            this.__addBondByHydrogen(theEtaOxygenIndex, theHydrogenIndexListBoundedEtaOxygen);
        }
    }

    private void __inputTryptophanBondInformation() throws ArrayIndexOutOfBoundsException {
        Integer theAlphaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        Integer theBetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.BETA);
        Integer theGammaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        Integer theFirstDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.FIRST);
        Integer theSecondDeltaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.DELTA + this.SECOND);
        Integer theEpsilonNitrogenIndex = this.__getAtomIndex(AtomInformation.Nitrogen.SYMBOL + Constant.EPSILON + this.FIRST);
        Integer theFirstEpsilonCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.EPSILON + this.SECOND);
        Integer theSecondEpsilonCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.EPSILON + this.THIRD);
        Integer theFirstZetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ZETA + this.SECOND);
        Integer theSecondZetaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ZETA + this.THIRD);
        Integer theEtaCarbonIndex = this.__getAtomIndex(AtomInformation.Carbon.SYMBOL + Constant.ETA + this.SECOND);

        this.__addBond(theAlphaCarbonIndex, theBetaCarbonIndex, Order.SINGLE);
        this.__addBond(theBetaCarbonIndex, theGammaCarbonIndex, Order.SINGLE);
        this.__addBond(theGammaCarbonIndex, theFirstDeltaCarbonIndex, Order.DOUBLE);
        this.__addBond(theGammaCarbonIndex, theSecondDeltaCarbonIndex, Order.SINGLE);
        this.__addBond(theFirstDeltaCarbonIndex, theEpsilonNitrogenIndex, Order.SINGLE);
        this.__addBond(theSecondDeltaCarbonIndex, theFirstEpsilonCarbonIndex, Order.DOUBLE);
        this.__addBond(theSecondDeltaCarbonIndex, theSecondEpsilonCarbonIndex, Order.SINGLE);
        this.__addBond(theEpsilonNitrogenIndex, theFirstEpsilonCarbonIndex, Order.SINGLE);
        this.__addBond(theFirstEpsilonCarbonIndex, theFirstZetaCarbonIndex, Order.SINGLE);
        this.__addBond(theSecondEpsilonCarbonIndex, theSecondZetaCarbonIndex, Order.DOUBLE);
        this.__addBond(theFirstZetaCarbonIndex, theEtaCarbonIndex, Order.DOUBLE);
        this.__addBond(theSecondZetaCarbonIndex, theEtaCarbonIndex, Order.SINGLE);

        if (this.__inputHydrogenBond()) {
            List<Integer> theHydrogenIndexListBoundedAlphaCarbon = this.__getHydrogenAtomIndexList(Constant.ALPHA);
            List<Integer> theHydrogenIndexListBoundedBetaCarbon = this.__getHydrogenAtomIndexList(Constant.BETA);
            List<Integer> theHydrogenIndexListBoundedGammaCarbon = this.__getHydrogenAtomIndexList(Constant.GAMMA);
            List<Integer> theHydrogenIndexListBoundedFirstDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedSecondDeltaCarbon = this.__getHydrogenAtomIndexList(Constant.DELTA + this.SECOND);
            List<Integer> theHydrogenIndexListBoundedEpsilonNitrogen = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.FIRST);
            List<Integer> theHydrogenIndexListBoundedFirstEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.SECOND);
            List<Integer> theHydrogenIndexListBoundedSecondEpsilonCarbon = this.__getHydrogenAtomIndexList(Constant.EPSILON + this.THIRD);
            List<Integer> theHydrogenIndexListBoundedFirstZetaCarbon = this.__getHydrogenAtomIndexList(Constant.ZETA + this.SECOND);
            List<Integer> theHydrogenIndexListBoundedSecondZetaCarbon = this.__getHydrogenAtomIndexList(Constant.ZETA + this.THIRD);
            List<Integer> theHydrogenIndexListBoundedEtaCarbon = this.__getHydrogenAtomIndexList(Constant.ETA + this.SECOND);

            this.__addBondByHydrogen(theAlphaCarbonIndex, theHydrogenIndexListBoundedAlphaCarbon);
            this.__addBondByHydrogen(theBetaCarbonIndex, theHydrogenIndexListBoundedBetaCarbon);
            this.__addBondByHydrogen(theGammaCarbonIndex, theHydrogenIndexListBoundedGammaCarbon);
            this.__addBondByHydrogen(theFirstDeltaCarbonIndex, theHydrogenIndexListBoundedFirstDeltaCarbon);
            this.__addBondByHydrogen(theSecondDeltaCarbonIndex, theHydrogenIndexListBoundedSecondDeltaCarbon);
            this.__addBondByHydrogen(theEpsilonNitrogenIndex, theHydrogenIndexListBoundedEpsilonNitrogen);
            this.__addBondByHydrogen(theFirstEpsilonCarbonIndex, theHydrogenIndexListBoundedFirstEpsilonCarbon);
            this.__addBondByHydrogen(theSecondEpsilonCarbonIndex, theHydrogenIndexListBoundedSecondEpsilonCarbon);
            this.__addBondByHydrogen(theFirstZetaCarbonIndex, theHydrogenIndexListBoundedFirstZetaCarbon);
            this.__addBondByHydrogen(theSecondZetaCarbonIndex, theHydrogenIndexListBoundedSecondZetaCarbon);
            this.__addBondByHydrogen(theEtaCarbonIndex, theHydrogenIndexListBoundedEtaCarbon);
        }
    }

    private void __addBond(Integer theFirstAtomIndex, Integer theSecondAtomIndex, Order theOrder) {
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();

        if (theFirstAtomIndex >= 0 && theSecondAtomIndex >= 0) {
            IAtom theFirstAtom = theMolecule.getAtom(theFirstAtomIndex);
            IAtom theSecondAtom = theMolecule.getAtom(theSecondAtomIndex);

            theMolecule.addBond(new Bond(theFirstAtom, theSecondAtom, theOrder));
        }
    }

    private void __addBondByHydrogen(Integer theFirstAtomIndex, List<Integer> theHydrogenAtomIndexList) {
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();

        if (theFirstAtomIndex >= 0) {
            IAtom theFirstAtom = theMolecule.getAtom(theFirstAtomIndex);

            for (Integer theSecondAtomIndex : theHydrogenAtomIndexList) {
                IAtom theSecondAtom = theMolecule.getAtom(theSecondAtomIndex);

                theMolecule.addBond(new Bond(theFirstAtom, theSecondAtom, Order.SINGLE));
            }
        }
    }

    private List<Integer> __getHydrogenAtomIndexList(String theAminoAcidName) {
        IAtomContainer theMolecule = this.itsAminoAcid.getMolecule();
        List<Integer> theHydrogenAtomIndexList = new ArrayList<>();

        for (int ai = 0, aEnd = theMolecule.getAtomCount(); ai < aEnd; ai++) {
            IAtom theAtom = theMolecule.getAtom(ai);

            if (theAtom.getAtomicNumber().equals(AtomInformation.Hydrogen.ATOM_NUMBER) && this.__isMatchedHydrogenAtom(theAtom, theAminoAcidName)) {
                theHydrogenAtomIndexList.add(ai);
            }
        }

        return theHydrogenAtomIndexList;
    }

    private boolean __isMatchedHydrogenAtom(IAtom theAtom, String theAminoAcidName) {
        String theAtomName = theAtom.getProperty(AminoAcidInformation.ATOM_NAME).toString().trim();

        if (theAminoAcidName.length() == 1) {
            boolean theJugment = theAtomName.substring(theAtomName.length() - 1).equals(theAminoAcidName);

            if (!theJugment && theAtomName.length() > 1) {
                return theAtomName.substring(theAtomName.length() - 2, theAtomName.length() - 1).equals(theAminoAcidName);
            }

            return theJugment;
        } else if (theAtomName.length() >= theAminoAcidName.length()) {
            return theAtomName.substring(theAtomName.length() - 2).equals(theAminoAcidName);
        }

        return false;
    }
}
