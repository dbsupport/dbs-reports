/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import com.google.common.base.Strings;
import com.sun.org.apache.bcel.internal.util.BCELifier;

/**
 *
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceHospitalCodeResolver {
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

	public static String resolve(String code, boolean hospital) {
		if (!hospital) return code;

		if (Strings.isNullOrEmpty(code)) return S_CODE;

		if (code.length()==1) {
			if (same(A_CODE, code)) return AS_CODE;
			else if (same(B_CODE, code)) return B_CODE;
			else if (same(C_CODE, code)||same(E_CODE, code)) return S_CODE;
			else if (same(D_CODE, code)) return SD_CODE;
		} else if (code.length()==2) {
				 if (has(A_CODE, code)&&has(B_CODE, code)) return AB_CODE;
			else if (has(A_CODE, code)&&has(D_CODE, code)) return AD_CODE;
			else if (has(D_CODE, code)) return D_CODE;
			else if (has(C_CODE, code)) return C_CODE;
			else if (has(E_CODE, code)) return E_CODE;
		}

		return null;
	}

	private static boolean has(String code1, String code2) {
		return !Strings.isNullOrEmpty(code1)
				&&!Strings.isNullOrEmpty(code2)
				&&code2.toUpperCase().contains(code1.toUpperCase());
	}

	private static boolean same(String code1, String code2) {
		return !Strings.isNullOrEmpty(code1)
				&&!Strings.isNullOrEmpty(code2)
				&&code2.equalsIgnoreCase(code1);
	}
}
