package com.loserico.aio;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * The Java Path interface is part of the Java NIO 2 update which Java NIO received in
 * Java 6 and Java 7. The Java Path interface was added to Java NIO in Java 7. The
 * Path interface is located in the java.nio.file package, so the fully qualified name
 * of the Java Path interface is java.nio.file.Path.
 * 
 * A Java Path instance represents a path in the file system. A path can point to
 * either a file or a directory. A path can be absolute or relative. An absolute path
 * contains the full path from the root of the file system down to the file or
 * directory it points to. A relative path contains the path to the file or directory
 * relative to some other path. Relative paths may sound a bit confusing.
 * 
 * The java.nio.file.Path interface represents a hierarchical path to a file that may
 * not exist. It optionally starts with a name element identifying a file system
 * hierarchy and optionally continues with a sequence of directory elements separated
 * by a separator character. The name element that is farthest from the root of the
 * directory hierarchy is the name of a directory or other kind of file. The other
 * name elements are directory names.
 * 
 * In many ways the java.nio.file.Path interface is similar to the java.io.File class,
 * but there are some minor differences. In many cases though, you can replace the use
 * of the File class with use of the Path interface.
 * 
 * @author Rico Yu
 * @since 2016-12-11 14:52
 * @version 1.0
 *
 */
public class PathTest {

	/**
	 * In order to use a java.nio.file.Path instance you must create a Path instance.
	 * You create a Path instance using a static method in the Paths class
	 * (java.nio.file.Paths) named Paths.get().
	 */
	@Test
	public void testPathsGet() {
		// Creating an absolute path is done by calling the Paths.get() factory
		// method with the absolute file as parameter.
		Path path = Paths.get("D:\\Loser\\loser-io\\test.txt");
		// The first example creates a Java Path instance which points to the
		// path (directory) D:\Loser
		Path relativePath = Paths.get("D:\\", "Loser");
		System.out.println(relativePath.toString());

		/*
		 * When working with relative paths there are two special codes you can use
		 * inside the path string. . .. The . code means "current directory".
		 * 
		 * @formatter:on
		 * 
		 * Then the absolute path the Java Path instance corresponds to will be the
		 * directory in which the application executing the above code is executed.
		 * 
		 * If the . is used in the middle of a path string it just means the same
		 * directory as the path was pointing to at that point. Here is an Path
		 * example illustrating that:
		 * 
		 * Path currentDir = Paths.get("d:\\data\\projects\.\a-project"); This path
		 * will correspond to the path:
		 * 
		 * d:\data\projects\a-project
		 */
		Path currentDir = Paths.get(".");
		System.out.println(currentDir);
		System.out.println(currentDir.toAbsolutePath());

		/*
		 * The .. code means "parent directory" or "one directory up". Here is a Path
		 * Java example illustrating that:
		 */
		Path parentPath = Paths.get("..");

		String originalPath = "d:\\data\\projects\\a-project\\..\\another-project";

		/*
		 * @of The normalize() method of the Path interface can normalize a
		 * path. Normalizing means that it removes all the . and .. codes in the
		 * middle of the path string, and resolves what path the path string
		 * refers to. path1 = d:\data\projects\a-project\..\another-project
		 * path2 = d:\data\projects\another-project
		 * 
		 * @on
		 */
		Path path1 = Paths.get(originalPath);
		System.out.println("path1 = " + path1);
		Path path2 = path1.normalize();
		System.out.println("path2 = " + path2);
	}
	
	/*
	 * Creating an Instance of java.nio.file.Path
	 * As I mentioned before, java.nio.file.Paths is the creator for java.nio.file.Path. It provides two factory methods:
	 * 
	 * static Path get(String first, String … more)
	 * static Path get(URI uri)
	 * Those can be used to get an instance of java.nio.file.Path. Let us look at the two ways to obtain the instance:
	 * @on
	 */
	@Test
	public void testPathCreation() {
		Path path = Paths.get("src", "main", "resources");
		Assertions.assertThat(path.isAbsolute()).isFalse();
		Assertions.assertThat(path.toString()).isEqualTo("src\\main\\resources");
	}

	@Test
	public void testPathWithFileSystem() {
		FileSystem fsDefault = FileSystems.getDefault();
		Path path = fsDefault.getPath("a", "b", "c");
		System.out.println(path);
		System.out.printf("File name: %s%n", path.getFileName());
		for (int i = 0; i < path.getNameCount(); i++) {
			System.out.println("name" + i + ": " + path.getName(i));
		}
		System.out.printf("Parent: %s%n", path.getParent());
		System.out.printf("Root: %s%n", path.getRoot());
		System.out.printf("SubPath [0, 2): %s%n", path.subpath(0, 2));
	}

	@Test
	public void testRelativeAbsolute() {
		FileSystem fsDefault = FileSystems.getDefault();
		Path path = fsDefault.getPath("a", "b", "c");
		System.out.println(path);
		System.out.printf("Absolute: %b%n", path.isAbsolute());
		System.out.printf("Root: %s%n", path.getRoot());
		System.out.println();
		for (Path root : fsDefault.getRootDirectories()) {
			path = fsDefault.getPath(root.toString(), "a", "b", "c");
			System.out.println(path);
			System.out.printf("Absolute: %b%n", path.isAbsolute());
			System.out.printf("Root: %s%n", path.getRoot());
			System.out.println();
		}
	}

	@Test
	public void test2Absolute() {
		Path path = Paths.get("a", "b", "c");
		System.out.printf("Path: %s%n", path.toString());
		System.out.printf("Absolute: %b%n", path.isAbsolute());
		System.out.println();
		path = path.toAbsolutePath();
		System.out.printf("Path: %s%n", path.toString());
		System.out.printf("Absolute: %b%n", path.isAbsolute());
	}

	/**
	 * normalize() is useful for removing redundancies from a path. For example,
	 * reports/./2015/jan includes the redundant “.” (current directory) element. When
	 * normalized, this path becomes the shorter reports/2015/jan.
	 */
	@Test
	public void testNormalize() {

	}

	/**
	 * relativize() creates a relative path between two paths. For example, given
	 * current directory jan in the reports/2015/jan hierarchy, the relative path to
	 * navigate to reports/2016/mar is ../../2016/mar.
	 */
	@Test
	public void testRelativize() {

	}

	/**
	 * resolve() is the inverse of relativize(). It lets you join a partial path (a
	 * path without a root element) to another path. For example, resolving apr
	 * against reports/2015 results in reports/2015/apr.
	 */
	@Test
	public void testResolve() {

	}

	@Test
	public void testNormalizeRelativizeResolve() {
		Path path1 = Paths.get("reports", ".", "2015", "jan");
		System.out.println(path1); // reports\.\2015\jan
		/*
		 * normalize() is useful for removing redundancies from a path. For example,
		 * reports/./2015/jan includes the redundant “.” (current directory) element.
		 * When normalized, this path becomes the shorter reports/2015/jan.
		 */
		System.out.println(path1.normalize()); // reports\2015\jan
		path1 = Paths.get("reports", "2015", "..", "jan");
		System.out.println(path1.normalize());// reports\jan
		System.out.println();
		path1 = Paths.get("reports", "2015", "jan");
		System.out.println(path1);// reports\2015\jan
		/*
		 * relativize() creates a relative path between two paths. For example, given
		 * current directory jan in the reports/2015/jan hierarchy, the relative path
		 * to navigate to reports/2016/mar is ../../2016/mar.
		 */
		System.out.println(path1.relativize(Paths.get("reports", "2016", "mar"))); // ..\..\2016\mar
		try {
			Path root = FileSystems.getDefault().getRootDirectories().iterator().next();
			if (root != null) {
				System.out.printf("Root: %s%n", root.toString());
				Path path = Paths.get(root.toString(), "reports", "2016", "mar");
				System.out.printf("Path: %s%n", path);
				/*
				 * The output reveals IllegalArgumentException, which is thrown from
				 * relativize() when it cannot relativize its Path argument against
				 * the current Path. It cannot do so when one of the Paths has a root
				 * element.
				 */
				System.out.println(path1.relativize(path));
			}
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		}
		System.out.println();
		path1 = Paths.get("reports", "2015");
		System.out.println(path1);
		/*
		 * resolve() is the inverse of relativize(). It lets you join a partial path
		 * (a path without a root element) to another path. For example, resolving apr
		 * against reports/2015 results in reports/2015/apr.
		 */
		System.out.println(path1.resolve("apr"));
		System.out.println();
		Path path2 = Paths.get("reports", "2015", "jan");
		System.out.println(path2);
		System.out.println(path2.getParent());
		/*
		 * @of
		 * Furthermore, Path declares the following methods to resolve a path
		 * string against the current path’s parent path:  
		 * Path resolveSibling(Path other)  
		 * Path resolveSibling(String other)
		 * @on
		 */
		System.out.println(path2.resolveSibling(Paths.get("mar")));
		System.out.println(path2.resolve(Paths.get("mar")));
	}

	@Test
	public void testAdditional() {
		/*
		 * first obtains a reference to the current file system and uses this
		 * reference to create a pair of Path objects.
		 */
		Path path1 = Paths.get("a", "b", "c");
		Path path2 = Paths.get("a", "b", "c", "d");
		System.out.printf("path1: %s%n", path1.toString());
		System.out.printf("path2: %s%n", path2.toString());
		System.out.printf("path1.equals(path2): %b%n", path1.equals(path2));
		System.out.printf("path1.equals(path2.subpath(0, 3)): %b%n", path1.equals(path2.subpath(0, 3)));
		/*
		 * You can also compare paths to determine whether they are equal or which
		 * path alphabetically precedes the other path.
		 */
		System.out.printf("path1.compareTo(path2): %d%n", path1.compareTo(path2));
		System.out.printf("path1.startsWith(\"x\"): %b%n", path1.startsWith("x"));
		System.out.printf("path1.startsWith(Paths.get(\"a\"): %b%n", path1.startsWith(Paths.get("a")));
		System.out.printf("path2.endsWith(\"d\"): %b%n", path2.startsWith("d"));
		System.out.printf("path2.endsWith(Paths.get(\"c\", \"d\"): " + "%b%n", path2.endsWith(Paths.get("c", "d")));
		System.out.printf("path2.toUri(): %s%n", path2.toUri());
		Path path3 = Paths.get(".");
		System.out.printf("path3: %s%n", path3.toString());
		try {
			System.out.printf("path3.toRealPath(): %s%n", path3.toRealPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * mods/BlackOpsUnleashed/icons/yoda_icon.bmp -> icons/yoda_icon.bmp
	 */
	@Test
	public void testSubPath() {
		Path iconPath = Paths.get("mods/BlackOpsUnleashed/icons/yoda_icon.bmp");
		System.out.println("iconPath.getNameCount(): " + iconPath.getNameCount());
		iconPath = iconPath.subpath(2, iconPath.getNameCount());
		System.out.println(iconPath);
	}

	/*
	 * code
	 * @throws IOException
	 */
	@Test
	public void testRealPath() throws IOException {
		final String newPathName = "tutorial/java/demo.txt";
		Path path = Paths.get(newPathName);
		System.out.println("path: " + path);
		/*
		 * path必须真实存在，否则抛NoSuchFileException
		 */
		Path realPath = path.toRealPath(NOFOLLOW_LINKS);
		System.out.println("realPath: " + realPath);
	}
}
