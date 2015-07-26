package com.microstep.android.onclick;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;

import com.microstep.android.onclick.adapter.IconMenuAdapter;
import com.microstep.android.onclick.dao.FactoryDAO;
import com.microstep.android.onclick.db.DatabaseHelper;
import com.microstep.android.onclick.listener.TabEventListener;
import com.microstep.android.onclick.location.PhoneLocation;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.DeviceClass;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.rest.NoteService;
import com.microstep.android.onclick.rest.WebServiceFactory;
import com.microstep.android.onclick.rest.model.CategoryXml;
import com.microstep.android.onclick.rest.model.Login;
import com.microstep.android.onclick.rest.model.OnClickXml;
import com.microstep.android.onclick.security.SecurityContextHolder;
import com.microstep.android.onclick.util.Language;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends Activity implements OnSharedPreferenceChangeListener {

	public final static int REQUEST_ORGANIZATION_LIST = 1;
	public final static int REQUEST_CREATE_SHOP = 2;
	
	public final static int ACTION_ADD_BASKET = 1;
	
	private ArrayList<TabEventListener> tabListeners = new ArrayList<TabEventListener>();
	
	private LicenseCheckerCallback mLicenseCheckerCallback;
    private LicenseChecker mChecker;
    // A handler on the UI thread.
    private Handler mHandler;
    
    private static String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuLCZNFPKoUH0fOMgM6xneewv8AEt4Me5gk2iFnUTB6kgrAIcJ9QS9S6hiUwQaPp7zq9bh+tQQQ61UTks0dQ/kNm+2fLJ8KHC4C/jfyI84WFW4jU0Na+iSt6QIyY3xaItOsUNPfHCfRxQVjEwJQaZ33Uozhmhe7RAh9r6Nwq+7NLmKdvuHe+ycqvw9RLz6rnzn3yZL0dbqSYEY0N8TT01Uy4Z5fDYtkXTbGrkDoQ6Zfskbtwa6QWjZSPDcANQ+WvOP8K8xJoFt8JHYaHuYgS1idggwo1wkIYgdvxG3v24QN1TE2SLL4Ora0JlCoGypagv3G2uDeNmbwZFjQgf8nPJNQIDAQAB";    
    public static String SERVER_HOST_REST_API = "m.listonclick.com";
    //public static String SERVER_HOST_REST_API = "192.168.1.4";    
    public static int SERVER_PORT_REST_API = 80;
    public static String SERVER_PATH_REST_API = "/rest/";
    //public static String SERVER_PATH_REST_API = "/webonclick/rest/";
    //public static String SERVER_PATH_APPLICATION = "/webonclick/";
    //public static String DEVICE_TYPE = "ANDROID";
    //public static String ANDROID_UNIQUE_DEVICE_ID;    
    //public Login login;
	
    // Generate your own 20 random bytes, and put them here.
    private static final byte[] SALT = new byte[] {
        -42, 65, 30, -123, -103, -57, 75, -64, 51, 88, -95, -45, 77, -117, -38, -113, -13, 32, -64, 89
    };
    
    protected Dialog onCreateDialog(int id) {
        final boolean bRetry = id == 1;
        return new AlertDialog.Builder(this)
            .setTitle(R.string.unlicensed_dialog_title)
            .setMessage(bRetry ? R.string.unlicensed_dialog_retry_body : R.string.unlicensed_dialog_body)
            .setPositiveButton(bRetry ? R.string.retry_button : R.string.buy_button, new DialogInterface.OnClickListener() {
                boolean mRetry = bRetry;
                public void onClick(DialogInterface dialog, int which) {
                    if ( mRetry ) {
                        doCheck();
                    } else {
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "http://market.android.com/details?id=" + getPackageName()));
                            startActivity(marketIntent);                        
                    }
                }
            })
            .setNegativeButton(R.string.quit_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).create();
    }
    
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
	    setContentView(R.layout.main_menu_icon);
        
	    // Initial Database
	    DatabaseHelper.getSQLInstance(this);
	    
	    // Initial phone GPS location
	    PhoneLocation phoneLocation =  new PhoneLocation();
	    phoneLocation.init(this);	    
	    
	    if(SecurityContextHolder.getSecurityContext().getDeviceClass() == null){	    
		    DeviceClass deviceClass = new DeviceClass();
		    deviceClass.setId("3");
		    String locale = getResources().getConfiguration().locale.getCountry();
		    deviceClass.setLanguage(locale);
		    deviceClass.setType("ANDROID");
		    deviceClass.setUniqueID("Android."+Secure.getString(getContentResolver(), Secure.ANDROID_ID));
		    
			try {
				String versionName = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
				deviceClass.setVersion(versionName);
			} catch (NameNotFoundException e) {
			}
		    
		    SecurityContextHolder.getSecurityContext().setDeviceClass(deviceClass);		    
	    }
	    
	    mHandler = new Handler();
        // Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        // Library calls this when it's done.
        mLicenseCheckerCallback = new OnClickLicenseCheckerCallback();
        // Construct the LicenseChecker with a policy.
        mChecker = new LicenseChecker(this, 
        		new ServerManagedPolicy(this,
        				new AESObfuscator(SALT, getPackageName(), deviceId)),
        		BASE64_PUBLIC_KEY);    
        
        //doCheck();  // Check licence                      

	    Resources res = getResources(); // Resource object to get Drawables
//	    TabHost tabHost = getTabHost();  // The activity TabHost
//	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
//	    Intent intent;  // Reusable Intent for each tab
//
//	    // Create an Intent to launch an Activity for the tab (to be reused)
//	    intent = new Intent().setClass(this, ShopListActivity.class);
//	    spec = tabHost
//	    		.newTabSpec("shoplist")
//	    		.setIndicator(getResources().getText(R.string.tab_shopping_list), 
//	    				res.getDrawable(R.drawable.ic_tab_list))
//	    		.setContent(intent);
//	    tabHost.addTab(spec);
//
//	    // Do the same for the other tabs
//	    intent = new Intent().setClass(this, HistoryActivity.class);
//	    spec = tabHost
//	    		.newTabSpec("history")
//	    		.setIndicator(getResources().getText(R.string.tab_history), 
//	    				res.getDrawable(R.drawable.ic_tab_history))
//	    		.setContent(intent);
//	    tabHost.addTab(spec);
//	    tabHost.setCurrentTab(0);	   
	    
	    GridView grid_main = (GridView)findViewById(R.id.main_menu_grid);
	    grid_main.setAdapter(new IconMenuAdapter(this));	    
	    // Implement On Item click listener  
	    grid_main.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {				
				Intent intent = null;
				switch(position){
					case 0: 
						intent = new Intent();
						intent.setClass(MainActivity.this, ShopListActivity.class);
						startActivity(intent);
						break;
					case 1: 
						intent = new Intent();
						intent.setClass(MainActivity.this, OrganizationListActivity.class);
						startActivityForResult(intent, REQUEST_ORGANIZATION_LIST);
						break;
					case 2: 
						intent = new Intent();
						intent.setClass(MainActivity.this, AccountActivity.class);
						startActivity(intent);
						break;
					case 3: 
						intent = new Intent();
						intent.setClass(MainActivity.this, HistoryActivity.class);
						startActivity(intent);
						break;					
					case 4: 
						intent = new Intent();
						intent.setClass(MainActivity.this, InfoActivity.class);
						startActivity(intent);
						break;
					case 5: 
						intent = new Intent();
						intent.setClass(MainActivity.this, SettingsActivity.class);
						startActivity(intent);
						break;
				}				
			}  
        }); 
	    
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
	    
        // Register device on the server onClick        
    	new Thread(new Runnable() {
            public void run() {                                    	
            	try{
            		if(Utils.isOnline(MainActivity.this)){
        	        	NoteService noteService = WebServiceFactory.getNoteService();
        	        	DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
        	        	Date date = FactoryDAO.getCategoryDAO(MainActivity.this).getLastDateUpdate(Language.value(dc.getLanguage()));        	        	
        	        	OnClickXml oXml = noteService.loginDeviceUpdateCategory(dc.getUniqueID(), date, dc.getLanguage());
        	        	Login login = oXml.getDataXml().getLogin().get(0);
        	        	SecurityContextHolder.getSecurityContext().setLogin(login);
        	        	List<CategoryXml> categories = oXml.getDataXml().getCategoryXml();
        	        	if(categories.size() > 0){
        	        		FactoryDAO.getCategoryDAO(MainActivity.this).deleteAll();
        	        		for(CategoryXml xml: categories){
        	        			Category cat = noteService.getXmlBeanFactory().getCategoryFromCategoryXml(xml);
        	        			FactoryDAO.getCategoryDAO(MainActivity.this).insert(cat);
        	        		}
        	        	}
            		}
            	}catch(Exception e){
            		e.printStackTrace();
            		Login login = new Login();
                    login.setUserId("-1");
                    SecurityContextHolder.getSecurityContext().setLogin(login);
            	}
            }
        }).start();

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent = null;
		if(requestCode == REQUEST_ORGANIZATION_LIST){
			if(resultCode == ACTION_ADD_BASKET){
				Product product = (Product) data.getExtras().getSerializable("product");
				intent = new Intent();
				intent.setClass(this, ShopListActivity.class);
				intent.putExtra("product", product);
				startActivityForResult(intent, REQUEST_CREATE_SHOP);
			}
		}
	}
	
	private void doCheck() {
        mChecker.checkAccess(mLicenseCheckerCallback);
    }
	
	public void addListener(TabEventListener listener) {
		tabListeners.add(listener);
    }
	
    public void removeListener(TabEventListener listener) {
    	tabListeners.remove(listener);
    }
    
    public ArrayList<TabEventListener> getTabListeners() {
		return tabListeners;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {		
	}		
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mChecker.onDestroy();
	}
    
    private void displayDialog(final boolean showRetry) {
        mHandler.post(new Runnable() {
            public void run() {
            	setProgressBarIndeterminateVisibility(false);
            	showDialog(showRetry ? 1 : 0);            	
            }
        });
    }
    
    private void displayResult(final String result) {
    	new AlertDialog.Builder(this)
	        .setTitle(R.string.unlicensed_dialog_title)
	        .setMessage("ERROR")
	        .setPositiveButton(result, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        }).create();
    }
    
	private class OnClickLicenseCheckerCallback implements LicenseCheckerCallback {
        public void allow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
        }

        public void dontAllow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should not allow access. In most cases, the app should assume
            // the user has access unless it encounters this. If it does,
            // the app should inform the user of their unlicensed ways
            // and then either shut down the app or limit the user to a
            // restricted set of features.
            // In this example, we show a dialog that takes the user to Market.
            // If the reason for the lack of license is that the service is
            // unavailable or there is another problem, we display a
            // retry button on the dialog and a different message.
            displayDialog(policyReason == Policy.RETRY);
        }

        public void applicationError(int errorCode) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.
            String result = String.format(getString(R.string.application_error), errorCode);
            displayResult(result);
        }
    }

}
