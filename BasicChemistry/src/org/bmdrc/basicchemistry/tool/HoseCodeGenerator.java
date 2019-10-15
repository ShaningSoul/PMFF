package org.bmdrc.basicchemistry.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.bmdrc.basicchemistry.interfaces.IHoseCodeSymbol;
import org.bmdrc.ui.abstracts.StringConstant;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 */
public class HoseCodeGenerator implements IHoseCodeSymbol, Serializable {

    private static final long serialVersionUID = -7914116239198228236L;

    private IAtomContainer itsMolecule;
    private TopologicalDistanceMatrix itsDistanceMatrix;
    private List<Integer> itsPreviousUsedAtomNumberList;
    private List<Integer> itsTotalUsedAtomNumberList;
    private TwoDimensionList<Integer> itsTotalUsedAtomNumber2dListByLevel;
    private List<Integer> itsUsedAtomNumberListInSphere;
    private TwoDimensionList<Integer> itsAtomNumber2dListContainedAromaticRing;
    //constant variable
    private final int HYDROGEN_ATOMIC_NUMBER = 1;
    private final int INITIAL_VALUE_OF_BOND_ORDER = 0;
    private final int INDEX_OF_FIRST_ATOM_IN_BOND = 0;
    private final int INDEX_OF_SECOND_ATOM_IN_BOND = 1;
    private final int VALUE_DIVIDING_ATOM_NUMBER_LIST = -1;
    private final int INVALID_INDEX = -1;
    private final int CARBON_ATOMIC_NUMBER = 6;
    private final int OXYGEN_ATOMIC_NUMBER = 8;
    private final int NITROGEN_ATOMIC_NUMBER = 7;
    private final int SULFUR_ATOMIC_NUMBER = 16;
    private final int PHOSPORUS_ATOMIC_NUMBER = 15;
    private final int SILICON_ATOMIC_NUMBER = 14;
    private final int BORON_ATOMIC_NUMBER = 5;
    private final int FLUORE_ATOMIC_NUMBER = 9;
    private final int CHLORIDE_ATOMIC_NUMBER = 17;
    private final int BROME_ATOMIC_NUMBER = 35;
    private final int IODIDE_ATOMIC_NUMBER = 53;
    private final int MAXIMUM_PRIORITY = 14;
    private final int NEIBOR_ATOM_TOPOLOGICAL_DISTANCE = 1;
    private final int FIRST_SPHERE_INDEX = 0;
    private final int FIRST_ATOM_INDEX = 0;
    private final int SECOND_ATOM_INDEX = 1;
    //constant string variable
    public static final String HOSE_CODE_KEY = "HOSE_CODE_";
    private final String OPEN_PARENTHESES = "(";
    private final String CLOSE_PARENTHESES = ")";
    private final String AROMATIC_RING_KEY = "Aromatic_Ring";

    public HoseCodeGenerator() {
        this.itsMolecule = new AtomContainer();
        this.itsDistanceMatrix = new TopologicalDistanceMatrix();
        this.__setAtomNumber2dListContainedAromaticRing(new TwoDimensionList<Integer>());
        this.__setPreviousUsedAtomNumberList(new ArrayList<Integer>());
        this.__setTotalUsedAtomNumberList(new ArrayList<Integer>());
    }

    public HoseCodeGenerator(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
        this.itsDistanceMatrix = new TopologicalDistanceMatrix(theMolecule);
        this.__generateAtomNumber2dListContainedAromaticRing();
        this.__setPreviousUsedAtomNumberList(new ArrayList<Integer>());
        this.__setTotalUsedAtomNumberList(new ArrayList<Integer>());
    }

    public IAtomContainer getMolecule() {
        return itsMolecule;
    }

    public void setMolecule(IAtomContainer itsMolecule) {
        this.itsMolecule = itsMolecule;
    }

    public IAtomContainer setMolecule() {
        return itsMolecule;
    }

    public TopologicalDistanceMatrix getDistanceMatrix() {
        return itsDistanceMatrix;
    }

    public void setDistanceMatrix(TopologicalDistanceMatrix itsDistanceMatrix) {
        this.itsDistanceMatrix = itsDistanceMatrix;
    }

    private List<Integer> __getPreviousUsedAtomNumberList() {
        return itsPreviousUsedAtomNumberList;
    }

    private void __setPreviousUsedAtomNumberList(List<Integer> thePreviousUsedAtomNumberList) {
        this.itsPreviousUsedAtomNumberList = thePreviousUsedAtomNumberList;
    }

    private List<Integer> __setPreviousUsedAtomNumberList() {
        return itsPreviousUsedAtomNumberList;
    }

    private List<Integer> __getTotalUsedAtomNumberList() {
        return itsTotalUsedAtomNumberList;
    }

    private void __setTotalUsedAtomNumberList(List<Integer> theTotalUsedAtomNumberList) {
        this.itsTotalUsedAtomNumberList = theTotalUsedAtomNumberList;
    }

    private List<Integer> __setTotalUsedAtomNumberList() {
        return itsTotalUsedAtomNumberList;
    }

    private TwoDimensionList<Integer> __getTotalUsedAtomNumber2dListByLevel() {
        return itsTotalUsedAtomNumber2dListByLevel;
    }

    private void __setTotalUsedAtomNumber2dListByLevel(TwoDimensionList<Integer> theTotalUsedAtomNumber2dListByLevel) {
        this.itsTotalUsedAtomNumber2dListByLevel = theTotalUsedAtomNumber2dListByLevel;
    }

    private TwoDimensionList<Integer> __setTotalUsedAtomNumber2dListByLevel() {
        return itsTotalUsedAtomNumber2dListByLevel;
    }

    private List<Integer> __getUsedAtomNumberListInSphere() {
        return itsUsedAtomNumberListInSphere;
    }

    private void __setUsedAtomNumberListInSphere(List<Integer> theUsedAtomNumberListInSphere) {
        this.itsUsedAtomNumberListInSphere = theUsedAtomNumberListInSphere;
    }

    private List<Integer> __setUsedAtomNumberListInSphere() {
        return itsUsedAtomNumberListInSphere;
    }

    private TwoDimensionList<Integer> __getAtomNumber2dListContainedAromaticRing() {
        return itsAtomNumber2dListContainedAromaticRing;
    }

    private void __setAtomNumber2dListContainedAromaticRing(TwoDimensionList<Integer> theAtomNumber2dListContainedAromaticRing) {
        this.itsAtomNumber2dListContainedAromaticRing = theAtomNumber2dListContainedAromaticRing;
    }

    private TwoDimensionList<Integer> __setAtomNumber2dListContainedAromaticRing() {
        return itsAtomNumber2dListContainedAromaticRing;
    }

    public void inputHoseCodeProperty(IAtomContainerSet theMoleculeSet, int theNumberOfSphere, String theSeparator) throws CDKException {
        for (IAtomContainer theMolecule : theMoleculeSet.atomContainers()) {
            this.inputHoseCodeProperty(theMolecule, theNumberOfSphere, theSeparator);
        }
    }

    public void inputHoseCodeProperty(IAtomContainer theMolecule, int theNumberOfSphere, String theSeparator) throws CDKException {
        this.setMolecule(theMolecule);
        this.inputHoseCodeProperty(theNumberOfSphere, theSeparator);
    }

    public void inputHoseCodeProperty(int theNumberOfSphere, String theSeparator) throws CDKException {
        this.setMolecule().setProperty(this.HOSE_CODE_KEY + theNumberOfSphere, this.generateHoseCode(theNumberOfSphere, theSeparator));
    }

    public String generateHoseCode(IAtomContainer theMolecule, int theNumberOfSphere, String theSeparator) throws CDKException {
//        RingPerception theRingPerception = new RingPerception(this.getMolecule());
//
//        this.setMolecule(theMolecule);
//        RingPerception.setAromaticityBond();
//        this.__generateAtomNumber2dListContainedAromaticRing();

        return this.generateHoseCode(theNumberOfSphere, theSeparator);
    }

    public String generateHoseCode(int theNumberOfSphere, String theSeparator) throws CDKException {
        StringBuilder theHoseCode = new StringBuilder();
//        RingPerception theRingPerception = new RingPerception(this.getMolecule());
//
//        theRingPerception.setAromaticityBond();
//        this.__generateAtomNumber2dListContainedAromaticRing();

        for (int ai = 0, aEnd = this.getMolecule().getAtomCount(); ai < aEnd; ai++) {
            this.__setPreviousUsedAtomNumberList(new ArrayList<Integer>());
            theHoseCode.append(this.__getHoseCodeByAtom(this.getMolecule().getAtom(ai), theNumberOfSphere));

            if (ai < aEnd - 1) {
                theHoseCode.append(theSeparator);
            }
        }

        return theHoseCode.toString();
    }

    private void __generateAtomNumber2dListContainedAromaticRing() {
        this.__setAtomNumber2dListContainedAromaticRing(new TwoDimensionList<Integer>());
        
        if (this.getMolecule().getProperties().containsKey(this.AROMATIC_RING_KEY)) {
            String[] theAromaticRingInformationArray = this.getMolecule().getProperty(this.AROMATIC_RING_KEY).toString().split(StringConstant.TAB_STRING);

            for (String theAromaticRingInformation : theAromaticRingInformationArray) {
                String[] theAtomNumberArray = theAromaticRingInformation.split(StringConstant.SLUSH_STRING);
                List<Integer> theAtomNumberList = new ArrayList<>();

                for (String theAtomNumber : theAtomNumberArray) {
                    theAtomNumberList.add(Integer.parseInt(theAtomNumber));
                }

                this.__setAtomNumber2dListContainedAromaticRing().add(theAtomNumberList);
            }
        }
    }

    private void __initializeAtomNumberList(final Integer theAtomNumber) {
        List<Integer> theZeroSphereAtomNumberList = new ArrayList<>();

        this.__setTotalUsedAtomNumberList(new ArrayList<Integer>());
        this.__setTotalUsedAtomNumber2dListByLevel(new TwoDimensionList<Integer>());
        this.__setUsedAtomNumberListInSphere(new ArrayList<Integer>());
    }

    private String __getHoseCodeByAtom(IAtom theAtom, int theNumberOfSphere) {
        StringBuilder theHoseCodeByAtom = new StringBuilder();

        this.__initializeAtomNumberList(this.getMolecule().getAtomNumber(theAtom));
        theHoseCodeByAtom.append(this.__getHoseCodeByAtomInZeroSphere(this.getMolecule().getAtomNumber(theAtom)));
        this.__setTotalUsedAtomNumber2dListByLevel().add(this.__getPreviousUsedAtomNumberList());

        if (theNumberOfSphere != 0) {
            for (int si = 0; si < theNumberOfSphere; si++) {
                if (si == 0) {
                    int theAtomNumber = this.getMolecule().getAtomNumber(theAtom);

                    theHoseCodeByAtom.append(this.__getHoseCodeByAtomInFirstSphere(theAtom));
                } else {
                    theHoseCodeByAtom.append(this.__getHoseCodeByAtomInNotFirstSphere(si));
                }

                this.__setTotalUsedAtomNumber2dListByLevel().add(new ArrayList<>(this.__getPreviousUsedAtomNumberList()));
            }
        }

        if (this.__isNeededClosedParentheses(theNumberOfSphere)) {
            theHoseCodeByAtom.append(this.CLOSE_PARENTHESES);
        }

        return theHoseCodeByAtom.toString();
    }

    private boolean __isNeededClosedParentheses(final int theNumberOfSphere) {
        if (theNumberOfSphere % 6 > 1 && theNumberOfSphere % 6 < 5) {
            return true;
        }

        return false;
    }

    private String __getHoseCodeByAtomInFirstSphere(final IAtom theAtom) {
        StringBuilder theHoseCodeInFirstSphere = new StringBuilder();
        List<String> theHoseCodeStringList = this.__getHoseCodeStringList(theAtom, this.FIRST_SPHERE_INDEX);

        for (String theHoseCode : theHoseCodeStringList) {
            theHoseCodeInFirstSphere.append(theHoseCode);
        }

        this.__setPreviousUsedAtomNumberList(new ArrayList<>(this.__getUsedAtomNumberListInSphere()));
        this.__setUsedAtomNumberListInSphere(new ArrayList<Integer>());

        return theHoseCodeInFirstSphere.toString();
    }

    private <T> Integer __getNovelIndex(final T theValue, final List<T> theList, List<Integer> theUsedIndexList) {
        Integer theIndex = theList.indexOf(theValue);

        while (theUsedIndexList.contains(theIndex)) {
            theIndex += theList.subList(theIndex + 1, theList.size()).indexOf(theValue) + 1;
        }

        return theIndex;
    }

    private List<Integer> __getPriorityList(final List<IAtom> theConnectedAtomList, final IAtom theStandardAtom) {
        List<Integer> thePriorityList = new ArrayList<>();

        for (IAtom theAtom : theConnectedAtomList) {
            int thePriority = this.__getPriority(this.getMolecule().getAtomNumber(theAtom), this.getMolecule().getAtomNumber(theStandardAtom));

            thePriorityList.add(thePriority);
        }

        return thePriorityList;
    }

    private List<String> __getHoseCodeStringList(final IAtom theAtom, final Integer theSphereLevel) {
        List<String> theHoseCodeStringList = new ArrayList<>();
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);
        List<Integer> thePriorityList = this.__getPriorityList(theConnectedAtomList, theAtom);
        List<Integer> theSortedPriorityList = new ArrayList<>(thePriorityList);
        List<Integer> theUsedIndexList = new ArrayList<>();

        Collections.sort(theSortedPriorityList);

        for (Integer theSortedPriority : theSortedPriorityList) {
            int theMatchedIndex = this.__getNovelIndex(theSortedPriority, thePriorityList, theUsedIndexList);
            int theAtomNumber = this.getMolecule().getAtomNumber(theConnectedAtomList.get(theMatchedIndex));

            if (theConnectedAtomList.get(theMatchedIndex).getAtomicNumber() != this.HYDROGEN_ATOMIC_NUMBER
                    && !this.__containAtomNumberListUpperTwoLevelShpere(theAtomNumber, theSphereLevel)) {
                theHoseCodeStringList.add(this.__getHoseCodeStringByAtom(theAtom, theConnectedAtomList.get(theMatchedIndex)));
            }
            theUsedIndexList.add(theMatchedIndex);
        }

        this.__setTotalUsedAtomNumberList().add(this.getMolecule().getAtomNumber(theAtom));

        return theHoseCodeStringList;
    }

    private boolean __containAtomNumberListUpperTwoLevelShpere(final Integer theAtomNumber, final Integer theSphereLevel) {
        if (theSphereLevel < 1) {
            return false;
        } else {
            return this.__getTotalUsedAtomNumber2dListByLevel().get(theSphereLevel - 1).contains(theAtomNumber);
        }
    }

    private boolean __containAtomNumberInTotalUsedAtomNumber(IAtom theAtom) {
        for (List<Integer> theAtomNumberList : this.__getTotalUsedAtomNumber2dListByLevel()) {
            if (theAtomNumberList.contains(this.getMolecule().getAtomNumber(theAtom))) {
                return true;
            }
        }

        return false;
    }

    private String __getHoseCodeStringByAtom(final IAtom theStandardAtom, final IAtom theStringAtom) {
        Order theBondOrder = this.getMolecule().getBond(theStandardAtom, theStringAtom).getOrder();
        StringBuilder theHoseCode = new StringBuilder();

        if (this.__isAromaticBond(this.getMolecule().getBond(theStandardAtom, theStringAtom))) {
            theHoseCode.append(this.AROMATIC_SYMBOL);
        } else if (theBondOrder.equals(Order.DOUBLE)) {
            theHoseCode.append(this.DOUBLE_BOND_SYMBOL);
        } else if (theBondOrder.equals(Order.TRIPLE)) {
            theHoseCode.append(this.TRIPLE_BOND_SYMBOL);
        }

        theHoseCode.append(this.__getAtomSymbol(theStringAtom));

        if (!theHoseCode.toString().contains(this.RING_CLOSER_SYMBOL)) {
            this.__setUsedAtomNumberListInSphere().add(this.getMolecule().getAtomNumber(theStringAtom));
        }

        return theHoseCode.toString();
    }

    private boolean __isAromaticBond(IBond theBond) {
        int theFirstAtomNumber = this.getMolecule().getAtomNumber(theBond.getAtom(this.FIRST_ATOM_INDEX));
        int theSecondAtomNumber = this.getMolecule().getAtomNumber(theBond.getAtom(this.SECOND_ATOM_INDEX));

        for (List<Integer> theAromaticRingAtomNumberList : this.__getAtomNumber2dListContainedAromaticRing()) {
            if (theAromaticRingAtomNumberList.contains(theFirstAtomNumber) && theAromaticRingAtomNumberList.contains(theSecondAtomNumber)) {
                return true;
            }
        }

        return false;
    }

    private String __getAtomSymbol(final IAtom theAtom) {
        if (this.__getTotalUsedAtomNumberList().contains(this.getMolecule().getAtomNumber(theAtom))
                || this.__getUsedAtomNumberListInSphere().contains(this.getMolecule().getAtomNumber(theAtom))) {
            return this.RING_CLOSER_SYMBOL;
        } else if (theAtom.getAtomicNumber() == this.CHLORIDE_ATOMIC_NUMBER) {
            return this.CHLORIDE_SYMBOL;
        } else if (theAtom.getAtomicNumber() == this.BROME_ATOMIC_NUMBER) {
            return this.BROMIDE_SYMBOL;
        }

        return theAtom.getSymbol();
    }

    private int __getNumberOfConnectedAtomNotHydrogen(final int theAtomNumber) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(this.getMolecule().getAtom(theAtomNumber));
        int theNumberOfConnectedAtom = theConnectedAtomList.size();

        for (IAtom theAtom : theConnectedAtomList) {
            if (theAtom.getAtomicNumber() == this.HYDROGEN_ATOMIC_NUMBER) {
                theNumberOfConnectedAtom--;
            }
        }

        return theNumberOfConnectedAtom;
    }

    private String __getHoseCodeByAtomInZeroSphere(final int theAtomNumber) {
        StringBuilder theHoseCodeByAtomInFirstSphere = new StringBuilder();

        theHoseCodeByAtomInFirstSphere.append(this.__getAtomSymbol(this.getMolecule().getAtom(theAtomNumber))).append(StringConstant.MINUS_STRING)
                .append(this.__getNumberOfConnectedAtomNotHydrogen(theAtomNumber)).append(StringConstant.SEMICOLON_STRING);

        this.__setPreviousUsedAtomNumberList().add(theAtomNumber);
        return theHoseCodeByAtomInFirstSphere.toString();
    }

    private String __getHoseCodeByAtomInNotFirstSphere(final int theNumberOfSphere) {
        StringBuilder theHOSECODE = new StringBuilder();

        theHOSECODE.append(this.__getDividedCellSymbol(theNumberOfSphere));

        for (int vi = 0, vEnd = this.__getPreviousUsedAtomNumberList().size(); vi < vEnd; vi++) {
            if (!this.__getPreviousUsedAtomNumberList().get(vi).equals(this.INVALID_INDEX)) {
                theHOSECODE.append(this.__getHoseCodeByAtomInNotFirstSphere(this.getMolecule().getAtom(this.__getPreviousUsedAtomNumberList().get(vi)), theNumberOfSphere));

                if (vi < vEnd - 1) {
                    theHOSECODE.append(StringConstant.COMMA_STRING);
                }
            }
        }

        this.__setPreviousUsedAtomNumberList(new ArrayList<>(this.__getUsedAtomNumberListInSphere()));
        this.__setUsedAtomNumberListInSphere(new ArrayList<Integer>());
        return theHOSECODE.toString();
    }

    private String __getHoseCodeByAtomInNotFirstSphere(final IAtom theAtom, final int theNumberOfSphere) {
        StringBuilder theStringBuilder = new StringBuilder();
        List<String> theHoseCodeListByAtom = this.__getHoseCodeStringList(theAtom, theNumberOfSphere);

        for (String theHoseCode : theHoseCodeListByAtom) {
            theStringBuilder.append(theHoseCode);
        }

        return theStringBuilder.toString();
    }

    private String __getDividedCellSymbol(int theNumberOfSphere) {
        if (theNumberOfSphere % 6 == 1) {
            return this.OPEN_PARENTHESES;
        } else if (theNumberOfSphere % 6 == 4) {
            return this.CLOSE_PARENTHESES;
        }

        return StringConstant.SLUSH_STRING;
    }

    private List<Integer> __getConnectedAtomNumberNotHydrogen(int theCheckAtomNumber, int thePreviousUsedAtomNumber) {
        List<Integer> theConnectedAtomNumberNotHydrogenList = new ArrayList<>();
        int theIndexOfAtom = 0;

        for (int di = 0, dEnd = this.getDistanceMatrix().getNumberOfAtomAtDistance(theCheckAtomNumber, 1); di < dEnd; di++) {
            if (di == 0) {
                theIndexOfAtom = this.getDistanceMatrix().indexOfDistance(theCheckAtomNumber, 1, theIndexOfAtom);
            }

            if (!this.__isHydrogenAtom(theIndexOfAtom) && thePreviousUsedAtomNumber != theIndexOfAtom) {
                theConnectedAtomNumberNotHydrogenList.add(theIndexOfAtom);
            }
        }

        return this.__sortByPriority(theConnectedAtomNumberNotHydrogenList, thePreviousUsedAtomNumber);
    }

    private List<Integer> __sortByPriority(List<Integer> theConnectedAtomNumberNotHydrogenList, int thePreviousUsedAtomNumber) {
        List<Integer> theSortedConnectedAtomNumberListNotHydrogen = new ArrayList<>();
        List<Integer> thePriorityList = new ArrayList<>();

        for (Integer theConnectedAtomNumber : theConnectedAtomNumberNotHydrogenList) {
            Integer thePriority = this.__getPriority(theConnectedAtomNumber, thePreviousUsedAtomNumber);
            thePriorityList.add(thePriority);
        }

        for (int vi = 1, vEnd = theConnectedAtomNumberNotHydrogenList.size(); (vi <= this.MAXIMUM_PRIORITY) && (theSortedConnectedAtomNumberListNotHydrogen.size() < vEnd); vi++) {
            int theIndexOfMatchedPriority = 0;

            while ((theIndexOfMatchedPriority = thePriorityList.subList(theIndexOfMatchedPriority, thePriorityList.size()).indexOf(vi)) != this.INVALID_INDEX) {
                theSortedConnectedAtomNumberListNotHydrogen.add(theConnectedAtomNumberNotHydrogenList.get(theIndexOfMatchedPriority++));
            }
        }

        return theSortedConnectedAtomNumberListNotHydrogen;
    }

    private Integer __getPriority(final Integer theAtomNumber, final Integer theStandardAtomNumber) {
        for (IBond theBond : this.getMolecule().bonds()) {
            if (this.__isMatchedBond(theBond, theAtomNumber, theStandardAtomNumber)) {
                if (this.__isAromaticBond(theBond)) {
                    return 0;
                } else if (theBond.getOrder().equals(Order.TRIPLE)) {
                    return 1;
                } else if (theBond.getOrder().equals(Order.DOUBLE)) {
                    return 2;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.CARBON_ATOMIC_NUMBER)) {
                    return 3;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.OXYGEN_ATOMIC_NUMBER)) {
                    return 4;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.NITROGEN_ATOMIC_NUMBER)) {
                    return 5;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.SULFUR_ATOMIC_NUMBER)) {
                    return 6;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.PHOSPORUS_ATOMIC_NUMBER)) {
                    return 7;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.SILICON_ATOMIC_NUMBER)) {
                    return 8;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.BORON_ATOMIC_NUMBER)) {
                    return 9;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.FLUORE_ATOMIC_NUMBER)) {
                    return 10;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.CHLORIDE_ATOMIC_NUMBER)) {
                    return 11;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.BROME_ATOMIC_NUMBER)) {
                    return 12;
                } else if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber().equals(this.IODIDE_ATOMIC_NUMBER)) {
                    return 13;
                } else {
                    return 14;
                }
            }
        }

        return this.INVALID_INDEX;
    }

    private boolean __isMatchedBond(final IBond theBond, final Integer theFirstAtomNumber, final Integer theSecondAtomNumber) {
        Integer theFirstAtomNumberInBond = this.getMolecule().getAtomNumber(theBond.getAtom(this.INDEX_OF_FIRST_ATOM_IN_BOND));
        Integer theSecondAtomNumberInBond = this.getMolecule().getAtomNumber(theBond.getAtom(this.INDEX_OF_SECOND_ATOM_IN_BOND));

        if (theFirstAtomNumberInBond.equals(theFirstAtomNumber) && theSecondAtomNumberInBond.equals(theSecondAtomNumber)) {
            return true;
        } else if (theFirstAtomNumberInBond.equals(theSecondAtomNumber) && theSecondAtomNumberInBond.equals(theFirstAtomNumber)) {
            return true;
        }

        return false;
    }

    private boolean __isHydrogenAtom(int theAtomNumber) {
        if (this.getMolecule().getAtom(theAtomNumber).getAtomicNumber() == this.HYDROGEN_ATOMIC_NUMBER) {
            return true;
        }

        return false;
    }

    private int __getConnectedNumberOfAtomNotHydrogen(IAtom theAtom) {
        List<IAtom> theConnectedAtomList = this.getMolecule().getConnectedAtomsList(theAtom);
        int theConnectedNumberOfAtomNotHydrogen = 0;

        for (IAtom theConnectedAtom : theConnectedAtomList) {
            if (theConnectedAtom.getAtomicNumber() != this.HYDROGEN_ATOMIC_NUMBER) {
                theConnectedNumberOfAtomNotHydrogen++;
            }
        }

        return theConnectedNumberOfAtomNotHydrogen;
    }
}