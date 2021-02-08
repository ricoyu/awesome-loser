package com.loserico.validation.validation;

import com.loserico.networking.utils.NetUtils;
import com.loserico.validation.enums.IPCategory;
import com.loserico.validation.validation.annotation.IP;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 验证这是一个合法的IP地址
 * 可以是IPv4或者IPv6
 * 可以包含子网掩码部分或者不包含
 *
 * <ul>
 * <li>58.222.16.61</li>
 * <li>10.10.10.0/24</li>
 * <li>2001:0db8:3c4d:0015::/64</li>
 * <li>2001:0db8:85a3:0000:0000:8a2e:0370:7334</li>
 * <ul/>
 * <p>
 * Copyright: (C), 2021-02-02 16:58
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IPValidator implements ConstraintValidator<IP, String> {
	
	private IPCategory category;
	
	private String message;
	
	@Override
	public void initialize(IP ip) {
		this.category = ip.category();
		this.message = ip.message();
	}
	
	@Override
	public boolean isValid(String ip, ConstraintValidatorContext context) {
		// 不做必填验证, 所以IP为空的话直接跳过校验
		if (isBlank(ip)) {
			return true;
		}
		
		boolean isValid = false;
		
		if (category == IPCategory.IP_V4) {
			isValid = NetUtils.isValidIpV4(ip);
		}
		if (category == IPCategory.IP_V6) {
			isValid = NetUtils.isValidIpV6(ip);
		} else {
			isValid = NetUtils.isValidIpV4(ip) || NetUtils.isValidIpV6(ip);
		}
		
		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ofNullable(message).orElse("IP 地址不合法"))
					.addConstraintViolation();
		}
		
		return isValid;
	}
}
