package pl.com.dbs.reports.absence.domain;

import java.util.Date;

/**
 * Absence for export.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceOutput implements Absence {
	String pesel;
	String series;
	String number;
	String nip;

	String nudoss;
	String matcle;
	String socdos;

	Date dateFrom;
	Date dateTo;
	Date date;
	Date employmentDate;

	String sicknessCode;
	String motifa;
	boolean hospital = false;

	private Date processingDate;

	public AbsenceOutput() {
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

	public Date getDateFrom() {
		return dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public Date getDate() {
		return date;
	}

	public Date getEmploymentDate() {
		return employmentDate;
	}

	public String getSicknessCode() {
		return sicknessCode;
	}

	public String getMotifa() {
		return motifa;
	}

	public Date getProcessingDate() {
		return processingDate;
	}

	public boolean isHospital() {
		return hospital;
	}
}
