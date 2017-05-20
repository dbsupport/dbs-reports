package pl.com.dbs.reports.absence.domain;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import liquibase.util.csv.opencsv.bean.CsvBind;
import org.apache.commons.lang.Validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Absence csv entity.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceInput implements Absence {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	static final String MOTIF_ODR = "ODR";
	static final String MOTIF_CHR = "CHR";
	public static final List<String> AVAILABLE_SICKNESS_CODES = Lists.newArrayList(
			"A", "B", "C", "D", "E", "AB", "AC", "AD", "AE", "BA", "BC", "BD", "BE", "CA", "CB", "CD", "CE", "DA", "DB", "DC", "DE", "EA", "EB", "EC", "ED"
	);

	@CsvBind
	String series;
	@CsvBind
	String number;
	@CsvBind
	String pesel;
	@CsvBind(required = true)
	String dateFrom;
	@CsvBind(required = true)
	String dateTo;
	@CsvBind(required = true)
	String date;
	@CsvBind
	String sicknessCode;
	@CsvBind
	String nip;
	@CsvBind
	String relationshipCode;
	@CsvBind
	String hospitalDateFrom;
	@CsvBind
	String hospitalDateTo;

	public String getTrimedSicknessCode() {
		if (Strings.isNullOrEmpty(sicknessCode)) return null;
		return sicknessCode.toUpperCase().replaceAll("[^A-Z]", "").trim();
	}

	public String getAvailableSicknessCode() {
		String c = getTrimedSicknessCode();
		return (!Strings.isNullOrEmpty(c) && AVAILABLE_SICKNESS_CODES.contains(c))?c:null;
	}

	public boolean isSamePesel(String pesel) {
		return !Strings.isNullOrEmpty(pesel)
				&&!Strings.isNullOrEmpty(this.pesel)
				&&pesel.equalsIgnoreCase(this.pesel);
	}

	public boolean hasPesel() {
		return !Strings.isNullOrEmpty(getPesel());
	}

	public boolean hasNip() {
		return !Strings.isNullOrEmpty(getNip());
	}

	public boolean hasRelationshipCode() {
		return !Strings.isNullOrEmpty(relationshipCode);
	}

	public boolean hasDateFrom() {
		return getDateFromAsDate()!=null;
	}

	public boolean hasDateTo() {
		return getDateToAsDate()!=null;
	}

	public boolean hasHospitalDateFrom() {
		return getHospitalDateFrom()!=null;
	}

	public boolean hasHospitalDateTo() {
		return getHospitalDateTo()!=null;
	}

	public boolean hasHospitalDates() {
		return hasHospitalDateFrom()||hasHospitalDateTo();
	}

	public String getMotifa() {
		return !Strings.isNullOrEmpty(relationshipCode)?MOTIF_ODR:MOTIF_CHR;
	}

	private Date parse(String value) {
		if (Strings.isNullOrEmpty(value)) return null;

		try {
			return DATE_FORMAT.parse(value);
		} catch (ParseException e) {

		}
		return null;
	}

	public String getSeries() {
		return series;
	}

	public String getNumber() {
		return number;
	}

	public String getPesel() {
		return !Strings.isNullOrEmpty(pesel)?pesel.toUpperCase():pesel;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public String getSicknessCode() {
		return sicknessCode;
	}

	public Date getDateFromAsDate() {
		return parse(dateFrom);
	}

	public Date getDateToAsDate() {
		return parse(dateTo);
	}

	public String getDate() {
		return date;
	}

	public Date getDateAsDate() {
		return parse(date);
	}

	public String getNip() {
		return !Strings.isNullOrEmpty(nip)?nip.toUpperCase():nip;
	}

	public String getRelationshipCode() {
		return relationshipCode;
	}

	public String getHospitalDateFrom() {
		return hospitalDateFrom;
	}

	public Date getHospitalDateFromAsDate() {
		return parse(hospitalDateFrom);
	}

	public String getHospitalDateTo() {
		return hospitalDateTo;
	}

	public Date getHospitalDateToAsDate() {
		return parse(hospitalDateTo);
	}

	public static class AbsenceRange {
		Date start;
		Date end;
		boolean hospital = false;

		public AbsenceRange(Date start, Date end) {
			Validate.notNull(start);
			Validate.notNull(end);
			Validate.isTrue(end.compareTo(start) >= 0);
			this.start = start;
			this.end = end;
		}

		public AbsenceRange(Date start, Date end, boolean hospital) {
			this(start, end);
			this.hospital = hospital;
		}

		public Date getStart() {
			return start;
		}

		public Date getEnd() {
			return end;
		}

		public boolean isHospital() {
			return hospital;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
			sb.append("seria:").append(series)
			.append(",numer:").append(number)
			.append(",pesel:").append(pesel)
			.append(",data od:").append(dateFrom)
			.append(",data do:").append(dateFrom)
			.append(",kod choroby:").append(sicknessCode)
			.append(",nip:").append(nip)
			.append(",szpital od:").append(hospitalDateFrom)
			.append(",szpital do:").append(hospitalDateTo)
			.append(",data wystawienia:").append(date);
		return sb.toString();
	}
}
