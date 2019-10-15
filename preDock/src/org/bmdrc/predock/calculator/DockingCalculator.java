package org.bmdrc.predock.calculator;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.minimization.abstracts.AbstractConjugatedGradientMinimizer;
import org.bmdrc.basicchemistry.minimizer.ConjugatedGradientMinimizerInMolecularStructure;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.tool.AngleCalculator;
import org.bmdrc.basicchemistry.tool.MoleculeManipulator;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.vector.Vector3dCalculator;
import org.bmdrc.basicchemistry.windows.MoleculeStructureViewerTopComponent;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.basicchemistry.writer.SDFWriter;
import org.bmdrc.predock.variable.BindingSite;
import org.bmdrc.predock.variable.DockingParameter;
import org.bmdrc.predock.variable.WeightAminoAcidInformationList;
import org.bmdrc.sbff.energyfunction.SbffEnergyFunction;
import org.bmdrc.sbff.intraenergy.SbffIntraEnergyFunction;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.netbeans.api.progress.*;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class DockingCalculator extends AbstractAction implements Serializable, Runnable, Cancellable {

    private static final long serialVersionUID = -8149571330691191658L;

    private SbffEnergyFunction itsBindingEnergyFunction;
    private ConjugatedGradientMinimizerInMolecularStructure<SbffIntraEnergyFunction> itsLigandOptimizer;
    private Protein itsProtein;
    private IAtomContainer itsLigand;
    private BindingSite itsBindingSite;
    private DockingParameter itsParameter;
    private WeightAminoAcidInformationList itsWeightAminoAcidSerialList;
    private BindingPoseGenerator itsBindingPoseGenerator;
    private IAtomContainerSet itsResultBindingPoseSet;
    private Thread itsThread;
    private MoleculeStructureViewerTopComponent itsViewerTopComponent;
    private MoleculeTableTopComponent itsTableTopComponent;
    //constant Double variable

    //constant String variable
    private final String RotatableBondSmartKey = "[!$(*#*)&!D1]-!@[!$(*#*)&!D1]";
    public static final String SYSTEM_ENERGY_KEY = "Docking pose energy";
    public static final String INITIAL_ENERGY_KEY = "Initial docking pose energy";
    //constant Object variable
    private final Comparator<IAtomContainer> ENERGY_COMPARATOR = new Comparator<IAtomContainer>() {
        @Override
        public int compare(IAtomContainer t, IAtomContainer t1) {
            return t.getProperty(DockingCalculator.SYSTEM_ENERGY_KEY, Double.class)
                    .compareTo(t1.getProperty(DockingCalculator.SYSTEM_ENERGY_KEY, Double.class));
        }
    };

    public DockingCalculator(Protein theProtein, IAtomContainer theLigand, BindingSite theBindingSite, DockingParameter theParameter,
            WeightAminoAcidInformationList theWeightedAminoAcidSerialList) {
        this.itsProtein = theProtein;
        this.itsLigand = theLigand;
        this.itsBindingSite = theBindingSite;
        this.itsParameter = theParameter;
        this.itsWeightAminoAcidSerialList = theWeightedAminoAcidSerialList;
    }

    public IAtomContainerSet getResultBindingPoseSet() {
        return this.itsResultBindingPoseSet;
    }

    public Thread getThread() {
        return itsThread;
    }

    public MoleculeStructureViewerTopComponent getViewerTopComponent() {
        return this.itsViewerTopComponent;
    }

    public void setViewerTopComponent(MoleculeStructureViewerTopComponent theViewerTopComponent) {
        this.itsViewerTopComponent = theViewerTopComponent;
    }

    public MoleculeTableTopComponent getTableTopComponent() {
        return itsTableTopComponent;
    }

    public void setTableTopComponent(MoleculeTableTopComponent theTableTopComponent) {
        this.itsTableTopComponent = theTableTopComponent;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.itsThread = new Thread(this);

        this.itsThread.start();
    }

    @Override
    public void run() {
        ProgressHandle theProgress = ProgressHandleFactory.createHandle("Docking...", this, this);
        int theIndex = 1;

        theProgress.start(6);

        theProgress.progress("Initialize components...");
        this.itsBindingEnergyFunction = new SbffEnergyFunction(this.itsProtein, this.itsLigand, this.itsParameter.getCalculationParameter());
        this.itsParameter.getCalculationParameter().setNotCalculableAtomIndexList(this.__generateNotCalculableAtomIndexList());
        this.itsBindingEnergyFunction = new SbffEnergyFunction(this.__generateBindingSiteAndLigandComplex(), this.itsParameter.getCalculationParameter());
        this.itsLigandOptimizer = new ConjugatedGradientMinimizerInMolecularStructure(new SbffIntraEnergyFunction(this.itsLigand, this.itsParameter.getCalculationParameter().getIntraParameter()));
        this.itsBindingPoseGenerator = new BindingPoseGenerator();
        this.itsResultBindingPoseSet = new AtomContainerSet();
        this.itsLigand.setProperty(DockingCalculator.INITIAL_ENERGY_KEY, this.itsBindingEnergyFunction.calculateInterEnergy());
        theProgress.progress(theIndex++);

        theProgress.progress("Generate conformer...");
        IAtomContainerSet theConformerSet = this.__makeConformerSet();
        theProgress.progress(theIndex++);
        theProgress.progress("Generate binding pose...");

        this.itsResultBindingPoseSet = this.itsBindingPoseGenerator.calculateBindingPose(this.itsProtein, this.itsBindingSite, this.itsParameter,
                this.itsLigand, theConformerSet, this.itsWeightAminoAcidSerialList);

        theProgress.progress(theIndex++);

        theProgress.progress("Calculate score...");
        for (IAtomContainer theBindingPose : this.itsResultBindingPoseSet.atomContainers()) {
            this.__calculateEnergy(theBindingPose);
        }
        theProgress.progress(theIndex++);

        theProgress.progress("Sort pose by score...");
        this.itsResultBindingPoseSet.sortAtomContainers(this.ENERGY_COMPARATOR);
        theProgress.progress(theIndex++);

        theProgress.progress("Prepare result...");
        this.__setResult();
        theProgress.finish();
    }

    private IAtomContainer __generateBindingSiteAndLigandComplex() {
        IAtomContainer theComplex = new AtomContainer();

        theComplex.add(this.itsLigand);
        theComplex.add(this.itsBindingSite.getAminoAcids());

        return theComplex;
    }

    private List<Integer> __generateNotCalculableAtomIndexList() {
        List<Integer> theNotCalculableAtomIndexList = new ArrayList<>();
        int theLigandAtomCount = this.itsLigand.getAtomCount();

        for (int ai = 0, aEnd = this.itsBindingSite.getAminoAcids().getAtomCount(); ai < aEnd; ai++) {
            theNotCalculableAtomIndexList.add(theLigandAtomCount + ai);
        }

        return theNotCalculableAtomIndexList;
    }

    private void __setResult() {
        File theTempFile = new File(Constant.TEMP_DIR + "SBFF_" + System.nanoTime() + "_Docking_Result.sdf");

        SDFWriter.writeSDFile(this.itsResultBindingPoseSet, theTempFile);

        this.itsTableTopComponent.setMoleculeFile(theTempFile);
        this.itsViewerTopComponent.openFile(theTempFile);
        this.itsTableTopComponent.setData(DockingCalculator.SYSTEM_ENERGY_KEY);

        this.itsTableTopComponent.revalidate();
        this.itsViewerTopComponent.revalidate();
    }

    public IAtomContainerSet calculateDocking(Protein theProtein, IAtomContainer theLigand, BindingSite theBindingSite,
            DockingParameter theParameter, WeightAminoAcidInformationList theWeightedAminoAcidSerialList) {
        this.itsProtein = theProtein;
        this.itsLigand = theLigand;
        this.itsBindingSite = theBindingSite;
        this.itsParameter = theParameter;
        this.itsWeightAminoAcidSerialList = theWeightedAminoAcidSerialList;

        return this.__calculateDocking();
    }

    private IAtomContainerSet __calculateDocking() {
        this.itsBindingEnergyFunction = new SbffEnergyFunction(this.itsProtein, this.itsLigand, this.itsParameter.getCalculationParameter());

        IAtomContainerSet theConformerSet = this.__makeConformerSet();
        IAtomContainerSet theBindingPoseSet = new AtomContainerSet();

        for (IAtomContainer theConformer : theConformerSet.atomContainers()) {
            theBindingPoseSet.add(this.itsBindingPoseGenerator.calculateBindingPose(this.itsProtein, this.itsBindingSite, this.itsParameter,
                    theConformer, this.itsWeightAminoAcidSerialList));
        }

        for (IAtomContainer theBindingPose : theBindingPoseSet.atomContainers()) {
            this.__calculateEnergy(theBindingPose);
        }

        theBindingPoseSet.sortAtomContainers(this.ENERGY_COMPARATOR);

        return theBindingPoseSet;
    }

    private void __calculateEnergy(IAtomContainer theBindingPose) {
        Vector theAtomPositionDifferenceVector = this.__getAtomPositionDifferenceVector(theBindingPose, this.itsLigand);
        Double theEnergy = this.itsBindingEnergyFunction.calculateInterEnergyUsingGradientVector(theAtomPositionDifferenceVector);

        theBindingPose.setProperty(DockingCalculator.SYSTEM_ENERGY_KEY, theEnergy);
    }

    private Vector __getAtomPositionDifferenceVector(IAtomContainer theBindingPose, IAtomContainer theLigand) {
        Vector theAtomPositionDifferenceVector = new Vector();

        for (int ai = 0, aEnd = theLigand.getAtomCount(); ai < aEnd; ai++) {
            Vector3d theDifferenceVector = Vector3dCalculator.minus(new Vector3d(theBindingPose.getAtom(ai)), new Vector3d(theLigand.getAtom(ai)));

            theAtomPositionDifferenceVector.addAll(theDifferenceVector);
        }

        return theAtomPositionDifferenceVector;
    }

    private Double __getSignedRandomNumber(Random theRandom) {
        return (theRandom.nextDouble() - 0.5) * 2.0;
    }

    private double __getMaximumDistance(IAtomContainer theConformer, Vector3d theCenterPosition) {
        double theMaximumDistance = 0.0;

        for (IAtom theAtom : theConformer.atoms()) {
            double theDistance = theCenterPosition.distance(new Vector3d(theAtom));

            if (theDistance > theMaximumDistance) {
                theMaximumDistance = theDistance;
            }
        }

        return theMaximumDistance;
    }

    private IAtomContainerSet __makeConformerSet() {
        try {
            List<Integer> theRotatableBondIndexList = this.__getRotatableBondIndexList();
            IAtomContainerSet theConformerSet = new AtomContainerSet();
            Double theInitialEnergy = this.itsLigandOptimizer.getFunction().calculateEnergyFunction();
            IAtomContainer theOptimizedLigand = this.itsLigandOptimizer.optimize();

            theOptimizedLigand.setProperty(DockingCalculator.SYSTEM_ENERGY_KEY,
                    theOptimizedLigand.getProperty(AbstractConjugatedGradientMinimizer.OPTIMIZED_ENERGY_KEY, Double.class));

            theConformerSet.addAtomContainer(theOptimizedLigand);

            for (Integer theRotatableBondIndex : theRotatableBondIndexList) {
                IAtomContainerSet theResultSet = new AtomContainerSet();

                for (IAtomContainer theConformer : theConformerSet.atomContainers()) {
                    theResultSet.add(this.__makeConformerSet(theConformer, theRotatableBondIndex, theInitialEnergy));
                }

                theConformerSet.add(theResultSet);

                theConformerSet.sortAtomContainers(this.ENERGY_COMPARATOR);

                if (theConformerSet.getAtomContainerCount() > this.itsParameter.getVisibleResultCount()) {
                    for (int mi = theConformerSet.getAtomContainerCount() - 1, mEnd = this.itsParameter.getVisibleResultCount(); mi >= mEnd;
                            mi--) {
                        theConformerSet.removeAtomContainer(mi);
                    }
                }
            }

            return theConformerSet;
        } catch (Exception ex) {
            System.err.println("Error!! __makeConformerSet() in DockingCalculate");
            ex.printStackTrace();
        }

        return null;
    }

    private IAtomContainerSet __makeConformerSet(IAtomContainer theConformer, Integer theBondIndex, Double theInitialEnergy) {
        IBond theBond = theConformer.getBond(theBondIndex);
        IAtom theFirstAtom = theBond.getAtom(Constant.FIRST_INDEX);
        IAtom theSecondAtom = theBond.getAtom(Constant.SECOND_INDEX);
        int theFirstAtomIndex = theConformer.getAtomNumber(theFirstAtom);
        int theSecondAtomIndex = theConformer.getAtomNumber(theSecondAtom);
        List<Integer> theMoveTogetherAtomNumberByFirstAtom = this.itsBindingEnergyFunction.getMoveTogetherAtomNumberMap()
                .get(theFirstAtomIndex, theSecondAtomIndex);
        Vector3d theAxisVector = Vector3dCalculator.minus(new Vector3d(theFirstAtom), new Vector3d(theSecondAtom));
        IAtomContainerSet theResultSet = new AtomContainerSet();

        for (double ai = this.itsParameter.getRotationStepSize(); ai < 360.0; ai += this.itsParameter.getRotationStepSize()) {
            try {
                IAtomContainer theCopiedMolecule = theConformer.clone();
                Vector theMoveVector = new Vector();

                for (Integer theAtomNumber : theMoveTogetherAtomNumberByFirstAtom) {
                    if (!theAtomNumber.equals(theFirstAtomIndex)) {
                        IAtom theTargetAtom = theCopiedMolecule.getAtom(theAtomNumber);
                        Vector3d thePosition = AngleCalculator.rotatePosition(theTargetAtom, theFirstAtom, theAxisVector, ai);

                        theTargetAtom.getPoint3d().setX(thePosition.getX());
                        theTargetAtom.getPoint3d().setY(thePosition.getY());
                        theTargetAtom.getPoint3d().setZ(thePosition.getZ());
                    }
                }

                if (this.__containEnergyVarianceRange(theCopiedMolecule, theInitialEnergy) && this.__existConformerInsideBindingSite(theCopiedMolecule)) {
                    this.itsLigandOptimizer.getFunction().setMolecule(theCopiedMolecule);
                    theCopiedMolecule = this.itsLigandOptimizer.optimize();
                    theCopiedMolecule.setProperty(DockingCalculator.SYSTEM_ENERGY_KEY,
                            theCopiedMolecule.getProperty(AbstractConjugatedGradientMinimizer.OPTIMIZED_ENERGY_KEY, Double.class));
                    theResultSet.addAtomContainer(theCopiedMolecule);
                }
            } catch (CloneNotSupportedException ex) {
                System.err.println("Error!! __makeConformerSet(IAtomContainer theConformer, Integer theBondIndex, DockingParameter theParameter) "
                        + "in DockingCalculate");
            }
        }

        return theResultSet;
    }

    private boolean __containEnergyVarianceRange(IAtomContainer theConformer, Double theInitialEnergy) {
        Double theConformerEnergy = this.itsLigandOptimizer.getFunction().calculateEnergyFunction(theConformer);
        Double theAbsoluteEnergyDifference = Math.abs(theInitialEnergy - theConformerEnergy);

        if (this.itsParameter.getMaximumEnergyVariance() < theAbsoluteEnergyDifference) {
            return false;
        } else if (this.itsParameter.getMinimumEnergyVariance() > theAbsoluteEnergyDifference) {
            return false;
        }

        return true;
    }

    private boolean __existConformerInsideBindingSite(IAtomContainer theConformer) {
        Vector3d theCenter = new Vector3d(GeometryTools.get3DCenter(theConformer));

        for (IAtom theAtom : theConformer.atoms()) {
            Vector3d thePosition = new Vector3d(theAtom);

            if (this.itsBindingSite.getRadius() * 2.0 < thePosition.distance(theCenter)) {
                return false;
            }
        }

        return true;
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
            Exceptions.printStackTrace(ex);
        }

        return null;
    }

    private boolean __containHydrogenInBond(Integer theBondIndex) {
        IBond theBond = this.itsLigand.getBond(theBondIndex);

        return theBond.getAtom(Constant.FIRST_INDEX).getAtomicNumber().equals(AtomInformation.Hydrogen.ATOM_NUMBER)
                || theBond.getAtom(Constant.SECOND_INDEX).getAtomicNumber().equals(AtomInformation.Hydrogen.ATOM_NUMBER);
    }

    private void __translateLigand(Vector3d theTranslationVector) {
        for (IAtom theAtom : this.itsLigand.atoms()) {
            Point3d thePosition = theAtom.getPoint3d();

            thePosition.setX(thePosition.x + theTranslationVector.getX());
            thePosition.setY(thePosition.y + theTranslationVector.getY());
            thePosition.setZ(thePosition.z + theTranslationVector.getZ());
        }
    }

    private void __rotateLigand(IAtomContainer theMolecule, Vector3d theTranslationVector) {
        MoleculeManipulator.rotateByCenter(theMolecule, theTranslationVector);
    }

    @Override
    public boolean cancel() {
        this.itsThread.interrupt();

        return true;
    }
}
