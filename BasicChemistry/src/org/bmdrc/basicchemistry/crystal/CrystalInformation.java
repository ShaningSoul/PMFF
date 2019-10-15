/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang (tyamazaki@naver.com).
 */
package org.bmdrc.basicchemistry.crystal;

import java.util.Objects;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2015. 11. 27
 */
public class CrystalInformation {

    public static enum CrystalInformationKey {

        SpaceGroup("Space group"), A("a"), B("b"), C("c"), Alpha("Alpha"), Beta("Beta"), Gamma("Gamma");

        public final String KEY;

        private CrystalInformationKey(String theKey) {
            this.KEY = theKey;
        }
    }

    private SpaceGroup itsSpaceGroup;
    private Vector3d itsCellDimensionVector;
    private Vector3d itsCoordinateAngleVector;
    //constant Integer variable
    private final int A_INDEX = 0;
    private final int B_INDEX = 1;
    private final int C_INDEX = 2;
    private final int ALPHA_INDEX = 0;
    private final int BETA_INDEX = 1;
    private final int GAMMA_INDEX = 2;

    public CrystalInformation() {
        this(null, new Vector3d(), new Vector3d());
    }
    
    public CrystalInformation(SpaceGroup theSpaceGroup) {
        this(theSpaceGroup, new Vector3d(), new Vector3d());
    }

    public CrystalInformation(SpaceGroup theSpaceGroup, double theA, double theB, double theC, double theAlpha, double theBeta, double theGamma) {
        this(theSpaceGroup, new Vector3d(theA, theB, theC), new Vector3d(theAlpha, theBeta, theGamma));
    }

    public CrystalInformation(SpaceGroup theSpaceGroup, Vector3d theCellDimensionVector, Vector3d theCoordinateAngleVector) {
        this.itsSpaceGroup = theSpaceGroup;
        this.itsCellDimensionVector = theCellDimensionVector;
        this.itsCoordinateAngleVector = theCoordinateAngleVector;
    }
    
    public CrystalInformation(CrystalInformation theCrystalInformation) {
        this.itsSpaceGroup = theCrystalInformation.getSpaceGroup();
        this.itsCellDimensionVector = new Vector3d(theCrystalInformation.getCellDimensionVector());
        this.itsCoordinateAngleVector = new Vector3d(theCrystalInformation.getCoordinateAngleVector());
    }
    
    public CrystalInformation(IAtomContainer theMolecule) {
        if (this.__containInformationProperty(theMolecule)) {
            this.itsSpaceGroup = SpaceGroup.getSpaceGroup(theMolecule.getProperty(CrystalInformation.CrystalInformationKey.SpaceGroup.KEY).toString());
            this.itsCellDimensionVector = new Vector3d(Double.parseDouble(theMolecule.getProperty(CrystalInformation.CrystalInformationKey.A.KEY).toString()),
                    Double.parseDouble(theMolecule.getProperty(CrystalInformation.CrystalInformationKey.B.KEY).toString()),
                    Double.parseDouble(theMolecule.getProperty(CrystalInformation.CrystalInformationKey.C.KEY).toString()));
            this.itsCoordinateAngleVector = new Vector3d(Double.parseDouble(theMolecule.getProperty(CrystalInformation.CrystalInformationKey.Alpha.KEY).toString()),
                    Double.parseDouble(theMolecule.getProperty(CrystalInformation.CrystalInformationKey.Beta.KEY).toString()),
                    Double.parseDouble(theMolecule.getProperty(CrystalInformation.CrystalInformationKey.Gamma.KEY).toString()));
        } else {
            System.err.println(theMolecule.getProperty("cdk:Title") + " Molecule does not contain crystal information");
        }
    }

    public SpaceGroup getSpaceGroup() {
        return itsSpaceGroup;
    }

    public void setSpaceGroup(SpaceGroup theSpaceGroup) {
        this.itsSpaceGroup = theSpaceGroup;
    }

    public Vector3d getCellDimensionVector() {
        return itsCellDimensionVector;
    }

    public void setCellDimensionVector(Vector3d theCellDimensionVector) {
        this.itsCellDimensionVector = theCellDimensionVector;
    }

    public Vector3d getCoordinateAngleVector() {
        return itsCoordinateAngleVector;
    }

    public void setCoordinateAngleVector(Vector3d theCoordinateAngleVector) {
        this.itsCoordinateAngleVector = theCoordinateAngleVector;
    }

    public double getAInCellDimension() {
        return this.itsCellDimensionVector.get(this.A_INDEX);
    }

    public void setAInCellDimension(double theA) {
        this.itsCellDimensionVector.set(this.A_INDEX, theA);
    }

    public double getBInCellDimension() {
        return this.itsCellDimensionVector.get(this.B_INDEX);
    }

    public void setBInCellDimension(double theB) {
        this.itsCellDimensionVector.set(this.B_INDEX, theB);
    }

    public double getCInCellDimension() {
        return this.itsCellDimensionVector.get(this.C_INDEX);
    }

    public void setCInCellDimension(double theC) {
        this.itsCellDimensionVector.set(this.C_INDEX, theC);
    }

    public double getAlpha() {
        return this.itsCoordinateAngleVector.get(this.ALPHA_INDEX);
    }

    public void setAlpha(double theAlpha) {
        this.itsCoordinateAngleVector.set(this.ALPHA_INDEX, theAlpha);
    }

    public double getBeta() {
        return this.itsCoordinateAngleVector.get(this.BETA_INDEX);
    }

    public void setBeta(double theBeta) {
        this.itsCoordinateAngleVector.set(this.BETA_INDEX, theBeta);
    }

    public double getGamma() {
        return this.itsCoordinateAngleVector.get(this.GAMMA_INDEX);
    }

    public void setGamma(double theGamma) {
        this.itsCoordinateAngleVector.set(this.GAMMA_INDEX, theGamma);
    }

    private boolean __containInformationProperty(IAtomContainer theMolecule) {
        for (CrystalInformationKey theKey : CrystalInformation.CrystalInformationKey.values()) {
            if (!theMolecule.getProperties().containsKey(theKey.KEY)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.itsSpaceGroup);
        hash = 31 * hash + Objects.hashCode(this.itsCellDimensionVector);
        hash = 31 * hash + Objects.hashCode(this.itsCoordinateAngleVector);
        
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
        final CrystalInformation other = (CrystalInformation) obj;
        if (this.itsSpaceGroup != other.itsSpaceGroup) {
            return false;
        }
        if (!Objects.equals(this.itsCellDimensionVector, other.itsCellDimensionVector)) {
            return false;
        }
        if (!Objects.equals(this.itsCoordinateAngleVector, other.itsCoordinateAngleVector)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder theStringBuilder = new StringBuilder();
        
        theStringBuilder.append("Space group : ").append(this.itsSpaceGroup.NAME).append(" ");
        theStringBuilder.append("Cell Dimension Length : ").append(this.itsCellDimensionVector).append(" ");
        theStringBuilder.append("Cell Angle : ").append(this.itsCoordinateAngleVector);
        
        return theStringBuilder.toString();
    }
}
