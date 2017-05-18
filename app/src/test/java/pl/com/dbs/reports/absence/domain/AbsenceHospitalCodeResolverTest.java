/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceHospitalCodeResolverTest {
	private static final String S_CODE = "S";
	private static final String A_CODE = "A";
	private static final String B_CODE = "B";
	private static final String C_CODE = "C";
	private static final String D_CODE = "D";
	private static final String E_CODE = "E";

	private static final String AS_CODE = "AS";
	private static final String SD_CODE = "SD";
	private static final String AB_CODE = "AB";
	private static final String AD_CODE = "AD";

	@Test
	public void should_not_consider_hospital() throws IOException {
		String code = "QWERT";
		String result = AbsenceHospitalCodeResolver.resolve(code, false);

		assertTrue(code.equals(result));
	}

	@Test
	public void should_return_s() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("", true);
		assertTrue(S_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve(null, true);
		assertTrue(S_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("C", true);
		assertTrue(S_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("E", true);
		assertTrue(S_CODE.equals(result));
	}

	@Test
	public void should_return_as() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("A", true);
		assertTrue(AS_CODE.equals(result));
	}

	@Test
	public void should_return_b() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("B", true);
		assertTrue(B_CODE.equals(result));
	}

	@Test
	public void should_return_sd() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("D", true);
		assertTrue(SD_CODE.equals(result));
	}

	@Test
	public void should_return_ab() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("AB", true);
		assertTrue(AB_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("BA", true);
		assertTrue(AB_CODE.equals(result));
	}

	@Test
	public void should_return_ad() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("AD", true);
		assertTrue(AD_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("DA", true);
		assertTrue(AD_CODE.equals(result));
	}

	@Test
	public void should_return_d() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("ED", true);
		assertTrue(D_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("DE", true);
		assertTrue(D_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("DX", true);
		assertTrue(D_CODE.equals(result));
	}

	@Test
	public void should_return_c() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("AC", true);
		assertTrue(C_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("CA", true);
		assertTrue(C_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("BC", true);
		assertTrue(C_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("CB", true);
		assertTrue(C_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("EC", true);
		assertTrue(C_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("CE", true);
		assertTrue(C_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("XC", true);
		assertTrue(C_CODE.equals(result));
	}

	@Test
	public void should_return_e() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("AE", true);
		assertTrue(E_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("EA", true);
		assertTrue(E_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("BE", true);
		assertTrue(E_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("EB", true);
		assertTrue(E_CODE.equals(result));

		result = AbsenceHospitalCodeResolver.resolve("EX", true);
		assertTrue(E_CODE.equals(result));
	}

	@Test
	public void should_return_null() throws IOException {
		String result = AbsenceHospitalCodeResolver.resolve("QWERTY", true);
		assertNull(result);

		result = AbsenceHospitalCodeResolver.resolve("Z", true);
		assertNull(result);

		result = AbsenceHospitalCodeResolver.resolve("AA", true);
		assertNull(result);
	}
}
