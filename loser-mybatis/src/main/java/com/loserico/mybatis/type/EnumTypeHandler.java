package com.loserico.mybatis.type;

import com.loserico.common.lang.utils.EnumUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * Copyright: Copyright (c) 2021-05-14 17:54
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
	
	private final Class<E> type;
	
	public EnumTypeHandler(Class<E> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		this.type = type;
	}
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		if (jdbcType == null) {
			ps.setInt(i, parameter.ordinal());
		} else {
			ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE); // see r3589
		}
	}
	
	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String s = rs.getString(columnName);
		return (E)EnumUtils.lookupEnum(type, s);
	}
	
	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String s = rs.getString(columnIndex);
		return (E)EnumUtils.lookupEnum(type, s);
	}
	
	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String s = cs.getString(columnIndex);
		return (E)EnumUtils.lookupEnum(type, s);
	}
}
