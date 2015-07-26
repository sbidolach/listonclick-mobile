package com.microstep.android.onclick.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.microstep.android.onclick.db.DatabaseHelper;
import com.microstep.android.onclick.db.DatabaseShop;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.util.Language;
import com.microstep.android.onclick.util.Utils;

public class CategoryDAOImpl implements CategoryDAO{

	private Context context;
	
	public CategoryDAOImpl(Context context){
		this.context = context;
	}
	
	@Override
	public Long insert(Category category) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		try {
			ContentValues values = new ContentValues();
			values.put(DatabaseShop.Category._ID, category.getId());
			values.put(DatabaseShop.Category.COLUMN_NAME_NAME, category.getName());	    
		    values.put(DatabaseShop.Category.COLUMN_NAME_LANGUAGE, category.getLanguage());
		    values.put(DatabaseShop.Category.COLUMN_NAME_CREATE_DATE, Utils.currentTime());
			return sqlDb.insert(DatabaseShop.Category.TABLE_NAME, null, values);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int delete(Category category) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		return sqlDb.delete(DatabaseShop.Category.TABLE_NAME, "_id=?", 
				new String[]{category.getId().toString()});
	}

	@Override
	public List<Category> getCategories(Language language) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);		
		ArrayList<Category> categories = new ArrayList<Category>();
		Cursor cursor = null;
		try {						
			cursor = sqlDb.rawQuery(
					"SELECT "
	                + DatabaseShop.Category._ID + " ,"
	                + DatabaseShop.Category.COLUMN_NAME_NAME + " ,"
	                + DatabaseShop.Category.COLUMN_NAME_LANGUAGE + " ,"
	                + DatabaseShop.Category.COLUMN_NAME_CREATE_DATE + " "              
	                + "FROM " 
	                + DatabaseShop.Category.TABLE_NAME + " "
	                + "WHERE "
	                + DatabaseShop.Category.COLUMN_NAME_LANGUAGE + " = ? "
	                + "ORDER BY "
	                + DatabaseShop.Category.COLUMN_NAME_NAME + " "
	                , new String[]{language.toString()});			
			while(cursor.moveToNext()){
				Category cat = new Category();
				cat.setId(cursor.getLong(DatabaseShop.Category.COLUMN_INDEX_ID));
				cat.setName(cursor.getString(DatabaseShop.Category.COLUMN_INDEX_NAME));
				cat.setLanguage(cursor.getString(DatabaseShop.Category.COLUMN_INDEX_LANGUAGE));
				cat.setCreated(cursor.getInt(DatabaseShop.Category.COLUMN_INDEX_CREATE_DATE) * 1000L);
				categories.add(cat);
			}					
			Log.i("ARRAY", "" + categories.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
		}
		return categories;
	}

	@Override
	public void setContext(Context context) {
		this.context = context;		
	}

	@Override
	public int deleteAll() {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		return sqlDb.delete(DatabaseShop.Category.TABLE_NAME, null, null);
	}

	@Override
	public Date getLastDateUpdate(Language language) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);		
		Cursor cursor = null;
		try {						
			cursor = sqlDb.rawQuery(
					"SELECT " + DatabaseShop.Category.COLUMN_NAME_CREATE_DATE + " "	                
	                + "FROM " + DatabaseShop.Category.TABLE_NAME + " "
	                + "WHERE "+ DatabaseShop.Category.COLUMN_NAME_LANGUAGE + " = ? "
	                + "ORDER BY " + DatabaseShop.Category.COLUMN_NAME_CREATE_DATE, 
	                new String[]{language.toString()});			
			while(cursor.moveToNext()){
				long milliseconds = cursor.getInt(0) * 1000L;
				return new Date(milliseconds);
			}
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
		}
		return null;
	}

	@Override
	public Category getById(Long categoryId) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);		
		Cursor cursor = null;
		try {						
			cursor = sqlDb.rawQuery(
					"SELECT "
	                + DatabaseShop.Category._ID + " ,"
	                + DatabaseShop.Category.COLUMN_NAME_NAME + " ,"
	                + DatabaseShop.Category.COLUMN_NAME_LANGUAGE + " ,"
	                + DatabaseShop.Category.COLUMN_NAME_CREATE_DATE + " "              
	                + "FROM " 
	                + DatabaseShop.Category.TABLE_NAME + " "
	                + "WHERE "
	                + DatabaseShop.Category._ID + " = ? "	          
	                , new String[]{String.valueOf(categoryId)});			
			while(cursor.moveToNext()){
				Category category = new Category();
				category.setId(cursor.getLong(DatabaseShop.Category.COLUMN_INDEX_ID));
				category.setName(cursor.getString(DatabaseShop.Category.COLUMN_INDEX_NAME));
				category.setLanguage(cursor.getString(DatabaseShop.Category.COLUMN_INDEX_LANGUAGE));
				category.setCreated(cursor.getInt(DatabaseShop.Category.COLUMN_INDEX_CREATE_DATE) * 1000L);
				return category;
			}
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
		}
		return null;
	}

	@Override
	public Category getByName(String name, Language language) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);		
		Cursor cursor = null;
		try {						
			cursor = sqlDb.rawQuery(
					"SELECT "
	                + DatabaseShop.Category._ID + " ,"
	                + DatabaseShop.Category.COLUMN_NAME_NAME + " ,"
	                + DatabaseShop.Category.COLUMN_NAME_LANGUAGE + " ,"
	                + DatabaseShop.Category.COLUMN_NAME_CREATE_DATE + " "              
	                + "FROM " 
	                + DatabaseShop.Category.TABLE_NAME + " "
	                + "WHERE "
	                + DatabaseShop.Category.COLUMN_NAME_NAME + " = ? AND "
	                + DatabaseShop.Category.COLUMN_NAME_LANGUAGE + " = ? "
	                , new String[]{name, language.toString()});			
			while(cursor.moveToNext()){
				Category category = new Category();
				category.setId(cursor.getLong(DatabaseShop.Category.COLUMN_INDEX_ID));
				category.setName(cursor.getString(DatabaseShop.Category.COLUMN_INDEX_NAME));
				category.setLanguage(cursor.getString(DatabaseShop.Category.COLUMN_INDEX_LANGUAGE));
				category.setCreated(cursor.getInt(DatabaseShop.Category.COLUMN_INDEX_CREATE_DATE) * 1000L);
				return category;
			}
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}finally{
			if(!cursor.isClosed()){
				cursor.close();
			}
		}
		return null;
	}

}
