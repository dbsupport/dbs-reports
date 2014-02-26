/**
 * 
 */
package pl.com.dbs.reports.support.web.form.validator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import pl.com.dbs.reports.support.web.form.field.AField;

/**
 * Field error data.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class FieldValidatorException extends Exception {
	private static final long serialVersionUID = 1L;
	private AField<?> field;
	private String code;
	private List<String> args = new LinkedList<String>();
	
	public FieldValidatorException(AField<?> field, String code) {
		this.code = code;
		this.field = field;
	}
	
	public FieldValidatorException(AField<?> field, String code, String[] args) {
		this.code = code;
		this.field = field;
		this.args = Arrays.asList(args);
	}
	
	public String getCode() {
		return code;
	}

	public AField<?> getField() {
		return field;
	}

	public Object[] getArgs() {
		return args.toArray();
	}
	
	public void addArg(String arg) {
		this.args.add(arg);
	}
	
	private boolean isParametrized() {
		return this.args!=null&&!this.args.isEmpty();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" | field: "+field.getName()).
		append(" ;code:"+code);
		
		if (isParametrized()) {
			sb.append(" ;params:");
			for (String param: args) sb.append(param).append(" ,");
		}
		return sb.toString(); 
	}	
}
