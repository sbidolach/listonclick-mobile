package com.microstep.android.onclick.rest.model;

public class CategoryXml {

	protected int id;
	protected String name;
	protected String language;
	protected String dateCreated;
	protected String dateModified;
	protected String imageSmallUrl;
	protected String imageNormalUrl;
	protected OrganizationXml organizationXml;
	protected boolean hasChild;
	protected Integer parentCategoryId;
	protected String type;
	protected String url;
	protected String start;
	protected String end;

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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getImageSmallUrl() {
		return imageSmallUrl;
	}

	public void setImageSmallUrl(String imageSmallUrl) {
		this.imageSmallUrl = imageSmallUrl;
	}

	public OrganizationXml getOrganizationXml() {
		return organizationXml;
	}

	public void setOrganizationXml(OrganizationXml organizationXml) {
		this.organizationXml = organizationXml;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public Integer getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Integer parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	public void setImageNormalUrl(String imageNormalUrl) {
		this.imageNormalUrl = imageNormalUrl;
	}
	
	public String getImageNormalUrl() {
		return imageNormalUrl;
	}

}
