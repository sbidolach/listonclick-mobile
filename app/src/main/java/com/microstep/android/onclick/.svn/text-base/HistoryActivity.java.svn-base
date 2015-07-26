package com.microstep.android.onclick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.microstep.android.onclick.adapter.HistoryAdapter;
import com.microstep.android.onclick.dao.FactoryDAO;
import com.microstep.android.onclick.db.DatabaseShop;
import com.microstep.android.onclick.listener.TabEventListener;
import com.microstep.android.onclick.model.DeviceClass;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
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
import android.app.ProgressDialog;
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
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class HistoryActivity extends ListActivity implements OnClickListener, TabEventListener, OnSharedPreferenceChangeListener {

	public final static int REQUEST_PRODUCT_LIST = 1;
	public final static int ACTION_DELETE = 1;
	
	private ArrayList<Shop> m_orders = null;
	private HistoryAdapter m_adapter = null;
	private String currency = null;
	
	private Button bSynch = null;
	private Button bMenu = null;
	private Spinner sPeriod = null;
	private ImageButton bSearch = null;
	
	private Runnable viewOrders = new Runnable() {
		@Override
		public void run() {
			getHistory();
		}
	};

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			double price = 0.0;
			m_adapter.clear();
			if (m_orders != null && m_orders.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < m_orders.size(); i++) {
					Shop s = m_orders.get(i);
					m_adapter.add(s);
					for(Product p : s.getProducts()){
						if(p.getPrice() != null && p.getCount() != null){
	                		if(p.isSelected()){
	                			price += (p.getCount() * p.getPrice());
	                		}
	                	}
					}
				}
			}
			m_adapter.notifyDataSetChanged();
			
			TextView pt = (TextView) HistoryActivity.this.findViewById(R.id.history_price);									
			pt.setText(String.format(getResources().getString(R.string.label_product_amount_total), price, currency));
			
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.historylist);

		this.m_orders = new ArrayList<Shop>();
		this.m_adapter = new HistoryAdapter(this, R.layout.historyrow, m_orders);
		setListAdapter(this.m_adapter);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);        
        this.currency = prefs.getString("currencyPref", Utils.getDefaulCurrency(this));
        
		ListView lv = getListView();	
		registerForContextMenu(lv);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent();
				intent.putExtra("shop", m_adapter.getItem(position));
				intent.setClass(HistoryActivity.this, ProductListActivity.class);
				startActivityForResult(intent, REQUEST_PRODUCT_LIST);

			}

		});

		this.bMenu = (Button) this.findViewById(R.id.bn_menu);
		this.bMenu.setOnClickListener(this);				
		this.bSynch = (Button) this.findViewById(R.id.bn_synch);
		this.bSynch.setOnClickListener(this);		
		this.sPeriod = (Spinner) this.findViewById(R.id.spinner_period);
		this.bSearch = (ImageButton) this.findViewById(R.id.bn_search);
		this.bSearch.setOnClickListener(this);
		
		Activity act = getParent();
		if(act instanceof MainActivity){
			MainActivity m = (MainActivity) act;
			m.addListener(this);
		}
		
		refreshListView();
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
	
	public int getFirstDay(Date d) throws Exception  {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(d);  
        calendar.set(Calendar.DAY_OF_MONTH, 1);  
        Date dddd = calendar.getTime();  
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");  
        System.out.println(sdf1.format(dddd));
        return (int)(dddd.getTime() / 1000);  
    }
	
	public int getDay(Date d, int days) throws Exception  {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(d);  
        calendar.add(Calendar.DATE, days); 
        Date dddd = calendar.getTime();  
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");  
        System.out.println(sdf1.format(dddd));
        return (int)(dddd.getTime() / 1000);  
    }
	
	private void getHistory() {		
		try {			
			this.m_orders = new ArrayList<Shop>();			
			int position = this.sPeriod.getSelectedItemPosition();
			List<Shop> shops = new ArrayList<Shop>();
			switch(position){
				case 0:
					shops = FactoryDAO.getShopDAO(this).getHistory(
							true, DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, null);
					break;
				case 1:					
					shops = FactoryDAO.getShopDAO(this).getHistory(
							true, DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, 
							getFirstDay(new Date()));
					break;
				case 2:
					shops = FactoryDAO.getShopDAO(this).getHistory(
							true, DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, 
							getDay(new Date(), -30));
					break;
				case 3:
					shops = FactoryDAO.getShopDAO(this).getHistory(
							true, DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, 
							getDay(new Date(), -60));
					break;
				default:
					shops = FactoryDAO.getShopDAO(this).getHistory(
							true, DatabaseShop.Shops.COLUMN_NAME_MODIFICATION_DATE, null);
			}
			this.m_orders.addAll(shops);		
			Log.i("ARRAY", "" + m_orders.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		runOnUiThread(returnRes);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Shop shop = null;
		switch (item.getItemId()) {
	        case R.id.context_delete:
	        	shop = m_adapter.getItem((int)info.id);
	        	confirmPopupWindowsDelete(shop, HistoryActivity.this);				
	            return true;
	        case R.id.context_open:
	        	Intent intent = new Intent();
				intent.putExtra("shop", m_adapter.getItem((int)info.id));
				intent.setClass(HistoryActivity.this, ProductListActivity.class);
				startActivityForResult(intent, REQUEST_PRODUCT_LIST);				
	            return true;
	        default:
	            return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.history_context_menu, menu);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);		
		if(requestCode == REQUEST_PRODUCT_LIST){
			Shop shop = null;
			switch(resultCode){
				case ACTION_DELETE:
					shop = (Shop) data.getExtras().getSerializable("shop");
					FactoryDAO.getShopDAO(this).delete(shop);	        	
					refreshListView();
					break;
			}			
		}
	}

	@Override
	public void handleTabEvent() {
		refreshListView();		
	}
	
	public void refreshListView(){
		Utils.refreshListView(viewOrders);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.history_option_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//	        case R.id.option_settings:
//	        	Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
//	        	startActivity(settingsActivity);
//	        	return true;
//	        case R.id.option_info:
//	        	Intent infoActivity = new Intent(getBaseContext(), InfoActivity.class);
//	        	startActivity(infoActivity);
//	            return true;
//	        case R.id.option_account:
//	        	Intent accountActivity = new Intent(getBaseContext(), AccountActivity.class);
//        		startActivity(accountActivity);
//	        	return true;
	        case R.id.option_synch:       	       		        		
	        	synchronizeHistoryData();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.currency = prefs.getString("currencyPref", Utils.getDefaulCurrency(this));	
        this.m_adapter.setCurrency(this.currency);
        refreshListView();
	}
	
	private void synchronizeHistoryData(){
		
		final ProgressDialog progressDialog = new ProgressDialog(HistoryActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage(getResources().getText(R.string.dialog_synchronize));
		progressDialog.setCancelable(false); 
		
    	// Synchronization data with onClick server
		new Thread(new Runnable() {
            public void run() {    
            	if(Utils.isOnline(HistoryActivity.this)){
					Login login = SecurityContextHolder.getSecurityContext().getLogin();
					DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
					if(login != null && !"-1".equals(login.getUserId())){
	            		List<Shop> shops = FactoryDAO.getShopDAO(HistoryActivity.this).feachSynch();				            		
	            		if(shops.size() > 0){					            						            			
	            			progressDialog.setMax(shops.size());
		            		
		            		HistoryActivity.this.runOnUiThread(new Runnable() {	
		            			@Override
		            			public void run() {
		            				progressDialog.show();
		            			}
		            		});
		            		
		            		int i = 1;
			            	for(Shop shop:shops){
			            		if(!shop.isSynch()){
			            			try{
										NoteService noteService = WebServiceFactory.getNoteService();
										StatusXml success = noteService.addNote(
												login.getUserId(), shop, login.getAuthToken(), dc.getLanguage());
										progressDialog.setProgress(i++);
										if(success.getCode() != 0){
											//TODO note can not be add to DB server
										}else{
											shop.setSynch(true);
											FactoryDAO.getShopDAO(HistoryActivity.this).update(shop);
										}
									}catch(Exception e){
										e.printStackTrace();
									}
			            		}
			            	}
			            	progressDialog.dismiss();
			            	refreshListView();
						}else{																				      
							HistoryActivity.this.runOnUiThread(new Runnable() {											
								@Override
								public void run() {
									AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
									builder.setMessage(R.string.dialog_no_synchronize)
								       .setCancelable(false)
								       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
								           public void onClick(DialogInterface dialog, int id) {
								        	   dialog.cancel();
								           }
								    }).show();
								}
							});								
						}
					}else{
						HistoryActivity.this.runOnUiThread(new Runnable() {		
							@Override
							public void run() {								
								AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
								builder.setMessage(R.string.dialog_error_network)
							       .setCancelable(false)
							       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							        	   dialog.cancel();
							           }
							    }).show();
							}
						});
					}
            	}else{
            		HistoryActivity.this.runOnUiThread(new Runnable() {		
						@Override
						public void run() {								
		            		AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
							builder.setMessage(R.string.dialog_no_network)
						       .setCancelable(false)
						       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						        	   dialog.cancel();
						           }
						    }).show();
						}
					});
            	}
            }
        }).start();	
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bn_synch :
				synchronizeHistoryData();
				break;
			case R.id.bn_menu :
				finish();
				break;
			case R.id.bn_search:
				refreshListView();
				break;
		}	
	}
	
}
