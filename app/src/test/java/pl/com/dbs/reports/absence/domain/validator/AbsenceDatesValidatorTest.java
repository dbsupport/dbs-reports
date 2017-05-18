package pl.com.dbs.reports.absence.domain.validator;

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

import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceDatesValidatorTest {
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
		AbsenceDatesValidator validator = new AbsenceDatesValidator(messageSource);
		AbsenceOutputBuilder builder = AbsenceOutputBuilder.builder();
		AbsenceOutput absence = builder
				.build();

		assertFalse(validator.supports(absence));
	}


	@Test
	public void should_detect_invalid_dates() throws AbsenceValidationException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.dates.missing"));
		thrown.expect(Matchers.hasProperty("skip", is(true)));

		AbsenceDatesValidator validator = new AbsenceDatesValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.dateFrom("2017.40.40")
				.dateTo("2017-40.40")
				.build();

		validator.validate(absence, null);
	}

	@Test
	public void should_detect_missing_dates() throws AbsenceValidationException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.dates.missing"));
		thrown.expect(Matchers.hasProperty("skip", is(true)));

		AbsenceDatesValidator validator = new AbsenceDatesValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.build();

		validator.validate(absence, null);
	}

	@Test
	public void should_detect_mixed_dates() throws AbsenceValidationException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.dates.invalid"));
		thrown.expect(Matchers.hasProperty("skip", is(true)));

		AbsenceDatesValidator validator = new AbsenceDatesValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.dateFrom("2017-05-10")
				.dateTo("2017-05-01")
				.build();

		validator.validate(absence, null);
	}

	@Test
	public void should_detect_missing_hospital_dates() throws AbsenceValidationException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.dates.hospital.missing"));
		thrown.expect(Matchers.hasProperty("skip", is(false)));

		AbsenceDatesValidator validator = new AbsenceDatesValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-05-01")
				.build();

		validator.validate(absence, null);

		absence = builder
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom(null)
				.hospitalDateTo("2017-05-10")
				.build();

		validator.validate(absence, null);
	}

	@Test
	public void should_detect_mixed_hospital_dates() throws AbsenceValidationException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.dates.hospital.invalid"));
		thrown.expect(Matchers.hasProperty("skip", is(false)));

		AbsenceDatesValidator validator = new AbsenceDatesValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.dateFrom("2017-05-01")
				.dateTo("2017-05-10")
				.hospitalDateFrom("2017-05-10")
				.hospitalDateTo("2017-05-01")
				.build();

		validator.validate(absence, null);
	}
}
