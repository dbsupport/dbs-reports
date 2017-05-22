package pl.com.dbs.reports.absence.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.dbs.reports.api.report.ReportLoggings;
import pl.com.dbs.reports.communication.service.FtpService;
import pl.com.dbs.reports.communication.service.SshService;

import java.io.IOException;
import java.util.List;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Slf4j
@Service
public class AbsenceProcessor {
	static final String COMMAND_FILENAME_TEMPLATE = "_filename_";

	private AbsenceImporter absenceImporter;
	private AbsenceExporter absenceExporter;
	private FtpService ftpService;
	private SshService sshService;

	@Value("absence.ssh.command")
	String command = "imp_zla _filename_";
	@Value("${absence.ftp.path}")
	String path;

	@Autowired
	public AbsenceProcessor(AbsenceImporter absenceImporter, AbsenceExporter absenceExporter, FtpService ftpService,
							SshService sshService) {
		this.absenceExporter = absenceExporter;
		this.absenceImporter = absenceImporter;
		this.ftpService = ftpService;
		this.sshService = sshService;
	}

	public AbsenceResult process(final byte[] content, String filename) {
		AbsenceResult result = null;
		try {
			//..parse and validate input data..
			List<AbsenceInput> inputs = absenceImporter.read(content);
			result = absenceExporter.export(inputs);
		} catch (IOException e) {
			log.error(ReportLoggings.MDC_ID, e.getMessage());
			result = new AbsenceResult();
			result.addError(e);
			return result;
		}

		try {
			//..export to ftp..
			final byte[] ftpcontent = AbsenceExportBuilder.builder().with(result.getAbsences()).build();
			ftpService.upload(ftpcontent, filename, path);

			//..sexcute ssh command..
			String cmd = command.replace(COMMAND_FILENAME_TEMPLATE, filename);
			sshService.execute(cmd);
		} catch (Exception e) {
			log.error(ReportLoggings.MDC_ID, e.getMessage());
			result.addError(e);
		}

		return result;
	}






}
