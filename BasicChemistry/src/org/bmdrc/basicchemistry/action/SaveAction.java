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
import org.bmdrc.basicchemistry.filechooser.SaveFileChooser;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.basicchemistry.writer.SDFWriter;
import org.bmdrc.ui.abstracts.WindowAdapter;
import org.bmdrc.ui.writer.XlsxWriter;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
        category = "File",
        id = "org.bmdrc.basicchemistry.action.SaveAction"
)
@ActionRegistration(
        iconBase = "org/bmdrc/basicchemistry/resource/disk.png",
        displayName = "#CTL_SaveAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1025, separatorAfter = 1050)
    ,
  @ActionReference(path = "Toolbars/File", position = 200)
})
@Messages("CTL_SaveAction=Save")
public final class SaveAction implements ActionListener, Serializable {

    private static final long serialVersionUID = -3980157466002342829L;

    @Override
    public void actionPerformed(ActionEvent e) {
        SaveFileChooser theFileChooser = new SaveFileChooser();
        File theSelectedFile = theFileChooser.getSelectedSaveFile();
        MoleculeTableTopComponent theMoleculeTableTopComponent = this.__getShowingTopComponent();
        
        if (theSelectedFile != null) {
            if(this.__isAcceptableFile(theFileChooser, "sdf", "sd")) {
                SDFWriter.writeSDFile(theMoleculeTableTopComponent.getMoleculeSet(), this.__getNormalFile(theSelectedFile, "sdf"));
            } else if(this.__isAcceptableFile(theFileChooser, "xlsx")) {
                XlsxWriter.write(this.__getNormalFile(theSelectedFile, "xlsx").getAbsolutePath(), theMoleculeTableTopComponent.getData());
            } else {
                System.err.println("Error!!!!!!!!!!1");
            }
        }
    }
    
    private File __getNormalFile(File theSelectedFile, String theSuffix) {
        String theFileName = theSelectedFile.getName();
        
        if(!theFileName.substring(theFileName.length()-theSuffix.length()-1).equals("."+theSuffix)) {
            return new File(theSelectedFile.getAbsoluteFile() + "." + theSuffix);
        }
        
        return theSelectedFile;
    }
    
    private boolean __isAcceptableFile(SaveFileChooser theFileChooser, String... theSuffixs) {
        for(String theSuffix : theSuffixs) {
            if(theFileChooser.accept(new File("a." + theSuffix))) {
                return true;
            }
        }
        
        return false;
    }
    
    private MoleculeTableTopComponent __getShowingTopComponent() {
        TopComponent[] theTopComponents = WindowAdapter.getOutputMode().getTopComponents();
        
        for(TopComponent theTopComponent : theTopComponents) {
            if(theTopComponent.isShowing() && theTopComponent instanceof MoleculeTableTopComponent) {
                return (MoleculeTableTopComponent)theTopComponent;
            }
        }
        
        return null;
    }
}
