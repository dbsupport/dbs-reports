package pl.com.dbs.reports.absence.domain.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.absence.domain.Absence;
import pl.com.dbs.reports.absence.domain.AbsenceResult;

import java.util.List;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Slf4j
@Component
public class AbsenceValidatorProcessor {
	private List<AbsenceValidator> validators;

	@Autowired
	public AbsenceValidatorProcessor(List<AbsenceValidator> validators) {
		this.validators = validators;
	}

	public boolean validate(final AbsenceResult result, final Absence absence, final List<? extends Absence> absences) {
		boolean skip = false;
		for (AbsenceValidator validator : validators) {
			try {
				validator.validate(absence, absences);
			} catch (AbsenceValidationException e) {
				result.addError(e);
				if (e.isSkip()) skip = true;
				if (e.isStop()) break;
			}
		}
		return skip;
	}
}
