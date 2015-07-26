package com.microstep.android.onclick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.microstep.android.onclick.adapter.OrganizationAdapter;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.rest.NoteService;
import com.microstep.android.onclick.rest.WebServiceFactory;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class OrganizationCategoryActivity extends ListActivity implements OnClickListener, OnItemClickListener {

	public final static int REQUEST_PRODUCT_LIST 	  = 1;
	public final static int REQUEST_PROM_PRODUCT_LIST = 2;
	public final static int REQUEST_PDF 			  = 3;
	public final static int ACTION_LIST 	= 1;
	public final static int ACTION_SHOPS 	= 2;
	public final static int ACTION_ADD_ITEM = 3;
	public final static int ACTION_ADD_BASKET = 4;
	
	private ArrayList<Category> categories = new ArrayList<Category>();
    private OrganizationAdapter<Category> adapter = null;
    private Organization organization = null;
    private Category category = null;
    private Shop shop = null;
    private int parentRequestCode;
    
	private Button bList = null;
	private Button bBack = null;
	private ImageButton bSearch = null;
	private ImageButton bScan = null;
	private TextView tProductName = null;
	private LinearLayout lProgess = null;
	private TextView topTextView = null;
	
//	private Runnable viewCategories = new Runnable(){
//	    @Override
//	    public void run() {
//	    	getCategories();    		    	
//	    }
//    };
    
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
        	adapter.clear();
        	if(categories != null && categories.size() > 0){
                adapter.notifyDataSetChanged();    			
                for(int i=0;i<categories.size();i++){
                	Category o = categories.get(i);
                	adapter.add(o);
                }
            }
        	OrganizationCategoryActivity.this.lProgess.setVisibility(View.GONE);
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.organizationcategory);
		
		Bundle bundle = getIntent().getExtras();
        this.organization = (Organization) bundle.getSerializable("organization");
        this.shop = (Shop) bundle.getSerializable("shop");
        this.parentRequestCode = bundle.getInt("requestCode");        
                
        int rowLayoutID;        
        switch(parentRequestCode){
        	case OrganizationListActivity.REQUEST_PDF_LIST :
        		rowLayoutID = R.layout.organizationpromotion_row;
        		LinearLayout linearSearchLayout = (LinearLayout) this.findViewById(R.id.linearSearchLayout);
                linearSearchLayout.setVisibility(View.GONE);
        		break;
        	case OrganizationListActivity.REQUEST_PROM_LIST :
        		rowLayoutID = R.layout.organizationpromotion_row;
        		break;
        	default:
        		rowLayoutID = R.layout.organizationcategory_row;
        }          
		
        this.adapter = new OrganizationAdapter<Category>(this, rowLayoutID, categories);
		setListAdapter(this.adapter);
		
		ListView lv = getListView();        
        lv.setOnItemClickListener(this);                        
        
        this.lProgess = (LinearLayout) this.findViewById(R.id.layout_progress);
		this.bList = (Button) this.findViewById(R.id.bn_shopping_list);
		this.topTextView = (TextView) this.findViewById(R.id.text_title_top);
		this.bList.setOnClickListener(this);
		this.topTextView.setText(this.organization.getName());
		
		if(this.shop != null){
			this.bList.setText(getResources().getString(R.string.menu_shopping_list));
			TextView textItem = (TextView) this.findViewById(R.id.products_basket);
			TextView textPrice = (TextView) this.findViewById(R.id.products_price);
			textItem.setText(String.format(getResources().getString(R.string.label_product_amount_items), 
					this.shop.getProducts().size()));
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			textPrice.setText(Utils.getTotalPrice(this.shop.getProducts(), 
					prefs.getString("currencyPref", Utils.getDefaulCurrency(this)), getResources()));
		}else{
			this.bList.setText(getResources().getString(R.string.menu_shops));
			LinearLayout bottomPanel = (LinearLayout) this.findViewById(R.id.product_bottom_panel);
			bottomPanel.setVisibility(View.GONE);	
		}
		
		this.bBack = (Button) this.findViewById(R.id.bn_back);
		this.bBack.setOnClickListener(this);	
		this.bSearch = (ImageButton) this.findViewById(R.id.bn_search);
		this.bSearch.setOnClickListener(this);	
		this.bScan = (ImageButton) this.findViewById(R.id.bn_scan);
		this.bScan.setOnClickListener(this);	
		this.tProductName = (TextView) this.findViewById(R.id.organization_product);
		
		GetProductNameTask task = new GetProductNameTask();
    	task.execute();	    	
		//Utils.refreshListView(this.viewCategories);
	}
	
	private void getCategories(){
        try{                   	
        	this.categories = new ArrayList<Category>();		
        	NoteService noteService = WebServiceFactory.getNoteService();
        	if(organization != null){
        		List<Category> lCategory = new ArrayList<Category>();
        		if(parentRequestCode == OrganizationListActivity.REQUEST_CATEGORY_LIST){        			
        			if(category != null){
        				lCategory = noteService.getOrganizationCategoryById(organization.getId(), category.getId());
	        		}else{
	        			lCategory = noteService.getOrganizationCategoryById(organization.getId(), null);
	        		}
        		}else if(parentRequestCode == OrganizationListActivity.REQUEST_PROM_LIST){
        			lCategory = noteService.getOrganizationCategories(organization.getId(), "PROMOTION");
        		}else if(parentRequestCode == OrganizationListActivity.REQUEST_PDF_LIST){
        			lCategory = noteService.getOrganizationCategories(organization.getId(), "PDF");        			
        		}
        		this.categories.addAll(lCategory);
        	}
            Log.i("ARRAY", ""+ categories.size());
          } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		this.category = this.adapter.getItem(position);
		if(OrganizationListActivity.REQUEST_CATEGORY_LIST == parentRequestCode){
			if(!this.category.isHasChild()){
				Intent intent = new Intent();
				intent.putExtra("category", this.category);
				intent.putExtra("organization", this.organization);
				intent.putExtra("shop", this.shop);
				intent.setClass(this, OrganizationListProductActivity.class);
				startActivityForResult(intent, REQUEST_PRODUCT_LIST);
			}else{
				this.lProgess.setVisibility(View.VISIBLE);
				//Utils.refreshListView(this.viewCategories);
				GetProductNameTask task = new GetProductNameTask();
				task.execute();	
			}
		}else if(OrganizationListActivity.REQUEST_PROM_LIST == parentRequestCode){
			Intent intent = new Intent();
			intent.putExtra("category", this.category);
			intent.putExtra("organization", this.organization);
			intent.putExtra("shop", this.shop);
			intent.setClass(this, OrganizationListProductActivity.class);
			startActivityForResult(intent, REQUEST_PROM_PRODUCT_LIST);
		}else if(OrganizationListActivity.REQUEST_PDF_LIST == parentRequestCode){
			Intent intent = new Intent();
			intent.putExtra("category", this.category);
			intent.putExtra("organization", this.organization);
			intent.putExtra("shop", this.shop);
			intent.setClass(this, OrganizationPdfViewActivity.class);
			startActivityForResult(intent, REQUEST_PDF);		    
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bn_back:	
				if(this.category == null || this.category.getParent() == null){
					finish();
				}else{
					this.category = null;
					this.lProgess.setVisibility(View.VISIBLE);
					//Utils.refreshListView(this.viewCategories);
					GetProductNameTask task = new GetProductNameTask();
					task.execute();	
				}
				break;
			case R.id.bn_shopping_list:
				if(this.shop != null){
					setResult(OrganizationListActivity.ACTION_LIST);
					finish();
				}else{
					finish();
				}
				break;
			case R.id.bn_search:									
				String name = this.tProductName.getText().toString();			
				if(!"".equals(name)){
					Product product = new Product();
					product.setName(name);
					Intent intent = new Intent();
					intent.putExtra("category", this.category);
					intent.putExtra("organization", this.organization);
					intent.putExtra("shop", this.shop);
					intent.putExtra("product", product);
					intent.setClass(this, OrganizationListProductActivity.class);
					startActivityForResult(intent, REQUEST_PRODUCT_LIST);
            	}				
				break;
			case R.id.bn_scan:
				IntentIntegrator integrator = new IntentIntegrator(this);
	        	integrator.initiateScan();
				break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent = null;
		if(requestCode == REQUEST_PRODUCT_LIST || requestCode == REQUEST_PROM_PRODUCT_LIST){
			if(resultCode == ACTION_LIST){
				setResult(OrganizationListActivity.ACTION_LIST);
				finish();
			}else if(resultCode == ACTION_SHOPS){
				finish();
			}else if(resultCode == ACTION_ADD_ITEM){
				intent = new Intent();
				Product product = (Product) data.getExtras().getSerializable("product");
				intent.putExtra("product", product);
				setResult(OrganizationListActivity.ACTION_ADD_ITEM, intent);
				finish();
			}else if(resultCode == ACTION_ADD_BASKET){
				intent = new Intent();
				Product product = (Product) data.getExtras().getSerializable("product");
				intent.putExtra("product", product);
				setResult(OrganizationListActivity.ACTION_ADD_BASKET, intent);
				finish();
			}
		}else if(requestCode == IntentIntegrator.REQUEST_CODE){
			if (resultCode != RESULT_CANCELED) {
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult != null) {					
					String upc = scanResult.getContents();
					String format = scanResult.getFormatName();		
					
					if(IntentIntegrator.QR_CODE_TYPES.contains(format)){					
						new AlertDialog.Builder(this)
		        		.setMessage(String.format(getResources().getString(R.string.zxing_qr_code), ""))
		        		.setCancelable(true)
		        		.setNegativeButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
		        			public void onClick(DialogInterface dialog, int id) {
		        				dialog.cancel();
		        			}
		        		}).show();
					}else{
						intent = new Intent();
						Product product = new Product();
						product.setIdentifier(upc);
						intent.putExtra("category", this.category);
						intent.putExtra("organization", this.organization);
						intent.putExtra("shop", this.shop);
					    intent.putExtra("product", product);
						intent.setClass(this, ProductScanActivity.class);
						startActivityForResult(intent, REQUEST_PRODUCT_LIST);					
					}
				}
			}
		}
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("requestCode", requestCode);
		super.startActivityForResult(intent, requestCode);
	}
	
	public class GetProductNameTask extends AsyncTask<String, Void, ArrayList<String>> {
		
		@Override
	    // three dots is java for an array of strings
		protected ArrayList<String> doInBackground(String... args){
			Log.d("gottaGo", "doInBackground");													
			try{
				getCategories();
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
