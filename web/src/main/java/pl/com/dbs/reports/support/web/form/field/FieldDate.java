/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
import org.joda.time.format.DateTimeFormat;


/**
 * Default date field.
 * WATCH IT! bootstrap format:dd/mm/yyyy = DateFormat:dd/MM/yyyy
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("date")
public class FieldDate  extends AField<Date> {
	public FieldDate() {
		super();
	}
	
	@Override
	public Date getValueTypized() {
		try {
			return DateTimeFormat.forPattern(resolveFormat()).parseDateTime(this.value).toDate();
		} catch (Exception e) {}
		return null;
	}
	
	@Override
	public String getTile() {
		return "tiles-field-date";
	}
	
	@Override
	public String getFormat() {
		return StringUtils.isBlank(format)?"yyyy-mm-dd":format;
	}	
	
	private String resolveFormat() {
		/**
		 * FIXME: 
		 * bootstrap mm = format MM
		 */
		return getFormat().replaceFirst("mm", "MM");		
	}
	
}
