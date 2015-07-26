package com.microstep.android.onclick.dao;

import java.util.ArrayList;

import com.microstep.android.onclick.db.DatabaseHelper;
import com.microstep.android.onclick.db.DatabaseShop;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.util.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PropertyDAOImpl implements PropertyDAO{

	private Context context;
	
	public PropertyDAOImpl(Context context){
		this.context = context;
	}

	@Override
	public void setContext(Context context) {
		this.context = context;		
	}

	@Override
	public boolean update(String property, String value) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);
		ContentValues values = new ContentValues();
		values.put(DatabaseShop.Property.COLUMN_NAME_VALUE, value);		
		values.put(DatabaseShop.Property.COLUMN_NAME_MODIFICATION_DATE, Utils.currentTime());
		sqlDb.update(DatabaseShop.Property.TABLE_NAME, values, 
				DatabaseShop.Property.COLUMN_NAME_PROPERTY + "=?", 
				new String[]{property});
		return false;
	}

	@Override
	public String get(String property) {
		SQLiteDatabase sqlDb = DatabaseHelper.getSQLInstance(context);		
		Cursor cursor = null;
		try {						
			cursor = sqlDb.rawQuery(
					"SELECT " + DatabaseShop.Property.COLUMN_NAME_VALUE + " "             
	                + "FROM " + DatabaseShop.Property.TABLE_NAME + " "
	                + "WHERE " + DatabaseShop.Property.COLUMN_NAME_PROPERTY + " = ? "
	                , new String[]{property});			
			while(cursor.moveToNext()){
				return cursor.getString(0);
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
