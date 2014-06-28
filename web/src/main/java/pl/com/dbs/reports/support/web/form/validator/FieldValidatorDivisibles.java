/**
 * 
 */
package pl.com.dbs.reports.support.web.form.validator;

import java.util.LinkedList;
import java.util.List;

import pl.com.dbs.reports.support.web.form.field.AField;
import pl.com.dbs.reports.support.web.form.field.IFieldDivisible;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.internal.Lists;


/**
 * ..disallow more than one divisible field that has many values!
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class FieldValidatorDivisibles extends AFieldValidator {
	public FieldValidatorDivisibles() {
		super();
	}
	
	@Override
	public void validate(AField<?> field, LinkedList<AField<?>> fields) throws FieldValidatorException {
		List<IFieldDivisible> divisibles = getDivisibles(fields);
		if (divisibles.size()>1) {
			throw new FieldValidatorException(field, "report.execute.too.many.divisibles");
		}		
	}
	
	private static List<IFieldDivisible> getDivisibles(LinkedList<AField<?>> fields) {
		if (fields==null) return Lists.newArrayList();
		Iterable<IFieldDivisible> divisionables = Iterables.filter(fields, IFieldDivisible.class);
		divisionables = Iterables.filter(divisionables, new Predicate<IFieldDivisible>() {
			@Override
			public boolean apply(IFieldDivisible input) {
				return input.divides();
			}
		});
		return Lists.newArrayList(divisionables);
	}	
	

	@Override
	public void init(AField<?> field, LinkedList<AField<?>> fields) {
		this.description = new FieldValidatorDescription("errors.required");
	}	
	
}
