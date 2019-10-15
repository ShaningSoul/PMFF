/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.basicchemistry.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import org.bmdrc.basicchemistry.filechooser.OpenFileChooser;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.bmdrc.basicchemistry.windows.BasicChemistryWindowAdapter;
import org.bmdrc.basicchemistry.windows.MoleculeStructureViewerTopComponent;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.ui.abstracts.WindowAdapter;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "org.bmdrc.basicchemistry.action.OpenAction"
)
@ActionRegistration(
        iconBase = "org/bmdrc/basicchemistry/resource/molecule.png",
        displayName = "#CTL_OpenAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1000)
    ,
    @ActionReference(path = "Toolbars/File", position = 100)
})
@Messages("CTL_OpenAction=Open")
public final class OpenAction implements ActionListener, Serializable {

    private static final long serialVersionUID = -8081818710253215521L;

    public OpenAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OpenFileChooser theFileChooser = new OpenFileChooser();
        File theSelectedFile = theFileChooser.getSelectedMoleculeFile();

        if (theSelectedFile != null) {
            this.__setWindows(theSelectedFile);
        }
    }

    private void __setWindows(File theFile) {
        MoleculeTableTopComponent theTableTopComponent = BasicChemistryWindowAdapter.setTableTopComponent(theFile, theFile.getName());
        MoleculeStructureViewerTopComponent theViewerTopcomponent = new MoleculeStructureViewerTopComponent(theFile, theTableTopComponent.getName());
        
        WindowAdapter.getOutputMode().dockInto(theTableTopComponent);
        WindowAdapter.getEditorMode().dockInto(theViewerTopcomponent);

        theTableTopComponent.setViewerTopComponent(theViewerTopcomponent);
        theViewerTopcomponent.setTableComponent(theTableTopComponent);

        theTableTopComponent.setMoleculeSet(SDFReader.openMoleculeFile(theFile));
        theTableTopComponent.setMoleculeFile(theFile);

        theTableTopComponent.open();
        theViewerTopcomponent.open();

        theTableTopComponent.requestActive();
        theViewerTopcomponent.requestActive();
    }
}
