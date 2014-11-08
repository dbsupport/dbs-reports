/**
 * 
 */
package pl.com.dbs.reports.profile.web.view;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public class ClientProfileEncoding {
	private String inencoding;
	private String outencoding;
	private String value;
	
	public ClientProfileEncoding(String inencoding, String outencoding, String value) {
		this.inencoding = inencoding;
		this.outencoding = outencoding;
		this.value = value;
	}

	public String getInencoding() {
		return inencoding;
	}

	public String getOutencoding() {
		return outencoding;
	}

	public String getValue() {
		return value;
	}
}
