package org.bmdrc.sbff.abstracts;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class SbffConstant implements Serializable {

    public static final Double INITIAL_ENERGY = 0.0;
    
    public static final int NOT_ATOM_BOUNDED = 0;
    public static final int ONE_ATOM_BOUNDED = 1;
    public static final int TWO_ATOM_BOUNDED = 2;
    public static final int THREE_ATOM_BOUNDED = 3;
    public static final int FOUR_ATOM_BOUNDED = 4;
    public static final int FIVE_ATOM_BOUNDED = 5;
    public static final int SIX_ATOM_BOUNDED = 6;
    
    public static final double BOND_ORDER_SUM_ONE = 1.0;
    public static final double BOND_ORDER_SUM_TWO = 2.0;
    public static final double BOND_ORDER_SUM_THREE = 3.0;
    public static final double BOND_ORDER_SUM_FOUR = 4.0;
    public static final double BOND_ORDER_SUM_FIVE = 5.0;
    public static final double BOND_ORDER_SUM_SIX = 6.0;
    
    public static final int NEGATIVE_ONE_FORMAL_CHARGE = -1;
}
