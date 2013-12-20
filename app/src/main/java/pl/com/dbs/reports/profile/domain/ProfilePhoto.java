/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * TODO
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
	
	public ProfilePhoto(String path,
						byte[] content) {
		this.path = path;
		this.content = content;
	}

}
