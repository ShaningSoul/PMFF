/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */

package org.bmdrc.basicchemistry.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.Serializable;
import javax.swing.JPanel;
import org.bmdrc.basicchemistry.windows.interfaces.IMoleculeViewerPanel;
import org.bmdrc.basicchemistry.warmup.InitialWarmup;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolViewer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class Molecule3dStructurePanel extends JPanel implements Serializable, IMoleculeViewerPanel {

    private static final long serialVersionUID = 2430182561624136807L;

    private JmolViewer itsViewer;
    private JmolAdapter itsAdapter;
    private final Dimension itsDimension;
    private final Rectangle itsRectClip;
    private String itsMenuFilePath;
    
    public Molecule3dStructurePanel() {
        this.itsAdapter = new SmarterJmolAdapter();
        this.itsViewer = JmolViewer.allocateViewer(this, this.itsAdapter);
        this.itsDimension = new Dimension();
        this.itsRectClip = new Rectangle();
        
        this.executeCmd("load menu " + InitialWarmup.OUTPUT_MENU_FILE_PATH);
    }
    
    public Molecule3dStructurePanel(File theMoleculeFile) {
        this.itsAdapter = new SmarterJmolAdapter();
        this.itsViewer = JmolViewer.allocateViewer(this, this.itsAdapter);
        this.itsDimension = new Dimension();
        this.itsRectClip = new Rectangle();
        
        this.openFile(theMoleculeFile);
        
        this.executeCmd("load menu " + InitialWarmup.OUTPUT_MENU_FILE_PATH);
    }

    public void openFile(File theFile) {
        this.itsViewer.openFile(theFile.getAbsolutePath());
        
        System.out.println("AA : " + theFile.getAbsolutePath());
        
        if(theFile.getName().substring(theFile.getName().length()-4).equals(".pdb")) {
            this.executeCmd("cartoon ONLY");
            this.executeCmd("set defaultColors Rasmol");
        }
    }
    
    public void executeCmd(String theRasmolScript) {
        this.itsViewer.evalString("delete model 1");
        
        this.itsViewer.evalString(theRasmolScript);
    }
    
    @Override
    public void paint(Graphics g) {
        this.getSize(this.itsDimension);
        g.getClipBounds(this.itsRectClip);
        
        this.itsViewer.renderScreenImage(g, this.itsDimension.width, this.itsDimension.height);
    }

    @Override
    public void viewMolecule(int theMoleculeIndex) {
        this.itsViewer.evalString("model " + theMoleculeIndex);
    }
}
