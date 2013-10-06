/**
 * 
 */
package pl.com.dbs.reports.report.pattern.application;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.inner.report.pattern.Pattern;
import pl.com.dbs.reports.api.inner.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.inner.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.pattern.domain.PatternAsset;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

/**
 * Domyslna obsluga paczek z wzorcami (definicjami) raportow.
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.pattern.factory.default")
public final class PatternFactoryDefault implements PatternFactory {
	private static final Log logger = LogFactory.getLog(PatternFactoryDefault.class);
	
	@Autowired private PatternManifestValidatorDefault manifestValidator;
	@Autowired private PatternContentValidatorDefault contentValidator;
	@Autowired private PatternManifestResolver manifestResolver;
	
	public PatternFactoryDefault() {}
	
	/**
	 * zwaliduj manifest
	 * Zwaliduj zawartosc paczki
	 * Zwaliduj dane w pliku definicji (SQL)
	 * 
	 * Wyprodukuj obiekt.
	 */
	@Override
	public Pattern produce(File file) throws IOException, PatternValidationException {
		Validate.notNull(file, "A file is no more!");

		
		ZipFile zip = new ZipFile(file);
		Validate.notNull(zip, "Zip file is no more!");
		logger.info("Extracting zip file with default method:"+zip.getName());

		
		Manifest manifest = null;
		List<PatternAsset> assets = new ArrayList<PatternAsset>();

		ZipEntry entry;
		for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
			entry = e.nextElement();
           
            //if the entry is directory, leave it. Otherwise extract it.
            if (entry.isDirectory()) {
            	logger.info("Directory found: "+entry.getName()+" Skipping.");
            	continue;
            } else if (PatternManifestResolver.isManifestFile(entry.getName())) {
            	logger.info("Manifest found: "+entry.getName());
            	BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
            	manifest = new Manifest(bis);            	
            	continue;
            } else {
            	logger.info("Asset file found: "+entry.getName());
            	StringBuilder sb = readZipEntry(zip, entry);
                //..create and add asset..
            	assets.add(new PatternAsset(entry.getName(), String.valueOf(sb).getBytes()));
            }
        }
		
		//..validate manifest..
		manifestValidator.validate(manifest);
		//..validate content..
		contentValidator.validate(manifest, assets);
		
		//..instantiate report pattern..
		return new ReportPattern(manifest, assets, null);
	}
	
	/**
	 * Put entry to StringBuilder.
	 */
	private StringBuilder readZipEntry(ZipFile zip, ZipEntry entry) throws IOException {
        /*
         * Get the InputStream for current entry
         * of the zip file using
         */
        BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));

        /*
         * read the current entry from the zip file, extract it
         * and write to string buffer
         */
        int b;
        byte buffer[] = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while ((b = bis.read(buffer, 0, 1024)) != -1) sb.append(b);

        //close the input stream.
        bis.close();
        
        return sb;
	}

	/* (non-Javadoc)
	 * @see pl.com.dbs.reports.report.domain.pattern.PatternFactory#getName()
	 */
	@Override
	public String getName() {
		return PatternFactoryDefault.class.getCanonicalName();
	}

	
//	ZipFile zip = new ZipFile(file);
//	Validate.notNull(zip, "Zip file is no more!");
//	logger.info("Extracting zip file with default method:"+zip.getName());
	//..find manifest file first..
//    Deflater deflater = new Deflater(Deflater.DEFLATED, false);
//    deflater.setInput(IOUtils.toByteArray(new FileReader(file)));
//    deflater.finish();
//    ByteArrayOutputStream zip = new ByteArrayOutputStream();
//    try {
//    	while (!deflater.finished()) {
//	        byte[] byRead = new byte[4096];
//	        int iBytesRead = deflater.deflate(byRead);
//	        if (iBytesRead == byRead.length) zip.write(byRead);
//	        else zip.write(byRead, 0, iBytesRead);
//    	}
//    	deflater.end();
//    	
//    	//logger.info("File "+file.getName()+" extracted to byte array.");
//    } finally {
//      zip.close();
//    }

//	//..zip file as assest..
//    PatternAsset assets = new PatternAsset(file.getName(), FileUtils.readFileToByteArray(file));
//		
	
}
