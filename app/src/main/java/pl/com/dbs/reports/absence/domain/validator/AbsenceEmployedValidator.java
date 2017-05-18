package pl.com.dbs.reports.absence.domain.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.absence.domain.Absence;
import pl.com.dbs.reports.absence.domain.AbsenceOutput;
import pl.com.dbs.reports.api.support.db.SqlExecutor;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Was eployee employed while absence day start?
 * If not - skip absence.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Order(101)
@Slf4j
@Component
public class AbsenceEmployedValidator implements AbsenceValidator {
	private SqlExecutor<Map<String, String>> executor;
	private MessageSource messageSource;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	static final String EMPLOYMENT_QUERY =
			"SELECT CASE WHEN NVL(DATSOR, TO_DATE('0001-01-01','YYYY-MM-DD')) >= TO_DATE('?', 'YYYY-MM-DD') THEN 1 ELSE 0 END EMPLOYED " +
					"FROM (SELECT MAX(A.DATENT) DATENT, MAX(A.DATSOR) DATSOR " +
					"FROM HR.ZAES A " +
					"WHERE A.NUDOSS = '?'" +
					"AND TO_DATE('?', 'YYYY-MM-DD') BETWEEN A.DATENT AND A.DATSOR)";

	@Autowired
	public AbsenceEmployedValidator(SqlExecutor<Map<String, String>> executor, MessageSource messageSource) {
		this.executor = executor;
		this.messageSource = messageSource;
	}

	@Override
	public boolean supports(final Absence absence) {
		return absence.getClass().equals(AbsenceOutput.class);
	}

	@Override
	public void validate(final Absence input, final List<? extends Absence> absences) throws AbsenceValidationException {
		if (!supports(input)) return;

		AbsenceOutput absence = (AbsenceOutput)input;

		String pdate = DATE_FORMAT.format(absence.getDateFrom());
		String pnudoss = absence.getNudoss();
		Object[] params = new Object[]{pdate, pnudoss, pdate};

		List<Map<String, Object>> rows = executor.query(EMPLOYMENT_QUERY, params);

		if (!rows.isEmpty()) {
			if (rows.size() != 1) {
				log.error("Multiple employment dates for nudoss {}", pnudoss);
			}
			Iterator<Map.Entry<String, Object>> i = rows.get(0).entrySet().iterator();
			Map.Entry<String, Object> row = i.next();
			boolean employed = ((Number)row.getValue()).equals(1);
			if (!employed) {
				String msg = messageSource.getMessage("absence.validation.not.employed.in.absences", null, null);
				throw new AbsenceValidationException(msg, absence).skip();
			}
		}
	}
}
