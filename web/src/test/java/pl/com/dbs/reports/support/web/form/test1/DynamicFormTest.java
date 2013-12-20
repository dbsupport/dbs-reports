/**
 * 
 */
package pl.com.dbs.reports.support.web.form.test1;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public abstract class DynamicFormTest {
	
	protected File read(String src) {
		File file = null;
		try {
			URL url = DynamicFormTest.class.getClassLoader().getResource(src);
			file = new File(url.toURI());
		} catch (URISyntaxException e) {}
		if (!file.exists()) throw new IllegalStateException(); 
		return file;
	}
}
