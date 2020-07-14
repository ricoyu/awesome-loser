package com.loserico.nio.directory;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class DirectoryTest {

	@Test
	public void testDirectoryStreamFilter() {
		Path path = Paths.get("D:\\ERP\\Taidii\\export\\customer");
		//		Path path = Paths.get("C:/tutorial/Java/JavaFX");

		/*
		 * DirectoryStream.Filter<Path> dirFilter = new
		 * DirectoryStream.Filter<Path>() { public boolean accept(Path path) throws
		 * IOException { return (Files.isDirectory(path,
		 * LinkOption.NOFOLLOW_LINKS)); } };
		 */

		DirectoryStream.Filter<Path> dirFilter = p -> !Files.isDirectory(p, NOFOLLOW_LINKS);
		//		DirectoryStream.Filter<Path> dirFilter = p -> Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS);
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, dirFilter)) {
			for (Path file : ds) {
				System.out.println(file.getFileName());
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
