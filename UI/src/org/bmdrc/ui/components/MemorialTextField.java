package org.bmdrc.ui.components;

import java.io.Serializable;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.bmdrc.ui.listener.DoubleValueCheckerListener;
import org.bmdrc.ui.listener.IntegerValueCheckerListener;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class MemorialTextField extends JTextField implements Serializable {

    private static final long serialVersionUID = 6764657169299351512L;
    
    private String itsPreviousText;
    private Number itsMinValue;

    public MemorialTextField() {
    }
    
    public MemorialTextField(Class theClass) {
        this(theClass, null);
    }
    
    public MemorialTextField(Class theClass, Number theMinValue) {
        this.__addFocuseListener(theClass);
        
        if(theClass.getSuperclass().equals(Number.class)) {
            this.itsMinValue = theMinValue;
        }
    }

    public MemorialTextField(String string) {
        super(string);
    }

    public MemorialTextField(int i) {
        super(i);
    }

    public MemorialTextField(String string, int i) {
        super(string, i);
    }

    public MemorialTextField(Document dcmnt, String string, int i) {
        super(dcmnt, string, i);
    }

    public String getPreviousText() {
        return itsPreviousText;
    }

    public void setPreviousText(String itsPreviousText) {
        this.itsPreviousText = itsPreviousText;
    }

    public Number getMinValue() {
        return this.itsMinValue;
    }

    public void setMinValue(Number theMinValue) {
        this.itsMinValue = theMinValue;
    }
    
    private void __addFocuseListener(Class theClass) {
        if(theClass.equals(Integer.class)) {
            this.addFocusListener(new IntegerValueCheckerListener(this));
        } else if(theClass.equals(Double.class)) {
            this.addFocusListener(new DoubleValueCheckerListener(this));
        }
    }
}
