/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.com.dbs.reports.TestFileHelper;
import pl.com.dbs.reports.report.PatternFactoryDefaultTest;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class AbsenceExportBuilderTest {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final String PATH = "pl/com/dbs/reports/absence/reader/test/";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void should_produce_file() throws IOException, ParseException {
		AbsenceExportBuilder builder = AbsenceExportBuilder.builder();
		String filename = "ZLA_INS_20170505_test_in.TXT";
		byte[] inputb = TestFileHelper.read2byte(PATH +filename);
		String inputs = new String(inputb, "UTF-8");

		List<AbsenceOutput> absences = Lists.newArrayList();

		absences.add(build("56121907940","8975308","ZZ","5260309174","2017-05-04","2017-05-07","2017-05-01","2017-05-04","689861408","POL","POL0010003",null,"CHR"));
		absences.add(build("53092605017","8975640","ZZ","5260309174","2017-05-02","2017-05-26","2017-05-01","2017-05-08","689861407","POL","POL0010002",null,"CHR"));
		absences.add(build("51030218017","8975712","ZZ","5260309174","2017-05-04","2017-05-05","2017-05-01","2017-05-04","689861457","POL","POL0010054",null,"ODR"));
		absences.add(build("65010503688","8975250","ZZ","5260309174","2017-05-04","2017-05-18","2017-05-01","2017-05-04","689861410","POL","POL0010005","AB","CHR"));
		absences.add(build("60040210197","8978223","ZZ","5260309174","2017-04-21","2017-05-02","2017-05-01","2017-05-02","689861492","POL","POL0010089","S","CHR"));
		absences.add(build("60040210197","8978223","ZZ","5260309174","2017-05-03","2017-06-21","2017-05-01","2017-05-02","689861492","POL","POL0010089","C","CHR"));
		absences.add(build("67042809321","8979789","ZZ","5260309174","2017-05-02","2017-05-03","2017-05-01","2017-05-02","689861413","POL","POL0010008",null,"CHR"));
		absences.add(build("67042809321","8979789","ZZ","5260309174","2017-05-04","2017-05-15","2017-05-01","2017-05-02","689861413","POL","POL0010008","S","CHR"));
		absences.add(build("52062416116","8977814","ZZ","5260309174","2017-05-01","2017-05-02","2017-05-01","2017-05-02","689861494","POL","POL0010091",null,"CHR"));
		absences.add(build("62093000028","8977791","ZZ","5260309174","2017-05-02","2017-05-03","2017-05-01","2017-05-02","689872610","POL","POL0010015",null,"CHR"));
		absences.add(build("62093000028","8977791","ZZ","5260309174","2017-05-04","2017-05-25","2017-05-01","2017-05-02","689872610","POL","POL0010015","S","CHR"));
		absences.add(build("62093000028","8977791","ZZ","5260309174","2017-05-26","2017-05-28","2017-05-01","2017-05-02","689872610","POL","POL0010015",null,"CHR"));

		byte[] outputb = builder.with(absences).build();
		String outputs = new String(outputb, "UTF-8");

		System.out.println(inputs);
		System.out.println(outputs);

		System.out.println("------------------------------------");
		List<String> inputl = Lists.newArrayList(Splitter.on(AbsenceExportBuilder.EOL).split(inputs).iterator());
		List<String> outputl = Lists.newArrayList(Splitter.on(AbsenceExportBuilder.EOL).split(outputs).iterator());

		for (String  output: outputl) {
			if (!inputl.contains(output)) {
				System.out.println(output);
			}
		}

		assertTrue(outputl.size() == inputl.size());
		for (String  output: outputl) {
			assertTrue(inputl.contains(output));
		}

	}


	private AbsenceOutput build(String ... parts) throws ParseException {
		AbsenceOutputBuilder builder = AbsenceOutputBuilder.builder();
		int i = 0;
		return builder
				.pesel(parts[i++]).number(parts[i++]).series(parts[i++]).nip(parts[i++])
				.dateFrom(DATE_FORMAT.parse(parts[i++])).dateTo(DATE_FORMAT.parse(parts[i++]))
				.employmentDate(DATE_FORMAT.parse(parts[i++])).date(DATE_FORMAT.parse(parts[i++]))
				.nudoss(parts[i++]).socdos(parts[i++]).matcle(parts[i++]).sicknessCode(parts[i++]).motifa(parts[i++]).hospital(false)
				.build();
	}

//	private marshall(String filename) {
//
//	}
//
//	private List<String> marshallAbsenceOutput(AbsenceOutput output) {
//		String SEPARATOR = "\",\"";
//		StringBuilder sb = new StringBuilder();
//		sb.append("absences.add(build(\"")
//				.append(output.pesel).append(SEPARATOR)
//				.append(output.number).append(SEPARATOR)
//				.append(output.series).append(SEPARATOR)
//				.append(output.nip).append(SEPARATOR)
//				.append(DATE_FORMAT.format(output.dateFrom)).append(SEPARATOR)
//				.append(DATE_FORMAT.format(output.dateTo)).append(SEPARATOR)
//				.append(DATE_FORMAT.format(output.employmentDate)).append(SEPARATOR)
//				.append(DATE_FORMAT.format(output.date)).append(SEPARATOR)
//				.append(output.nudoss).append(SEPARATOR)
//				.append(output.socdos).append(SEPARATOR)
//				.append(output.matcle).append(SEPARATOR)
//				.append(output.sicknessCode).append(SEPARATOR)
//				.append(output.motifa).append("\"));");
//
//		return sb.toString();
//	}
//
//	private File read(String src) {
//		File file = null;
//		try {
//			URL url = PatternFactoryDefaultTest.class.getClassLoader().getResource(src);
//			file = new File(url.toURI());
//		} catch (URISyntaxException e) {}
//		if (!file.exists()) throw new IllegalStateException();
//		return file;
//	}
}
