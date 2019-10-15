/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.sbff.windows;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Serializable;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.bmdrc.basicchemistry.minimizer.ConjugatedGradientMinimizerInMolecularStructure;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.bmdrc.basicchemistry.windows.BasicChemistryWindowAdapter;
import org.bmdrc.basicchemistry.windows.MoleculeStructureViewerTopComponent;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.basicchemistry.writer.SDFWriter;
import org.bmdrc.sbff.energyfunction.SbffEnergyFunction;
import org.bmdrc.sbff.energyfunction.parameter.SbffCalculationParameter;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.abstracts.WindowAdapter;
import org.bmdrc.ui.components.MemorialTextField;
import org.bmdrc.ui.listener.DoubleValueCheckerListener;
import org.bmdrc.ui.listener.IntegerValueCheckerListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Cancellable;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.bmdrc.sbff.windows//MinimizationWindow//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "MinimizationWindowTopComponent",
        iconBase = "org/bmdrc/sbff/resource/MinimizationButton.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "org.bmdrc.sbff.windows.MinimizationWindowTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Calculate", position = 2100)
    ,
    @ActionReference(path = "Toolbars/Calculate", position = 2100)
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_MinimizationWindowAction",
        preferredID = "MinimizationWindowTopComponent"
)
@Messages({
    "CTL_MinimizationWindowAction=Minimization",
    "CTL_MinimizationWindowTopComponent=Minimization",
    "HINT_MinimizationWindowTopComponent=Molecule minimization"
})
public final class MinimizationWindowTopComponent extends TopComponent implements Serializable {

    private static final long serialVersionUID = 5061294738438612046L;

    private MemorialTextField itsDielectricConstantTextField;
    private MemorialTextField itsElectroStaticMaxCutoffDistanceTextField;
    private MemorialTextField itsElectroStaticMinCutoffDistanceTextField;
    private MemorialTextField itsNonbondingMaxCutoffDistanceTextField;
    private MemorialTextField itsNonbondingMinCutoffDistanceTextField;
    private MemorialTextField itsMaxStepTextField;
    private MemorialTextField itsConvergedRmsdTextField;

    public MinimizationWindowTopComponent() {
        initComponents();
        setName(Bundle.CTL_MinimizationWindowTopComponent());
        setToolTipText(Bundle.HINT_MinimizationWindowTopComponent());

        this.__initializeVariable();
        this.__addListener();
    }

    private void __initializeVariable() {
        this.itsDielectricConstantTextField = new MemorialTextField();
        this.itsElectroStaticMinCutoffDistanceTextField = new MemorialTextField();
        this.itsElectroStaticMaxCutoffDistanceTextField = new MemorialTextField();
        this.itsNonbondingMinCutoffDistanceTextField = new MemorialTextField();
        this.itsNonbondingMaxCutoffDistanceTextField = new MemorialTextField();
        this.itsMaxStepTextField = new MemorialTextField();
        this.itsConvergedRmsdTextField = new MemorialTextField();

        this.__initializeMemorialTextField(this.itsDielectricConstantPanel, this.itsDielectricConstantTextField, "1.0");
        this.__initializeMemorialTextField(this.itsElectricMinCutOffDistancePanel, this.itsElectroStaticMinCutoffDistanceTextField, "6.0");
        this.__initializeMemorialTextField(this.itsElectroStaticMaxCutoffDistancePanel, this.itsElectroStaticMaxCutoffDistanceTextField, "12.0");
        this.__initializeMemorialTextField(this.itsNonbondingMinCutOffDistancePanel, this.itsNonbondingMinCutoffDistanceTextField, "4.0");
        this.__initializeMemorialTextField(this.itsNonbondingMaxCutoffDistancePanel, this.itsNonbondingMaxCutoffDistanceTextField, "6.0");
        this.__initializeMemorialTextField(this.itsMaxStepPanel, this.itsMaxStepTextField, "100");
        this.__initializeMemorialTextField(this.itsConvergedRmsdPanel, this.itsConvergedRmsdTextField, "0.0001");
    }

    private void __initializeMemorialTextField(JPanel theParentPanel, MemorialTextField theVariable, String theInitialValue) {
        theVariable.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        theVariable.setText(theInitialValue);
        theVariable.setMaximumSize(new java.awt.Dimension(80, 21));
        theVariable.setMinimumSize(new java.awt.Dimension(80, 21));
        theVariable.setPreferredSize(new java.awt.Dimension(80, 21));
        theParentPanel.add(theVariable, java.awt.BorderLayout.EAST);
    }

    private void __addListener() {
        this.itsCalculateButton.addActionListener(new MinimizationCalculationButtonListener(this));

        this.itsDielectricConstantTextField.addFocusListener(new DoubleValueCheckerListener(this.itsDielectricConstantTextField));
        this.itsElectroStaticMinCutoffDistanceTextField.addFocusListener(new DoubleValueCheckerListener(this.itsElectroStaticMinCutoffDistanceTextField));
        this.itsElectroStaticMaxCutoffDistanceTextField.addFocusListener(new DoubleValueCheckerListener(this.itsElectroStaticMaxCutoffDistanceTextField));
        this.itsNonbondingMinCutoffDistanceTextField.addFocusListener(new DoubleValueCheckerListener((this.itsNonbondingMinCutoffDistanceTextField)));
        this.itsNonbondingMaxCutoffDistanceTextField.addFocusListener(new DoubleValueCheckerListener((this.itsNonbondingMaxCutoffDistanceTextField)));
        this.itsMaxStepTextField.addFocusListener(new IntegerValueCheckerListener((this.itsMaxStepTextField)));
        this.itsConvergedRmsdTextField.addFocusListener(new DoubleValueCheckerListener(this.itsConvergedRmsdTextField));
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        itsTotalScrollPane = new javax.swing.JScrollPane();
        itsTotalPanel = new javax.swing.JPanel();
        itsInputPanel = new javax.swing.JPanel();
        itsInputFileLabel = new javax.swing.JLabel();
        itsInputFileComboBox = new javax.swing.JComboBox<>();
        itsParameterPanel = new javax.swing.JPanel();
        itsDielectricConstantPanel = new javax.swing.JPanel();
        itsDielectricConstantLabel = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsElectricParameter = new javax.swing.JPanel();
        itsElectricMinCutOffDistancePanel = new javax.swing.JPanel();
        itsElectroStaticMinCutoffDistanceLabel = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        itsElectroStaticMaxCutoffDistancePanel = new javax.swing.JPanel();
        itsElectroStaticMaxCutoffDistanceLabel = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsNonbondingParameter = new javax.swing.JPanel();
        itsNonbondingMinCutOffDistancePanel = new javax.swing.JPanel();
        itsNonbondingMinCutoffDistanceLabel = new javax.swing.JLabel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        itsNonbondingMaxCutoffDistancePanel = new javax.swing.JPanel();
        itsNonbondingMaxCutoffDistanceLabel = new javax.swing.JLabel();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsMinimizationParameter = new javax.swing.JPanel();
        itsMaxStepPanel = new javax.swing.JPanel();
        itsMaxStepLabel = new javax.swing.JLabel();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        itsConvergedRmsdPanel = new javax.swing.JPanel();
        itsConvergedRmsdLabel = new javax.swing.JLabel();
        itsCalculateButtonPanel = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        itsCalculateButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        setLayout(new java.awt.BorderLayout());

        itsTotalScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        itsTotalScrollPane.setBorder(null);

        itsTotalPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsTotalPanel.setLayout(new javax.swing.BoxLayout(itsTotalPanel, javax.swing.BoxLayout.Y_AXIS));

        itsInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsInputPanel.border.title"))); // NOI18N
        itsInputPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        itsInputPanel.setMinimumSize(new java.awt.Dimension(160, 50));
        itsInputPanel.setPreferredSize(new java.awt.Dimension(160, 50));
        itsInputPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsInputFileLabel, org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsInputFileLabel.text")); // NOI18N
        itsInputPanel.add(itsInputFileLabel, java.awt.BorderLayout.WEST);

        itsInputFileComboBox.setMaximumSize(new java.awt.Dimension(32767, 21));
        itsInputFileComboBox.setMinimumSize(new java.awt.Dimension(100, 21));
        itsInputFileComboBox.setPreferredSize(new java.awt.Dimension(100, 21));
        itsInputPanel.add(itsInputFileComboBox, java.awt.BorderLayout.EAST);

        itsTotalPanel.add(itsInputPanel);

        itsParameterPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsParameterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsParameterPanel.border.title"))); // NOI18N
        itsParameterPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        itsParameterPanel.setMinimumSize(new java.awt.Dimension(160, 300));
        itsParameterPanel.setPreferredSize(new java.awt.Dimension(160, 300));
        itsParameterPanel.setLayout(new javax.swing.BoxLayout(itsParameterPanel, javax.swing.BoxLayout.Y_AXIS));

        itsDielectricConstantPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsDielectricConstantPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsDielectricConstantPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsDielectricConstantLabel, org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsDielectricConstantLabel.text")); // NOI18N
        itsDielectricConstantPanel.add(itsDielectricConstantLabel, java.awt.BorderLayout.WEST);

        itsParameterPanel.add(itsDielectricConstantPanel);
        itsParameterPanel.add(filler3);

        itsElectricParameter.setBackground(new java.awt.Color(255, 255, 255));
        itsElectricParameter.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsElectricParameter.border.title"))); // NOI18N
        itsElectricParameter.setMinimumSize(new java.awt.Dimension(160, 50));
        itsElectricParameter.setPreferredSize(new java.awt.Dimension(160, 50));
        itsElectricParameter.setLayout(new javax.swing.BoxLayout(itsElectricParameter, javax.swing.BoxLayout.Y_AXIS));

        itsElectricMinCutOffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsElectricMinCutOffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsElectricMinCutOffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsElectroStaticMinCutoffDistanceLabel, org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsElectroStaticMinCutoffDistanceLabel.text")); // NOI18N
        itsElectricMinCutOffDistancePanel.add(itsElectroStaticMinCutoffDistanceLabel, java.awt.BorderLayout.WEST);

        itsElectricParameter.add(itsElectricMinCutOffDistancePanel);
        itsElectricParameter.add(filler6);

        itsElectroStaticMaxCutoffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsElectroStaticMaxCutoffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsElectroStaticMaxCutoffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsElectroStaticMaxCutoffDistanceLabel, org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsElectroStaticMaxCutoffDistanceLabel.text")); // NOI18N
        itsElectroStaticMaxCutoffDistancePanel.add(itsElectroStaticMaxCutoffDistanceLabel, java.awt.BorderLayout.WEST);

        itsElectricParameter.add(itsElectroStaticMaxCutoffDistancePanel);

        itsParameterPanel.add(itsElectricParameter);
        itsParameterPanel.add(filler5);

        itsNonbondingParameter.setBackground(new java.awt.Color(255, 255, 255));
        itsNonbondingParameter.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsNonbondingParameter.border.title"))); // NOI18N
        itsNonbondingParameter.setMinimumSize(new java.awt.Dimension(160, 50));
        itsNonbondingParameter.setPreferredSize(new java.awt.Dimension(160, 50));
        itsNonbondingParameter.setLayout(new javax.swing.BoxLayout(itsNonbondingParameter, javax.swing.BoxLayout.Y_AXIS));

        itsNonbondingMinCutOffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsNonbondingMinCutOffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsNonbondingMinCutOffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsNonbondingMinCutoffDistanceLabel, org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsNonbondingMinCutoffDistanceLabel.text")); // NOI18N
        itsNonbondingMinCutOffDistancePanel.add(itsNonbondingMinCutoffDistanceLabel, java.awt.BorderLayout.WEST);

        itsNonbondingParameter.add(itsNonbondingMinCutOffDistancePanel);
        itsNonbondingParameter.add(filler7);

        itsNonbondingMaxCutoffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsNonbondingMaxCutoffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsNonbondingMaxCutoffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsNonbondingMaxCutoffDistanceLabel, org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsNonbondingMaxCutoffDistanceLabel.text")); // NOI18N
        itsNonbondingMaxCutoffDistancePanel.add(itsNonbondingMaxCutoffDistanceLabel, java.awt.BorderLayout.WEST);

        itsNonbondingParameter.add(itsNonbondingMaxCutoffDistancePanel);

        itsParameterPanel.add(itsNonbondingParameter);
        itsParameterPanel.add(filler8);

        itsMinimizationParameter.setBackground(new java.awt.Color(255, 255, 255));
        itsMinimizationParameter.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsMinimizationParameter.border.title"))); // NOI18N
        itsMinimizationParameter.setMinimumSize(new java.awt.Dimension(160, 50));
        itsMinimizationParameter.setPreferredSize(new java.awt.Dimension(160, 50));
        itsMinimizationParameter.setLayout(new javax.swing.BoxLayout(itsMinimizationParameter, javax.swing.BoxLayout.Y_AXIS));

        itsMaxStepPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsMaxStepPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsMaxStepPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsMaxStepLabel, org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsMaxStepLabel.text")); // NOI18N
        itsMaxStepPanel.add(itsMaxStepLabel, java.awt.BorderLayout.WEST);

        itsMinimizationParameter.add(itsMaxStepPanel);
        itsMinimizationParameter.add(filler9);

        itsConvergedRmsdPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsConvergedRmsdPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsConvergedRmsdPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsConvergedRmsdLabel, org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsConvergedRmsdLabel.text")); // NOI18N
        itsConvergedRmsdPanel.add(itsConvergedRmsdLabel, java.awt.BorderLayout.WEST);

        itsMinimizationParameter.add(itsConvergedRmsdPanel);

        itsParameterPanel.add(itsMinimizationParameter);

        itsTotalPanel.add(itsParameterPanel);

        itsCalculateButtonPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsCalculateButtonPanel.setMaximumSize(new java.awt.Dimension(32767, 30));
        itsCalculateButtonPanel.setMinimumSize(new java.awt.Dimension(100, 30));
        itsCalculateButtonPanel.setPreferredSize(new java.awt.Dimension(470, 50));
        itsCalculateButtonPanel.setLayout(new javax.swing.BoxLayout(itsCalculateButtonPanel, javax.swing.BoxLayout.X_AXIS));
        itsCalculateButtonPanel.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(itsCalculateButton, org.openide.util.NbBundle.getMessage(MinimizationWindowTopComponent.class, "MinimizationWindowTopComponent.itsCalculateButton.text")); // NOI18N
        itsCalculateButtonPanel.add(itsCalculateButton);
        itsCalculateButtonPanel.add(filler2);

        itsTotalPanel.add(itsCalculateButtonPanel);

        itsTotalScrollPane.setViewportView(itsTotalPanel);

        add(itsTotalScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JButton itsCalculateButton;
    private javax.swing.JPanel itsCalculateButtonPanel;
    private javax.swing.JLabel itsConvergedRmsdLabel;
    private javax.swing.JPanel itsConvergedRmsdPanel;
    private javax.swing.JLabel itsDielectricConstantLabel;
    private javax.swing.JPanel itsDielectricConstantPanel;
    private javax.swing.JPanel itsElectricMinCutOffDistancePanel;
    private javax.swing.JPanel itsElectricParameter;
    private javax.swing.JLabel itsElectroStaticMaxCutoffDistanceLabel;
    private javax.swing.JPanel itsElectroStaticMaxCutoffDistancePanel;
    private javax.swing.JLabel itsElectroStaticMinCutoffDistanceLabel;
    private javax.swing.JComboBox<String> itsInputFileComboBox;
    private javax.swing.JLabel itsInputFileLabel;
    private javax.swing.JPanel itsInputPanel;
    private javax.swing.JLabel itsMaxStepLabel;
    private javax.swing.JPanel itsMaxStepPanel;
    private javax.swing.JPanel itsMinimizationParameter;
    private javax.swing.JLabel itsNonbondingMaxCutoffDistanceLabel;
    private javax.swing.JPanel itsNonbondingMaxCutoffDistancePanel;
    private javax.swing.JPanel itsNonbondingMinCutOffDistancePanel;
    private javax.swing.JLabel itsNonbondingMinCutoffDistanceLabel;
    private javax.swing.JPanel itsNonbondingParameter;
    private javax.swing.JPanel itsParameterPanel;
    private javax.swing.JPanel itsTotalPanel;
    private javax.swing.JScrollPane itsTotalScrollPane;
    // End of variables declaration//GEN-END:variables
    public JComboBox<String> getInputFileComboBox() {
        return this.itsInputFileComboBox;
    }

    public MemorialTextField getMaxStepTextField() {
        return itsMaxStepTextField;
    }

    public MemorialTextField getConvergedRmsdTextField() {
        return itsConvergedRmsdTextField;
    }

    public MoleculeTableTopComponent getSelectedMoleculeTableTopComponent() {
        return BasicChemistryWindowAdapter.getMoleculeTableTopComponent(this.itsInputFileComboBox.getSelectedItem().toString());
    }

    public SbffCalculationParameter getCalculationParameter() {
        return new SbffCalculationParameter(Double.parseDouble(this.itsElectroStaticMinCutoffDistanceTextField.getText()),
                Double.parseDouble(this.itsElectroStaticMaxCutoffDistanceTextField.getText()),
                Double.parseDouble(this.itsNonbondingMinCutoffDistanceTextField.getText()),
                Double.parseDouble(this.itsNonbondingMaxCutoffDistanceTextField.getText()),
                Double.parseDouble(this.itsDielectricConstantTextField.getText()));
    }

    @Override
    protected void componentActivated() {
        TopComponent[] theTopComponents = WindowAdapter.getOutputMode().getTopComponents();

        this.itsInputFileComboBox.removeAllItems();

        for (TopComponent theTopComponent : theTopComponents) {
            if (theTopComponent instanceof MoleculeTableTopComponent && theTopComponent.isOpened()) {
                this.itsInputFileComboBox.addItem(theTopComponent.getName());
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

class MinimizationCalculationButtonListener extends AbstractAction implements Runnable, Cancellable, Serializable {

    private static final long serialVersionUID = 4204441286280492130L;

    private MinimizationWindowTopComponent itsTopComponent;
    private MoleculeTableTopComponent itsInputTableTopComponent;
    private MoleculeTableTopComponent itsResultTableTopComponent;
    private MoleculeStructureViewerTopComponent itsResultStructureViewerTopComponent;
    private IAtomContainerSet itsResultMoleculeSet;
    private File itsResultTempFile;
    private Thread itsThread;

    public MinimizationCalculationButtonListener(MinimizationWindowTopComponent theTopComponent) {
        this.itsTopComponent = theTopComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.itsInputTableTopComponent = BasicChemistryWindowAdapter.getMoleculeTableTopComponent(this.itsTopComponent.getInputFileComboBox()
                .getSelectedItem().toString());
        this.itsResultTempFile = new File(Constant.TEMP_DIR + System.nanoTime() + "_Minimization_result.sdf");
        this.itsThread = new Thread(this);

        this.itsThread.start();

        this.__setWindows();
    }

    private void __setWindows() {
        this.itsResultTableTopComponent = BasicChemistryWindowAdapter.setTableTopComponent(null, "Minimized_" + this.itsInputTableTopComponent.getName());
        this.itsResultStructureViewerTopComponent = new MoleculeStructureViewerTopComponent(this.itsResultTableTopComponent);

        WindowAdapter.getOutputMode().dockInto(this.itsResultTableTopComponent);
        WindowAdapter.getEditorMode().dockInto(this.itsResultStructureViewerTopComponent);

        this.itsResultTableTopComponent.setViewerTopComponent(this.itsResultStructureViewerTopComponent);
        this.itsResultStructureViewerTopComponent.setTableComponent(this.itsResultTableTopComponent);

        this.itsResultTableTopComponent.open();
        this.itsResultStructureViewerTopComponent.open();

        this.itsResultTableTopComponent.requestActive();
        this.itsResultStructureViewerTopComponent.requestActive();
    }

    @Override
    public void run() {
        ProgressHandle theProgress = ProgressHandleFactory.createHandle("Minimization " + this.itsInputTableTopComponent.getName() + "...", this, this);
        SbffCalculationParameter theCalculationParameter = this.itsTopComponent.getCalculationParameter();
        int theIndex = 1;

        this.itsResultMoleculeSet = new AtomContainerSet();
        theProgress.start(this.itsInputTableTopComponent.getMoleculeSet().getAtomContainerCount() + 1);

        for (IAtomContainer theMolecule : this.itsInputTableTopComponent.getMoleculeSet().atomContainers()) {
            theProgress.setDisplayName("Minimization " + this.itsInputTableTopComponent.getName() + " (" + theMolecule.getProperty("cdk:Title").toString()
                    + ") " + theIndex + "/" + this.itsInputTableTopComponent.getMoleculeSet().getAtomContainerCount());

            SbffEnergyFunction theEnergyFunction = new SbffEnergyFunction(theMolecule, theCalculationParameter);
            ConjugatedGradientMinimizerInMolecularStructure theOptimizer = new ConjugatedGradientMinimizerInMolecularStructure(theEnergyFunction,
                    Integer.parseInt(this.itsTopComponent.getMaxStepTextField().getText()),
                    Double.parseDouble(this.itsTopComponent.getConvergedRmsdTextField().getText()));

            this.itsResultMoleculeSet.addAtomContainer(theOptimizer.optimize());

            theProgress.progress(theMolecule.getProperty("cdk:Title").toString() + "...", theIndex++);
        }

        SDFWriter.writeSDFile(this.itsResultMoleculeSet, this.itsResultTempFile);

        this.__setWindowsAtFinished();

        theProgress.finish();
    }

    private void __setWindowsAtFinished() {
        this.itsResultTableTopComponent.setMoleculeSet(SDFReader.openMoleculeFile(this.itsResultTempFile));
        this.itsResultTableTopComponent.setMoleculeFile(this.itsResultTempFile);
        this.itsResultStructureViewerTopComponent.openFile(this.itsResultTempFile);
        
        this.itsResultTableTopComponent.getTable().addData(ConjugatedGradientMinimizerInMolecularStructure.INITIAL_ENERGY_KEY, 
                ConjugatedGradientMinimizerInMolecularStructure.OPTIMIZED_ENERGY_KEY);
        
        this.itsResultTableTopComponent.revalidate();
        this.itsResultStructureViewerTopComponent.revalidate();
    }

    @Override
    public boolean cancel() {
        this.itsThread.interrupt();

        return true;
    }
}
