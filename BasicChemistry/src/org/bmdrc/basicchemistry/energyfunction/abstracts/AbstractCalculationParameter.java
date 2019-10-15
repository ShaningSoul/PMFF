package org.bmdrc.basicchemistry.energyfunction.abstracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.energyfunction.interfaces.ICalculationParameter;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.ui.abstracts.Constant;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class AbstractCalculationParameter implements Serializable, ICalculationParameter {

    private static final long serialVersionUID = -8130095266485309661L;

    protected Double itsDielectricConstant;
    protected List<Integer> itsNotCalculableAtomIndexList;

    public AbstractCalculationParameter() {
        this(Constant.DEFAULT_DIELECTRIC_CONSTANT);
    }

    public AbstractCalculationParameter(List<Integer> theNotCalculableAtomIndexList) {
        this(Constant.DEFAULT_DIELECTRIC_CONSTANT, theNotCalculableAtomIndexList);
    }
    
    public AbstractCalculationParameter(Double theDielectricCostant) {
        this(theDielectricCostant, new ArrayList<Integer>());
    }
    
    public AbstractCalculationParameter(Double theDielectricCostant, List<Integer> theNotCalculableAtomIndexList) {
        this.itsDielectricConstant = theDielectricCostant;
        this.itsNotCalculableAtomIndexList = theNotCalculableAtomIndexList;
    }
    
    public AbstractCalculationParameter(ICalculationParameter theCalculationParameter) {
        this.itsDielectricConstant = theCalculationParameter.getDielectricConstant();
        this.itsNotCalculableAtomIndexList = new ArrayList<>(theCalculationParameter.getNotCalculableAtomIndexList());
    }
    
    public AbstractCalculationParameter(Protein theProtein, IAtomContainer theLigand) {
        this.itsDielectricConstant = Constant.DEFAULT_DIELECTRIC_CONSTANT;
        
        this.generateNotCalculableAtomIndexList(theProtein, theLigand);
    }
    
    public AbstractCalculationParameter(Protein theProtein, IAtomContainer theLigand, Double theDielectricConstant) {
        this.itsDielectricConstant = theDielectricConstant;
        
        this.generateNotCalculableAtomIndexList(theProtein, theLigand);
    }

    @Override
    public Double getDielectricConstant() {
        return this.itsDielectricConstant;
    }

    @Override
    public void setDielectricConstant(Double theDielectricConstant) {
        this.itsDielectricConstant = theDielectricConstant;
    }

    @Override
    public List<Integer> getNotCalculableAtomIndexList() {
        return this.itsNotCalculableAtomIndexList;
    }

    @Override
    public void setNotCalculableAtomIndexList(List<Integer> theNotCalculableAtomIndexList) {
        this.itsNotCalculableAtomIndexList = theNotCalculableAtomIndexList;
    }
    
    public abstract AbstractCalculationParameter clone();
    
    public void generateNotCalculableAtomIndexList(Protein theProtein, IAtomContainer theLigand) {
        int theLigandAtomCount = theLigand.getAtomCount();
        
        this.itsNotCalculableAtomIndexList = new ArrayList<Integer>();
        
        for(int ai = 0, aEnd = theProtein.getMolecule().getAtomCount(); ai < aEnd; ai++) {
            this.itsNotCalculableAtomIndexList.add(ai+theLigandAtomCount);
        }
    }
}
