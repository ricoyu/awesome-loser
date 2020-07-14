package com.loserico.workbook.marshal;

/**
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:43
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class VarInfo {

	/**
	 * 数字
	 */
	public static final int NUM = 0;
	/**
	 * 字符串
	 */
	public static final int STR = 1;
	/**
	 * 日期
	 */
	public static final int DT = 2;

	private String varname;
	private int varType;
	private String dateFormat;
	/**
	 * 涉及到金额的地方格式化成123.0000这种形式
	 */
	private Integer precision;
	/**
	 * 是否将数字显示为负数
	 */
	private boolean displayNegative = false;

	public VarInfo(String varname, int varType) {
		this.varname = varname;
		this.varType = varType;
	}

	public VarInfo(String varname, int varType, Integer precision) {
		this.varname = varname;
		this.varType = varType;
		this.precision = precision;
	}

	public VarInfo(String varname, int varType, Integer precision, boolean displayNegative) {
		this.varname = varname;
		this.varType = varType;
		this.precision = precision;
		this.displayNegative = displayNegative;
	}

	public VarInfo(String varname, int varType, String dateFormat) {
		this.varname = varname;
		this.varType = varType;
		this.dateFormat = dateFormat;
	}

	public String getVarname() {
		return varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}

	public int getVarType() {
		return varType;
	}

	public void setVarType(int varType) {
		this.varType = varType;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public boolean isDisplayNegative() {
		return displayNegative;
	}

	public void setDisplayNegative(boolean displayNegative) {
		this.displayNegative = displayNegative;
	}

}
