package com.loserico.aio.files;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @of
 * In the following code shows how to use Files.newInputStream(Path path, OpenOption... options) method.
 * @on
 * @author Loser
 * @since Aug 13, 2016
 * @version
 *
 */
public class FilesInputStream {

	public static void main(String[] args) {
		Path path = Paths.get("C:/tutorial/Java", "demo.txt");

		// using NIO.2 unbuffered stream
		int n;
		try (InputStream in = Files.newInputStream(path)) {
			while ((n = in.read()) != -1) {
				System.out.print((char) n);
			}
		} catch (IOException e) {
			System.err.println(e);
		}

	}
}