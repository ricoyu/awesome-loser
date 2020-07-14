package com.loserico.aio;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

/**
 * FileSystem relies on the java.nio.file.FileStore class to provide information
 * about file stores, which are storage pools, devices, partitions, volumes,
 * concrete file systems, or other implementation-specific means of file
 * storage. A file store consists of a name, a type, space amounts (in bytes),
 * and other information.
 * 
 * Files declares the FileStore getFileStore(Path path) method to return a
 * FileStore representing the file store where the file identified by path is
 * stored. Once you have the FileStore, you can call methods to obtain amounts
 * of space, determine if the file store is read-only, and obtain the name and
 * type of the file store
 * 
 * @author Rico Yu
 * @since 2016-12-15 20:39
 * @version 1.0
 *
 */
public class FileStoreTest {

	@Test
	public void testFileStore() throws IOException {
		FileSystem fileSystem = FileSystems.getDefault();
		Iterable<Path> roots = fileSystem.getRootDirectories();
		long G = 1024 * 1024 * 1024;
		for (Path path : roots) {
			FileStore fs = Files.getFileStore(path);
			System.out.printf("Total space: %d%n", fs.getTotalSpace());
			System.out.printf("Total space(G): %d%n", fs.getTotalSpace() / G);
			System.out.printf("Unallocated space: %d%n", fs.getUnallocatedSpace());
			System.out.printf("Unallocated space(G): %d%n", fs.getUnallocatedSpace() / G);
			System.out.printf("Usable space: %d%n", fs.getUsableSpace());
			System.out.printf("Usable space(G): %d%n", fs.getUsableSpace() / G);
			System.out.printf("Read only: %b%n", fs.isReadOnly());
			System.out.printf("Name: %s%n", fs.name());
			System.out.printf("Type: %s%n%n", fs.type());
		}
	}

	/**
	 * The getFileStore() method focuses on a specific file store. If you want
	 * to iterate over all file stores for a given FileSystem object, you need
	 * to work with FileSystem’s Iterable<FileStore> getFileStores() method,
	 * which lets you iterate over all of the file stores.
	 */
	@Test
	public void testGetFileStores() {
		FileSystem fsDefault = FileSystems.getDefault();
		for (FileStore fileStore : fsDefault.getFileStores()) {
			System.out.printf("Filestore: %s%n", fileStore);
		}
	}
}
