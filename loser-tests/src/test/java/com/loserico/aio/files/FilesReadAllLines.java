package com.loserico.aio.files;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FilesReadAllLines {

	public static void main(String[] args) {
		Path wiki_path = Paths.get("C:/tutorial/wiki", "wiki.txt");
		try {
			List<String> lines = Files.readAllLines(wiki_path, UTF_8);
			for (String line : lines) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.out.println(e);
		}

	}
}