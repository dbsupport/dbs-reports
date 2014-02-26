/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternManifest;
import pl.com.dbs.reports.profile.domain.Profile;

/**
 * Pattern builder.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
final class PatternBuilder {
	private static final Logger logger = Logger.getLogger(PatternBuilder.class);
	
	private String filename;
	private byte[] content;	
	private Profile profile;
	private List<ReportPatternTransformate> transformates;
	private List<ReportPatternInflater> inflaters;
	private List<ReportPatternForm> forms;
	private Map<String, byte[]> inits;
	
	private PatternManifestBuilder manifestBuilder;
	
	private ReportPattern pattern;
	
	public PatternBuilder(final File file, Profile profile) throws IOException {
		this(FileUtils.readFileToByteArray(file));
		this.filename = file.getName();
		this.profile = profile;
	}
	
	private PatternBuilder(final byte[] content) {
		Validate.notNull(content, "A content is no more!");
		Validate.isTrue(content.length>0, "A content is 0 size!");
		this.content = content;
		this.transformates = new ArrayList<ReportPatternTransformate>();
		this.inflaters = new ArrayList<ReportPatternInflater>();
		this.forms = new ArrayList<ReportPatternForm>();
		this.inits = new HashMap<String, byte[]>();
		
		manifestBuilder = new PatternManifestBuilder(content);
	}
	
	public PatternBuilder build() throws IOException {
		manifestBuilder.build();
		
		resolveTransformates();
		resolveInflaters();		
		resolveForms();
		resolveInitFiles();
		
		/**
		 * assigment of inflaters..
		 */
		for (ReportPatternTransformate transformate : transformates)
			for (ReportPatternInflater inflater : inflaters)
				transformate.addInflater(inflater);
		
		this.pattern = new ReportPattern(new ReportPatternCreation() {
					@Override
					public String getVersion() {
						return manifestBuilder.getVersion();
					}
					
					@Override
					public List<ReportPatternTransformate> getTransformates() {
						return transformates;
					}
					
					@Override
					public String getName() {
						return manifestBuilder.getName();
					}
					
					@Override
					public PatternManifest getManifest() {
						return manifestBuilder.getManifest();
					}
					
					@Override
					public List<ReportPatternForm> getForms() {
						return forms;
					}
					
					@Override
					public String getFilename() {
						return filename;
					}
					
					@Override
					public String getFactory() {
						return manifestBuilder.getFactory();
					}
					
					@Override
					public Profile getCreator() {
						return profile;
					}
					
					@Override
					public byte[] getContent() {
						return content;
					}
					
					@Override
					public String getAuthor() {
						return manifestBuilder.getAuthor();
					}
					
					@Override
					public List<String> getAccesses() {
						return manifestBuilder.getAccesses();
					}
				});

		this.pattern.addInits(inits);
		return this;
	}
	
	public ReportPattern getPattern() {
		return pattern;
	}

	
	private void resolveInflaters() throws IOException {
		Validate.notNull(content, "Content is no more!");
		ZipArchiveInputStream zip = new ZipArchiveInputStream(new ByteArrayInputStream(content), "UTF-8", true);
		Validate.notNull(zip, "A zip file is no more!");
		
		logger.info("Resolving inflaters..");
		ArchiveEntry entry = null;
		while ((entry = zip.getNextEntry()) != null) {
            if (entry.isDirectory()) { 
            	continue;
            } else if (isInflater(entry.getName())) {
            	logger.info("Inflater found in file: "+entry.getName());
           		inflaters.add(new ReportPatternInflater(readArchiveEntry(zip, entry), entry.getName()));
            }
        }				
	}
	
	private void resolveTransformates() throws IOException {
		Validate.notNull(content, "Content is no more!");
		ZipArchiveInputStream zip = new ZipArchiveInputStream(new ByteArrayInputStream(content), "UTF-8", true);
		//ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(content));
		Validate.notNull(zip, "A zip file is no more!");

		logger.info("Resolving transformates..");
		ArchiveEntry entry = null;
		while ((entry = zip.getNextEntry()) != null) {
            if (entry.isDirectory()) { 
            	continue;
            } else if (isTransformate(entry.getName())) {
            	PatternFormat format = resolveFormat(entry.getName());
            	logger.info("Transformate found in file: "+entry.getName()+" as :"+format);
           		transformates.add(new ReportPatternTransformate(readArchiveEntry(zip, entry), entry.getName(), format));
            }
        }
	}		
	
	/**
	 * Input forms definitions..
	 */
	private void resolveForms() throws IOException {
		Validate.notNull(content, "Content is no more!");
		ZipArchiveInputStream zip = new ZipArchiveInputStream(new ByteArrayInputStream(content), "UTF-8", true);
		Validate.notNull(zip, "A zip file is no more!");

		logger.info("Resolving forms..");
		ArchiveEntry entry = null;
		while ((entry = zip.getNextEntry()) != null) {
            if (entry.isDirectory()) { 
            	continue;
            } else if (isForm(entry.getName())) {
            	logger.info("Form found in file: "+entry.getName());
           		forms.add(new ReportPatternForm(readArchiveEntry(zip, entry), entry.getName()));
            }
        }
	}
	
	/**
	 * Some init sqls?
	 */
	private void resolveInitFiles() throws IOException {
		Validate.notNull(content, "Content is no more!");
		ZipArchiveInputStream zip = new ZipArchiveInputStream(new ByteArrayInputStream(content), "UTF-8", true);
		Validate.notNull(zip, "A zip file is no more!");

		logger.info("Resolving init files..");
		ArchiveEntry entry = null;
		while ((entry = zip.getNextEntry()) != null) {
            if (entry.isDirectory()) { 
            	continue;
            } else if (isInitFile(entry.getName())) {
            	logger.info("Init file found in file: "+entry.getName());
            	this.inits.put(entry.getName(), readArchiveEntry(zip, entry));
            }
        }
	}		

	/**
	 * Is this init sql file?
	 */
	private boolean isInitFile(String filename) {
		if (!isRootDirectory(filename)) return false;
		
		PatternManifest manifest = manifestBuilder.getManifest();
		Validate.notNull(manifest, "Manifest is no more!");
		String sqls = manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_INIT_SQL);		

		if (!StringUtils.isBlank(sqls)) {
			StringTokenizer st = new StringTokenizer(sqls, ";");
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trim(st.nextToken());
				if (token.equalsIgnoreCase(filename)) return true;
			}
		}
        return false;
	}		
	
	/**
	 * Is this inflater file?
	 */
	private boolean isInflater(String filename) {
		return isRootDirectory(filename)&&ReportPatternManifest.INFLATER_PATTERN.matcher(filename).find()&&!isInitFile(filename);
	}
	
	/**
	 * Is this form file?
	 */
	private boolean isForm(String filename) {
		if (!isRootDirectory(filename)) return false;
		
		PatternManifest manifest = manifestBuilder.getManifest();
		Validate.notNull(manifest, "Manifest is no more!");
		
		String forms = manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_FORM_FILENAME);		

		if (!StringUtils.isBlank(forms)) {
			StringTokenizer st = new StringTokenizer(forms, ";");
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trim(st.nextToken());
				if (token.equalsIgnoreCase(filename)) return true;
			}
		}
        return false;
	}	
	
	/**
	 * Is this manifest file name?
	 */
	private static boolean isManifestFile(String filename) {
        return ReportPatternManifest.MANIFEST_PATTERN.matcher(filename).find();
	}	
	
	/**
	 * Transformate can be anything..
	 */
	private boolean isTransformate(String filename) {
		return     isRootDirectory(filename) 
				&&!isManifestFile(filename)
				&&!isInitFile(filename)
				&&!isInflater(filename)
				&&!isForm(filename);
	}

	/**
	 * File name containf path separator "/" ?
	 */
	private boolean isRootDirectory(String filename) {
		File f = new File(filename);
		return f.getParent()==null;
//		String path = filename.replace("/", File.separator);
//		return !path.contains(File.separator)&&path.st;
	}
	
	private PatternFormat resolveFormat(final String name) throws IOException {
		PatternManifest manifest = manifestBuilder.getManifest();
		Validate.notNull(manifest, "Manifest is no more!");
		String exts = manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_EXTENSION_MAP);	
		
		if (!StringUtils.isBlank(exts)) {
			//.resolve from map..
			StringTokenizer st = new StringTokenizer(exts, ";");
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trim(st.nextToken());
				/**
				 * Find pairs filename=ext
				 */
			    Matcher m = ReportPatternManifest.EXTENSION_PATTERN.matcher(token);
			    m.reset();
			    while (m.find()) {
			    	/**
			    	 * Confirm if file exist with given filename...
			    	 */
			    	final String filename = StringUtils.trim(m.group(1));
			    	final String engext = StringUtils.trim(m.group(2));
			    	if (name.equalsIgnoreCase(filename)) {
					    Matcher mext = ReportPatternManifest.EXTENSION_ENGINE_PATTERN.matcher(engext);
					    mext.reset();
					    while (mext.find()) {
					    	/**
					    	 * i.e. 
					    	 * doc|rtf = format doc, extension rtf
					    	 * |xml = format txt (default), extension xml
					    	 * pdf| = format pdf, pdf (default ext of format)
					    	 */
					    	String eng = StringUtils.trim(mext.group(1));
					    	String ext = StringUtils.trim(mext.group(2));
					    	final ReportType format = StringUtils.isBlank(eng)?ReportType.TXT:ReportType.of(eng);
					    	final String extension = StringUtils.isBlank(ext)?format.getDefaultExt():ext;
					    	return new ReportPatternFormat(filename, format, extension);
					    }
			    	}
			    }
			}
		}
		//..default everything is a text..
		return new ReportPatternFormat(name);
	}	
	

//	/**
//	 * Read entry part of zip.
//	 */
//	private byte[] readZipEntry(final ZipInputStream zip, final ZipEntry entry) throws IOException {
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		byte[] buffer = new byte[1024];
//		int bytes;
//
//		while ((bytes = zip.read(buffer)) != -1) 
//	        output.write(buffer, 0, bytes);
//	    
//	    output.close();
//	    return output.toByteArray();
//	}
	
	private byte[] readArchiveEntry(final ZipArchiveInputStream zip, final ArchiveEntry entry) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytes;
		
		while ((bytes = zip.read(buffer)) != -1) 
	        output.write(buffer, 0, bytes);
	    
	    output.close();
	    return output.toByteArray();
	}	
	
}
