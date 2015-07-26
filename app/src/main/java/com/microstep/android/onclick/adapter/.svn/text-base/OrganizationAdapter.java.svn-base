package com.microstep.android.onclick.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.microstep.android.onclick.image.ImageManager;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrganizationAdapter<T> extends ArrayAdapter<T> {

	private final int ORGANIZATION_NAME_LENGTH = 21;
	private final int PRODUCT_NAME_LENGTH = 61;
	
	private ArrayList<T> items;
	private Context context;
	private int textViewResourceId;
	public ImageManager imageManager;
	
	public OrganizationAdapter(Context context, int textViewResourceId,
			ArrayList<T> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.imageManager = new ImageManager(context, 300000);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(textViewResourceId, null);
			holder = new ViewHolder();
			holder.bt = (TextView) v.findViewById(R.id.bottomtext);
			holder.tt = (TextView) v.findViewById(R.id.toptext);
			holder.iv = (ImageView) v.findViewById(R.id.image_has_child);
			holder.pp = (TextView) v.findViewById(R.id.productprice);
			holder.pd = (TextView) v.findViewById(R.id.productdescription);
			holder.pic = (ImageView) v.findViewById(R.id.icon);
			v.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}				
		
		if(items.size() > position){
			T object = items.get(position);
			if(object != null){		
				switch(textViewResourceId){
					case R.layout.organizationlist_row:
						Organization o = (Organization) object;						
						if(o.getName().length() > ORGANIZATION_NAME_LENGTH){
							holder.tt.setText(o.getName().substring(0, ORGANIZATION_NAME_LENGTH).concat("..."));
						}else{
							holder.tt.setText(o.getName());
						}
						
						if(o.getLocations() != null && o.getLocations().size() > 0){
							Double distance = o.getLocations().get(0).getDistance() / 1000D;
							DecimalFormat twoDForm = new DecimalFormat("########.#");	
							holder.bt.setText(String.format(this.context.getResources().getString(R.string.label_organization_distance), 
									twoDForm.format(distance)));
						}
						if(o.getLogoImagePath() != null){
							holder.pic.setTag(o.getLogoImagePath());
							imageManager.displayImage(o.getLogoImagePath(), holder.pic, R.drawable.img_no_photo);
						}
						break;
					case R.layout.organizationcategory_row:
						Category c = (Category) object;
						if(c.getName().length() > ORGANIZATION_NAME_LENGTH){
							holder.tt.setText(c.getName().substring(0, ORGANIZATION_NAME_LENGTH).concat("..."));
						}else{
							holder.tt.setText(c.getName());
						}
						
						if(c.getImageSmallUrl() != null){
							holder.pic.setTag(c.getImageSmallUrl());
							imageManager.displayImage(c.getImageSmallUrl(), holder.pic, R.drawable.img_no_photo);
						}
						
						if(c.isHasChild()){
							holder.iv.setImageResource(R.drawable.ic_menu_arrow);
						}else{
							holder.iv.setImageResource(R.drawable.ic_menu_list);
						}						
						break;
					case R.layout.organizationlistproduct_row:
						Product p = (Product) object;
						if(p.getName().length() > PRODUCT_NAME_LENGTH){
							holder.tt.setText(p.getName().substring(0, PRODUCT_NAME_LENGTH).concat("..."));
						}else{
							holder.tt.setText(p.getName());
						}
						
						if(p.getImageSmallPath() != null){
							holder.pic.setTag(p.getImageSmallPath());
							imageManager.displayImage(p.getImageSmallPath(), holder.pic, R.drawable.img_no_photo);
						}
						
						holder.pp.setText(Utils.doublePrecision(p.getPrice()) + " " + p.getCurrency().getValue());
						holder.pd.setText(p.getPriceDescription());						
						break;
					case R.layout.organizationpromotion_row:
						Category pr = (Category) object;
						if(pr.getName().length() > PRODUCT_NAME_LENGTH){
							holder.tt.setText(pr.getName().substring(0, PRODUCT_NAME_LENGTH).concat("..."));
						}else{
							holder.tt.setText(pr.getName());
						}
						
						if(pr.getImageSmallUrl() != null){
							holder.pic.setTag(pr.getImageSmallUrl());
							imageManager.displayImage(pr.getImageSmallUrl(), holder.pic, R.drawable.img_no_photo);
						}

						holder.bt.setText(Utils.timesToString(Utils.shortDateLong(pr.getStart())) + " - " + 
								Utils.timesToString(Utils.shortDateLong(pr.getEnd())));						
						break;
				}
			}
		}
		return v;
	}

	@Override
	public void clear() {
		this.items.clear();
		super.clear();
	}	
	
	private static class ViewHolder {
		TextView bt;
		TextView tt;
		ImageView pic;
		ImageView iv;
		TextView pp;
		TextView pd;
	}		

}
