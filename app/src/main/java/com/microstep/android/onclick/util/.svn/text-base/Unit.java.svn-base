package com.microstep.android.onclick.util;

public enum Unit {

	PC(0), KG(1), L(2);
	
	private int value;
	
	Unit(int value){
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static Unit value(String value){
		try{
			return valueOf(value.toUpperCase());
		}catch (Exception e) {
			return PC;
		}
	}
	
}
