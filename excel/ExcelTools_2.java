package tools;

import common.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class ExcelTools_2 {
	public static String		filePath	= "D:\\";
	public static String		fileName	= "order_info.xlsx";
	public static XSSFWorkbook	workbook;
	public static XSSFSheet		sheet;
	public static String		sql			= "insert into INS.UNION_ORDER_IMPORT(ID,PROD_ID,PROD_NAME,INS_COMPANY_ID,INS_COMPANY_NAME,"
			+ "START_TIME,APP_NAME,"
			+ "ACCEPT_DATE,POLICY_NO,APP_PRICE,UNION_LOGIN_NAME,SERVICE_FEE_RATE,SERVICE_FEE,HANDING_FEE_RATE,HANDING_FEE,RISK_TYPE,ADDER_NO,"
			+ "UPDATER_NO,ADDER_NAME,UPDATER_NAME,ADD_TIME,UPDATE_TIME)"
			+ "values (INS.S_UNION_ORDER_IMPORT.NEXTVAL,'#prodId#','#prodName#',#insCompanyId#,'#insCompanyName#',#startTime#,'#appName#',"
			+ "#acceptDate#,'#policyNo#',"
			+ "#appPrice#,'#unionLoginName#',#serviceFeeRate#,#serviceFee#,#handingFeeRate#,#handingFee#,#riskType#,-1,-1,'admin','admin',sysdate,sysdate);";

	static {
		try {
			workbook = new XSSFWorkbook(new FileInputStream(filePath + fileName));
			sheet = workbook.getSheetAt(4);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		int startRow = 1;
		int endRow = 5980;
		int index = 1;

		OutputStreamWriter writer = null;
		// FileWriter fileWriter = new FileWriter(filePath + outFileName);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat dfHm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (int i = startRow; i < endRow; i++) {
			XSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			if (writer == null) {
				String outFileName = "团险_" + index + ".sql";
				writer = new OutputStreamWriter(new FileOutputStream(filePath + outFileName), "GBK");
			}
			String prodId = ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 0));
			String prodName = ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 1));
			String insCompanyId = ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 2));
			String insCompanyName = ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 3));
			String startTime = null;
			XSSFCell cell = row.getCell(7);
			if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				startTime = "TO_DATE('" + df.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()))
						+ "' , 'yyyy/MM/dd')";
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
				startTime = "TO_DATE('" + ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 7)) + "' , 'yyyy/MM/dd')";
			}
			String appName = ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 9));
            String acceptDate = null;
            cell = row.getCell(6);
            if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                acceptDate = "TO_DATE('" + df.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()))
                        + "' , 'yyyy/MM/dd')";
            } else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                acceptDate = "TO_DATE('" + ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 6)) + "' , 'yyyy/MM/dd')";
            }
			String policyNo = ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 4));
			String appPrice = new BigDecimal(ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 5)))
					.multiply(new BigDecimal(1)).setScale(3).toString();
			String unionLoginName = ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 10));
			String serviceFeeRate = new BigDecimal(ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 11)))
					.multiply(new BigDecimal(100)).setScale(2).toString();
			String serviceFee = new BigDecimal(ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 12)))
					.multiply(new BigDecimal(1)).setScale(4).toString();
			String handingFeeRate = new BigDecimal(ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 13)))
					.multiply(new BigDecimal(100)).setScale(2).toString();
			String handingFee = new BigDecimal(ExcelUtils.trimToZero(ExcelUtils.getCellValue(row, 14)))
					.multiply(new BigDecimal(1)).setScale(4).toString();
			String riskType = "4";
			String result = sql.replace("#prodId#", prodId);
			result = result.replace("#prodName#", prodName);
			result = result.replace("#insCompanyId#", insCompanyId);
			result = result.replace("#insCompanyName#", insCompanyName);
			result = result.replace("#startTime#", startTime);
			result = result.replace("#appName#", appName);
			result = result.replace("#acceptDate#", acceptDate);
			result = result.replace("#policyNo#", policyNo);
			result = result.replace("#appPrice#", appPrice);
			result = result.replace("#unionLoginName#", unionLoginName);
			result = result.replace("#serviceFeeRate#", serviceFeeRate);
			result = result.replace("#serviceFee#", serviceFee);
			result = result.replace("#handingFeeRate#", handingFeeRate);
			result = result.replace("#handingFee#", handingFee);
			result = result.replace("#riskType#", riskType);
			System.out.println(i + "result is : " + result);
			writer.write(result);
			writer.write("\r\n");
			writer.flush();
			if (i % 500 == 0) {
				writer.write("commit;");
				writer.write("\r\n");
				writer.flush();
			}
			// if(i%5000 == 0){
			// index ++;
			// writer.close();
			// writer = null;
			// }
		}
		writer.write("commit;");
		writer.close();
		System.out.println("convert success !!!!");
	}

}
