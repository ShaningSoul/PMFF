package org.bmdrc.ui.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bmdrc.ui.util.TwoDimensionList;

/**
 *
 * @author Sungbo Hwang <tyamazaki@naver.com / sbhwang@bmdrc.org>
 */
public class XlsxReader implements Serializable {

    private static final long serialVersionUID = -5312211952681263180L;

    public static TwoDimensionList<String> read(File theFile) throws IOException {
        return XlsxReader.read(theFile.toString());
    }

    public static TwoDimensionList<String> read(String theFilePath, String theSheetName) throws IOException {
        TwoDimensionList<String> theData2dList = new TwoDimensionList<>();
        FileInputStream theFileInputStream = new FileInputStream(theFilePath);
        XSSFWorkbook theWorkBook = new XSSFWorkbook(theFileInputStream);
        XSSFSheet theSheet = theWorkBook.getSheet(theSheetName);

        for (int ri = 0, rEnd = theSheet.getPhysicalNumberOfRows(); ri < rEnd; ri++) {
            XSSFRow theRow = theSheet.getRow(ri);
            List<String> theDataList = new ArrayList<>();

            if (theRow != null) {
                for (int ci = 0, cEnd = theRow.getPhysicalNumberOfCells(); ci < cEnd; ci++) {
                    XSSFCell theCell = theRow.getCell(ci);
                    
                    if(theCell == null) {
                        theDataList.add(new String());
                    } else {
                        theDataList.add(XlsxReader.__read(theCell));
                    }
                }
            }
            
            theData2dList.add(theDataList);
        }

        return theData2dList;
    }
    
    public static TwoDimensionList<String> read(String theFilePath) throws IOException {
        TwoDimensionList<String> theData2dList = new TwoDimensionList<>();
        FileInputStream theFileInputStream = new FileInputStream(theFilePath);
        XSSFWorkbook theWorkBook = new XSSFWorkbook(theFileInputStream);
        XSSFSheet theSheet = theWorkBook.getSheetAt(0);

        for (int ri = 0, rEnd = theSheet.getPhysicalNumberOfRows(); ri < rEnd; ri++) {
            XSSFRow theRow = theSheet.getRow(ri);
            List<String> theDataList = new ArrayList<>();

            if (theRow != null) {
                for (int ci = 0, cEnd = theRow.getPhysicalNumberOfCells(); ci < cEnd; ci++) {
                    XSSFCell theCell = theRow.getCell(ci);
                    
                    if(theCell == null) {
                        theDataList.add(new String());
                    } else {
                        theDataList.add(XlsxReader.__read(theCell));
                    }
                }
            }
            
            theData2dList.add(theDataList);
        }

        return theData2dList;
    }

    private static String __read(XSSFCell theCell) {
        switch (theCell.getCellType()) {
            case XSSFCell.CELL_TYPE_FORMULA:
                return theCell.getCellFormula();
            case XSSFCell.CELL_TYPE_NUMERIC:
                return theCell.getNumericCellValue() + "";
            case XSSFCell.CELL_TYPE_STRING:
                return theCell.getStringCellValue() + "";
            case XSSFCell.CELL_TYPE_BLANK:
                return theCell.getBooleanCellValue() + "";
            case XSSFCell.CELL_TYPE_ERROR:
                return theCell.getErrorCellValue() + "";
            default:
                return null;
        }
    }
}
