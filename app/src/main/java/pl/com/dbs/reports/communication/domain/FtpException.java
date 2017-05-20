package pl.com.dbs.reports.communication.domain;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class FtpException extends Exception {
	private String host;
	private String user;
	private String path;
	private String filename;
	private int tries = 0;

	public FtpException() {
		super();
	}

	public FtpException(Exception e) {
		super(e);
	}

	public FtpException host(String host) {
		this.host = host;
		return this;
	}

	public FtpException user(String user) {
		this.user = user;
		return this;
	}

	public FtpException path(String path) {
		this.host = path;
		return this;
	}

	public FtpException filename(String filename) {
		this.host = filename;
		return this;
	}

	public FtpException tries(int tries) {
		this.tries = tries;
		return this;
	}

	public String getHost() {
		return host;
	}

	public String getUser() {
		return user;
	}

	public String getPath() {
		return path;
	}

	public String getFilename() {
		return filename;
	}

	public int getTries() {
		return tries;
	}
}
