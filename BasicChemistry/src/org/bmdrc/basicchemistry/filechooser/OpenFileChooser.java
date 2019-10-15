package org.bmdrc.basicchemistry.filechooser;

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
public class OpenFileChooser extends AbstractOpenFileChooser implements Serializable {

    private static final long serialVersionUID = 3430845692143784297L;

    //constant String variable
    private static final String CURRENT_DIRECTORY_KEY = "Basic chemistry open file chooser current directory";
    
    public OpenFileChooser() {
        super(OpenFileChooser.CURRENT_DIRECTORY_KEY, new FileNameExtensionFilter("MDL File(.sd, .sdf)", "sd", "sdf"), new FileNameExtensionFilter("Protein Data Bank File(.pdb)", "pdb"));
    }

    public File getSelectedMoleculeFile() {
        File theSelectedFile = null;
        
        if(this.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            theSelectedFile = this.getSelectedFile();
            
            Property.saveProperty(this.CURRENT_DIRECTORY_KEY, theSelectedFile.getPath());
        }
        
        return theSelectedFile;
    }
}
