package pl.com.dbs.reports.absence.domain.validator;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.absence.domain.Absence;
import pl.com.dbs.reports.absence.domain.AbsenceInput;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Many absences for given employee in given set?
 * If more than one - proceed with first (or latest according to data wystawienia).
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Order(3)
@Component
public class AbsenceMultipleValidator implements AbsenceValidator {
	private MessageSource messageSource;

	@Autowired
	public AbsenceMultipleValidator(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public boolean supports(final Absence absence) {
		return absence.getClass().equals(AbsenceInput.class);
	}

	@Override
	public void validate(final Absence input, final List<? extends Absence> inputs) throws AbsenceValidationException {
		if (!supports(input)) return;

		final AbsenceInput absence = (AbsenceInput)input;
		final List<AbsenceInput> absences = (List<AbsenceInput>)inputs;

		Map<String, Collection<AbsenceInput>> multimap = Multimaps.index(
				Iterables.filter(absences,
					new Predicate<AbsenceInput>() {
						@Override
						public boolean apply(AbsenceInput input) {
							return input.isSamePesel(absence.getPesel());
						}
					}) ,
					new Function<AbsenceInput, String>() {
						public String apply(AbsenceInput input){
							return input.getPesel();
						}
				}).asMap();

		if (multimap.size() != 1) {
			throw new IllegalStateException("No absence found for pesel " + absence.getPesel());
		}

		Map.Entry<String, Collection<AbsenceInput>> row = multimap.entrySet().iterator().next();
		Collection<AbsenceInput> aabsences = row.getValue();

		if (aabsences.size() > 1) {
			String msg = messageSource.getMessage("absence.validation.multiple.absences", new Object[]{aabsences.size(), absence.getPesel()}, null);
			throw new AbsenceValidationException(msg, absence).skip();
		}
	}
}
