package org.bmdrc.basicchemistry.table;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.bmdrc.basicchemistry.reader.SDFReader;
import org.bmdrc.basicchemistry.table.enums.MoleculeTableColumnName;
import org.bmdrc.basicchemistry.windows.MoleculeTableTopComponent;
import org.bmdrc.ui.abstracts.Constant;
import org.bmdrc.ui.table.abstracts.AbstractTable;
import org.bmdrc.ui.util.TwoDimensionList;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 * @author Sungbo Hwang <sbhwang@bmdrc.org/tyamazaki@naver.com>
 */
public class MoleculeTable extends AbstractTable implements Serializable {

    private static final long serialVersionUID = 8038095477821223748L;

    private MoleculeTableTopComponent itsTopComponent;
    private IAtomContainerSet itsMoleculeSet;
    private File itsMoleculeFile;

    public MoleculeTable(MoleculeTableTopComponent theTopComponent) {
        this(null, theTopComponent);
    }

    public MoleculeTable(IAtomContainerSet theMoleculeSet) {
        super(MoleculeTableColumnName.getBasicMoleculeTableColumnNames());
        this.addMouseListener(new MoleculeTableListener(this));
    }

    public MoleculeTable(File theMoleculeFile, MoleculeTableTopComponent theTopComponent) {
        super(MoleculeTableColumnName.getBasicMoleculeTableColumnNames());

        this.itsMoleculeFile = theMoleculeFile;
        this.itsTopComponent = theTopComponent;

        this.addMouseListener(new MoleculeTableListener(this));
        this.addKeyListener(new MoleculeTableListener(this));

        this.__setHorizontalAlign();
        this.setRowSorter(new MoleculeTableRowSorter(this.itsTableModel));
        this.setGridColor(Color.LIGHT_GRAY);
    }

    private void __setHorizontalAlign() {
        DefaultTableCellRenderer theCenterRenderer = new DefaultTableCellRenderer();

        theCenterRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int ci = 0/*MoleculeTableColumnName.getBasicMoleculeTableColumnNames().length*/, cEnd = this.getColumnModel().getColumnCount(); ci < cEnd; ci++) {
            this.getColumnModel().getColumn(ci).setCellRenderer(theCenterRenderer);
        }

        this.getTableHeader().setDefaultRenderer(theCenterRenderer);
    }

    public IAtomContainerSet getMoleculeSet() {
        return itsMoleculeSet;
    }

    public void setMoleculeSet(IAtomContainerSet theMoleculeSet) {
        this.itsMoleculeSet = theMoleculeSet;

        if (theMoleculeSet != null) {
            this.setData(theMoleculeSet);
        }
    }

    public File getMoleculeFile() {
        return itsMoleculeFile;
    }

    public void setMoleculeFile(File theMoleculeFile) {
        this.itsMoleculeFile = theMoleculeFile;
        this.setMoleculeSet(SDFReader.openMoleculeFile(theMoleculeFile));
    }

    public MoleculeTableTopComponent getTopComponent() {
        return itsTopComponent;
    }

    public void addData(String... theAddedColumnNames) {
        this.addData(this.itsMoleculeSet, theAddedColumnNames);
    }

    public void addData(IAtomContainerSet theMoleculeSet, String... theAddedColumnNames) {
        List<String> theColumnHeaderList = new ArrayList<>();

        for (int ci = MoleculeTableColumnName.values().length, cEnd = this.itsColumnHeaders.length; ci < cEnd; ci++) {
            if (!theColumnHeaderList.contains(this.itsColumnHeaders[ci])) {
                theColumnHeaderList.add(this.itsColumnHeaders[ci]);
            }
        }

        for (String theAddedColumnName : theAddedColumnNames) {
            if (!theColumnHeaderList.contains(theAddedColumnName)) {
                theColumnHeaderList.add(theAddedColumnName);
            }
        }

        this.setData(theMoleculeSet, theColumnHeaderList.toArray(new String[theColumnHeaderList.size()]));
    }

    public void setData(IAtomContainerSet theMoleculeSet, String... theColumns) {
        this.itsMoleculeSet = theMoleculeSet;

        this.setData(theColumns);
    }

    public void setData(String... theColumns) {
        TwoDimensionList<String> theData2dList = this.__getData2dList(theColumns);
        List<String> theColumnHeaderList = new ArrayList<String>();

        Collections.addAll(theColumnHeaderList, MoleculeTableColumnName.getBasicMoleculeTableColumnNames());

        for (String theColumn : theColumns) {
            theColumnHeaderList.add(theColumn);
        }

        super.setData(theData2dList, theColumnHeaderList);
        this.__setHorizontalAlign();
    }

    public void setData(IAtomContainerSet theMoleculeSet) {
        this.setData(this.__getData2dList());
        this.__setHorizontalAlign();
    }
    
    @Override
    public void setData(TwoDimensionList theData2dList) {
        super.setData(theData2dList);
        this.__setHorizontalAlign();
    }

    private TwoDimensionList<String> __getData2dList(String... theColumns) {
        TwoDimensionList<String> theData2dList = new TwoDimensionList<String>();

        for (int mi = 0, mEnd = this.itsMoleculeSet.getAtomContainerCount(); mi < mEnd; mi++) {
            List<String> theDataList = new ArrayList<String>(MoleculeTableColumnName.getBasicMoleculeTableColumnNames().length);

            theDataList.add(new Integer(mi + 1).toString());
            theDataList.add(this.itsMoleculeSet.getAtomContainer(mi).getProperty(Constant.MOLECULE_NAME_KEY).toString());

            for (String theColumn : theColumns) {
                String theValue = this.itsMoleculeSet.getAtomContainer(mi).getProperty(theColumn).toString();

                theDataList.add(theValue.toString());
            }

            theData2dList.add(theDataList);
        }

        return theData2dList;
    }

    private boolean __isDouble(String theValue) {
        try {
            Double.parseDouble(theValue);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}

class MoleculeTableListener extends MouseAdapter implements KeyListener, Serializable {

    private static final long serialVersionUID = 7155870041835006642L;

    private MoleculeTable itsTable;

    public MoleculeTableListener(MoleculeTable theTable) {
        this.itsTable = theTable;
    }

    private void __setMolecule() {
        int theSelectedIndex = this.itsTable.getSelectedRow();

        if (theSelectedIndex >= Constant.FIRST_INDEX) {
            this.itsTable.getTopComponent().getViewerTopComponent().viewMolecule(theSelectedIndex + 1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.__setMolecule();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.__setMolecule();
        }
    }
}

class MoleculeTableRowSorter extends TableRowSorter<DefaultTableModel> implements Serializable {

    private static final long serialVersionUID = 3357127215564413908L;

    public MoleculeTableRowSorter(DefaultTableModel model) {
        super(model);
    }

    @Override
    public Comparator<?> getComparator(int theColumnIndex) {
        Comparator theComparator = null;

        if (this.__getClass(theColumnIndex).equals(Integer.class)) {
            theComparator = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.compare(Integer.parseInt(o1), Integer.parseInt(o2));
                }
            };
        } else if (this.__getClass(theColumnIndex).equals(Double.class)) {
            theComparator = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Double.compare(Double.parseDouble(o1), Double.parseDouble(o2));
                }
            };
        } else {
            theComparator = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            };
        }

        return theComparator;
    }

    private Class<?> __getClass(int theColumnIndex) {
        String theValue = this.getModel().getValueAt(Constant.FIRST_INDEX, theColumnIndex).toString();

        try {
            Integer.parseInt(theValue);
            return Integer.class;
        } catch (Exception ex) {
        }

        try {
            Double.parseDouble(theValue);
            return Double.class;
        } catch (Exception ex) {
        }

        return String.class;
    }
}
