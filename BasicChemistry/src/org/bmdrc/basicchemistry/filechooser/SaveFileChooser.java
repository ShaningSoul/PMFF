package org.bmdrc.basicchemistry.filechooser;

import java.io.File;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.bmdrc.basicchemistry.windows.MoleculeStructureViewerTopComponent;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.ui.abstracts.WindowAdapter;
import org.bmdrc.ui.filechooser.abstracts.AbstractSaveFileChooser;
import org.bmdrc.ui.property.Property;
import org.openide.windows.TopComponent;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class SaveFileChooser extends AbstractSaveFileChooser implements Serializable {

    private static final long serialVersionUID = 7605793454968798885L;

    //constant String variable
    private final String CURRENT_DIRECTORY_KEY = "Basic chemistry save file chooser current directory";
    
    public SaveFileChooser() {
        super(SaveFileChooser.__getBasicDataFormat(), new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
    }
    
    private static FileNameExtensionFilter __getBasicDataFormat() {
        TopComponent[] theTopComponents = WindowAdapter.getOutputMode().getTopComponents();
        
        for(TopComponent theTopComponent : theTopComponents) {
            if(theTopComponent instanceof MoleculeTableTopComponent && theTopComponent.isRequestFocusEnabled()) {
                MoleculeTableTopComponent theMoleculeTableTopComponent = (MoleculeTableTopComponent)theTopComponent;
                String theFileName = theMoleculeTableTopComponent.getMoleculeFile().getName();
                String theSuffix = theFileName.substring(theFileName.lastIndexOf(".")+1);
                
                if(theSuffix.equals("pdb")) {
                    return new FileNameExtensionFilter("Protein Data Bank Files (*.pdb)", "pdb");
                } else if(theSuffix.equals(".sd") || theSuffix.equals(".sdf")) {
                    return new FileNameExtensionFilter("Molecule Files (*.sd, *.sdf)", "sd", "sdf");
                }
            }
        }
        
        return null;
    } 
    
    @Override
    protected void _setCurrentDirectory() {
        Object theCurrentDirectoryPath = Property.loadProperty(this.CURRENT_DIRECTORY_KEY);
        
        if(theCurrentDirectoryPath != null) {
            this.setCurrentDirectory(new File(theCurrentDirectoryPath.toString()));
        }
    }
    
    public File getSelectedSaveFile() {
        File theSelectedFile = null;
        
        if(this.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            theSelectedFile = this.getSelectedFile();
            
            Property.saveProperty(this.CURRENT_DIRECTORY_KEY, theSelectedFile.getPath());
        }
        
        return theSelectedFile;
    }
}
