package org.bmdrc.ui.property;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import java.util.Properties;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class Property extends Properties implements Serializable {

    private static final long serialVersionUID = -8740952295194744255L;

    private static final Property itsProperty = new Property();
    
    //constant String variable
    private final String PROPERTY_FILE_PATH = "program.properties";

    public Property() {
        super();
        this.load();
    }

    public void load() {
        try {
            if (new File(this.PROPERTY_FILE_PATH).exists()) {
                this.load(new BufferedReader(new FileReader(this.PROPERTY_FILE_PATH)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void store() {
        try {
            this.store(new BufferedWriter(new FileWriter(this.PROPERTY_FILE_PATH)), "Main property");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Object setProperty(String key, String value) {
        Object theObject = super.setProperty(key, value);
        
        this.store();
        
        return theObject;
    }
    
    public static void saveProperty(String theKey, String theValue) {
        Property.itsProperty.setProperty(theKey, theValue);
    }
    
    public static Object loadProperty(String theKey) {
        return Property.itsProperty.getProperty(theKey);
    }
}
