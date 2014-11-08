/**
 * 
 */
package pl.com.dbs.reports.report;

import java.util.Stack;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * http://stackoverflow.com/questions/1827677/how-to-do-a-junit-assert-on-a-message-in-a-logger
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public class ReportLogTestAppender extends AppenderBase<ILoggingEvent> {
    private Stack<ILoggingEvent> events = new Stack<ILoggingEvent>();

    @Override
    protected void append(ILoggingEvent event) {
        events.add(event);
    }

    public void clear() {
        events.clear();
    }

    public ILoggingEvent getLastEvent() {
        return events.pop();
    }

}
