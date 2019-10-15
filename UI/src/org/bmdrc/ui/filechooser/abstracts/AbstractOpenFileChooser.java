package org.bmdrc.ui.filechooser.abstracts;

import java.io.File;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.bmdrc.ui.property.Property;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class AbstractOpenFileChooser extends JFileChooser implements Serializable {

    private static final long serialVersionUID = -4104285755262385801L;

    public AbstractOpenFileChooser(String thePropertyKey, FileFilter... theFileFilters) {
        super();
        this.__initializeFileChooser(thePropertyKey, theFileFilters);
    }
    
    private void __initializeFileChooser(String thePropertyKey, FileFilter... theFileFilters) {
        this.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        this.setMultiSelectionEnabled(false);
        
        for(FileFilter theFileFilter : theFileFilters) {
            this.addChoosableFileFilter(theFileFilter);
        }
        
        this.setAcceptAllFileFilterUsed(true);
        this.setDialogTitle("Open");
        this.setApproveButtonText("Open");
        
        this._setCurrentDirectory(thePropertyKey);
    }
    
    protected void _setCurrentDirectory(String thePropertyKey) {
        Object theCurrentDirectoryPath = Property.loadProperty(thePropertyKey);
        
        if(theCurrentDirectoryPath != null) {
            this.setCurrentDirectory(new File(theCurrentDirectoryPath.toString()));
        }
    }
}
