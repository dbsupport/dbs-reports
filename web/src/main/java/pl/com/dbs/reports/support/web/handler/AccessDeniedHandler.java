/**
 * 
 */
package pl.com.dbs.reports.support.web.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Przekieruj na ... jesli nie ma dostepu.
 * 
 * @author krzysztof.kaziura@gmail.com
 *
 */
@Component("support.access.denied.handler")
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.access.AccessDeniedHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.access.AccessDeniedException)
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
		response.sendRedirect(request.getContextPath()+"/security/noaccess");
	}

}
