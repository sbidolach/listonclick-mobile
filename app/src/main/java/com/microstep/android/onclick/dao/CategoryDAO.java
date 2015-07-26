package com.microstep.android.onclick.dao;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.util.Language;

public interface CategoryDAO {

	public Long insert(Category category);
	public int delete(Category category);
	public int deleteAll();	
	public List<Category> getCategories(Language language);
	public void setContext(Context context);
	public Date getLastDateUpdate(Language language);
	public Category getById(Long categoryId);
	public Category getByName(String name, Language language);
	
}
