package pl.com.dbs.reports.absence.domain;

import com.google.common.collect.Lists;
import pl.com.dbs.reports.absence.domain.validator.AbsenceValidationException;

import java.util.List;

/**
 * Processing results.
 *
 */
public class AbsenceResult {
	List<AbsenceOutput> absences = Lists.newArrayList();
	List<AbsenceError> errors = Lists.newArrayList();

	public AbsenceResult addError(AbsenceValidationException e) {
		AbsenceErrorBuilder builder = AbsenceErrorBuilder.builder().description(e.getMessage());
		if (e.hasAbsence()) {
			Absence absence = e.getAbsence();
			if (AbsenceInput.class.equals(absence.getClass())) {
				AbsenceInput input = (AbsenceInput)absence;

				builder.pesel(input.getPesel());
				builder.series(input.getSeries());
				builder.number(input.getNumber());
				builder.nip(input.getNip());

				builder.date(input.getDate());
				builder.dateFrom(input.getDateFrom());
				builder.dateTo(input.getDateTo());

				builder.sicknessCode(input.getSicknessCode());
				builder.motifa(input.getMotifa());


			} else if (AbsenceOutput.class.equals(absence.getClass())) {
				AbsenceOutput output = (AbsenceOutput)absence;

				builder.pesel(output.getPesel());
				builder.series(output.getSeries());
				builder.number(output.getNumber());
				builder.nip(output.getNip());

				builder.socdos(output.getSocdos());
				builder.matcle(output.getMatcle());

				builder.dateFrom(output.getDateFrom());
				builder.dateTo(output.getDateTo());
				builder.date(output.getDate());

				builder.sicknessCode(output.getSicknessCode());
				builder.motifa(output.getMotifa());
			} else {
				throw new IllegalStateException("Unknown type of absence!");
			}

		}
		errors.add(builder.build());
		return this;
	}

	public AbsenceResult addAbsence(AbsenceOutput absence) {
		absences.add(absence);
		return this;
	}

	public List<AbsenceOutput> getAbsences() {
		return absences;
	}


	public boolean hasAbsences() {
		return absences!=null && !absences.isEmpty();
	}

	public List<AbsenceError> getErrors() {
		return errors;
	}

	public boolean hasErrors() {
		return errors!=null && !errors.isEmpty();
	}

}
