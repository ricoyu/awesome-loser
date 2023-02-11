package com.loserico.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2023-02-05 11:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SftpTest {
	
	private static ChannelSftp sftp;
	
	@BeforeClass
	public static void setup() {
		JSch jSch = new JSch();
		try {
			//jSch.setKnownHosts("C:\\Users\\yuxh\\.ssh\\known_hosts");
			Session session = jSch.getSession("root", "192.168.100.101");
			session.setPassword("qianyu14");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
			System.out.println("Connected");
		} catch (JSchException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testUploadFile() {
		String file = "D:\\ebook";
		String remoteDir = "/opt/openresty/nginx/html/";
		try {
			sftp.put(file, remoteDir);
			sftp.exit();
		} catch (SftpException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testDownload() {
		String remoteDir = "/opt/openresty/nginx/html/";
		try {
			sftp.get(remoteDir+"index.html", "d://");
		} catch (SftpException e) {
			throw new RuntimeException(e);
		}
	}
}
