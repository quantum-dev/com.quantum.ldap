package com.quantum.ldap.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Classe permettant d'envoyer un mail.
 */
public class Mailer {
	private final static String MAILER_VERSION = "Java";

	public static boolean sendMailSMTP(String smtpServer, 
			final String username,
			final String password,
			String fromMailAddress, 
			String toMailAddress, 
			String subject,
			String content,
			boolean debug) {
		boolean result = false;
		try {

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", smtpServer);
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.ssl.enable","true");
			props.put("mail.smtp.username", username);
			props.put("mail.smtp.password", password);
			props.put("mail.smtp.ssl.enable","true");

//			props.put("mail.smtp.socketFactory.port", "587");
//			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//			props.put("mail.smtp.socketFactory.fallback", "false");
			
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}}
			);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromMailAddress));
			InternetAddress[] internetAddresses = new InternetAddress[1];
			internetAddresses[0] = new InternetAddress(toMailAddress);
			message.setRecipients(Message.RecipientType.TO, internetAddresses);
			message.setSubject(subject);
			message.setText(content);
			message.setHeader("X-Mailer", MAILER_VERSION);
			message.setSentDate(new Date());
			session.setDebug(debug);
			Transport.send(message);
			result = true;
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		Mailer.sendMailSMTP("smtp.domain.com", 
				"username", "password", 
				"source@test.com", 
				"target@test.com", 
				"Test", "No content", true);
	}
}