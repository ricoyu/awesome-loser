package com.loserico.excel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XlsxFileTest {
	
	public static void main(String[] args) throws IOException {
		XlsxFileTest xlsxFileTest = new XlsxFileTest();
		xlsxFileTest.updateXlsx();
	}

	public void updateXlsx() throws IOException {
		FileInputStream fis = new FileInputStream(new File("D:\\test.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row1 = sheet.createRow(1);
		XSSFCell cell1 = row1.createCell(1);
		cell1.setCellValue("Mahesh");
		XSSFRow row2 = sheet.createRow(2);
		XSSFCell cell2 = row2.createCell(1);
		cell2.setCellValue("Ramesh");
		fis.close();
		
		FileOutputStream fos = new FileOutputStream(new File("D:\\test.xlsx"));
		workbook.write(fos);
		fos.close();
		System.out.println("Done");
	}
}
