package com.microstep.android.onclick.rest.model;

import java.util.ArrayList;
import java.util.List;

public class DataXml {

    protected List<Login> login = new ArrayList<Login>();
    protected List<CategoryXml> categoryXml = new ArrayList<CategoryXml>();
    protected List<IdentityXml> identityXml = new ArrayList<IdentityXml>();
    protected List<ProductXml> productXml = new ArrayList<ProductXml>();
    protected List<LocationXml> locationXml = new ArrayList<LocationXml>();
    protected List<OrganizationXml> organizationXml = new ArrayList<OrganizationXml>();
    
    public List<Login> getLogin() {
        return this.login;
    }
    
    public List<CategoryXml> getCategoryXml() {
		return categoryXml;
	}
    
    public List<IdentityXml> getIdentityXml() {
		return identityXml;
	}
    
    public List<ProductXml> getProductXml() {
		return productXml;
	}
    
    public List<OrganizationXml> getOrganizationXml() {
		return organizationXml;
	}
    
    public List<LocationXml> getLocationXml() {
		return locationXml;
	}

}
