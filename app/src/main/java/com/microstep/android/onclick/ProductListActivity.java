package com.microstep.android.onclick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.microstep.android.onclick.adapter.ProductAdapter;
import com.microstep.android.onclick.dao.FactoryDAO;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ProductListActivity extends ListActivity implements OnClickListener, OnItemClickListener, OnSharedPreferenceChangeListener {
	
	public final static int REQUEST_CURRENT = 1;
	public final static int REQUEST_SCAN = 2;
	public final static int REQUEST_DETAIL = 3;
	public final static int REQUEST_SHOP = 4;
	
	public final static int ACTION_DELETE = 1;
	public final static int ACTION_REVERT = 2;
	public final static int ACTION_ADD = 3;
	public final static int ACTION_EDIT = 4;
	public final static int ACTION_SELECT = 5;
	public final static int ACTION_ADD_ITEM = 6;
	
    private ArrayList<Product> m_orders = null;
    private ProductAdapter m_adapter = null;
    
    private Shop shop = null;
    private String currency = null;
    
    private Button bShoppingList = null;
    private Button bAddItem = null;
    private ImageButton bDisplayShops = null;
    private ImageButton bRunScanner = null;
    
    private TextView textViewPriceTotal = null;
    private TextView textViewPriceBasket = null;
    
    private Runnable viewOrders = new Runnable(){
	    @Override
	    public void run() {
	    	getProducts();
	    }
    };
    
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
        	double priceBasket = 0.0;
        	double priceTotal = 0.0;
        	m_adapter.clear();
        	if(m_orders != null && m_orders.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_orders.size();i++){
                	Product p = m_orders.get(i);
                	m_adapter.add(p);
                	if(p.getPrice() != null && p.getCount() != null){
                		if(p.isSelected()){
                			priceBasket += (p.getCount() * p.getPrice());
                		}
                		priceTotal += (p.getCount() * p.getPrice());
                	}
                }
            }
            m_adapter.notifyDataSetChanged();            	
			textViewPriceTotal.setText(String.format(getResources().getString(
					R.string.label_product_amount_total), priceTotal, currency));			
			if(textViewPriceBasket != null){
				textViewPriceBasket.setText(String.format(getResources().getString(
						R.string.label_product_amount_basket), priceBasket, currency));				
			}
			ProductListActivity.this.shop.getProducts().clear();
			ProductListActivity.this.shop.getProducts().addAll(m_orders);
        }
    };
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);               
        this.currency = prefs.getString("currencyPref", Utils.getDefaulCurrency(this));
        this.m_orders = new ArrayList<Product>();
        
        Intent intent = ProductListActivity.this.getIntent();
        Bundle bundle = intent.getExtras();        
        this.shop = (Shop) bundle.getSerializable("shop");        	
        
		if(HistoryActivity.class.getName().equals(getCallingActivity().getClassName())){
			setContentView(R.layout.historyproductlist);
			this.m_adapter = new ProductAdapter(this, R.layout.productrow_history, m_orders, this.shop, this.currency);
		}else{					
			setContentView(R.layout.productlist);
			this.textViewPriceBasket = (TextView) ProductListActivity.this.findViewById(R.id.products_basket);					
			this.bAddItem = (Button) this.findViewById(R.id.bn_add_item);
			this.bAddItem.setOnClickListener(this);
			this.bDisplayShops = (ImageButton) this.findViewById(R.id.bn_shops);
			this.bDisplayShops.setOnClickListener(this);
			this.bRunScanner = (ImageButton) this.findViewById(R.id.bn_scan);
			this.bRunScanner.setOnClickListener(this);
			this.m_adapter = new ProductAdapter(this, R.layout.productrow_list, m_orders, this.shop, this.currency);
		}
		this.textViewPriceTotal = (TextView) ProductListActivity.this.findViewById(R.id.products_price);
		this.bShoppingList = (Button) this.findViewById(R.id.bn_shopping_list);
		this.bShoppingList.setOnClickListener(this);		
		               
        TextView textTitle = (TextView) this.findViewById(R.id.text_title_top);
		textTitle.setText(this.shop.getTitle());
		               
        setListAdapter(this.m_adapter);        
    	if(!this.shop.isFinished() && isAllProductSelected()){					
			createDialogFinishShop();			
		}	
    	refreshListView();
        ListView lv = getListView();
        registerForContextMenu(lv);
        lv.setOnItemClickListener(this);       
        // Refresh shopping list after close activity eg. number of items
        setResult(ShopListActivity.ACTION_REFRESH);		
        
        try{
        	// Create product from organization
        	Product product = (Product) bundle.getSerializable("product");
        	if(product != null){
        		onActivityResult(REQUEST_DETAIL, ACTION_ADD, intent);
        	}
        }catch(Exception e){
        	System.err.println(e);
        }

	}    	
	
	private void getProducts(){
        try{                   	
        	this.m_orders = new ArrayList<Product>();			
        	this.m_orders.addAll(FactoryDAO.getProductDAO(this).getProducts(this.shop.getId()));
            Log.i("ARRAY", ""+ m_orders.size());
          } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		if(!this.shop.isFinished()){
			if(menuInfo instanceof AdapterContextMenuInfo){
				AdapterContextMenuInfo adapterView = (AdapterContextMenuInfo) menuInfo;
				Product product = m_adapter.getItem(adapterView.position);
				if(!product.isSelected()){
					MenuInflater inflater = getMenuInflater();
					inflater.inflate(R.menu.productlist_context_menu_selected, menu);
				}else{
					MenuInflater inflater = getMenuInflater();
					inflater.inflate(R.menu.productlist_context_menu_unselected, menu);
				}
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Product product = null;
		switch (item.getItemId()) {
	        case R.id.context_edit:	            
	        	product = m_adapter.getItem((int)info.id);
	        	Intent intent = new Intent();
				intent.putExtra("product", product);
				intent.putExtra("shop", shop);
				intent.setClass(this, ProductDetailActivity.class);
				startActivityForResult(intent, REQUEST_DETAIL);
	            return true;
	        case R.id.context_delete:
	        	product = m_adapter.getItem((int)info.id);
	        	confirmPopupWindowsProductDelete(product, ProductListActivity.this);				
	            return true;
	        case R.id.context_revert:
	        	product = m_adapter.getItem((int)info.id);	        	
	        	confirmPopupWindowsProductUnselect(product, ProductListActivity.this);
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		final boolean scanAvailable = Utils.isIntentAvailable(this,"com.google.zxing.client.android.SCAN");
//	    MenuItem item = menu.findItem(R.id.option_scan);
//	    item.setEnabled(scanAvailable);
//	    return super.onPrepareOptionsMenu(menu);
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(!this.shop.isFinished()){
			MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.productlist_option_menu, menu);
		}
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
	        case R.id.option_create:	        	
	        	intent = new Intent();
				intent.setClass(this, ProductDetailActivity.class);
				startActivityForResult(intent, REQUEST_DETAIL);				
	        	return true;
	        case R.id.option_delete:        	
	        	confirmPopupWindowsShopListDelete(this.shop, this);
	        	return true;
	        case R.id.option_send:
	        	intent = new Intent();	        	
	    		intent.putExtra("shop", shop);
	        	setResult(ShopListActivity.ACTION_SMS, intent);
	        	finish();
	        	return true;
	        case R.id.option_scan:        	
	        	if(Utils.isOnline(this)){
		        	IntentIntegrator integrator = new IntentIntegrator(this);
		        	integrator.initiateScan();
	        	}else{
	        		new AlertDialog.Builder(this)
	        		.setMessage(String.format(getResources().getString(R.string.dialog_no_scan), shop.getTitle()))
	        		.setCancelable(true)
	        		.setNegativeButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int id) {
	        				dialog.cancel();
	        			}
	        		}).show();
	        	}
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	private void confirmPopupWindowsShopListDelete(final Shop shop, Context context){
		new AlertDialog.Builder(context)
		.setMessage(String.format(getResources().getString(R.string.question_shop_delete), shop.getTitle()))
		.setCancelable(false)
		.setPositiveButton(R.string.button_shop_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent();	        	
	        	intent.putExtra("shop", shop);
	        	setResult(ShopListActivity.ACTION_DELETE, intent);
	        	finish();
			}
		})
		.setNegativeButton(R.string.button_shop_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}).show();		
	}
	
	private void confirmPopupWindowsProductDelete(final Product product, Context context){
		new AlertDialog.Builder(context)
		.setMessage(String.format(getResources().getString(R.string.question_product_delete), shop.getTitle()))
		.setCancelable(false)
		.setPositiveButton(R.string.button_shop_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent();	        	
	        	intent.putExtra("product", product);
	        	onActivityResult(REQUEST_CURRENT, ACTION_DELETE, intent);
	        	dialog.cancel();
			}
		})
		.setNegativeButton(R.string.button_shop_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}).show();		
	}
	
	private void confirmPopupWindowsProductUnselect(final Product product, Context context){
		new AlertDialog.Builder(context)
		.setMessage(String.format(getResources().getString(R.string.question_product_revert), product.getName()))
		.setCancelable(false)
		.setPositiveButton(R.string.button_shop_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent();	        	
				product.setSelected(false);
				intent.putExtra("product", product);
	        	onActivityResult(REQUEST_CURRENT, ACTION_REVERT, intent);
	        	dialog.cancel();
			}
		})
		.setNegativeButton(R.string.button_shop_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}).show();		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CURRENT){
			Product product = null;
			switch(resultCode){
				case ACTION_DELETE:
					product = (Product) data.getExtras().getSerializable("product");
					FactoryDAO.getProductDAO(this).delete(product);	        	
					Utils.refreshListView(this.viewOrders);
					break;
				case ACTION_REVERT:
					product = (Product) data.getExtras().getSerializable("product");
					FactoryDAO.getProductDAO(this).update(product);	        	
					Utils.refreshListView(this.viewOrders);
					break;
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
						Intent intent = new Intent();					
						intent.putExtra("SCAN_CONTENT", upc);
					    intent.putExtra("SCAN_FORMAT", format);					
						intent.setClass(this, ProductScanActivity.class);
						startActivityForResult(intent, REQUEST_SCAN);					
					}
				}
			}
		}else if(requestCode == REQUEST_SCAN){
			Product product = null;
			switch(resultCode){
				case ACTION_ADD:
					product = (Product) data.getExtras().getSerializable("product");
					product.setShop(shop);
					FactoryDAO.getProductDAO(this).insert(product);	        	
					Utils.refreshListView(this.viewOrders);
					break;
			}
		}else if(requestCode == REQUEST_DETAIL){
			Product product = null;
			switch(resultCode){
				case ACTION_ADD:
					product = (Product) data.getExtras().getSerializable("product");
					product.setShop(shop);
					FactoryDAO.getProductDAO(this).insert(product);	        	
					Utils.refreshListView(this.viewOrders);
					break;
				case ACTION_EDIT:
					product = (Product) data.getExtras().getSerializable("product");
					product.setShop(shop);
					FactoryDAO.getProductDAO(this).update(product);
					Utils.refreshListView(this.viewOrders);										        		
					break;
				case ACTION_SELECT:
					product = (Product) data.getExtras().getSerializable("product");
					product.setShop(shop);
					FactoryDAO.getProductDAO(this).update(product);
					if(isAllProductSelected()){	createDialogFinishShop(); }					
					Utils.refreshListView(this.viewOrders);	
					break;
			}
		}else if(requestCode == REQUEST_SHOP){
			Product product = null;
			switch(resultCode){
				case ACTION_ADD_ITEM:
					product = (Product) data.getExtras().getSerializable("product");
					product.setShop(shop);
					FactoryDAO.getProductDAO(this).insert(product);	        	
					Utils.refreshListView(this.viewOrders);
					break;
			}
		}
		
	}
	
	private boolean isAllProductSelected(){		
		List<Product> products = FactoryDAO.getProductDAO(this)
				.getProducts(this.shop.getId());
		if(products.size() > 0){
			Iterator<Product> iter = products.iterator();
			while(iter.hasNext()){
				Product prod = iter.next();
				if(!prod.isSelected()){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {		
		Product product = m_adapter.getItem(position);
		if(!ProductListActivity.this.shop.isFinished()){			
			if(!product.isSelected()){				
				Intent intent = new Intent();	        	
				product.setSelected(true);
				intent.putExtra("product", product);
				intent.putExtra("shop", shop);
	        	onActivityResult(REQUEST_DETAIL, ACTION_SELECT, intent);				
			}else{
				Intent intent = new Intent();
				intent.putExtra("product", product);
				intent.putExtra("shop", shop);
				intent.putExtra("isEditable", false);
				intent.setClass(this, ProductDetailActivity.class);
				startActivityForResult(intent, REQUEST_DETAIL);				
			}		
		}else{			
			Intent intent = new Intent();
			intent.putExtra("product", product);
			intent.putExtra("shop", shop);
			intent.putExtra("isEditable", false);
			intent.setClass(this, ProductDetailActivity.class);
			startActivityForResult(intent, REQUEST_DETAIL);			
		}		
	}
    
	public void refreshListView(){
		Utils.refreshListView(ProductListActivity.this.viewOrders);
	}
	
	public void createDialogFinishShop(){
		new AlertDialog.Builder(ProductListActivity.this)
			.setMessage(getResources().getString(R.string.dialog_shop_finish))
		    .setCancelable(false)
		    .setPositiveButton(R.string.button_shop_yes, new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) {
		    		Intent intent = new Intent();	        	
		    		intent.putExtra("shop", shop);
		    		setResult(ShopListActivity.ACTION_FINISH, intent);
		    		ProductListActivity.this.finish();
		    	}
		    })
		    .setNegativeButton(R.string.button_shop_no, new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) {
		    		dialog.cancel();
		    	}
	    }).show();
	}	
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch(view.getId()){
			case R.id.bn_shopping_list :
				intent = new Intent();	        	
	    		intent.putExtra("shop", shop);
				setResult(ShopListActivity.ACTION_REFRESH, intent);
				finish();
				break;
			case R.id.bn_add_item :
				intent = new Intent();
				intent.putExtra("shop", shop);
				intent.setClass(this, ProductDetailActivity.class);
				startActivityForResult(intent, REQUEST_DETAIL);
				break;
			case R.id.bn_shops :
				intent = new Intent();
				intent.putExtra("shop", shop);
				intent.setClass(this, OrganizationListActivity.class);
				startActivityForResult(intent, REQUEST_SHOP);
				break;
			case R.id.bn_scan :
				if(Utils.isOnline(this)){
		        	IntentIntegrator integrator = new IntentIntegrator(this);
		        	integrator.initiateScan();
	        	}else{
	        		new AlertDialog.Builder(this)
	        		.setMessage(String.format(getResources().getString(R.string.dialog_no_scan), shop.getTitle()))
	        		.setCancelable(true)
	        		.setNegativeButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int id) {
	        				dialog.cancel();
	        			}
	        		}).show();
	        	}
				break;
		}		
	}
	
}