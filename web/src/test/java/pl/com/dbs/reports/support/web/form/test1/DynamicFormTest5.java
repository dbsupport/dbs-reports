/**
 * 
 */
package pl.com.dbs.reports.support.web.form.test1;

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

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

/**
 * Tests.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public class DynamicFormTest5 extends DynamicFormTest {
	@Test
	public void test() throws JAXBException, IOException {
		File file = read("pl/com/dbs/reports/support/web/form/test5/form.xml");
		
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


	@Test
	public void test_single_validator() throws JAXBException, IOException {
		File file = read("pl/com/dbs/reports/support/web/form/test5/form-single-validator.xml");

		DFormBuilder<TestDynamicForm> builder = new DFormBuilder<TestDynamicForm>(file, TestDynamicForm.class);

		TestDynamicForm form = builder.build().getForm();

		Assert.assertNotNull(form);

		System.out.println(form);

		Errors errors = new MapBindingResult(new HashMap<Object, Object>(), "test");
		form.validate(errors);

		assertTrue(errors.hasErrors());
		assertTrue(errors.getFieldErrorCount() == 2);

		for (ObjectError e : errors.getAllErrors()) {
			System.out.println("error: "+e);
		}

	}

}
