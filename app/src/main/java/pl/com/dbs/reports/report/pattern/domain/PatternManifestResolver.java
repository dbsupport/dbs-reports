/**
 * 
 */
package pl.com.dbs.reports.report.pattern.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Bootstrap logic for Manifest.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.pattern.manifest.resolver")
public class ManifestResolver {
	private static final Log logger = LogFactory.getLog(ManifestResolver.class);
	
	@Autowired private List<PatternFactory> factories;
	
	private static byte[] ZIP_CHUNK = { 'P', 'K', 0x3, 0x4 };

	/**
	 * Get manifest from zip file and according 
	 * to its properties return proper pattern factory.
	 */
	public PatternFactory resolveFactory(final File file) throws PatternFactoryNotFoundException, PatternValidationException, IOException {
		if (file==null) throw new PatternValidationException("report.import.file.empty");
		if (!isZip(file)) throw new PatternValidationException("report.import.file.no.zip");
		
		PatternManifestBuilder builder = new PatternManifestBuilder(file);
		builder.build();
		Validate.notNull(builder.getManifest(), "Manifest is no more!");
		
		//..given or default factory..
		final String factory = builder.getFactory();
		
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
