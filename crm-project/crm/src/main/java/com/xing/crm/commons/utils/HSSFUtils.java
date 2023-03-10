package com.xing.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFUtils {

    public static String getCellValueForStr(HSSFCell cell){
        String ret = "";
        switch (cell.getCellType()){
            case HSSFCell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret = cell.getNumericCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret = cell.getBooleanCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                ret = cell.getCellFormula() + "";
                break;
        }
        return ret;
    }
}
