/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.api.report.ReportFormat;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternManifest;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.db.domain.AEntity;
import pl.com.dbs.reports.support.utils.separator.Separator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Encja z wzocem raportu.
 * Obiekt zawiera paczke danych tak jak zostala dostarczona (czyli spakowane).
 * Dla optymalizacji dane z manifestu sa dostepne "od reki".
 *
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tre_pattern")
public class ReportPattern extends AEntity implements Pattern {
	private static final long serialVersionUID = 8993097483507616245L;
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_pattern", sequenceName = "sre_pattern", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_pattern")
	private Long id;
	
	@Column(name = "active")
	private boolean active;	
	
	@Column(name = "upload_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;

	@OneToOne(fetch=FetchType.EAGER, orphanRemoval=false)
    @JoinColumn(name="creator_id")
    private Profile creator;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "version")
	private String version;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "factory")
	private String factory;
	
	@Convert(converter=ReportPatternManifestConverter.class)
	private ReportPatternManifest manifest;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name = "tre_pattern_access", joinColumns = @JoinColumn(name = "pattern_id"))
	@Column(name = "name")
    private List<String> accesses = new ArrayList<String>();
	
	@OneToMany(mappedBy="pattern", orphanRemoval=true)
    private List<ReportPatternTransformate> transformates = new ArrayList<ReportPatternTransformate>();
	
	@OneToMany(mappedBy="pattern", orphanRemoval=true)
    private List<ReportPatternForm> forms = new ArrayList<ReportPatternForm>();
	
	/**
	 * Init sqls. Accessed only while import (NOT PERSIST) 
	 */
	@Transient
	private Map<String, byte[]> inits = new HashMap<String, byte[]>();
	
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;	
	
	public ReportPattern() {/* JPA */}
	
	public ReportPattern(final byte[] content, 
						 final Profile creator, 
						 final String name, 
						 final String version, 
						 final String author, 
						 final String factory, 
						 final PatternManifest manifest,
						 final List<String> accesses,
						 final List<ReportPatternTransformate> transformates,
						 final List<ReportPatternForm> forms) {
		Validate.isTrue(content.length>0, "A content is 0!");
		Validate.notNull(creator, "Creator is no more!");
	
		this.uploadDate = new Date();
		this.creator = creator;
		this.name = name;		
		this.version = version;
		this.author = author;
		this.factory = factory;
		this.manifest = (ReportPatternManifest)manifest;
		this.accesses = accesses;
		this.transformates = transformates;
		this.content = content;
		this.forms = forms;
		this.active = true;
		
		for (ReportPatternTransformate t : transformates) t.setPattern(this);
		for (ReportPatternForm f : forms) f.setPattern(this);
	}
	
	/**
	 * Deactivate entity.
	 */
	public void deactivate() {
		this.active = false;
	}

	@Override
	public Date getUploadDate() {
		return uploadDate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getFactory() {
		return factory;
	}

	@Override
	public PatternManifest getManifest() {
		return manifest;
	}

	@Override
	public List<? extends PatternTransformate> getTransformates() {
		return transformates;
	}
	
	@Override
	public List<ReportPatternForm> getForms() {
		return forms;
	}

	/**
	 * Get first form.
	 */
	public ReportPatternForm getForm() {
		return forms!=null&&!forms.isEmpty()?forms.get(0):null;
	}

	public long getId() {
		return id;
	}

	public boolean isActive() {
		return active;
	}

	public String getAuthor() {
		return author;
	}
	
	public Profile getCreator() {
		return creator;
	}	
	
	public byte[] getContent() {
		return content;
	}
	
	public List<String> getAccesses() {
		return accesses;
	}
	
	public String getAccessesAsString() {
		StringBuffer sb = new StringBuffer();
		Separator s = new Separator(", ");
		for (String access : accesses) sb.append(s).append(access);
		return sb.toString();
	}	
	
	/**
	 * Is pattern accessigle for given accesses collection?
	 */
	public boolean isAccessible(final List<Access> accesses) {
		for (final Access access : accesses)
			if (Iterables.find(this.accesses, new Predicate<String>() {
					@Override
					public boolean apply(String input) { return input.equals(access.getName()); }
				}, null)!=null) return true;
		return false;
	}
	
	/**
	 * Is accessible for session user?
	 */
	public boolean isAccessible() {
		return SessionContext.getProfile()!=null&&isAccessible(SessionContext.getProfile().getAccesses());
	}
	
	public List<ReportFormat> getFormats() {
		Set<ReportFormat> result = new HashSet<ReportFormat>();
		for (ReportPatternTransformate transformate : transformates) 
			result.add(transformate.getFormat());
		return new ArrayList<ReportFormat>(result);		
	}
	
	public String getFormatsAsString() {
		StringBuffer sb = new StringBuffer();
		Separator s = new Separator(",");
		for (ReportFormat format : getFormats()) sb.append(s).append(format.getExt());
		return sb.toString();		
	}

	public void addInits(Map<String, byte[]> inits) {
		if (inits != null) this.inits.putAll(inits);
	}

	public Map<String, byte[]> getInits() {
		return this.inits;
	}
	
}
