package org.bmdrc.basicchemistry.windows.interfaces;

import java.io.Serializable;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public interface IMoleculeViewerPanel extends Serializable {

    void viewMolecule(int theMoleculeIndex);
}
