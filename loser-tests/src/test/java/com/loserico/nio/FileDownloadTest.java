package com.loserico.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class FileDownloadTest {

	@Test
	public void testDownloadViaApacheHttpUtils() throws IOException {
		String imageUrl = "https://deepdata.b0.upaiyun.com/head-icon/guardian/hjja5sIjDa1bXnD120170824094759.png";
		File toFile = Files.createTempFile("avatar", "png").toFile();
		FileUtils.copyURLToFile(new URL(imageUrl), toFile, 10000, 10000);
		System.out.println(toFile);
	}
	
	@Test
	public void testDownloadViaNIO() throws IOException {
		String imageUrl = "https://deepdata.b0.upaiyun.com/head-icon/guardian/hjja5sIjDa1bXnD120170824094759.png";
		URL fromUrl = new URL(imageUrl);
		ReadableByteChannel rbc = Channels.newChannel(fromUrl.openStream());
		File toFile = Files.createTempFile("avatar", "png").toFile();
		FileOutputStream fos = new FileOutputStream(toFile);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		rbc.close();
		System.out.println(toFile);
	}
}
