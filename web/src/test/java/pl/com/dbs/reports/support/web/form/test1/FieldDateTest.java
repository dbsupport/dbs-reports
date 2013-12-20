/**
 * 
 */
package pl.com.dbs.reports.support.web.form.test1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class FieldDateTest {

	@Test
	public void testDateFormat1() throws ParseException {
		DateFormat DF1 = new SimpleDateFormat("MM/dd/yyyy")  ;
		String value = "11/21/2013";
		
		Date date = DF1.parse(value);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		Assert.assertTrue(cal.get(Calendar.YEAR)==2013);
		Assert.assertTrue(cal.get(Calendar.MONTH)+1==11);
		Assert.assertTrue(cal.get(Calendar.DAY_OF_MONTH)==21);
	}
}
