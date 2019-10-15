package org.bmdrc.basicmath.variable;

import java.io.Serializable;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class Range<Type extends Number> implements Serializable {

    private static final long serialVersionUID = -8210004629647921758L;

    private Type itsStart;
    private Type itsEnd;

    public Range(Type theStart, Type theEnd) {
        this.itsStart = theStart;
        this.itsEnd = theEnd;
    }

    public Type getStart() {
        return this.itsStart;
    }

    public void setStart(Type theStart) {
        this.itsStart = theStart;
    }

    public Type getEnd() {
        return this.itsEnd;
    }

    public void setItsEnd(Type theEnd) {
        this.itsEnd = theEnd;
    }
}
