package com.loserico.base64;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Base64EncorderTest {

	@Test
	public void testEncoderStr() throws UnsupportedEncodingException {
		String str = "this is an string";
		String encorded = Base64.getEncoder().encodeToString("ricoyu:123456".getBytes("UTF-8"));
		System.out.println(encorded);

		byte[] decorded = Base64.getDecoder().decode(encorded);
		System.out.println(new String(decorded, "UTF-8"));
	}

	@Test
	public void testName() {
		System.out.println("asdasdasd");
	}

	/**
	 * URL 
	 * 
	 * This one is very similar to the basic encoder. It uses the URL and Filename
	 * safe base64 alphabet and does not add any line separation. This alphabet does
	 * not use special characters used in URLs like ‘/’. Here is an example:
	 * 
	 * We can see that using the URL encoder we do not have any problem while using these strings in an URL, 
	 * using the basic one, we do because it contains characters like ‘/’.
	 * @on
	 */
	@Test
	public void testUrlEncoded() {
		String url = "subjects?mathematics";
		String urlEncoded = Base64.getUrlEncoder().encodeToString(url.getBytes(UTF_8));
		System.out.println(urlEncoded);
		String urlEncodedBasic = Base64.getEncoder().encodeToString(url.getBytes(UTF_8));
		System.out.println(urlEncodedBasic);
	}
}
