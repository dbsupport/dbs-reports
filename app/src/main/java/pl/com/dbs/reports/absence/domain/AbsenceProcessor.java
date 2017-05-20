package pl.com.dbs.reports.absence.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.dbs.reports.api.report.ReportLoggings;
import pl.com.dbs.reports.communication.service.FtpService;
import pl.com.dbs.reports.communication.service.SshService;
import pl.com.dbs.reports.parameter.service.ParameterService;

import java.io.IOException;
import java.util.List;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Slf4j
@Service
public class AbsenceProcessor {

	private AbsenceImporter absenceImporter;
	private AbsenceExporter absenceExporter;
	private FtpService ftpService;
	private SshService sshService;
	private ParameterService parameterService;

	@Value("absence.ssh.command")
	String command;

	@Autowired
	public AbsenceProcessor(AbsenceImporter absenceImporter, AbsenceExporter absenceExporter, FtpService ftpService,
							SshService sshService, ParameterService parameterService) {
		this.absenceExporter = absenceExporter;
		this.absenceImporter = absenceImporter;
		this.ftpService = ftpService;
		this.sshService = sshService;
		this.parameterService = parameterService;
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
			String path = parameterService.find(ParameterService.FTP_ABSENCE_PATH).getValueAsString();
			final byte[] ftpcontent = AbsenceExportBuilder.builder().with(result.getAbsences()).build();
			ftpService.upload(ftpcontent, filename, path);

			//..sexcute ssh command..
			String cmd = command.replace("{filename}", filename);
			sshService.execute(cmd);
		} catch (Exception e) {
			log.error(ReportLoggings.MDC_ID, e.getMessage());
			result.addError(e);
		}

		return result;
	}






}
