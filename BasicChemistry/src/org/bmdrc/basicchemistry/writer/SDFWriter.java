package org.bmdrc.basicchemistry.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @author Gi Bum Shin (gbshin@bmdrc.org)
 */
public abstract class SDFWriter implements Serializable {
    private static final long serialVersionUID = -2297477678943575687L;
    
    public static void writeSDFile(IAtomContainer theMolecule, File theOutputFile) {
        try {
            FileWriter out = new FileWriter(theOutputFile);
            org.openscience.cdk.io.SDFWriter sdfW = new org.openscience.cdk.io.SDFWriter(out);

            sdfW.write(theMolecule);
            sdfW.close();
            out.close();
        } catch (CDKException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public static void writeSDFile(IAtomContainerSet molSet, File outputFile) {
        try {
            FileWriter out = new FileWriter(outputFile);
            org.openscience.cdk.io.SDFWriter sdfW = new org.openscience.cdk.io.SDFWriter(out);

            sdfW.write(molSet);
            sdfW.close();
            out.close();
        } catch (CDKException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
