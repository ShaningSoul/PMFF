/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.predock.calculator;

import java.io.File;
import java.io.IOException;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.reader.PDBReader;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.predock.variable.BindingSite;
import org.bmdrc.predock.variable.DockingParameter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Exceptions;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class DockingCalculatorTest {
    
    private Protein itsProtein;
    private IAtomContainer itsLigand;
    private BindingSite itsBindingSite;
    private DockingParameter itsParameter;
    
    public DockingCalculatorTest() {
        try {
            PDBReader theReader = new PDBReader();
            
            this.itsProtein = theReader.readPDBFile(new File("G:\\내 드라이브\\SBFF\\ProteinLigandComplex\\Protein\\4dhw.pdb"));
            this.itsLigand = SDFReader.openMoleculeFile(new File("G:\\내 드라이브\\SBFF\\ProteinLigandComplex\\Ligand\\4dhw.sdf")).getAtomContainer(0);
            this.itsBindingSite = new BindingSite(this.itsProtein, this.itsLigand, new Vector3d(11.355644, 12.988078, -0.433922), 6.0);
            this.itsParameter = new DockingParameter();
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Test
    public void testSomeMethod() {
    }
    
}
