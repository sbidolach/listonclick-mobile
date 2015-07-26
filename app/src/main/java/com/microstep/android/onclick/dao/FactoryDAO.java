package com.microstep.android.onclick.dao;

import android.content.Context;

public class FactoryDAO {

	private static ProductDAO 	productDAO;
	private static ShopDAO 		shopDAO;
	private static CategoryDAO 	categoryDAO;
	private static PropertyDAO 	propertyDAO;
	
	public static ProductDAO getProductDAO(Context context) {		
		if(productDAO == null){
			productDAO = new ProductDAOImpl(context);
		}else{
			productDAO.setContext(context);
		}
		return productDAO;
	}
	
	public static ShopDAO getShopDAO(Context context) {
		if(shopDAO == null){
			shopDAO = new ShopDAOImpl(context);
		}else{
			shopDAO.setContext(context);
		}
		return shopDAO;
	}
	
	public static CategoryDAO getCategoryDAO(Context context) {
		if(categoryDAO == null){
			categoryDAO = new CategoryDAOImpl(context);
		}else{
			categoryDAO.setContext(context);
		}
		return categoryDAO;
	}
	
	public static PropertyDAO getPropertyDAO(Context context) {
		if(propertyDAO == null){
			propertyDAO = new PropertyDAOImpl(context);
		}else{
			propertyDAO.setContext(context);
		}
		return propertyDAO;
	}
}
