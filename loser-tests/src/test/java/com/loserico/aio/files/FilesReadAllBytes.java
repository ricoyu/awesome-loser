package com.loserico.aio.files;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesReadAllBytes {

	public static void main(String[] args) {
		Path wiki_path = Paths.get("C:/tutorial/wiki", "wiki.txt");
		try {
			byte[] wikiArray = Files.readAllBytes(wiki_path);
			String wikiString = new String(wikiArray, UTF_8);
			System.out.println(wikiString);
		} catch (IOException e) {
			System.out.println(e);
		}

	}
}