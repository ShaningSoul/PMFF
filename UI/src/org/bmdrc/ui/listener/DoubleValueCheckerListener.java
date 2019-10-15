package org.bmdrc.ui.listener;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.Serializable;
import javax.swing.JOptionPane;
import org.bmdrc.ui.components.MemorialTextField;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class DoubleValueCheckerListener extends FocusAdapter implements Serializable {

    private static final long serialVersionUID = 5048041429479703708L;

    private MemorialTextField itsComponent;

    public DoubleValueCheckerListener(MemorialTextField theComponent) {
        this.itsComponent = theComponent;
    }

    @Override
    public void focusGained(FocusEvent fe) {
        String theValue = this.itsComponent.getText();
        
        try {
            Double.parseDouble(theValue);
            
            this.itsComponent.setPreviousText(this.itsComponent.getText());
        } catch (Exception ex) {
        }
    }

    
    @Override
    public void focusLost(FocusEvent fe) {
        String theValue = this.itsComponent.getText();
        
        try {
            Double theNumber = Double.parseDouble(theValue);
            
            if(this.itsComponent.getMinValue() != null && theNumber.compareTo(this.itsComponent.getMinValue().doubleValue()) < 0) {
                this.__undoText("Minimum value is " + this.itsComponent.getMinValue());
            }
        } catch (Exception ex) {
            this.__undoText("Need to write double value \nex)1.0");
        }
    }
    
    private void __undoText(String theMessage) {
        JOptionPane.showMessageDialog(this.itsComponent, theMessage, "Error", JOptionPane.ERROR_MESSAGE);
        this.itsComponent.setText(this.itsComponent.getPreviousText());
        this.itsComponent.revalidate();
    }
}
