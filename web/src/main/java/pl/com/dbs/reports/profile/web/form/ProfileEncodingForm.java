/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import pl.com.dbs.reports.support.utils.separator.Separator;


/**
 * Test encoding form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileEncodingForm {
	public static final String KEY = "profileEncodingForm";
	private static final String DELIM = ";";
	private String inencodings = "UTF-8";
	private String outencodings = "UTF-8";
	private String uuid;
	
	public ProfileEncodingForm() {}
	
	private String encodings() {
		StringBuffer sb = new StringBuffer();
		Separator s = new Separator(DELIM);
		SortedMap<String, Charset> m = Charset.availableCharsets();
	    Set<String> k = m.keySet();
	    Iterator<String> i = k.iterator();
	    while (i.hasNext()) {
	       String n = i.next();
//	       Charset e = (Charset) m.get(n);
//	       String d = e.displayName();
//	       boolean c = e.canEncode();
//	       System.out.print(n+", "+d+", "+c);
//	       Set s = e.aliases();
//	       Iterator j = s.iterator();
//	       while (j.hasNext()) {
//	          String a = (String) j.next();         
//	          System.out.print(", "+a);
//	       }
//	       System.out.println("");
	       sb.append(s).append(n);
	    }
	    return sb.toString();
	}
	
	public void resetInEncodings() {
		this.inencodings = encodings();
	}
	
	public void resetOutEncodings() {
		this.outencodings = encodings();
	}	
	
	public void addInEncoding(String value) {
		this.inencodings = StringUtils.isBlank(inencodings)?value:(inencodings+DELIM+value);
	}
	
	public void addOutEncoding(String value) {
		this.outencodings = StringUtils.isBlank(outencodings)?value:(outencodings+DELIM+value);
	}
	
	public List<String> getInEncodings() {
		return split(getInencodings());
	}
	
	public List<String> getOutEncodings() {
		return split(getOutencodings());
	}

	private List<String> split(String value) {
		List<String> result = new ArrayList<String>();
		StringTokenizer t = new StringTokenizer(value, DELIM);
		while (t.hasMoreTokens()) {
			result.add(t.nextToken());
		}
		return result;
	}
	
	public String getInencodings() {
		return inencodings;
	}

	public String getOutencodings() {
		return outencodings;
	}

	public void setInencodings(String inencodings) {
		this.inencodings = inencodings;
	}

	public void setOutencodings(String outencodings) {
		this.outencodings = outencodings;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
