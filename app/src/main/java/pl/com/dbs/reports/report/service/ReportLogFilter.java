/**
 * 
 */
package pl.com.dbs.reports.report.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

import pl.com.dbs.reports.api.report.ReportLoggings;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Reports logs filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public class ReportLogFilter extends Filter<ILoggingEvent> {
    private Level level;
    private String logger;

    @Override
    public FilterReply decide(ILoggingEvent event) {
    	if (!isStarted()) return FilterReply.NEUTRAL;
        if (!supports(event)) return FilterReply.DENY;
        
        markers(event);
        return FilterReply.NEUTRAL;
    }
    
    private boolean supports(final ILoggingEvent event) {
        if (StringUtils.isBlank(MDC.get(ReportLoggings.MDC_ID))) return false;
    	if (!event.getLoggerName().startsWith(logger)) return false;
        if (!event.getLevel().isGreaterOrEqual(level)) return false;
        return true;
    }
    
    /**
     * ..store report markers as MDC property..
     */
    private void markers(ILoggingEvent event) {
		if (event.getMarker()!=null&&ReportLoggings.MRK_USER.contains(event.getMarker())) {
			event.getMDCPropertyMap().put(ReportLoggings.MRK_USER.getName(), Boolean.TRUE.toString());
		} else { 
			event.getMDCPropertyMap().remove(ReportLoggings.MRK_USER.getName());
		}
    }

	public void setLevel(Level level) {
		this.level = level;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}


}
