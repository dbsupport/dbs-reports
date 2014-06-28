/**
 * 
 */
package pl.com.dbs.reports.support.web.form.inflater;

import pl.com.dbs.reports.support.web.form.field.IFieldInflatable;
import pl.com.dbs.reports.support.web.form.option.FieldOption;

/**
 * Inflates form fields with faken values.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */

public class FieldInflaterMock implements FieldInflater {
	private static final String[] LABELS = new String[] {"Jest li w istocie", "szlachetniejsza rzeczą", "Znosić pociski zawistnego losu", "Czy też", "stawiwszy czoło morzu nędzy", "Przez opór wybrnąc z niego?"};
	
	@Override
	public boolean supports(IFieldInflatable field) {
		return true;
	}

	@Override
	public FieldInflaterMock inflate(IFieldInflatable field) {
		for (int idx=0; idx<LABELS.length; idx++) {
			FieldOption option = new FieldOption(String.valueOf(idx), LABELS[idx]);
			field.inflate(option);
		}
		
		return this;
	}

}
