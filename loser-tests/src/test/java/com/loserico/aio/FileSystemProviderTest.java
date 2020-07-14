package com.loserico.aio;

import java.nio.file.spi.FileSystemProvider;
import java.util.List;

import org.junit.Test;

public class FileSystemProviderTest {

	/**
	 * A Java implementation provides concrete FileSystemProvider subclasses
	 * that describe different kinds of file system providers. If you’re curious
	 * about the file system providers supported by your Java implementation,
	 * run the application
	 * 
	 * When I run this application, I observe the following output:
	 * sun.nio.fs.WindowsFileSystemProvider@18f65a4
	 * com.sun.nio.zipfs.ZipFileSystemProvider@168ddbd
	 * 
	 * This output tells me two things: FileSystems that interface to the file
	 * systems that are native to my Windows 7 operating system are obtained
	 * from the WindowsFileSystemProvider subclass. Also, I can obtain
	 * FileSystems that are based on ZIP files.
	 */
	@Test
	public void testFileSystemProviders() {
		List<FileSystemProvider> fileSystemProviders = FileSystemProvider.installedProviders();
		for (FileSystemProvider fileSystemProvider : fileSystemProviders) {
			System.out.println(fileSystemProvider);
		}
	}
}
