package org.bmdrc.predock.calculator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Point3d;
import org.bmdrc.basicchemistry.atom.AtomInformation;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.bmdrc.basicchemistry.tool.MoleculeManipulator;
import org.bmdrc.basicchemistry.util.MoveTogetherAtomNumberList;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.writer.SDFWriter;
import org.bmdrc.basicmath.matrix.Matrix;
import org.bmdrc.predock.energyfunction.GridEnergyFunction;
import org.bmdrc.predock.variable.AtomType;
import org.bmdrc.predock.variable.AtomTypeList;
import org.bmdrc.predock.variable.BindingSite;
import org.bmdrc.predock.variable.ConformerInformation;
import org.bmdrc.predock.variable.DockingParameter;
import org.bmdrc.predock.variable.GridEnergy;
import org.bmdrc.predock.variable.WeightAminoAcidInformationList;
import org.bmdrc.sbff.energyfunction.parameter.SbffInterCalculationParameter;
import org.bmdrc.sbff.solvation.Grid;
import org.bmdrc.sbff.solvation.GridList;
import org.bmdrc.sbff.solvation.SurfaceGridGenerator;
import org.bmdrc.sbff.tool.MpeoeChargeCalculator;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.vector.Vector;
import org.bmdrc.ui.util.ManipulationTool;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.alignment.KabschAlignment;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class BindingPoseGenerator implements Callable<File>, Serializable {

    private static final long serialVersionUID = -1624293284391096849L;

    private class EnergyComparator implements Comparator<Grid> {

        private AtomType itsAtomType;

        public EnergyComparator(AtomType theAtomType) {
            this.itsAtomType = theAtomType;
        }

        @Override
        public int compare(Grid theFirst, Grid theSecond) {
            Double theFirstEnergy = (Double) (theFirst.getProperty(BindingPoseGenerator.ENERGY_KEY, HashMap.class).get(this.itsAtomType));
            Double theSecondEnergy = (Double) (theSecond.getProperty(BindingPoseGenerator.ENERGY_KEY, HashMap.class).get(this.itsAtomType));

            return theFirstEnergy.compareTo(theSecondEnergy);
        }
    }

    private class LigandDistanceVector extends Vector {

        private List<IAtom> itsAtomList;

        public LigandDistanceVector() {
            this.itsAtomList = new ArrayList<IAtom>();
        }

        public LigandDistanceVector(List<IAtom> theAtomList) {
            this.generateDistanceVector(theAtomList);
        }

        public List<IAtom> getAtomList() {
            return itsAtomList;
        }

        public void setAtomList(List<IAtom> itsAtomList) {
            this.itsAtomList = itsAtomList;
        }

        public void generateDistanceVector(List<IAtom> theAtomList) {
            this.itsAtomList = theAtomList;

            for (int fi = 0, fEnd = this.itsAtomList.size() - 1; fi < fEnd; fi++) {
                Point3d theFirstAtomPosition = this.itsAtomList.get(fi).getPoint3d();

                for (int si = fi + 1, sEnd = fEnd + 1; si < sEnd; si++) {
                    Point3d theSecondAtomPosition = this.itsAtomList.get(si).getPoint3d();

                    this.add(theFirstAtomPosition.distance(theSecondAtomPosition));
                }
            }
        }
    }

    private class GridDistanceVector extends Vector {

        private List<GridEnergy> itsGridEnergList;
        private Double itsTotalEnergy;

        public GridDistanceVector(List<GridEnergy> theGridEnergList) {
            this.__generateDistanceVector(theGridEnergList);
            this.__calculateTotalEnergy();
        }

        public List<GridEnergy> getGridEnergList() {
            return this.itsGridEnergList;
        }

        public Double getTotalEnergy() {
            return this.itsTotalEnergy;
        }

        public void setGridEnergList(List<GridEnergy> theGridEnergList) {
            this.__generateDistanceVector(theGridEnergList);
            this.__calculateTotalEnergy();
        }

        private void __generateDistanceVector(List<GridEnergy> theGridEnergList) {
            this.itsGridEnergList = theGridEnergList;

            for (int fi = 0, fEnd = this.itsGridEnergList.size() - 1; fi < fEnd; fi++) {
                Grid theFirstAtomPosition = this.itsGridEnergList.get(fi).getGrid();

                for (int si = fi + 1, sEnd = fEnd + 1; si < sEnd; si++) {
                    Grid theSecondAtomPosition = this.itsGridEnergList.get(si).getGrid();

                    this.add(theFirstAtomPosition.distance(theSecondAtomPosition));
                }
            }
        }

        private void __calculateTotalEnergy() {
            this.itsTotalEnergy = 0.0;

            for (GridEnergy theGridEnergy : this.itsGridEnergList) {
                this.itsTotalEnergy += theGridEnergy.getEnergy();
            }
        }
    }

    private class CandidateData implements Comparable<CandidateData> {

        private LigandDistanceVector itsLigandDistanceVector;
        private GridDistanceVector itsGridDistanceVector;
        private Double itsSimilarity;

        public CandidateData(LigandDistanceVector theLigandDistanceVector, GridDistanceVector theGridDistanceVector, Double theSimilarity) {
            this.itsLigandDistanceVector = theLigandDistanceVector;
            this.itsGridDistanceVector = theGridDistanceVector;
            this.itsSimilarity = theSimilarity;
        }

        public LigandDistanceVector getLigandDistanceVector() {
            return this.itsLigandDistanceVector;
        }

        public GridDistanceVector getGridDistanceVector() {
            return this.itsGridDistanceVector;
        }

        public Double getSimilarity() {
            return itsSimilarity;
        }

        public void setSimilarity(Double theSimilarity) {
            this.itsSimilarity = theSimilarity;
        }

        public List<IAtom> getAtomList() {
            return this.itsLigandDistanceVector.getAtomList();
        }

        public Double getEnergy() {
            return this.itsGridDistanceVector.getTotalEnergy();
        }

        public List<IAtom> getGridListByAtom() {
            List<IAtom> theCopiedAtomList = new ArrayList<>();

            for (int ai = 0, aEnd = this.getAtomList().size(); ai < aEnd; ai++) {
                Grid theGrid = this.itsGridDistanceVector.getGridEnergList().get(ai).getGrid();
                try {
                    IAtom theCopiedAtom = (IAtom) this.getAtomList().get(ai).clone();

                    theCopiedAtom.setPoint3d(theGrid.toPoint3d());
                    theCopiedAtomList.add(theCopiedAtom);
                } catch (CloneNotSupportedException ex) {
                    ex.printStackTrace();
                }
            }

            return theCopiedAtomList;
        }

        @Override
        public int compareTo(CandidateData theTemplate) {
            Double theFinalValue = this.itsSimilarity * this.getEnergy();
            Double theTemplateFinalValue = theTemplate.getSimilarity() * theTemplate.getEnergy();

            return theFinalValue.compareTo(theTemplateFinalValue);
        }
    }

    private Protein itsProtein;
    private IAtomContainer itsLigand;
    private BindingSite itsBindingSite;
    private DockingParameter itsParameter;
    private SurfaceGridGenerator itsSurfaceGridGenerator;
    private AtomTypeList itsTemplateAtomTypeList;
    private WeightAminoAcidInformationList itsWeightedAminoAcidSerialList;
    //Call function variable
    private IAtomContainer itsConformer;
    private Map<AtomType, List<IAtom>> itsNewAtomTypeMap;
    private List<AtomTypeList> itsAtomTypeGroup;
    private Map<AtomType, List<GridEnergy>> itsSelectedGridMap;
    private Integer itsThreadIndex;
    private String itsTempDir;

    //constant Double variable
    private final double MINIMUM_SHELL_SIZE = 2.0;
    private final double MAXIMUM_SHELL_SIZE = 4.0;
    private final double MINIMUM_DISTANCE_BETWEEN_ATOMS = 1.0;
    private final double DEFAULT_WEIGHT = 1.0;
    private final double DEFAULT_WEIGHTED_AMINO_ACID = 2.0;
    //constant String variable
    private static final String ENERGY_KEY = "Binding pose energy key";
    private static final String FINAL_SELECTED_ATOM_TYPE_KEY = "Final selected atom type";
    private final String TEMP_FILE_NAME = "BindingPoseGenerator_Temp_";

    public BindingPoseGenerator() {
        this.itsSurfaceGridGenerator = new SurfaceGridGenerator();
    }

    public BindingPoseGenerator(BindingPoseGenerator theBindingPoseGenerator) throws CloneNotSupportedException {
        this(theBindingPoseGenerator, null, null, null, Constant.TEMP_DIR);
    }

    public BindingPoseGenerator(BindingPoseGenerator theBindingPoseGenerator, IAtomContainer theConformer, Map<AtomType, List<IAtom>> theNewAtomTypeMap,
            Integer theThreadIndex, String theTempDir) throws CloneNotSupportedException {
        this.itsLigand = theBindingPoseGenerator.itsLigand.clone();
        this.itsBindingSite = new BindingSite(theBindingPoseGenerator.itsBindingSite);
        this.itsParameter = new DockingParameter(theBindingPoseGenerator.itsParameter);
        this.itsTemplateAtomTypeList = new AtomTypeList(theBindingPoseGenerator.itsTemplateAtomTypeList);
        this.itsSelectedGridMap = new HashMap<>(theBindingPoseGenerator.itsSelectedGridMap);
        this.itsAtomTypeGroup = new ArrayList<>(theBindingPoseGenerator.itsAtomTypeGroup);

        this.itsConformer = theConformer;
        this.itsNewAtomTypeMap = theNewAtomTypeMap;
        this.itsThreadIndex = theThreadIndex;

        this.itsSurfaceGridGenerator = new SurfaceGridGenerator();
    }

    @Override
    public File call() {
        System.out.println("Start pose generating #" + this.itsThreadIndex);

        long time = System.currentTimeMillis();

        IAtomContainerSet theBindingPoseSet = this.__generateBindingPoseSet(this.itsConformer);
        IAtomContainerSet theResultBindingPoseSet = new AtomContainerSet();

        for (IAtomContainer theBindingPose : theBindingPoseSet.atomContainers()) {
            if (!this.__containSameBindingPose(theBindingPose, theResultBindingPoseSet)) {
                theResultBindingPoseSet.addAtomContainer(theBindingPose);
            }
        }

        File theTempFile = new File(Constant.TEMP_DIR + this.TEMP_FILE_NAME + time + "_" + this.itsThreadIndex + ".sdf");
        int theIndex = 1;

        if(!new File(Constant.TEMP_DIR).exists()) {
            File theTempDir = new File(Constant.TEMP_DIR);
            
            theTempDir.mkdir();
            System.out.println("Make Temp Directory : " + theTempDir.getAbsolutePath());
        }
        
        if (!theResultBindingPoseSet.isEmpty()) {
            SDFWriter.writeSDFile(theResultBindingPoseSet, theTempFile);
        }
        
        System.out.print("Completed pose generating #" + this.itsThreadIndex + " ");
        ManipulationTool.printCalculationTime(time);
        System.out.println(" " + theResultBindingPoseSet.getAtomContainerCount());

        return theTempFile;
    }

    public IAtomContainerSet calculateBindingPose(Protein theProtein, BindingSite theBindingSite, DockingParameter theParameter,
            IAtomContainer theLigand, List<ConformerInformation> theConformerInformationList, WeightAminoAcidInformationList theWeightedAminoAcidSerialList) {
        return this.calculateBindingPose(theProtein, theBindingSite, theParameter, theLigand, theConformerInformationList, theWeightedAminoAcidSerialList, new MoveTogetherAtomNumberList(theLigand));
    }

    public IAtomContainerSet calculateBindingPose(Protein theProtein, BindingSite theBindingSite, DockingParameter theParameter,
            IAtomContainer theLigand, List<ConformerInformation> theConformerInformationList, WeightAminoAcidInformationList theWeightedAminoAcidSerialList, 
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberList) {
        this.itsProtein = theProtein;
        this.itsBindingSite = theBindingSite;
        this.itsParameter = theParameter;
        this.itsLigand = theLigand;
        this.itsWeightedAminoAcidSerialList = theWeightedAminoAcidSerialList;

        this.__initializeVariable();

        IAtomContainerSet theResultBindingPoseSet = new AtomContainerSet();
        long time = System.currentTimeMillis();

        System.out.println("Start to search binding pose " + ManipulationTool.getTime(time));

        Map<AtomType, List<IAtom>> theAtomTypeMap = this.__generateAtomTypeMap();
        System.out.println("Completed Atom Type Map " + ManipulationTool.getTime(time));

        this.__setSelectedGridMap(theAtomTypeMap);
        System.out.println("Completed selected grid map " + ManipulationTool.getTime(time));

        this.itsAtomTypeGroup = this.__generateAtomTypeGroup(theAtomTypeMap);//this.__generateAtomTypeGroup(theAtomTypeMap, theSelectedGridMap);
        System.out.println(this.itsAtomTypeGroup.size());
//        System.out.print("Completed atom type group ");
//        ManipulationTool.printCalculationTime(time);
//        IAtomContainerSet theResultBindingPoseSet = new AtomContainerSet();

        int theIndex = 1;

        for (ConformerInformation theConformerInformation : theConformerInformationList) {
            IAtomContainer theConformer = BindingConformerGenerator.generateConformer(theConformerInformation, theLigand, theMoveTogetherAtomNumberList);
            Map<AtomType, List<IAtom>> theNewAtomTypeMap = this.__regenerateAtomTypeMap(theAtomTypeMap, theConformer);
//            IAtomContainerSet theBindingPoseSet = this.__generateBindingPoseSet(theConformer);
            try {
                //            for(int mi = theBindingPoseSet.getAtomContainerCount()-1; mi >= 0; mi--) {
//            for (IAtomContainer theBindingPose : theBindingPoseSet.atomContainers()) {
//                if (!this.__containSameBindingPose(theBindingPose, theResultBindingPoseSet)) {
//                    theResultBindingPoseSet.addAtomContainer(theBindingPose);
//                }
//            }

                theResultBindingPoseSet.add(SDFReader.openMoleculeFile(new BindingPoseGenerator(this, theConformer, theNewAtomTypeMap, theIndex++, this.itsTempDir).call()));
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(BindingPoseGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.print("Completed pose generating #" + theIndex++ + " ");
            ManipulationTool.printCalculationTime(time);
        }

        return theResultBindingPoseSet;
    }

    public List<File> calculateBindingPoseUsingMultiThread(Protein theProtein, BindingSite theBindingSite, DockingParameter theParameter,
            IAtomContainer theLigand, List<ConformerInformation> theConformerInformationList, WeightAminoAcidInformationList theWeightedAminoAcidSerialList, int theThreadCount) {
        return this.calculateBindingPoseUsingMultiThread(theProtein, theBindingSite, theParameter, theLigand, theConformerInformationList, theWeightedAminoAcidSerialList,
                new MoveTogetherAtomNumberList(theLigand), theThreadCount);
    }

    public List<File> calculateBindingPoseUsingMultiThread(Protein theProtein, BindingSite theBindingSite, DockingParameter theParameter,
            IAtomContainer theLigand, List<ConformerInformation> theConformerInformationList, WeightAminoAcidInformationList theWeightedAminoAcidSerialList, 
            MoveTogetherAtomNumberList theMoveTogetherAtomNumberList,
            int theThreadCount) {
        this.itsProtein = theProtein;
        this.itsBindingSite = theBindingSite;
        this.itsParameter = theParameter;
        this.itsLigand = theLigand;
        this.itsWeightedAminoAcidSerialList = theWeightedAminoAcidSerialList;

        this.__initializeVariable();

        long time = System.currentTimeMillis();

        System.out.print("Start to search binding pose ");
        ManipulationTool.printCalculationTime(time);

        Map<AtomType, List<IAtom>> theAtomTypeMap = this.__generateAtomTypeMap();
        System.out.println("Completed Atom Type Map " + ManipulationTool.getTime(time));
        
        this.__setSelectedGridMap(theAtomTypeMap);
        System.out.println("Completed selected grid map " + ManipulationTool.getTime(time));
        
        this.itsAtomTypeGroup = this.__generateAtomTypeGroup(theAtomTypeMap);
        System.out.println("Completed atom type group " + ManipulationTool.getTime(time));
        
        IAtomContainerSet theResultBindingPoseSet = new AtomContainerSet();

        int theUsableThreadCount = theThreadCount > Runtime.getRuntime().availableProcessors() ? Runtime.getRuntime().availableProcessors() : theThreadCount;

        ExecutorService theExecutorService = Executors.newFixedThreadPool(theUsableThreadCount);
        List<Future<File>> theResultList = new ArrayList<>();

        int theIndex = 1;

        for (ConformerInformation theConformerInformation : theConformerInformationList) {
            IAtomContainer theConformer = BindingConformerGenerator.generateConformer(theConformerInformation, theLigand, theMoveTogetherAtomNumberList);
            Map<AtomType, List<IAtom>> theNewAtomTypeMap = this.__regenerateAtomTypeMap(theAtomTypeMap, theConformer);

            try {
                theResultList.add(theExecutorService.submit(new BindingPoseGenerator(this, theConformer, theNewAtomTypeMap, theIndex++, this.itsTempDir)));
            } catch (CloneNotSupportedException ex) {
                System.err.println("Error for calculator : calculateBindingPoseUsingMultiThread(Protein theProtein, BindingSite theBindingSite, DockingParameter theParameter,\n"
                        + "IAtomContainer theLigand, List<ConformerInformation> theConformerInformationList, List<Integer> theWeightedAminoAcidSerialList, MoveTogetherAtomNumberList theMoveTogetherAtomNumberList, \n"
                        + "int theThreadCount)");
            }
        }

        List<File> theResultFileList = new ArrayList<>();

        for (Future<File> theResultFuture : theResultList) {
            try {
                File theResultFile = theResultFuture.get();

                theResultFileList.add(theResultFile);
            } catch (InterruptedException ex) {
                Logger.getLogger(BindingPoseGenerator.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(BindingPoseGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        theExecutorService.shutdown();

        System.out.print("Completed binding pose generator : " + theResultBindingPoseSet.getAtomContainerCount());
        ManipulationTool.printCalculationTime(time);

        return theResultFileList;
//        return theResultBindingPoseSet;
    }

    public IAtomContainerSet calculateBindingPose(Protein theProtein, BindingSite theBindingSite, DockingParameter theParameter,
            IAtomContainer theLigand, IAtomContainerSet theConformers, WeightAminoAcidInformationList theWeightedAminoAcidSerialList) {
        this.itsProtein = theProtein;
        this.itsBindingSite = theBindingSite;
        this.itsParameter = theParameter;
        this.itsLigand = theLigand;

        this.__initializeVariable();

        long time = System.currentTimeMillis();

        System.out.print("Start to search binding pose ");
        ManipulationTool.printCalculationTime(time);

        Map<AtomType, List<IAtom>> theAtomTypeMap = this.__generateAtomTypeMap();
        System.out.print("Completed Atom Type Map ");
        ManipulationTool.printCalculationTime(time);

        this.__setSelectedGridMap(theAtomTypeMap);
        System.out.print("Completed selected grid map ");
        ManipulationTool.printCalculationTime(time);

        this.itsAtomTypeGroup = this.__generateAtomTypeGroup(theAtomTypeMap);
        System.out.print("Completed atom type group ");
        ManipulationTool.printCalculationTime(time);
        IAtomContainerSet theResultBindingPoseSet = new AtomContainerSet();

        int theIndex = 1;

        for (IAtomContainer theConformer : theConformers.atomContainers()) {
            Map<AtomType, List<IAtom>> theNewAtomTypeMap = this.__regenerateAtomTypeMap(theAtomTypeMap, theConformer);
            IAtomContainerSet theBindingPoseSet = this.__generateBindingPoseSet(theConformer);

            for (IAtomContainer theBindingPose : theBindingPoseSet.atomContainers()) {
                if (!this.__containSameBindingPose(theBindingPose, theResultBindingPoseSet)) {
                    theResultBindingPoseSet.addAtomContainer(theBindingPose);
                }
            }

            System.out.print("Completed pose generating #" + theIndex++ + " ");
            ManipulationTool.printCalculationTime(time);
        }

        return theResultBindingPoseSet;
    }

    private Map<AtomType, List<IAtom>> __regenerateAtomTypeMap(Map<AtomType, List<IAtom>> theAtomTypeMap, IAtomContainer theConformer) {
        Map<AtomType, List<IAtom>> theNewAtomTypeMap = new TreeMap<>();

        for (AtomType theAtomType : theAtomTypeMap.keySet()) {
            List<IAtom> theAtomList = new ArrayList<>();

            for (IAtom theTargetAtom : theAtomTypeMap.get(theAtomType)) {
                Integer theAtomIndex = this.itsLigand.getAtomNumber(theTargetAtom);

                theAtomList.add(theConformer.getAtom(theAtomIndex));
            }

            theNewAtomTypeMap.put(theAtomType, theAtomList);
        }

        return theNewAtomTypeMap;
    }

    public IAtomContainerSet calculateBindingPose(Protein theProtein, BindingSite theBindingSite, DockingParameter theParameter,
            IAtomContainer theLigand, WeightAminoAcidInformationList theWeightedAminoAcidSerialList) {
        this.itsProtein = theProtein;
        this.itsBindingSite = theBindingSite;
        this.itsParameter = theParameter;
        this.itsLigand = theLigand;
        this.itsWeightedAminoAcidSerialList = theWeightedAminoAcidSerialList;

        this.__initializeVariable();

        Map<AtomType, List<IAtom>> theAtomTypeMap = this.__generateAtomTypeMap();

        this.__setSelectedGridMap(theAtomTypeMap);

        List<AtomTypeList> theAtomTypeGroup = this.__generateAtomTypeGroup(theAtomTypeMap);
        IAtomContainerSet theResultBindingPoseSet = this.__generateBindingPoseSet(this.itsLigand);

        return theResultBindingPoseSet;
    }

    private void __initializeVariable() {
        MpeoeChargeCalculator theChargeCalculator = new MpeoeChargeCalculator();

        theChargeCalculator.inputMpeoeCharge(this.itsLigand);
        theChargeCalculator.inputMpeoeCharge(this.itsProtein);
    }

    private Map<AtomType, List<IAtom>> __generateAtomTypeMap() {
        Map<AtomType, List<IAtom>> theAtomTypeMap = new TreeMap<>();

        this.itsTemplateAtomTypeList = new AtomTypeList();

        for (IAtom theAtom : this.itsLigand.atoms()) {
            AtomType theAtomType = new AtomType(theAtom);

            if (!theAtomTypeMap.keySet().contains(theAtomType)) {
                theAtomTypeMap.put(theAtomType, new ArrayList<IAtom>());
                this.itsTemplateAtomTypeList.add(theAtomType);
            }

            theAtomTypeMap.get(theAtomType).add(theAtom);
        }

        Collections.sort(this.itsTemplateAtomTypeList);

        return theAtomTypeMap;
    }

    private <Type extends Object> List<AtomTypeList> __generateAtomTypeGroup(Map<AtomType, List<IAtom>> theAtomTypeMap) {
        List<AtomTypeList> theResultAtomTypeGroup = new ArrayList<AtomTypeList>();
        Set<AtomTypeList> theNewAtomTypeGroup = new HashSet<AtomTypeList>();

        for (int ai = 0, aEnd = this.itsTemplateAtomTypeList.size(); ai < aEnd; ai++) {
            AtomTypeList theNewAtomTypeList = new AtomTypeList();
            AtomType theAtomType = this.itsTemplateAtomTypeList.get(ai);

            theNewAtomTypeList.add(theAtomType);
            theNewAtomTypeGroup.add(theNewAtomTypeList);
        }

        for (int ii = 1, iEnd = this.itsParameter.getMaximumGridCountUsedInMatching(); ii < iEnd; ii++) {
            theNewAtomTypeGroup = this.__generateAtomTypeGroup(theNewAtomTypeGroup, theAtomTypeMap);

            if (ii > 1) {
                theResultAtomTypeGroup.addAll(theNewAtomTypeGroup);
            }
        }

        return theResultAtomTypeGroup;
    }

    private <Type extends Object> List<AtomType> __getAtomTypeList(Map<AtomType, List<Type>> theAtomTypeMap) {
        List<AtomType> theAtomTypeList = new ArrayList<>();

        for (AtomType theAtomType : theAtomTypeMap.keySet()) {
            theAtomTypeList.add(theAtomType);
        }

        return theAtomTypeList;
    }

    private <Type extends Object> Set<AtomTypeList> __generateAtomTypeGroup(Set<AtomTypeList> thePreviousAtomTypeGroup,
            Map<AtomType, List<Type>> theAtomTypeMap) {
        Set<AtomTypeList> theNewAtomTypeGroup = new HashSet<AtomTypeList>();

        for (AtomTypeList thePreviousAtomTypeList : thePreviousAtomTypeGroup) {
            Map<AtomType, Integer> theAtomTypeCountMap = thePreviousAtomTypeList.getAtomTypeCountMap();
            AtomType theLastAtomType = thePreviousAtomTypeList.get(thePreviousAtomTypeList.size() - 1);

            for (int ai = this.itsTemplateAtomTypeList.indexOf(theLastAtomType), aEnd = this.itsTemplateAtomTypeList.size(); ai < aEnd; ai++) {
                AtomTypeList theNewAtomTypeList = new AtomTypeList(thePreviousAtomTypeList);
                AtomType theAtomType = this.itsTemplateAtomTypeList.get(ai);

                theNewAtomTypeList.add(theAtomType);
                Collections.sort(theNewAtomTypeList);

                if (!theAtomTypeCountMap.containsKey(theAtomType)) {
                    theNewAtomTypeList.getAtomTypeCountMap().put(theAtomType, 1);
                    theNewAtomTypeGroup.add(theNewAtomTypeList);
                } else if (theAtomTypeCountMap.get(theAtomType).compareTo(theAtomTypeMap.get(theAtomType).size()) <= 0) {
                    theNewAtomTypeList.getAtomTypeCountMap().put(theAtomType, theAtomTypeCountMap.get(theAtomType) + 1);
                    theNewAtomTypeGroup.add(theNewAtomTypeList);
                }
            }
        }

        return theNewAtomTypeGroup;
    }

    private TwoDimensionList<GridList> __generateGridList() {
        TwoDimensionList<GridList> theAminoAcidSurfaceGridList = new TwoDimensionList<GridList>();

        List<GridList> theAminoAcidGridList = new ArrayList<>(this.itsSurfaceGridGenerator.draw(this.itsBindingSite.getAminoAcids(), this.itsParameter.getGridShellRadius(),
                this.itsParameter.getGridInterval()));

        for (int ai = 0, aEnd = theAminoAcidGridList.size(); ai < aEnd; ai++) {
            GridList theGridList = theAminoAcidGridList.get(ai);

            for (int gi = theGridList.size() - 1; gi >= 0; gi--) {
                if (this.itsBindingSite.getRadius().compareTo(this.itsBindingSite.getCenterPosition().distance(theGridList.get(gi))) < 0) {
                    theGridList.remove(gi);
                } else {
                    theGridList.get(gi).setProperty(this.ENERGY_KEY, new HashMap<AtomType, Double>());
                    theGridList.get(gi).setProperty(BindingSite.ATOM_SERIAL_KEY,
                            this.itsBindingSite.getAminoAcids().getAtom(ai).getProperty(BindingSite.ATOM_SERIAL_KEY).toString());
                }
            }
        }

        theAminoAcidSurfaceGridList.add(theAminoAcidGridList);

        return theAminoAcidSurfaceGridList;
    }

    private void __setSelectedGridMap(Map<AtomType, List<IAtom>> theAtomTypeMap) {
        TwoDimensionList<GridList> theAminoAcidSurfaceGridList = this.__generateGridList();

        this.itsSelectedGridMap = new HashMap<AtomType, List<GridEnergy>>();

        for (List<GridList> theGridListByAtom : theAminoAcidSurfaceGridList) {
            List<GridList> theGridListByAminoAcid = this.__getGridListByAminoAcid(theGridListByAtom);

            for (GridList theGridList : theGridListByAminoAcid) {
                if (!theGridList.isEmpty()) {
                    this.__setSelectedGridMap(theGridList, theAtomTypeMap);
                }
            }
        }
    }

    private void __setSelectedGridMap(GridList theGridList, Map<AtomType, List<IAtom>> theAtomTypeMap) {
        List<GridEnergy> theOptimumGridList = this.__calculateOptimumGrid(theGridList, theAtomTypeMap);

        if (theOptimumGridList != null) {
            for (GridEnergy theOptimumGrid : theOptimumGridList) {
                if (!this.itsSelectedGridMap.containsKey(theOptimumGrid.getAtomType())) {
                    this.itsSelectedGridMap.put(theOptimumGrid.getAtomType(), new ArrayList<GridEnergy>());
                }

                List<GridEnergy> theTargetList = this.itsSelectedGridMap.get(theOptimumGrid.getAtomType());

                theTargetList.add(theOptimumGrid);

                if (theTargetList.size() > this.itsParameter.getMaximumGridCountUsedInMatching()) {
                    Collections.sort(theTargetList, GridEnergy.getEnergyComparator());
                    theTargetList.remove(theTargetList.size() - 1);
                }
            }
        }
    }

    private List<GridEnergy> __calculateOptimumGrid(GridList theGridList, Map<AtomType, List<IAtom>> theAtomTypeMap) {
        try {
            GridEnergyFunction theGridEnergyFunction = this.__initializeGridEnergyFunction();
            List<GridEnergy> theOptimumGridList = new ArrayList<>();

            for (AtomType theAtomType : theAtomTypeMap.keySet()) {
                IAtom theTemplateAtom = (IAtom) theAtomTypeMap.get(theAtomType).get(Constant.FIRST_INDEX).clone();
                GridEnergy theGridEnergy = new GridEnergy(Double.MAX_VALUE, theAtomType);

                List<Double> theEnergyList = new ArrayList<>();

                Collections.addAll(theEnergyList, null, null, null);

                theGridEnergyFunction.getMolecule().setAtom(theGridEnergyFunction.getMolecule().getAtomCount() - 1, theTemplateAtom);
                theGridEnergyFunction.regenerateCalculableSet();

                for (Grid theGrid : theGridList) {
                    theTemplateAtom.setPoint3d(theGrid.toPoint3d());

                    Double theEnergy = theGridEnergyFunction.calculateEnergyFunction();
                    Double theElectroStaticEnergy = theGridEnergyFunction.getElectroStaticEnergyFunction().calculateEnergyFunction();
                    Double theHydrogenBondEnergy = theGridEnergyFunction.getHydrogenBondEnergyFunctionInGrid().calculateEnergyFunction();
                    Double theNonbondingEnergy = theGridEnergyFunction.getNonbondingEnergyFunction().calculateEnergyFunction();

                    if (theGridEnergy.getEnergy().compareTo(theEnergy) > 0) {
                        theGridEnergy.setEnergy(theEnergy);
                        theGridEnergy.setGrid(theGrid);

                        theEnergyList.set(0, theElectroStaticEnergy);
                        theEnergyList.set(1, theHydrogenBondEnergy);
                        theEnergyList.set(2, theNonbondingEnergy);
                    }
                }

//                if (theGridEnergy.getEnergy().compareTo(0.0) < 0) {
                theOptimumGridList.add(theGridEnergy);
//                }
            }

            theOptimumGridList.sort(GridEnergy.getEnergyComparator());

            return theOptimumGridList;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private GridEnergyFunction __initializeGridEnergyFunction() throws CloneNotSupportedException {
        IAtomContainer theMolecule = this.itsBindingSite.getAminoAcids().clone();
        List<Integer> theNotCalculatedAtomIndexList = new ArrayList<Integer>();

        theMolecule.addAtom((IAtom) this.itsLigand.getAtom(Constant.FIRST_INDEX).clone());

        for (int ai = 0, aEnd = this.itsBindingSite.getAminoAcids().getAtomCount(); ai < aEnd; ai++) {
            theNotCalculatedAtomIndexList.add(ai);
        }

        SbffInterCalculationParameter theCalculationParameter = new SbffInterCalculationParameter(this.itsParameter.getCalculationParameter().getInterParameter());

        theCalculationParameter.setNotCalculableAtomIndexList(theNotCalculatedAtomIndexList);

        return new GridEnergyFunction(theMolecule, theCalculationParameter);
    }

    private List<GridList> __getGridListByAminoAcid(List<GridList> theGridListByAtom) {
        List<Integer> theNewAminoAcidAtomIndexList = this.itsBindingSite.getNewAminoAcidAtomIndexList();
        List<GridList> theGridListByAminoAcid = new ArrayList<>();

        for (int li = 0, lEnd = theNewAminoAcidAtomIndexList.size() - 1; li < lEnd; li++) {
            GridList theGridList = new GridList();

            for (int gi = theNewAminoAcidAtomIndexList.get(li), gEnd = theNewAminoAcidAtomIndexList.get(li + 1); gi < gEnd; gi++) {
                theGridList.addAll(theGridListByAtom.get(gi));
            }

            theGridListByAminoAcid.add(theGridList);
        }

        return theGridListByAminoAcid;
    }

    private List<GridDistanceVector> __generateGridDistanceVectorList(TwoDimensionList<GridEnergy> theGrid2dListByAtomType) {
        List<GridDistanceVector> theGridDistanceVectorList = new ArrayList<GridDistanceVector>();

        for (List<GridEnergy> theAtomList : theGrid2dListByAtomType) {
            GridDistanceVector theGridDistanceVector = new GridDistanceVector(theAtomList);

            if (theGridDistanceVector.getTotalEnergy().compareTo(0.0) < 0) {
                theGridDistanceVectorList.add(theGridDistanceVector);
            }
        }

        return theGridDistanceVectorList;
    }

    private List<GridEnergy> __generateGridListByAtomType(AtomTypeList theAtomTypeList) {
        List<GridEnergy> theResultGridEnergyList = new ArrayList<>();

        for (AtomType theAtomType : theAtomTypeList) {
            List<GridEnergy> theGridEnergyList = this.itsSelectedGridMap.get(theAtomType);

            for (GridEnergy theGridEnergy : theGridEnergyList) {
                if (!theResultGridEnergyList.contains(theGridEnergy)) {
                    theResultGridEnergyList.add(theGridEnergy);
                    break;
                }
            }
        }

        return theResultGridEnergyList;
    }

    private TwoDimensionList<GridEnergy> __generateGrid2dListByAtomType(AtomTypeList theAtomTypeList, Map<AtomType, List<GridEnergy>> theAtomTypeMap) {
        TwoDimensionList<GridEnergy> theResult2dList = new TwoDimensionList<GridEnergy>();

        theResult2dList.add(new ArrayList<GridEnergy>());

        for (AtomType theAtomType : theAtomTypeList) {
            theResult2dList = this.__generateGrid2dListByAtomType(theAtomTypeMap.get(theAtomType), theResult2dList);
        }

        return theResult2dList;
    }

    private TwoDimensionList<GridEnergy> __generateGrid2dListByAtomType(List<GridEnergy> theGridEnergyList,
            TwoDimensionList<GridEnergy> thePrevious2dList) {
        TwoDimensionList<GridEnergy> theNew2dList = new TwoDimensionList<GridEnergy>();

        for (List<GridEnergy> thePreviousList : thePrevious2dList) {
            for (GridEnergy theTargetGridEnergy : theGridEnergyList) {
                if (!thePreviousList.contains(theTargetGridEnergy)) {
                    List<GridEnergy> theNewGridEnergyList = new ArrayList<GridEnergy>(thePreviousList);

                    theNewGridEnergyList.add(theTargetGridEnergy);
                    theNew2dList.add(theNewGridEnergyList);
                }
            }
        }

        return theNew2dList;
    }

    private Map<AtomTypeList, List<LigandDistanceVector>> __generateLigandDistanceVectorMap(List<AtomTypeList> theAtomTypeGroup,
            Map<AtomType, List<IAtom>> theAtomTypeMap) {
        Map<AtomTypeList, List<LigandDistanceVector>> theLigandDistanceVectorMap = new HashMap<AtomTypeList, List<LigandDistanceVector>>();

        for (AtomTypeList theAtomTypeList : theAtomTypeGroup) {
            TwoDimensionList<IAtom> theAtom2dListByAtomType = this.__generateAtom2dListByAtomType(theAtomTypeList, theAtomTypeMap);
            List<LigandDistanceVector> theLigandDistanceVectorList = this.__generateLigandDistanceVectorList(theAtom2dListByAtomType);

            theLigandDistanceVectorMap.put(theAtomTypeList, theLigandDistanceVectorList);
        }

        return theLigandDistanceVectorMap;
    }

    private List<LigandDistanceVector> __generateLigandDistanceVectorList(TwoDimensionList<IAtom> theAtom2dListByAtomType) {
        List<LigandDistanceVector> theLigandDistanceVectorList = new ArrayList<LigandDistanceVector>();

        for (List<IAtom> theAtomList : theAtom2dListByAtomType) {
            theLigandDistanceVectorList.add(new LigandDistanceVector(theAtomList));
        }

        return theLigandDistanceVectorList;
    }

    private TwoDimensionList<IAtom> __generateAtom2dListByAtomType(AtomTypeList theAtomTypeList, Map<AtomType, List<IAtom>> theAtomTypeMap) {
        TwoDimensionList<IAtom> theResult2dList = new TwoDimensionList<IAtom>();

        theResult2dList.add(new ArrayList<IAtom>());

        for (AtomType theAtomType : theAtomTypeList) {
            theResult2dList = this.__generateAtom2dListByAtomType(theAtomTypeMap.get(theAtomType), theResult2dList);
        }

        return theResult2dList;
    }

    private TwoDimensionList<IAtom> __generateAtom2dListByAtomType(List<IAtom> theAtomList, TwoDimensionList<IAtom> thePrevious2dList) {
        TwoDimensionList<IAtom> theNew2dList = new TwoDimensionList<IAtom>();

        for (List<IAtom> thePreviousList : thePrevious2dList) {
            for (IAtom theTargetAtom : theAtomList) {
                if (!thePreviousList.contains(theTargetAtom)) {
                    List<IAtom> theNewAtomList = new ArrayList<IAtom>(thePreviousList);

                    theNewAtomList.add(theTargetAtom);
                    theNew2dList.add(theNewAtomList);
                }
            }
        }

        return theNew2dList;
    }

    private IAtomContainerSet __generateBindingPoseSet(IAtomContainer theConformer) {
        IAtomContainerSet theResultBindingPoseSet = new AtomContainerSet();

        for (AtomTypeList theAtomTypeList : this.itsAtomTypeGroup) {
            List<GridEnergy> theGridListByAtomType = this.__generateGridListByAtomType(theAtomTypeList);

            if (theGridListByAtomType.size() == theAtomTypeList.size() && this.__isNegativeGridEnergy(theGridListByAtomType)) {
                TwoDimensionList<IAtom> theAtom2dListByAtomType = this.__generateAtom2dListByAtomType(theAtomTypeList, this.itsNewAtomTypeMap);
                List<LigandDistanceVector> theLigandDistanceVectorList = this.__generateLigandDistanceVectorList(theAtom2dListByAtomType);
                GridDistanceVector theGridDistanceVector = new GridDistanceVector(theGridListByAtomType);
                IAtomContainer theBindingPose = this.__generateBindingPose(theLigandDistanceVectorList, theGridDistanceVector, theConformer);

                if (theBindingPose != null && !this.__containSameBindingPose(theBindingPose, theResultBindingPoseSet)) {
                    theResultBindingPoseSet.addAtomContainer(theBindingPose);
                }
            }
        }

        return theResultBindingPoseSet;
    }

    private boolean __isNegativeGridEnergy(List<GridEnergy> theGridListByAtomType) {
        double theEnergy = 0.0;

        for (GridEnergy theGridEnergy : theGridListByAtomType) {
            theEnergy += theGridEnergy.getEnergy();
        }

        return theEnergy < 0.0;
    }

    private IAtomContainer __generateBindingPose(List<LigandDistanceVector> theLigandDistanceVectorList,
            GridDistanceVector theGridDistanceVector, IAtomContainer theConformer) {
        IAtomContainer theBindingPose = null;

        for (LigandDistanceVector theLigandDistanceVector : theLigandDistanceVectorList) {
            CandidateData theMinimumCandidateData = new CandidateData(null, theGridDistanceVector, 0.0);
            Double theSimilarity = theLigandDistanceVector.calculateSimilarity(theGridDistanceVector);

            if (!theSimilarity.isNaN() && theMinimumCandidateData.getSimilarity().compareTo(theSimilarity) < 0) {
                theMinimumCandidateData = new CandidateData(theLigandDistanceVector, theGridDistanceVector, theSimilarity);

                if (theMinimumCandidateData.getGridDistanceVector() != null) {
                    IAtomContainer theCheckBindingPose = this.__generateBindingPose(theMinimumCandidateData, theConformer);

                    if (this.__isCorrectBindingPose(theCheckBindingPose)) {
                        theBindingPose = theCheckBindingPose;
                    }
                }
            }
        }

        return theBindingPose;
    }

    private IAtomContainerSet __generateBindingPoseSet(List<LigandDistanceVector> theLigandDistanceVectorList,
            List<GridDistanceVector> theGridDistanceVectorList, IAtomContainer theConformer) {
        IAtomContainerSet theBindingPoseSet = new AtomContainerSet();

        for (LigandDistanceVector theLigandDistanceVector : theLigandDistanceVectorList) {
            CandidateData theMinimumCandidateData = new CandidateData(theLigandDistanceVector, null, 0.0);

            for (GridDistanceVector theGridDistanceVector : theGridDistanceVectorList) {
                Double theSimilarity = theLigandDistanceVector.calculateSimilarity(theGridDistanceVector);

                if (!theSimilarity.isNaN() && theMinimumCandidateData.getSimilarity().compareTo(theSimilarity) < 0) {
                    theMinimumCandidateData = new CandidateData(theLigandDistanceVector, theGridDistanceVector, theSimilarity);
                }
            }

            if (theMinimumCandidateData.getGridDistanceVector() != null) {
                IAtomContainer theBindingPose = this.__generateBindingPose(theMinimumCandidateData, theConformer);

                if (this.__isCorrectBindingPose(theBindingPose) && !this.__containSameBindingPose(theBindingPose, theBindingPoseSet)) {
                    theBindingPoseSet.addAtomContainer(theBindingPose);
                }
            }
        }

        return theBindingPoseSet;
    }

    private boolean __containSameBindingPose(IAtomContainer theBindingPose, IAtomContainerSet theBindingPoseSet) {
        int theAtomCount = theBindingPose.getAtomCount();

        for (IAtomContainer theTargetPose : theBindingPoseSet.atomContainers()) {
            boolean theJugment = true;

            for (int ai = 0; ai < theAtomCount; ai++) {
                IAtom theTargetAtom = theBindingPose.getAtom(ai);
                IAtom theResultAtom = theTargetPose.getAtom(ai);

                if (!this.__isSamePosition(theTargetAtom, theResultAtom)) {
                    theJugment = false;
                    break;
                }
            }

            if (theJugment) {
                return true;
            }
        }

        return false;
    }

    private boolean __isSamePosition(IAtom theFirstAtom, IAtom theSecondAtom) {
        Vector3d theFirstPosition = new Vector3d(theFirstAtom);
        Vector3d theSecondPosition = new Vector3d(theSecondAtom);

        return theFirstPosition.toString().equals(theSecondPosition.toString());
    }

    private boolean __isCorrectBindingPose(IAtomContainer theBindingPose) {
        if (theBindingPose == null) {
            return false;
        }

        for (IAtom theBindingPoseAtom : theBindingPose.atoms()) {
            Vector3d theBindingPoseAtomPosition = new Vector3d(theBindingPoseAtom);

            if (theBindingPoseAtomPosition.distance(this.itsBindingSite.getCenterPosition()) > this.itsBindingSite.getRadius()) {
                return false;
            }

            for (IAtom theAminoAcidAtom : this.itsBindingSite.getAminoAcids().atoms()) {
                Vector3d theAminoAcidAtomPosition = new Vector3d(theAminoAcidAtom);

                if (theBindingPoseAtomPosition.distance(theAminoAcidAtomPosition) < this.MINIMUM_DISTANCE_BETWEEN_ATOMS) {
                    return false;
                }
            }
        }

        return true;
    }

    private IAtomContainer __generateBindingPose(CandidateData theCandidateData, IAtomContainer theConformer) {
        try {
            List<IAtom> theLigandAtomList = theCandidateData.getAtomList();
            List<IAtom> theGridAtomList = theCandidateData.getGridListByAtom();

            KabschAlignment theAlignement = this.__initializeAlignment(theCandidateData);
            IAtomContainer theCopiedLigand = theConformer.clone();

            theAlignement.align();

            Matrix theRotationMatrix = new Matrix(theAlignement.getRotationMatrix());
            Vector3d theCenter = new Vector3d(theAlignement.getCenterOfMass());

            if (theAlignement.getRMSD() >= 0.0 && this.__isCorrectRotationMatrix(theRotationMatrix)) {
                List<Integer> theMatchedAtomIndexList = this.__getAtomIndexList(theLigandAtomList, theConformer);

                theAlignement.rotateAtomContainer(theCopiedLigand);
                theCopiedLigand = MoleculeManipulator.translateMolecule(theCopiedLigand, theCenter);

                theCopiedLigand.setProperty("Similarity", theCandidateData.getSimilarity());
                theCopiedLigand.setProperty("RMSD", theAlignement.getRMSD());
                theCopiedLigand.setProperty("Matched atom index", this.__getAtomPositionList(theMatchedAtomIndexList, theCopiedLigand));
                theCopiedLigand.setProperty("Matched grid position", this.__getMatchedGridList(theGridAtomList));
                return theCopiedLigand;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private String __getMatchedGridList(List<IAtom> theGridAtomList) {
        StringBuilder theStringBuilder = new StringBuilder();

        for (IAtom theAtom : theGridAtomList) {
            theStringBuilder.append(theAtom.getPoint3d()).append("||");
        }

        return theStringBuilder.toString();
    }

    private String __getAtomPositionList(List<Integer> theAtomIndexList, IAtomContainer theConformer) {
        StringBuilder theStringBuilder = new StringBuilder();

        for (Integer theAtomIndex : theAtomIndexList) {
            Point3d thePosition = theConformer.getAtom(theAtomIndex).getPoint3d();
            
            theStringBuilder.append("(");
            theStringBuilder.append(String.format("%.4f", thePosition.x)).append(", ");
            theStringBuilder.append(String.format("%.4f", thePosition.y)).append(", ");
            theStringBuilder.append(String.format("%.4f", thePosition.z)).append(") ||");
        }

        return theStringBuilder.toString();
    }

    private List<Integer> __getAtomIndexList(List<IAtom> theLigandAtomList, IAtomContainer theConformer) {
        List<Integer> theAtomIndexList = new ArrayList<>();

        for (IAtom theLigandAtom : theLigandAtomList) {
            theAtomIndexList.add(theConformer.getAtomNumber(theLigandAtom));
        }

        return theAtomIndexList;
    }

    private boolean __isCorrectRotationMatrix(Matrix theRotationMatrix) {
        double theDeterminant = theRotationMatrix.determinant();

        return String.format("%.4f", theDeterminant).equals("1.0000");
    }

    private KabschAlignment __initializeAlignment(CandidateData theCandidateData) throws CDKException {
        List<IAtom> theLigandAtomList = theCandidateData.getAtomList();
        List<IAtom> theGridAtomList = theCandidateData.getGridListByAtom();

        if (this.itsWeightedAminoAcidSerialList != null && !this.itsWeightedAminoAcidSerialList.isEmpty()) {
            return new KabschAlignment(theGridAtomList.toArray(new Atom[theGridAtomList.size()]),
                    theLigandAtomList.toArray(new Atom[theLigandAtomList.size()]),
                    this.__generateWeightArray(theCandidateData));
        } else {
            return new KabschAlignment(theGridAtomList.toArray(new Atom[theGridAtomList.size()]),
                    theLigandAtomList.toArray(new Atom[theLigandAtomList.size()]));
        }
    }

    private double[] __generateWeightArray(CandidateData theCandidateData) {
        double[] theWeightArray = new double[theCandidateData.getGridDistanceVector().getGridEnergList().size()];

        for (int ai = 0, aEnd = theCandidateData.getGridDistanceVector().getGridEnergList().size(); ai < aEnd; ai++) {
            GridEnergy theGridEnergy = theCandidateData.getGridDistanceVector().getGridEnergList().get(ai);
            String theSerial = theGridEnergy.getGrid().getProperty(BindingSite.ATOM_SERIAL_KEY, String.class);
            Integer theNumber = Integer.parseInt(theSerial.replaceAll("[^0-9]+", ""));

            if (this.itsWeightedAminoAcidSerialList.containSerial(theNumber)) {
                theWeightArray[ai] = this.itsWeightedAminoAcidSerialList.get(theNumber).getWeight();
            } else {
                theWeightArray[ai] = this.DEFAULT_WEIGHT;
            }
        }

        return theWeightArray;
    }

    private Vector3d __getCenterOfMass(List<IAtom> theAtomList) {
        double x = 0.;
        double y = 0.;
        double z = 0.;
        double totalmass = 0.;
        AtomInformation[] theAtomInformations = AtomInformation.values();

        for (IAtom theAtom : theAtomList) {
            Point3d thePosition = theAtom.getPoint3d();
            Double theWeight = theAtomInformations[theAtom.getAtomicNumber() - 1].WEIGHT;

            x += theWeight * thePosition.x;
            y += theWeight * thePosition.y;
            z += theWeight * thePosition.z;
            totalmass += theWeight;
        }

        return (new Vector3d(x / totalmass, y / totalmass, z / totalmass));
    }
}
