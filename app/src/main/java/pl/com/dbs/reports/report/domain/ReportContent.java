/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import org.apache.commons.lang.Validate;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.domain.ReportPhase.ReportPhaseStatus;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternFormat;
import pl.com.dbs.reports.support.db.domain.AEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * Report entity content.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Entity
@Table(name = "tre_report_content")
public class ReportContent {
	private static final long serialVersionUID = 391747562802238863L;

	@Id
    //@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="report_id", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
	private Report report;


    /**
     * http://stackoverflow.com/questions/10108533/jpa-should-i-store-a-blob-in-the-same-table-with-fetch-lazy-or-should-i-store-i
     * http://www.hostettler.net/blog/2012/03/22/one-to-one-relations-in-jpa-2-dot-0/
     */
    @Lob
    @Basic(optional = true, fetch = FetchType.LAZY)
    private byte[] content;

    public ReportContent() {}

    public ReportContent(byte[] content, Report report) {
        this.content = content;
        this.report = report;
    }


    public byte[] getContent() {
        return content;
    }

    public boolean hasContent() {
        return content!=null&&content.length>0;
    }

}
