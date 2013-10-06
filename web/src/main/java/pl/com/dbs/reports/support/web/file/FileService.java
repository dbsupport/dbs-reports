/**
 * 
 */
package pl.com.dbs.reports.support.web.file;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class FileService {
	
    /**
     * 
     */
    public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
    	File tmpDir = Files.createTempDir();
//        File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +
//        						//UUID.randomUUID() + System.getProperty("file.separator") +
//                                multipart.getOriginalFilename());
        File tmpFile = new File(tmpDir, multipart.getOriginalFilename());
        tmpFile.deleteOnExit();
        multipart.transferTo(tmpFile);
        return tmpFile;
    }		
}
