/**
 * 
 */
package pl.com.dbs.reports.parameter.service;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.api.support.db.ConnectionContext;
import pl.com.dbs.reports.parameter.dao.ParameterDao;
import pl.com.dbs.reports.parameter.dao.ParameterFilter;
import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.support.encoding.EncodingContext;

/**
 * Simple parameters.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Service("parameter.service")
public class ParameterService {
	private static final String DB_DRIVER = "client.db.driver";
	private static final String DB_IP = "client.db.ip";
	private static final String DB_PORT = "client.db.port";
	private static final String DB_NAME = "client.db.name";
    private static final String DB_URL = "client.db.url";
	private static final String DB_SCHEMA = "client.db.schema";
	private static final String DB_USER = "client.db.user";
	private static final String DB_PASSWD = "client.db.passwd";
	private static final String DB_IENCODING = "client.db.encoding";
	private static final String DB_OENCODING = "local.db.encoding";
	private static final String DB_MAXACTIVE = "client.db.max.active";
	
	public static final String APP_HELP_FILE = "app.help.file";

	public static final String FTP_HOST = "client.ftp.host";
	public static final String FTP_USER = "client.ftp.user";
	public static final String FTP_PASSWD = "client.ftp.passwd";

	public static final String SSH_HOST = "client.ssh.host";
	public static final String SSH_USER = "client.ssh.user";
	public static final String SSH_PASSWD = "client.ssh.passwd";

	@Autowired private ParameterDao parameterDao;

	/**
	 * Get params.
	 */
	public List<Parameter> find(ParameterFilter filter)  {
		return parameterDao.find(filter);
	}
	
	public Parameter find(final String key)  {
		Validate.notNull(key, "Key is no more!");
		return parameterDao.find(key);
	}

	/**
	 * Save..
	 */
	@Transactional
	public boolean edit(final String key, final byte[] value, final String desc) {
		Validate.notNull(key, "Key is no more!");
		Parameter parameter = parameterDao.find(key);
		Validate.notNull(parameter, "No such parameter!");
		boolean result = !parameter.isSame(value);
		parameter.changeValue(value);
		parameter.changeDesc(desc);
		return result;
	}
	
	public EncodingContext getEncodingContext() {
		final Parameter in = parameterDao.find(DB_IENCODING);
		final Parameter out = parameterDao.find(DB_OENCODING);
		
		return new EncodingContext() {

			@Override
			public String getInEncoding() {
				return in.getValueAsString();
			}

			@Override
			public String getOutEncoding() {
				return out.getValueAsString();
			}
			
		};
	}
	
	/**
	 * DB holds state.
	 * 
	 * <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
     * <property name="username" value="user" />
     * <property name="password" value="pwd" />
     * <property name="url" value="some:url" />
     * <property name="driverClassName" value="some.class.Driver" />
     * <property name="initialSize" value="5" />
     * <property name="maxActive" value="10" />
     * <property name="testOnBorrow" value="true" />
     * <property name="validationQuery" value="SELECT 1" />
	 * </bean>
	 */
	public ConnectionContext getConnectionContext() {
		final Parameter driver = parameterDao.find(DB_DRIVER);
		final Parameter ip = parameterDao.find(DB_IP);
		final Parameter port = parameterDao.find(DB_PORT);
		final Parameter name = parameterDao.find(DB_NAME);
        final Parameter url = parameterDao.find(DB_URL);
		final Parameter schema = parameterDao.find(DB_SCHEMA);
		final Parameter user = parameterDao.find(DB_USER);
		final Parameter passwd = parameterDao.find(DB_PASSWD);
		final Parameter maxactive = parameterDao.find(DB_MAXACTIVE);
		
		return new ConnectionContext() {
			@Override
			public String getUser() {
				return user.getValueAsString();
			}
			
			@Override
			public String getUrl() {
				return url.getValueAsString();
			}
			
			@Override
			public String getSchema() {
				return schema.getValueAsString();
			}
			
			@Override
			public String getPassword() {
				return passwd.getValueAsString();
			}
			
			@Override
			public String getName() {
				return name.getValueAsString();
			}
			
			@Override
			public String getDriver() {
				return driver.getValueAsString();
			}

			@Override
			public Integer getMaxActive() {
				return maxactive.getValueAsInteger();
			}
		};
	}
	
}
