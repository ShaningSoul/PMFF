/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2016, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.basicchemistry.reaction.Reaction;
import org.bmdrc.basicchemistry.reaction.ReactionList;
import org.bmdrc.basicchemistry.fileformat.enums.RdFileTag;
import org.bmdrc.ui.abstracts.StringConstant;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 07. 09
 */
public class RdWriter implements Serializable {

    private static final long serialVersionUID = 6328424529902513922L;
    
    //constant String variable
    private static final String FORMAT_END = "$RFMT";
    private static final String MOLECULE_END_STRING = "M  END";
    private static final String TEMP_FILE = Constant.TEMP_DIR + "Temp_SBFF_RdWriter_Molecule.sdf";
    
    public static void write(ReactionList theReactionList, File theRdFile) {
        StringBuilder theStringBuilder = new StringBuilder();

        theStringBuilder.append(RdWriter.__initialString());

        for (Reaction theReaction : theReactionList) {
            theStringBuilder.append(RdWriter.__getReactionString(theReaction));
        }
        
        BufferedWriter theWriter = null;
        try {
            theWriter = new BufferedWriter(new FileWriter(theRdFile));
            
            theWriter.flush();
            theWriter.write(theStringBuilder.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                theWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static String __initialString() {
        StringBuilder theStringBuilder = new StringBuilder();
        SimpleDateFormat theFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss");

        theStringBuilder.append("$RDFILE 1").append(StringConstant.END_LINE);
        theStringBuilder.append("$DATM  SBFF      ").append(theFormat.format(new Date())).append(StringConstant.END_LINE);

        return theStringBuilder.toString();
    }

    private static String __getReactionString(Reaction theReaction) {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append(RdWriter.FORMAT_END).append(StringConstant.END_LINE);
        theStringBuilder.append(RdWriter.__getRXNPart(theReaction)).append(StringConstant.END_LINE);
        theStringBuilder.append(RdWriter.__getMOLPart(theReaction));
        theStringBuilder.append(RdWriter.__getDataPart(theReaction));

        return theStringBuilder.toString();
    }

    private static String __getDataPart(Reaction theReaction) {
        StringBuilder theStringBuilder = new StringBuilder();
        
        for(Object theKey : theReaction.getProperties().keySet()) {
            theStringBuilder.append(RdFileTag.DTYPE.TAG).append(" ").append(theKey.toString()).append(StringConstant.END_LINE);
            theStringBuilder.append(RdFileTag.DATUM.TAG).append(" ").append(theReaction.getProperty(theKey).toString()).append(StringConstant.END_LINE);
        }
        
        return theStringBuilder.toString();
    }
    
    private static String __getMOLPart(Reaction theReaction) {
        StringBuilder theStringBuilder = new StringBuilder();
        
        for(IAtomContainer theMolecule : theReaction.getSubstrateSet().atomContainers()) {
            theStringBuilder.append(RdFileTag.MOL.TAG).append(StringConstant.END_LINE);
            theStringBuilder.append(RdWriter.__getMoleculeString(theMolecule)).append(StringConstant.END_LINE);
        }
        
        for(IAtomContainer theMolecule : theReaction.getProductSet().atomContainers()) {
            theStringBuilder.append(RdFileTag.MOL.TAG).append(StringConstant.END_LINE);
            theStringBuilder.append(RdWriter.__getMoleculeString(theMolecule)).append(StringConstant.END_LINE);
        }
        
        return theStringBuilder.toString();
    }
    
    private static String __getMoleculeString(IAtomContainer theMolecule) {
        SDFWriter.writeSDFile(theMolecule, new File(RdWriter.TEMP_FILE));
        StringBuilder theStringBuilder = new StringBuilder();
        BufferedReader theFileReader = null;
        
        try {
            theFileReader = new BufferedReader(new FileReader(RdWriter.TEMP_FILE));
            String theFileString = null;
            
            while(!(theFileString = theFileReader.readLine()).equals(RdWriter.MOLECULE_END_STRING)) {
                theStringBuilder.append(theFileString).append(StringConstant.END_LINE);
            }
            
            theStringBuilder.append(RdWriter.MOLECULE_END_STRING);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                theFileReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return theStringBuilder.toString();
    }
    
    private static String __getRXNPart(Reaction theReaction) {
        StringBuilder theStringBuilder = new StringBuilder();
        SimpleDateFormat theFormat = new SimpleDateFormat("ddMMyyhhmm");

        theStringBuilder.append(RdFileTag.RXN.TAG).append(StringConstant.END_LINE);
        theStringBuilder.append(StringConstant.END_LINE);
        theStringBuilder.append("  SBFF         ").append(theFormat.format(new Date())).append(StringConstant.END_LINE);
        theStringBuilder.append(StringConstant.END_LINE);
        theStringBuilder.append(String.format("%3d", theReaction.getSubstrateSet().getAtomContainerCount()))
                .append(String.format("%3d", theReaction.getProductSet().getAtomContainerCount()));

        return theStringBuilder.toString();
    }
}
