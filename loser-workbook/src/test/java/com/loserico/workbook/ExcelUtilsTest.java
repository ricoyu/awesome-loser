package com.loserico.workbook;

import com.loserico.workbook.utils.ExcelUtils;
import com.loserico.workbook.utils.IOUtils;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

public class ExcelUtilsTest {

	@Test
	public void testWriteByCellPosi() throws Exception {
		File file = IOUtils.readClasspathFileAsFile("excel/ListMonthInvoiceEmailTemplate.xlsx");
		Workbook workbook = ExcelUtils.getWorkbook(file);
		Sheet sheet = workbook.getSheet("New Invoice - sample");
		ExcelUtils.writeCell(sheet, 0, 0, "September '18 INVOICE For Student 三少爷");
		
		Path path = ExcelUtils.write2TmpFile(workbook, "xlsx");
		System.out.println(path);
	}
	
	@Test
	public void testWrite2Template() {
		Rebate rebate1 = Rebate.builder()
				.businessTime(LocalDateTime.now())
				.amount(BigDecimal.valueOf(100L))
				.quantityStr("66")
				.unitPrice(BigDecimal.valueOf(1L))
				.billType("人民币")
				.goodsDesc("球鞋")
				.purchaseLotNo("0001")
				.buyTime(new Date())
				.goodsNo("球鞋001")
				.supplierNo("supplier001")
				.billNo("bill001")
				.build();
		Rebate rebate2 = Rebate.builder()
				.businessTime(LocalDateTime.of(2020, 11, 1, 11, 01))
				.amount(BigDecimal.valueOf(100L))
				.quantityStr("66")
				.unitPrice(BigDecimal.valueOf(1L))
				.billType("美金")
				.goodsDesc("笔记本电脑")
				.purchaseLotNo("0002")
				.buyTime(new Date())
				.goodsNo("笔记本电脑002")
				.supplierNo("supplier003")
				.billNo("bill003")
				.build();
		List<Rebate> rebates = asList(rebate1, rebate2);
		ExcelUtils.write2Excel("D:\\rebate_detail_template.xls", 0, rebates, "D:\\temp");
	}
	
	@Data
	@Builder
	private static class Rebate {
		
		private LocalDateTime businessTime;
		
		private BigDecimal amount;
		
		private String quantityStr;
		
		private BigDecimal unitPrice;
		
		private String billType;
		
		private String goodsDesc;
		
		private String purchaseLotNo;
		
		private Date buyTime;
		
		private String goodsNo;
		
		private String supplierNo;
		
		private String billNo;
		
	}
}
