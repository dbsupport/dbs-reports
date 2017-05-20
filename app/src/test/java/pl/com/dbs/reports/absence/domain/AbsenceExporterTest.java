/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import pl.com.dbs.reports.absence.domain.validator.*;
import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.report.PatternFactoryDefaultTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceExporterTest {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final String PATH = "pl/com/dbs/reports/absence/reader/test/";

	private MessageSource messageSource;
	private SqlExecutor<Map<String, String>> executor;
	private AbsenceDatesValidator datesValidator;
	private AbsenceDateValidator dateValidator;
	private AbsenceMultipleValidator multipleValidator;
	private AbsenceSicknessCodeValidator sicknessCodeValidator;
	private AbsenceEmployedValidator employedValidator;
	private AbsenceEmployeeValidator employeeValidator;

	private AbsenceValidatorProcessor validatorProcessor;

	private List<AbsenceValidator> validators = Lists.newArrayList();

	private AbsenceInputSplitter splitter;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void doBeforeEachTestCase() {
		messageSource = mock(MessageSource.class);
		when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).then(returnsFirstArg());

		executor = mock(SqlExecutor.class);

		datesValidator = new AbsenceDatesValidator(messageSource);
		dateValidator = new AbsenceDateValidator(messageSource);
		multipleValidator = new AbsenceMultipleValidator(messageSource);
		sicknessCodeValidator = new AbsenceSicknessCodeValidator(messageSource);
		employedValidator = new AbsenceEmployedValidator(executor, messageSource);
		employeeValidator = new AbsenceEmployeeValidator(executor, messageSource);

		validators.add(datesValidator);
		validators.add(dateValidator);
		validators.add(multipleValidator);
		validators.add(sicknessCodeValidator);

		validators.add(employedValidator);
		validators.add(employeeValidator);

		validatorProcessor = new AbsenceValidatorProcessor(validators);

		splitter = new AbsenceInputSplitter();
	}


	@Test
	public void should_return_no_absences_dates_missing() throws IOException {
		setEmploymentQueryResult(1);

		AbsenceExporter exporter = new AbsenceExporter(executor, validatorProcessor, splitter);

		List<AbsenceInput> inputs = Lists.newArrayList();
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		inputs.add(builder.pesel("123").build());

		AbsenceResult result = exporter.export(inputs);

		assertNotNull(result);
		assertFalse(result.hasAbsences());
		assertTrue(result.hasErrors());
		assertTrue(result.getErrors().size() == 1);
		assertTrue(result.getErrors().get(0).getDescription().equals("absence.validation.dates.missing"));
	}

	@Test
	public void should_return_no_absences_not_employed() throws IOException {
		setEmploymentQueryResult(0);

		AbsenceExporter exporter = new AbsenceExporter(executor, validatorProcessor, splitter);

		List<AbsenceInput> inputs = Lists.newArrayList();
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		inputs.add(builder
				.pesel("123")
				.sicknessCode("A")
				.date("2017-05-01")
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.build());

		AbsenceResult result = exporter.export(inputs);

		assertNotNull(result);
		assertFalse(result.hasAbsences());
		assertTrue(result.hasErrors());
		assertTrue(result.getErrors().size() == 1);
		assertTrue(result.getErrors().get(0).getDescription().equals("absence.validation.not.employed.in.absences"));
	}

	@Test
	public void should_return_1_absence() throws IOException {
		setEmploymentQueryResult(1);
		setEmployedLastDateQueryResult("2017-05-01");
		setHrDataQueryResult();

		AbsenceExporter exporter = new AbsenceExporter(executor, validatorProcessor, splitter);

		List<AbsenceInput> inputs = Lists.newArrayList();
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		inputs.add(builder
				.pesel("123")
				.nip("123456")
				.sicknessCode("A")
				.date("2017-05-01")
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.build());

		AbsenceResult result = exporter.export(inputs);

		assertNotNull(result);
		assertTrue(result.hasAbsences());
		assertFalse(result.hasErrors());
		assertTrue(result.getAbsences().size() == 1);
		assertTrue(result.getAbsences().get(0).getNudoss().equals("nudoss"));
		assertTrue(result.getAbsences().get(0).getMatcle().equals("matcle"));
		assertTrue(result.getAbsences().get(0).getSocdos().equals("socdos"));
	}

	@Test
	public void should_return_2_absence() throws IOException {
		setEmploymentQueryResult(1);
		setEmployedLastDateQueryResult("2017-05-01");
		setHrDataQueryResult();

		AbsenceExporter exporter = new AbsenceExporter(executor, validatorProcessor, splitter);

		List<AbsenceInput> inputs = Lists.newArrayList();
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput i1 = builder.pesel("123")
				.nip("123456")
				.sicknessCode("A")
				.date("2017-05-01")
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-05-02")
				.hospitalDateTo("2017-05-10")
				.build();
		inputs.add(i1);

		AbsenceResult result = exporter.export(inputs);

		assertNotNull(result);
		assertTrue(result.hasAbsences());
		assertFalse(result.hasErrors());
		assertTrue(result.getAbsences().size() == 2);
		AbsenceOutput a1 = result.getAbsences().get(0);
		assertTrue(a1.getDateFrom().compareTo(i1.getDateFromAsDate()) == 0);
		assertTrue(a1.getDateTo().compareTo(addDay(i1.getHospitalDateFromAsDate(), -1)) == 0);
		AbsenceOutput a2 = result.getAbsences().get(1);
		assertTrue(a2.getDateFrom().compareTo(i1.getHospitalDateFromAsDate()) == 0);
		assertTrue(a2.getDateTo().compareTo(i1.getDateToAsDate()) == 0);
	}

	@Test
	public void should_return_3_absence() throws IOException {
		setEmploymentQueryResult(1);
		setEmployedLastDateQueryResult("2017-05-01");
		setHrDataQueryResult();

		AbsenceExporter exporter = new AbsenceExporter(executor, validatorProcessor, splitter);

		List<AbsenceInput> inputs = Lists.newArrayList();
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput i1 = builder.pesel("123")
				.sicknessCode("A")
				.nip("123456")
				.date("2017-05-01")
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-05-02")
				.hospitalDateTo("2017-05-08")
				.build();
		inputs.add(i1);

		AbsenceResult result = exporter.export(inputs);

		assertNotNull(result);
		assertTrue(result.hasAbsences());
		assertFalse(result.hasErrors());
		assertTrue(result.getAbsences().size() == 3);
		AbsenceOutput a1 = result.getAbsences().get(0);
		assertTrue(a1.getDateFrom().compareTo(i1.getDateFromAsDate()) == 0);
		assertTrue(a1.getDateTo().compareTo(addDay(i1.getHospitalDateFromAsDate(), -1)) == 0);
		AbsenceOutput a2 = result.getAbsences().get(1);
		assertTrue(a2.getDateFrom().compareTo(i1.getHospitalDateFromAsDate()) == 0);
		assertTrue(a2.getDateTo().compareTo(i1.getHospitalDateToAsDate()) == 0);
		AbsenceOutput a3 = result.getAbsences().get(2);
		assertTrue(a3.getDateFrom().compareTo(addDay(i1.getHospitalDateToAsDate(), 1)) == 0);
		assertTrue(a3.getDateTo().compareTo(i1.getDateToAsDate()) == 0);
	}

	@Test
	public void should_process_file() throws IOException {
		setEmploymentQueryResult(1);
		setEmployedLastDateQueryResult("2017-05-01");
		setHrDataQueryResult();
		setEmployedExceptQueryResult("70120300719");

		String filename = "Zaswiadczenia lekarskie ubezpieczonych platnika 05.05.2017.test_in.csv";

		AbsenceImporter importer = new AbsenceImporter(messageSource);
		File file = read(PATH + filename);

		AbsenceExporter exporter = new AbsenceExporter(executor, validatorProcessor, splitter);

		List<AbsenceInput> inputs = importer.read(file);

		AbsenceResult result = exporter.export(inputs);

		assertNotNull(result);
		assertTrue(result.hasAbsences());
		assertTrue(result.hasErrors());

		List<AbsenceOutput> absences = result.getAbsences();
		assertTrue(absences.size() == 12);

		for (AbsenceOutput a : absences) {
			System.out.println(a);
		}

		List<AbsenceOutput> absences60040210197 = collectByPesel(absences, "60040210197");
		assertTrue(absences60040210197.size() == 2);

		List<AbsenceOutput> absences62093000028 = collectByPesel(absences, "62093000028");
		assertTrue(absences62093000028.size() == 3);

		List<AbsenceOutput> absences67042809321 = collectByPesel(absences, "67042809321");
		assertTrue(absences67042809321.size() == 2);

		List<AbsenceOutput> absences70120300719 = collectByPesel(absences, "70120300719");
		assertTrue(absences70120300719.size() == 0);

		List<AbsenceOutput> absences56121907940 = collectByPesel(absences, "56121907940");
		assertTrue(absences56121907940.size() == 1);

		List<AbsenceOutput> absences53092605017 = collectByPesel(absences, "53092605017");
		assertTrue(absences53092605017.size() == 1);

		List<AbsenceOutput> absences51030218017 = collectByPesel(absences, "51030218017");
		assertTrue(absences51030218017.size() == 1);

		List<AbsenceOutput> absences65010503688 = collectByPesel(absences, "65010503688");
		assertTrue(absences65010503688.size() == 1);

		List<AbsenceOutput> absences52062416116 = collectByPesel(absences, "52062416116");
		assertTrue(absences52062416116.size() == 1);


		List<AbsenceError> errors = result.getErrors();
		assertTrue(errors.size() == 3);

		List<AbsenceError> errorDateValidation = collectByDescription(errors, "absence.validation.date.absence.after.from");
		assertTrue(errorDateValidation.size() == 1);

		List<AbsenceError> errorSicknessCodeValidation = collectByDescription(errors, "absence.validation.sickness.code.invalid");
		assertTrue(errorSicknessCodeValidation.size() == 1);

		List<AbsenceError> errorEmployedValidation = collectByDescription(errors, "absence.validation.not.employed");
		assertTrue(errorEmployedValidation.size() == 1);
	}

	private List<AbsenceOutput> collectByPesel(List<AbsenceOutput> absences, final String pesel) {
		return Lists.newArrayList(Iterables.filter(absences, new Predicate<AbsenceOutput>() {
			@Override
			public boolean apply(AbsenceOutput input) {
				return pesel.equalsIgnoreCase(input.getPesel());
			}
		}));
	}

	private List<AbsenceError> collectByDescription(List<AbsenceError> errors, final String description) {
		return Lists.newArrayList(Iterables.filter(errors, new Predicate<AbsenceError>() {
			@Override
			public boolean apply(AbsenceError input) {
				return description.equalsIgnoreCase(input.getDescription());
			}
		}));
	}

	private void setEmploymentQueryResult(int employed) {
		List<Map<String, Object>> results = Lists.newArrayList();
		Map<String, Object> row = Maps.newHashMap();
		row.put("EMPLOYED", employed);
		results.add(row);
		when(executor.query(startsWith("SELECT CASE WHEN NVL(DATSOR, TO_DATE('0001-01-01','YYYY-MM-DD'))"), any(Object[].class))).thenReturn(results);
	}

	private void setEmployedExceptQueryResult(final String pesel) {
		final List<Map<String, Object>> notemployed = Lists.newArrayList();
		Map<String, Object> row1 = Maps.newHashMap();
		row1.put("EMPLOYEE", 0);
		notemployed.add(row1);


		final List<Map<String, Object>> employed = Lists.newArrayList();
		Map<String, Object> row2 = Maps.newHashMap();
		row2.put("EMPLOYEE", 1);
		employed.add(row2);

		when(executor.query(startsWith("SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END EMPLOYEE"), any(Object[].class))).thenAnswer(new Answer<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> answer(InvocationOnMock invocationOnMock) throws Throwable {
				Object[] args = invocationOnMock.getArguments();
				Object[] params = (Object[])args[1];
				return ((String)params[0]).equalsIgnoreCase(pesel) ? notemployed : employed;
			}
		});

	}

	private void setEmployedLastDateQueryResult(String date) {
		List<Map<String, Object>> results = Lists.newArrayList();
		Map<String, Object> row = Maps.newHashMap();
		row.put("DATENT", date);
		results.add(row);
		when(executor.query(startsWith(AbsenceExporter.EMPLOYEE_LATEST_EMPLOYMENT_DATE_QUERY.substring(0, 30)), any(Object[].class))).thenReturn(results);
	}


	private void setHrDataQueryResult() {
		final Map<String, String> NUDOSS = Maps.newHashMap();
		NUDOSS.put("62093000028","689872610");
		NUDOSS.put("53092605017","689861407");
		NUDOSS.put("56121907940","689861408");
		NUDOSS.put("65010503688","689861410");
		NUDOSS.put("67042809321","689861413");
		NUDOSS.put("51030218017","689861457");
		NUDOSS.put("60040210197","689861492");
		NUDOSS.put("52062416116","689861494");

		final Map<String, String> MATCLE = Maps.newHashMap();
		MATCLE.put("62093000028","POL0010015");
		MATCLE.put("53092605017","POL0010002");
		MATCLE.put("56121907940","POL0010003");
		MATCLE.put("65010503688","POL0010005");
		MATCLE.put("67042809321","POL0010008");
		MATCLE.put("51030218017","POL0010054");
		MATCLE.put("60040210197","POL0010089");
		MATCLE.put("52062416116","POL0010091");

		final Map<String, String> SOCDOS = Maps.newHashMap();
		SOCDOS.put("62093000028","POL");
		SOCDOS.put("53092605017","POL");
		SOCDOS.put("56121907940","POL");
		SOCDOS.put("65010503688","POL");
		SOCDOS.put("67042809321","POL");
		SOCDOS.put("51030218017","POL");
		SOCDOS.put("60040210197","POL");
		SOCDOS.put("52062416116","POL");

		when(executor.query(startsWith(AbsenceExporter.EMPLOYEE_QUERY.substring(0, 15)), any(Object[].class))).thenAnswer(new Answer<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> answer(InvocationOnMock invocationOnMock) throws Throwable {
				Object[] args = invocationOnMock.getArguments();
				Object[] params = (Object[])args[1];
				String pesel = (String)params[0];

				List<Map<String, Object>> results = Lists.newArrayList();
				String nudoss = NUDOSS.containsKey(pesel)? NUDOSS.get(pesel):"nudoss";
				String matcle = MATCLE.containsKey(pesel)?MATCLE.get(pesel):"matcle";
				String socdos = SOCDOS.containsKey(pesel)?SOCDOS.get(pesel):"socdos";

				Map<String, Object> row = Maps.newHashMap();
				row.put("nudoss", nudoss);
				row.put("matcle", matcle);
				row.put("socdos", socdos);
				results.add(row);

				return results;
			}
		});
	}


	private Date addDay(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
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
