package com.loserico.classloader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyClassLoader extends ClassLoader {
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] data = loadClassData(name);
		return super.defineClass(name, data, 0, data.length);
	}

	private byte[] loadClassData(String name) {
		name = name.replaceAll("\\.", "/");
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		try {
			in = new FileInputStream("D:\\StarLord\\Workspace\\JVMTest\\bin\\" + name + ".class");
			baos = new ByteArrayOutputStream();
			int b = 0;
			while ((b = in.read()) != -1) {
				baos.write(b);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			log.error("", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
		return null;
	}

	
}
