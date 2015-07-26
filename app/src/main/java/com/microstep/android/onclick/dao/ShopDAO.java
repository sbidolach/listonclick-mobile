package com.microstep.android.onclick.dao;

import java.util.List;

import android.content.Context;

import com.microstep.android.onclick.model.Shop;

public interface ShopDAO {

	public Long insert(Shop shop);	
	public int delete(Shop shop);	
	public boolean update(Shop shop);
	public List<Shop> feachAll();
	public List<Shop> feachAll(boolean finished, String orderBy);
	public List<Shop> getHistory(boolean finished, String orderBy, Integer termtime);
	public Shop getShop(Long shopId);
	public void setContext(Context context);
	public List<Shop> feachSynch();
	
}
