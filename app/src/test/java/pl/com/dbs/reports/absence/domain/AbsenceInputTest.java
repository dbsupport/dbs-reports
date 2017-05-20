/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

	@Test
	public void should_have_available_code() {
		AbsenceInput absence = new AbsenceInput();
		absence.sicknessCode = "/-A-\\'_  $! B;'@#$%*())(^";

		String code = absence.getAvailableSicknessCode();
		assertNotNull(code);
		assertTrue(code.equals("AB"));
	}

	@Test
	public void should_have_unavailable_code() {
		AbsenceInput absence = new AbsenceInput();
		absence.sicknessCode = "/-t-\\'R_u  $! M;'@#$%p*())(^";

		assertNull(absence.getAvailableSicknessCode());
	}


	@Test
	public void should_have_motifa_chr() {
		AbsenceInput absence = AbsenceInputTestBuilder.builder()
				.relationshipCode("")
				.build();

		assertTrue(absence.getMotifa().equals(AbsenceInput.MOTIF_CHR));
	}

	@Test
	public void should_have_motifa_odr() {
		AbsenceInput absence = AbsenceInputTestBuilder.builder()
				.relationshipCode("ABC")
				.build();

		assertTrue(absence.getMotifa().equals(AbsenceInput.MOTIF_ODR));
	}

}
