package com.loserico.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-07-08 14:56
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class IOCreateFileTest {
	
	private final String FILE_NAME = "src/test/resources/fileToCreate.txt";
	
	@After
	@Before
	public void testCleanUpFiles() {
		File targetFil = new File(FILE_NAME);
		targetFil.delete();
	}
	
	@SneakyThrows
	@Test
	public void testGivenUsingNio_whenCreatingFile_thenCorrect() {
		Path newFilePath = Paths.get(FILE_NAME);
		Files.createFile(newFilePath);
	}
	
	@SneakyThrows
	@Test
	public void testGivenUsingFile_whenCreatingFile_thenCorrect() {
		File newFile = new File(FILE_NAME);
		boolean created = newFile.createNewFile();
		assertTrue(created);
	}
	
	/**
	 * In this case, a new file is created when we instantiate the FileOutputStream object.
	 * If a file with a given name already exists, it will be overwritten.
	 * <p>
	 * If, however, the existing file is a directory or a new file cannot be created for any reason, then we'll get a FileNotFoundException.
	 */
	@SneakyThrows
	@Test
	public void testGivenUsingFileOutputStream_whenCreatingFile_thenCorrect() {
		try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
			
		}
	}
	
	/**
	 * The Guava solution for creating a new file is a quick one-liner as well:
	 */
	@SneakyThrows
	@Test
	public void testGivenUsingGuava_whenCreatingFile_thenCorrect() {
		com.google.common.io.Files.touch(new File(FILE_NAME));
	}
	
	@SneakyThrows
	@Test
	public void testgivenUsingCommonsIo_whenCreatingFile_thenCorrect() {
		FileUtils.touch(new File(FILE_NAME));
	}
}
