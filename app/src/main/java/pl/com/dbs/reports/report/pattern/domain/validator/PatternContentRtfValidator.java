/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;


/**
 * Validate transformate in RTF format.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class PatternContentRtfValidator { //extends PatternValidator {
//	private RTFEditorKit parser = new RTFEditorKit();

//	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
//      try {
//    	Document document = parser.createDefaultDocument();
//    	parser.read(new ByteArrayInputStream(content), document, 0);
//	    } catch (IOException e) {
//	    	throw new PatternValidationException("FIXME");
//	    } catch (BadLocationException e) {
//	    	throw new PatternValidationException(e, "FIXME");
//		}		
	}

//	@Override
	public int getOrder() {
		return 7000;
	}	
}
