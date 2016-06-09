/**
 * 
 */
package pl.com.dbs.reports.support.web.client;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * What client (dbs, auchan..) is on runtime..
 * 
 * FIXXME: client packages should have required classes, resorces etc.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public class ClientTag extends RequestContextAwareTag {
	private static final long serialVersionUID = -5594388951260185733L;
	private static final Pattern NAME_PATTERN = Pattern.compile("^(.+)-\\d(\\.\\d)*(\\.\\d)*(-SNAPSHOT)*$",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	private static final String CLIENT_DEFAULT = "dbs";
	private String names = CLIENT_DEFAULT;
	
	@Override
	protected int doStartTagInternal() throws Exception {
		return isSame()?EVAL_BODY_INCLUDE:SKIP_BODY;
	}

//    private void evaluate() throws Exception {
//        JspWriter out = pageContext.getOut();
//        out.write("<div data-build-number=\"\" data-build-timestamp=\"\"></div>");
//    }
	
	private boolean isSame() {
		String client = retrieveClient();
		
		StringTokenizer st = new StringTokenizer(names.trim(), ",");
		while (st.hasMoreTokens()) {
			if (client.equalsIgnoreCase(st.nextToken())) return true;
		}
		
		return false;
	}

	private String retrieveClient() {
		String fullclient = getRequestContext().getMessage("app.client");
		Matcher matcher = NAME_PATTERN.matcher(fullclient);
		if (matcher.find()) return matcher.group(1);
		return CLIENT_DEFAULT;
	}

	public void setNames(String names) {
		this.names = names;
	}			



}
