package com.microstep.android.onclick;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftConditionalCommentTagTypes;
import net.htmlparser.jericho.Source;

import com.google.zxing.client.android.LocaleManager;
import com.microstep.android.onclick.dao.FactoryDAO;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.DeviceClass;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.security.SecurityContextHolder;
import com.microstep.android.onclick.util.Language;
import com.microstep.onclick.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ProductScanActivity extends Activity implements OnClickListener {	
	
	private String content;
	private String format;
	
	private Button bBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scandetail);
		
		Bundle bundle = getIntent().getExtras();
        this.content = (String) bundle.getSerializable("SCAN_CONTENT");
        this.format = (String) bundle.getSerializable("SCAN_FORMAT");
        
        //set up button
        Button button = null;
        button = (Button) findViewById(R.id.button_productdetail_add);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_productdetail_sel);
        button.setOnClickListener(this);
        button.setVisibility(View.GONE);
        button = (Button) findViewById(R.id.button_productdetail_cancel);
        button.setOnClickListener(this);
        this.bBack = (Button) this.findViewById(R.id.bn_back);
		this.bBack.setOnClickListener(this);
		
        openProductSearch(this.content);    
        
        DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();        
        List<Category> categories = FactoryDAO.getCategoryDAO(this)
        		.getCategories(Language.value(dc.getLanguage()));
        String[] items = new String[categories.size()+1];
        int i = 0;
        items[i++] = getResources().getString(R.string.main_no_category);
        for(Category c : categories){
        	items[i++] = c.getName();
        }

        Spinner sCategory = (Spinner) findViewById(R.id.productdetail_category);        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategory.setAdapter(adapter);
        
	}
	
	final void openProductSearch(String upc) {
	    try{
		
	    	String encodedProductID = URLEncoder.encode(upc, "UTF-8");	    	    	    	
//	    	String sourceUrlString = "http://m.google." + LocaleManager.getProductSearchCountryTLD(this) + "/m/products?&q=" + encodedProductID + "&hl=en";	
	    	String sourceUrlString = "http://www.google." + LocaleManager.getProductSearchCountryTLD(this) + "/search?hl=pl&tbm=shop&q=" + encodedProductID;

			MicrosoftConditionalCommentTagTypes.register();
			MasonTagTypes.register();
			Source source=new Source(new URL(sourceUrlString));				
			List<Element> elementList = source.getAllElements(HTMLElementName.IMG);
			boolean find = false;
			for (Element element : elementList) {
				String url = element.getAttributeValue("src");
				String name = element.getAttributeValue("alt");				
				if(url != null && url.contains("googleusercontent")){					
			    	setImageView(url);
			    	EditText editText = (EditText) findViewById(R.id.productdetail_name_auto);
			        editText.setText(name);
			        find = true;
					break;
				}
			}
			
			if(find == false){
				TextView tView = (TextView) findViewById(R.id.scan_bottom_text);
		    	tView.setText(R.string.zxing_activity_no_data);		    	
			}			
		    
	    }catch(IOException e){
	    	Log.e("Pobieranie danych o produkcie z Google", e.getMessage());
	    	TextView tView = (TextView) findViewById(R.id.scan_bottom_text);
	    	tView.setText(R.string.zxing_activity_no_data);
	    }catch(Exception e){
	    	Log.e("Pobieranie danych o produkcie z Google", e.getMessage());
	    	TextView tView = (TextView) findViewById(R.id.scan_bottom_text);
	    	tView.setText(R.string.zxing_activity_no_data);
	    }
	    
	}
	
	private static String unescapeHTML(String raw) {
		return Html.fromHtml(raw).toString();
	}
	
	private void setImageView(String imgURL){
		
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
        	URL url = new URL(imgURL);
        	connection = (HttpURLConnection) url.openConnection();
        	connection.connect();
            
        	if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
              throw new IOException("Bad HTTP response: " + connection.getResponseCode());
            }
        	
            in = connection.getInputStream();			            
            Bitmap bitmap = BitmapFactory.decodeStream(in);		
            
	    	ImageView imageView = (ImageView) findViewById(R.id.productimage);
	    	imageView.setImageBitmap(bitmap);
	    	
        } catch(IOException e){
  	    	Log.e("Pobieranie danych o produkcie z Google", e.getMessage());
  	    }finally {
            if(connection != null){
            	connection.disconnect();
            }
            if(in != null){
        		try {
					in.close();
				} catch (IOException e) {}            	
            }
        }
	
	}

	@Override
	public void onClick(View v) {
		
		Editable eName = ((EditText) findViewById(R.id.productdetail_name_auto)).getText();
		Editable eCount = ((EditText) findViewById(R.id.productdetail_count)).getText();
		Editable ePrice = ((EditText) findViewById(R.id.productdetail_value)).getText();
		int iUnit = ((Spinner) findViewById(R.id.productdetail_unit)).getSelectedItemPosition();
		String sCategory = (String) ((Spinner) findViewById(R.id.productdetail_category)).getSelectedItem();
		
		String name = eName.toString();
		String count = eCount.toString();
		String price = ePrice.toString();
		
		Product product = new Product();
    	product.setCount(1D);
    	Intent intent = null;
    	
		switch(v.getId()){
			case R.id.bn_back:
				finish();
				break;
			case R.id.button_productdetail_add: 
				if(name != null && !"".equals(name)){
	            	try{            		            	
		            	if(!"".equals(name)){ product.setName(name); }	            	
		        		if(!"".equals(count)){ product.setCount(Double.parseDouble(count)); }
		        		if(!"".equals(price)){ product.setPrice(Double.parseDouble(price)); } 
		        		product.setUnit(iUnit);	        	
		        		
		        		if(!sCategory.equals(getResources().getString(R.string.main_no_category))){
		        			DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
		        			Category category = FactoryDAO.getCategoryDAO(this)
		        					.getByName(sCategory, Language.value(dc.getLanguage()));
		            		product.setCategory(category);
		            	}else{
		            		product.setCategory(null);
		            	}
		        		
		        		intent = new Intent();	        	
			        	intent.putExtra("product", product);
			        	setResult(ProductListActivity.ACTION_ADD, intent);
			        	finish();		        	
	            	}catch(Exception e){
	            		Log.e("Blad", e.getMessage());
	            	}
				}
				break;		
			case R.id.button_productdetail_sel:				
				if(name != null && !"".equals(name)){
	        		if(!"".equals(count)){ product.setCount(Double.parseDouble(count)); }		        	
	        		if(!"".equals(price)){ product.setPrice(Double.parseDouble(price)); }
	    			if(!product.isSelected()){ product.setSelected(true); }
	    			product.setUnit(iUnit);
	
	    			if(!sCategory.equals(getResources().getString(R.string.main_no_category))){
	        			DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
	        			Category category = FactoryDAO.getCategoryDAO(this)
	        					.getByName(sCategory, Language.value(dc.getLanguage()));
	            		product.setCategory(category);
	            	}else{
	            		product.setCategory(null);
	            	}

	    			intent = new Intent();
		        	intent.putExtra("product", product);
		        	setResult(ProductListActivity.ACTION_SELECT, intent);
		        	finish();    			
				}
				break;
			case R.id.button_productdetail_cancel:
				finish();
				break;
		}

	}
	
}