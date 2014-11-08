/**
 * 
 */
package pl.com.dbs.reports.support.web.form.test1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import pl.com.dbs.reports.support.web.form.DForm;

/**
 * Tests.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@XmlRootElement(name="form", namespace = "http://www.dbs.com.pl/reports/1.0/form")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestDynamicForm extends DForm {
	private static final long serialVersionUID = 1940183221000154272L;

	public TestDynamicForm() {
		super();
	}
	
}
