/**
 * 
 */
package pl.com.dbs.reports.report.domain.pattern.defaultt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.jar.Manifest;
import java.util.zip.Deflater;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.report.api.PatternFactory;
import pl.com.dbs.reports.report.api.PatternManifestValidationException;
import pl.com.dbs.reports.report.domain.pattern.PatternAsset;
import pl.com.dbs.reports.report.domain.pattern.ReportPattern;

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
	
	@Autowired private PatternManifestFactoryDefault manifestFactory;
	
	public PatternFactoryDefault() {}
	
	/**
	 * zwaliduj manifest
	 * Zwaliduj zawartosc paczki
	 * Zwaliduj dane w pliku definicji (SQL)
	 * 
	 * Wyprodukuj obiekt.
	 */
	@Override
	public ReportPattern produce(File file, Manifest manifest) throws IOException, PatternManifestValidationException {
		Validate.notNull(file, "A file is no more!");
		Validate.notNull(manifest, "Manifest file is no more!");
		
//		ZipFile zip = new ZipFile(file);
//		Validate.notNull(zip, "Zip file is no more!");
//		logger.info("Extracting zip file with default method:"+zip.getName());

		//..find manifest file first..
		
		PatternAsset assets = null;
		
	    Deflater deflater = new Deflater(Deflater.DEFLATED, false);
	    deflater.setInput(IOUtils.toByteArray(new FileReader(file)));
	    deflater.finish();
	    ByteArrayOutputStream zip = new ByteArrayOutputStream();
	    try {
	    	while (!deflater.finished()) {
		        byte[] byRead = new byte[4096];
		        int iBytesRead = deflater.deflate(byRead);
		        if (iBytesRead == byRead.length) zip.write(byRead);
		        else zip.write(byRead, 0, iBytesRead);
	    	}
	    	deflater.end();
	    	logger.info("File "+file.getName()+" extracted to byte array.");
	    	assets = new PatternAsset(file.getName(), zip.toByteArray());
	    } finally {
	      zip.close();
	    }
		
		//..instantiate report pattern..
		ReportPattern pattern = new ReportPattern(manifestFactory.create(manifest), assets);
		
//		logger.info("Extracting zip file with default method:"+zip.getName());
//		ZipEntry entry;
//		for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
//			entry = e.nextElement();
//           
//            //if the entry is directory, leave it. Otherwise extract it.
//            if (entry.isDirectory()) {
//            	logger.info("Directory found: "+entry.getName()+" Skipping.");
//            	continue;
//            } else {
//            	logger.info("File found: "+entry.getName());
//                /*
//                 * Get the InputStream for current entry
//                 * of the zip file using
//                 */
//                BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
//
//                /*
//                 * read the current entry from the zip file, extract it
//                 * and write to string buffer
//                 */
//                int b;
//                byte buffer[] = new byte[1024];
//                StringBuilder sb = new StringBuilder();
//                while ((b = bis.read(buffer, 0, 1024)) != -1) sb.append(b);
//
//                //close the input stream.
//                bis.close();
//                
//                //..create asset..
//                Asset asset = new PatternAsset(entry.getName(), sb);
//                logger.info("Assets created: "+asset.getName());
//                pattern.addAsset((PatternAsset)asset);
//            }
//        }
		
		return pattern;
	}

	/* (non-Javadoc)
	 * @see pl.com.dbs.reports.report.domain.pattern.PatternFactory#getName()
	 */
	@Override
	public String getName() {
		return PatternFactoryDefault.class.getCanonicalName();
	}

}
