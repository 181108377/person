package common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.text.SimpleDateFormat;

public class ExcelUtils {
    public static String getCellValue(XSSFRow row, int cellNum){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        XSSFCell cell = row.getCell(cellNum);
        if(cell == null){
            return null;
        }
//        if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
//            return df.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
//        }
        cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        return StringUtils.trim(cell.getStringCellValue());
    }

    public static String trimToZero(String str){
        if(StringUtils.isBlank(str)){
            return "0";
        }
        return StringUtils.trim(str);
    }
}
