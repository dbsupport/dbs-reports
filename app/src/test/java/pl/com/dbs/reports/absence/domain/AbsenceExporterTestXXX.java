/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.MessageSource;
import pl.com.dbs.reports.absence.domain.validator.AbsenceValidationException;
import pl.com.dbs.reports.absence.domain.validator.AbsenceValidatorProcessor;
import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.communication.domain.FtpException;
import pl.com.dbs.reports.communication.service.FtpService;
import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.parameter.service.ParameterService;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceExporterTestXXX {
	private AbsenceValidatorProcessor validatorProcessor;
	private SqlExecutor<Map<String, String>> executor;
	private AbsenceInputSplitter splitter;

//	@Rule
//	public ExpectedException thrown = ExpectedException.none();
//
//	@Before
//	public void doBeforeEachTestCase() {
//		validatorProcessor = mock(MessageSource.class);
//		when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).then(returnsFirstArg());
//
//		ftpSender = mock(FtpService.class);
//		parameterService = mock(ParameterService.class);
//
//	}
//
//	@Test
//	public void should_catch_ftp_exception() throws AbsenceValidationException, FtpException {
////		thrown.expect(AbsenceValidationException.class);
////		thrown.expectMessage("absence.validation.export.ftp.problem");
////		thrown.expect(Matchers.hasProperty("skip", is(true)));
////		thrown.expect(Matchers.hasProperty("stop", is(true)));
//
//		AbsenceExporter exporter = new AbsenceExporter(executor, validatorProcessor, splitter);
//		exporter.ftpTries = 3;
//
//		Parameter parameter = mock(Parameter.class);
//		when(parameter.getKey()).thenReturn("file");
//		when(parameter.getValueAsString()).thenReturn("file");
//
//		when(parameterService.find(anyString())).thenReturn(parameter);
//		doThrow(new FtpException()).when(ftpSender).send(any(byte[].class), anyString(), anyString());
//
//		List<AbsenceOutput> absences = Lists.newArrayList();
//		List<AbsenceError> errors = exporter.export(absences, "filename");
//		verify(ftpSender, times(3)).send(any(byte[].class), anyString(), anyString());
//
//		assertTrue(errors.size() == 1);
//	}

}
