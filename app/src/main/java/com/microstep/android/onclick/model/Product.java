package com.microstep.android.onclick.model;

import java.io.Serializable;

import com.microstep.android.onclick.util.Currency;
import com.microstep.android.onclick.util.Language;

public class Product implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private Double count;
	private Double price;
	private boolean selected;
	private Shop shop;
	private long created;
	private long modified;
	private int unit;
	private Category category;
	private Integer identityId;
	private Integer categoryId;
	private Currency currency;
	private String identifier;
	private Language language;
	private String imageSmallPath;
	private String imageNormalPath;
    private String priceDescription;
    private String description;
    private Organization organization;  
    private Category organizationCategory;
    private Long organizationProductId;
    private Long organizationLocationId;
	
	public Integer getIdentityId() {
		return identityId;
	}

	public void setIdentityId(Integer identityId) {
		this.identityId = identityId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getImageSmallPath() {
		return imageSmallPath;
	}

	public void setImageSmallPath(String imageSmallPath) {
		this.imageSmallPath = imageSmallPath;
	}

	public String getPriceDescription() {
		return priceDescription;
	}

	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}		
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	public Organization getOrganization() {
		return organization;
	}
	
	public void setOrganizationCategory(Category organizationCategory) {
		this.organizationCategory = organizationCategory;
	}
	
	public Category getOrganizationCategory() {
		return organizationCategory;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getImageNormalPath() {
		return imageNormalPath;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setImageNormalPath(String imageNormalPath) {
		this.imageNormalPath = imageNormalPath;
	}
	
	public Long getOrganizationProductId() {
		return organizationProductId;
	}
	
	public void setOrganizationProductId(Long organizationProductId) {
		this.organizationProductId = organizationProductId;
	}
	
	public Long getOrganizationLocationId() {
		return organizationLocationId;
	}
	
	public void setOrganizationLocationId(Long organizationLocationId) {
		this.organizationLocationId = organizationLocationId;
	}
	
}
