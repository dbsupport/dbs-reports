package pl.com.dbs.reports.absence.domain.validator;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.MessageSource;
import pl.com.dbs.reports.absence.domain.AbsenceInput;
import pl.com.dbs.reports.absence.domain.AbsenceInputTestBuilder;
import pl.com.dbs.reports.absence.domain.AbsenceOutput;
import pl.com.dbs.reports.absence.domain.AbsenceOutputBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceDateValidatorTest {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private MessageSource messageSource;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void doBeforeEachTestCase() {
		messageSource = mock(MessageSource.class);
		when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).then(returnsFirstArg());
	}

	@Test
	public void should_not_call_validator() throws AbsenceValidationException {
		AbsenceDateValidator validator = new AbsenceDateValidator(messageSource);
		AbsenceOutputBuilder builder = AbsenceOutputBuilder.builder();
		AbsenceOutput absence = builder
				.build();

		assertFalse(validator.supports(absence));
	}

	@Test
	public void should_not_process_hospital() throws AbsenceValidationException {
		AbsenceDateValidator validator = new AbsenceDateValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.hospitalDateFrom("2017-05-01")
				.date("2017-05-12")
				.dateFrom("2017-05-01")
				.build();

		validator.validate(absence, null);
	}


	@Test
	public void should_detect_date_after() throws AbsenceValidationException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.date.absence.after.from"));
		thrown.expect(Matchers.hasProperty("skip", is(false)));

		AbsenceDateValidator validator = new AbsenceDateValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.date("2017-05-12")
				.dateFrom("2017-05-08")
				.build();

		validator.validate(absence, null);
	}

	@Test
	public void should_not_detect_date_after() throws AbsenceValidationException {
		AbsenceDateValidator validator = new AbsenceDateValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.date("2017-05-12")
				.dateFrom("2017-05-09")
				.build();

		validator.validate(absence, null);
	}

	@Test
	public void should_not_detect_date_after_too() throws AbsenceValidationException {
		AbsenceDateValidator validator = new AbsenceDateValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.date("2017-05-12")
				.dateFrom("2017-05-13")
				.build();

		validator.validate(absence, null);
	}
}
