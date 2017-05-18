package pl.com.dbs.reports.absence.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.absence.domain.Absence;
import pl.com.dbs.reports.absence.domain.AbsenceInput;
import pl.com.dbs.reports.absence.domain.AbsenceOutput;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Absences date later that start absence validation.
 *
 * Data wystawienia zwolnienia jest pozniejsza o 5 dni od daty rozpoczecia zwolnienia to do weryfikcaji ZUS.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Order(2)
@Component
public class AbsenceDateValidator implements AbsenceValidator {
	private MessageSource messageSource;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final int DAYS_SHIFT = 3;

	@Autowired
	public AbsenceDateValidator(MessageSource messageSource) {
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
		if (absence.hasHospitalDateFrom()) return;

		Date date = addDay(absence.getDateAsDate(), -DAYS_SHIFT);

		if (absence.getDateFromAsDate().compareTo(date) < 0) {
			String msg = messageSource.getMessage("absence.validation.date.absence.after.from", new Object[] {
					DATE_FORMAT.format(absence.getDateAsDate()), DAYS_SHIFT, DATE_FORMAT.format(absence.getDateFromAsDate())
			}, null);
			throw new AbsenceValidationException(msg, absence);
		}
	}

	private Date addDay(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
}
