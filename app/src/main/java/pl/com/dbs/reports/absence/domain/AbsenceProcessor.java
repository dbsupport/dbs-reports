package pl.com.dbs.reports.absence.domain;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pl.com.dbs.reports.absence.domain.validator.AbsenceValidatorProcessor;
import pl.com.dbs.reports.api.support.db.SqlExecutor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Slf4j
@Service
public class AbsenceProcessor {
	private static final String NUDOSS = "NUDOSS";
	private static final String MATCLE = "MATCLE";
	private static final String SOCDOS = "SOCDOS";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	static final String EMPLOYEE_LATEST_EMPLOYMENT_DATE_QUERY =
			"SELECT TO_CHAR(MAX(A.DATENT), 'YYYY-MM-DD') DATENT " +
					"FROM HR.ZAES A, HR.ZANT T " +
					"WHERE " +
					"    upper(T.NUMERO) = '?'" +
					"    AND A.NUDOSS = T.NUDOSS" +
					"    AND A.DATENT <= TO_DATE('?', 'YYYY-MM-DD')";

	static final String EMPLOYEE_QUERY =
			"SELECT A.NUDOSS "+NUDOSS+", A.MATCLE "+MATCLE+", A.SOCDOS " +SOCDOS+
					"   FROM HR.ZA00 A, HR.ZANT T, HR.ZAES E, HR.ZAZZ Z, TOMEK.FIRMY F, TOMEK.LOKALIZACJE L" +
					"   WHERE " +
					"	  upper(trim(T.NUMERO)) = '?'" +
					"     AND T.NUDOSS = E.NUDOSS" +
					"     AND E.DATENT = TO_DATE('?', 'YYYY-MM-DD')'" +
					"     AND T.NUDOSS = A.NUDOSS" +
					"     AND A.NUDOSS = Z.NUDOSS" +
					"     AND Z.CENAFF = L.LOKALIZACJA" +
					"     AND L.FIRMA = F.FIRMA" +
					"     AND upper(F.NIP) = '?'";

	private AbsenceValidatorProcessor validatorProcessor;
	private SqlExecutor<Map<String, String>> executor;
	private AbsenceInputSplitter splitter;

	@Autowired
	public AbsenceProcessor(SqlExecutor<Map<String, String>> executor, 	AbsenceValidatorProcessor validatorProcessor, AbsenceInputSplitter splitter) {
		this.executor = executor;
		this.validatorProcessor = validatorProcessor;
		this.splitter = splitter;
	}

	public AbsenceResult process(final List<AbsenceInput> inputs) {
		AbsenceResult result = new AbsenceResult();
		List<AbsenceOutput> exported = Lists.newArrayList();

		for (AbsenceInput input : inputs) {
			boolean skip = validatorProcessor.validate(result, input, inputs);
			if (skip) continue;

			exported.addAll(export(input));
		}

		for (AbsenceOutput absence : exported) {
			boolean skip = validatorProcessor.validate(result, absence, inputs);
			if (skip) continue;
			result.addAbsence(absence);
		}

		return result;
	}

	private List<AbsenceOutput> export(AbsenceInput input) {
		List<AbsenceOutput> absences = Lists.newArrayList();

		for (AbsenceInput.AbsenceRange range : splitter.split(input)) {
			Date employmentDate = fetchEmploymentDate(input.getPesel(), range.getStart());

			AbsenceOutput absence = AbsenceOutputBuilder.builder()
					.pesel(input.getPesel())
					.number(input.getNumber())
					.series(input.getSeries())
					.nip(input.getNip())

					.dateFrom(range.getStart())
					.dateTo(range.getEnd())
					.employmentDate(employmentDate)
					.wholeAbsenceDateFrom(input.getDateFromAsDate())
					.wholeAbsenceDateTo(input.getDateToAsDate())

					.hrdata(fetchHrData(input.getPesel(), input.getNip(), employmentDate))
					.sicknessCode(input.getTrimedSicknessCode())
					.motifa(input.getMotifa())

					.hospital(range.isHospital())

					.build();

			absences.add(absence);
		}
		return absences;
	}

	private List<String> fetchHrData(String pesel, String nip, Date employmentDate) throws DataAccessException {
		if (Strings.isNullOrEmpty(pesel) || Strings.isNullOrEmpty(nip) || employmentDate == null) return null;

		String pdate = DATE_FORMAT.format(employmentDate);
		String ppesel = pesel;
		String pnip = nip;
		Object[] params = new Object[]{pesel, pdate, nip};

		List<Map<String, Object>> rows = executor.query(EMPLOYEE_QUERY, params);

		if (!rows.isEmpty()) {
			if (rows.size() != 1) {
				log.error("Multiple HR data for pesel {}", pesel);
			}
			Iterator<Map.Entry<String, Object>> i = rows.get(0).entrySet().iterator();
			String nudoss = null;
			String matcle = null;
			String socdos = null;
			while (i.hasNext()) {
				Map.Entry<String, Object> entry = i.next();
				if (NUDOSS.equalsIgnoreCase(entry.getKey())) nudoss = (String)entry.getValue();
				if (MATCLE.equalsIgnoreCase(entry.getKey())) matcle = (String)entry.getValue();
				if (SOCDOS.equalsIgnoreCase(entry.getKey())) socdos = (String)entry.getValue();
			}

			return Lists.newArrayList(nudoss, matcle, socdos);
		}
		return null;
	}

	private Date fetchEmploymentDate(String pesel, Date startDate) throws DataAccessException {
		if (Strings.isNullOrEmpty(pesel) || startDate == null) return null;

		String pdate = DATE_FORMAT.format(startDate);
		String ppesel = pesel;
		Object[] params = new Object[]{pesel, pdate};

		List<Map<String, Object>> rows = executor.query(EMPLOYEE_LATEST_EMPLOYMENT_DATE_QUERY, params);

		if (!rows.isEmpty()) {
			if (rows.size() != 1) {
				log.error("Multiple max employment dates for pesel {} with absence from: {}", pesel, pdate);
			}
			Map.Entry<String, Object> row = rows.get(0).entrySet().iterator().next();
			try {
				String value = (String)row.getValue();
				return DATE_FORMAT.parse(value);
			} catch (ParseException e) {
				log.error("Error parsing employment date {} for pesel {}", pesel, row.getValue());
			}
		}
		return null;
	}



}
