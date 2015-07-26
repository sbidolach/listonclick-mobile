package com.microstep.android.onclick.adapter;

import java.util.ArrayList;

import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoryAdapter extends ArrayAdapter<Shop>{

	private final int TITLE_NAME_LENGTH = 21;
	
	private ArrayList<Shop> items;
	private Context context;
	private String currency;
	
	public HistoryAdapter(Context context, int textViewResourceId, ArrayList<Shop> items) {
		super(context, textViewResourceId, items);
        this.items = items;
        this.context = context;
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);        
        this.currency = prefs.getString("currencyPref", Utils.getDefaulCurrency(context));        
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.historyrow, null);
        }
        Shop o = items.get(position);
        if (o != null) {
            TextView tt = (TextView) v.findViewById(R.id.shop_toptext);
            TextView tb = (TextView) v.findViewById(R.id.shop_bottomtext);
            TextView tp = (TextView) v.findViewById(R.id.shop_price);
            
            if(o.getTitle().length() > TITLE_NAME_LENGTH){
				tt.setText(o.getTitle().substring(0, TITLE_NAME_LENGTH).concat("..."));
			}else{
				tt.setText(o.getTitle());
			}         
            
            switch(o.getType()){
	            case RECEIVE:
	            	tb.setText(context.getResources().getString(R.string.label_shop_from) + ": " + o.getSmsPerson());
	            	break;
	            case SEND:
	            	tb.setText(context.getResources().getString(R.string.label_shop_to) + ": " + o.getSmsPerson());
	            	break;
	            default:
	            	tb.setText(context.getResources().getString(R.string.label_shop_finished) + ": " + Utils.timesToString(o.getModified()));
	            	break;
	        }      
            
            double price = 0.0D;
            for(Product p : o.getProducts()){
            	if(p.getPrice() != null){
            		price += p.getCount() * p.getPrice();
            	}
            }
                                    
            tp.setText(Utils.doublePrecision(price) + " " + this.currency);
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
		
}
