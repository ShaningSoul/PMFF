/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.basicchemistry.table.enums;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public enum MoleculeTableColumnName {
    Index, Name;
    
    public static String[] getBasicMoleculeTableColumnNames() {
        int theSize = MoleculeTableColumnName.values().length;
        String[] theBasicMoleculeTableColumnNames = new String[theSize];
        MoleculeTableColumnName[] theColumnNames = MoleculeTableColumnName.values();
        
        for(int ci = 0; ci < theSize; ci++) {
            theBasicMoleculeTableColumnNames[ci] = theColumnNames[ci].name();
        }
        
        return theBasicMoleculeTableColumnNames;
    }
}
