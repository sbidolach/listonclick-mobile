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
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.model.ShopType;
import com.microstep.android.onclick.util.Utils;

public class ShopDAOImpl implements ShopDAO{

	private Context context;
	
	public ShopDAOImpl(Context context){
		this.context = context;
	}
	
	@Override
	public Long insert(Shop shop) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		ContentValues values = new ContentValues();
	    values.put(DatabaseShop.Shops.COLUMN_NAME_TITLE, shop.getTitle());	    
	    values.put(DatabaseShop.Shops.COLUMN_NAME_FINISHED, 0);
	    int timestamp = Utils.currentTime();
	    values.put(DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE, timestamp);
	    values.put(DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, timestamp);	    
	    values.put(DatabaseShop.Shops.COLUMN_NAME_TYPE, shop.getType().toString());
	    values.put(DatabaseShop.Shops.COLUMN_NAME_SMS_PERSON, shop.getSmsPerson());
	    values.put(DatabaseShop.Shops.COLUMN_NAME_SYNCH, 0);
		return sqlDb.insert(DatabaseShop.Shops.TABLE_NAME, null, values);
	}

	@Override
	public int delete(Shop shop) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		sqlDb.delete(DatabaseShop.Products.TABLE_NAME, "shop_id=?", 
				new String[]{shop.getId().toString()});		
		return sqlDb.delete(DatabaseShop.Shops.TABLE_NAME, "_id=?", 
				new String[]{shop.getId().toString()});
	}

	@Override
	public boolean update(Shop shop) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		ContentValues values = new ContentValues();
		values.put(DatabaseShop.Shops.COLUMN_NAME_TITLE, shop.getTitle());
		values.put(DatabaseShop.Shops.COLUMN_NAME_FINISHED, shop.isFinished());
		values.put(DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
		values.put(DatabaseShop.Shops.COLUMN_NAME_TYPE, shop.getType().toString());
		values.put(DatabaseShop.Shops.COLUMN_NAME_SMS_PERSON, shop.getSmsPerson());
		values.put(DatabaseShop.Shops.COLUMN_NAME_SYNCH, shop.isSynch());
		sqlDb.update(DatabaseShop.Shops.TABLE_NAME, values, "_id=?", 
				new String[]{shop.getId().toString()});
		return false;
	}

	@Override
	public List<Shop> feachAll() {		
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);		
		ArrayList<Shop> shops = new ArrayList<Shop>();
		Cursor cursor = null;
		try {						
			cursor = sqlDb.rawQuery(
					"SELECT "
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
			while(cursor.moveToNext()){
				Shop s = new Shop();
				s.setId(cursor.getLong(DatabaseShop.Shops.COLUMN_INDEX_ID));
				s.setTitle(cursor.getString(DatabaseShop.Shops.COLUMN_INDEX_TITLE));
				s.setFinished(cursor.getInt(DatabaseShop.Shops.COLUMN_INDEX_FINISHED)==1?true:false);
				s.setCreated(cursor.getInt(DatabaseShop.Shops.COLUMN_INDEX_CREATE_DATE) * 1000L);
				s.setModified(cursor.getInt(DatabaseShop.Shops.COLUMN_INDEX_MODIFICATION_DATE) * 1000L);
				s.setType(ShopType.valueOf(cursor.getString(DatabaseShop.Shops.COLUMN_INDEX_TYPE)));
				s.setSmsPerson(cursor.getString(DatabaseShop.Shops.COLUMN_INDEX_SMS_PERSON));
				shops.add(s);
			}					
			Log.i("ARRAY", "" + shops.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
		}
		return shops;
	}
	
	private List<Shop> feachAll(Boolean finished, String orderBy, Long shopId, Integer termtime) {		
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);		
		ArrayList<Shop> shops = new ArrayList<Shop>();
		Cursor cNote = null;
		try {									
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT ");
			buffer.append(DatabaseShop.Shops._ID + " ,");
			buffer.append(DatabaseShop.Shops.COLUMN_NAME_TITLE + " ,");
			buffer.append(DatabaseShop.Shops.COLUMN_NAME_FINISHED + " ,");
			buffer.append(DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE + " ,");
			buffer.append(DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE + " ,");
			buffer.append(DatabaseShop.Shops.COLUMN_NAME_TYPE + " ,");
			buffer.append(DatabaseShop.Shops.COLUMN_NAME_SMS_PERSON + ", ");
			buffer.append(DatabaseShop.Shops.COLUMN_NAME_SYNCH + " ");
			buffer.append("FROM ");
			buffer.append(DatabaseShop.Shops.TABLE_NAME + " ");
			buffer.append("WHERE ");
			if(finished != null){
				buffer.append(DatabaseShop.Shops.COLUMN_NAME_FINISHED);
				buffer.append(" = ");
				buffer.append(finished?"1":"0");
				buffer.append(" ");
			}
			if(shopId != null){
				if(finished != null){
					buffer.append("AND ");
				}
				buffer.append(DatabaseShop.Shops._ID);
				buffer.append(" = ");
				buffer.append(shopId);
				buffer.append(" ");
			}		
			if(termtime != null){
				if(finished != null || shopId != null){
					buffer.append("AND ");
				}
				buffer.append(DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE);
				buffer.append(" >= ");
				buffer.append(termtime);
				buffer.append(" ");
			}
			if(orderBy != null){
				buffer.append("ORDER BY ");
				buffer.append(orderBy + " DESC ");
			}
			cNote = sqlDb.rawQuery(buffer.toString(), null);			
			while(cNote.moveToNext()){
				Shop s = new Shop();
				s.setId(cNote.getLong(DatabaseShop.Shops.COLUMN_INDEX_ID));
				s.setTitle(cNote.getString(DatabaseShop.Shops.COLUMN_INDEX_TITLE));
				s.setFinished(cNote.getInt(DatabaseShop.Shops.COLUMN_INDEX_FINISHED)==1?true:false);
				s.setCreated(cNote.getInt(DatabaseShop.Shops.COLUMN_INDEX_CREATE_DATE) * 1000L);
				s.setModified(cNote.getInt(DatabaseShop.Shops.COLUMN_INDEX_MODIFICATION_DATE) * 1000L);
				s.setType(ShopType.valueOf(cNote.getString(DatabaseShop.Shops.COLUMN_INDEX_TYPE)));
				s.setSmsPerson(cNote.getString(DatabaseShop.Shops.COLUMN_INDEX_SMS_PERSON));
				s.setSynch(cNote.getInt(DatabaseShop.Shops.COLUMN_INDEX_SMS_SYNCH)==1?true:false);
				Cursor cProduct = null;
				try{
					buffer = new StringBuffer();
					buffer.append("SELECT ");
					buffer.append("_id, name, count, value, selected, unit, created, ");
					buffer.append("modified, category_id, organization_product_id, organization_id, ");
					buffer.append("organization_location_id, organization_category_id ");
					buffer.append("FROM ");
					buffer.append(DatabaseShop.Products.TABLE_NAME);
					buffer.append(" ");
					buffer.append("WHERE shop_id = ? ");					
					cProduct = sqlDb.rawQuery(buffer.toString(), new String[]{s.getId().toString()});
					while(cProduct.moveToNext()){
						Product p = new Product();
						p.setId(cProduct.getLong(DatabaseShop.Products.COLUMN_INDEX_ID));
						p.setName(cProduct.getString(DatabaseShop.Products.COLUMN_INDEX_NAME));
						p.setCount(cProduct.getDouble(DatabaseShop.Products.COLUMN_INDEX_COUNT));
						p.setPrice(cProduct.getDouble(DatabaseShop.Products.COLUMN_INDEX_VALUE));
						p.setSelected(cProduct.getInt(DatabaseShop.Products.COLUMN_INDEX_SELECTED)==1?true:false);
						p.setUnit(cProduct.getInt(5));
						p.setCreated(cProduct.getInt(6) * 1000L);
						p.setModified(cProduct.getInt(7) * 1000L);
						Integer categoryId = cProduct.getInt(8);
						if(categoryId != null){
							Category category = new Category();
							category.setId(new Long(categoryId));
							p.setCategory(category);
						}
						
						try{
							Long orgId = cProduct.getLong(10);
							if(orgId != null){
								Organization organization = new Organization();
								organization.setId(orgId);
								p.setOrganization(organization);
							}
							Long orgProductId = cProduct.getLong(9);
							if(orgProductId != null){
								p.setOrganizationProductId(orgProductId);
							}		
							Long orgLocationId = cProduct.getLong(11);
							if(orgLocationId != null){
								p.setOrganizationLocationId(orgLocationId);
							}							
							Long orgCategoryId = cProduct.getLong(12);
							if(orgCategoryId != null){
								Category category = new Category();
								category.setId(orgCategoryId);
								p.setOrganizationCategory(category);
							}							
						}catch(Exception e){
							Log.e("Problem object", e.getMessage());
						}
						
						s.getProducts().add(p);
					}
				}catch(Exception e){
					if(!cProduct.isClosed()){
						cProduct.close();
					}
				}
				shops.add(s);
			}
			Log.i("ARRAY", "" + shops.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}finally{
			if(!cNote.isClosed()){
				cNote.close();
			}
		}
		return shops;
	}
	
	@Override
	public List<Shop> feachAll(boolean finished, String orderBy) {		
		return feachAll(finished, orderBy, null, null);		
	}

	@Override
	public Shop getShop(Long shopId) {		
		List<Shop> shops = feachAll(null, null, shopId, null);		
		try{
			return shops.get(0);
		}catch(Exception e){			
		}
		
		return null;
	}

	@Override
	public void setContext(Context context) {
		this.context = context;		
	}

	@Override
	public List<Shop> feachSynch() {
		List<Shop> shops = feachAll(true, null);
		List<Shop> returns = new ArrayList<Shop>();
		for(Shop shop:shops){
			if(!shop.isSynch()){
				returns.add(shop);
			}
		}
		return returns;
	}

	@Override
	public List<Shop> getHistory(boolean finished, String orderBy,
			Integer termtime) {
		return feachAll(finished, orderBy, null, termtime);		
	}

}