/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
	private static final Pattern TIME_PATTERN = Pattern.compile("hh|HH|HH24|hh24", Pattern.CASE_INSENSITIVE);
	private static final Pattern NOW_PATTERN = Pattern.compile("now|teraz", Pattern.CASE_INSENSITIVE);
	
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
	public void init(LinkedList<AField<?>> fields) {
		if (NOW_PATTERN.matcher(getValue()).find()) {
			setValue(DateFormatUtils.format(new Date(), resolveFormat()));
		}
		super.init(fields);
	}
	
	
	@Override
	public String getTile() {
		return "tiles-field-date";
	}
	
	@Override
	public String getFormat() {
		return StringUtils.isBlank(format)?"yyyy-mm-dd":format;
	}
	
	public boolean isWithTime() {
		return TIME_PATTERN.matcher(getFormat()).find();
	}
	
	private String resolveFormat() {
		/**
		 * http://www.malot.fr/bootstrap-datetimepicker/
		 * 
		 * bootstrap mm = format MM
		 * bootstrap ii = format mm
		 */
		String format = getFormat(); 
		format = format.replaceFirst("mm", "MM");
		format = format.replaceFirst("ii", "mm");
		
		return format;
	}
	
}
