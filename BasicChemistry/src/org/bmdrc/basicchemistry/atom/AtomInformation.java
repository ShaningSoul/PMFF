/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.atom;

/**
 * This enumeration has atom information
 *
 * @author SungBo Hwang (tyamazaki@naver.com)
 */
public enum AtomInformation {

    Hydrogen("H", 1.00794, 1.2), Helieum("He", 4.002602, 1.43), Lithium("Li", 6.941, 2.12), Berylium("Be", 9.012182, 1.98), Boron("B", 10.811, 1.91), Carbon("C", 12.0107, 1.77),
    Nitrogen("N", 14.0087, 1.66), Oxygen("O", 15.9994, 1.5), Fluorine("F", 18.9984032, 1.46), Neon("Ne", 20.1797, 1.58), Sodium("Na", 22.98976928, 2.5), Magnesium("Mg", 24.3050, 2.51), 
    Aluminium("Al", 26.9815386, 2.25), Silicon("Si", 280.0855, 2.19), Phosphorus("P", 30.973762, 1.90), Sulfur("S", 32.065, 1.89), Chlorine("Cl", 35.453, 1.82), Argon("Ar", 39.948, 1.83),
    Potassium("K", 39.0983, 2.73), Calcium("Ca", 40.078, 2.62), Scandium("Sc", 44.955912, 2.58), Titanium("Ti", 47.867, 2.46), Vanadium("V", 50.9415, 2.42), Chromium("Cr", 51.9961, 2.45), 
    Manganese("Mn", 54.938045, 2.45), Iron("Fe", 55.845, 2.44), Cobalt("Co", 58.933195, 2.40), Nickel("Ni", 58.6934, 2.40), Copper("Cu", 63.546, 2.38), Zinc("Zn", 65.38, 2.39), 
    Gallium("Ga", 69.723, 2.32), Germanium("Ge", 72.64, 2.29), Arsenic("As", 74.9216, 1.88), Selenium("Se", 78.96, 1.82), Bromine("Br", 79.904, 1.86), Krypton("Kr", 83.798, 2.25), 
    Rubidium("Rb", 85.4678, 3.21), Strontium("Sr", 87.62, 2.84), Yttrium("Y", 88.90585, 2.75), Zirconium("Zr", 91.224, 2.52), Niobium("Nb", 92.90638, 2.56), Molybdenum("Mo", 95.96, 2.45),
    Technetium("Tc", 97.9072, 2.44), Ruthenium("Ru", 101.07, 2.46), Rhodium("Rh", 101.07, 2.44), Palladium("Pd", 106.42, 2.15), Silver("Ag", 107.8682, 2.53), Cadmium("Cd", 112.411, 2.49), 
    Indium("In", 114.818, 2.43), Tin("Sn", 118.710, 2.42), Antimony("Sb", 121.76, 2.47), Tellurium("Te", 127.6, 1.99), Iodine("I", 126.90447, 2.04);
    
    public final String SYMBOL;
    public final double WEIGHT;
    public final double VDW_RAIDUS;//Reference : Santiago Alvarez, A cartography of the van der Waals territories, Royal Society of Chemistry, 2013, 42, 8617-8636
    public final int ATOM_NUMBER;
    
    private AtomInformation(String theSymbol, double theWeight, double theVdwRadius) {
        this.SYMBOL = theSymbol;
        this.WEIGHT = theWeight;
        this.VDW_RAIDUS = theVdwRadius;
        this.ATOM_NUMBER = this.ordinal() + 1;
    }
    
    public static AtomInformation getAtomInformation(String theSymbol) {
        for(AtomInformation theAtomInformation : AtomInformation.values()) {
            if(theAtomInformation.SYMBOL.equals(theSymbol)) {
                return theAtomInformation;
            }
        }
        
        return null;
    }
}
