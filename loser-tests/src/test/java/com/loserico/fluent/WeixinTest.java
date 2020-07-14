package com.loserico.fluent;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.junit.Test;

import java.io.IOException;

public class WeixinTest {

	@Test
	public void testGetToken() throws ClientProtocolException, IOException {
		String result = Request.Get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
				+ "&appid=wx5f87d4d45e56eb7f"
				+ "&secret=0e47d95d9807cd085eed1011e21d6e5e")
			.execute()
			.returnContent()
			.asString();
		
		System.out.println(result);
	}
	
	@Test
	public void testResult() throws ClientProtocolException, IOException {
		
		String result = Request.Get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?"
				+ "access_token=q9QZXT-QznVbdnC1B66cAUHDdTO4MtxR2OUk7EAajDwxaTfBQvRg7fsiBk9VfxOiDW3m0aZn6Ct8oOEsfSAcneM9YVJKfzzBHMKWpaA7SBnJiRJmwuEBA7LByRJzHukAAEWfAFAHLD"
				+ "&type=jsapi")
			.execute()
			.returnContent()
			.asString();
		System.out.println(result);
	}
	
	   /**
     * 获取jsapi_ticket
	 * @throws IOException 
	 * @throws ClientProtocolException 
     * 
     */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws ClientProtocolException, IOException {
/*		String ticket = "";
		String result = Request.Get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
				+ "&appid=wx5f87d4d45e56eb7f"
				+ "&secret=0e47d95d9807cd085eed1011e21d6e5e")
				.execute()
				.returnContent()
				.asString();
		JSONObject tokenResult = (JSONObject) JSON.parse(result, -1);
		String accessToken = tokenResult.getString("access_token");
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
		
		String tocketResult = Request.Get(requestUrl)
			.execute()
			.returnContent()
			.asString();
		System.out.println(tocketResult);

		String ticket = "HoagFKDcsGMVCIY2vOjf9gIe5APOsH82KC2MgUyDawx1u9mBabNkR8EbBcygWgPxGrv_PGpXoWawP-LxCCOeTg";
		
		String timestamp = Long.toString(new Date().getTime());
		System.out.println("timestamp: " + timestamp);
		String noncestr = RandomStringUtils.randomAlphabetic(16);
		System.out.println("noncestr: " + noncestr);
		String url = "http://mulberrylearning.cn/reg/event.html";
		String resultStr = "jsapi_ticket="+ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
		System.out.println(Hashing.sha1().hashString(resultStr, UTF_8));*/
	}
}
