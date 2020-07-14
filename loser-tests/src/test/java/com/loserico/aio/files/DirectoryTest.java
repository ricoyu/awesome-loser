package com.loserico.aio.files;

import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DirectoryTest {

	@Test
	public void testCreateSingleDirectory() {
		assertFalse(new File("Directory1").exists());
		new File("Directory1").mkdir();
		assertTrue(new File("Directory1").exists());
		new File("Directory1").delete();
	}

	/*
	 * Create a directory named “Directory2 and all its sub-directories “Sub2” and
	 * “Sub-Sub2” together.
	 */
	@Test
	public void testCreateSubDir() {
		File dir = new File("Directory2\\Sub2\\Sub-Sub2");
		assertFalse(dir.exists());
		assertFalse(dir.getParentFile().exists());
		assertFalse(dir.getParentFile().getParentFile().exists());
		dir.mkdirs();
		assertTrue(dir.exists());
		assertTrue(dir.getParentFile().exists());
		assertTrue(dir.getParentFile().getParentFile().exists());
		IOUtils.deleteDirectory(dir.getParentFile().getParentFile().getAbsolutePath());
	}
	
	@Test
	public void testCreateDirs() throws IOException {
		Path path = Paths.get("Directory1");
		assertFalse(Files.exists(path, NOFOLLOW_LINKS));
		Files.createDirectories(path);
		assertTrue(Files.exists(path, NOFOLLOW_LINKS));
		IOUtils.deleteDirectory(path);
//		Files.delete(path);
	}
	
	@Test
	public void testCreateDirs2() {
		File file = new File("Directory1");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        IOUtils.deleteDirectory(file);

        File files = new File("Directory2\\Sub2\\Sub-Sub2");
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("Multiple directories are created!");
            } else {
                System.out.println("Failed to create multiple directories!");
            }
        }
        IOUtils.deleteDirectory("Directory2");
	}
	
	@Test
	public void testGetRootDirectory() {
		Path path = IOUtils.getRootDirectory(Paths.get("first"));
		System.out.println(path);
	}
}
