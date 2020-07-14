package com.loserico.aio.files;

import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertFalse;

/**
 * The Java NIO Files class (java.nio.file.Files) provides several methods for
 * manipulating files in the file system.
 * 
 * The java.nio.file.Files class works with java.nio.file.Path instances, so you
 * need to understand the Path class before you can work with the Files class.
 * 
 * @author Rico Yu
 * @since 2016-12-11 15:33
 * @version 1.0
 *
 */
public class FilesTest {
	private static final Logger log = LoggerFactory.getLogger(FilesTest.class);

	@Test
	public void testFilePermission() {
		Path newDirectoryPath = Paths.get("home/jstas/testPosix");

		if (!Files.exists(newDirectoryPath)) {
			Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("r-xr-----");
			FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions
					.asFileAttribute(permissions);

			try {
				Files.createDirectory(newDirectoryPath, fileAttributes);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	@Test
	public void testFileCopy() throws IOException {
		Path copyFrom = Paths.get("tutorial/Java/JavaFX", "tutor.txt");
		Path copyTo = Paths.get("tutorial/Java/Swing", "tutor.txt");

		IOUtils.write(copyFrom, "Rico is a loser!");
		IOUtils.copy(copyFrom, copyTo, REPLACE_EXISTING);
		IOUtils.deleteDirectory("tutorial");
	}

	@Test
	public void testFileCopy2() {
		Path sourcePath = Paths.get("data/logging.properties");
		if (!Files.exists(sourcePath)) {
			try {
				Files.createFile(sourcePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Path destinationPath = Paths.get("data/logging-copy.properties");
		try {
			Files.copy(sourcePath, destinationPath, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The Java NIO Files class also contains a function for moving files from
	 * one path to another. Moving a file is the same as renaming it, except
	 * moving a file can both move it to a different directory and change its
	 * name in the same operation. Yes, the java.io.File class could also do
	 * that with its renameTo() method, but now you have the file move
	 * functionality in the java.nio.file.Files class too.
	 */
	@Test
	public void testFileMove() {
		Path sourcePath = Paths.get("data/logging.properties");
		if (!Files.exists(sourcePath)) {
			try {
				Files.createFile(sourcePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Path destinationPath = Paths.get("data/logging-copy.properties");

		/*
		 * First the source path and destination path are created. The source
		 * path points to the file to move, and the destination path points to
		 * where the file should be moved to. Then the Files.move() method is
		 * called. This results in the file being moved.
		 * 
		 * Notice the third parameter passed to Files.move() . This parameter
		 * tells the Files.move() method to overwrite any existing file at the
		 * destination path. This parameter is actually optional.
		 */
		try {
			Files.move(sourcePath, destinationPath, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Source Exists? " + Files.exists(sourcePath, NOFOLLOW_LINKS));
		System.out.println("Destination Exists? " + Files.exists(destinationPath, NOFOLLOW_LINKS));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCreateLinuxFile() {
		Path newfile_2 = FileSystems.getDefault().getPath("/home/tutorial/Java/2010/demo.txt");

		// create a file with a set of specified attributes
		Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
		FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
		try {
			Files.createFile(newfile_2, attr);
		} catch (IOException e) {
			System.err.println(e);
		}

	}

	@Test
	public void testCreateTmpFile() {
		Path baseDir = FileSystems.getDefault().getPath("tutorial/tmp");
		String prefix = "Swing_";
		String sufix = ".txt";

		// get the default temporary folders path
		String defaultTmp = System.getProperty("java.io.tmpdir");
		System.out.println(defaultTmp);
		IOUtils.createDir(baseDir);

		try {
			// create a tmp file in a the base dir
			Path tempFile = Files.createTempFile(baseDir, prefix, sufix);
			System.out.println("TMP: " + tempFile.toString());

		} catch (IOException e) {
			System.err.println(e);
		}
		IOUtils.deleteDirectory(baseDir.getParent());
	}

	@Test
	public void testDeleteFile() {
		Path path = FileSystems.getDefault().getPath("tutorial/photos", "Demo.jpg");

		// delete the file
		try {
			Files.delete(path);
		} catch (IOException | SecurityException e) {
			System.err.println(e);
		}
	}

	@Test
	public void testDelete() {
		Path path = Paths.get("data/logging-copy.properties");
		try {
			if (Files.exists(path, NOFOLLOW_LINKS)) {
				Files.delete(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteIfExist() {
		Path path = FileSystems.getDefault().getPath("tutorial/photos", "Demo.jpg");

		// delete if exists
		try {
			boolean success = Files.deleteIfExists(path);
			System.out.println("Delete status: " + success);
		} catch (IOException | SecurityException e) {
			System.err.println(e);
		}
	}

	/**
	 * The Files.exists() method checks if a given Path exists in the file
	 * system.
	 * 
	 * It is possible to create Path instances that do not exist in the file
	 * system. For instance, if you plan to create a new directory, you would
	 * first create the corresponding Path instance, and then create the
	 * directory.
	 * 
	 * Since Path instances may or may not point to paths that exist in the file
	 * system, you can use the Files.exists() method to determine if they do (in
	 * case you need to check that).
	 */
	@Test
	public void testExist() {
		/*
		 * This example first creates a Path instance pointing to the path we
		 * want to check if exists or not. Second, the example calls the
		 * Files.exists() method with the Path instance as the first parameter.
		 * 
		 * Notice the second parameter of the Files.exists() method. This
		 * parameter is an array of options that influence how the
		 * Files.exists() determines if the path exists or not. In this example
		 * above the array contains the LinkOption.NOFOLLOW_LINKS which means
		 * that the Files.exists() method should not follow symbolic links in
		 * the file system to determine if the path exists.
		 */
		// Path path = FileSystems.getDefault().getPath("tutorial/Java/JavaFX",
		// "Demo.txt");
		Path path = Paths.get("tutorial/Java/JavaFX", "Demo.txt");

		boolean pathExists = Files.exists(path, NOFOLLOW_LINKS);
		assertFalse(pathExists);
		System.out.println("Exists? " + pathExists);
	}

	@Test
	public void testGetAttribute() {
		Path path = Paths.get("tutorial/Java/JavaFX", "Topic.txt");
		IOUtils.write(path, "HELLO");

		// extract a single attribute with getAttribute
		try {
			long size = (Long) Files.getAttribute(path, "basic:size", NOFOLLOW_LINKS);
			System.out.println("Size: " + size);
		} catch (IOException e) {
			System.err.println(e);
		}
		IOUtils.deleteFile(path);
	}

	@Test
	public void testGetFileAttributeView() {
		Path path = Paths.get("D:/tutorial/Java/JavaFX", "Topic.txt");
		IOUtils.write(path, "HELLO");

		long time = System.currentTimeMillis();
		FileTime fileTime = FileTime.fromMillis(time);
		try {
			BasicFileAttributeView bv = Files.getFileAttributeView(path,
					BasicFileAttributeView.class);
			bv.setTimes(fileTime, fileTime, fileTime);
		} catch (IOException e) {
			System.err.println(e);
		}
		IOUtils.deleteDirectory(path.subpath(0, 1));
	}

	@Test
	public void testCreateDirectory() {
		Path path = Paths.get("data/photo");
		try {
			System.out.println("Exists? " + Files.exists(path, LinkOption.NOFOLLOW_LINKS));
			/*
			 * The first line creates the Path instance that represents the
			 * directory to create. Inside the try-catch block the
			 * Files.createDirectory() method is called with the path as
			 * parameter. If creating the directory succeeds, a Path instance is
			 * returned which points to the newly created path.
			 */
			Path newDir = Files.createDirectories(path);
			System.out.println("Exists? " + Files.exists(newDir, LinkOption.NOFOLLOW_LINKS));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @of
	 * The Files.walkFileTree() method contains functionality for traversing a
	 * directory tree recursively. The walkFileTree() method takes a Path
	 * instance and a FileVisitor as parameters. The Path instance points to the
	 * directory you want to traverse. The FileVisitor is called during
	 * traversion.
	 * 
	 * You have to implement the FileVisitor interface yourself, and pass an
	 * instance of your implementation to the walkFileTree() method. Each method
	 * of your FileVisitor implementation will get called at different times
	 * during the directory traversal. If you do not need to hook into all of
	 * these methods, you can extend the SimpleFileVisitor class, which contains
	 * default implementations of all methods in the FileVisitor interface.
	 * 
	 * Each of the methods in the FileVisitor implementation gets called at
	 * different times during traversal:
	 * 
	 * The preVisitDirectory() method is called just before visiting any
	 * directory. The postVisitDirectory() method is called just after visiting
	 * a directory.
	 * 
	 * The visitFile() mehtod is called for every file visited during the file
	 * walk. It is not called for directories - only files. The
	 * visitFileFailed() method is called in case visiting a file fails. For
	 * instance, if you do not have the right permissions, or something else
	 * goes wrong.
	 * 
	 * Each of the four methods return a FileVisitResult enum instance. The
	 * FileVisitResult enum contains the following four options:
	 * 
	 * 	CONTINUE 
	 * 	TERMINATE 
	 * 	SKIP_SIBLINGS 
	 * 	SKIP_SUBTREE
	 * 
	 * By returning one of these values the called method can decide how the
	 * file walk should continue.
	 * 
	 * CONTINUE 	 means that the file walk should continue as normal.
	 * TERMINATE 	 means that the file walk should terminate now.
	 * SKIP_SIBLINGS means that the file walk should continue but without
	 * 				 visiting any siblings of this file or directory.
	 * SKIP_SUBTREE  means that the file walk should continue but without
	 * 				 visiting the entries in this directory. 
	 * 				 This value only has a function if returned from preVisitDirectory(). 
	 * 				 If returned from any other methods it will be interpreted as a CONTINUE.
	 * @on
	 */
	@Test
	public void testWalkFileTree() {
		Path path = Paths.get("D:\\CloudStore\\ebook");
		try {
			Files.walkFileTree(path, new FileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
						throws IOException {
					System.out.println("pre visit dir: " + dir);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
						throws IOException {
					System.out.println("visit file: " + file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc)
						throws IOException {
					System.out.println("visit file failed: " + file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc)
						throws IOException {
					System.out.println("post visit directory: " + dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The Files.walkFileTree() can also be used to delete a directory with all
	 * files and subdirectories inside it. The Files.delete() method will only
	 * delete a directory if it is empty. By walking through all directories and
	 * deleting all files (inside visitFile()) in each directory, and afterwards
	 * delete the directory itself (inside postVisitDirectory()) you can delete
	 * a directory with all subdirectories and files.
	 */
	@Test
	public void testDeleteRecusive() {
		Path path = Paths.get("data");
		if (Files.exists(path, NOFOLLOW_LINKS)) {
			try {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
							throws IOException {
						System.out.println("delete file: " + file.toString());
						Files.deleteIfExists(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc)
							throws IOException {
						Files.delete(dir);
						System.out.println("delete dir: " + dir.toString());
						return FileVisitResult.CONTINUE;
					}

				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testPathChecking() {
		Path path1 = Paths.get("E:\\StormMedia");
		System.out.printf("Path1: %s%n", path1);
		System.out.printf("Exists: %b%n", Files.exists(path1));
		System.out.printf("Not exists: %b%n", Files.notExists(path1));
		System.out.printf("Is directory: %b%n", Files.isDirectory(path1));
		System.out.printf("Is executable: %b%n", Files.isExecutable(path1));
		try {
			System.out.printf("Hidden: %b%n", Files.isHidden(path1));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.printf("Is readable: %b%n", Files.isReadable(path1));
		System.out.printf("Is regular file: %b%n", Files.isRegularFile(path1));
		System.out.printf("Is writable: %b%n", Files.isWritable(path1));
		Path path2 = Paths.get("E:\\StormMedia");
		System.out.printf("Path2: %s%n", path2);
		try {
			System.out.printf("Is same path: %b%n", Files.isSameFile(path1, path2));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testName() throws IOException {
		BufferedReader br = Files.newBufferedReader(Paths.get("test.txt"));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}
}
