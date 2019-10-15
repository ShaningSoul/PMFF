package org.bmdrc.sbff.solvation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.bmdrc.basicchemistry.vector.Vector3d;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class Grid extends Vector3d implements Serializable {

    private static final long serialVersionUID = -270898040952834405L;

    private Map itsProperties;

    public Grid() {
        super();
        this.itsProperties = new HashMap<>();
    }

    public Grid(Double theX, Double theY, Double theZ) {
        super(theX, theY, theZ);
        
        this.itsProperties = new HashMap<>();
    }
    
    public Grid(Grid theGrid) {
        super(theGrid.getX(), theGrid.getY(), theGrid.getZ());
        
        this.itsProperties = new HashMap<>(theGrid.getProperties());
    }

    public Map getProperties() {
        return this.itsProperties;
    }

    public void setProperties(Map theProperties) {
        this.itsProperties = theProperties;
    }
    
    public <Type> Type getProperty(Object theKey, Class<Type> theClass) {
        return theClass.cast(this.itsProperties.get(theKey));
    }
    
    public void setProperty(Object theKey, Object theValue) {
        this.itsProperties.put(theKey, theValue);
    }
}
