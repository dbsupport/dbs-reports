package pl.com.dbs.reports.absence.domain;

import java.util.Date;

/**
 * Error processing absence.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceError {
	String pesel;
	String series;
	String number;
	String nip;

	String nudoss;
	String matcle;
	String socdos;

	String dateFrom;
	String dateTo;
	String date;

	String sicknessCode;
	String motifa;

	String 	description;

	private Date processingDate;

	public AbsenceError() {
		processingDate = new Date();
	}

	public String getPesel() {
		return pesel;
	}

	public String getSeries() {
		return series;
	}

	public String getNumber() {
		return number;
	}

	public String getNip() {
		return nip;
	}

	public String getNudoss() {
		return nudoss;
	}

	public String getMatcle() {
		return matcle;
	}

	public String getSocdos() {
		return socdos;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public String getDate() {
		return date;
	}

	public String getSicknessCode() {
		return sicknessCode;
	}

	public String getMotifa() {
		return motifa;
	}

	public String getDescription() {
		return description;
	}

	public Date getProcessingDate() {
		return processingDate;
	}
}
