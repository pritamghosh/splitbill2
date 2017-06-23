package util;


import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dto.Item;
import dto.MemberAmount;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
 
/**
 * A very simple program that writes some data to an Excel file
 * using the Apache POI library.
 * @author www.codejava.net
 *
 */
public class SplitBillUtil {
	
	public static void setNumericTrue(TextField textField, String newValue) {
		if (!newValue.matches("\\d*")) {
			textField.setText(newValue.replaceAll("[^\\d|.]", ""));
		}
	}

	public static void export(ObservableList<TableColumn<Item, ?>> columns, ObservableList<Item> items, String id)
			throws IOException {
		if(items.isEmpty()){
			return;
		}
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(id);

		XSSFFont headingfont = workbook.createFont();
		headingfont.setFontHeight(12);
		headingfont.setFontName("Calibri");
		headingfont.setBold(true);
		headingfont.setColor(new XSSFColor(Color.BLACK));

		XSSFCellStyle headingStyle = workbook.createCellStyle();
		headingStyle.setFont(headingfont);
		headingStyle.setBorderLeft(BorderStyle.THIN);
		headingStyle.setBorderRight(BorderStyle.THIN);
		headingStyle.setBorderBottom(BorderStyle.THIN);
		headingStyle.setBorderTop(BorderStyle.THIN);
		headingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headingStyle.setFillForegroundColor(new XSSFColor(new Color(237, 234, 234)));

		int rowCount = 0;
		Row row = sheet.createRow(rowCount++);
		int columnCount = 0;
		ArrayList<String> indexArray = new ArrayList<String>();
		for (TableColumn<Item, ?> tableColumn : columns) {
			Cell cell = row.createCell(columnCount);
			cell.setCellValue(tableColumn.getText());
			cell.setCellStyle(headingStyle);
			indexArray.add(columnCount++, tableColumn.getText());
		}
		
		for (Item item : items) {
			columnCount = 0;
			row = sheet.createRow(rowCount++);
			Cell cell = row.createCell(indexArray.indexOf(SpliBillConstant.ITEM));
			cell.setCellValue(item.getItemName());
			cell.setCellStyle(headingStyle);
			for (MemberAmount memberAmount : item.getMemberAmountList()) {
				cell = row.createCell(indexArray.indexOf(memberAmount.getMemberName()));
				cell.setCellValue(memberAmount.getAmount());
				cell.setCellStyle(headingStyle);
			}
			cell = row.createCell(indexArray.indexOf(SpliBillConstant.ACTUAL_TOTAL));
			cell.setCellValue(item.getActualTotal());
			cell.setCellStyle(headingStyle);
			cell = row.createCell(indexArray.indexOf(SpliBillConstant.CALCULATED_TOTAL));
			cell.setCellValue(item.getCalculatedtotal());
			cell.setCellStyle(headingStyle);

		}
		for (int i = 0; i < indexArray.size(); i++) {
			sheet.autoSizeColumn(i);
		}

		boolean createFlightStatus = createFlight(id, workbook);
		int i = 1;
		while (!createFlightStatus) {
			createFlightStatus = createFlight(id + String.valueOf(i++), workbook);
		}
	}

	private static boolean createFlight(String id, XSSFWorkbook workbook) throws IOException {
		String dirName = "G:\\Excel\\";
		File invoiceDir = new File(dirName);
		invoiceDir.mkdirs();
		if (invoiceDir.exists()) {
			try (FileOutputStream outputStream = new FileOutputStream(id + ".xlsx")) {
				Thread.sleep(2000);
				workbook.write(outputStream);
				return true;
			} catch (FileNotFoundException|InterruptedException e) {
				return false;
			} 
		}
		return false;
	}

}
