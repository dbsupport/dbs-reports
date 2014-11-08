package pl.com.dbs.reports.support.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.support.db.ClientDataSource;
import pl.com.dbs.reports.parameter.service.ParameterService;

/**
 * Application events listener.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Component(value="support.application.events.listener")
public class ApplicationEventsListener implements ApplicationListener<ApplicationContextEvent> {
	final static Logger log = LoggerFactory.getLogger(ApplicationEventsListener.class);
	@Autowired private ParameterService parameterService;

	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			//..application started..
			try {
				//..initialize CLIENT db connection..
				ClientDataSource datasource = event.getApplicationContext().getBean(ClientDataSource.DATASOURCE, ClientDataSource.class);
				datasource.reconnect(parameterService.getConnectionContext());
				log.info("CLIENT datasource reconnected!");
			} catch (Exception e) {
				log.error("Error initializing CLIENT db connection!", e);
			}
			
//			//..initialize mail sender.. 
//			try {
//				MailSender emailsender = event.getApplicationContext().getBean(SenderContext.SENDER, MailSender.class);
//				emailsender.reconnect(parameterService.getSenderContext());
//				log.info("Mail sender reconfigured!");
//			} catch (Exception e) {
//				log.error("Error initializing mail sender!", e);
//			}
		}
	}

}
