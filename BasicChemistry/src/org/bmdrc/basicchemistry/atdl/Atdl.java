/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.atdl;

import java.io.Serializable;
import org.openscience.cdk.interfaces.IAtom;

/**
 * ATDL : Atom type Description Language
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 * @since 2015. 09. 14
 */
public class Atdl implements Serializable, Comparable {

    private static final long serialVersionUID = 6665538610235986717L;

    private String itsSymbol;//Atom symbol
    private Integer itsNumberOfConnectedAtom; //Size of connnected atom
    private Integer itsRingIndicator; // Ring size
    private Integer itsAromaticIndicator; // whether exist aromatic ring (Existed : 1, Not existed : 0)
    //constant Integer variable
    public static final int NOT_HAVE_RING = 0;
    public static final int THREE_MEMBERED_RING = 3;
    public static final int FOUR_MEMBERED_RING = 4;
    public static final int FIVE_MEMBERED_RING = 5;
    public static final int SIX_MEMBERED_RING = 6;
    public static final int NOT_AROMATIC = 0;
    public static final int AROMATIC = 1;
    public static final int ONE_ATOM_CONNECTED = 1;
    public static final int TWO_ATOM_CONNECTED = 2;
    public static final int THREE_ATOM_CONNECTED = 3;
    public static final int FOUR_ATOM_CONNECTED = 4;
    //constant String variable
    public static final String ATDL_KEY = "Atom Type Description Language";

    /**
     * Constructor
     */
    public Atdl() {
    }

    /**
     * Constructor<br>
     * Using Atdl String variable, input ATDL format<br>
     * 
     * @param theAtdlString : ATDL String
     */
    public Atdl(String theAtdlString) {
        this.__inputDataUsingAtdlString(theAtdlString);
    }

    /**
     * get atom symbol
     * 
     * @return string variable in atom symbol 
     */
    public String getSymbol() {
        return itsSymbol;
    }

    /**
     * set atom symbol
     * 
     * @param theSymbol : string variable
     */
    public void setSymbol(String theSymbol) {
        this.itsSymbol = theSymbol;
    }

    /**
     * get number of connected atom
     * 
     * @return Number of connected atoms
     */
    public Integer getNumberOfConnectedAtom() {
        return itsNumberOfConnectedAtom;
    }

    /**
     * set number of connected atom<br>
     * You can use static variable to set up this variable<br>
     * <br>
     * public static final Integer ONE_ATOM_CONNECTED = 1;<br>
     * public static final Integer TWO_ATOM_CONNECTED = 2;<br>
     * public static final Integer THREE_ATOM_CONNECTED = 3;<br>
     * public static final Integer FOUR_ATOM_CONNECTED = 4;<br>
     * 
     * @param theNumberOfConnectedAtom Number of connected atoms
     */
    public void setNumberOfConnectedAtom(Integer theNumberOfConnectedAtom) {
        this.itsNumberOfConnectedAtom = theNumberOfConnectedAtom;
    }

    /**
     * get ring size
     * 
     * @return Ring size
     */
    public Integer getRingIndicator() {
        return itsRingIndicator;
    }

    /**
     * set ring size <br>
     * You can use static variable<br>
     * NOT_HAVE_RING = 0;<br>
     * THREE_MEMBERED_RING = 3;<br>
     * FOUR_MEMBERED_RING = 4;<br>
     * FIVE_MEMBERED_RING = 5;<br>
     * SIX_MEMBERED_RING = 6;<br>
     * 
     * @param theRingIndicator static variable defined in this class
     */
    public void setRingIndicator(Integer theRingIndicator) {
        this.itsRingIndicator = theRingIndicator;
    }

    /**
     * get information whether this atom contain aromatic ring
     * You can get output these variable
     * 
     * public static final Integer NOT_AROMATIC = 0;
     * public static final Integer AROMATIC = 1;
     * 
     * @return 
     */
    public Integer getAromaticIndicator() {
        return itsAromaticIndicator;
    }

    /**
     * set information whether this atom contain aroamtic ring
     * You can set static variable
     * 
     * NOT_AROMATIC = 0;
     * AROMATIC = 1;
     * 
     * @param theAromaticIndicator static variabe defined in this class
     */
    public void setAromaticIndicator(Integer theAromaticIndicator) {
        this.itsAromaticIndicator = theAromaticIndicator;
    }

    /**
     * This function can sort ATDL information structure
     * 
     * @param theAtdlString ATDL string variable
     */
    private void __inputDataUsingAtdlString(String theAtdlString) {
        String theAtomSymbol = theAtdlString.substring(0, 2);
        String theNumberOfBondedAtom = theAtdlString.substring(2, 3);
        String theRingIndicator = theAtdlString.substring(3, 4);
        String theAromaticIndicator = theAtdlString.substring(4);

        this.setSymbol(theAtomSymbol);
        this.setNumberOfConnectedAtom(Integer.parseInt(theNumberOfBondedAtom));
        this.setRingIndicator(Integer.parseInt(theRingIndicator));
        this.setAromaticIndicator(Integer.parseInt(theAromaticIndicator));
    }

    /**
     * This function is used to show ATDL
     * 
     * @return ATDL string variable
     */
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        theStringBuilder.append(String.format("%2s", this.getSymbol())).append(this.getNumberOfConnectedAtom()).append(this.getRingIndicator()).append(this.getAromaticIndicator());

        return theStringBuilder.toString();
    }

    /**
     * This function is used to whether Two ATDL equal
     * 
     * @param theAtdl ATDL class variable
     * @return boolean value whether theAtdl equal
     */
    @Override
    public boolean equals(Object theAtdl) {
        return this.hashCode() == theAtdl.hashCode();
    }

    /**
     * This function is used to get ATDL string hash code
     * 
     * @return hash code in this ATDL
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * This function is used to whether validated ATDL equal ATDL information structure
     * 
     * @param theAtdl ATDL value
     * @return boolean value whether theAtdl equal
     */
    public boolean equalsAtdl(Atdl theAtdl) {
        if (!this.getSymbol().equals(((Atdl) theAtdl).getSymbol())) {
            return false;
        } else if (!this.getNumberOfConnectedAtom().equals(((Atdl) theAtdl).getNumberOfConnectedAtom())) {
            return false;
        } else if (!this.getRingIndicator().equals(((Atdl) theAtdl).getRingIndicator())) {
            return false;
        } else if (!this.getAromaticIndicator().equals(((Atdl) theAtdl).getAromaticIndicator())) {
            return false;
        }

        return true;
    }

    /**
     * This function is used to sort ATDL
     * 
     * @param o Must be inputed ATDL
     * @return the result of ATDL comparing
     */
    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }
}
