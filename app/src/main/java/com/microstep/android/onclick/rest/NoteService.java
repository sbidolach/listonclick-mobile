package com.microstep.android.onclick.rest;

import java.util.Date;
import java.util.List;

import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.OrganizationProductType;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.rest.model.IdentityXml;
import com.microstep.android.onclick.rest.model.Login;
import com.microstep.android.onclick.rest.model.OnClickXml;
import com.microstep.android.onclick.rest.model.ProductXml;
import com.microstep.android.onclick.rest.model.StatusXml;

public interface NoteService {
	
	public Login registerDevice(String deviceId);
	public Login loginDevice(String deviceId);
	public OnClickXml loginDeviceUpdateCategory(String deviceId, Date lastUpdate, String language);
	public StatusXml addNote(String userId, Shop shop, String authToken, String language);	
	public XmlBeanFactory getXmlBeanFactory();
	public StatusXml addDeviceToAccount(String deviceId, String email, String language, String authToken);
	public StatusXml removeDeviceFromAccount(String deviceId, String accountId, String authToken);
	public IdentityXml getAccount(String accountId, String authToken);
	public OnClickXml registerAccount(String email, String password, String currency);
	public List<ProductXml> getProductName(String name, String categoryId);
	public List<Organization> getOrganizationsNearMe(Double latitude, Double longitude);
	public List<Category> getOrganizationCategoryById(Long oid, Long cid);
	public List<Category> getOrganizationCategories(Long oid, String type);
	public List<Product> getOrganizationProductsByCategoryId(Long oid, Long cid, OrganizationProductType pType);
	public List<Product> getOrganizationProductsByName(Long oid, Long cid, String name, OrganizationProductType pType);
	public List<Product> getOrganizationProductsByIdentifier(Long oid, String identifier);
	
}