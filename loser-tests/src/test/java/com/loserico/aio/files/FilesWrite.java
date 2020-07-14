package com.loserico.aio.files;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

public class FilesWrite {

	@Test
	public void testWrite() {
		Path ball_path = Paths.get("C:/tutorial/photos", "ball.png");
		byte[] ball_bytes = new byte[] { (byte) 0x89, (byte) 0x50, (byte) 0x4e, };

		try {
			Files.write(ball_path, ball_bytes);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	@Test
	public void testWriteIterate() throws IOException {
        Path path = Paths.get("C:/tutorial/wiki", "wiki.txt");
        Files.createDirectories(path.toAbsolutePath().getParent());
        ArrayList<String> lines = new ArrayList<>();
        lines.add("\n");
        lines.add("tutorial");

        try {
            Files.write(path, lines, UTF_8, CREATE, APPEND);
        } catch (IOException e) {
            System.err.println(e);
        }
	}
}