package pl.com.dbs.reports.communication.service;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.dbs.reports.communication.domain.FtpException;
import pl.com.dbs.reports.parameter.service.ParameterService;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Sends files via ftp.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Slf4j
@Service
public class FtpService {
	private ParameterService parameterService;
	@Value("#{new Integer.parseInt('${ftp.service.try.counter}')}")
	Integer tries;

	@Autowired
	public FtpService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}

	public void upload(final byte[] content, String filename, String path) throws FtpException {
		Context context = new Context();
		context.host = parameterService.find(ParameterService.FTP_HOST).getValueAsString();
		context.user = parameterService.find(ParameterService.FTP_USER).getValueAsString();
		context.passwd = parameterService.find(ParameterService.FTP_PASSWD).getValueAsString();
		context.stream = new ByteArrayInputStream(content);
		context.filename = filename;
		context.path = path;

		try {
			log.info("Uploading {}/{} to {} on user {} ...", context.path, context.filename, context.host, context.user);
			upload(context);
		} catch (Exception e) {
			log.error("Error uploading {}/{} to {} on user {}.", context.path, context.filename, context.host, context.user);
			throw new FtpException(e).host(context.host).user(context.user).path(context.path).filename(context.filename).tries(context.counter);
		}
	}

	void upload(Context context) throws Exception {
		FTPClient client = new FTPClient();
		FileInputStream fis = null;

		try {
			client.connect(context.host);
			client.login(context.user, context.passwd);
			if (!Strings.isNullOrEmpty(context.path))
				client.changeWorkingDirectory(context.path);

			client.storeFile(context.filename, context.stream);
			client.logout();
		} catch (Exception e) {
			retryOrGiveUp(context, e);
			upload(context);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				client.disconnect();
			} catch (Exception e) {
				throw e;
			}
		}
	}

	private void retryOrGiveUp(Context context, Exception e) throws Exception {
		if (context.counter < tries) {
			log.error("Failed ({}) to upload file to FTP server {}. Details: {}", context.counter, context.host, e.getMessage());
			context.counter++;
			return;
		}

		throw e;
	}

	static class Context {
		String host;
		String user;
		String passwd;
		String path;
		String filename;
		InputStream stream;
		int counter = 1;
	}

}
