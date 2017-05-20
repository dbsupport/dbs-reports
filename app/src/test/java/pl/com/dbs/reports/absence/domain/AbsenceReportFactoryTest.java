package pl.com.dbs.reports.absence.domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.com.dbs.reports.report.domain.builders.inflaters.ReportTextBlockInflater;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceReportFactoryTest {
	private ReportTextBlockInflater inflater;
	private AbsenceProcessor absenceProcessor;


	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void doBeforeEachTestCase() {
		absenceProcessor = mock(AbsenceProcessor.class);
		inflater = mock(ReportTextBlockInflater.class);
	}


	@Test
	public void should_prepare_filename() {
		AbsenceReportFactory factory = new AbsenceReportFactory(absenceProcessor, inflater);

		String filename = factory.filename("+{*u=Ni,a.'Eu  r0p/eJ5k:a.MU$%si_#z0)@stA&!c.^zN`iS$zcZ0<>n|a");
		assertTrue(filename.equals("unia_eur0pej5ka_musi_z0stac_zniszcz0na"));
	}
}
