/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, SungBo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.basicchemistry.vector;

import org.bmdrc.ui.vector.Vector;
import java.io.Serializable;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import org.openscience.cdk.interfaces.IAtom;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class Vector3d extends Vector implements Serializable {
    private static final long serialVersionUID = -14013777890076963L;

    //constant Integer variable
    public static final int X_INDEX = 0;
    public static final int Y_INDEX = 1;
    public static final int Z_INDEX = 2;
    //constant double variable
    private final double INVERSE_FACTOR = -1.0;
    private static final double INITIAL_VALUE = 0.0;

    public Vector3d() {
        this(Vector3d.INITIAL_VALUE, Vector3d.INITIAL_VALUE, Vector3d.INITIAL_VALUE);
    }

    public Vector3d(Double theX, Double theY, Double theZ) {
        super();
        this.add(theX);
        this.add(theY);
        this.add(theZ);
    }

    public Vector3d(IAtom theAtom) {
        this();

        if (theAtom.getPoint3d() != null) {
            this.setX(theAtom.getPoint3d().x);
            this.setY(theAtom.getPoint3d().y);
            this.setZ(theAtom.getPoint3d().z);
        } else {
            this.setX(theAtom.getPoint2d().x);
            this.setY(theAtom.getPoint2d().y);
            this.setZ(0.0);
        }
    }

    public Vector3d(Point3d thePoint3d) {
        this();

        this.setX(thePoint3d.x);
        this.setY(thePoint3d.y);
        this.setZ(thePoint3d.z);
    }
    
    public Vector3d(Point2d thePoint2d) {
        this();
        
        this.setX(thePoint2d.x);
        this.setY(thePoint2d.y);
        this.setZ(0.0);
    }

    public Vector3d(IAtom theStartAtom, IAtom theEndAtom) {
        this();
        
        this.__setPoint3d(theStartAtom);
        this.__setPoint3d(theEndAtom);

        this.setX(theEndAtom.getPoint3d().x - theStartAtom.getPoint3d().x);
        this.setY(theEndAtom.getPoint3d().y - theStartAtom.getPoint3d().y);
        this.setZ(theEndAtom.getPoint3d().z - theStartAtom.getPoint3d().z);

    }
    
    public Vector3d(Vector3d theVector3d) {
        super(theVector3d);
    }

    public Double getX() {
        return this.get(this.X_INDEX);
    }

    public void setX(Double theX) {
        this.set(this.X_INDEX, theX);
    }

    public Double getY() {
        return this.get(this.Y_INDEX);
    }

    public void setY(Double theY) {
        this.set(this.Y_INDEX, theY);
    }

    public Double getZ() {
        return this.get(this.Z_INDEX);
    }

    public void setZ(Double theZ) {
        this.set(this.Z_INDEX, theZ);
    }

    public Vector3d add(Vector3d theVector) {
        this.setX(this.getX() + theVector.getX());
        this.setY(this.getY() + theVector.getY());
        this.setZ(this.getZ() + theVector.getZ());

        return this;
    }

    public Vector3d sbutraction(Vector3d theVector) {
        this.setX(this.getX() - theVector.getX());
        this.setY(this.getY() - theVector.getY());
        this.setZ(this.getZ() - theVector.getZ());

        return this;
    }

    public Vector3d productScalar(double theScalar) {
        this.setX(this.getX() * theScalar);
        this.setY(this.getY() * theScalar);
        this.setZ(this.getZ() * theScalar);

        return this;
    }

    public Vector3d divideScalar(double theScalar) {
        this.setX(this.getX() / theScalar);
        this.setY(this.getY() / theScalar);
        this.setZ(this.getZ() / theScalar);

        return this;
    }

    public Vector3d inverse() {
        this.productScalar(this.INVERSE_FACTOR);
        
        return this;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();

        theStringBuilder.append("X : ").append(String.format("%.4f", this.getX())).append(" Y : ").append(String.format("%.4f", this.getY())).append(" Z : ").append(String.format("%.4f", this.getZ()));

        return theStringBuilder.toString();
    }

    public Point3d toPoint3d() {
        return new Point3d(this.getX(), this.getY(), this.getZ());
    }
    
    public double distance(Vector3d theVector3d) {
        return Math.sqrt(Math.pow(this.getX() - theVector3d.getX(), 2.0) + Math.pow(this.getY() - theVector3d.getY(), 2.0) + Math.pow(this.getZ() - theVector3d.getZ(), 2.0));
    }
    
    private void __setPoint3d(IAtom theAtom) {
        if(theAtom.getPoint3d() == null) {
            theAtom.setPoint3d(new Point3d(theAtom.getPoint2d().x, theAtom.getPoint2d().y, 0.0));
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        Vector3d theObject = (Vector3d)obj;
        
        for(int vi = 0, vEnd = this.size(); vi < vEnd; vi++) {
            if(!theObject.get(vi).equals(this.get(vi))) {
                return false;
            }
        }
        return true;
    }
    
    public Vector3d getUnitVector() {
        return Vector3dCalculator.divideByScalar(this.length(), this);
    }
}
