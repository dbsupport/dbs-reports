/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceErrorBuilder {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private String pesel;
	private String series;
	private String number;
	private String nip;

	private String socdos;
	private String matcle;

	private String dateFrom;
	private String dateTo;
	private String date;

	private String sicknessCode;
	private String motifa;

	private String description;

	private AbsenceErrorBuilder() {	}

	public static AbsenceErrorBuilder builder() {
		return new AbsenceErrorBuilder();
	}

	public AbsenceErrorBuilder pesel(String pesel) {
		this.pesel = pesel;
		return this;
	}

	public AbsenceErrorBuilder series(String series) {
		this.series = series;
		return this;
	}

	public AbsenceErrorBuilder number(String number) {
		this.number = number;
		return this;
	}

	public AbsenceErrorBuilder nip(String nip) {
		this.nip = nip;
		return this;
	}

	public AbsenceErrorBuilder socdos(String socdos) {
		this.socdos = socdos;
		return this;
	}

	public AbsenceErrorBuilder matcle(String matcle) {
		this.matcle = matcle;
		return this;
	}

	public AbsenceErrorBuilder date(String date) {
		this.date = date;
		return this;
	}

	public AbsenceErrorBuilder date(Date date) {
		this.date = format(date);
		return this;
	}

	public AbsenceErrorBuilder dateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public AbsenceErrorBuilder dateFrom(Date date) {
		this.dateFrom = format(date);
		return this;
	}

	public AbsenceErrorBuilder dateTo(String dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public AbsenceErrorBuilder dateTo(Date date) {
		this.dateTo = format(date);
		return this;
	}

	public AbsenceErrorBuilder sicknessCode(String sicknessCode) {
		this.sicknessCode = sicknessCode;
		return this;
	}

	public AbsenceErrorBuilder motifa(String motifa) {
		this.motifa = motifa;
		return this;
	}


	public AbsenceErrorBuilder description(String description) {
		this.description = description;
		return this;
	}

	private String format(Date value) {
		if (value == null) return null;
		return DATE_FORMAT.format(value);
	}

	public AbsenceError build() {
		AbsenceError absence = new AbsenceError();
		absence.pesel = pesel;
		absence.series = series;
		absence.number = number;
		absence.nip = nip;

		absence.matcle = matcle;
		absence.socdos = socdos;

		absence.sicknessCode = sicknessCode;
		absence.motifa = motifa;

		absence.dateFrom = dateFrom;
		absence.dateTo = dateTo;
		absence.date = date;

		absence.description = description;
		return absence;
	}


}
