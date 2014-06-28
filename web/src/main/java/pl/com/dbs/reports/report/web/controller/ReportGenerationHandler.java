/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Error marshaling..
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@ControllerAdvice
public class ReportGenerationHandler {
	private static final Logger logger = Logger.getLogger(ReportGenerationHandler.class);
	@Autowired private ReportGenerationHelper reportGenerationHelper;
	
	@ExceptionHandler(ReportGenerationException.class)
    public ModelAndView handleDynamicFormException(HttpServletRequest request, Exception ex) {
		logger.error("Requested URL="+request.getRequestURL());
		logger.error("Exception Raised="+ex);
		
		Throwable e = ex.getCause();
         
        ModelAndView modelAndView = new ModelAndView();
        
        modelAndView.addObject("exceptionId",  e.getClass());
        modelAndView.addObject("exception",  e.getStackTrace());
        modelAndView.addObject("url", request.getRequestURL());
         
        modelAndView.setViewName("error/error");
        return modelAndView;
    }  
	
	
}
