package org.bmdrc.sbff.energyfunction.abstracts;

import java.io.Serializable;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractUsableInMinimizationInEnergyFunction;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.sbff.energyfunction.parameter.SbffInterCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.SbffIntraCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffInterCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class AbstractInterEnergyFunction extends AbstractUsableInMinimizationInEnergyFunction<ISbffInterCalculationParameter> implements Serializable {

    private static final long serialVersionUID = 3261047069814497962L;

    public AbstractInterEnergyFunction(IAtomContainer theMolecule) {
        this(theMolecule, new MoveTogetherAtomNumberList(theMolecule));
    }

    public AbstractInterEnergyFunction(IAtomContainer theMolecule, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        this(theMolecule, new SbffInterCalculationParameter(), theMoveTogetherAtomNumberMap);
    }

    public AbstractInterEnergyFunction(IAtomContainer theMolecule, ISbffInterCalculationParameter theCalculationParameter, 
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        super(theMolecule, theCalculationParameter, theMoveTogetherAtomNumberMap);
    }
}
