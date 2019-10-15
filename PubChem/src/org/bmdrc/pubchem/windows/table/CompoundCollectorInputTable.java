package org.bmdrc.pubchem.windows.table;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.table.abstracts.AbstractTable;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class CompoundCollectorInputTable extends AbstractTable implements Serializable {

    private static final long serialVersionUID = -3881471974120763777L;

    private enum ColumnName {
        Input_Data("Input Data");

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

    private Class itsInputClass;
    private static String CLIP_BOARD_KEY = "Clip board using";

    public CompoundCollectorInputTable(Class theInptDataClass) {
        super(3, ColumnName.getColumnNames());

        this.itsInputClass = theInptDataClass;
        
        this.setGridColor(Color.LIGHT_GRAY);

        this.__setHorizontalAlign();
        this.__setHeaderColor();
    }

    public Class getInputClass() {
        return itsInputClass;
    }

    public void setInputClass(Class theInputClass) {
        this.itsInputClass = theInputClass;
    }

    private void __setHeaderColor() {
        Border theBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

        this.getTableHeader().setBackground(Color.LIGHT_GRAY);
        this.getTableHeader().setBorder(theBorder);
    }

    public void changeRow(Integer theRowCount) {
        DefaultTableModel theTableModel = (DefaultTableModel) this.getModel();

        theTableModel.setRowCount(theRowCount);

        this.setModel(theTableModel);
        this.revalidate();
    }

    
    @Override
    public void setValueAt(Object o, int i, int j) {
        String theString = o.toString();

        try {
            if (this.itsInputClass.equals(Integer.class)) {
                Integer.parseInt(theString);
            } else if (this.itsInputClass.equals(Double.class)) {
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

    public List<String> getDataList() {
        List<String> theDataList = new ArrayList<>();

        for (int ri = 0, rEnd = this.getRowCount(); ri < rEnd; ri++) {
            Object theValue = this.getValueAt(ri, Constant.FIRST_INDEX);

            if (theValue != null && theValue.toString().length() != 0) {
                theDataList.add(theValue.toString());
            }
        }

        return theDataList;
    }

    
}
