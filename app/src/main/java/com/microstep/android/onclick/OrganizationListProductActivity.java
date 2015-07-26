package com.microstep.android.onclick;

import java.util.ArrayList;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.microstep.android.onclick.adapter.OrganizationAdapter;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.OrganizationProductType;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.rest.NoteService;
import com.microstep.android.onclick.rest.WebServiceFactory;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class OrganizationListProductActivity extends ListActivity implements OnClickListener, OnItemClickListener {

	public final static int REQUEST_DETAIL = 1;
	
	public final static int ACTION_ADD_ITEM = 1;
	public final static int ACTION_ADD_BASKET = 2;
	
	private ArrayList<Product> products = new ArrayList<Product>();
    private OrganizationAdapter<Product> adapter = null;
    
    private Organization organization = null;
    private Category category = null;
    private Shop shop = null;
    private Product product = null;
    private OrganizationProductType organizationProductType;
    
	private Button bShoppingList = null;
	private Button bBack = null;
	private ImageButton bSearch = null;
	private ImageButton bScan = null;
	private TextView tProductName = null;
	private LinearLayout lProgess = null;
	private TextView topTextView = null;
	
//	private Runnable viewProducts = new Runnable(){
//	    @Override
//	    public void run() {
//	    	getProducts();
//	    }
//    };
    
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
        	adapter.clear();
        	if(products != null && products.size() > 0){
                adapter.notifyDataSetChanged();    			
                for(int i=0;i<products.size();i++){
                	Product o = products.get(i);
                	adapter.add(o);
                }
            }
        	OrganizationListProductActivity.this.lProgess.setVisibility(View.GONE);

        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.organizationlistproduct);
		
		this.adapter = new OrganizationAdapter<Product>(this, R.layout.organizationlistproduct_row, products);
		setListAdapter(this.adapter);
		
		ListView lv = getListView();        
        lv.setOnItemClickListener(this);
        this.lProgess = (LinearLayout) this.findViewById(R.id.layout_progress);
		this.bShoppingList = (Button) this.findViewById(R.id.bn_shopping_list);
		this.bShoppingList.setOnClickListener(this);			
		this.bBack = (Button) this.findViewById(R.id.bn_back);
		this.bBack.setOnClickListener(this);	
		this.bSearch = (ImageButton) this.findViewById(R.id.bn_search);
		this.bSearch.setOnClickListener(this);	
		this.bScan = (ImageButton) this.findViewById(R.id.bn_scan);
		this.bScan.setOnClickListener(this);	
		this.tProductName = (TextView) this.findViewById(R.id.organization_product);
		this.topTextView = (TextView) this.findViewById(R.id.text_title_top);
		
		Bundle bundle = getIntent().getExtras();
        this.organization = (Organization) bundle.getSerializable("organization");
        this.category = (Category) bundle.getSerializable("category");
        this.shop = (Shop) bundle.getSerializable("shop");
        this.product = (Product) bundle.getSerializable("product");
        this.topTextView.setText(this.organization.getName());
        
        // Set type of display product Promotion product or normal category product from shop
        int parentRequestCode = bundle.getInt("requestCode");
        switch(parentRequestCode){
        	case OrganizationCategoryActivity.REQUEST_PROM_PRODUCT_LIST :
        		organizationProductType = OrganizationProductType.ORGANIZATION_PROMOTION;
        		break;
        	default:
        		organizationProductType = OrganizationProductType.ORGANIZATION_PRODUCT;
        		break;
        }
        
        if(this.shop != null){
			this.bShoppingList.setText(getResources().getString(R.string.menu_shopping_list));
			TextView textItem = (TextView) this.findViewById(R.id.products_basket);
			TextView textPrice = (TextView) this.findViewById(R.id.products_price);
			textItem.setText(String.format(getResources().getString(R.string.label_product_amount_items), 
					this.shop.getProducts().size()));
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			textPrice.setText(Utils.getTotalPrice(this.shop.getProducts(), 
					prefs.getString("currencyPref", Utils.getDefaulCurrency(this)), getResources()));
		}else{
			this.bShoppingList.setText(getResources().getString(R.string.menu_shops));
			LinearLayout bottomPanel = (LinearLayout) this.findViewById(R.id.product_bottom_panel);
			bottomPanel.setVisibility(View.GONE);
		}
        
//		Utils.refreshListView(this.viewProducts);
        GetProductTask task = new GetProductTask();
        task.execute();
	}
	
	/**
	 * Method get product from web service
	 */
	private void getProducts(){
        try{                   	
        	this.products = new ArrayList<Product>();			
        	NoteService noteService = WebServiceFactory.getNoteService();    
        	if(this.product != null){
        		if(this.product.getIdentifier() != null){
        			this.products.addAll(noteService.getOrganizationProductsByIdentifier(
    	        			this.organization.getId(), this.product.getIdentifier()));
        		}else{
        			this.products.addAll(
        					noteService.getOrganizationProductsByName(
        							this.organization.getId(), null, this.product.getName(), 
        							organizationProductType));
        		}
        	}else{
	        	this.products.addAll(
	        			noteService.getOrganizationProductsByCategoryId(
	        					this.organization.getId(), this.category.getId(), 
	        					organizationProductType));
        	}
            Log.i("ARRAY", ""+ products.size());
          } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
	}	
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Product product = this.adapter.getItem(position);
		product.setCount(1D);
		Intent intent = new Intent();
		intent.setClass(this, ProductDetailActivity.class);
		intent.putExtra("category", this.category);
		intent.putExtra("organization", this.organization);
		intent.putExtra("shop", this.shop);
		intent.putExtra("product", product);
		startActivityForResult(intent, REQUEST_DETAIL);		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bn_back:	
				finish();
				break;
			case R.id.bn_shopping_list:
				if(this.shop != null){
					setResult(OrganizationCategoryActivity.ACTION_LIST);
					finish();
				}else{
					setResult(OrganizationCategoryActivity.ACTION_SHOPS);
					finish();
				}	    		
				break;
			case R.id.bn_search:									
				String name = this.tProductName.getText().toString();			
				if(!"".equals(name)){
					this.lProgess.setVisibility(View.VISIBLE);
					this.product = new Product();
					this.product.setName(name);
//					Utils.refreshListView(this.viewProducts);
					GetProductTask task = new GetProductTask();
					task.execute();
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
		if(requestCode == REQUEST_DETAIL){
			if(resultCode == ACTION_ADD_ITEM){
				intent = new Intent();
				Product product = (Product) data.getExtras().getSerializable("product");
				intent.putExtra("product", product);
				setResult(OrganizationCategoryActivity.ACTION_ADD_ITEM, intent);
				finish();
			}else if(resultCode == ACTION_ADD_BASKET){
				intent = new Intent();
				Product product = (Product) data.getExtras().getSerializable("product");
				intent.putExtra("product", product);
				setResult(OrganizationCategoryActivity.ACTION_ADD_BASKET, intent);
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
						this.lProgess.setVisibility(View.VISIBLE);
						this.product = new Product();
						this.product.setIdentifier(upc);
//						Utils.refreshListView(this.viewProducts);
						GetProductTask task = new GetProductTask();
						task.execute();
					}
				}
			}
		}

	}
	
	public class GetProductTask extends AsyncTask<String, Void, ArrayList<String>> {
		
		@Override
	    // three dots is java for an array of strings
		protected ArrayList<String> doInBackground(String... args){
			Log.d("gottaGo", "doInBackground");													
			try{
				getProducts();
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
