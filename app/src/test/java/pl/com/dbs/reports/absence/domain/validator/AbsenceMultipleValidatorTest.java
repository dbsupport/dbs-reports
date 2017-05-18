package pl.com.dbs.reports.absence.domain.validator;

import com.google.common.collect.Lists;
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

import java.util.List;
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
public class AbsenceMultipleValidatorTest {
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
		AbsenceMultipleValidator validator = new AbsenceMultipleValidator(messageSource);
		AbsenceOutputBuilder builder = AbsenceOutputBuilder.builder();
		AbsenceOutput absence = builder
				.build();

		assertFalse(validator.supports(absence));
	}

	@Test
	public void should_detect_multiple_absences() throws AbsenceValidationException {
		thrown.expect(AbsenceValidationException.class);
		thrown.expectMessage(is("absence.validation.multiple.absences"));
		thrown.expect(Matchers.hasProperty("skip", is(true)));

		AbsenceMultipleValidator validator = new AbsenceMultipleValidator(messageSource);
		List<AbsenceInput> absences = Lists.newArrayList();
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		absences.add(builder.pesel("ABc").build());
		absences.add(builder.pesel("QwE").build());
		absences.add(builder.pesel("abC").build());

		validator.validate(absences.get(0), absences);
	}

	@Test
	public void should_not_detect_multiple_absences() throws AbsenceValidationException {
		AbsenceMultipleValidator validator = new AbsenceMultipleValidator(messageSource);
		List<AbsenceInput> absences = Lists.newArrayList();
		AbsenceInputTestBuilder builder = AbsenceInputTestBuilder.builder();
		absences.add(builder.pesel("ABc").build());
		absences.add(builder.pesel("QwE").build());
		absences.add(builder.pesel("abC").build());

		validator.validate(absences.get(1), absences);
	}
}
