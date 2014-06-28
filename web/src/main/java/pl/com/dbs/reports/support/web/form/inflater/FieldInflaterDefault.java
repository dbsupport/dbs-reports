/**
 * 
 */
package pl.com.dbs.reports.support.web.form.inflater;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.support.db.dao.Dao;
import pl.com.dbs.reports.support.web.form.field.IFieldInflatable;
import pl.com.dbs.reports.support.web.form.option.FieldOption;

/**
 * Inflates for form fields that operates on local db.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Service
public class FieldInflaterDefault implements FieldInflater {
	private static final Logger logger = Logger.getLogger(FieldInflaterDefault.class);
	private static final String LOCAL = "LOCAL:";
	private static final Pattern LOCAL_PATTERN = Pattern.compile("^"+LOCAL, Pattern.CASE_INSENSITIVE);
	@Autowired private Dao dao;
	
	@Override
	public boolean supports(IFieldInflatable field) {
		if (StringUtils.isBlank(field.getSource())) return false;
		
		return !PREFIX_PATTERN.matcher(field.getSource()).find()
				||LOCAL_PATTERN.matcher(field.getSource()).find();
	}

	@Override
	public FieldInflaterDefault inflate(IFieldInflatable field) {
		String sql = field.getSource();
		if (LOCAL_PATTERN.matcher(field.getSource()).find()) {
			String[] source = field.getSource().split(LOCAL);
			sql = source[1];
		}
		logger.debug("Local inflater ready to execute sql:"+sql);
		
		List<?> rows = dao.executeQuery(sql);
		logger.debug("Local inflater succesfully executed sql:"+sql);
		
		for (Object row : rows) {
			String value = null;
			String label = null;
			if (row instanceof Object[]) {
				Object[] entry = (Object[])row;
				if (entry.length>1) {
					value = (String)entry[0];
					label = (String)entry[1];
				} else if (entry.length==1) {
					value = (String)entry[0];
					label = (String)entry[0];
				}
			} else if (row instanceof Object) {
				Object entry = (Object)row;
				value = (String)entry;
				label = (String)entry;
			}
			if (!StringUtils.isBlank(value)) {
				FieldOption option = new FieldOption(value, label);
				field.inflate(option);
			}
		}
		
		return this;
	}

}
