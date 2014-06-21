/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

/**
 * Report template enum.
 * @see Manifest ATTRIBUTE_NAME_TEMPLATE
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public enum PatternReportNameTemplate {
	FILENAME(1, "filename"), DATE_TIME(2, "date-time"), DATE(3, "date");
	
	private int id;
	private String key;
	private String vkey;
	
	private PatternReportNameTemplate(int id, String key) {
		this.id = id;
		this.key = key;
		this.vkey = "${"+key+"}";
	}
	
	public static PatternReportNameTemplate of(String key) {
		for (PatternReportNameTemplate e : values()) 
			if (e.key.equalsIgnoreCase(key)||e.vkey.equalsIgnoreCase(key)) return e;
		return null;
	}
	
	public int getId() {
		return id;
	}

	public String getVkey() {
		return vkey;
	}
}
