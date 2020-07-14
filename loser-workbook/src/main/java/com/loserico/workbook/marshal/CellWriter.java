package com.loserico.workbook.marshal;

import com.loserico.workbook.utils.ExcelUtils;
import com.loserico.workbook.utils.MathUtils;
import com.loserico.workbook.utils.ReflectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * 负责将值写入Cell 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:42
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CellWriter {

	private int cellnum;

	private VarInfo varInfo;

	public CellWriter(int cellnum, VarInfo varInfo) {
		this.cellnum = cellnum;
		this.varInfo = varInfo;
	}
	
	public Object write(Object dataContainer, Row row) {
		//空列的情况
		if(varInfo == null) {
			return null;
		}
		Object value = ReflectionUtils.invokeGetterMethod(dataContainer, varInfo.getVarname());
		if (value == null) {
			return null;
		}

		Cell cell = row.createCell(cellnum);
		if (varInfo.getVarType() == VarInfo.NUM) {
			if(varInfo.getPrecision() != null) {
				cell.setCellValue(MathUtils.formatDouble(ExcelUtils.toDouble(value, varInfo.isDisplayNegative()), varInfo.getPrecision()));
			} else {
				cell.setCellValue(ExcelUtils.toDouble(value));
			}
			return value;
		}

		if (varInfo.getVarType() == VarInfo.STR) {
			cell.setCellValue(ExcelUtils.toString(value, "yyyy-M-d"));
			return value;
		}
		
		if(varInfo.getVarType() == VarInfo.DT) {
			cell.setCellValue(ExcelUtils.toString(value, varInfo.getDateFormat()));
			return value;
		}

		return null;
	}

	public VarInfo getVarInfo() {
		return varInfo;
	}

	public void setVarInfo(VarInfo varInfo) {
		this.varInfo = varInfo;
	}

}
