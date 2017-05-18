package pl.com.dbs.reports.absence.domain.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.absence.domain.Absence;
import pl.com.dbs.reports.absence.domain.AbsenceOutput;
import pl.com.dbs.reports.api.support.db.SqlExecutor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Is such employee?
 * If not - skip absence.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Order(100)
@Slf4j
@Component
public class AbsenceEmployeeValidator implements AbsenceValidator {
	private SqlExecutor<Map<String, String>> executor;
	private MessageSource messageSource;

	static final String EMPLOYMEE_QUERY =
			"SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END EMPLOYEE " +
					"     FROM HR.ZANT T, HR.ZAZZ Z, TOMEK.FIRMY F, TOMEK.LOKALIZACJE L" +
					"   WHERE T.NUMERO IN ('?')" +
					"     AND T.NUDOSS = Z.NUDOSS" +
					"     AND Z.CENAFF = L.LOKALIZACJA" +
					"     AND L.FIRMA = F.FIRMA" +
					"     AND F.NIP = '?';";

	@Autowired
	public AbsenceEmployeeValidator(SqlExecutor<Map<String, String>> executor, MessageSource messageSource) {
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

		String ppesel = absence.getPesel();
		String pnip = absence.getNip();
		Object[] params = new Object[]{ppesel, pnip};

		List<Map<String, Object>> rows = executor.query(EMPLOYMEE_QUERY, params);

		if (!rows.isEmpty()) {
			if (rows.size() != 1) {
				log.error("Employee checking problem for pesel {} and nip {}", ppesel, pnip);
			}
			Iterator<Map.Entry<String, Object>> i = rows.get(0).entrySet().iterator();
			Map.Entry<String, Object> row = i.next();
			boolean employed = ((Number)row.getValue()).equals(1);
			if (!employed) {
				String msg = messageSource.getMessage("absence.validation.not.employed", new Object[]{ppesel, pnip}, null);
				throw new AbsenceValidationException(msg, absence).skip();
			}
		}
	}
}
