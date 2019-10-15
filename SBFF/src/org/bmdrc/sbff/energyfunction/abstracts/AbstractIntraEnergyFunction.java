package org.bmdrc.sbff.energyfunction.abstracts;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.bmdrc.sbff.intraenergy.calculableset.CalculableIntraNonbondingList;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class AbstractIntraEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction<ISbffIntraCalculationParameter> implements Serializable {

    private static final long serialVersionUID = 4025309901934104560L;

    public AbstractIntraEnergyFunction(IAtomContainer theMolecule) {
        this(theMolecule, new MoveTogetherAtomNumberList(theMolecule));
    }

    public AbstractIntraEnergyFunction(IAtomContainer theMolecule, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, new SbffIntraCalculationParameter(), theMoveTogetherAtomNumberMap);
    }

    public AbstractIntraEnergyFunction(IAtomContainer theMolecule, ISbffIntraCalculationParameter theCalculationParameter, 
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
    }
}
