/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2015, Sungbo Hwang <tyamazaki@naver.com>.
 */
package org.bmdrc.sbff.solvation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicmath.matrix.Matrix;
import org.bmdrc.sbff.atomtype.MpeoeAtomTypeGenerator;
import org.bmdrc.sbff.parameter.solvation.VanDerWaalsParameterList;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 */
public class SurfaceGridGenerator implements Serializable {

    private static final long serialVersionUID = -8907479529985047668L;

    private IAtomContainer itsMolecule;
    private Double itsShsell;
    private Double itsLR;
    private Vector3d itsMaximumVector;
    private Vector3d itsMinimumVector;
    private VanDerWaalsParameterList itsParameterList;
    private Map<Double, GridList> itsSphereGrid;
    private List<GridList> itsSurfaceGridList;
    private TwoDimensionList<TwoDimensionList<Integer>> itsGrid4dList;
    private TwoDimensionList<Integer> itsGridIndex2dList;
    //constant Integer variable
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    private final int THIRD_INDEX = 2;
    //constant String variable
    private final String SURFACE_GRID_RANGE = "Surface_grid_range";
    public static final String KEY_CONTAINED_ATOM = "Contained atom key";

    public SurfaceGridGenerator() {
        this.itsMolecule = new AtomContainer();
        this.itsShsell = SphereGridGenerator.INITIAL_SHELL;
        this.itsLR = 0.0;
        this.itsMaximumVector = new Vector3d();
        this.itsMinimumVector = new Vector3d();
        this.itsParameterList = new VanDerWaalsParameterList();
    }

    public SurfaceGridGenerator(Double theShsell) {
        this.itsMolecule = new AtomContainer();
        this.itsShsell = theShsell;
        this.itsLR = 0.0;
        this.itsMaximumVector = new Vector3d();
        this.itsMinimumVector = new Vector3d();
        this.itsParameterList = new VanDerWaalsParameterList();
    }

    public SurfaceGridGenerator(IAtomContainer theMolecule) {
        this.itsMolecule = theMolecule;
        this.itsShsell = SphereGridGenerator.INITIAL_SHELL;
        this.itsLR = 0.0;
        this.itsMaximumVector = new Vector3d();
        this.itsMinimumVector = new Vector3d();
        this.itsParameterList = new VanDerWaalsParameterList();
    }

    public SurfaceGridGenerator(IAtomContainer theMolecule, Double theShsell) {
        this.itsMolecule = theMolecule;
        this.itsShsell = theShsell;
        this.itsLR = 0.0;
        this.itsMaximumVector = new Vector3d();
        this.itsMinimumVector = new Vector3d();
        this.itsParameterList = new VanDerWaalsParameterList();
    }

    public List<GridList> draw(IAtomContainer theMolecule) {
        return this.draw(theMolecule, SphereGridGenerator.INITIAL_SHELL, SphereGridGenerator.INITIAL_INTERVAL);
    }
    
    public List<GridList> draw(IAtomContainer theMolecule, Double theShell, Double theInterval) {
        this.itsMolecule = theMolecule;
        this.itsShsell = theShell;
        this.itsSurfaceGridList = new ArrayList<>();
        this.itsLR = 0.0;

        this.__initializeSphereGrid(theInterval);
        this.__inputAtomInformation();
        this.__convertSphereToSurface();
        this.__makeGrid();
        this.__allocation();
        this.__adjacency();
        this.__move();

        return this.itsSurfaceGridList;
    }

    private void __move() {
        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            int theIndex = 0;
            
            for (int gi = 0, gEnd = this.itsSurfaceGridList.get(ai).size(); gi < gEnd; gi++) {
                Grid theNewGrid = new Grid(this.itsSurfaceGridList.get(ai).get(gi).getX() + this.itsMolecule.getAtom(ai).getPoint3d().x,
                        this.itsSurfaceGridList.get(ai).get(gi).getY() + this.itsMolecule.getAtom(ai).getPoint3d().y,
                        this.itsSurfaceGridList.get(ai).get(gi).getZ() + this.itsMolecule.getAtom(ai).getPoint3d().z);
                
                theNewGrid.setProperty(SurfaceGridGenerator.KEY_CONTAINED_ATOM, this.itsMolecule.getAtom(ai));
                
                this.itsSurfaceGridList.get(ai).set(gi, theNewGrid);

                theIndex++;
            }
        }
    }

    private void __adjacency() {
        for (int ai = 0, aEnd = this.itsMolecule.getAtomCount(); ai < aEnd; ai++) {
            Set<Integer> theOverlapAtomIndexSet = this.__getOverlapAtomIndexSet(ai);

            for (Integer theAtomIndex : theOverlapAtomIndexSet) {
                if (theAtomIndex < ai) {
                    this.__overlap(this.itsMolecule.getAtom(ai), this.itsMolecule.getAtom(theAtomIndex));
                } else {
                    break;
                }
            }
        }
    }

    private void __overlap(IAtom theAtom, IAtom theOverlapAtom) {
        double theDX = theOverlapAtom.getPoint3d().x - theAtom.getPoint3d().x;
        double theDY = theOverlapAtom.getPoint3d().y - theAtom.getPoint3d().y;
        double theDZ = theOverlapAtom.getPoint3d().z - theAtom.getPoint3d().z;
        double theDistance3d = theAtom.getPoint3d().distance(theOverlapAtom.getPoint3d());

        if (theDistance3d < (Double) theAtom.getProperty(this.SURFACE_GRID_RANGE) + (Double) theOverlapAtom.getProperty(this.SURFACE_GRID_RANGE)) {
            double theDistance2d = Math.sqrt(theDX * theDX + theDY * theDY);
            double theT1 = Math.acos(theDZ / theDistance3d);
            double theT2 = Math.PI - theT1;
            double theV1 = (theDistance3d * theDistance3d + (Double) theAtom.getProperty(this.SURFACE_GRID_RANGE) * (Double) theAtom.getProperty(this.SURFACE_GRID_RANGE)
                    - (Double) theOverlapAtom.getProperty(this.SURFACE_GRID_RANGE) * (Double) theOverlapAtom.getProperty(this.SURFACE_GRID_RANGE)) / (2.0 * theDistance3d);
            double theV2 = theDistance3d - theV1;
            double theP1;
            double theP2;

            if (theDistance2d != 0.0) {
                if (theDY < 0.0) {
                    theP1 = 2.0 * Math.PI - Math.acos(theDX / theDistance2d);
                    theP2 = theP1 + Math.PI;
                } else {
                    theP1 = Math.acos(theDX / theDistance2d);
                    theP2 = theP1 + Math.PI;
                }
            } else {
                theP1 = 0.0;
                theP2 = Math.PI;
            }

            this.__removeOverlap(theAtom, theT1, theP1, theV1);
            this.__removeOverlap(theOverlapAtom, theT2, theP2, theV2);
        }
    }

    private void __removeOverlap(IAtom theAtom, double theT, double theP, double theV) {
        double theA = Math.sin(theP);
        double theB = Math.cos(theP);
        double theCosineT = Math.cos(theT);
        double theSineT = Math.sin(theT);
        GridList theSurfaceGridList = this.itsSurfaceGridList.get(this.itsMolecule.getAtomNumber(theAtom));

        for (int si = theSurfaceGridList.size() - 1; si >= 0; si--) {
            double theZValue = theSurfaceGridList.get(si).getX() * (theB * theSineT) + theSurfaceGridList.get(si).getY() * (theA * theSineT) + theSurfaceGridList.get(si).getZ() * theCosineT;
            
            if (theZValue > theV) {
                this.itsSurfaceGridList.get(this.itsMolecule.getAtomNumber(theAtom)).remove(si);
            }
        }

    }

    private Set<Integer> __getOverlapAtomIndexSet(int theAtomIndex) {
        Set<Integer> theOverlapAtomIndexSet = new TreeSet<>();

        for (int xi = 0; xi < 3; xi++) {
            int theX = this.itsGridIndex2dList.get(theAtomIndex, this.FIRST_INDEX).intValue() - 1 + xi;

            for (int yi = 0; yi < 3; yi++) {
                int theY = this.itsGridIndex2dList.get(theAtomIndex, this.SECOND_INDEX).intValue() - 1 + yi;

                for (int zi = 0; zi < 3; zi++) {
                    int theZ = this.itsGridIndex2dList.get(theAtomIndex, this.THIRD_INDEX).intValue() - 1 + zi;

                    for (int ii = 0, iEnd = this.itsGrid4dList.get(theX, theY).get(theZ).size(); ii < iEnd; ii++) {
                        theOverlapAtomIndexSet.add(this.itsGrid4dList.get(theX, theY).get(theZ, ii));
                    }
                }
            }
        }

        return theOverlapAtomIndexSet;
    }

    private void __allocation() {
        int theAtomIndex = 0;

        this.itsGridIndex2dList = new TwoDimensionList<>();

        for (IAtom theAtom : this.itsMolecule.atoms()) {
            int theGridX = (int) ((theAtom.getPoint3d().x - this.itsMinimumVector.getX()) / this.itsLR) + 1;
            int theGridY = (int) ((theAtom.getPoint3d().y - this.itsMinimumVector.getY()) / this.itsLR) + 1;
            int theGridZ = (int) ((theAtom.getPoint3d().z - this.itsMinimumVector.getZ()) / this.itsLR) + 1;
            List<Integer> theGridIndexList = new ArrayList<>();

            theGridIndexList.add(theGridX);
            theGridIndexList.add(theGridY);
            theGridIndexList.add(theGridZ);

            this.itsGridIndex2dList.add(theGridIndexList);
            this.itsGrid4dList.get(theGridX, theGridY).get(theGridZ).add(theAtomIndex++);
        }
    }

    private void __makeGrid() {
        int theGSX = (int) ((this.itsMaximumVector.getX() - this.itsMinimumVector.getX()) / this.itsLR) + 3;
        int theGSY = (int) ((this.itsMaximumVector.getY() - this.itsMinimumVector.getY()) / this.itsLR) + 3;
        int theGSZ = (int) ((this.itsMaximumVector.getZ() - this.itsMinimumVector.getZ()) / this.itsLR) + 3;

        this.itsGrid4dList = new TwoDimensionList<>();

        for (int xi = 0; xi < theGSX; xi++) {
            List<TwoDimensionList<Integer>> theGrid3dList = new ArrayList<>();

            for (int yi = 0; yi < theGSY; yi++) {
                TwoDimensionList<Integer> theGrid2dList = new TwoDimensionList<>();

                for (int zi = 0; zi < theGSZ; zi++) {
                    theGrid2dList.add(new ArrayList<Integer>());
                }

                theGrid3dList.add(theGrid2dList);
            }

            this.itsGrid4dList.add(theGrid3dList);
        }
    }

    private void __convertSphereToSurface() {
        for (IAtom theAtom : this.itsMolecule.atoms()) {
            if (theAtom.getAtomicNumber() == 1) {
                this.__sphereRotate(theAtom);
            } else {
                this.itsSurfaceGridList.add(new GridList(this.itsSphereGrid.get((Double) theAtom.getProperty(this.SURFACE_GRID_RANGE))));
            }
        }
    }

    private void __sphereRotate(IAtom theAtom) {
        GridList theGridList = new GridList(this.itsSphereGrid.get((Double) theAtom.getProperty(this.SURFACE_GRID_RANGE)));
        IAtom theConnectedAtom = this.itsMolecule.getConnectedAtomsList(theAtom).get(this.FIRST_INDEX);
        double theTargetRange = (Double) theAtom.getProperty(this.SURFACE_GRID_RANGE);
        double theConnectedRange = (Double) theConnectedAtom.getProperty(this.SURFACE_GRID_RANGE);
        double theDx = theConnectedAtom.getPoint3d().x - theAtom.getPoint3d().x;
        double theDy = theConnectedAtom.getPoint3d().y - theAtom.getPoint3d().y;
        double theDz = theConnectedAtom.getPoint3d().z - theAtom.getPoint3d().z;
        
        double theDistance3d = theConnectedAtom.getPoint3d().distance(theAtom.getPoint3d());
        double theDistance2d = Math.sqrt(theDx * theDx + theDy * theDy);
        double thePiAngle;
        
        double theThetaAngle = Math.acos(theDz / theDistance3d);
        double theV = (theDistance3d * theDistance3d + theTargetRange * theTargetRange - theConnectedRange * theConnectedRange) / (2.0 * theDistance3d);
        
        if(theDistance2d != 0.0) {
            if(theDy < 0) {
                thePiAngle = Math.PI * 2 - Math.acos(theDx / theDistance2d);
            } else {
                thePiAngle = Math.acos(theDx / theDistance2d);
            }
        } else {
            thePiAngle = 0.0;
        }
        
        double theA = Math.cos(thePiAngle + Math.PI / 2);
        double theB = Math.sin(thePiAngle + Math.PI / 2);
        
        double theCosTheta = Math.cos(-theThetaAngle);
        double theSinTheta = Math.sin(-theThetaAngle);
        
        Matrix theRMatrix = this.__getRMatrixInRotateSphere(theThetaAngle, thePiAngle);
        
        GridList theResultGridList = new GridList();
        
        for(Vector3d theGrid : theGridList) {
            if(theGrid.getZ() < theV) {
                theResultGridList.add(this.__getSurfaceGrid(theGrid, theRMatrix));
            }
        }
        
        this.itsSurfaceGridList.add(theResultGridList);
    }
    
    private Grid __getSurfaceGrid(Vector3d theSphereGrid, Matrix theRMatrix) {
        Grid theSurfaceGrid = new Grid(1.0, 1.0, 1.0);

        theSurfaceGrid.setX(theRMatrix.get(this.FIRST_INDEX, this.FIRST_INDEX) * theSphereGrid.getX() + theRMatrix.get(this.SECOND_INDEX, this.FIRST_INDEX) * theSphereGrid.getY() + theRMatrix.get(this.THIRD_INDEX, this.FIRST_INDEX) * theSphereGrid.getZ());
        theSurfaceGrid.setY(theRMatrix.get(this.FIRST_INDEX, this.SECOND_INDEX) * theSphereGrid.getX() + theRMatrix.get(this.SECOND_INDEX, this.SECOND_INDEX) * theSphereGrid.getY() + theRMatrix.get(this.THIRD_INDEX, this.SECOND_INDEX) * theSphereGrid.getZ());
        theSurfaceGrid.setZ(theRMatrix.get(this.FIRST_INDEX, this.THIRD_INDEX) * theSphereGrid.getX() + theRMatrix.get(this.SECOND_INDEX, this.THIRD_INDEX) * theSphereGrid.getY() + theRMatrix.get(this.THIRD_INDEX, this.THIRD_INDEX) * theSphereGrid.getZ());

        return theSurfaceGrid;
    }

    private Matrix __getRMatrixInRotateSphere(double theThetaAngle, double thePiAngle) {
        Matrix theRMatrix = new Matrix();
        List<Double> theRList = new ArrayList<>();
        double theA = Math.cos(thePiAngle + Math.PI / 2.0);
        double theB = Math.sin(thePiAngle + Math.PI / 2.0);
        double theCosineTheta = Math.cos(-theThetaAngle);
        double theSineTheta = Math.sin(-theThetaAngle);

        theRList.add(theA * theA * (1.0 - theCosineTheta) + theCosineTheta);
        theRList.add(theA * theB * (1.0 - theCosineTheta));
        theRList.add(theB * theSineTheta);
        theRMatrix.addRow(new ArrayList<>(theRList));
        theRList.clear();

        theRList.add(theA * theB * (1.0 - theCosineTheta));
        theRList.add(theB * theB * (1.0 - theCosineTheta) + theCosineTheta);
        theRList.add(-theA * theSineTheta);
        theRMatrix.addRow(new ArrayList<>(theRList));
        theRList.clear();

        theRList.add(-theB * theSineTheta);
        theRList.add(theA * theSineTheta);
        theRList.add(theCosineTheta);
        theRMatrix.addRow(new ArrayList<>(theRList));
        theRList.clear();

        return theRMatrix;
    }

    private double __getPiAngleInRatateSphere(double theDistance2d, double theDx, double theDy) {
        if (theDistance2d != 0) {
            if (theDy < 0) {
                return Math.PI * 2.0 - Math.acos(theDx / theDistance2d);
            } else {
                return Math.acos(theDx / theDistance2d);
            }
        }

        return 0.0;
    }

    private void __initializeSphereGrid(Double theInterval) {
        SphereGridGenerator theSphereGridGenerator = new SphereGridGenerator(this.itsParameterList);

        this.itsSphereGrid = theSphereGridGenerator.draw(this.itsShsell, theInterval);
    }

    private void __inputAtomInformation() {
        this.__initilizeMaximumAndMinimumVector();

        for (IAtom theAtom : this.itsMolecule.atoms()) {
            double theRange = this.itsParameterList.get((Integer) theAtom.getProperty(MpeoeAtomTypeGenerator.VDW_ATOM_TYPE_KEY)) + this.itsShsell;

            theAtom.setProperty(this.SURFACE_GRID_RANGE, theRange);

            if (theRange > this.itsLR) {
                this.itsLR = theRange;
            }
        }
    }

    private void __initilizeMaximumAndMinimumVector() {
        this.itsMaximumVector = new Vector3d();
        this.itsMinimumVector = new Vector3d();

        for (IAtom theAtom : this.itsMolecule.atoms()) {
            Point3d theAtomPosition = this.__getAtomPosition(theAtom);

            if (this.itsMaximumVector.getX() < theAtomPosition.x) {
                this.itsMaximumVector.setX(theAtomPosition.x);
            }

            if (this.itsMaximumVector.getY() < theAtomPosition.y) {
                this.itsMaximumVector.setY(theAtomPosition.y);
            }

            if (this.itsMaximumVector.getZ() < theAtomPosition.z) {
                this.itsMaximumVector.setZ(theAtomPosition.z);
            }

            if (this.itsMinimumVector.getX() > theAtomPosition.x) {
                this.itsMinimumVector.setX(theAtomPosition.x);
            }

            if (this.itsMinimumVector.getY() > theAtomPosition.y) {
                this.itsMinimumVector.setY(theAtomPosition.y);
            }

            if (this.itsMinimumVector.getZ() > theAtomPosition.z) {
                this.itsMinimumVector.setZ(theAtomPosition.z);
            }
        }
    }

    private Point3d __getAtomPosition(IAtom theAtom) {
        if (theAtom.getPoint3d() == null) {
            theAtom.setPoint3d(new Point3d(theAtom.getPoint2d().x, theAtom.getPoint2d().y, 0.0));
        }

        return theAtom.getPoint3d();
    }
}
