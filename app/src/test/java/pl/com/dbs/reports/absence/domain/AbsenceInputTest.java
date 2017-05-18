/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceInputTest {

	@Test
	public void should_trim_code() {
		AbsenceInput absence = new AbsenceInput();
		absence.sicknessCode = "/-t-\\'R_u  $! M;'@#$%p*())(^";

		String code = absence.getTrimedSicknessCode();

		assertTrue(code!=null);
		assertTrue(code.equals("TRUMP"));
	}
}
