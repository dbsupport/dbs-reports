package pl.com.dbs.reports.absence.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.absence.domain.Absence;
import pl.com.dbs.reports.absence.domain.AbsenceInput;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Absences validation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Order(1)
@Component
public class AbsenceDatesValidator implements AbsenceValidator {
	private MessageSource messageSource;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	public AbsenceDatesValidator(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public boolean supports(final Absence absence) {
		return absence.getClass().equals(AbsenceInput.class);
	}

	@Override
	public void validate(final Absence input, final List<? extends Absence> absences) throws AbsenceValidationException {
		if (!supports(input)) return;

		AbsenceInput absence = (AbsenceInput)input;

		if (!absence.hasDateFrom() || !absence.hasDateTo()) {
			String msg = messageSource.getMessage("absence.validation.dates.missing", null, null);
			throw new AbsenceValidationException(msg, absence).skip().stop();
		}

		Date dateFrom = absence.getDateFromAsDate();
		Date dateTo = absence.getDateToAsDate();

		if (dateFrom.compareTo(dateTo) > 0) {
			String dateFromString = DATE_FORMAT.format(dateFrom);
			String dateToString = DATE_FORMAT.format(dateTo);
			String msg = messageSource.getMessage("absence.validation.dates.invalid", new Object[]{dateFromString, dateToString}, null);
			throw new AbsenceValidationException(msg, absence).skip();
		}

		if (absence.hasHospitalDates()) {
			Date hospitalDateFrom = absence.getHospitalDateFromAsDate();
			Date hospitalDateTo = absence.getHospitalDateToAsDate();

			if ((absence.hasHospitalDateFrom() && !absence.hasHospitalDateTo())
					|| (!absence.hasHospitalDateFrom() && absence.hasHospitalDateTo())) {
				String msg = messageSource.getMessage("absence.validation.dates.hospital.missing", null, null);
				throw new AbsenceValidationException(msg, absence);
			}

			if (hospitalDateFrom.compareTo(hospitalDateTo) > 0) {
				String dateFromString = DATE_FORMAT.format(hospitalDateFrom);
				String dateToString = DATE_FORMAT.format(hospitalDateTo);
				String msg = messageSource.getMessage("absence.validation.dates.hospital.invalid", new Object[]{dateFromString, dateToString}, null);
				throw new AbsenceValidationException(msg, absence);
			}
		}
	}
}
