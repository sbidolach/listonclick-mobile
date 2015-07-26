package com.microstep.android.onclick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author Seweryn Bidolach
 *
 */
public class OrganizationPdfViewActivity extends Activity implements OnClickListener {

	public final static int REQUEST_PDF_VIEW = 1;
	
	private transient File downloadDir = null;
	private transient ProgressBar progressBar = null;	
	private transient Organization organization = null;
    private transient Category category = null;
    private transient Shop shop = null;
    private transient Thread thread = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.organizationpdf);		
		this.progressBar = (ProgressBar) this.findViewById(R.id.progressPdfBar);		
		Bundle bundle = getIntent().getExtras();
        this.organization = (Organization) bundle.getSerializable("organization");
        this.shop = (Shop) bundle.getSerializable("shop");
        this.category = (Category) bundle.getSerializable("category");
        
        if(this.shop != null){
			TextView textItem = (TextView) this.findViewById(R.id.products_basket);
			TextView textPrice = (TextView) this.findViewById(R.id.products_price);
			textItem.setText(String.format(getResources().getString(R.string.label_product_amount_items), 
					this.shop.getProducts().size()));
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			textPrice.setText(Utils.getTotalPrice(this.shop.getProducts(), 
					prefs.getString("currencyPref", Utils.getDefaulCurrency(this)), getResources()));
		}else{
			LinearLayout bottomPanel = (LinearLayout) this.findViewById(R.id.product_bottom_panel);
			bottomPanel.setVisibility(View.GONE);	
		}
        
		downloadPdfFile();		
	}
	
	/**
	 * Method download PDF files to cache
	 */
	private void downloadPdfFile(){

		thread = new Thread(new Runnable() {
			public void run() {
				try{											
					
					// Find the dir to save cached images
					String sdState = android.os.Environment.getExternalStorageState();
					if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
						File sdDir = android.os.Environment.getExternalStorageDirectory();
						downloadDir = new File(sdDir, "onclick/download");
					} else {
						downloadDir = getCacheDir();
					}

					if (!downloadDir.exists()) {
						downloadDir.mkdirs();
					}
					
					URL u = new URL(OrganizationPdfViewActivity.this.category.getUrl());
					HttpURLConnection c = (HttpURLConnection) u.openConnection();
					c.setRequestMethod("GET");
					c.setDoOutput(true);
					c.connect();
					
					int urlLength = c.getContentLength();
					progressBar.setMax(urlLength);
					boolean isSave = true;
					
					removeOldFile();
					
					File df = new File(downloadDir, category.getEnd()+"_"+category.getId()+".pdf");
					if(df.exists()){
						Long diskLength = df.length();
						if(urlLength != diskLength.intValue()){
							isSave = saveFile(c, df);
						}else{
							progressBar.setProgress(urlLength);
						}
					}else{
						isSave = saveFile(c, df);
					}

					c.disconnect();										
					
					if(isSave == true){
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(df), "application/pdf");
						startActivityForResult(intent, REQUEST_PDF_VIEW);
					}

				}catch(Exception ex){
					OrganizationPdfViewActivity.this.runOnUiThread(new Runnable() {	
            			@Override
            			public void run() {
            				WebView mWebView = new WebView(OrganizationPdfViewActivity.this);
        				    mWebView.getSettings().setJavaScriptEnabled(true);
        				    mWebView.getSettings().setPluginsEnabled(true);
        				    mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+OrganizationPdfViewActivity.this.category.getUrl());
        				    setContentView(mWebView);
            			}
            		});										
				}
			}
		});
		
		thread.start();				
					
	}
	
	/**
	 * Method remove all PDF files from cache
	 */
	private void removeOldFile(){
		for (final File fileEntry : downloadDir.listFiles()) {
	        if (fileEntry.isFile()) {
	            String filname = fileEntry.getName();
	            try{
	            	String extension = filname.substring(filname.lastIndexOf("."), filname.length());
	            	if(".pdf".equals(extension)){
	            		String[] timestamp = filname.split("_");
	            		int timecurr = Utils.currentTime() - 86400;
	            		int timefile = Integer.valueOf(timestamp[0].substring(0, 10));
	            		if(timefile < timecurr){
	            			fileEntry.delete();
	            		}
	            	}
	            }catch(Exception e){	            	
	            }            	            
	        }
	    }
	}
	
	private boolean saveFile(HttpURLConnection c, File df) throws IOException {							
		FileOutputStream f = new FileOutputStream(df);	
		InputStream in = c.getInputStream();
		byte[] buffer = new byte[1024];
		int total = 0;
		int len1 = 0;
		while ((len1 = in.read(buffer)) > 0) {
			if(thread.isInterrupted()){
				f.close();
				df.delete();
				return false;
			}
			total += len1;
			progressBar.setProgress(total);
			f.write(buffer, 0, len1);
		}
		f.close();
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_PDF_VIEW){
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void finish() {
		if(thread.isAlive()){
			thread.interrupt();			
		}
		super.finish();
	}	
	
}
