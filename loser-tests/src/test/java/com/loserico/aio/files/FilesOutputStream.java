package com.loserico.aio.files;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

/**
 * @of
 * In the following code shows how to use 
 * 		Files.newOutputStream(Path path, OpenOption ... options) 
 * method.
 * @on
 * 
 * @author Loser
 * @since Aug 13, 2016
 * @version
 *
 */
public class FilesOutputStream {

	public static void main(String[] args) throws IOException {
		Path path = Paths.get("tutorial/Java", "demo.txt");
		String demo = "tutorial \nString: from java2s.com";
		if (Files.notExists(path, NOFOLLOW_LINKS)) {
			Files.createDirectories(path.getParent());
		}

		// using NIO.2 unbuffered stream
		byte data[] = demo.getBytes(UTF_8);
		try (OutputStream outputStream = Files.newOutputStream(path)) {
			outputStream.write(data);
		} catch (IOException e) {
			System.err.println(e);
		}

	}
}