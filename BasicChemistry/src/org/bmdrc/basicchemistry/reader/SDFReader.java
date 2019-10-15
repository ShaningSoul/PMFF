package org.bmdrc.basicchemistry.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Serializable;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.io.iterator.IteratingSDFReader;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @author GiBum Shin (gbshin@bmdrc.org)
 */
public abstract class SDFReader implements Serializable {
    
    private static final long serialVersionUID = -2191073842377518778L;
    
    public static IAtomContainerSet openMoleculeFile(File theSdFile) {
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();
        
        try {
            IteratingSDFReader reader = new IteratingSDFReader(
                    new FileReader(theSdFile),
                    DefaultChemObjectBuilder.getInstance());
            while (reader.hasNext()) {
                theMoleculeSet.addAtomContainer(reader.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return theMoleculeSet;
    }
    
    public static IAtomContainerSet openMoleculeFile(InputStream theInputStream) {
        IAtomContainerSet theMoleculeSet = new AtomContainerSet();
            IteratingSDFReader reader = new IteratingSDFReader(
                    theInputStream,
                    DefaultChemObjectBuilder.getInstance());
            while (reader.hasNext()) {
                theMoleculeSet.addAtomContainer(reader.next());
            }
        return theMoleculeSet;
    }
}
