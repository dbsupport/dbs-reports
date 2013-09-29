/**
 * 
 */
package pl.com.dbs.reports.report.api;


/**
 * Manifest file error pointing to wrong manifest attribute.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternManifestValidationException extends Exception {
	private static final long serialVersionUID = -5553968663222700803L;
	private String attr;
	
	public PatternManifestValidationException(String attr) {
		super();
		this.attr = attr;
	}

	public String getAttr() {
		return attr;
	}

}
