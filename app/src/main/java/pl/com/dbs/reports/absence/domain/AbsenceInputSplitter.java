package pl.com.dbs.reports.absence.domain;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 1:  HS 									HE				  	| [AS, HE-1], [HE, AE]
 * 2:  HS												 HE		| [AS, AE]
 * 3:			HS							HE					| [AS, HS-1], [HS, HE], [HE+1, AE]
 * 4:			HS										 HE		| [AS, HS-1], [HS, AE]
 * ----|---------|---------------------------|-----------|-------
 *     AS												 AE
 *
 * AS - absence start (dateFrom)
 * AE - absence end (dateTo)
 * HS - hospital start (hospitalDateFrom)
 * HE - hospital end (hospitalDateTo)
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Component
public class AbsenceInputSplitter {

	public List<AbsenceInput.AbsenceRange> split(final AbsenceInput absence) {
		List<AbsenceInput.AbsenceRange> ranges = Lists.newArrayList();

		Date dateFrom = absence.getDateFromAsDate();
		Date dateTo = absence.getDateToAsDate();
		Date hospitalDateFrom = absence.hasHospitalDateFrom()?absence.getHospitalDateFromAsDate():dateFrom;
		Date hospitalDateTo = absence.hasHospitalDateTo()?absence.getHospitalDateToAsDate():dateTo;
		boolean hospital = absence.hasHospitalDateFrom();

		if (hospitalDateFrom.compareTo(dateFrom) < 0) {
			hospitalDateFrom = dateFrom;
		}

		if (hospitalDateTo.compareTo(dateTo) > 0) {
			hospitalDateTo = dateTo;
		}

		if (hospitalDateFrom.compareTo(dateFrom) <= 0 && hospitalDateTo.compareTo(dateTo) < 0) {
			ranges.add(new AbsenceInput.AbsenceRange(dateFrom, addDay(hospitalDateTo, -1), hospital));
			ranges.add(new AbsenceInput.AbsenceRange(hospitalDateTo, dateTo));
		} else if (hospitalDateFrom.compareTo(dateFrom) <= 0 && hospitalDateTo.compareTo(dateTo) >= 0) {
			ranges.add(new AbsenceInput.AbsenceRange(dateFrom, dateTo));
		} else if (hospitalDateFrom.compareTo(dateFrom) > 0 && hospitalDateTo.compareTo(dateTo) < 0) {
			ranges.add(new AbsenceInput.AbsenceRange(dateFrom, addDay(hospitalDateFrom, -1)));
			ranges.add(new AbsenceInput.AbsenceRange(hospitalDateFrom, hospitalDateTo, hospital));
			ranges.add(new AbsenceInput.AbsenceRange(addDay(hospitalDateTo, 1), dateTo));
		} else if (hospitalDateFrom.compareTo(dateFrom) > 0 && hospitalDateTo.compareTo(dateTo) >= 0) {
			ranges.add(new AbsenceInput.AbsenceRange(dateFrom, addDay(hospitalDateFrom, -1)));
			ranges.add(new AbsenceInput.AbsenceRange(hospitalDateFrom, dateTo, hospital));
		} else {
			throw new IllegalStateException("Illegal range dates! absence.start:"+dateFrom+" absence.end:"+dateTo+" hospital.start:"+hospitalDateFrom+" hospital.end:"+hospitalDateTo);
		}

		return ranges;
	}

	private Date addDay(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
}
