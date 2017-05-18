package pl.com.dbs.reports.absence.domain.validator;

import pl.com.dbs.reports.absence.domain.Absence;

import java.util.List;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public interface AbsenceValidator {
	boolean supports(final Absence absence);
	void validate(final Absence absence, List<? extends Absence> absences) throws AbsenceValidationException;
}
