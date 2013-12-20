/**
 * 
 */
package pl.com.dbs.reports.support.web.form.test1;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.web.form.DFormBuilder;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public class DynamicFormTest3 extends DynamicFormTest {
	@Test
	public void test3() throws JAXBException, IOException {
		File file = read("pl/com/dbs/reports/support/web/form/test3/form.xml");
		
		
		
		DFormBuilder<TestDynamicForm> builder = new DFormBuilder<TestDynamicForm>(file, TestDynamicForm.class);
		
		TestDynamicForm form = builder.build().getForm();
		
		Assert.assertNotNull(form);
		
		System.out.println(form);
		
		Errors errors = new MapBindingResult(new HashMap<Object, Object>(), "test");
		form.validate(errors);
		
		for (ObjectError e : errors.getAllErrors()) {
			System.out.println("error: "+e);
		}
		
	}		
}
