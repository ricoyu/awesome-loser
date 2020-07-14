package com.loserico.workbook.unmarshal.convertor.datetime;

import java.time.LocalDateTime;

/**
 * 负责将日期时间型字符串转换成LocalDateTime
 * <p>
 * Copyright: Copyright (c) 2019-06-18 13:39
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public interface DateTimeConvertor {
	
	/**
	 * 判断是否可以转换<p/>
	 * 
	 * 请自行检查datetime是否为null, 是空就不要调用我了<br/>
	 * 调用我之前先在datetime两边trim一下, 我不负责干这事
	 * @param datetime
	 * @return
	 */
	public boolean supports(String datetime);

	/**
	 * 执行日期时间字符串到LocalDateTime的转换<p/>
	 * 
	 * 请自行检查datetime是否为null, 是空就不要调用我了<br/>
	 * 调用我之前先在datetime两边trim一下, 我不负责干这事
	 * 
	 * @param datetime
	 * @return LocalDateTime
	 * @on
	 */
	public LocalDateTime convert(String datetime);
}
