package com.loserico.aio;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.time.Instant;
import java.time.LocalDateTime;

public class AttributeViewTest {
	
	public static void main(String[] args) {
		LocalDateTime localDateTime = null;
		localDateTime.getMonth();
	}

	/**
	 * Before working with any of these views, make sure that it’s supported.
	 * One way to accomplish this task is to call FileSystem’s Set<String>
	 * supportedFileAttributeViews() method, which returns a set of strings
	 * identifying views that are supported by the invoking FileSystem.
	 */
	@Test
	public void testViewSupport() {
		FileSystem fsDefault = FileSystems.getDefault();
		for (String view : fsDefault.supportedFileAttributeViews()) {
			System.out.println(view);
		}
	}

	/**
	 * @of
	 * You could also use Files’s 
	 * 	<V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options)
	 * method, which returns an object created from an implementation of the
	 * view interface type or null when the view isn’t supported, to accomplish this task.
	 * @on
	 */
	@Test
	public void testFileAttributeView() {
		System.out.printf("Supports basic: %b%n", isSupported(BasicFileAttributeView.class));
		System.out.printf("Supports posix: %b%n", isSupported(PosixFileAttributeView.class));
		System.out.printf("Supports acl: %b%n", isSupported(AclFileAttributeView.class));
	}

	/**
	 * isSupported() utility method that takes a java.lang.Class object
	 * representing a FileAttributeView subinterface as an argument. It returns
	 * true when the view is supported or false when it isn’t supported.
	 * 
	 * The Class argument and a Path object describing the current directory are
	 * passed to getFileAttributeView(), which returns either an object created
	 * from a class that implements the interface when the view is supported or
	 * null when it isn’t supported.
	 * 
	 * Ultimately, getFileAttributeView() provides a result for the FileSystem
	 * associated with the Path argument. Because Paths.get(".") returns a
	 * FileSystem for the default file system, isSupport() is relevant in a
	 * default file system context only.
	 * 
	 * @param clazz
	 * @return
	 */
	static boolean isSupported(Class<? extends FileAttributeView> clazz) {
		return Files.getFileAttributeView(Paths.get("."), clazz) != null;
	}

	@Test
	public void testBasicFileAttributeInBulk() throws IOException {
		Path path = Paths.get("D:\\CloudStore\\ebook");
		BasicFileAttributes bfa;
		bfa = Files.readAttributes(path, BasicFileAttributes.class);
		System.out.printf("Creation time: %s%n", bfa.creationTime());
		System.out.printf("File key: %s%n", bfa.fileKey());
		System.out.printf("Is directory: %b%n", bfa.isDirectory());
		System.out.printf("Is other: %b%n", bfa.isOther());
		System.out.printf("Is regular file: %b%n", bfa.isRegularFile());
		System.out.printf("Is symbolic link: %b%n", bfa.isSymbolicLink());
		System.out.printf("Last access time: %s%n", bfa.lastAccessTime());
		System.out.printf("Last modified time: %s%n", bfa.lastModifiedTime());
		System.out.printf("Size: %d%n", bfa.size());
	}

	@Test
	public void testSetAttribute() throws IOException {
		Path path = Paths.get("D:\\opj\\bootstrap-4.0.0-alpha.5-dist");
		boolean setAttr = true;
		System.out.printf("Creation time: %s%n", Files.getAttribute(path, "creationTime"));
		System.out.printf("File key: %s%n", Files.getAttribute(path, "fileKey"));
		System.out.printf("Is directory: %b%n", Files.getAttribute(path, "isDirectory"));
		System.out.printf("Is other: %b%n", Files.getAttribute(path, "isOther"));
		System.out.printf("Is regular file: %b%n", Files.getAttribute(path, "isRegularFile"));
		System.out.printf("Is symbolic link: %b%n", Files.getAttribute(path, "isSymbolicLink"));
		System.out.printf("Last access time: %s%n", Files.getAttribute(path, "lastAccessTime"));
		System.out.printf("Last modified time: %s%n", Files.getAttribute(path, "lastModifiedTime"));
		System.out.printf("Size: %d%n", Files.getAttribute(path, "size"));
		if (setAttr) {
			Files.setAttribute(path, "lastModifiedTime", FileTime.from(Instant.now().plusSeconds(60)));
			System.out.printf("Last modified time: %s%n", Files.getAttribute(path, "lastModifiedTime"));
		}
	}
}
