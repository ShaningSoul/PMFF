package org.bmdrc.basicchemistry.panel;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicGenerator;
import org.openscience.cdk.renderer.generators.HighlightGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SbffAtomContainerRenderer extends AtomContainerRenderer implements Serializable {

    private static final long serialVersionUID = -2172281710125700302L;
    
    public SbffAtomContainerRenderer() {
        super(SbffAtomContainerRenderer.__getGenerators(), new AWTFontManager());
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
