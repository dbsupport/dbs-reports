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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceSicknessCodeValidatorTest {
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
		AbsenceSicknessCodeValidator validator = new AbsenceSicknessCodeValidator(messageSource);
		AbsenceOutputBuilder builder = AbsenceOutputBuilder.builder();
		AbsenceOutput absence = builder
				.build();

		assertFalse(validator.supports(absence));
	}

	@Test
	public void should_detect_missing_code() throws AbsenceValidationException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.sickness.code.invalid"));
		thrown.expect(Matchers.hasProperty("skip", is(false)));

		AbsenceSicknessCodeValidator validator = new AbsenceSicknessCodeValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder
				.sicknessCode(" ")
				.build();

		validator.validate(absence, null);
	}

	@Test
	public void should_detect_bad_code() throws AbsenceValidationException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.sickness.code.invalid"));
		thrown.expect(Matchers.hasProperty("skip", is(false)));

		AbsenceSicknessCodeValidator validator = new AbsenceSicknessCodeValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder.sicknessCode("TT").build();

		validator.validate(absence, null);
	}

	@Test
	public void should_detect_correct_code() throws AbsenceValidationException {
		AbsenceSicknessCodeValidator validator = new AbsenceSicknessCodeValidator(messageSource);
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		AbsenceInput absence = builder.sicknessCode("AB").build();

		validator.validate(absence, null);
	}
}
