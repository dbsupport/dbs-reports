package pl.com.dbs.reports.communication.domain;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class SshException extends Exception {
	private String host;
	private String port;
	private String user;
	private String command;
	private int tries;

	public SshException() {
		super();
	}

	public SshException(Exception e) {
		super(e);
	}

	public SshException host(String host) {
		this.host = host;
		return this;
	}

	public SshException user(String user) {
		this.user = user;
		return this;
	}

	public SshException port(int port) {
		this.port = String.valueOf(port);
		return this;
	}

	public SshException command(String command) {
		this.command = command;
		return this;
	}

	public SshException tries(int tries) {
		this.tries = tries;
		return this;
	}

	public String getHost() {
		return host;
	}

	public String getUser() {
		return user;
	}

	public String getCommand() {
		return command;
	}

	public String getPort() {
		return port;
	}

	public int getTries() {
		return tries;
	}
}
