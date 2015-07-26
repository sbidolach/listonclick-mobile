package com.microstep.android.onclick.adapter;

import com.microstep.onclick.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IconMenuAdapter extends BaseAdapter {
	
	private Context mContext;

	public IconMenuAdapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return 6;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.main_default_icon, null);
			TextView tv = (TextView) v.findViewById(R.id.icon_text);			
			ImageView iv = (ImageView) v.findViewById(R.id.icon_image);
			
			switch(position){
				case 0:
					tv.setText(this.mContext.getResources().getString(R.string.menu_shopping));
					iv.setImageResource(R.drawable.icon1);					
					break;				
				case 1:
					tv.setText(this.mContext.getResources().getString(R.string.menu_shops));
					iv.setImageResource(R.drawable.icon4);
					break;
				case 2:
					tv.setText(this.mContext.getResources().getString(R.string.menu_account));
					iv.setImageResource(R.drawable.icon3);
					break;				
				case 3:
					tv.setText(this.mContext.getResources().getString(R.string.menu_history));
					iv.setImageResource(R.drawable.icon2);
					break;
				case 4:
					tv.setText(this.mContext.getResources().getString(R.string.menu_info));
					iv.setImageResource(R.drawable.icon5);
					break;
				case 5:
					tv.setText(this.mContext.getResources().getString(R.string.menu_settings));
					iv.setImageResource(R.drawable.icon6);
					break;
			}

		} else {
			v = convertView;
		}
		return v;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
