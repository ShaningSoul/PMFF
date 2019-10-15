package org.bmdrc.basicchemistry.windows;

import java.io.File;
import java.io.Serializable;
import org.bmdrc.ui.abstracts.WindowAdapter;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class BasicChemistryWindowAdapter implements Serializable {

    private static final long serialVersionUID = 5673673460343459752L;

    public static MoleculeStructureViewerTopComponent getMoleculeStructureViewerTopComponent(String theName) {
        return WindowAdapter.getTopComponentInEditor(theName, MoleculeStructureViewerTopComponent.class);
    }
    
    public static MoleculeTableTopComponent getMoleculeTableTopComponent(String theName) {
        return WindowAdapter.getTopComponentInOutput(theName, MoleculeTableTopComponent.class);
    }
    
    public static Molecule3dStructureViewerTopComponent getMolecule3dStructureViewerTopComponent(String theName) {
        return WindowAdapter.getTopComponentInEditor(theName, Molecule3dStructureViewerTopComponent.class);
    }
    
    public static MoleculeTableTopComponent setTableTopComponent(File theFile, String theWindowName) {
        MoleculeTableTopComponent theTableTopComponent = BasicChemistryWindowAdapter.getMoleculeTableTopComponent(theWindowName);
        
        if (theTableTopComponent == null) {
            theTableTopComponent = new MoleculeTableTopComponent(theFile, theWindowName);
        } else {
            int theIndex = 1;
            String theNewComponentName = theWindowName + " (" + theIndex++ + ")";

            while ((theTableTopComponent = BasicChemistryWindowAdapter.getMoleculeTableTopComponent(theNewComponentName)) != null) {
                theNewComponentName = theWindowName + " (" + theIndex++ + ")";
            }
        }
        
        return theTableTopComponent;
    }
}
