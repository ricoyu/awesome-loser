package com.loserico.base64;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * The Base64 encoding encodes a sequence of bytes into a (longer) sequence of 
 * printable ASCII characters. It is used for binary data in email messages and 
 * “basic” HTTP authentication. For many years, the JDK had a nonpublic (and therefore unusable) 
 * class java.util.prefs.Base64 and an undocumented class sun.misc.BASE64Encoder. 
 * 
 * Finally, Java 8 provides a standard encoder and decoder.
 * 
 * The Base64 encoding uses 64 characters to encode six bits of information:
 * 	26 uppercase letters A . . . Z
 • 	26 lowercase letters a . . . z
 • 	10 digits 0 . . . 9
 • 	2 symbols, + and / (basic) or - and _ (URL- and filename-safe variant)
 *
 * Normally, an encoded string has no line breaks, but the MIME standard used for 
 * email requires a "\r\n" line break every 76 characters.
 * 
 * For encoding, request a Base64.Encoder with one of the static methods 
 * 	getEncoder, 
 *  getUrlEncoder, 
 *  getMimeEncoder of the Base64 class.
 * <p>
 * Copyright: Copyright (c) 2017-10-01 21:36
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class Base64Test {

	private static final Logger logger = LoggerFactory.getLogger(Base64Test.class);

	@Test
	public void testEncodertoString() {
		Base64.Encoder encoder = Base64.getEncoder();
		String original = "ricoyu" + ":" + "123456";
		String encoded = encoder.encodeToString(original.getBytes(UTF_8));
		System.out.println(encoded);
	}

	/**
	 * Alternatively, you can “wrap” an output stream, so that all data sent to it is
	 * automatically encoded.
	 */
	@Test
	public void testEncoderFileOutputStream() {
		String tmpdir = System.getProperty("java.io.tmpdir");
		System.out.println(tmpdir);

		Path originalPath = Paths.get(tmpdir, "Rico@DeepData.bmp");
		Path encodedPath = Paths.get(tmpdir, "rico@deepdata2.tmp");
		Base64.Encoder encoder = Base64.getMimeEncoder();

		try (OutputStream output = Files.newOutputStream(encodedPath)) {
			Files.copy(originalPath, encoder.wrap(output));
		} catch (IOException e) {
			logger.error("msg", e);
		}
	}

	/**
	 * To decode, reverse these operations
	 */
	@Test
	public void testDecodeFile() {
		String tmpdir = System.getProperty("java.io.tmpdir");
		Path encodedPath = Paths.get(tmpdir, "rico@deepdata2.tmp");
		Path decodedPath = Paths.get(tmpdir, "rico@deepdata3.tmp");

		Base64.Decoder decoder = Base64.getMimeDecoder();
		try (InputStream in = Files.newInputStream(encodedPath)) {
			Files.copy(decoder.wrap(in), decodedPath);
		} catch (IOException e) {
			logger.error("msg", e);
		}
	}

	/**
	 * 将一张图片转成base64编码字符串，然后再解码回图片
	 * 
	 * @throws IOException
	 */
	@Test
	public void testEncoderThenPrint() throws IOException {
		Base64.Encoder encoder = Base64.getMimeEncoder();
		Path dir = Paths.get("D:\\Dropbox\\图片");
		Path path = Paths.get("D:\\\\Dropbox\\\\图片", "20141015134542_A8eci.png");
		String encoded = encoder.encodeToString(Files.readAllBytes(path));
		System.out.println(encoded);

		Base64.Decoder decoder = Base64.getMimeDecoder();
		byte[] bytes = decoder.decode(encoded);
		Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "loser.png"), bytes, CREATE_NEW);
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
