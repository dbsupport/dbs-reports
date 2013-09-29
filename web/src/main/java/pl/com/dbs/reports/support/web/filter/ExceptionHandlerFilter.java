package pl.com.dbs.reports.support.web.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Filtr przechwytujacy wyjatki - w przypadku ich wystapienia przekierowyje na strone z bledem. 
 * Kazdemu wyjatkowi przypisany jest ID.
 * 
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ExceptionHandlerFilter implements Filter {
	private String errorPath;
	private Logger log = Logger.getLogger(ExceptionHandlerFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		  try { 
		    filterChain.doFilter(request, response);
		  } catch (Throwable ex) {
			String exceptionId = UUID.randomUUID().toString();
			log.error(exceptionId, ex);
			
			request.setAttribute("exceptionId", exceptionId);
			request.setAttribute("exception", ExceptionUtils.getStackTrace(ex));
			
			request.getRequestDispatcher(errorPath).forward(request, response);
		  }
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		errorPath = filterConfig.getInitParameter("errorPath");
	}

	@Override
	public void destroy() {
	}
}
