/**
 * 
 */
package pl.com.dbs.reports.support.web.form;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.springframework.validation.Errors;

import pl.com.dbs.reports.support.utils.separator.Separator;
import pl.com.dbs.reports.support.web.form.field.AField;



/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "form", namespace = "http://www.dbs.com.pl/reports/1.0/form")
public abstract class DForm extends AForm {
	@XmlElement(name="field", namespace = "http://www.dbs.com.pl/reports/1.0/form")
	@XmlElementWrapper(name="fields", namespace = "http://www.dbs.com.pl/reports/1.0/form")
	protected LinkedList<AField<?>> fields;
	
	@XmlElement(name="@stop", namespace = "http://www.dbs.com.pl/reports/1.0/form")
	private boolean stop;	
	
	public DForm() {
		super();
	}

	/**
	 * Initialize objects..
	 */
	public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if (isFieldfull()) for (AField<?> field : fields) field.init(fields);
	}
	
	public void reset() {
		super.reset();
	}

	public boolean isFieldfull() {
		return this.fields!=null&&!this.fields.isEmpty();
	}

	public LinkedList<AField<?>> getFields() {
		return fields;
	}
	
	public void validate(Errors errors) {
		for (int idx=0; idx<this.fields.size(); idx++) {
			AField<?> field = this.fields.get(idx);
			//..validate..
			field.validate(this.fields, errors, "fields["+idx+"]");
			if (errors.hasErrors()&&stop) break;
		}
	}
	
	/**
	 * Returns fields and values as map.
	 */
	public Map<String, String> getParameters() {
		Map<String, String> result = new HashMap<String, String>();
		for (AField<?> field : this.fields) field.addAsParameter(result);
		return result;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Separator s = new Separator("\n");
		for (AField<?> field : fields) sb.append(s).append(field);
		return sb.toString();
	}
	
}
