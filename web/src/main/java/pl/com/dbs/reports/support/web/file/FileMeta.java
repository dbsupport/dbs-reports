/**
 * 
 */
package pl.com.dbs.reports.support.web.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

/**
 * File upload class.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@JsonIgnoreProperties({"content", "file"})
public class FileMeta implements Serializable {
	private static final long serialVersionUID = -1978428468004824529L;
	
	private String name;
	private long size;
	private String type;
	private byte[] content;
	private File file;
	
	public FileMeta(MultipartFile multipart) throws IOException {
		this.name = multipart.getOriginalFilename();
		this.size = multipart.getSize()/1024;
        this.type = multipart.getContentType();
        this.content = multipart.getBytes();
        this.file = multipartToFile(multipart);
	}
	
	public FileMeta(String name, byte[] content) throws IOException {
		this.name = name;
        this.size = content.length/1024;
        this.type = "FIXME";
        this.content = content;
        this.file = bytesToFile(name, content);
	}	
	
    /**
     * Copies uploaded file to temp disc folder.
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
    
    /**
     * Copies bytes to temp disc folder.
     */
    public static File bytesToFile(String name, byte[] content) throws IOException {
    	File tmpDir = Files.createTempDir();
        File tmpFile = new File(tmpDir, name);
        tmpFile.deleteOnExit();
        FileCopyUtils.copy(content, new FileOutputStream(tmpFile));
        return tmpFile;
    }    

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

	public String getType() {
		return type;
	}

	public byte[] getContent() {
		return content;
	}

	public File getFile() {
		return file;
	}		
}
