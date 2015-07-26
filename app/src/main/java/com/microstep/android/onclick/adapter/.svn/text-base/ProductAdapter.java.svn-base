package com.microstep.android.onclick.adapter;

import java.util.ArrayList;

import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductAdapter extends ArrayAdapter<Product> {

	private final int PRODUCT_NAME_LENGTH = 41;
	
	private ArrayList<Product> items;
	private Context context;
	private String currency;
	private int textViewResourceId;
	
	public ProductAdapter(Context context, int textViewResourceId,
			ArrayList<Product> items, Shop shop, String currency) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;
		this.currency = currency;
		this.textViewResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(this.textViewResourceId, null);
		}
		Product p = items.get(position);
		if (p != null) {			
			TextView tt = (TextView) v.findViewById(R.id.toptext);
			ImageView ic = (ImageView) v.findViewById(R.id.icon_category);
			String[] units = context.getResources().getStringArray(R.array.unitArray);
			
			if(p.getName().length() > PRODUCT_NAME_LENGTH){
				tt.setText(p.getName().substring(0, PRODUCT_NAME_LENGTH).concat("..."));
			}else{
				tt.setText(p.getName());
			}

			ic.setImageResource(Utils.getImageCategoryResourceId(p.getCategory()));
			
			switch(this.textViewResourceId){
				case R.layout.productrow_list:					
					ImageView iv = (ImageView) v.findViewById(R.id.icon);
					LinearLayout ll = (LinearLayout) v.findViewById(R.id.row);
					TextView bu = (TextView) v.findViewById(R.id.productunit);
					TextView bt = (TextView) v.findViewById(R.id.productcount);	

					if(p.isSelected()){
						iv.setImageResource(R.drawable.ic_checkbox_checked);
						ll.setBackgroundColor(Color.parseColor("#6F6F6F"));
					}else{
						iv.setImageResource(R.drawable.ic_checkbox_unchecked);
						ll.setBackgroundResource(R.drawable.bg_shopping_row);
					}
										
					bu.setText(units[p.getUnit()]);					
					bt.setText(Utils.getUnitValue(p));										
					
					break;
				case R.layout.productrow_history:					
					TextView pt = (TextView) v.findViewById(R.id.productprice);
					TextView pd = (TextView) v.findViewById(R.id.productdescription);
					
					// Calculate total price for product quality * price
					if(p.getPrice() == null){
						pt.setText("0.0 " + this.currency);
					}else{         
						pt.setText(Utils.doublePrecision(p.getCount() * p.getPrice()) + " " + this.currency);
					}
					
					// Create information about unit and quality
					StringBuffer buffer = new StringBuffer();
					buffer.append("( ");
					buffer.append(Utils.getUnitValue(p));
					buffer.append(" ");
					buffer.append(units[p.getUnit()]);
					buffer.append(" )");
					pd.setText(buffer.toString());
					
					break;
			}
						
		}
		return v;
	}

	@Override
	public void clear() {
		this.items.clear();
		super.clear();
	}

}
