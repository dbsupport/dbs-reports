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

/**
 * Simple parameters.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("parameter.service")
public class ParameterService {
	private static final String DB_DRIVER = "client.db.driver";
	private static final String DB_IP = "client.db.ip";
	private static final String DB_PORT = "client.db.port";
	private static final String DB_NAME = "client.db.name";
	private static final String DB_SCHEMA = "client.db.schema";
	private static final String DB_USER = "client.db.user";
	private static final String DB_PASSWD = "client.db.passwd";

	/** 
	 * ! w obecnej wersji nie wymagane !
	 */
//	private static final String MAIL_HOST = "mail.host";
//	private static final String MAIL_PORT = "mail.port";
//	private static final String MAIL_USER = "mail.user";
//	private static final String MAIL_PASSWD = "mail.passwd";
//	private static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
//	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
//	private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
//	private static final String MAIL_SENDER_NAME = "mail.sender.name";
//	private static final String MAIL_SENDER_ADDRESS = "mail.sender.address";
//	private static final String MAIL_RECEIVER_ADDRESS = "mail.receiver.address";
	
	@Autowired private ParameterDao parameterDao;

	/**
	 * Get all.
	 */
	public List<Parameter> find()  {
		return parameterDao.find(new ParameterFilter());
	}
	
	public Parameter find(final String key)  {
		Validate.notNull(key, "Key is no more!");
		return parameterDao.find(key);
	}

	/**
	 * Save..
	 */
	@Transactional
	public boolean edit(final String key, final String value) {
		Validate.notNull(key, "Key is no more!");
		Parameter parameter = parameterDao.find(key);
		Validate.notNull(parameter, "No such parameter!");
		boolean result = !parameter.isSame(value);
		parameter.changeValue(value);
		return result;
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
		final Parameter schema = parameterDao.find(DB_SCHEMA);
		final Parameter user = parameterDao.find(DB_USER);
		final Parameter passwd = parameterDao.find(DB_PASSWD);
		
		return new ConnectionContext() {
			@Override
			public String getUser() {
				return user.toString();
			}
			
			@Override
			public String getUrl() {
				return "jdbc:oracle:thin:@"+ip+":"+port+":"+name;
			}
			
			@Override
			public String getSchema() {
				return schema.toString();
			}
			
			@Override
			public String getPassword() {
				return passwd.toString();
			}
			
			@Override
			public String getName() {
				return name.toString();
			}
			
			@Override
			public String getDriver() {
				return driver.toString();
			}
		};
	}
	
//	/**
//	 *  * ! W obecnej wersji nie potrzebne !
//	 */
//	public SenderContext getSenderContext() {
//		final Parameter host = parameterDao.find(MAIL_HOST);
//		final Parameter port = parameterDao.find(MAIL_PORT);
//		final Parameter user = parameterDao.find(MAIL_USER);
//		final Parameter passwd = parameterDao.find(MAIL_PASSWD);
//		final Parameter protocol = parameterDao.find(MAIL_TRANSPORT_PROTOCOL);
//		final Parameter auth = parameterDao.find(MAIL_SMTP_AUTH);
//		final Parameter starttls = parameterDao.find(MAIL_SMTP_STARTTLS_ENABLE);
//		
//		return new SenderContext() {
//
//			@Override
//			public String getHost() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public int getPort() {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//
//			@Override
//			public String getUser() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public String getPassword() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public Properties getJavaMailProperties() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//		};
//	}
	
}
