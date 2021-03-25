package com.loserico.nio;

import lombok.SneakyThrows;

import java.io.RandomAccessFile;
import java.nio.file.Paths;

/**
 * <p>
 * Copyright: (C), 2021-03-03 13:57
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RandomAccessFileTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		RandomAccessFile randomAccessFile =
				new RandomAccessFile(Paths.get("D:\\Learning\\awesome-loser\\loser-tests\\src\\test\\resources\\suricata-simple.yaml").toFile(), "rw");
		String s1 = randomAccessFile.readLine();
		System.out.println(s1 + "L" + randomAccessFile.getFilePointer());
		randomAccessFile.writeUTF("ens-33");
		randomAccessFile.close();
	}
}
