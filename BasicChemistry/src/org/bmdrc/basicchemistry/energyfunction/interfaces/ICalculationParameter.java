package org.bmdrc.basicchemistry.energyfunction.interfaces;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public interface ICalculationParameter extends Serializable, Cloneable {

    Double getDielectricConstant();

    void setDielectricConstant(Double theDielectricCosntant);

    List<Integer> getNotCalculableAtomIndexList();

    void setNotCalculableAtomIndexList(List<Integer> theNotCalculableAtomIndexList);
    
    public ICalculationParameter clone();
}
