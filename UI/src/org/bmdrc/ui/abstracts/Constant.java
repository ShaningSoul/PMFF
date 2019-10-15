package org.bmdrc.ui.abstracts;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class Constant implements Serializable {

    private static final long serialVersionUID = 8083744658406934317L;

    public static final int FIRST_INDEX = 0;
    public static final int SECOND_INDEX = 1;
    public static final int THIRD_INDEX = 2;
    public static final int FOURTH_INDEX = 3;
    
    public static final int X_INDEX = 0;
    public static final int Y_INDEX = 1;
    public static final int Z_INDEX = 2;
    public static final int POSITION_DIMENSION_SIZE = 3;
    
    public static final String ALPHA = "A";
    public static final String BETA = "B";
    public static final String GAMMA = "G";
    public static final String DELTA = "D";
    public static final String EPSILON = "E";
    public static final String ZETA = "Z";
    public static final String ETA = "H";
    public static final String ONE_SYMBOL = "1";
    public static final String TWO_SYMBOL = "2";
    public static final String THREE_SYMBOL = "3";
    
    public static final String MOLECULE_NAME_KEY = "cdk:Title";
    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    public static final String END_LINE = "\n";
    
    public static final double DEFAULT_DIELECTRIC_CONSTANT = 1.0;
    public static final double CONVERT_FROM_J_TO_KCAL = 2.3901e-4;
    public static final double VACUUM_PERMITTIVITY = 8.854e-22;
    public static final double ELECTRON_CHARGE = 1.602e-19;
    
    //constant smooth function range
    public static final double DEFAULT_ELECTROSTATIC_MINIMUM_SCALING_FUNCTION_DISTANCE = 6.0;
    public static final double DEFAULT_ELECTROSTATIC_MAXIMUM_SCALING_FUNCTION_DISTANCE = 12.0;
    public static final double DEFAULT_NONBONDING_MAXIMUM_SCALING_FUNCTION_DISTANCE = 7.0;
    public static final double DEFAULT_NONBONDING_MINIMUM_SCALING_FUNCTION_DISTANCE = 4.0;
    
    public static final double MAXIMUM_DEGREE_ANGLE = 360.0;
}
