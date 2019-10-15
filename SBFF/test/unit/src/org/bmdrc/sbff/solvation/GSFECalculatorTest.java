/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.sbff.solvation;

import java.io.File;
import java.io.IOException;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.reader.PDBReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class GSFECalculatorTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void calculateGSFE() {
        PDBReader theReader = new PDBReader();
        File[] theProteinFiles = new File("G:\\내 드라이브\\Research\\Binding affinity prediction\\Test_File\\").listFiles();
        GSFECalculator theCalculator = new GSFECalculator();

        for (File theFile : theProteinFiles) {
            try {
                Protein theProtein = theReader.readPDBFile(theFile);

                System.out.println(theFile.getName() + " : " + theCalculator.calculateGSFE(theProtein.getMolecule()));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

}
