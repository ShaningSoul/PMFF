package org.bmdrc.ui.listener;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.Serializable;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.bmdrc.ui.components.MemorialTextField;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class IntegerValueCheckerListener extends FocusAdapter implements Serializable {

    private static final long serialVersionUID = 5048041429479703708L;

    private MemorialTextField itsComponent;

    public IntegerValueCheckerListener(MemorialTextField theComponent) {
        this.itsComponent = theComponent;
    }

    @Override
    public void focusGained(FocusEvent fe) {
        String theValue = this.itsComponent.getText();

        try {
            Integer.parseInt(theValue);

            this.itsComponent.setPreviousText(theValue);
        } catch (Exception ex) {
        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
        String theValue = this.itsComponent.getText();

        try {
            Integer theNumber = Integer.parseInt(theValue);

            if (this.itsComponent.getMinValue() != null && theNumber.compareTo(this.itsComponent.getMinValue().intValue()) < 0) {
                this.__undoText("Minimum value is " + this.itsComponent.getMinValue().intValue());
            }
        } catch (Exception ex) {
            this.__undoText("Need to write integer value\nex) 100");
        }
    }

    private void __undoText(String theMessage) {
        JOptionPane.showMessageDialog(this.itsComponent, theMessage, "Error", JOptionPane.ERROR_MESSAGE);
        this.itsComponent.setText(this.itsComponent.getPreviousText());
        this.itsComponent.revalidate();
    }

}
