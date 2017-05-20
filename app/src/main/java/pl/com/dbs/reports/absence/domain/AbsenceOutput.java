package pl.com.dbs.reports.absence.domain;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Absence for export.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceOutput implements Absence {
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final String SEPARATOR = "\",\"";

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

	public String getMatcleOrEmpty() {
		return !Strings.isNullOrEmpty(matcle)?matcle:"";
	}


	public String getSocdos() {
		return socdos;
	}

	public String getSocdosOrEmpty() {
		return !Strings.isNullOrEmpty(socdos)?socdos:"";
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

	public String getSicknessCodeOrEmpty() {
		return !Strings.isNullOrEmpty(sicknessCode)?sicknessCode:"";
	}

	public String getMotifa() {
		return motifa;
	}

	public String getMotifaOrEmpty() {
		return !Strings.isNullOrEmpty(motifa)?motifa:"";
	}

	public Date getProcessingDate() {
		return processingDate;
	}

	public boolean isHospital() {
		return hospital;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("absences.add(build(\"")
		.append(pesel).append(SEPARATOR)
		.append(number).append(SEPARATOR)
		.append(series).append(SEPARATOR)
		.append(nip).append(SEPARATOR)
		.append(DATE_FORMAT.format(dateFrom)).append(SEPARATOR)
		.append(DATE_FORMAT.format(dateTo)).append(SEPARATOR)
		.append(DATE_FORMAT.format(employmentDate)).append(SEPARATOR)
		.append(DATE_FORMAT.format(date)).append(SEPARATOR)
		.append(nudoss).append(SEPARATOR)
		.append(socdos).append(SEPARATOR)
		.append(matcle).append(SEPARATOR)
		.append(sicknessCode).append(SEPARATOR)
		.append(motifa).append("\"));");

//		.append(hospital);

		return sb.toString();
	}
}
