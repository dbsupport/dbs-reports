/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.report.api.Pattern;
import pl.com.dbs.reports.report.api.PatternFactory;
import pl.com.dbs.reports.report.domain.pattern.PatternFactoryDefault;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("reports.pattern.manifest.resolver")
public class ManifestResolver {
	private static final Log logger = LogFactory.getLog(ManifestResolver.class);
	private static final java.util.regex.Pattern MANIFEST_PATTERN = java.util.regex.Pattern.compile("^manifest(\\..+)*$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	@Autowired private List<PatternFactory> factories;
	

	/**
	 * Get manifest from zip file and according 
	 * to its properties return proper pattern factory.
	 */
	public PatternFactory resolveFactory(Manifest manifest) throws PatternFactoryNotFoundException {
		Validate.notNull(manifest, "Manifest is no more!");
		
		Attributes attrs = manifest.getMainAttributes();
		//..given or default factory..
		final String factory = StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_FACTORY))?PatternFactoryDefault.class.getName():attrs.getValue(Pattern.ATTRIBUTE_FACTORY);
		
		PatternFactory result = Iterables.find(factories, new Predicate<PatternFactory>() {  
				public boolean apply(PatternFactory f) {  
					return f.getName().equals(factory);
				}  
			}, null);  
		
		if (result==null) {
			logger.warn("Factory NOT found for: "+factory);
			throw new PatternFactoryNotFoundException(factory);
		}
		return result;
	}
	
	
	/**
	 * Find manifest in zip file.
	 * First occurance.
	 */
	public Manifest findManifest(File file) throws IOException, ManifestNotFoundException {
		Validate.notNull(file, "A file is no more!");
		ZipFile zip = new ZipFile(file);
		Validate.notNull(zip, "Zip file is no more!");
		
		logger.info("Searching for manifest file in zip: "+zip.getName());
		ZipEntry entry;
		for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
			entry = e.nextElement();
            if (entry.isDirectory()) { 
            	continue;
            } else {
                if (isManifestFile(entry.getName())) {
                	logger.info("Manifest file found: "+entry.getName());
                	//..Get the InputStream for current entry of the zip file using
                	BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
                	return new Manifest(bis);
                }
            }
        }
		zip.close();
		logger.warn("Manifest file NOT found in: "+file.getName());
		throw new ManifestNotFoundException();
	}	

	/**
	 * Is this manifest file name?
	 */
	public static boolean isManifestFile(String name) {
        return MANIFEST_PATTERN.matcher(name).find();
	}
	
//	
//	/**
//	 * Znajdz manifest.
//	 * http://www.java2s.com/Code/Java/File-Input-Output/Zipunzipbytearray.htm
//	 */
//	public PatternManifest find(byte[] content) throws IOException, PatternManifestValidationException {
//		logger.info("Searching for manifest file in zip content: "+content.length+"b");
//	
//		ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(content));
//		ZipEntry entry = null;
//		while ((entry = zip.getNextEntry()) != null) {
//            Matcher matcher = MANIFEST_PATTERN.matcher(entry.getName());
//            if (matcher.find()) {
//            	logger.info("Manifest file found: "+entry.getName());
////            	FileOutputStream out = new FileOutputStream(entryName);
////            	byte[] byteBuff = new byte[4096];
////            	int bytesRead = 0;
////            	while ((bytesRead = zipStream.read(byteBuff)) != -1) {
////            		out.write(byteBuff, 0, bytesRead);
////            	}
//            	BufferedInputStream bis = new BufferedInputStream(zip);
//            	return new PatternManifestDefault(bis);
//            }
//		    zip.closeEntry();
//		}
//		zip.close();
//		logger.info("Manifest file NOT found in zip content: "+content.length+"b");
//		return null;
//	}	
	
}
