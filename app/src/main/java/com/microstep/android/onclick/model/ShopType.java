package com.microstep.android.onclick.model;

public enum ShopType {

	CREATE, SEND, RECEIVE;
	
	public static boolean contains(String name) {
		try {
			valueOf(name.toUpperCase());
			return true;
		} catch (IllegalArgumentException e) {
		}
		return false;
	}
	
}
