/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bmdrc.pubchem.windows;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.bmdrc.basicchemistry.windows.MoleculeStructureViewerTopComponent;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.pubchem.calculator.CompoundCollector;
import org.bmdrc.pubchem.windows.table.CompoundCollectorInputTable;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.abstracts.WindowAdapter;
import org.bmdrc.ui.components.MemorialTextField;
import org.bmdrc.ui.filechooser.ExcelFileChooser;
import org.bmdrc.ui.reader.XlsxReader;
import org.bmdrc.ui.util.TwoDimensionList;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.bmdrc.pubchem.windows//CompoundCollectorWindows//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "CompoundCollectorWindowsTopComponent",
        iconBase = "org/bmdrc/pubchem/resources/PubChem_Icon_v2.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "CompoundCollectorWindowsTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 3100)
    ,
    @ActionReference(path = "Toolbars/Tools", position = 3100)
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_CompoundCollectorWindowsAction",
        preferredID = "CompoundCollectorWindowsTopComponent"
)
@Messages({
    "CTL_CompoundCollectorWindowsAction=Compound Collector",
    "CTL_CompoundCollectorWindowsTopComponent=Compound Collector",
    "HINT_CompoundCollectorWindowsTopComponent=This is a CompoundCollectorWindows window"
})
public final class CompoundCollectorWindowsTopComponent extends TopComponent {

    private enum InputMethods {
        Direct, Excel;

        public static List<String> getInputMethodList() {
            List<String> theInputMethodList = new ArrayList<>();

            for (InputMethods theMethod : InputMethods.values()) {
                theInputMethodList.add(theMethod.name());
            }

            return theInputMethodList;
        }
    }

    private CompoundCollectorInputTable itsInputTable;
    private MemorialTextField itsDataCountTextField;
    private List<JCheckBox> itsOutputFormCheckBoxList;

    public CompoundCollectorWindowsTopComponent() {
        initComponents();
        setName(Bundle.CTL_CompoundCollectorWindowsTopComponent());
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);

        this.__initializeVariable();
    }

    public CompoundCollectorInputTable getInputTable() {
        return itsInputTable;
    }

    public MemorialTextField getDataCountTextField() {
        return itsDataCountTextField;
    }

    public JComboBox<String> getInputTypeComboBox() {
        return itsInputTypeComboBox;
    }

    public JComboBox<String> getInputMethodComboBox() {
        return itsInputMethodComboBox;
    }

    public JPanel getDataCountPanel() {
        return itsDataCountPanel;
    }

    public JPanel getExcelOpenPanel() {
        return itsExcelOpenPanel;
    }

    public JLabel getExcelPathLabel() {
        return this.itsExcelPathLabel;
    }

    private void __initializeVariable() {
        this.__initializeTable();
        this.__initializeMemorailTextField();
        this.__initializeComboBox();
        this.__initializeOutputFormCheckBoxList();
        this.__initializeListener();
        this.__registerKeyActionInInputTable();
    }

    private void __initializeComboBox() {
        this.itsInputTypeComboBox.setModel(this.__getComboBoxModel(CompoundCollector.Pubchem_Compound_Domain.getDomainList()));
        this.itsInputMethodComboBox.setModel(this.__getComboBoxModel(InputMethods.getInputMethodList()));

        this.itsInputMethodComboBox.addActionListener(new InputMethodComboBoxItemListener(this));

        this.itsInputTypeComboBox.setSelectedIndex(Constant.FIRST_INDEX);
        this.itsInputMethodComboBox.setSelectedIndex(Constant.FIRST_INDEX);
    }

    private void __initializeTable() {
        this.itsInputTable = new CompoundCollectorInputTable(String.class);

        this.itsInputTable.addFocusListener(new TableFocusListener(this));
        this.itsInputDataScrollPane.setViewportView(this.itsInputTable);
    }

    private void __initializeMemorailTextField() {
        this.itsDataCountTextField = new MemorialTextField(Integer.class);

        this.__initializeMemorialTextField(this.itsDataCountPanel, this.itsDataCountTextField, "3");

        this.itsDataCountTextField.addFocusListener(new DataCountFocusListener(this.itsInputTable));
    }

    private void __initializeOutputFormCheckBoxList() {
        this.itsOutputFormCheckBoxList = new ArrayList<>();

        for (CompoundCollector.Pubchem_Output_Form theOutputForm : CompoundCollector.Pubchem_Output_Form.values()) {
            JCheckBox theCheckBox = new JCheckBox(theOutputForm.MOLECULE_KEY);

            theCheckBox.setMaximumSize(new java.awt.Dimension(32767, 22));
            theCheckBox.setMinimumSize(new java.awt.Dimension(160, 15));
            theCheckBox.setPreferredSize(new java.awt.Dimension(160, 15));
            theCheckBox.setBackground(new java.awt.Color(255, 255, 255));

            theCheckBox.setSelected(true);

            this.itsOutputFormCheckBoxList.add(theCheckBox);
        }
    }

    private void __initializeMemorialTextField(JPanel theParentPanel, MemorialTextField theVariable, String theInitialValue) {
        theVariable.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        theVariable.setText(theInitialValue);
        theVariable.setMaximumSize(new java.awt.Dimension(100, 21));
        theVariable.setMinimumSize(new java.awt.Dimension(100, 21));
        theVariable.setPreferredSize(new java.awt.Dimension(100, 21));

        if (theParentPanel != null) {
            theParentPanel.add(theVariable, java.awt.BorderLayout.EAST);
        }
    }

    private void __initializeListener() {
        this.itsCalculateButton.addActionListener(new CalculateButtonListener(this));
        this.itsExcelOpenButton.addActionListener(new InputExcelButtonActionListner(this));
    }

    private DefaultComboBoxModel<String> __getComboBoxModel(List<String> theItemList) {
        return new DefaultComboBoxModel<>(theItemList.toArray(new String[theItemList.size()]));
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        itsTotalPanel = new javax.swing.JPanel();
        itsInputTypePanel = new javax.swing.JPanel();
        itsInputTypeLabel = new javax.swing.JLabel();
        itsInputTypeComboBox = new javax.swing.JComboBox<>();
        itsDataPanel = new javax.swing.JPanel();
        itsInputFileTypePanel = new javax.swing.JPanel();
        itsDataType = new javax.swing.JLabel();
        itsInputMethodComboBox = new javax.swing.JComboBox<>();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsDataCountPanel = new javax.swing.JPanel();
        itsDataCountLabel = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsExcelOpenPanel = new javax.swing.JPanel();
        itsExcelOpenLeftPanel = new javax.swing.JPanel();
        itsExcelPathNameLabel = new javax.swing.JLabel();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        itsExcelOpenCenterPanel = new javax.swing.JPanel();
        itsExcelPathLabel = new javax.swing.JLabel();
        itsExcelOpenRightPanel = new javax.swing.JPanel();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        itsExcelOpenButton = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        itsInputDataScrollPane = new javax.swing.JScrollPane();
        itsCalculateButtonPanel = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        itsCalculateButton = new javax.swing.JButton();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        setLayout(new java.awt.BorderLayout());

        itsTotalPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsTotalPanel.setLayout(new javax.swing.BoxLayout(itsTotalPanel, javax.swing.BoxLayout.Y_AXIS));

        itsInputTypePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsInputTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CompoundCollectorWindowsTopComponent.class, "CompoundCollectorWindowsTopComponent.itsInputTypePanel.border.title"))); // NOI18N
        itsInputTypePanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        itsInputTypePanel.setMinimumSize(new java.awt.Dimension(250, 50));
        itsInputTypePanel.setPreferredSize(new java.awt.Dimension(250, 50));
        itsInputTypePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsInputTypeLabel, org.openide.util.NbBundle.getMessage(CompoundCollectorWindowsTopComponent.class, "CompoundCollectorWindowsTopComponent.itsInputTypeLabel.text")); // NOI18N
        itsInputTypePanel.add(itsInputTypeLabel, java.awt.BorderLayout.WEST);

        itsInputTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        itsInputTypeComboBox.setMinimumSize(new java.awt.Dimension(160, 21));
        itsInputTypeComboBox.setPreferredSize(new java.awt.Dimension(160, 21));
        itsInputTypePanel.add(itsInputTypeComboBox, java.awt.BorderLayout.EAST);

        itsTotalPanel.add(itsInputTypePanel);

        itsDataPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CompoundCollectorWindowsTopComponent.class, "CompoundCollectorWindowsTopComponent.itsDataPanel.border.title"))); // NOI18N
        itsDataPanel.setMaximumSize(new java.awt.Dimension(32767, 220));
        itsDataPanel.setMinimumSize(new java.awt.Dimension(250, 220));
        itsDataPanel.setPreferredSize(new java.awt.Dimension(250, 220));
        itsDataPanel.setLayout(new javax.swing.BoxLayout(itsDataPanel, javax.swing.BoxLayout.Y_AXIS));

        itsInputFileTypePanel.setBackground(new java.awt.Color(255, 255, 255));
        itsInputFileTypePanel.setMaximumSize(new java.awt.Dimension(32767, 25));
        itsInputFileTypePanel.setMinimumSize(new java.awt.Dimension(160, 25));
        itsInputFileTypePanel.setPreferredSize(new java.awt.Dimension(160, 25));
        itsInputFileTypePanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsDataType, org.openide.util.NbBundle.getMessage(CompoundCollectorWindowsTopComponent.class, "CompoundCollectorWindowsTopComponent.itsDataType.text")); // NOI18N
        itsInputFileTypePanel.add(itsDataType, java.awt.BorderLayout.WEST);

        itsInputMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        itsInputMethodComboBox.setMinimumSize(new java.awt.Dimension(160, 21));
        itsInputMethodComboBox.setPreferredSize(new java.awt.Dimension(160, 21));
        itsInputFileTypePanel.add(itsInputMethodComboBox, java.awt.BorderLayout.EAST);

        itsDataPanel.add(itsInputFileTypePanel);
        itsDataPanel.add(filler1);

        itsDataCountPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsDataCountPanel.setMaximumSize(new java.awt.Dimension(32767, 25));
        itsDataCountPanel.setMinimumSize(new java.awt.Dimension(160, 25));
        itsDataCountPanel.setPreferredSize(new java.awt.Dimension(160, 25));
        itsDataCountPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsDataCountLabel, org.openide.util.NbBundle.getMessage(CompoundCollectorWindowsTopComponent.class, "CompoundCollectorWindowsTopComponent.itsDataCountLabel.text")); // NOI18N
        itsDataCountPanel.add(itsDataCountLabel, java.awt.BorderLayout.WEST);

        itsDataPanel.add(itsDataCountPanel);
        itsDataPanel.add(filler2);

        itsExcelOpenPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsExcelOpenPanel.setMaximumSize(new java.awt.Dimension(65634, 30));
        itsExcelOpenPanel.setMinimumSize(new java.awt.Dimension(250, 30));
        itsExcelOpenPanel.setPreferredSize(new java.awt.Dimension(250, 30));
        itsExcelOpenPanel.setLayout(new java.awt.BorderLayout());

        itsExcelOpenLeftPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsExcelOpenLeftPanel.setLayout(new javax.swing.BoxLayout(itsExcelOpenLeftPanel, javax.swing.BoxLayout.X_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(itsExcelPathNameLabel, org.openide.util.NbBundle.getMessage(CompoundCollectorWindowsTopComponent.class, "CompoundCollectorWindowsTopComponent.itsExcelPathNameLabel.text")); // NOI18N
        itsExcelOpenLeftPanel.add(itsExcelPathNameLabel);
        itsExcelOpenLeftPanel.add(filler8);

        itsExcelOpenPanel.add(itsExcelOpenLeftPanel, java.awt.BorderLayout.WEST);

        itsExcelOpenCenterPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsExcelOpenCenterPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(itsExcelPathLabel, org.openide.util.NbBundle.getMessage(CompoundCollectorWindowsTopComponent.class, "CompoundCollectorWindowsTopComponent.itsExcelPathLabel.text")); // NOI18N
        itsExcelOpenCenterPanel.add(itsExcelPathLabel, java.awt.BorderLayout.CENTER);

        itsExcelOpenPanel.add(itsExcelOpenCenterPanel, java.awt.BorderLayout.CENTER);

        itsExcelOpenRightPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsExcelOpenRightPanel.setLayout(new javax.swing.BoxLayout(itsExcelOpenRightPanel, javax.swing.BoxLayout.X_AXIS));
        itsExcelOpenRightPanel.add(filler9);

        org.openide.awt.Mnemonics.setLocalizedText(itsExcelOpenButton, org.openide.util.NbBundle.getMessage(CompoundCollectorWindowsTopComponent.class, "CompoundCollectorWindowsTopComponent.itsExcelOpenButton.text")); // NOI18N
        itsExcelOpenButton.setMaximumSize(new java.awt.Dimension(100, 23));
        itsExcelOpenButton.setMinimumSize(new java.awt.Dimension(100, 23));
        itsExcelOpenButton.setPreferredSize(new java.awt.Dimension(100, 23));
        itsExcelOpenRightPanel.add(itsExcelOpenButton);

        itsExcelOpenPanel.add(itsExcelOpenRightPanel, java.awt.BorderLayout.EAST);

        itsDataPanel.add(itsExcelOpenPanel);
        itsDataPanel.add(filler5);

        itsInputDataScrollPane.setMaximumSize(new java.awt.Dimension(32767, 100));
        itsInputDataScrollPane.setMinimumSize(new java.awt.Dimension(23, 100));
        itsInputDataScrollPane.setPreferredSize(new java.awt.Dimension(2, 100));
        itsDataPanel.add(itsInputDataScrollPane);

        itsTotalPanel.add(itsDataPanel);

        itsCalculateButtonPanel.setBackground(new java.awt.Color(255, 255, 255));
        itsCalculateButtonPanel.setMaximumSize(new java.awt.Dimension(65634, 30));
        itsCalculateButtonPanel.setMinimumSize(new java.awt.Dimension(250, 30));
        itsCalculateButtonPanel.setPreferredSize(new java.awt.Dimension(250, 30));
        itsCalculateButtonPanel.setLayout(new javax.swing.BoxLayout(itsCalculateButtonPanel, javax.swing.BoxLayout.LINE_AXIS));
        itsCalculateButtonPanel.add(filler3);

        org.openide.awt.Mnemonics.setLocalizedText(itsCalculateButton, org.openide.util.NbBundle.getMessage(CompoundCollectorWindowsTopComponent.class, "CompoundCollectorWindowsTopComponent.itsCalculateButton.text")); // NOI18N
        itsCalculateButton.setMaximumSize(new java.awt.Dimension(100, 23));
        itsCalculateButton.setMinimumSize(new java.awt.Dimension(100, 23));
        itsCalculateButton.setPreferredSize(new java.awt.Dimension(100, 23));
        itsCalculateButtonPanel.add(itsCalculateButton);
        itsCalculateButtonPanel.add(filler4);

        itsTotalPanel.add(itsCalculateButtonPanel);

        add(itsTotalPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JButton itsCalculateButton;
    private javax.swing.JPanel itsCalculateButtonPanel;
    private javax.swing.JLabel itsDataCountLabel;
    private javax.swing.JPanel itsDataCountPanel;
    private javax.swing.JPanel itsDataPanel;
    private javax.swing.JLabel itsDataType;
    private javax.swing.JButton itsExcelOpenButton;
    private javax.swing.JPanel itsExcelOpenCenterPanel;
    private javax.swing.JPanel itsExcelOpenLeftPanel;
    private javax.swing.JPanel itsExcelOpenPanel;
    private javax.swing.JPanel itsExcelOpenRightPanel;
    private javax.swing.JLabel itsExcelPathLabel;
    private javax.swing.JLabel itsExcelPathNameLabel;
    private javax.swing.JScrollPane itsInputDataScrollPane;
    private javax.swing.JPanel itsInputFileTypePanel;
    private javax.swing.JComboBox<String> itsInputMethodComboBox;
    private javax.swing.JComboBox<String> itsInputTypeComboBox;
    private javax.swing.JLabel itsInputTypeLabel;
    private javax.swing.JPanel itsInputTypePanel;
    private javax.swing.JPanel itsTotalPanel;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
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

    private void __registerKeyActionInInputTable() {
        TableActionListener theTableActionListener = new TableActionListener(this);

        KeyStroke theCopy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
        KeyStroke thePaste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        KeyStroke theEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

        this.itsInputTable.registerKeyboardAction(theTableActionListener, "Copy", theCopy, JComponent.WHEN_FOCUSED);
        this.itsInputTable.registerKeyboardAction(theTableActionListener, "Paste", thePaste, JComponent.WHEN_FOCUSED);

        this.itsDataCountTextField.registerKeyboardAction(new TextFieldActionListener(this.itsDataCountTextField), "Enter", theEnter, JComponent.WHEN_FOCUSED);
    }

    public List<String> getInputDataList() {
        List<String> theInputDataList = new ArrayList<>();

        this.itsInputTable.updateUI();

        for (int ri = 0, rEnd = this.itsInputTable.getRowCount(); ri < rEnd; ri++) {
            if (this.itsInputTable.getValueAt(ri, 0) != null && !this.itsInputTable.getValueAt(ri, 0).toString().isEmpty()) {
                theInputDataList.add(this.itsInputTable.getValueAt(ri, 0).toString());
            }
        }

        return theInputDataList;
    }

    public List<CompoundCollector.Pubchem_Output_Form> getOutputFormList() {
        List<CompoundCollector.Pubchem_Output_Form> theOutputFormList = new ArrayList<>();
        int theIndex = 0;

        for (JCheckBox theCheckBox : this.itsOutputFormCheckBoxList) {
            if (theCheckBox.isSelected()) {
                theOutputFormList.add(CompoundCollector.Pubchem_Output_Form.values()[theIndex]);
            }

            theIndex++;
        }

        return theOutputFormList;
    }

    private class DataCountFocusListener extends FocusAdapter implements Serializable {

        private static final long serialVersionUID = 9079087754031912329L;

        private CompoundCollectorInputTable itsTable;

        public DataCountFocusListener(CompoundCollectorInputTable theTable) {
            this.itsTable = theTable;
        }

        @Override
        public void focusLost(FocusEvent fe) {
            String theValue = ((MemorialTextField) fe.getComponent()).getText();

            this.itsTable.changeRow(Integer.parseInt(theValue));
        }
    }

    private class TableFocusListener extends FocusAdapter {

        private CompoundCollectorWindowsTopComponent itsTopComponent;

        public TableFocusListener(CompoundCollectorWindowsTopComponent itsTopComponent) {
            this.itsTopComponent = itsTopComponent;
        }

        @Override
        public void focusLost(FocusEvent fe) {
            this.itsTopComponent.getInputTable().revalidate();
        }

    }

    private class TableActionListener implements ActionListener {

        private CompoundCollectorWindowsTopComponent itsTopComponent;

        public TableActionListener(CompoundCollectorWindowsTopComponent theTopComponent) {
            this.itsTopComponent = theTopComponent;
        }

        @Override
        public void actionPerformed(ActionEvent theEvent) {
            switch (theEvent.getActionCommand()) {
                case "Copy":
                    this.__copy();
                    break;
                case "Paste":
                    this.__paste();
                    break;
                default:
            }
        }

        private void __copy() {
            CompoundCollectorInputTable theInputTable = this.itsTopComponent.getInputTable();
            StringBuilder theStringBuilder = new StringBuilder();

            for (int ri = 0, rEnd = theInputTable.getRowCount(); ri < rEnd; ri++) {
                theStringBuilder.append(theInputTable.getValueAt(ri, 0).toString()).append("\n");
            }

            StringSelection theStringSelection = new StringSelection(theStringBuilder.toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(theStringSelection, theStringSelection);
        }

        private void __paste() {
            try {
                CompoundCollectorInputTable theInputTable = this.itsTopComponent.getInputTable();
                String theString = (String) (Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this).getTransferData(DataFlavor.stringFlavor));
                StringTokenizer theStringTokenizer = new StringTokenizer(theString, "\n");

                for (int ri = theInputTable.getSelectedRow() == -1 ? 0 : theInputTable.getSelectedRow(); theStringTokenizer.hasMoreTokens(); ri++) {
                    String theValue = theStringTokenizer.nextToken();

                    if (ri == theInputTable.getRowCount()) {
                        theInputTable.addRow();
                    }

                    theInputTable.setValueAt(theValue, ri, 0);
                }

                this.itsTopComponent.getDataCountTextField().setText(new Integer(theInputTable.getRowCount()).toString());
                this.itsTopComponent.revalidate();
            } catch (Exception ex) {

            }
        }
    }

    private class CalculateButtonListener implements ActionListener {

        private CompoundCollectorWindowsTopComponent itsTopComponent;

        public CalculateButtonListener(CompoundCollectorWindowsTopComponent theTopComponent) {
            this.itsTopComponent = theTopComponent;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
//            this.itsTopComponent.getInputTable().getCellEditor().stopCellEditing();
            
            String theInputDataType = CompoundCollector.Pubchem_Compound_Domain.values()[this.itsTopComponent.getInputTypeComboBox().getSelectedIndex()].name().toLowerCase();
            MoleculeTableTopComponent theTableTopComponent = new MoleculeTableTopComponent("Pubchem_Search_Result");
            MoleculeStructureViewerTopComponent theViewerTopComponent = new MoleculeStructureViewerTopComponent(theTableTopComponent);
            CompoundCollector theCollector = new CompoundCollector();

            this.__setWindows(theViewerTopComponent, theTableTopComponent);

            theCollector.setVariable(this.itsTopComponent.getInputDataList(), theInputDataType, this.itsTopComponent.getOutputFormList(), theTableTopComponent, theViewerTopComponent);
            theCollector.actionPerformed(ae);
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

    private class TextFieldActionListener implements ActionListener {

        private MemorialTextField itsTextField;

        public TextFieldActionListener(MemorialTextField theTextField) {
            this.itsTextField = theTextField;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            this.itsTextField.transferFocus();
        }

    }

    private class InputExcelButtonActionListner implements ActionListener {

        private CompoundCollectorWindowsTopComponent itsTopComponent;
        private ExcelFileChooser itsExcelFileChooser;

        public InputExcelButtonActionListner(CompoundCollectorWindowsTopComponent itsTopComponent) {
            this.itsTopComponent = itsTopComponent;
            this.itsExcelFileChooser = new ExcelFileChooser("Compound_Collector_Input_Excel");
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            File theSelectedFile = this.itsExcelFileChooser.getSelectedExcelFile();

            if (theSelectedFile != null) {
                List<String> theInformationList = this.__readInformation(theSelectedFile);
                TwoDimensionList<String> theDataList = new TwoDimensionList<>();

                theDataList.add(theInformationList);

                this.itsTopComponent.getInputTable().setData(theDataList.transposeMatrix());
                this.itsTopComponent.getExcelPathLabel().setText(theSelectedFile.getAbsolutePath());
                
                this.itsTopComponent.getInputTable().revalidate();
                this.itsTopComponent.getExcelPathLabel().revalidate();
            }
        }

        private List<String> __readInformation(File theSelectedExcelFile) {
            try {
                TwoDimensionList<String> theData2dList = XlsxReader.read(theSelectedExcelFile);
                List<String> theInformationList = new ArrayList<>();

                for (List<String> theDataList : theData2dList) {
                    for (String theData : theDataList) {
                        if (theData != null && !theData.isEmpty()) {
                            theInformationList.add(theData);
                        }
                    }
                }

                return theInformationList;
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

            return null;
        }
    }

    private class InputMethodComboBoxItemListener implements ActionListener {

        private CompoundCollectorWindowsTopComponent itsTopComponent;

        public InputMethodComboBoxItemListener(CompoundCollectorWindowsTopComponent itsTopComponent) {
            this.itsTopComponent = itsTopComponent;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            String theInputMethod = this.itsTopComponent.getInputMethodComboBox().getSelectedItem().toString();

            if (theInputMethod.equals(InputMethods.Direct.name())) {
                this.itsTopComponent.getDataCountPanel().setVisible(true);
                this.itsTopComponent.getExcelOpenPanel().setVisible(false);
            } else if (theInputMethod.equals(InputMethods.Excel.name())) {
                this.itsTopComponent.getDataCountPanel().setVisible(false);
                this.itsTopComponent.getExcelOpenPanel().setVisible(true);
            }

            this.itsTopComponent.getDataCountPanel().revalidate();
            this.itsTopComponent.getExcelOpenPanel().revalidate();
        }
    }
}
