package pl.com.dbs.reports.communication.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.dbs.reports.api.report.ReportLoggings;
import pl.com.dbs.reports.communication.domain.SshException;
import pl.com.dbs.reports.parameter.service.ParameterService;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Executes command via ssh.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Slf4j
@Service
public class SshService {
	private ParameterService parameterService;
	private static final int BUFFER_SIZE = 1024;

	@Value("#{new Integer.parseInt('${ssh.service.sleep.time}')}")
	Integer sleepTime;

	@Value("#{new Integer.parseInt('${ssh.service.try.counter}')}")
	Integer tries;

	@Autowired
	public SshService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}

	public void execute(final String command) throws SshException {
		Context context = new Context();
		context.host(parameterService.find(ParameterService.SSH_HOST).getValueAsString());
		context.user = parameterService.find(ParameterService.SSH_USER).getValueAsString();
		context.passwd = parameterService.find(ParameterService.SSH_PASSWD).getValueAsString();
		context.command = command;

		try {
			log.info("Executing {} on {} for user {} ...", context.command, context.host, context.user);
			execute(context);
		} catch (Exception e) {
			log.info("Error executing {} on {} for user {}.", context.command, context.host, context.user);
			throw new SshException(e).host(context.host).port(context.port).user(context.user).command(context.command).tries(context.counter);
		}
	}

	void execute(Context context) throws Exception {
		Channel channel = null;
		Session session = null;
		try {
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			session = jsch.getSession(context.user, context.host, context.port);
			session.setPassword(context.passwd);
			session.setConfig(config);
			session.connect();

			log.info("Succesfully connected to {} via ssh.", context.host);

			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(context.command);
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] buffer = new byte[BUFFER_SIZE];
			while (true) {
				while (in.available()>0) {
					int i = in.read(buffer, 0, BUFFER_SIZE);
					if (i < 0) break;
				}
				if (channel.isClosed()) {
					log.warn(/*ReportLoggings.MRK_USER, */"Ssh channel closed. Exit status: " + channel.getExitStatus());
				}
				try { Thread.sleep(sleepTime); } catch ( Exception ee) {}
			}
		} catch (Exception e) {
			retryOrGiveUp(context, e);
		} finally {
			channel.disconnect();
			session.disconnect();

			log.info("Succesfully executed commant {} via ssh on server {}.", context.command, context.host);
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
		private String host;
		private int port = -1;
		String user;
		String passwd;
		String command;
		int counter = 1;

		void host(String host) {
			List<String> hosts = Lists.newArrayList(Splitter.on(":").trimResults().omitEmptyStrings().split(host));
			this.host = hosts.size() > 1 ? hosts.get(0) : host;
			this.port = hosts.size() > 1 ? Integer.valueOf(hosts.get(1)) : 22;
		}


	}
}
