package com.microstep.android.onclick.util;

public enum Currency {
	
	PLN("zł"), USD("$"), EUR("€");
	
	private String value;
	
	Currency(String value){
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static Currency value(String value){
		try{
			return valueOf(value.toUpperCase());
		}catch (Exception e) {
			return PLN;
		}
	}
	
}
