package com.microstep.android.onclick.rest.model;

public class IdentityIdentifierXml {
	
	protected String authToken;
	protected String dateCreated;
	protected String dateModified;
	protected int id;
	protected String identifier;
	protected String identityIdentifierType;
	protected String password;
	
	public String getAuthToken() {
		return authToken;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getDateModified() {
		return dateModified;
	}
	
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentityIdentifierType() {
		return identityIdentifierType;
	}
	
	public void setIdentityIdentifierType(String identityIdentifierType) {
		this.identityIdentifierType = identityIdentifierType;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
    	
}
