package org.bmdrc.basicchemistry.warmup;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.bmdrc.ui.abstracts.Constant;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class InitialWarmup implements Runnable, Serializable {

    private static final long serialVersionUID = -8487465000246746231L;

    private final String RESOURCE_MENU_FILE_PATH = "/org/bmdrc/basicchemistry/resource/jmol.mnu";
    public static final String OUTPUT_MENU_FILE_PATH = Constant.TEMP_DIR + "jmol.mnu";

    @Override
    public void run() {
        this.__copyMenuFile();
    }

    private void __copyMenuFile() {
        try {
            InputStream theInputStream = InitialWarmup.class.getResourceAsStream(this.RESOURCE_MENU_FILE_PATH);
            OutputStream theOutputStream = new FileOutputStream(InitialWarmup.OUTPUT_MENU_FILE_PATH);
            int theReadBytes;
            byte[] theBuffer = new byte[4096];
            
            while((theReadBytes = theInputStream.read(theBuffer)) > 0) {
                theOutputStream.write(theBuffer, 0, theReadBytes);
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
}
