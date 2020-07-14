package com.loserico.aio;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Test;

public class FileSystemTest {

	@Test
	public void testDefaultFileSystem() {
		FileSystem fileSystem = FileSystems.getDefault();
		Iterable<Path> roots = fileSystem.getRootDirectories();
		for (Path path : roots) {
			System.out.println(path);
		}
		
	}
}
