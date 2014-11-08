/**
 * 
 */
package pl.com.dbs.reports.support.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * Wszystkie interceptory powinny omijac statyczne zasoby.
 * W tym celu jest ten abstrakt.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public abstract class ResourceExcludingHandlerInterceptorAdapter implements HandlerInterceptor {
	private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
 
    public void doAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
    
    public void doPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
 
    public boolean doPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	return true;
    }
 
    private boolean isResourceHandler(Object handler) {
    	return handler instanceof ResourceHttpRequestHandler;
    }
 
    @Override
    public final void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	if (!isResourceHandler(handler)) {
    		doPostHandle(request, response, handler, modelAndView);
    	}
    }
 
    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	return isResourceHandler(handler) ? true : doPreHandle(request, response, handler);
    }
 
    @Override
    public final void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	if (!isResourceHandler(handler)) {
    		doAfterCompletion(request, response, handler, ex);
    	}
    }
 
    public boolean post(HttpServletRequest request) {
    	return METHOD_POST.equalsIgnoreCase(request.getMethod());
    }
 
    public boolean get(HttpServletRequest request) {
    	return METHOD_GET.equalsIgnoreCase(request.getMethod());
    }
}