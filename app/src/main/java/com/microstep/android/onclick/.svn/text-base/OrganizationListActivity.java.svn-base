package com.microstep.android.onclick;

import java.util.ArrayList;
import java.util.List;

import com.microstep.android.onclick.adapter.OrganizationAdapter;
import com.microstep.android.onclick.location.PhoneLocation;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.rest.NoteService;
import com.microstep.android.onclick.rest.WebServiceFactory;
import com.microstep.android.onclick.rest.model.Login;
import com.microstep.android.onclick.security.SecurityContextHolder;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class OrganizationListActivity extends ListActivity implements OnClickListener, OnItemClickListener {

	public final static int REQUEST_CATEGORY_LIST = 1;
	public final static int REQUEST_MENU 		  = 2;
	public final static int REQUEST_PDF_LIST 	  = 3;
	public final static int REQUEST_PROM_LIST 	  = 4;
	public final static int ACTION_LIST 	= 1;
	public final static int ACTION_ADD_ITEM = 2;
	public final static int ACTION_ADD_BASKET = 3;
	
	private ArrayList<Organization> organizatons = new ArrayList<Organization>();
    private OrganizationAdapter<Organization> adapter = null;
    
	private Button bMap = null;
	private Button bBack = null;
	private LinearLayout lProgess = null;
	private Shop shop = null;
	
//	private Runnable viewOrganizations = new Runnable(){
//	    @Override
//	    public void run() {
//	    	getOrganizations();
//	    }
//    };
    
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
        	adapter.clear();
        	if(organizatons != null && organizatons.size() > 0){
                adapter.notifyDataSetChanged();    			
                for(int i=0;i<organizatons.size();i++){
                	Organization o = organizatons.get(i);                	
                	adapter.add(o);
                }
            }
        	OrganizationListActivity.this.lProgess.setVisibility(View.GONE);
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		if(Utils.isOnline(this)){		
			Login login = SecurityContextHolder.getSecurityContext().getLogin();
			if(login != null && !"-1".equals(login.getUserId())){						
				setContentView(R.layout.organizationlist);				
				Bundle bundle = OrganizationListActivity.this.getIntent().getExtras();
				if(bundle != null){
					this.shop = (Shop) bundle.getSerializable("shop");
				}
				this.adapter = new OrganizationAdapter<Organization>(this, R.layout.organizationlist_row, organizatons);
				setListAdapter(this.adapter);				
				ListView lv = getListView();        
		        lv.setOnItemClickListener(this);   
		        registerForContextMenu(lv);
		    	this.lProgess = (LinearLayout) this.findViewById(R.id.layout_progress);		    	
				this.bMap = (Button) this.findViewById(R.id.bn_map);
				this.bMap.setOnClickListener(this);	
				this.bBack = (Button) this.findViewById(R.id.bn_back);
				this.bBack.setOnClickListener(this);				
				
				if(this.shop == null){
					LinearLayout bottomPanel = (LinearLayout) this.findViewById(R.id.product_bottom_panel);
					bottomPanel.setVisibility(View.GONE);							
				}else{
					TextView textItem = (TextView) this.findViewById(R.id.products_basket);
					TextView textPrice = (TextView) this.findViewById(R.id.products_price);
					textItem.setText(String.format(getResources().getString(R.string.label_product_amount_items), 
							this.shop.getProducts().size()));
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
					textPrice.setText(Utils.getTotalPrice(this.shop.getProducts(), 
							prefs.getString("currencyPref", Utils.getDefaulCurrency(this)), getResources()));		        	
				}
				
//				Utils.refreshListView(this.viewOrganizations);
				GetProductNameTask task = new GetProductNameTask();
				task.execute();
			}else{			
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.dialog_error_network)
			       .setCancelable(false)
			       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   finish();
			        	   dialog.cancel();
			           }
			    }).show();					
			}
			
//			Toast.makeText(OrganizationListActivity.this, 
//					getResources().getString(R.string.label_product_price_info), Toast.LENGTH_SHORT).show();
			
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.dialog_no_network)
		       .setCancelable(false)
		       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
		        	   dialog.cancel();
		           }
		    }).show();
		}				
	}
	
	/**
	 * Method get closest organization from web service
	 */
	private void getOrganizations(){
        try{                   	
        	this.organizatons = new ArrayList<Organization>();
        	NoteService noteService = WebServiceFactory.getNoteService();        	

        	// Get last know location from GPS or network
        	Location location = PhoneLocation.getCurrentLocation(OrganizationListActivity.this);        	
        	if(location != null){        		
        		List<Organization> orgs = noteService.getOrganizationsNearMe(location.getLatitude(), location.getLongitude());
        		for(Organization o : orgs){
        			this.organizatons.add(o);
        		}
        	}else{
        		OrganizationListActivity.this.runOnUiThread(new Runnable() {	
        			@Override
        			public void run() {
        				Toast.makeText(OrganizationListActivity.this, 
        						getResources().getString(R.string.label_no_valid_gps), Toast.LENGTH_SHORT).show();
        			}
        		});        		
        	}
            Log.i("ARRAY", ""+ organizatons.size());
          } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
	}		
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {		
		Organization organization = this.adapter.getItem(position);		
		int news = organization.getNewsletterSize();
		int prod = organization.getProductSize();
		int prom = organization.getPromotionSize();
		
		int p1 = news * prod;
		int p2 = prod * prom;
		int p3 = news * prom;
		
		if(p1>0 || p2>0 || p3>0 ){
			Intent intent = new Intent();
			intent.putExtra("organization", organization);
			intent.putExtra("shop", this.shop);
			intent.setClass(OrganizationListActivity.this, OrganizationMenuActivity.class);
			startActivityForResult(intent, REQUEST_MENU);
		}else{
			if(news > 0){				
				Intent intent = new Intent();
				intent.putExtra("organization", organization);
				intent.putExtra("shop", this.shop);
				intent.setClass(OrganizationListActivity.this, OrganizationCategoryActivity.class);
				startActivityForResult(intent, REQUEST_PDF_LIST);				
			}else if(prom > 0){
				Intent intent = new Intent();
				intent.putExtra("organization", organization);
				intent.putExtra("shop", this.shop);
				intent.setClass(OrganizationListActivity.this, OrganizationCategoryActivity.class);
				startActivityForResult(intent, REQUEST_PROM_LIST);
			}else{
				Intent intent = new Intent();
				intent.putExtra("organization", organization);
				intent.putExtra("shop", this.shop);
				intent.setClass(OrganizationListActivity.this, OrganizationCategoryActivity.class);
				startActivityForResult(intent, REQUEST_CATEGORY_LIST);
			}
		}					
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("requestCode", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bn_back:	
				finish();
				break;
			case R.id.bn_map:
				Toast.makeText(OrganizationListActivity.this, 
						getResources().getString(R.string.label_no_map), Toast.LENGTH_SHORT).show();
				break;
		}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.organizationlist_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();		
		switch (item.getItemId()) {
	        case R.id.context_map_route:	            
	        	Organization organization = adapter.getItem((int)info.id);
	        	if(organization != null){              	
	            	Location srcLocation = PhoneLocation.getCurrentLocation(this);	            		            	
	            	com.microstep.android.onclick.model.Location destLocation = organization.getLocations().get(0);
	            	if(srcLocation != null && destLocation != null){
			        	Uri uri = Uri.parse("http://maps.google.com/maps?&saddr="+
			        			srcLocation.getLatitude()+","+srcLocation.getLongitude()+"&daddr="+
			        			destLocation.getLatitude()+","+destLocation.getLongitude());			        	
			        	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			        	startActivity(intent);
	            	}
	        	}
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent = null;
		if(requestCode == REQUEST_CATEGORY_LIST || requestCode == REQUEST_MENU || requestCode == REQUEST_PROM_LIST){
			if(resultCode == ACTION_LIST){
				finish();
			}else if(resultCode == ACTION_ADD_ITEM){
				intent = new Intent();
				Product product = (Product) data.getExtras().getSerializable("product");
				intent.putExtra("product", product);
				setResult(ProductListActivity.ACTION_ADD_ITEM, intent);
				finish();
			}else if(resultCode == ACTION_ADD_BASKET){
				intent = new Intent();
				Product product = (Product) data.getExtras().getSerializable("product");
				intent.putExtra("product", product);
				setResult(MainActivity.ACTION_ADD_BASKET, intent);
				finish();
			}
		}
	}
	
	public class GetProductNameTask extends AsyncTask<String, Void, ArrayList<String>> {
		
		@Override
	    // three dots is java for an array of strings
		protected ArrayList<String> doInBackground(String... args){
			Log.d("gottaGo", "doInBackground");													
			try{
				getOrganizations();
			}catch (Exception e) {
				Log.e("YourApp", "GetPlaces : doInBackground", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result){			
		}

	}

}
