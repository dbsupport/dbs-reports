/**
 * 
 */
package pl.com.dbs.reports.support.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.support.db.ConnectionContext;
import pl.com.dbs.reports.api.support.db.ConnectionService;
import pl.com.dbs.reports.parameter.dao.ParameterDao;
import pl.com.dbs.reports.parameter.domain.Parameter;

/**
 * Serve connection data for client db connection.
 * 
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("connection.default.service")
public class ConnectionDeafultService implements ConnectionService {
	@Autowired private ParameterDao parameterDao;
	private static final String DB_DRIVER = "client.db.driver";
	private static final String DB_IP = "client.db.ip";
	private static final String DB_PORT = "client.db.port";
	private static final String DB_NAME = "client.db.name";
	private static final String DB_SCHEMA = "client.db.schema";
	private static final String DB_USER = "client.db.user";
	private static final String DB_PASSWD = "client.db.passwd";
	
	@Override
	public ConnectionContext getContext() {
		//FIXME: keszowanie na 15 min?
		final Parameter driver = parameterDao.find(DB_DRIVER);
		final Parameter ip = parameterDao.find(DB_IP);
		final Parameter port = parameterDao.find(DB_PORT);
		final Parameter name = parameterDao.find(DB_NAME);
		final Parameter schema = parameterDao.find(DB_SCHEMA);
		final Parameter user = parameterDao.find(DB_USER);
		final Parameter passwd = parameterDao.find(DB_PASSWD);
		
		return new ConnectionContext() {
			@Override
			public String getUrl() {
				return "jdbc:oracle:thin:@"+ip+":"+port+":"+name;
			}

			@Override
			public String getUser() {
				return user.toString();
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

			@Override
			public String getSchema() {
				return schema.toString();
			}
		};
	}
	
}
