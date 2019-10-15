package org.bmdrc.sbff.windows;

import java.io.File;
import java.io.Serializable;
import org.bmdrc.basicchemistry.windows.BasicChemistryWindowAdapter;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.ui.abstracts.WindowAdapter;
import org.openide.util.NbBundle;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class SbffWindowAdapter implements Serializable {

    private static final long serialVersionUID = -8060640932665920659L;

    public static EnergyCalculationTopComponent getEnergyCalculationTopComponent() {
        return WindowAdapter.getTopComponentInEditor(NbBundle.getMessage(EnergyCalculationTopComponent.class, 
                "CTL_EnergyCalculationTopComponent"), EnergyCalculationTopComponent.class);
    }
}
