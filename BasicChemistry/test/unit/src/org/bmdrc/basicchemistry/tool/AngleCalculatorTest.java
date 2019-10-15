/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.basicchemistry.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.basicchemistry.writer.SDFWriter;
import org.bmdrc.ui.abstracts.Constant;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Exceptions;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class AngleCalculatorTest {

    public AngleCalculatorTest() {
    }

    @Test
    public void rotationTest() {
        IAtomContainer theMolecule = SDFReader.openMoleculeFile(new File("G:\\내 드라이브\\SBFF\\ProteinLigandComplex\\Ligand\\1fen.sdf"))
                .getAtomContainer(0);
        MoveTogetherAtomNumberList theMoveTogetherAtomNumberList = new MoveTogetherAtomNumberList(theMolecule);
        List<Integer> theRotatableBondIndexList = this.__getRotatableBondIndexList(theMolecule);
        Integer theRotatableBondIndex = theRotatableBondIndexList.get(0);
        IAtomContainerSet theResultSet = new AtomContainerSet();

        IBond theBond = theMolecule.getBond(theRotatableBondIndex);
        IAtom theFirstAtom = theBond.getAtom(Constant.FIRST_INDEX);
        IAtom theSecondAtom = theBond.getAtom(Constant.SECOND_INDEX);
        int theFirstAtomIndex = theMolecule.getAtomNumber(theFirstAtom);
        int theSecondAtomIndex = theMolecule.getAtomNumber(theSecondAtom);
        List<Integer> theMoveTogetherAtomNumberByFirstAtom = theMoveTogetherAtomNumberList.get(theFirstAtomIndex, theSecondAtomIndex);
        Vector3d theAxisVector = Vector3dCalculator.minus(new Vector3d(theFirstAtom), new Vector3d(theSecondAtom));

        try {
            IAtomContainer theCopiedMolecule = theMolecule.clone();

            for (Integer theAtomNumber : theMoveTogetherAtomNumberByFirstAtom) {
                if (!theAtomNumber.equals(theFirstAtomIndex)) {
                    IAtom theTargetAtom = theCopiedMolecule.getAtom(theAtomNumber);
                    Vector3d thePosition = AngleCalculator.rotatePosition(theTargetAtom, theFirstAtom, theAxisVector, 90.0);

                    System.out.println(theFirstAtomIndex + " " + theSecondAtomIndex + " " + theAtomNumber + ": " + theTargetAtom.getPoint3d()
                            + " " + thePosition);
                    theTargetAtom.getPoint3d().set(thePosition.getX(), thePosition.getY(), thePosition.getZ());
                }
            }

            theResultSet.addAtomContainer(theCopiedMolecule);
        } catch (CloneNotSupportedException ex) {
            System.err.println("Error!! __makeConformerSet(IAtomContainer theConformer, Integer theBondIndex, DockingParameter theParameter) "
                    + "in DockingCalculate");
        }

        SDFWriter.writeSDFile(theResultSet, new File("G:\\내 드라이브\\SBFF\\ProteinLigandComplex\\Ligand\\1fen_test.sdf"));
        assertTrue(true);
    }

    private List<Integer> __getRotatableBondIndexList(IAtomContainer theMolecule) {
        try {
            SMARTSQueryTool theQueryTool = new SMARTSQueryTool("[!$(*#*)&!D1]-!@[!$(*#*)&!D1]");
            boolean theIsMatched = theQueryTool.matches(theMolecule);
            List<Integer> theRotatableBondIndexList = new ArrayList<>();

            if (theIsMatched) {

                for (List<Integer> theAtomIndexList : theQueryTool.getMatchingAtoms()) {
                    theRotatableBondIndexList.add(theMolecule.getBondNumber(theMolecule.getAtom(theAtomIndexList.get(Constant.FIRST_INDEX)),
                            theMolecule.getAtom(theAtomIndexList.get(Constant.SECOND_INDEX))));
                }
            }

            return theRotatableBondIndexList;
        } catch (CDKException ex) {
            Exceptions.printStackTrace(ex);
        }

        return null;
    }
}
