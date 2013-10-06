/**
 * 
 */
package pl.com.dbs.reports.report.pattern;

import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternTest {

	@Test
	public void test() {
		StringTokenizer st = new StringTokenizer("init.sql", ";");
		
		while (st.hasMoreTokens()) {
			System.out.println(StringUtils.trim(st.nextToken()));
		}
		
	}

}
