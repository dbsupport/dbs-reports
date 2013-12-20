/**
 * 
 */
package pl.com.dbs.reports.parameter.service;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		if (!parameter.isSame(value)) {
			parameter.changeValue(value);
			return true;
		}
		return false;
	}
	
}
