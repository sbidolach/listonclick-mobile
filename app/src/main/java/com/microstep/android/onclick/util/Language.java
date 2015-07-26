package com.microstep.android.onclick.util;

public enum Language {
	
	PL, EN;
	
	public static Language value(String value){
		try{
			return valueOf(value.toUpperCase());
		}catch (Exception e) {
			return EN;
		}
	}
	
}
