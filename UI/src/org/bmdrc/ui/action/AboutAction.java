/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Help",
        id = "org.bmdrc.ui.action.AboutAction"
)
@ActionRegistration(
        displayName = "#CTL_AboutAction"
)
@ActionReference(path = "Menu/Help", position = 5100)
@Messages("CTL_AboutAction=About")
public final class AboutAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append("Main Developer : Sungbo Hwang (tyamazaki@naver.com)");
        
        JOptionPane.showMessageDialog(null, theStringBuilder.toString(), "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
