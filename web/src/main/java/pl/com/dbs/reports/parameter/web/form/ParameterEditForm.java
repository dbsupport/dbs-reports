/**
 * 
 */
package pl.com.dbs.reports.parameter.web.form;

import java.util.ArrayList;
import java.util.List;

import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.support.web.form.AForm;



/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ParameterEditForm extends AForm {
	public static final String KEY = "paramEditForm";
	private List<Param> params = new ArrayList<Param>();

	public ParameterEditForm() {
		super();
	}
	
	public void reset(List<Parameter> params) {
		super.reset();
		this.params = new ArrayList<Param>();
		for (Parameter param : params)
			this.params.add(new Param(param));
	}
	
	public List<Param> getParams() {
		return params;
	}

	public void setParams(List<Param> params) {
		this.params = params;
	}	

	public class Param { 
		private String key;
		private String value;
		
		Param(Parameter param) {
			this.key = param.getKey();
			this.value = param.toString();
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
	
}
