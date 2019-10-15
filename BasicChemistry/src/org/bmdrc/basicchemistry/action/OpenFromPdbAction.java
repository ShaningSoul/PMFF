/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.basicchemistry.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.bmdrc.basicchemistry.windows.PDBImportFrame;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "org.bmdrc.basicchemistry.action.OpenFromPdbAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenFromPdbAction"
)
@ActionReference(path = "Menu/File", position = 1012)
@Messages("CTL_OpenFromPdbAction=OpenFromPDB")
public final class OpenFromPdbAction implements ActionListener {

    private PDBImportFrame itsFrame;

    public OpenFromPdbAction() {
        this.itsFrame = new PDBImportFrame();
        this.itsFrame.setVisible(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        this.itsFrame.setVisible(true);
    }
}
