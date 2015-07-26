package com.microstep.android.onclick.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shop implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;	
	private String smsPerson;
	private boolean finished;
	private List<Product> products = new ArrayList<Product>();
	private long created;
	private long modified;
	private ShopType type;	
	private boolean synch;
	private Category category;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}	

	public String getSmsPerson() {
		return smsPerson;
	}

	public void setSmsPerson(String smsPerson) {
		this.smsPerson = smsPerson;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public ShopType getType() {
		return type;
	}

	public void setType(ShopType type) {
		this.type = type;
	}		
	
	public boolean isSynch() {
		return synch;
	}
	
	public void setSynch(boolean synch) {
		this.synch = synch;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Category getCategory() {
		return category;
	}
	
}
