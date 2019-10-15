package org.bmdrc.basicchemistry.action.engine;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import org.bmdrc.basicchemistry.windows.MoleculeStructureViewerTopComponent;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.abstracts.WindowAdapter;
import org.bmdrc.ui.util.TwoDimensionList;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class PDBImporter extends AbstractAction implements Serializable, Runnable, Cancellable {

    private String itsPdbId;
    private Thread itsThread;
    private MoleculeTableTopComponent itsTableTopComponent;
    private MoleculeStructureViewerTopComponent itsViewerTopComponent;

    public static final String URL_HEADER = "https://files.rcsb.org/view/";
    public static final String URL_TAIL = ".pdb";

    public String getPdbId() {
        return this.itsPdbId;
    }

    public void setPdbId(String thePdbId) {
        this.itsPdbId = thePdbId;
    }

    public void setVariable(String thePdbId, MoleculeTableTopComponent theTableTopComponent, MoleculeStructureViewerTopComponent theViewerTopComponent) {
        this.itsPdbId = thePdbId;
        this.itsTableTopComponent = theTableTopComponent;
        this.itsViewerTopComponent = theViewerTopComponent;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.itsThread = new Thread(this);

        this.itsThread.start();
    }

    @Override
    public void run() {
        this.__importPdb();
    }

    @Override
    public boolean cancel() {
        this.itsThread.interrupt();

        return true;
    }

    public void importPdb(String thePdbId) {
        this.itsPdbId = thePdbId;
        this.__importPdb();
    }

    private void __importPdb() {
        try {
            ProgressHandle theProgress = ProgressHandleFactory.createHandle("Search PDB information", this, this);

            theProgress.start(1);

            URL theUrl = new URL(PDBImporter.URL_HEADER + this.itsPdbId.toUpperCase() + PDBImporter.URL_TAIL);
            File theTempFile = new File(Constant.TEMP_DIR + "SBFF_" + System.nanoTime() + "_PDB_Importer.pdb");
            BufferedReader theReader = new BufferedReader(new InputStreamReader(theUrl.openStream()));
            StringBuilder theStringBuilder = new StringBuilder();
            String theString = null;

            while ((theString = theReader.readLine()) != null) {
                theStringBuilder.append(theString).append("\n");
            }

            BufferedWriter theWriter = new BufferedWriter(new FileWriter(theTempFile));

            theWriter.flush();
            theWriter.write(theStringBuilder.toString());
            theWriter.close();

            this.__setWindows(theTempFile);
            
            theTempFile.deleteOnExit();
            
            theProgress.progress(1);
            theProgress.finish();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void __setWindows(File theTempFile) {
        this.itsTableTopComponent.setMoleculeFile(theTempFile);
        this.itsViewerTopComponent.openFile(theTempFile);

        this.__setTable();

        this.itsTableTopComponent.revalidate();
        this.itsViewerTopComponent.revalidate();
    }

    private void __setTable() {
        TwoDimensionList<String> theData2dList = new TwoDimensionList<>();
        List<String> theDataList = new ArrayList<>();

        Collections.addAll(theDataList, "1", this.itsPdbId);
        theData2dList.add(theDataList);

        this.itsTableTopComponent.setData(theData2dList);
    }
}
