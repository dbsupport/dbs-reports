/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import java.io.File;
import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.io.FileUtils;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * Profile photo data.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tpr_photo")
public class ProfilePhoto implements IEntity {
	private static final long serialVersionUID = -4233780517131203738L;

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_photo", sequenceName = "spr_photo", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_photo")
	private Long id;	
	
	@Column(name = "path")
	private String path;
	
	@Column(name = "content")
	private byte[] content;
	
	public ProfilePhoto() {/*JPA*/}
	
	public ProfilePhoto(File file) throws IOException {
		this.path = file.getName();
		this.content = FileUtils.readFileToByteArray(file);
	}

	public Long getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public byte[] getContent() {
		return content;
	}
}
