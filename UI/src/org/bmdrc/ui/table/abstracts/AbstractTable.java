package org.bmdrc.ui.table.abstracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.bmdrc.ui.util.TwoDimensionList;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public abstract class AbstractTable extends JTable implements Serializable {

    private static final long serialVersionUID = 2788163436316531734L;

    protected DefaultTableModel itsTableModel;
    protected String[] itsColumnHeaders;
    
    public AbstractTable(String... theColumnHeaders) {
        this(0, theColumnHeaders);
    }
    
    public AbstractTable(int theDefaultRowCount, String... theColumnHeaders) {
        super(theDefaultRowCount, theColumnHeaders.length);
        this.itsColumnHeaders = theColumnHeaders;
        this.itsTableModel = new DefaultTableModel(new Object[theDefaultRowCount][theColumnHeaders.length], theColumnHeaders);
        
        this.setModel(this.itsTableModel);
    }

    public String[] getColumnHeaders() {
        return itsColumnHeaders;
    }

    public void setColumnHeaders(String[] theColumnHeaders) {
        this.itsColumnHeaders = theColumnHeaders;
    }
    
    public void addRow(Object[] theObjects) {
        this.itsTableModel.addRow(theObjects);
    }
    
    public void addRow(List theObject) {
        this.itsTableModel.addRow(theObject.toArray(new Object[theObject.size()]));
    }
    
    public void removeRow(int theIndex) {
        this.itsTableModel.removeRow(theIndex);
    }
    
    public void setData(TwoDimensionList theDataList) {
        this.itsTableModel.setDataVector(theDataList.to2dArray(new String[theDataList.size()][theDataList.getMaximumNumberOfColumn()]), this.itsColumnHeaders);
    }
    
    public void setData(TwoDimensionList theDataList, List<String> theColumnHeaderList) {
        this.itsColumnHeaders = theColumnHeaderList.toArray(new String[theColumnHeaderList.size()]);
        
        this.setData(theDataList);
    }
    
    public TwoDimensionList<String> getData() {
        TwoDimensionList<String> theData2dList = new TwoDimensionList<String>();
        List<String> theColumnHeaderList = new ArrayList<String>();
        
        Collections.addAll(theColumnHeaderList, this.itsColumnHeaders);
        
        theData2dList.add(theColumnHeaderList);
        
        for(int ri = 0, rEnd = this.getRowCount(); ri < rEnd; ri++) {
            List<String> theValueList = new ArrayList<>();
            
            for(int ci = 0, cEnd = this.getColumnCount(); ci < cEnd; ci++) {
                theValueList.add(this.getValueAt(ri, ci).toString());
            }
            
            theData2dList.add(theValueList);
        }
        
        return theData2dList;
    }
}
