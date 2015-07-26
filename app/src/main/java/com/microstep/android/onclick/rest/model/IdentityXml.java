package com.microstep.android.onclick.rest.model;

import java.util.ArrayList;
import java.util.List;

public class IdentityXml {

    protected int id;
    protected String name;
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String gender;
    protected String birthDate;
    protected String dateCreated;
    protected String dateModified;
    protected List<IdentityIdentifierXml> 
    	identityIdentifierXml = new ArrayList<IdentityIdentifierXml>();
	
    public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
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
	
	public List<IdentityIdentifierXml> getIdentityIdentifierXml() {
		return identityIdentifierXml;
	}
	
	public void setIdentityIdentifierXml(
			List<IdentityIdentifierXml> identityIdentifierXml) {
		this.identityIdentifierXml = identityIdentifierXml;
	}

}
