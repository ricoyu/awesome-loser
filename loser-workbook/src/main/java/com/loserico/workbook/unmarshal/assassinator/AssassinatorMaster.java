package com.loserico.workbook.unmarshal.assassinator;

import com.loserico.workbook.exception.AssassinationTrainFailedException;
import com.loserico.workbook.exception.RowNotFoundException;
import com.loserico.workbook.exception.SheetNotExistException;
import com.loserico.workbook.unmarshal.iterator.RowIterator;
import com.loserico.workbook.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static com.loserico.workbook.utils.ExcelUtils.stringVal;
import static java.util.stream.Collectors.toList;

/**
 * Assassinator大师, 负责将某个POJOAssassinator与Sheet中的某列对应起来
 * <p>
 * Copyright: Copyright (c) 2019-05-23 16:04
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public final class AssassinatorMaster {

	/**
	 * 有些cell里面的值是双引号括起来的, 这里要去掉双引号
	 */
	protected static Pattern PATTERN_QUOTE = Pattern.compile("\"(.+)\"");

	/**
	 * Excel中标题栏的行号, csv从1开始计数
	 */
	private int titleRowIndex = -1;

	/**
	 * 要读取的sheet页的名字
	 */
	private String sheetName;

	/**
	 * 指定的sheetName 不存在时按指定的sheetIndex读取
	 */
	private int fallbackSheetIndex = -1;

	private AssassinatorMaster(Builder builder) {
		this.titleRowIndex = builder.titleRowIndex;
		this.sheetName = builder.sheetName;
		this.fallbackSheetIndex = builder.fallbackSheetIndex;
	}

	public static final class Builder {

		private Builder() {
		}

		/**
		 * Excel中标题栏的行号, 默认0
		 */
		private int titleRowIndex = -1;

		/**
		 * 要读取的sheet页的名字
		 */
		private String sheetName;

		private int fallbackSheetIndex = -1;

		public Builder titleRowIndex(int titleRowIndex) {
			this.titleRowIndex = titleRowIndex;
			return this;
		}

		public Builder sheetName(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}

		public Builder fallbackSheetIndex(int fallbackSheetIndex) {
			this.fallbackSheetIndex = fallbackSheetIndex;
			return this;
		}

		public AssassinatorMaster build() {
			return new AssassinatorMaster(this);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * 目的是为每个POJOAssassinator在workbook中找到一个对应的列, 并设置该列的index到POJOAssassinator
	 *
	 * @param assassinators
	 * @param workbook
	 * @return 标题行的行号, -1表示没有找到标题行
	 */
	public RowIterator train(List<POJOAssassinator> assassinators, Workbook workbook) {
		Sheet sheet = ExcelUtils.getSheetByNameOrIndex(workbook, sheetName, fallbackSheetIndex);
		if (sheet == null) {
			throw new SheetNotExistException("No sheet for name[" + sheetName + "]");
		}

		// 先要确定哪一行是标题行
		Row titleRow = null; 
		int titleRowIndex = -1;
		// 表示我显式指定了哪一行是标题行
		if (this.titleRowIndex != -1) {
			titleRow = sheet.getRow(this.titleRowIndex);
			titleRowIndex = this.titleRowIndex;
		} else {
			/*
			 * 如果没有显式指定标题行, 那么进入自动寻找标题行模式
			 * 查找逻辑: 从第0行开始依次查找, 第0列值不为空的即认为是标题所在行
			 */
			Iterator<Row> iterator = sheet.rowIterator();
			while (iterator.hasNext()) {
				titleRow = (Row) iterator.next();
				String title = ExcelUtils.stringVal(titleRow.getCell(0));
				if (isNotBlank(title)) {
					titleRowIndex = titleRow.getRowNum();
					break;
				}
			}
		}

		if (titleRowIndex == -1) {
			throw new RowNotFoundException("找不到标题行");
		}

		RowIterator<Row> rowIterator = new RowIterator<>(sheet, titleRowIndex + 1);
		/*
		 * 从这一行开始, 第0列是有值的, 那么认为这行就是标题行
		 * 从这行的第0列开始一致到最后一列, 取每一列的字符串值 title,
		 * title先和POJOAssassinator.columnName比较, 相同则把当前列的index设置到POJOAssassinator.cellIndex上
		 * 如果不相同并且POJOAssassinator.fallbackName != null, 那么title和fallbackName比较, 相同则设置cellIndex
		 */
		int beginIndex = titleRow.getFirstCellNum();
		int lastColumnIndex = titleRow.getLastCellNum();

		for (POJOAssassinator assassinator : assassinators) {
			/*
			 * cellIndex != -1 有两种可能
			 * @Col(index = 2) 在注解里已经显式指定了这个字段对应Excel中的哪一列
			 * 第二种可能就是已经在Excel中找到匹配的列了
			 */
			if (assassinator.getCellIndex() != -1) {
				continue;
			}

			for (int index = beginIndex; index <= lastColumnIndex; index++) {
				/*
				
				 */
				Cell cell = titleRow.getCell(index);
				/*
				 * getLastCellNum()返回的列数不一定是真正的列的数量, 有可能后面的都是空的列
				 * 这种情况拿到的Cell对象是null, 这里如果遇到Cell是null, 认为Column到这里就
				 * 结束了, 不再往后面找了
				 */
				if (cell == null) {
					break;
				}
				String title = ExcelUtils.stringVal(cell);

				/*
				 * 这一列没有值的话就认为中间空了一列, 继续
				 */
				if (title == null || "".equals(title)) {
					continue;
				}
				if (title.equals(assassinator.getColumnName()) || title.equals(assassinator.getFallbackName())) {
					assassinator.setCellIndex(index);
					break;
				}

			}
		}

		/*
		 * 如果标题行的每一列都尝试过一遍, 但是还有POJOAssassinator没有找到对应的列(没有完成训练), 那么就得抛个异常玩玩了, 这个训练有点失败呀
		 */
		long uncompleteCount = assassinators.stream().filter(a -> a.getCellIndex() < 0).count();
		if (uncompleteCount != 0) {
			/*
			 * 为了记log, 表示POJO中有哪些字段在Excel中没有找到对应的列
			 */
			List<String> missingColumns = assassinators.stream()
					.filter(a -> a.getCellIndex() == -1)
					.map((a) -> {
						return a.getColumnName() != null ? a.getColumnName() : a.getFallbackName();
					}).collect(toList());
			throw new AssassinationTrainFailedException(missingColumns.toString());
		}

		int totalRowCount = sheet.getLastRowNum() - titleRowIndex;
		rowIterator.setTotalCount(totalRowCount);
		return rowIterator;
	}

	/**
	 * 检测字符串不为null并且不是只包含空格的空字符串
	 *
	 * @param s
	 * @return
	 */
	private boolean isNotBlank(String s) {
		return s != null && !"".equals(s.trim());
	}
}
