package com.microstep.android.onclick.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Product;
import com.microstep.onclick.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	
	public static void refreshListView(Runnable runnable){
		new Thread(null, runnable, "MagentoBackground").start();        
	}
	
	public static int currentTime(){
		return (int)(System.currentTimeMillis() / 1000);
	}
	
	public static String timesToString(long timestamp){
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date d = new Date(timestamp);
			return f.format(d);
		}catch(Exception e){
			return "";
		}
	}	
	
	public static boolean isIntentAvailable(Context context, String action) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
	                    PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	public static boolean isOnline(Context c) {
	    ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public static String doublePrecision(double value){
		DecimalFormat twoDForm = new DecimalFormat("########.##");
		return twoDForm.format(value);
	}
	
	public static long shortDateLong(long time){		
		return time/1000L;
	}
	
	public static long dateLong(long time){		
		return time * 1000L;
	}
	
	public static String timesToJsonString(long timestamp){
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try{
			Date d = new Date(timestamp);
			return f.format(d);
		}catch(Exception e){
			return "";
		}
	}
	
	public long getCurrentTimestamp(){
		return System.currentTimeMillis() / 1000L;
	}
	
	public static String getUnit(int unit){
		switch(unit){
			case 0: return "PC";
			case 1: return "KG";
			case 2: return "L";
		}
		return "PC";
	}
	
	public static String getUnitValue(Product product){
		switch(product.getUnit()){
			case 0: return String.valueOf((int)product.getCount().doubleValue());				
			case 1: return String.valueOf(product.getCount());
			case 2: return String.valueOf(product.getCount());
			default: return String.valueOf((int)product.getCount().doubleValue());
		} 
	}
	
	public static Double getUnitDouble(Product product){
		switch(product.getUnit()){
			case 0: return Double.valueOf((int)product.getCount().doubleValue());				
			case 1: return product.getCount();
			case 2: return product.getCount();
			default: return Double.valueOf((int)product.getCount().doubleValue());
		} 
	}
	
	public static String getTotalPrice(List<Product> products, String currency, Resources resources){
		double priceTotal = 0.0;
		for(Product p : products){
        	if(p.getPrice() != null && p.getCount() != null){
        		priceTotal += (p.getCount() * p.getPrice());
        	}
        }
		return String.format(resources.getString(R.string.label_product_amount_total), priceTotal, currency);
	}
	
	public static int getImageCategoryResourceId(Category category){
		if(category != null){
			switch((int)category.getId().doubleValue()){		
				case 1: return R.drawable.ic_category_shopping;
				case 2: return R.drawable.ic_category_bill;
				case 3: return R.drawable.ic_category_party;
				case 4: return R.drawable.ic_category_oil;
				case 5: return R.drawable.ic_category_travel;
				case 6: return R.drawable.ic_category_other;
				case 7: return R.drawable.ic_category_company;
				default: return R.drawable.ic_category_no;
			} 
		}
		return R.drawable.ic_category_no;
	}
	
	public static String getDefaulCurrency(Context context){
		String locale = context.getResources().getConfiguration().locale.getCountry();
		Language language = Language.value(locale);
		switch(language){
			case PL:
				return "zł";
			default:
				return "$";
		}
	}
}
