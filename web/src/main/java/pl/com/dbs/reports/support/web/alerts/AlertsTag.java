/**
 * 
 */
package pl.com.dbs.reports.support.web.alerts;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.google.inject.internal.Lists;

/**
 * Renders all alerts..
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class AlertsTag extends RequestContextAwareTag {
	private static final long serialVersionUID = -5594388951260185733L;
	private boolean clean = false;
	
	
	@Override
	protected int doStartTagInternal() throws Exception {
		if (!clean&&hasAny()) {
			renderErrors();
			renderWarnings();
			renderInfos();
			renderSuccesses();
		}
		
		HttpSession session = pageContext.getSession();
		session.setAttribute(Alerts.ERRORS_KEY, null);
		session.setAttribute(Alerts.WARNINGS_KEY, null);
		session.setAttribute(Alerts.INFOS_KEY, null);
		session.setAttribute(Alerts.SUCCESSES_KEY, null);
		
		return SKIP_BODY;
	}
	
	private boolean hasAny() {
		return !retrieve(Alerts.ERRORS_KEY).isEmpty()
				||!retrieve(Alerts.INFOS_KEY).isEmpty()
				||!retrieve(Alerts.SUCCESSES_KEY).isEmpty()
				||!retrieve(Alerts.WARNINGS_KEY).isEmpty();
	}
	
	private List<String> retrieve(String key) {
		List<String> items = Lists.newArrayList();

		HttpSession session = pageContext.getSession();
		ServletRequest request = pageContext.getRequest();
		
		List<String> sitems = Alerts.retreiveCollection(session, key);
		if (sitems!=null) items.addAll(sitems);
		
		List<String> ritems = Alerts.retreiveCollection(request, key);
		if (ritems!=null) items.addAll(ritems);
		
		return items;
	}
	
	
	private void renderErrors() throws IOException {
		List<String> items = retrieve(Alerts.ERRORS_KEY);
		for (String msg : items) renderError(msg);
	}
	
	private void renderWarnings() throws IOException {
		List<String> items = retrieve(Alerts.WARNINGS_KEY);
		for (String msg : items) renderWarning(msg);
	}	
	
	private void renderInfos() throws IOException {
		List<String> items = retrieve(Alerts.INFOS_KEY);
		for (String msg : items) renderInfo(msg);		
	}		
	
	private void renderSuccesses() throws IOException {
		List<String> items = retrieve(Alerts.SUCCESSES_KEY);
		for (String msg : items) renderSuccess(msg);			
	}		
	
	
	private void renderError(String msg) throws IOException {
		JspWriter out = pageContext.getOut();
		out.println("<div class=\"alert alert-danger\">");
		out.println("<i class=\"icon-remove-sign\"></i>");
		out.println(msg);
		out.println("</div>");		
	}
	
	private void renderWarning(String msg) throws IOException {
		JspWriter out = pageContext.getOut();
		out.println("<div class=\"alert alert-warning\">");
		out.println("<i class=\"icon-warning-sign\"></i>");
		out.println(msg);
		out.println("</div>");		
	}
	
	private void renderInfo(String msg) throws IOException {
		JspWriter out = pageContext.getOut();
		out.println("<div class=\"alert alert-info\">");
		out.println("<i class=\"icon-exclamation-sign\"></i>");
		out.println(msg);
		out.println("</div>");		
	}		
	
	private void renderSuccess(String msg) throws IOException {
		JspWriter out = pageContext.getOut();
		out.println("<div class=\"alert alert-success\">");
		out.println("<i class=\"icon-ok-sign\"></i>");
		out.println(msg);
		out.println("</div>");		
	}			

	public void setClean(boolean clean) {
		this.clean = clean;
	}

}
