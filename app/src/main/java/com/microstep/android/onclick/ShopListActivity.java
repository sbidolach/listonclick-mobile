package com.microstep.android.onclick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.microstep.android.onclick.adapter.ShopAdapter;
import com.microstep.android.onclick.dao.FactoryDAO;
import com.microstep.android.onclick.db.DatabaseShop;
import com.microstep.android.onclick.listener.TabEventListener;
import com.microstep.android.onclick.model.DeviceClass;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.model.ShopType;
import com.microstep.android.onclick.rest.NoteService;
import com.microstep.android.onclick.rest.WebServiceFactory;
import com.microstep.android.onclick.rest.model.Login;
import com.microstep.android.onclick.rest.model.StatusXml;
import com.microstep.android.onclick.security.SecurityContextHolder;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ShopListActivity extends ListActivity implements OnClickListener, TabEventListener, OnSharedPreferenceChangeListener  {

	public final static int REQUEST_PRODUCT_LIST = 1;	
	public final static int REQUEST_CONTACT_LIST = 2;
	public final static int REQUEST_SMS = 3;
	public final static int ACTION_DELETE 	= 1;
	public final static int ACTION_FINISH 	= 2;
	public final static int ACTION_REFRESH 	= 3;
	public final static int ACTION_SEND 	= 4;
	public final static int ACTION_SMS 		= 5;
	
	private ArrayList<Shop> m_orders = null;
	private ShopAdapter m_adapter = null;
	private Button bAddShopList = null;
	private Button bMenu = null;
	
	private Shop selectedShop = null;

	private Runnable viewOrders = new Runnable() {
		@Override
		public void run() {
			getShops();
		}
	};

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			m_adapter.clear();
			if (m_orders != null && m_orders.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < m_orders.size(); i++) {
					m_adapter.add(m_orders.get(i));
				}
			}
			m_adapter.notifyDataSetChanged();
		}
	};	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shoplist);		
		
		this.m_orders = new ArrayList<Shop>();
		this.m_adapter = new ShopAdapter(this, R.layout.shoprow, m_orders);
		setListAdapter(this.m_adapter);

		ListView lv = getListView();	
		registerForContextMenu(lv);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent();
				intent.putExtra("shop", m_adapter.getItem(position));
				intent.setClass(ShopListActivity.this, ProductListActivity.class);
				startActivityForResult(intent, REQUEST_PRODUCT_LIST);

			}

		});

		this.bAddShopList = (Button) this.findViewById(R.id.bn_add);
		this.bAddShopList.setOnClickListener(this);		
		this.bMenu = (Button) this.findViewById(R.id.bn_menu);
		this.bMenu.setOnClickListener(this);

		Activity act = getParent();
		if(act instanceof MainActivity){
			MainActivity m = (MainActivity) act;
			m.addListener(this);
		}
		
		refreshListView();
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		pref.registerOnSharedPreferenceChangeListener(this);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			Product product = (Product) bundle.getSerializable("product");
			if(product != null){
				createPopupWindowShopTitle(null, product);
			}
		}

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Activity act = getParent();
		if(act instanceof MainActivity){
			MainActivity m = (MainActivity) act;
			m.removeListener(this);
		}
		
	}

	private void getShops() {		
		try {			
			this.m_orders = new ArrayList<Shop>();			
			this.m_orders.addAll(FactoryDAO.getShopDAO(this)
					.feachAll(false, DatabaseShop.Shops.COLUMN_NAME_CREATE_DATE));		
			Log.i("ARRAY", "" + m_orders.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	@Override
	public void onClick(View v) {		
		switch(v.getId()){
			case R.id.bn_add :
				createPopupWindowShopTitle(null, null);
				break;
			case R.id.bn_menu :
				finish();
				break;
		}				
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.shoplist_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Shop shop = null;
		Intent intent = null;
		switch (item.getItemId()) {
	        case R.id.context_edit:	            
	        	shop = m_adapter.getItem((int)info.id);
	        	createPopupWindowShopTitle(shop, null);
	            return true;
	        case R.id.context_delete:
	        	shop = m_adapter.getItem((int)info.id);
	        	confirmPopupWindowsDelete(shop, ShopListActivity.this);				
	            return true;
	        case R.id.context_open:
	        	intent = new Intent();
				intent.putExtra("shop", m_adapter.getItem((int)info.id));
				intent.setClass(ShopListActivity.this, ProductListActivity.class);
				startActivityForResult(intent, REQUEST_PRODUCT_LIST);				
	            return true;
	        case R.id.context_send:
	        	this.selectedShop = m_adapter.getItem((int)info.id);	        	
	        	validateDataBeforSendSMSwithList(this.selectedShop);	        			
	            return true;    	            
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.shoplist_option_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.option_create:
	        	createPopupWindowShopTitle(null, null);
	            return true;
//	        case R.id.option_info:
//	        	Intent infoActivity = new Intent(getBaseContext(), InfoActivity.class);
//	        	startActivity(infoActivity);
//	            return true;
//	        case R.id.option_settings:
//	        	Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
//	        	startActivity(settingsActivity);
//	        	return true;
//	        case R.id.option_account:
//	        	Intent accountActivity = new Intent(getBaseContext(), AccountActivity.class);
//        		startActivity(accountActivity);
//	        	return true;	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void validateDataBeforSendSMSwithList(Shop shop){
		
		if(shop.getType() != ShopType.CREATE){
    		new AlertDialog.Builder(this)
  		      .setMessage(getResources().getString(R.string.dialog_send_sms_phone))
  		      .setTitle(getResources().getString(R.string.label_send_list))
  		      .setCancelable(true)
  		      .setNeutralButton(android.R.string.cancel,
  		         new DialogInterface.OnClickListener() {
  		         public void onClick(DialogInterface dialog, int whichButton){
  		        	 dialog.cancel();
  		         }})
  		      .show();
    		return;
    	}
    	
    	List<Product> list = shop.getProducts();
    	Iterator<Product> iter = list.iterator();
    	while(iter.hasNext()){
    		Product p = iter.next();
    		if(p.isSelected()){
    			new AlertDialog.Builder(this)
    		      .setMessage(getResources().getString(R.string.dialog_send_sms_product))
    		      .setTitle(getResources().getString(R.string.label_send_list))
    		      .setCancelable(true)
    		      .setNeutralButton(android.R.string.cancel,
    		         new DialogInterface.OnClickListener() {
    		         public void onClick(DialogInterface dialog, int whichButton){
    		        	 dialog.cancel();
    		         }})
    		      .show();       			
    			return;
    		}
    	}	       
    	
    	Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CONTACT_LIST);
	}
	
	private void createPopupWindowShopTitle(final Shop shop, final Product product){
		
		final EditText input = new EditText(ShopListActivity.this);		
		
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(20);
		input.setFilters(FilterArray);
		input.setPadding(5, 5, 5, 5);
		
		if(shop != null){
			input.setText(shop.getTitle());
		}
		
		new AlertDialog.Builder(ShopListActivity.this)
		.setTitle(getResources().getString(R.string.dialog_shop_create))
		.setView(input)
		.setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {				
				Editable value = input.getText();				
				// Create new shopping list
				if(shop == null){					
					Shop shop = new Shop();
					if("".equals(value.toString())){
						shop.setTitle(Utils.timesToString(System.currentTimeMillis()));
					}else{
						shop.setTitle(value.toString());
					}
					shop.setType(ShopType.CREATE);
					Long shopId = FactoryDAO.getShopDAO(ShopListActivity.this).insert(shop);
					refreshListView();
					setResult(RESULT_OK);					
					// Create product from organization for new shopping list
					if(product != null){
						Intent intent = new Intent();
						shop.setId(shopId);
						intent.putExtra("shop", shop);
						intent.putExtra("product", product);
						intent.setClass(ShopListActivity.this, ProductListActivity.class);
						startActivityForResult(intent, REQUEST_PRODUCT_LIST);	
					}
				// Edit existing shopping list
				}else{
					shop.setTitle(value.toString());
					FactoryDAO.getShopDAO(ShopListActivity.this).update(shop);
					refreshListView();
					setResult(RESULT_OK);
				}							
				
			}
		})
		.setNegativeButton(R.string.button_shop_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				setResult(RESULT_CANCELED);
			}
		}).show();		
	}
	
	public void confirmPopupWindowsDelete(final Shop shop, Context context){
		new AlertDialog.Builder(context)
		.setMessage(String.format(getResources().getString(R.string.question_shop_delete), shop.getTitle()))
		.setCancelable(false)
		.setPositiveButton(R.string.button_shop_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent();
				intent.putExtra("shop", shop);				
				onActivityResult(REQUEST_PRODUCT_LIST, ACTION_DELETE, intent);				
			}
		})
		.setNegativeButton(R.string.button_shop_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}).show();		
	}
	
	public void confirmPopupWindowsSendSMS(final Shop shop, final String number, final String name, Context context){
		new AlertDialog.Builder(context)
		.setMessage(String.format(getResources().getString(R.string.question_product_send_sms), shop.getTitle(), name))
		.setCancelable(false)
		.setPositiveButton(R.string.button_shop_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent();
				intent.putExtra("number", number);
				intent.putExtra("shop", shop);
				intent.setClass(ShopListActivity.this, SmsActivity.class);
				startActivityForResult(intent, REQUEST_SMS);				
			}
		})
		.setNegativeButton(R.string.button_shop_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}).show();		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);		
		if(requestCode == REQUEST_PRODUCT_LIST){
			Shop shop = null;
			switch(resultCode){
				case ACTION_DELETE:
					shop = (Shop) data.getExtras().getSerializable("shop");
					FactoryDAO.getShopDAO(this).delete(shop);	        	
					refreshListView();
					break;
				case ACTION_FINISH:					
					// Synchronization data with onClick server
					new Thread(new Runnable() {
			            public void run() {    
			            	Shop shop = (Shop) data.getExtras().getSerializable("shop");
							shop.setFinished(true);
							shop.setModified(Utils.currentTime());					
							FactoryDAO.getShopDAO(ShopListActivity.this).update(shop);
			            	if(Utils.isOnline(ShopListActivity.this)){
								Login login = SecurityContextHolder.getSecurityContext().getLogin();
								DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
								if(login != null && !"-1".equals(login.getUserId())){
									try{
										NoteService noteService = WebServiceFactory.getNoteService();
										Shop s = FactoryDAO.getShopDAO(ShopListActivity.this).getShop(shop.getId());
										StatusXml success = noteService.addNote(
												login.getUserId(), s, login.getAuthToken(), dc.getLanguage());
										if(success.getCode() != 0){
											//TODO note can not be add to DB server
										}else{
											s.setSynch(true);
											FactoryDAO.getShopDAO(ShopListActivity.this).update(s);
										}
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							}
			            	//refreshAllListView();
			            	refreshListView();
			            }
			        }).start();					
					break;
				case ACTION_REFRESH:
					//refreshAllListView();
					refreshListView();
					break;
				case ACTION_SMS:
					shop = (Shop) data.getExtras().getSerializable("shop");
					this.selectedShop = shop;	        	
		        	validateDataBeforSendSMSwithList(this.selectedShop);
					break;
			}
		}else if(requestCode == REQUEST_CONTACT_LIST){
			switch(resultCode){
				case Activity.RESULT_OK:
					Cursor cursor = managedQuery(data.getData(), null, null, null, null);
			        cursor.moveToNext();
			        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); 
			        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
			             Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
			            		 null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{contactId}, null);
			             pCur.moveToNext();
			             String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));			            	 
			             confirmPopupWindowsSendSMS(ShopListActivity.this.selectedShop, phone, name, ShopListActivity.this);
			             pCur.close();
			        }
			        cursor.close();			        
					break;
			}
		}else if(requestCode == REQUEST_SMS){
			switch (resultCode){
	            case Activity.RESULT_OK:
	                Toast.makeText(getBaseContext(), R.string.label_send_sms_delivered, Toast.LENGTH_SHORT).show();
	                refreshListView();
	                break;
	            case Activity.RESULT_CANCELED:
	                Toast.makeText(getBaseContext(), R.string.label_send_sms_no_delivered, Toast.LENGTH_SHORT).show();
	                refreshListView();
	                break;                        
			}
		}
	}	
	
	public void refreshListView(){
		Utils.refreshListView(viewOrders);
	}
	
	public void refreshAllListView(){
		Activity activity = getParent();
		if(activity instanceof MainActivity){
			MainActivity mActivity = (MainActivity) activity;
			ArrayList<TabEventListener> listeners = mActivity.getTabListeners();
			Iterator<TabEventListener> iter = listeners.iterator();
			while(iter.hasNext()){
				TabEventListener listener = iter.next();
				listener.handleTabEvent();
			}
		}
	}

	@Override
	public void handleTabEvent() {
		refreshListView();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}

}