package org.bmdrc.basicchemistry.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @author GiBum Shin (gbshin@bmdrc.org)
 */
public class SDFReaderUsingDbSearch implements Serializable {
    
    private static final long serialVersionUID = -2191073842377518778L;
    
    private BufferedReader itsFileReader;
    private String itsStructureInformation;
    private String itsNextString;
    private Map<String, String> itsDescriptorMap;
    //constant String variable
    private final String STRUCTURE_INFORMATION_END_STRING = "M  END";
    private final String MOLECULE_END_STRING = "$$$$";
    private final String END_LINE = "\n";
    private final String DESCRIPTOR_NAME_STRING = "> <";
    
    public SDFReaderUsingDbSearch(File theMoleculeFile) throws FileNotFoundException {
        this.itsFileReader = new BufferedReader(new FileReader(theMoleculeFile));
        this.itsStructureInformation = new String();
        this.itsNextString = new String();
        this.itsDescriptorMap = new HashMap<>();
    }
    
    public void next() throws IOException {
        StringBuilder theStringBuilder = new StringBuilder();
        String theFileString;
        
        this.itsStructureInformation = new String();
        
        if(this.itsNextString != null) {
            theStringBuilder.append(this.itsNextString).append(this.END_LINE);
        }
        
        while(!(theFileString = this.itsFileReader.readLine()).contains(this.STRUCTURE_INFORMATION_END_STRING)) {
            theStringBuilder.append(theFileString).append(this.END_LINE);
        }
        
        
        this.itsStructureInformation = theStringBuilder.toString();
        
        String theDescriptorName = new String();
        StringBuilder theDescriptor = new StringBuilder();
        
        while(!(theFileString = this.itsFileReader.readLine()).contains(this.MOLECULE_END_STRING)) {
            if(theFileString.contains(this.DESCRIPTOR_NAME_STRING)) {
                theDescriptorName = theFileString.substring(this.DESCRIPTOR_NAME_STRING.length(), theFileString.length()-1);
            } else if(!theFileString.isEmpty()) {
                theDescriptor.append(theFileString).append(this.END_LINE);
            } else {
                this.itsDescriptorMap.put(theDescriptorName, theDescriptor.toString());
            }
        }
    }
    
    public boolean hasNext() throws IOException {
        this.itsNextString = this.itsFileReader.readLine();
        
        return this.itsNextString != null;
    }
}
