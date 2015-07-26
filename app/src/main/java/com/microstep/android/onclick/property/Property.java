package com.microstep.android.onclick.property;

import android.content.Context;

import com.microstep.android.onclick.dao.FactoryDAO;

public class Property {

	public static String AUTH_TOKEN = "com.microstep.onlick.authtoken";
	public static String EMAIL = "com.microstep.onlick.email";

	public static String getAuthToken(Context context){
		return FactoryDAO.getPropertyDAO(context).get(AUTH_TOKEN);
	}
	
	public static void setAuthToken(Context context, String value){
		FactoryDAO.getPropertyDAO(context).update(AUTH_TOKEN, value);
	}
	
	public static String getAccountEmail(Context context){
		return FactoryDAO.getPropertyDAO(context).get(EMAIL);
	}
	
	public static void setAccountEmail(Context context, String value){
		FactoryDAO.getPropertyDAO(context).update(EMAIL, value);
	}
	
}
