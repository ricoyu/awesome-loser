package com.loserico.aio.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

public class FilesSetLastModifiedTime {

	public static void main(String[] args) throws Exception {

		long time = System.currentTimeMillis();
		FileTime fileTime = FileTime.fromMillis(time);
		Path path = Paths.get("C:/tutorial/Java/JavaFX", "Topic.txt");

		try {
			Files.setLastModifiedTime(path, fileTime);
		} catch (IOException e) {
			System.err.println(e);
		}

	}
}