/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceInputSplitterTest {
	private AbsenceInputSplitter splitter;

	@Before
	public void doBeforeEachTestCase() {
		splitter = new AbsenceInputSplitter();
	}

	@Test
	public void should_have_one_range() {
		AbsenceInput absence = AbsenceInputTestBuilder.builder()
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.build();

		List<AbsenceInput.AbsenceRange> ranges = splitter.split(absence);

		assertNotNull(ranges);
		assertTrue(ranges.size() == 1);
		assertTrue(ranges.get(0).getStart().compareTo(absence.getDateFromAsDate()) == 0);
		assertTrue(ranges.get(0).getEnd().compareTo(absence.getDateToAsDate()) == 0);
	}

	@Test
	public void should_have_one_range_too() {
		AbsenceInput absence = AbsenceInputTestBuilder.builder()
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-05-01")
				.build();

		List<AbsenceInput.AbsenceRange> ranges = splitter.split(absence);

		assertNotNull(ranges);
		assertTrue(ranges.size() == 1);
		assertTrue(ranges.get(0).getStart().compareTo(absence.getDateFromAsDate()) == 0);
		assertTrue(ranges.get(0).getEnd().compareTo(absence.getDateToAsDate()) == 0);
	}

	@Test
	public void should_have_one_range_either() {
		AbsenceInput absence = AbsenceInputTestBuilder.builder()
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-04-01")
				.hospitalDateTo("2017-06-01")
				.build();

		List<AbsenceInput.AbsenceRange> ranges = splitter.split(absence);

		assertNotNull(ranges);
		assertTrue(ranges.size() == 1);
		assertTrue(ranges.get(0).getStart().compareTo(absence.getDateFromAsDate()) == 0);
		assertTrue(ranges.get(0).getEnd().compareTo(absence.getDateToAsDate()) == 0);
	}

	@Test
	public void should_have_two_ranges_as_he_ae() {
		AbsenceInput absence = AbsenceInputTestBuilder.builder()
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-05-01")
				.hospitalDateTo("2017-05-05")
				.build();

		List<AbsenceInput.AbsenceRange> ranges = splitter.split(absence);

		assertNotNull(ranges);
		assertTrue(ranges.size() == 2);
		assertTrue(ranges.get(0).getStart().compareTo(absence.getDateFromAsDate()) == 0);
		assertTrue(ranges.get(0).getEnd().compareTo(addDay(absence.getHospitalDateToAsDate(),-1)) == 0);

		assertTrue(ranges.get(1).getStart().compareTo(absence.getHospitalDateToAsDate()) == 0);
		assertTrue(ranges.get(1).getEnd().compareTo(absence.getDateToAsDate()) == 0);
	}

	@Test
	public void should_have_one_range_as_ae() {
		AbsenceInput absence = AbsenceInputTestBuilder.builder()
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-05-01")
				.hospitalDateTo("2017-05-10")
				.build();

		List<AbsenceInput.AbsenceRange> ranges = splitter.split(absence);

		assertNotNull(ranges);
		assertTrue(ranges.size() == 1);
		assertTrue(ranges.get(0).getStart().compareTo(absence.getDateFromAsDate()) == 0);
		assertTrue(ranges.get(0).getEnd().compareTo(absence.getDateToAsDate()) == 0);
	}

	@Test
	public void should_have_three_ranges_as_hs_he_ae() {
		AbsenceInput absence = AbsenceInputTestBuilder.builder()
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-05-02")
				.hospitalDateTo("2017-05-09")
				.build();

		List<AbsenceInput.AbsenceRange> ranges = splitter.split(absence);

		assertNotNull(ranges);
		assertTrue(ranges.size() == 3);
		assertTrue(ranges.get(0).getStart().compareTo(absence.getDateFromAsDate()) == 0);
		assertTrue(ranges.get(0).getEnd().compareTo(addDay(absence.getHospitalDateFromAsDate(),-1)) == 0);

		assertTrue(ranges.get(1).getStart().compareTo(absence.getHospitalDateFromAsDate()) == 0);
		assertTrue(ranges.get(1).getEnd().compareTo(absence.getHospitalDateToAsDate()) == 0);

		assertTrue(ranges.get(2).getStart().compareTo(addDay(absence.getHospitalDateToAsDate(),1)) == 0);
		assertTrue(ranges.get(2).getEnd().compareTo(absence.getDateToAsDate()) == 0);
	}

	@Test
	public void should_have_two_ranges_as_hs_ae() {
		AbsenceInput absence = AbsenceInputTestBuilder.builder()
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-05-02")
				.hospitalDateTo("2017-05-10")
				.build();

		List<AbsenceInput.AbsenceRange> ranges = splitter.split(absence);

		assertNotNull(ranges);
		assertTrue(ranges.size() == 2);
		assertTrue(ranges.get(0).getStart().compareTo(absence.getDateFromAsDate()) == 0);
		assertTrue(ranges.get(0).getEnd().compareTo(addDay(absence.getHospitalDateFromAsDate(),-1)) == 0);

		assertTrue(ranges.get(1).getStart().compareTo(absence.getHospitalDateFromAsDate()) == 0);
		assertTrue(ranges.get(1).getEnd().compareTo(absence.getDateToAsDate()) == 0);
	}

	private Date addDay(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

}
