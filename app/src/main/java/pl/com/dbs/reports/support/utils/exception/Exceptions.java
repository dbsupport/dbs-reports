/**
 * 
 */
package pl.com.dbs.reports.support.utils.exception;

/**
 * Utils...
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class Exceptions {
	private static int MAX = 1024;
	
	public static String stack(Exception e) {
		return stack(e, MAX);
	}
	
	public static String stack(Exception e, int max) {
		StringBuilder sb = new StringBuilder();
		if (e!=null&&e.getStackTrace()!=null) {
			for (StackTraceElement s : e.getStackTrace()) {
				sb.append(s).append(System.getProperty("line.separator"));
				if (sb.length()>max) break;
			}
		}
		return sb.toString();
	}	
}
