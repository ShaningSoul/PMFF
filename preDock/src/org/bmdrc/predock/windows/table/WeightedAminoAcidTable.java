package org.bmdrc.predock.windows.table;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import org.bmdrc.basicchemistry.protein.Protein;
import org.bmdrc.predock.variable.WeightAminoAcidInformation;
import org.bmdrc.predock.variable.WeightAminoAcidInformationList;
import org.bmdrc.ui.table.abstracts.AbstractTable;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class WeightedAminoAcidTable extends AbstractTable implements Serializable {

    private static final long serialVersionUID = 1692960904165146736L;

    private enum ColumnName {
        AminoAcid("Amino Acid Serial Number"),
        Weight("Weight");

        public final String NAME;

        private ColumnName(String NAME) {
            this.NAME = NAME;
        }

        public static String[] getColumnNames() {
            List<String> theColumnNameList = new ArrayList<>();

            for (ColumnName theName : ColumnName.values()) {
                theColumnNameList.add(theName.NAME);
            }

            return theColumnNameList.toArray(new String[theColumnNameList.size()]);
        }
    }

    private Protein itsProtein;

    public WeightedAminoAcidTable() {
        super(3, ColumnName.getColumnNames());

        this.setGridColor(Color.LIGHT_GRAY);
        this.__setHorizontalAlign();
    }

    @Override
    public void setValueAt(Object o, int i, int j) {
        String theString = o.toString();

        try {
            if (i == ColumnName.AminoAcid.ordinal()) {
                Integer.parseInt(theString);
            } else if(i == ColumnName.Weight.ordinal()) {
                Double.parseDouble(theString);
            }

            super.setValueAt(o, i, j);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Incorrect number format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void __setHorizontalAlign() {
        DefaultTableCellRenderer theCenterRenderer = new DefaultTableCellRenderer();

        theCenterRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int ci = 0, cEnd = this.getColumnModel().getColumnCount(); ci < cEnd; ci++) {
            this.getColumnModel().getColumn(ci).setCellRenderer(theCenterRenderer);
        }

        this.getTableHeader().setDefaultRenderer(theCenterRenderer);
    }

    public void addRow() {
        String[] theNewRow = new String[ColumnName.values().length];

        Arrays.fill(theNewRow, "");

        this.addRow(theNewRow);
    }

    public WeightAminoAcidInformationList getSerialNumberList() {
        WeightAminoAcidInformationList theSerialNumberList = new WeightAminoAcidInformationList();

        for (int ri = 0, rEnd = this.getRowCount(); ri < rEnd; ri++) {
            Object theValue = this.getValueAt(ri, ColumnName.AminoAcid.ordinal());

            if (theValue != null && theValue.toString().length() != 0) {
                theSerialNumberList.add(new WeightAminoAcidInformation(Integer.parseInt(this.getValueAt(ri, ColumnName.AminoAcid.ordinal()).toString()), 2.0));
            }
        }

        return theSerialNumberList;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }
}
