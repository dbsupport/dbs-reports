package pl.com.dbs.reports.absence.domain.validator;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.absence.domain.Absence;
import pl.com.dbs.reports.absence.domain.AbsenceInput;

import java.util.List;

/**
 * Is sickness code valid?
 * If no - treat like no code provided.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Order(4)
@Component
public class AbsenceSicknessCodeValidator implements AbsenceValidator {
	private MessageSource messageSource;

	private static final List<String> SICKNESS_CODES = Lists.newArrayList(
			"A", "B", "C", "D", "E", "AB", "AC", "AD", "AE", "BA", "BC", "BD", "BE", "CA", "CB", "CD", "CE", "DA", "DB", "DC", "DE", "EA", "EB", "EC", "ED"
	);

	@Autowired
	public AbsenceSicknessCodeValidator(MessageSource messageSource) {
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

		String code = absence.getSicknessCode();
		if (!Strings.isNullOrEmpty(code) && !SICKNESS_CODES.contains(code)) {
			String msg = messageSource.getMessage("absence.validation.sickness.code.invalid", null, null);
			throw new AbsenceValidationException(msg, absence);
		}
	}
}
