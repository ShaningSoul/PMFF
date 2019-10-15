package org.bmdrc.predock.calculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.tool.AngleCalculator;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.predock.variable.BindingSite;
import org.bmdrc.predock.variable.ConformerGeneratorParameter;
import org.bmdrc.predock.variable.ConformerInformation;
import org.bmdrc.sbff.intraenergy.SbffIntraEnergyFunction;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.util.ManipulationTool;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.geometry.alignment.KabschAlignment;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class BindingConformerGenerator implements Serializable {

    private static final long serialVersionUID = 8117241592941205877L;

    private BindingSite itsBindingSite;
    private IAtomContainer itsLigand;
    private ConformerGeneratorParameter itsParameter;
    private SbffIntraEnergyFunction itsConformerEnergyFunction;
    private int itsNumber = 1;
    //constant String variable
    private final String RotatableBondSmartKey = "[!$(*#*)&!D1]-!@[!$(*#*)&!D1]";
    private final String CONFORMER_ENERGY_KEY = "Conformer Energy";
    //constant Object variable
    private final Comparator<IAtomContainer> ENERGY_COMPARATOR = new Comparator<IAtomContainer>() {
        @Override
        public int compare(IAtomContainer t, IAtomContainer t1) {
            return ((Double) t.getProperty(DockingCalculator.SYSTEM_ENERGY_KEY))
                    .compareTo((Double) t1.getProperty(DockingCalculator.SYSTEM_ENERGY_KEY));
        }
    };

    public IAtomContainerSet makeConformerSet(IAtomContainer theMolecule) {
        return this.makeConformerSet(theMolecule, new ConformerGeneratorParameter());
    }

    public IAtomContainerSet makeConformerSet(IAtomContainer theMolecule, ConformerGeneratorParameter theParameter) {
        try {
            this.itsConformerEnergyFunction = new SbffIntraEnergyFunction(theMolecule, theParameter.getEnergyCalculationParameter());
            this.itsLigand = theMolecule;
            this.itsParameter = theParameter;

            List<Integer> theRotatableBondIndexList = this.__getRotatableBondIndexList();
            IAtomContainerSet theConformerSet = new AtomContainerSet();
            Double theInitialEnergy = this.itsConformerEnergyFunction.calculateEnergyFunction();

            this.itsLigand.setProperty(DockingCalculator.SYSTEM_ENERGY_KEY, theInitialEnergy);

            theConformerSet.addAtomContainer(this.itsLigand);
            int theIndex = 0;
            for (Integer theRotatableBondIndex : theRotatableBondIndexList) {
                IAtomContainerSet theResultSet = new AtomContainerSet();

                for (IAtomContainer theConformer : theConformerSet.atomContainers()) {
                    IAtomContainerSet theNewConformerSet = this.__makeConformerSet(theConformer, theRotatableBondIndex, theInitialEnergy);

                    if (!theNewConformerSet.isEmpty()) {
                        theResultSet.add(theNewConformerSet);

                        theResultSet = this.__sortConformerSet(theResultSet);
                    }
                }

                theConformerSet.add(theResultSet);

                theConformerSet = this.__sortConformerSet(theConformerSet);
            }

            return theConformerSet;
        } catch (Exception ex) {
            System.err.println("Error!! __makeConformerSet() in DockingCalculate");
            ex.printStackTrace();
        }

        return null;
    }

    public List<ConformerInformation> makeConformerInformation(IAtomContainer theMolecule) {
        return this.makeConformerInformation(theMolecule, new ConformerGeneratorParameter(), null);
    }

    public List<ConformerInformation> makeConformerInformation(IAtomContainer theMolecule, ConformerGeneratorParameter theParameter) {
        return this.makeConformerInformation(theMolecule, theParameter, null);
    }

    public List<ConformerInformation> makeConformerInformation(IAtomContainer theMolecule, ConformerGeneratorParameter theParameter, BindingSite theBindingSite) {
        this.itsConformerEnergyFunction = new SbffIntraEnergyFunction(theMolecule, theParameter.getEnergyCalculationParameter());
        this.itsLigand = theMolecule;
        this.itsParameter = theParameter;
        this.itsBindingSite = theBindingSite;

        long time = System.currentTimeMillis();

        System.out.print("Start to make conformer information ");
        ManipulationTool.printCalculationTime(time);
        List<Integer> theRotatableBondIndexList = this.__getRotatableBondIndexList();
        List<ConformerInformation> theConformerInformationList = new ArrayList<>();
        Double theInitialEnergy = this.itsConformerEnergyFunction.calculateEnergyFunction();

        this.itsLigand.setProperty(DockingCalculator.SYSTEM_ENERGY_KEY, theInitialEnergy);

        theConformerInformationList.add(new ConformerInformation());
        int theIndex = 0;

        System.out.print("finished to initialize function to make conformer information ");
        ManipulationTool.printCalculationTime(time);

        for (Integer theRotatableBondIndex : theRotatableBondIndexList) {
            List<ConformerInformation> theResultConformerInformationList = new ArrayList<>();

            for (ConformerInformation theConformerInformation : theConformerInformationList) {
                theResultConformerInformationList.addAll(this.__makeConformerInformation(theConformerInformation, theRotatableBondIndex, theInitialEnergy));
            }

            theConformerInformationList.addAll(theResultConformerInformationList);

            System.out.print("Completed conformer generator [" + ++theIndex + "/" + theRotatableBondIndexList.size() + "] " + theConformerInformationList.size() + " ");
            ManipulationTool.printCalculationTime(time);
        }

        return theConformerInformationList;
    }

    private List<ConformerInformation> __makeConformerInformation(ConformerInformation theConformerInformation, Integer theBondIndex, Double theInitialEnergy) {
        MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap = this.itsConformerEnergyFunction.getMoveTogetherAtomNumberMap();
        IAtomContainer theConformer = this.generateConformer(theConformerInformation, this.itsLigand, theMoveTogetherAtomNumberMap);
        IBond theBond = theConformer.getBond(theBondIndex);
        IAtom theFirstAtom = theBond.getAtom(Constant.FIRST_INDEX);
        IAtom theSecondAtom = theBond.getAtom(Constant.SECOND_INDEX);
        int theFirstAtomIndex = theConformer.getAtomNumber(theFirstAtom);
        int theSecondAtomIndex = theConformer.getAtomNumber(theSecondAtom);
        List<Integer> theMoveTogetherAtomNumberByFirstAtom = theMoveTogetherAtomNumberMap.get(theFirstAtomIndex, theSecondAtomIndex);
        Vector3d theAxisVector = Vector3dCalculator.minus(new Vector3d(theFirstAtom), new Vector3d(theSecondAtom));
        List<ConformerInformation> theConformerInformationList = new ArrayList<>();

        for (double ai = this.itsParameter.getRotationStepSize(); ai < 360.0; ai += this.itsParameter.getRotationStepSize()) {
            try {
                IAtomContainer theCopiedMolecule = theConformer.clone();

                for (Integer theAtomNumber : theMoveTogetherAtomNumberByFirstAtom) {
                    if (!theAtomNumber.equals(theFirstAtomIndex)) {
                        IAtom theTargetAtom = theCopiedMolecule.getAtom(theAtomNumber);
                        Vector3d thePosition = AngleCalculator.rotatePosition(theTargetAtom, theFirstAtom, theAxisVector, ai);

                        theTargetAtom.getPoint3d().set(thePosition.getX(), thePosition.getY(), thePosition.getZ());
                    }
                }

                if (!this.__containSameConformer(theCopiedMolecule, theConformerInformationList)
                        && this.__containEnergyVarianceRange(theCopiedMolecule, theInitialEnergy) && this.__existConformerInsideBindingSite(theCopiedMolecule)) {
                    ConformerInformation theNewConformerInformation = new ConformerInformation(theConformerInformation);

                    theNewConformerInformation.put(theBondIndex, ai);
                    theConformerInformationList.add(theNewConformerInformation);
                }
            } catch (CloneNotSupportedException ex) {
                System.err.println("Error!! __makeConformerInformation(ConformerInformation theConformerInformation, Integer theBondIndex, Double theInitialEnergy) "
                        + "in DockingCalculate");
            }
        }

        return theConformerInformationList;
    }

    private boolean __containSameConformer(IAtomContainer theConformer, List<ConformerInformation> theConformerInformationList) {
        try {
            if(this.__containSameConformer(theConformer, this.itsLigand.clone())) {
                return true;
            }
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(BindingConformerGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (ConformerInformation theConformerInformation : theConformerInformationList) {
            IAtomContainer thePreviousConformer = BindingConformerGenerator.generateConformer(theConformerInformation, this.itsLigand,
                    this.itsConformerEnergyFunction.getMoveTogetherAtomNumberMap());

            if(this.__containSameConformer(theConformer, thePreviousConformer)) {
                return true;
            }
        }

        return false;
    }

    private boolean __containSameConformer(IAtomContainer theConformer, IAtomContainer thePreviousConformer) {
        try {
            KabschAlignment theAlignment = new KabschAlignment(thePreviousConformer, theConformer);

            theAlignment.align();

            if (theAlignment.getRMSD() < 1e-4 / this.itsLigand.getAtomCount()) {
                return true;
            } else {
                return false;
            }
        } catch (CDKException ex) {
            Logger.getLogger(BindingConformerGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    private IAtomContainerSet __makeConformerSet(IAtomContainer theConformer, Integer theBondIndex, Double theInitialEnergy) {
        IBond theBond = theConformer.getBond(theBondIndex);
        IAtom theFirstAtom = theBond.getAtom(Constant.FIRST_INDEX);
        IAtom theSecondAtom = theBond.getAtom(Constant.SECOND_INDEX);
        int theFirstAtomIndex = theConformer.getAtomNumber(theFirstAtom);
        int theSecondAtomIndex = theConformer.getAtomNumber(theSecondAtom);
        List<Integer> theMoveTogetherAtomNumberByFirstAtom = this.itsConformerEnergyFunction.getMoveTogetherAtomNumberMap().get(theFirstAtomIndex, theSecondAtomIndex);
        Vector3d theAxisVector = Vector3dCalculator.minus(new Vector3d(theFirstAtom), new Vector3d(theSecondAtom));
        IAtomContainerSet theResultSet = new AtomContainerSet();

        for (double ai = this.itsParameter.getRotationStepSize(); ai < 360.0; ai += this.itsParameter.getRotationStepSize()) {
            try {
                IAtomContainer theCopiedMolecule = theConformer.clone();

                for (Integer theAtomNumber : theMoveTogetherAtomNumberByFirstAtom) {
                    if (!theAtomNumber.equals(theFirstAtomIndex)) {
                        IAtom theTargetAtom = theCopiedMolecule.getAtom(theAtomNumber);
                        Vector3d thePosition = AngleCalculator.rotatePosition(theTargetAtom, theFirstAtom, theAxisVector, ai);

                        theTargetAtom.getPoint3d().set(thePosition.getX(), thePosition.getY(), thePosition.getZ());
                    }
                }

                if (this.__containEnergyVarianceRange(theCopiedMolecule, theInitialEnergy) && !this.__isWrongStructure(theConformer) &&
                        this.__existConformerInsideBindingSite(theCopiedMolecule)) {
                    theResultSet.addAtomContainer(theCopiedMolecule);
                }
            } catch (CloneNotSupportedException ex) {
                System.err.println("Error!! __makeConformerSet(IAtomContainer theConformer, Integer theBondIndex, DockingParameter theParameter) "
                        + "in DockingCalculate");
            }
        }

        return theResultSet;
    }

    public static IAtomContainer generateConformer(ConformerInformation theConformerInformation, IAtomContainer theMolecule) {
        return BindingConformerGenerator.generateConformer(theConformerInformation, theMolecule, new MoveTogetherAtomNumberList(theMolecule));
    }

    public static IAtomContainer generateConformer(ConformerInformation theConformerInformation, IAtomContainer theMolecule, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) {
        try {
            IAtomContainer theCopiedMolecule = theMolecule.clone();

            for (Integer theBondIndex : theConformerInformation.keySet()) {
                IBond theBond = theCopiedMolecule.getBond(theBondIndex);
                IAtom theFirstAtom = theBond.getAtom(Constant.FIRST_INDEX);
                IAtom theSecondAtom = theBond.getAtom(Constant.SECOND_INDEX);
                int theFirstAtomIndex = theCopiedMolecule.getAtomNumber(theFirstAtom);
                int theSecondAtomIndex = theCopiedMolecule.getAtomNumber(theSecondAtom);
                List<Integer> theMoveTogetherAtomNumberByFirstAtom = theMoveTogetherAtomNumberMap.get(theFirstAtomIndex, theSecondAtomIndex);
                Vector3d theAxisVector = Vector3dCalculator.minus(new Vector3d(theFirstAtom), new Vector3d(theSecondAtom));

                for (Integer theAtomNumber : theMoveTogetherAtomNumberByFirstAtom) {
                    if (!theAtomNumber.equals(theFirstAtomIndex)) {
                        IAtom theTargetAtom = theCopiedMolecule.getAtom(theAtomNumber);
                        Vector3d thePosition = AngleCalculator.rotatePosition(theTargetAtom, theFirstAtom, theAxisVector, theConformerInformation.get(theBondIndex));

                        theTargetAtom.getPoint3d().set(thePosition.getX(), thePosition.getY(), thePosition.getZ());
                    }
                }
            }

            theCopiedMolecule.setProperty("ConformationInformation", theConformerInformation);

            return theCopiedMolecule;
        } catch (CloneNotSupportedException ex) {
            System.err.println("Error!! generateConformer(ConformerInformation theConformerInformation, IAtomContainer theMolecule, MoveTogetherAtomNumberList theMoveTogetherAtomNumberMap) "
                    + "in DockingCalculate");
        }

        return null;
    }

    private boolean __containEnergyVarianceRange(IAtomContainer theConformer, Double theInitialEnergy) {
        Double theConformerEnergy = this.itsConformerEnergyFunction.calculateEnergyFunction(theConformer);
        Double theAbsoluteEnergyDifference = Math.abs(theConformerEnergy - theInitialEnergy);

        if (this.itsParameter.getMaximumEnergyVariance().compareTo(theAbsoluteEnergyDifference) < 0) {
            return false;
//        } else if (this.itsParameter.getMinimumEnergyVariance() > theAbsoluteEnergyDifference) {
//            return false;
        }

        theConformer.setProperty("Initial Energy", theInitialEnergy);
        theConformer.setProperty(this.CONFORMER_ENERGY_KEY, theConformerEnergy);

        return true;
    }
    
    private boolean __isWrongStructure(IAtomContainer theConformer) {
        for(int fi = 0, fEnd = theConformer.getAtomCount()-1; fi < fEnd; fi++) {
            for(int si = fi + 1, sEnd = fEnd + 1; si < sEnd; si++) {
                IAtom theFirstAtom = theConformer.getAtom(fi);
                IAtom theSecondAtom = theConformer.getAtom(si);
                
                if(theConformer.getBond(theFirstAtom, theSecondAtom) == null) {
                    continue;
                } else if(theFirstAtom.getPoint3d().distance(theSecondAtom.getPoint3d()) < 1.0) {
                    return true;
                }
            }
        }
        
        return false;
    }

    private boolean __existConformerInsideBindingSite(IAtomContainer theConformer) {
        Vector3d theCenter = new Vector3d(GeometryTools.get3DCenter(theConformer));

        if (this.itsBindingSite != null) {
            for (IAtom theAtom : theConformer.atoms()) {
                Vector3d thePosition = new Vector3d(theAtom);

                if (this.itsBindingSite.getRadius() * 2.0 < thePosition.distance(theCenter)) {
                    return false;
                }
            }
        }

        return true;
    }

    private IAtomContainerSet __sortConformerSet(IAtomContainerSet theConformerSet) {
        theConformerSet.sortAtomContainers(this.ENERGY_COMPARATOR);

        if (theConformerSet.getAtomContainerCount() > this.itsParameter.getMaximumConformerCount()) {
            for (int mi = theConformerSet.getAtomContainerCount() - 1, mEnd = this.itsParameter.getMaximumConformerCount(); mi >= mEnd;
                    mi--) {
                theConformerSet.removeAtomContainer(mi);
            }
        }

        return theConformerSet;
    }

    private List<Integer> __getRotatableBondIndexList() {
        try {
            SMARTSQueryTool theQueryTool = new SMARTSQueryTool(this.RotatableBondSmartKey, DefaultChemObjectBuilder.getInstance());
            boolean theIsMatched = theQueryTool.matches(this.itsLigand);
            List<Integer> theRotatableBondIndexList = new ArrayList<>();

            if (theIsMatched) {
                for (List<Integer> theAtomIndexList : theQueryTool.getMatchingAtoms()) {
                    Integer theBondIndex = this.itsLigand.getBondNumber(this.itsLigand.getAtom(theAtomIndexList.get(Constant.FIRST_INDEX)),
                            this.itsLigand.getAtom(theAtomIndexList.get(Constant.SECOND_INDEX)));

                    if (!this.__containHydrogenInBond(theBondIndex) && !theRotatableBondIndexList.contains(theBondIndex)) {
                        theRotatableBondIndexList.add(theBondIndex);
                    }
                }
            }

            return theRotatableBondIndexList;
        } catch (CDKException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private boolean __containHydrogenInBond(Integer theBondIndex) {
        IBond theBond = this.itsLigand.getBond(theBondIndex);

        return theBond.getAtom(Constant.FIRST_INDEX).getAtomicNumber().equals(AtomInformation.Hydrogen.ATOM_NUMBER)
                || theBond.getAtom(Constant.SECOND_INDEX).getAtomicNumber().equals(AtomInformation.Hydrogen.ATOM_NUMBER);
    }
}
