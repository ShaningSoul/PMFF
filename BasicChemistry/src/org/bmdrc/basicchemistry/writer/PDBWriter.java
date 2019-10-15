/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.basicchemistry.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bmdrc.basicchemistry.protein.AminoAcid;
import org.bmdrc.basicchemistry.protein.AminoAcidInformation;
import org.bmdrc.basicchemistry.protein.Chain;
import org.bmdrc.basicchemistry.protein.Protein;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author labwindows
 */
public class PDBWriter {

    private final String REMARK = "REMARK   ";
    private final String HEADER = "File used in SBFF Program";
    private final String CREATED = "Created : ";
    private final String ATOM = "ATOM  ";
    private final String TERMINATION = "TER   ";
    private final String HETATM = "HETATM";
    private final String LIGAND_NAME_KEY = "cdk:Title";
    private final String CONNECT = "CONECT";
    private final String END = "END";

    public void write(String theFilePath, Protein theProtein, IAtomContainer ... theLigand) {
        String thePDBString = this.__getPDBString(theProtein, theLigand);
        try {
            BufferedWriter theFileWriter = new BufferedWriter(new FileWriter(new File(theFilePath)));
            
            theFileWriter.flush();
            theFileWriter.write(thePDBString);
            theFileWriter.close();
        } catch (IOException ex) {
        }
    }

    private String __getPDBString(Protein theProtein, IAtomContainer ... theLigand) {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.__initializeHeader());
        theStringBuilder.append(this.__getProteinString(theProtein));
        theStringBuilder.append(this.__getHeteroAtomString(theProtein, theLigand));
        theStringBuilder.append(this.__getConnectString(theProtein, theLigand));
        
        return theStringBuilder.toString();
    }
    
    private String __getConnectString(Protein theProtein, IAtomContainer ... theLigand) {
        StringBuilder theStringBuilder = new StringBuilder();
        int thePreviousAtomCount = theProtein.getMolecule().getAtomCount() + 1;
        
        for(IAtomContainer theMolecule : theLigand) {
            theStringBuilder.append(this.__getConnectedString(theMolecule, thePreviousAtomCount));
            thePreviousAtomCount += theMolecule.getAtomCount();
        }
        
        theStringBuilder.append(this.END).append("\n");
        
        return theStringBuilder.toString();
    }
    
    private String __getConnectedString(IAtomContainer theLigand, int thePreviousAtomCount) {
        StringBuilder theStringBuilder = new StringBuilder();
        
        for(int ai = 0, aEnd = theLigand.getAtomCount(); ai < aEnd; ai++) {
            List<Integer> theConnectedAtomNumberList = this.__getConnectedAtomIndexList(theLigand, ai);
            
            theStringBuilder.append(this.CONNECT).append(String.format("%5d", thePreviousAtomCount + ai + 1));
            
            for(Integer theConnectedAtomNumber : theConnectedAtomNumberList) {
                theStringBuilder.append(String.format("%5d", thePreviousAtomCount + theConnectedAtomNumber + 1));
            }
            
            theStringBuilder.append("\n");
        }
        
        return theStringBuilder.toString();
    }
    
    private List<Integer> __getConnectedAtomIndexList(IAtomContainer theLigand, int theTargetAtomIndex) {
        List<IAtom> theConnectedAtomList = theLigand.getConnectedAtomsList(theLigand.getAtom(theTargetAtomIndex));
        List<Integer> theConnectedAtomIndexList = new ArrayList<>();
        
        for(IAtom theConnectedAtom : theConnectedAtomList) {
            theConnectedAtomIndexList.add(theLigand.getAtomNumber(theConnectedAtom));
        }
        
        return theConnectedAtomIndexList;
    }
    
    private String __getHeteroAtomString(Protein theProtein, IAtomContainer ... theLigand) {
        StringBuilder theStringBuilder = new StringBuilder();
        int thePreviousAtomCount = theProtein.getMolecule().getAtomCount() + 1;
        
        for(IAtomContainer theMolecule : theLigand) {
            theStringBuilder.append(this.__getHeteroAtomString(theMolecule, thePreviousAtomCount));
            thePreviousAtomCount += theMolecule.getAtomCount();
        }
        
        theStringBuilder.append(this.__getTerminationStringForHeteroAtom(theLigand[theLigand.length-1], thePreviousAtomCount));
        
        return theStringBuilder.toString();
    }
    
    private String __getTerminationStringForHeteroAtom(IAtomContainer theLastLigand, int thePreviousAtomCount) {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.TERMINATION).append(String.format("%5d ", thePreviousAtomCount + 1)).append("     ")
                .append(theLastLigand.getProperty(this.LIGAND_NAME_KEY)).append(String.format("%2s", theLastLigand.getProperty(Protein.HETERO_MOLECULE_CHAIN_ID_KEY)))
                .append(String.format("%4s    ", theLastLigand.getProperty(Protein.HETERO_MOLECULE_SERIAL_KEY))).append("\n");
        
        return theStringBuilder.toString();
    }
    
    private String __getHeteroAtomString(IAtomContainer theLigand, int thePreviousAtomCount) {
        StringBuilder theStringBuilder = new StringBuilder();

        for (int ai = 0, aEnd = theLigand.getAtomCount(); ai < aEnd; ai++) {
            IAtom theAtom = theLigand.getAtom(ai);

            theStringBuilder.append(this.HETATM).append(String.format("%5d ", thePreviousAtomCount + ai + 1));
            if (theAtom.getProperty(AminoAcidInformation.ATOM_NAME).toString().charAt(0) >= '0' && theAtom.getProperty(AminoAcidInformation.ATOM_NAME).toString().charAt(0) <= '9') {
                theStringBuilder.append(String.format("%-4s ", theAtom.getProperty(AminoAcidInformation.ATOM_NAME)));
            } else {
                theStringBuilder.append(" ").append(String.format("%-3s ", theAtom.getProperty(AminoAcidInformation.ATOM_NAME)));
            }

            theStringBuilder.append(theLigand.getProperty(this.LIGAND_NAME_KEY)).append(String.format("%2s", theLigand.getProperty(Protein.HETERO_MOLECULE_CHAIN_ID_KEY)))
                    .append(String.format("%4s    ", theLigand.getProperty(Protein.HETERO_MOLECULE_SERIAL_KEY))).append(String.format("%8.3f", theAtom.getPoint3d().x))
                    .append(String.format("%8.3f", theAtom.getPoint3d().y)).append(String.format("%8.3f", theAtom.getPoint3d().z)).append("  1.00  0.00           ").append(theAtom.getSymbol());

            if (theAtom.getFormalCharge() != 0) {
                theStringBuilder.append(Math.abs(theAtom.getFormalCharge()));
            }

            if (theAtom.getFormalCharge().intValue() > 0) {
                theStringBuilder.append("+");
            } else if (theAtom.getFormalCharge().intValue() < 0) {
                theStringBuilder.append("-");
            }

            theStringBuilder.append("\n");
        }

        return theStringBuilder.toString();
    }
    
    private String __getProteinString(Protein theProtein) {
        StringBuilder theStringBuilder = new StringBuilder();
        int thePreviousAtomCount = 0;
        
        for (Chain theChain : theProtein) {
            for (AminoAcid theAminoAcid : theChain.getAminoAcidList()) {
                String theString = this.__getStringForAminoAcid(theAminoAcid, theChain.getChainName(), thePreviousAtomCount);
                theStringBuilder.append(theString);
                thePreviousAtomCount += theAminoAcid.getMolecule().getAtomCount();
            }
            
            theStringBuilder.append(this.__getTerminationStringForProtein(theChain.getAminoAcidList().get(theChain.getAminoAcidList().size()-1), theChain.getChainName(), thePreviousAtomCount));
        }
        
        return theStringBuilder.toString();
    }
    
    private String __getTerminationStringForProtein(AminoAcid theLastAminoAcid, String theChainName, int thePreviousAtomCount) {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(this.TERMINATION).append(String.format("%5d ", thePreviousAtomCount + 1)).append("     ")
                .append(theLastAminoAcid.getName()).append(String.format("%2s", theChainName)).append(String.format("%4s    ", theLastAminoAcid.getSerial())).append("\n");
        
        return theStringBuilder.toString();
    }
    
    private String __getStringForAminoAcid(AminoAcid theAminoAcid, String theChainName, int thePreviousAtomCount) {
        StringBuilder theStringBuilder = new StringBuilder();

        for (int ai = 0, aEnd = theAminoAcid.getMolecule().getAtomCount(); ai < aEnd; ai++) {
            IAtom theAtom = theAminoAcid.getMolecule().getAtom(ai);

            theStringBuilder.append(this.ATOM).append(String.format("%5d ", thePreviousAtomCount + ai + 1));
            if (theAtom.getProperty(AminoAcidInformation.ATOM_NAME).toString().charAt(0) >= '0' && theAtom.getProperty(AminoAcidInformation.ATOM_NAME).toString().charAt(0) <= '9') {
                theStringBuilder.append(String.format("%-4s ", theAtom.getProperty(AminoAcidInformation.ATOM_NAME)));
            } else {
                theStringBuilder.append(" ").append(String.format("%-3s ", theAtom.getProperty(AminoAcidInformation.ATOM_NAME)));
            }

            theStringBuilder.append(theAminoAcid.getName()).append(String.format("%2s", theChainName)).append(String.format("%4s    ", theAminoAcid.getSerial()))
                    .append(String.format("%8.3f", theAtom.getPoint3d().x)).append(String.format("%8.3f", theAtom.getPoint3d().y)).append(String.format("%8.3f", theAtom.getPoint3d().z))
                    .append("  1.00  0.00           ").append(theAtom.getSymbol());

            if (theAtom.getFormalCharge() != 0) {
                theStringBuilder.append(Math.abs(theAtom.getFormalCharge()));
            }

            if (theAtom.getFormalCharge().intValue() > 0) {
                theStringBuilder.append("+");
            } else if (theAtom.getFormalCharge().intValue() < 0) {
                theStringBuilder.append("-");
            }

            theStringBuilder.append("\n");
        }

        return theStringBuilder.toString();
    }

    private String __initializeHeader() {
        StringBuilder theStringBuilder = new StringBuilder();
        SimpleDateFormat theFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date theDate = new Date();

        theStringBuilder.append(this.REMARK).append(this.HEADER).append("\n");
        theStringBuilder.append(this.REMARK).append(theFormat.format(theDate)).append("\n");

        return theStringBuilder.toString();
    }
}
