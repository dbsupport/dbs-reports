/**
 * 
 */
package pl.com.dbs.reports.asset.api;

/**
 * Any asset.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface Asset {
	/**
	 * Asset id.
	 */
	long getId();

	/**
	 * A name of content
	 */
	String getName();
	
	/**
	 * Optional path
	 */
	String getPath();
	
	/**
	 * Content
	 */
	byte[] getContent();
}
