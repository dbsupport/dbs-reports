package pl.com.dbs.reports.absence.domain;

import liquibase.util.csv.opencsv.CSVReader;
import liquibase.util.csv.opencsv.bean.ColumnPositionMappingStrategy;
import liquibase.util.csv.opencsv.bean.CsvToBean;
import liquibase.util.csv.opencsv.bean.MappingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Slf4j
@Service
public class AbsenceImporter {
	private static final String[] COLUMNS = new String[]{
			"series", "number", "pesel", "firstName", "lastName", "insurerCode", "passport", "birthDate", "zipCode", "city",
			"street", "building", "flat", "dateFrom", "dateTo", "hospitalDateFrom", "hospitalDateTo", "wsklek", "sicknessCode", "sickness",
			"relationshipCode", "dataurodzeniaopd", "rodzideplatnika", "nip", "shortName", "payorZipCode", "payorCity", "payorStreet", "payorBuilding", "payorFlat",
			"doctorId", "doctorFirstName", "doctorLastName", "date"};

	private MessageSource messageSource;

	@Autowired
	public AbsenceImporter(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public List<AbsenceInput> read(File file) throws IOException {
		CsvToBean<AbsenceInput> parser = new CsvToBean<AbsenceInput>();
		CSVReader reader = new CSVReader(new FileReader(file), ',', '\'', 1);
		return parser.parse(strategy(), reader);
	}

	public List<AbsenceInput> read(final byte[] content) throws IOException {
		CsvToBean<AbsenceInput> parser = new CsvToBean<AbsenceInput>();
		InputStream stream = new ByteArrayInputStream(content);
		CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '\'', 1);
		return parser.parse(strategy(), reader);
	}

	private MappingStrategy<AbsenceInput> strategy() {
		ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
		strategy.setType(AbsenceInput.class);
		strategy.setColumnMapping(COLUMNS);
		return strategy;
	}
}
