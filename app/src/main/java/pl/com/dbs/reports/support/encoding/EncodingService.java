/**
 * 
 */
package pl.com.dbs.reports.support.encoding;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.parameter.service.ParameterService;

/**
 * Encodings..
 * http://www.joelonsoftware.com/articles/Unicode.html
 * http://docs.oracle.com/cd/A97339_01/doc/java.816/a81354/advanc1.htm
 * http://docs.oracle.com/javaee/1.4/tutorial/doc/WebI18N5.html
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Service("encoding.service")
public class EncodingService {
	private static final Logger logger = Logger.getLogger(EncodingService.class);
	@Autowired private ParameterService parameterService;
	
	public String encode(String value, EncodingContext context) {
		try {
			return (!StringUtils.isBlank(value)&&!StringUtils.isBlank(context.getInEncoding())&&!StringUtils.isBlank(context.getOutEncoding()))?
					new String(value.getBytes(Charset.forName(context.getInEncoding())), Charset.forName(context.getOutEncoding())):value;
		} catch (Exception e) {
			logger.error("Error encoding: "+value, e);
		}
		return value;
	}
	
	public String decode(String value, EncodingContext context) {
		try {
			return (!StringUtils.isBlank(value)&&!StringUtils.isBlank(context.getInEncoding()))?
					new String(value.getBytes(), Charset.forName(context.getInEncoding())):value;
		} catch (Exception e) {
			logger.error("Error decoding: "+value, e);
		}
		return value;
	}	
	
	public String encode(String value) {
		EncodingContext context = getEncodingContext();
		return encode(value, context);
	}	
	
	/**
	 * Db query for encoding parameters.
	 */
	public EncodingContext getEncodingContext() {
		return parameterService.getEncodingContext();
	}
}
