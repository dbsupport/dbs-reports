/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import com.google.common.base.Strings;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceOutputBuilder {
	private String pesel;
	private String series;
	private String number;
	private String nip;

	private String nudoss;
	private String socdos;
	private String matcle;

	private Date dateFrom;
	private Date dateTo;
	private Date date;
	private Date wholeAbsenceDateFrom;
	private Date wholeAbsenceDateTo;

	private String sicknessCode;
	private String motifa;

	private Date employmentDate;
	private boolean hospital = false;


	private AbsenceOutputBuilder() {	}

	public static AbsenceOutputBuilder builder() {
		return new AbsenceOutputBuilder();
	}

	public AbsenceOutputBuilder pesel(String pesel) {
		this.pesel = pesel;
		return this;
	}

	public AbsenceOutputBuilder series(String series) {
		this.series = series;
		return this;
	}

	public AbsenceOutputBuilder number(String number) {
		this.number = number;
		return this;
	}

	public AbsenceOutputBuilder nip(String nip) {
		this.nip = nip;
		return this;
	}

	public AbsenceOutputBuilder sicknessCode(String sicknessCode) {
		this.sicknessCode = sicknessCode;
		return this;
	}

	public AbsenceOutputBuilder motifa(String motifa) {
		this.motifa = motifa;
		return this;
	}

	public AbsenceOutputBuilder nudoss(String nudoss) {
		this.nudoss = nudoss;
		return this;
	}

	public AbsenceOutputBuilder matcle(String matcle) {
		this.matcle = matcle;
		return this;
	}

	public AbsenceOutputBuilder socdos(String socdos) {
		this.socdos = socdos;
		return this;
	}

	public AbsenceOutputBuilder hrdata(List<String> hrdata) {
		if (hrdata!=null) {
			this.nudoss(hrdata.get(0));
			this.matcle(hrdata.get(1));
			this.socdos(hrdata.get(2));
		}
		return this;
	}

	public AbsenceOutputBuilder employmentDate(Date date) {
		this.employmentDate = date;
		return this;
	}

	public AbsenceOutputBuilder date(Date date) {
		this.date = date;
		return this;
	}

	public AbsenceOutputBuilder dateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public AbsenceOutputBuilder dateTo(Date dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public AbsenceOutputBuilder wholeAbsenceDateFrom(Date wholeAbsenceDateFrom) {
		this.wholeAbsenceDateFrom = wholeAbsenceDateFrom;
		return this;
	}

	public AbsenceOutputBuilder wholeAbsenceDateTo(Date wholeAbsenceDateTo) {
		this.wholeAbsenceDateTo = wholeAbsenceDateTo;
		return this;
	}

	public AbsenceOutputBuilder hospital(boolean hospital) {
		this.hospital = hospital;
		return this;
	}

	public AbsenceOutput build() {
		AbsenceOutput absence = new AbsenceOutput();
		absence.pesel = pesel;
		absence.series = series;
		absence.number = number;
		absence.nip = nip;

		absence.nudoss = nudoss;
		absence.matcle = matcle;
		absence.socdos = socdos;

		absence.dateFrom = dateFrom;
		absence.dateTo = dateTo;
		absence.date = date;
//		absence.wholeAbsenceDateFrom = wholeAbsenceDateFrom;
//		absence.wholeAbsenceDateTo = wholeAbsenceDateTo;

		absence.sicknessCode = AbsenceHospitalCodeResolver.resolve(sicknessCode, hospital);
		absence.motifa = motifa;

		absence.employmentDate = employmentDate;
		return absence;
	}


}
