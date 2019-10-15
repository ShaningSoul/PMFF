package org.bmdrc.ui.filechooser.abstracts;

import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.bmdrc.ui.property.Property;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class AbstractSaveFileChooser extends JFileChooser implements Serializable {

    private static final long serialVersionUID = -8083263830746452812L;

    protected static Property itsProperties = new Property();
    
    public AbstractSaveFileChooser(FileFilter... theFileFilters) {
        super();
        this.__initializeFileChooser(theFileFilters);
    }

    
    private void __initializeFileChooser(FileFilter... theFileFilters) {
        this.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        this.setMultiSelectionEnabled(false);
        
        for(FileFilter theFileFilter : theFileFilters) {
            this.addChoosableFileFilter(theFileFilter);
        }
        
        this.setAcceptAllFileFilterUsed(false);
        this.setDialogTitle("Save");
        this.setApproveButtonText("Save");
        
        this._setCurrentDirectory();
    }
    
    protected abstract void _setCurrentDirectory();
}
