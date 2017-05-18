/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.com.dbs.reports.report.PatternFactoryDefaultTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceImporterTest {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final String PATH = "pl/com/dbs/reports/absence/reader/test/";
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void should_read_file() throws IOException {
		AbsenceImporter reader = new AbsenceImporter();
		String filename = "Zaswiadczenia lekarskie ubezpieczonych platnika 11.05.2017.csv";
		File file = read(PATH +filename);

		List<AbsenceInput> absences = reader.read(file);

		assertTrue(absences.size() == 22);
		AbsenceInput row1 = absences.get(0);
		assertTrue(row1.getSeries().equals("ZZ"));
		assertTrue(row1.getNumber().equals("8954459"));
		assertTrue(row1.getPesel().equals("86050714913"));
		assertTrue(row1.getNip().equals("5260309174"));
		assertTrue(row1.getSicknessCode() == null);
		//assertTrue(row1.getRelationshipCode() == null);
		assertTrue(row1.getDateFrom().equals("2017-05-10"));
		assertTrue(row1.getDateTo().equals("2017-05-14"));
		assertTrue(row1.getDate().equals("2017-05-10"));

		assertNull(row1.getHospitalDateFrom());
		assertNull(row1.getHospitalDateTo());

//		assertTrue(row1.getFirstName().equals("KRZYSZTOF"));
//		assertTrue(row1.getLastName().equals("SARNEK"));


		AbsenceInput row2 = absences.get(1);
		assertTrue(row2.getSicknessCode().equalsIgnoreCase("A"));
		assertTrue(row2.getTrimedSicknessCode().equals("A"));

		AbsenceInput row5 = absences.get(4);
		assertTrue(row5.getHospitalDateFrom().equals("2017-05-05"));
		assertTrue(row5.getHospitalDateTo().equals("2017-05-10"));

		AbsenceInput row17 = absences.get(16);
		assertTrue(row17.getSicknessCode().equalsIgnoreCase("A/B"));
		assertTrue(row17.getTrimedSicknessCode().equals("AB"));

		AbsenceInput row22 = absences.get(21);
//		assertTrue(row22.getRelationshipCode().equals("1"));
	}

	@Test
	public void should_read_quoted_values() throws IOException {
		AbsenceImporter reader = new AbsenceImporter();
		String filename = "quoted.csv";
		File file = read(PATH +filename);

		List<AbsenceInput> absences = reader.read(file);

		assertTrue(absences.size() == 1);
		AbsenceInput row = absences.get(0);
		assertTrue(row.getSeries().equals("ZZ"));
		assertTrue(row.getNumber().equals("8954459"));
		assertTrue(row.getPesel().equals("86050714913"));
	}

	@Test
	public void should_read_string() throws IOException {
		AbsenceImporter reader = new AbsenceImporter();
		String content = "Seria,Numer,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32\n" +
				"ZZ,8954459,86050714913,KRZYSZTOF,SARNEK,1,,,80043,GDAŃSK,TRAKT ŚW. WOJCIECHA,87,1,2017-05-10,2017-05-14,,,1,,,,,1,5260309174,PRZYCH.LEK.NOWY CHEŁM,80807,GDANSK,CHAŁUBIŃSKIEGO,23,,2351553,KATARZYNA,NYCEK,2017-05-10\n";

		List<AbsenceInput> absences = reader.read(content);

		assertTrue(absences.size() == 1);
		AbsenceInput row1 = absences.get(0);
		assertTrue(row1.getSeries().equals("ZZ"));
		assertTrue(row1.getNumber().equals("8954459"));
		assertTrue(row1.getPesel().equals("86050714913"));
		assertTrue(row1.getNip().equals("5260309174"));
		assertTrue(row1.getSicknessCode() == null);
		assertTrue(row1.getDateFrom().equals("2017-05-10"));
		assertTrue(row1.getDateTo().equals("2017-05-14"));
		assertTrue(row1.getDate().equals("2017-05-10"));
		assertNull(row1.getHospitalDateFrom());
		assertNull(row1.getHospitalDateTo());
	}

	private File read(String src) {
		File file = null;
		try {
			URL url = PatternFactoryDefaultTest.class.getClassLoader().getResource(src);
			file = new File(url.toURI());
		} catch (URISyntaxException e) {}
		if (!file.exists()) throw new IllegalStateException();
		return file;
	}


}
