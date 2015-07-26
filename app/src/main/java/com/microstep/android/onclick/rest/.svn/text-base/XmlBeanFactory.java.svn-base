package com.microstep.android.onclick.rest;

import java.util.ArrayList;
import java.util.List;

import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Location;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.rest.model.CategoryXml;
import com.microstep.android.onclick.rest.model.LocationXml;
import com.microstep.android.onclick.rest.model.NoteXml;
import com.microstep.android.onclick.rest.model.OrganizationXml;
import com.microstep.android.onclick.rest.model.ProductXml;
import com.microstep.android.onclick.util.Currency;
import com.microstep.android.onclick.util.Language;
import com.microstep.android.onclick.util.Unit;
import com.microstep.android.onclick.util.Utils;

public class XmlBeanFactory {
	
	public NoteXml getNoteXmlFromShop(String userId, Shop shop){
		NoteXml note = new NoteXml();
		note.setTitle(shop.getTitle());
		note.setType(shop.getType().toString());
		note.setDescription(shop.getSmsPerson());
		note.setIdentityId(Integer.valueOf(userId));
		if(shop.getCreated() > 0){			
			note.setDateCreated(Utils.timesToJsonString(shop.getCreated()));
		}
		if(shop.getModified() > 0){
			note.setDateFinished(Utils.timesToJsonString(shop.getModified()));
		}
		
		for(Product product : shop.getProducts()){
			note.getProductXml().add(getProductXmlFromProduct(userId, product));
		}
		
		return note;
	}	
	
	public ProductXml getProductXmlFromProduct(String userId, Product product){
		ProductXml p = new ProductXml();
		p.setCount(product.getCount());
		p.setIdentityId(Integer.valueOf(userId));
		p.setName(product.getName());
		p.setPrice(product.getPrice());
		p.setUnit(Utils.getUnit(product.getUnit()));
		p.setImageNormalPath(product.getImageNormalPath());
		p.setImageSmallPath(product.getImageSmallPath());
				
		if(product.getCategory() != null){
			String integer = String.valueOf(product.getCategory().getId());			
			p.setCategoryId(Integer.valueOf(integer));
		}else{
			p.setCategoryId(null);
		}
		if(product.getCreated() > 0){
			p.setDateCreated(Utils.timesToJsonString(product.getCreated()));	
		}
		if(product.getModified() > 0){
			p.setDateModified(Utils.timesToJsonString(product.getModified()));		
		}
		try{
			if(product.getOrganization() != null){
				p.setOrganizationId((int)product.getOrganization().getId().longValue());
				p.setOrganizationProductId((int)product.getOrganizationProductId().longValue());				
				p.setOrganizationLocationId((int)product.getOrganizationLocationId().longValue());
				if(product.getOrganizationCategory() != null){					
					p.setOrganizationCategoryId(product.getOrganizationCategory().getId().intValue());
				}
			}
		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
		}
		return p;
	}
	
	public Product getProductFromProductXml(ProductXml productXml){
		if(productXml != null){
			Product p = new Product();			
			p.setCount(productXml.getCount());
			p.setId(new Long(productXml.getId()));
			p.setName(productXml.getName());
			p.setPrice(productXml.getPrice());	
			p.setIdentifier(productXml.getIdentifier());
			if(productXml.getLanguage() != null){
				p.setLanguage(Language.value(productXml.getLanguage()));
			}
			p.setPriceDescription(productXml.getPriceDescription());
			p.setImageSmallPath(productXml.getImageSmallPath());
			p.setImageNormalPath(productXml.getImageNormalPath());
			p.setCurrency(Currency.value(productXml.getCurrency()));
			p.setUnit(Unit.value(productXml.getUnit()).getValue());
			return p;
		}
		return null;
	}
	
	public List<Product> getProductListFromProductXmlList(List<ProductXml> productXmls){
		List<Product> products = new ArrayList<Product>();
		if(productXmls != null){
			for(ProductXml productXml : productXmls){
				products.add(getProductFromProductXml(productXml));
			}
			return products;
		}
		return null;
	}
	
	public Category getCategoryFromCategoryXml(CategoryXml categoryXml){
		Category category = new Category();
		if(categoryXml != null){
			category.setId(new Long(categoryXml.getId()));
			category.setName(categoryXml.getName());
			category.setLanguage(categoryXml.getLanguage());
			category.setType(categoryXml.getType());
			category.setUrl(categoryXml.getUrl());
			if(categoryXml.getDateCreated() != null){
				category.setCreated(Utils.dateLong(Long.valueOf(categoryXml.getDateCreated())));
			}
			if(categoryXml.getDateModified() != null){
				category.setModified(Utils.dateLong(Long.valueOf(categoryXml.getDateModified())));
			}
			category.setHasChild(categoryXml.isHasChild());	
			if(categoryXml.getParentCategoryId() != null){
				Category parent = new Category();
				parent.setId(new Long(categoryXml.getParentCategoryId()));
				category.setParent(parent);
			}
			if(categoryXml.getOrganizationXml() != null && "CATEGORY".equals(categoryXml.getType())){
				category.setOrganization(getOrganizationFromOrganizationXml(categoryXml.getOrganizationXml()));				
				if(categoryXml.getImageSmallUrl() != null){
					category.setImageSmallUrl(categoryXml.getImageSmallUrl());
				}else{
					category.setImageSmallUrl(categoryXml.getOrganizationXml().getLogoSmallUrl());
				}
			}else{
				if(categoryXml.getImageSmallUrl() != null){
					category.setImageSmallUrl(categoryXml.getImageSmallUrl());
				}
				if(categoryXml.getImageNormalUrl() != null){
					category.setImageNormalUrl(categoryXml.getImageNormalUrl());
				}
			}
			if(categoryXml.getStart() != null){
				category.setStart(Utils.dateLong(Long.valueOf(categoryXml.getStart())));
			}
			if(categoryXml.getEnd() != null){
				category.setEnd(Utils.dateLong(Long.valueOf(categoryXml.getEnd())));
			}		
			return category;
		}
		return null;
	}
	
	public Organization getOrganizationFromOrganizationXml(OrganizationXml organizationXml){
		Organization organization = new Organization();
		if(organizationXml != null){
			organization.setId(new Long(organizationXml.getId()));
			organization.setName(organizationXml.getName());
			organization.setLogoImagePath(organizationXml.getLogoSmallUrl());
			if(organizationXml.getLocationXml() != null){
				organization.setLocations(getLocationListFromLocationXmlList(
						organizationXml.getLocationXml()));
			}
			if(organizationXml.getNewsletterSize() != null){
				organization.setNewsletterSize(organizationXml.getNewsletterSize().intValue());
			}
			if(organizationXml.getProductSize() != null){
				organization.setProductSize(organizationXml.getProductSize().intValue());
			}
			if(organizationXml.getPromotionSize() != null){
				organization.setPromotionSize(organizationXml.getPromotionSize().intValue());
			}			
			return organization;
		}
		return null;
	}
	
	public List<Organization> getOrganizationListFromOrganizationXmlList(List<OrganizationXml> organizationXmls){
		List<Organization> organizations = new ArrayList<Organization>();
		if(organizationXmls != null){
			for(OrganizationXml organizationXml : organizationXmls){
				organizations.add(getOrganizationFromOrganizationXml(organizationXml));
			}
			return organizations;
		}
		return null;
	}
	
	public Location getLocationFromLocationXml(LocationXml locationXml){
		Location location = new Location();
		if(locationXml != null){
			location.setId(new Long(locationXml.getId()));
			location.setAddress1(locationXml.getAddress1());
			location.setCity(locationXml.getCity());
			location.setDistance(locationXml.getDistance());
			location.setLatitude(locationXml.getLatitude());
			location.setLongitude(locationXml.getLongitude());
			location.setZipCode(locationXml.getZipCode());
			location.setCountryCode(locationXml.getCountryCode());
			return location;
		}
		return null;
	}
	
	public List<Location> getLocationListFromLocationXmlList(List<LocationXml> locationXmls){
		List<Location> locations = new ArrayList<Location>();
		if(locationXmls != null){
			for(LocationXml locationXml : locationXmls){
				locations.add(getLocationFromLocationXml(locationXml));
			}
			return locations;
		}
		return null;
	}
	
	public List<Category> getCategoryListFromCategoryXmlList(List<CategoryXml> categoryXmls){
		List<Category> categories = new ArrayList<Category>();
		if(categoryXmls != null){
			for(CategoryXml categoryXml : categoryXmls){
				categories.add(getCategoryFromCategoryXml(categoryXml));
			}
			return categories;
		}
		return null;
	}

}