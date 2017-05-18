package pl.com.dbs.reports.absence.domain.validator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.MessageSource;
import pl.com.dbs.reports.absence.domain.AbsenceOutput;
import pl.com.dbs.reports.absence.domain.AbsenceOutputBuilder;
import pl.com.dbs.reports.absence.domain.AbsenceInput;
import pl.com.dbs.reports.absence.domain.AbsenceInputTestBuilder;
import pl.com.dbs.reports.api.support.db.SqlExecutor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceEmployedValidatorTest {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private MessageSource messageSource;
	private SqlExecutor<Map<String, String>> executor;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void doBeforeEachTestCase() {
		messageSource = mock(MessageSource.class);
		when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).then(returnsFirstArg());

		executor = mock(SqlExecutor.class);
	}

	@Test
	public void should_not_call_validator() throws AbsenceValidationException {
		verify(executor, times(0)).query(anyString(), any(Object[].class));

		AbsenceEmployedValidator validator = new AbsenceEmployedValidator(executor, messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.build();

		validator.validate(absence, null);
	}


	@Test
	public void should_detect_unemployment() throws AbsenceValidationException, ParseException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.not.employed.in.absences"));
		thrown.expect(Matchers.hasProperty("skip", is(true)));

		List<Map<String, Object>> results = Lists.newArrayList();
		Map<String, Object> row = Maps.newHashMap();
		row.put("EMPLOYED", 0);
		results.add(row);
		when(executor.query(anyString(), any(Object[].class))).thenReturn(results);

		AbsenceEmployedValidator validator = new AbsenceEmployedValidator(executor, messageSource);
		AbsenceOutputBuilder builder = AbsenceOutputBuilder.builder();
		AbsenceOutput absence = builder.nudoss("NUDOSS12345")
				.dateFrom(DATE_FORMAT.parse("2017-05-01"))
				.build();

		validator.validate(absence, null);
	}

	@Test
	public void should_detect_employment() throws AbsenceValidationException, ParseException {
		List<Map<String, Object>> results = Lists.newArrayList();
		Map<String, Object> row = Maps.newHashMap();
		row.put("EMPLOYED", 1);
		results.add(row);
		when(executor.query(anyString(), any(Object[].class))).thenReturn(results);

		AbsenceEmployedValidator validator = new AbsenceEmployedValidator(executor, messageSource);
		AbsenceOutputBuilder builder = AbsenceOutputBuilder.builder();
		AbsenceOutput absence = builder.nudoss("NUDOSS12345")
				.dateFrom(DATE_FORMAT.parse("2017-05-01"))
				.build();

		validator.validate(absence, null);
	}

}
