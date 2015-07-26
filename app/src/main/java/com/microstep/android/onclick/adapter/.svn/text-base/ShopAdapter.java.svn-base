package com.microstep.android.onclick.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShopAdapter extends ArrayAdapter<Shop>{

	private ArrayList<Shop> items;
	private Context context;
	
	public ShopAdapter(Context context, int textViewResourceId, ArrayList<Shop> items) {
		super(context, textViewResourceId, items);
        this.items = items;
        this.context = context;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.shoprow, null);
        }
        Shop o = items.get(position);
        if (o != null) {
            TextView tt = (TextView) v.findViewById(R.id.shop_toptext);
            TextView tb = (TextView) v.findViewById(R.id.shop_bottomtext);
            TextView tp = (TextView) v.findViewById(R.id.shop_percent);
            tt.setText(o.getTitle());
            
            switch(o.getType()){
	            case RECEIVE:
	            	tb.setText(context.getResources().getString(R.string.label_shop_from) + ": " + o.getSmsPerson());
	            	break;
	            case SEND:
	            	tb.setText(context.getResources().getString(R.string.label_shop_to) + ": " + o.getSmsPerson());
	            	break;
	            default:
	            	tb.setText(context.getResources().getString(R.string.label_shop_created) + ": " + Utils.timesToString(o.getCreated()));
	            	break;
            }
            
            List<Product> products = o.getProducts();
            if(products != null){
	            StringBuffer buffer = new StringBuffer();
	            buffer.append(selectedProduct(products));
	            buffer.append("/");
	            buffer.append(products.size());
	            tp.setText(buffer.toString());
            }
        }
        return v;
    }
	
	@Override
	public void clear() {
		this.items.clear();
		super.clear();
	}
	
	@Override
	public Shop getItem(int position) {
		return super.getItem(position);
	}
	
	private int selectedProduct(List<Product> products){
		Iterator<Product> iter = products.iterator();
		int count = 0;
		while(iter.hasNext()){
			Product p = iter.next();
			if(p.isSelected()){
				count++;
			}
		}
		return count;
	}
		
}
