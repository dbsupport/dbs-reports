/**
 * 
 */
package pl.com.dbs.reports.communication.service;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.com.dbs.reports.communication.domain.SshException;
import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.parameter.service.ParameterService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class SshServiceTest {
	private ParameterService parameterService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void doBeforeEachTestCase() {
		parameterService = mock(ParameterService.class);

	}

	@Test
	public void should_throw_ssh_exception() throws Exception {
		thrown.expect(SshException.class);
		thrown.expect(Matchers.hasProperty("tries", is(2)));

		SshService sshService = new SshService(parameterService);
		sshService.tries = 2;
		sshService.sleepTime = 10;

		Parameter parameter = mock(Parameter.class);
		when(parameter.getKey()).thenReturn("qwerty");
		when(parameter.getValueAsString()).thenReturn("qwerty");

		when(parameterService.find(anyString())).thenReturn(parameter);

		sshService.execute("unknown-command");
	}

}
