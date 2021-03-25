package com.loserico.networking.http;

import com.loserico.networking.enums.GrantType;
import com.loserico.networking.enums.Scope;

/**
 * <p>
 * Copyright: (C), 2020/5/5 16:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface OAuth2Support {
	
	/**
	 * 设置 OAuth 2.0 Bearer Token
	 * 请求头: Authorization 值: Bearer AbCdEf123456
	 * @param token
	 * @return
	 */
	public OAuth2Support bearerAuth(String token);
	
	/**
	 * 授权模式
	 * @param grantType
	 * @return OAuth2Support
	 */
	public OAuth2Support grantType(GrantType grantType);
	
	/**
	 * Oauth2权限范围
	 * @param scope
	 * @return
	 */
	public OAuth2Support scope(Scope scope);
}
