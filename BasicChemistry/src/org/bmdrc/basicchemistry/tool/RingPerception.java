/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.util.PathIncludeMatrix;
import org.bmdrc.basicchemistry.protein.AminoAcid;
import org.bmdrc.basicchemistry.protein.AminoAcidInformation;
import org.bmdrc.basicchemistry.protein.Chain;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Ring;
import org.openscience.cdk.RingSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;

/**
 * Ring perception<br>
 * Reference : http://www.pnas.org/content/106/41/17355.full.pdf+html
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 24
 */
public class RingPerception implements Serializable {

    private static final long serialVersionUID = 2952624407394611082L;

    /**
     * Candidate ring information internal class
     */
    private class CandidateRing {

        protected Integer itsRingSize;
        protected TwoDimensionList<IBond> itsPMatrix;
        protected TwoDimensionList<IBond> itsPCommaMatrix;

        public CandidateRing(Integer theRingSize, TwoDimensionList<IBond> thePMatrix, TwoDimensionList<IBond> thePCommaMatrix) {
            this.itsRingSize = theRingSize;
            this.itsPMatrix = thePMatrix;
            this.itsPCommaMatrix = thePCommaMatrix;
        }
    }

    private IAtomContainer itsMolecule;
    private PathIncludeMatrix itsPathIncludeMatrix;
    private TopologicalDistanceMatrix itsTopologicalDistanceMatrix;
    private List<CandidateRing> itsCandidateRingList;
    private TwoDimensionList<IBond> itsResultBond2dList;
    //constant Integer variable
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;

    /**
     * Constructor
     */
    public RingPerception() {
    }

    public IRingSet recognizeRing(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.__initializeVariable(theMolecule, theTopologicalDistanceMatrix);
        this.__initializeCandidateRingList();

        return this.__makeRingSet();
    }

    /**
     * recognize ring set
     *
     * @param theMolecule Template molecule
     * @return recognized ring set
     */
    public IRingSet recognizeRing(IAtomContainer theMolecule) {
        this.__initializeVariable(theMolecule, new TopologicalDistanceMatrix(theMolecule));
        this.__initializeCandidateRingList();

        return this.__makeRingSet();
    }

    public IRingSet recognizeAromaticRing(IAtomContainer theMolecule, IRingSet theRingSet) {
        IRingSet theResultRingSet = new RingSet();

        for (IAtomContainer theRing : theRingSet.atomContainers()) {
            IRing theTestRing = new Ring(theRing);

            if (this.__isAromaticRing(theTestRing, theMolecule)) {
                theResultRingSet.addAtomContainer(theRing);
            }
        }

        return theResultRingSet;
    }

    private boolean __isAromaticRing(IAtomContainer theRing, IAtomContainer theMolecule) {
        int theNumberOfPiElectron = 0;

        for (IAtom theRingAtom : theRing.atoms()) {

            if (theRingAtom.getAtomicNumber().equals(AtomInformation.Carbon.ATOM_NUMBER)) {
                if (this.__containDoubleBond(theRingAtom, theMolecule)) {
                    theNumberOfPiElectron += 1;
                } else {
                    return false;
                }
            } else if (theRingAtom.getAtomicNumber().equals(AtomInformation.Nitrogen.ATOM_NUMBER) || theRingAtom.getAtomicNumber().equals(AtomInformation.Oxygen.ATOM_NUMBER)) {
                if (this.__containDoubleBond(theRingAtom, theMolecule)) {
                    theNumberOfPiElectron += 1;
                } else {
                    theNumberOfPiElectron += 2;
                }
            }
        }

        return theNumberOfPiElectron % 4 == 2;
    }

    private boolean __containDoubleBond(IAtom theAtom, IAtomContainer theMolecule) {
        List<IBond> theConnectedBondList = theMolecule.getConnectedBondsList(theAtom);

        for (IBond theBond : theConnectedBondList) {
            if (theBond.getOrder().equals(Order.DOUBLE)) {
                return true;
            }
        }

        return false;
    }

    public IRingSet recognizeAromaticRing(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        IRingSet theRingSet = this.recognizeRing(theMolecule, theTopologicalDistanceMatrix);

        return this.recognizeAromaticRing(theMolecule, theRingSet);
    }

    /**
     * recognize aromatic ring set
     *
     * @param theMolecule template molecule
     * @return recognized aromatic ring set
     */
    public IRingSet recognizeAromaticRing(IAtomContainer theMolecule) {
        IRingSet theRingSet = this.recognizeRing(theMolecule);

        return this.recognizeAromaticRing(theMolecule, theRingSet);
    }

    private IRingSet __makeRingSet() {
        IRingSet theRingSet = new RingSet();

        for (CandidateRing theCandidateRing : this.itsCandidateRingList) {
            if (theCandidateRing.itsRingSize % 2 == 0) {
                theRingSet.add(this.__makeEvenOrderRingSet(theCandidateRing));
            } else {
                theRingSet.add(this.__makeOddOrderRingSet(theCandidateRing));
            }
        }

        return theRingSet;
    }

    private IRingSet __makeOddOrderRingSet(CandidateRing theCandidateRing) {
        IRingSet theRingSet = new RingSet();

        for (List<IBond> thePBondList : theCandidateRing.itsPMatrix) {
            for (List<IBond> thePCommaBondList : theCandidateRing.itsPCommaMatrix) {
                if (!this.__containSameBond(thePBondList, thePCommaBondList)) {
                    List<IBond> theTotalBondList = new ArrayList<>(thePBondList);

                    theTotalBondList.addAll(thePCommaBondList);
                    Collections.sort(theTotalBondList, this.__getBondComparator());

                    if (!this.itsResultBond2dList.contains(theTotalBondList)) {
                        theRingSet.addAtomContainer(this.__makeRing(theTotalBondList));
                        this.itsResultBond2dList.add(theTotalBondList);
                    }
                }
            }
        }

        return theRingSet;
    }

    private IRingSet __makeEvenOrderRingSet(CandidateRing theCandidateRing) {
        IRingSet theRingSet = new RingSet();
        int theBondListCount = theCandidateRing.itsPMatrix.size();

        for (int fi = 0; fi < theBondListCount - 1; fi++) {
            List<IBond> theFirstBondList = theCandidateRing.itsPMatrix.get(fi);

            for (int si = fi + 1; si < theBondListCount; si++) {
                List<IBond> theSecondBondList = theCandidateRing.itsPMatrix.get(si);

                if (!this.__containSameBond(theFirstBondList, theSecondBondList)) {
                    List<IBond> theTotalBondList = new ArrayList<>(theFirstBondList);

                    theTotalBondList.addAll(theSecondBondList);
                    Collections.sort(theTotalBondList, this.__getBondComparator());

                    if (!this.itsResultBond2dList.contains(theTotalBondList)) {
                        theRingSet.addAtomContainer(this.__makeRing(theTotalBondList));
                        this.itsResultBond2dList.add(theTotalBondList);
                    }
                }
            }
        }

        return theRingSet;
    }

    private IRing __makeRing(List<IBond> theBondList) {
        IRing theRing = new Ring();

        for (IBond theBond : theBondList) {
            IAtom theFirstAtom = theBond.getAtom(this.FIRST_INDEX);
            IAtom theSecondAtom = theBond.getAtom(this.SECOND_INDEX);

            if (!theRing.contains(theFirstAtom)) {
                theRing.addAtom(theFirstAtom);
            }

            if (!theRing.contains(theSecondAtom)) {
                theRing.addAtom(theSecondAtom);
            }

            theRing.addBond(theBond);
        }

        return theRing;
    }

    private boolean __containSameBond(List<IBond> theFirstBondList, List<IBond> theSecondBondList) {
        for (IBond theFirstBond : theFirstBondList) {
            for (IBond theSecondBond : theSecondBondList) {
                if (theFirstBond.equals(theSecondBond)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void __initializeCandidateRingList() {
        int theAtomCount = this.itsMolecule.getAtomCount();

        this.itsCandidateRingList = new ArrayList<>();

        for (int ri = 0; ri < theAtomCount; ri++) {
            for (int ci = 0; ci < theAtomCount; ci++) {
                if (!this.__isNotRingCondition(ri, ci)) {
                    int theRingSize = 0;

                    if (this.itsPathIncludeMatrix.getPCommaMatrix().get(ri, ci).isEmpty()) {
                        theRingSize = 2 * this.itsTopologicalDistanceMatrix.getDistance(ri, ci);
                    } else {
                        theRingSize = 2 * this.itsTopologicalDistanceMatrix.getDistance(ri, ci) + 1;
                    }

                    if (theRingSize <= 7) {
                        this.itsCandidateRingList.add(new CandidateRing(theRingSize, new TwoDimensionList<>(this.itsPathIncludeMatrix.getPMatrix().get(ri, ci)), new TwoDimensionList<>(this.itsPathIncludeMatrix.getPCommaMatrix().get(ri, ci))));
                    }
                }
            }
        }
    }

    private boolean __isNotRingCondition(int theFirstAtomNumber, int theSecondAtomNumber) {
        if (this.itsTopologicalDistanceMatrix.getDistance(theFirstAtomNumber, theSecondAtomNumber) == TopologicalDistanceMatrix.NON_CONNECTED_ATOM_PAIR) {
            return true;
        } else if (this.itsPathIncludeMatrix.getPMatrix().get(theFirstAtomNumber, theSecondAtomNumber).size() == 1
                && this.itsPathIncludeMatrix.getPCommaMatrix().get(theFirstAtomNumber, theSecondAtomNumber).isEmpty()) {
            return true;
        }

        return false;
    }

    private void __initializeVariable(IAtomContainer theMolecule, TopologicalDistanceMatrix theTopologicalDistanceMatrix) {
        this.itsMolecule = theMolecule;
        this.itsPathIncludeMatrix = new PathIncludeMatrix(theMolecule, theTopologicalDistanceMatrix);
        this.itsTopologicalDistanceMatrix = this.itsPathIncludeMatrix.getTopologicalDistanceMatrix();
        this.itsResultBond2dList = new TwoDimensionList<>();
    }

    private Comparator<IBond> __getBondComparator() {
        Comparator<IBond> theComparator = new Comparator<IBond>() {
            @Override
            public int compare(IBond o1, IBond o2) {
                return ((Integer) itsMolecule.getBondNumber(o1)).compareTo((Integer) itsMolecule.getBondNumber(o2));
            }
        };

        return theComparator;
    }

    public IRingSet recognizeRing(Protein theProtein) {
        IRingSet theRingSet = new RingSet();

        for (Chain theChain : theProtein) {
            theRingSet.add(this.__recognizeRing(theProtein, theChain));
        }

        return theRingSet;
    }

    private IRingSet __recognizeRing(Protein theProtein, Chain theChain) {
        IRingSet theRingSetInChain = new RingSet();

        for (AminoAcid theAminoAcid : theChain.getAminoAcidList()) {
            IRingSet theRingSet = this.__recongnizeRing(theAminoAcid);

            if (theRingSet != null) {
                theRingSetInChain.add(theRingSet);
            }
        }

        return theRingSetInChain;
    }

    private IRingSet __recongnizeRing(AminoAcid theAminoAcid) {
        switch (theAminoAcid.getName()) {
            case AminoAcidInformation.HISTIDINE_THREE_LETTER:
                return this.__makeRingSetUsingAminoAcid(this.__getRingInHistidine(theAminoAcid));
            case AminoAcidInformation.PROLINE_THREE_LETTER:
                return this.__makeRingSetUsingAminoAcid(this.__getRingInProline(theAminoAcid));
            case AminoAcidInformation.PHENYLALANINE_THREE_LETTER:
                return this.__makeRingSetUsingAminoAcid(this.__getRingInPhenylAlanine(theAminoAcid));
            case AminoAcidInformation.TYROSINE_THREE_LETTER:
                return this.__makeRingSetUsingAminoAcid(this.__getRingInTyrosin(theAminoAcid));
            case AminoAcidInformation.TRYPTOPHAN_THREE_LETTER:
                return this.__makeRingSetUsingAminoAcid(this.__getFiveMemberedRingInTryptophan(theAminoAcid), this.__getSixMemberedRingInTryptophan(theAminoAcid));
            default:
                return null;
        }
    }

    private IRingSet __makeRingSetUsingAminoAcid(IAtomContainer... theRing) {
        IRingSet theRingSet = new RingSet();

        for (IAtomContainer theTargetRing : theRing) {
            theRingSet.addAtomContainer(theTargetRing);
        }

        return theRingSet;
    }

    private IAtomContainer __getRingInHistidine(AminoAcid theAminoAcid) {
        IAtomContainer theRing = new AtomContainer();
        IAtom theGammaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        IAtom theDeltaNitrogen = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Nitrogen.SYMBOL + Constant.DELTA + AminoAcidInformation.FIRST);
        IAtom theDeltaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.DELTA + AminoAcidInformation.SECOND);
        IAtom theEpsilonCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.EPSILON + AminoAcidInformation.FIRST);
        IAtom theEpsilonNitrogen = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Nitrogen.SYMBOL + Constant.EPSILON + AminoAcidInformation.SECOND);

        theRing.addAtom(theGammaCarbon);
        theRing.addAtom(theDeltaNitrogen);
        theRing.addAtom(theDeltaCarbon);
        theRing.addAtom(theEpsilonCarbon);
        theRing.addAtom(theEpsilonNitrogen);

        theRing.addBond(theAminoAcid.getMolecule().getBond(theGammaCarbon, theDeltaNitrogen));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theGammaCarbon, theDeltaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theDeltaNitrogen, theEpsilonCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theDeltaCarbon, theEpsilonNitrogen));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theEpsilonCarbon, theEpsilonNitrogen));

        return theRing;
    }

    private IAtomContainer __getRingInProline(AminoAcid theAminoAcid) {
        IAtomContainer theRing = new AtomContainer();
        IAtom theBackBoneNitrogen = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Nitrogen.SYMBOL);
        IAtom theAlphaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.ALPHA);
        IAtom theBetaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.BETA);
        IAtom theGammaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        IAtom theDeltaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.DELTA);

        theRing.addAtom(theBackBoneNitrogen);
        theRing.addAtom(theAlphaCarbon);
        theRing.addAtom(theBetaCarbon);
        theRing.addAtom(theGammaCarbon);
        theRing.addAtom(theDeltaCarbon);

        theRing.addBond(theAminoAcid.getMolecule().getBond(theAlphaCarbon, theBetaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theBetaCarbon, theGammaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theGammaCarbon, theDeltaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theDeltaCarbon, theBackBoneNitrogen));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theBackBoneNitrogen, theAlphaCarbon));

        return theRing;
    }

    private IAtomContainer __getRingInPhenylAlanine(AminoAcid theAminoAcid) {
        IAtomContainer theRing = new AtomContainer();
        IAtom theGammaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        IAtom theFirstDeltaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.DELTA + AminoAcidInformation.FIRST);
        IAtom theSecondDeltaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.DELTA + AminoAcidInformation.SECOND);
        IAtom theFirstEpsilonCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.EPSILON + AminoAcidInformation.FIRST);
        IAtom theSecondEpsilonCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.EPSILON + AminoAcidInformation.SECOND);
        IAtom theZetaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.ZETA);

        theRing.addAtom(theGammaCarbon);
        theRing.addAtom(theFirstDeltaCarbon);
        theRing.addAtom(theSecondDeltaCarbon);
        theRing.addAtom(theFirstEpsilonCarbon);
        theRing.addAtom(theSecondEpsilonCarbon);
        theRing.addAtom(theZetaCarbon);

        theRing.addBond(theAminoAcid.getMolecule().getBond(theGammaCarbon, theFirstDeltaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theGammaCarbon, theSecondDeltaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theFirstDeltaCarbon, theFirstEpsilonCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theSecondDeltaCarbon, theSecondEpsilonCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theFirstEpsilonCarbon, theZetaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theSecondEpsilonCarbon, theZetaCarbon));

        return theRing;
    }

    private IAtomContainer __getRingInTyrosin(AminoAcid theAminoAcid) {
        return this.__getRingInPhenylAlanine(theAminoAcid);
    }

    private IAtomContainer __getFiveMemberedRingInTryptophan(AminoAcid theAminoAcid) {
        IAtomContainer theRing = new AtomContainer();
        IAtom theGammaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.GAMMA);
        IAtom theFirstDeltaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.DELTA + AminoAcidInformation.FIRST);
        IAtom theSecondDeltaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.DELTA + AminoAcidInformation.SECOND);
        IAtom theEpsilonNitrogen = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Nitrogen.SYMBOL + Constant.EPSILON + AminoAcidInformation.FIRST);
        IAtom theFirstEpsilonCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.EPSILON + AminoAcidInformation.SECOND);

        theRing.addAtom(theGammaCarbon);
        theRing.addAtom(theFirstDeltaCarbon);
        theRing.addAtom(theSecondDeltaCarbon);
        theRing.addAtom(theFirstEpsilonCarbon);
        theRing.addAtom(theEpsilonNitrogen);

        theRing.addBond(theAminoAcid.getMolecule().getBond(theGammaCarbon, theFirstDeltaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theGammaCarbon, theSecondDeltaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theFirstDeltaCarbon, theEpsilonNitrogen));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theSecondDeltaCarbon, theFirstEpsilonCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theEpsilonNitrogen, theFirstEpsilonCarbon));

        return theRing;
    }

    private IAtomContainer __getSixMemberedRingInTryptophan(AminoAcid theAminoAcid) {
        IAtomContainer theRing = new AtomContainer();
        IAtom theSecondDeltaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.DELTA + AminoAcidInformation.SECOND);
        IAtom theFirstEpsilonCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.EPSILON + AminoAcidInformation.SECOND);
        IAtom theSecondEpsilonCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.EPSILON + AminoAcidInformation.THIRD);
        IAtom theFirstZetaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.ZETA + AminoAcidInformation.SECOND);
        IAtom theSecondZetaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.ZETA + AminoAcidInformation.THIRD);
        IAtom theEtaCarbon = this.__getAtomInAminoAcid(theAminoAcid, AtomInformation.Carbon.SYMBOL + Constant.ETA + AminoAcidInformation.SECOND);

        theRing.addAtom(theSecondDeltaCarbon);
        theRing.addAtom(theFirstEpsilonCarbon);
        theRing.addAtom(theSecondEpsilonCarbon);
        theRing.addAtom(theFirstZetaCarbon);
        theRing.addAtom(theSecondZetaCarbon);
        theRing.addAtom(theEtaCarbon);

        theRing.addBond(theAminoAcid.getMolecule().getBond(theSecondDeltaCarbon, theFirstEpsilonCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theSecondDeltaCarbon, theSecondEpsilonCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theFirstEpsilonCarbon, theFirstZetaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theSecondEpsilonCarbon, theSecondZetaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theFirstZetaCarbon, theEtaCarbon));
        theRing.addBond(theAminoAcid.getMolecule().getBond(theSecondZetaCarbon, theEtaCarbon));

        return theRing;
    }

    private IAtom __getAtomInAminoAcid(AminoAcid theAminoAcid, String theAtomSynonum) {
        for (IAtom theAtom : theAminoAcid.getMolecule().atoms()) {
            if (theAtom.getProperty(AminoAcidInformation.ATOM_NAME).toString().equals(theAtomSynonum)) {
                return theAtom;
            }
        }

        return null;
    }
}
