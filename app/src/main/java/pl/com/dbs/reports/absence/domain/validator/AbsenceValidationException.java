package pl.com.dbs.reports.absence.domain.validator;

import pl.com.dbs.reports.absence.domain.Absence;

/**
 * Validation of absence import/export.
 * Stop processing?
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceValidationException extends Exception {
	private Absence absence;
	private boolean skip = false;
	private boolean stop = false;

	public AbsenceValidationException(String msg) {
		super(msg);
	}

	public AbsenceValidationException(String msg, Exception e) {
		super(msg, e);
	}

	public AbsenceValidationException(String msg, Absence absence) {
		this(msg);
		this.absence = absence;
	}

	public AbsenceValidationException skip() {
		this.skip = true;
		return this;
	}

	public AbsenceValidationException stop() {
		this.stop = true;
		return this;
	}

	public Absence getAbsence() {
		return absence;
	}

	public boolean hasAbsence() {
		return absence!=null;
	}

	public boolean isSkip() {
		return skip;
	}

	public boolean isStop() {
		return stop;
	}
}
