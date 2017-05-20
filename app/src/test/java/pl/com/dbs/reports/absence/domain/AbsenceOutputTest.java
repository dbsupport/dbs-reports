package pl.com.dbs.reports.absence.domain;

import com.google.common.base.Strings;
import liquibase.util.csv.opencsv.bean.CsvBind;
import org.apache.commons.lang.Validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Absence csv entity.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceOutputTest implements Absence {
	@CsvBind
	String pesel;
	@CsvBind
	String series;
	@CsvBind
	String number;
	@CsvBind
	String nip;
	@CsvBind
	String nudoss;
	@CsvBind
	String matcle;
	@CsvBind
	String socdos;
	@CsvBind
	Date dateFrom;
	@CsvBind
	Date dateTo;
	@CsvBind
	Date date;
	@CsvBind
	Date employmentDate;
	@CsvBind
	String sicknessCode;
	@CsvBind
	String motifa;
	@CsvBind
	boolean hospital = false;


}
