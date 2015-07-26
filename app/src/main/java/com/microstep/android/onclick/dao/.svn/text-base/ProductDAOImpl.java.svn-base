package com.microstep.android.onclick.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.microstep.android.onclick.db.DatabaseHelper;
import com.microstep.android.onclick.db.DatabaseShop;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Location;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.util.Utils;

public class ProductDAOImpl implements ProductDAO{

	private Context context;
	
	public ProductDAOImpl(Context context){
		this.context = context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	@Override
	public Long insert(Product product) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		ContentValues values = new ContentValues();
	    values.put(DatabaseShop.Products.COLUMN_NAME_NAME, product.getName());	    
	    values.put(DatabaseShop.Products.COLUMN_NAME_COUNT, Utils.getUnitDouble(product));	    
	    values.put(DatabaseShop.Products.COLUMN_NAME_VALUE, product.getPrice());
	    values.put(DatabaseShop.Products.COLUMN_NAME_SHOP_ID, product.getShop().getId());
	    values.put(DatabaseShop.Products.COLUMN_NAME_SELECTED, 0);
	    int timestamp = Utils.currentTime();
	    values.put(DatabaseShop.Products.COLUMN_NAME_CREATE_DATE, timestamp);
	    values.put(DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE, timestamp);
	    values.put(DatabaseShop.Products.COLUMN_NAME_UNIT, product.getUnit());	    
	    if(product.getCategory() != null){
	    	values.put(DatabaseShop.Products.COLUMN_NAME_CATEGORY_ID, product.getCategory().getId());
	    }	    
	    if(product.getOrganization() != null){
	    	System.out.println("Is organization: " + product.getOrganization());
		    System.out.println("Image: " + product.getImageNormalPath());
		    System.out.println("ProductId: " + product.getId());
		    System.out.println("LocationId: " + product.getOrganization().getLocations().get(0).getId());
	    	values.put(DatabaseShop.Products.COLUMN_NAME_ORG_ID, product.getOrganization().getId());
	    	Category category = product.getOrganizationCategory();
		    if(category != null){
		    	values.put(DatabaseShop.Products.COLUMN_NAME_ORG_CAT_ID, category.getId());
		    }
		    values.put(DatabaseShop.Products.COLUMN_NAME_ORG_PROD_ID, product.getId());
		    values.put(DatabaseShop.Products.COLUMN_NAME_IMG_SMALL, product.getImageSmallPath());
		    values.put(DatabaseShop.Products.COLUMN_NAME_IMG_NORMAL, product.getImageNormalPath());
		    values.put(DatabaseShop.Products.COLUMN_NAME_DESCRIPTION, product.getDescription());
		    List<Location> location = product.getOrganization().getLocations();
		    if(location != null && location.size() > 0){
		    	values.put(DatabaseShop.Products.COLUMN_NAME_ORG_LOCATION_ID, location.get(0).getId());
		    }
	    }
		return sqlDb.insert(DatabaseShop.Products.TABLE_NAME, null, values);
	}

	@Override
	public int delete(Product product) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		return sqlDb.delete(DatabaseShop.Products.TABLE_NAME, "_id=?", 
				new String[]{product.getId().toString()});
	}

	@Override
	public boolean update(Product product) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		ContentValues values = new ContentValues();
		values.put(DatabaseShop.Products.COLUMN_NAME_NAME, product.getName());		
		values.put(DatabaseShop.Products.COLUMN_NAME_COUNT, Utils.getUnitDouble(product));
		values.put(DatabaseShop.Products.COLUMN_NAME_VALUE, product.getPrice());
		values.put(DatabaseShop.Products.COLUMN_NAME_SELECTED, product.isSelected());
		values.put(DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
		values.put(DatabaseShop.Products.COLUMN_NAME_UNIT, product.getUnit());
		if(product.getCategory() != null){
	    	values.put(DatabaseShop.Products.COLUMN_NAME_CATEGORY_ID, product.getCategory().getId());
	    }else{
	    	values.put(DatabaseShop.Products.COLUMN_NAME_CATEGORY_ID, 0);
	    }
		if(product.getOrganization() != null){
		    values.put(DatabaseShop.Products.COLUMN_NAME_ORG_ID, product.getOrganization().getId());
		    Category category = product.getOrganizationCategory();
		    if(category != null){
		    	values.put(DatabaseShop.Products.COLUMN_NAME_ORG_CAT_ID, category.getId());
		    }		    
		    values.put(DatabaseShop.Products.COLUMN_NAME_ORG_LOCATION_ID, product.getOrganizationLocationId());		    
		    values.put(DatabaseShop.Products.COLUMN_NAME_ORG_PROD_ID, product.getOrganizationProductId());
		    values.put(DatabaseShop.Products.COLUMN_NAME_IMG_SMALL, product.getImageSmallPath());
		    values.put(DatabaseShop.Products.COLUMN_NAME_IMG_NORMAL, product.getImageNormalPath());
		    values.put(DatabaseShop.Products.COLUMN_NAME_DESCRIPTION, product.getDescription());
	    }
		sqlDb.update(DatabaseShop.Products.TABLE_NAME, values, "_id=?", 
				new String[]{product.getId().toString()});
		return false;
	}

	@Override
	public List<Product> getProducts(Long id) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);		
		ArrayList<Product> products = new ArrayList<Product>();
		Cursor cursor = null;
		try {						
			cursor = sqlDb.rawQuery(
					"SELECT "
	                + DatabaseShop.Products._ID + " ,"
	                + DatabaseShop.Products.COLUMN_NAME_NAME + " ,"
	                + DatabaseShop.Products.COLUMN_NAME_COUNT + " ,"
	                + DatabaseShop.Products.COLUMN_NAME_VALUE + " ,"
	                + DatabaseShop.Products.COLUMN_NAME_SELECTED + " ,"
	                + DatabaseShop.Products.COLUMN_NAME_CREATE_DATE + " ,"
	                + DatabaseShop.Products.COLUMN_NAME_SHOP_ID + " ,"
	                + DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE + ", "
	                + DatabaseShop.Products.COLUMN_NAME_UNIT + ", "
	                + DatabaseShop.Products.COLUMN_NAME_CATEGORY_ID + ", "
	                + DatabaseShop.Products.COLUMN_NAME_ORG_ID + ", "
	                + DatabaseShop.Products.COLUMN_NAME_ORG_CAT_ID + ", "
	                + DatabaseShop.Products.COLUMN_NAME_ORG_PROD_ID + ", "
	                + DatabaseShop.Products.COLUMN_NAME_IMG_SMALL + ", "
	                + DatabaseShop.Products.COLUMN_NAME_IMG_NORMAL + ", "
	                + DatabaseShop.Products.COLUMN_NAME_DESCRIPTION + ", "
	                + DatabaseShop.Products.COLUMN_NAME_ORG_LOCATION_ID + " "
	                + "FROM " 
	                + DatabaseShop.Products.TABLE_NAME + " "
	                + "WHERE "
	                + DatabaseShop.Products.COLUMN_NAME_SHOP_ID + " = ? "
	                + "ORDER BY "
	                + DatabaseShop.Products.COLUMN_NAME_SELECTED + ", "
	                + DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE + " "
	                , new String[]{id.toString()});			
			while(cursor.moveToNext()){
				Product p = new Product();
				p.setId(cursor.getLong(DatabaseShop.Products.COLUMN_INDEX_ID));
				p.setName(cursor.getString(DatabaseShop.Products.COLUMN_INDEX_NAME));
				p.setCount(cursor.getDouble(DatabaseShop.Products.COLUMN_INDEX_COUNT));
				p.setPrice(cursor.getDouble(DatabaseShop.Products.COLUMN_INDEX_VALUE));
				p.setSelected(cursor.getInt(DatabaseShop.Products.COLUMN_INDEX_SELECTED)==1?true:false);
				p.setCreated(cursor.getInt(DatabaseShop.Products.COLUMN_INDEX_CREATE_DATE) * 1000L);
				p.setModified(cursor.getInt(DatabaseShop.Products.COLUMN_INDEX_MODIFICATION_DATE) * 1000L);
				p.setUnit(cursor.getInt(DatabaseShop.Products.COLUMN_INDEX_UNIT));
				Integer categoryId = cursor.getInt(DatabaseShop.Products.COLUMN_INDEX_CATEGORY_ID);
				if(categoryId != null){
					Category category = new Category();
					category.setId(new Long(categoryId));
					p.setCategory(category);
				}
				Long oid = cursor.getLong(DatabaseShop.Products.COLUMN_INDEX_ORG_ID);
				if(oid != null){
					Organization organization = new Organization();
					organization.setId(oid);
					p.setOrganization(organization);
				}
				Long cid = cursor.getLong(DatabaseShop.Products.COLUMN_INDEX_ORG_CAT_ID);
				if(cid != null){
					Category category = new Category();
					category.setId(cid);
					p.setOrganizationCategory(category);
				}
				p.setOrganizationProductId(cursor.getLong(DatabaseShop.Products.COLUMN_INDEX_ORG_PROD_ID));
				p.setImageSmallPath(cursor.getString(DatabaseShop.Products.COLUMN_INDEX_IMG_SMALL));
				p.setImageNormalPath(cursor.getString(DatabaseShop.Products.COLUMN_INDEX_IMG_NORMAL));
				p.setDescription(cursor.getString(DatabaseShop.Products.COLUMN_INDEX_DESCRIPTION));       
				p.setOrganizationLocationId(cursor.getLong(DatabaseShop.Products.COLUMN_INDEX_ORG_LOCATION_ID));
				products.add(p);
			}					
			Log.i("ARRAY", "" + products.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
		}
		return products;
	}

	@Override
	public Product getProductById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
