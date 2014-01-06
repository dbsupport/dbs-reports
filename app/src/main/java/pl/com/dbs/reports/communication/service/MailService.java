/**
 * 
 */
package pl.com.dbs.reports.communication.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * ! w obecnej wersji nie wymagane !
 * Sends mails..
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
//@Service("communication.mail.service")
public class MailService {
	private static final Logger logger = Logger.getLogger(MailService.class);
    @Autowired private JavaMailSender  sender;
	
	public void send() {
		logger.info("Sending email..");
        
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper wiadomoscPomocnik = new MimeMessageHelper(wiadomosc, coding);
//        try {
//            wiadomoscPomocnik.setFrom(nadawca);
//            wiadomoscPomocnik.setReplyTo(nadawca);
//            wiadomoscPomocnik.setTo(adresat);
//            wiadomoscPomocnik.setSubject(temat);
//            wiadomoscPomocnik.setText(tresc, czyHtml);
//        } catch (MessagingException e) {
//            throw new CEmailWyjatek("Blad przygotowywania maila", e);
//        }
//        try {
//            mailSender.send(wiadomosc);
//        } catch(MailException e) {
//            throw new CEmailWyjatek("Blad wyslania maila", e);
//        }     		
	}
}
