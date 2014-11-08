/**
 * 
 */
package pl.com.dbs.reports.support.web.form.validator;

import pl.com.dbs.reports.support.web.form.field.AField;

/**
 * Validator NOT IMPLEMENTED YET!
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class FieldValidatorNotImplementedException extends FieldValidatorException {
	private static final long serialVersionUID = 1L;
	
	public FieldValidatorNotImplementedException(AField<?> field, String type) {
		super(field, "errors.validator.not.implemented", new String[]{type});
	}
	
}
