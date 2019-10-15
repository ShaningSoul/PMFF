package org.bmdrc.sbff.energyfunction.parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.abstracts.AbstractCalculationParameter;
import org.bmdrc.basicchemistry.energyfunction.interfaces.ICalculationParameter;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.sbff.energyfunction.Cutoff;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffInterCalculationParameter;
import org.bmdrc.sbff.energyfunction.parameter.interfaces.ISbffIntraCalculationParameter;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class SbffCalculationParameter extends AbstractCalculationParameter implements Serializable, ISbffIntraCalculationParameter, 
        ISbffInterCalculationParameter, ICalculationParameter {

    private static final long serialVersionUID = 3221828327379866913L;

    private SbffIntraCalculationParameter itsIntraParameter;
    private SbffInterCalculationParameter itsInterParameter;

    public SbffCalculationParameter() {
        super();
        
        this.itsIntraParameter = new SbffIntraCalculationParameter();
        this.itsInterParameter = new SbffInterCalculationParameter();
    }
    
    public SbffCalculationParameter(List<Integer> theNotCalculateAtomNumberList) {
        super(theNotCalculateAtomNumberList);
        
        this.itsIntraParameter = new SbffIntraCalculationParameter();
        this.itsInterParameter = new SbffInterCalculationParameter();
    }
    
    public SbffCalculationParameter(Protein theProtein, IAtomContainer theLigand) {
        super(theProtein, theLigand);
        
        this.itsIntraParameter = new SbffIntraCalculationParameter();
        this.itsInterParameter = new SbffInterCalculationParameter();
    }

    public SbffCalculationParameter(Double theDielectricConstant) {
        super(theDielectricConstant);
        
        this.itsIntraParameter = new SbffIntraCalculationParameter(theDielectricConstant);
        this.itsInterParameter = new SbffInterCalculationParameter(theDielectricConstant);
    }

    public SbffCalculationParameter(Double theMinElectroStaticCutoff, Double theMaxElectroStaticCutoff, Double theMinNonbondingCutoff,
            Double theMaxNonbondingCutoff, Double theDielectricConstant) {
        super(theDielectricConstant);
        
        this.itsIntraParameter = new SbffIntraCalculationParameter(theMinElectroStaticCutoff, theMaxElectroStaticCutoff, 
                theMinNonbondingCutoff, theMaxNonbondingCutoff);
        this.itsInterParameter = new SbffInterCalculationParameter(theMinElectroStaticCutoff, theMaxElectroStaticCutoff, theMinNonbondingCutoff, 
                theMaxNonbondingCutoff);
    }
    
    public SbffCalculationParameter(Double theMinElectroStaticCutoff, Double theMaxElectroStaticCutoff, Double theMinNonbondingCutoff,
            Double theMaxNonbondingCutoff, Double theDielectricConstant, List<Integer> theNotCalculateAtomIndexList) {
        super(theDielectricConstant, theNotCalculateAtomIndexList);
        
        this.itsIntraParameter = new SbffIntraCalculationParameter(theMinElectroStaticCutoff, theMaxElectroStaticCutoff, 
                theMinNonbondingCutoff, theMaxNonbondingCutoff);
        this.itsInterParameter = new SbffInterCalculationParameter(theMinElectroStaticCutoff, theMaxElectroStaticCutoff, theMinNonbondingCutoff, 
                theMaxNonbondingCutoff);
    }
    
    public SbffCalculationParameter(Double theMinIntraElectroStaticCutoff, Double theMaxIntraElectroStaticCutoff, Double theMinIntraNonbondingCutoff,
            Double theMaxIntraNonbondingCutoff, Double theMinElectroStaticCutoff, Double theMaxElectroStaticCutoff, Double theMinNonbondingCutoff,
            Double theMaxNonbondingCutoff, Double theDielectricConstant, List<Integer> theNotCalculateAtomIndexList) {
        super(theDielectricConstant, theNotCalculateAtomIndexList);
        
        this.itsIntraParameter = new SbffIntraCalculationParameter(theMinIntraElectroStaticCutoff, theMaxIntraElectroStaticCutoff, 
                theMinIntraNonbondingCutoff, theMaxIntraNonbondingCutoff);
        this.itsInterParameter = new SbffInterCalculationParameter(theMinElectroStaticCutoff, theMaxElectroStaticCutoff, theMinNonbondingCutoff, 
                theMaxNonbondingCutoff);
    }
    
    public SbffCalculationParameter(SbffCalculationParameter theCalculationParameter) {
        super(theCalculationParameter);
        
        this.itsIntraParameter = new SbffIntraCalculationParameter(theCalculationParameter);
        this.itsInterParameter = new SbffInterCalculationParameter(theCalculationParameter);
    }

    public SbffIntraCalculationParameter getIntraParameter() {
        return this.itsIntraParameter;
    }

    public void setIntraParameter(SbffIntraCalculationParameter theIntraParameter) {
        this.itsIntraParameter = theIntraParameter;
    }

    public SbffInterCalculationParameter getInterParameter() {
        return itsInterParameter;
    }

    public void setInterParameter(SbffInterCalculationParameter theInterParameter) {
        this.itsInterParameter = theInterParameter;
    }

    @Override
    public Cutoff getIntraElectroStaticCutoff() {
        return this.itsIntraParameter.getIntraElectroStaticCutoff();
    }

    @Override
    public void setIntraElectroStaticCutoff(Cutoff theIntraElectroStaticCutoff) {
        this.itsIntraParameter.setIntraElectroStaticCutoff(theIntraElectroStaticCutoff);
    }

    @Override
    public Cutoff getIntraNonbondingCutoff() {
        return this.itsIntraParameter.getIntraNonbondingCutoff();
    }

    @Override
    public void setIntraNonbondingCutoff(Cutoff theIntraNonbondingCutoff) {
        this.itsIntraParameter.setIntraNonbondingCutoff(theIntraNonbondingCutoff);
    }

    @Override
    public Cutoff getElectroStaticCutoff() {
        return this.itsInterParameter.getElectroStaticCutoff();
    }

    @Override
    public void setElectroStaticCutoff(Cutoff theElectroStaticCutoff) {
        this.itsInterParameter.setElectroStaticCutoff(theElectroStaticCutoff);
    }

    @Override
    public Cutoff getNonbondingCutoff() {
        return this.itsInterParameter.getNonbondingCutoff();
    }

    @Override
    public void setNonbondingCutoff(Cutoff theNonbondingCutoff) {
        this.itsInterParameter.setNonbondingCutoff(theNonbondingCutoff);
    }

    @Override
    public Double getMinIntraElectroStaticCutoff() {
        return this.itsIntraParameter.getMinIntraElectroStaticCutoff();
    }

    @Override
    public void setMinIntraElectroStaticCutoff(Double theMinCutoff) {
        this.itsIntraParameter.setMinIntraElectroStaticCutoff(theMinCutoff);
    }

    @Override
    public Double getMaxIntraElectroStaticCutoff() {
        return this.itsIntraParameter.getMaxIntraElectroStaticCutoff();
    }

    @Override
    public void setMaxIntraElectroStaticCutoff(Double theMaxCutoff) {
        this.itsIntraParameter.setMaxIntraElectroStaticCutoff(theMaxCutoff);
    }

    @Override
    public Double getMinIntraNonbondingCutoff() {
        return this.itsIntraParameter.getMinIntraNonbondingCutoff();
    }

    @Override
    public void setMinIntraNonbondingCutoff(Double theMinCutoff) {
        this.itsIntraParameter.setMinIntraNonbondingCutoff(theMinCutoff);
    }

    @Override
    public Double getMaxIntraNonbondingCutoff() {
        return this.itsIntraParameter.getMaxIntraNonbondingCutoff();
    }

    @Override
    public void setMaxIntraNonbondingCutoff(Double theMaxCutoff) {
        this.itsIntraParameter.setMaxIntraNonbondingCutoff(theMaxCutoff);
    }

    @Override
    public Double getMinElectroStaticCutoff() {
        return this.itsInterParameter.getMinElectroStaticCutoff();
    }

    @Override
    public void setMinElectroStaticCutoff(Double theMinCutoff) {
        this.itsInterParameter.setMinElectroStaticCutoff(theMinCutoff);
    }

    @Override
    public Double getMaxElectroStaticCutoff() {
        return this.itsInterParameter.getMaxElectroStaticCutoff();
    }

    @Override
    public void setMaxElectroStaticCutoff(Double theMaxCutoff) {
        this.itsInterParameter.setMaxElectroStaticCutoff(theMaxCutoff);
    }

    @Override
    public Double getMinNonbondingCutoff() {
        return this.itsInterParameter.getMinNonbondingCutoff();
    }

    @Override
    public void setMinNonbondingCutoff(Double theMinCutoff) {
        this.itsInterParameter.setMinNonbondingCutoff(theMinCutoff);
    }

    @Override
    public Double getMaxNonbondingCutoff() {
        return this.itsInterParameter.getMaxNonbondingCutoff();
    }

    @Override
    public void setMaxNonbondingCutoff(Double theMaxCutoff) {
        this.itsInterParameter.setMaxNonbondingCutoff(theMaxCutoff);
    }
     
    @Override
    public void setDielectricConstant(Double theDielectricConstant) {
        super.setDielectricConstant(theDielectricConstant);
        
        this.itsIntraParameter.setDielectricConstant(theDielectricConstant);
        this.itsInterParameter.setDielectricConstant(theDielectricConstant);
    }

    @Override
    public AbstractCalculationParameter clone() {
        return new SbffCalculationParameter(this);
    }
    
    
}
