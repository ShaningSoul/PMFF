package org.bmdrc.ui.util;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class ManipulationTool implements Serializable {

    private static final long serialVersionUID = -4334817453203378044L;

    public static void printCalculationTime(long theTime) {
        long theCalculationTime = (System.currentTimeMillis() - theTime) / 1000;
        long theHour = theCalculationTime / 3600;
        long theMin = (theCalculationTime - theHour * 3600) / 60;
        long theSecond = theCalculationTime - theHour * 3600 - theMin * 60;
        
        System.out.print(theHour + ":" + String.format("%02d", theMin) + ":" + String.format("%02d", theSecond));
    }
    
    public static String getTime(long theTime) {
        long theCalculationTime = (System.currentTimeMillis() - theTime) / 1000;
        long theHour = theCalculationTime / 3600;
        long theMin = (theCalculationTime - theHour * 3600) / 60;
        long theSecond = theCalculationTime - theHour * 3600 - theMin * 60;
        
        return theHour + ":" + String.format("%02d", theMin) + ":" + String.format("%02d", theSecond);
    }
}
