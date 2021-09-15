package com.loserico.workbook.utils;

import com.loserico.workbook.exception.CellNotFoundException;
import com.loserico.workbook.exception.ExcelOutputStreamWriteException;
import com.loserico.workbook.exception.InvalidConfigurationException;
import com.loserico.workbook.exception.InvalidVarTemplateException;
import com.loserico.workbook.exception.RowNotFoundException;
import com.loserico.workbook.exception.SheetNotExistException;
import com.loserico.workbook.exception.UnrecognizedWorkbookException;
import com.loserico.workbook.exception.WorkbookCreationException;
import com.loserico.workbook.marshal.CellWriter;
import com.loserico.workbook.marshal.VarInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.loserico.workbook.marshal.VarInfo.DT;
import static com.loserico.workbook.marshal.VarInfo.NUM;
import static com.loserico.workbook.marshal.VarInfo.STR;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 读写excel工具类
 * <p>
 * Copyright: Copyright (c) 2019-10-15 11:15
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ExcelUtils {
	/**
	 * 2003- 版本的excel
	 */
	private final static String excel2003L = ".xls";

	/**
	 * 2003- 版本的excel
	 */
	private final static String excel2003csv = ".csv";

	/**
	 * 2007+ 版本的excel
	 */
	private final static String excel2007U = ".xlsx";

	/**
	 * 去除包含cell内容的""
	 */
	private static Pattern pattern = Pattern.compile("\"(.+)\"");
	private static Pattern moneyDoublePattern = Pattern.compile("\\$(.+)");
	private static Pattern placeholderPattern = Pattern.compile("\\$\\{(.+)\\}([a-z]{0,1})");

	/**
	 * ${amount}n4- 注：n4保留小数点后4位, - 表示取负数
	 */
	private static Pattern precisionNumberPlaceholderPattern = Pattern.compile("\\$\\{(.+)\\}([n]{0,1})(\\d*)(-?)");
	/**
	 * ${businessTime}d{yyyy-M-d HH:mm} 匹配这种模式
	 */
	private static Pattern dateTimeShortPattern1 = Pattern.compile("\\$\\{(.+)\\}([a-z]{0,1})\\{(yyyy-M-d HH:mm)\\}");
	private static Pattern dateTimePattern = Pattern.compile("\\$\\{(.+)\\}([a-z]{0,1})\\{(yyyy-MM-dd HH:mm:ss)\\}");
	private static Pattern datePattern1 = Pattern.compile("\\$\\{(.+)\\}([a-z]{0,1})\\{(yyyy-MM-dd)\\}");

	/**
	 * 描述：根据文件后缀, 自适应上传文件的版本
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Workbook getWorkbook(MultipartFile file) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
			String fileName = file.getOriginalFilename();
			Workbook wb = null;
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			if (excel2003L.equalsIgnoreCase(fileType)) {
				wb = new HSSFWorkbook(bais); // 2003-
			} else if (excel2007U.equalsIgnoreCase(fileType)) {
				wb = new XSSFWorkbook(bais); // 2007+
			} else if (excel2003csv.equalsIgnoreCase(fileType)) {
				File xlsxFile = csvToXLS(bais);
				wb = new XSSFWorkbook(IOUtils.toByteArrayInputStream(xlsxFile)); // 2003-
			} else {
				throw new UnrecognizedWorkbookException("Unsupported file type");
			}
			return wb;
		} catch (Exception e) {
			log.error("msg", e);
			throw new WorkbookCreationException(e);
		}
	}

	/**
	 * 描述：根据文件后缀, 自适应上传文件的版本
	 *
	 * @param inStr,fileName
	 * @return
	 * @throws Exception
	 */
	public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (excel2003L.equals(fileType)) {
			wb = new HSSFWorkbook(inStr); // 2003-
		} else if (excel2007U.equals(fileType)) {
			wb = new XSSFWorkbook(inStr); // 2007+
		} else if (excel2003csv.equals(fileType)) {
			wb = new HSSFWorkbook(inStr); // 2003-
		} else {
			throw new Exception("解析的文件格式有误！");
		}
		return wb;
	}

	public static Workbook getWorkbook(File file) throws Exception {
		requireNonNull(file, "file不能为null");
		return getWorkbook(file.toPath());
	}

	public static Workbook getWorkbook(Path path) throws Exception {
		requireNonNull(path, "path不能为null");
		ByteArrayInputStream bais = new ByteArrayInputStream(Files.readAllBytes(path));
		Workbook wb = null;
		String fileName = path.getFileName().toString();
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (excel2003L.equalsIgnoreCase(fileType)) {
			wb = new HSSFWorkbook(bais); // 2003-
		} else if (excel2007U.equalsIgnoreCase(fileType)) {
			wb = new XSSFWorkbook(bais); // 2007+
		} else if (excel2003csv.equalsIgnoreCase(fileType)) {
			File xlsxFile = csvToXLS(bais);
			wb = new XSSFWorkbook(IOUtils.toByteArrayInputStream(xlsxFile)); // 2003-
		} else {
			throw new Exception("解析的文件格式有误！");
		}
		return wb;
	}

	/**
	 * 关闭Workbook同时关闭输入流或者File对象
	 *
	 * @param workbook
	 */
	public static void closeWorkbook(Workbook workbook) {
		if (workbook == null) {
			return;
		}

		try {
			workbook.close();
		} catch (IOException e) {
			log.error("关闭Workbook失败", e);
		}
	}

	/**
	 * 返回指定名字的Sheet, 如果该sheetName不存在, 返回null
	 *
	 * @param workbook
	 * @param sheetName
	 * @return Sheet
	 */
	public static Sheet getSheet(Workbook workbook, String sheetName) {
		Objects.requireNonNull(workbook, "Workbook 对象不能为null");
		Objects.requireNonNull(sheetName, "sheetName不能为null");
		Sheet sheet = workbook.getSheet(sheetName);
		return sheet;
	}

	/**
	 * 返回指定名字的Sheet, 如果不存在则返回指定index的Sheet
	 *
	 * @param workbook
	 * @param sheetName
	 * @param index
	 * @return Sheet
	 */
	public static Sheet getSheetByNameOrIndex(Workbook workbook, String sheetName, int index) {
		Objects.requireNonNull(workbook, "Workbook 对象不能为null");
		if (sheetName == null && index < 0) {
			throw new InvalidConfigurationException("SheetName and index should specify atleast one");
		}

		Sheet sheet = null;
		if (sheetName != null && !"".equals(sheetName.trim())) {
			sheet = workbook.getSheet(sheetName);
		}
		if (sheet == null) {
			if (index < 0) {
				log.warn("Sheet index {} invalid, should >=0", index);
				return null;
			}
			return workbook.getSheetAt(0);
		}
		return sheet;
	}

	public static Sheet getSheet(Workbook workbook, int sheetIndex) {
		Objects.requireNonNull(workbook, "Workbook 对象不能为null");
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		return sheet;
	}

	/**
	 * csv 转 xls 格式
	 *
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static File csvToXLS(InputStream inputStream) throws Exception {
		try {
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet1");
			String currentLine = null;
			int RowNum = 0;
			BufferedReader br = IOUtils.toBufferedReader(inputStream, IOUtils.GBK);
			while ((currentLine = br.readLine()) != null) {
				String str[] = currentLine.split(",");
				RowNum++;
				XSSFRow currentRow = sheet.createRow(RowNum);
				for (int i = 0; i < str.length; i++) {
					currentRow.createCell(i).setCellValue(str[i]);
				}
			}

			File tempFile = IOUtils.tempFile("xls");
			FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
			workBook.write(fileOutputStream);
			fileOutputStream.close();
			workBook.close();
			return tempFile;
		} catch (Exception e) {
			log.error("转换CSV-XLS 失败", e);
			throw e;
		}
	}

	/**
	 * CSV格式的第一行的index是1 XLS, XLSX格式的第一行的index是0
	 *
	 * @param sheet
	 * @return
	 */
	public Row getFirstRow(Sheet sheet) {
		Row row = sheet.getRow(0);
		if (row == null) {
			row = sheet.getRow(1);
		}
		return row;
	}

	/**
	 * 写入到指定sheetName的Sheet页面, sheetName不存在则写入到Sheet1
	 * <br/>
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetName
	 * @param pojos
	 * @on
	 */
	public static Path write2Excel(String template, String sheetName, List<?> pojos) {
		Path newFile = com.loserico.common.lang.utils.IOUtils.fileCopy(template);
		return doWrite2Excel(newFile, sheetName, null, pojos);
	}

	/**
	 * 写入到指定sheetName的Sheet页面, sheetName不存在则写入到Sheet1
	 * <br/>
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetName
	 * @param pojos
	 */
	public static Path write2Excel(String template, String sheetName, List<?> pojos, String targetDir) {
		Path targetDirPath = Paths.get(targetDir);
		Path filePath = write2Excel(template, sheetName, null, pojos);
		if (!targetDirPath.toFile().exists()) {
			targetDirPath.toFile().mkdirs();
		}
		Path realPath = targetDirPath.resolve(filePath.getFileName());
		IOUtils.copy(filePath, realPath, REPLACE_EXISTING);
		return realPath;
	}

	/**
	 * 写入到指定sheetName的Sheet页面, sheetName不存在则写入到Sheet1
	 * 同时可以指定生成的文件名的前缀, 后面会有一串随机数
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetName
	 * @param targetFileName
	 * @param pojos
	 * @return
	 */
	public static Path write2Excel(String template, String sheetName, String targetFileName, List<?> pojos) {
		Path newFile = com.loserico.common.lang.utils.IOUtils.fileCopy(template);
		return doWrite2Excel(newFile, sheetName, targetFileName, pojos);
	}

	/**
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetName
	 * @param targetFileName 文件名, 不带后缀
	 * @param pojos
	 * @return
	 */
	public static Path write2Excel(String template, String sheetName, String targetFileName, List<?> pojos, String targetDir) {
		Path newFile = com.loserico.common.lang.utils.IOUtils.fileCopy(template);
		Path targetDirPath = Paths.get(targetDir);
		Path filePath = doWrite2Excel(newFile, sheetName, targetFileName, pojos);
		if (!targetDirPath.toFile().exists()) {
			targetDirPath.toFile().mkdirs();
		}
		Path realPath = targetDirPath.resolve(filePath.getFileName());
		IOUtils.copy(filePath, realPath, REPLACE_EXISTING);
		return realPath;
	}

	/**
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetName
	 * @param pojos
	 */
	public static Path write2Excel(Path template, String sheetName, List<?> pojos) {
		return write2Excel(template, sheetName, null, pojos);
	}

	/**
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetName
	 * @param pojos
	 * @param targetDir 最终文件存放目录
	 */
	public static Path write2Excel(Path template, String sheetName, List<?> pojos, Path targetDir) {
		Path filePath = write2Excel(template, sheetName, null, pojos);
		if (!targetDir.toFile().exists()) {
			targetDir.toFile().mkdirs();
		}
		Path realPath = targetDir.resolve(filePath.getFileName());
		IOUtils.copy(filePath, realPath, REPLACE_EXISTING);
		return realPath;
	}

	/**
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetIndex
	 * @param pojos
	 */
	public static Path write2Excel(Path template, int sheetIndex, List<?> pojos) {
		return write2Excel(template, sheetIndex, null, pojos);
	}

	/**
	 * 写Excel到指定的目录下, 文件名前缀是targetFileName, 后面带上随机字符串
	 *
	 * @param template
	 * @param sheetIndex
	 * @param pojos
	 * @return
	 */
	public static Path write2Excel(String template, int sheetIndex, List<?> pojos) {
		Path newFile = com.loserico.common.lang.utils.IOUtils.fileCopy(template);
		Path filePath = doWrite2Excel(newFile, sheetIndex, null, pojos);
		return filePath;
	}

	/**
	 * 写Excel到指定的目录下, 文件名前缀是targetFileName, 后面带上随机字符串
	 *
	 * @param template
	 * @param sheetIndex
	 * @param pojos
	 * @param targetDir
	 * @return
	 */
	public static Path write2Excel(String template, int sheetIndex, List<?> pojos, String targetDir) {
		Path templatePath = com.loserico.common.lang.utils.IOUtils.fileCopy(template);
		Path targetDirPath = Paths.get(targetDir);
		Path path = doWrite2Excel(templatePath, sheetIndex, null, pojos);
		
		if (!targetDirPath.toFile().exists()) {
			targetDirPath.toFile().mkdirs();
		}
		Path realPath = targetDirPath.resolve(path.getFileName());
		IOUtils.copy(path, realPath, REPLACE_EXISTING);
		return realPath;
	}

	/**
	 * 写Excel到指定的目录下, 文件名前缀是targetFileName, 后面带上随机字符串
	 *
	 * @param template
	 * @param sheetIndex
	 * @param pojos
	 * @param targetDir
	 * @return Path
	 */
	public static Path write2Excel(Path template, int sheetIndex, List<?> pojos, Path targetDir) {
		Path filePath = write2Excel(template, sheetIndex, pojos);
		if (!targetDir.toFile().exists()) {
			targetDir.toFile().mkdirs();
		}
		Path realPath = targetDir.resolve(filePath.getFileName());
		IOUtils.copy(filePath, realPath, REPLACE_EXISTING);
		return realPath;
	}

	/**
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetName
	 * @param targetFileName 文件名, 不带后缀
	 * @param pojos
	 * @return
	 */
	public static Path write2Excel(Path template, String sheetName, String targetFileName, List<?> pojos) {
		Path newFile = IOUtils.fileCopy(template);
		return doWrite2Excel(newFile, sheetName, targetFileName, pojos);
	}

	/**
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetIndex
	 * @param targetFileName 文件名, 不带后缀
	 * @param pojos
	 * @return
	 */
	public static Path write2Excel(Path template, int sheetIndex, String targetFileName, List<?> pojos) {
		Path newFile = IOUtils.fileCopy(template);
		return doWrite2Excel(newFile, sheetIndex, targetFileName, pojos);
	}

	/**
	 * 模版的第一行是标题 第二行是变量占位符 第三行用于设置格式（style）
	 *
	 * @param template
	 * @param sheetIndex
	 * @param targetFileName 文件名, 不带后缀
	 * @param pojos
	 * @return
	 */
	public static Path write2Excel(Path template, int sheetIndex, String targetFileName, List<?> pojos,
	                               Path targetDir) {
		Path newFile = IOUtils.fileCopy(template);
		Path filePath = doWrite2Excel(newFile, sheetIndex, targetFileName, pojos);

		if (!targetDir.toFile().exists()) {
			targetDir.toFile().mkdirs();
		}
		Path realPath = targetDir.resolve(filePath.getFileName());
		IOUtils.copy(filePath, realPath, REPLACE_EXISTING);
		return realPath;
	}

	private static Path doWrite2Excel(Path newFile, int sheetIndex, String targetFileName, List<?> pojos) {
		requireNonNull(newFile, "找不到文件");
		String templateName = newFile.getFileName().toString();
		Workbook workbook = null;
		try {
			workbook = getWorkbook(newFile);
		} catch (Exception e) {
			log.error("创建Workbook对象失败", e);
			throw new WorkbookCreationException(e);
		}

		Sheet sheet = workbook.getSheetAt(sheetIndex);
		if (sheet == null) {
			try {
				workbook.close();
			} catch (IOException e) {
				log.error("", e);
			}
			throw new SheetNotExistException(format("模版[{0}] 不存在Index为[{1}]的Sheet", templateName, sheetIndex));
		}
		String extension = IOUtils.fileExtension(templateName);
		return doWrite2Excel(workbook, sheet, targetFileName, extension, pojos);
	}

	/**
	 * 写入到指定sheetName的Sheet页面, sheetName不存在则写入到Sheet1
	 *
	 * @param newFile
	 * @param sheetName
	 * @param targetFileName
	 * @param pojos
	 * @return
	 */
	private static Path doWrite2Excel(Path newFile, String sheetName, String targetFileName, List<?> pojos) {
		requireNonNull(newFile, "找不到文件");
		String templateName = newFile.getFileName().toString();
		Workbook workbook = null;
		try {
			workbook = getWorkbook(newFile);
		} catch (Exception e) {
			log.error("创建Workbook对象失败", e);
			throw new WorkbookCreationException(e);
		}

		Sheet sheet = workbook.getSheet(ofNullable(sheetName).orElse("Sheet1"));
		if (sheet == null) {
			try {
				workbook.close();
			} catch (IOException e) {
				log.error("", e);
			}
			throw new SheetNotExistException(format("模版[{0}] 不存在名字为[{1}]的Sheet", templateName, sheetName));
		}
		String extension = IOUtils.fileExtension(templateName);
		return doWrite2Excel(workbook, sheet, targetFileName, extension, pojos);
	}

	private static Path doWrite2Excel(Workbook workbook, Sheet sheet, String targetFileName, String extension,
	                                  List<?> pojos) {
		requireNonNull(workbook);
		requireNonNull(sheet);

		int varRowNum = 1; // 变量占位符所在行
		int styleRowNum = 2; // 样式模版所在行

		// 解析模版行
		Row varRow = sheet.getRow(varRowNum);
		int cellBeginIndex = (int) varRow.getFirstCellNum(); // 第一列
		int cellEndIndex = (int) varRow.getLastCellNum(); // 最后一列,这个API 是从1开始计数的
		List<CellWriter> cellWriters = new ArrayList<>();
		for (int cellIndex = cellBeginIndex; cellIndex < cellEndIndex; cellIndex++) {
			VarInfo varInfo = varInfo(varRow, cellIndex);
			if (varInfo == null) {
				continue;
			}
			cellWriters.add(new CellWriter(cellIndex, varInfo));
		}

		int startRowNum = 3; // 写入第一行数据的index
		for (Object dataContainer : pojos) { // 一个pojo代表一行
			Row writingRow = copyRow(workbook, sheet, styleRowNum, startRowNum);
			for (CellWriter cellWriter : cellWriters) {
				cellWriter.write(dataContainer, writingRow);
			}
			startRowNum++;
		}

		// 这行有可能存在也有可能不存在
		Row styleRow = sheet.getRow(styleRowNum);
		if (styleRow == null) {
			shiftRowsAfter(sheet, styleRowNum, -1);
		} else {
			removeRow(sheet, styleRowNum);
		}
		removeRow(sheet, varRowNum);

		try {
			File destFile = targetFileName == null ? IOUtils.tempFile(extension)
					: IOUtils.tempFile(targetFileName, extension);
			FileOutputStream fos = new FileOutputStream(destFile);
			workbook.write(fos);
			fos.close();
			workbook.close();
			return destFile.toPath();
		} catch (IOException e) {
			log.error("写入到Excel文件失败！", e);
			throw new ExcelOutputStreamWriteException(e);
		}
	}

	/**
	 * 写入字符串值
	 *
	 * @param sheet
	 * @param rowIndex
	 * @param cellIndex
	 * @param value
	 */
	public static void writeCell(Sheet sheet, int rowIndex, int cellIndex, String value) {
		requireNonNull(sheet, "sheet cannot be null");
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			throw new RowNotFoundException(
					format("Row[{1}] cannot be found at Sheet[{0}]", rowIndex, sheet.getSheetName()));
		}
		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			throw new CellNotFoundException(format("Cell[{0}] cannot be found at Sheet[{1}], Row[{2}]", cellIndex,
					rowIndex, sheet.getSheetName()));
		}

		cell.setCellValue(value);
	}

	public static Path write2TmpFile(Workbook workbook, String extension) throws IOException {
		File destFile = IOUtils.tempFile(extension);
		FileOutputStream fos = new FileOutputStream(destFile);
		workbook.write(fos);
		fos.close();
		workbook.close();
		return destFile.toPath();
	}

	/**
	 * 复制 sourceRowNum 指定的行到目标行 destinationRowNum, destinationRowNum之后已经存在的行将下移
	 *
	 * @param workbook
	 * @param worksheet
	 * @param styleRowNum 样式所在行
	 * @param startRowNum 写数据的行号
	 * @return Row
	 */
	private static Row copyRow(Workbook workbook, Sheet worksheet, int styleRowNum, int startRowNum) {
		// Get the source / new row
		Row styleRow = worksheet.getRow(styleRowNum);// 2 样式行
		Row startRow = worksheet.getRow(startRowNum);// 3

		/*
		 * 如果startRow已经存在, 从startRow开始的行, 依次下移一行
		 * 如果不存在, 创建一新行
		 */
		if (startRow != null) {
			worksheet.shiftRows(startRowNum, worksheet.getLastRowNum(), 1);
		}

		/*
		 * 如果开始写数据的行不存在则创建一样
		 * 但是如果startRow存在, 那么startRow连同他下面的所有行, 会被下移一行, 注意这时候startRow.rowNum会加1
		 * 那么在调用copyRow()的方法里面执行startRowNum++, 然后调用copyRow(), 下一个循环就又写到同一行上去了
		 * 所以这里一定要调: worksheet.createRow(startRowNum);
		 */
		startRow = worksheet.createRow(startRowNum);

		if (styleRow == null) { // 没有拷贝的对象, 直接返回新创建的行, 不去复制style了
			return startRow;
		}
		// Loop through source columns to add to new row
		for (int i = 0; i < styleRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
			Cell oldCell = styleRow.getCell(i);
			Cell newCell = startRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			CellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());

			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
				case Cell.CELL_TYPE_BLANK:
					newCell.setCellValue(oldCell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					newCell.setCellValue(oldCell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_ERROR:
					newCell.setCellErrorValue(oldCell.getErrorCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					newCell.setCellFormula(oldCell.getCellFormula());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					newCell.setCellValue(oldCell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_STRING:
					newCell.setCellValue(oldCell.getRichStringCellValue());
					break;
			}
		}

		// If there are are any merged regions in the source row, copy to new
		// row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == styleRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(startRow.getRowNum(),
						(startRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(),
						cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}

		return startRow;
	}

	public static void removeRow(Sheet sheet, int rowIndex) {
		if (rowIndex >= 0) {
			Row rowToRemove = sheet.getRow(rowIndex);
			ofNullable(rowToRemove).ifPresent((row) -> {
				sheet.removeRow(row);
				if (rowIndex < sheet.getLastRowNum()) {
					sheet.shiftRows(rowIndex + 1, sheet.getLastRowNum(), -1);
				}
			});
		}
	}

	/**
	 * 将指定rowIndex下一行开始的所有行下移(shiftRows为正数), 上移(shiftRows为负数)指定行数(shiftRows)
	 *
	 * @param sheet
	 * @param rowIndex
	 * @param shiftRows
	 */
	public static void shiftRowsAfter(Sheet sheet, int rowIndex, int shiftRows) {
		if (rowIndex >= 0) {
			if (rowIndex < sheet.getLastRowNum()) {
				sheet.shiftRows(rowIndex + 1, sheet.getLastRowNum(), -1);
			}
		}
	}

	/**
	 * 占位符格式为 ${varName}, 返回varName
	 *
	 * @param row
	 * @return String
	 */
	public static VarInfo varInfo(Row row, int cellIndex) {
		String varTemplate = stringVal(row, cellIndex);
		if (isBlank(varTemplate)) {
			return null;
		}

		List<Pattern> patterns = asList(dateTimePattern, dateTimeShortPattern1, datePattern1,
				precisionNumberPlaceholderPattern,
				placeholderPattern);

		// 找到第一个匹配的模式
		Matcher matcher = null;
		for (Pattern pattern : patterns) {
			matcher = pattern.matcher(varTemplate);
			if (matcher.matches()) {
				break;
			}
		}
		if (!matcher.matches()) {
			log.warn("一个都不match哦~");
			throw new InvalidVarTemplateException(format("无法解析变量模版[{0}]", varTemplate));
		}

		String varname = matcher.group(1);

		/*
		 * 取模版的最后一个字符, 如 ${varTemplate}n
		 * n表示数字类型
		 * ${varTemplate}后面不跟数据类型默认字符串
		 * @on
		 */
		if (matcher.groupCount() > 1) {
			// String varType = StringUtils.lastN(varTemplate, 1);
			String varType = matcher.group(2);
			if ("n".equalsIgnoreCase(varType)) {
				if (matcher.groupCount() > 2) {
					String matcherN = matcher.group(3);
					Integer precision = isBlank(matcherN) ? 0 : Integer.parseInt(matcherN);
					String negativeSign = matcher.group(4);
					if (isNotBlank(negativeSign) && "-".equals(negativeSign)) {
						return new VarInfo(varname, NUM, precision, true);
					} else {
						return new VarInfo(varname, NUM, precision);
					}
				}
				return new VarInfo(varname, NUM);
			}
			if ("d".equalsIgnoreCase(varType)) {
				String datePattern = matcher.group(3);
				return new VarInfo(varname, DT, datePattern);
			}

		}
		return new VarInfo(varname, STR);
	}

	public static String stringVal(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			try {
				if (cell.getCellTypeEnum() == CellType.STRING) {
					String cellValue = cell.getStringCellValue();
					if ("\"\"".equals(cellValue)) {
						return null;
					}
					Matcher matcher = pattern.matcher(cellValue);
					if (matcher.matches()) {
						return matcher.group(1).trim();
					}
					if (isNotBlank(cellValue)) {
						return cellValue.trim();
					}
				} else if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
					return new BigDecimal(cell.getNumericCellValue()).toPlainString();
				}
			} catch (Exception e) {
				log.info("get string value failed with row[" + row.getRowNum() + "], cell[" + cellIndex + "]", e);
			}
		}
		return null;
	}
	
	public static String stringVal(Cell cell) {
		if (cell != null) {
			try {
				if (cell.getCellTypeEnum() == CellType.STRING) {
					String cellValue = cell.getStringCellValue();
					if ("\"\"".equals(cellValue)) {
						return null;
					}
					Matcher matcher = pattern.matcher(cellValue);
					if (matcher.matches()) {
						return matcher.group(1).trim();
					}
					if (isNotBlank(cellValue)) {
						return cellValue.trim();
					}
				} else if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
					return new BigDecimal(cell.getNumericCellValue()).toPlainString();
				}
			} catch (Exception e) {
				log.info("get string value failed for given cell", e);
			}
		}
		return null;
	}

	public static LocalDateTime dateTimeVal(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			try {
				if (cell.getCellTypeEnum() == CellType.STRING) {
					String dateStr = StringUtils.trimQuote(stringVal(row, cellIndex));
					if (isBlank(dateStr)) {
						return null;
					}
					return DateUtils.toLocalDateTime(dateStr);
				}
				return DateUtils.toLocalDateTime(cell.getDateCellValue());
			} catch (Exception e) {
				log.info("调用 cell.getDateCellValue() 失败, 改用 cell.getStringCellValue()");
			}
		}
		return null;
	}

	public static LocalDate dateVal(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			try {
				CellType cellTypeEnum = cell.getCellTypeEnum();
				if (cellTypeEnum == CellType.STRING) {
					String dateStr = stringVal(row, cellIndex);
					if (isBlank(dateStr)) {
						return null;
					}
					return DateUtils.toLocalDate(StringUtils.trimQuote(dateStr));
				}

				LocalDate localDate = DateUtils.toLocalDate(cell.getDateCellValue());
				if (localDate != null) {
					return localDate;
				}
				/*
				 * 付款时间
				 * 2018-9-10
				 *
				 * 遇到Excel某列值是如上, 获取到的CellType却是Numeric, 但是拿到的值是类似65333这样的
				 * 所以现在改成先拿DateCellValue, 拿不到再走Numberic
				 */
				if (cellTypeEnum == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
					String dateStr = String.valueOf((long) cell.getNumericCellValue());
					return DateUtils.toLocalDate(dateStr);
				}
			} catch (Exception e) {
				log.info("调用 cell.getDateCellValue() 失败, 改用 cell.getStringCellValue()");
			}
		}
		return null;
	}

	public static Double doubleVal(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			try {
				if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
					return cell.getNumericCellValue();
				}
				String value = StringUtils.trimQuote(cell.getStringCellValue());
				if (isBlank(value)) {
					return null;
				}

				Matcher matcher = moneyDoublePattern.matcher(value);
				if (matcher.matches()) {
					return Double.valueOf(matcher.group(1).replaceAll(",", ""));
				}
				return Double.valueOf(value.replaceAll(",", ""));
			} catch (Exception e) {
				log.info("调用 cell.getNumericCellValue(),  改用 cell.getStringCellValue()");
			}
		}
		return null;
	}

	public static Long longVal(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			try {
				if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
					return (long) cell.getNumericCellValue();
				}
				String value = StringUtils.trimQuote(cell.getStringCellValue());
				if (isBlank(value)) {
					return null;
				}
				return Long.parseLong(value);
			} catch (Exception e) {
				log.info("调用cell.getNumericCellValue()失败, 改用cell.getStringCellValue()");
			}
		}
		return null;
	}

	public static Integer intVal(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			try {
				if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
					return (int) cell.getNumericCellValue();
				}
				String value = StringUtils.trimQuote(cell.getStringCellValue());
				if (isBlank(value)) {
					return null;
				}
				return Integer.valueOf(value);
			} catch (Exception e) {
				log.info("调用cell.getNumericCellValue()失败, 改用cell.getStringCellValue()");
			}
		}
		return null;
	}

	public static BigDecimal bigDecimalVal(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			try {
				if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
					double value = cell.getNumericCellValue();
					return new BigDecimal(value);
				}
				String value = StringUtils.trimQuote(cell.getStringCellValue());
				if (value == null || value.trim().equals("")) {
					return null;
				}
				value = value.replaceAll(",", "")
						.replaceAll("-", "")
						.replaceAll("_", "");
				if (isNotBlank(value)) {
					return new BigDecimal(value);
				}
				return null;
			} catch (Exception e) {
				log.error("调用 cell.getNumericCellValue() 失败, 改用 cell.getStringCellValue()", e);
			}
		}
		return null;
	}

	public static Double toDouble(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Double) {
			return (Double) value;
		}

		if (value instanceof Long) {
			return ((Long) value).doubleValue();
		}

		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).doubleValue();
		}

		if (value instanceof Integer) {
			return ((Integer) value).doubleValue();
		}

		if (value instanceof String) {
			try {
				return Double.parseDouble((String) value);
			} catch (NumberFormatException e) {
				log.error("msg", e);
			}
		}

		return null;
	}

	public static Double toDouble(Object value, boolean toNegative) {
		Double v = toDouble(value);
		if (toNegative && value != null) {
			return 0 - v;
		}
		return v;
	}

	public static String toString(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return (String) value;
		}

		if (value instanceof LocalDate) {
			return DateUtils.format((LocalDate) value, "yyyy-m-d");
		}

		return value.toString();
	}

	public static String toString(Object value, String dateFormat) {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return (String) value;
		}

		if (value instanceof Long) {
			return value.toString();
		}

		if (value instanceof Integer) {
			return value.toString();
		}

		if (value instanceof Double) {
			return value.toString();
		}

		if (value instanceof Float) {
			return value.toString();
		}

		if (value instanceof BigDecimal) {
			return value.toString();
		}

		if (value instanceof LocalDate) {
			if (dateFormat == null) {
				return DateUtils.format((LocalDate) value, "yyyy-M-d");
			}
			return DateUtils.format((LocalDate) value, dateFormat);
		}

		if (value instanceof LocalDateTime) {
			if (dateFormat == null) {
				return DateUtils.format((LocalDateTime) value, "yyyy-M-d HH:mm:ss");
			}
			return DateUtils.format((LocalDateTime) value, dateFormat);
		}

		if (value instanceof Date) {
			if (dateFormat == null) {
				return DateUtils.format((Date) value, "yyyy-M-d HH:mm:ss");
			}
			return DateUtils.format((Date) value, dateFormat);
		}

		return null;
	}

}
