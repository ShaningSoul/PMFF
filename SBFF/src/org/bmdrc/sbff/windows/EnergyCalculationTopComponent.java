/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.sbff.windows;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.bmdrc.basicchemistry.windows.BasicChemistryWindowAdapter;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.sbff.energyfunction.SbffEnergyFunction;
import org.bmdrc.ui.abstracts.WindowAdapter;
import org.bmdrc.ui.components.MemorialTextField;
import org.bmdrc.ui.listener.DoubleValueCheckerListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Cancellable;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.bmdrc.sbff.windows//EnergyCalculation//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "EnergyCalculationTopComponent",
        iconBase = "org/bmdrc/sbff/resource/EnergyButton.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "org.bmdrc.sbff.windows.EnergyCalculationTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Calculate", position = 2000)
    ,
    @ActionReference(path = "Toolbars/Calculate", position = 2000)
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_EnergyCalculationAction",
        preferredID = "EnergyCalculationTopComponent"
)
@Messages({
    "CTL_EnergyCalculationAction=EnergyCalculation",
    "CTL_EnergyCalculationTopComponent=Energy Calculation"
})
public final class EnergyCalculationTopComponent extends TopComponent implements Serializable {

    private static final long serialVersionUID = 4647990757728088173L;

    private MemorialTextField itsDielectricConstantTextField;
    private MemorialTextField itsElectroStaticMaxCutoffDistanceTextField;
    private MemorialTextField itsElectroStaticMinCutoffDistanceTextField;
    private MemorialTextField itsNonbondingMaxCutoffDistanceTextField;
    private MemorialTextField itsNonbondingMinCutoffDistanceTextField;
    
    public EnergyCalculationTopComponent() {
        initComponents();
        setName(Bundle.CTL_EnergyCalculationTopComponent());

        this.__initializeVariable();
        this.__addListener();
    }

    private void __initializeVariable() {
        this.itsDielectricConstantTextField = new MemorialTextField();
        this.itsElectroStaticMinCutoffDistanceTextField = new MemorialTextField();
        this.itsElectroStaticMaxCutoffDistanceTextField = new MemorialTextField();
        this.itsNonbondingMinCutoffDistanceTextField = new MemorialTextField();
        this.itsNonbondingMaxCutoffDistanceTextField = new MemorialTextField();
        
        this.__initializeMemorialTextField(this.itsDielectricConstantPanel, this.itsDielectricConstantTextField, "1.0");
        this.__initializeMemorialTextField(this.itsElectricMinCutOffDistancePanel, this.itsElectroStaticMinCutoffDistanceTextField, "6.0");
        this.__initializeMemorialTextField(this.itsElectroStaticMaxCutoffDistancePanel, this.itsElectroStaticMaxCutoffDistanceTextField, "12.0");
        this.__initializeMemorialTextField(this.itsNonbondingMinCutOffDistancePanel, this.itsNonbondingMinCutoffDistanceTextField, "4.0");
        this.__initializeMemorialTextField(this.itsNonbondingMaxCutoffDistancePanel, this.itsNonbondingMaxCutoffDistanceTextField, "6.0");
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
        this.itsCalculateButton.addActionListener(new EnergyCalculationButtonListener(this));
        
        this.itsDielectricConstantTextField.addFocusListener(new DoubleValueCheckerListener(this.itsDielectricConstantTextField));
        this.itsElectroStaticMinCutoffDistanceTextField.addFocusListener(new DoubleValueCheckerListener(this.itsElectroStaticMinCutoffDistanceTextField));
        this.itsElectroStaticMaxCutoffDistanceTextField.addFocusListener(new DoubleValueCheckerListener(this.itsElectroStaticMaxCutoffDistanceTextField));
        this.itsNonbondingMinCutoffDistanceTextField.addFocusListener(new DoubleValueCheckerListener((this.itsNonbondingMinCutoffDistanceTextField)));
        this.itsNonbondingMaxCutoffDistanceTextField.addFocusListener(new DoubleValueCheckerListener((this.itsNonbondingMaxCutoffDistanceTextField)));
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
        itsNonbondingMinCutoffDistanceLabel1 = new javax.swing.JLabel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        itsNonbondingMaxCutoffDistancePanel = new javax.swing.JPanel();
        itsNonbondingMaxCutoffDistanceLabel1 = new javax.swing.JLabel();
        itsCalculateButtonPanel = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        itsCalculateButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        setLayout(new java.awt.BorderLayout());

        itsTotalPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsTotalPanel.setMinimumSize(new java.awt.Dimension(300, 80));
        itsTotalPanel.setPreferredSize(new java.awt.Dimension(300, 100));
        itsTotalPanel.setLayout(new javax.swing.BoxLayout(itsTotalPanel, javax.swing.BoxLayout.Y_AXIS));

        itsInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsInputPanel.border.title"))); // NOI18N
        itsInputPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        itsInputPanel.setMinimumSize(new java.awt.Dimension(160, 50));
        itsInputPanel.setPreferredSize(new java.awt.Dimension(160, 50));
        itsInputPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsInputFileLabel, org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsInputFileLabel.text")); // NOI18N
        itsInputPanel.add(itsInputFileLabel, java.awt.BorderLayout.WEST);

        itsInputFileComboBox.setMaximumSize(new java.awt.Dimension(32767, 21));
        itsInputFileComboBox.setMinimumSize(new java.awt.Dimension(100, 21));
        itsInputFileComboBox.setPreferredSize(new java.awt.Dimension(100, 21));
        itsInputPanel.add(itsInputFileComboBox, java.awt.BorderLayout.EAST);

        itsTotalPanel.add(itsInputPanel);

        itsParameterPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsParameterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsParameterPanel.border.title"))); // NOI18N
        itsParameterPanel.setMaximumSize(new java.awt.Dimension(2147483647, 220));
        itsParameterPanel.setMinimumSize(new java.awt.Dimension(160, 100));
        itsParameterPanel.setPreferredSize(new java.awt.Dimension(160, 100));
        itsParameterPanel.setLayout(new javax.swing.BoxLayout(itsParameterPanel, javax.swing.BoxLayout.Y_AXIS));

        itsDielectricConstantPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsDielectricConstantPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsDielectricConstantPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsDielectricConstantLabel, org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsDielectricConstantLabel.text")); // NOI18N
        itsDielectricConstantPanel.add(itsDielectricConstantLabel, java.awt.BorderLayout.WEST);

        itsParameterPanel.add(itsDielectricConstantPanel);
        itsParameterPanel.add(filler3);

        itsElectricParameter.setBackground(new java.awt.Color(255, 255, 255));
        itsElectricParameter.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsElectricParameter.border.title"))); // NOI18N
        itsElectricParameter.setMinimumSize(new java.awt.Dimension(160, 50));
        itsElectricParameter.setPreferredSize(new java.awt.Dimension(160, 50));
        itsElectricParameter.setLayout(new javax.swing.BoxLayout(itsElectricParameter, javax.swing.BoxLayout.Y_AXIS));

        itsElectricMinCutOffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsElectricMinCutOffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsElectricMinCutOffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsElectroStaticMinCutoffDistanceLabel, org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsElectroStaticMinCutoffDistanceLabel.text")); // NOI18N
        itsElectricMinCutOffDistancePanel.add(itsElectroStaticMinCutoffDistanceLabel, java.awt.BorderLayout.WEST);

        itsElectricParameter.add(itsElectricMinCutOffDistancePanel);
        itsElectricParameter.add(filler6);

        itsElectroStaticMaxCutoffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsElectroStaticMaxCutoffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsElectroStaticMaxCutoffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsElectroStaticMaxCutoffDistanceLabel, org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsElectroStaticMaxCutoffDistanceLabel.text")); // NOI18N
        itsElectroStaticMaxCutoffDistancePanel.add(itsElectroStaticMaxCutoffDistanceLabel, java.awt.BorderLayout.WEST);

        itsElectricParameter.add(itsElectroStaticMaxCutoffDistancePanel);

        itsParameterPanel.add(itsElectricParameter);
        itsParameterPanel.add(filler5);

        itsNonbondingParameter.setBackground(new java.awt.Color(255, 255, 255));
        itsNonbondingParameter.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsNonbondingParameter.border.title"))); // NOI18N
        itsNonbondingParameter.setMinimumSize(new java.awt.Dimension(160, 50));
        itsNonbondingParameter.setPreferredSize(new java.awt.Dimension(160, 50));
        itsNonbondingParameter.setLayout(new javax.swing.BoxLayout(itsNonbondingParameter, javax.swing.BoxLayout.Y_AXIS));

        itsNonbondingMinCutOffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsNonbondingMinCutOffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsNonbondingMinCutOffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsNonbondingMinCutoffDistanceLabel1, org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsNonbondingMinCutoffDistanceLabel1.text")); // NOI18N
        itsNonbondingMinCutOffDistancePanel.add(itsNonbondingMinCutoffDistanceLabel1, java.awt.BorderLayout.WEST);

        itsNonbondingParameter.add(itsNonbondingMinCutOffDistancePanel);
        itsNonbondingParameter.add(filler7);

        itsNonbondingMaxCutoffDistancePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsNonbondingMaxCutoffDistancePanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsNonbondingMaxCutoffDistancePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsNonbondingMaxCutoffDistanceLabel1, org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsNonbondingMaxCutoffDistanceLabel1.text")); // NOI18N
        itsNonbondingMaxCutoffDistancePanel.add(itsNonbondingMaxCutoffDistanceLabel1, java.awt.BorderLayout.WEST);

        itsNonbondingParameter.add(itsNonbondingMaxCutoffDistancePanel);

        itsParameterPanel.add(itsNonbondingParameter);

        itsTotalPanel.add(itsParameterPanel);

        itsCalculateButtonPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsCalculateButtonPanel.setMaximumSize(new java.awt.Dimension(32767, 30));
        itsCalculateButtonPanel.setMinimumSize(new java.awt.Dimension(160, 30));
        itsCalculateButtonPanel.setPreferredSize(new java.awt.Dimension(160, 50));
        itsCalculateButtonPanel.setLayout(new javax.swing.BoxLayout(itsCalculateButtonPanel, javax.swing.BoxLayout.X_AXIS));
        itsCalculateButtonPanel.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(itsCalculateButton, org.openide.util.NbBundle.getMessage(EnergyCalculationTopComponent.class, "EnergyCalculationTopComponent.itsCalculateButton.text")); // NOI18N
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
    private javax.swing.JButton itsCalculateButton;
    private javax.swing.JPanel itsCalculateButtonPanel;
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
    private javax.swing.JLabel itsNonbondingMaxCutoffDistanceLabel1;
    private javax.swing.JPanel itsNonbondingMaxCutoffDistancePanel;
    private javax.swing.JPanel itsNonbondingMinCutOffDistancePanel;
    private javax.swing.JLabel itsNonbondingMinCutoffDistanceLabel1;
    private javax.swing.JPanel itsNonbondingParameter;
    private javax.swing.JPanel itsParameterPanel;
    private javax.swing.JPanel itsTotalPanel;
    private javax.swing.JScrollPane itsTotalScrollPane;
    // End of variables declaration//GEN-END:variables
    public MoleculeTableTopComponent getSelectedMoleculeTableTopComponent() {
        return BasicChemistryWindowAdapter.getMoleculeTableTopComponent(this.itsInputFileComboBox.getSelectedItem().toString());
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
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER; //To change body of generated methods, choose Tools | Templates.
    }

}

class EnergyCalculationButtonListener extends AbstractAction implements Runnable, Cancellable, Serializable {

    private static final long serialVersionUID = -8389977570221450176L;

    private EnergyCalculationTopComponent itsTopComponent;
    private MoleculeTableTopComponent itsTableTopComponent;
    private Thread itsThread;
    
    public EnergyCalculationButtonListener(EnergyCalculationTopComponent theTopComponent) {
        this.itsTopComponent = theTopComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.itsTableTopComponent = this.itsTopComponent.getSelectedMoleculeTableTopComponent();
        this.itsThread = new Thread(this);

        this.itsThread.start();
    }

    @Override
    public void run() {
        ProgressHandle theProgress = ProgressHandleFactory.createHandle("Calculate energy", this, this);
        int theIndex = 1;

        theProgress.start(this.itsTableTopComponent.getMoleculeSet().getAtomContainerCount() + 1);

        for (IAtomContainer theMolecule : this.itsTableTopComponent.getMoleculeSet().atomContainers()) {
            SbffEnergyFunction theEnergyFunction = new SbffEnergyFunction(theMolecule);
            Double theEnergy = theEnergyFunction.calculateEnergyFunction();

            theMolecule.setProperty(SbffEnergyFunction.SBFF_ENERGY_KEY, String.format("%4.4f", theEnergy.doubleValue()));
            theProgress.progress(theIndex++);
        }

        theProgress.progress("Table setting...", theIndex);
        this.itsTableTopComponent.getTable().addData(this.itsTableTopComponent.getMoleculeSet(), SbffEnergyFunction.SBFF_ENERGY_KEY);

        theProgress.finish();
    }

    @Override
    public boolean cancel() {
        this.itsThread.interrupt();

        return true;
    }
}
