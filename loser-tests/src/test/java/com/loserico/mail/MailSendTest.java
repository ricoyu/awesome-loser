package com.loserico.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.SneakyThrows;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * <p>
 * Copyright: (C), 2020-08-27 16:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MailSendTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		InternetAddress[] tos = new InternetAddress[1];
		tos[0] = new InternetAddress("ricoyu_521@163.com");
		
		Properties props = new Properties();
		//1.1设置邮件发送的协议
		props.put("mail.transport.protocol", "smtp");
		
		//1.2设置发送邮件的服务器
		String host = "smtp.exmail.qq.com";
		props.put("mail.smtp.host", host);
		//1.3需要经过授权，也就是有户名和密码的校验
		props.put("mail.smtp.auth", "true");
		//1.4下面一串是发送邮件用465端口，如果不写就是以25端口发送
		int port = 465;
		props.put("mail.smtp.port", port);
		
		// 兼容旧数据：若465端口没有指定是否加密，则默认使用SSL加密
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.starttls.enable", "true");
		
		MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
		mailSSLSocketFactory.setTrustedHosts(new String[]{host});
		props.put("mail.smtp.socketFactory", mailSSLSocketFactory);
		props.put("mail.smtp.socketFactory.port", port);
		
		//1.5认证信息
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("yuxh@idss-cn.com", "Yuxuehua20131120");
			}
		});
		
		// 2、创建邮件对象
		Message message = new MimeMessage(session);
		//2.1设置发件人
		message.setFrom(new InternetAddress("yuxh@idss-cn.com"));
		//2.2设置收件人 TO-发送    CC-抄送   BCC-密送
		message.setRecipients(MimeMessage.RecipientType.TO, tos);
		//2.4设置邮件的主题
		message.setSubject("test smtps");
		//2.5设置邮件的内容
		message.setContent("this is a mail from rico", "text/html; charset=utf-8");
		// 3、发送邮件
		Transport.send(message);
	}
}
