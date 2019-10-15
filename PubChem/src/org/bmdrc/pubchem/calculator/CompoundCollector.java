package org.bmdrc.pubchem.calculator;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.bmdrc.basicchemistry.windows.MoleculeStructureViewerTopComponent;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.basicchemistry.writer.SDFWriter;
import org.bmdrc.ui.abstracts.Constant;
import org.netbeans.api.progress.*;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class CompoundCollector extends AbstractAction implements Serializable, Runnable, Cancellable {

    private static final long serialVersionUID = 3159088838531386259L;

    public enum Pubchem_Compound_Domain {
        CID(Integer.class),
        Name(String.class),
        Smiles(String.class),
        InchiKey(String.class),
        Formula(Integer.class);

        public final Class DATA_CLASS;

        private Pubchem_Compound_Domain(Class DATA_CLASS) {
            this.DATA_CLASS = DATA_CLASS;
        }
        
        public static List<String> getDomainList() {
            List<String> theDomainList = new ArrayList<>();
            
            for(Pubchem_Compound_Domain theDomain : Pubchem_Compound_Domain.values()) {
                theDomainList.add(theDomain.name());
            }
            
            return theDomainList;
        }
    }

    public enum Pubchem_Output_Form {
        CasNumber("/synonyms/txt", "CAS_number") {
            @Override
            public String getInformation(String theCid) {
                try {
                    URL theUrl = new URL(CompoundCollector.URL_CID_HEADER + theCid + this.URL_TAIL);
                    HttpURLConnection theConnection = (HttpURLConnection)theUrl.openConnection();
                    int theResponseCode = theConnection.getResponseCode();
                    StringBuilder theStringBuilder = new StringBuilder();

                    if (theResponseCode != 400) {
                        BufferedReader theReader = new BufferedReader(new InputStreamReader(theConnection.getInputStream()));
                        String theString = null;

                        while ((theString = theReader.readLine()) != null) {
                            if (theString.matches("[0-9]+\\-[0-9]+\\-[0-9]+")) {
                                return theString;
                            }
                        }
                    }

                    return null;
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }

                return null;
            }
        };

        public final String URL_TAIL;
        public final String MOLECULE_KEY;

        private Pubchem_Output_Form(String theUrlTail, String theMoleculeKey) {
            this.URL_TAIL = theUrlTail;
            this.MOLECULE_KEY = theMoleculeKey;
        }

        public abstract String getInformation(String theCid);
    }

    private enum BasicHeader {
        DataType("Input_data_type"),
        Data("Input_Data"),
        CID("CID")
        ;
        
        public final String KEY;

        private BasicHeader(String KEY) {
            this.KEY = KEY;
        }
        
        public static String[] getKeyArray() {
            List<String> theKeyList = new ArrayList<>();
            
            for(BasicHeader theHeader : BasicHeader.values()) {
                theKeyList.add(theHeader.KEY);
            }
            
            return theKeyList.toArray(new String[theKeyList.size()]);
        }
    }
    
    private List<String> itsInputDataList;
    private String itsInputDataType;
    private List<Pubchem_Output_Form> itsOutputFormList;
    private List<String> itsResultTableHeaderList;
    private MoleculeTableTopComponent itsTableTopComponent;
    private MoleculeStructureViewerTopComponent itsViewerTopComponent;
    private Thread itsThread;

    private static final String URL_HEADER = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/";
    private static final String URL_CID_HEADER = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/cid/";
    private final String STRUCTURE_URL_TAIL = "/record/SDF/?record_type=3d&response_type=display";
    private static final String CID_URL_TAIL = "/cids/txt";
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        this.itsThread = new Thread(this);

        this.itsThread.start();
    }

    @Override
    public void run() {
        this.__calculate();
    }

    @Override
    public boolean cancel() {
        this.itsThread.interrupt();

        return true;
    }

    public void setVariable(List<String> theInputDataList, String theInputDataType, List<Pubchem_Output_Form> theOutputFormList, MoleculeTableTopComponent theTableTopComponent,
            MoleculeStructureViewerTopComponent theViewerTopComponent) {
        this.itsInputDataList = theInputDataList;
        this.itsInputDataType = theInputDataType;
        this.itsOutputFormList = theOutputFormList;
        this.itsTableTopComponent = theTableTopComponent;
        this.itsViewerTopComponent = theViewerTopComponent;
        
        this.__initilizeResultTableHeaderList();
    }

    private void __initilizeResultTableHeaderList() {
        this.itsResultTableHeaderList = new ArrayList<>();
        
        Collections.addAll(this.itsResultTableHeaderList, BasicHeader.getKeyArray());
        
        for (Pubchem_Output_Form theOutputForm : this.itsOutputFormList) {
            this.itsResultTableHeaderList.add(theOutputForm.MOLECULE_KEY);
        }
    }
    private void __calculate() {
        ProgressHandle theProgress = ProgressHandleFactory.createHandle("Search compound in PubChem", this, this);
        IAtomContainerSet theResultMoleculeSet = new AtomContainerSet();
        int theInputDataSize = this.itsInputDataList.size();
        int theCount = 1;

        theProgress.start(theInputDataSize + 1);

        for (String theInputData : this.itsInputDataList) {
            theProgress.progress("Search compound (" + theCount++ + "/" + theInputDataSize + ")");
            List<String> theCidList = this.__getCidList(theInputData);

            for (String theCid : theCidList) {
                IAtomContainer theData = this.__getStructure(theCid);

                if (theData != null) {
                    this.__setInformation(theData, theInputData, theCid);

                    theResultMoleculeSet.addAtomContainer(theData);
                }
            }
        }

        theProgress.progress("Generate Result");
        this.__setResult(theResultMoleculeSet);

        theProgress.finish();
    }

    private void __setResult(IAtomContainerSet theResultMoleculeSet) {
        File theTempFile = new File(Constant.TEMP_DIR + "SBFF_" + System.nanoTime() + "_Search_Pubchem_Result.sdf");

        SDFWriter.writeSDFile(theResultMoleculeSet, theTempFile);
        theTempFile.deleteOnExit();

        this.itsTableTopComponent.setMoleculeFile(theTempFile);
        this.itsViewerTopComponent.openFile(theTempFile);
        this.itsTableTopComponent.setData(this.itsResultTableHeaderList.toArray(new String[this.itsResultTableHeaderList.size()]));
        
        this.itsTableTopComponent.revalidate();
        this.itsViewerTopComponent.revalidate();
    }
    
    private void __setInformation(IAtomContainer theMolecule, String theInputData, String theCid) {
        String theName = theMolecule.getProperty("cdk:Title").toString();
        Map theNewProperties = new HashMap<>();

        theNewProperties.put(Constant.MOLECULE_NAME_KEY, theName);
        theNewProperties.put(BasicHeader.DataType.KEY, this.itsInputDataType);
        theNewProperties.put(BasicHeader.Data.KEY, theInputData);
        theNewProperties.put(BasicHeader.CID.KEY, theCid);

        for (Pubchem_Output_Form theOutputForm : this.itsOutputFormList) {
            theNewProperties.put(theOutputForm.MOLECULE_KEY, theOutputForm.getInformation(theCid));
        }

        theMolecule.setProperties(theNewProperties);
    }

    private IAtomContainer __getStructure(String theCid) {
        try {
            URL theUrl = new URL(CompoundCollector.URL_CID_HEADER + theCid + this.STRUCTURE_URL_TAIL);
            URLConnection theConnection = theUrl.openConnection();

            if (theConnection.getDoInput()) {
                return SDFReader.openMoleculeFile(theConnection.getInputStream()).getAtomContainer(0);
            }

            return null;
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        return null;
    }

    private List<String> __getCidList(String theInputData) {
        try {
            URL theUrl = new URL(this.URL_HEADER + this.itsInputDataType.toLowerCase() + "/" + theInputData + this.CID_URL_TAIL);
            URLConnection theConnection = theUrl.openConnection();

            if (theConnection.getDoInput()) {
                BufferedReader theReader = new BufferedReader(new InputStreamReader(theConnection.getInputStream()));
                List<String> theCidList = new ArrayList<>();
                String theString = null;

                while ((theString = theReader.readLine()) != null) {
                    theCidList.add(theString);
                }

                theReader.close();

                return theCidList;
            }

            return new ArrayList<>();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        return new ArrayList<>();
    }
}
