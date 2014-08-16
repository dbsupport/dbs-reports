/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternForm;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.web.form.ReportGenerationForm;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.form.DFormBuilder;
import pl.com.dbs.reports.support.web.form.inflater.FieldInflater;

/**
 * Generate dynamic form... 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Component
public class ReportGenerationHelper {
	private static final Logger logger = Logger.getLogger(ReportGenerationHelper.class);
	@Autowired private PatternService patternService;
	@Autowired private Set<FieldInflater> inflaters;
	@Autowired private Alerts alerts;
	@Autowired private MessageSource messageSource;
	
//	public ReportGenerationForm constructMockForm(Long id) {
//		return constructMockForm(patternService.find(id));
//	}
//	
//	/**
//	 * Generate without inflations..
//	 */
//	public ReportGenerationForm constructMockForm(ReportPattern pattern) {
//		ReportGenerationForm result = new ReportGenerationForm();
//		if (pattern==null) return null;
//		
//		ReportPatternForm form = pattern.getForm();
//		if (form!=null) {
//			try {
//				Set<FieldInflater> inflaters = new HashSet<FieldInflater>();
//				inflaters.add(new FieldInflaterMock());
//				DFormBuilder<ReportGenerationForm> builder = new DFormBuilder<ReportGenerationForm>(form.getContent(), ReportGenerationForm.class).add(inflaters);
//				
//				result = builder.build().getForm();
//				
//				result.reset(pattern);
//			} catch (Exception e) {
//				throw new ReportGenerationException(e);
//			}
//			
//		} else {
//			logger.warn("No form found for dynamic form generation!");
//		}
//		return result;		
//	}
	
	/**
	 * with inflating data..
	 */
	public ReportGenerationForm constructFullForm(long id) {
		ReportPattern pattern = patternService.find(id);
		return constructFullForm(pattern);
	}
	
	/**
	 * generate with inflater..
	 */
	public ReportGenerationForm constructFullForm(ReportPattern pattern) {
		if (pattern==null) return null;
		ReportGenerationForm result = new ReportGenerationForm();
		
		//..only one form allowed..
		ReportPatternForm form = pattern.getForm();
		if (form!=null) {
			try {
				DFormBuilder<ReportGenerationForm> builder = new DFormBuilder<ReportGenerationForm>(form.getContent(), ReportGenerationForm.class).add(inflaters);
				
				result = builder.build().getForm();
				
				result.reset(pattern);
			} catch (Exception e) {
				throw new ReportGenerationException(e);
			}
			
		} else {
			logger.warn("No pattern found for dynamic form generation!");
		}
		return result;
	}	
	
	/**
	 * 
	 */
	public void exception(Throwable e, HttpSession session) {
		if (e instanceof PatternFactoryNotFoundException) {
			alerts.addError(session, "report.pattern.import.factory.error", ((PatternFactoryNotFoundException)e).getFactory());
			logger.error("report.pattern.import.factory.error:"+e.getMessage());
		} else if (e instanceof IOException) {
			alerts.addError(session, "report.pattern.import.file.ioexception", e.getMessage());
			logger.error("report.pattern.import.file.ioexception"+e.getMessage());
		} else if (e instanceof PatternValidationException) {
			String msg = e.getMessage();
			if (((PatternValidationException) e).getCode()!=null) {
				if (!((PatternValidationException) e).getParams().isEmpty()) 
					msg = messageSource.getMessage(((PatternValidationException) e).getCode(), ((PatternValidationException) e).getParams().toArray(), null);
				else msg = messageSource.getMessage(((PatternValidationException) e).getCode(), null, null);
				
				alerts.addError(session, msg);
				logger.error(msg);
			}
		} else if (e instanceof JAXBException) {
			alerts.addError(session, "report.execute.jaxbexception", ((JAXBException)e).getLinkedException().getMessage());
			logger.error("report.execute.jaxbexception:"+((JAXBException)e).getLinkedException().getMessage());
		} else {
			alerts.addError(session, "report.pattern.import.file.unknown", e.getMessage());
			logger.error("report.pattern.import.file.unknown:"+e.getMessage());
		}
	}	
 
}
