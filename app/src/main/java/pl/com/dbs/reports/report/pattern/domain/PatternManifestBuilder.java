/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.api.report.pattern.PatternManifest;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
final class PatternManifestBuilder {
	private static final Logger logger = Logger.getLogger(PatternManifestBuilder.class);
	private static byte[] ZIP_CHUNK = { 'P', 'K', 0x3, 0x4 };

	private byte[] content;	
	private String name;
	private String version;
	private String author;
	private List<String> accesses;
	private PatternManifest manifest;
	private Attributes attributes;
	private String nameTemplate;
	private String factory;
	
	public PatternManifestBuilder(final File file) throws IOException, PatternValidationException {
		this(FileUtils.readFileToByteArray(file));
		if (file==null) throw new PatternValidationException("report.pattern.import.file.empty");
		if (!isZip(file)) throw new PatternValidationException("report.pattern.import.file.no.zip");
	}
	
	public PatternManifestBuilder(final byte[] content) {
		Validate.notNull(content, "A content is no more!");
		Validate.isTrue(content.length>0, "A content is 0 size!");
		this.content = content;
		this.accesses = new ArrayList<String>();
	}
	
	public PatternManifestBuilder build() throws IOException {
		resolveManifest();
		resolveAccesses();
		return this;
	}
	
	public PatternManifest getManifest() {
		return manifest;
	}
	
	
	public byte[] getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getAuthor() {
		return author;
	}

	public List<String> getAccesses() {
		return accesses;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public String getNameTemplate() {
		return nameTemplate;
	}

	public String getFactory() {
		return factory;
	}

	/**
	 * Find manifest in zip file. First occurance.
	 */
	private void resolveManifest() throws IOException {
		Validate.notNull(content, "Content is no more!");
		ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(content));
		Validate.notNull(zip, "A zip file is no more!");
		
		logger.info("Resolving manifest file..");
		
		ZipEntry entry = null;
		while ((entry = zip.getNextEntry()) != null) {
            if (entry.isDirectory()) { 
            	continue;
            } else if (isManifestFile(entry.getName())) {
            	logger.info("Manifest file fount as: "+entry.getName());
            	this.manifest = new ReportPatternManifest(zip);
        		this.attributes = manifest.getPatternAttributes();
        		this.name = manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_NAME);
        		this.version = manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_VERSION);		
        		this.nameTemplate = (!StringUtils.isBlank(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_NAME_TEMPLATE))?
        								manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_NAME_TEMPLATE):
        								ReportPatternManifest.REPORT_NAME_TEMPLATE_DEFAULT);
        		this.factory = StringUtils.isBlank(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_FACTORY))?
        							   		PatternFactoryDefault.class.getName():
       							   			manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_FACTORY);
        		this.author = manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_AUTHOR);            	
            	
            	break;
            }
        }
	}	
	
	private void resolveAccesses() {
		Validate.notNull(manifest, "Manifest is no more!");
		String accesses = manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_ACCESSES);		
		
		logger.info("Resolving accesses..");
		if (!StringUtils.isBlank(accesses)) {
			StringTokenizer st = new StringTokenizer(accesses, ";");
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trim(st.nextToken());
				if (!StringUtils.isBlank(token)) {
					logger.info("Access found as: "+token);
					this.accesses.add(token);
				}
			}
		}
	}

	/**
	 * Is this manifest file name?
	 */
	private static boolean isManifestFile(String filename) {
        return ReportPatternManifest.MANIFEST_PATTERN.matcher(filename).find();
	}
	
	/**
	 * Is this a zip file?
	 */
	private static boolean isZip(File file) {
		boolean isZip = true;
		byte[] buffer = new byte[ZIP_CHUNK.length];
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.readFully(buffer);
			for (int i = 0; i < ZIP_CHUNK.length; i++) {
				if (buffer[i] != ZIP_CHUNK[i]) {
				    isZip = false;
				    break;
				}
			}
			raf.close();
		} catch (Throwable e) {
			isZip = false;
		}
			return isZip;
		}		
	
}
