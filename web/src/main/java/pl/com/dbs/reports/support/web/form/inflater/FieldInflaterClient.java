/**
 * 
 */
package pl.com.dbs.reports.support.web.form.inflater;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.api.support.db.SqlExecutorContext;
import pl.com.dbs.reports.support.web.form.field.IFieldInflatable;
import pl.com.dbs.reports.support.web.form.option.FieldOption;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Service
public class FieldInflaterClient implements FieldInflater {
	private static final Logger logger = Logger.getLogger(FieldInflaterClient.class);
	
	private static final String PREFIX = "CLIENT:";
	private static final Pattern CLIENT_PATTERN = Pattern.compile("^"+PREFIX, Pattern.CASE_INSENSITIVE);
	@Autowired private SqlExecutor<FieldOption> executor;
	
	
	@Override
	public boolean supports(IFieldInflatable field) {
		if (StringUtils.isBlank(field.getSource())) return false;
		return CLIENT_PATTERN.matcher(field.getSource()).find();
	}

	@Override
	public FieldInflater inflate(final IFieldInflatable field) {
		String[] source = field.getSource().split(PREFIX);
		final String sql = source[1];

		logger.debug("Client inflater ready to execute sql:"+sql);
		
		executor.query(new SqlExecutorContext<FieldOption>(
				sql)
				.mapping(new RowMapper<FieldOption>() {
				@Override
				public FieldOption mapRow(ResultSet rs, int rowNum) throws SQLException {
					ResultSetMetaData metaData = rs.getMetaData();
					int idx = metaData.getColumnCount()>1?2:1;
					String value = null;
					String label = null;
					for (int i=1; i<=idx; i++) {
						final String column = metaData.getColumnName(i);
						//final String value = encodingService.encode(rs.getString(key), encodingContext);
						if (i==1) value = rs.getString(column);
						else label = rs.getString(column);
					}
					FieldOption option = new FieldOption(value, label);
					field.inflate(option);
					return option;
				}
		}));
    	return this;
	}

}
