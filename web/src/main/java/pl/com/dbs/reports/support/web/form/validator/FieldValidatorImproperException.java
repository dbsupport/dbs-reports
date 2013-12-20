/**
 * 
 */
package pl.com.dbs.reports.support.web.form.validator;

import pl.com.dbs.reports.support.web.form.field.AField;

/**
 * Validator badly configured.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class FieldValidatorImproperException extends FieldValidatorException {
	private static final long serialVersionUID = 1L;
	
	public FieldValidatorImproperException(AField<?> field, String type, Exception e) {
		super(field, "errors.validator.improper", new String[]{ type, e.getMessage()});
	}
	
}
