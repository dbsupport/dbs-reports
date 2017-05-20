/**
 * 
 */
package pl.com.dbs.reports.communication.service;

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import pl.com.dbs.reports.communication.domain.SenderContext;

/**
 * ! w obecnej wersji nie wymagane !
 * 
 * Proxy JavaMailSender.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Deprecated
//@Component(value=SenderContext.SENDER)
public class MailSender implements JavaMailSender {
	private JavaMailSenderImpl sender;
	
	/**
	 * Renew sender properties.
	 */
	public void reconnect(SenderContext context) {
		sender = new JavaMailSenderImpl();
		sender.setDefaultEncoding("UTF-8");
		//sender.setDefaultFileTypeMap(defaultFileTypeMap);
		sender.setHost(context.getHost());
		sender.setJavaMailProperties(context.getJavaMailProperties());
		sender.setPassword(context.getPassword());
		sender.setPort(context.getPort());
		//sender.setProtocol(protocol);
		//sender.setSession(session);
		sender.setUsername(context.getUser());
	}

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		sender.send(simpleMessage);
	}

	@Override
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		sender.send(simpleMessages);
	}

	@Override
	public MimeMessage createMimeMessage() {
		return sender.createMimeMessage();
	}

	@Override
	public MimeMessage createMimeMessage(InputStream is) throws MailException {
		return sender.createMimeMessage(is);
	}

	@Override
	public void send(MimeMessage mimeMessage) throws MailException {
		sender.send(mimeMessage);
	}

	@Override
	public void send(MimeMessage[] mimeMessages) throws MailException {
		sender.send(mimeMessages);
	}

	@Override
	public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
		sender.send(mimeMessagePreparator);
	}

	@Override
	public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
		sender.send(mimeMessagePreparators);
	}

}
