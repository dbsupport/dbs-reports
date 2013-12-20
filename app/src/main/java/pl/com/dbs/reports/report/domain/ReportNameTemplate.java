/**
 * 
 */
package pl.com.dbs.reports.report.domain;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public enum ReportNameTemplate {
	FILENAME(1, "filename"), DATE_TIME(2, "date-time"), DATE(3, "date");
	
	private int id;
	private String key;
	private String vkey;
	
	private ReportNameTemplate(int id, String key) {
		this.id = id;
		this.key = key;
		this.vkey = "${"+key+"}";
	}
	
	public static ReportNameTemplate of(String key) {
		for (ReportNameTemplate e : values()) 
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
