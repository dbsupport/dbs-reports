/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

/**
 *
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceInputTestBuilder {
	private String pesel;
	private String nip;
	private String sicknessCode;
	private String relationshipCode;
	private String number;
	private String nudoss;
	private String date;
	private String dateFrom;
	private String dateTo;
	private String hospitalDateFrom;
	private String hospitalDateTo;

	private AbsenceInputTestBuilder() {	}

	public static AbsenceInputTestBuilder builder() {
		return new AbsenceInputTestBuilder();
	}

	public AbsenceInputTestBuilder pesel(String pesel) {
		this.pesel = pesel;
		return this;
	}

	public AbsenceInputTestBuilder nip(String nip) {
		this.nip = nip;
		return this;
	}

	public AbsenceInputTestBuilder sicknessCode(String sicknessCode) {
		this.sicknessCode = sicknessCode;
		return this;
	}

	public AbsenceInputTestBuilder number(String number) {
		this.number = number;
		return this;
	}

	public AbsenceInputTestBuilder nudoss(String nudoss) {
		this.nudoss = nudoss;
		return this;
	}

	public AbsenceInputTestBuilder relationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
		return this;
	}

	public AbsenceInputTestBuilder dateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public AbsenceInputTestBuilder dateTo(String dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public AbsenceInputTestBuilder date(String date) {
		this.date = date;
		return this;
	}

	public AbsenceInputTestBuilder hospitalDateFrom(String hospitalDateFrom) {
		this.hospitalDateFrom = hospitalDateFrom;
		return this;
	}

	public AbsenceInputTestBuilder hospitalDateTo(String hospitalDateTo) {
		this.hospitalDateTo = hospitalDateTo;
		return this;
	}


	public AbsenceInput build() {
		AbsenceInput absence = new AbsenceInput();
		absence.pesel = pesel;
		absence.nip = nip;
		absence.sicknessCode = sicknessCode;
		absence.relationshipCode = relationshipCode;
		absence.number = number;
		absence.dateFrom = dateFrom;
		absence.dateTo = dateTo;
		absence.date = date;
		absence.hospitalDateFrom = hospitalDateFrom;
		absence.hospitalDateTo = hospitalDateTo;
		return absence;
	}


}
