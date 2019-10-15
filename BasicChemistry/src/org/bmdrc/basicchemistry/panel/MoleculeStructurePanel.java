/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.windows.interfaces.IMoleculeViewerPanel;
import org.bmdrc.ui.abstracts.Constant;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.BoundsCalculator;
import org.openscience.cdk.renderer.color.IAtomColorer;
import org.openscience.cdk.renderer.color.RasmolColors;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicGenerator;
import org.openscience.cdk.renderer.generators.HighlightGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.renderer.visitor.IDrawVisitor;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class MoleculeStructurePanel extends JPanel implements Serializable, IMoleculeViewerPanel {

    private static final long serialVersionUID = -7172368144843543564L;

    private IAtomContainer itsMolecule;
    private IDrawVisitor itsVisitor;
    private SbffAtomContainerRenderer itsRenderer;

    public MoleculeStructurePanel() {
        this.itsMolecule = new AtomContainer();
        this.itsRenderer = new SbffAtomContainerRenderer();
        
        this.setBackground(Color.WHITE);
    }

    public AtomContainerRenderer getRenderer() {
        return itsRenderer;
    }

    public void setRenderer(SbffAtomContainerRenderer theRenderer) {
        this.itsRenderer = theRenderer;
    }

    public IAtomContainer getMolecule() {
        return itsMolecule;
    }

    public void setMolecule(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        if (this.itsMolecule != null) {
            Rectangle2D theRectangle2D = BoundsCalculator.calculateBounds(this.itsMolecule);
            theRectangle2D.setFrame(0.0, 0.0, this.getWidth(), this.getHeight());

            this.itsVisitor = new AWTDrawVisitor((Graphics2D) g);
            this.itsRenderer.paint(this.itsMolecule, this.itsVisitor, theRectangle2D, true);
        }
    }

    private void __drawMolecule() {
        Rectangle2D theRectangle2D = BoundsCalculator.calculateBounds(this.itsMolecule);
        theRectangle2D.setFrame(0.0, 0.0, this.getWidth(), this.getHeight());

        this.itsVisitor = new AWTDrawVisitor((Graphics2D) this.getGraphics());
        this.itsRenderer.paint(this.itsMolecule, this.itsVisitor, theRectangle2D, true);
    }

    public void showHighlightedAtom(IAtom theAtom) {
        Map<IChemObject, Integer> theMap = new HashMap<>();

        theMap.put(theAtom, Constant.FIRST_INDEX);

        this.itsMolecule.setProperty(HighlightGenerator.ID_MAP, theMap);
        this.repaint();
        this.validate();
    }

    public void showHighlightedAtom(List<IAtom> theAtomList) {
        Map<IChemObject, Integer> theMap = new HashMap<>();

        for (IAtom theAtom : theAtomList) {
            theMap.put(theAtom, Constant.FIRST_INDEX);
        }

        this.itsMolecule.setProperty(HighlightGenerator.ID_MAP, theMap);
        this.repaint();
        this.validate();
    }

    public void viewMolecule(IAtomContainer theMolecule) {
        try {
            if (!this.itsMolecule.equals(theMolecule)) {
                this.itsMolecule = theMolecule;
                StructureDiagramGenerator theGenerator = new StructureDiagramGenerator(this.itsMolecule);

                theGenerator.generateCoordinates();
                this.repaint();
            }
        } catch (CDKException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void viewMolecule(int theMoleculeIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

class MoleculeStructureRenderer extends AtomContainerRenderer implements Serializable {

    private static final long serialVersionUID = -2172281710125700302L;

    public MoleculeStructureRenderer() {
        super(MoleculeStructureRenderer.__getGenerators(), new AWTFontManager());
        this.__initializeParameter();
    }

    private static List<IGenerator<IAtomContainer>> __getGenerators() {
        IGenerator<IAtomContainer> theBasicGenerator = new BasicGenerator();
        IGenerator<IAtomContainer> theHightlightGenerator = new HighlightGenerator();
        List<IGenerator<IAtomContainer>> theList = new ArrayList<>();

        theList.add(theBasicGenerator);
        theList.add(theHightlightGenerator);

        return theList;
    }

    private void __initializeParameter() {
        this.getRenderer2DModel().getParameter(BasicAtomGenerator.AtomColorer.class).setValue(new preMetaboAtomColor());
        this.getRenderer2DModel().set(HighlightGenerator.HighlightPalette.class, HighlightGenerator.createPalette(Color.RED));
    }
}

class preMetaboAtomColor extends RasmolColors implements IAtomColorer, Serializable {

    private static final long serialVersionUID = -5391509366015035072L;

    @Override
    public Color getAtomColor(IAtom theAtom, Color theColor) {
        if (theAtom.getSymbol().equals(AtomInformation.Hydrogen.SYMBOL)) {
            return Color.blue;
        } else {
            return super.getAtomColor(theAtom, theColor);
        }
    }

}
