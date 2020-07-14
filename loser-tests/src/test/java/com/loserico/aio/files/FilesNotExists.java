package com.loserico.aio.files;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class FilesNotExists {

	public static void main(String[] args) {
		Path path = FileSystems.getDefault().getPath("C:/tutorial/Java/JavaFX", "Demo.txt");
		boolean pathNotexists = Files.notExists(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
		System.out.println("Not exists? " + pathNotexists);
	}
}