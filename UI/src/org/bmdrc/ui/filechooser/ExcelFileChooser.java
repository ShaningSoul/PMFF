package org.bmdrc.ui.filechooser;

import java.io.File;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.bmdrc.ui.filechooser.abstracts.AbstractOpenFileChooser;
import org.bmdrc.ui.property.Property;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class ExcelFileChooser extends AbstractOpenFileChooser implements Serializable {

    private static final long serialVersionUID = 1242330491944211282L;

    private String itsDirectoryKey;
    
    public ExcelFileChooser(String thePropertyDirectoryName) {
        super(thePropertyDirectoryName, new FileNameExtensionFilter("Excel File(.xlsx)", "xlsx"));
        
        this.itsDirectoryKey = thePropertyDirectoryName;
    }
    
    @Override
    protected void _setCurrentDirectory(String thePropertyDirectoryName) {
        Object theCurrentDirectoryPath = Property.loadProperty(thePropertyDirectoryName);
        
        if(theCurrentDirectoryPath != null) {
            this.setCurrentDirectory(new File(theCurrentDirectoryPath.toString()));
        }
    }
    
    public File getSelectedExcelFile() {
        File theSelectedFile = null;
        
        if(this.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            theSelectedFile = this.getSelectedFile();
            
            Property.saveProperty(this.itsDirectoryKey, theSelectedFile.getPath());
        }
        
        return theSelectedFile;
    }
}
