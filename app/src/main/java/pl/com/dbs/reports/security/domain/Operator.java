package pl.com.dbs.reports.security.domain;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class Operator extends User {
	private static final long serialVersionUID = -2698295268207284285L;
	
	private String firstname;
	private String surename;
	private String pesel;
	private Long   defId;
	private String identity;
	private String identityType;
	//private List<OperatorGroup> groups;

	public Operator() {
		super("test", "PROTECTED", Collections.EMPTY_LIST);
	}
	
//	@SuppressWarnings("unchecked")
//	public Operator(String username, OperatorGroup group) {
//		super(username, "PROTECTED", Collections.EMPTY_LIST);
//		this.firstname = username;
//		this.surename = username;
//		this.pesel = username;
////		this.groups = new ArrayList<OperatorGroup>();
////		this.groups.add(group);
//	}
	
//	public boolean isInGroup(OperatorGroup group) {
//		return this.groups.contains(group);
//	}
//	public boolean isCreator() {
//		return isInGroup(OperatorGroup.CREATOR);
//	}
//	
//	public boolean isFirstHandAcceptator() {
//		return isInGroup(OperatorGroup.FIRST_HAND_ACCEPTATOR);
//	}
//	
//	public boolean isSecondHandAcceptator() {
//		return isInGroup(OperatorGroup.SECOND_HAND_ACCEPTATOR);
//	}	
	
	public String getFirstname() {
		return firstname;
	}

	public String getSurename() {
		return surename;
	}
	
	public String getFirstnameSurename() {
		return getFirstname()+" "+getSurename();
	}
	
	public String getPesel() {
		return pesel;
	}

	public Long getDefId() {
		return defId;
	}

	public String getIdentity() {
		return identity;
	}

	public String getIdentityType() {
		return identityType;
	}
	
	
}
