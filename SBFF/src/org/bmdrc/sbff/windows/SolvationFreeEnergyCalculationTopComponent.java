/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.sbff.windows;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.bmdrc.basicchemistry.windows.BasicChemistryWindowAdapter;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.sbff.solvation.GSFECalculator;
import org.bmdrc.sbff.solvation.parameter.SolventParameter;
import org.bmdrc.sbff.solvation.parameter.SolventProperty;
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
        dtd = "-//org.bmdrc.sbff.windows//SolvationFreeEnergyCalculation//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "SolvationFreeEnergyCalculationTopComponent",
        iconBase="org/bmdrc/sbff/resource/Solvation_icon.png", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "org.bmdrc.sbff.windows.SolvationFreeEnergyCalculationTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Calculate", position = 2200)
    ,
    @ActionReference(path = "Toolbars/Calculate", position = 2200)
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_SolvationFreeEnergyCalculationAction",
        preferredID = "SolvationFreeEnergyCalculationTopComponent"
)
@Messages({
    "CTL_SolvationFreeEnergyCalculationAction=Solvation Free Energy Calculation",
    "CTL_SolvationFreeEnergyCalculationTopComponent=Solvation Free Energy Calculation",
    "HINT_SolvationFreeEnergyCalculationTopComponent=Solvation Free Energy Calculation"
})
public final class SolvationFreeEnergyCalculationTopComponent extends TopComponent implements Serializable {
    
    private static final long serialVersionUID = 954570304251500041L;
    
    private MemorialTextField itsDielectricConstantTextField;
    private MemorialTextField itsReflectiveIndexTextField;
    private MemorialTextField itsSurfaceTensionTextField;
    private MemorialTextField itsSolventHydrogenBondAcidityTextField;
    private MemorialTextField itsSolventHydrogenBondBasicityTextField;
    
    public SolvationFreeEnergyCalculationTopComponent() {
        initComponents();
        setName(Bundle.CTL_SolvationFreeEnergyCalculationTopComponent());
        
        this.__initializeVariable();
        this.__addListener();
    }
    
    private void __initializeVariable() {
        this.itsDielectricConstantTextField = new MemorialTextField();
        this.itsReflectiveIndexTextField = new MemorialTextField();
        this.itsSurfaceTensionTextField = new MemorialTextField();
        this.itsSolventHydrogenBondAcidityTextField = new MemorialTextField();
        this.itsSolventHydrogenBondBasicityTextField = new MemorialTextField();
        
        this.__initializeMemorialTextField(this.itsDielectricConstantPanel, this.itsDielectricConstantTextField);
        this.__initializeMemorialTextField(this.itsReflectiveIndexPanel, this.itsReflectiveIndexTextField);
        this.__initializeMemorialTextField(this.itsSurfaceTensionPanel, this.itsSurfaceTensionTextField);
        this.__initializeMemorialTextField(this.itsSolventHydrogenBondAcidityPanel, this.itsSolventHydrogenBondAcidityTextField);
        this.__initializeMemorialTextField(this.itsSolventHydrogenBondBasicityPanel, this.itsSolventHydrogenBondBasicityTextField);
        
        this.itsPresetComboBox.setSelectedItem(SolventProperty.Water);
    }
    
    private void __initializeMemorialTextField(JPanel theParentPanel, MemorialTextField theVariable) {
        theVariable.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        theVariable.setMaximumSize(new java.awt.Dimension(80, 21));
        theVariable.setMinimumSize(new java.awt.Dimension(80, 21));
        theVariable.setPreferredSize(new java.awt.Dimension(80, 21));
        theParentPanel.add(theVariable, java.awt.BorderLayout.EAST);
    }
    
    private void __addListener() {
        this.itsCalculateButton.addActionListener(new SolvationFreeEnergyCalculateButtonListener(this));
        
        this.itsDielectricConstantTextField.addFocusListener(new DoubleValueCheckerListener(this.itsDielectricConstantTextField));
        this.itsReflectiveIndexTextField.addFocusListener(new DoubleValueCheckerListener(this.itsReflectiveIndexTextField));
        this.itsSurfaceTensionTextField.addFocusListener(new DoubleValueCheckerListener(this.itsSurfaceTensionTextField));
        this.itsSolventHydrogenBondAcidityTextField.addFocusListener(new DoubleValueCheckerListener((this.itsSolventHydrogenBondAcidityTextField)));
        this.itsSolventHydrogenBondBasicityTextField.addFocusListener(new DoubleValueCheckerListener((this.itsSolventHydrogenBondBasicityTextField)));
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
        itsPresetPanel = new javax.swing.JPanel();
        itsPresetLabel = new javax.swing.JLabel();
        itsPresetComboBox = new javax.swing.JComboBox<>();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsDielectricConstantPanel = new javax.swing.JPanel();
        itsDielectricConstantLabel = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsReflectiveIndexPanel = new javax.swing.JPanel();
        itsReflectiveIndexLabel = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsSurfaceTensionPanel = new javax.swing.JPanel();
        itsSurfaceTensionLabel = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsSolventHydrogenBondAcidityPanel = new javax.swing.JPanel();
        itsSolventHydrogenBondAcidityLabel = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsSolventHydrogenBondBasicityPanel = new javax.swing.JPanel();
        itsSolventHydrogenBondBasicityLabel = new javax.swing.JLabel();
        itsCalculateButtonPanel = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        itsCalculateButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        setLayout(new java.awt.BorderLayout());

        itsTotalScrollPane.setMinimumSize(new java.awt.Dimension(160, 23));

        itsTotalPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsTotalPanel.setMinimumSize(new java.awt.Dimension(300, 260));
        itsTotalPanel.setPreferredSize(new java.awt.Dimension(300, 298));
        itsTotalPanel.setLayout(new javax.swing.BoxLayout(itsTotalPanel, javax.swing.BoxLayout.Y_AXIS));

        itsInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsInputPanel.border.title"))); // NOI18N
        itsInputPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        itsInputPanel.setMinimumSize(new java.awt.Dimension(130, 50));
        itsInputPanel.setPreferredSize(new java.awt.Dimension(130, 50));
        itsInputPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsInputFileLabel, org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsInputFileLabel.text")); // NOI18N
        itsInputPanel.add(itsInputFileLabel, java.awt.BorderLayout.WEST);

        itsInputFileComboBox.setMaximumSize(new java.awt.Dimension(32767, 21));
        itsInputFileComboBox.setMinimumSize(new java.awt.Dimension(100, 21));
        itsInputFileComboBox.setPreferredSize(new java.awt.Dimension(100, 21));
        itsInputPanel.add(itsInputFileComboBox, java.awt.BorderLayout.EAST);

        itsTotalPanel.add(itsInputPanel);

        itsParameterPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsParameterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsParameterPanel.border.title"))); // NOI18N
        itsParameterPanel.setMaximumSize(new java.awt.Dimension(2147483647, 180));
        itsParameterPanel.setMinimumSize(new java.awt.Dimension(130, 180));
        itsParameterPanel.setPreferredSize(new java.awt.Dimension(130, 180));
        itsParameterPanel.setLayout(new javax.swing.BoxLayout(itsParameterPanel, javax.swing.BoxLayout.Y_AXIS));

        itsPresetPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsPresetPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsPresetPanel.setMinimumSize(new java.awt.Dimension(130, 21));
        itsPresetPanel.setPreferredSize(new java.awt.Dimension(130, 21));
        itsPresetPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsPresetLabel, org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsPresetLabel.text")); // NOI18N
        itsPresetPanel.add(itsPresetLabel, java.awt.BorderLayout.WEST);

        itsPresetComboBox.setModel(new SolvationFreeEnergyPresetComboBoxModel(this.itsPresetComboBox));
        itsPresetComboBox.setMaximumSize(new java.awt.Dimension(32767, 21));
        itsPresetComboBox.setMinimumSize(new java.awt.Dimension(100, 21));
        itsPresetComboBox.setPreferredSize(new java.awt.Dimension(100, 21));
        itsPresetComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                itsPresetComboBoxItemStateChanged(evt);
            }
        });
        itsPresetPanel.add(itsPresetComboBox, java.awt.BorderLayout.EAST);

        itsParameterPanel.add(itsPresetPanel);
        itsParameterPanel.add(filler7);

        itsDielectricConstantPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsDielectricConstantPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsDielectricConstantPanel.setMinimumSize(new java.awt.Dimension(130, 15));
        itsDielectricConstantPanel.setPreferredSize(new java.awt.Dimension(130, 15));
        itsDielectricConstantPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsDielectricConstantLabel, org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsDielectricConstantLabel.text")); // NOI18N
        itsDielectricConstantPanel.add(itsDielectricConstantLabel, java.awt.BorderLayout.WEST);

        itsParameterPanel.add(itsDielectricConstantPanel);
        itsParameterPanel.add(filler3);

        itsReflectiveIndexPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsReflectiveIndexPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsReflectiveIndexPanel.setMinimumSize(new java.awt.Dimension(130, 15));
        itsReflectiveIndexPanel.setPreferredSize(new java.awt.Dimension(130, 15));
        itsReflectiveIndexPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsReflectiveIndexLabel, org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsReflectiveIndexLabel.text")); // NOI18N
        itsReflectiveIndexPanel.add(itsReflectiveIndexLabel, java.awt.BorderLayout.WEST);

        itsParameterPanel.add(itsReflectiveIndexPanel);
        itsParameterPanel.add(filler4);

        itsSurfaceTensionPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsSurfaceTensionPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsSurfaceTensionPanel.setMinimumSize(new java.awt.Dimension(130, 15));
        itsSurfaceTensionPanel.setPreferredSize(new java.awt.Dimension(130, 15));
        itsSurfaceTensionPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsSurfaceTensionLabel, org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsSurfaceTensionLabel.text")); // NOI18N
        itsSurfaceTensionPanel.add(itsSurfaceTensionLabel, java.awt.BorderLayout.WEST);

        itsParameterPanel.add(itsSurfaceTensionPanel);
        itsParameterPanel.add(filler5);

        itsSolventHydrogenBondAcidityPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsSolventHydrogenBondAcidityPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsSolventHydrogenBondAcidityPanel.setMinimumSize(new java.awt.Dimension(130, 15));
        itsSolventHydrogenBondAcidityPanel.setPreferredSize(new java.awt.Dimension(130, 15));
        itsSolventHydrogenBondAcidityPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsSolventHydrogenBondAcidityLabel, org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsSolventHydrogenBondAcidityLabel.text")); // NOI18N
        itsSolventHydrogenBondAcidityPanel.add(itsSolventHydrogenBondAcidityLabel, java.awt.BorderLayout.WEST);

        itsParameterPanel.add(itsSolventHydrogenBondAcidityPanel);
        itsParameterPanel.add(filler6);

        itsSolventHydrogenBondBasicityPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsSolventHydrogenBondBasicityPanel.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        itsSolventHydrogenBondBasicityPanel.setMinimumSize(new java.awt.Dimension(130, 15));
        itsSolventHydrogenBondBasicityPanel.setPreferredSize(new java.awt.Dimension(130, 15));
        itsSolventHydrogenBondBasicityPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsSolventHydrogenBondBasicityLabel, org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsSolventHydrogenBondBasicityLabel.text")); // NOI18N
        itsSolventHydrogenBondBasicityPanel.add(itsSolventHydrogenBondBasicityLabel, java.awt.BorderLayout.WEST);

        itsParameterPanel.add(itsSolventHydrogenBondBasicityPanel);

        itsTotalPanel.add(itsParameterPanel);

        itsCalculateButtonPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsCalculateButtonPanel.setMaximumSize(new java.awt.Dimension(32767, 30));
        itsCalculateButtonPanel.setMinimumSize(new java.awt.Dimension(100, 30));
        itsCalculateButtonPanel.setPreferredSize(new java.awt.Dimension(470, 50));
        itsCalculateButtonPanel.setLayout(new javax.swing.BoxLayout(itsCalculateButtonPanel, javax.swing.BoxLayout.X_AXIS));
        itsCalculateButtonPanel.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(itsCalculateButton, org.openide.util.NbBundle.getMessage(SolvationFreeEnergyCalculationTopComponent.class, "SolvationFreeEnergyCalculationTopComponent.itsCalculateButton.text")); // NOI18N
        itsCalculateButtonPanel.add(itsCalculateButton);
        itsCalculateButtonPanel.add(filler2);

        itsTotalPanel.add(itsCalculateButtonPanel);

        itsTotalScrollPane.setViewportView(itsTotalPanel);

        add(itsTotalScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void itsPresetComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_itsPresetComboBoxItemStateChanged
        SolventProperty theProperty = (SolventProperty)this.itsPresetComboBox.getSelectedItem();
        
        this.itsDielectricConstantTextField.setText(theProperty.DIELECTRIC_CONSTANT.toString());
        this.itsReflectiveIndexTextField.setText(theProperty.REFLACTIVE_INDEX.toString());
        this.itsSurfaceTensionTextField.setText(theProperty.SURFACE_TENSION.toString());
        this.itsSolventHydrogenBondAcidityTextField.setText(theProperty.HBOND_ACIDITY.toString());
        this.itsSolventHydrogenBondBasicityTextField.setText(theProperty.HBOND_BASICITY.toString());
        
        this.revalidate();
    }//GEN-LAST:event_itsPresetComboBoxItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JButton itsCalculateButton;
    private javax.swing.JPanel itsCalculateButtonPanel;
    private javax.swing.JLabel itsDielectricConstantLabel;
    private javax.swing.JPanel itsDielectricConstantPanel;
    private javax.swing.JComboBox<String> itsInputFileComboBox;
    private javax.swing.JLabel itsInputFileLabel;
    private javax.swing.JPanel itsInputPanel;
    private javax.swing.JPanel itsParameterPanel;
    private javax.swing.JComboBox<SolventProperty> itsPresetComboBox;
    private javax.swing.JLabel itsPresetLabel;
    private javax.swing.JPanel itsPresetPanel;
    private javax.swing.JLabel itsReflectiveIndexLabel;
    private javax.swing.JPanel itsReflectiveIndexPanel;
    private javax.swing.JLabel itsSolventHydrogenBondAcidityLabel;
    private javax.swing.JPanel itsSolventHydrogenBondAcidityPanel;
    private javax.swing.JLabel itsSolventHydrogenBondBasicityLabel;
    private javax.swing.JPanel itsSolventHydrogenBondBasicityPanel;
    private javax.swing.JLabel itsSurfaceTensionLabel;
    private javax.swing.JPanel itsSurfaceTensionPanel;
    private javax.swing.JPanel itsTotalPanel;
    private javax.swing.JScrollPane itsTotalScrollPane;
    // End of variables declaration//GEN-END:variables
    public MoleculeTableTopComponent getSelectedMoleculeTableTopComponent() {
        return BasicChemistryWindowAdapter.getMoleculeTableTopComponent(this.itsInputFileComboBox.getSelectedItem().toString());
    }
    
    public MemorialTextField getDielectricConstantTextField() {
        return itsDielectricConstantTextField;
    }
    
    public MemorialTextField getReflectiveIndexTextField() {
        return itsReflectiveIndexTextField;
    }
    
    public MemorialTextField getSurfaceTensionTextField() {
        return itsSurfaceTensionTextField;
    }
    
    public MemorialTextField getSolventHydrogenBondAcidityTextField() {
        return itsSolventHydrogenBondAcidityTextField;
    }
    
    public MemorialTextField getSolventHydrogenBondBasicityTextField() {
        return itsSolventHydrogenBondBasicityTextField;
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
}

class SolvationFreeEnergyCalculateButtonListener extends AbstractAction implements Runnable, Cancellable, Serializable {
    
    private static final long serialVersionUID = 1136480963111715893L;
    
    private SolvationFreeEnergyCalculationTopComponent itsTopComponent;
    private MoleculeTableTopComponent itsTableTopComponent;
    private Thread itsThread;
    
    public SolvationFreeEnergyCalculateButtonListener(SolvationFreeEnergyCalculationTopComponent theTopComponent) {
        this.itsTopComponent = theTopComponent;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        this.itsTableTopComponent = this.itsTopComponent.getSelectedMoleculeTableTopComponent();
        this.itsThread = new Thread(this);
        
        this.itsThread.start();
    }
    
    @Override
    public void run() {
        ProgressHandle theProgress = ProgressHandleFactory.createHandle("Solvation free energy " + this.itsTopComponent.getName() + "calculation...", this, this);
        SolventParameter theParameter = new SolventParameter(Double.parseDouble(this.itsTopComponent.getDielectricConstantTextField().getText()),
                Double.parseDouble(this.itsTopComponent.getReflectiveIndexTextField().getText()),
                Double.parseDouble(this.itsTopComponent.getSurfaceTensionTextField().getText()),
                Double.parseDouble(this.itsTopComponent.getSolventHydrogenBondAcidityTextField().getText()),
                Double.parseDouble(this.itsTopComponent.getSolventHydrogenBondBasicityTextField().getText()));
        GSFECalculator theCalculator = new GSFECalculator(theParameter);
        int theIndex = 1;
        
        theProgress.start(this.itsTableTopComponent.getMoleculeSet().getAtomContainerCount() + 1);

        for (IAtomContainer theMolecule : this.itsTableTopComponent.getMoleculeSet().atomContainers()) {
            theProgress.setDisplayName("Solvation free energy " + this.itsTopComponent.getName() + " (" + theMolecule.getProperty("cdk:Title").toString()
                    + ") " + theIndex++ + "/" + this.itsTableTopComponent.getMoleculeSet().getAtomContainerCount());
            
            theCalculator.calculateGSFE(theMolecule);
            
            theProgress.progress(theIndex);
        }

        theProgress.progress("Table setting...", theIndex);
        this.itsTableTopComponent.getTable().addData(this.itsTableTopComponent.getMoleculeSet(), GSFECalculator.GSFE_KEY);

        theProgress.finish();
    }
    
    @Override
    public boolean cancel() {
        this.itsThread.interrupt();
        
        return true;
    }
}

class SolvationFreeEnergyPresetComboBoxModel extends AbstractListModel<SolventProperty> implements Serializable, ComboBoxModel<SolventProperty> {

    private static final long serialVersionUID = -4626729146919908146L;

    private JComboBox itsComboBox;
    private SolventProperty itsSelectedItem;
    
    public SolvationFreeEnergyPresetComboBoxModel(JComboBox theComboBox) {
        this.itsComboBox = theComboBox;
    }

    @Override
    public void setSelectedItem(Object theObject) {
        if(theObject instanceof SolventProperty) {
            this.itsSelectedItem = (SolventProperty)theObject;
        }
    }

    @Override
    public int getSize() {
        return SolventProperty.values().length;
    }

    @Override
    public SolventProperty getElementAt(int i) {
        return SolventProperty.values()[i];
    }

    @Override
    public Object getSelectedItem() {
        return this.itsSelectedItem;
    }
}
