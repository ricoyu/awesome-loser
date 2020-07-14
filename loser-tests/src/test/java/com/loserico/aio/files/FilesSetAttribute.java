package com.loserico.aio.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FilesSetAttribute {

	public static void main(String[] args) throws Exception {

		Path path = Paths.get("C:/tutorial/Java/JavaFX", "Topic.txt");
		long time = LocalDateTime.of(2015, 11, 9, 04, 12).toInstant(ZoneOffset.of("+8")).toEpochMilli();
		FileTime fileTime = FileTime.fromMillis(time);
		try {
			Files.setAttribute(path, "basic:lastModifiedTime", fileTime, LinkOption.NOFOLLOW_LINKS);
			Files.setAttribute(path, "basic:creationTime", fileTime, LinkOption.NOFOLLOW_LINKS);
			Files.setAttribute(path, "basic:lastAccessTime", fileTime, LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
			System.err.println(e);
		}

	}
}