package com.microstep.android.onclick.db;

import java.util.ArrayList;
import java.util.Iterator;

import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.model.ShopType;
import com.microstep.android.onclick.property.Property;
import com.microstep.android.onclick.util.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "shop_list.db";
    private static final int DATABASE_VERSION = 17;
    private static SQLiteDatabase sqlDb = null;

    public static SQLiteDatabase getSQLInstance(Context context){
    	if(sqlDb == null){
    		DatabaseHelper db = new DatabaseHelper(context);
    		sqlDb =	db.getWritableDatabase();
    		
    	}
    	return sqlDb;
    }
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseShop.Shops.TABLE_NAME + " ("
                + DatabaseShop.Shops._ID + " INTEGER PRIMARY KEY,"
                + DatabaseShop.Shops.COLUMN_NAME_TITLE + " TEXT,"
                + DatabaseShop.Shops.COLUMN_NAME_FINISHED + " BIT,"
                + DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                + DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE + " INTEGER,"
                + DatabaseShop.Shops.COLUMN_NAME_TYPE + " TEXT,"
                + DatabaseShop.Shops.COLUMN_NAME_SMS_PERSON + " TEXT,"
                + DatabaseShop.Shops.COLUMN_NAME_SYNCH + " INTEGER,"
                + DatabaseShop.Shops.COLUMN_NAME_CATEGORY_ID + " INTEGER"
                + ");");

        db.execSQL("CREATE TABLE " + DatabaseShop.Products.TABLE_NAME + " ("
                + DatabaseShop.Products._ID + " INTEGER PRIMARY KEY,"
                + DatabaseShop.Products.COLUMN_NAME_NAME + " TEXT,"
                + DatabaseShop.Products.COLUMN_NAME_COUNT + " DOUBLE,"
                + DatabaseShop.Products.COLUMN_NAME_VALUE + " DOUBLE,"                
                + DatabaseShop.Products.COLUMN_NAME_SELECTED + " BIT,"
                + DatabaseShop.Products.COLUMN_NAME_SHOP_ID + " INTEGER,"
                + DatabaseShop.Products.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                + DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE + " INTEGER,"
                + DatabaseShop.Products.COLUMN_NAME_UNIT + " INTEGER,"
                + DatabaseShop.Products.COLUMN_NAME_CATEGORY_ID + " INTEGER,"
                + DatabaseShop.Products.COLUMN_NAME_ORG_ID + " INTEGER,"
                + DatabaseShop.Products.COLUMN_NAME_ORG_CAT_ID + " INTEGER,"
                + DatabaseShop.Products.COLUMN_NAME_ORG_PROD_ID + " INTEGER,"
                + DatabaseShop.Products.COLUMN_NAME_IMG_SMALL + " TEXT,"
                + DatabaseShop.Products.COLUMN_NAME_IMG_NORMAL + " TEXT,"
                + DatabaseShop.Products.COLUMN_NAME_DESCRIPTION + " TEXT,"
                + DatabaseShop.Products.COLUMN_NAME_ORG_LOCATION_ID + " INTEGER"
                + ");");
        
        db.execSQL("CREATE TABLE " + DatabaseShop.Category.TABLE_NAME + " ("
                + DatabaseShop.Category._ID + " INTEGER PRIMARY KEY,"
                + DatabaseShop.Category.COLUMN_NAME_NAME + " TEXT,"
                + DatabaseShop.Category.COLUMN_NAME_LANGUAGE + " TEXT,"
                + DatabaseShop.Category.COLUMN_NAME_CREATE_DATE + " INTEGER"
                + ");");
        
        db.execSQL("CREATE TABLE " + DatabaseShop.Property.TABLE_NAME + " ("
                + DatabaseShop.Property._ID + " INTEGER PRIMARY KEY,"
                + DatabaseShop.Property.COLUMN_NAME_PROPERTY + " TEXT,"
                + DatabaseShop.Property.COLUMN_NAME_VALUE + " TEXT,"
                + DatabaseShop.Property.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                + DatabaseShop.Property.COLUMN_NAME_MODIFICATION_DATE + " INTEGER,"
                + "UNIQUE ("+DatabaseShop.Property.COLUMN_NAME_PROPERTY+"));");
        
        ContentValues values = new ContentValues();
        values.put(DatabaseShop.Shops.COLUMN_NAME_TITLE, "Tesco");
        values.put(DatabaseShop.Shops.COLUMN_NAME_FINISHED, "0");
        values.put(DatabaseShop.Shops.COLUMN_NAME_TYPE, ShopType.CREATE.toString());
        values.put(DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE, Utils.currentTime());        
        values.put(DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
        Long shop_id = db.insert(DatabaseShop.Shops.TABLE_NAME, null, values);
        values.put(DatabaseShop.Shops.COLUMN_NAME_TITLE, "Breakfast");
        values.put(DatabaseShop.Shops.COLUMN_NAME_FINISHED, "0");
        values.put(DatabaseShop.Shops.COLUMN_NAME_TYPE, ShopType.CREATE.toString());
        values.put(DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE, Utils.currentTime());
        values.put(DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
        db.insert(DatabaseShop.Shops.TABLE_NAME, null, values);
        values.put(DatabaseShop.Shops.COLUMN_NAME_TITLE, "Grill Saturday 05.12");
        values.put(DatabaseShop.Shops.COLUMN_NAME_FINISHED, "0");
        values.put(DatabaseShop.Shops.COLUMN_NAME_TYPE, ShopType.CREATE.toString());
        values.put(DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE, Utils.currentTime());
        values.put(DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
        db.insert(DatabaseShop.Shops.TABLE_NAME, null, values);
        
        values = new ContentValues();
        values.put(DatabaseShop.Products.COLUMN_NAME_NAME, "Potatoes 5kg");
        values.put(DatabaseShop.Products.COLUMN_NAME_COUNT, "2");
        values.put(DatabaseShop.Products.COLUMN_NAME_VALUE, "2.5");
        values.put(DatabaseShop.Products.COLUMN_NAME_SELECTED, "0");        
        values.put(DatabaseShop.Products.COLUMN_NAME_SHOP_ID, shop_id.toString());
        values.put(DatabaseShop.Products.COLUMN_NAME_CREATE_DATE, Utils.currentTime());
        values.put(DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
        values.put(DatabaseShop.Products.COLUMN_NAME_UNIT, "0");
        db.insert(DatabaseShop.Products.TABLE_NAME, null, values);        
        values.put(DatabaseShop.Products.COLUMN_NAME_NAME, "Bread");
        values.put(DatabaseShop.Products.COLUMN_NAME_COUNT, "1");
        values.put(DatabaseShop.Products.COLUMN_NAME_VALUE, "3.25");
        values.put(DatabaseShop.Products.COLUMN_NAME_SELECTED, "0");        
        values.put(DatabaseShop.Products.COLUMN_NAME_SHOP_ID, shop_id.toString());
        values.put(DatabaseShop.Products.COLUMN_NAME_CREATE_DATE, Utils.currentTime());
        values.put(DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
        values.put(DatabaseShop.Products.COLUMN_NAME_UNIT, "0");
        db.insert(DatabaseShop.Products.TABLE_NAME, null, values);                       
        values.put(DatabaseShop.Products.COLUMN_NAME_NAME, "Butter");
        values.put(DatabaseShop.Products.COLUMN_NAME_COUNT, "1");
        values.put(DatabaseShop.Products.COLUMN_NAME_VALUE, "1.29");
        values.put(DatabaseShop.Products.COLUMN_NAME_SELECTED, "0");
        values.put(DatabaseShop.Products.COLUMN_NAME_SHOP_ID, shop_id.toString());
        values.put(DatabaseShop.Products.COLUMN_NAME_CREATE_DATE, Utils.currentTime());
        values.put(DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
        values.put(DatabaseShop.Products.COLUMN_NAME_UNIT, "0");
        db.insert(DatabaseShop.Products.TABLE_NAME, null, values);
        values.put(DatabaseShop.Products.COLUMN_NAME_NAME, "CocaCola 2L");
        values.put(DatabaseShop.Products.COLUMN_NAME_COUNT, "2");
        values.put(DatabaseShop.Products.COLUMN_NAME_VALUE, "4.78");
        values.put(DatabaseShop.Products.COLUMN_NAME_SELECTED, "0");       
        values.put(DatabaseShop.Products.COLUMN_NAME_SHOP_ID, shop_id.toString());
        values.put(DatabaseShop.Products.COLUMN_NAME_CREATE_DATE, Utils.currentTime());
        values.put(DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
        values.put(DatabaseShop.Products.COLUMN_NAME_UNIT, "0");
        db.insert(DatabaseShop.Products.TABLE_NAME, null, values);
        
        // Properties
        values = new ContentValues();
        values.put(DatabaseShop.Property.COLUMN_NAME_PROPERTY, Property.AUTH_TOKEN);        
        values.put(DatabaseShop.Property.COLUMN_NAME_CREATE_DATE, Utils.currentTime());        
        values.put(DatabaseShop.Property.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
        db.insert(DatabaseShop.Property.TABLE_NAME, null, values);
        
        values = new ContentValues();
        values.put(DatabaseShop.Property.COLUMN_NAME_PROPERTY, Property.EMAIL);
        values.put(DatabaseShop.Property.COLUMN_NAME_CREATE_DATE, Utils.currentTime());        
        values.put(DatabaseShop.Property.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
        db.insert(DatabaseShop.Property.TABLE_NAME, null, values);
        
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.toString(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");                
        
        if(oldVersion == 15 && newVersion == 17){
        
	        ArrayList<Shop> shops = new ArrayList<Shop>();
			Cursor cShop = null;
			try {						
				cShop = db.rawQuery("SELECT "
						+ DatabaseShop.Shops._ID + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_TITLE + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_FINISHED + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE + " , "
						+ DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_TYPE + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_SMS_PERSON + " "
						+ "FROM "
						+ DatabaseShop.Shops.TABLE_NAME, 
						null);
				
				while(cShop.moveToNext()){
					Shop s = new Shop();
					s.setId(cShop.getLong(DatabaseShop.Shops.COLUMN_INDEX_ID));
					s.setTitle(cShop.getString(DatabaseShop.Shops.COLUMN_INDEX_TITLE));
					s.setFinished(cShop.getInt(DatabaseShop.Shops.COLUMN_INDEX_FINISHED)==1?true:false);
					s.setCreated(cShop.getInt(DatabaseShop.Shops.COLUMN_INDEX_CREATE_DATE));
					s.setModified(cShop.getInt(DatabaseShop.Shops.COLUMN_INDEX_MODIFICATION_DATE));
					s.setType(ShopType.valueOf(cShop.getString(DatabaseShop.Shops.COLUMN_INDEX_TYPE)));
					s.setSmsPerson(cShop.getString(DatabaseShop.Shops.COLUMN_INDEX_SMS_PERSON));
					
					ArrayList<Product> products = new ArrayList<Product>();
					Cursor cProduct = null;
					try {						
						cProduct = db.rawQuery(
								"SELECT "
				                + DatabaseShop.Products._ID + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_NAME + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_COUNT + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_VALUE + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_SELECTED + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_SHOP_ID + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_CREATE_DATE + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE + " "
				                + "FROM " 
				                + DatabaseShop.Products.TABLE_NAME + " "
				                + "WHERE "
				                + DatabaseShop.Products.COLUMN_NAME_SHOP_ID + " = ? "
				                + "ORDER BY "
				                + DatabaseShop.Products.COLUMN_NAME_SELECTED + ", "
				                + DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE + " "
				                , new String[]{s.getId().toString()});			
						while(cProduct.moveToNext()){
							Product p = new Product();
							p.setId(cProduct.getLong(DatabaseShop.Products.COLUMN_INDEX_ID));
							p.setName(cProduct.getString(DatabaseShop.Products.COLUMN_INDEX_NAME));
							p.setCount(cProduct.getDouble(DatabaseShop.Products.COLUMN_INDEX_COUNT));
							p.setPrice(cProduct.getDouble(DatabaseShop.Products.COLUMN_INDEX_VALUE));
							p.setSelected(cProduct.getInt(DatabaseShop.Products.COLUMN_INDEX_SELECTED)==1?true:false);
							p.setCreated(cProduct.getInt(DatabaseShop.Products.COLUMN_INDEX_CREATE_DATE));
							p.setModified(cProduct.getInt(DatabaseShop.Products.COLUMN_INDEX_MODIFICATION_DATE));
							p.setUnit(1);
							products.add(p);
						}					
						Log.i("ARRAY", "" + products.size());
					} catch (Exception e) {
						Log.e("BACKGROUND_PROC", e.getMessage());
					}finally{
						if(!cProduct.isClosed()){
							cProduct.close();
						}
					}
					
					s.setProducts(products);
					shops.add(s);
				}
				Log.i("ARRAY", "" + shops.size());
			} catch (Exception e) {
				Log.e("BACKGROUND_PROC", e.getMessage());
			}finally{
				if(!cShop.isClosed()){
					cShop.close();
				}
			}
	        
	        db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Products.TABLE_NAME);
	        db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Shops.TABLE_NAME);
	        
	        onCreate(db);
	        
	        if(shops.size() > 0){
	        	db.execSQL("DELETE FROM " + DatabaseShop.Products.TABLE_NAME);
	            db.execSQL("DELETE FROM " + DatabaseShop.Shops.TABLE_NAME);            
	            Iterator<Shop> iterator = shops.iterator();
	            while(iterator.hasNext()){
	            	Shop shop = iterator.next();
	            	ContentValues values = new ContentValues();
					values.put(DatabaseShop.Shops.COLUMN_NAME_TITLE, shop.getTitle());
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_FINISHED, shop.isFinished());
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE, shop.getCreated());
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, shop.getModified());	    
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_TYPE, shop.getType().toString());
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_SMS_PERSON, shop.getSmsPerson());
	        	    values.put(DatabaseShop.Shops._ID, shop.getId());
	        	    db.insert(DatabaseShop.Shops.TABLE_NAME, null, values);
	        	    Iterator<Product> pIter = shop.getProducts().iterator();
	        	    while(pIter.hasNext()){
	        	    	Product product = pIter.next();
	        	    	values = new ContentValues();
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_NAME, product.getName());	    
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_COUNT, product.getCount());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_VALUE, product.getPrice());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_SHOP_ID, shop.getId());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_SELECTED, product.isSelected());        		    
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_CREATE_DATE, product.getCreated());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE, product.getModified());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_UNIT, product.getUnit());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_CATEGORY_ID, 0);
	        		    values.put(DatabaseShop.Products._ID, product.getId());
	        			db.insert(DatabaseShop.Products.TABLE_NAME, null, values);
	        	    }        	    
	            }
	        }
        	
        }else if(oldVersion == 16 && newVersion == 17){
        
        	ArrayList<Shop> shops = new ArrayList<Shop>();
        	ArrayList<Category> categories = new ArrayList<Category>();
			Cursor cShop = null;
			try {						
				cShop = db.rawQuery("SELECT "
						+ DatabaseShop.Shops._ID + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_TITLE + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_FINISHED + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE + " , "
						+ DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_TYPE + " ,"
						+ DatabaseShop.Shops.COLUMN_NAME_SMS_PERSON + " "
						+ "FROM "
						+ DatabaseShop.Shops.TABLE_NAME, 
						null);
				
				while(cShop.moveToNext()){
					Shop s = new Shop();
					s.setId(cShop.getLong(DatabaseShop.Shops.COLUMN_INDEX_ID));
					s.setTitle(cShop.getString(DatabaseShop.Shops.COLUMN_INDEX_TITLE));
					s.setFinished(cShop.getInt(DatabaseShop.Shops.COLUMN_INDEX_FINISHED)==1?true:false);
					s.setCreated(cShop.getInt(DatabaseShop.Shops.COLUMN_INDEX_CREATE_DATE));
					s.setModified(cShop.getInt(DatabaseShop.Shops.COLUMN_INDEX_MODIFICATION_DATE));
					s.setType(ShopType.valueOf(cShop.getString(DatabaseShop.Shops.COLUMN_INDEX_TYPE)));
					s.setSmsPerson(cShop.getString(DatabaseShop.Shops.COLUMN_INDEX_SMS_PERSON));
					ArrayList<Product> products = new ArrayList<Product>();
					Cursor cProduct = null;
					try {						
						cProduct = db.rawQuery(
								"SELECT "
				                + DatabaseShop.Products._ID + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_NAME + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_COUNT + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_VALUE + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_SELECTED + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_SHOP_ID + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_CREATE_DATE + " ,"
				                + DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE + ", "
				                + DatabaseShop.Products.COLUMN_NAME_UNIT + ", "
				                + DatabaseShop.Products.COLUMN_NAME_CATEGORY_ID + " "
				                + "FROM " 
				                + DatabaseShop.Products.TABLE_NAME + " "
				                + "WHERE "
				                + DatabaseShop.Products.COLUMN_NAME_SHOP_ID + " = ? "
				                + "ORDER BY "
				                + DatabaseShop.Products.COLUMN_NAME_SELECTED + ", "
				                + DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE + " "
				                , new String[]{s.getId().toString()});			
						while(cProduct.moveToNext()){
							Product p = new Product();
							p.setId(cProduct.getLong(DatabaseShop.Products.COLUMN_INDEX_ID));
							p.setName(cProduct.getString(DatabaseShop.Products.COLUMN_INDEX_NAME));
							p.setCount(cProduct.getDouble(DatabaseShop.Products.COLUMN_INDEX_COUNT));
							p.setPrice(cProduct.getDouble(DatabaseShop.Products.COLUMN_INDEX_VALUE));
							p.setSelected(cProduct.getInt(DatabaseShop.Products.COLUMN_INDEX_SELECTED)==1?true:false);
							p.setCreated(cProduct.getInt(DatabaseShop.Products.COLUMN_INDEX_CREATE_DATE));
							p.setModified(cProduct.getInt(DatabaseShop.Products.COLUMN_INDEX_MODIFICATION_DATE));
							p.setUnit(cProduct.getInt(DatabaseShop.Products.COLUMN_INDEX_UNIT));							
							Long categoryId = cProduct.getLong(DatabaseShop.Products.COLUMN_INDEX_CATEGORY_ID);
							if(categoryId != null){
								Category category = new Category();
								category.setId(categoryId);
								p.setCategory(category);
							}							
							products.add(p);
						}					
						Log.i("ARRAY", "" + products.size());
					} catch (Exception e) {
						Log.e("BACKGROUND_PROC", e.getMessage());
					}finally{
						if(!cProduct.isClosed()){
							cProduct.close();
						}
					}
					
					s.setProducts(products);
					shops.add(s);
				}
				Log.i("ARRAY", "" + shops.size());
			} catch (Exception e) {
				Log.e("BACKGROUND_PROC", e.getMessage());
			}finally{
				if(!cShop.isClosed()){
					cShop.close();
				}
			}			
			
	        db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Products.TABLE_NAME);
	        db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Shops.TABLE_NAME);
	        db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Category.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Property.TABLE_NAME);
	        
	        onCreate(db);
	        
	        if(shops.size() > 0){
	        	db.execSQL("DELETE FROM " + DatabaseShop.Products.TABLE_NAME);
	            db.execSQL("DELETE FROM " + DatabaseShop.Shops.TABLE_NAME);            
	            Iterator<Shop> iterator = shops.iterator();
	            while(iterator.hasNext()){
	            	Shop shop = iterator.next();
	            	ContentValues values = new ContentValues();
					values.put(DatabaseShop.Shops.COLUMN_NAME_TITLE, shop.getTitle());
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_FINISHED, shop.isFinished());
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE, shop.getCreated());
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, shop.getModified());	    
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_TYPE, shop.getType().toString());
	        	    values.put(DatabaseShop.Shops.COLUMN_NAME_SMS_PERSON, shop.getSmsPerson());
	        	    values.put(DatabaseShop.Shops._ID, shop.getId());
	        	    db.insert(DatabaseShop.Shops.TABLE_NAME, null, values);
	        	    Iterator<Product> pIter = shop.getProducts().iterator();
	        	    while(pIter.hasNext()){
	        	    	Product product = pIter.next();
	        	    	values = new ContentValues();
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_NAME, product.getName());	    
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_COUNT, product.getCount());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_VALUE, product.getPrice());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_SHOP_ID, shop.getId());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_SELECTED, product.isSelected());        		    
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_CREATE_DATE, product.getCreated());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_MODIFICATION_DATE, product.getModified());
	        		    values.put(DatabaseShop.Products.COLUMN_NAME_UNIT, product.getUnit());
	        		    if(product.getCategory() != null){
	        		    	values.put(DatabaseShop.Products.COLUMN_NAME_CATEGORY_ID, product.getCategory().getId());
	        		    }
	        		    values.put(DatabaseShop.Products._ID, product.getId());
	        			db.insert(DatabaseShop.Products.TABLE_NAME, null, values);
	        	    }        	    
	            }
	        }
        	
        }else{        
        	db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Products.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Shops.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Category.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseShop.Property.TABLE_NAME);
            onCreate(db);                                    
        }

    }

}