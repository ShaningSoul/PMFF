package org.bmdrc.predock.variable;

import java.io.Serializable;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.sbff.energyfunction.parameter.SbffCalculationParameter;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class DockingParameter implements Serializable {

    private static final long serialVersionUID = 613644122211292041L;

    private ConformerGeneratorParameter itsConformerGeneratorParameter;
    private Double itsGridInterval;
    private Double itsGridShellRadius;
    private Integer itsMaximumGridCountUsedInMatching;
    private Integer itsDockingCountByConformer;
    private Integer itsVisibleResultCount;
    private SbffCalculationParameter itsCalculationParameter;
    //constant default value
    public static final Double DEFAULT_GRID_INTERVAL = 0.1;
    public static final Double DEFAULT_GRID_SHELL_RADIUS = 2.0;
    public static final Integer DEFAULT_NUMBER_OF_GRID_USED_IN_MATCHING = 4;
    public static final Integer DEFAULT_DOCKING_COUNT_BY_CONFORMER = 10;
    public static final Integer DEFAULT_VISIBLE_RESULT_COUNT = 100;
    public static final Double DEFAULT_MINIMUM_ENERGY_VARIANCE = 1e-4;
    public static final Double DEFAULT_MAXIMUM_ENERGY_VARIANCE = 1e2;
    public static final Double DEFAULT_ROTATION_STEP_SIZE = 60.0;

    public DockingParameter() {
        this(new ConformerGeneratorParameter(), DockingParameter.DEFAULT_GRID_INTERVAL, DockingParameter.DEFAULT_GRID_SHELL_RADIUS,
                DockingParameter.DEFAULT_NUMBER_OF_GRID_USED_IN_MATCHING, DockingParameter.DEFAULT_DOCKING_COUNT_BY_CONFORMER,
                DockingParameter.DEFAULT_VISIBLE_RESULT_COUNT);
    }

    public DockingParameter(Protein theProtein, IAtomContainer theLigand) {
        this(new ConformerGeneratorParameter(), DockingParameter.DEFAULT_GRID_INTERVAL, DockingParameter.DEFAULT_GRID_SHELL_RADIUS,
                DockingParameter.DEFAULT_DOCKING_COUNT_BY_CONFORMER,
                DockingParameter.DEFAULT_VISIBLE_RESULT_COUNT, new SbffCalculationParameter(theProtein, theLigand));
    }

    public DockingParameter(Double theMinimumEnergyVariance, Double theMaximumEnergyVariance, Double theRotationStepSize,
            Double theGridInterval, Double theGridShellRadius, Integer theMaximumGridCountUsedInMatching, Integer theDockingCountByConformer,
            Integer theVisibleResultCount) {
        this.itsDockingCountByConformer = theDockingCountByConformer;
        this.itsGridInterval = theGridInterval;
        this.itsGridShellRadius = theGridShellRadius;
        this.itsMaximumGridCountUsedInMatching = theMaximumGridCountUsedInMatching;
        this.itsVisibleResultCount = theVisibleResultCount;
        this.itsCalculationParameter = new SbffCalculationParameter();
        this.itsConformerGeneratorParameter = new ConformerGeneratorParameter(theMinimumEnergyVariance, theMaximumEnergyVariance, theRotationStepSize, theVisibleResultCount,
                this.itsCalculationParameter.getIntraParameter());
    }

    public DockingParameter(ConformerGeneratorParameter theConformerGeneratorParameter, Double theGridInterval, Double theGridShellRadius, Integer theMaximumGridCountUsedInMatching,
            Integer theDockingCountByConformer, Integer theVisibleResultCount) {
        this.itsConformerGeneratorParameter = theConformerGeneratorParameter;
        this.itsDockingCountByConformer = theDockingCountByConformer;
        this.itsGridInterval = theGridInterval;
        this.itsGridShellRadius = theGridShellRadius;
        this.itsMaximumGridCountUsedInMatching = theMaximumGridCountUsedInMatching;
        this.itsVisibleResultCount = theVisibleResultCount;
        this.itsCalculationParameter = new SbffCalculationParameter();
    }

    public DockingParameter(Double theMinimumEnergyVariance, Double theMaximumEnergyVariance,
            Double theRotationStepSize, Double theGridInterval, Double theGridShellRadius, Integer theDockingCountByConformer, Integer theVisibleResultCount,
            SbffCalculationParameter theCalculationParameter) {
        this.itsConformerGeneratorParameter = new ConformerGeneratorParameter(theMinimumEnergyVariance, theMaximumEnergyVariance, theRotationStepSize, theVisibleResultCount,
                theCalculationParameter.getIntraParameter());
        this.itsDockingCountByConformer = theDockingCountByConformer;
        this.itsGridInterval = theGridInterval;
        this.itsGridShellRadius = theGridShellRadius;
        this.itsVisibleResultCount = theVisibleResultCount;
        this.itsCalculationParameter = theCalculationParameter;
    }

    public DockingParameter(ConformerGeneratorParameter theConformerGeneratorParameter, Double theGridInterval, Double theGridShellRadius, Integer theDockingCountByConformer,
            Integer theVisibleResultCount, SbffCalculationParameter theCalculationParameter) {
        this.itsConformerGeneratorParameter = theConformerGeneratorParameter;
        this.itsDockingCountByConformer = theDockingCountByConformer;
        this.itsGridInterval = theGridInterval;
        this.itsGridShellRadius = theGridShellRadius;
        this.itsVisibleResultCount = theVisibleResultCount;
        this.itsCalculationParameter = theCalculationParameter;
    }
    
    public DockingParameter(DockingParameter theDockingParameter) {
        this.itsConformerGeneratorParameter = new ConformerGeneratorParameter(theDockingParameter.itsConformerGeneratorParameter);
        this.itsDockingCountByConformer = theDockingParameter.itsDockingCountByConformer;
        this.itsGridInterval = theDockingParameter.itsGridInterval;
        this.itsGridShellRadius = theDockingParameter.itsGridShellRadius;
        this.itsVisibleResultCount = theDockingParameter.itsVisibleResultCount;
        this.itsCalculationParameter = new SbffCalculationParameter(theDockingParameter.itsCalculationParameter);
    }

    public Double getMinimumEnergyVariance() {
        return this.itsConformerGeneratorParameter.getMinimumEnergyVariance();
    }

    public void setMinimumEnergyVariance(Double theMinimumEnergyVariance) {
        this.itsConformerGeneratorParameter.setMinimumEnergyVariance(theMinimumEnergyVariance);
    }

    public Double getMaximumEnergyVariance() {
        return this.itsConformerGeneratorParameter.getMaximumEnergyVariance();
    }

    public void setMaximumEnergyVariance(Double theMaximumEnergyVariance) {
        this.itsConformerGeneratorParameter.setMaximumEnergyVariance(theMaximumEnergyVariance);
    }

    public Double getRotationStepSize() {
        return this.itsConformerGeneratorParameter.getRotationStepSize();
    }

    public void setRotationStepSize(Double theRotationStepSize) {
        this.itsConformerGeneratorParameter.setRotationStepSize(theRotationStepSize);
    }

    public Double getGridInterval() {
        return this.itsGridInterval;
    }

    public void setGridInterval(Double theGridInterval) {
        this.itsGridInterval = theGridInterval;
    }

    public Double getGridShellRadius() {
        return itsGridShellRadius;
    }

    public void setGridShellRadius(Double theGridShellRadius) {
        this.itsGridShellRadius = theGridShellRadius;
    }

    public Integer getMaximumGridCountUsedInMatching() {
        return itsMaximumGridCountUsedInMatching;
    }

    public void setMaximumGridCountUsedInMatching(Integer theMaximumGridCountUsedInMatching) {
        this.itsMaximumGridCountUsedInMatching = theMaximumGridCountUsedInMatching;
    }

    public Integer getVisibleResultCount() {
        return this.itsVisibleResultCount;
    }

    public void setVisibleResultCount(Integer theVisibleResultCount) {
        this.itsVisibleResultCount = theVisibleResultCount;
    }

    public Integer getDockingCountByConformer() {
        return this.itsDockingCountByConformer;
    }

    public void setDockingCountByConformer(Integer theDockingCountByConformer) {
        this.itsDockingCountByConformer = theDockingCountByConformer;
    }

    public SbffCalculationParameter getCalculationParameter() {
        return this.itsCalculationParameter;
    }

    public void setCalculationParameter(SbffCalculationParameter theCalculationParameter) {
        this.itsCalculationParameter = theCalculationParameter;
    }

    public ConformerGeneratorParameter getConformerGeneratorParameter() {
        return itsConformerGeneratorParameter;
    }

    public void setConformerGeneratorParameter(ConformerGeneratorParameter theConformerGeneratorParameter) {
        this.itsConformerGeneratorParameter = theConformerGeneratorParameter;
    }
}
