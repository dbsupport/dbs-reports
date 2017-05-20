package pl.com.dbs.reports.absence.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Produces input data for script.
 *
 * 000000000POLPOL0010003                    *FZAAG                       02017-05-04CHR2017-05-07    +00000 +00000   2017-05-042017-05-04                  X          +000000+00000
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceExportBuilder {
	static final String EOL = "\r\n";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private  List<AbsenceOutput> absences;

	private AbsenceExportBuilder() {}

	public static AbsenceExportBuilder builder() {
		return new AbsenceExportBuilder();
	}

	public AbsenceExportBuilder with(final List<AbsenceOutput> absences) {
		this.absences = absences;
		return this;
	}

	public byte[] build() {
		StringBuilder sb = new StringBuilder();
		for (AbsenceOutput output : absences) {
			sb
					.append("000000000")
					.append(output.getSocdosOrEmpty())
					.append(output.getMatcleOrEmpty())
					.append("                    *FZAAG                       0")
					.append(format(output.getDateFrom()))
					.append(output.getMotifaOrEmpty())
					.append(format(output.getDateTo()))
					.append("    +00000 +00000 ")
					.append(String.format("%-2s", output.getSicknessCodeOrEmpty()))
					.append(format(output.getDate()))
					.append(format(output.getDate()))
					.append("                  X          +000000+00000 ")
					.append(EOL);
		}

		return sb.toString().getBytes();
	}

	private String format(Date date) {
		return date!=null?DATE_FORMAT.format(date):"          ";
	}

}
