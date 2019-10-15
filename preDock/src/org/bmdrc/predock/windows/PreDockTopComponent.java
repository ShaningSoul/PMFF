/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.predock.windows;

import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.basicchemistry.reader.PDBReader;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.bmdrc.basicchemistry.vector.Vector3d;
import org.bmdrc.basicchemistry.windows.BasicChemistryWindowAdapter;
import org.bmdrc.basicchemistry.windows.MoleculeStructureViewerTopComponent;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.predock.calculator.DockingCalculator;
import org.bmdrc.predock.variable.BindingSite;
import org.bmdrc.predock.variable.DockingParameter;
import org.bmdrc.predock.variable.WeightAminoAcidInformationList;
import org.bmdrc.predock.windows.table.WeightedAminoAcidTable;
import org.bmdrc.sbff.energyfunction.parameter.SbffCalculationParameter;
import org.bmdrc.ui.abstracts.WindowAdapter;
import org.bmdrc.ui.components.MemorialTextField;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.bmdrc.predock.windows//PreDock//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "PreDockTopComponent",
        iconBase = "org/bmdrc/predock/resources/preDock_Icon.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "org.bmdrc.predock.windows.PreDockTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Calculate", position = 2300)
    ,
    @ActionReference(path = "Toolbars/Calculate", position = 2300)
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_PreDockAction",
        preferredID = "PreDockTopComponent"
)
@Messages({
    "CTL_PreDockAction=Docking",
    "CTL_PreDockTopComponent=Docking",
    "HINT_PreDockTopComponent=Molecular Docking"
})
public final class PreDockTopComponent extends TopComponent implements Serializable {

    private static final long serialVersionUID = 6625185920737292474L;

    private MemorialTextField itsXCoordinateInCenterOfBindingSiteTextField;
    private MemorialTextField itsYCoordinateInCenterOfBindingSiteTextField;
    private MemorialTextField itsZCoordinateInCenterOfBindingSiteTextField;
    private MemorialTextField itsBindingSiteRadiusTextField;
    private MemorialTextField itsNumberOfResultTextField;
    private MemorialTextField itsMinimumEnergyVarianceTextField;
    private MemorialTextField itsMaximumEnergyVarianceTextField;
    private MemorialTextField itsDockingCountByConformerTextField;
    private MemorialTextField itsRotationStepSizeTextField;
    private MemorialTextField itsGridIntervalTextField;
    private MemorialTextField itsGridShellRadiusTextField;
    private MemorialTextField itsMaximumGridCountUsedInMatchingTextField;
    private MemorialTextField itsDielectricConstantTextField;
    private MemorialTextField itsElectroStaticMaxCutoffDistanceTextField;
    private MemorialTextField itsElectroStaticMinCutoffDistanceTextField;
    private MemorialTextField itsNonbondingMaxCutoffDistanceTextField;
    private MemorialTextField itsNonbondingMinCutoffDistanceTextField;
    private WeightedAminoAcidTable itsWeightedAminoAcidTable;
    private WeightAminoAcidInformationList itsWeightAminoAcidSerialList;

    public PreDockTopComponent() {
        initComponents();
        setName(Bundle.CTL_PreDockTopComponent());

        this.__initializeVariable();
        this.__addListener();
    }

    private void __initializeVariable() {
        this.__initializeTable();
        this.__initializeMemorailTextField();
    }

    private void __initializeTable() {
        this.itsWeightedAminoAcidTable = new WeightedAminoAcidTable();
        this.itsWeightAminoAcidSerialList = new WeightAminoAcidInformationList();

        this.itsWeightedAminoAcidScrollPane.setViewportView(this.itsWeightedAminoAcidTable);
        this.itsWeightedAminoAcidFrame.setLocationRelativeTo(null);
    }

    private void __initializeMemorailTextField() {
        this.itsXCoordinateInCenterOfBindingSiteTextField = new MemorialTextField(Double.class);
        this.itsYCoordinateInCenterOfBindingSiteTextField = new MemorialTextField(Double.class);
        this.itsZCoordinateInCenterOfBindingSiteTextField = new MemorialTextField(Double.class);
        this.itsBindingSiteRadiusTextField = new MemorialTextField(Double.class, 0.0);
        this.itsNumberOfResultTextField = new MemorialTextField(Integer.class, 0);
        this.itsMinimumEnergyVarianceTextField = new MemorialTextField(Double.class, 0.0);
        this.itsMaximumEnergyVarianceTextField = new MemorialTextField(Double.class, 0.0);
        this.itsDockingCountByConformerTextField = new MemorialTextField(Integer.class, 0);
        this.itsRotationStepSizeTextField = new MemorialTextField(Double.class, 0.0);
        this.itsGridIntervalTextField = new MemorialTextField(Double.class, 0.0);
        this.itsGridShellRadiusTextField = new MemorialTextField(Double.class, 0.0);
        this.itsMaximumGridCountUsedInMatchingTextField = new MemorialTextField(Integer.class, 2);
        this.itsDielectricConstantTextField = new MemorialTextField(Double.class, 0.0);
        this.itsElectroStaticMinCutoffDistanceTextField = new MemorialTextField(Double.class, 0.0);
        this.itsElectroStaticMaxCutoffDistanceTextField = new MemorialTextField(Double.class, 0.0);
        this.itsNonbondingMinCutoffDistanceTextField = new MemorialTextField(Double.class, 0.0);
        this.itsNonbondingMaxCutoffDistanceTextField = new MemorialTextField(Double.class, 0.0);

        this.__initializeBindingSiteCoordinatePanel();
        this.__initializeMemorialTextField(this.itsBindingSiteRadiusPanel, this.itsBindingSiteRadiusTextField,
                "6.0");
        this.__initializeMemorialTextField(this.itsVisibleResultCountPanel, this.itsNumberOfResultTextField,
                DockingParameter.DEFAULT_VISIBLE_RESULT_COUNT.toString());
        this.__initializeMemorialTextField(this.itsMinimumEnergyVariancePanel, this.itsMinimumEnergyVarianceTextField,
                String.format("%.4f", DockingParameter.DEFAULT_MINIMUM_ENERGY_VARIANCE));
        this.__initializeMemorialTextField(this.itsMaximumEnergyVariancePanel, this.itsMaximumEnergyVarianceTextField,
                String.format("%.1f", DockingParameter.DEFAULT_MAXIMUM_ENERGY_VARIANCE));
        this.__initializeMemorialTextField(this.itsDockingCountByConformerPanel, this.itsDockingCountByConformerTextField,
                DockingParameter.DEFAULT_DOCKING_COUNT_BY_CONFORMER.toString());
        this.__initializeMemorialTextField(this.itsStepSizeInRotationPanel, this.itsRotationStepSizeTextField,
                String.format("%.1f", DockingParameter.DEFAULT_ROTATION_STEP_SIZE));
        this.__initializeMemorialTextField(this.itsGridIntervalPanel, this.itsGridIntervalTextField,
                String.format("%.1f", DockingParameter.DEFAULT_GRID_INTERVAL));
        this.__initializeMemorialTextField(this.itsGridIntervalPanel, this.itsGridShellRadiusTextField,
                String.format("%.1f", DockingParameter.DEFAULT_GRID_SHELL_RADIUS));
        this.__initializeMemorialTextField(this.itsMaximumGridCountUsedInMatchingPanel, this.itsMaximumGridCountUsedInMatchingTextField,
                DockingParameter.DEFAULT_NUMBER_OF_GRID_USED_IN_MATCHING.toString());
        this.__initializeMemorialTextField(this.itsDielectricConstantPanel, this.itsDielectricConstantTextField, "1.0");
        this.__initializeMemorialTextField(this.itsElectricMinCutOffDistancePanel, this.itsElectroStaticMinCutoffDistanceTextField, "6.0");
        this.__initializeMemorialTextField(this.itsElectroStaticMaxCutoffDistancePanel, this.itsElectroStaticMaxCutoffDistanceTextField, "12.0");
        this.__initializeMemorialTextField(this.itsNonbondingMinCutOffDistancePanel, this.itsNonbondingMinCutoffDistanceTextField, "4.0");
        this.__initializeMemorialTextField(this.itsNonbondingMaxCutoffDistancePanel, this.itsNonbondingMaxCutoffDistanceTextField, "6.0");
    }

    private void __initializeBindingSiteCoordinatePanel() {
        this.__initializeMemorialTextField(null, this.itsXCoordinateInCenterOfBindingSiteTextField, "0.0");
        this.__initializeMemorialTextField(null, this.itsYCoordinateInCenterOfBindingSiteTextField, "0.0");
        this.__initializeMemorialTextField(null, this.itsZCoordinateInCenterOfBindingSiteTextField, "0.0");

        this.itsBindingSiteCenterPositionInputPanel.add(this.itsXCoordinateInCenterOfBindingSiteTextField);
        this.itsBindingSiteCenterPositionInputPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0),
                new java.awt.Dimension(5, 32767)));
        this.itsBindingSiteCenterPositionInputPanel.add(this.itsYCoordinateInCenterOfBindingSiteTextField);
        this.itsBindingSiteCenterPositionInputPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0),
                new java.awt.Dimension(5, 32767)));
        this.itsBindingSiteCenterPositionInputPanel.add(this.itsZCoordinateInCenterOfBindingSiteTextField);
    }

    private void __initializeMemorialTextField(JPanel theParentPanel, MemorialTextField theVariable, String theInitialValue) {
        theVariable.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        theVariable.setText(theInitialValue);
        theVariable.setMaximumSize(new java.awt.Dimension(80, 21));
        theVariable.setMinimumSize(new java.awt.Dimension(80, 21));
        theVariable.setPreferredSize(new java.awt.Dimension(80, 21));

        if (theParentPanel != null) {
            theParentPanel.add(theVariable, java.awt.BorderLayout.EAST);
        }
    }

    private void __addListener() {
        this.addFocusListener(new PreDockTopcomponentFocusListener(this));
        this.itsCalculateButton.addActionListener(new PreDockCalculationButtonListener(this));
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        itsWeightedAminoAcidFrame = new javax.swing.JFrame();
        itsWeightedAminoAcidScrollPane = new javax.swing.JScrollPane();
        itsWeightedAminoAcidPanelInsideScrollPane = new javax.swing.JPanel();
        itsWeightedAminoAcidFrameButtonPanel1 = new javax.swing.JPanel();
        filler29 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        itsWeightedAminoAcidFrameButtonPanel = new javax.swing.JPanel();
        filler21 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        itsSelectedButton = new javax.swing.JButton();
        filler24 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        itsCancelButton = new javax.swing.JButton();
        filler23 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler28 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        itsWeightedAminoAcidFrameButtonPanel2 = new javax.swing.JPanel();
        filler25 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        itsSelectedButton1 = new javax.swing.JButton();
        filler26 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        itsCancelButton1 = new javax.swing.JButton();
        filler27 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler30 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        itsTotalScrollPane = new javax.swing.JScrollPane();
        itsTotalPanel = new javax.swing.JPanel();
        itsIInputFilePanel = new javax.swing.JPanel();
        itsProteinFilePanel = new javax.swing.JPanel();
        itsProteinFileLabel = new javax.swing.JLabel();
        itsProteinFileComboBox = new javax.swing.JComboBox<>();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsLigandFillePanel = new javax.swing.JPanel();
        itsLigandFileLabel = new javax.swing.JLabel();
        itsLigandFileComboBox = new javax.swing.JComboBox<>();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsParameterPanel = new javax.swing.JPanel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsDockingParameterPanel = new javax.swing.JPanel();
        itsBindingSiteParameter = new javax.swing.JPanel();
        itsBindingSiteCenterPositionPanel = new javax.swing.JPanel();
        itsBindingSiteCenterPositionLabel = new javax.swing.JLabel();
        itsBindingSiteCenterPositionInputPanel = new javax.swing.JPanel();
        filler16 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        itsBindingSiteRadiusPanel = new javax.swing.JPanel();
        itsBindingSiteRadiusLabel = new javax.swing.JLabel();
        itsVisibleResultCountPanel = new javax.swing.JPanel();
        itsVisibleResultCountLabel = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsMinimumEnergyVariancePanel = new javax.swing.JPanel();
        itsMinimumEnergyVarianceLabel = new javax.swing.JLabel();
        filler17 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsMaximumEnergyVariancePanel = new javax.swing.JPanel();
        itsMaximumEnergyVarianceLabel = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsDockingCountByConformerPanel = new javax.swing.JPanel();
        itsDockingCountByConformerLabel = new javax.swing.JLabel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsStepSizeInRotationPanel = new javax.swing.JPanel();
        itsStepSizeByRotationLabel = new javax.swing.JLabel();
        filler19 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsGridIntervalPanel = new javax.swing.JPanel();
        itsGridIntervalLabel = new javax.swing.JLabel();
        filler18 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsGridShellRadiusPanel = new javax.swing.JPanel();
        itsGridShellRadiusLabel = new javax.swing.JLabel();
        itsMaximumGridCountUsedInMatchingPanel = new javax.swing.JPanel();
        itsMaximumGridCountUsedInMatchingLabel = new javax.swing.JLabel();
        filler20 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsWeightedAminoAcidPanel = new javax.swing.JPanel();
        itsWeightedAminoAcidLabel = new javax.swing.JLabel();
        itsWeightedAminoAcidButtonPanel = new javax.swing.JPanel();
        filler22 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        itsWeightedAminoAcidButton1 = new javax.swing.JButton();
        filler12 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsEnergyCalculationParameterPanel = new javax.swing.JPanel();
        itsDielectricConstantPanel = new javax.swing.JPanel();
        itsDielectricConstantLabel = new javax.swing.JLabel();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsElectricParameter = new javax.swing.JPanel();
        itsElectricMinCutOffDistancePanel = new javax.swing.JPanel();
        itsElectroStaticMinCutoffDistanceLabel = new javax.swing.JLabel();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        itsElectroStaticMaxCutoffDistancePanel = new javax.swing.JPanel();
        itsElectroStaticMaxCutoffDistanceLabel = new javax.swing.JLabel();
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsNonbondingParameter = new javax.swing.JPanel();
        itsNonbondingMinCutOffDistancePanel = new javax.swing.JPanel();
        itsNonbondingMinCutoffDistanceLabel1 = new javax.swing.JLabel();
        filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        itsNonbondingMaxCutoffDistancePanel = new javax.swing.JPanel();
        itsNonbondingMaxCutoffDistanceLabel1 = new javax.swing.JLabel();
        filler13 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsCalculationButtonPanel = new javax.swing.JPanel();
        filler14 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        itsCalculateButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler15 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));

        itsWeightedAminoAcidFrame.setTitle(org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsWeightedAminoAcidFrame.title")); // NOI18N
        itsWeightedAminoAcidFrame.setMinimumSize(new java.awt.Dimension(350, 300));

        itsWeightedAminoAcidPanelInsideScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        itsWeightedAminoAcidPanelInsideScrollPane.setLayout(new java.awt.BorderLayout());
        itsWeightedAminoAcidScrollPane.setViewportView(itsWeightedAminoAcidPanelInsideScrollPane);

        itsWeightedAminoAcidFrame.getContentPane().add(itsWeightedAminoAcidScrollPane, java.awt.BorderLayout.CENTER);

        itsWeightedAminoAcidFrameButtonPanel1.setBackground(java.awt.Color.white);
        itsWeightedAminoAcidFrameButtonPanel1.setMaximumSize(new java.awt.Dimension(65698, 50));
        itsWeightedAminoAcidFrameButtonPanel1.setMinimumSize(new java.awt.Dimension(150, 50));
        itsWeightedAminoAcidFrameButtonPanel1.setPreferredSize(new java.awt.Dimension(150, 50));
        itsWeightedAminoAcidFrameButtonPanel1.setLayout(new javax.swing.BoxLayout(itsWeightedAminoAcidFrameButtonPanel1, javax.swing.BoxLayout.Y_AXIS));
        itsWeightedAminoAcidFrameButtonPanel1.add(filler29);

        itsWeightedAminoAcidFrameButtonPanel.setBackground(java.awt.Color.white);
        itsWeightedAminoAcidFrameButtonPanel.setMaximumSize(new java.awt.Dimension(65698, 23));
        itsWeightedAminoAcidFrameButtonPanel.setMinimumSize(new java.awt.Dimension(150, 23));
        itsWeightedAminoAcidFrameButtonPanel.setPreferredSize(new java.awt.Dimension(150, 23));
        itsWeightedAminoAcidFrameButtonPanel.setLayout(new javax.swing.BoxLayout(itsWeightedAminoAcidFrameButtonPanel, javax.swing.BoxLayout.LINE_AXIS));
        itsWeightedAminoAcidFrameButtonPanel.add(filler21);

        org.openide.awt.Mnemonics.setLocalizedText(itsSelectedButton, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsSelectedButton.text")); // NOI18N
        itsSelectedButton.setMaximumSize(new java.awt.Dimension(81, 23));
        itsSelectedButton.setMinimumSize(new java.awt.Dimension(81, 23));
        itsSelectedButton.setPreferredSize(new java.awt.Dimension(81, 23));
        itsSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itsSelectedButtonActionPerformed(evt);
            }
        });
        itsWeightedAminoAcidFrameButtonPanel.add(itsSelectedButton);
        itsWeightedAminoAcidFrameButtonPanel.add(filler24);

        org.openide.awt.Mnemonics.setLocalizedText(itsCancelButton, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsCancelButton.text")); // NOI18N
        itsCancelButton.setMaximumSize(new java.awt.Dimension(81, 23));
        itsCancelButton.setMinimumSize(new java.awt.Dimension(81, 23));
        itsCancelButton.setPreferredSize(new java.awt.Dimension(81, 23));
        itsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itsCancelButtonActionPerformed(evt);
            }
        });
        itsWeightedAminoAcidFrameButtonPanel.add(itsCancelButton);
        itsWeightedAminoAcidFrameButtonPanel.add(filler23);

        itsWeightedAminoAcidFrameButtonPanel1.add(itsWeightedAminoAcidFrameButtonPanel);
        itsWeightedAminoAcidFrameButtonPanel1.add(filler28);

        itsWeightedAminoAcidFrameButtonPanel2.setBackground(java.awt.Color.white);
        itsWeightedAminoAcidFrameButtonPanel2.setMaximumSize(new java.awt.Dimension(65698, 23));
        itsWeightedAminoAcidFrameButtonPanel2.setMinimumSize(new java.awt.Dimension(150, 23));
        itsWeightedAminoAcidFrameButtonPanel2.setPreferredSize(new java.awt.Dimension(150, 23));
        itsWeightedAminoAcidFrameButtonPanel2.setLayout(new javax.swing.BoxLayout(itsWeightedAminoAcidFrameButtonPanel2, javax.swing.BoxLayout.LINE_AXIS));
        itsWeightedAminoAcidFrameButtonPanel2.add(filler25);

        org.openide.awt.Mnemonics.setLocalizedText(itsSelectedButton1, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsSelectedButton1.text")); // NOI18N
        itsSelectedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itsSelectedButton1ActionPerformed(evt);
            }
        });
        itsWeightedAminoAcidFrameButtonPanel2.add(itsSelectedButton1);
        itsWeightedAminoAcidFrameButtonPanel2.add(filler26);

        org.openide.awt.Mnemonics.setLocalizedText(itsCancelButton1, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsCancelButton1.text")); // NOI18N
        itsCancelButton1.setMaximumSize(new java.awt.Dimension(81, 23));
        itsCancelButton1.setMinimumSize(new java.awt.Dimension(81, 23));
        itsCancelButton1.setPreferredSize(new java.awt.Dimension(81, 23));
        itsCancelButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itsCancelButton1ActionPerformed(evt);
            }
        });
        itsWeightedAminoAcidFrameButtonPanel2.add(itsCancelButton1);
        itsWeightedAminoAcidFrameButtonPanel2.add(filler27);

        itsWeightedAminoAcidFrameButtonPanel1.add(itsWeightedAminoAcidFrameButtonPanel2);
        itsWeightedAminoAcidFrameButtonPanel1.add(filler30);

        itsWeightedAminoAcidFrame.getContentPane().add(itsWeightedAminoAcidFrameButtonPanel1, java.awt.BorderLayout.SOUTH);

        setMinimumSize(new java.awt.Dimension(23, 0));
        setPreferredSize(new java.awt.Dimension(474, 500));
        setLayout(new java.awt.BorderLayout());

        itsTotalScrollPane.setMaximumSize(new java.awt.Dimension(32767, 500));
        itsTotalScrollPane.setMinimumSize(new java.awt.Dimension(23, 0));
        itsTotalScrollPane.setPreferredSize(new java.awt.Dimension(474, 700));

        itsTotalPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsTotalPanel.setMaximumSize(new java.awt.Dimension(32972, 500));
        itsTotalPanel.setMinimumSize(new java.awt.Dimension(23, 500));
        itsTotalPanel.setPreferredSize(new java.awt.Dimension(472, 700));
        itsTotalPanel.setLayout(new javax.swing.BoxLayout(itsTotalPanel, javax.swing.BoxLayout.Y_AXIS));

        itsIInputFilePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsIInputFilePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsIInputFilePanel.border.title"))); // NOI18N
        itsIInputFilePanel.setMaximumSize(new java.awt.Dimension(32767, 70));
        itsIInputFilePanel.setMinimumSize(new java.awt.Dimension(160, 70));
        itsIInputFilePanel.setPreferredSize(new java.awt.Dimension(160, 70));
        itsIInputFilePanel.setLayout(new javax.swing.BoxLayout(itsIInputFilePanel, javax.swing.BoxLayout.Y_AXIS));

        itsProteinFilePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsProteinFilePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsProteinFilePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsProteinFileLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsProteinFileLabel.text")); // NOI18N
        itsProteinFileLabel.setMaximumSize(new java.awt.Dimension(159, 15));
        itsProteinFileLabel.setMinimumSize(new java.awt.Dimension(159, 15));
        itsProteinFileLabel.setPreferredSize(new java.awt.Dimension(159, 15));
        itsProteinFilePanel.add(itsProteinFileLabel, java.awt.BorderLayout.WEST);

        itsProteinFileComboBox.setMaximumSize(new java.awt.Dimension(32767, 21));
        itsProteinFileComboBox.setMinimumSize(new java.awt.Dimension(150, 21));
        itsProteinFileComboBox.setPreferredSize(new java.awt.Dimension(150, 21));
        itsProteinFilePanel.add(itsProteinFileComboBox, java.awt.BorderLayout.EAST);

        itsIInputFilePanel.add(itsProteinFilePanel);
        itsIInputFilePanel.add(filler1);

        itsLigandFillePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsLigandFillePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsLigandFillePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsLigandFileLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsLigandFileLabel.text")); // NOI18N
        itsLigandFileLabel.setMaximumSize(new java.awt.Dimension(159, 15));
        itsLigandFileLabel.setMinimumSize(new java.awt.Dimension(159, 15));
        itsLigandFileLabel.setPreferredSize(new java.awt.Dimension(159, 15));
        itsLigandFillePanel.add(itsLigandFileLabel, java.awt.BorderLayout.WEST);

        itsLigandFileComboBox.setMaximumSize(new java.awt.Dimension(32767, 21));
        itsLigandFileComboBox.setMinimumSize(new java.awt.Dimension(150, 21));
        itsLigandFileComboBox.setPreferredSize(new java.awt.Dimension(150, 21));
        itsLigandFillePanel.add(itsLigandFileComboBox, java.awt.BorderLayout.EAST);

        itsIInputFilePanel.add(itsLigandFillePanel);

        itsTotalPanel.add(itsIInputFilePanel);
        itsTotalPanel.add(filler3);

        itsParameterPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsParameterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsParameterPanel.border.title"))); // NOI18N
        itsParameterPanel.setMaximumSize(new java.awt.Dimension(32767, 580));
        itsParameterPanel.setMinimumSize(new java.awt.Dimension(160, 580));
        itsParameterPanel.setPreferredSize(new java.awt.Dimension(160, 580));
        itsParameterPanel.setLayout(new javax.swing.BoxLayout(itsParameterPanel, javax.swing.BoxLayout.Y_AXIS));
        itsParameterPanel.add(filler4);

        itsDockingParameterPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsDockingParameterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsDockingParameterPanel.border.title"))); // NOI18N
        itsDockingParameterPanel.setMaximumSize(new java.awt.Dimension(32767, 340));
        itsDockingParameterPanel.setMinimumSize(new java.awt.Dimension(160, 340));
        itsDockingParameterPanel.setPreferredSize(new java.awt.Dimension(160, 340));
        itsDockingParameterPanel.setLayout(new javax.swing.BoxLayout(itsDockingParameterPanel, javax.swing.BoxLayout.Y_AXIS));

        itsBindingSiteParameter.setBackground(new java.awt.Color(255, 255, 255));
        itsBindingSiteParameter.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsBindingSiteParameter.border.title"))); // NOI18N
        itsBindingSiteParameter.setMinimumSize(new java.awt.Dimension(160, 78));
        itsBindingSiteParameter.setPreferredSize(new java.awt.Dimension(160, 78));
        itsBindingSiteParameter.setLayout(new javax.swing.BoxLayout(itsBindingSiteParameter, javax.swing.BoxLayout.Y_AXIS));

        itsBindingSiteCenterPositionPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsBindingSiteCenterPositionPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsBindingSiteCenterPositionPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsBindingSiteCenterPositionPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsBindingSiteCenterPositionPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsBindingSiteCenterPositionLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsBindingSiteCenterPositionLabel.text")); // NOI18N
        itsBindingSiteCenterPositionLabel.setMaximumSize(new java.awt.Dimension(151, 15));
        itsBindingSiteCenterPositionLabel.setMinimumSize(new java.awt.Dimension(151, 15));
        itsBindingSiteCenterPositionLabel.setPreferredSize(new java.awt.Dimension(151, 15));
        itsBindingSiteCenterPositionPanel.add(itsBindingSiteCenterPositionLabel, java.awt.BorderLayout.WEST);

        itsBindingSiteCenterPositionInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsBindingSiteCenterPositionInputPanel.setLayout(new javax.swing.BoxLayout(itsBindingSiteCenterPositionInputPanel, javax.swing.BoxLayout.X_AXIS));
        itsBindingSiteCenterPositionPanel.add(itsBindingSiteCenterPositionInputPanel, java.awt.BorderLayout.EAST);

        itsBindingSiteParameter.add(itsBindingSiteCenterPositionPanel);
        itsBindingSiteParameter.add(filler16);

        itsBindingSiteRadiusPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsBindingSiteRadiusPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsBindingSiteRadiusPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsBindingSiteRadiusPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsBindingSiteRadiusPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsBindingSiteRadiusLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsBindingSiteRadiusLabel.text")); // NOI18N
        itsBindingSiteRadiusPanel.add(itsBindingSiteRadiusLabel, java.awt.BorderLayout.WEST);

        itsBindingSiteParameter.add(itsBindingSiteRadiusPanel);

        itsDockingParameterPanel.add(itsBindingSiteParameter);

        itsVisibleResultCountPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsVisibleResultCountPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsVisibleResultCountPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsVisibleResultCountPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsVisibleResultCountPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsVisibleResultCountLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsVisibleResultCountLabel.text")); // NOI18N
        itsVisibleResultCountLabel.setMaximumSize(new java.awt.Dimension(159, 15));
        itsVisibleResultCountLabel.setMinimumSize(new java.awt.Dimension(159, 15));
        itsVisibleResultCountLabel.setPreferredSize(new java.awt.Dimension(159, 15));
        itsVisibleResultCountPanel.add(itsVisibleResultCountLabel, java.awt.BorderLayout.WEST);

        itsDockingParameterPanel.add(itsVisibleResultCountPanel);
        itsDockingParameterPanel.add(filler5);

        itsMinimumEnergyVariancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsMinimumEnergyVariancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsMinimumEnergyVariancePanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsMinimumEnergyVariancePanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsMinimumEnergyVariancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsMinimumEnergyVarianceLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsMinimumEnergyVarianceLabel.text")); // NOI18N
        itsMinimumEnergyVarianceLabel.setMaximumSize(new java.awt.Dimension(159, 15));
        itsMinimumEnergyVarianceLabel.setMinimumSize(new java.awt.Dimension(159, 15));
        itsMinimumEnergyVarianceLabel.setPreferredSize(new java.awt.Dimension(159, 15));
        itsMinimumEnergyVariancePanel.add(itsMinimumEnergyVarianceLabel, java.awt.BorderLayout.WEST);

        itsDockingParameterPanel.add(itsMinimumEnergyVariancePanel);
        itsDockingParameterPanel.add(filler17);

        itsMaximumEnergyVariancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsMaximumEnergyVariancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsMaximumEnergyVariancePanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsMaximumEnergyVariancePanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsMaximumEnergyVariancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsMaximumEnergyVarianceLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsMaximumEnergyVarianceLabel.text")); // NOI18N
        itsMaximumEnergyVarianceLabel.setMaximumSize(new java.awt.Dimension(159, 15));
        itsMaximumEnergyVarianceLabel.setMinimumSize(new java.awt.Dimension(159, 15));
        itsMaximumEnergyVarianceLabel.setPreferredSize(new java.awt.Dimension(159, 15));
        itsMaximumEnergyVariancePanel.add(itsMaximumEnergyVarianceLabel, java.awt.BorderLayout.WEST);

        itsDockingParameterPanel.add(itsMaximumEnergyVariancePanel);
        itsDockingParameterPanel.add(filler6);

        itsDockingCountByConformerPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsDockingCountByConformerPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsDockingCountByConformerPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsDockingCountByConformerPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsDockingCountByConformerPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsDockingCountByConformerLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsDockingCountByConformerLabel.text")); // NOI18N
        itsDockingCountByConformerPanel.add(itsDockingCountByConformerLabel, java.awt.BorderLayout.WEST);

        itsDockingParameterPanel.add(itsDockingCountByConformerPanel);
        itsDockingParameterPanel.add(filler7);

        itsStepSizeInRotationPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsStepSizeInRotationPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsStepSizeInRotationPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsStepSizeInRotationPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsStepSizeInRotationPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsStepSizeByRotationLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsStepSizeByRotationLabel.text")); // NOI18N
        itsStepSizeByRotationLabel.setMaximumSize(new java.awt.Dimension(159, 15));
        itsStepSizeByRotationLabel.setMinimumSize(new java.awt.Dimension(159, 15));
        itsStepSizeByRotationLabel.setPreferredSize(new java.awt.Dimension(159, 15));
        itsStepSizeInRotationPanel.add(itsStepSizeByRotationLabel, java.awt.BorderLayout.WEST);

        itsDockingParameterPanel.add(itsStepSizeInRotationPanel);
        itsDockingParameterPanel.add(filler19);

        itsGridIntervalPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsGridIntervalPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsGridIntervalPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsGridIntervalPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsGridIntervalPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsGridIntervalLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsGridIntervalLabel.text")); // NOI18N
        itsGridIntervalLabel.setMaximumSize(new java.awt.Dimension(240, 15));
        itsGridIntervalLabel.setMinimumSize(new java.awt.Dimension(240, 15));
        itsGridIntervalLabel.setPreferredSize(new java.awt.Dimension(240, 15));
        itsGridIntervalPanel.add(itsGridIntervalLabel, java.awt.BorderLayout.WEST);

        itsDockingParameterPanel.add(itsGridIntervalPanel);
        itsDockingParameterPanel.add(filler18);

        itsGridShellRadiusPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsGridShellRadiusPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsGridShellRadiusPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsGridShellRadiusPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsGridShellRadiusPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsGridShellRadiusLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsGridShellRadiusLabel.text")); // NOI18N
        itsGridShellRadiusLabel.setMaximumSize(new java.awt.Dimension(240, 15));
        itsGridShellRadiusLabel.setMinimumSize(new java.awt.Dimension(240, 15));
        itsGridShellRadiusLabel.setPreferredSize(new java.awt.Dimension(240, 15));
        itsGridShellRadiusPanel.add(itsGridShellRadiusLabel, java.awt.BorderLayout.WEST);

        itsDockingParameterPanel.add(itsGridShellRadiusPanel);

        itsMaximumGridCountUsedInMatchingPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsMaximumGridCountUsedInMatchingPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsMaximumGridCountUsedInMatchingPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsMaximumGridCountUsedInMatchingPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsMaximumGridCountUsedInMatchingPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsMaximumGridCountUsedInMatchingLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsMaximumGridCountUsedInMatchingLabel.text")); // NOI18N
        itsMaximumGridCountUsedInMatchingLabel.setMaximumSize(new java.awt.Dimension(240, 15));
        itsMaximumGridCountUsedInMatchingLabel.setMinimumSize(new java.awt.Dimension(240, 15));
        itsMaximumGridCountUsedInMatchingLabel.setPreferredSize(new java.awt.Dimension(240, 15));
        itsMaximumGridCountUsedInMatchingPanel.add(itsMaximumGridCountUsedInMatchingLabel, java.awt.BorderLayout.WEST);

        itsDockingParameterPanel.add(itsMaximumGridCountUsedInMatchingPanel);
        itsDockingParameterPanel.add(filler20);

        itsWeightedAminoAcidPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsWeightedAminoAcidPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsWeightedAminoAcidPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsWeightedAminoAcidPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsWeightedAminoAcidPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsWeightedAminoAcidLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsWeightedAminoAcidLabel.text")); // NOI18N
        itsWeightedAminoAcidLabel.setMaximumSize(new java.awt.Dimension(240, 15));
        itsWeightedAminoAcidLabel.setMinimumSize(new java.awt.Dimension(240, 15));
        itsWeightedAminoAcidLabel.setPreferredSize(new java.awt.Dimension(240, 15));
        itsWeightedAminoAcidPanel.add(itsWeightedAminoAcidLabel, java.awt.BorderLayout.WEST);

        itsWeightedAminoAcidButtonPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsWeightedAminoAcidButtonPanel.setMaximumSize(new java.awt.Dimension(160, 22));
        itsWeightedAminoAcidButtonPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsWeightedAminoAcidButtonPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsWeightedAminoAcidButtonPanel.setLayout(new javax.swing.BoxLayout(itsWeightedAminoAcidButtonPanel, javax.swing.BoxLayout.LINE_AXIS));
        itsWeightedAminoAcidButtonPanel.add(filler22);

        org.openide.awt.Mnemonics.setLocalizedText(itsWeightedAminoAcidButton1, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsWeightedAminoAcidButton1.text")); // NOI18N
        itsWeightedAminoAcidButton1.setMaximumSize(new java.awt.Dimension(100, 23));
        itsWeightedAminoAcidButton1.setMinimumSize(new java.awt.Dimension(100, 23));
        itsWeightedAminoAcidButton1.setPreferredSize(new java.awt.Dimension(100, 23));
        itsWeightedAminoAcidButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itsWeightedAminoAcidButton1ActionPerformed(evt);
            }
        });
        itsWeightedAminoAcidButtonPanel.add(itsWeightedAminoAcidButton1);

        itsWeightedAminoAcidPanel.add(itsWeightedAminoAcidButtonPanel, java.awt.BorderLayout.EAST);

        itsDockingParameterPanel.add(itsWeightedAminoAcidPanel);

        itsParameterPanel.add(itsDockingParameterPanel);
        itsParameterPanel.add(filler12);

        itsEnergyCalculationParameterPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsEnergyCalculationParameterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsEnergyCalculationParameterPanel.border.title"))); // NOI18N
        itsEnergyCalculationParameterPanel.setMaximumSize(new java.awt.Dimension(2147483647, 220));
        itsEnergyCalculationParameterPanel.setMinimumSize(new java.awt.Dimension(160, 100));
        itsEnergyCalculationParameterPanel.setPreferredSize(new java.awt.Dimension(160, 100));
        itsEnergyCalculationParameterPanel.setLayout(new javax.swing.BoxLayout(itsEnergyCalculationParameterPanel, javax.swing.BoxLayout.Y_AXIS));

        itsDielectricConstantPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsDielectricConstantPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsDielectricConstantPanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsDielectricConstantPanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsDielectricConstantPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsDielectricConstantLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsDielectricConstantLabel.text")); // NOI18N
        itsDielectricConstantLabel.setMaximumSize(new java.awt.Dimension(159, 15));
        itsDielectricConstantLabel.setMinimumSize(new java.awt.Dimension(159, 15));
        itsDielectricConstantLabel.setPreferredSize(new java.awt.Dimension(159, 15));
        itsDielectricConstantPanel.add(itsDielectricConstantLabel, java.awt.BorderLayout.WEST);

        itsEnergyCalculationParameterPanel.add(itsDielectricConstantPanel);
        itsEnergyCalculationParameterPanel.add(filler8);

        itsElectricParameter.setBackground(new java.awt.Color(255, 255, 255));
        itsElectricParameter.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsElectricParameter.border.title"))); // NOI18N
        itsElectricParameter.setMinimumSize(new java.awt.Dimension(160, 78));
        itsElectricParameter.setPreferredSize(new java.awt.Dimension(160, 78));
        itsElectricParameter.setLayout(new javax.swing.BoxLayout(itsElectricParameter, javax.swing.BoxLayout.Y_AXIS));

        itsElectricMinCutOffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsElectricMinCutOffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsElectricMinCutOffDistancePanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsElectricMinCutOffDistancePanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsElectricMinCutOffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsElectroStaticMinCutoffDistanceLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsElectroStaticMinCutoffDistanceLabel.text")); // NOI18N
        itsElectroStaticMinCutoffDistanceLabel.setMaximumSize(new java.awt.Dimension(151, 15));
        itsElectroStaticMinCutoffDistanceLabel.setMinimumSize(new java.awt.Dimension(151, 15));
        itsElectroStaticMinCutoffDistanceLabel.setPreferredSize(new java.awt.Dimension(151, 15));
        itsElectricMinCutOffDistancePanel.add(itsElectroStaticMinCutoffDistanceLabel, java.awt.BorderLayout.WEST);

        itsElectricParameter.add(itsElectricMinCutOffDistancePanel);
        itsElectricParameter.add(filler9);

        itsElectroStaticMaxCutoffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsElectroStaticMaxCutoffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsElectroStaticMaxCutoffDistancePanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsElectroStaticMaxCutoffDistancePanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsElectroStaticMaxCutoffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsElectroStaticMaxCutoffDistanceLabel, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsElectroStaticMaxCutoffDistanceLabel.text")); // NOI18N
        itsElectroStaticMaxCutoffDistancePanel.add(itsElectroStaticMaxCutoffDistanceLabel, java.awt.BorderLayout.WEST);

        itsElectricParameter.add(itsElectroStaticMaxCutoffDistancePanel);

        itsEnergyCalculationParameterPanel.add(itsElectricParameter);
        itsEnergyCalculationParameterPanel.add(filler10);

        itsNonbondingParameter.setBackground(new java.awt.Color(255, 255, 255));
        itsNonbondingParameter.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsNonbondingParameter.border.title"))); // NOI18N
        itsNonbondingParameter.setMinimumSize(new java.awt.Dimension(160, 78));
        itsNonbondingParameter.setPreferredSize(new java.awt.Dimension(160, 78));
        itsNonbondingParameter.setLayout(new javax.swing.BoxLayout(itsNonbondingParameter, javax.swing.BoxLayout.Y_AXIS));

        itsNonbondingMinCutOffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsNonbondingMinCutOffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsNonbondingMinCutOffDistancePanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsNonbondingMinCutOffDistancePanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsNonbondingMinCutOffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsNonbondingMinCutoffDistanceLabel1, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsNonbondingMinCutoffDistanceLabel1.text")); // NOI18N
        itsNonbondingMinCutoffDistanceLabel1.setMaximumSize(new java.awt.Dimension(151, 15));
        itsNonbondingMinCutoffDistanceLabel1.setMinimumSize(new java.awt.Dimension(151, 15));
        itsNonbondingMinCutoffDistanceLabel1.setPreferredSize(new java.awt.Dimension(151, 15));
        itsNonbondingMinCutOffDistancePanel.add(itsNonbondingMinCutoffDistanceLabel1, java.awt.BorderLayout.WEST);

        itsNonbondingParameter.add(itsNonbondingMinCutOffDistancePanel);
        itsNonbondingParameter.add(filler11);

        itsNonbondingMaxCutoffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsNonbondingMaxCutoffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsNonbondingMaxCutoffDistancePanel.setMinimumSize(new java.awt.Dimension(160, 22));
        itsNonbondingMaxCutoffDistancePanel.setPreferredSize(new java.awt.Dimension(160, 22));
        itsNonbondingMaxCutoffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsNonbondingMaxCutoffDistanceLabel1, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsNonbondingMaxCutoffDistanceLabel1.text")); // NOI18N
        itsNonbondingMaxCutoffDistancePanel.add(itsNonbondingMaxCutoffDistanceLabel1, java.awt.BorderLayout.WEST);

        itsNonbondingParameter.add(itsNonbondingMaxCutoffDistancePanel);

        itsEnergyCalculationParameterPanel.add(itsNonbondingParameter);

        itsParameterPanel.add(itsEnergyCalculationParameterPanel);

        itsTotalPanel.add(itsParameterPanel);
        itsTotalPanel.add(filler13);

        itsCalculationButtonPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsCalculationButtonPanel.setMinimumSize(new java.awt.Dimension(160, 23));
        itsCalculationButtonPanel.setPreferredSize(new java.awt.Dimension(160, 23));
        itsCalculationButtonPanel.setLayout(new javax.swing.BoxLayout(itsCalculationButtonPanel, javax.swing.BoxLayout.X_AXIS));
        itsCalculationButtonPanel.add(filler14);

        org.openide.awt.Mnemonics.setLocalizedText(itsCalculateButton, org.openide.util.NbBundle.getMessage(PreDockTopComponent.class, "PreDockTopComponent.itsCalculateButton.text")); // NOI18N
        itsCalculationButtonPanel.add(itsCalculateButton);
        itsCalculationButtonPanel.add(filler2);

        itsTotalPanel.add(itsCalculationButtonPanel);
        itsTotalPanel.add(filler15);

        itsTotalScrollPane.setViewportView(itsTotalPanel);

        add(itsTotalScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void itsWeightedAminoAcidButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itsWeightedAminoAcidButton1ActionPerformed
        this.itsWeightedAminoAcidFrame.setVisible(true);
    }//GEN-LAST:event_itsWeightedAminoAcidButton1ActionPerformed

    private void itsSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itsSelectedButtonActionPerformed
        this.itsWeightedAminoAcidTable.addRow();
    }//GEN-LAST:event_itsSelectedButtonActionPerformed

    private void itsCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itsCancelButtonActionPerformed
        int[] theRowIndexs = this.itsWeightedAminoAcidTable.getSelectedRows();

        for (int ii = theRowIndexs.length - 1; ii >= 0; ii--) {
            this.itsWeightedAminoAcidTable.removeRow(theRowIndexs[ii]);
        }
    }//GEN-LAST:event_itsCancelButtonActionPerformed

    private void itsCancelButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itsCancelButton1ActionPerformed
        this.itsWeightedAminoAcidFrame.dispose();
    }//GEN-LAST:event_itsCancelButton1ActionPerformed

    private void itsSelectedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itsSelectedButton1ActionPerformed
        this.itsWeightAminoAcidSerialList = this.itsWeightedAminoAcidTable.getSerialNumberList();
        this.itsWeightedAminoAcidFrame.dispose();
    }//GEN-LAST:event_itsSelectedButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler12;
    private javax.swing.Box.Filler filler13;
    private javax.swing.Box.Filler filler14;
    private javax.swing.Box.Filler filler15;
    private javax.swing.Box.Filler filler16;
    private javax.swing.Box.Filler filler17;
    private javax.swing.Box.Filler filler18;
    private javax.swing.Box.Filler filler19;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler20;
    private javax.swing.Box.Filler filler21;
    private javax.swing.Box.Filler filler22;
    private javax.swing.Box.Filler filler23;
    private javax.swing.Box.Filler filler24;
    private javax.swing.Box.Filler filler25;
    private javax.swing.Box.Filler filler26;
    private javax.swing.Box.Filler filler27;
    private javax.swing.Box.Filler filler28;
    private javax.swing.Box.Filler filler29;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler30;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JPanel itsBindingSiteCenterPositionInputPanel;
    private javax.swing.JLabel itsBindingSiteCenterPositionLabel;
    private javax.swing.JPanel itsBindingSiteCenterPositionPanel;
    private javax.swing.JPanel itsBindingSiteParameter;
    private javax.swing.JLabel itsBindingSiteRadiusLabel;
    private javax.swing.JPanel itsBindingSiteRadiusPanel;
    private javax.swing.JButton itsCalculateButton;
    private javax.swing.JPanel itsCalculationButtonPanel;
    private javax.swing.JButton itsCancelButton;
    private javax.swing.JButton itsCancelButton1;
    private javax.swing.JLabel itsDielectricConstantLabel;
    private javax.swing.JPanel itsDielectricConstantPanel;
    private javax.swing.JLabel itsDockingCountByConformerLabel;
    private javax.swing.JPanel itsDockingCountByConformerPanel;
    private javax.swing.JPanel itsDockingParameterPanel;
    private javax.swing.JPanel itsElectricMinCutOffDistancePanel;
    private javax.swing.JPanel itsElectricParameter;
    private javax.swing.JLabel itsElectroStaticMaxCutoffDistanceLabel;
    private javax.swing.JPanel itsElectroStaticMaxCutoffDistancePanel;
    private javax.swing.JLabel itsElectroStaticMinCutoffDistanceLabel;
    private javax.swing.JPanel itsEnergyCalculationParameterPanel;
    private javax.swing.JLabel itsGridIntervalLabel;
    private javax.swing.JPanel itsGridIntervalPanel;
    private javax.swing.JLabel itsGridShellRadiusLabel;
    private javax.swing.JPanel itsGridShellRadiusPanel;
    private javax.swing.JPanel itsIInputFilePanel;
    private javax.swing.JComboBox<String> itsLigandFileComboBox;
    private javax.swing.JLabel itsLigandFileLabel;
    private javax.swing.JPanel itsLigandFillePanel;
    private javax.swing.JLabel itsMaximumEnergyVarianceLabel;
    private javax.swing.JPanel itsMaximumEnergyVariancePanel;
    private javax.swing.JLabel itsMaximumGridCountUsedInMatchingLabel;
    private javax.swing.JPanel itsMaximumGridCountUsedInMatchingPanel;
    private javax.swing.JLabel itsMinimumEnergyVarianceLabel;
    private javax.swing.JPanel itsMinimumEnergyVariancePanel;
    private javax.swing.JLabel itsNonbondingMaxCutoffDistanceLabel1;
    private javax.swing.JPanel itsNonbondingMaxCutoffDistancePanel;
    private javax.swing.JPanel itsNonbondingMinCutOffDistancePanel;
    private javax.swing.JLabel itsNonbondingMinCutoffDistanceLabel1;
    private javax.swing.JPanel itsNonbondingParameter;
    private javax.swing.JPanel itsParameterPanel;
    private javax.swing.JComboBox<String> itsProteinFileComboBox;
    private javax.swing.JLabel itsProteinFileLabel;
    private javax.swing.JPanel itsProteinFilePanel;
    private javax.swing.JButton itsSelectedButton;
    private javax.swing.JButton itsSelectedButton1;
    private javax.swing.JLabel itsStepSizeByRotationLabel;
    private javax.swing.JPanel itsStepSizeInRotationPanel;
    private javax.swing.JPanel itsTotalPanel;
    private javax.swing.JScrollPane itsTotalScrollPane;
    private javax.swing.JLabel itsVisibleResultCountLabel;
    private javax.swing.JPanel itsVisibleResultCountPanel;
    private javax.swing.JButton itsWeightedAminoAcidButton1;
    private javax.swing.JPanel itsWeightedAminoAcidButtonPanel;
    private javax.swing.JFrame itsWeightedAminoAcidFrame;
    private javax.swing.JPanel itsWeightedAminoAcidFrameButtonPanel;
    private javax.swing.JPanel itsWeightedAminoAcidFrameButtonPanel1;
    private javax.swing.JPanel itsWeightedAminoAcidFrameButtonPanel2;
    private javax.swing.JLabel itsWeightedAminoAcidLabel;
    private javax.swing.JPanel itsWeightedAminoAcidPanel;
    private javax.swing.JPanel itsWeightedAminoAcidPanelInsideScrollPane;
    private javax.swing.JScrollPane itsWeightedAminoAcidScrollPane;
    // End of variables declaration//GEN-END:variables
    public JComboBox<String> getLigandFileComboBox() {
        return itsLigandFileComboBox;
    }

    public JComboBox<String> getProteinFileComboBox() {
        return itsProteinFileComboBox;
    }

    public File getSelectedProteinFile() {
        MoleculeTableTopComponent theTopComponent = BasicChemistryWindowAdapter.getMoleculeTableTopComponent(this.itsProteinFileComboBox.getSelectedItem().toString());

        return theTopComponent.getMoleculeFile();
    }

    public File getSelectedLigandFile() {
        MoleculeTableTopComponent theTopComponent = BasicChemistryWindowAdapter.getMoleculeTableTopComponent(this.itsLigandFileComboBox.getSelectedItem().toString());

        return theTopComponent.getMoleculeFile();
    }

    public SbffCalculationParameter getEnergyCalculationParameter() {
        return new SbffCalculationParameter(Double.parseDouble(this.itsElectroStaticMinCutoffDistanceTextField.getText()),
                Double.parseDouble(this.itsElectroStaticMaxCutoffDistanceTextField.getText()),
                Double.parseDouble(this.itsNonbondingMinCutoffDistanceTextField.getText()),
                Double.parseDouble(this.itsNonbondingMaxCutoffDistanceTextField.getText()),
                Double.parseDouble(this.itsDielectricConstantTextField.getText()));
    }

    public BindingSite getBindingSite() {
        Vector3d theCenterPosition = new Vector3d(Double.parseDouble(this.itsXCoordinateInCenterOfBindingSiteTextField.getText()),
                Double.parseDouble(this.itsYCoordinateInCenterOfBindingSiteTextField.getText()),
                Double.parseDouble(this.itsZCoordinateInCenterOfBindingSiteTextField.getText()));

        return new BindingSite(theCenterPosition, Double.parseDouble(this.itsBindingSiteRadiusTextField.getText()));
    }

    public DockingParameter getDockingParameter() {
        return new DockingParameter(Double.parseDouble(this.itsMinimumEnergyVarianceTextField.getText()),
                Double.parseDouble(this.itsMaximumEnergyVarianceTextField.getText()),
                Double.parseDouble(this.itsRotationStepSizeTextField.getText()),
                Double.parseDouble(this.itsGridIntervalTextField.getText()),
                Double.parseDouble(this.itsGridShellRadiusTextField.getText()),
                Integer.parseInt(this.itsMaximumGridCountUsedInMatchingTextField.getText()),
                Integer.parseInt(this.itsDockingCountByConformerTextField.getText()),
                Integer.parseInt(this.itsNumberOfResultTextField.getText()));
    }

    public WeightAminoAcidInformationList getWeightAminoAcidSerialList() {
        return this.itsWeightAminoAcidSerialList;
    }

    @Override
    protected void componentActivated() {
        TopComponent[] theTopComponents = WindowAdapter.getOutputMode().getTopComponents();

        this.itsProteinFileComboBox.removeAllItems();
        this.itsLigandFileComboBox.removeAllItems();

        for (TopComponent theTopComponent : theTopComponents) {
            if (theTopComponent instanceof MoleculeTableTopComponent && theTopComponent.isOpened()) {
                MoleculeTableTopComponent theMoleculeTableTopComponent = (MoleculeTableTopComponent)theTopComponent;
                String theFileName = theMoleculeTableTopComponent.getTable().getMoleculeFile().getName();
                String theSuffix = theFileName.substring(theFileName.lastIndexOf("."));

                if (theSuffix.equals(".pdb")) {
                    this.itsProteinFileComboBox.addItem(theTopComponent.getName());
                } else if (theSuffix.equals(".sd") || theSuffix.equals(".sdf")) {
                    this.itsLigandFileComboBox.addItem(theTopComponent.getName());
                }
            }
        }

        super.componentActivated();
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
//        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
//        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}

class PreDockTopcomponentFocusListener extends FocusAdapter implements Serializable {

    private static final long serialVersionUID = -2257492420601114624L;

    private PreDockTopComponent itsTopComponent;

    public PreDockTopcomponentFocusListener(PreDockTopComponent itsTopComponent) {
        this.itsTopComponent = itsTopComponent;
    }

    @Override
    public void focusGained(FocusEvent fe) {
        TopComponent[] theTopComponents = WindowAdapter.getOutputMode().getTopComponents();

        this.itsTopComponent.getProteinFileComboBox().removeAllItems();
        this.itsTopComponent.getLigandFileComboBox().removeAllItems();

        for (TopComponent theTopComponent : theTopComponents) {
            if (theTopComponent instanceof MoleculeTableTopComponent && theTopComponent.isOpened()) {
                String theSuffix = theTopComponent.getName().substring(theTopComponent.getName().lastIndexOf("."));

                if (theSuffix.equals(".pdb")) {
                    this.itsTopComponent.getProteinFileComboBox().addItem(theTopComponent.getName());
                } else if (theSuffix.equals(".sd") || theSuffix.equals(".sdf")) {
                    this.itsTopComponent.getLigandFileComboBox().addItem(theTopComponent.getName());
                }
            }
        }

        this.itsTopComponent.revalidate();
    }
}

class PreDockCalculationButtonListener extends AbstractAction implements Serializable {

    private static final long serialVersionUID = 8064587347515307849L;

    private PreDockTopComponent itsTopComponent;
    private DockingCalculator itsCalculator;
    private Thread itsThread;

    public PreDockCalculationButtonListener(PreDockTopComponent itsTopComponent) {
        this.itsTopComponent = itsTopComponent;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Protein theProtein = this.__getProtein();
        IAtomContainer theLigand = SDFReader.openMoleculeFile(this.itsTopComponent.getSelectedLigandFile()).getAtomContainer(0);
        DockingParameter theDockingParameter = this.itsTopComponent.getDockingParameter();
        BindingSite theBindingSite = this.itsTopComponent.getBindingSite();
        String theTopComponentName = "Docking " + this.itsTopComponent.getSelectedProteinFile().getName() + " and "
                + this.itsTopComponent.getSelectedLigandFile().getName();
        MoleculeTableTopComponent theTableTopComponent = new MoleculeTableTopComponent(theTopComponentName);
        MoleculeStructureViewerTopComponent theViewerTopComponent = new MoleculeStructureViewerTopComponent(theTableTopComponent);

        theDockingParameter.getCalculationParameter().generateNotCalculableAtomIndexList(theProtein, theLigand);
        theBindingSite.generateAminoAcidsInBindingSite(theProtein);

        this.__setWindows(theViewerTopComponent, theTableTopComponent);

        this.itsCalculator = new DockingCalculator(theProtein, theLigand, theBindingSite, theDockingParameter,
                this.itsTopComponent.getWeightAminoAcidSerialList());
        this.itsCalculator.setTableTopComponent(theTableTopComponent);
        this.itsCalculator.setViewerTopComponent(theViewerTopComponent);
        this.itsCalculator.actionPerformed(ae);
    }

    private Protein __getProtein() {
        try {
            File theProteinFile = this.itsTopComponent.getSelectedProteinFile();
            PDBReader theReader = new PDBReader();

            return theReader.readPDBFile(this.itsTopComponent.getSelectedProteinFile());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return null;
    }

    private void __setWindows(MoleculeStructureViewerTopComponent theViewerTopComponent, MoleculeTableTopComponent theTableTopComponent) {
        WindowAdapter.getOutputMode().dockInto(theTableTopComponent);
        WindowAdapter.getEditorMode().dockInto(theViewerTopComponent);

        theTableTopComponent.setViewerTopComponent(theViewerTopComponent);
        theViewerTopComponent.setTableComponent(theTableTopComponent);

        theTableTopComponent.open();
        theViewerTopComponent.open();

        theTableTopComponent.requestActive();
        theViewerTopComponent.requestActive();
    }
}
