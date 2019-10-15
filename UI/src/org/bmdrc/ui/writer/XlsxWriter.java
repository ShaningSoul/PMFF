package org.bmdrc.ui.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bmdrc.ui.util.TwoDimensionList;

/**
 *
 * @author Sungbo Hwang (tyamazaki@naver.com)
 * @since 2016. 05. 01
 */
public class XlsxWriter {

    private static final String SUFFIX = "xlsx";

    public static <Type extends Object> void write(String theFilePath, TwoDimensionList<Type> theData2dList) {
        XSSFWorkbook theWorkBook = XlsxWriter.__getWorkBook(theData2dList);
        File theSaveFile = new File(theFilePath);
        String theSuffix = theSaveFile.getName().substring(theSaveFile.getName().length() - XlsxWriter.SUFFIX.length() - 1, theSaveFile.getName().length());

        if (!theSuffix.equals("." + XlsxWriter.SUFFIX)) {
            theFilePath += "." + XlsxWriter.SUFFIX;
        }
        // 해당 워크시트를 저장함.
        try {
            FileOutputStream theStream = new FileOutputStream(theFilePath);
            theWorkBook.write(theStream);
            theStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <Type extends Object> XSSFWorkbook __getWorkBook(TwoDimensionList<Type> theData2dList) {
        XSSFWorkbook theWorkBook = new XSSFWorkbook();
        Sheet theSheet = theWorkBook.createSheet();
        int theRowSize = theData2dList.size();
        int theColumnSize = theData2dList.getMaximumNumberOfColumn();

        for (int ri = 0; ri < theRowSize; ri++) {
            Row theRow = theSheet.createRow(ri);

            for (int ci = 0; ci < theColumnSize; ci++) {
                Cell theCell = theRow.createCell(ci);

                if (theData2dList.get(ri).size() > ci) {
                    if (theData2dList.get(ri, ci) instanceof String) {
                        theCell.setCellValue((String)theData2dList.get(ri, ci));
                    } else if(theData2dList.get(ri, ci) instanceof Double) {
                        theCell.setCellValue((Double)theData2dList.get(ri, ci));
                    } else {
                        System.err.println("Type Error");
                    }
                }
            }
        }

        return theWorkBook;
    }

    private static Class __canWriteClass(Class theClass) {
        if (!theClass.getName().equals("String") && !theClass.getName().equals("Double")) {
            return String.class;
        }

        return theClass;
    }
}
